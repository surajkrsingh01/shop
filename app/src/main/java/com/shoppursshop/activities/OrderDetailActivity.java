package com.shoppursshop.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.pnsol.sdk.auth.AccountValidator;
import com.shoppursshop.R;
import com.shoppursshop.activities.payment.mPos.MPayActivity;
import com.shoppursshop.activities.payment.mPos.MPayTransactionDetailsActivity;
import com.shoppursshop.adapters.ProductAdapter;
import com.shoppursshop.fragments.AcceptOrderDialogFragment;
import com.shoppursshop.fragments.CancelDialogFragment;
import com.shoppursshop.interfaces.MyItemClickListener;
import com.shoppursshop.models.MyProductItem;
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

import static com.pnsol.sdk.interfaces.PaymentTransactionConstants.FAIL;
import static com.pnsol.sdk.interfaces.PaymentTransactionConstants.SUCCESS;

public class OrderDetailActivity extends NetworkBaseActivity implements MyItemClickListener {

    private String PARTNER_API_KEY = "2C869B63DD4E";
    private String MERCHANT_API_KEY = "2C869B63DD4E";

    private TextView textViewId,textViewCustomerName,textViewOrderDate,
            textViewSelfDelivery,textViewDeliveryAddress,textViewTotalAmount,textViewOrderStatus;
    private RelativeLayout relativeLayoutDeliveryContainer;
    private Button buttonAccept,buttonCancel,buttonOrderDelivered,btnViewInvoice;
    private RelativeLayout relativeLayoutPayOptionLayout;
    private TextView tvCash,tvCard;
    private View view1,view2;
    private TextView textView1,textView2,textView4;
    private ImageView imageView1,imageView2,imageView4;

    private RecyclerView recyclerView;
    private List<Object> itemList;
    private ProductAdapter myItemAdapter;
    Intent intent;

    CancelDialogFragment cancelBottomSheetDialog;
    AcceptOrderDialogFragment acceptOrderDialogFragment;

