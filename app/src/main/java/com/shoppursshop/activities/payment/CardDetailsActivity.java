package com.shoppursshop.activities.payment;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.shoppursshop.R;
import com.shoppursshop.activities.NetworkBaseActivity;

public class CardDetailsActivity extends NetworkBaseActivity {

    private RelativeLayout relative_card_type;
    private String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        flag = getIntent().getStringExtra("flag");
        relative_card_type = findViewById(R.id.relative_card_type);
        if(flag.equals("OPTCRDC")){
            relative_card_type.setVisibility(View.GONE);
            findViewById(R.id.tv_card_type_label).setVisibility(View.GONE);
        }

        Button btnPay = findViewById(R.id.btn_pay);
        Button btnCencel = findViewById(R.id.btn_cancel);

        btnPay.setBackgroundColor(colorTheme);
        btnCencel.setBackgroundColor(colorTheme);

    }

}
