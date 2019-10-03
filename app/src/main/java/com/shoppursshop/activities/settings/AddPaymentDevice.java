package com.shoppursshop.activities.settings;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.android.gms.maps.model.Marker;
import com.shoppursshop.R;
import com.shoppursshop.activities.MainActivity;
import com.shoppursshop.activities.NetworkBaseActivity;
import com.shoppursshop.activities.SplashActivity;
import com.shoppursshop.activities.payment.PaymentActivity;
import com.shoppursshop.activities.payment.ccavenue.activities.CCAvenueWebViewActivity;
import com.shoppursshop.activities.payment.ccavenue.utility.AvenuesParams;
import com.shoppursshop.adapters.MyDeviceAdapter;
import com.shoppursshop.adapters.ShoppursProductAdapter;
import com.shoppursshop.interfaces.MyImageClickListener;
import com.shoppursshop.models.Barcode;
import com.shoppursshop.models.MyDevice;
import com.shoppursshop.models.MyProductItem;
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
import java.util.Random;

public class AddPaymentDevice extends NetworkBaseActivity implements MyImageClickListener {

    private MyProductItem myProduct;
    private String dbName, dbUserName, dbPassword, shopCode, subCatId;
    private String TAG = "AddPaymentDevice";
    private List<MyProductItem> myProductList;
    private List<MyDevice> deviceList;
    private TextView text_device_label,text_add_payment_device_label,tvErrorNoProduct,tvnoMyDeviceData;
    private RecyclerView recyclerView,recyclerViewMyDevices;
    private ShoppursProductAdapter shoppursProductAdapter;
    private MyDeviceAdapter myDeviceAdapter;
    private RelativeLayout rlfooterviewcart;
    private float total_amount,totCGST,totSGST,totIGST;
    private int total_quantity, cartSize;
    private TextView tv_total,tv_totalItems, tv_placeorder, tvnoData, viewCart;
    private LinearLayout linear_details;
    private String transactionId;
    private List<Barcode> barCodeList;
    private TextView tv_top_parent;

    private float totalTax,totDiscount,deliveryDistance,deliveryCharges;

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
        tvErrorNoProduct = findViewById(R.id.tvnoProductData);
        tvnoMyDeviceData = findViewById(R.id.tvnoMyDeviceData);
        text_add_payment_device_label = findViewById(R.id.text_add_payment_device_label);
        text_device_label = findViewById(R.id.text_device_label);
        linear_details = findViewById(R.id.linear_details);
        rlfooterviewcart = findViewById(R.id.rlfooterviewcart);
        rlfooterviewcart.setBackgroundColor(colorTheme);
        tvnoData = findViewById(R.id.tvnoData);
        viewCart = findViewById(R.id.viewCart);

        deviceList = new ArrayList<>();
        recyclerViewMyDevices = findViewById(R.id.recycler_view_my_devices);
        recyclerViewMyDevices.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewMyDevices.setLayoutManager(layoutManager);
        recyclerViewMyDevices.setItemAnimator(new DefaultItemAnimator());
        // myProductList = dbHelper.getShoppursProducts(subCatId);
        myDeviceAdapter = new MyDeviceAdapter(this, deviceList);
        myDeviceAdapter.setColorTheme(colorTheme);
        //  myDeviceAdapter.setMyItemClickListener(this);
        recyclerViewMyDevices.setAdapter(myDeviceAdapter);
        recyclerViewMyDevices.setNestedScrollingEnabled(false);


        viewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateJson();

                /*Intent intent = new Intent(AddPaymentDevice.this, CCAvenueWebViewActivity.class);
                intent.putExtra("flag", "wallet");
                intent.putExtra(AvenuesParams.AMOUNT, String.format("%.02f",total_amount));
                intent.putExtra(AvenuesParams.ORDER_ID, orderNumber);
                intent.putExtra(AvenuesParams.CURRENCY, "INR");
                intent.putExtra("shopArray",shopArray.toString());
                startActivity(intent);
                finish();*/
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

