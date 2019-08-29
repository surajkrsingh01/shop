package com.shoppursshop.fragments;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.shoppursshop.R;
import com.shoppursshop.adapters.SearchCustomerAdapter;
import com.shoppursshop.adapters.SearchProductAdapter;
import com.shoppursshop.adapters.ShopProductListAdapter;
import com.shoppursshop.database.DbHelper;
import com.shoppursshop.interfaces.MyImageClickListener;
import com.shoppursshop.interfaces.MyItemClickListener;
import com.shoppursshop.interfaces.MyItemTypeClickListener;
import com.shoppursshop.interfaces.MyListItemClickListener;
import com.shoppursshop.models.Barcode;
import com.shoppursshop.models.MyCustomer;
import com.shoppursshop.models.MyProductItem;
import com.shoppursshop.models.ProductColor;
import com.shoppursshop.models.ProductComboDetails;
import com.shoppursshop.models.ProductComboOffer;
import com.shoppursshop.models.ProductDiscountOffer;
import com.shoppursshop.models.ProductSize;
import com.shoppursshop.models.ProductUnit;
import com.shoppursshop.utilities.AppController;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.DialogAndToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchProductFragment extends BottomSheetDialogFragment implements MyItemClickListener,
        MyItemTypeClickListener, MyImageClickListener {

    private String TAG = "SearchProductFragment";
    protected ProgressDialog progressDialog;
    protected SharedPreferences sharedPreferences;
    protected SharedPreferences.Editor editor;
    protected DbHelper dbHelper;
    private ImageView iv_clear;
    private RecyclerView recyclerView_Search;
    private EditText etSearch;
    private ShopProductListAdapter productAdapter;
    private List<MyProductItem> myProductList;
    private String callingActivityName, shopCode,catId, subCatName, subcatId,token;
    private int colorTheme;
    private boolean isDarkTheme;
    private int limit = 20,offset = 0,position,type,counter,productDetailsType;
    private MyProductItem myProduct,freeProdut;
    private MyItemClickListener myItemClickListener;
    private MyListItemClickListener myListItemClickListener;

    public void setCallingActivityName(String name, SharedPreferences sharedPreferences, boolean isDarkTheme){
        this.callingActivityName = name;
        this.sharedPreferences = sharedPreferences;
        this.isDarkTheme = isDarkTheme;
        token = sharedPreferences.getString(Constants.TOKEN,"");
    }

    public void setMyItemClickListener(MyItemClickListener myItemClickListener) {
        this.myItemClickListener = myItemClickListener;
    }

    public void setMyListItemClickListener(MyListItemClickListener myListItemClickListener) {
        this.myListItemClickListener = myListItemClickListener;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public void setSubCatName(String name){
        this.subCatName = name;
    }

    public void setSubcatId(String id){
        this.subcatId = id;
    }

    public SearchProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_bottom_sheet_dialog, container, false);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCanceledOnTouchOutside(false);
        dbHelper = new DbHelper(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        iv_clear = view.findViewById(R.id.iv_clear);
        etSearch = view.findViewById(R.id.et_search);
        recyclerView_Search = view.findViewById(R.id.recyclerView_Search);

        iv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchProductFragment.this.dismiss();
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Bundle bundle = getArguments();
                shopCode = bundle.getString("shopCode");
                searchProducts(s.toString());
            }
        });
        return view;
    }

    private void searchProducts(String query){
        myProductList = new ArrayList<>();
        recyclerView_Search.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView_Search.setLayoutManager(layoutManager);
        recyclerView_Search.setItemAnimator(new DefaultItemAnimator());
        productAdapter = new ShopProductListAdapter(getContext(),myProductList);
        productAdapter.setMyImageClickListener(this);
        productAdapter.setMyItemTypeClickListener(this);
        productAdapter.setDarkTheme(isDarkTheme);
        recyclerView_Search.setAdapter(productAdapter);

        //"code":"SHP4","dbName":"SHP4","id":"3","dbUserName":"SHPC1","dbPassword":"SHPC1"

        Map<String,String> params=new HashMap<>();
        params.put("query",query);
        params.put("shopCode", shopCode);
        params.put("limit", ""+limit);
        params.put("offset", ""+myProductList.size());
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+"/api/search/shop_products";
        //showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"searchProducts");
    }

    private void getProductDetails(String prodId){
        showProgress(true);
        Map<String,String> params=new HashMap<>();
        params.put("id", prodId); // as per user selected category from top horizontal categories list
        params.put("code", shopCode);
        // params.put("dbName",shopCode);
        Log.d(TAG, params.toString());
        String url=getResources().getString(R.string.url)+"/api/customers/products/ret_products_details";
        jsonObjectApiRequest(Request.Method.POST, url,new JSONObject(params),"productDetails");
    }

    protected void jsonObjectApiRequest(int method, String url, JSONObject jsonObject, final String apiName){
        try {
            jsonObject.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
            jsonObject.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
            jsonObject.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i(TAG,url);
        Log.i(TAG,jsonObject.toString());
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(method,url,jsonObject,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                AppController.getInstance().getRequestQueue().getCache().clear();
                Log.i(TAG,response.toString());
                showProgress(false);
                onJsonObjectResponse(response,apiName);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                AppController.getInstance().getRequestQueue().getCache().clear();
                Log.i(TAG,"Json Error "+error.toString());
                showProgress(false);
                DialogAndToast.showDialog(getResources().getString(R.string.connection_error),getActivity());
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer "+token);
                //params.put("VndUserDetail", appVersion+"#"+deviceName+"#"+osVersionName);
                return params;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    public void onJsonObjectResponse(JSONObject response, String apiName) {
        try {
            if(apiName.equals("searchProducts")){
                if(response.getBoolean("status")){
                    if(!response.getString("result").equals("null")){
                        JSONArray productJArray = response.getJSONArray("result");
                        JSONObject dataObject = null;
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
                    productAdapter.notifyDataSetChanged();
                }else{
                    DialogAndToast.showDialog(response.getString("message"),getActivity());
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
                    DialogAndToast.showToast("Something went wrong, Please try again", getActivity());
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    void showProgress(boolean show){
        if(show){
            progressDialog.show();
        }else{
            progressDialog.dismiss();
        }
    }

    public void updateCart(int type, int position){
        Log.d("clicked Position ", position+"");
        this.position = position;
        this.type = type;
        if(type==2){
            productDetailsType = 1;
            getProductDetails(""+myProductList.get(position).getId());
        }else{
            onProductItemClicked(position,type);
        }
    }

    @Override
    public void onItemClicked(int position) {

    }

    @Override
    public void onItemClicked(int position, int type) {
        if(type == 3){
          //  showOfferDescription(myProductList.get(position));
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
                    productAdapter.notifyItemChanged(position);
                    myItemClickListener.onItemClicked(myProduct.getProdId());
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
                    productAdapter.notifyItemChanged(position);
                    myItemClickListener.onItemClicked(myProduct.getProdId());
                }
            }

        }else if(type == 2){
            if(myProduct.getQty() >= myProduct.getProdQoh()){
                productAdapter.notifyDataSetChanged();
                DialogAndToast.showDialog("There are no more stocks",getActivity());
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
                productAdapter.notifyItemChanged(position);
                productAdapter.notifyDataSetChanged();
                myItemClickListener.onItemClicked(myProduct.getProdId());
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
       // showImageDialog( myProductList.get(position).getProdImage1(),view);
    }


}
