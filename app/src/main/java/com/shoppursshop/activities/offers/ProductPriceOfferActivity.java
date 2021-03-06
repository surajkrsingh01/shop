package com.shoppursshop.activities.offers;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.shoppursshop.R;
import com.shoppursshop.activities.base.NetworkBaseActivity;
import com.shoppursshop.activities.ScannarActivity;
import com.shoppursshop.fragments.BottomSearchFragment;
import com.shoppursshop.interfaces.MyItemClickListener;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductPriceOfferActivity extends NetworkBaseActivity implements MyItemClickListener {

    private RecyclerView recyclerView;
    private ProductPriceOfferAdapter priceOfferAdapter;
    private List<MyProductItem> myProductItems;
    private Button add_combo;
    private ImageView image_scan;
    private TextView tv_parent, tv_top_parent;
    private int product_id;
    private float prodSp;
    private RelativeLayout relative_footer_action;
    private EditText edit_offer_name, edit_product_name, edit_offer_start_date, edit_offer_end_date;

    private String flag;
    private ProductComboOffer productComboOffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_price_offer);
        initFooterAction(this);
        init();
    }

    private void init(){
        flag = getIntent().getStringExtra("flag");
        tv_top_parent = findViewById(R.id.text_left_label);
        tv_parent = findViewById(R.id.text_right_label);
        image_scan = findViewById(R.id.image_scan);
        edit_offer_name  = findViewById(R.id.edit_offer_name);
        edit_product_name = findViewById(R.id.edit_name);
        edit_offer_start_date = findViewById(R.id.edit_offer_start_date);
        edit_offer_end_date = findViewById(R.id.edit_offer_end_date);
        relative_footer_action = findViewById(R.id.relative_footer_action);

        edit_product_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSearchFragment bottomSearchFragment = new BottomSearchFragment();
                bottomSearchFragment.setCallingActivityName("productList");
                Bundle bundle = new Bundle();
                bundle.putString("flag","searchCartProduct");
                bottomSearchFragment.setArguments(bundle);
                bottomSearchFragment.setMyItemClickListener(ProductPriceOfferActivity.this);
                bottomSearchFragment.show(getSupportFragmentManager(), "Search Product Bottom Sheet");
            }
        });

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog datePicker1=new DatePickerDialog(this,new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker datePicker, int yr, int mon, int dy) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR,yr);
                cal.set(Calendar.MONTH,mon);
                cal.set(Calendar.DATE,dy);
                edit_offer_start_date.setText(Utility.parseDate(cal,"yyyy-MM-dd"));
            }
        },year,month,day);
        datePicker1.setCancelable(false);
        datePicker1.setMessage("Select Offer Start Date");

        final DatePickerDialog datePicker2=new DatePickerDialog(this,new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker datePicker, int yr, int mon, int dy) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR,yr);
                cal.set(Calendar.MONTH,mon);
                cal.set(Calendar.DATE,dy);
                edit_offer_end_date.setText(Utility.parseDate(cal,"yyyy-MM-dd"));
            }
        },year,month,day);
        datePicker2.setCancelable(false);
        datePicker2.setMessage("Select Offer End Date");

        edit_offer_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker1.show();
            }
        });

        edit_offer_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker2.show();
            }
        });

        image_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openScannar(1);
            }
        });

        tv_top_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductPriceOfferActivity.this, MyOffersActivity.class));
                finish();
            }
        });
        tv_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductPriceOfferActivity.this, CreateOfferActivity.class));
                finish();
            }
        });


        myProductItems = new ArrayList<>();
        MyProductItem productItem = new MyProductItem();
        productItem.setQty(1);
        productItem.setProdName("");
        myProductItems.add(productItem);
        productItem = new MyProductItem();
        productItem.setQty(2);
        productItem.setProdName("");
        myProductItems.add(productItem);

        add_combo = findViewById(R.id.add_combo);
        ((GradientDrawable)add_combo.getBackground()).setColor(colorTheme);
        add_combo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyProductItem productItem = new MyProductItem();
                productItem.setQty(myProductItems.size()+1);
                productItem.setProdSp(prodSp);
                productItem.setProdSp(prodSp);
                productItem.setOfferPrice(0f);
                myProductItems.add(productItem);
                priceOfferAdapter.notifyDataSetChanged();
              //  priceOfferAdapter.notifyItemInserted(myProductItems.size());
            }
        });

        relative_footer_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createOffer();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        priceOfferAdapter=new ProductPriceOfferAdapter(this,myProductItems);
        recyclerView.setAdapter(priceOfferAdapter);
        recyclerView.setNestedScrollingEnabled(false);

        if (flag != null && flag.equals("edit")) {
            productComboOffer = (ProductComboOffer)getIntent().getSerializableExtra("data");
            edit_offer_name.setText(productComboOffer.getOfferName());
            edit_offer_start_date.setText(productComboOffer.getStartDate());
            edit_offer_end_date.setText(productComboOffer.getEndDate());
            MyProductItem item = dbHelper.getProductDetails(productComboOffer.getProdId());
            priceOfferAdapter.setSp(item.getProdSp());
            product_id = item.getProdId();
            edit_product_name.setText(item.getProdName());
            myProductItems.clear();
            for(ProductComboDetails details : productComboOffer.getProductComboOfferDetails()){
                productItem = new MyProductItem();
                productItem.setId(details.getId());
                productItem.setPcoId(details.getPcodPcoId());
                productItem.setQty(details.getPcodProdQty());
                productItem.setProdSp(item.getProdSp());
                productItem.setOfferPrice(details.getPcodPrice());
                productItem.setStatus("1");
                myProductItems.add(productItem);
                Log.i(TAG," pcoId "+details.getPcodPcoId()+" qty "+details.getPcodProdQty()+" price "+details.getPcodPrice());
            }

            priceOfferAdapter.notifyDataSetChanged();

            TextView textView = findViewById(R.id.text_action);
            textView.setText("Update");
            add_combo.setVisibility(View.GONE);
        }
    }

    private void createOffer() {
        Log.i(TAG,"in create prod id "+product_id);
        String offer_name = edit_offer_name.getText().toString();
        String buying_product = edit_product_name.getText().toString();
        //String buy_qty_product = edit_buy_qty_product.getText().toString();
        String offer_start_date = edit_offer_start_date.getText().toString();
        String offer_end_date = edit_offer_end_date.getText().toString();

        if (TextUtils.isEmpty(offer_name)) {
            DialogAndToast.showDialog("Enter Offer Name", this);
            return;
        }
        if (TextUtils.isEmpty(buying_product)) {
            DialogAndToast.showDialog("Enter Buying Product", this);
            return;
        }
        /*if (TextUtils.isEmpty(buy_qty_product)) {
            DialogAndToast.showDialog("Enter Buy Quantity", this);
            return;
        }
        if (TextUtils.isEmpty(free_qty_product)) {
            DialogAndToast.showDialog("Enter Free Quantity", this);
            return;
        }*/
        if (TextUtils.isEmpty(offer_start_date)) {
            DialogAndToast.showDialog("Enter Offer Start Date", this);
            return;
        }
        if (TextUtils.isEmpty(offer_end_date)) {
            DialogAndToast.showDialog("Enter Offer End Date", this);
            return;
        }

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject productObject = null;
        try {
            jsonObject.put("offerName",offer_name);
            jsonObject.put("status","1");
            jsonObject.put("startDate",offer_start_date);
            jsonObject.put("endDate",offer_end_date);
            jsonObject.put("pcodProdId",product_id);
            jsonObject.put("prodId",product_id);
            for(MyProductItem item : myProductItems){
                productObject = new JSONObject();
                if(flag != null && flag.equals("edit")){
                    productObject.put("id",item.getId());
                    productObject.put("pcodPcoId",item.getPcoId());
                }
                productObject.put("pcodProdQty",item.getQty());
                productObject.put("pcodPrice",item.getOfferPrice());
                productObject.put("status","1");
                jsonArray.put(productObject);
            }
            if(flag != null && flag.equals("edit")){
                jsonObject.put("id",productComboOffer.getId());
            }
            jsonObject.put("productComboOfferDetails",jsonArray);
            jsonObject.put("userName",sharedPreferences.getString(Constants.FULL_NAME,""));
            jsonObject.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
            jsonObject.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
            jsonObject.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Map<String, String> params = new HashMap<>();
        params.put("offerName", offer_name);
        params.put("prodId", String.valueOf(product_id));
        params.put("status", "1");
        params.put("startDate", offer_start_date);
        params.put("endDate", offer_end_date);
        params.put("userName","Suraj");
        params.put("dbName", sharedPreferences.getString(Constants.DB_NAME, ""));
        params.put("dbUserName", sharedPreferences.getString(Constants.DB_USER_NAME, ""));
        params.put("dbPassword", sharedPreferences.getString(Constants.DB_PASSWORD, ""));
        JSONArray dataArray = new JSONArray();
        JSONObject dataObject = new JSONObject(params);
        dataArray.put(dataObject);
        String url ="";
        String api = "";
        if(flag != null && flag.equals("edit")){
            // params.put("id", ""+productDiscountOffer.getId());
            url = getResources().getString(R.string.url) + Constants.UPDATE_PRODUCT_PRICE_OFFER;
            api = "updateProductPriceOffer";

        }else{
            url = getResources().getString(R.string.url) + Constants.CREATE_PRODUCT_PRICE_OFFER;
            api = "createProductPriceOffer";
        }
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST, url, jsonObject, api);
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {
        showProgress(false);
        try {
            Log.d("response", response.toString());
            if(apiName.equals("createProductPriceOffer")){
                if(response.getString("status").equals("true")||response.getString("status").equals(true)){
                    JSONObject dataObject = response.getJSONObject("result");
                    JSONArray productComboArray = dataObject.getJSONArray("productComboOfferDetails");
                    ProductComboDetails productComboDetails = null;
                    List<ProductComboDetails> productComboOfferDetails = new ArrayList<>();
                    productComboOffer = new ProductComboOffer();
                    productComboOffer.setId(dataObject.getInt("id"));
                    productComboOffer.setOfferName(dataObject.getString("offerName"));
                    productComboOffer.setProdId(dataObject.getInt("prodId"));
                    productComboOffer.setStatus(dataObject.getString("status"));
                    productComboOffer.setStartDate(dataObject.getString("startDate"));
                    productComboOffer.setEndDate(dataObject.getString("endDate"));
                    dbHelper.addProductComboOffer(productComboOffer,Utility.getTimeStamp(),Utility.getTimeStamp());
                    productComboOfferDetails = new ArrayList<>();
                    int innerLen = productComboArray.length();
                    for (int k = 0; k < innerLen; k++) {
                        dataObject = productComboArray.getJSONObject(k);
                        productComboDetails = new ProductComboDetails();
                        productComboDetails.setId(dataObject.getInt("id"));
                        productComboDetails.setPcodPcoId(dataObject.getInt("pcodPcoId"));
                        productComboDetails.setPcodProdQty(dataObject.getInt("pcodProdQty"));
                        productComboDetails.setPcodPrice((float)dataObject.getDouble("pcodPrice"));
                        productComboDetails.setStatus(dataObject.getString("status"));
                        productComboOfferDetails.add(productComboDetails);
                        dbHelper.addProductComboDetailOffer(productComboDetails,
                                Utility.getTimeStamp(),Utility.getTimeStamp());
                    }
                    showMyDialog("Offer created successfully.");
                }else {
                    DialogAndToast.showToast(response.getString("message"),ProductPriceOfferActivity.this);
                }
            }else if(apiName.equals("updateProductPriceOffer")){
                if(response.getString("status").equals("true")||response.getString("status").equals(true)){
                    JSONObject dataObject = response.getJSONObject("result");
                    JSONArray productComboArray = dataObject.getJSONArray("productComboOfferDetails");
                    ProductComboDetails productComboDetails = null;
                    List<ProductComboDetails> productComboOfferDetails = new ArrayList<>();
                    productComboOffer = new ProductComboOffer();
                    productComboOffer.setId(dataObject.getInt("id"));
                    productComboOffer.setOfferName(dataObject.getString("offerName"));
                    productComboOffer.setProdId(dataObject.getInt("prodId"));
                    productComboOffer.setStatus(dataObject.getString("status"));
                    productComboOffer.setStartDate(dataObject.getString("startDate"));
                    productComboOffer.setEndDate(dataObject.getString("endDate"));
                    dbHelper.updateProductComboOffer(productComboOffer,Utility.getTimeStamp());
                    productComboOfferDetails = new ArrayList<>();
                    int innerLen = productComboArray.length();
                    for (int k = 0; k < innerLen; k++) {
                        dataObject = productComboArray.getJSONObject(k);
                        productComboDetails = new ProductComboDetails();
                        productComboDetails.setId(dataObject.getInt("id"));
                        productComboDetails.setPcodPcoId(dataObject.getInt("pcodPcoId"));
                        productComboDetails.setPcodProdQty(dataObject.getInt("pcodProdQty"));
                        productComboDetails.setPcodPrice((float)dataObject.getDouble("pcodPrice"));
                        productComboDetails.setStatus(dataObject.getString("status"));
                        productComboOfferDetails.add(productComboDetails);
                        dbHelper.updateProductComboDetailOffer(productComboDetails, Utility.getTimeStamp());
                    }
                    showMyDialog("Offer updated successfully.");
                }else {
                    DialogAndToast.showToast(response.getString("message"),ProductPriceOfferActivity.this);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            DialogAndToast.showToast(getResources().getString(R.string.json_parser_error)+e.toString(),ProductPriceOfferActivity.this);
        }
    }

    private void openScannar(int requestCode){
        Intent intent = new Intent(this, ScannarActivity.class);
        intent.putExtra("flag","offers");
        intent.putExtra("type","offers");
        // startActivity(intent);
        startActivityForResult(intent,requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "Result received");
        if (data != null) {
            if (requestCode == 1) {
                MyProductItem productItem = dbHelper.getProductDetails(data.getIntExtra("prod_id", 0));
                List<ProductComboOffer> comboOfferList = dbHelper.getProductPriceOffer(""+productItem.getProdId());
                List<ProductDiscountOffer> productDiscountOfferList = dbHelper.getProductFreeOffer(""+productItem.getProdId());
                if(comboOfferList.size() > 0 || productDiscountOfferList.size() > 0){
                    if(comboOfferList.size() > 0){
                        DialogAndToast.showDialog(comboOfferList.get(0).getOfferName()+" is already applied to selected product",this);
                    }else{
                        DialogAndToast.showDialog(productDiscountOfferList.get(0).getOfferName()+" is already applied to selected product",this);
                    }
                }else{
                    prodSp = productItem.getProdSp();
                    priceOfferAdapter.setSp(prodSp);
                    priceOfferAdapter.notifyDataSetChanged();
                    edit_product_name.setText(productItem.getProdName());
                    product_id = productItem.getProdId();
                    Log.i(TAG,"prod id "+product_id);
                }

            }
        }
    }

    @Override
    public void onItemClicked(int prodId) {
        MyProductItem productItem = dbHelper.getProductDetails(prodId);
        List<ProductComboOffer> comboOfferList = dbHelper.getProductPriceOffer(""+prodId);
        List<ProductDiscountOffer> productDiscountOfferList = dbHelper.getProductFreeOffer(""+prodId);
        if(comboOfferList.size() > 0 || productDiscountOfferList.size() > 0){
            if(comboOfferList.size() > 0){
                DialogAndToast.showDialog(comboOfferList.get(0).getOfferName()+" is already applied to selected product",this);
            }else{
                DialogAndToast.showDialog(productDiscountOfferList.get(0).getOfferName()+" is already applied to selected product",this);
            }
        }else{
            prodSp = productItem.getProdSp();
            priceOfferAdapter.setSp(prodSp);
            edit_product_name.setText(productItem.getProdName());
            product_id = productItem.getProdId();
            priceOfferAdapter.notifyDataSetChanged();
            Log.i(TAG,"prod id "+product_id);
        }

    }

    public void onDialogPositiveClicked(){
        Intent intent = new Intent(ProductPriceOfferActivity.this,MyOffersActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
