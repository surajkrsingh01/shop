package com.shoppursshop.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;

import com.android.volley.Request;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.shoppursshop.R;
import com.shoppursshop.activities.base.NetworkBaseActivity;
import com.shoppursshop.activities.product.ProductDetailActivity;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScannarActivity extends NetworkBaseActivity {

    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;
    private boolean isProduct;
    private String flag,type,qrCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scannar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        flag = intent.getStringExtra("flag");
        type = intent.getStringExtra("type");

        Log.i(TAG,"flag "+flag+" type "+type);
        //Initialize barcode scanner view
        barcodeScannerView = findViewById(R.id.zxing_barcode_scanner);

        //start capture
        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();


    }

    private void processResult(String rawValue){
        try {
            if(type.equals("addProduct") || type.equals("addProductBarcode") || type.equals("updateStock")
                    || type.equals("searchInvoice")){
                Intent intent = new Intent();
                intent.putExtra("barCode",rawValue);
                setResult(-1,intent);
                finish();
            }else if(type.equals("addToCart")){
                int id = dbHelper.checkBarCodeExist(rawValue);
                if(id == 0){
                    showMyDialog("Product does not exist in database.");
                   // DialogAndToast.showToast("Product does not exist in database.",this);
                  //  finish();
                }else{
                    if(dbHelper.checkProdExistInCart(rawValue)){
                        showMyDialog("Product is already added in cart.");
                      Log.i(TAG,"prod already exist.. "+rawValue);
                    }else{
                        Log.i(TAG,"prod not exist.. "+rawValue);
                        Intent intent = new Intent();
                        intent.putExtra("barCode",rawValue);
                        setResult(-1,intent);
                        finish();
                    }
                }
            }else if(type.equals("offers")){
                int id = dbHelper.checkBarCodeExist(rawValue);
                if(id == 0){
                    showMyDialog("Product does not exist in database.");
                   // DialogAndToast.showToast("Product does not exist in database.",this);
                  //  finish();
                }else{
                    Intent intent = new Intent();
                    intent.putExtra("prod_id",id);
                    setResult(-1,intent);
                    finish();
                }
            }else if(type.equals("customerInfo")){
                checkCustomerInfo(rawValue);
            }else{
               int  id = dbHelper.checkBarCodeExist(rawValue);
               if(id == 0){
                   showMyDialog("Product does not exist in database.");
                 //  DialogAndToast.showToast("Product does not exist in database.",this);
                //   finish();
               }else{
                   Intent intent = new Intent(this, ProductDetailActivity.class);
                   intent.putExtra("id",id);
                   intent.putExtra("subCatName","");
                   intent.putExtra("flag","scan");
                   startActivity(intent);
                   finish();
               }
            }
        }catch (Exception e){
            Log.e(TAG,"error "+e.toString());
        }

    }

    private void checkCustomerInfo(String mobile){

        Map<String,String> params=new HashMap<>();
        params.put("mobile",mobile);
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+Constants.IS_CUSTOMER_REGISTERED;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"checkCustomerRegistered");


    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {

        try {
            if (apiName.equals("checkCustomerRegistered")) {
                if (response.getBoolean("status")) {
                    JSONObject dataObject = response.getJSONObject("result");
                    Intent intent = new Intent();
                    intent.putExtra("custMobile",dataObject.getString("mobileNo"));
                    intent.putExtra("custName",dataObject.getString("name"));
                    intent.putExtra("custId", dataObject.getInt("id"));
                    intent.putExtra("custCode",dataObject.getString("code"));
                    intent.putExtra("custImage",dataObject.getString("photo"));
                    setResult(-1,intent);
                    finish();
                }else{
                    showMyDialog(response.getString("message"));
                   // DialogAndToast.showDialog(response.getString("message"),this);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if(capture != null){
            barcodeScannerView.decodeSingle(new BarcodeCallback() {
                @Override
                public void barcodeResult(BarcodeResult result) {
                    Log.i(TAG,"result "+result.toString());
                    processResult(result.toString());
                }

                @Override
                public void possibleResultPoints(List<ResultPoint> resultPoints) {

                }
            });

            capture.onResume();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if(capture != null)
            capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(capture != null)
            capture.onDestroy();
    }

    @Override
    public void onDialogPositiveClicked(){
        resetBarCodeScanner();
    }

    @Override
    public void onDialogNegativeClicked(){
        finish();
    }

    private void resetBarCodeScanner(){
        barcodeScannerView.decodeSingle(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                Log.i(TAG,"result "+result.toString());
                processResult(result.toString());
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {

            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case Utility.MY_PERMISSIONS_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onDialogPositiveClicked();
                } else {

                }
                break;
        }

    }
}
