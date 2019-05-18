package com.shoppursshop.activities.settings;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.shoppursshop.R;
import com.shoppursshop.activities.NetworkBaseActivity;
import com.shoppursshop.adapters.PaymentSchemeAdapter;
import com.shoppursshop.adapters.SettingsAdapter;
import com.shoppursshop.interfaces.MyItemClickListener;
import com.shoppursshop.models.MyProductItem;
import com.shoppursshop.utilities.DialogAndToast;
import com.shoppursshop.utilities.Utility;

import java.util.ArrayList;
import java.util.List;

public class BuyUserLicenceActivity extends NetworkBaseActivity implements MyItemClickListener {

    private TextView tvnoOfUser, tvtotalAmount;
    private Button btnProceed, btn_minus, btn_plus;
    private List<MyProductItem> mschemeList;
    private float totalAmount;
    private int noOfUser = 1, schemeCode =-1;
    private RecyclerView recyclerView;
    private PaymentSchemeAdapter paymentSchemeAdapter;
    private MyProductItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_user_licence);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setToolbarTheme();
        recyclerView = findViewById(R.id.recycler_payment_option);
        tvnoOfUser = findViewById(R.id.tv_cartCount);
        tvtotalAmount = findViewById(R.id.tvTotalAmount);
        btn_minus = findViewById(R.id.btn_minus);
        btn_plus = findViewById(R.id.btn_plus);
        btnProceed = findViewById(R.id.btnProceed);

        mschemeList = new ArrayList<>();
        item = new MyProductItem();
        item.setProdName("Monthly Payment ");
        item.setProdMrp(500);
        item.setSelected(false);
        mschemeList.add(item);

        item = new MyProductItem();
        item.setProdName("Quarterly Payment ");
        item.setProdMrp(2500);
        item.setSelected(false);
        mschemeList.add(item);

        item = new MyProductItem();
        item.setProdName("Yearly Payment ");
        item.setProdMrp(4500);
        item.setSelected(false);
        mschemeList.add(item);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        paymentSchemeAdapter=new PaymentSchemeAdapter(this,mschemeList);
        paymentSchemeAdapter.setMyItemClickListener(this);
        recyclerView.setAdapter(paymentSchemeAdapter);

        btn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(noOfUser>1){
                    noOfUser = noOfUser - 1;
                    tvnoOfUser.setText(String.valueOf(noOfUser));
                    calculateTotal();
                }
            }
        });

        btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noOfUser = noOfUser + 1;
                tvnoOfUser.setText(String.valueOf(noOfUser));
                calculateTotal();
            }
        });

        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(noOfUser == 0)
                    DialogAndToast.showDialog("Please Add User", BuyUserLicenceActivity.this);
                else if(schemeCode == -1)
                    DialogAndToast.showDialog("Please Select Scheme", BuyUserLicenceActivity.this);
                else
                    DialogAndToast.showDialog("Total Amout Paybale Rs. "+totalAmount, BuyUserLicenceActivity.this);
            }
        });
    }

    private void calculateTotal(){
        noOfUser = Integer.parseInt(tvnoOfUser.getText().toString());
        if(schemeCode>=0){
            totalAmount = mschemeList.get(schemeCode).getProdMrp() * noOfUser;
        }
        tvtotalAmount.setText(String.valueOf(totalAmount));
    }

    @Override
    public void onItemClicked(int position) {
        Log.d("position ", ""+position);
        for(int i=0;i<mschemeList.size();i++){
            if(i!=position)
                mschemeList.get(i).setSelected(false);
        }
        schemeCode = position;
        calculateTotal();
    }
}
