package com.shoppursshop.activities;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.shoppursshop.R;
import com.shoppursshop.adapters.CouponOfferAdapter;
import com.shoppursshop.interfaces.MyItemClickListener;
import com.shoppursshop.models.Coupon;
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

public class CouponOffersActivity extends NetworkBaseActivity implements MyItemClickListener {

    private List<Object> itemList;
    private RecyclerView recyclerView;
    private CouponOfferAdapter myItemAdapter;
    private TextView textApply,textViewError;

    private EditText editTextCoupon;
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
        editTextCoupon = findViewById(R.id.edit_coupon_code);
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

        textApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ConnectionDetector.isNetworkAvailable(CouponOffersActivity.this)){
                    applyCoupon();
                }else{
                    DialogAndToast.showDialog(getResources().getString(R.string.no_internet),CouponOffersActivity.this);
                }
            }
        });

        if(ConnectionDetector.isNetworkAvailable(this)){
            getCouponOffers();
        }
    }

    private void getCouponOffers(){
        Map<String,String> params=new HashMap<>();
        if(flag.equals("shoppursCoupons")){
            params.put("dbName","SHP1");
        }else{
            params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        }
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+Constants.GET_COUPON_OFFER;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"offerList");
    }

    private void applyCoupon(){
        String couponCode = editTextCoupon.getText().toString();
        if(TextUtils.isEmpty(couponCode)){
            DialogAndToast.showDialog("Please enter coupon code",this);
            return;
        }
        Map<String,String> params=new HashMap<>();
        params.put("couponCode",couponCode);
        params.put("shopCode","SHP1");
        if(flag.equals("shoppursCoupons")){
            params.put("dbName","SHP1");
        }else{
            params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        }
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+Constants.VALIDATE_COUPON_OFFER;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"applyCoupon");
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
                        coupon.setShopCode(sharedPreferences.getString(Constants.SHOP_CODE,""));
                        coupon.setPercentage(dataObject.getInt("percentage"));
                        coupon.setAmount((float)dataObject.getDouble("amount"));
                        coupon.setName(dataObject.getString("name"));
                        coupon.setStatus(dataObject.getString("status"));
                        coupon.setStartDate(dataObject.getString("startDate"));
                        coupon.setEndDate(dataObject.getString("endDate"));
                        dbHelper.addCouponOffer(coupon, Utility.getTimeStamp(),Utility.getTimeStamp());
                        itemList.add(coupon);
                    }

                    if(couponArray.length() == 0){
                        showNoData(true);
                    }else{
                        showNoData(false);
                        myItemAdapter.notifyDataSetChanged();
                    }

                }
            }else if(apiName.equals("applyCoupon")){
                Log.d("response ", response.toString());
                if (response.getBoolean("status")) {
                    JSONObject dataObject = response.getJSONObject("result");
                    Coupon coupon = new Coupon();
                    coupon.setId(dataObject.getInt("id"));
                    coupon.setShopCode(dataObject.getString("shopCode"));
                    coupon.setPercentage(dataObject.getInt("percentage"));
                    coupon.setAmount((float)dataObject.getDouble("amount"));
                    coupon.setName(dataObject.getString("name"));
                    coupon.setStatus(dataObject.getString("status"));
                    coupon.setStartDate(dataObject.getString("startDate"));
                    coupon.setEndDate(dataObject.getString("endDate"));

                    if(coupon.getPercentage()==0){
                        showMyDialog("Coupon is not Valid");
                        return;
                    }else {
                        if(coupon.getPercentage()>0) {
                            Intent intent  = new Intent();
                            intent.putExtra("name",coupon.getName());
                            intent.putExtra("id",coupon.getId());
                            intent.putExtra("shopCode",coupon.getShopCode());
                            intent.putExtra("per",coupon.getPercentage());
                            intent.putExtra("amount",coupon.getAmount());
                            setResult(-1,intent);
                            finish();
                        }
                    }
                }else {
                    int result = response.getInt("result");
                    if(result==0){
                        showMyDialog("Coupon is not Valid");
                    }else if(result==1)
                        showMyDialog("Something went wrong, please try letter");
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
        intent.putExtra("shopCode",coupon.getShopCode());
        intent.putExtra("per",coupon.getPercentage());
        intent.putExtra("amount",coupon.getAmount());
        setResult(-1,intent);
        finish();
    }
}
