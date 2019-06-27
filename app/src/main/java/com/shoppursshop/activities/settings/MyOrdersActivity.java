package com.shoppursshop.activities.settings;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.shoppursshop.R;
import com.shoppursshop.activities.NetworkBaseActivity;
import com.shoppursshop.adapters.OrderAdapter;
import com.shoppursshop.models.OrderItem;
import com.shoppursshop.utilities.ConnectionDetector;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyOrdersActivity extends NetworkBaseActivity {

    private RecyclerView recyclerView;
    private OrderAdapter myItemAdapter;
    private List<Object> itemList;
    private TextView textViewError,tv_top_parent, text_second_label;

    private String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
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
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        myItemAdapter=new OrderAdapter(this,itemList,"orderList");
        recyclerView.setAdapter(myItemAdapter);

        if (ConnectionDetector.isNetworkAvailable(this)){
            getItemList();
        }else{
            showNoNetwork(true);
        }
        tv_top_parent = findViewById(R.id.text_left_label);
        text_second_label = findViewById(R.id.text_second_label);
        if(flag.equals("customerOrders"))
            text_second_label.setText("Customer Orders");
        else text_second_label.setText("Store Orders");
        tv_top_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyOrdersActivity.this, SettingActivity.class));
                finish();
            }
        });
    }

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
                    OrderItem orderItem= null;

                    for (int i = 0; i < len; i++) {
                        jsonObject = dataArray.getJSONObject(i);
                        orderItem = new OrderItem();
                        orderItem.setType(1);
                        orderItem.setId(jsonObject.getString("orderId"));
                        orderItem.setOrderNumber(jsonObject.getString("orderNumber"));
                        orderItem.setDateTime(jsonObject.getString("orderDate"));
                        orderItem.setCustomerName(jsonObject.getString("custName"));
                        orderItem.setCustCode(jsonObject.getString("custCode"));
                        orderItem.setMobile(jsonObject.getString("mobileNo"));
                        orderItem.setAmount(Float.parseFloat(jsonObject.getString("toalAmount")));
                        orderItem.setDeliveryType(jsonObject.getString("orderDeliveryMode"));
                        orderItem.setDeliveryAddress(jsonObject.getString("deliveryAddress"));
                        //orderItem.setLocalImage(R.drawable.default_pic);
                        orderItem.setOrderImage(jsonObject.getString("orderImage"));
                        orderItem.setStatus(jsonObject.getString("orderStatus"));
                        orderItem.setOrderPayStatus(jsonObject.getString("oderPaymentStatus"));
                        orderItem.setLocalImage(R.drawable.thumb_12);

                        itemList.add(orderItem);
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
