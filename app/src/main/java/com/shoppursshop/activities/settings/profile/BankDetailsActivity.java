package com.shoppursshop.activities.settings.profile;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.shoppursshop.R;
import com.shoppursshop.activities.BaseImageActivity;
import com.shoppursshop.activities.NetworkBaseActivity;
import com.shoppursshop.fragments.BankFragment;
import com.shoppursshop.interfaces.OnFragmentInteraction;
import com.shoppursshop.utilities.DialogAndToast;

import java.io.File;

public class BankDetailsActivity extends BaseImageActivity implements OnFragmentInteraction {

    private BankFragment bankFragment;

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
