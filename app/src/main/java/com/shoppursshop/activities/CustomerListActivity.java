package com.shoppursshop.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.shoppursshop.R;
import com.shoppursshop.activities.settings.AddCategoryActivity;
import com.shoppursshop.activities.settings.MyCategoriesActivity;
import com.shoppursshop.adapters.CustomerAdapter;
import com.shoppursshop.adapters.OrderAdapter;
import com.shoppursshop.fragments.BottomSearchFragment;
import com.shoppursshop.interfaces.MyItemClickListener;
import com.shoppursshop.interfaces.MyItemTypeClickListener;
import com.shoppursshop.models.HomeListItem;
import com.shoppursshop.models.MyCustomer;
import com.shoppursshop.models.MyHeader;
import com.shoppursshop.utilities.ConnectionDetector;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.Utility;

import org.bouncycastle.pqc.math.ntru.util.Util;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerListActivity extends NetworkBaseActivity implements MyItemTypeClickListener {

    private RecyclerView recyclerView,recyclerViewFav;
    private CustomerAdapter myItemAdapter,myItemAdapterFav;
    private List<Object> itemList,itemListFav;
    private TextView textViewError,textViewMyCustomerLabel,textViewFavCustomerLabel;
    private View viewSeparator;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private boolean isCustomerLoaded,isFavCustomerLoaded;
    private String mobile;

    private ImageView imageViewSearch;

    private BottomSearchFragment bottomSearchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("My Customers");

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.grey900), PorterDuff.Mode.SRC_ATOP);

        itemList = new ArrayList<>();
        itemListFav = new ArrayList<>();
        //setHeaders();

       /* MyCustomer myCustomer = new MyCustomer();
        myCustomer.setName("Sonam Kapoor");
        myCustomer.setMobile("9718181697");
        myCustomer.setAddress("M-180, Kendriya Vihar, Sector-56");
        myCustomer.setState("Haryana");
        myCustomer.setCity("Gurugram");
        myCustomer.setLocalImage(R.drawable.author_1);
        itemList.add(myCustomer);

        myCustomer = new MyCustomer();
        myCustomer.setName("Katie Perry");
        myCustomer.setMobile("9718181698");
        myCustomer.setAddress("M-181, Kendriya Vihar, Sector-56");
        myCustomer.setState("Haryana");
        myCustomer.setCity("Gurugram");
        myCustomer.setLocalImage(R.drawable.author_2);
        itemList.add(myCustomer);

        myCustomer = new MyCustomer();
        myCustomer.setName("Himani Saraswat");
        myCustomer.setMobile("9718181699");
        myCustomer.setAddress("M-182, Kendriya Vihar, Sector-56");
        myCustomer.setState("Haryana");
        myCustomer.setCity("Gurugram");
        myCustomer.setLocalImage(R.drawable.author_3);
        itemList.add(myCustomer);

        myCustomer = new MyCustomer();
        myCustomer.setName("Miley Cirus");
        myCustomer.setMobile("9718181610");
        myCustomer.setAddress("M-183, Kendriya Vihar, Sector-56");
        myCustomer.setState("Haryana");
        myCustomer.setCity("Gurugram");
        myCustomer.setLocalImage(R.drawable.author_4);
        itemList.add(myCustomer);

        myCustomer = new MyCustomer();
        myCustomer.setName("Sachin Kumar");
        myCustomer.setMobile("9718181611");
        myCustomer.setAddress("M-184, Kendriya Vihar, Sector-56");
        myCustomer.setState("Haryana");
        myCustomer.setCity("Gurugram");
        myCustomer.setLocalImage(R.drawable.author_5);
        itemList.add(myCustomer);

        myCustomer = new MyCustomer();
        myCustomer.setName("Sumit Kumar");
        myCustomer.setMobile("9718181612");
        myCustomer.setAddress("M-185, Kendriya Vihar, Sector-56");
        myCustomer.setState("Haryana");
        myCustomer.setCity("Gurugram");
        myCustomer.setLocalImage(R.drawable.author_6);
        itemList.add(myCustomer);*/

        swipeRefreshLayout=findViewById(R.id.swipe_refresh);
        progressBar=findViewById(R.id.progress_bar);
        textViewError = findViewById(R.id.text_error);
        imageViewSearch = findViewById(R.id.image_search);
        textViewMyCustomerLabel = findViewById(R.id.text_customer_label);
        textViewFavCustomerLabel = findViewById(R.id.text_my_fav);
        recyclerView=findViewById(R.id.recycler_view_my_customers);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        // staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
      /*  int resId = R.anim.layout_animation_slide_from_bottom;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        recyclerView.setLayoutAnimation(animation);*/
        myItemAdapter=new CustomerAdapter(this,itemList,"customerList");
        myItemAdapter.setMyItemClickListener(this);
        recyclerView.setAdapter(myItemAdapter);
        recyclerView.setNestedScrollingEnabled(false);

        recyclerViewFav=findViewById(R.id.recycler_view_fav);
        recyclerViewFav.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManagerFav=new LinearLayoutManager(this);
        // staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerViewFav.setLayoutManager(layoutManagerFav);
        recyclerViewFav.setItemAnimator(new DefaultItemAnimator());
      /*  int resId = R.anim.layout_animation_slide_from_bottom;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        recyclerView.setLayoutAnimation(animation);*/
        myItemAdapterFav=new CustomerAdapter(this,itemListFav,"customerList");
        myItemAdapterFav.setMyItemClickListener(this);
        recyclerViewFav.setAdapter(myItemAdapterFav);
        recyclerViewFav.setNestedScrollingEnabled(false);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                getItemList();
            }
        });

        imageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSearchFragment = new BottomSearchFragment();
                bottomSearchFragment.setCallingActivityName("customerList");
                bottomSearchFragment.show(getSupportFragmentManager(), "Search Customer Bottom Sheet");
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerListActivity.this, AddCustomerActivity.class);
                startActivityForResult(intent,3);
            }
        });

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.yellow500));
        }*/

        initFooter(this,2);
    }

    @Override
    public void onResume(){
        super.onResume();

        if(bottomSearchFragment != null){
            bottomSearchFragment.dismiss();
        }

        if (ConnectionDetector.isNetworkAvailable(this)){
            getItemList();
        }
    }

    private void setHeaders(){
        MyHeader myItem = new MyHeader();
        myItem.setTitle("Customers");
        myItem.setDesc("Stores Customers");
        myItem.setType(0);
        itemList.add(myItem);

        myItem = new MyHeader();
        myItem.setTitle("My Favourite Customers");
        myItem.setType(1);
        itemList.add(myItem);
    }

    private void getItemList(){
        Map<String,String> params=new HashMap<>();
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+Constants.CUSTOMER_LIST;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"customerList");
    }

    private void getFavItemList(){
        Map<String,String> params=new HashMap<>();
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+Constants.CUSTOMER_LIST;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"customerFavList");
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {

        try {
            if (apiName.equals("customerList")) {
                isCustomerLoaded = true;
                if (response.getBoolean("status")) {
                    JSONArray dataArray = response.getJSONArray("result");
                    JSONObject jsonObject = null;
                    int len = dataArray.length();
                    MyCustomer myCustomer= null;
                    itemList.clear();
                    itemListFav.clear();
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
                        myCustomer.setState(jsonObject.getString("state"));
                        myCustomer.setCity(jsonObject.getString("city"));
                        myCustomer.setImage(jsonObject.getString("photo"));
                        myCustomer.setIsFav(jsonObject.getString("isFav"));
                        myCustomer.setRatings((float)jsonObject.getDouble("ratings"));
                        myCustomer.setLocalImage(R.drawable.author_1);
                        if(myCustomer.getIsFav().equals("Y")){
                            itemListFav.add(myCustomer);
                        }else{
                            itemList.add(myCustomer);
                        }

                    }

                    if(itemList.size() == 0){
                        showNoData(true);
                    }else{
                        showNoData(false);
                        myItemAdapter.notifyDataSetChanged();
                    }

                    if(itemListFav.size() == 0){
                        showNoFavData(true);
                    }else{
                        showNoFavData(false);
                        myItemAdapterFav.notifyDataSetChanged();
                    }
                }
            }else if (apiName.equals("customerFavList")) {
                isFavCustomerLoaded = true;
                if (response.getBoolean("status")) {
                    JSONArray dataArray = response.getJSONArray("result");
                    JSONObject jsonObject = null;
                    int len = dataArray.length();
                    MyCustomer myCustomer= null;
                    itemListFav.clear();
                    //setHeaders();
                    for (int i = 0; i < len; i++) {
                        jsonObject = dataArray.getJSONObject(i);
                        myCustomer = new MyCustomer();
                        myCustomer.setName(jsonObject.getString("name"));
                        myCustomer.setMobile(jsonObject.getString("mobileNo"));
                        myCustomer.setEmail(jsonObject.getString("email"));
                        myCustomer.setImage(jsonObject.getString("photo"));
                        myCustomer.setLocalImage(R.drawable.author_1);
                        itemListFav.add(myCustomer);
                    }
                }

                checkLoading();
            }
        }catch (JSONException e) {
            checkLoading();
            e.printStackTrace();
        }
    }

    @Override
    public void onServerErrorResponse(VolleyError error, String apiName) {
        if(apiName.equals("customerList")){
            isCustomerLoaded = true;
        }else{
            isFavCustomerLoaded = true;
        }
       checkLoading();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            bottomSearchFragment = new BottomSearchFragment();
            bottomSearchFragment.setCallingActivityName("customerList");
            bottomSearchFragment.show(getSupportFragmentManager(), "Search Customer Bottom Sheet");
            return true;
        }else if (id == R.id.action_favourite) {
            return true;
        }else if (id == android.R.id.home) {
            super.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showNoData(boolean show){
        if(show){
            recyclerView.setVisibility(View.GONE);
            textViewMyCustomerLabel.setVisibility(View.GONE);
           // viewSeparator.setVisibility(View.GONE);
        }else{
            recyclerView.setVisibility(View.VISIBLE);
            textViewMyCustomerLabel.setVisibility(View.VISIBLE);
            textViewError.setVisibility(View.GONE);
        }

        checkShowAllData();
    }

    private void showNoFavData(boolean show){
        if(show){
            recyclerViewFav.setVisibility(View.GONE);
            textViewFavCustomerLabel.setVisibility(View.GONE);
        }else{
            recyclerViewFav.setVisibility(View.VISIBLE);
            textViewError.setVisibility(View.GONE);
            textViewFavCustomerLabel.setVisibility(View.VISIBLE);
        }

        checkShowAllData();
    }

    private void checkShowAllData(){
        if(recyclerView.getVisibility() == View.GONE && recyclerViewFav.getVisibility() == View.GONE){
            textViewError.setVisibility(View.VISIBLE);
        }else{
            textViewError.setVisibility(View.GONE);
        }
    }

    private void checkLoading(){
        if(isCustomerLoaded && isFavCustomerLoaded){
            progressDialog.dismiss();
        }
    }

    private void showProgressBar(boolean show){
        if(show){
            recyclerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            textViewError.setVisibility(View.GONE);
        }else{
            recyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClicked(int position,int type) {
        MyCustomer customer = null;
        if(type == 1 || type == 2){
            customer = (MyCustomer)itemListFav.get(position);
            if(type == 1){
                makeCall(customer.getMobile());
            }else{
                messageCustomer(customer.getMobile());
            }
        }else{
            customer = (MyCustomer)itemList.get(position);
            if(type == 3){
                makeCall(customer.getMobile());
            }else{
                messageCustomer(customer.getMobile());
            }
        }
        Log.i(TAG,"Customer clicked "+customer.getName());
    }


    public void makeCall(String mobile){
        this.mobile = mobile;
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+mobile));
        if(Utility.verifyCallPhonePermissions(this))
            startActivity(callIntent);
    }

    public void messageCustomer(String mobile){
        openWhatsApp(mobile);
       /* this.mobile = mobile;
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
            String sAux = "\n Download "+getResources().getString(R.string.app_name)+" app from below link \n\n";
            sAux = sAux + "https://app.box.com/s/ky40pmzmzuf0e5aiifpan0illv1pavou \n\n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "Choose one"));
        } catch(Exception e) {
            //e.toString();
        }*/
    }

    public void openWhatsApp(String mobile){
        try {
            mobile = "91"+mobile;
            String text = "This is a test";// Replace with your message.
         //   String toNumber = "xxxxxxxxxx"; // Replace with mobile phone number without +Sign or leading zeros, but with country code
            //Suppose your country is India and your phone number is “xxxxxxxxxx”, then you need to send “91xxxxxxxxxx”.
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+mobile +"&text="));
            startActivity(intent);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //mLocationPermissionGranted = false;

        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_CALL_PHONE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makeCall(mobile);

                }
                break;
        }

    }

}
