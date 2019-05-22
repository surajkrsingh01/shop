package com.shoppursshop.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.shoppursshop.R;
import com.shoppursshop.activities.payment.mPos.MPayActivity;
import com.shoppursshop.adapters.CartAdapter;
import com.shoppursshop.adapters.ProductAdapter;
import com.shoppursshop.database.DbHelper;
import com.shoppursshop.fragments.BottomSearchFragment;
import com.shoppursshop.interfaces.MyItemClickListener;
import com.shoppursshop.interfaces.MyItemTypeClickListener;
import com.shoppursshop.models.Barcode;
import com.shoppursshop.models.MyProductItem;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.DialogAndToast;
import com.shoppursshop.utilities.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends NetworkBaseActivity implements MyItemTypeClickListener, MyItemClickListener {

    private RecyclerView recyclerView;
    private CartAdapter myItemAdapter;
    private List<MyProductItem> itemList;
    private Button buttonScan;

    private RelativeLayout relativeLayoutCartFooter;
    private TextView tvItemCount,tvItemPrice,tvCheckout;
    private RelativeLayout relativeLayoutPayOptionLayout;
    private TextView tvCash,tvCard,tvMPos,tvAndroidPos;

    private ImageView imageViewScan,imageViewSearch;

    private float totalPrice;

    private String paymentMode,orderId;
    private String deviceType;

    private JSONArray shopArray;
    List <MyProductItem> cartItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
    }

    private void init(){
        dbHelper.deleteTable(DbHelper.CART_TABLE);
        itemList = new ArrayList<>();
        imageViewScan = findViewById(R.id.image_scan);
        imageViewSearch = findViewById(R.id.image_search);
        buttonScan = findViewById(R.id.btn_scan);
        tvItemCount=findViewById(R.id.itemCount);
        tvItemPrice=findViewById(R.id.itemPrice);
        tvCheckout=findViewById(R.id.viewCart);
        tvCard = findViewById(R.id.tv_pay_card);
        tvCash = findViewById(R.id.tv_pay_cash);
        tvMPos = findViewById(R.id.tv_mpos);
        tvAndroidPos = findViewById(R.id.tv_android_pos);
        relativeLayoutPayOptionLayout = findViewById(R.id.relative_pay_layout);
        relativeLayoutCartFooter=findViewById(R.id.rlfooterviewcart);
        relativeLayoutCartFooter.setBackgroundColor(colorTheme);
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        final RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        myItemAdapter=new CartAdapter(this,itemList);
        myItemAdapter.setMyItemTypeClickListener(this);
        recyclerView.setAdapter(myItemAdapter);

        tvCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativeLayoutPayOptionLayout.setVisibility(View.VISIBLE);
            }
        });

        tvCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvCard.setVisibility(View.GONE);
                tvCash.setVisibility(View.GONE);
                tvAndroidPos.setVisibility(View.VISIBLE);
                tvMPos.setVisibility(View.VISIBLE);
            }
        });

        tvCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentMode = "Cash";
                generateJson(paymentMode);
                /*Intent intent = new Intent(CartActivity.this, MPayActivity.class);
                intent.putExtra("totalAmount",String.format("%.02f",dbHelper.getTotalPriceCart()));
                intent.putExtra("ordId",getIntent().getStringExtra("ordId"));
                startActivity(intent);*/
            }
        });

        tvMPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentMode = "Credit/Debit Card";
                deviceType = "ME30S";
                generateJson(paymentMode);
            }
        });

        tvAndroidPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentMode = "Credit/Debit Card";
                deviceType = "N910";
                generateJson(paymentMode);
            }
        });

        imageViewScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openScannar();
            }
        });

        imageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSearchFragment bottomSearchFragment = new BottomSearchFragment();
                bottomSearchFragment.setCallingActivityName("productList");
                Bundle bundle = new Bundle();
                bundle.putString("flag","searchCartProduct");
                bottomSearchFragment.setArguments(bundle);
                bottomSearchFragment.setMyItemClickListener(CartActivity.this);
                bottomSearchFragment.show(getSupportFragmentManager(), "Search Product Bottom Sheet");
            }
        });

        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openScannar();
            }
        });
    }

    private void openScannar(){
        Intent intent = new Intent(this,ScannarActivity.class);
        intent.putExtra("flag","scan");
        intent.putExtra("type","addToCart");
        startActivity(intent);

      /* dbHelper.deleteTable(DbHelper.CART_TABLE);

        MyProductItem myProductItem = dbHelper.getProductDetailsByBarCode("N7NL00824803");
        myProductItem.setProdBarCode("N7NL00824803");
        myProductItem.setQty(1);
        myProductItem.setTotalAmount(myProductItem.getProdSp());
        dbHelper.addProductToCart(myProductItem);

        itemList.clear();
        List<MyProductItem> cartList =  dbHelper.getCartProducts();
        for(MyProductItem ob : cartList){
            itemList.add(ob);
        }

        myItemAdapter.notifyDataSetChanged();

        if(itemList.size() > 0){
            setFooterValues();
            relativeLayoutCartFooter.setVisibility(View.VISIBLE);
            myItemAdapter.notifyDataSetChanged();
        }else{
            relativeLayoutCartFooter.setVisibility(View.GONE);
        }*/

    }

    @Override
    public void onResume(){
        super.onResume();

        Log.i(TAG,"onResume called.");

        if(itemList == null){
            itemList = dbHelper.getCartProducts();
        }else{
            itemList.clear();
            List<MyProductItem> cartList =  dbHelper.getCartProducts();
            for(MyProductItem ob : cartList){
                itemList.add(ob);
            }
        }

        if(itemList.size() > 0){
            setFooterValues();
            relativeLayoutCartFooter.setVisibility(View.VISIBLE);
            myItemAdapter.notifyDataSetChanged();
        }else{
            relativeLayoutCartFooter.setVisibility(View.GONE);
        }
    }

    private void setFooterValues(){
        if(itemList.size() == 1){
            tvItemCount.setText(itemList.size()+" item");
        }else{
            tvItemCount.setText(itemList.size()+" items");
        }

        tvItemPrice.setText("Rs "+Utility.numberFormat(dbHelper.getTotalPriceCart()));

        tvCheckout.setVisibility(View.VISIBLE);

    }

    private void generateOrder(JSONArray shopArray){
        Log.d(TAG, shopArray.toString());
        String url=getResources().getString(R.string.url)+Constants.GENERATE_ORDER;
        showProgress(true);
        jsonArrayV2ApiRequest(Request.Method.POST,url, shopArray,"generate_order");
    }

    private void placeOrder(JSONArray shopArray, String orderId) throws JSONException {
        shopArray.getJSONObject(0).put("orderId", orderId );
        Log.d(TAG, shopArray.toString());
        String url=getResources().getString(R.string.url)+Constants.PLACE_ORDER;
        showProgress(true);
        jsonArrayV2ApiRequest(Request.Method.POST,url, shopArray,"place_order");
    }

    private void generateJson(String paymentMode){
        try {
            cartItemList = dbHelper.getCartProducts();

            List<String> tempShopList = new ArrayList<>();
            shopArray = new JSONArray();
            JSONObject shopObject = new JSONObject();
            JSONArray productArray = new JSONArray();
            JSONObject productObject = new JSONObject();

            JSONArray tempbarcodeArray =null;
            JSONObject tempbarcodeObject = null;

            String shopCode = sharedPreferences.getString(Constants.SHOP_CODE,"");

            for (MyProductItem cartItem : cartItemList) {
                Log.d(TAG, cartItem.getProdBarCode()+"");
                if (!tempShopList.contains(shopCode)) {
                    //Log.d("PRD list "+tempShopList.toString(), cartItem.getShopCode());
                    tempShopList.add(shopCode);
                    shopObject = new JSONObject();
                    productArray = new JSONArray();
                    productObject = new JSONObject();

                    if(cartItem.getIsBarCodeAvailable().equals("Y")){
                        cartItem.setBarcodeList(dbHelper.getBarCodesForCart(cartItem.getProdId()));
                        tempbarcodeArray = new JSONArray();
                        for (int i=0;i<cartItem.getQty();i++){
                            tempbarcodeObject = new JSONObject();
                            tempbarcodeObject.put("barcode", cartItem.getBarcodeList().get(i).getBarcode());
                            tempbarcodeArray.put(tempbarcodeObject);
                        }
                    }


                    shopObject.put("shopCode", shopCode);
                    shopObject.put("orderDate", Utility.getTimeStamp());
                    shopObject.put("orderDeliveryNote","Note");
                    shopObject.put("orderDeliveryMode","Self");
                    shopObject.put("paymentMode",paymentMode);
                    shopObject.put("deliveryAddress","");
                    shopObject.put("pinCode","");
                    shopObject.put("createdBy",sharedPreferences.getString(Constants.FULL_NAME,""));
                    shopObject.put("updateBy",sharedPreferences.getString(Constants.FULL_NAME,""));
                    if(paymentMode.equals("Cash")){
                        shopObject.put("orderStatus","Delivered");
                        shopObject.put("orderPaymentStatus", "Done");
                    }else{
                        shopObject.put("orderStatus","pending");
                        shopObject.put("orderPaymentStatus", "pending");
                    }
                    shopObject.put("custName", getIntent().getStringExtra("custName"));
                    shopObject.put("custCode",getIntent().getStringExtra("custCode"));
                    shopObject.put("mobileNo",getIntent().getStringExtra("custMobile"));
                    shopObject.put("orderImage",getIntent().getStringExtra("custImage"));
                    shopObject.put("totalQuantity",String.valueOf(dbHelper.getTotalQuantityCart()));
                    shopObject.put("toalAmount",String.valueOf(dbHelper.getTotalPriceCart()));
                    shopObject.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
                    shopObject.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
                    shopObject.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));

                    productObject.put("prodCode", cartItem.getProdCode());
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


    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {
        showProgress(false);
        try {
            // JSONObject jsonObject=response.getJSONObject("response");
            Log.d("response", response.toString());
             if (apiName.equals("generate_order")){
                if(response.getString("status").equals("true")||response.getString("status").equals(true)){
                    Log.d(TAG, "Ordeer Generated" );
                    orderId = response.getJSONObject("result").getString("orderId");
                    totalPrice = dbHelper.getTotalPriceCart();
                   // dbHelper.deleteTable(DbHelper.CART_TABLE);
                    itemList.clear();
                    myItemAdapter.notifyDataSetChanged();
                    if(paymentMode.equals("Cash")){
                        for (MyProductItem cartItem : cartItemList) {
                            dbHelper.setQoh(cartItem.getProdId(),cartItem.getQty());
                            if(cartItem.getIsBarCodeAvailable().equals("Y")){
                                for(Barcode barcode : cartItem.getBarcodeList()){
                                    dbHelper.removeBarCode(barcode.getBarcode());
                                }
                            }
                        }
                        showMyDialog("Take Cash Rs "+Utility.numberFormat(totalPrice));
                    }else{
                        Log.d(TAG, "orderId "+orderId );
                        Intent intent = new Intent(CartActivity.this, MPayActivity.class);
                        intent.putExtra("totalAmount",String.format("%.02f",dbHelper.getTotalPriceCart()));
                        intent.putExtra("ordId",orderId);
                        intent.putExtra("custCode", getIntent().getStringExtra("custCode"));
                        intent.putExtra("custId", getIntent().getIntExtra("custId",0));
                        intent.putExtra("custName", getIntent().getStringExtra("custName"));
                        intent.putExtra("custMobile", getIntent().getStringExtra("custMobile"));
                        intent.putExtra("custImage", getIntent().getStringExtra("custImage"));
                        intent.putExtra("deviceType", deviceType);
                        startActivity(intent);
                        finish();
                    }

                    //placeOrder(shopArray, orderId);
                }else {
                    DialogAndToast.showToast(response.getString("message"),CartActivity.this);
                }
            }else if(apiName.equals("place_order")){
                if(response.getString("status").equals("true")||response.getString("status").equals(true)){
                    dbHelper.deleteTable(DbHelper.CART_TABLE);
                    itemList.clear();
                    myItemAdapter.notifyDataSetChanged();
                    relativeLayoutCartFooter.setVisibility(View.GONE);
                    Log.d(TAG, "Ordeer Placed" );
                }else {
                    DialogAndToast.showToast(response.getString("message"),CartActivity.this);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            DialogAndToast.showToast(getResources().getString(R.string.json_parser_error)+e.toString(),CartActivity.this);
        }
    }

    @Override
    public void onDialogPositiveClicked(){
      Intent intent = new Intent(CartActivity.this,InvoiceActivity.class);
      intent.putExtra("orderId",orderId);
      startActivity(intent);
      finish();
    }

    @Override
    public void onItemClicked(int position, int type) {
        MyProductItem item = (MyProductItem)itemList.get(position);
        if(type == 1){
            if(itemList.size() == 1 && item.getQty() == 1){
                itemList.clear();
                dbHelper.removeProductFromCart(item.getProdBarCode());
                dbHelper.removeProductFromCart(item.getProdId());
                relativeLayoutCartFooter.setVisibility(View.GONE);
                myItemAdapter.notifyDataSetChanged();
            }else{
                int qty = item.getQty() - 1;
                item.setQty(qty);
                float amount = item.getTotalAmount() - item.getProdSp();
                dbHelper.updateCartData(item.getProdBarCode(),qty,amount);
                setFooterValues();
                if(qty == 0){
                    itemList.remove(position);
                }
                myItemAdapter.notifyItemChanged(position);
            }
        }else{
            if(item.getIsBarCodeAvailable().equals("Y")){
                if(item.getQty() == item.getBarcodeList().size()){
                    DialogAndToast.showDialog("There are no more stocks",this);
                }else{
                    int qty = item.getQty() + 1;
                    item.setQty(qty);
                    float amount = item.getTotalAmount() + item.getProdSp();
                    dbHelper.updateCartData(item.getProdBarCode(),qty,amount);
                    if(itemList.size() == 1){
                        relativeLayoutCartFooter.setVisibility(View.VISIBLE);
                    }
                    setFooterValues();
                    myItemAdapter.notifyItemChanged(position);
                }

            }else{
                if(item.getQty() == item.getProdQoh()){
                    DialogAndToast.showDialog("There are no more stocks",this);
                }else{
                    int qty = item.getQty() + 1;
                    item.setQty(qty);
                    float amount = item.getTotalAmount() + item.getProdSp();
                    dbHelper.updateCartData(item.getProdBarCode(),qty,amount);
                    if(itemList.size() == 1){
                        relativeLayoutCartFooter.setVisibility(View.VISIBLE);
                    }
                    setFooterValues();
                    myItemAdapter.notifyItemChanged(position);
                }
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cart_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            BottomSearchFragment bottomSearchFragment = new BottomSearchFragment();
            bottomSearchFragment.setCallingActivityName("productList");
            Bundle bundle = new Bundle();
            bundle.putString("flag","searchCartProduct");
            bottomSearchFragment.setArguments(bundle);
            bottomSearchFragment.setMyItemClickListener(this);
            bottomSearchFragment.show(getSupportFragmentManager(), "Search Product Bottom Sheet");
            return true;
        }else if (id == R.id.action_scan) {
            openScannar();
            return true;
        }else if (id == android.R.id.home) {
            super.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClicked(int prodId) {

        Log.i(TAG,"item clicked "+prodId+" "+dbHelper.getBarCodesForCart(prodId).size());

        MyProductItem item = dbHelper.getProductDetails(prodId);

        if(item.getIsBarCodeAvailable().equals("Y")){
            item.setBarcodeList(dbHelper.getBarCodesForCart(prodId));
            if(item.getBarcodeList() != null && item.getBarcodeList().size() > 0){
                item.setQty(1);
                item.setTotalAmount(item.getProdSp());
                dbHelper.addProductToCart(item);
                itemList.add(item);
                myItemAdapter.notifyItemChanged(itemList.size() -1 );
                if(itemList.size() > 0){
                    setFooterValues();
                    relativeLayoutCartFooter.setVisibility(View.VISIBLE);
                }
            }else{
                DialogAndToast.showDialog("Product is out of stock.",this);
            }

        }else{
            if(item.getProdQoh() > 0){
                item.setQty(1);
                item.setTotalAmount(item.getProdSp());
                dbHelper.addProductToCart(item);
                itemList.add(item);
                myItemAdapter.notifyItemChanged(itemList.size() -1 );
                if(itemList.size() > 0){
                    setFooterValues();
                    relativeLayoutCartFooter.setVisibility(View.VISIBLE);
                }
            }else{
                DialogAndToast.showDialog("Product is out of stock.",this);
            }
        }


    }
}
