package com.shoppursshop.activities;

import android.content.pm.PackageManager;
import android.os.Bundle;

import com.android.volley.Request;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.shoppursshop.R;
import com.shoppursshop.utilities.ConnectionDetector;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.DialogAndToast;
import com.shoppursshop.utilities.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.shoppursshop.utilities.Utility.MY_PERMISSIONS_REQUEST_SEND_SMS;

public class ForgotPasswordActivity extends NetworkBaseActivity {

    private EditText editMobile;
    private Button btnSubmit,btnCancel;
    private boolean isSubmitEnable;
    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        init();
    }

    private void init(){
        btnSubmit=(Button)findViewById(R.id.btn_submit);
        btnCancel=(Button)findViewById(R.id.btn_cancel);
        editMobile=(EditText)findViewById(R.id.edit_mobile);

        editMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String userInput=editable.toString();
                if(userInput.length() == 10){
                    btnSubmit.setBackgroundResource(R.drawable.submit);
                    isSubmitEnable=true;
                }else {
                    btnSubmit.setBackgroundResource(R.drawable.cancel);
                    isSubmitEnable=false;
                }
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isSubmitEnable){
                    if(ConnectionDetector.isNetworkAvailable(ForgotPasswordActivity.this)) {
                        Map<String,String> params=new HashMap<>();
                        params.put("mobile",editMobile.getText().toString());
                        String url=getResources().getString(R.string.url)+ Constants.GET_PASSWORD+"?mobile="+editMobile.getText().toString();
                        showProgress(true);
                        jsonObjectApiRequest(Request.Method.GET,url,new JSONObject(params),"getPassword");
                    }else {
                        DialogAndToast.showDialog(getResources().getString(R.string.no_internet),ForgotPasswordActivity.this);
                    }
                }else {
                    // DialogAndToast.showToast("Disable",ForgotPasswordActivity.this);
                }
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {
        try {
            if(apiName.equals("getPassword")){
                if(response.getBoolean("status")){
                    JSONObject dataObject=response.getJSONObject("result");
                    String mobile = dataObject.getString("mobile");
                    String deviceID = dataObject.getString("imeiNo");
                    message = "Hi your password is "+dataObject.getString("password");
                    if(deviceID != null){
                        if(mobile.equals(editMobile.getText().toString()) && deviceID.equals(sharedPreferences.getString(Constants.IMEI_NO,"")))
                            sendSMSMessage();
                        else
                            DialogAndToast.showDialog("You are not authorized. Please contact administration.",ForgotPasswordActivity.this);
                    }else
                        DialogAndToast.showDialog("You are not authorized. Please contact administration.",ForgotPasswordActivity.this);
                }else{
                    DialogAndToast.showDialog("You are not authorized. Please contact administration.",ForgotPasswordActivity.this);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    protected void sendSMSMessage() {
        // phoneNo = txtphoneNo.getText().toString();
        // message = txtMessage.getText().toString();

        if (Utility.verifySendSmsPermissions(this)) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(editMobile.getText().toString(), null, message, null, null);
            DialogAndToast.showToast("SMS sent",ForgotPasswordActivity.this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(editMobile.getText().toString(), null, message, null, null);
                    DialogAndToast.showToast("SMS sent", ForgotPasswordActivity.this);
                } else {
                    DialogAndToast.showToast("SMS failed. Please try again.",ForgotPasswordActivity.this);
                    return;
                }
            }
        }

    }

}
