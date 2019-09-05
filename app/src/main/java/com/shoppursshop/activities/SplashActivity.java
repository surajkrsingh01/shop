package com.shoppursshop.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.shoppursshop.R;
import com.shoppursshop.activities.payment.PaymentActivity;
import com.shoppursshop.activities.payment.ccavenue.activities.CCAvenueWebViewActivity;
import com.shoppursshop.activities.payment.ccavenue.utility.AvenuesParams;
import com.shoppursshop.activities.payment.mPos.MPayActivity;
import com.shoppursshop.activities.payment.mPos.MPayTransactionDetailsActivity;
import com.shoppursshop.database.DbHelper;
import com.shoppursshop.utilities.ConnectionDetector;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.DialogAndToast;
import com.shoppursshop.utilities.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends NetworkBaseActivity {

    // private SharedPreferences sharedPreferences;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // sharedPreferences=getSharedPreferences(Constants.MYPREFERENCEKEY,MODE_PRIVATE);

     //   startActivityForResult(new Intent(SplashActivity.this, DeliveryAddressActivity.class), 101);

        if (Utility.checkLocationPermission(this)) {
            init();
            //test();
        }

    }

    private void init(){
        String IMEI = sharedPreferences.getString(Constants.IMEI_NO,"");
        Log.i(TAG,"IMEI NO "+IMEI);

        editor.putString(Constants.GOOGLE_MAP_API_KEY,"AIzaSyB-GKvcnqqzEBxT6OvmVPfNs7FBppblo-s");
        editor.commit();

        if (sharedPreferences.getBoolean(Constants.IS_LOGGED_IN, false)) {
            intent = new Intent(SplashActivity.this, MainActivity.class);
            intent.putExtra("flag", "wallet");
            intent.putExtra("amount", 500);
            //  intent.putExtra(AvenuesParams.ORDER_ID, orderID);
            intent.putExtra(AvenuesParams.CURRENCY, "INR");
            //  startActivityForResult(intent,1);
            if(ConnectionDetector.isNetworkAvailable(this)){
                authenticateUser();
            }else{
                showMyDialog(getResources().getString(R.string.no_internet));
            }
        } else {

            String deviceType = sharedPreferences.getString(Constants.ANDROID_DEVICE_TYPE,"");

            if(TextUtils.isEmpty(deviceType)){
                intent = new Intent(SplashActivity.this, ChooseDeviceActivity.class);
                intent.putExtra("flag", "splash");
                moveToNextActivity();
            }else{
                intent = new Intent(SplashActivity.this, LoginActivity.class);
                if (TextUtils.isEmpty(sharedPreferences.getString(Constants.IMEI_NO, ""))) {
                    getMacID();
                } else {
                    moveToNextActivity();
                }
            }

        }

    }

    private void moveToNextActivity() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
                // overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        }, 2000);
    }


    /**
     * Get MAC ID from real device
     */
    private void getMacID() {
        // gets the current TelephonyManager
        TelephonyManager teleManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

        if (Utility.verifyReadPhoneStatePermissions(this)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            String IMEI = teleManager.getDeviceId();
            editor.putString(Constants.IMEI_NO,IMEI);
            editor.commit();
            Log.i(TAG,"Mac id "+IMEI);
            moveToNextActivity();
        }
        //      mMacId = "866700048591240";

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_PHONE_STATE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getMacID();
                }else{
                    finish();
                }
                break;
            case Utility.MY_PERMISSIONS_REQUEST_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    init();
                }else{
                    finish();
                }
                break;
        }
    }


    private void authenticateUser(){
        Map<String,String> params=new HashMap<>();
        params.put("userType",sharedPreferences.getString(Constants.USER_TYPE,""));
        params.put("mobile",sharedPreferences.getString(Constants.MOBILE_NO,""));
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+Constants.AUTHENTICATE;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"authenticate");
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {
        showProgress(false);
        try {
            // JSONObject jsonObject=response.getJSONObject("response");
            if (apiName.equals("authenticate")) {
                if (response.getBoolean("status")) {
                  JSONObject dataObject = response.getJSONObject("result");
                  int isActive = dataObject.getInt("isActive");
                  if(isActive == 1){
                      if (TextUtils.isEmpty(sharedPreferences.getString(Constants.IMEI_NO, ""))) {
                          getMacID();
                      } else {
                          moveToNextActivity();
                      }
                  }else{

                  }
                }else{
                    if(response.getInt("result") == 0){
                       showMyDialog(response.getString("message"));
                    }else{
                        DialogAndToast.showDialog(response.getString("message"),this);
                    }

                }
            }
        }catch (JSONException error){
            error.printStackTrace();
        }
    }

    @Override
    public void onDialogPositiveClicked(){
        logout();
    }

}
