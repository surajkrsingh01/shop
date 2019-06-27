package com.shoppursshop.activities.settings.profile;

import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.shoppursshop.R;
import com.shoppursshop.activities.BaseImageActivity;
import com.shoppursshop.utilities.ConnectionDetector;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.DialogAndToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class KYCActivity extends BaseImageActivity {

    private RadioButton rbPan,rbAadhar;
    private EditText etName,etDocNumber;
    private TextInputLayout tilDocNumber;
    private TextView tvUploadLabel;

    private ImageView imageViewDoc,imageViewCamera;
    private RelativeLayout rvDoc;
    private RequestOptions requestOptions;

    private Button btnSubmit;

    private String imageBase64;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kyc);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    private void init(){
        rbPan = findViewById(R.id.radio_pan);
        rbAadhar = findViewById(R.id.radio_aadhar);
        tvUploadLabel = findViewById(R.id.text_upload_label);
        etName = findViewById(R.id.edit_full_name);
        etDocNumber = findViewById(R.id.edit_doc_number);
        tilDocNumber = findViewById(R.id.til_doc_number);
        btnSubmit = findViewById(R.id.btn_submit);
        imageBase64 = "no";
        requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        // requestOptions.override(Utility.dpToPx(150, context), Utility.dpToPx(150, context));
        requestOptions.centerCrop();
        requestOptions.skipMemoryCache(true);

        rvDoc = findViewById(R.id.relative_doc_image);
        imageViewDoc = findViewById(R.id.image_doc);
        imageViewCamera = findViewById(R.id.image_camera);


        rbPan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                   // etDocNumber.setHint("PAN Card Number");
                    tilDocNumber.setHint("PAN Card Number");
                    tvUploadLabel.setText("Upload PAN Image");
                }
            }
        });

        rbAadhar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                  //  etDocNumber.setHint("AADHAR Card Number");
                    tilDocNumber.setHint("AADHAR Card Number");
                    tvUploadLabel.setText("Upload AADHAR Image");
                }

            }
        });

        String docType = sharedPreferences.getString(Constants.KYC_DOC_TYPE,"");
        if(TextUtils.isEmpty(docType) || docType.equals("pan")){
            rbPan.setChecked(true);
        }else{
            rbAadhar.setChecked(true);
        }

        etName.setText(sharedPreferences.getString(Constants.KYC_NAME,""));
        etDocNumber.setText(sharedPreferences.getString(Constants.KYC_DOC_NUMBER,""));

        if(!TextUtils.isEmpty(sharedPreferences.getString(Constants.CHEQUE_IMAGE,""))){
            imageViewDoc.setVisibility(View.VISIBLE);
            imageViewCamera.setVisibility(View.GONE);
            Glide.with(this)
                    .load(sharedPreferences.getString(Constants.KYC_DOC_IMAGE,""))
                    .apply(requestOptions)
                    .into(imageViewDoc);
        }

        rvDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 if(ConnectionDetector.isNetworkAvailable(KYCActivity.this)){
                     attemptSubmit();
                 }
            }
        });
    }

    private void attemptSubmit(){
        String name = etName.getText().toString();
        String docNumber = etDocNumber.getText().toString();
        boolean isPanChecked = rbPan.isChecked();

        Map<String,String> params=new HashMap<>();

        params.put("userId",sharedPreferences.getString(Constants.USER_ID,""));
        params.put("mobile",sharedPreferences.getString(Constants.MOBILE_NO,""));
        params.put("fullName",name);
        params.put("docNumber",docNumber);
        params.put("isPanChecked",""+isPanChecked);
        params.put("docImage",imageBase64);
        // params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        // params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        // params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));

        String url=getResources().getString(R.string.url)+Constants.UPDATE_BANK_DETAILS;
      //  showProgress(true);
     //   jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"updateKYCDetails");
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {

        try {
            if(apiName.equals("updateBankDetails")){
                if(response.getBoolean("status")){
                    editor.putString(Constants.KYC_NAME,etName.getText().toString());
                    editor.putString(Constants.KYC_DOC_NUMBER,etDocNumber.getText().toString());
                    if(rbPan.isChecked()){
                        editor.putString(Constants.KYC_DOC_TYPE,"pan");
                    }else{
                        editor.putString(Constants.KYC_DOC_TYPE,"aadhar");
                    }
                    if(!imageBase64.equals("no")){
                        editor.putString(Constants.KYC_DOC_IMAGE,"");
                    }
                    editor.commit();
                }else{
                    DialogAndToast.showDialog(response.getString("message"),this);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void imageAdded(){
        imageBase64 = convertToBase64(new File(imagePath));
        imageViewDoc.setVisibility(View.VISIBLE);
        imageViewCamera.setVisibility(View.GONE);
        Glide.with(this)
                .load(imagePath)
                .apply(requestOptions)
                .into(imageViewDoc);
    }

}
