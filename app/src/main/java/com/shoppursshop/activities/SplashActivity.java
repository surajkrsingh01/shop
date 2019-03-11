package com.shoppursshop.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.Utility;

public class SplashActivity extends BaseActivity {

    private SharedPreferences sharedPreferences;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // sharedPreferences=getSharedPreferences(Constants.MYPREFERENCEKEY,MODE_PRIVATE);

        if(sharedPreferences.getBoolean(Constants.IS_LOGGED_IN,false)){
           /*if(!sharedPreferences.getBoolean(Constants.IS_SUB_CAT_ADDED,false)){
                intent=new Intent(SplashActivity.this,RegisterActivity.class);
                intent.putExtra("type",RegisterActivity.SUB_CATEGORY);
            }else{
                intent=new Intent(SplashActivity.this,MainActivity.class);
            }*/

            intent=new Intent(SplashActivity.this,MainActivity.class);
        }else {
            intent=new Intent(SplashActivity.this,LoginActivity.class);
        }

        if(TextUtils.isEmpty(sharedPreferences.getString(Constants.IMEI_NO,""))){
            getMacID();
        }else{
            moveToNextActivity();
        }



    }

    private void moveToNextActivity(){
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
        if (TextUtils.isEmpty(sharedPreferences.getString(Constants.IMEI_NO, ""))) {
            TelephonyManager teleManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

            if (Utility.verifyReadPhoneStatePermissions(this)) {
                String IMEI = teleManager.getDeviceId();
                editor.putString(Constants.IMEI_NO,IMEI);
                editor.commit();
                Log.i(TAG,"Mac id "+IMEI);
                moveToNextActivity();
            }

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
        }
    }
}
