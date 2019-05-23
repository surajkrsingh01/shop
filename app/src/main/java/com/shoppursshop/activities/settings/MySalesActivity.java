package com.shoppursshop.activities.settings;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.shoppursshop.R;
import com.shoppursshop.activities.InvoiceActivity;
import com.shoppursshop.activities.NetworkBaseActivity;
import com.shoppursshop.adapters.CustomerSaleReportAdapter;
import com.shoppursshop.adapters.MonthlyGraphAdapter;
import com.shoppursshop.interfaces.MyItemClickListener;
import com.shoppursshop.models.Bar;
import com.shoppursshop.models.CustomerSaleReport;
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

public class MySalesActivity extends NetworkBaseActivity implements MyItemClickListener {

    private List<Bar> barList;
    private List<Bar> productSaleList;
    private List<CustomerSaleReport> customerSaleList;
    private RecyclerView recyclerViewMonthlyGraph,recyclerViewCustomerSale;
    private MonthlyGraphAdapter monthlyGraphAdapter;
    private CustomerSaleReportAdapter customerSaleReportAdapter;
    private Toolbar toolbar;
    private TextView tv_top_parent;

    private RelativeLayout relativeLayoutFromDate, relativeLayoutToDate;
    private TextView textViewFromDate, textViewToDate;
    private DatePickerDialog frpmDatePicker, toDatePicker;
    private String fromDate, toDate;
    private String monthTwoDigits = "";
    private String dayTwoDigits = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sales);
        init();
        setToolbarTheme();
        getSales();
    }

    private void init(){
        textViewFromDate = findViewById(R.id.text_from_date);
        textViewToDate = findViewById(R.id.text_to_date);
        relativeLayoutFromDate = findViewById(R.id.relative_from_date);
        relativeLayoutToDate = findViewById(R.id.relative_to_date);
        recyclerViewMonthlyGraph= findViewById(R.id.recyclerView);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.grey900), PorterDuff.Mode.SRC_ATOP);

        Utility.setColorFilter(relativeLayoutFromDate.getBackground(),colorTheme);
        Utility.setColorFilter(relativeLayoutToDate.getBackground(),colorTheme);

        tv_top_parent = findViewById(R.id.text_left_label);
        tv_top_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MySalesActivity.this, SettingActivity.class));
                finish();
            }
        });

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        Calendar calendarPre = Calendar.getInstance();
       // calendarPre.add(Calendar.DAY_OF_MONTH, -(day - 1));
        int yearPre = calendarPre.get(Calendar.YEAR);
        int monthPre = calendarPre.get(Calendar.MONTH);
        int dayPre = calendarPre.get(Calendar.DAY_OF_MONTH);

        if (month < 10) {
            monthTwoDigits = "0" + (month + 1);
        } else {
            monthTwoDigits = "" + (month + 1);
        }

        if (day < 10) {
            dayTwoDigits = "0" + day;
        } else {
            dayTwoDigits = "" + day;
        }
        toDate = year + "-" + monthTwoDigits + "-" + dayTwoDigits;

        if (monthPre < 10) {
            monthTwoDigits = "0" + (monthPre + 1);
        } else {
            monthTwoDigits = "" + (monthPre + 1);
        }

        if (dayPre < 10) {
            dayTwoDigits = "0" + dayPre;
        } else {
            dayTwoDigits = "" + dayPre;
        }
        fromDate = yearPre + "-" + monthTwoDigits + "-" + dayTwoDigits;

        textViewFromDate.setText(dayPre + " " + Utility.getMonthName(monthPre) + " " + yearPre);
        textViewToDate.setText(day + " " + Utility.getMonthName(month) + " " + year);

        frpmDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int yr, int mon, int dy) {
                textViewFromDate.setText(dy + " " + Utility.getMonthName(mon) + " " + yr);
                if (mon < 10) {
                    monthTwoDigits = "0" + (mon + 1);
                } else {
                    monthTwoDigits = "" + (mon + 1);
                }

                if (dy < 10) {
                    dayTwoDigits = "0" + dy;
                } else {
                    dayTwoDigits = "" + dy;
                }

                fromDate = year + "-" + monthTwoDigits + "-" + dayTwoDigits;

                getSaleData();
            }
        }, yearPre, monthPre, dayPre);
        frpmDatePicker.setMessage("Select Start Date");


        toDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int yr, int mon, int dy) {
                textViewToDate.setText(dy + " " + Utility.getMonthName(mon) + " " + yr);
                if (mon < 10) {
                    monthTwoDigits = "0" + (mon + 1);
                } else {
                    monthTwoDigits = "" + (mon + 1);
                }

                if (dy < 10) {
                    dayTwoDigits = "0" + dy;
                } else {
                    dayTwoDigits = "" + dy;
                }

                toDate = year + "-" + monthTwoDigits + "-" + dayTwoDigits;

                getSaleData();
            }
        }, year, month, day);
        toDatePicker.setMessage("Select End Date");

        relativeLayoutFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frpmDatePicker.show();
            }
        });

        relativeLayoutToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDatePicker.show();
            }
        });

        customerSaleList = new ArrayList<>();
        recyclerViewCustomerSale= findViewById(R.id.recyclerViewCustomerSaleReport);
        recyclerViewCustomerSale.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCustomerSale.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManagerMonthlyGraph=new LinearLayoutManager(this);
        recyclerViewCustomerSale.setLayoutManager(layoutManagerMonthlyGraph);
        customerSaleReportAdapter=new CustomerSaleReportAdapter(this,customerSaleList);
        customerSaleReportAdapter.setMyItemClickListener(this);
        customerSaleReportAdapter.setColoTheme(colorTheme);
        recyclerViewCustomerSale.setAdapter(customerSaleReportAdapter);
        recyclerViewCustomerSale.setNestedScrollingEnabled(false);
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
        ((MonthlyGraphAdapter) monthlyGraphAdapter).setTotalTarget(25000);
        recyclerViewMonthlyGraph.setAdapter(monthlyGraphAdapter);
        getSaleData();
    }

    private void getSaleData(){
        Map<String,String> params=new HashMap<>();
        params.put("fromDate",fromDate);
        params.put("toDate",toDate);
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+Constants.SHOP_SALE_DATE;
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
                        JSONObject dataObject = response.getJSONObject("result");
                        JSONArray dataArray = dataObject.getJSONArray("monthlyGraphData");
                        JSONArray custSaleReportArray = dataObject.getJSONArray("customerSaleData");
                        JSONObject jsonObject = null;
                        int len = dataArray.length();
                        for (int i = 0; i < len; i++) {
                            jsonObject = dataArray.getJSONObject(i);
                            updateMonthlySaleList(Utility.parseMonth(jsonObject.getString("orderDate"),
                                    "yyyy-MM-dd HH:mm:ss"), jsonObject.getInt("amount"));
                        }

                        len = custSaleReportArray.length();
                        CustomerSaleReport item = null;
                        for (int i = 0; i < len; i++) {
                            jsonObject = custSaleReportArray.getJSONObject(i);
                            item = new CustomerSaleReport();
                            item.setName(jsonObject.getString("custName"));
                          //  item.setMobile(jsonObject.getString("custName"));
                            item.setMobile("9718181697");
                            item.setAmount((float) jsonObject.getDouble("netPayable"));
                            item.setInvId(jsonObject.getInt("invId"));
                            customerSaleList.add(item);
                        }

                        customerSaleReportAdapter.notifyDataSetChanged();

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
            bar.setBarColor(getBarColor(month));
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
            bar.setBarColor(getBarColor(month));
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
            bar.setBarColor(getBarColor(month));
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

    private int getBarColor(int month){
        int[] barColor={getResources().getColor(R.color.light_blue500),
                getResources().getColor(R.color.yellow500),getResources().getColor(R.color.green500),
                getResources().getColor(R.color.orange500),getResources().getColor(R.color.red_500),
                getResources().getColor(R.color.teal_500),getResources().getColor(R.color.cyan500),
                getResources().getColor(R.color.deep_orange500),getResources().getColor(R.color.blue500),
                getResources().getColor(R.color.purple500),getResources().getColor(R.color.amber500),
                getResources().getColor(R.color.light_green500)};

        return barColor[month];
    }

    @Override
    public void onItemClicked(int position) {
       CustomerSaleReport item = customerSaleList.get(position);
        Intent intent = new Intent(MySalesActivity.this, InvoiceActivity.class);
        intent.putExtra("orderId",""+item.getInvId());
        startActivity(intent);
    }
}
