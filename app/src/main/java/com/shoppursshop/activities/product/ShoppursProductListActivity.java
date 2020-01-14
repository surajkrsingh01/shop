package com.shoppursshop.activities.product;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shoppursshop.R;
import com.shoppursshop.activities.base.NetworkBaseActivity;
import com.shoppursshop.activities.ScannarActivity;
import com.shoppursshop.activities.order.ShopCartActivity;
import com.shoppursshop.adapters.OfferDescAdapter;
import com.shoppursshop.adapters.ShopProductListAdapter;
import com.shoppursshop.adapters.TopCategoriesAdapter;
import com.shoppursshop.fragments.SearchProductFragment;
import com.shoppursshop.interfaces.MyImageClickListener;
import com.shoppursshop.interfaces.MyItemClickListener;
import com.shoppursshop.interfaces.MyItemTypeClickListener;
import com.shoppursshop.models.Barcode;
import com.shoppursshop.models.MyProductItem;
import com.shoppursshop.models.ProductColor;
import com.shoppursshop.models.ProductComboDetails;
import com.shoppursshop.models.ProductComboOffer;
import com.shoppursshop.models.ProductDiscountOffer;
import com.shoppursshop.models.ProductSize;
import com.shoppursshop.models.ProductUnit;
import com.shoppursshop.models.SubCategory;
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

