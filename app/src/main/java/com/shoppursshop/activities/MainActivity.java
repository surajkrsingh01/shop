package com.shoppursshop.activities;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.shoppursshop.R;
import com.shoppursshop.adapters.MyItemAdapter;
import com.shoppursshop.adapters.OrderAdapter;
import com.shoppursshop.interfaces.MyImageClickListener;
import com.shoppursshop.models.HomeListItem;
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

public class MainActivity extends NetworkBaseActivity implements MyImageClickListener {

    private RecyclerView recyclerView;
    private OrderAdapter myItemAdapter;
    private List<Object> itemList;
    private TextView textViewError;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;

    private Button btnUpdateStock,btnLoadMore;
    private FloatingActionButton fabNewOrder;

    private CoordinatorLayout coordinatorLayout;
    private NestedScrollView nestedScrollView;

    private float MIN_WIDTH = 200,MIN_HEIGHT = 230,MAX_WIDTH = 200,MAX_HEIGHT = 290;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        Log.i(TAG,"available memory "+ activityManager.getMemoryClass());

        itemList = new ArrayList<>();
        HomeListItem myItem = new HomeListItem();
       // myItem.setTitle("Sunday 16 December, 2018");
        myItem.setTitle(Utility.getTimeStamp("EEE dd MMMM, yyyy"));
        myItem.setDesc("Today Orders");
        myItem.setType(0);
        itemList.add(myItem);

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

        btnUpdateStock = findViewById(R.id.btn_update_stock);
        btnUpdateStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,UpdateStockActivity.class);
                startActivity(intent);
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
            if(!sharedPreferences.getBoolean(Constants.IS_TOKEN_SAVED,false) &&
            !TextUtils.isEmpty(sharedPreferences.getString(Constants.TOKEN,""))){
                saveFcmToken();
            }
        }else{
            showNoNetwork(true);
        }


    }

    private void getItemList(){
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

    @Override
    public void onResume(){
        super.onResume();

        editor.putInt("orderPosition",-1);
        editor.putString("type","");
        editor.putString("orderStatus","");
        editor.commit();

        int position = sharedPreferences.getInt("orderPosition",-1);

        Log.i(TAG,"position "+position);
        if(position > -1){
            String type = sharedPreferences.getString("type","");
            String status = sharedPreferences.getString("orderStatus","");
            Log.i(TAG,"type "+type);
            Log.i(TAG,"status "+status);
            if(type.equals("today")){
                ((OrderItem)itemList.get(position)).setStatus(status);
                myItemAdapter.notifyItemChanged(position);
            }

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

                    if(itemList.size() == 1){
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
                if (response.getBoolean("status")) {
                    editor.putBoolean(Constants.IS_TOKEN_SAVED,true);
                    editor.commit();
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
        itemList.add(myItem);
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
}
