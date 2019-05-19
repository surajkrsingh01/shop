package com.shoppursshop.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.shoppursshop.R;

public class AddCustomerActivity extends BaseActivity {

    private EditText etName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_customer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initFooterAction(this);

        init();

    }

    private void init(){
        etName = findViewById(R.id.edit_customer_name);
        //Paris.style(etName).apply(R.style.MyBlueViewTheme);
    }

}
