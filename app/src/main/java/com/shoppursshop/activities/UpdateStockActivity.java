package com.shoppursshop.activities;

import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.shoppursshop.R;
import com.shoppursshop.adapters.UpdateStockAdapter;
import com.shoppursshop.fragments.BottomSearchFragment;
import com.shoppursshop.interfaces.MyImageClickListener;
import com.shoppursshop.interfaces.MyItemClickListener;
import com.shoppursshop.interfaces.MyItemTypeClickListener;
import com.shoppursshop.models.MyProductItem;
import com.shoppursshop.utilities.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateStockActivity extends NetworkBaseActivity implements MyItemClickListener, MyItemTypeClickListener, MyImageClickListener {

    private RecyclerView recyclerView;
    private UpdateStockAdapter myItemAdapter;
    private List<MyProductItem> itemList;

    private ImageView imageViewSearch;
    private LinearLayout linearLayoutScanCenter;
    private BottomSearchFragment bottomSearchFragment;
    private int type = 0, position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_stock);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();

    }

    private void init(){
         itemList = new ArrayList<>();
        linearLayoutScanCenter = findViewById(R.id.linear_action);
        imageViewSearch = findViewById(R.id.image_search);
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        final RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        myItemAdapter=new UpdateStockAdapter(this,itemList);
        myItemAdapter.setMyImageClickListener(this);
        myItemAdapter.setDarkTheme(isDarkTheme);
        myItemAdapter.setMyItemTypeClickListener(this);
        recyclerView.setAdapter(myItemAdapter);

        linearLayoutScanCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openScannar();
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openScannar();
            }
        });

        imageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSearchFragment = new BottomSearchFragment();
                bottomSearchFragment.setCallingActivityName("productList");
                Bundle bundle = new Bundle();
                bundle.putString("flag","searchCartProduct");
                bottomSearchFragment.setArguments(bundle);
                bottomSearchFragment.setMyItemClickListener(UpdateStockActivity.this);
                bottomSearchFragment.show(getSupportFragmentManager(), "Search Product Bottom Sheet");
            }
        });
    }

    private void openScannar(){
        Intent intent = new Intent(this,ScannarActivity.class);
        intent.putExtra("flag","scan");
        intent.putExtra("type","updateStock");
        startActivityForResult(intent,10);
    }

    private void updateStock(int quantity,int prodId){
        Map<String,String> params=new HashMap<>();
        params.put("quantity",""+quantity);
        params.put("prodId",""+prodId);
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+Constants.UPDATE_STOCK;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"updateStock");
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {

        try {
            if (apiName.equals("updateStock")) {
                if (response.getBoolean("status")) {
                    MyProductItem item = (MyProductItem) itemList.get(position);
                  if(type == 1){
                      dbHelper.setQoh(item.getProdId(),item.getQty());
                  }else{
                      dbHelper.setQoh(item.getProdId(),-item.getQty());
                  }
                    myItemAdapter.notifyItemChanged(position);
                }
            }
        }catch (JSONException error){
            error.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(requestCode == 10) {
            if (intent != null) {
                String rawValue = intent.getStringExtra("barCode");
                int id = dbHelper.checkBarCodeExist(rawValue);
                if(id == 0){
                    showMyDialog("Product does not exist in database.");
                }else{
                    MyProductItem myProductItem = dbHelper.getProductDetailsByBarCode(rawValue);
                    itemList.add(myProductItem);
                    myItemAdapter.notifyDataSetChanged();

                    if(itemList.size() == 1){
                        showData(true);
                    }
                }
                }

            }
    }

    @Override
    public void onItemClicked(int prodId) {

        Log.i(TAG,"item clicked "+prodId+" "+dbHelper.getBarCodesForCart(prodId).size());
        MyProductItem item = dbHelper.getProductDetails(prodId);
        itemList.add(item);
        myItemAdapter.notifyDataSetChanged();

        if(itemList.size() == 1){
          showData(true);
        }
    }

    @Override
    public void onItemClicked(int position, int type) {
        MyProductItem item = (MyProductItem) itemList.get(position);
        this.type = type;
        this.position = position;
        if(type == 1){
            // add to stock
            item.setQty(item.getQty() + 1);
            item.setProdQoh(item.getProdQoh() + 1);
            updateStock(item.getProdQoh(),item.getProdId());
        }else if(type == 2){
            // minus to stock
            if(item.getProdQoh() > 0){
                item.setQty(item.getQty() - 1);
                item.setProdQoh(item.getProdQoh() - 1);
                updateStock(item.getProdQoh(),item.getProdId());
            }
        }
    }

    @Override
    public void onImageClicked(int position, int type, View view) {
        MyProductItem item = (MyProductItem)itemList.get(position);
        showImageDialog(item.getProdImage1(),view);
    }

    private void showData(boolean show){
        if(show){
            recyclerView.setVisibility(View.VISIBLE);
            linearLayoutScanCenter.setVisibility(View.GONE);
        }else{
            recyclerView.setVisibility(View.GONE);
            linearLayoutScanCenter.setVisibility(View.VISIBLE);
        }
    }
}
