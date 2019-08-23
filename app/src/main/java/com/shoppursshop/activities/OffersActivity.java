package com.shoppursshop.activities;

import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.shoppursshop.R;
import com.shoppursshop.adapters.MyItemAdapter;
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

    private float MIN_WIDTH = 200,MIN_HEIGHT = 230,MAX_WIDTH = 200,MAX_HEIGHT = 290;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        itemList = new ArrayList<>();
        HomeListItem myItem = new HomeListItem();
        myItem.setTitle("Offers");
        myItem.setDesc("Store Offers");
        myItem.setType(0);
        itemList.add(myItem);

       /* myItem = new HomeListItem();
        myItem.setName("Samsung Stores");
        myItem.setLocalIcon(R.drawable.icon_1);
        myItem.setLocalImage(R.drawable.thumb_1);
        myItem.setType(1);
        itemList.add(myItem);

        myItem = new HomeListItem();
        myItem.setTitle("The Fashion Trends");
        myItem.setDesc("Handmade in Italy, Now in India");
        myItem.setCategory("Fashion");
        myItem.setLocalImage(R.drawable.thumb_2);
        myItem.setType(2);
        itemList.add(myItem);

        myItem = new HomeListItem();
        myItem.setName("Grocery");
        myItem.setTitle("Big Grocery Sale !!");
        myItem.setLocalIcon(R.drawable.icon_1);
        myItem.setLocalImage(R.drawable.thumb_3);
        myItem.setType(1);
        itemList.add(myItem);

        myItem = new HomeListItem();
        myItem.setTitle("15 December - 16 December");
        myItem.setDesc("Festive Sales");
        myItem.setType(0);
        itemList.add(myItem);

        myItem = new HomeListItem();
        myItem.setTitle("Sports Store");
        myItem.setLocalImage(R.drawable.thumb_4);
        myItem.setType(3);
        myItem.setWidth(MIN_WIDTH);
        myItem.setHeight(MIN_HEIGHT);
        itemList.add(myItem);

        myItem = new HomeListItem();
        myItem.setTitle("Books and Toys");
        myItem.setLocalImage(R.drawable.thumb_5);
        myItem.setType(3);
        myItem.setWidth(MAX_WIDTH);
        myItem.setHeight(MAX_HEIGHT);
        itemList.add(myItem);

        myItem = new HomeListItem();
        myItem.setTitle("Top Sunglasses");
        myItem.setLocalImage(R.drawable.thumb_6);
        myItem.setType(3);
        myItem.setWidth(MAX_WIDTH);
        myItem.setHeight(MAX_HEIGHT);
        itemList.add(myItem);

        myItem = new HomeListItem();
        myItem.setTitle("Fashion Makeups");
        myItem.setLocalImage(R.drawable.thumb_7);
        myItem.setType(3);
        myItem.setWidth(MAX_WIDTH);
        myItem.setHeight(MAX_HEIGHT);
        itemList.add(myItem);

        myItem = new HomeListItem();
        myItem.setTitle("MotoBikes");
        myItem.setLocalImage(R.drawable.thumb_8);
        myItem.setType(3);
        myItem.setWidth(MAX_WIDTH);
        myItem.setHeight(MAX_HEIGHT);
        itemList.add(myItem);

        myItem = new HomeListItem();
        myItem.setTitle("Swimwear and Lingerie");
        myItem.setLocalImage(R.drawable.thumb_9);
        myItem.setType(3);
        myItem.setWidth(MIN_WIDTH);
        myItem.setHeight(MIN_HEIGHT);
        itemList.add(myItem);

        myItem = new HomeListItem();
        myItem.setTitle("Big Discount in Titan Watches");
        myItem.setDesc("Titan Stores");
        myItem.setCategory("Watches");
        myItem.setLocalImage(R.drawable.thumb_11);
        myItem.setType(4);
        itemList.add(myItem);

        myItem = new HomeListItem();
        myItem.setTitle("10% Discount on Camera And HandyCams");
        myItem.setDesc("Sony Stores");
        myItem.setCategory("Camera And HandyCams");
        myItem.setLocalImage(R.drawable.thumb_12);
        myItem.setType(4);
        itemList.add(myItem);

        myItem = new HomeListItem();
        myItem.setTitle("Upto 30% discount in furnitures and other products");
        myItem.setDesc("Home Town Stores");
        myItem.setCategory("Home Furnishing");
        myItem.setLocalImage(R.drawable.thumb_13);
        myItem.setType(4);
        itemList.add(myItem);*/

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

        if(itemList.size() == 0){
            showNoData(true);
        }

        initFooter(this,3);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                getItemList();
            }
        });

        if(ConnectionDetector.isNetworkAvailable(this)){
            getBanners();
        }
    }

    private void getItemList(){
        swipeRefreshLayout.setRefreshing(false);
    }

    private void getBanners(){
        Map<String,String> params=new HashMap<>();
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+Constants.GET_BANNER_OFFERS;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"bannerOffers");
    }

    private void getCategories(){
        Map<String,String> params=new HashMap<>();
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
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
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+Constants.GET_PRODUCT_OFFERS;
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
                        if(position != prePosition){
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
                            homeListItem = (HomeListItem) itemList.get(position);
                            myItem = new HomeListItem();
                            myItem.setId(jsonObject.getString("id"));
                            myItem.setName(jsonObject.getString("name"));
                            myItem.setImage(jsonObject.getString("image"));
                            myItem.setDesc(jsonObject.getString("desc"));
                            myItem.setType(1);
                            homeListItem.getItemList().add(myItem);
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
                            myItem.setId(jsonObject.getString("catId"));
                            myItem.setTitle(jsonObject.getString("catName"));
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
                    JSONArray dataArray = response.getJSONArray("result");
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
                        productItem.setQty(dbHelper.getTotalQuantityCart(productItem.getProdId()));

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
            }
        }catch (JSONException jsonError){
            jsonError.printStackTrace();
        }
    }

    private int getBannerPosition(String name){
        int position = Integer.parseInt(name.split("_")[0]);
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

    @Override
    public void onItemClicked(int position, int type) {
        if(type == 1){

        }else if(type == 2){
          //btn click add to cart



        }else if(type == 3){

        }else if(type == 4){

        }
    }

    @Override
    public void onImageClicked(int position, int type, View view) {
        final MyProductItem product = (MyProductItem) itemList.get(position);
        showImageDialog(product.getProdImage1(), view);
    }
}
