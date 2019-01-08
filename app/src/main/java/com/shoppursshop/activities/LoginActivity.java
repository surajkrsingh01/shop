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
import com.shoppursshop.utilities.ConnectionDetector;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.DialogAndToast;
import com.shoppursshop.utilities.Utility;

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

        mobile = "9718181697";
        password = "12345";

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
                editor.putString(Constants.MOBILE_NO,mobile);
                editor.putBoolean(Constants.IS_LOGGED_IN,true);
                editor.commit();
                //DialogAndToast.showToast("Account created",LoginActivity.this);
                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();

            }else {
                DialogAndToast.showDialog(getResources().getString(R.string.no_internet),this);
            }

        }
    }

    public void volleyRequest(){
        Map<String,String> params=new HashMap<>();
        if(mobile.contains("@"))
            params.put("username",mobile.split("@")[0]);
        else
            params.put("username",mobile);
        params.put("password",password);
        String url=getResources().getString(R.string.url)+"/Users/login";
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"login");
    }


    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {
        showProgress(false);
        try {
            // JSONObject jsonObject=response.getJSONObject("response");
            if(apiName.equals("login")){
                if(response.getString("status").equals("true")||response.getString("status").equals(true)){
                    JSONObject dataObject=response.getJSONObject("data");
                    editor.putString(Constants.FULL_NAME,dataObject.getString("name"));
                    editor.putString(Constants.EMAIL,dataObject.getString("mobile"));
                    editor.putInt(Constants.MOBILE_NO,dataObject.getInt("mobile"));
                    editor.putString(Constants.LOCATION,dataObject.getString("city"));
                    editor.putInt(Constants.USER_TYPE_ID,dataObject.getInt("user_type_id"));
                    editor.putString(Constants.USERNAME,dataObject.getString("username"));
                    editor.putString(Constants.ROLE,dataObject.getString("role"));
                    editor.putString(Constants.ACTIVATE_KEY,dataObject.getString("activate_key"));
                    editor.putString(Constants.GUID,dataObject.getString("guid"));
                    editor.putString(Constants.FORGOT_PASSWORD_REQUEST_TIME,dataObject.getString("forgot_password_request_time"));
                    editor.putString(Constants.STATUS,dataObject.getString("status"));
                    editor.putString(Constants.TOKEN,dataObject.getString("token"));
                    editor.putString(Constants.CREATED,dataObject.getString("created"));
                    editor.putString(Constants.MODIFIED,dataObject.getString("modified"));
                    editor.putBoolean(Constants.IS_LOGGED_IN,true);
                    editor.commit();
                }else {
                    DialogAndToast.showDialog(response.getString("message"),LoginActivity.this);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            DialogAndToast.showToast(getResources().getString(R.string.json_parser_error)+e.toString(),LoginActivity.this);
        }
    }

}
