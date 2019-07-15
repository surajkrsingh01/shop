package com.shoppursshop.activities;

import android.Manifest;
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
import com.shoppursshop.activities.payment.ccavenue.activities.CCAvenueWebViewActivity;
import com.shoppursshop.activities.payment.ccavenue.utility.AvenuesParams;
import com.shoppursshop.activities.payment.mPos.MPayActivity;
import com.shoppursshop.activities.payment.mPos.MPayTransactionDetailsActivity;
import com.shoppursshop.database.DbHelper;
import com.shoppursshop.utilities.ConnectionDetector;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.dspread.xnpos.EmvAppTag.status;

public class SplashActivity extends NetworkBaseActivity {

    // private SharedPreferences sharedPreferences;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // sharedPreferences=getSharedPreferences(Constants.MYPREFERENCEKEY,MODE_PRIVATE);

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
            intent.putExtra(AvenuesParams.AMOUNT, String.format("%.02f",51.00f));
            //  intent.putExtra(AvenuesParams.ORDER_ID, orderID);
            intent.putExtra(AvenuesParams.CURRENCY, "INR");
          //  startActivityForResult(intent,1);
            if(ConnectionDetector.isNetworkAvailable(this)){
                authenticateUser();
            }else{
                showMyDialog(getResources().getString(R.string.no_internet));
            }
        } else {
            intent = new Intent(SplashActivity.this, LoginActivity.class);
            if (TextUtils.isEmpty(sharedPreferences.getString(Constants.IMEI_NO, ""))) {
                getMacID();
            } else {
                moveToNextActivity();
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

    @Override
    public void onDialogPositiveClicked(){
          finish();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i(TAG,"requestCode code "+requestCode+" "+resultCode+" "+RESULT_OK);

        if (requestCode == 1)
            if(data != null){
                try {
                    JSONObject jsonObject = new JSONObject(data.getStringExtra("response"));
                    Log.i(TAG,"Transaction status "+jsonObject.getString("order_status"));
                    String statusCode = jsonObject.getString("response_code");
                    if(statusCode.equals("0")){
                        showMyDialog(data.getStringExtra("message"));
                    }else{
                        showMyDialog("Payment is unsuccessful. Please try again later.");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
    }

    private void authenticateUser(){
        Map<String,String> params=new HashMap<>();
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
                }
            }
        }catch (JSONException error){
            error.printStackTrace();
        }
    }


    private void test(){
        JSONObject dataObject = getTestObject();
        Intent intent = new Intent(SplashActivity.this, TransactionDetailsActivity.class);
        intent.putExtra("responseData",dataObject.toString());
       // intent.putExtra("shopArray",getIntent().getStringExtra("shopArray"));
        intent.putExtra("response", dataObject.toString());
        startActivity(intent);
    }

    private JSONObject getTestObject(){
       String data = "{\"status_message\":\"Transaction success\",\"response_code\":\"0\",\"delivery_address\":\"Delhi\",\"trans_date\":\"04/07/2019 12:37:18\",\"status_code\":\"null\",\"delivery_name\":\"Vipin Dhama\",\"billing_address\":\"Delhi\",\"failure_message\":\"\",\"bank_ref_no\":\"6124386838\",\"order_status\":\"Success\",\"billing_tel\":\"9718181699\",\"billing_state\":\"Delhi\",\"billing_email\":\"vipin.dhama8800@gmail.com\",\"billing_zip\":\"110091\",\"currency\":\"INR\",\"merchant_param1\":\"\",\"merchant_param2\":\"\",\"merchant_param3\":\"4\",\"merchant_param4\":\"\",\"retry\":\"N\",\"vault\":\"N\",\"offer_type\":\"null\",\"tracking_id\":\"108619244755\",\"merchant_param5\":\"\",\"delivery_country\":\"India\",\"amount\":\"1.00\",\"payment_mode\":\"Unified Payments\",\"bin_country\":\"\",\"billing_country\":\"India\",\"mer_amount\":\"1.00\",\"discount_value\":\"0.0\",\"delivery_state\":\"Delhi\",\"offer_code\":\"null\",\"billing_name\":\"Vipin Dhama\",\"billing_city\":\"Delhi\",\"billing_notes\":\"\",\"eci_value\":\"null\",\"card_name\":\"UPI\",\"delivery_tel\":\"9718181699\",\"order_id\":\"7518993\",\"delivery_city\":\"Delhi\",\"delivery_zip\":\"110091\"}";

        JSONObject dataObject = null;
        try {
            dataObject = new JSONObject(data);
            dataObject.put("orderNumber","SHP1/1");
            if(dataObject.getString("response_code").equals("0")){
                dataObject.put("status", "Done");
                dataObject.put("approved", true);
            }else{
                dataObject.put("status", "Failed");
                dataObject.put("approved", false);
            }
            dataObject.put("transactionType", "NA");
            dataObject.put("merchantId", "143051");
            dataObject.put("paymentMethod", dataObject.getString("payment_mode"));
            dataObject.put("paymentMode", "ePay");
            dataObject.put("transactionId", dataObject.getString("tracking_id"));
            dataObject.put("cardBrand", dataObject.getString("card_name"));
            dataObject.put("responseCode", dataObject.getString("response_code"));
            dataObject.put("responseMessage", dataObject.getString("order_status"));
            dataObject.put("statusMessage", dataObject.getString("status_message"));
            dataObject.put("currencyCode", dataObject.getString("currency"));
            dataObject.put("orderRefNo", dataObject.getString("bank_ref_no"));
            dataObject.put("date", Utility.getTimeStamp("yyyy-mm-dd HH:mm:ss"));
            //dataObject.put("status", dataObject.getString("order_status"));
            dataObject.put("custCode","SHPC1");
            //dataObject.put("cardHolderName",dataObject.getString("Card Hodler Name"));
            dataObject.put("userName",sharedPreferences.getString(com.shoppursshop.utilities.Constants.FULL_NAME,""));
            dataObject.put("dbName",sharedPreferences.getString(com.shoppursshop.utilities.Constants.DB_NAME,""));
            dataObject.put("dbUserName",sharedPreferences.getString(com.shoppursshop.utilities.Constants.DB_USER_NAME,""));
            dataObject.put("dbPassword",sharedPreferences.getString(com.shoppursshop.utilities.Constants.DB_PASSWORD,""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return dataObject;

    }
}