    private String orderStatus,ordPayStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();
        initFooter(this,0);
    }

    private void init(){
        intent = getIntent();
        textViewId = findViewById(R.id.text_order_id);
        textViewCustomerName = findViewById(R.id.text_customer_name);
        textViewOrderDate = findViewById(R.id.text_order_date);
        textViewSelfDelivery = findViewById(R.id.text_self_delivery);
        textViewDeliveryAddress = findViewById(R.id.text_delivery_address);
        textViewTotalAmount = findViewById(R.id.text_total_amount);
        textViewOrderStatus = findViewById(R.id.text_order_status);
        tvCard = findViewById(R.id.tv_pay_card);
        tvCash = findViewById(R.id.tv_pay_cash);
        buttonAccept = findViewById(R.id.btn_accept);
        buttonCancel = findViewById(R.id.btn_cancel);
        buttonOrderDelivered = findViewById(R.id.btn_order_delivered);
        btnViewInvoice = findViewById(R.id.btn_view_invoice);
        relativeLayoutDeliveryContainer = findViewById(R.id.relative_delivery_container);
        relativeLayoutPayOptionLayout = findViewById(R.id.relative_pay_layout);

        view1 = findViewById(R.id.view_status_1);
        view2 = findViewById(R.id.view_status_2);
        textView1 = findViewById(R.id.text_1);
        textView2 = findViewById(R.id.text_2);
        textView4 = findViewById(R.id.text_4);
        imageView1 = findViewById(R.id.image_1);
        imageView2 = findViewById(R.id.image_2);
        imageView4 = findViewById(R.id.image_4);

        orderStatus = getIntent().getStringExtra("status");
        ordPayStatus = getIntent().getStringExtra("ordPaymentStatus");
        setTrackStatus(orderStatus);

        if(orderStatus.equals("Accepted") || orderStatus.equals("Cancelled") || orderStatus.equals("Delivered")){
            buttonAccept.setVisibility(View.GONE);
            buttonCancel.setVisibility(View.GONE);
            textViewOrderStatus.setVisibility(View.VISIBLE);
            textViewOrderStatus.setText("Order "+orderStatus);

            if(orderStatus.equals("Accepted") || orderStatus.equals("Delivered")){
                textViewOrderStatus.setTextColor(getResources().getColor(R.color.green500));
            }else{
                textViewOrderStatus.setTextColor(getResources().getColor(R.color.red_500));
            }

            if(orderStatus.equals("Accepted")){
                if(ordPayStatus.equals("Pay at counter")){
                    relativeLayoutPayOptionLayout.setVisibility(View.VISIBLE);
                }else{
                    buttonOrderDelivered.setVisibility(View.VISIBLE);
                }

            }

            if(orderStatus.equals("Delivered")){
                buttonOrderDelivered.setVisibility(View.GONE);
                btnViewInvoice.setVisibility(View.VISIBLE);
            }
        }

        textViewId.setText("Order No - "+intent.getStringExtra("id"));
        textViewCustomerName.setText(intent.getStringExtra("custName"));
        textViewOrderDate.setText(Utility.parseDate(intent.getStringExtra("date"),
                "yyyy-MM-dd HH:mm:ss","HH:mm, MMM dd, yyyy"));
        textViewTotalAmount.setText("Rs "+String.format("%.02f",intent.getFloatExtra("totalAmount",0f)));
        String deliveryMode = intent.getStringExtra("deliveryMode");
        if(deliveryMode.equals("Self")){

        }else{
            relativeLayoutDeliveryContainer.setVisibility(View.VISIBLE);
            textViewSelfDelivery.setVisibility(View.GONE);
            textViewDeliveryAddress.setText(intent.getStringExtra("deliveryAddress"));
        }

        itemList = new ArrayList<>();
        recyclerView=findViewById(R.id.recycler_view_product_list);
        recyclerView.setHasFixedSize(true);
        final RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        myItemAdapter=new ProductAdapter(this,itemList,"orderProductList");
        recyclerView.setAdapter(myItemAdapter);

        buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptOrderDialogFragment = AcceptOrderDialogFragment.getInstance();
                acceptOrderDialogFragment.setTotalAmount("Rs "+String.format("%.02f",intent.getFloatExtra("totalAmount",0f)));
                acceptOrderDialogFragment.setOrdPayStatus(ordPayStatus);
                acceptOrderDialogFragment.setMyItemClickListener(OrderDetailActivity.this);
                acceptOrderDialogFragment.show(getSupportFragmentManager(), "Accept Bottom Sheet");
                //acceptOrder();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelBottomSheetDialog = CancelDialogFragment.getInstance();
                cancelBottomSheetDialog.setTotalAmount("Rs "+String.format("%.02f",intent.getFloatExtra("totalAmount",0f)));
                cancelBottomSheetDialog.setMyItemClickListener(OrderDetailActivity.this);
                cancelBottomSheetDialog.show(getSupportFragmentManager(), "Cancel Bottom Sheet");
             // cancelOrder();
            }
        });

        buttonOrderDelivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // setPaymentPos();
                deliverOrder();
            }
        });

        tvCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderDetailActivity.this, MPayActivity.class);
                intent.putExtra("totalAmount",String.format("%.02f",intent.getFloatExtra("totalAmount",0f)));
                intent.putExtra("ordId",getIntent().getStringExtra("ordId"));
                startActivity(intent);
            }
        });

        btnViewInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderDetailActivity.this,InvoiceActivity.class);
                intent.putExtra("orderId",getIntent().getStringExtra("id"));
                startActivity(intent);
            }
        });

        getProducts();
    }

    private void setTrackStatus(String mode){
        if(mode.toLowerCase().equals("accepted")){
           view1.setBackgroundColor(getResources().getColor(R.color.accent_color_4));
           textView2.setTextColor(getResources().getColor(R.color.accent_color_4));
           imageView2.setBackgroundResource(R.drawable.accent_color_4_circle_background);
        }else if(mode.toLowerCase().equals("delivered")){
            view1.setBackgroundColor(getResources().getColor(R.color.accent_color_4));
            textView2.setTextColor(getResources().getColor(R.color.accent_color_4));
            imageView2.setBackgroundResource(R.drawable.accent_color_4_circle_background);

            view2.setBackgroundColor(getResources().getColor(R.color.accent_color_4));
            textView4.setTextColor(getResources().getColor(R.color.accent_color_4));
            imageView4.setBackgroundResource(R.drawable.accent_color_4_circle_background);
        }else if(mode.toLowerCase().equals("cancelled")){
            view1.setBackgroundColor(getResources().getColor(R.color.accent_color_4));
            textView2.setTextColor(getResources().getColor(R.color.accent_color_4));
            imageView2.setBackgroundResource(R.drawable.accent_color_4_circle_background);
            textView2.setText("Cancelled");
        }
    }

    private void getProducts(){
        Map<String,String> params=new HashMap<>();
        params.put("id", getIntent().getStringExtra("id"));
        params.put("shopId",sharedPreferences.getString(Constants.USER_ID,""));
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+Constants.ORDER_PRODUCTS;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"orderProducts");
    }

    private void acceptOrder(){
        Map<String,String> params=new HashMap<>();
        params.put("id", getIntent().getStringExtra("id"));
        params.put("deliveryBy","");
        params.put("custCode",getIntent().getStringExtra("custCode"));;
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+Constants.ACCEPT_ORDER;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"acceptOrder");
    }

    private void cancelOrder(){
        Map<String,String> params=new HashMap<>();
        params.put("id", getIntent().getStringExtra("id"));
        params.put("custCode",getIntent().getStringExtra("custCode"));
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+Constants.CANCEL_ORDER;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"cancelOrder");
    }

    private void deliverOrder(){
        Map<String,String> params=new HashMap<>();
        params.put("id", getIntent().getStringExtra("id"));
        params.put("custCode",getIntent().getStringExtra("custCode"));
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+Constants.ORDER_DELIVERED;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"orderDelivered");
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {

        try {
            if (apiName.equals("orderProducts")) {
                if (response.getBoolean("status")) {
                    JSONArray dataArray = response.getJSONArray("result");
                    JSONObject jsonObject = null;
                    int len = dataArray.length();
                    MyProductItem productItem = null;
                    itemList.clear();
                    for (int i = 0; i < len; i++) {
                        jsonObject = dataArray.getJSONObject(i);
                        productItem = new MyProductItem();
                        productItem.setQty(jsonObject.getInt("qty"));
                        productItem.setProdId(jsonObject.getInt("prodId"));
                        productItem.setProdCatId(jsonObject.getInt("prodCatId"));
                        productItem.setProdName(jsonObject.getString("prodName"));
                        productItem.setProdCode(jsonObject.getString("prodCode"));
                        productItem.setProdBarCode(jsonObject.getString("prodBarCode"));
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
                        productItem.setProdMrp(Float.parseFloat(jsonObject.getString("prodMrp")));
                        productItem.setProdSp(Float.parseFloat(jsonObject.getString("prodSp")));
                        productItem.setCreatedBy(jsonObject.getString("createdBy"));
                        productItem.setUpdatedBy(jsonObject.getString("updatedBy"));
                        productItem.setCreatedDate(jsonObject.getString("createdDate"));
                        productItem.setUpdatedDate(jsonObject.getString("updatedDate"));
                       // productItem.setStatus(jsonObject.getString("status"));
                        itemList.add(productItem);
                    }

                    myItemAdapter.notifyDataSetChanged();
                }else{
                    DialogAndToast.showDialog(response.getString("message"),this);
                }
            }else if (apiName.equals("acceptOrder")) {
                if (response.getBoolean("status")) {
                    buttonAccept.setVisibility(View.GONE);
                    buttonCancel.setVisibility(View.GONE);
                    if(ordPayStatus.equals("Pay at counter")){
                        relativeLayoutPayOptionLayout.setVisibility(View.VISIBLE);
                    }else{
                        buttonOrderDelivered.setVisibility(View.VISIBLE);
                    }
                    textViewOrderStatus.setVisibility(View.VISIBLE);
                    textViewOrderStatus.setText("Order Accepted");
                    textViewOrderStatus.setTextColor(getResources().getColor(R.color.green500));
                    editor.putString("orderStatus","Accepted");
                    editor.putInt("orderPosition",getIntent().getIntExtra("orderPosition",0));
                    editor.putString("type",getIntent().getStringExtra("type"));
                    editor.commit();
                    setTrackStatus("accepted");
                    MyProductItem item = null;
                    for(Object ob : itemList){
                        item = (MyProductItem) ob;
                        dbHelper.setQoh(item.getProdId(),-item.getQty());
                    }
                }else{
                    DialogAndToast.showDialog(response.getString("message"),this);
                }
            }else if (apiName.equals("cancelOrder")) {
                if (response.getBoolean("status")) {
                    buttonAccept.setVisibility(View.GONE);
                    buttonCancel.setVisibility(View.GONE);
                    textViewOrderStatus.setVisibility(View.VISIBLE);
                    textViewOrderStatus.setText("Order Cancelled");
                    textViewOrderStatus.setTextColor(getResources().getColor(R.color.red_500));
                    editor.putString("orderStatus","Cancelled");
                    editor.putInt("orderPosition",getIntent().getIntExtra("orderPosition",0));
                    editor.putString("type",getIntent().getStringExtra("type"));
                    editor.commit();
                    setTrackStatus("cancelled");
                }else{
                    DialogAndToast.showDialog(response.getString("message"),this);
                }
            }else if (apiName.equals("orderDelivered")) {
                if (response.getBoolean("status")) {
                    textViewOrderStatus.setText("Order Delivered");
                    buttonOrderDelivered.setVisibility(View.GONE);
                    btnViewInvoice.setVisibility(View.VISIBLE);
                    editor.putString("orderStatus","Delivered");
                    editor.putInt("orderPosition",getIntent().getIntExtra("orderPosition",0));
                    editor.putString("type",getIntent().getStringExtra("type"));
                    editor.commit();
                    setTrackStatus("delivered");
                }else{
                    DialogAndToast.showDialog(response.getString("message"),this);
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClicked(int position) {
        if(position == 0){
            acceptOrderDialogFragment.dismiss();
        }else if(position == 1){
            acceptOrderDialogFragment.dismiss();
            acceptOrder();
        }else if(position == 2){
            cancelBottomSheetDialog.dismiss();
        }else if(position == 3){
            cancelBottomSheetDialog.dismiss();
            cancelOrder();
        }

    }

    private void setPaymentPos(){
        try {
            AccountValidator validator = new AccountValidator(getApplicationContext());
            if (!validator.isAccountActivated()) {
                validator.accountActivation(handler, MERCHANT_API_KEY, PARTNER_API_KEY);
            }else{
                DialogAndToast.showToast("Activated",OrderDetailActivity.this);
            }
        }catch (RuntimeException e) {
            e.printStackTrace();
            Log.i(TAG,"Exception "+e.getMessage());
        }

    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            Log.i(TAG,"Handler "+(String) msg.obj);
            if (msg.what == SUCCESS) {
                Log.i(TAG,"Success");
                DialogAndToast.showToast("Success",OrderDetailActivity.this);
            }
            if (msg.what == FAIL) {
                Log.i(TAG,"Failed "+(String) msg.obj);
                DialogAndToast.showToast((String) msg.obj,OrderDetailActivity.this);
            }
        };
    };

}
