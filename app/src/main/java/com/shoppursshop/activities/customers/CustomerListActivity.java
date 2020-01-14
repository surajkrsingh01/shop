package com.shoppursshop.activities.customers;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shoppursshop.R;
import com.shoppursshop.activities.base.NetworkBaseActivity;
import com.shoppursshop.adapters.CustomerAdapter;
import com.shoppursshop.fragments.BottomSearchFragment;
import com.shoppursshop.interfaces.MyImageClickListener;
import com.shoppursshop.interfaces.MyItemTypeClickListener;
import com.shoppursshop.models.MyCustomer;
import com.shoppursshop.models.MyHeader;
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

public class CustomerListActivity extends NetworkBaseActivity implements MyItemTypeClickListener, MyImageClickListener {

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

        Log.i(TAG,"no fav list "+itemList.size()+" fav size "+itemListFav.size());

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
        myItemAdapter.setMyImageClickListener(this);
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
        myItemAdapterFav.setMyImageClickListener(this);
        recyclerViewFav.setAdapter(myItemAdapterFav);
        recyclerViewFav.setNestedScrollingEnabled(false);

        /*if(itemList.size() == 0){
            showNoData(true);
        }else{
            showNoData(false);
        }

        if(itemListFav.size() == 0){
            showNoFavData(true);
        }else{
            showNoFavData(false);
        }*/

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                if (ConnectionDetector.isNetworkAvailable(CustomerListActivity.this)){
                    getItemList();
                }
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

        List<Object> itemNewList = dbHelper.getCustomerList("N",limit,offset);
        List<Object> itemNewListFav = dbHelper.getFavCustomerList("Y",limit,offset);
        itemList.clear();
        itemListFav.clear();
        itemList.addAll(itemNewList);
        itemListFav.addAll(itemNewListFav);

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

        if(itemList.size() == 0 && itemListFav.size() == 0){
            if (ConnectionDetector.isNetworkAvailable(this)){
                getItemList();
            }
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

    private void refreshCustomerList(){
        JSONArray codeArray = new JSONArray();
        JSONObject dataObject = null,codeObject = null;
        Map<String,String> params=new HashMap<>();
        MyCustomer myCustomer = null;
        for(Object ob : itemList){
            myCustomer = (MyCustomer)ob;
            dataObject = new JSONObject();
            try {
                dataObject.put("custCode",myCustomer.getCode());
                codeArray.put(dataObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        for(Object ob : itemListFav){
            myCustomer = (MyCustomer)ob;
            dataObject = new JSONObject();
            try {
                dataObject.put("custCode",myCustomer.getCode());
                codeArray.put(dataObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        dataObject = new JSONObject();
        try {
            dataObject.put("codeArray",codeArray);
            dataObject.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
            dataObject.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
            dataObject.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url=getResources().getString(R.string.url)+Constants.REFRESH_CUSTOMER_LIST;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,dataObject,"refreshCustomerList");
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {

        try {
            if (apiName.equals("customerList") || apiName.equals("refreshCustomerList")) {
                isCustomerLoaded = true;
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

                        if(!myCustomer.getIsFav().equals("Y")){
                            myCustomer.setIsFav("N");
                        }

                        if(!dbHelper.checkCustomerExistInCart(jsonObject.getString("code"))){
                            if(myCustomer.getIsFav().equals("Y")){
                                itemListFav.add(myCustomer);
                            }else{
                                myCustomer.setIsFav("N");
                                itemList.add(myCustomer);
                            }
                            dbHelper.addCustomerInfo(myCustomer,Utility.getTimeStamp(),Utility.getTimeStamp());
                        }else{
                            dbHelper.updatedCustomerInfo(myCustomer,Utility.getTimeStamp());
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
        if(type == 1 || type == 2 || type == 5){
            customer = (MyCustomer)itemListFav.get(position);
            if(type == 1){
                makeCall(customer.getMobile());
            }else if(type == 2){
                messageCustomer(customer.getMobile());
            }else{
                if(customer.getLatitude() == null || customer.getLatitude().equals("0.0")
                        || customer.getLatitude().equals("")|| customer.getLatitude().equals("null")){
                    DialogAndToast.showDialog("Location is not available",this);
                }else{
                    startCustomerAddressActivity(customer);
                }
            }
        }else if(type == 3 || type == 4|| type == 6){
            customer = (MyCustomer)itemList.get(position);
            if(type == 3){
                makeCall(customer.getMobile());
            }else if(type == 4){
                messageCustomer(customer.getMobile());
            }else{
                if(customer.getLatitude() == null || customer.getLatitude().equals("0.0")
                        || customer.getLatitude().equals("")|| customer.getLatitude().equals("null")){
                    DialogAndToast.showDialog("Location is not available",this);
                }else{
                    startCustomerAddressActivity(customer);
                }
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

    public void startCustomerAddressActivity(MyCustomer item){
        Intent intent = new Intent(CustomerListActivity.this, CustomerAddressActivity.class);
        intent.putExtra("name",item.getName());
        intent.putExtra("address",item.getAddress());
        intent.putExtra("mobile",item.getMobile());
        intent.putExtra("country",item.getCountry());
        intent.putExtra("stateCity",item.getState()+", "+item.getCity());
        intent.putExtra("locality",item.getLocality());
        intent.putExtra("longitude",item.getLongitude());
        intent.putExtra("latitude",item.getLatitude());
        intent.putExtra("customerImage",item.getImage());
        intent.putExtra("isFav",item.getIsFav());
        intent.putExtra("custCode",item.getCode());
        intent.putExtra("custId",item.getId());
        intent.putExtra("ratings",item.getRatings());
        startActivity(intent);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 3){
            if(data != null){
                MyCustomer myCustomer = (MyCustomer) data.getSerializableExtra("myCustomer");
                if(myCustomer.getIsFav().equals("Y")){
                    itemListFav.add(myCustomer);
                    myItemAdapterFav.notifyDataSetChanged();
                }else{
                    myCustomer.setIsFav("N");
                    itemList.add(myCustomer);
                    myItemAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void onImageClicked(int position, int type, View view) {
        MyCustomer customer = null;
        if(type == 7){
            customer = (MyCustomer)itemListFav.get(position);
            showImageDialog(customer.getImage(),view);
        }else if(type == 8){
            customer = (MyCustomer)itemList.get(position);
            showImageDialog(customer.getImage(),view);
        }
    }
}
