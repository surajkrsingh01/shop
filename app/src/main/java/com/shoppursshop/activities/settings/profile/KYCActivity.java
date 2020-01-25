package com.shoppursshop.activities.settings.profile;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.shoppursshop.R;
import com.shoppursshop.activities.base.BaseImageActivity;
import com.shoppursshop.activities.settings.SettingActivity;
import com.shoppursshop.adapters.UploadedDocAdapter;
import com.shoppursshop.interfaces.FirebaseImageUploadListener;
import com.shoppursshop.interfaces.MyItemTypeClickListener;
import com.shoppursshop.models.MyKyc;
import com.shoppursshop.services.FirebaseImageUploadService;
import com.shoppursshop.utilities.ConnectionDetector;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.DialogAndToast;
import com.shoppursshop.utilities.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KYCActivity extends BaseImageActivity implements MyItemTypeClickListener, FirebaseImageUploadListener {

    private EditText edit_doc_number;
    private ImageView btn_camera,image_doc;
    private RecyclerView recycler_view;
    private UploadedDocAdapter docAdapter;
    private Spinner spinner_doc_type;
    private List<String> itemList;
    private List<MyKyc> kycList;
    private ArrayAdapter<String> itemAdapter;

    private RequestOptions requestOptions;
    private String docNumber,imageDoc;
    private int position;
    private  MyKyc myKyc;

    private FirebaseImageUploadService firebaseImageUploadService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kyc);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    private void init(){
        btn_camera = findViewById(R.id.btn_camera_pan);
        image_doc= findViewById(R.id.image_doc);
        edit_doc_number= findViewById(R.id.edit_doc_number);
        recycler_view= findViewById(R.id.recycler_view);
        itemList = new ArrayList<>();
        kycList = new ArrayList<>();
        spinner_doc_type = findViewById(R.id.spinner_doc_type);
        Button btn_next = findViewById(R.id.btn_next);

        // recycler_view.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recycler_view.setLayoutManager(layoutManager);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        docAdapter=new UploadedDocAdapter(this,kycList);
        docAdapter.setMyItemClickListener(this);
        recycler_view.setAdapter(docAdapter);
        recycler_view.setNestedScrollingEnabled(false);

        itemList.add(0,"Select Document Type");
        itemList.add("Passport");
        itemList.add("Driving License");
        itemList.add("Pan Card");
        itemList.add("Adhar Card");
        itemList.add("Voter Id Card");
        itemList.add("Electricity Bill");
        itemList.add("Gas Connection");

        itemAdapter = new ArrayAdapter<String>(this, R.layout.simple_dropdown_list_item, itemList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0){
                    return false;
                }else{
                    return true;
                }
            }
            @Override
            public View getView(int position, View convertView,
                                ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(getResources().getColor(R.color.grey500));
                }else{
                    if(isDarkTheme){
                        tv.setTextColor(getResources().getColor(R.color.white));
                    }else{
                        tv.setTextColor(getResources().getColor(R.color.primary_text_color));
                    }
                }
                return view;
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                if(isDarkTheme){
                    view.setBackgroundColor(getResources().getColor(R.color.dark_color));
                }else{
                    view.setBackgroundColor(getResources().getColor(R.color.white));
                }
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(getResources().getColor(R.color.grey500));
                }else{
                    if(isDarkTheme){
                        tv.setTextColor(getResources().getColor(R.color.white));
                    }else{
                        tv.setTextColor(getResources().getColor(R.color.primary_text_color));
                    }
                }
                tv.setPadding(20,20,20,20);
                return view;
            }
        };

        spinner_doc_type.setAdapter(itemAdapter);

        requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        // requestOptions.override(Utility.dpToPx(150, context), Utility.dpToPx(150, context));
        requestOptions.fitCenter();
        requestOptions.skipMemoryCache(false);

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        Button btn_add = findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptAddKYCDetails();
            }
        });

        TextView tv_top_parent = findViewById(R.id.text_right_label);
        tv_top_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(KYCActivity.this, SettingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        TextView text_label_1 = findViewById(R.id.text_label_1);
        text_label_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        firebaseImageUploadService = FirebaseImageUploadService.getInstance();

        if(ConnectionDetector.isNetworkAvailable(this))
        getKycDetails();

    }

    public void attemptAddKYCDetails(){
        docNumber = edit_doc_number.getText().toString();
        String docType = (String)spinner_doc_type.getSelectedItem();
        boolean cancel = false;
        View focus = null;

        if(TextUtils.isEmpty(docNumber)){
            cancel = true;
            focus = edit_doc_number;
            edit_doc_number.setError("Please enter document number");
        }

        if(cancel){
            focus.requestFocus();
            return;
        }else{
            if(docType.equals("Select Document Type")){
                DialogAndToast.showDialog("Please select document type",this);
                return;
            }

            if(TextUtils.isEmpty(imageDoc)){
                DialogAndToast.showDialog("Please select document image",this);
                return;
            }

            if(ConnectionDetector.isNetworkAvailable(this)) {
                // progressDialog.setMessage(getResources().getString(R.string.creating_account));
                Map<String,String> params=new HashMap<>();

                params.put("userId",sharedPreferences.getString(Constants.USER_ID,""));
                params.put("kycDocType",docType);
                params.put("kycDocNumber",docNumber);
                params.put("kycDocPic",imageDoc);
                params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
                params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
                params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
                String url=getResources().getString(R.string.url)+Constants.ADD_KYC_DETAILS;
                showProgress(true);
                jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"addKycDetails");
            }else {
                DialogAndToast.showDialog(getResources().getString(R.string.no_internet),this);
            }
        }
    }

    public void attemptUpdateKYCDetails(String firebaseUrl){
        MyKyc myKyc = kycList.get(position);
        myKyc.setKycDocPic(firebaseUrl);
        Map<String,String> params=new HashMap<>();
        params.put("id",String.valueOf(myKyc.getId()));
        params.put("kycDocType",myKyc.getKycDocType());
        params.put("kycDocNumber",myKyc.getKycDocNumber());
        params.put("kycDocPic",myKyc.getKycDocPic());
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+Constants.UPDATE_KYC_DETAILS;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"updateKycDetails");
    }

    private void getKycDetails(){
        Map<String,String> params=new HashMap<>();
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+Constants.GET_KYC_DETAILS+"?userId="+
                sharedPreferences.getString(Constants.USER_ID,"");
        // showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"getKycList");
    }


    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {

        try {
            if(apiName.equals("addKycDetails")){
                if(response.getBoolean("status")){

                    Gson gson = new Gson();
                    MyKyc myKyc = gson.fromJson(response.getString("result"),MyKyc.class);
                    myKyc.setSignature(Utility.getTimeStamp());
                    kycList.add(myKyc);
                    position = kycList.size()-1;
                    docAdapter.notifyItemInserted(position);
                    uploadImagesToFirebase();
                }else{
                    DialogAndToast.showDialog(response.getString("message"),this);
                }
            }else if(apiName.equals("updateKycDetails")) {
                if (response.getBoolean("status")) {
                    MyKyc myKyc = kycList.get(position);
                    editor.putString(Constants.KYC_STATUS,"Uploaded");
                    editor.commit();
                    imageDoc = null;
                    edit_doc_number.setText("");
                    Glide.with(this).clear(image_doc);
                    spinner_doc_type.setSelection(0);
                }
            }else if(apiName.equals("getKycList")) {
                if (response.getBoolean("status")) {
                    JSONArray jsonArray = response.getJSONArray("result");
                    JSONObject jsonObject = null;
                    Gson gson = new Gson();
                    for(int i = 0; i <jsonArray.length() ; i++){
                        jsonObject = jsonArray.getJSONObject(i);
                        myKyc = gson.fromJson(jsonObject.toString(),MyKyc.class);
                        myKyc.setSignature(Utility.getTimeStamp());
                        kycList.add(myKyc);
                    }
                    docAdapter.notifyDataSetChanged();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void imageAdded(){
        imageDoc = imagePath;
        Glide.with(this)
                .load(imagePath)
                .apply(requestOptions)
                .into(image_doc);
        alertDialog.dismiss();
    }

    private void uploadImagesToFirebase(){
        Log.i(TAG,"uploading images to firebase..");
        showProgress(true);
        String name = Utility.getTimeStamp("yyyyMMddHHmmss");
        firebaseImageUploadService.setFirebaseImageUploadListener(this);
        firebaseImageUploadService.uploadImage(
                "shop/"+sharedPreferences.getString(Constants.SHOP_CODE,"")+"/kyc/"+name,
                imagePath);

    }

    @Override
    public void onImageUploaded(String position, String url) {
        showProgress(false);
        imagePath = null;
        attemptUpdateKYCDetails(url);
    }

    @Override
    public void onImageFailed(String position) {
        showProgress(false);
    }

    @Override
    public void onItemClicked(int position, int type) {

    }
}
