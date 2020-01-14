package com.shoppursshop.activities.settings.profile;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.shoppursshop.R;
import com.shoppursshop.activities.base.BaseImageActivity;
import com.shoppursshop.activities.settings.ProfileActivity;
import com.shoppursshop.activities.settings.SettingActivity;
import com.shoppursshop.interfaces.FirebaseImageUploadListener;
import com.shoppursshop.services.FirebaseImageUploadService;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.DialogAndToast;
import com.shoppursshop.utilities.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class BasicProfileActivity extends BaseImageActivity implements FirebaseImageUploadListener {

    private EditText etUsername,etShopName,etGstNo,etEmail,etMobile;
    private CircleImageView profileImage;
    private RelativeLayout rlImageLayout;
    private String imageUrl;
    private TextView tv_top_parent, tv_parent;
    private ImageView btn_camera;
    private FirebaseImageUploadService firebaseImageUploadService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initFooterAction(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        setToolbarTheme();
    }

    private void init(){
        imageBase64 = "no";
        etUsername = findViewById(R.id.edit_user_name);
        etShopName = findViewById(R.id.edit_shop_name);
        etGstNo = findViewById(R.id.edit_gst_no);
        etEmail = findViewById(R.id.edit_email);
        etMobile = findViewById(R.id.edit_mobile);
        profileImage = findViewById(R.id.profile_image);
        rlImageLayout = findViewById(R.id.relative_image);
        RelativeLayout btnUpdate = findViewById(R.id.relative_footer_action);
        btn_camera = findViewById(R.id.btn_camera);
        ((GradientDrawable)btn_camera.getBackground()).setColor(colorTheme);

        etUsername.setText(sharedPreferences.getString(Constants.FULL_NAME,""));
        etShopName.setText(sharedPreferences.getString(Constants.SHOP_NAME,""));
        etGstNo.setText(sharedPreferences.getString(Constants.GST_NO,""));
        etMobile.setText(sharedPreferences.getString(Constants.MOBILE_NO,""));
        etEmail.setText(sharedPreferences.getString(Constants.EMAIL,""));

        imageUrl = sharedPreferences.getString(Constants.PROFILE_PIC,"");

        requestOptions.signature(new ObjectKey(sharedPreferences.getString("profile_image_signature","")));
        String profilePicLocal = sharedPreferences.getString(Constants.PROFILE_PIC_LOCAL,"");
        File fileLocal = new File(profilePicLocal);
        if(fileLocal.exists()){
            Glide.with(this)
                    .load(profilePicLocal)
                    .apply(requestOptions)
                    .error(R.drawable.ic_photo_black_192dp)
                    .into(profileImage);
        }else{
            Log.i(TAG,"Downloading image..."+sharedPreferences.getString(Constants.PROFILE_PIC,""));
            if(!sharedPreferences.getString(Constants.PROFILE_PIC,"").equals(""))
            downloadImage(1,sharedPreferences.getString(Constants.PROFILE_PIC,""),this);
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });

        rlImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        tv_top_parent = findViewById(R.id.text_left_label);
        tv_parent = findViewById(R.id.text_right_label);

        tv_top_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BasicProfileActivity.this, SettingActivity.class));
                finish();
            }
        });
        tv_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BasicProfileActivity.this, ProfileActivity.class));
                finish();
            }
        });

        firebaseImageUploadService = FirebaseImageUploadService.getInstance();

    }

    private void updateProfile(){
        String username = etUsername.getText().toString();
       String shopName = etShopName.getText().toString();
        String gstNo = etGstNo.getText().toString();
       String mobile = etMobile.getText().toString();
       String email = etEmail.getText().toString();

        if(TextUtils.isEmpty(username)){
            DialogAndToast.showDialog("Please enter name",this);
            return;
        }

       if(TextUtils.isEmpty(shopName)){
           DialogAndToast.showDialog("Please enter shop name",this);
           return;
       }

        if(TextUtils.isEmpty(gstNo)){
            DialogAndToast.showDialog("Please enter GST No",this);
            return;
        }

        if(TextUtils.isEmpty(mobile)){
            DialogAndToast.showDialog("Please enter mobile number",this);
            return;
        }else  if(mobile.length() != 10){
            DialogAndToast.showDialog("Please enter valid mobile number",this);
            return;
        }

        if(TextUtils.isEmpty(email)){
            DialogAndToast.showDialog("Please enter email",this);
            return;
        }

        Map<String,String> params=new HashMap<>();
        params.put("retName",username);
        params.put("shopName",shopName);
        params.put("gstNo",gstNo);
        params.put("email",email);
        params.put("mobile",sharedPreferences.getString(Constants.MOBILE_NO,""));
        params.put("profileImage",imageUrl);
        params.put("id",sharedPreferences.getString(Constants.USER_ID,""));
        params.put("shopCode",sharedPreferences.getString(Constants.SHOP_CODE,""));
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        JSONArray dataArray = new JSONArray();
        JSONObject dataObject = new JSONObject(params);
        dataArray.put(dataObject);
        String url=getResources().getString(R.string.url)+Constants.UPDATE_BASIC_PROFILE;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"updateBasicProfile");

    }

    private void updateProfileImage(){
        HashMap<String,String> params = new HashMap<>();
        params.put("profileImage",imageUrl);
        params.put("shopCode",sharedPreferences.getString(Constants.SHOP_CODE,""));
        String url=getResources().getString(R.string.url)+Constants.UPDATE_PROFILE_IMAGE;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"updateProfileImage");

    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {
        if (apiName.equals("updateBasicProfile")) {
           try{
               if(response.getBoolean("status")){
                   editor.putString(Constants.FULL_NAME,etUsername.getText().toString());
                   editor.putString(Constants.SHOP_NAME,etShopName.getText().toString());
                   editor.putString(Constants.MOBILE_NO,etMobile.getText().toString());
                   editor.putString(Constants.EMAIL,etEmail.getText().toString());
                   editor.putString(Constants.GST_NO,etGstNo.getText().toString());
                   if(imagePath != null ){
                       uploadImagesToFirebase();
                   }else{
                       editor.commit();
                       DialogAndToast.showToast(response.getString("message"),this);
                   }

               }else{
                   DialogAndToast.showDialog(response.getString("message"),this);
               }
           }catch (JSONException e){
               e.printStackTrace();
           }
        }else if (apiName.equals("updateProfileImage")) {
            try{
                if(response.getBoolean("status")){
                    String timestamp = Utility.getTimeStamp();
                    requestOptions.signature(new ObjectKey(timestamp));
                    editor.putString(Constants.PROFILE_PIC,imageUrl);
                    editor.putString(Constants.PROFILE_PIC_LOCAL,imagePath);
                    editor.putString("profile_image_signature",timestamp);
                    editor.commit();
                    DialogAndToast.showDialog("Profile has been updated successfully.",this);
                }else{
                    DialogAndToast.showDialog(response.getString("message"),this);
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void imageAdded(){
        Glide.with(this)
                .load(imagePath)
                .apply(requestOptions)
                .into(profileImage);
      // imageBase64 = convertToBase64(new File(imagePath));

    }

    private void uploadImagesToFirebase(){
        Log.i(TAG,"uploading images to firebase..");
        if(imagePath != null){
            showProgress(true);
            firebaseImageUploadService.setFirebaseImageUploadListener(this);
            firebaseImageUploadService.uploadImage(
                    "shops/"+sharedPreferences.getString(Constants.SHOP_CODE,"")+"/photo.jpg",
                    imagePath);
        }else{

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            super.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onImageUploaded(String position, String url) {
        imageUrl = url;
        updateProfileImage();
    }

    @Override
    public void onImageFailed(String position) {

    }

    @Override
    protected void imageDownloaded() {
        super.imageDownloaded();
        Log.i(TAG,"Downloaded image...");
       // showProgress(false);
        editor.putString(Constants.PROFILE_PIC_LOCAL,imagePath);
        editor.putString("profile_image_signature",Utility.getTimeStamp());
        editor.commit();
        Glide.with(this)
                .load(imagePath)
                .apply(requestOptions)
                .error(R.drawable.ic_photo_black_192dp)
                .into(profileImage);
        imagePath = null;
    }

    @Override
    protected void imageDownloadedFailed() {
        super.imageDownloadedFailed();
        Glide.with(this)
                .load(R.drawable.ic_photo_black_192dp)
                .apply(requestOptions)
                .into(profileImage);
    }
}
