package com.shoppursshop.activities;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.shoppursshop.R;
import com.shoppursshop.utilities.ConnectionDetector;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.DialogAndToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ResetPasswordActivity extends NetworkBaseActivity {

    private EditText editPassword,editConfPassword;
    private Button btnSubmit,btnCancel;
    private String mobile,otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();
    }

    private void init(){
        editPassword = findViewById(R.id.edit_password);
        editConfPassword = findViewById(R.id.edit_conf_password);
        btnSubmit=(Button)findViewById(R.id.btn_submit);
        btnCancel=(Button)findViewById(R.id.btn_cancel);

        mobile = getIntent().getStringExtra("mobile");

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ConnectionDetector.isNetworkAvailable(ResetPasswordActivity.this))
                resetPassword();
                else
                    DialogAndToast.showDialog(getResources().getString(R.string.no_internet),ResetPasswordActivity.this);
            }
        });
    }

    private void resetPassword(){
         String password = editPassword.getText().toString();
         String confPassword = editConfPassword.getText().toString();

        if(TextUtils.isEmpty(password)){
            DialogAndToast.showDialog("Please enter password",this);
            return;
        }

        if(!password.equals(confPassword)){
            DialogAndToast.showDialog("Passwords are not matching.",this);
            return;
        }

        Map<String,String> params=new HashMap<>();
        params.put("mobile",mobile);
        params.put("newPssword",password);
        String url=getResources().getString(R.string.url)+ Constants.RESET_PASSWORD;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"resetPassword");
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {
        try {
            if(apiName.equals("resetPassword")){
                if(response.getBoolean("status")){
                    showMyDialog(response.getString("message"));
                }else{
                    DialogAndToast.showDialog(response.getString("message"),ResetPasswordActivity.this);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void onDialogPositiveClicked(){
        Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
