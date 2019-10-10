package com.shoppursshop.activities.settings;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.shoppursshop.R;
import com.shoppursshop.activities.NetworkBaseActivity;
import com.shoppursshop.adapters.CustomerAdapter;
import com.shoppursshop.interfaces.MyImageClickListener;
import com.shoppursshop.interfaces.MyItemTypeClickListener;
import com.shoppursshop.models.MyCustomer;
import com.shoppursshop.utilities.ConnectionDetector;
import com.shoppursshop.utilities.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FrequencyOrderCustomerListActivity extends NetworkBaseActivity implements MyImageClickListener, MyItemTypeClickListener {

    private RecyclerView recyclerView;
    private CustomerAdapter myItemAdapter;
    private List<Object> itemList;
    private TextView textViewError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fequency_order_customer_list);

        initFooter(this,10);
        init();
    }

    private void init(){
        itemList = new ArrayList<>();
        textViewError = findViewById(R.id.text_error);
        //imageViewSearch = findViewById(R.id.image_search);
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        myItemAdapter=new CustomerAdapter(this,itemList,"frequencyCustomerList");
        myItemAdapter.setMyItemClickListener(this);
        myItemAdapter.setMyImageClickListener(this);
        recyclerView.setAdapter(myItemAdapter);

        if(ConnectionDetector.isNetworkAvailable(this)){
            getItemList();
        }else{
            showNoData(true,getResources().getString(R.string.no_internet));
        }
    }

    private void getItemList(){
        loading = true;
        Map<String,String> params=new HashMap<>();
        params.put("limit",""+limit);
        params.put("offset",""+offset);
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+Constants.FREQUENCY_CUSTOMER_LIST;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"customerList");
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {

        try {
            if (apiName.equals("customerList") || apiName.equals("refreshCustomerList")) {
                loading = false;
                if (response.getBoolean("status")) {
                    JSONArray dataArray = response.getJSONArray("result");
                    JSONObject jsonObject = null;
                    int len = dataArray.length();
                    MyCustomer myCustomer= null;
                    // itemList.clear();
                    //  itemListFav.clear();
                    //dbHelper.deleteTable(DbHelper.CUSTOMER_INFO_TABLE);
                    //setHeaders();
                    for (int i = 0; i < len; i++) {
                        jsonObject = dataArray.getJSONObject(i);
                        myCustomer = new MyCustomer();
                        myCustomer.setId(jsonObject.getString("id"));
                        myCustomer.setCode(jsonObject.getString("code"));
                        myCustomer.setName(jsonObject.getString("name"));
                        myCustomer.setMobile(jsonObject.getString("mobileNo"));
                        myCustomer.setEmail(jsonObject.getString("email"));
                        myCustomer.setAddress(jsonObject.getString("address"));
                        myCustomer.setCountry(jsonObject.getString("country"));
                        myCustomer.setLocality(jsonObject.getString("locality"));
                        myCustomer.setState(jsonObject.getString("state"));
                        myCustomer.setCity(jsonObject.getString("city"));
                        myCustomer.setLatitude(jsonObject.getString("latitude"));
                        myCustomer.setLongitude(jsonObject.getString("longitude"));
                        myCustomer.setImage(jsonObject.getString("photo"));
                        myCustomer.setIsFav(jsonObject.getString("isFav"));
                        myCustomer.setRatings((float)jsonObject.getDouble("ratings"));
                        myCustomer.setStatus(jsonObject.getString("isActive"));
                        myCustomer.setCustUserCreateStatus(jsonObject.getString("userCreateStatus"));
                        itemList.add(myCustomer);
                    }

                    if(itemList.size() == 0){
                        showNoData(true,"No data available");
                    }else{
                        showNoData(false,null);
                        myItemAdapter.notifyDataSetChanged();
                    }
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onImageClicked(int position, int type, View view) {
        MyCustomer customer = (MyCustomer)itemList.get(position);
        showImageDialog(customer.getImage(),view);
    }

    private void showNoData(boolean show,String message){
        if(show){
            recyclerView.setVisibility(View.GONE);
            textViewError.setVisibility(View.VISIBLE);
            textViewError.setText(message);
        }else{
            recyclerView.setVisibility(View.VISIBLE);
            textViewError.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClicked(int position, int type) {
       Log.i(TAG,"customer clicked "+position);
        Intent intent = new Intent(FrequencyOrderCustomerListActivity.this, FrequencyProductListActivity.class);
        intent.putExtra("custCode",((MyCustomer)itemList.get(position)).getCode());
        startActivity(intent);
    }
}