public class ShoppursProductListActivity extends NetworkBaseActivity implements MyItemClickListener,MyItemTypeClickListener,
        MyImageClickListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerViewProduct, recyclerViewCategory;
    private TextView tvNoData, cartItemCount, cartItemPrice, viewCart;
    private TextView text_shop_name, tv_shortName, text_mobile, text_address,
            text_state_city, text_left_label, text_right_label;
    private ProgressBar progressBar;
    private RelativeLayout rlfooterviewcart,rlOfferDesc;
    private ShopProductListAdapter shopProductAdapter;
    private TopCategoriesAdapter categoriesAdapter;
    private List<SubCategory> subCategoryList;
    private List<MyProductItem> myProductList;
    // private CartItem cartItem;
    private MyProductItem myProduct, freeProdut;
    private ImageView image_view_shop, image_search, image_scan;
    private String flag,shopCode, shopName, shopImage, shopMobile, address, statecity, catId, selectdSubCatId, selectedSubCatName, custCode, shopdbname, custdbname, dbuser, dbpassword;

    private int position, type, productDetailsType, selectdSubCatPosition;
    private int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoppurs_product_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();
    }

    private void init(){
        flag = getIntent().getStringExtra("flag");
        shopCode = "SHP1";
        shopName = "Areeva Products & Services";
        shopdbname = "SHP1";
        shopImage = "";
        shopMobile = "9718181697";
        address = "Harikunj Apartments";
        statecity = "Gurgaon, Haryana";

       /* dbHelper.deleteTable(DbHelper.SHOP_CART_TABLE);
        dbHelper.deleteTable(DbHelper.CART_PROD_PRICE_TABLE);
        dbHelper.deleteTable(DbHelper.CART_PROD_PRICE_DETAIL_TABLE);*/


        rlOfferDesc = findViewById(R.id.rl_offer_desc);
        text_shop_name = findViewById(R.id.text_shop_name);
        tv_shortName = findViewById(R.id.tv_shortName);
        image_view_shop = findViewById(R.id.image_view_shop);
        text_mobile = findViewById(R.id.text_mobile);
        text_address = findViewById(R.id.text_address);
        text_state_city = findViewById(R.id.text_state_city);
        image_search = findViewById(R.id.image_search);
        image_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchProductFragment bottomSearchFragment = new SearchProductFragment();
                Bundle bundle = new Bundle();
                bundle.putString("shopCode", shopCode);
                bundle.putString("shopName", shopName);
                bundle.putString("shopAddress", address);
                bundle.putString("shopMobile", shopMobile);
                bottomSearchFragment.setArguments(bundle);
                bottomSearchFragment.setCallingActivityName("ShopProductListActivity", sharedPreferences, isDarkTheme);
                bottomSearchFragment.setSubCatName(selectedSubCatName);
                bottomSearchFragment.setSubcatId(selectdSubCatId);
                bottomSearchFragment.setMyItemClickListener(ShoppursProductListActivity.this);
                bottomSearchFragment.show(getSupportFragmentManager(), bottomSearchFragment.getTag());
            }
        });

        image_scan = findViewById(R.id.image_scan);
        image_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShoppursProductListActivity.this, ScannarActivity.class);
                intent.putExtra("flag","scan");
                intent.putExtra("type","scanProducts");
                intent.putExtra("shopCode",shopCode);
                // startActivity(intent);
                startActivityForResult(intent,112);
            }
        });

        text_left_label = findViewById(R.id.text_left_label);
        text_right_label = findViewById(R.id.text_right_label);
        text_left_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(ShopProductListActivity.this, ShopListActivity.class));
                finish();
            }
        });

        recyclerViewCategory = findViewById(R.id.recycler_viewCategory);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        recyclerViewProduct = findViewById(R.id.recycler_viewProduct);
        tvNoData = findViewById(R.id.tvNoData);
        progressBar=findViewById(R.id.progress_bar);
        rlfooterviewcart = findViewById(R.id.rlfooterviewcart);
        rlfooterviewcart.setBackgroundColor(colorTheme);
        cartItemCount= findViewById(R.id.itemCount);
        cartItemPrice = findViewById(R.id.itemPrice);
        viewCart = findViewById(R.id.viewCart);

        subCategoryList = new ArrayList<>();
        recyclerViewCategory.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        recyclerViewCategory.setLayoutManager(layoutManager);
        recyclerViewCategory.setItemAnimator(new DefaultItemAnimator());
        categoriesAdapter = new TopCategoriesAdapter(this, subCategoryList);
        recyclerViewCategory.setAdapter(categoriesAdapter);

        myProductList = new ArrayList<>();
        recyclerViewProduct.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManagerProduct = new LinearLayoutManager(this);
        recyclerViewProduct.setLayoutManager(layoutManagerProduct);
        recyclerViewProduct.setItemAnimator(new DefaultItemAnimator());
        shopProductAdapter = new ShopProductListAdapter(this, myProductList);
        shopProductAdapter.setDarkTheme(isDarkTheme);
        shopProductAdapter.setMyImageClickListener(this);
        shopProductAdapter.setMyItemTypeClickListener(this);
        recyclerViewProduct.setAdapter(shopProductAdapter);



        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

      //  setShopDetails();

        if(flag.equals("shoppursSubCatProducts")){
            catId = getIntent().getStringExtra("catId");
            selectdSubCatId = getIntent().getStringExtra("subcatid");
            selectedSubCatName = getIntent().getStringExtra("subcatname");
            SubCategory subCategory = new SubCategory();
            subCategory.setId(selectdSubCatId);
            subCategory.setCatId("");
            subCategory.setName(selectedSubCatName);
            subCategoryList.add(subCategory);
            categoriesAdapter.notifyDataSetChanged();
            getProducts(selectdSubCatId);
        }else if(flag.equals("shoppursProducts")){
            getsubCategories();
        }else if(flag.equals("shoppursOfferProducts")){
            recyclerViewCategory.setVisibility(View.GONE);
            findViewById(R.id.view_separator).setVisibility(View.GONE);
            getProductsOnOffers();
        }

        getShopDetails();
    }

    private void setShopDetails(){
        text_shop_name.setText(shopName);

        if(shopName.length()>1) {
            tv_shortName.setText(shopName.substring(0, 1));
            //image_view_shop .setText(shopName);
            text_mobile.setText(shopMobile);
            text_address.setText(address);
            text_state_city.setText(statecity);

            String initials = "";
            if (shopName.contains(" ")) {
                String[] nameArray = shopName.split(" ");
                initials = nameArray[0].substring(0, 1) + nameArray[1].substring(0, 1);
            } else {
                initials = shopName.substring(0, 2);
            }

            tv_shortName.setText(initials);
        }

        if(shopImage !=null && shopImage.contains("http")){
            tv_shortName.setVisibility(View.GONE);
            image_view_shop.setVisibility(View.VISIBLE);
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
            requestOptions.dontTransform();
            // requestOptions.override(Utility.dpToPx(150, context), Utility.dpToPx(150, context));
            // requestOptions.centerCrop();
            requestOptions.skipMemoryCache(false);

            Glide.with(this)
                    .load(shopImage)
                    .apply(requestOptions)
                    .into(image_view_shop);

        }else{
            tv_shortName.setVisibility(View.VISIBLE);
            image_view_shop.setVisibility(View.GONE);
        }
    }

    private void getsubCategories(){

        Map<String,String> params=new HashMap<>();
        params.put("code", shopCode);
        params.put("dbName", sharedPreferences.getString(Constants.DB_NAME, ""));
        String url=getResources().getString(R.string.url)+"/api/categories/retailer_sub_categories";
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url, new JSONObject(params),"get_subcategories");
    }

    private void getShopDetails(){
        Map<String,String> params=new HashMap<>();
        params.put("shopCode", shopCode);
        params.put("dbName", sharedPreferences.getString(Constants.DB_NAME, ""));
        String url=getResources().getString(R.string.url)+"/api/user/shopDetails";
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url, new JSONObject(params),"shopDetails");
    }

    public void getProducts(String subCatId){
        selectdSubCatId = subCatId;
        myProductList.clear();

        Map<String,String> params=new HashMap<>();
        params.put("subCatId", subCatId); // as per user selected category from top horizontal categories list
        params.put("shopCode", shopCode);
        params.put("dbName",shopdbname);
        params.put("limit",""+limit);
        params.put("offset",""+offset);
        Log.d(TAG, params.toString());
        String url="";
        if(flag.equals("shoppursProducts")){
            url=getResources().getString(R.string.cust_url)+"/api/customers/products/ret_productslist";
        }else{
            url=getResources().getString(R.string.cust_url)+"/api/customers/products/ret_productslist_with_offers_by_cat";
        }
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST, url,new JSONObject(params),"productfromshop");
    }

    public void getProductsOnOffers(){
        Map<String,String> params=new HashMap<>();
        params.put("dbName",shopdbname);
        params.put("limit",""+limit);
        params.put("offset",""+offset);
        Log.d(TAG, params.toString());
        String url=getResources().getString(R.string.cust_url)+"/api/customers/products/ret_productslist_with_offers";
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST, url,new JSONObject(params),"productfromshop");
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
            getProductDetails(""+myProductList.get(position).getProdId());
        }else{
            onProductItemClicked(position,type);
        }
    }

    @Override
    public void onImageClicked(int position, int type, View view) {
        showImageDialog( myProductList.get(position).getProdImage1(),view);
    }

    @Override
    public void onItemClicked(int position) {
        int i = 0;
        for(MyProductItem item : myProductList){

            if(dbHelper.checkProdExistInShopCart(item.getProdId())){
                MyProductItem item1 = dbHelper.getShopCartProductDetails(item.getProdId());
                item.setQty(item1.getQty());
                shopProductAdapter.notifyItemChanged(i);
                break;
            }
            i++;
        }
       onResume();
    }

    @Override
    public void onItemClicked(int position,int type) {
        if(type == 3){
            showOfferDescription(myProductList.get(position));
        }else{
            this.position = position;
            this.myProduct = myProductList.get(position);
            if(type == 1){
                updateCart(type,position);
            }else if(type == 2){
                updateCart(type,position);
            }
        }

    }

    public void onProductItemClicked(int position,int type) {
        this.position = position;
        this.myProduct = myProductList.get(position);

        if(type == 1){
            if(myProduct.getQty() > 0){
                if(myProduct.getQty() == 1){
                    counter--;
                    //dbHelper.removeProductFromCart(myProduct.getProdBarCode());
                    dbHelper.removeProductFromShopCart(myProduct.getId());
                    dbHelper.removePriceProductFromCart(""+myProduct.getId());
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
                    shopProductAdapter.notifyItemChanged(position);
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
                    shopProductAdapter.notifyItemChanged(position);
                    updateCartCount();
                }
            }

        }else if(type == 2){
            if(myProduct.getQty() >= myProduct.getProdQoh()){
                shopProductAdapter.notifyDataSetChanged();
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
                shopProductAdapter.notifyItemChanged(position);
                shopProductAdapter.notifyDataSetChanged();
                updateCartCount();
            }
            //  }
        }
    }

    private void checkFreeProductOffer(){
        Object ob = myProductList.get(position).getProductOffer();
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

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {
        showProgressBar(false);
        try {
            // JSONObject jsonObject=response.getJSONObject("response");
            Log.d("response", response.toString());
            if(apiName.equals("productfromshop")){
                if(response.getString("status").equals("true")||response.getString("status").equals(true)) {
                    JSONObject dataObject;
                    if(!response.getString("result").equals("null")){
                        JSONArray productJArray = response.getJSONArray("result");

                        for (int i = 0; i < productJArray.length(); i++) {
                            MyProductItem myProduct = new MyProductItem();
                            myProduct.setId(productJArray.getJSONObject(i).getInt("prodId"));
                            myProduct.setProdId(productJArray.getJSONObject(i).getInt("prodId"));
                            myProduct.setProdCatId(productJArray.getJSONObject(i).getInt("prodCatId"));
                            myProduct.setProdSubCatId(productJArray.getJSONObject(i).getInt("prodSubCatId"));
                            myProduct.setProdName(productJArray.getJSONObject(i).getString("prodName"));
                            myProduct.setProdQoh(productJArray.getJSONObject(i).getInt("prodQoh"));
                            Log.d("Qoh ", myProduct.getProdQoh()+"");
                            myProduct.setProdMrp(Float.parseFloat(productJArray.getJSONObject(i).getString("prodMrp")));
                            myProduct.setProdSp(Float.parseFloat(productJArray.getJSONObject(i).getString("prodSp")));
                            myProduct.setProdCode(productJArray.getJSONObject(i).getString("prodCode"));
                            myProduct.setIsBarCodeAvailable(productJArray.getJSONObject(i).getString("isBarcodeAvailable"));
                            //myProduct.setBarCode(productJArray.getJSONObject(i).getString("prodBarCode"));
                            myProduct.setProdDesc(productJArray.getJSONObject(i).getString("prodDesc"));
                            myProduct.setProdImage1(productJArray.getJSONObject(i).getString("prodImage1"));
                            myProduct.setProdImage2(productJArray.getJSONObject(i).getString("prodImage2"));
                            myProduct.setProdImage3(productJArray.getJSONObject(i).getString("prodImage3"));
                            myProduct.setProdHsnCode(productJArray.getJSONObject(i).getString("prodHsnCode"));
                            myProduct.setProdMfgDate(productJArray.getJSONObject(i).getString("prodMfgDate"));
                            myProduct.setProdExpiryDate(productJArray.getJSONObject(i).getString("prodExpiryDate"));
                            myProduct.setProdMfgBy(productJArray.getJSONObject(i).getString("prodMfgBy"));
                            myProduct.setProdExpiryDate(productJArray.getJSONObject(i).getString("prodExpiryDate"));
                            myProduct.setOfferId(productJArray.getJSONObject(i).getString("offerId"));
                            myProduct.setProdCgst(Float.parseFloat(productJArray.getJSONObject(i).getString("prodCgst")));
                            myProduct.setProdIgst(Float.parseFloat(productJArray.getJSONObject(i).getString("prodIgst")));
                            myProduct.setProdSgst(Float.parseFloat(productJArray.getJSONObject(i).getString("prodSgst")));
                            myProduct.setProdWarranty(Float.parseFloat(productJArray.getJSONObject(i).getString("prodWarranty")));
                            //myProduct.setSubCatName(subcatname);

                            if(dbHelper.checkProdExistInShopCart(myProduct.getProdId())){
                                myProduct = dbHelper.getShopCartProductDetails(myProduct.getProdId());
                                myProduct.setProdQoh(productJArray.getJSONObject(i).getInt("prodQoh"));
                            }

                            int innerLen = 0;
                            int len = 0;

                            JSONArray tempArray = null;
                            JSONObject jsonObject = null, tempObject = null;
                            ProductUnit productUnit = null;
                            ProductSize productSize = null;
                            ProductColor productColor = null;

                            if (!productJArray.getJSONObject(i).getString("productUnitList").equals("null")) {
                                tempArray = productJArray.getJSONObject(i).getJSONArray("productUnitList");
                                int tempLen = tempArray.length();
                                List<ProductUnit> productUnitList = new ArrayList<>();

                                for (int unitCounter = 0; unitCounter < tempLen; unitCounter++) {
                                    tempObject = tempArray.getJSONObject(unitCounter);
                                    productUnit = new ProductUnit();
                                    productUnit.setId(tempObject.getInt("id"));
                                    productUnit.setUnitName(tempObject.getString("unitName"));
                                    productUnit.setUnitValue(tempObject.getString("unitValue"));
                                    productUnit.setStatus(tempObject.getString("status"));
                                    productUnitList.add(productUnit);
                                }
                                myProduct.setProductUnitList(productUnitList);
                            }

                            if (!productJArray.getJSONObject(i).getString("productSizeList").equals("null")) {
                                tempArray = productJArray.getJSONObject(i).getJSONArray("productSizeList");
                                int tempLen = tempArray.length();
                                List<ProductSize> productSizeList = new ArrayList<>();

                                for (int unitCounter = 0; unitCounter < tempLen; unitCounter++) {
                                    List<ProductColor> productColorList = new ArrayList<>();
                                    tempObject = tempArray.getJSONObject(unitCounter);
                                    productSize = new ProductSize();
                                    productSize.setId(tempObject.getInt("id"));
                                    productSize.setSize(tempObject.getString("size"));
                                    productSize.setStatus(tempObject.getString("status"));
                                    productSize.setProductColorList(productColorList);

                                    if (!productJArray.getJSONObject(i).getString("productSizeList").equals("null")) {
                                        JSONArray colorArray = tempObject.getJSONArray("productColorList");
                                        for (int colorCounter = 0; colorCounter < colorArray.length(); colorCounter++) {
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
                                myProduct.setProductSizeList(productSizeList);
                            }


                            if (!productJArray.getJSONObject(i).getString("barcodeList").equals("null")) {
                                JSONArray productBarCodeArray = productJArray.getJSONObject(i).getJSONArray("barcodeList");
                                len = productBarCodeArray.length();
                                JSONObject barcodeJsonObject = null;
                                List<Barcode> barcodeList = new ArrayList<>();
                                for (int j = 0; j < len; j++) {
                                    barcodeJsonObject = productBarCodeArray.getJSONObject(j);
                                    barcodeList.add(new Barcode(barcodeJsonObject.getString("barcode")));
                                    // dbHelper.addProductBarcode(jsonObject.getInt("prodId"),jsonObject.getString("prodBarCode"));
                                }
                                myProduct.setBarcodeList(barcodeList);
                            }

                            if (!productJArray.getJSONObject(i).getString("productDiscountOfferList").equals("null")) {
                                JSONArray freeArray = productJArray.getJSONObject(i).getJSONArray("productDiscountOfferList");
                                len = freeArray.length();
                                ProductDiscountOffer productDiscountOffer = null;
                                for (int k = 0; k < len; k++) {
                                    dataObject = freeArray.getJSONObject(k);
                                    Log.d("index ", "" + len);
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

                                    //myProduct.setproductoffer
                                    myProduct.setOfferId(String.valueOf(productDiscountOffer.getId()));
                                    myProduct.setOfferType("free");
                                    myProduct.setProductOffer(productDiscountOffer);

                                    //  dbHelper.addProductFreeOffer(productDiscountOffer, Utility.getTimeStamp(),Utility.getTimeStamp());
                                }
                                myProduct.setProductOffer(productDiscountOffer);
                            }


                            if (!productJArray.getJSONObject(i).getString("productPriceOffer").equals("null")) {
                                JSONArray priceArray = productJArray.getJSONObject(i).getJSONArray("productPriceOffer");
                                len = priceArray.length();
                                JSONArray productPriceArray = null;
                                ProductComboOffer productPriceOffer = null;
                                ProductComboDetails productPriceDetails;
                                List<ProductComboDetails> productPriceOfferDetails = null;
                                for (int l = 0; l < len; l++) {
                                    dataObject = priceArray.getJSONObject(l);
                                    productPriceOffer = new ProductComboOffer();
                                    productPriceOffer.setId(dataObject.getInt("id"));
                                    productPriceOffer.setProdId(dataObject.getInt("prodId"));
                                    productPriceOffer.setOfferName(dataObject.getString("offerName"));
                                    productPriceOffer.setStatus(dataObject.getString("status"));
                                    productPriceOffer.setStartDate(dataObject.getString("startDate"));
                                    productPriceOffer.setEndDate(dataObject.getString("endDate"));
                                    productPriceArray = dataObject.getJSONArray("productComboOfferDetails");

                                    myProduct.setOfferId(String.valueOf(productPriceOffer.getId()));
                                    myProduct.setOfferType("price");
                                    myProduct.setProductOffer(productPriceOffer);


                                    productPriceOfferDetails = new ArrayList<>();
                                    innerLen = productPriceArray.length();
                                    for (int k = 0; k < innerLen; k++) {
                                        dataObject = productPriceArray.getJSONObject(k);
                                        productPriceDetails = new ProductComboDetails();
                                        productPriceDetails.setId(dataObject.getInt("id"));
                                        productPriceDetails.setPcodPcoId(dataObject.getInt("pcodPcoId"));
                                        productPriceDetails.setPcodProdQty(dataObject.getInt("pcodProdQty"));
                                        productPriceDetails.setPcodPrice((float) dataObject.getDouble("pcodPrice"));
                                        if(k==0)
                                            myProduct.setProdSp(productPriceDetails.getPcodPrice());
                                        productPriceDetails.setStatus(dataObject.getString("status"));
                                        productPriceOfferDetails.add(productPriceDetails);
                                    }
                                    productPriceOffer.setProductComboOfferDetails(productPriceOfferDetails);
                                }
                                myProduct.setProductOffer(productPriceOffer);
                            }

                            myProductList.add(myProduct);

                        }
                    }
                    if(myProductList.size()>0){
                        shopProductAdapter.notifyDataSetChanged();
                    }else {
                        showNoData(true);
                    }

                }else {
                    DialogAndToast.showDialog(response.getString("message"),ShoppursProductListActivity.this);
                }
            }else if(apiName.equals("get_subcategories")){
                if(response.getString("status").equals("true")||response.getString("status").equals(true)){
                    JSONArray subcatJArray = response.getJSONArray("result");

                    for (int j = 0; j < subcatJArray.length(); j++) {
                        SubCategory subCategory = new SubCategory();
                        subCategory.setId(subcatJArray.getJSONObject(j).getString("subCatId"));
                        subCategory.setCatId(subcatJArray.getJSONObject(j).getString("catId"));
                        subCategory.setName(subcatJArray.getJSONObject(j).getString("subCatName"));
                        subCategoryList.add(subCategory);
                    }

                    if(subCategoryList.size()>0) {

                        if(!TextUtils.isEmpty(selectdSubCatId)) {
                            selectdSubCatPosition = 0;
                            for (SubCategory subCategory : subCategoryList) {
                                if (subCategory.getId().equals(selectdSubCatId)) {
                                    subCategory.setSelected(true);
                                    selectdSubCatPosition = subCategoryList.indexOf(subCategory);
                                    break;
                                }
                            }
                        }else {
                            subCategoryList.get(0).setSelected(true);
                            selectdSubCatId = subCategoryList.get(0).getId();
                        }

                        categoriesAdapter.notifyDataSetChanged();
                        Log.d("selectdSubCatPosition", ""+selectdSubCatPosition);
                        recyclerViewCategory.scrollToPosition(selectdSubCatPosition);
                        getProducts(selectdSubCatId);
                    }
                }else {
                    DialogAndToast.showToast(response.getString("message"),ShoppursProductListActivity.this);
                }
            } else if(apiName.equals("addtocart")){
                if(response.getString("status").equals("true")||response.getString("status").equals(true)){
                    dbHelper.addProductToShopCart(myProduct);
                    updateCartCount();
                    Log.d(TAG, "added o cart" );
                }else {
                    DialogAndToast.showToast(response.getString("message"),ShoppursProductListActivity.this);
                }
            }else if(apiName.equals("updatCart")){
                if(response.getString("status").equals("true")||response.getString("status").equals(true)){
                    dbHelper.updateShopCartData(myProduct);
                    updateCartCount();
                    Log.d(TAG, "updated cart" );
                }else {
                    DialogAndToast.showToast(response.getString("message"),ShoppursProductListActivity.this);
                }
            }else if(apiName.equals("removeCart")){
                if(response.getString("status").equals("true")||response.getString("status").equals(true)){
                    //    dbHelper.deleteCart(cartItem);
                    updateCartCount();
                    Log.d(TAG, "Deleted cart" );
                }else {
                    DialogAndToast.showToast(response.getString("message"),ShoppursProductListActivity.this);
                }
            }else if(apiName.equals("productDetails")){
                if(response.getString("status").equals("true")||response.getString("status").equals(true)){
                    JSONObject jsonObject = response.getJSONObject("result");
                    if(productDetailsType == 1){
                        myProductList.get(position).setProdQoh(jsonObject.getInt("prodQoh"));
                        Log.d("Qoh ", myProductList.get(position).getProdQoh()+"");
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
                    DialogAndToast.showToast("Something went wrong, Please try again", ShoppursProductListActivity.this);
                }
            }else if(apiName.equals("shopDetails")){
                if(response.getBoolean("status")){
                    JSONObject jsonObject = response.getJSONObject("result");
                    shopName = jsonObject.getString("shopName");
                    shopImage = jsonObject.getString("photo");
                    shopMobile = jsonObject.getString("mobile");
                    address = jsonObject.getString("address");
                    statecity = jsonObject.getString("city")+", "+jsonObject.getString("province");
                    setShopDetails();
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            DialogAndToast.showToast(getResources().getString(R.string.json_parser_error)+e.toString(),ShoppursProductListActivity.this);
        }
    }

    private void showNoData(boolean show){
        if(show){
            recyclerViewProduct.setVisibility(View.GONE);
            tvNoData.setVisibility(View.VISIBLE);
        }else{
            recyclerViewProduct.setVisibility(View.VISIBLE);
            tvNoData.setVisibility(View.GONE);
        }
    }

    private void showProgressBar(boolean show){
        if(show){
            recyclerViewProduct.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            tvNoData.setVisibility(View.GONE);
        }else{
            recyclerViewProduct.setVisibility(View.VISIBLE);
            tvNoData.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(shopProductAdapter!=null) {
            shopProductAdapter.notifyDataSetChanged();
            counter = dbHelper.getCartCount();
        }
        updateCartCount();
    }

    float totalPrice;
    public void updateCartCount(){
        totalPrice = 0;
        if(dbHelper.getCartCount()>0){
            rlfooterviewcart.setVisibility(View.VISIBLE);
            viewCart.setVisibility(View.VISIBLE);
            viewCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(ShoppursProductListActivity.this, ShopCartActivity.class));
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
        }else rlfooterviewcart.setVisibility(View.GONE);

    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.my_product_menu, menu);
        if(dbHelper.isShopFavorited(shopCode))
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this,R.drawable.favroite_selected));
        else menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.favrorite_select));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            onBackPressed();
            return true;
        }else if(item.getItemId() == R.id.action_favrouite){
            if(dbHelper.isShopFavorited(shopCode))
                shopFavorite("N");
            else shopFavorite("Y");
        }
        return super.onOptionsItemSelected(item);
    }*/


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

}
