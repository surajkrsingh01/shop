package com.shoppursshop.activities.settings;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
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

import com.android.volley.Request;
import com.shoppursshop.R;
import com.shoppursshop.activities.NetworkBaseActivity;
import com.shoppursshop.adapters.MySubscriptionAdapter;
import com.shoppursshop.adapters.PaymentSchemeAdapter;
import com.shoppursshop.adapters.SettingsAdapter;
import com.shoppursshop.interfaces.MyItemClickListener;
import com.shoppursshop.models.MyProductItem;
import com.shoppursshop.models.UserLicense;
import com.shoppursshop.utilities.ConnectionDetector;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.DialogAndToast;
import com.shoppursshop.utilities.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuyUserLicenceActivity extends NetworkBaseActivity implements MyItemClickListener {

    private TextView tvnoOfUser, tvtotalAmount;
    private Button btnProceed, btn_minus, btn_plus;
    private List<MyProductItem> mschemeList;
    private List<UserLicense> userLicenseList;
    private float totalAmount;
    private int noOfUser = 1, schemeCode =-1;
    private RecyclerView recyclerView,recyclerViewUserLicense;
    private PaymentSchemeAdapter paymentSchemeAdapter;
    private MySubscriptionAdapter mySubscriptionAdapter;
    private MyProductItem item;
    private TextView tv_top_parent;

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
        ((GradientDrawable)btn_minus.getBackground()).setColor(colorTheme);
        btn_plus = findViewById(R.id.btn_plus);
        ((GradientDrawable)btn_plus.getBackground()).setColor(colorTheme);
        btnProceed = findViewById(R.id.btnProceed);
        btnProceed.setBackgroundColor(colorTheme);

        btnProceed.setBackgroundColor(colorTheme);

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

        userLicenseList = new ArrayList<>();
        recyclerViewUserLicense = findViewById(R.id.recycler_mysubscription);
        recyclerViewUserLicense.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager1=new LinearLayoutManager(this);
        recyclerViewUserLicense.setLayoutManager(layoutManager1);
        recyclerViewUserLicense.setItemAnimator(new DefaultItemAnimator());
        mySubscriptionAdapter=new MySubscriptionAdapter(this,userLicenseList);
        //paymentSchemeAdapter.setMyItemClickListener(this);
        recyclerViewUserLicense.setAdapter(mySubscriptionAdapter);

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

        tv_top_parent = findViewById(R.id.text_left_label);
        tv_top_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BuyUserLicenceActivity.this, SettingActivity.class));
                finish();
            }
        });

        if(ConnectionDetector.isNetworkAvailable(this)){
            getUserLicense();
        }
    }

    private void getUserLicense(){
        Map<String,String> params=new HashMap<>();
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+Constants.GET_USER_LICENSE;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"userLicenses");
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {

        try {
            if (apiName.equals("userLicenses")) {
                if (response.getBoolean("status")) {
                    JSONArray jsonArray = response.getJSONArray("result");
                    int len = jsonArray.length();
                    JSONObject jsonObject = null;
                    UserLicense item = null;
                    for(int i=0; i<len; i++){
                        jsonObject = jsonArray.getJSONObject(i);
                        item = new UserLicense();
                        item.setLicenseType(jsonObject.getString("licenseType"));
                        item.setPurchaseDate(jsonObject.getString("purchaseDate"));
                        item.setRenewdDate(jsonObject.getString("renewdDate"));
                        item.setExpiryDate(jsonObject.getString("expiryDate"));
                        item.setNumOfUsers(jsonObject.getInt("numOfUsers"));
                        item.setAmount((float)jsonObject.getDouble("amount"));
                        item.setScheme(jsonObject.getString("scheme"));

                        userLicenseList.add(item);
                    }

                    mySubscriptionAdapter.notifyDataSetChanged();
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void calculateTotal(){
        noOfUser = Integer.parseInt(tvnoOfUser.getText().toString());
        if(schemeCode>=0){
            totalAmount = mschemeList.get(schemeCode).getProdMrp() * noOfUser;
        }
        tvtotalAmount.setText(Utility.numberFormat(totalAmount));
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
