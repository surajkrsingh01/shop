package com.shoppursshop.activities.product;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.shoppursshop.R;
import com.shoppursshop.activities.base.NetworkBaseActivity;
import com.shoppursshop.activities.settings.SettingActivity;
import com.shoppursshop.adapters.ProductAdapter;
import com.shoppursshop.interfaces.MyImageClickListener;
import com.shoppursshop.interfaces.MyItemClickListener;
import com.shoppursshop.models.MyProductItem;
import com.shoppursshop.utilities.ConnectionDetector;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.DialogAndToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyProductListActivity extends NetworkBaseActivity implements MyItemClickListener, MyImageClickListener {

    private RecyclerView recyclerView;
    private List<Object> itemList;
    private ProductAdapter itemAdapter;
    private int counter,position;
    private Menu menu;
    private TextView tv_top_parent,tvError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_product_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setToolbarTheme();

        itemList = new ArrayList<>();
        tvError=findViewById(R.id.text_error);
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        final RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        // staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        itemAdapter=new ProductAdapter(this,itemList,"syncedProductList");
        itemAdapter.setMyImageClickListener(this);
        itemAdapter.setFlag("syncedProductList");
        itemAdapter.setSubCatName("");
        itemAdapter.setMyItemClickListener(this);
        recyclerView.setAdapter(itemAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(isScroll){
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    Log.i(TAG,"visible "+visibleItemCount+" total "+totalItemCount);
                    pastVisibleItems = ((LinearLayoutManager)layoutManager).findLastVisibleItemPosition();
                    Log.i(TAG,"total visible "+(visibleItemCount+pastVisibleItems));

                    if (!loading) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            loading = true;
                            offset = limit + offset;
                            getProductList();
                        }
                    }
                }
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyProductListActivity.this, SyncProductActivity.class);
                startActivityForResult(intent,2);
            }
        });
        tv_top_parent = findViewById(R.id.text_left_label);
        tv_top_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyProductListActivity.this, SettingActivity.class));
                finish();
            }
        });

        if(ConnectionDetector.isNetworkAvailable(this)){
            getProductList();
        }else{
            showError(getResources().getString(R.string.no_internet));
        }
    }

    private void getProductList(){
        loading = true;
        Map<String,String> params=new HashMap<>();
        params.put("limit",""+limit);
        params.put("offset",""+offset);
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+Constants.GET_SYNCED_PRODUCTS;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"productList");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i(TAG,"Result received");

        if (requestCode == 2){
            if(data != null){
               offset = 0;
               itemList.clear();
               getProductList();
            }
        }
    }

    @Override
    public void onItemClicked(int position) {
        this.position = position;
        showMyBothDialog("Your action will delete the products and affect your stock control. Do you wish to continue?","Cancel","Ok");

    }

    @Override
    public void onDialogPositiveClicked(){
        MyProductItem item = (MyProductItem) itemList.get(position);
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        StringBuilder sb = new StringBuilder();
        try {
            sb.append(item.getProdCode());
            String prodIds = sb.toString();
            //catIds = catIds.substring(0,catIds.length()-1);
            jsonObject.put("prodIds",prodIds);
            jsonObject.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
            jsonObject.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
            jsonObject.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        }catch (JSONException e){
            e.printStackTrace();
        }

        String url=getResources().getString(R.string.url)+Constants.DELETE_PRODUCTS;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,jsonObject,"deleteProducts");
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {
        Log.d(TAG, response.toString());

        try {
            if(apiName.equals("deleteProducts")){

                if(response.getBoolean("status")){
                    MyProductItem item = (MyProductItem) itemList.get(position);
                    dbHelper.deleteProductByCode(item.getProdCode());
                    itemList.remove(position);
                    itemAdapter.notifyItemRemoved(position);
                }else{
                    DialogAndToast.showDialog(response.getString("message"), MyProductListActivity.this);
                }
            }else if(apiName.equals("productList")){

                if(response.getBoolean("status")){
                    JSONArray productArray = response.getJSONArray("result");
                    JSONObject jsonObject =null;
                    MyProductItem productItem = null;
                    int len = productArray.length();
                    for(int i=0; i<len; i++){
                        jsonObject = productArray.getJSONObject(i);
                        productItem = dbHelper.getProductDetailsByCode(jsonObject.getString("prodCode"));

                        if(productItem != null)
                        itemList.add(productItem);
                    }

                    if(itemList.size() == 0){
                        showError("Please click on + icon to add products.");
                    }else{
                        tvError.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        if(len < limit){
                            isScroll = false;
                        }
                        if(len > 0){
                            if(offset == 0){
                                itemAdapter.notifyDataSetChanged();
                            }else{
                                recyclerView.post(new Runnable() {
                                    public void run() {
                                        itemAdapter.notifyItemRangeInserted(offset,limit);
                                        loading = false;
                                    }
                                });
                                Log.d(TAG, "NEXT ITEMS LOADED");
                            }
                        }else{
                            Log.d(TAG, "NO ITEMS FOUND");
                        }
                    }

                }else{
                    DialogAndToast.showDialog(response.getString("message"), MyProductListActivity.this);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showError(String message){
        tvError.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        tvError.setText(message);
    }

    @Override
    public void onImageClicked(int position, int type, View view) {
        MyProductItem item = (MyProductItem) itemList.get(position);
        showImageDialog(item.getProdImage1(),view);
    }
}
