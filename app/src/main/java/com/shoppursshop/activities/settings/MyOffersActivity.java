package com.shoppursshop.activities.settings;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.shoppursshop.R;
import com.shoppursshop.activities.NetworkBaseActivity;
import com.shoppursshop.adapters.OrderAdapter;
import com.shoppursshop.adapters.ShopOfferListAdapter;
import com.shoppursshop.database.DbHelper;
import com.shoppursshop.interfaces.MyItemTypeClickListener;
import com.shoppursshop.models.Coupon;
import com.shoppursshop.models.MyProduct;
import com.shoppursshop.models.MyProductItem;
import com.shoppursshop.models.OrderItem;
import com.shoppursshop.models.ProductComboDetails;
import com.shoppursshop.models.ProductComboOffer;
import com.shoppursshop.models.ProductDiscountOffer;
import com.shoppursshop.models.ShopOfferItem;
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

public class MyOffersActivity extends NetworkBaseActivity implements MyItemTypeClickListener {

    private RecyclerView recyclerView;
    private ShopOfferListAdapter myItemAdapter;
    private List<ShopOfferItem> itemList;
    private TextView textViewError,tv_top_parent, text_second_label;
    private FloatingActionButton fab_new_offer;
    private Button btnAll;
    private String flag;
    private boolean isAllShowing;
    private int changeType,position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_offers);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setToolbarTheme();
        init();
    }

    private void init(){
        flag = getIntent().getStringExtra("flag");
        itemList = new ArrayList<>();
        textViewError = findViewById(R.id.text_no_order);
        btnAll = findViewById(R.id.btn_all);
        recyclerView=findViewById(R.id.recycler_view);
        fab_new_offer = findViewById(R.id.fab_new_offer);
        fab_new_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyOffersActivity.this, CreateOfferActivity.class));
                //DialogAndToast.showToast("Creating New Offer ", MyOffersActivity.this);
            }
        });

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        myItemAdapter=new ShopOfferListAdapter(this,itemList);
        myItemAdapter.setMyItemTypeClickListener(this);
        recyclerView.setAdapter(myItemAdapter);

        tv_top_parent = findViewById(R.id.text_left_label);
        text_second_label = findViewById(R.id.text_second_label);

        tv_top_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyOffersActivity.this, SettingActivity.class));
                finish();
            }
        });

        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ConnectionDetector.isNetworkAvailable(MyOffersActivity.this)){
                    isAllShowing = true;
                    getItemList();
                }else{
                    DialogAndToast.showDialog(getResources().getString(R.string.no_internet),MyOffersActivity.this);
                }
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        if (ConnectionDetector.isNetworkAvailable(this)){
            getActiveItemList();
        }else{
            showNoNetwork(true);
        }
    }

    //ems simulation iq


    private void getActiveItemList(){
        Map<String,String> params=new HashMap<>();
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+Constants.GET_ACTIVE_PRODUCT_OFFER;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"offerList");
    }

    private void getItemList(){
        Map<String,String> params=new HashMap<>();
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+Constants.GET_ALL_PRODUCT_OFFER;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"offerList");
    }

    private void changeStatus(String id,String status,String type){
        Map<String,String> params=new HashMap<>();
        params.put("id",id);
        params.put("status",status);
        params.put("userName",sharedPreferences.getString(Constants.FULL_NAME,""));
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url = "",api = "";
        if(type.equals("free")){
            url=getResources().getString(R.string.url)+Constants.CHANGE_FREE_PRODUCT_OFFER;
        }else if(type.equals("combo")){
            url=getResources().getString(R.string.url)+Constants.CHANGE_COMBO_PRODUCT_OFFER;
        }else if(type.equals("price")){
            url=getResources().getString(R.string.url)+Constants.CHANGE_PRODUCT_PRICE_OFFER;
        }else if(type.equals("coupon")){
            url=getResources().getString(R.string.url)+Constants.CHANGE_COUPON_OFFER;
        }

        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"statusChangeOffer");
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {

        try {
            if (apiName.equals("offerList")) {
                if (response.getBoolean("status")) {
                    itemList.clear();
                    JSONObject jsonObject = response.getJSONObject("result");
                    JSONArray freeArray = jsonObject.getJSONArray("freeOfferList");
                    JSONArray comboArray = jsonObject.getJSONArray("comboOfferList");
                    JSONArray priceArray = jsonObject.getJSONArray("priceOfferList");
                    JSONArray couponArray = jsonObject.getJSONArray("couponOfferList");
                    JSONObject dataObject = null;
                    ShopOfferItem offerItem= null;
                    ProductComboOffer productComboOffer = null;
                    ProductComboDetails productComboDetails = null;
                    ProductDiscountOffer productDiscountOffer = null;
                    Coupon coupon = null;
                    int len = freeArray.length();
                    dbHelper.deleteTable(DbHelper.PROD_FREE_OFFER_TABLE);
                    for (int i = 0; i < len; i++) {
                        dataObject = freeArray.getJSONObject(i);
                        productDiscountOffer = new ProductDiscountOffer();
                        offerItem = new ShopOfferItem();
                        offerItem.setOfferId(dataObject.getString("id"));
                        offerItem.setOfferName(dataObject.getString("offerName"));
                        offerItem.setProductName("Free Product Offer");
                        offerItem.setProductLocalImage(R.drawable.thumb_12);
                        offerItem.setOfferType("free");
                        offerItem.setOfferStatus(dataObject.getString("status"));
                        productDiscountOffer.setId(dataObject.getInt("id"));
                        productDiscountOffer.setOfferName(dataObject.getString("offerName"));
                        productDiscountOffer.setProdBuyId(dataObject.getInt("prodBuyId"));
                        productDiscountOffer.setProdFreeId(dataObject.getInt("prodFreeId"));
                        productDiscountOffer.setProdBuyQty(dataObject.getInt("prodBuyQty"));
                        productDiscountOffer.setProdFreeQty(dataObject.getInt("prodFreeQty"));
                        productDiscountOffer.setStatus(dataObject.getString("status"));
                        productDiscountOffer.setStartDate(dataObject.getString("startDate"));
                        productDiscountOffer.setEndDate(dataObject.getString("endDate"));
                        dbHelper.addProductFreeOffer(productDiscountOffer,Utility.getTimeStamp(),Utility.getTimeStamp());
                        offerItem.setProductObject(productDiscountOffer);
                        itemList.add(offerItem);
                    }

                    len = comboArray.length();
                    JSONArray productComboArray = null;
                    List<ProductComboDetails> productComboOfferDetails = null;
                    int innerLen = 0;
                    for (int i = 0; i < len; i++) {
                        dataObject = comboArray.getJSONObject(i);
                        offerItem = new ShopOfferItem();
                        offerItem.setOfferId(dataObject.getString("id"));
                        offerItem.setOfferName(dataObject.getString("offerName"));
                        offerItem.setProductName("Combo Product Offer");
                        offerItem.setProductLocalImage(R.drawable.thumb_12);
                        offerItem.setOfferType("combo");
                        offerItem.setOfferStatus(dataObject.getString("status"));
                        productComboOffer = new ProductComboOffer();
                        productComboOffer.setId(dataObject.getInt("id"));
                        productComboOffer.setOfferName(dataObject.getString("offerName"));
                        productComboOffer.setStatus(dataObject.getString("status"));
                        productComboOffer.setStartDate(dataObject.getString("startDate"));
                        productComboOffer.setEndDate(dataObject.getString("endDate"));
                        productComboArray = dataObject.getJSONArray("productComboOfferDetails");
                        dbHelper.deleteTable(DbHelper.PROD_COMBO_TABLE);
                        dbHelper.deleteTable(DbHelper.PROD_COMBO_DETAIL_TABLE);
                        dbHelper.addProductComboOffer(productComboOffer,Utility.getTimeStamp(),Utility.getTimeStamp());
                        productComboOfferDetails = new ArrayList<>();
                        innerLen = productComboArray.length();
                        for (int k = 0; k < innerLen; k++) {
                            dataObject = productComboArray.getJSONObject(k);
                            productComboDetails = new ProductComboDetails();
                            productComboDetails.setId(dataObject.getInt("id"));
                            productComboDetails.setPcodPcoId(dataObject.getInt("pcodPcoId"));
                            productComboDetails.setPcodProdId(dataObject.getInt("pcodProdId"));
                            productComboDetails.setPcodProdQty(dataObject.getInt("pcodProdQty"));
                            productComboDetails.setPcodPrice((float)dataObject.getDouble("pcodPrice"));
                            productComboDetails.setStatus(dataObject.getString("status"));
                            productComboOfferDetails.add(productComboDetails);
                            dbHelper.addProductComboDetailOffer(productComboDetails,
                                    Utility.getTimeStamp(),Utility.getTimeStamp());
                        }
                        productComboOffer.setProductComboOfferDetails(productComboOfferDetails);

                        offerItem.setProductObject(productComboOffer);
                        itemList.add(offerItem);
                    }

                    len = priceArray.length();
                    for (int i = 0; i < len; i++) {
                        dataObject = priceArray.getJSONObject(i);
                        offerItem = new ShopOfferItem();
                        offerItem.setOfferId(dataObject.getString("id"));
                        offerItem.setOfferName(dataObject.getString("offerName"));
                        offerItem.setProductName("Product Price Offer");
                        offerItem.setProductLocalImage(R.drawable.thumb_12);
                        offerItem.setOfferType("price");
                        offerItem.setOfferStatus(dataObject.getString("status"));

                        productComboOffer = new ProductComboOffer();
                        productComboOffer.setId(dataObject.getInt("id"));
                        productComboOffer.setProdId(dataObject.getInt("prodId"));
                        productComboOffer.setOfferName(dataObject.getString("offerName"));
                        productComboOffer.setStatus(dataObject.getString("status"));
                        productComboOffer.setStartDate(dataObject.getString("startDate"));
                        productComboOffer.setEndDate(dataObject.getString("endDate"));
                        productComboArray = dataObject.getJSONArray("productComboOfferDetails");
                        dbHelper.deleteTable(DbHelper.PROD_PRICE_TABLE);
                        dbHelper.deleteTable(DbHelper.PROD_PRICE_DETAIL_TABLE);
                        dbHelper.addProductPriceOffer(productComboOffer,Utility.getTimeStamp(),Utility.getTimeStamp());
                        productComboOfferDetails = new ArrayList<>();
                        innerLen = productComboArray.length();
                        for (int k = 0; k < innerLen; k++) {
                            dataObject = productComboArray.getJSONObject(k);
                            productComboDetails = new ProductComboDetails();
                            productComboDetails.setId(dataObject.getInt("id"));
                            productComboDetails.setPcodPcoId(dataObject.getInt("pcodPcoId"));
                            productComboDetails.setPcodProdQty(dataObject.getInt("pcodProdQty"));
                            productComboDetails.setPcodPrice((float)dataObject.getDouble("pcodPrice"));
                            productComboDetails.setStatus(dataObject.getString("status"));
                            productComboOfferDetails.add(productComboDetails);
                            dbHelper.addProductPriceDetailOffer(productComboDetails,
                                    Utility.getTimeStamp(),Utility.getTimeStamp());
                        }
                        productComboOffer.setProductComboOfferDetails(productComboOfferDetails);
                        offerItem.setProductObject(productComboOffer);
                        itemList.add(offerItem);
                    }

                    len = couponArray.length();
                    dbHelper.deleteTable(DbHelper.COUPON_TABLE);
                    for (int i = 0; i < len; i++) {
                        dataObject = couponArray.getJSONObject(i);
                        offerItem = new ShopOfferItem();
                        offerItem.setOfferId(dataObject.getString("id"));
                        offerItem.setOfferName(dataObject.getString("name"));
                        offerItem.setProductName("Coupon Offer");
                        offerItem.setProductLocalImage(R.drawable.thumb_12);
                        offerItem.setOfferType("coupon");
                        offerItem.setOfferStatus(dataObject.getString("status"));
                        coupon = new Coupon();
                        coupon.setId(dataObject.getInt("id"));
                        coupon.setPercentage(dataObject.getInt("percentage"));
                        coupon.setAmount((float)dataObject.getDouble("amount"));
                        coupon.setName(dataObject.getString("name"));
                        coupon.setStatus(dataObject.getString("status"));
                        coupon.setStartDate(dataObject.getString("startDate"));
                        coupon.setEndDate(dataObject.getString("endDate"));
                        offerItem.setProductObject(coupon);
                        dbHelper.addCouponOffer(coupon, Utility.getTimeStamp(),Utility.getTimeStamp());
                        itemList.add(offerItem);
                    }

                    if(freeArray.length() == 0 && comboArray.length() == 0 && priceArray.length() == 0 &&
                            couponArray.length() == 0){
                        showNoData(true);
                    }else{
                        showNoData(false);
                        myItemAdapter.notifyDataSetChanged();
                    }

                }
            }else if (apiName.equals("statusChangeOffer")) {
                if (response.getBoolean("status")) {
                   if(changeType == 0){
                       itemList.remove(position);
                       myItemAdapter.notifyItemRemoved(position);
                       DialogAndToast.showDialog("Offer deleted successfully.",this);
                   }else{
                       if(isAllShowing){
                           ShopOfferItem item = itemList.get(position);
                           item.setOfferStatus("2");
                           myItemAdapter.notifyItemChanged(position);
                           DialogAndToast.showDialog("Offer disabled successfully.",this);
                       }else{
                           itemList.remove(position);
                           myItemAdapter.notifyItemRemoved(position);
                           DialogAndToast.showDialog("Offer disabled successfully.",this);
                       }
                   }
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showNoData(boolean show){
        if(show){
            recyclerView.setVisibility(View.GONE);
            textViewError.setVisibility(View.VISIBLE);
        }else{
            recyclerView.setVisibility(View.VISIBLE);
            textViewError.setVisibility(View.GONE);
        }
    }

    private void showNoNetwork(boolean show){
        if(show){
            recyclerView.setVisibility(View.GONE);
            textViewError.setVisibility(View.VISIBLE);
            textViewError.setText(getResources().getString(R.string.no_internet));
        }else{
            recyclerView.setVisibility(View.VISIBLE);
            textViewError.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClicked(int position, int type) {
        changeType = type;
        this.position = position;
        ShopOfferItem item = itemList.get(position);
        if(type ==1){
            changeStatus(item.getOfferId(),"0",item.getOfferType());
        }else if(type ==2){
            changeStatus(item.getOfferId(),"2",item.getOfferType());
        }else{

        }
    }
}
