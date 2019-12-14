package com.shoppursshop.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.shoppursshop.R;
import com.shoppursshop.adapters.MyItemAdapter;
import com.shoppursshop.adapters.OfferDescAdapter;
import com.shoppursshop.database.DbHelper;
import com.shoppursshop.interfaces.MyImageClickListener;
import com.shoppursshop.interfaces.MyItemTypeClickListener;
import com.shoppursshop.models.Barcode;
import com.shoppursshop.models.HomeListItem;
import com.shoppursshop.models.MyProductItem;
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

public class OffersActivity extends NetworkBaseActivity implements MyItemTypeClickListener, MyImageClickListener {

    private RecyclerView recyclerView;
    private MyItemAdapter myItemAdapter;
    private List<Object> itemList;
    private TextView textViewError;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;

    private int position,type,productDetailsType,counter;
    private String shopCode;
    private MyProductItem myProduct,freeProdut;
    private RelativeLayout rlfooterviewcart,rlOfferDesc;
    private TextView cartItemCount, cartItemPrice, viewCart;

    private float MIN_WIDTH = 200,MIN_HEIGHT = 230,MAX_WIDTH = 200,MAX_HEIGHT = 290;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initFooter(this,3);

        itemList = new ArrayList<>();
        shopCode = "SHP1";
        rlOfferDesc = findViewById(R.id.rl_offer_desc);
        rlfooterviewcart = findViewById(R.id.rlfooterviewcart);
        rlfooterviewcart.setBackgroundColor(colorTheme);
        cartItemCount= findViewById(R.id.itemCount);
        cartItemPrice = findViewById(R.id.itemPrice);
        viewCart = findViewById(R.id.viewCart);
        swipeRefreshLayout=findViewById(R.id.swipe_refresh);
        progressBar=findViewById(R.id.progress_bar);
        textViewError = findViewById(R.id.text_error);
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        // RecyclerView.LayoutManager layoutManagerHomeMenu=new LinearLayoutManager(this);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
      /*  int resId = R.anim.layout_animation_slide_from_bottom;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        recyclerView.setLayoutAnimation(animation);*/
        myItemAdapter=new MyItemAdapter(this,itemList,"homeList");
        myItemAdapter.setMyItemTypeClickListener(this);
        myItemAdapter.setDarkTheme(isDarkTheme);
        recyclerView.setAdapter(myItemAdapter);

