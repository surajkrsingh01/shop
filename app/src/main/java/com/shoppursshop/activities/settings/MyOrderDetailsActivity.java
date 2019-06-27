package com.shoppursshop.activities.settings;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.shoppursshop.R;
import com.shoppursshop.activities.NetworkBaseActivity;
import com.shoppursshop.activities.settings.profile.DeliveryActivity;
import com.shoppursshop.adapters.ProductAdapter;
import com.shoppursshop.models.MyProductItem;
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

public class MyOrderDetailsActivity extends NetworkBaseActivity {

    private TextView textViewId,textViewTotAmt,textViewOrderDate,tvShopName,tvAddressMobile;
    private String orderStatus,ordPayStatus;

    private View view1,view2;
    private TextView textView1,textView2,textView4;
    private ImageView imageView1,imageView2,imageView4;

    private RecyclerView recyclerView;
    private List<Object> itemList;
    private ProductAdapter myItemAdapter;

    private Intent intent;
    private TextView tv_top_parent, tv_parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();

    }

    private void init(){
        intent = getIntent();
        textViewId = findViewById(R.id.tv_order_id);
        textViewOrderDate = findViewById(R.id.tv_date);
        textViewTotAmt = findViewById(R.id.tv_amount);
        tvShopName = findViewById(R.id.tv_shop_name);
        tvAddressMobile = findViewById(R.id.tv_shop_address_mobile);
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

        itemList = new ArrayList<>();
        recyclerView=findViewById(R.id.recycler_order);
        recyclerView.setHasFixedSize(true);
        final RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        myItemAdapter=new ProductAdapter(this,itemList,"orderProductList");
        recyclerView.setAdapter(myItemAdapter);

        setTrackStatus(orderStatus);

        textViewId.setText("Order No - "+intent.getStringExtra("id"));
        String[] orderDate = Utility.parseDate(intent.getStringExtra("date"),
                "yyyy-MM-dd HH:mm:ss","HH:mm, MMM dd, yyyy").split(",");
        textViewOrderDate.setText(orderDate[0]+" hrs,"+orderDate[1]+orderDate[2]);
        textViewTotAmt.setText("Total Amount: Rs "+Utility.numberFormat(intent.getFloatExtra("totalAmount",0f)));

        if(ConnectionDetector.isNetworkAvailable(this))
        getProducts();

        tv_top_parent = findViewById(R.id.text_left_label);
        tv_parent = findViewById(R.id.text_right_label);
        tv_top_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyOrderDetailsActivity.this, SettingActivity.class));
                finish();
            }
        });
        tv_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyOrderDetailsActivity.this, MyOrdersActivity.class));
                finish();
            }
        });
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

                        if(i == 0){
                            tvShopName.setText(jsonObject.getString("shopName"));
                            tvAddressMobile.setText(jsonObject.getString("shopAddress")+", Ph: "+jsonObject.getString("shopMobile"));
                        }

                        // productItem.setStatus(jsonObject.getString("status"));
                        itemList.add(productItem);
                    }

                    myItemAdapter.notifyDataSetChanged();
                }else{
                    DialogAndToast.showDialog(response.getString("message"),this);
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
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

}
