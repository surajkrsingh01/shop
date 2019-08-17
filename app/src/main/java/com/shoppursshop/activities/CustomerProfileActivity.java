package com.shoppursshop.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.shoppursshop.R;
import com.shoppursshop.adapters.MonthlyGraphAdapter;
import com.shoppursshop.adapters.MyReviewAdapter;
import com.shoppursshop.adapters.OrderAdapter;
import com.shoppursshop.models.Bar;
import com.shoppursshop.models.MyReview;
import com.shoppursshop.models.OrderItem;
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

public class CustomerProfileActivity extends NetworkBaseActivity {

    private RecyclerView recyclerViewMonthlyGraph,recyclerViewOrder;
    private RecyclerView.Adapter monthlyGraphAdapter;
    private OrderAdapter orderAdapter;
    private List<Bar> barList,customerSaleList;
    private List<Object> orderItemList;

    private String custId,custCode,isFav;
    private float selectedRatings,ratings;
    private Menu menu;

    private TextView textViewInitials,textViewName,textViewAddress,textViewStateCity,
            textViewMobile,textTotalSale,textRatings;
    private ImageView imageView2,imageView3,imageView4,imageFav;
    private Button btnLoadMorePreOrders;

    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.grey900), PorterDuff.Mode.SRC_ATOP);

        init();
        initFooter(this,2);
    }

    private void init(){
        Intent intent = getIntent();
        textViewName = findViewById(R.id.text_customer_name);
        textViewAddress = findViewById(R.id.text_address);
        textViewStateCity = findViewById(R.id.text_state_city);
        textViewMobile = findViewById(R.id.text_mobile);
        textViewInitials = findViewById(R.id.tv_initial);
        textTotalSale = findViewById(R.id.text_total_sale);
        textRatings = findViewById(R.id.text_start_rating);

        imageView2 = findViewById(R.id.image_view_2);
        imageView3 = findViewById(R.id.image_view_3);
        imageView4 = findViewById(R.id.image_view_4);
        imageFav = findViewById(R.id.image_fav);

        btnLoadMorePreOrders = findViewById(R.id.btn_load_more_order);

        isFav = intent.getStringExtra("isFav");
        custCode = intent.getStringExtra("custCode");
        custId = intent.getStringExtra("custId");
        ratings = intent.getFloatExtra("ratings",0f);

        String name = intent.getStringExtra("name");
        textViewName.setText(name);
        String address = intent.getStringExtra("address");
        String city = intent.getStringExtra("stateCity");
        String mobile = intent.getStringExtra("mobile");
        final String image = intent.getStringExtra("customerImage");

      //  final String image = "http://www.shoppurs.in/images/shops/SHP4/photo.jpg";

        textViewMobile.setText(mobile);

        if(TextUtils.isEmpty(address) || address.trim().equals("null")){
            textViewAddress.setVisibility(View.GONE);
        }else{
            textViewAddress.setText(address);
        }

        Log.i(TAG,"state and city "+city+".");

        if(TextUtils.isEmpty(city) || city.trim().equals("null, null") || city.trim().equals(",")){
            textViewStateCity.setVisibility(View.GONE);
        }else{
            textViewStateCity.setText(city);
        }

        String initials = "";
        if(name.contains(" ")){
            String[] nameArray = name.split(" ");
            initials = nameArray[0].substring(0,1)+nameArray[1].substring(0,1);
        }else{
            initials = name.substring(0,2);
        }

        textViewInitials.setText(initials);

        if(image !=null && image.contains("http")){
            textViewInitials.setVisibility(View.GONE);
            imageView2.setVisibility(View.VISIBLE);
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
            requestOptions.dontTransform();
            // requestOptions.override(Utility.dpToPx(150, context), Utility.dpToPx(150, context));
            // requestOptions.centerCrop();
            requestOptions.skipMemoryCache(false);

            Glide.with(this)
                    .load(image)
                    .apply(requestOptions)
                    .into(imageView2);

            imageView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showImageDialog(image,imageView2);
                    /*Intent intent=new Intent(CustomerProfileActivity.this, ImageActivity.class);
                    intent.putExtra("imageUrl",image);
                    Pair participants = new Pair<>(imageView2, ViewCompat.getTransitionName(imageView2));
                    ActivityOptionsCompat transitionActivityOptions =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(
                                    CustomerProfileActivity.this, participants);
                    ActivityCompat.startActivity(CustomerProfileActivity.this,
                            intent, transitionActivityOptions.toBundle());*/
                }
            });

        }else{
            textViewInitials.setVisibility(View.VISIBLE);
            imageView2.setVisibility(View.GONE);
        }

        barList = new ArrayList<>();
        customerSaleList = new ArrayList<>();
        initMonthlySaleList();
        recyclerViewMonthlyGraph=(RecyclerView)findViewById(R.id.recycler_view_monthly_graph);
        recyclerViewMonthlyGraph.setItemAnimator(new DefaultItemAnimator());
        recyclerViewMonthlyGraph.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManagerMonthlyGraph=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        recyclerViewMonthlyGraph.setLayoutManager(layoutManagerMonthlyGraph);
        monthlyGraphAdapter=new MonthlyGraphAdapter(this,barList,1);
        ((MonthlyGraphAdapter) monthlyGraphAdapter).setTotalTarget(25000);
        recyclerViewMonthlyGraph.setAdapter(monthlyGraphAdapter);

        orderItemList = new ArrayList<>();
        recyclerViewOrder=findViewById(R.id.recycler_view_order);
        recyclerViewOrder.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerViewOrder.setLayoutManager(layoutManager);
        recyclerViewOrder.setItemAnimator(new DefaultItemAnimator());
        orderAdapter=new OrderAdapter(this,orderItemList,"orderList");
        recyclerViewOrder.setAdapter(orderAdapter);
        recyclerViewOrder.setNestedScrollingEnabled(false);

        btnLoadMorePreOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                offset = offset + limit;
                getPreOrders();
            }
        });

        imageFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFavStatus();
            }
        });

        if(isFav.equals("Y")){
            Log.i(TAG,"Customer is favourite: "+isFav);
            imageFav.setImageResource(R.drawable.ic_favorite_black_24dp);
        }else{
            Log.i(TAG,"Customer is not favourite:: "+isFav);
            imageFav.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }

        getSaleData();
        getPreOrders();
        getRatings();
    }



    private void setPreOrders(){
        OrderItem orderItem = new OrderItem();
        orderItem.setType(2);
        orderItem.setId("00001123");
        orderItem.setStatus("Delivered");
        orderItem.setDateTime("13:32, Nov, 05, 2018");
        orderItem.setRating(4);
        orderItem.setCustomerName("Sonam Kapoor");
        orderItem.setMobile("9718181697");
        orderItem.setAmount(2000);
        orderItem.setDeliveryType("Cash On Delivery");
        orderItem.setLocalImage(R.drawable.thumb_11);
        orderItemList.add(orderItem);

        orderItem = new OrderItem();
        orderItem.setType(2);
        orderItem.setId("00001124");
        orderItem.setStatus("Delivered");
        orderItem.setDateTime("14:32, Nov, 06, 2018");
        orderItem.setRating(4);
        orderItem.setCustomerName("Katie Perry");
        orderItem.setMobile("9718181698");
        orderItem.setAmount(24000);
        orderItem.setDeliveryType("Pick Up");
        orderItem.setLocalImage(R.drawable.thumb_12);
        orderItemList.add(orderItem);

        orderItem = new OrderItem();
        orderItem.setType(2);
        orderItem.setId("00001125");
        orderItem.setStatus("Cancelled");
        orderItem.setDateTime("13:32, Nov, 07, 2018");
        orderItem.setRating(4);
        orderItem.setCustomerName("Mohit Kumar");
        orderItem.setMobile("9718181699");
        orderItem.setAmount(34000);
        orderItem.setDeliveryType("In Store");
        orderItem.setLocalImage(R.drawable.thumb_13);
        orderItemList.add(orderItem);

        orderItem = new OrderItem();
        orderItem.setType(2);
        orderItem.setId("00001128");
        orderItem.setStatus("Delivered");
        orderItem.setDateTime("13:32, Nov, 08, 2018");
        orderItem.setRating(4);
        orderItem.setCustomerName("Sachin Kumar");
        orderItem.setMobile("9718181610");
        orderItem.setAmount(4000);
        orderItem.setDeliveryType("In Store");
        orderItem.setLocalImage(R.drawable.thumb_11);
        orderItemList.add(orderItem);

        orderItem = new OrderItem();
        orderItem.setType(2);
        orderItem.setId("00001129");
        orderItem.setStatus("Delivered");
        orderItem.setDateTime("13:32, Nov, 09, 2018");
        orderItem.setRating(4);
        orderItem.setCustomerName("Amit Kumar");
        orderItem.setMobile("9718181611");
        orderItem.setAmount(10000);
        orderItem.setDeliveryType("Cash On Delivery");
        orderItem.setLocalImage(R.drawable.thumb_12);
        orderItemList.add(orderItem);

        orderAdapter.notifyDataSetChanged();
    }

    private void getSaleData(){
        Map<String,String> params=new HashMap<>();
        params.put("code",sharedPreferences.getString(Constants.SHOP_CODE,""));
        params.put("id",custId);
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+Constants.CUST_SALE_DATA;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"customerSaleData");
    }

    private void getRatings(){
        Map<String,String> params=new HashMap<>();
        params.put("code",custCode);
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+Constants.CUST_GET_RATINGS;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"customerRatings");
    }

    private void getPreOrders(){
        Map<String,String> params=new HashMap<>();
        params.put("limit",""+limit);
        params.put("offset",""+offset);
        params.put("code",custCode);
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+Constants.CUST_PRE_ORDER;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"customerPreOrder");
    }

    private void setFavStatus(){
        String favStatus = "";
        if(isFav.equals("Y")){
            favStatus = "N";
        }else{
            favStatus = "Y";
        }
        Map<String,String> params=new HashMap<>();
        params.put("favStatus", favStatus);
        params.put("code",custCode);
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+Constants.CUST_SET_FAV_STATUS;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"setFavStatus");
    }

    private void setRatings(){
        Map<String,String> params=new HashMap<>();
        params.put("ratings",""+selectedRatings);
        params.put("custCode",custCode);
        params.put("shopCode",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+Constants.CUST_SET_RATINGS;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"setRatings");
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {

        try {
            if (apiName.equals("setFavStatus")) {
                if (response.getBoolean("status")) {
                    if(isFav.equals("Y")){
                        isFav = "N";
                        imageFav.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    }else{
                        isFav = "Y";
                        imageFav.setImageResource(R.drawable.ic_favorite_black_24dp);
                    }

                }
            }else if (apiName.equals("setRatings")) {
                if (response.getBoolean("status")) {
                    ratings = selectedRatings;
                    //alertDialog.dismiss();
                }
            }else if (apiName.equals("customerSaleData")) {
                if (response.getBoolean("status")) {
                    if(response.getString("result") == null ||
                            response.getString("result").equals("null")){
                        setMonthlyBar();
                    }else{
                        float totalSale = 0;
                        JSONArray dataArray = response.getJSONArray("result");
                        JSONObject jsonObject = null;
                        int len = dataArray.length();
                        double maxValue = 0;
                        for (int i = 0; i < len; i++) {
                            jsonObject = dataArray.getJSONObject(i);
                            totalSale = totalSale + (float) jsonObject.getDouble("amount");
                            if(maxValue < jsonObject.getDouble("amount")){
                                maxValue = jsonObject.getDouble("amount");
                            }
                            updateMonthlySaleList(Utility.parseMonth(jsonObject.getString("orderDate"),
                                    "yyyy-MM-dd HH:mm:ss"), jsonObject.getInt("amount"));
                        }

                        textTotalSale.setText(Utility.numberFormat(totalSale));
                        if(maxValue == 0d){
                            ((MonthlyGraphAdapter) monthlyGraphAdapter).setTotalTarget(50000);
                        }else{
                            ((MonthlyGraphAdapter) monthlyGraphAdapter).setTotalTarget((float) maxValue);
                        }

                        if(len == 0){
                            setNullMonthlyBar();
                        }else{
                            setMonthlyBar();
                        }
                    }
                }
            }else if (apiName.equals("customerPreOrder")) {
                if (response.getBoolean("status")) {
                    JSONArray dataArray = response.getJSONArray("result");
                    JSONObject jsonObject = null;
                    int len = dataArray.length();
                    OrderItem orderItem = null;
                    for (int i = 0; i < len; i++) {
                        jsonObject = dataArray.getJSONObject(i);
                        orderItem = new OrderItem();
                        orderItem.setType(2);
                        orderItem.setId(jsonObject.getString("orderId"));
                        orderItem.setStatus(jsonObject.getString("orderStatus"));
                        orderItem.setDateTime("13:32, Nov, 09, 2018");
                        orderItem.setDateTime(Utility.parseDate(jsonObject.getString("orderDate")
                                ,"yyyy-MM-dd HH:mm:ss","HH:mm, MMM, dd, yyyy"));
                       // orderItem.setRating(4);
                        orderItem.setCustomerName(jsonObject.getString("custName"));
                        orderItem.setMobile(jsonObject.getString("mobileNo"));
                        orderItem.setAmount(jsonObject.getInt("toalAmount"));
                        orderItem.setDeliveryType(jsonObject.getString("orderDeliveryMode"));
                        orderItem.setLocalImage(R.drawable.thumb_12);
                        orderItemList.add(orderItem);
                    }

                    if(len == 0){
                        btnLoadMorePreOrders.setVisibility(View.GONE);
                    }else{
                        if(len == limit){
                            btnLoadMorePreOrders.setVisibility(View.VISIBLE);
                        }else{
                            btnLoadMorePreOrders.setVisibility(View.GONE);
                        }
                        orderAdapter.notifyDataSetChanged();
                    }
                }
            }else if (apiName.equals("customerRatings")) {
                if (response.getBoolean("status")) {
                   float ratings = (float)response.getDouble("result");
                   textRatings.setText(String.format("%.01f",ratings));
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
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

    public int getSaleData(String monthName){
        int value = 0;
        for(Bar bar : customerSaleList){
            if(bar.getName().equals(monthName)){
                value = bar.getSaleValue();
                break;
            }
        }

        return value;
    }

    public void initMonthlySaleList(){
        Calendar calendarTemp =Calendar.getInstance();
        customerSaleList.clear();
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
            customerSaleList.add(bar);
        }
    }

    public void updateMonthlySaleList(String monthName,int value){
        for(Bar bar : customerSaleList){
            if(bar.getName().equals(monthName)){
                bar.setSaleValue(value);
                break;
            }
        }
    }


    public String getMonth(int position){
        String[] monthInText={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

        return monthInText[position];
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;
        menu.getItem(0).setVisible(true);
        menu.getItem(1).setVisible(false);
       // menu.getItem(2).setVisible(true);
        if(isFav.equals("Y")){
            Log.i(TAG,"Customer is favourite: "+isFav);
            menu.getItem(0).setIcon(R.drawable.ic_favorite_black_24dp);
        }else{
            Log.i(TAG,"Customer is not favourite:: "+isFav);
            menu.getItem(0).setIcon(R.drawable.ic_favorite_border_black_24dp);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }else if (id == R.id.action_favourite) {
            setFavStatus();
            return true;
        }else if (id == android.R.id.home) {
            super.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openSetRatingDialog(){
        int view=R.layout.rating_dialog_layout;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setCancelable(false)
                .setView(view);

        // create alert dialog
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        final Button btnCancel=(Button) alertDialog.findViewById(R.id.btn_cancel);
        final Button btnSubmit=(Button) alertDialog.findViewById(R.id.btn_submit);
        final RatingBar ratingBar= alertDialog.findViewById(R.id.ratingBar);

        ratingBar.setRating(ratings);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedRatings = ratingBar.getRating();
                setRatings();
            }
        });

        alertDialog.show();
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

}
