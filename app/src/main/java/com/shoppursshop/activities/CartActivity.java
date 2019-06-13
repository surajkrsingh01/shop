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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.shoppursshop.R;
import com.shoppursshop.activities.payment.mPos.MPayActivity;
import com.shoppursshop.activities.settings.ApplyOffersActivity;
import com.shoppursshop.activities.settings.profile.DeliveryActivity;
import com.shoppursshop.adapters.CartAdapter;
import com.shoppursshop.adapters.OfferDescAdapter;
import com.shoppursshop.adapters.ProductAdapter;
import com.shoppursshop.database.DbHelper;
import com.shoppursshop.fragments.BottomSearchFragment;
import com.shoppursshop.fragments.OfferDescriptionFragment;
import com.shoppursshop.interfaces.MyItemClickListener;
import com.shoppursshop.interfaces.MyItemTypeClickListener;
import com.shoppursshop.models.Barcode;
import com.shoppursshop.models.MyProductItem;
import com.shoppursshop.models.ProductComboDetails;
import com.shoppursshop.models.ProductComboOffer;
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

    private RelativeLayout relativeLayoutCartFooter;
    private LinearLayout rlAddressBilling;
    private TextView tvItemCount,tvItemPrice,tvCheckout,tvItemTotal,tvTotalTaxes,tvSgst,tvTotalDiscount,
            tvDeliveryCharges,tvNetPayable,tvOfferName;
    private RelativeLayout relativeLayoutPayOptionLayout,rlDiscount,rlDelivery,rl_offer_applied_layout,rlOfferLayout;
    private TextView tvCash,tvCard,tvMPos,tvAndroidPos;

    private ImageView imageViewScan,imageViewSearch,imageViewRemoveOffer;

    private float totalPrice,offerPer,offerMaxAmount,deliveryDistance;
    private int offerId,deliveryTypeId;

    private String offerName,paymentMode,orderNumber;
    private String deviceType;

    private JSONArray shopArray;
    List <MyProductItem> cartItemList;

    private TextView tv_mode, tv_self_status, tv_address_label, tv_address,tvApplyCoupon;
    private View seperator_delivery_address;
    private RadioGroup rg_delivery;
    private RadioButton rb_home_delivery, rb_self_delivery;
    private LinearLayout linearLayoutScanCenter;
    private RelativeLayout rlOfferDesc;

    private BottomSearchFragment bottomSearchFragment;

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
        tvItemCount=findViewById(R.id.itemCount);
        tvItemPrice=findViewById(R.id.itemPrice);
        tvCheckout=findViewById(R.id.viewCart);
        tvCard = findViewById(R.id.tv_pay_card);
        tvCash = findViewById(R.id.tv_pay_cash);
        tvMPos = findViewById(R.id.tv_mpos);
        tvAndroidPos = findViewById(R.id.tv_android_pos);
        linearLayoutScanCenter = findViewById(R.id.linear_action);

        tvApplyCoupon = findViewById(R.id.tv_coupon_label);
        tvItemTotal = findViewById(R.id.tv_item_total);
        tvTotalTaxes = findViewById(R.id.tv_total_taxes);
        tvSgst = findViewById(R.id.tv_sgst);
        tvTotalDiscount = findViewById(R.id.tv_total_discount);
        tvDeliveryCharges = findViewById(R.id.tv_delivery_charges);
        tvNetPayable = findViewById(R.id.tv_net_pay);
        tvOfferName = findViewById(R.id.tv_offer_name);
        rlDelivery = findViewById(R.id.relative_delivery_layout);
        rlDiscount = findViewById(R.id.relative_discount);
        rl_offer_applied_layout = findViewById(R.id.rl_offer_applied_layout);
        rlOfferDesc = findViewById(R.id.rl_offer_desc);
        rlOfferLayout = findViewById(R.id.rl_offer_layout);
        imageViewRemoveOffer = findViewById(R.id.image_remove_offer);

        Utility.setColorFilter(imageViewRemoveOffer.getDrawable(),colorTheme);

        relativeLayoutPayOptionLayout = findViewById(R.id.relative_pay_layout);
        rlAddressBilling = findViewById(R.id.linear_billing);
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

        tvCheckout.setText("Make Payment");


        tvCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativeLayoutPayOptionLayout.setVisibility(View.VISIBLE);
            }
        });

        tvCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*tvCard.setVisibility(View.GONE);
                tvCash.setVisibility(View.GONE);
                tvAndroidPos.setVisibility(View.VISIBLE);
                tvMPos.setVisibility(View.VISIBLE);*/

                paymentMode = "Credit/Debit Card";
                deviceType = "ME30S";
                generateJson(paymentMode);
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
        linearLayoutScanCenter.setOnClickListener(new View.OnClickListener() {
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

        boolean isDeliveryAvailable = sharedPreferences.getBoolean(Constants.IS_DELIVERY_AVAILABLE, false);
        tv_mode = findViewById(R.id.tv_mode);
        tv_self_status = findViewById(R.id.tv_self_status);
        rg_delivery = findViewById(R.id.rg_delivery);
        tv_address_label = findViewById(R.id.tv_address_label);
        tv_address = findViewById(R.id.tv_address);
       // seperator_delivery_address = findViewById(R.id.seperator_delivery_address);
        rb_home_delivery = findViewById(R.id.rb_home_delivery);
        rb_self_delivery = findViewById(R.id.rb_self_delivery);

        rb_self_delivery.setChecked(true);

        if(!isDeliveryAvailable){ // home delivery not available
            tv_self_status.setText("Self Delivery");
            rg_delivery.setVisibility(View.GONE);
        } else {
            rg_delivery.setVisibility(View.VISIBLE);
            tv_self_status.setVisibility(View.GONE);
        }

        rg_delivery.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rb_self_delivery){
                    deliveryTypeId = checkedId;
                    tv_self_status.setText("Self Delivery");
                    //tv_self_status.setVisibility(View.VISIBLE);
                    tv_address_label.setVisibility(View.GONE);
                   // seperator_delivery_address.setVisibility(View.GONE);
                    tv_address.setVisibility(View.GONE);
                    tvDeliveryCharges.setText("0.00");
                    deliveryDistance = 0;
                    rlDelivery.setVisibility(View.GONE);
                }else{
                    if(totalPrice > sharedPreferences.getInt(Constants.MIN_DELIVERY_AMOUNT,0)){
                        startActivityForResult(new Intent(CartActivity.this, DeliveryAddressActivity.class), 101);
                        tv_self_status.setVisibility(View.GONE);
                    }else{
                        rb_self_delivery.setChecked(true);
                        DialogAndToast.showDialog("Minimum amount should be more than Rs."
                                +sharedPreferences.getInt(Constants.MIN_DELIVERY_AMOUNT,0)+" for home delivery.",CartActivity.this);
                    }

                }
            }
        });

        tv_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(CartActivity.this, DeliveryAddressActivity.class), 101);
                tv_self_status.setVisibility(View.GONE);
            }
        });

        tv_address_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(CartActivity.this, DeliveryAddressActivity.class), 101);
                tv_self_status.setVisibility(View.GONE);
            }
        });

        tvApplyCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CartActivity.this,CouponOffersActivity.class);
                intent.putExtra("custCode",getIntent().getStringExtra("custCode"));
                intent.putExtra("flag","coupons");
                startActivityForResult(intent,5);
            }
        });

        imageViewRemoveOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_offer_applied_layout.setVisibility(View.GONE);
                tvApplyCoupon.setVisibility(View.VISIBLE);
                offerId = 0;
                offerMaxAmount = 0f;
                offerPer = 0f;
                offerName = "";
                setFooterValues();
            }
        });

        rlOfferLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CartActivity.this, ApplyOffersActivity.class);
                intent.putExtra("custCode",getIntent().getStringExtra("custCode"));
                intent.putExtra("flag","offers");
               // startActivityForResult(intent,6);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(requestCode ==101 && intent!=null){
            String address = intent.getStringExtra("address");
            String country = intent.getStringExtra("country");
            String state = intent.getStringExtra("state");
            String city = intent.getStringExtra("city");
            String zip = intent.getStringExtra("zip");
            deliveryDistance = intent.getFloatExtra("distance",0f);
            Log.d("addr ", address +country + state + city +zip+deliveryDistance);
            tv_address_label.setVisibility(View.VISIBLE);
            tv_address.setVisibility(View.VISIBLE);
            tv_address.setText(address.concat(zip));
            setFooterValues();
           // seperator_delivery_address.setVisibility(View.VISIBLE);
        }else if(requestCode == 5){
            if(intent != null){
                offerName = intent.getStringExtra("name");
                offerId = intent.getIntExtra("id",0);
                offerPer = intent.getFloatExtra("per",0f);
                offerMaxAmount = intent.getFloatExtra("amount",0f);
                rl_offer_applied_layout.setVisibility(View.VISIBLE);
                tvOfferName.setText(offerName);
                tvApplyCoupon.setVisibility(View.GONE);
                setFooterValues();
            }
        }
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
                ob.setProductPriceOfferList(dbHelper.getProductPriceOffer(""+ob.getProdId()));
                Log.i(TAG,"offer sizze "+ob.getProductPriceOfferList().size());
                itemList.add(ob);
            }
        }

        if(itemList.size() > 0){
            setFooterValues();
            relativeLayoutCartFooter.setVisibility(View.VISIBLE);
            myItemAdapter.notifyDataSetChanged();
        }else{
            relativeLayoutCartFooter.setVisibility(View.GONE);
            linearLayoutScanCenter.setVisibility(View.VISIBLE);
        }
    }

    private void setFooterValues(){
        recyclerView.setVisibility(View.VISIBLE);
        linearLayoutScanCenter.setVisibility(View.GONE);
        if(itemList.size() == 1){
            tvItemCount.setText(itemList.size()+" item");
        }else{
            tvItemCount.setText(itemList.size()+" items");
        }


        tvItemTotal.setText(Utility.numberFormat(dbHelper.getTotalMrpPriceCart()));
        tvTotalTaxes.setText(Utility.numberFormat(dbHelper.getTotalTaxesart()));
        totalPrice = dbHelper.getTotalPriceCart() + dbHelper.getTotalTaxesart();
        float totalDis = dbHelper.getTotalMrpPriceCart() - dbHelper.getTotalPriceCart();
        float dis = 0f;
        if(offerPer > 0f){
            dis = totalPrice * offerPer / 100;
            totalDis = totalDis + dis ;
        }

        Log.i(TAG," diff "+totalDis+" offerPer "+offerPer+" totalPrice "+totalPrice);

        if(totalDis == 0f){
            rlDiscount.setVisibility(View.GONE);
        }else{
            rlDiscount.setVisibility(View.VISIBLE);
            tvTotalDiscount.setText(Utility.numberFormat(totalDis));
        }

        totalPrice = totalPrice - dis;
        float deliveryCharges = 0f;
        if(deliveryTypeId == R.id.rb_self_delivery){
            rlDelivery.setVisibility(View.GONE);
        }else{
            rlDelivery.setVisibility(View.VISIBLE);
            Log.i(TAG,"Min Delivery Distance "+sharedPreferences.getInt(Constants.MIN_DELIVERY_DISTANCE,0));
            Log.i(TAG,"Charges after Delivery Distance "+sharedPreferences.getInt(Constants.CHARGE_AFTER_MIN_DISTANCE,0));
            Log.i(TAG,"Delivery Distance "+deliveryDistance);
            if(deliveryDistance > sharedPreferences.getInt(Constants.MIN_DELIVERY_DISTANCE,0)){
               float diffKms = deliveryDistance -  sharedPreferences.getInt(Constants.MIN_DELIVERY_DISTANCE,0);
               int intKms = (int)diffKms;
               float decimalKms = diffKms - intKms;

                int chargesPerKm = sharedPreferences.getInt(Constants.CHARGE_AFTER_MIN_DISTANCE,0);
                deliveryCharges = intKms * chargesPerKm + decimalKms * chargesPerKm;
                tvDeliveryCharges.setText(Utility.numberFormat(deliveryCharges));
            }else{
                tvDeliveryCharges.setText("0.00");
            }
        }

        totalPrice = totalPrice + deliveryCharges;
        tvItemPrice.setText("Rs "+Utility.numberFormat(totalPrice));
        tvNetPayable.setText("Rs "+Utility.numberFormat(totalPrice));

        tvCheckout.setVisibility(View.VISIBLE);
        rlAddressBilling.setVisibility(View.VISIBLE);

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
                    shopObject.put("custUserCreateStatus",getIntent().getStringExtra("custUserCreateStatus"));
                    shopObject.put("totalQuantity",String.valueOf(dbHelper.getTotalQuantityCart()));
                    shopObject.put("toalAmount",String.valueOf(totalPrice));
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
                    orderNumber = response.getJSONObject("result").getString("orderNumber");
                    //totalPrice = dbHelper.getTotalPriceCart();
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
                        Log.d(TAG, "orderId "+orderNumber );
                        Intent intent = new Intent(CartActivity.this, MPayActivity.class);
                        intent.putExtra("totalAmount",String.format("%.02f",totalPrice));
                        intent.putExtra("orderNumber",orderNumber);
                        intent.putExtra("custCode", getIntent().getStringExtra("custCode"));
                        intent.putExtra("custId", getIntent().getIntExtra("custId",0));
                        intent.putExtra("custName", getIntent().getStringExtra("custName"));
                        intent.putExtra("custMobile", getIntent().getStringExtra("custMobile"));
                        intent.putExtra("custImage", getIntent().getStringExtra("custImage"));
                        intent.putExtra("custUserCreateStatus", getIntent().getStringExtra("custUserCreateStatus"));
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
      intent.putExtra("orderNumber",orderNumber);
      startActivity(intent);
      finish();
    }

    @Override
    public void onItemClicked(int position, int type) {
        Log.i(TAG,"onItemClicked "+position+" type "+type);
        MyProductItem item = (MyProductItem)itemList.get(position);
        if(type == 1){
            if(itemList.size() == 1 && item.getQty() == 1){
                itemList.clear();
                dbHelper.removeProductFromCart(item.getProdBarCode());
                dbHelper.removeProductFromCart(item.getProdId());
                relativeLayoutCartFooter.setVisibility(View.GONE);
                rlAddressBilling.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                linearLayoutScanCenter.setVisibility(View.VISIBLE);
                myItemAdapter.notifyDataSetChanged();
                offerPer = 0f;
                offerName = "";
                offerMaxAmount = 0f;
                offerId = 0;
                deliveryDistance = 0;

            }else{
                int qty = item.getQty() - 1;
                float netSellingPrice = getOfferAmount(item);
                item.setQty(qty);
                Log.i(TAG,"netSellingPrice "+netSellingPrice);
                float amount = item.getTotalAmount() - netSellingPrice;
                Log.i(TAG,"tot amount "+amount);
                item.setTotalAmount(amount);
                dbHelper.updateCartData(item.getProdBarCode(),qty,amount);
                dbHelper.updateCartData(item.getProdId(),qty,amount);
                setFooterValues();
                if(qty == 0){
                    itemList.remove(position);
                }
                myItemAdapter.notifyItemChanged(position);
            }
        }else if(type == 2){
            if(item.getIsBarCodeAvailable().equals("Y")){
                if(item.getQty() == item.getBarcodeList().size()){
                    DialogAndToast.showDialog("There are no more stocks",this);
                }else{
                    int qty = item.getQty() + 1;
                    item.setQty(qty);
                    Log.i(TAG,"qty "+qty);
                    float netSellingPrice = getOfferAmount(item);
                    Log.i(TAG,"netSellingPrice "+netSellingPrice);
                    float amount = item.getTotalAmount() + netSellingPrice;
                    Log.i(TAG,"tot amount "+amount);
                    item.setTotalAmount(amount);
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
                    Log.i(TAG,"qty "+qty);
                    float netSellingPrice = getOfferAmount(item);
                    Log.i(TAG,"netSellingPrice "+netSellingPrice);
                    float amount = item.getTotalAmount() + netSellingPrice;
                    Log.i(TAG,"tot amount "+amount);
                    item.setTotalAmount(amount);
                    dbHelper.updateCartData(item.getProdId(),qty,amount);
                    if(itemList.size() == 1){
                        relativeLayoutCartFooter.setVisibility(View.VISIBLE);
                    }
                    setFooterValues();
                    myItemAdapter.notifyItemChanged(position);
                }
            }

        }else if(type == 3){
            String offerName = item.getProductPriceOfferList().get(0).getOfferName();
           /* OfferDescriptionFragment offerDescriptionFragment = new OfferDescriptionFragment();
            offerDescriptionFragment.setFlag("productPriceOffer");
            offerDescriptionFragment.setColorTheme(colorTheme);
            Bundle bundle = new Bundle();
            bundle.putString("offerName",offerName);
            offerDescriptionFragment.setArguments(bundle);
            offerDescriptionFragment.setMyItemTypeClickListener(CartActivity.this);
            offerDescriptionFragment.show(getSupportFragmentManager(), "Product Price Offer");*/

            rlOfferDesc.setVisibility(View.VISIBLE);
            ImageView iv_clear = findViewById(R.id.iv_clear);
            //Utility.setColorFilter(iv_clear.getDrawable(),colorTheme);
            TextView tvOfferName = findViewById(R.id.text_offer_name);
            findViewById(R.id.relative_footer_action).setBackgroundColor(colorTheme);
            TextView tv = findViewById(R.id.text_action);
            tv.setText("OKAY! GOT IT");

            tvOfferName.setText(offerName);

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
            ProductComboOffer productComboOffer = item.getProductPriceOfferList().get(0);
            float totOfferAmt = 0f;
            for(ProductComboDetails productComboDetails : productComboOffer.getProductComboOfferDetails()){
                totOfferAmt = totOfferAmt + productComboDetails.getPcodPrice();
                offerDescList.add("Buy "+productComboDetails.getPcodProdQty()+" at Rs "+
                        Utility.numberFormat(totOfferAmt));
            }

            offerDescList.add("Offer valid till "+Utility.parseDate(productComboOffer.getEndDate(),"yyyy-MM-dd",
                    "EEE dd MMMM, yyyy")+" 23:59 PM");

            RecyclerView recyclerViewOfferDesc=findViewById(R.id.recycler_view_offer_desc);
            recyclerViewOfferDesc.setHasFixedSize(true);
            final RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
            recyclerViewOfferDesc.setLayoutManager(layoutManager);
            recyclerViewOfferDesc.setItemAnimator(new DefaultItemAnimator());
            OfferDescAdapter offerDescAdapter =new OfferDescAdapter(this,offerDescList);
            recyclerViewOfferDesc.setAdapter(offerDescAdapter);
            recyclerViewOfferDesc.setNestedScrollingEnabled(false);
        }
    }

    private float getOfferAmount(MyProductItem item){
        float amount = 0f;
        int qty = item.getQty();
        ProductComboOffer productComboOffer = item.getProductPriceOfferList().get(0);
        if(qty > 1){
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
        return amount;
    }

    private float getOfferPrice(int qty,float sp,List<ProductComboDetails> productComboDetailsList){
        float amount = 0f;
        for(ProductComboDetails productComboDetails:productComboDetailsList){
            if(productComboDetails.getPcodProdQty() == qty){
                amount = productComboDetails.getPcodPrice();
                Log.i(TAG,"offer price "+amount);
                break;
            }else{
                amount = sp;
            }
        }
        Log.i(TAG,"final selling price "+amount);
        return amount;
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
            bottomSearchFragment = new BottomSearchFragment();
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
        item.setProductPriceOfferList(dbHelper.getProductPriceOffer(""+item.getProdId()));
        Log.i(TAG,"offer sizze "+item.getProductPriceOfferList().size());

        if(item.getIsBarCodeAvailable().equals("Y")){
            item.setBarcodeList(dbHelper.getBarCodesForCart(prodId));
            if(item.getBarcodeList() != null && item.getBarcodeList().size() > 0){
                item.setQty(1);
                item.setTotalAmount(item.getProdSp());
                dbHelper.addProductToCart(item);
                itemList.add(item);
                myItemAdapter.notifyDataSetChanged();
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
                //myItemAdapter.notifyItemChanged(itemList.size() -1 );
                myItemAdapter.notifyDataSetChanged();
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
