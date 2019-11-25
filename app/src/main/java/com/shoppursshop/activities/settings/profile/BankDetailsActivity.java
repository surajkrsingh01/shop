package com.shoppursshop.activities.settings.profile;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.shoppursshop.R;
import com.shoppursshop.activities.BaseImageActivity;
import com.shoppursshop.activities.NetworkBaseActivity;
import com.shoppursshop.activities.settings.ProfileActivity;
import com.shoppursshop.activities.settings.SettingActivity;
import com.shoppursshop.fragments.BankFragment;
import com.shoppursshop.interfaces.FirebaseImageUploadListener;
import com.shoppursshop.interfaces.OnFragmentInteraction;
import com.shoppursshop.services.FirebaseImageUploadService;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.DialogAndToast;

import java.io.File;

public class BankDetailsActivity extends BaseImageActivity implements OnFragmentInteraction, FirebaseImageUploadListener {

    private BankFragment bankFragment;
    private TextView tv_top_parent, tv_parent;

    private FirebaseImageUploadService firebaseImageUploadService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        setToolbarTheme();
    }

    private void init(){
        bankFragment = BankFragment.newInstance("update","");

        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.replace(R.id.container, bankFragment,"bankFragment");
        /*
         * IMPORTANT: The following lines allow us to add the fragment
         * to the stack and return to it later, by pressing back
         */
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        //  trans.addToBackStack(null);
        trans.commit();

        tv_top_parent = findViewById(R.id.text_left_label);
        tv_parent = findViewById(R.id.text_right_label);
        tv_top_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BankDetailsActivity.this, SettingActivity.class));
                finish();
            }
        });
        tv_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BankDetailsActivity.this, ProfileActivity.class));
                finish();
            }
        });

        firebaseImageUploadService = FirebaseImageUploadService.getInstance();
    }

    @Override
    public void onFragmentInteraction(Object item, int type) {
        if(type == 0){
            selectImage();
        }
        else{
            if(imagePath == null){
                DialogAndToast.showDialog("Bank details successfully updated",this);
            }else{
                uploadImagesToFirebase();
            }

            //DialogAndToast.showDialog("Bank details successfully updated",this);
        }
    }

    @Override
    protected void imageAdded(){
       bankFragment.setImageBase64(convertToBase64(new File(imagePath)),imagePath);
    }

    private void uploadImagesToFirebase(){
        Log.i(TAG,"uploading images to firebase..");
        showProgress(true);
        firebaseImageUploadService.setFirebaseImageUploadListener(this);
        firebaseImageUploadService.uploadImage(
                "shops/"+sharedPreferences.getString(Constants.SHOP_CODE,"")+"/chequeImage.jpg",
                imagePath);
    }

    @Override
    public void onImageUploaded(String position, String url) {
        imagePath = null;
        showProgress(false);
        bankFragment.attemptUpdateBankDetails(url);
    }

    @Override
    public void onImageFailed(String position) {

    }
}
