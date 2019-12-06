package com.shoppursshop.activities.settings;

import android.os.Bundle;

import com.android.volley.Request;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.shoppursshop.R;
import com.shoppursshop.activities.NetworkBaseActivity;
import com.shoppursshop.adapters.ProductAdapter;
import com.shoppursshop.interfaces.MyImageClickListener;
import com.shoppursshop.models.FrequencyProduct;
import com.shoppursshop.models.MyProductItem;
import com.shoppursshop.utilities.ConnectionDetector;
import com.shoppursshop.utilities.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FrequencyProductListActivity extends NetworkBaseActivity implements MyImageClickListener {

    private RecyclerView recyclerView;
    private ProductAdapter myItemAdapter;
    private List<Object> itemList;
    private TextView textViewError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frequency_product_list);

        initFooter(this,10);
        init();
    }

    private void init(){
        itemList = new ArrayList<>();
        textViewError = findViewById(R.id.text_error);
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        final RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        // staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        myItemAdapter=new ProductAdapter(this,itemList,"frequencyOrderProductList");
        myItemAdapter.setMyImageClickListener(this);
        myItemAdapter.setFlag("frequencyOrderProductList");
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
        params.put("code",getIntent().getStringExtra("custCode"));
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.cust_url)+Constants.GET_FREQUENCY_PRODUCT_LIST;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"productList");
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {

        try {
            if (apiName.equals("productList")) {
                if (response.getBoolean("status")) {
                    JSONArray dataArray = response.getJSONArray("result");
                    JSONObject dataObject = null,jsonObject = null;
                    JSONArray productArray = null;
                    int len = dataArray.length();
                    FrequencyProduct productItem= null;
                    for (int i = 0; i < len; i++) {
                        dataObject = dataArray.getJSONObject(i);
                        productArray = dataObject.getJSONArray("myProductList");
                        jsonObject = productArray.getJSONObject(0);
                        productItem = new FrequencyProduct();
                        productItem.setProdId(jsonObject.getInt("prodId"));
                        productItem.setProdName(jsonObject.getString("prodName"));
                        productItem.setProdCode(jsonObject.getString("prodCode"));
                        productItem.setProdDesc(jsonObject.getString("prodDesc"));
                        productItem.setProdCgst(Float.parseFloat(jsonObject.getString("prodCgst")));
                        productItem.setProdIgst(Float.parseFloat(jsonObject.getString("prodIgst")));
                        productItem.setProdSgst(Float.parseFloat(jsonObject.getString("prodSgst")));
                        productItem.setProdImage1(jsonObject.getString("prodImage1"));
                        productItem.setProdImage2(jsonObject.getString("prodImage2"));
                        productItem.setProdImage3(jsonObject.getString("prodImage3"));
                        productItem.setProdMrp(Float.parseFloat(jsonObject.getString("prodMrp")));
                        productItem.setProdSp(Float.parseFloat(jsonObject.getString("prodSp")));
                        productItem.setFrequency(jsonObject.getString("frequency"));
                        productItem.setFrequencyQty(jsonObject.getInt("frequencyQty"));
                        productItem.setFrequencyValue(jsonObject.getInt("frequencyValue"));
                        productItem.setFrequencyStartDate(jsonObject.getString("frequencyStartDate"));
                        productItem.setFrequencyEndDate(jsonObject.getString("frequencyEndDate"));
                        productItem.setNextOrderDate(jsonObject.getString("nextOrderDate"));
                        productItem.setLastOrderDate(jsonObject.getString("lastOrderDate"));
                        productItem.setStatus(jsonObject.getString("status"));
                        itemList.add(productItem);
                    }

                    if(itemList.size() == 0){
                        showNoData(true,"No data available");
                    }else{
                        showNoData(false,null);
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
                        // myItemAdapter.notifyDataSetChanged();
                    }
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onImageClicked(int position, int type, View view) {
        MyProductItem item = (MyProductItem) itemList.get(position);
        showImageDialog(item.getProdImage1(),view);
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
}
