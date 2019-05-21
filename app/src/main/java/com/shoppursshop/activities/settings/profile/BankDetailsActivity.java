package com.shoppursshop.activities.settings.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.shoppursshop.R;
import com.shoppursshop.activities.BaseImageActivity;
import com.shoppursshop.activities.NetworkBaseActivity;
import com.shoppursshop.activities.settings.ProfileActivity;
import com.shoppursshop.activities.settings.SettingActivity;
import com.shoppursshop.fragments.BankFragment;
import com.shoppursshop.interfaces.OnFragmentInteraction;
import com.shoppursshop.utilities.DialogAndToast;

import java.io.File;

public class BankDetailsActivity extends BaseImageActivity implements OnFragmentInteraction {

    private BankFragment bankFragment;
    private TextView tv_top_parent, tv_parent;

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

        FragmentTransaction trans = getSupportFragmentManager()
                .beginTransaction();
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
    }

    @Override
    public void onFragmentInteraction(Object item, int type) {
        if(type == 0){
            selectImage();
        }
        else{
            DialogAndToast.showDialog("Bank details successfully updated",this);
        }
    }

    @Override
    protected void imageAdded(){
       bankFragment.setImageBase64(convertToBase64(new File(imagePath)),imagePath);
    }
}
