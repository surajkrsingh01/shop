package com.shoppursshop.activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.crashlytics.android.Crashlytics;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.widget.PopupMenu;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.shoppursshop.R;
import com.shoppursshop.activities.settings.RlevelAndExpiredProductActivity;
import com.shoppursshop.adapters.MyItemAdapter;
import com.shoppursshop.adapters.OrderAdapter;
import com.shoppursshop.interfaces.MyImageClickListener;
import com.shoppursshop.models.HomeListItem;
import com.shoppursshop.models.OrderItem;
import com.shoppursshop.utilities.ConnectionDetector;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.DialogAndToast;
import com.shoppursshop.utilities.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends NetworkBaseActivity implements MyImageClickListener {

    private RecyclerView recyclerView;
    private OrderAdapter myItemAdapter;
    private List<Object> itemList;
    private TextView textViewError;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private FloatingActionButton fabNewOrder;
    private TextView textHeader,textDesc,tv_expire_product,tv_reorder_product;
    private LinearLayout ll_rl_and_expire_layout;
    private RelativeLayout rl_expire_layout,rl_reorder_layout;


    private CoordinatorLayout coordinatorLayout;
    private NestedScrollView nestedScrollView;

    private ImageView ivMenu;
    private boolean openingStore,refresh;

    private float MIN_WIDTH = 200,MIN_HEIGHT = 230,MAX_WIDTH = 200,MAX_HEIGHT = 290;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Crashlytics.getInstance().crash(); // Force a crash


        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        Log.i(TAG,"available memory "+ activityManager.getMemoryClass());

        itemList = new ArrayList<>();
        HomeListItem myItem = new HomeListItem();
       // myItem.setTitle("Sunday 16 December, 2018");
        myItem.setTitle(Utility.getTimeStamp("EEE dd MMMM, yyyy"));
        myItem.setDesc("Today Orders");
        myItem.setType(0);
       // itemList.add(myItem);

        textHeader=findViewById(R.id.text_small_desc);
        textDesc=findViewById(R.id.text_desc);
        ll_rl_and_expire_layout=findViewById(R.id.ll_rl_and_expire_layout);
        rl_expire_layout=findViewById(R.id.rl_expire_layout);
        rl_reorder_layout=findViewById(R.id.rl_reorder_layout);
        tv_expire_product=findViewById(R.id.tv_expire_product);
        tv_reorder_product=findViewById(R.id.tv_reorder_product);
        swipeRefreshLayout=findViewById(R.id.swipe_refresh);
        progressBar=findViewById(R.id.progress_bar);
        textViewError = findViewById(R.id.text_error);
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        final RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        myItemAdapter=new OrderAdapter(this,itemList,"orderList");
        myItemAdapter.setMyImageClickListener(this);
        recyclerView.setAdapter(myItemAdapter);

        textHeader.setText(Utility.getTimeStamp("EEE dd MMMM, yyyy"));
        textDesc.setText("Today Orders");

        Calendar calendar = Calendar.getInstance();
        final String startDate = Utility.parseDate(calendar,"yyyy-MM-dd HH:mm:ss");
        calendar.add(Calendar.DATE,7);
        final String endDate = Utility.parseDate(calendar,"yyyy-MM-dd HH:mm:ss");
        int expiredSize = dbHelper.getExpiredProductSize(startDate,endDate);
        int reorderSize = dbHelper.getReorderedProductSize();

        if(expiredSize > 0 || reorderSize > 0){
            ll_rl_and_expire_layout.setVisibility(View.VISIBLE);
            if(expiredSize > 0){
                tv_expire_product.setText(""+expiredSize);
            }else{
                rl_expire_layout.setVisibility(View.GONE);
            }

            if(reorderSize > 0){
                tv_reorder_product.setText(""+reorderSize);
            }else{
                rl_reorder_layout.setVisibility(View.GONE);
            }

        }

        rl_expire_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RlevelAndExpiredProductActivity.class);
                intent.putExtra("flag","home");
                intent.putExtra("type","expire");
                intent.putExtra("startDate",startDate.split(" ")[0]);
                intent.putExtra("endDate",endDate.split(" ")[0]);
                startActivity(intent);
            }
        });

        rl_reorder_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RlevelAndExpiredProductActivity.class);
                intent.putExtra("flag","home");
                intent.putExtra("type","reorder");
                startActivity(intent);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isScroll) {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    Log.i(TAG,"visible "+visibleItemCount+" total "+totalItemCount);
                    pastVisibleItems = ((LinearLayoutManager)layoutManager).findLastVisibleItemPosition();
                    Log.i(TAG,"past visible "+(pastVisibleItems));

                    if (!loading) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            loading = true;
                            offset = limit + offset;
                            getItemList();
                        }
                    }

                }
            }
        });


        fabNewOrder = findViewById(R.id.fab_new_order);
        fabNewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,CustomerInfoActivity.class);
                startActivity(intent);
            }
        });

        ivMenu = findViewById(R.id.iv_menu);
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG,"menu cliecked");
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, ivMenu);
                getMenuInflater().inflate(R.menu.home_menu, popupMenu.getMenu());
                if(sharedPreferences.getBoolean(Constants.STORE_OPEN_STATUS,false)){
                    popupMenu.getMenu().getItem(0).setVisible(false);
                    popupMenu.getMenu().getItem(1).setVisible(true);
                }else{
                    popupMenu.getMenu().getItem(0).setVisible(true);
                    popupMenu.getMenu().getItem(1).setVisible(false);
                }
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals("Open Store")){
                            changeStoreStatus("1");
                        }else if(item.getTitle().equals("Close Store")){
                            changeStoreStatus("0");
                        }else if(item.getTitle().equals("Update Stock")){
                            Intent intent = new Intent(MainActivity.this,UpdateStockActivity.class);
                            startActivity(intent);
                        }

                        return true;
                    }
                });

                popupMenu.show();
            }
        });

        initFooter(this,0);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                if (ConnectionDetector.isNetworkAvailable(MainActivity.this)){
                    showNoNetwork(false);
                    offset = 0;
                    resetItemList();
                    getItemList();
                }else{
                    showNoNetwork(true);
                }

            }
        });

        limit = 5;
        offset = 0;

        if (ConnectionDetector.isNetworkAvailable(this)){
            getItemList();
        }else{
            showNoNetwork(true);
        }

    }

    private void getItemList(){
        loading = true;
        Map<String,String> params=new HashMap<>();
        params.put("limit", ""+limit);
        params.put("offset",""+offset);
        params.put("code",sharedPreferences.getString(Constants.SHOP_CODE,""));
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+Constants.GET_PENDING_ORDERS;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"orders");
    }

    private void saveFcmToken(){
        Map<String,String> params=new HashMap<>();
        params.put("token",sharedPreferences.getString(Constants.FCM_TOKEN,""));
        params.put("userType",sharedPreferences.getString(Constants.USER_TYPE,""));
        params.put("mobile",sharedPreferences.getString(Constants.MOBILE_NO,""));
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+"/api/user/save_fcm_token";
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"saveFcmToken");
    }

    private void changeStoreStatus(String status){
        Map<String,String> params=new HashMap<>();
        params.put("status",status);
        params.put("mobile",sharedPreferences.getString(Constants.MOBILE_NO,""));
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+"/api/user/update_store_open_status";
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"changeOpenStatus");
    }

    private void generateFrequencyOrder(){
        Map<String,String> params=new HashMap<>();
        params.put("shopCode",sharedPreferences.getString(Constants.SHOP_CODE,""));
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.cust_url)+Constants.GENERATE_FREQUENCY_ORDER;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"generateFrequencyOrder");
    }

    @Override
    public void onResume(){
        super.onResume();

       /* editor.putInt("orderPosition",-1);
        editor.putString("type","");
        editor.putString("orderStatus","");
        editor.commit();*/

        int position = sharedPreferences.getInt("orderPosition",-1);

        Log.i(TAG,"position "+position);
        if(position > -1){
            String status = sharedPreferences.getString("orderStatus","");
            Log.i(TAG,"status "+status);
            ((OrderItem)itemList.get(position)).setStatus(status);
            myItemAdapter.notifyItemChanged(position);
            editor.putInt("orderPosition",-1);
            editor.putString("type","");
            editor.putString("orderStatus","");
            editor.commit();
        }

    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {

        try {
            if (apiName.equals("orders")) {
                if(refresh){
                    refresh = false;
                   // DialogAndToast.showDialog(getResources().getString(R.string.expiration_goods_notice),this);
                }else{
                    if(!sharedPreferences.getBoolean(Constants.IS_TOKEN_SAVED,false) &&
                            !TextUtils.isEmpty(sharedPreferences.getString(Constants.TOKEN,""))){
                        saveFcmToken();
                    }else{
                        if(!sharedPreferences.getBoolean(Constants.STORE_OPEN_STATUS,false)){
                            if(!sharedPreferences.getString(Constants.STORE_CLOSE_DATE,"").
                                    equals(Utility.getTimeStamp("yyyy-MM-dd"))){
                                openingStore = true;
                                showMyBothDialog("Open your store","Cancel","Open");
                            }else{
                                generateFrequencyOrder();
                            }

                        }else{
                            generateFrequencyOrder();
                        }
                    }
                }

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
                        if(jsonObject.has("deliveryLat")){
                            orderItem.setDeliveryLat(jsonObject.getString("deliveryLat"));
                            orderItem.setDeliveryLong(jsonObject.getString("deliveryLong"));
                        }
                        orderItem.setOrderImage(jsonObject.getString("orderImage"));
                        orderItem.setStatus(jsonObject.getString("orderStatus"));
                        orderItem.setOrderPayStatus(jsonObject.getString("oderPaymentStatus"));
                        orderItem.setLocalImage(R.drawable.thumb_12);

                        Log.i(TAG,"order date "+Utility.getTimeStamp("yyyy-MM-dd")+
                                " "+orderItem.getDateTime().split(" ")[0]);

                        if(Utility.getTimeStamp("yyyy-MM-dd").equals(orderItem.getDateTime().split(" ")[0]))
                        itemList.add(orderItem);
                        //else
                        //preItemList.add(orderItem);
                    }

                    if(itemList.size() == 0){
                        showNoData(true);
                    }else{
                        showNoData(false);
                        if(len < limit){
                            isScroll = false;
                        }
                        if(len > 0){
                            if(offset == 0){
                                myItemAdapter.notifyDataSetChanged();
                            }else{
                                recyclerView.post(new Runnable() {
                                    public void run() {
                                        myItemAdapter.notifyItemRangeInserted(offset,limit);
                                        loading = false;
                                    }
                                });
                                Log.d(TAG, "NEXT ITEMS LOADED");
                            }
                        }else{
                            Log.d(TAG, "NO ITEMS FOUND");
                        }
                    }
                }

            }else if (apiName.equals("saveFcmToken")) {
                if(!sharedPreferences.getBoolean(Constants.STORE_OPEN_STATUS,false)){
                    if(!sharedPreferences.getString(Constants.STORE_CLOSE_DATE,"").
                            equals(Utility.getTimeStamp("yyyy-MM-dd"))){
                        openingStore = true;
                        showMyBothDialog("Open your store","Cancel","Open");
                    }else{
                        generateFrequencyOrder();
                    }

                }else{
                    generateFrequencyOrder();
                }
                if (response.getBoolean("status")) {
                    editor.putBoolean(Constants.IS_TOKEN_SAVED,true);
                    editor.commit();
                }
            }else if (apiName.equals("changeOpenStatus")) {

                generateFrequencyOrder();

                if (response.getBoolean("status")) {
                    int status = response.getInt("result");
                    if(status == 1){
                        editor.putBoolean(Constants.STORE_OPEN_STATUS,true);
                    }else{
                        editor.putBoolean(Constants.STORE_OPEN_STATUS,false);
                        editor.putString(Constants.STORE_CLOSE_DATE,Utility.getTimeStamp("yyyy-MM-dd"));
                    }
                    editor.commit();
                    DialogAndToast.showDialog(response.getString("message"),this);
                }else{
                    DialogAndToast.showDialog(response.getString("message"),this);
                }
            }else if(apiName.equals("generateFrequencyOrder")){
                if (response.getBoolean("status")) {

                    if(!response.getString("result").equals("0")){
                        offset = 0;
                        resetItemList();
                        refresh = true;
                        getItemList();
                    }else{
                        //DialogAndToast.showDialog(getResources().getString(R.string.expiration_goods_notice),this);
                    }

                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void resetItemList(){
        itemList.clear();
        HomeListItem myItem = new HomeListItem();
        // myItem.setTitle("Sunday 16 December, 2018");
        myItem.setTitle(Utility.getTimeStamp("EEEE dd MMMM, yyyy"));
        myItem.setDesc("Today Orders");
        myItem.setType(0);
       // itemList.add(myItem);
    }

    private void runLayoutAnimation() {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_slide_from_bottom);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
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

        return super.onOptionsItemSelected(item);
    }

    private void showNoData(boolean show){
        if(show){
           // recyclerView.setVisibility(View.GONE);
            textViewError.setVisibility(View.VISIBLE);
        }else{
          //  recyclerView.setVisibility(View.VISIBLE);
            textViewError.setVisibility(View.GONE);
        }
    }

    private void showNoNetwork(boolean show){
        if(show){
            swipeRefreshLayout.setVisibility(View.GONE);
            textViewError.setVisibility(View.VISIBLE);
            textViewError.setText(getResources().getString(R.string.no_internet));
        }else{
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            textViewError.setVisibility(View.GONE);
        }
    }

    @Override
    public void onImageClicked(int position, int type, View view) {
        OrderItem orderItem = null;
        if(type == 1){
            orderItem = (OrderItem)itemList.get(position);
            showImageDialog(orderItem.getOrderImage(),view);
        }
    }

    @Override
    public void onDialogPositiveClicked(){
        if(openingStore){
            openingStore = false;
            changeStoreStatus("1");
        }
    }

    @Override
    public void onDialogNegativeClicked(){
        if(openingStore){
            openingStore =false;
            generateFrequencyOrder();
        }
    }
}
