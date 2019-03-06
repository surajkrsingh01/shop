package com.shoppursshop.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.shoppursshop.R;

import java.util.List;

public class ScannarActivity extends BaseActivity {

    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;
    private boolean isProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scannar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        isProduct = getIntent().getBooleanExtra("isProduct",false);

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
            if(isProduct){
                Intent intent = new Intent(ScannarActivity.this,AddProductActivity.class);
                intent.putExtra("flag","scan");
                intent.putExtra("data",rawValue);
                startActivity(intent);
            }else{

            }
        }catch (Exception e){
            Log.e(TAG,"error "+e.toString());
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

}
