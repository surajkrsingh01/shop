package com.shoppursshop.activities.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.shoppursshop.R;
import com.shoppursshop.activities.NetworkBaseActivity;
import com.shoppursshop.adapters.OrderAdapter;
import com.shoppursshop.adapters.ShopOfferListAdapter;
import com.shoppursshop.models.MyProduct;
import com.shoppursshop.models.MyProductItem;
import com.shoppursshop.models.OrderItem;
import com.shoppursshop.models.ShopOfferItem;
import com.shoppursshop.utilities.ConnectionDetector;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.DialogAndToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyOffersActivity extends NetworkBaseActivity {

    private RecyclerView recyclerView;
    private ShopOfferListAdapter myItemAdapter;
    private List<ShopOfferItem> itemList;
    private TextView textViewError,tv_top_parent, text_second_label;
    private FloatingActionButton fab_new_offer;

    private String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_offers);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setToolbarTheme();
        init();
    }

    private void init(){
        flag = getIntent().getStringExtra("flag");
        itemList = new ArrayList<>();
        textViewError = findViewById(R.id.text_no_order);
        recyclerView=findViewById(R.id.recycler_view);
        fab_new_offer = findViewById(R.id.fab_new_offer);
        fab_new_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyOffersActivity.this, CreateOfferActivity.class));
                //DialogAndToast.showToast("Creating New Offer ", MyOffersActivity.this);
            }
        });

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        myItemAdapter=new ShopOfferListAdapter(this,itemList);
        recyclerView.setAdapter(myItemAdapter);

        if (ConnectionDetector.isNetworkAvailable(this)){
            ShopOfferItem offerItem;

            offerItem = new ShopOfferItem();
            offerItem.setOfferName("Buy 1 Get 1 Free");
            offerItem.setProductName("Round Polo Tshirt");
            offerItem.setProductLocalImage(R.drawable.thumb_12);
            itemList.add(offerItem);


            //getItemList();
        }else{
            showNoNetwork(true);
        }
        tv_top_parent = findViewById(R.id.text_left_label);
        text_second_label = findViewById(R.id.text_second_label);

        tv_top_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyOffersActivity.this, SettingActivity.class));
                finish();
            }
        });
    }

    //ems simulation iq


    private void getItemList(){
        Map<String,String> params=new HashMap<>();
        params.put("limit", ""+limit);
        params.put("offset",""+offset);
        params.put("code",sharedPreferences.getString(Constants.SHOP_CODE,""));
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url="",api="";
        if(flag.equals("customerOrders")){
            url=getResources().getString(R.string.url)+Constants.GET_CUSTOMER_ORDERS;
            api = "customerOrders";
        }else{
            url=getResources().getString(R.string.url)+Constants.GET_SHOP_ORDERS;
            api = "shopOrders";
        }
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),api);
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {

        try {
            if (apiName.equals("customerOrders") || apiName.equals("shopOrders")) {
                if (response.getBoolean("status")) {
                    JSONArray dataArray = response.getJSONArray("result");
                    JSONObject jsonObject = null;
                    int len = dataArray.length();
                    ShopOfferItem offerItem= null;

                    for (int i = 0; i < len; i++) {
                        jsonObject = dataArray.getJSONObject(i);
                        offerItem = new ShopOfferItem();
                        offerItem.setProductLocalImage(R.drawable.thumb_12);
                        itemList.add(offerItem);
                    }

                    if(len == 0){
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

    private void showNoNetwork(boolean show){
        if(show){
            recyclerView.setVisibility(View.GONE);
            textViewError.setVisibility(View.VISIBLE);
            textViewError.setText(getResources().getString(R.string.no_internet));
        }else{
            recyclerView.setVisibility(View.VISIBLE);
            textViewError.setVisibility(View.GONE);
        }
    }

}
