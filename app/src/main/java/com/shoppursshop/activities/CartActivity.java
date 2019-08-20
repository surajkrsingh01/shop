package com.shoppursshop.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
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
import com.shoppursshop.interfaces.MyImageClickListener;
import com.shoppursshop.interfaces.MyItemClickListener;
import com.shoppursshop.interfaces.MyItemTypeClickListener;
import com.shoppursshop.models.Barcode;
import com.shoppursshop.models.MyProductItem;
import com.shoppursshop.models.ProductComboDetails;
import com.shoppursshop.models.ProductComboOffer;
import com.shoppursshop.models.ProductDiscountOffer;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.DialogAndToast;
import com.shoppursshop.utilities.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends NetworkBaseActivity implements MyItemTypeClickListener, MyItemClickListener, MyImageClickListener {

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

    private float totalPrice,totalTax,totDiscount,offerPer,offerMaxAmount,deliveryDistance,deliveryCharges;
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
    private Button btnStoreOffers;
    private int position;
    private float viewScrolled = 0;
    private FloatingActionButton fabScan;
    private String address;
    private String country;
    private String state;
    private String city;
    private String zip;

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
        btnStoreOffers = findViewById(R.id.btn_store_offers);
        fabScan = findViewById(R.id.fab);

        Utility.setColorFilter(imageViewRemoveOffer.getDrawable(),colorTheme);
        Utility.setColorFilter(btnStoreOffers.getBackground(),colorTheme);

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
        myItemAdapter.setMyImageClickListener(this);
        myItemAdapter.setDarkTheme(isDarkTheme);
        myItemAdapter.setMyItemTypeClickListener(this);
        recyclerView.setAdapter(myItemAdapter);
        recyclerView.setNestedScrollingEnabled(false);

        tvCheckout.setText("Make Payment");


        tvCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativeLayoutPayOptionLayout.setVisibility(View.VISIBLE);

                String deviceType = sharedPreferences.getString(Constants.ANDROID_DEVICE_TYPE,"");
                Log.i(TAG,"device type "+deviceType);
                if(deviceType.equals("N910")){
                    tvMPos.setVisibility(View.GONE);
                    tvCard.setVisibility(View.GONE);
                    tvAndroidPos.setVisibility(View.VISIBLE);
                }else if(deviceType.equals("Android")){
                    tvAndroidPos.setVisibility(View.GONE);
                    tvCard.setVisibility(View.GONE);
                    tvMPos.setVisibility(View.VISIBLE);
                }
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

        fabScan.setOnClickListener(new View.OnClickListener() {
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

        findViewById(R.id.rb_home_delivery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rb_home_delivery.isChecked() == true){
                    if(totalPrice > sharedPreferences.getInt(Constants.MIN_DELIVERY_AMOUNT,0)){
                        startActivityForResult(new Intent(CartActivity.this, DeliveryAddressActivity.class), 101);
                    }
                }

            }
        });

        rg_delivery.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                deliveryTypeId = checkedId;
                if(checkedId == R.id.rb_self_delivery){
                    tv_self_status.setText("Self Delivery");
                    //tv_self_status.setVisibility(View.VISIBLE);
                    tv_address_label.setVisibility(View.GONE);
                   // seperator_delivery_address.setVisibility(View.GONE);
                    tv_address.setVisibility(View.GONE);
                    tvDeliveryCharges.setText("0.00");
                    deliveryDistance = 0;
                    rlDelivery.setVisibility(View.GONE);
                    setFooterValues();
                }else{
                    rlDelivery.setVisibility(View.VISIBLE);
                    if(totalPrice > sharedPreferences.getInt(Constants.MIN_DELIVERY_AMOUNT,0)){
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

        btnStoreOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CartActivity.this, ApplyOffersActivity.class);
                intent.putExtra("custCode",getIntent().getStringExtra("custCode"));
                intent.putExtra("flag","offers");
                startActivityForResult(intent,6);
            }
        });

        final NestedScrollView nsv = findViewById(R.id.nested_scrollview);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            nsv.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY > oldScrollY) {
                        Log.i(TAG, "Scroll UP");
                        if(fabScan.getVisibility() == View.VISIBLE)
                        fabScan.setVisibility(View.GONE);
                    }
                    if (scrollY < oldScrollY) {
                        Log.i(TAG, "Scroll DOWN");
                        if(fabScan.getVisibility() == View.GONE)
                        fabScan.setVisibility(View.VISIBLE);
                    }
                }
            });
        }else{
            nsv.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {
                    if (viewScrolled < nsv.getScrollY()){
                        viewScrolled = nsv.getScrollY();
                        Log.d(TAG, "scrolling up");
                        if(fabScan.getVisibility() == View.VISIBLE)
                        fabScan.setVisibility(View.GONE);

                    }
                    if (viewScrolled > nsv.getScrollY()){
                        viewScrolled = nsv.getScrollY();
                        Log.d(TAG, "scrolling down");
                        if(fabScan.getVisibility() == View.GONE)
                        fabScan.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(requestCode ==101 && intent!=null){
            address = intent.getStringExtra("address");
            country = intent.getStringExtra("country");
            state = intent.getStringExtra("state");
            city = intent.getStringExtra("city");
            zip = intent.getStringExtra("zip");
            deliveryDistance = (float)intent.getDoubleExtra("distance",0d);
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
                if(ob.getProdSp() != 0f)
                setOffer(ob);
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


        totalTax = dbHelper.getTotalTaxesart();
        tvTotalTaxes.setText(Utility.numberFormat(totalTax));
        totalPrice = dbHelper.getTotalPriceCart();
        tvItemTotal.setText(Utility.numberFormat(totalPrice - totalTax));
        totDiscount = dbHelper.getTotalMrpPriceCart() - dbHelper.getTotalPriceCart();
        float dis = 0f;
        if(offerPer > 0f){
            dis = totalPrice * offerPer / 100;
            totDiscount = totDiscount + dis ;
        }

        Log.i(TAG," Taxes "+dbHelper.getTotalTaxesart());
        Log.i(TAG," MRP "+dbHelper.getTotalMrpPriceCart()
                +" Price "+dbHelper.getTotalPriceCart());
        Log.i(TAG," diff "+totDiscount+" offerPer "+offerPer+" totalPrice "+totalPrice);

        if(totDiscount == 0f){
            rlDiscount.setVisibility(View.GONE);
        }else{
            rlDiscount.setVisibility(View.VISIBLE);
            tvTotalDiscount.setText(Utility.numberFormat(totDiscount));
        }

        totalPrice = totalPrice - dis;
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
                    shopObject.put("paymentMode",paymentMode);
                    if(address == null){
                        shopObject.put("orderDeliveryMode","Self");
                        shopObject.put("deliveryAddress","");
                        shopObject.put("deliveryCountry","");
                        shopObject.put("deliveryState","");
                        shopObject.put("deliveryCity","");
                        shopObject.put("pinCode","");
                    }else{
                        shopObject.put("orderDeliveryMode","Home");
                        shopObject.put("deliveryAddress",address);
                        shopObject.put("deliveryCountry",country);
                        shopObject.put("deliveryState",state);
                        shopObject.put("deliveryCity",city);
                        shopObject.put("pinCode",zip);
                    }

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
                    shopObject.put("ordCouponId",String.valueOf(offerId));
                    shopObject.put("totCgst",String.valueOf(dbHelper.getTaxesCart("cgst")));
                    shopObject.put("totSgst",String.valueOf(dbHelper.getTaxesCart("sgst")));
                    shopObject.put("totIgst",String.valueOf(dbHelper.getTaxesCart("igst")));
                    shopObject.put("totTax",String.valueOf(totalTax));
                    shopObject.put("deliveryCharges",String.valueOf(deliveryCharges));
                    shopObject.put("totDiscount",String.valueOf(totDiscount));
                    shopObject.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
                    shopObject.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
                    shopObject.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));

                    productObject.put("prodCode", cartItem.getProdCode());
                    productObject.put("prodHsnCode", cartItem.getProdHsnCode());
                    productObject.put("prodId", cartItem.getProdId());

                    if(cartItem.getComboProductIds() != null)
                    productObject.put("comboProdIds", cartItem.getComboProductIds());

                    if(cartItem.getIsBarCodeAvailable().equals("Y")){
                        productObject.put("prodBarCode", cartItem.getBarcodeList().get(0).getBarcode());
                        productObject.put("barcodeList",  tempbarcodeArray);
                    }

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
                    if(cartItem.getOfferCounter() > 0){
                        productObject.put("offerId", cartItem.getOfferId());
                        productObject.put("offerType", cartItem.getOfferType());
                        productObject.put("freeItems", String.valueOf(cartItem.getOfferCounter()));
                    }
                    productArray.put(productObject);
                    shopObject.put("myProductList", productArray);
                    shopArray.put(shopObject);
                } else {
                    productObject = new JSONObject();
                    if(cartItem.getComboProductIds() != null)
                    productObject.put("comboProdIds", cartItem.getComboProductIds());
                    productObject.put("prodCode", cartItem.getProdCode());
                    productObject.put("prodHsnCode", cartItem.getProdHsnCode());
                    productObject.put("prodId", cartItem.getProdId());
                    if(cartItem.getIsBarCodeAvailable().equals("Y")){
                        productObject.put("prodBarCode", cartItem.getBarcodeList().get(0).getBarcode());
                        productObject.put("barcodeList",  tempbarcodeArray);
                    }
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
                    productObject.put("prodCgst", cartItem.getProdCgst());
                    productObject.put("prodSgst", cartItem.getProdSgst());
                    productObject.put("prodIgst", cartItem.getProdIgst());
                    productObject.put("prodSp", cartItem.getProdSp());

                    if(cartItem.getOfferCounter() > 0){
                        productObject.put("offerId", cartItem.getOfferId());
                        productObject.put("offerType", cartItem.getOfferType());
                        productObject.put("freeItems", String.valueOf(cartItem.getOfferCounter()));
                    }

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
                            dbHelper.setQoh(cartItem.getProdId(),-cartItem.getQty());
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
                        intent.putExtra("ordCouponId",""+offerId);
                        intent.putExtra("totalTax",totalTax);
                        intent.putExtra("deliveryCharges",deliveryCharges);
                        intent.putExtra("totDiscount",totDiscount);
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
        this.position = position;
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
                float netSellingPrice = getOfferAmount(item,type);
               // item.setQty(qty);
                qty = item.getQty();
                Log.i(TAG,"netSellingPrice "+netSellingPrice);
                float amount = item.getTotalAmount() - netSellingPrice;
                Log.i(TAG,"tot amount "+amount);
                item.setTotalAmount(amount);
                dbHelper.updateCartData(item,qty,amount);
                setFooterValues();
                if(qty == 0){
                    itemList.remove(position);
                }
                myItemAdapter.notifyItemChanged(position);
            }
        }else if(type == 2){
            if(item.getQty() == item.getProdQoh()){
                DialogAndToast.showDialog("There are no more stocks",this);
            }else{
                int qty = item.getQty() + 1;
                item.setQty(qty);
                float netSellingPrice = getOfferAmount(item,type);
                Log.i(TAG,"netSellingPrice "+netSellingPrice);
                float amount = item.getTotalAmount() + netSellingPrice;
                Log.i(TAG,"tot amount "+amount);
                item.setTotalAmount(amount);
                qty = item.getQty();
                Log.i(TAG,"qty "+qty);
                dbHelper.updateCartData(item,qty,amount);
                if(itemList.size() == 1){
                    relativeLayoutCartFooter.setVisibility(View.VISIBLE);
                }
                setFooterValues();
                myItemAdapter.notifyItemChanged(position);
            }

        }else if(type == 3){
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
                    totOfferAmt = totOfferAmt + productComboDetails.getPcodPrice();
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
        }else if(type == 4){

        }
    }

    private float getOfferAmount(MyProductItem item,int type){
        float amount = 0f;
        int qty = item.getQty();
        if(item.getProductOffer() instanceof ProductComboOffer){
            ProductComboOffer productComboOffer = (ProductComboOffer)item.getProductOffer();
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

            if(type == 1)
            item.setQty(item.getQty() - 1);

        }else if(item.getProductOffer() instanceof ProductDiscountOffer){

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
                        dbHelper.updateOfferCounterCartData(item.getOfferCounter(),item.getProdId());

                    }else{
                        item.setQty(item.getQty() - 1);
                    }
                }else{
                    item.setQty(item.getQty() - 1);
                    Log.i(TAG,"minus mode "+item.getQty() % productDiscountOffer.getProdBuyQty());
                    if(item.getQty() % productDiscountOffer.getProdBuyQty() == (productDiscountOffer.getProdBuyQty()-1)){
                        item.setOfferCounter(item.getOfferCounter() - 1);
                        if(item.getOfferCounter() == 0){
                            dbHelper.removeFreeProductFromCart(productDiscountOffer.getProdFreeId());
                            itemList.remove(item.getFreeProductPosition());
                          //  myItemAdapter.notifyItemRemoved(item.getFreeProductPosition());
                            myItemAdapter.notifyDataSetChanged();
                        }else{
                            MyProductItem item1 = itemList.get(item.getFreeProductPosition());
                            item1.setQty(item.getOfferCounter());
                            dbHelper.updateFreeCartData(productDiscountOffer.getProdFreeId(),item.getOfferCounter(),0f);
                            myItemAdapter.notifyItemChanged(item.getFreeProductPosition());
                            dbHelper.updateOfferCounterCartData(item.getOfferCounter(),item.getProdId());
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
                        dbHelper.updateOfferCounterCartData(item.getOfferCounter(),item.getProdId());
                    }else{

                    }
                }else{
                    Log.i(TAG,"Different product");
                   if(item.getQty() % productDiscountOffer.getProdBuyQty() == 0){
                       item.setOfferCounter(item.getOfferCounter() + 1);
                       MyProductItem item1 = null;
                       if(item.getOfferCounter() == 1){
                           item1 = dbHelper.getProductDetails(productDiscountOffer.getProdFreeId());
                           item1.setProdSp(0f);
                           item1.setQty(1);
                           item1.setFreeProductPosition(position+1);
                           dbHelper.addProductToCart(item1);
                           itemList.add(position+1,item1);
                           item.setFreeProductPosition(position+1);
                           dbHelper.updateFreePositionCartData(item.getFreeProductPosition(),item.getProdId());
                           dbHelper.updateOfferCounterCartData(item.getOfferCounter(),item.getProdId());
                           //myItemAdapter.notifyItemInserted(itemList.size()-1);
                           myItemAdapter.notifyDataSetChanged();
                           Log.i(TAG,"Different product added to cart");
                       }else{
                           item1 = itemList.get(item.getFreeProductPosition());
                           item1.setQty(item.getOfferCounter());
                           dbHelper.updateFreeCartData(item1.getProdId(),item1.getQty(),0f);
                           dbHelper.updateOfferCounterCartData(item.getOfferCounter(),item.getProdId());
                           myItemAdapter.notifyItemChanged(item.getFreeProductPosition());
                           Log.i(TAG,"Different product updated in cart");
                       }
                     //  myItemAdapter.notifyDataSetChanged();
                   }

                }

            }else{
                amount = item.getProdSp();
            }

        }else{
            amount = item.getProdSp();
            if(type == 1)
                item.setQty(item.getQty() - 1);
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
        setOffer(item);
       // item.setProductPriceOfferList(dbHelper.getProductPriceOffer(""+item.getProdId()));

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

    private void setOffer(MyProductItem item){
        List<ProductComboOffer> comboOfferList = dbHelper.getProductPriceOffer(""+item.getProdId());
        List<ProductDiscountOffer> productDiscountOfferList = dbHelper.getProductFreeOffer(""+item.getProdId());
        Log.i(TAG,"comboOfferList size "+comboOfferList.size());
        Log.i(TAG,"productDiscountOfferList size "+productDiscountOfferList.size());

        if(comboOfferList.size() > 0){
            item.setProductOffer(comboOfferList.get(0));
            item.setOfferId(""+comboOfferList.get(0).getId());
            item.setOfferType("price");
        }else if(productDiscountOfferList.size() > 0){
            item.setProductOffer(productDiscountOfferList.get(0));
            item.setOfferId(""+productDiscountOfferList.get(0).getId());
            item.setOfferType("free");
        }
    }

    @Override
    public void onImageClicked(int position, int type, View view) {
        MyProductItem item = (MyProductItem)itemList.get(position);
        showImageDialog(item.getProdImage1(),view);
    }
}
