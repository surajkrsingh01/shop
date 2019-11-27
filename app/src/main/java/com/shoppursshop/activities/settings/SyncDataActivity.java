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

import com.shoppursshop.R;
import com.shoppursshop.activities.LoginActivity;
import com.shoppursshop.activities.NetworkBaseActivity;
import com.shoppursshop.adapters.SimpleItemAdapter;
import com.shoppursshop.database.DbHelper;
import com.shoppursshop.models.Coupon;
import com.shoppursshop.models.MyCustomer;
import com.shoppursshop.models.MyProductItem;
import com.shoppursshop.models.MySimpleItem;
import com.shoppursshop.models.ProductColor;
import com.shoppursshop.models.ProductComboDetails;
import com.shoppursshop.models.ProductComboOffer;
import com.shoppursshop.models.ProductDiscountOffer;
import com.shoppursshop.models.ProductSize;
import com.shoppursshop.models.ProductUnit;
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

public class SyncDataActivity extends NetworkBaseActivity {

    private RecyclerView recyclerView;
    private List<Object> itemList;
    private SimpleItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_data);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();
        initFooterAction(this);

    }

    private void init(){
        itemList = new ArrayList<>();
        MySimpleItem item=new MySimpleItem();
        item.setName("Categories");
        itemList.add(item);
        item=new MySimpleItem();
        item.setName("Sub Categories");
        itemList.add(item);
        item=new MySimpleItem();
        item.setName("Products");
        itemList.add(item);
        item=new MySimpleItem();
        item.setName("Customers");
        itemList.add(item);
        item=new MySimpleItem();
        item.setName("Offers");
        itemList.add(item);

        recyclerView= findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        itemAdapter=new SimpleItemAdapter(this,itemList,"simpleSyncList");
        recyclerView.setAdapter(itemAdapter);


        findViewById(R.id.relative_footer_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ConnectionDetector.isNetworkAvailable(SyncDataActivity.this)){
                    syncSata();
                }else{
                    DialogAndToast.showDialog(getResources().getString(R.string.no_internet),SyncDataActivity.this);
                }
            }
        });

    }

    private void syncSata(){
        Map<String,String> params=new HashMap<>();
        params.put("id",sharedPreferences.getString(Constants.USER_ID,""));
        params.put("syncedDate",sharedPreferences.getString(Constants.SYNCED_DATE,"1900-01-01 00:00:00"));
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+Constants.SYNC;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"syncdata");
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {
        showProgress(false);
        try {
            // JSONObject jsonObject=response.getJSONObject("response");
            if(apiName.equals("syncdata")){
                if(response.getBoolean("status")){
                    JSONObject dataObject = response.getJSONObject("result");
                    JSONArray catArray = dataObject.getJSONArray("categories");
                    JSONArray subCatArray = dataObject.getJSONArray("sub_categories");
                    JSONArray productArray = dataObject.getJSONArray("products");
                    JSONArray productBarCodeArray = dataObject.getJSONArray("products_barcodes");
                    JSONArray freeArray = dataObject.getJSONArray("freeOfferList");
                    JSONArray comboArray = dataObject.getJSONArray("comboOfferList");
                    JSONArray priceArray = dataObject.getJSONArray("priceOfferList");
                    JSONArray couponArray = dataObject.getJSONArray("couponOfferList");
                    JSONArray customerArray = dataObject.getJSONArray("customerList");
                    JSONArray deviceArray = dataObject.getJSONArray("deviceList");
                    JSONArray tempArray = null;
                    JSONObject jsonObject =null,tempObject = null;
                    MySimpleItem item = null;
                    MyProductItem productItem = null;
                    MyCustomer myCustomer = null;
                    ProductComboOffer productComboOffer = null;
                    ProductComboDetails productComboDetails = null;
                    ProductDiscountOffer productDiscountOffer = null;
                    Coupon coupon = null;
                    ProductUnit productUnit = null;
                    ProductSize productSize = null;
                    ProductColor productColor = null;
                    int len = catArray.length();
                    dbHelper.deleteTable(DbHelper.CAT_TABLE);
                    dbHelper.deleteTable(DbHelper.SUB_CAT_TABLE);
                    dbHelper.deleteTable(DbHelper.PRODUCT_TABLE);
                    dbHelper.deleteTable(DbHelper.PRODUCT_BARCODE_TABLE);
                    dbHelper.deleteTable(DbHelper.PRODUCT_UNIT_TABLE);
                    dbHelper.deleteTable(DbHelper.PRODUCT_SIZE_TABLE);
                    dbHelper.deleteTable(DbHelper.PRODUCT_COLOR_TABLE);
                    dbHelper.deleteTable(DbHelper.PROD_COMBO_TABLE);
                    dbHelper.deleteTable(DbHelper.PROD_COMBO_DETAIL_TABLE);
                    dbHelper.deleteTable(DbHelper.PROD_PRICE_TABLE);
                    dbHelper.deleteTable(DbHelper.PROD_PRICE_DETAIL_TABLE);
                    dbHelper.deleteTable(DbHelper.PROD_FREE_OFFER_TABLE);
                    dbHelper.deleteTable(DbHelper.COUPON_TABLE);
                    dbHelper.deleteTable(DbHelper.CUSTOMER_INFO_TABLE);
                    for(int i=0; i<len; i++){
                        jsonObject = catArray.getJSONObject(i);
                        item = new MySimpleItem();
                        item.setId(jsonObject.getInt("catId"));
                        item.setName(jsonObject.getString("catName"));
                        item.setImage(jsonObject.getString("imageUrl"));
                        dbHelper.addCategory(item, Utility.getTimeStamp(),Utility.getTimeStamp());
                    }

                    ((MySimpleItem)itemList.get(0)).setSelected(true);

                    len = subCatArray.length();
                    for(int i=0; i<len; i++){
                        jsonObject = subCatArray.getJSONObject(i);
                        item = new MySimpleItem();
                        item.setId(jsonObject.getInt("subCatId"));
                        item.setName(jsonObject.getString("subCatName"));
                        item.setImage(jsonObject.getString("imageUrl"));
                        dbHelper.addSubCategory(item,""+jsonObject.getInt("catId"),Utility.getTimeStamp(),Utility.getTimeStamp());
                    }

                    ((MySimpleItem)itemList.get(1)).setSelected(true);

                    len = productArray.length();
                    for(int i=0; i<len; i++){
                        jsonObject = productArray.getJSONObject(i);
                        productItem = new MyProductItem();
                        productItem.setProdId(jsonObject.getInt("prodId"));
                        productItem.setProdCatId(jsonObject.getInt("prodCatId"));
                        productItem.setProdSubCatId(jsonObject.getInt("prodSubCatId"));
                        productItem.setProdName(jsonObject.getString("prodName"));
                        productItem.setProdCode(jsonObject.getString("prodCode"));
                        // productItem.setProdBarCode(jsonObject.getString("prodBarCode"));
                        productItem.setProdDesc(jsonObject.getString("prodDesc"));
                        productItem.setProdReorderLevel(jsonObject.getInt("prodReorderLevel"));
                        productItem.setProdQoh(jsonObject.getInt("prodQoh"));
                        productItem.setProdHsnCode(jsonObject.getString("prodHsnCode"));
                        productItem.setProdCgst(Float.parseFloat(jsonObject.getString("prodCgst")));
                        productItem.setProdIgst(Float.parseFloat(jsonObject.getString("prodIgst")));
                        productItem.setProdSgst(Float.parseFloat(jsonObject.getString("prodSgst")));
                        productItem.setProdWarranty(Float.parseFloat(jsonObject.getString("prodWarranty")));
                        productItem.setProdMfgDate(jsonObject.getString("prodMfgDate"));
                        productItem.setProdExpiryDate(jsonObject.getString("prodExpiryDate"));
                        productItem.setProdMfgBy(jsonObject.getString("prodMfgBy"));
                        productItem.setProdImage1(jsonObject.getString("prodImage1"));
                        productItem.setProdImage2(jsonObject.getString("prodImage2"));
                        productItem.setProdImage3(jsonObject.getString("prodImage3"));
                        productItem.setIsBarCodeAvailable(jsonObject.getString("isBarcodeAvailable"));
                        productItem.setProdMrp(Float.parseFloat(jsonObject.getString("prodMrp")));
                        productItem.setProdSp(Float.parseFloat(jsonObject.getString("prodSp")));
                        productItem.setCreatedBy(jsonObject.getString("createdBy"));
                        productItem.setUpdatedBy(jsonObject.getString("updatedBy"));
                        productItem.setCreatedDate(jsonObject.getString("createdDate"));
                        productItem.setUpdatedDate(jsonObject.getString("updatedDate"));
                        dbHelper.addProduct(productItem,Utility.getTimeStamp(),Utility.getTimeStamp());

                        if(!jsonObject.getString("productUnitList").equals("null")){
                            tempArray = jsonObject.getJSONArray("productUnitList");
                            int tempLen = tempArray.length();

                            for(int unitCounter = 0; unitCounter< tempLen ; unitCounter++){
                                tempObject = tempArray.getJSONObject(unitCounter);
                                productUnit = new ProductUnit();
                                productUnit.setId(tempObject.getInt("id"));
                                productUnit.setUnitName(tempObject.getString("unitName"));
                                productUnit.setUnitValue(tempObject.getString("unitValue"));
                                productUnit.setStatus(tempObject.getString("status"));
                                dbHelper.addProductUnit(productUnit,productItem.getProdId());
                            }
                        }

                        if(!jsonObject.getString("productSizeList").equals("null")){
                            tempArray = jsonObject.getJSONArray("productSizeList");
                            int tempLen = tempArray.length();

                            for(int unitCounter = 0; unitCounter< tempLen ; unitCounter++){
                                tempObject = tempArray.getJSONObject(unitCounter);
                                productSize = new ProductSize();
                                productSize.setId(tempObject.getInt("id"));
                                productSize.setSize(tempObject.getString("size"));
                                productSize.setStatus(tempObject.getString("status"));
                                dbHelper.addProductSize(productSize,productItem.getProdId());

                                if(!jsonObject.getString("productSizeList").equals("null")){
                                    JSONArray colorArray = tempObject.getJSONArray("productColorList");
                                    for(int colorCounter = 0; colorCounter < colorArray.length() ; colorCounter++){
                                        tempObject = colorArray.getJSONObject(colorCounter);
                                        productColor = new ProductColor();
                                        productColor.setId(tempObject.getInt("id"));
                                        productColor.setSizeId(tempObject.getInt("sizeId"));
                                        productColor.setColorName(tempObject.getString("colorName"));
                                        productColor.setColorValue(tempObject.getString("colorValue"));
                                        productColor.setStatus(tempObject.getString("status"));
                                        dbHelper.addProductColor(productColor,productSize.getId());
                                    }
                                }

                            }
                        }

                    }

                    len = productBarCodeArray.length();
                    for(int i=0; i<len; i++) {
                        jsonObject = productBarCodeArray.getJSONObject(i);
                        dbHelper.addProductBarcode(jsonObject.getInt("prodId"),jsonObject.getString("prodBarCode"));
                    }

                    ((MySimpleItem)itemList.get(2)).setSelected(true);

                    len = freeArray.length();
                    for (int i = 0; i < len; i++) {
                        dataObject = freeArray.getJSONObject(i);
                        Log.d("index ", ""+len);
                        productDiscountOffer = new ProductDiscountOffer();
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
                    }

                    len = comboArray.length();
                    JSONArray productComboArray = null;
                    List<ProductComboDetails> productComboOfferDetails = null;
                    int innerLen = 0;
                    for (int i = 0; i < len; i++) {
                        dataObject = comboArray.getJSONObject(i);
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
                    }

                    len = priceArray.length();
                    for (int i = 0; i < len; i++) {
                        dataObject = priceArray.getJSONObject(i);
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
                    }

                    len = couponArray.length();
                    dbHelper.deleteTable(DbHelper.COUPON_TABLE);
                    for (int i = 0; i < len; i++) {
                        dataObject = couponArray.getJSONObject(i);
                        coupon = new Coupon();
                        coupon.setId(dataObject.getInt("id"));
                        coupon.setPercentage(dataObject.getInt("percentage"));
                        coupon.setAmount((float)dataObject.getDouble("amount"));
                        coupon.setName(dataObject.getString("name"));
                        coupon.setStatus(dataObject.getString("status"));
                        coupon.setStartDate(dataObject.getString("startDate"));
                        coupon.setEndDate(dataObject.getString("endDate"));
                        dbHelper.addCouponOffer(coupon, Utility.getTimeStamp(),Utility.getTimeStamp());
                    }

                    ((MySimpleItem)itemList.get(4)).setSelected(true);

                    len = customerArray.length();
                    dbHelper.deleteTable(DbHelper.CUSTOMER_INFO_TABLE);
                    for (int i = 0; i < len; i++) {
                        dataObject = customerArray.getJSONObject(i);
                        myCustomer = new MyCustomer();
                        myCustomer.setId(dataObject.getString("id"));
                        myCustomer.setCode(dataObject.getString("code"));
                        myCustomer.setName(dataObject.getString("name"));
                        myCustomer.setMobile(dataObject.getString("mobileNo"));
                        myCustomer.setEmail(dataObject.getString("email"));
                        myCustomer.setAddress(dataObject.getString("address"));
                        myCustomer.setCountry(dataObject.getString("country"));
                        myCustomer.setLocality(dataObject.getString("locality"));
                        myCustomer.setState(dataObject.getString("state"));
                        myCustomer.setCity(dataObject.getString("city"));
                        myCustomer.setLatitude(dataObject.getString("latitude"));
                        myCustomer.setLongitude(dataObject.getString("longitude"));
                        myCustomer.setImage(dataObject.getString("photo"));
                        myCustomer.setIsFav(dataObject.getString("isFav"));
                        myCustomer.setRatings((float)dataObject.getDouble("ratings"));
                        myCustomer.setStatus(dataObject.getString("isActive"));
                        myCustomer.setCustUserCreateStatus(dataObject.getString("userCreateStatus"));
                        if(myCustomer.getIsFav().equals("Y")){
                        }else{
                            myCustomer.setIsFav("N");
                        }
                        dbHelper.addCustomerInfo(myCustomer,Utility.getTimeStamp(),Utility.getTimeStamp());
                    }

                    ((MySimpleItem)itemList.get(3)).setSelected(true);

                    len = deviceArray.length();
                    // dbHelper.deleteTable(DbHelper.CUSTOMER_INFO_TABLE);
                    for (int i = 0; i < len; i++) {
                        dataObject = deviceArray.getJSONObject(i);
                      /*  myDevice = new MyDevice();
                        myDevice.setId(dataObject.getInt("id"));
                        myDevice.setSerialNumber(dataObject.getString("serialNumber"));
                        myDevice.setModel(dataObject.getString("model"));
                        myDevice.setAllotment(dataObject.getString("allotment"));
                        myDevice.setMerchantId(dataObject.getString("merchantId"));
                        myDevice.setMerchantName(dataObject.getString("merchantName"));
                        myDevice.setMaker(dataObject.getString("maker"));
                        myDevice.setAllottedUserId(dataObject.getString("allottedUserId"));
                        myDevice.setAllottedUserName(dataObject.getString("allottedUserName"));
                        myDevice.setAllottedUserMobile(dataObject.getString("allottedUserMobile"));
                        myDevice.setStatus(dataObject.getString("status"));*/

                        if(sharedPreferences.getString(Constants.MOBILE_NO,"").
                                equals(dataObject.getString("allottedUserMobile")) &&
                                dataObject.getString("model").equals("ME3POS")){
                            editor.putString(Constants.MERCHANT_ID,dataObject.getString("merchantId"));
                            editor.putString(Constants.DEVICE_SER_NO,dataObject.getString("serialNumber"));
                            editor.putString(Constants.DEVICE_MODEL,dataObject.getString("model"));
                            editor.commit();
                        }

                    }

                    recyclerView.setVisibility(View.VISIBLE);
                    itemAdapter.notifyDataSetChanged();

                    editor.putString(Constants.SYNCED_DATE,Utility.getTimeStamp("yyyy-MM-dd HH:mm:ss"));
                    editor.commit();
                }else{
                    DialogAndToast.showDialog(response.getString("message"),this);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            DialogAndToast.showToast(getResources().getString(R.string.json_parser_error)+e.toString(),SyncDataActivity.this);
        }
    }

}
