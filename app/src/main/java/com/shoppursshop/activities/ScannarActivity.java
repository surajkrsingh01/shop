package com.shoppursshop.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.scandit.datacapture.barcode.capture.BarcodeCapture;
import com.scandit.datacapture.barcode.capture.BarcodeCaptureListener;
import com.scandit.datacapture.barcode.capture.BarcodeCaptureSession;
import com.scandit.datacapture.barcode.capture.BarcodeCaptureSettings;
import com.scandit.datacapture.barcode.capture.SymbologySettings;
import com.scandit.datacapture.barcode.data.Barcode;
import com.scandit.datacapture.barcode.data.Symbology;
import com.scandit.datacapture.barcode.data.SymbologyDescription;
import com.scandit.datacapture.core.capture.DataCaptureContext;
import com.scandit.datacapture.core.common.feedback.Feedback;
import com.scandit.datacapture.core.data.FrameData;
import com.scandit.datacapture.core.source.Camera;
import com.scandit.datacapture.core.source.CameraSettings;
import com.scandit.datacapture.core.source.FrameSourceState;
import com.scandit.datacapture.core.ui.DataCaptureView;
import com.shoppursshop.R;
import com.shoppursshop.models.MyCustomer;
import com.shoppursshop.models.MyProductItem;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.DialogAndToast;
import com.shoppursshop.utilities.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class ScannarActivity extends NetworkBaseActivity implements BarcodeCaptureListener {

    private BarcodeCapture barcodeCapture;
    private DataCaptureContext dataCaptureContext;
    private Camera camera;
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

        dataCaptureContext = DataCaptureContext.forLicenseKey(getResources().getString(R.string.scan_dit_key));
        CameraSettings cameraSettings = BarcodeCapture.createRecommendedCameraSettings();

// Depending on the use case further camera settings adjustments can be made here.

        camera = Camera.getDefaultCamera();

        if (camera != null) {
            camera.applySettings(cameraSettings);
        }

        if (camera != null) {
            camera.switchToDesiredState(FrameSourceState.ON);
        }

        dataCaptureContext.setFrameSource(camera);

        // The barcode capturing process is configured through barcode capture settings
        // which are then applied to the barcode capture instance that manages barcode recognition.
        BarcodeCaptureSettings barcodeCaptureSettings = new BarcodeCaptureSettings();

        // The settings instance initially has all types of barcodes (symbologies) disabled.
        // For the purpose of this sample we enable a very generous set of symbologies.
        // In your own app ensure that you only enable the symbologies that your app requires as
        // every additional enabled symbology has an impact on processing times.
        HashSet<Symbology> symbologies = new HashSet<>();
        symbologies.add(Symbology.EAN13_UPCA);
        symbologies.add(Symbology.EAN8);
        symbologies.add(Symbology.UPCE);
        symbologies.add(Symbology.QR);
        symbologies.add(Symbology.MICRO_QR);
        symbologies.add(Symbology.DATA_MATRIX);
        symbologies.add(Symbology.CODE39);
        symbologies.add(Symbology.CODE128);
        barcodeCaptureSettings.enableSymbologies(symbologies);

        // Some linear/1d barcode symbologies allow you to encode variable-length data.
        // By default, the Scandit Data Capture SDK only scans barcodes in a certain length range.
        // If your application requires scanning of one of these symbologies, and the length is
        // falling outside the default range, you may need to adjust the "active symbol counts"
        // for this symbology. This is shown in the following few lines of code for one of the
        // variable-length symbologies.
        SymbologySettings symbologySettings =
                barcodeCaptureSettings.getSymbologySettings(Symbology.CODE39);
        SymbologySettings symbologySettingsqR =
                barcodeCaptureSettings.getSymbologySettings(Symbology.QR);


        HashSet<Short> activeSymbolCounts = new HashSet<>(
                Arrays.asList(new Short[] { 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 }));

        symbologySettings.setActiveSymbolCounts(activeSymbolCounts);
        symbologySettingsqR.setActiveSymbolCounts(activeSymbolCounts);

        symbologySettings.setColorInvertedEnabled(true);
        symbologySettingsqR.setColorInvertedEnabled(true);

        barcodeCapture = BarcodeCapture.forDataCaptureContext(dataCaptureContext, barcodeCaptureSettings);
        barcodeCapture.addListener(this);

        barcodeCapture.getFeedback().setSuccess(new Feedback(null,null));

        DataCaptureView dataCaptureView = DataCaptureView.newInstance(this, dataCaptureContext);
        // Add a barcode capture overlay to the data capture view to render the location of captured
        // barcodes on top of the video preview.
        // This is optional, but recommended for better visual feedback.
        //BarcodeCaptureOverlay overlay = BarcodeCaptureOverlay.newInstance(barcodeCapture, dataCaptureView);
        //  overlay.setViewfinder(new RectangularViewfinder());

        ((ViewGroup) findViewById(R.id.scanner_container)).addView(dataCaptureView);
        //setContentView(dataCaptureView);


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
                      //  DialogAndToast.showToast("Product is already added in cart.",this);
                     //   finish();
                    }else{
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
                    showMyDialog(response.getString("message"));
                   // DialogAndToast.showDialog(response.getString("message"),this);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        pauseFrameSource();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        barcodeCapture.removeListener(this);
        dataCaptureContext.removeMode(barcodeCapture);
        super.onDestroy();
    }

    private void pauseFrameSource() {
        // Switch camera off to stop streaming frames.
        // The camera is stopped asynchronously and will take some time to completely turn off.
        // Until it is completely stopped, it is still possible to receive further results, hence
        // it's a good idea to first disable barcode capture as well.
        barcodeCapture.setEnabled(false);
        camera.switchToDesiredState(FrameSourceState.OFF, null);
    }

    @Override
    public void onResume() {
        super.onResume();

        if(Utility.verifyCameraPermissions(this)){
            barcodeCapture.setEnabled(true);
            camera.switchToDesiredState(FrameSourceState.ON, null);
        }

        // Check for camera permission and request it, if it hasn't yet been granted.
        // Once we have the permission the onCameraPermissionGranted() method will be called.
        // requestCameraPermission();

    }

    @Override
    public void onDialogPositiveClicked(){
        if(Utility.verifyCameraPermissions(this)){
            barcodeCapture.setEnabled(true);
            camera.switchToDesiredState(FrameSourceState.ON, null);
        }
    }

    @Override
    public void onDialogNegativeClicked(){
        finish();
    }


    @Override
    public void onBarcodeScanned(BarcodeCapture barcodeCapture, BarcodeCaptureSession barcodeCaptureSession, FrameData frameData) {
        if (barcodeCaptureSession.getNewlyRecognizedBarcodes().isEmpty()) return;

        // List<Barcode> recognizedBarcodes = barcodeCaptureSession.getNewlyRecognizedBarcodes();
        final Barcode barcode = barcodeCaptureSession.getNewlyRecognizedBarcodes().get(0);
        final String symbology = SymbologyDescription.create(barcode.getSymbology()).getReadableName();
        Log.i(TAG,"barcode "+barcode.getData()+ " (" + symbology + ")");
        qrCode = barcode.getData();
        barcodeCapture.setEnabled(false);



        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                processResult(qrCode);
                //showMyDialog("barcode "+barcode.getData()+ " (" + symbology + ")");
            }
        });
    }

    @Override
    public void onObservationStarted(BarcodeCapture barcodeCapture) {

    }

    @Override
    public void onObservationStopped(BarcodeCapture barcodeCapture) {

    }

    @Override
    public void onSessionUpdated(BarcodeCapture barcodeCapture, BarcodeCaptureSession barcodeCaptureSession, FrameData frameData) {

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
