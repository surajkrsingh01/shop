package com.shoppursshop.activities.auth;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;

import androidx.annotation.NonNull;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.shoppursshop.R;
import com.shoppursshop.activities.base.NetworkBaseActivity;
import com.shoppursshop.services.NotificationService;
import com.shoppursshop.utilities.ConnectionDetector;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.DialogAndToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ForgotPasswordActivity extends NetworkBaseActivity {

    private EditText editMobile;
    private Button btnSubmit,btnCancel;
    private String message;

    //These are the objects needed
    //It is the verification id that will be sent to the user
    private String mVerificationId;

    //The edittext to input the code
    private EditText editTextCode;
    private String OTP ="";
    private boolean generateOTP,otpAutoDetected;

    //firebase auth object
    private FirebaseAuth mAuth;

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
        editTextCode=(EditText)findViewById(R.id.edit_otp);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ConnectionDetector.isNetworkAvailable(ForgotPasswordActivity.this)) {
                    if(!otpAutoDetected && btnSubmit.getText().toString().equals("Submit")){
                        String code = editTextCode.getText().toString();
                        if(TextUtils.isEmpty(code)){
                            DialogAndToast.showDialog("Please enter OTP",ForgotPasswordActivity.this);
                            return;
                        }

                        verifyVerificationCode(code);

                    }else{
                        validateOTP();
                    }
                }else {
                    DialogAndToast.showDialog(getResources().getString(R.string.no_internet),ForgotPasswordActivity.this);
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

    private void saveOTP(){
        Map<String,String> params=new HashMap<>();
        params.put("mobile",editMobile.getText().toString());
        params.put("otp",OTP);
        String url=getResources().getString(R.string.url)+ Constants.SAVE_OTP;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"saveOTP");
    }

    private void validateOTP(){
        String enteredOtp = editTextCode.getText().toString();
        if(TextUtils.isEmpty(enteredOtp)){
            enteredOtp = "-1";
        }
        Map<String,String> params=new HashMap<>();
        params.put("mobile",editMobile.getText().toString());
        params.put("otp",enteredOtp);
        String url=getResources().getString(R.string.url)+ Constants.VALIDATE_OTP;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"validateOTP");
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
                            NotificationService.displayNotification(this,message);
                        else
                            DialogAndToast.showDialog("You are not authorized. Please contact administration.",ForgotPasswordActivity.this);
                    }else
                        DialogAndToast.showDialog("You are not authorized. Please contact administration.",ForgotPasswordActivity.this);
                }else{
                    DialogAndToast.showDialog("You are not authorized. Please contact administration.",ForgotPasswordActivity.this);
                }
            }else if(apiName.equals("validateOTP")){
                if(response.getBoolean("status")){
                    if(response.getInt("result") == 1){
                        Intent intent = new Intent(ForgotPasswordActivity.this, ResetPasswordActivity.class);
                        intent.putExtra("mobile",editMobile.getText().toString());
                        startActivity(intent);
                    }else{
                        editTextCode.setVisibility(View.VISIBLE);
                        generateOTP = false;
                        btnSubmit.setText("Submit");
                        DialogAndToast.showDialog("OTP has been sent to your mobile number. Please check your notification.",this);
                        NotificationService.displayNotification(this,response.getInt("result")+" is your verification code");
                    }

                }else{
                    if(response.getInt("result") == 0){
                      //  showMyDialog(response.getString("message"));
                        editTextCode.setVisibility(View.VISIBLE);
                        generateOTP = false;
                        btnSubmit.setText("Submit");
                        initFirebaseOtp(editMobile.getText().toString());
                    }else{
                        DialogAndToast.showDialog(response.getString("message"),ForgotPasswordActivity.this);
                    }
                }
            }else if(apiName.equals("saveOTP")){
                if(response.getBoolean("status")){
                    Intent intent = new Intent(ForgotPasswordActivity.this,ResetPasswordActivity.class);
                    intent.putExtra("mobile",editMobile.getText().toString());
                    startActivity(intent);
                }else{
                    DialogAndToast.showDialog(response.getString("message"),ForgotPasswordActivity.this);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDialogPositiveClicked(){
        initFirebaseOtp(editMobile.getText().toString());
    }

    private void initFirebaseOtp(String mobile){
        mAuth = FirebaseAuth.getInstance();
        sendVerificationCode(mobile);
    }

    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }

    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
             otpAutoDetected = true;
            //Getting the code sent by SMS
            OTP = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (OTP != null) {
                editTextCode.setText(OTP);
                //verifying the code
                verifyVerificationCode(OTP);
            }else{
                Intent intent = new Intent(ForgotPasswordActivity.this,ResetPasswordActivity.class);
                intent.putExtra("mobile",editMobile.getText().toString());
                startActivity(intent);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

            Toast.makeText(ForgotPasswordActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };


    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(ForgotPasswordActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            /*//verification successful we will start the profile activity
                            Intent intent = new Intent(VerifyPhoneActivity.this, ProfileActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);*/

                           // DialogAndToast.showDialog("verification successful", ForgotPasswordActivity.this);

                            saveOTP();

                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }

                            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
                            snackbar.setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                            snackbar.show();
                        }
                    }
                });
    }

}