    @Override
    public void onResume(){
        super.onResume();

        if(ConnectionDetector.isNetworkAvailable(this))
            getMyDevice();
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
                    shopObject.put("deliveryCountry",sharedPreferences.getString(Constants.COUNTRY, ""));
                    shopObject.put("deliveryState",sharedPreferences.getString(Constants.STATE, ""));
                    shopObject.put("deliveryCity",sharedPreferences.getString(Constants.CITY, ""));
                    shopObject.put("pinCode",sharedPreferences.getString(Constants.ZIP,""));
                    shopObject.put("createdBy",sharedPreferences.getString(Constants.FULL_NAME,""));
                    shopObject.put("updateBy",sharedPreferences.getString(Constants.FULL_NAME,""));
                    shopObject.put("orderStatus","pending");
                    shopObject.put("orderPaymentStatus", "pending");
                    shopObject.put("custName", sharedPreferences.getString(Constants.SHOP_NAME,""));
                    shopObject.put("custCode",sharedPreferences.getString(Constants.SHOP_CODE,""));
                    shopObject.put("mobileNo",sharedPreferences.getString(Constants.MOBILE_NO,""));
                    shopObject.put("orderImage",sharedPreferences.getString(Constants.PHOTO,""));
                    shopObject.put("totalQuantity",total_quantity);
                    shopObject.put("toalAmount",total_amount);
                    // shopObject.put("ordCouponId","");
                    //shopObject.put("totCgst",String.valueOf(dbHelper.getTaxesCart("cgst")));
                    //shopObject.put("totSgst",String.valueOf(dbHelper.getTaxesCart("sgst")));
                    //shopObject.put("totIgst",String.valueOf(dbHelper.getTaxesCart("igst")));
                    shopObject.put("totCgst",String.valueOf(totCGST));
                    shopObject.put("totSgst",String.valueOf(totSGST));
                    shopObject.put("totIgst",String.valueOf(totIGST));
                    shopObject.put("totTax",String.valueOf(totalTax));
                    shopObject.put("deliveryCharges",String.valueOf(0f));
                    shopObject.put("totDiscount",String.valueOf(totDiscount));
                    shopObject.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
                    shopObject.put("dbUserName",dbUserName);
                    shopObject.put("dbPassword",dbPassword);

                    productObject.put("prodCode", cartItem.getProdCode());
                    productObject.put("prodHsnCode", cartItem.getProdHsnCode());
                    productObject.put("prodId", cartItem.getProdId());
                    productObject.put("qty", cartItem.getQty());
                    productObject.put("prodName",cartItem.getProdName());
                    productObject.put("prodUnit",cartItem.getUnit());
                    productObject.put("prodSize",cartItem.getSize());
                    productObject.put("prodColor",cartItem.getColor());
                    productObject.put("prodDesc",cartItem.getProdDesc());
                    productObject.put("prodMrp",cartItem.getProdMrp());
                    productObject.put("prodSp", cartItem.getProdSp());
                    productObject.put("prodCgst", cartItem.getProdCgst());
                    productObject.put("prodSgst", cartItem.getProdSgst());
                    productObject.put("prodIgst", cartItem.getProdIgst());
                    productObject.put("prodImage1",cartItem.getProdImage1());
                    productObject.put("prodImage2",cartItem.getProdImage2());
                    productObject.put("prodImage3",cartItem.getProdImage3());
                    productObject.put("isBarcodeAvailable",cartItem.getIsBarCodeAvailable());

                    productArray.put(productObject);
                    shopObject.put("myProductList", productArray);
                    shopArray.put(shopObject);
                } else {
                    productObject = new JSONObject();

                    productObject.put("prodCode", cartItem.getProdCode());
                    productObject.put("prodHsnCode", cartItem.getProdHsnCode());
                    productObject.put("prodId", cartItem.getProdId());
                    productObject.put("qty", cartItem.getQty());
                    productObject.put("prodName",cartItem.getProdName());
                    productObject.put("prodUnit",cartItem.getUnit());
                    productObject.put("prodSize",cartItem.getSize());
                    productObject.put("prodColor",cartItem.getColor());
                    productObject.put("prodDesc",cartItem.getProdDesc());
                    productObject.put("prodMrp",cartItem.getProdMrp());
                    productObject.put("prodSp", cartItem.getProdSp());
                    productObject.put("prodCgst", cartItem.getProdCgst());
                    productObject.put("prodSgst", cartItem.getProdSgst());
                    productObject.put("prodIgst", cartItem.getProdIgst());
                    productObject.put("prodImage1",cartItem.getProdImage1());
                    productObject.put("prodImage2",cartItem.getProdImage2());
                    productObject.put("prodImage3",cartItem.getProdImage3());
                    productObject.put("isBarcodeAvailable",cartItem.getIsBarCodeAvailable());
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
        shoppursProductAdapter.setMyImageClickListener(this);
        shoppursProductAdapter.setColorTheme(colorTheme);
        recyclerView.setAdapter(shoppursProductAdapter);
        recyclerView.setNestedScrollingEnabled(false);

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

    private void getMyDevice(){
        Map<String,String> params=new HashMap<>();
        params.put("dbUserName",dbUserName);
        params.put("dbPassword",dbPassword);
        Log.d(TAG, params.toString());
        String url=getResources().getString(R.string.url)+"/api/device/order/get_my_devices";
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"getMyDevices");
    }



    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {
        showProgress(false);
        try {
            // JSONObject jsonObject=response.getJSONObject("response");
            Log.d("response", response.toString());
            if(apiName.equals("productfromshop")){
                if(response.getString("result") != null && !response.getString("result").equals("null")){
                    JSONArray productJArray = response.getJSONArray("result");
                    JSONArray barcodeArray = null;

                    for(int i=0;i<productJArray.length();i++){
                        MyProductItem myProduct = new MyProductItem();
                        myProduct.setProdId(productJArray.getJSONObject(i).getInt("prodId"));
                        myProduct.setProdCatId(productJArray.getJSONObject(i).getInt("prodCatId"));
                        myProduct.setProdSubCatId(productJArray.getJSONObject(i).getInt("prodSubCatId"));
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
                        myProduct.setIsBarCodeAvailable(productJArray.getJSONObject(i).getString("isBarcodeAvailable"));
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

                    Log.i(TAG,"product list "+myProductList.size());
                    if(myProductList.size()>0){
                        shoppursProductAdapter.notifyDataSetChanged();
                    }else {
                        showNoProduct(true);
                    }
                }else{
                    showNoProduct(true);
                }

            }else if (apiName.equals("generate_order")){
                if(response.getString("status").equals("true")||response.getString("status").equals(true)){
                    Log.d(TAG, "Ordeer Generated" );
                    String orderNumber = response.getJSONObject("result").getString("orderNumber");
                    Log.d(TAG, "orderNumber "+orderNumber );
                    //placeOrder(shopArray, orderId);  // open payment option
                    Intent intent = new Intent(AddPaymentDevice.this, CCAvenueWebViewActivity.class);
                    intent.putExtra("flag", "wallet");
                    intent.putExtra(AvenuesParams.AMOUNT, String.format("%.02f",total_amount));
                    intent.putExtra(AvenuesParams.ORDER_ID, orderNumber);
                    intent.putExtra(AvenuesParams.CURRENCY, "INR");
                    intent.putExtra("flag", "addPaymentDevice");
                    intent.putExtra("shopArray",shopArray.toString());
                    startActivity(intent);
                    finish();

                    /*Intent intent = new Intent(AddPaymentDevice.this, EPayPayswiffActivity.class);
                    intent.putExtra("orderNumber",orderNumber);
                    intent.putExtra("shopArray",shopArray.toString());
                    startActivity(intent);
                    finish();*/
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
            } else if(apiName.equals("getMyDevices")){
                if(response.getBoolean("status")){
                    JSONArray jsonArray = response.getJSONArray("result");
                    MyDevice item = null;
                    JSONObject jsonObject = null;
                    int len = jsonArray.length();
                    deviceList.clear();
                    for(int i=0; i<len; i++){
                        jsonObject = jsonArray.getJSONObject(i);
                        item = new MyDevice();
                        item.setId(jsonObject.getInt("id"));
                        item.setSerialNumber(jsonObject.getString("serialNumber"));
                        item.setModel(jsonObject.getString("model"));
                        item.setAllotment(jsonObject.getString("allotment"));
                        item.setMerchantId(jsonObject.getString("merchantId"));
                        item.setMerchantName(jsonObject.getString("merchantName"));
                        item.setMaker(jsonObject.getString("maker"));
                        item.setAllottedUserId(jsonObject.getString("allottedUserId"));
                        item.setAllottedUserName(jsonObject.getString("allottedUserName"));
                        item.setAllottedUserMobile(jsonObject.getString("allottedUserMobile"));
                        item.setStatus(jsonObject.getString("status"));
                        deviceList.add(item);
                    }

                    if(deviceList.size() == 0){
                        showNoMyDevice(true);
                    }else{
                        myDeviceAdapter.notifyDataSetChanged();
                    }
                }else{
                    DialogAndToast.showToast(response.getString("message"),AddPaymentDevice.this);
                }
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
        totalTax = 0;totSGST = 0;totCGST = 0;totIGST = 0;

        for (MyProductItem myProductItem : myProductList) {
            total_amount = total_amount + myProductItem.getTotalAmount();
            total_quantity = total_quantity + myProductItem.getQty();
            float rate = ((myProductItem.getProdSp() * (myProductItem.getProdCgst()+myProductItem.getProdSgst()))/(100 +
                    (myProductItem.getProdCgst()+myProductItem.getProdSgst())));
            totCGST = totCGST + rate/2;
            totSGST = totSGST + rate/2;
            totIGST = totIGST + rate;
            if(myProductItem.getTotalAmount()>0) {
                viewCart.setVisibility(View.VISIBLE);
                cartSize = cartSize + 1;
            }else viewCart.setVisibility(View.GONE);
        }

        totalTax = totCGST + totSGST;
        //totDiscount = dbHelper.getTotalMrpPriceCart() - dbHelper.getTotalPriceCart();

        tv_total.setText("Amount " + String.valueOf(Utility.numberFormat(total_amount)));
        tv_totalItems.setText("Items(" + cartSize + ")");
        //shoppursProductAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i(TAG,"requestCode code "+requestCode+" "+resultCode+" "+RESULT_OK);

        if (requestCode == 1)
            if(data != null){
                try {
                    JSONObject jsonObject = new JSONObject(data.getStringExtra("response"));
                    // Log.i(TAG,"Transaction status "+jsonObject.getString("order_status"));
                    String statusCode = jsonObject.getString("response_code");
                    if(statusCode.equals("0")){
                        //showMyDialog(data.getStringExtra("message"));



                    }else{
                        showMyDialog("Payment is unsuccessful. Please try again later.");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

    }

    @Override
    public void onDialogPositiveClicked(){

    }

    @Override
    public void onImageClicked(int position, int type, View view) {
        MyProductItem item = myProductList.get(position);
        showImageDialog(item.getProdImage1(),view);
    }

    private void showNoProduct(boolean show){
        if(show){
            tvErrorNoProduct.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            // text_add_payment_device_label.setVisibility(View.GONE);
        }else{
            tvErrorNoProduct.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            //  text_add_payment_device_label.setVisibility(View.VISIBLE);
        }
    }

    private void showNoMyDevice(boolean show){
        if(show){
            tvnoMyDeviceData.setVisibility(View.VISIBLE);
            recyclerViewMyDevices.setVisibility(View.GONE);
        }else{
            tvnoMyDeviceData.setVisibility(View.GONE);
            recyclerViewMyDevices.setVisibility(View.VISIBLE);
        }
    }
}
