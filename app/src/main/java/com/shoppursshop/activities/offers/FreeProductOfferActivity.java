package com.shoppursshop.activities.offers;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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
import com.shoppursshop.models.ProductComboOffer;
import com.shoppursshop.models.ProductDiscountOffer;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.DialogAndToast;
import com.shoppursshop.utilities.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FreeProductOfferActivity extends NetworkBaseActivity implements MyItemClickListener {

    private TextView tv_parent, tv_top_parent;
    private EditText edit_offer_name, edit_buying_product, edit_free_product, edit_buy_qty_product,
                     edit_free_qty_product, edit_offer_start_date, edit_offer_end_date;
    private int buyingProductId, freeProductId;
    private RelativeLayout relative_footer_action;
    private ImageView iv_buy_camera, iv_free_camera;

    private String flag;
    private ProductDiscountOffer productDiscountOffer;

    private int searchType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_product_offer);
        initFooterAction(this);
        init();
    }

    private void init(){
        flag = getIntent().getStringExtra("flag");
        tv_top_parent = findViewById(R.id.text_left_label);
        tv_parent = findViewById(R.id.text_right_label);
        edit_offer_name = findViewById(R.id.edit_offer_name);
        edit_buying_product = findViewById(R.id.edit_buying_product);
        edit_free_product = findViewById(R.id.edit_free_product);
        edit_buy_qty_product = findViewById(R.id.edit_buy_qty_product);
        edit_free_qty_product = findViewById(R.id.edit_free_qty_product);
        edit_offer_start_date = findViewById(R.id.edit_offer_start_date);
        edit_offer_end_date = findViewById(R.id.edit_offer_end_date);
        relative_footer_action = findViewById(R.id.relative_footer_action);
        iv_buy_camera = findViewById(R.id.iv_buy_camera);
        iv_free_camera = findViewById(R.id.iv_free_camera);

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

        edit_buying_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchType = 0;
                searchProduct();
            }
        });

        edit_free_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchType = 1;
                searchProduct();
            }
        });

        iv_buy_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openScannar(1);
            }
        });

        iv_free_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openScannar(2);
            }
        });

        relative_footer_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUpdateOffer();
            }
        });

        tv_top_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FreeProductOfferActivity.this, MyOffersActivity.class));
                finish();
            }
        });
        tv_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FreeProductOfferActivity.this, CreateOfferActivity.class));
                finish();
            }
        });

        if(flag != null && flag.equals("edit")){
            productDiscountOffer = (ProductDiscountOffer)getIntent().getSerializableExtra("data");
            if(productDiscountOffer != null){
                edit_offer_name.setText(productDiscountOffer.getOfferName());
                MyProductItem item = dbHelper.getProductDetails(productDiscountOffer.getProdBuyId());
                if(item != null){
                    edit_buying_product.setText(item.getProdName());
                    item = dbHelper.getProductDetails(productDiscountOffer.getProdFreeId());
                    edit_free_product.setText(item.getProdName());
                }
                edit_buy_qty_product.setText(""+productDiscountOffer.getProdBuyQty());
                edit_free_qty_product.setText(""+productDiscountOffer.getProdFreeQty());
                edit_offer_start_date.setText(productDiscountOffer.getStartDate());
                edit_offer_end_date.setText(productDiscountOffer.getEndDate());
                buyingProductId = productDiscountOffer.getProdBuyId();
                freeProductId = productDiscountOffer.getProdFreeId();
            }
            TextView textView = findViewById(R.id.text_action);
            textView.setText("Update");
        }
    }

    private void searchProduct(){
        BottomSearchFragment bottomSearchFragment = new BottomSearchFragment();
        bottomSearchFragment.setCallingActivityName("productList");
        Bundle bundle = new Bundle();
        bundle.putString("flag","searchOfferProduct");
        bottomSearchFragment.setArguments(bundle);
        bottomSearchFragment.setMyItemClickListener(this);
        bottomSearchFragment.show(getSupportFragmentManager(), "Search Product Bottom Sheet");
    }

    private void createUpdateOffer() {
        String offer_name = edit_offer_name.getText().toString();
        String buying_product = edit_buying_product.getText().toString();
        String free_product = edit_free_product.getText().toString();
        String buy_qty_product = edit_buy_qty_product.getText().toString();
        String free_qty_product = edit_free_qty_product.getText().toString();
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
        if (TextUtils.isEmpty(free_product)) {
            DialogAndToast.showDialog("Enter Free Product", this);
            return;
        }
        if (TextUtils.isEmpty(buy_qty_product)) {
            DialogAndToast.showDialog("Enter Buy Quantity", this);
            return;
        }
        if (TextUtils.isEmpty(free_qty_product)) {
            DialogAndToast.showDialog("Enter Free Quantity", this);
            return;
        }
        if (TextUtils.isEmpty(offer_start_date)) {
            DialogAndToast.showDialog("Enter Offer Start Date", this);
            return;
        }
        if (TextUtils.isEmpty(offer_end_date)) {
            DialogAndToast.showDialog("Enter Offer End Date", this);
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("offerName", offer_name);
        params.put("prodBuyId", String.valueOf(buyingProductId));
        params.put("prodFreeId", String.valueOf(freeProductId));
        params.put("prodBuyQty", buy_qty_product);
        params.put("prodFreeQty", free_qty_product);
        params.put("startDate", offer_start_date);
        params.put("endDate", offer_end_date);
        params.put("status", "1");
        params.put("userName",sharedPreferences.getString(Constants.FULL_NAME, ""));
        params.put("dbName", sharedPreferences.getString(Constants.DB_NAME, ""));
        params.put("dbUserName", sharedPreferences.getString(Constants.DB_USER_NAME, ""));
        params.put("dbPassword", sharedPreferences.getString(Constants.DB_PASSWORD, ""));
        JSONArray dataArray = new JSONArray();
        JSONObject dataObject = new JSONObject(params);
        dataArray.put(dataObject);
        String url ="";
        String api = "";
        if(flag != null && flag.equals("edit")){
            params.put("id", ""+productDiscountOffer.getId());
            url = getResources().getString(R.string.url) + Constants.UPDATE_FREE_PRODUCT_OFFER;
            api = "updateFreeProductOffer";

        }else{
            url = getResources().getString(R.string.url) + Constants.CREATE_FREE_PRODUCT_OFFER;
            api = "createFreeProductOffer";
        }
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST, url, new JSONObject(params), api);
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {
        showProgress(false);
        try {
            Log.d("response", response.toString());
            if(apiName.equals("createFreeProductOffer")){
                if(response.getString("status").equals("true")||response.getString("status").equals(true)){
                    JSONObject dataObject = response.getJSONObject("result");
                    productDiscountOffer.setId(dataObject.getInt("id"));
                    productDiscountOffer.setOfferName(dataObject.getString("offerName"));
                    productDiscountOffer.setProdBuyId(dataObject.getInt("prodBuyId"));
                    productDiscountOffer.setProdFreeId(dataObject.getInt("prodFreeId"));
                    productDiscountOffer.setProdBuyQty(dataObject.getInt("prodBuyQty"));
                    productDiscountOffer.setProdFreeQty(dataObject.getInt("prodFreeQty"));
                    productDiscountOffer.setStatus(dataObject.getString("status"));
                    productDiscountOffer.setStartDate(dataObject.getString("startDate"));
                    productDiscountOffer.setEndDate(dataObject.getString("endDate"));
                    dbHelper.addProductFreeOffer(productDiscountOffer,Utility.getTimeStamp(),Utility.getTimeStamp());
                    showMyDialog("Offer created successfully.");
                }else {
                    DialogAndToast.showToast(response.getString("message"),FreeProductOfferActivity.this);
                }
            }else if(apiName.equals("updateFreeProductOffer")){
                if(response.getString("status").equals("true")||response.getString("status").equals(true)){
                    JSONObject dataObject = response.getJSONObject("result");
                    productDiscountOffer.setId(dataObject.getInt("id"));
                    productDiscountOffer.setOfferName(dataObject.getString("offerName"));
                    productDiscountOffer.setProdBuyId(dataObject.getInt("prodBuyId"));
                    productDiscountOffer.setProdFreeId(dataObject.getInt("prodFreeId"));
                    productDiscountOffer.setProdBuyQty(dataObject.getInt("prodBuyQty"));
                    productDiscountOffer.setProdFreeQty(dataObject.getInt("prodFreeQty"));
                    productDiscountOffer.setStatus(dataObject.getString("status"));
                    productDiscountOffer.setStartDate(dataObject.getString("startDate"));
                    productDiscountOffer.setEndDate(dataObject.getString("endDate"));
                    dbHelper.updateProductFreeOffer(productDiscountOffer,Utility.getTimeStamp());
                    showMyDialog("Offer updated successfully.");
                }else {
                    DialogAndToast.showToast(response.getString("message"),FreeProductOfferActivity.this);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            DialogAndToast.showToast(getResources().getString(R.string.json_parser_error)+e.toString(),FreeProductOfferActivity.this);
        }
    }

    private void openScannar(int requestCode){
        Intent intent = new Intent(this,ScannarActivity.class);
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
                    edit_buying_product.setText(productItem.getProdName());
                    buyingProductId = productItem.getProdId();
                }

            } else if (requestCode == 2) {
                MyProductItem productItem = dbHelper.getProductDetails(data.getIntExtra("prod_id", 0));
                edit_free_product.setText(productItem.getProdName());
                freeProductId = productItem.getProdId();
            }
        }
    }

    public void onDialogPositiveClicked(){
        Intent intent = new Intent(FreeProductOfferActivity.this,MyOffersActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onItemClicked(int prodId) {
        MyProductItem item = dbHelper.getProductDetails(prodId);
        if(searchType == 0){
            List<ProductComboOffer> comboOfferList = dbHelper.getProductPriceOffer(""+prodId);
            List<ProductDiscountOffer> productDiscountOfferList = dbHelper.getProductFreeOffer(""+prodId);
            if(comboOfferList.size() > 0 || productDiscountOfferList.size() > 0){
                if(comboOfferList.size() > 0){
                    DialogAndToast.showDialog(comboOfferList.get(0).getOfferName()+" is already applied to selected product",this);
                }else{
                    DialogAndToast.showDialog(productDiscountOfferList.get(0).getOfferName()+" is already applied to selected product",this);
                }
            }else{
                buyingProductId = item.getProdId();
                edit_buying_product.setText(item.getProdName());
            }

        }else{
            freeProductId = item.getProdId();
            edit_free_product.setText(item.getProdName());
        }
    }
}
