package com.shoppursshop.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.android.volley.Request;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
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

import com.shoppursshop.R;
import com.shoppursshop.activities.payment.PaymentActivity;
import com.shoppursshop.activities.payment.ccavenue.activities.CCAvenueWebViewActivity;
import com.shoppursshop.activities.payment.ccavenue.utility.AvenuesParams;
import com.shoppursshop.activities.payment.mPos.MPayActivity;
import com.shoppursshop.activities.settings.ApplyOffersActivity;
import com.shoppursshop.adapters.CartAdapter;
import com.shoppursshop.adapters.OfferDescAdapter;
import com.shoppursshop.database.DbHelper;
import com.shoppursshop.fragments.BottomSearchFragment;
import com.shoppursshop.fragments.OfferDescriptionFragment;
import com.shoppursshop.fragments.SearchProductFragment;
import com.shoppursshop.interfaces.MyImageClickListener;
import com.shoppursshop.interfaces.MyItemClickListener;
import com.shoppursshop.interfaces.MyItemTypeClickListener;
import com.shoppursshop.models.Barcode;
import com.shoppursshop.models.MyProductItem;
import com.shoppursshop.models.ProductComboDetails;
import com.shoppursshop.models.ProductComboOffer;
import com.shoppursshop.models.ProductDiscountOffer;
import com.shoppursshop.models.ProductSize;
import com.shoppursshop.models.ProductUnit;
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

public class ShopCartActivity extends NetworkBaseActivity implements MyItemTypeClickListener, MyItemClickListener, MyImageClickListener {

    private RecyclerView recyclerView;
    private CartAdapter myItemAdapter;
    private List<MyProductItem> itemList;

    private RelativeLayout relativeLayoutCartFooter;
    private LinearLayout rlAddressBilling;
    private TextView tvItemCount,tvItemPrice,tvCheckout,tvItemTotal,tvTotalTaxes,tvSgst,tvTotalDiscount,
            tvDeliveryCharges,tvNetPayable,tvOfferName;
    private RelativeLayout relativeLayoutPayOptionLayout,rlDiscount,rlDelivery,rl_offer_applied_layout,rlOfferLayout;
    private TextView tvCash,tvCard;

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