        if(sharedPreferences.getString(Constants.SHOP_CODE,"") == "SHP1"){
            TextView text_desc = findViewById(R.id.text_desc);
            text_desc.setText("Shoppurs offers");
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                getItemList();
            }
        });

        findViewById(R.id.text_order_label).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if(ConnectionDetector.isNetworkAvailable(this)){
            getBanners();
        }
    }

    private void getItemList(){
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(myItemAdapter!=null) {
            counter = dbHelper.getCartCount();
            if(counter > 0){
                List<MyProductItem> cartList =  dbHelper.getShopCartProducts();
                MyProductItem myProductItem = null;
                for(MyProductItem item : cartList){
                    for(Object ob : itemList){
                        if(ob instanceof MyProductItem){
                            if(item.getProdId() == myProductItem.getProdId()){
                                myProductItem = (MyProductItem)ob;
                                Log.i(TAG,"Qty Before"+myProductItem.getQty());
                                myProductItem.setQty(item.getQty());
                                Log.i(TAG,"Qty After"+myProductItem.getQty());
                                break;
                            }

                        }
                    }
                }
            }else{
                MyProductItem myProductItem = null;
                for(Object ob : itemList){
                    if(ob instanceof MyProductItem){
                        myProductItem = (MyProductItem)ob;
                        Log.i(TAG,"Qty Before"+myProductItem.getQty());
                        myProductItem.setQty(0);
                        Log.i(TAG,"Qty After"+myProductItem.getQty());
                    }
                }
            }
            myItemAdapter.notifyDataSetChanged();
        }
        updateCartCount();
    }

    private void getBanners(){
        Map<String,String> params=new HashMap<>();
        params.put("dbName",shopCode);
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+Constants.GET_BANNER_OFFERS;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"bannerOffers");
    }

    private void getCategories(){
        Map<String,String> params=new HashMap<>();
        params.put("dbName",shopCode);
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+Constants.GET_CATEGORY_OFFERS;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"offer_categories");
    }

    private void getProducts(){
        Map<String,String> params=new HashMap<>();
        params.put("limit","5");
        params.put("offset","0");
        params.put("dbName",shopCode);
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.cust_url)+Constants.GET_PRODUCT_OFFERS;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"productList");
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {
        try {
            if (apiName.equals("bannerOffers")) {
                if (response.getBoolean("status")) {
                    JSONArray dataArray = response.getJSONArray("result");
                    JSONObject jsonObject = null;
                    int len = dataArray.length();
                    HomeListItem myItem = null,homeListItem = null;
                    List<Object> homeListItems;
                    int position = 0,prePosition = 0;
                    for (int i = 0; i < len; i++) {
                        jsonObject = dataArray.getJSONObject(i);
                        position = getBannerPosition(jsonObject.getString("name"));
                        if(position >= 0){
                            if(position > itemList.size()){
                                homeListItem = new HomeListItem();
                                homeListItem.setType(5);
                                homeListItems = new ArrayList<>();
                                myItem = new HomeListItem();
                                myItem.setId(jsonObject.getString("id"));
                                myItem.setName(jsonObject.getString("name"));
                                myItem.setImage(jsonObject.getString("image"));
                                myItem.setDesc(jsonObject.getString("desc"));
                                myItem.setType(1);
                                homeListItems.add(myItem);
                                homeListItem.setItemList(homeListItems);
                                prePosition = position;
                                itemList.add(homeListItem);
                            }else{
                                homeListItem = (HomeListItem) itemList.get(position-1);
                                myItem = new HomeListItem();
                                myItem.setId(jsonObject.getString("id"));
                                myItem.setName(jsonObject.getString("name"));
                                myItem.setImage(jsonObject.getString("image"));
                                myItem.setDesc(jsonObject.getString("desc"));
                                myItem.setType(1);
                                homeListItem.getItemList().add(myItem);
                            }
                        }

                    }

                    myItemAdapter.notifyDataSetChanged();
                }

                getCategories();

            }else if(apiName.equals("offer_categories")){
                if(response.getString("status").equals("true")||response.getString("status").equals(true)){
                    JSONArray jsonArray = response.getJSONArray("result");
                    HomeListItem myItem = new HomeListItem();
                    myItem.setTitle("Offers");
                    myItem.setDesc("Store Categories");
                    myItem.setType(0);
                    itemList.add(myItem);
                    for(int i=0;i<jsonArray.length();i++){
                        if(i < 4){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            myItem = new HomeListItem();
                            myItem.setId(jsonObject.getString("subCatId"));
                            myItem.setTitle(jsonObject.getString("subCatName"));
                            myItem.setImage(jsonObject.getString("imageUrl"));
                            myItem.setType(3);
                            if(jsonArray.length()==2){
                                myItem.setWidth(MIN_WIDTH);
                                myItem.setHeight(MIN_WIDTH);
                            } else if (jsonArray.length()>3 && i==0 || jsonArray.length()>3 && i == 3) {
                                myItem.setWidth(MIN_WIDTH);
                                myItem.setHeight(MIN_WIDTH);
                            }else {
                                myItem.setWidth(MAX_WIDTH);
                                myItem.setHeight(MAX_HEIGHT);
                            }
                            itemList.add(myItem);
                        }

                    }
                    Log.d("catList size ", itemList.size() + "");

                }else {
                    DialogAndToast.showDialog(response.getString("message"),OffersActivity.this);
                }

                getProducts();

            }else if(apiName.equals("productList")){
                if (response.getBoolean("status")) {
                    HomeListItem myItem = new HomeListItem();
                    myItem.setTitle("Offers");
                    myItem.setDesc("Store Products");
                    myItem.setType(0);
                    itemList.add(myItem);
                    JSONArray dataArray = response.getJSONObject("result").getJSONArray("productList");
                    JSONArray tempArray = null;
                    JSONObject tempObject = null;
                    JSONObject jsonObject = null;
                    ProductUnit productUnit = null;
                    ProductColor productColor = null;
                    ProductSize productSize = null;
                    int len = dataArray.length();
                    int innerLen = 0;
                    MyProductItem productItem = null;
                    for(int i=0;i<len;i++){
                        jsonObject = dataArray.getJSONObject(i);
                        productItem = new MyProductItem();
                        productItem.setProdId(jsonObject.getInt("prodId"));
                        productItem.setId(jsonObject.getInt("prodId"));
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

                        if(dbHelper.checkProdExistInShopCart(productItem.getProdId())){
                            productItem = dbHelper.getShopCartProductDetails(productItem.getProdId());
                            productItem.setProdQoh(jsonObject.getInt("prodQoh"));
                        }

                        if(!jsonObject.getString("productUnitList").equals("null")){
                            tempArray = jsonObject.getJSONArray("productUnitList");
                            int tempLen = tempArray.length();
                            List<ProductUnit> productUnitList = new ArrayList<>();
                            for(int unitCounter = 0; unitCounter< tempLen ; unitCounter++){
                                tempObject = tempArray.getJSONObject(unitCounter);
                                productUnit = new ProductUnit();
                                productUnit.setId(tempObject.getInt("id"));
                                productUnit.setUnitName(tempObject.getString("unitName"));
                                productUnit.setUnitValue(tempObject.getString("unitValue"));
                                productUnit.setStatus(tempObject.getString("status"));
                                productUnitList.add(productUnit);
                            }

                            productItem.setProductUnitList(productUnitList);
                        }

                        if(!jsonObject.getString("productSizeList").equals("null")){
                            tempArray = jsonObject.getJSONArray("productSizeList");
                            int tempLen = tempArray.length();
                            List<ProductSize> productSizeList = new ArrayList<>();
                            for(int unitCounter = 0; unitCounter< tempLen ; unitCounter++){
                                tempObject = tempArray.getJSONObject(unitCounter);
                                List<ProductColor> productColorList = new ArrayList<>();
                                productSize = new ProductSize();
                                productSize.setId(tempObject.getInt("id"));
                                productSize.setSize(tempObject.getString("size"));
                                productSize.setStatus(tempObject.getString("status"));
                                productSize.setProductColorList(productColorList);
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
                                        productColorList.add(productColor);
                                    }
                                }
                                productSizeList.add(productSize);
                            }

                            productItem.setProductSizeList(productSizeList);
                        }

                        if (!jsonObject.getString("barcodeList").equals("null")) {
                            JSONArray productBarCodeArray = jsonObject.getJSONArray("barcodeList");
                            len = productBarCodeArray.length();
                            JSONObject barcodeJsonObject = null;
                            List<Barcode> barcodeList = new ArrayList<>();
                            for (int j = 0; j < len; j++) {
                                barcodeJsonObject = productBarCodeArray.getJSONObject(j);
                                barcodeList.add(new Barcode(barcodeJsonObject.getString("barcode")));
                                // dbHelper.addProductBarcode(jsonObject.getInt("prodId"),jsonObject.getString("prodBarCode"));
                            }
                            productItem.setBarcodeList(barcodeList);
                        }

                        if (!jsonObject.getString("productDiscountOfferList").equals("null")) {
                            JSONArray freeArray = jsonObject.getJSONArray("productDiscountOfferList");
                            len = freeArray.length();
                            ProductDiscountOffer productDiscountOffer = null;
                            for (int k = 0; k < len; k++) {
                                tempObject = freeArray.getJSONObject(k);
                                Log.d("index ", "" + len);
                                productDiscountOffer = new ProductDiscountOffer();
                                productDiscountOffer.setId(tempObject.getInt("id"));
                                productDiscountOffer.setOfferName(tempObject.getString("offerName"));
                                productDiscountOffer.setProdBuyId(tempObject.getInt("prodBuyId"));
                                productDiscountOffer.setProdFreeId(tempObject.getInt("prodFreeId"));
                                productDiscountOffer.setProdBuyQty(tempObject.getInt("prodBuyQty"));
                                productDiscountOffer.setProdFreeQty(tempObject.getInt("prodFreeQty"));
                                productDiscountOffer.setStatus(tempObject.getString("status"));
                                productDiscountOffer.setStartDate(tempObject.getString("startDate"));
                                productDiscountOffer.setEndDate(tempObject.getString("endDate"));

                                //myProduct.setproductoffer
                                productItem.setOfferId(String.valueOf(productDiscountOffer.getId()));
                                productItem.setOfferType("free");
                                productItem.setProductOffer(productDiscountOffer);

                                //  dbHelper.addProductFreeOffer(productDiscountOffer, Utility.getTimeStamp(),Utility.getTimeStamp());
                            }
                            productItem.setProductOffer(productDiscountOffer);
                        }


                        if (!jsonObject.getString("productPriceOffer").equals("null")) {
                            JSONArray priceArray = jsonObject.getJSONArray("productPriceOffer");
                            len = priceArray.length();
                            JSONArray productPriceArray = null;
                            ProductComboOffer productPriceOffer = null;
                            ProductComboDetails productPriceDetails;
                            List<ProductComboDetails> productPriceOfferDetails = null;
                            for (int l = 0; l < len; l++) {
                                tempObject = priceArray.getJSONObject(l);
                                productPriceOffer = new ProductComboOffer();
                                productPriceOffer.setId(tempObject.getInt("id"));
                                productPriceOffer.setProdId(tempObject.getInt("prodId"));
                                productPriceOffer.setOfferName(tempObject.getString("offerName"));
                                productPriceOffer.setStatus(tempObject.getString("status"));
                                productPriceOffer.setStartDate(tempObject.getString("startDate"));
                                productPriceOffer.setEndDate(tempObject.getString("endDate"));
                                productPriceArray = tempObject.getJSONArray("productComboOfferDetails");

                                productItem.setOfferId(String.valueOf(productPriceOffer.getId()));
                                productItem.setOfferType("price");
                                productItem.setProductOffer(productPriceOffer);


                                productPriceOfferDetails = new ArrayList<>();
                                innerLen = productPriceArray.length();
                                for (int k = 0; k < innerLen; k++) {
                                    tempObject = productPriceArray.getJSONObject(k);
                                    productPriceDetails = new ProductComboDetails();
                                    productPriceDetails.setId(tempObject.getInt("id"));
                                    productPriceDetails.setPcodPcoId(tempObject.getInt("pcodPcoId"));
                                    productPriceDetails.setPcodProdQty(tempObject.getInt("pcodProdQty"));
                                    productPriceDetails.setPcodPrice((float) tempObject.getDouble("pcodPrice"));
                                    productPriceDetails.setStatus(tempObject.getString("status"));
                                    productPriceOfferDetails.add(productPriceDetails);
                                }
                                productPriceOffer.setProductComboOfferDetails(productPriceOfferDetails);
                            }
                            productItem.setProductOffer(productPriceOffer);
                        }

                        itemList.add(productItem);
                    }

                    myItemAdapter.notifyDataSetChanged();
                }
            }else if(apiName.equals("productDetails")){
                if(response.getString("status").equals("true")||response.getString("status").equals(true)){
                    JSONObject jsonObject = response.getJSONObject("result");
                    if(productDetailsType == 1){
                        ((MyProductItem)itemList.get(position)).setProdQoh(jsonObject.getInt("prodQoh"));
                        Log.d("Qoh ", ((MyProductItem)itemList.get(position)).getProdQoh()+"");
                        checkFreeProductOffer();
                    }else {
                        freeProdut = new MyProductItem();
                        freeProdut.setId(jsonObject.getInt("prodId"));
                        freeProdut.setProdId(jsonObject.getInt("prodId"));
                        freeProdut.setProdCatId(jsonObject.getInt("prodCatId"));
                        freeProdut.setProdSubCatId(jsonObject.getInt("prodSubCatId"));
                        freeProdut.setProdName(jsonObject.getString("prodName"));
                        freeProdut.setProdQoh(jsonObject.getInt("prodQoh"));
                        freeProdut.setQty(1);
                        freeProdut.setFreeProductPosition(position+1);
                        freeProdut.setProdMrp(Float.parseFloat(jsonObject.getString("prodMrp")));
                        freeProdut.setProdSp(0);
                        freeProdut.setProdCode(jsonObject.getString("prodCode"));
                        freeProdut.setIsBarCodeAvailable(jsonObject.getString("isBarcodeAvailable"));
                        //myProduct.setBarCode(productJArray.getJSONObject(i).getString("prodBarCode"));
                        freeProdut.setProdDesc(jsonObject.getString("prodDesc"));
                        freeProdut.setProdImage1(jsonObject.getString("prodImage1"));
                        freeProdut.setProdImage2(jsonObject.getString("prodImage2"));
                        freeProdut.setProdImage3(jsonObject.getString("prodImage3"));
                        freeProdut.setProdHsnCode(jsonObject.getString("prodHsnCode"));
                        freeProdut.setProdMfgDate(jsonObject.getString("prodMfgDate"));
                        freeProdut.setProdExpiryDate(jsonObject.getString("prodExpiryDate"));
                        freeProdut.setProdMfgBy(jsonObject.getString("prodMfgBy"));
                        freeProdut.setProdExpiryDate(jsonObject.getString("prodExpiryDate"));
                        freeProdut.setOfferId(jsonObject.getString("offerId"));
                        freeProdut.setProdCgst(Float.parseFloat(jsonObject.getString("prodCgst")));
                        freeProdut.setProdIgst(Float.parseFloat(jsonObject.getString("prodIgst")));
                        freeProdut.setProdSgst(Float.parseFloat(jsonObject.getString("prodSgst")));
                        freeProdut.setProdWarranty(Float.parseFloat(jsonObject.getString("prodWarranty")));
                        //myProduct.setSubCatName(subcatname);
                        onProductItemClicked(position,type);
                    }

                }else {
                    DialogAndToast.showToast("Something went wrong, Please try again", OffersActivity.this);
                }
            }
        }catch (JSONException jsonError){
            jsonError.printStackTrace();
        }
    }

    private int getBannerPosition(String name){
        int position = -1;
        if(name.contains("_")){
            try{
                position = Integer.parseInt(name.split("_")[0]);
            }catch (NumberFormatException nfe){
                nfe.printStackTrace();
            }
        }
        return position;
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

    private void getProductDetails(String prodId){
        if(productDetailsType==1)
            showProgress(true);
        Map<String,String> params=new HashMap<>();
        params.put("id", prodId); // as per user selected category from top horizontal categories list
        params.put("code", shopCode);
        // params.put("dbName",shopCode);
        Log.d(TAG, params.toString());
        String url=getResources().getString(R.string.cust_url)+"/api/customers/products/ret_products_details";
        jsonObjectApiRequest(Request.Method.POST, url,new JSONObject(params),"productDetails");
    }

    public void updateCart(int type, int position){
        Log.d("clicked Position ", position+"");
        this.position = position;
        this.type = type;
        if(type==2){
            productDetailsType = 1;
            getProductDetails(""+((MyProductItem)itemList.get(position)).getProdId());
        }else{
            onProductItemClicked(position,type);
        }
    }

    @Override
    public void onItemClicked(int position, int type) {
        if(type == 3){
            MyProductItem item = (MyProductItem)itemList.get(position);
            showOfferDescription(item);
        }else if(type == 1 || type == 2){
            MyProductItem item = (MyProductItem)itemList.get(position);
            this.position = position;
            this.myProduct = item;
            if(type == 1){
                updateCart(type,position);
            }else if(type == 2){
                updateCart(type,position);
            }
        }else if(type == 4){
            HomeListItem myItem = (HomeListItem)itemList.get(position);
            Intent intent = new Intent(OffersActivity.this,ShoppursProductListActivity.class);
            intent.putExtra("flag","shoppursSubCatProducts");
            intent.putExtra("subcatid",myItem.getId());
            intent.putExtra("subcatname",myItem.getTitle());
            intent.putExtra("catId","");
            startActivity(intent);
        }
    }

    public void onProductItemClicked(int position,int type) {
        this.position = position;
        this.myProduct = (MyProductItem)itemList.get(position);

        if(type == 1){
            if(myProduct.getQty() > 0){
                if(myProduct.getQty() == 1){
                    counter--;
                    //dbHelper.removeProductFromCart(myProduct.getProdBarCode());
                    dbHelper.removeProductFromShopCart(myProduct.getProdId());
                    dbHelper.removePriceProductFromCart(""+myProduct.getProdId());
                    Object ob = myProduct.getProductOffer();
                    if(ob instanceof ProductDiscountOffer){
                        ProductDiscountOffer productDiscountOffer = (ProductDiscountOffer)ob;
                        if(productDiscountOffer.getProdBuyId() != productDiscountOffer.getProdFreeId())
                            dbHelper.removeFreeProductFromShopCart(productDiscountOffer.getProdFreeId());
                    }else if(ob instanceof ProductComboOffer){
                        ProductComboOffer productComboOffer = (ProductComboOffer)ob;
                        for(ProductComboDetails productPriceDetails : productComboOffer.getProductComboOfferDetails()){
                            dbHelper.removePriceProductDetailsFromCart(String.valueOf(productPriceDetails.getId()));
                        }
                    }
                    myProduct.setQty(0);
                    myProduct.setTotalAmount(0);
                    myItemAdapter.notifyItemChanged(position);
                    updateCartCount();
                    Log.d("onRemove Qyantity ", myProduct.getQty()+"");
                }else{
                    int qty = myProduct.getQty() - 1;
                    float netSellingPrice = getOfferAmount(myProduct,type);
                    myProduct.setQty(myProduct.getQty());
                    qty = myProduct.getQty();
                    Log.i(TAG,"netSellingPrice "+netSellingPrice);
                    float amount = 0;
                    amount = myProduct.getTotalAmount() - netSellingPrice;
                    Log.i(TAG,"tot amount "+amount);
                    myProduct.setTotalAmount(amount);
                    Object ob = myProduct.getProductOffer();
                    if(ob instanceof ProductComboOffer){
                        myProduct.setProdSp(amount/qty);
                    }
                    dbHelper.updateShopCartData(myProduct);
                    myItemAdapter.notifyItemChanged(position);
                    updateCartCount();
                }
            }

        }else if(type == 2){
            if(myProduct.getQty() >= myProduct.getProdQoh()){
                myItemAdapter.notifyDataSetChanged();
                DialogAndToast.showDialog("There are no more stocks",this);
            }else{
                int qty = myProduct.getQty() + 1;
                myProduct.setQty(qty);
                if(qty == 1){
                    counter++;
                    myProduct.setFreeProductPosition(counter);
                    dbHelper.addProductToShopCart(myProduct);
                }
                float netSellingPrice = getOfferAmount(myProduct,type);
                float amount = 0;
                Log.i(TAG,"netSellingPrice "+netSellingPrice);
                amount = myProduct.getTotalAmount() + netSellingPrice;
                Log.i(TAG,"tot amount "+amount);
                myProduct.setTotalAmount(amount);
                Object ob = myProduct.getProductOffer();
                if(ob instanceof ProductComboOffer){
                    myProduct.setProdSp(amount/qty);
                }
                qty = myProduct.getQty();
                Log.i(TAG,"qty "+qty);
                //myProduct.setQuantity(myProduct.getQuantity());
                dbHelper.updateShopCartData(myProduct);
                myItemAdapter.notifyItemChanged(position);
                myItemAdapter.notifyDataSetChanged();
                updateCartCount();
            }
            //  }
        }
    }

    private void checkFreeProductOffer(){
        Object ob = ((MyProductItem)itemList.get(position)).getProductOffer();
        if(type ==2 && ob instanceof ProductDiscountOffer){
            ProductDiscountOffer productDiscountOffer = (ProductDiscountOffer)ob;
            if(productDiscountOffer.getProdBuyId()!= productDiscountOffer.getProdFreeId()){
                productDetailsType = 2;
                getProductDetails(String.valueOf(productDiscountOffer.getProdFreeId()));
            }else{
                onProductItemClicked(position,type);
            }
        }else{
            onProductItemClicked(position,type);
        }
    }

    float totalPrice;
    public void updateCartCount(){
        totalPrice = 0;
        if(dbHelper.getCartCount()>0){
            findViewById(R.id.linear_footer).setVisibility(View.GONE);
            rlfooterviewcart.setVisibility(View.VISIBLE);
            viewCart.setVisibility(View.VISIBLE);
            viewCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(OffersActivity.this, ShopCartActivity.class));
                }
            });
            totalPrice = dbHelper.getTotalPriceShopCart();
            Log.d("onRemove totalPrice ",totalPrice+"" );

            float deliveryDistance = 0;
            /*if(deliveryDistance > ){
                float diffKms = deliveryDistance -  sharedPreferences.getInt(Constants.MIN_DELIVERY_DISTANCE,0);
                int intKms = (int)diffKms;
                float decimalKms = diffKms - intKms;

                int chargesPerKm = sharedPreferences.getInt(Constants.CHARGE_AFTER_MIN_DISTANCE,0);
                deliveryCharges = intKms * chargesPerKm + decimalKms * chargesPerKm;
                tvDeliveryCharges.setText(Utility.numberFormat(deliveryCharges));
            }else{
                tvDeliveryCharges.setText("0.00");
            }*/
            //totalPrice = totalPrice + deliveryCharges;


            cartItemPrice.setText("Amount "+ Utility.numberFormat(totalPrice));
            cartItemCount.setText("Item "+String.valueOf(dbHelper.getCartCount()));
        }else{
            findViewById(R.id.linear_footer).setVisibility(View.VISIBLE);
            rlfooterviewcart.setVisibility(View.GONE);
        }

    }

    public void showOfferDescription(MyProductItem item){
        String offerName = null;
        rlOfferDesc.setVisibility(View.VISIBLE);
        ImageView iv_clear = findViewById(R.id.iv_clear);
        TextView tvOfferName = findViewById(R.id.text_offer_name);
        findViewById(R.id.relative_footer_action).setBackgroundColor(colorTheme);
        TextView tv = findViewById(R.id.text_action);
        tv.setText("OKAY! GOT IT");

        iv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlOfferDesc.setVisibility(View.GONE);
            }
        });
        findViewById(R.id.relative_footer_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rlOfferDesc.setVisibility(View.GONE);
            }
        });

        List<String> offerDescList = new ArrayList<>();
        if(item.getProductOffer() instanceof ProductComboOffer) {
            ProductComboOffer productComboOffer = (ProductComboOffer) item.getProductOffer();
            offerName = productComboOffer.getOfferName();
            float totOfferAmt = 0f;
            for(ProductComboDetails productComboDetails : productComboOffer.getProductComboOfferDetails()){
                totOfferAmt = productComboDetails.getPcodPrice();
                offerDescList.add("Buy "+productComboDetails.getPcodProdQty()+" at Rs "+
                        Utility.numberFormat(totOfferAmt));
            }
            offerDescList.add("Offer valid till "+Utility.parseDate(productComboOffer.getEndDate(),"yyyy-MM-dd",
                    "EEE dd MMMM, yyyy")+" 23:59 PM");
        }else if(item.getProductOffer() instanceof ProductDiscountOffer) {
            ProductDiscountOffer productDiscountOffer = (ProductDiscountOffer) item.getProductOffer();
            offerName = productDiscountOffer.getOfferName();
        }
        tvOfferName.setText(offerName);

        RecyclerView recyclerViewOfferDesc=findViewById(R.id.recycler_view_offer_desc);
        recyclerViewOfferDesc.setHasFixedSize(true);
        final RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerViewOfferDesc.setLayoutManager(layoutManager);
        recyclerViewOfferDesc.setItemAnimator(new DefaultItemAnimator());
        OfferDescAdapter offerDescAdapter =new OfferDescAdapter(this,offerDescList);
        recyclerViewOfferDesc.setAdapter(offerDescAdapter);
        recyclerViewOfferDesc.setNestedScrollingEnabled(false);
    }


    private float getOfferAmount(MyProductItem item,int type){
        float amount = 0f;
        int qty = item.getQty();
        if(item.getProductOffer() != null && item.getProductOffer() instanceof ProductComboOffer){
            ProductComboOffer productComboOffer = (ProductComboOffer)item.getProductOffer();
            if(qty >= 1){
                int maxSize = productComboOffer.getProductComboOfferDetails().size();
                int mod = qty % maxSize;
                Log.i(TAG,"mod "+mod);
                if(mod == 0){
                    mod = maxSize;
                }
                amount = getOfferPrice(mod,item.getProdSp(),productComboOffer.getProductComboOfferDetails());
            }else{
                amount = item.getProdSp();
            }

            if(type == 1)
                item.setQty(item.getQty() - 1);

        }else if(item.getProductOffer() != null && item.getProductOffer() instanceof ProductDiscountOffer){

            ProductDiscountOffer productDiscountOffer = (ProductDiscountOffer)item.getProductOffer();
            amount = item.getProdSp();
            if(type == 1){
                if(productDiscountOffer.getProdBuyId() == productDiscountOffer.getProdFreeId()){
                    Log.i(TAG,"item qty "+item.getQty()+" offer buy qty"+productDiscountOffer.getProdBuyQty());
                    Log.i(TAG,"minus mode "+(item.getQty() - item.getOfferCounter()-1)% productDiscountOffer.getProdBuyQty());
                    if((item.getQty() - item.getOfferCounter() -1)% productDiscountOffer.getProdBuyQty() ==
                            (productDiscountOffer.getProdBuyQty()-1)){
                        item.setQty(item.getQty() - 2);
                        item.setOfferCounter(item.getOfferCounter() - 1);
                        dbHelper.updateOfferCounterShopCartData(item.getOfferCounter(),item.getProdId());

                    }else{
                        item.setQty(item.getQty() - 1);
                    }
                }else{
                    item.setQty(item.getQty() - 1);
                    Log.i(TAG,"minus mode "+item.getQty() % productDiscountOffer.getProdBuyQty());
                    if(item.getQty() % productDiscountOffer.getProdBuyQty() == (productDiscountOffer.getProdBuyQty()-1)){
                        item.setOfferCounter(item.getOfferCounter() - 1);
                        if(item.getOfferCounter() == 0){
                            dbHelper.removeFreeProductFromShopCart(productDiscountOffer.getProdFreeId());
                        }else{
                            dbHelper.updateFreeShopCartData(productDiscountOffer.getProdFreeId(),item.getOfferCounter(),0f);
                            dbHelper.updateOfferCounterShopCartData(item.getOfferCounter(),item.getProdId());
                        }
                    }

                }
            }else if(type == 2){
                if(productDiscountOffer.getProdBuyId() == productDiscountOffer.getProdFreeId()){
                    Log.i(TAG,"Same product");
                    Log.i(TAG,"item qty "+item.getQty()+" offer buy qty"+productDiscountOffer.getProdBuyQty());
                    Log.i(TAG,"plus mode "+(item.getQty() - item.getOfferCounter())% productDiscountOffer.getProdBuyQty());
                    if((item.getQty() - item.getOfferCounter())% productDiscountOffer.getProdBuyQty() == 0){
                        item.setQty(item.getQty() + 1);
                        item.setOfferCounter(item.getOfferCounter() + 1);
                        dbHelper.updateOfferCounterShopCartData(item.getOfferCounter(),item.getProdId());
                    }else{

                    }
                }else{
                    Log.i(TAG,"Different product");
                    if(item.getQty() % productDiscountOffer.getProdBuyQty() == 0){
                        item.setOfferCounter(item.getOfferCounter() + 1);
                        MyProductItem item1 = null;
                        if(item.getOfferCounter() == 1){
                            item1 = freeProdut;
                            item1.setProdSp(0f);
                            item1.setQty(1);
                            item1.setFreeProductPosition(item.getFreeProductPosition());
                            dbHelper.addProductToShopCart(item1);
                            dbHelper.updateFreePositionShopCartData(item.getFreeProductPosition(),item.getProdId());
                            dbHelper.updateOfferCounterShopCartData(item.getOfferCounter(),item.getProdId());
                            Log.i(TAG,"Different product added to cart");
                        }else{
                            //  item1 = itemList.get(item.getFreeProductPosition());
                            //   item1.setQty(item.getOfferCounter());
                            dbHelper.updateFreeShopCartData(productDiscountOffer.getProdFreeId(),item.getOfferCounter(),0f);
                            dbHelper.updateOfferCounterShopCartData(item.getOfferCounter(),item.getProdId());
                            Log.i(TAG,"Different product updated in cart");
                        }
                        //  myItemAdapter.notifyDataSetChanged();
                    }

                }

            }else{
                amount = item.getProdSp();
            }

        }else{
            if(type == 1)
                item.setQty(item.getQty() - 1);
            amount = item.getProdSp();
        }

        return amount;
    }

    private float getOfferPrice(int qty,float sp,List<ProductComboDetails> productComboDetailsList){
        float amount = 0f;
        int i = -1;
        for(ProductComboDetails productComboDetails:productComboDetailsList){
            if(productComboDetails.getPcodProdQty() == qty){
                amount = productComboDetails.getPcodPrice();
                if(qty != 1){
                    amount = amount - productComboDetailsList.get(i).getPcodPrice();
                }
                Log.i(TAG,"offer price "+amount);
                break;
            }else{
                amount = sp;
            }
            i++;
        }
        Log.i(TAG,"final selling price "+amount);
        return amount;
    }

    @Override
    public void onImageClicked(int position, int type, View view) {
        final MyProductItem product = (MyProductItem) itemList.get(position);
        showImageDialog(product.getProdImage1(), view);
    }
}
