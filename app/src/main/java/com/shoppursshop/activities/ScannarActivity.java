package com.shoppursshop.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.shoppursshop.R;
import com.shoppursshop.models.MyCustomer;
import com.shoppursshop.models.MyProductItem;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.DialogAndToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScannarActivity extends NetworkBaseActivity {

    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;
    private boolean isProduct;
    private String flag,type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scannar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        flag = intent.getStringExtra("flag");
        type = intent.getStringExtra("type");

        //Initialize barcode scanner view
        barcodeScannerView = findViewById(R.id.zxing_barcode_scanner);

        //start capture
        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();


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

    private void processResult(String rawValue){
        try {
            if(type.equals("addProduct") || type.equals("addProductBarcode")){
                Intent intent = new Intent();
                intent.putExtra("barCode",rawValue);
                setResult(-1,intent);
                finish();
            }else if(type.equals("addToCart")){
                int id = dbHelper.checkBarCodeExist(rawValue);
                if(id == 0){
                    DialogAndToast.showToast("Product is not exist in database.",this);
                    finish();
                }else{
                    if(dbHelper.checkProdExistInCart(rawValue)){
                        DialogAndToast.showToast("Product is already added in cart.",this);
                        finish();
                    }else{
                       MyProductItem myProductItem = dbHelper.getProductDetailsByBarCode(rawValue);
                        if(myProductItem.getIsBarCodeAvailable().equals("Y")){
                            myProductItem.setBarcodeList(dbHelper.getBarCodesForCart(myProductItem.getProdId()));
                        }
                        if(myProductItem.getBarcodeList().size() > 0){
                            myProductItem.setQty(1);
                            myProductItem.setTotalAmount(myProductItem.getProdSp());
                            dbHelper.addProductToCart(myProductItem);
                            showMyBothDialog("Product is added in cart.","Checkout","Scan More");
                        }else{
                            showMyDialog("Product is out of stock.");
                        }

                    }
                }
            }else if(type.equals("customerInfo")){
                checkCustomerInfo(rawValue);
            }else{
               int  id = dbHelper.checkBarCodeExist(rawValue);
               if(id == 0){
                   DialogAndToast.showToast("Product is not exist in database.",this);
                   finish();
               }else{
                   Intent intent = new Intent(this,ProductDetailActivity.class);
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
                    DialogAndToast.showDialog(response.getString("message"),this);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
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

}