    private String flag,shopCode, shopName, shopImage, shopMobile, shopAddress, statecity, catId,
            shopPin, shopCountry, shopState, shopCity,shopdbname,shoppursDeliveryAvailable;
    private int shopppursMinDeliveryAmt,shoppursMinDeliveryTime,shoppursMinDeliveryDistance,
                shoppursChargesAfterMinDistance;
    private int type,productDetailsType;
    private MyProductItem myProduct,freeProdut;
    private BottomSearchFragment bottomSearchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_cart);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();
    }

    private void init(){

        shopCode = "SHP1";
        shopName = "Areeva Products & Services";
        shopdbname = "SHP1";
        shopImage = "";
        shopMobile = "9718181697";
        shopAddress = "Harikunj Apartments";
        statecity = "Gurgaon, Haryana";
        shopPin = "110091";
        shopCountry = "India";
        shopState = "Haryana";
        shopCity = "Gurgaon";

        address = sharedPreferences.getString(Constants.ADDRESS,"");
        country = sharedPreferences.getString(Constants.COUNTRY,"");
        state = sharedPreferences.getString(Constants.STATE,"");
        city = sharedPreferences.getString(Constants.CITY,"");
        zip = sharedPreferences.getString(Constants.ZIP,"");

        itemList = new ArrayList<>();
        imageViewScan = findViewById(R.id.image_scan);
        imageViewSearch = findViewById(R.id.image_search);
        tvItemCount=findViewById(R.id.itemCount);
        tvItemPrice=findViewById(R.id.itemPrice);
        tvCheckout=findViewById(R.id.viewCart);
        tvCard = findViewById(R.id.tv_pay_card);
        tvCash = findViewById(R.id.tv_pay_cash);
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
                //relativeLayoutPayOptionLayout.setVisibility(View.VISIBLE);
                generateJson("Online");
            }
        });

        tvCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                SearchProductFragment bottomSearchFragment = new SearchProductFragment();
                Bundle bundle = new Bundle();
                bundle.putString("shopCode", shopCode);
                bundle.putString("shopName", shopName);
                bundle.putString("shopAddress", address);
                bundle.putString("shopMobile", shopMobile);
                bottomSearchFragment.setArguments(bundle);
                bottomSearchFragment.setCallingActivityName("ShopProductListActivity", sharedPreferences, isDarkTheme);
               // bottomSearchFragment.setSubCatName(selectedSubCatName);
              //  bottomSearchFragment.setSubcatId(selectdSubCatId);
                bottomSearchFragment.setMyItemClickListener(ShopCartActivity.this);
                bottomSearchFragment.show(getSupportFragmentManager(), bottomSearchFragment.getTag());
            }
        });
        tv_mode = findViewById(R.id.tv_mode);
        tv_self_status = findViewById(R.id.tv_self_status);
        rg_delivery = findViewById(R.id.rg_delivery);
        tv_address_label = findViewById(R.id.tv_address_label);
        tv_address = findViewById(R.id.tv_address);
        // seperator_delivery_address = findViewById(R.id.seperator_delivery_address);
        rb_home_delivery = findViewById(R.id.rb_home_delivery);
        rb_self_delivery = findViewById(R.id.rb_self_delivery);

        rb_self_delivery.setChecked(true);

        findViewById(R.id.rb_home_delivery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rb_home_delivery.isChecked() == true){
                    if(totalPrice > shopppursMinDeliveryAmt){
                        startActivityForResult(new Intent(ShopCartActivity.this, DeliveryAddressActivity.class), 101);
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
                    if(totalPrice > shopppursMinDeliveryAmt){
                        tv_self_status.setVisibility(View.GONE);
                    }else{
                        rb_self_delivery.setChecked(true);
                        DialogAndToast.showDialog("Minimum amount should be more than Rs."
                                +sharedPreferences.getInt(Constants.MIN_DELIVERY_AMOUNT,0)+" for home delivery.",ShopCartActivity.this);
                    }

                }
            }
        });

        tv_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(ShopCartActivity.this, DeliveryAddressActivity.class), 101);
                tv_self_status.setVisibility(View.GONE);
            }
        });

        tv_address_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(ShopCartActivity.this, DeliveryAddressActivity.class), 101);
                tv_self_status.setVisibility(View.GONE);
            }
        });

        tvApplyCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShopCartActivity.this,CouponOffersActivity.class);
                intent.putExtra("custCode",sharedPreferences.getString(Constants.SHOP_CODE,""));
                intent.putExtra("flag","shoppursCoupons");
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
                Intent intent = new Intent(ShopCartActivity.this,ShoppursProductListActivity.class);
                intent.putExtra("flag","shoppursOfferProducts");
                startActivity(intent);
               // startActivityForResult(intent,6);
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

        getShopDetails();
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
        }else if(requestCode == 10){
            if(intent != null){
                String rawValue = intent.getStringExtra("barCode");
                MyProductItem myProductItem = dbHelper.getProductDetailsByBarCode(rawValue);
                if(myProductItem.getIsBarCodeAvailable().equals("Y")){
                    myProductItem.setBarcodeList(dbHelper.getBarCodesForCart(myProductItem.getProdId()));
                }
                if(myProductItem.getBarcodeList().size() > 0){
                    myProductItem.setQty(1);
                    float netSellingPrice = getOfferAmount(myProductItem,2);
                    myProductItem.setTotalAmount(netSellingPrice);
                    dbHelper.addProductToShopCart(myProductItem);
                    showMyBothDialog("Product is added in cart.","Checkout","Scan More");
                }else{
                    showMyDialog("Product is out of stock.");
                }
            }

        }
    }

    private void openScannar(){
        Intent intent = new Intent(this,ScannarActivity.class);
        intent.putExtra("flag","scan");
        intent.putExtra("type","addToCart");
        startActivityForResult(intent,10);

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
            itemList = dbHelper.getShopCartProducts();
        }else{
            itemList.clear();
            List<MyProductItem> cartList =  dbHelper.getShopCartProducts();
            itemList.addAll(cartList);
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


        totalTax = dbHelper.getTotalTaxesShopCart();
        tvTotalTaxes.setText(Utility.numberFormat(totalTax));
        totalPrice = dbHelper.getTotalPriceShopCart();
        tvItemTotal.setText(Utility.numberFormat(totalPrice - totalTax));
        //  totDiscount = dbHelper.getTotalMrpPriceCart() - dbHelper.getTotalPriceCart();
        float dis = 0f;
        if(offerPer > 0f){
            dis = totalPrice * offerPer / 100;
            //    totDiscount = totDiscount + dis ;
        }

        Log.i(TAG," Taxes "+dbHelper.getTotalTaxesShopCart());
        Log.i(TAG," MRP "+dbHelper.getTotalMrpShopPriceCart()
                +" Price "+dbHelper.getTotalPriceShopCart());

        totalPrice = totalPrice - dis;
        if(deliveryTypeId == R.id.rb_self_delivery){
            rlDelivery.setVisibility(View.GONE);
        }else{
            rlDelivery.setVisibility(View.VISIBLE);
            Log.i(TAG,"Min Delivery Distance "+shoppursChargesAfterMinDistance);
            Log.i(TAG,"Charges after Delivery Distance "+shoppursChargesAfterMinDistance);
            Log.i(TAG,"Delivery Distance "+deliveryDistance);
            if(deliveryDistance > shoppursMinDeliveryDistance){
                float diffKms = deliveryDistance -  shoppursMinDeliveryDistance;
                int intKms = (int)diffKms;
                float decimalKms = diffKms - intKms;

                int chargesPerKm = shoppursChargesAfterMinDistance;
                deliveryCharges = intKms * chargesPerKm + decimalKms * chargesPerKm;
                tvDeliveryCharges.setText(Utility.numberFormat(deliveryCharges));
            }else{
                tvDeliveryCharges.setText("0.00");
            }
        }

        totDiscount = dbHelper.getTotalMrpShopPriceCart() - totalPrice;
        Log.i(TAG," diff "+totDiscount+" offerPer "+offerPer+" totalPrice "+totalPrice);

        if(totDiscount == 0f){
            rlDiscount.setVisibility(View.GONE);
        }else{
            rlDiscount.setVisibility(View.VISIBLE);
            tvTotalDiscount.setText(Utility.numberFormat(totDiscount));
        }

        totalPrice = totalPrice + deliveryCharges;
        tvItemPrice.setText("Rs "+Utility.numberFormat(totalPrice));
        tvNetPayable.setText("Rs "+Utility.numberFormat(totalPrice));

        tvCheckout.setVisibility(View.VISIBLE);
        rlAddressBilling.setVisibility(View.VISIBLE);

    }

    private void generateOrder(JSONArray shopArray){
        Log.d(TAG, shopArray.toString());
        String url=getResources().getString(R.string.url)+"/api/device/order/generate_order";
        showProgress(true);
        jsonArrayV2ApiRequest(Request.Method.POST,url, shopArray,"generate_order");
    }

    private void getShopDetails(){
        Map<String,String> params=new HashMap<>();
        params.put("shopCode", shopCode);
        params.put("dbName", sharedPreferences.getString(Constants.DB_NAME, ""));
        String url=getResources().getString(R.string.url)+"/api/user/shopDetails";
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url, new JSONObject(params),"shopDetails");
    }

    private void getProductDetails(String prodId){
        if(productDetailsType==1)
            showProgress(true);
        Map<String,String> params=new HashMap<>();
        params.put("id", prodId); // as per user selected category from top horizontal categories list
        params.put("code", shopCode);
        params.put("dbName",shopCode);
        Log.d(TAG, params.toString());
        String url=getResources().getString(R.string.cust_url)+"/api/customers/products/ret_products_details";
        jsonObjectApiRequest(Request.Method.POST, url,new JSONObject(params),"productDetails");
    }

    private void generateJson(String paymentMode){
        try {
            cartItemList = dbHelper.getShopCartProducts();

            List<String> tempShopList = new ArrayList<>();
            shopArray = new JSONArray();
            JSONObject shopObject = new JSONObject();
            JSONArray productArray = new JSONArray();
            JSONObject productObject = new JSONObject();

            JSONArray tempbarcodeArray =null;
            JSONObject tempbarcodeObject = null;

           // String shopCode = sharedPreferences.getString(Constants.SHOP_CODE,"");
            MyProductItem cartProductItem = null;
            for (MyProductItem cartItem : cartItemList) {
                cartProductItem = dbHelper.getShopCartProductDetails(cartItem.getProdId());
                Log.d(TAG, cartItem.getProdBarCode()+"");
                if (!tempShopList.contains(shopCode)) {
                    //Log.d("PRD list "+tempShopList.toString(), cartItem.getShopCode());
                    tempShopList.add(shopCode);
                    shopObject = new JSONObject();
                    productArray = new JSONArray();
                    productObject = new JSONObject();

                   /* if(cartItem.getIsBarCodeAvailable().equals("Y")){
                        cartItem.setBarcodeList(dbHelper.getBarCodesForCart(cartItem.getProdId()));
                        tempbarcodeArray = new JSONArray();
                        for (int i=0;i<cartItem.getQty();i++){
                            tempbarcodeObject = new JSONObject();
                            tempbarcodeObject.put("barcode", cartItem.getBarcodeList().get(i).getBarcode());
                            tempbarcodeArray.put(tempbarcodeObject);
                        }
                    }*/

                    shopObject.put("shopCode", shopCode);
                    shopObject.put("orderDate", Utility.getTimeStamp());
                    shopObject.put("orderDeliveryNote","Note");
                    shopObject.put("orderDeliveryMode","Self");
                    shopObject.put("paymentMode","Online");
                    shopObject.put("deliveryAddress",address);
                    shopObject.put("deliveryCountry",country);
                    shopObject.put("deliveryState",state);
                    shopObject.put("deliveryCity",city);
                    shopObject.put("pinCode",zip);
                    shopObject.put("createdBy",sharedPreferences.getString(Constants.FULL_NAME,""));
                    shopObject.put("updateBy",sharedPreferences.getString(Constants.FULL_NAME,""));
                    shopObject.put("orderStatus","pending");
                    shopObject.put("orderPaymentStatus", "pending");
                    shopObject.put("custName", sharedPreferences.getString(Constants.SHOP_NAME,""));
                    shopObject.put("custCode",sharedPreferences.getString(Constants.SHOP_CODE,""));
                    shopObject.put("mobileNo",sharedPreferences.getString(Constants.MOBILE_NO,""));
                    shopObject.put("orderImage",sharedPreferences.getString(Constants.PHOTO,""));

                    shopObject.put("createdBy",sharedPreferences.getString(Constants.FULL_NAME,""));
                    shopObject.put("updateBy",sharedPreferences.getString(Constants.FULL_NAME,""));
                    if(paymentMode.equals("Cash")){
                        shopObject.put("orderStatus","Delivered");
                        shopObject.put("orderPaymentStatus", "Done");
                    }else{
                        shopObject.put("orderStatus","pending");
                        shopObject.put("orderPaymentStatus", "pending");
                    }
                    shopObject.put("custUserCreateStatus","A");
                    shopObject.put("totalQuantity",String.valueOf(dbHelper.getTotalQuantityCart()));
                    shopObject.put("toalAmount",String.valueOf(totalPrice));
                    shopObject.put("ordCouponId",String.valueOf(offerId));
                    shopObject.put("totCgst",String.valueOf(dbHelper.getTaxesShopCart("cgst")));
                    shopObject.put("totSgst",String.valueOf(dbHelper.getTaxesShopCart("sgst")));
                    shopObject.put("totIgst",String.valueOf(dbHelper.getTaxesShopCart("igst")));
                    shopObject.put("totTax",String.valueOf(totalTax));
                    shopObject.put("deliveryCharges",String.valueOf(deliveryCharges));
                    shopObject.put("totDiscount",String.valueOf(dbHelper.getTotalMrpShopPriceCart() - totalPrice));
                    shopObject.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
                    shopObject.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
                    shopObject.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));

                    productObject.put("prodCode", cartItem.getProdCode());
                    productObject.put("prodHsnCode", cartItem.getProdHsnCode());
                    productObject.put("prodId", cartItem.getProdId());

                    if(cartItem.getComboProductIds() != null)
                        productObject.put("comboProdIds", cartItem.getComboProductIds());

                    /*if(cartItem.getIsBarCodeAvailable().equals("Y")){
                        productObject.put("prodBarCode", cartItem.getBarcodeList().get(0).getBarcode());
                        productObject.put("barcodeList",  tempbarcodeArray);
                    }*/

                    productObject.put("qty", cartItem.getQty());
                    productObject.put("prodName",cartItem.getProdName());
                    productObject.put("prodUnit",cartItem.getUnit());
                    productObject.put("prodSize",cartItem.getSize());
                    productObject.put("prodColor",cartItem.getColor());
                    productObject.put("prodDesc",cartItem.getProdDesc());
                    productObject.put("prodMrp",cartItem.getProdMrp());
                    productObject.put("prodSp", cartItem.getProdSp());
                    productObject.put("prodCgst", cartProductItem.getProdCgst());
                    productObject.put("prodSgst", cartProductItem.getProdSgst());
                    productObject.put("prodIgst", cartProductItem.getProdIgst());
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
                   /* if(cartItem.getIsBarCodeAvailable().equals("Y")){
                        productObject.put("prodBarCode", cartItem.getBarcodeList().get(0).getBarcode());
                        productObject.put("barcodeList",  tempbarcodeArray);
                    }*/
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
                    Intent intent = new Intent(ShopCartActivity.this, CCAvenueWebViewActivity.class);
                    intent.putExtra("flag", "wallet");
                    intent.putExtra(AvenuesParams.AMOUNT, String.format("%.02f",totalPrice));
                    intent.putExtra(AvenuesParams.ORDER_ID, orderNumber);
                    intent.putExtra(AvenuesParams.CURRENCY, "INR");
                    intent.putExtra("flag", "buyShoppursProduct");
                    intent.putExtra("shopArray",shopArray.toString());
                    startActivity(intent);
                    finish();
                    //placeOrder(shopArray, orderId);
                }else {
                    DialogAndToast.showToast(response.getString("message"),ShopCartActivity.this);
                }
            }else if(apiName.equals("place_order")){
                if(response.getString("status").equals("true")||response.getString("status").equals(true)){
                    dbHelper.deleteTable(DbHelper.CART_TABLE);
                    itemList.clear();
                    myItemAdapter.notifyDataSetChanged();
                    relativeLayoutCartFooter.setVisibility(View.GONE);
                    Log.d(TAG, "Ordeer Placed" );
                }else {
                    DialogAndToast.showToast(response.getString("message"),ShopCartActivity.this);
                }
            }else if(apiName.equals("productDetails")){
                if(response.getString("status").equals("true")||response.getString("status").equals(true)){
                    JSONObject jsonObject = response.getJSONObject("result");
                    if(productDetailsType == 1){
                        itemList.get(position).setProdQoh(jsonObject.getInt("prodQoh"));
                        Log.d("Qoh ", itemList.get(position).getProdQoh()+"");
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
                    DialogAndToast.showToast("Something went wrong, Please try again", ShopCartActivity.this);
                }
            }else if(apiName.equals("shopDetails")){
                if(response.getBoolean("status")){
                    JSONObject jsonObject = response.getJSONObject("result");
                    shopName = jsonObject.getString("shopName");
                    shopImage = jsonObject.getString("photo");
                    shopMobile = jsonObject.getString("mobile");
                    address = jsonObject.getString("address");
                    statecity = jsonObject.getString("city")+", "+jsonObject.getString("province");
                    shopPin = jsonObject.getString("zip");
                    shopCountry = jsonObject.getString("country");
                    shopState = jsonObject.getString("province");
                    shopCity = jsonObject.getString("city");
                    shoppursDeliveryAvailable = jsonObject.getString("isDeliveryAvailable");
                    shopppursMinDeliveryAmt = jsonObject.getInt("minDeliveryAmount");
                    shoppursMinDeliveryTime = jsonObject.getInt("minDeliverytime");
                    shoppursMinDeliveryDistance =jsonObject.getInt("minDeliverydistance");
                    shoppursChargesAfterMinDistance = jsonObject.getInt("chargesAfterMinDistance");
                    if(shoppursDeliveryAvailable.equals("N") || shoppursDeliveryAvailable.equals("null")){ // home delivery not available
                        tv_self_status.setText("Self Delivery");
                        rg_delivery.setVisibility(View.GONE);
                    } else {
                        rg_delivery.setVisibility(View.VISIBLE);
                        tv_self_status.setVisibility(View.GONE);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            DialogAndToast.showToast(getResources().getString(R.string.json_parser_error)+e.toString(),ShopCartActivity.this);
        }
    }

    @Override
    public void onDialogPositiveClicked(){
        Intent intent = new Intent(ShopCartActivity.this,InvoiceActivity.class);
        intent.putExtra("orderNumber",orderNumber);
        startActivity(intent);
        finish();
    }

    public void updateCart(int type, int position){
        Log.d("clicked Position ", position+"");
        this.position = position;
        this.type = type;
        if(type==2){
            productDetailsType = 1;
            getProductDetails(""+itemList.get(position).getProdId());
        }else{
            onProductItemClicked(position,type);
        }
    }

    @Override
    public void onItemClicked(int position,int type) {
        if(type == 3){
            showOfferDescription(itemList.get(position));
        }else{
            this.position = position;
            this.myProduct = itemList.get(position);
            if(type == 1){
                updateCart(type,position);
            }else if(type == 2){
                updateCart(type,position);
            }
        }

    }

    public void onProductItemClicked(int position, int type) {
        this.position = position;
        this.myProduct = itemList.get(position);
        Log.i(TAG,"onItemClicked "+position+" type "+type);
        MyProductItem item = (MyProductItem)itemList.get(position);
        if(type == 1){
            if(itemList.size() == 1 && item.getQty() == 1){
                itemList.clear();
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
                for(ProductUnit pu : item.getProductUnitList()){
                    dbHelper.removeCartUnit(pu.getId());
                }
                for(ProductSize ps : item.getProductSizeList()){
                    dbHelper.removeCartSize(ps.getId());
                }
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
                myProduct.setQty(myProduct.getQty());
                qty = myProduct.getQty();
                Log.i(TAG,"netSellingPrice "+netSellingPrice);
                float amount = item.getTotalAmount() - netSellingPrice;
                Log.i(TAG,"tot amount "+amount);
                item.setTotalAmount(amount);
                Object ob = myProduct.getProductOffer();
                if(ob instanceof ProductComboOffer){
                    myProduct.setProdSp(amount/qty);
                }

                if(qty == 0){
                    itemList.remove(position);
                    dbHelper.removeProductFromShopCart(myProduct.getId());
                    dbHelper.removePriceProductFromCart(""+myProduct.getId());
                    ob = myProduct.getProductOffer();
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
                    for(ProductUnit pu : item.getProductUnitList()){
                        dbHelper.removeCartUnit(pu.getId());
                    }
                    for(ProductSize ps : item.getProductSizeList()){
                        dbHelper.removeCartSize(ps.getId());
                    }
                    myItemAdapter.notifyItemRemoved(position);
                }else{
                    dbHelper.updateShopCartData(item);
                    myItemAdapter.notifyItemChanged(position);
                }

                setFooterValues();
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
                Object ob = myProduct.getProductOffer();
                if(ob instanceof ProductComboOffer){
                    myProduct.setProdSp(amount/qty);
                }
                dbHelper.updateShopCartData(item);
                if(itemList.size() == 1){
                    relativeLayoutCartFooter.setVisibility(View.VISIBLE);
                }
                setFooterValues();
                myItemAdapter.notifyItemChanged(position);
            }

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
                            itemList.remove(item.getFreeProductPosition());
                            //  myItemAdapter.notifyItemRemoved(item.getFreeProductPosition());
                            myItemAdapter.notifyDataSetChanged();
                        }else{
                            MyProductItem item1 = itemList.get(item.getFreeProductPosition());
                            item1.setQty(item.getOfferCounter());
                            dbHelper.updateFreeShopCartData(productDiscountOffer.getProdFreeId(),item.getOfferCounter(),0f);
                            myItemAdapter.notifyItemChanged(item.getFreeProductPosition());
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
                            item1.setFreeProductPosition(position+1);
                            dbHelper.addProductToShopCart(item1);
                            itemList.add(position+1,item1);
                            item.setFreeProductPosition(position+1);
                            dbHelper.updateFreePositionShopCartData(item.getFreeProductPosition(),item.getProdId());
                            dbHelper.updateOfferCounterShopCartData(item.getOfferCounter(),item.getProdId());
                            //myItemAdapter.notifyItemInserted(itemList.size()-1);
                            myItemAdapter.notifyDataSetChanged();
                            Log.i(TAG,"Different product added to cart");
                        }else{
                            item1 = itemList.get(item.getFreeProductPosition());
                            item1.setQty(item.getOfferCounter());
                            dbHelper.updateFreeShopCartData(item1.getProdId(),item1.getQty(),0f);
                            dbHelper.updateOfferCounterShopCartData(item.getOfferCounter(),item.getProdId());
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
        int i = -1;
        Log.i(TAG,"mod "+qty);
        for(ProductComboDetails productComboDetails:productComboDetailsList){
            Log.i(TAG,"qty "+productComboDetails.getPcodProdQty());
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
        onResume();
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

    private void checkFreeProductOffer(){
        Object ob = itemList.get(position).getProductOffer();
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

    @Override
    public void onImageClicked(int position, int type, View view) {
        MyProductItem item = (MyProductItem)itemList.get(position);
        showImageDialog(item.getProdImage1(),view);
    }

}
