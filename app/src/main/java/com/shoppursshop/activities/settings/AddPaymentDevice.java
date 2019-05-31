package com.shoppursshop.activities.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.android.gms.maps.model.Marker;
import com.shoppursshop.R;
import com.shoppursshop.activities.NetworkBaseActivity;
import com.shoppursshop.adapters.ShoppursProductAdapter;
import com.shoppursshop.models.Barcode;
import com.shoppursshop.models.MyProductItem;
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
import java.util.Random;

public class AddPaymentDevice extends NetworkBaseActivity {

    private MyProductItem myProduct;
    private String dbName, dbUserName, dbPassword, shopCode, subCatId;
    private String TAG = "AddPaymentDevice";
    private List<MyProductItem> myProductList;
    private RecyclerView recyclerView;
    private ShoppursProductAdapter shoppursProductAdapter;
    private RelativeLayout rlfooterviewcart;
    private float total_amount;
    private int total_quantity, cartSize;
    private TextView tv_total,tv_totalItems, tv_placeorder, tvnoData, viewCart;
    private LinearLayout linear_details;
    private String transactionId;
    private List<Barcode> barCodeList;
    private TextView tv_top_parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment_device);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setToolbarTheme();
        shopCode = sharedPreferences.getString(Constants.SHOP_CODE,"");
        subCatId = "11";
        dbName = "SHP1";
        dbUserName = sharedPreferences.getString(Constants.DB_USER_NAME,"");
        dbPassword = sharedPreferences.getString(Constants.DB_PASSWORD,"");
        myProductList = new ArrayList<>();
        init();
        getPaymentDevice();
    }

    private void init(){
        tv_total = findViewById(R.id.itemPrice);
        tv_totalItems = findViewById(R.id.itemCount);
        tv_placeorder = findViewById(R.id.viewCart);
        linear_details = findViewById(R.id.linear_details);
        rlfooterviewcart = findViewById(R.id.rlfooterviewcart);
        rlfooterviewcart.setBackgroundColor(colorTheme);
        tvnoData = findViewById(R.id.tvnoData);
        viewCart = findViewById(R.id.viewCart);
        viewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //generateJson();

            }
        });

        tv_top_parent = findViewById(R.id.text_left_label);
        tv_top_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(AddPaymentDevice.this, SettingActivity.class));
                finish();
            }
        });
    }

    private JSONArray shopArray;
    private void generateJson(){
        try {
            List<String> tempShopList = new ArrayList<>();
            shopArray = new JSONArray();
            JSONObject shopObject = new JSONObject();
            JSONArray productArray = new JSONArray();
            JSONObject productObject = new JSONObject();
            JSONArray tempbarcodeArray =null;
            JSONObject tempbarcodeObject = null;

            for (MyProductItem cartItem : myProductList) {
                Log.d(TAG, cartItem.getProdBarCode()+"");
                tempbarcodeArray = new JSONArray();

                for (int i=0;i<cartItem.getQty();i++){
                    tempbarcodeObject = new JSONObject();
                    tempbarcodeObject.put("barcode", cartItem.getBarcodeList().get(i).getBarcode());
                    tempbarcodeArray.put(tempbarcodeObject);
                }
                if (!tempShopList.contains(shopCode)) {
                    //Log.d("PRD list "+tempShopList.toString(), cartItem.getShopCode());
                    tempShopList.add(shopCode);
                    shopObject = new JSONObject();
                    productArray = new JSONArray();
                    productObject = new JSONObject();

                    shopObject.put("shopCode", shopCode);
                    shopObject.put("orderDate", Utility.getTimeStamp());
                    shopObject.put("orderDeliveryNote","Note");
                    shopObject.put("orderDeliveryMode","Self");
                    shopObject.put("paymentMode","Online");
                    shopObject.put("deliveryAddress",sharedPreferences.getString(Constants.ADDRESS, ""));
                    shopObject.put("pinCode",sharedPreferences.getString(Constants.ZIP,""));
                    shopObject.put("createdBy",sharedPreferences.getString(Constants.FULL_NAME,""));
                    shopObject.put("updateBy",sharedPreferences.getString(Constants.FULL_NAME,""));
                    shopObject.put("orderStatus","pending");
                    shopObject.put("orderPaymentStatus", "pending");
                    shopObject.put("custName", sharedPreferences.getString(Constants.FULL_NAME,""));
                    shopObject.put("custCode",sharedPreferences.getString(Constants.SHOP_CODE,""));
                    shopObject.put("mobileNo",sharedPreferences.getString(Constants.MOBILE_NO,""));
                    shopObject.put("orderImage",sharedPreferences.getString(Constants.PHOTO,""));
                    shopObject.put("totalQuantity",total_quantity);
                    shopObject.put("toalAmount",total_amount);
                    shopObject.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
                    shopObject.put("dbUserName",dbUserName);
                    shopObject.put("dbPassword",dbPassword);
                    productObject.put("prodId", cartItem.getProdId());
                    if(cartItem.getIsBarCodeAvailable().equals("Y")){
                        productObject.put("prodBarCode", cartItem.getBarcodeList().get(0).getBarcode());
                        productObject.put("barcodeList",  tempbarcodeArray);
                    }
                    productObject.put("qty", cartItem.getQty());
                    productArray.put(productObject);
                    shopObject.put("myProductList", productArray);
                    shopArray.put(shopObject);
                } else {
                    productObject = new JSONObject();

                    productObject.put("prodCode", cartItem.getProdCode());
                    productObject.put("prodId", cartItem.getProdId());
                    if(cartItem.getIsBarCodeAvailable().equals("Y")){
                        productObject.put("prodBarCode", cartItem.getBarcodeList().get(0).getBarcode());
                        productObject.put("barcodeList",  tempbarcodeArray);
                    }
                    productObject.put("qty", cartItem.getQty());
                    productArray.put(productObject);
                    shopObject.put("myProductList", productArray);
                }
            }
            generateOrder(shopArray);
            Log.d(TAG, shopArray.toString());
        }catch (Exception a){
            a.printStackTrace();
        }
    }

    private void generateOrder(JSONArray shopArray){
        Log.d(TAG, shopArray.toString());
        String url=getResources().getString(R.string.url)+"/api/device/order/generate_order";
        showProgress(true);
        jsonArrayV2ApiRequest(Request.Method.POST,url, shopArray,"generate_order");
    }

    private void placeOrder(JSONArray shopArray, String orderId) throws JSONException {
        transactionId = "trans"+new Random().nextInt(100000);
        shopArray.getJSONObject(0).put("orderId", orderId );
        for(int i=0;i<shopArray.length();i++){
            shopArray.getJSONObject(i).put("orderPaymentStatus", "Done");
            shopArray.getJSONObject(i).put("transactionId", transactionId);
        }
        Log.d(TAG, shopArray.toString());
        String url=getResources().getString(R.string.url)+"/api/device/order/place_order";
        showProgress(true);
        jsonArrayV2ApiRequest(Request.Method.POST,url, shopArray,"place_order");
    }

    private void getPaymentDevice(){
        barCodeList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        // myProductList = dbHelper.getShoppursProducts(subCatId);
        shoppursProductAdapter = new ShoppursProductAdapter(this, myProductList);
        shoppursProductAdapter.setColorTheme(colorTheme);
        recyclerView.setAdapter(shoppursProductAdapter);

        Map<String,String> params=new HashMap<>();
        params.put("subCatId", subCatId);
        params.put("shopCode", shopCode);
        params.put("dbName",dbName);
        params.put("dbUserName",dbUserName);
        params.put("dbPassword",dbPassword);
        Log.d(TAG, params.toString());
        String url=getResources().getString(R.string.url)+"/api/customers/products/ret_productslist";
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"productfromshop");
    }



    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {
        showProgress(false);
        try {
            // JSONObject jsonObject=response.getJSONObject("response");
            Log.d("response", response.toString());
            if(apiName.equals("productfromshop")){
                JSONArray productJArray = response.getJSONArray("result");
                JSONArray barcodeArray = null;

                for(int i=0;i<productJArray.length();i++){
                    MyProductItem myProduct = new MyProductItem();
                    myProduct.setProdId(productJArray.getJSONObject(i).getInt("prodId"));
                    myProduct.setProdName(productJArray.getJSONObject(i).getString("prodName"));
                    myProduct.setProdQoh(productJArray.getJSONObject(i).getInt("prodQoh"));
                    myProduct.setProdMrp(Float.parseFloat(productJArray.getJSONObject(i).getString("prodMrp")));
                    myProduct.setProdSp(Float.parseFloat(productJArray.getJSONObject(i).getString("prodSp")));
                    myProduct.setProdCode(productJArray.getJSONObject(i).getString("prodCode"));
                    myProduct.setProdBarCode(productJArray.getJSONObject(i).getString("prodBarCode"));
                    myProduct.setProdDesc(productJArray.getJSONObject(i).getString("prodDesc"));
                    // myProduct.setLocalImage(R.drawable.thumb_16);
                    myProduct.setProdImage1(productJArray.getJSONObject(i).getString("prodImage1"));
                    myProduct.setProdImage2(productJArray.getJSONObject(i).getString("prodImage2"));
                    myProduct.setProdImage3(productJArray.getJSONObject(i).getString("prodImage3"));
                    myProduct.setProdHsnCode(productJArray.getJSONObject(i).getString("prodHsnCode"));
                    myProduct.setProdMfgDate(productJArray.getJSONObject(i).getString("prodMfgDate"));
                    myProduct.setProdExpiryDate(productJArray.getJSONObject(i).getString("prodExpiryDate"));
                    myProduct.setProdMfgBy(productJArray.getJSONObject(i).getString("prodMfgBy"));
                    myProduct.setProdExpiryDate(productJArray.getJSONObject(i).getString("prodExpiryDate"));
                    //myProduct.setOfferId(productJArray.getJSONObject(i).getString("offerId"));
                    myProduct.setProdCgst(Float.parseFloat(productJArray.getJSONObject(i).getString("prodCgst")));
                    myProduct.setProdIgst(Float.parseFloat(productJArray.getJSONObject(i).getString("prodIgst")));
                    myProduct.setProdSgst(Float.parseFloat(productJArray.getJSONObject(i).getString("prodSgst")));
                    myProduct.setProdWarranty(Float.parseFloat(productJArray.getJSONObject(i).getString("prodWarranty")));
                    // myProduct.setSubCatName(subcatname);

                    if(!productJArray.getJSONObject(i).getString("barcodeList").equals("null")){
                        myProductList.add(myProduct);
                        barcodeArray = productJArray.getJSONObject(i).getJSONArray("barcodeList");
                        barCodeList = new ArrayList<>();
                        for (int j= 0;j<barcodeArray.length();j++){
                            barCodeList.add(new Barcode(barcodeArray.getJSONObject(j).getString("barcode")));
                        }
                        myProduct.setBarcodeList(barCodeList);
                    }

                }
                if(myProductList.size()>0){
                    shoppursProductAdapter.notifyDataSetChanged();
                }else {
                    // showNoData(true);
                }

            }else if (apiName.equals("generate_order")){
                if(response.getString("status").equals("true")||response.getString("status").equals(true)){
                    Log.d(TAG, "Ordeer Generated" );
                    String orderNumber = response.getJSONObject("result").getString("orderNumber");
                    Log.d(TAG, "orderNumber "+orderNumber );
                    //placeOrder(shopArray, orderId);  // open payment option
                }else {
                    DialogAndToast.showToast(response.getString("message"),AddPaymentDevice.this);
                }
            }else if(apiName.equals("place_order")){
                if(response.getString("status").equals("true")||response.getString("status").equals(true)){
                    myProductList.clear();
                    recyclerView.setAdapter(null);
                    shoppursProductAdapter.notifyDataSetChanged();
                    Log.d(TAG, "Ordeer Placed" );
                }else {
                    DialogAndToast.showToast(response.getString("message"),AddPaymentDevice.this);
                }
            } else {
                DialogAndToast.showDialog(response.getString("message"),AddPaymentDevice.this);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            DialogAndToast.showToast(getResources().getString(R.string.json_parser_error)+e.toString(),AddPaymentDevice.this);
        }
    }

    public void updateUi(){
        total_amount = 0;
        total_quantity = 0;
        cartSize = 0;
        for (MyProductItem myProductItem : myProductList) {
            total_amount = total_amount + myProductItem.getTotalAmount();
            total_quantity = total_quantity + myProductItem.getQty();
            if(myProductItem.getTotalAmount()>0) {
                viewCart.setVisibility(View.VISIBLE);
                cartSize = cartSize + 1;
            }else viewCart.setVisibility(View.GONE);
        }
        tv_total.setText("Amount " + String.valueOf(Utility.numberFormat(total_amount)));
        tv_totalItems.setText("Items(" + cartSize + ")");
        //shoppursProductAdapter.notifyDataSetChanged();
    }
}
