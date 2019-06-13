package com.shoppursshop.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.shoppursshop.R;
import com.shoppursshop.adapters.CouponOfferAdapter;
import com.shoppursshop.interfaces.MyItemClickListener;
import com.shoppursshop.models.Coupon;
import com.shoppursshop.utilities.ConnectionDetector;
import com.shoppursshop.utilities.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CouponOffersActivity extends NetworkBaseActivity implements MyItemClickListener {

    private List<Object> itemList;
    private RecyclerView recyclerView;
    private CouponOfferAdapter myItemAdapter;
    private TextView textApply,textViewError;

    private String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_offers);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();

    }

    private void init(){
        flag = getIntent().getStringExtra("flag");
        itemList = new ArrayList<>();
        textViewError = findViewById(R.id.text_error);
        textApply = findViewById(R.id.btn_apply);
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        myItemAdapter=new CouponOfferAdapter(this,itemList);
        myItemAdapter.setMyItemClickListener(this);
        myItemAdapter.setColorTheme(colorTheme);
        recyclerView.setAdapter(myItemAdapter);

        textApply.setTextColor(colorTheme);

        if(ConnectionDetector.isNetworkAvailable(this)){
            getCouponOffers();
        }
    }


    private void getCouponOffers(){
        Map<String,String> params=new HashMap<>();
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+Constants.GET_COUPON_OFFER;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"offerList");
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {

        try {
            if (apiName.equals("offerList")) {
                if (response.getBoolean("status")) {
                    itemList.clear();
                    JSONArray couponArray = response.getJSONArray("result");
                    JSONObject dataObject = null;
                    Coupon coupon = null;
                    int len = couponArray.length();
                    len = couponArray.length();
                    for (int i = 0; i < len; i++) {
                        dataObject = couponArray.getJSONObject(i);
                        coupon = new Coupon();
                        coupon.setId(dataObject.getInt("id"));
                        coupon.setPercentage(dataObject.getInt("percentage"));
                        coupon.setAmount((float)dataObject.getDouble("amount"));
                        coupon.setName(dataObject.getString("name"));
                        coupon.setStatus(dataObject.getString("status"));
                        coupon.setStartDate(dataObject.getString("startDate"));
                        coupon.setEndDate(dataObject.getString("endDate"));
                        itemList.add(coupon);
                    }

                    if(couponArray.length() == 0){
                        showNoData(true);
                    }else{
                        showNoData(false);
                        myItemAdapter.notifyDataSetChanged();
                    }

                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showNoData(boolean show){
        if(show){
            recyclerView.setVisibility(View.GONE);
            textViewError.setVisibility(View.VISIBLE);
        }else{
            recyclerView.setVisibility(View.VISIBLE);
            textViewError.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClicked(int position) {
        Coupon coupon = (Coupon) itemList.get(position);
        Intent intent  = new Intent();
        intent.putExtra("name",coupon.getName());
        intent.putExtra("id",coupon.getId());
        intent.putExtra("per",coupon.getPercentage());
        intent.putExtra("amount",coupon.getAmount());
        setResult(-1,intent);
        finish();
    }
}
