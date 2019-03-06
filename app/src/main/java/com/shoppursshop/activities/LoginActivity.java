package com.shoppursshop.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;

import com.shoppursshop.R;
import com.shoppursshop.models.MyProductItem;
import com.shoppursshop.models.MySimpleItem;
import com.shoppursshop.utilities.ConnectionDetector;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.DialogAndToast;
import com.shoppursshop.utilities.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends NetworkBaseActivity{

    private EditText editTextMobile,editTextPassword;
    private TextView textForgotPassword;
    private Button btnLogin,btnSignUp;
    private String mobile,password;
    private Typeface iconFont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        iconFont = Utility.getSimpleLineIconsFont(this);

        RelativeLayout relativeForgotPassword=findViewById(R.id.relative_forgot_password);
        RelativeLayout relativeRegister=findViewById(R.id.relative_sign_up);
        editTextMobile=(EditText)findViewById(R.id.edit_mobile);
        editTextPassword=(EditText)findViewById(R.id.edit_password);
        textForgotPassword=(TextView)findViewById(R.id.text_forgot_password);
        TextView textMobile=(TextView)findViewById(R.id.text_mobile_icon);
        TextView textPassword=(TextView)findViewById(R.id.text_password_icon);
        TextView textSignUp=(TextView)findViewById(R.id.text_sign_up_icon);
        TextView textForgotPassword=(TextView)findViewById(R.id.text_forgot_password_icon);
        textMobile.setTypeface(iconFont);
        textPassword.setTypeface(iconFont);
        textSignUp.setTypeface(iconFont);
        textForgotPassword.setTypeface(iconFont);

        progressDialog.setMessage(getResources().getString(R.string.logging_user));

        relativeForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        relativeRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                intent.putExtra("type",0);
                startActivity(intent);
            }
        });

        //dbHelper.dropAndCreateAllTable();
        //createDatabase();



        btnLogin=(Button)findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

       /* btnSignUp=(Button)findViewById(R.id.btn_register);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });*/

        for(Drawable drawable : editTextMobile.getCompoundDrawables()){
            if(drawable != null){
                drawable.setColorFilter(new PorterDuffColorFilter(getResources().getColor(R.color.accent_color_1), PorterDuff.Mode.SRC_IN));
            }
        }

        for(Drawable drawable : editTextPassword.getCompoundDrawables()){
            if(drawable != null){
                drawable.setColorFilter(new PorterDuffColorFilter(getResources().getColor(R.color.accent_color_1), PorterDuff.Mode.SRC_IN));
            }
        }

    }

    public void attemptLogin(){
        mobile=editTextMobile.getText().toString();
        password=editTextPassword.getText().toString();
        View focus=null;
        boolean cancel=false;

        //mobile = "9718181697";
        //password = "12345";

        if(TextUtils.isEmpty(password)){
            editTextPassword.setError(getResources().getString(R.string.password_required));
            focus=editTextPassword;
            cancel=true;
        }

        if(TextUtils.isEmpty(mobile)){
            editTextMobile.setError(getResources().getString(R.string.mobile_required));
            focus=editTextMobile;
            cancel=true;
        }else if(mobile.length() != 10){
            editTextMobile.setError(getResources().getString(R.string.mobile_valid));
            focus=editTextMobile;
            cancel=true;
        }

        if(cancel){
            focus.requestFocus();
            return;
        }else {
            if(ConnectionDetector.isNetworkAvailable(this)) {
                progressDialog.setMessage(getResources().getString(R.string.logging_user));
                volleyRequest();
               /* editor.putString(Constants.MOBILE_NO,mobile);
                editor.putBoolean(Constants.IS_LOGGED_IN,true);
                editor.commit();
                //DialogAndToast.showToast("Account created",LoginActivity.this);
                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();*/

            }else {
                DialogAndToast.showDialog(getResources().getString(R.string.no_internet),this);
            }

        }
    }

    public void volleyRequest(){
        Map<String,String> params=new HashMap<>();
        params.put("mobile",mobile);
        params.put("password",password);
        String url=getResources().getString(R.string.url)+"/api/loginRetailer";
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"login");
    }

    private void syncData(){
        Map<String,String> params=new HashMap<>();
        params.put("id",sharedPreferences.getString(Constants.USER_ID,""));
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+"/api/syncdata";
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"syncdata");
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {
        showProgress(false);
        try {
            // JSONObject jsonObject=response.getJSONObject("response");
            if(apiName.equals("login")){
                if(response.getBoolean("status")){
                    JSONObject dataObject=response.getJSONObject("result");
                    editor.putString(Constants.USER_ID,dataObject.getString("id"));
                    editor.putString(Constants.FULL_NAME,dataObject.getString("username"));
                    editor.putString(Constants.EMAIL,dataObject.getString("userEmail"));
                    editor.putString(Constants.MOBILE_NO,dataObject.getString("mobile"));
                    editor.putString(Constants.SHOP_NAME,dataObject.getString("shopName"));
                    editor.putString(Constants.SHOP_CODE,dataObject.getString("shopCode"));
                    editor.putString(Constants.LANGUAGE,dataObject.getString("language"));
                    //myUser.setMpassword(dataObject.getString("RET_CODE"));
                    editor.putString(Constants.CITY,dataObject.getString("city"));
                    editor.putString(Constants.STATE,dataObject.getString("province"));
                    editor.putString(Constants.COUNTRY,dataObject.getString("country"));
                    editor.putString(Constants.ZIP,dataObject.getString("zip"));
                    editor.putString(Constants.ADDRESS,dataObject.getString("address"));
                    editor.putString(Constants.PHOTO,dataObject.getString("photo"));
                    //myUser.setIdProof(dataObject.getString("RET_CODE"));
                    editor.putString(Constants.PAN_NO,dataObject.getString("panNo"));
                    editor.putString(Constants.AADHAR_NO,dataObject.getString("aadharNo"));
                    editor.putString(Constants.GST_NO,dataObject.getString("gstNo"));
                    editor.putString(Constants.USER_LAT,dataObject.getString("userLat"));
                    editor.putString(Constants.USER_LONG,dataObject.getString("userLong"));
                    editor.putString(Constants.DB_NAME,dataObject.getString("dbName"));
                    editor.putString(Constants.DB_USER_NAME,dataObject.getString("dbUserName"));
                    editor.putString(Constants.DB_PASSWORD,dataObject.getString("dbPassword"));
                    editor.putString(Constants.USER_TYPE,"Seller");
                   // editor.putBoolean(Constants.IS_LOGGED_IN,true);
                    editor.commit();
                    syncData();
                    //editor.putBoolean(Constants.IS_LOGGED_IN,true);
                   // editor.commit();
                   // moveToActivities();
                    //moveToActivities();
                }else {
                    DialogAndToast.showDialog(response.getString("message"),LoginActivity.this);
                }
            }else if(apiName.equals("syncdata")){

                if(response.getBoolean("status")){
                    JSONObject dataObject = response.getJSONObject("result");
                    JSONArray catArray = dataObject.getJSONArray("categories");
                    JSONArray subCatArray = dataObject.getJSONArray("sub_categories");
                    JSONArray productArray = dataObject.getJSONArray("products");
                    JSONObject jsonObject =null;
                    MySimpleItem item = null;
                    MyProductItem productItem = null;
                    int len = catArray.length();
                    for(int i=0; i<len; i++){
                        jsonObject = catArray.getJSONObject(i);
                        item = new MySimpleItem();
                        item.setId(jsonObject.getInt("catId"));
                        item.setName(jsonObject.getString("catName"));
                        dbHelper.addCategory(item, Utility.getTimeStamp(),Utility.getTimeStamp());
                    }

                    len = subCatArray.length();
                    for(int i=0; i<len; i++){
                        jsonObject = subCatArray.getJSONObject(i);
                        item = new MySimpleItem();
                        item.setId(jsonObject.getInt("subCatId"));
                        item.setName(jsonObject.getString("subCatName"));
                        dbHelper.addSubCategory(item,""+jsonObject.getInt("catId"),Utility.getTimeStamp(),Utility.getTimeStamp());
                    }

                    len = productArray.length();
                    for(int i=0; i<len; i++){
                        jsonObject = productArray.getJSONObject(i);
                        productItem = new MyProductItem();
                        productItem.setProdId(jsonObject.getInt("prodId"));
                        productItem.setProdCatId(jsonObject.getInt("prodCatId"));
                        productItem.setProdName(jsonObject.getString("prodName"));
                        productItem.setProdBarCode(jsonObject.getString("prodBarCode"));
                        productItem.setProdDesc(jsonObject.getString("prodDesc"));
                        productItem.setProdReorderLevel(jsonObject.getInt("prodReorderLevel"));
                        productItem.setProdQoh(jsonObject.getInt("prodQoh"));
                        productItem.setProdHsnCode(jsonObject.getString("prodHsnCode"));
                        productItem.setProdCgst(Float.parseFloat(jsonObject.getString("prodCgst")));
                        productItem.setProdIgst(Float.parseFloat(jsonObject.getString("prodIgst")));
                        productItem.setProdSgst(Float.parseFloat(jsonObject.getString("prodSgst")));
                        productItem.setProdWarranty(Float.parseFloat(jsonObject.getString("prodWarranty")));
                        productItem.setProdMfgDate(jsonObject.getString("prodMfgDate"));
                        productItem.setProdExpiryDate(jsonObject.getString("prodExpiryDate"));
                        productItem.setProdMfgBy(jsonObject.getString("prodMfgBy"));
                        productItem.setProdImage1(jsonObject.getString("prodImage1"));
                        productItem.setProdImage2(jsonObject.getString("prodImage2"));
                        productItem.setProdImage3(jsonObject.getString("prodImage3"));
                        productItem.setProdMrp(Float.parseFloat(jsonObject.getString("prodMrp")));
                        productItem.setProdSp(Float.parseFloat(jsonObject.getString("prodSp")));
                        productItem.setCreatedBy(jsonObject.getString("createdBy"));
                        productItem.setUpdatedBy(jsonObject.getString("updatedBy"));
                        productItem.setCreatedDate(jsonObject.getString("createdDate"));
                        productItem.setUpdatedDate(jsonObject.getString("updatedDate"));
                        dbHelper.addProduct(productItem,Utility.getTimeStamp(),Utility.getTimeStamp());
                    }

                    editor.putBoolean(Constants.IS_LOGGED_IN,true);
                    editor.commit();
                    moveToActivities();

                }else{
                    DialogAndToast.showDialog(response.getString("message"),this);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            DialogAndToast.showToast(getResources().getString(R.string.json_parser_error)+e.toString(),LoginActivity.this);
        }
    }


    private void moveToActivities(){
        Intent intent=null;
        if(!sharedPreferences.getBoolean(Constants.IS_SUB_CAT_ADDED,false)){
            intent=new Intent(LoginActivity.this,RegisterActivity.class);
            intent.putExtra("type",RegisterActivity.SUB_CATEGORY);
        }else{
            intent=new Intent(LoginActivity.this,MainActivity.class);
        }

        startActivity(intent);
        finish();
    }

}
