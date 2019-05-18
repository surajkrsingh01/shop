package com.shoppursshop.activities.settings;

import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.shoppursshop.R;
import com.shoppursshop.activities.NetworkBaseActivity;
import com.shoppursshop.adapters.MonthlyGraphAdapter;
import com.shoppursshop.models.Bar;
import com.shoppursshop.models.MyReview;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MySalesActivity extends NetworkBaseActivity {

    private List<Bar> barList;
    private List<Bar> productSaleList;
    private RecyclerView recyclerViewMonthlyGraph;
    private MonthlyGraphAdapter monthlyGraphAdapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sales);
        init();
        setToolbarTheme();
        getSales();
    }

    private void init(){
        recyclerViewMonthlyGraph= findViewById(R.id.recyclerView);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.grey900), PorterDuff.Mode.SRC_ATOP);
    }

    private void getSales(){
        barList = new ArrayList<>();
        productSaleList = new ArrayList<>();
        initMonthlySaleList();
        recyclerViewMonthlyGraph.setItemAnimator(new DefaultItemAnimator());
        recyclerViewMonthlyGraph.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManagerMonthlyGraph=new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewMonthlyGraph.setLayoutManager(layoutManagerMonthlyGraph);
        monthlyGraphAdapter=new MonthlyGraphAdapter(this,barList,1);
        ((MonthlyGraphAdapter) monthlyGraphAdapter).setTotalTarget(10);
        recyclerViewMonthlyGraph.setAdapter(monthlyGraphAdapter);
        getSaleData();
    }

    private void getSaleData(){
        Map<String,String> params=new HashMap<>();
        params.put("id","1");
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+Constants.PRODUCT_SALE_DATA;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"shopSaleData");
    }

    public int getSaleData(String monthName){
        int value = 0;
        for(Bar bar : productSaleList){
            if(bar.getName().equals(monthName)){
                value = bar.getSaleValue();
                break;
            }
        }

        return value;
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {

        try {
            if (apiName.equals("shopSaleData")) {
                if (response.getBoolean("status")) {
                    if(response.getString("result") == null || response.getString("result").equals("null")){
                        setMonthlyBar();
                    }else{
                        JSONArray dataArray = response.getJSONArray("result");
                        JSONObject jsonObject = null;
                        int len = dataArray.length();
                        for (int i = 0; i < len; i++) {
                            jsonObject = dataArray.getJSONObject(i);
                            updateMonthlySaleList(Utility.parseMonth(jsonObject.getString("orderDate"),
                                    "yyyy-MM-dd HH:mm:ss"), jsonObject.getInt("qty"));
                        }

                        if(len == 0){
                            setNullMonthlyBar();
                        }else{
                            setMonthlyBar();
                        }
                    }
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void initMonthlySaleList(){
        Calendar calendarTemp =Calendar.getInstance();
        productSaleList.clear();
        for(int i=0;i<12;i++){
            if(i > 0)
                calendarTemp.add(Calendar.MONTH, -1);

            int month=calendarTemp.get(Calendar.MONTH);
            int year=calendarTemp.get(Calendar.YEAR);
            String monthName=getMonth(month);
            Bar bar=new Bar();
            bar.setName(monthName);
            bar.setSaleValue(0);
            productSaleList.add(bar);
        }
    }

    public String getMonth(int position){
        String[] monthInText={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

        return monthInText[position];
    }

    public void setMonthlyBar(){
        Calendar calendarTemp =Calendar.getInstance();
        barList.clear();
        for(int i=0;i<12;i++){
            if(i > 0)
                calendarTemp.add(Calendar.MONTH, -1);

            int month=calendarTemp.get(Calendar.MONTH);
            int year=calendarTemp.get(Calendar.YEAR);
            String monthName=getMonth(month);
            Bar bar=new Bar();
            bar.setName(monthName);
            bar.setSaleValue(getSaleData(monthName));
            Log.i(TAG,"Sale value "+bar.getSaleValue());
            bar.setSaleAchievedValue(5000);
            bar.setSaleTargetValue(8000);
            bar.setBudget(10000);
            barList.add(bar);
        }

        monthlyGraphAdapter.notifyDataSetChanged();
    }

    public void setNullMonthlyBar(){
        Calendar calendarTemp =Calendar.getInstance();
        barList.clear();
        for(int i=0;i<12;i++){
            if(i > 0)
                calendarTemp.add(Calendar.MONTH, -1);

            int month=calendarTemp.get(Calendar.MONTH);
            int year=calendarTemp.get(Calendar.YEAR);
            String monthName=getMonth(month);
            Bar bar=new Bar();
            bar.setName(monthName);
            bar.setSaleValue(0);
            Log.i(TAG,"Sale value "+bar.getSaleValue());
            bar.setSaleAchievedValue(5000);
            bar.setSaleTargetValue(8000);
            bar.setBudget(10000);
            barList.add(bar);
        }

        monthlyGraphAdapter.notifyDataSetChanged();
    }

    public void updateMonthlySaleList(String monthName,int value){
        for(Bar bar : productSaleList){
            if(bar.getName().equals(monthName)){
                bar.setSaleValue(value);
                break;
            }
        }
    }

}
