package com.shoppursshop.activities.settings;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.shoppursshop.R;
import com.shoppursshop.activities.AddProductActivity;
import com.shoppursshop.activities.NetworkBaseActivity;
import com.shoppursshop.activities.ScannarActivity;
import com.shoppursshop.adapters.PaymentSchemeAdapter;
import com.shoppursshop.fragments.BottomSearchFragment;
import com.shoppursshop.interfaces.MyItemClickListener;
import com.shoppursshop.interfaces.MyItemTypeClickListener;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComboProductOfferActivity extends NetworkBaseActivity implements MyItemClickListener, MyItemTypeClickListener {

    private RecyclerView recyclerView;
    private ComboOfferAdapter comboOfferAdapter;
    private List<MyProductItem> myProductItems;
    private Button add_combo;
    private boolean isProductSearch;
    private int position;

    private DatePickerDialog datePicker1,datePicker2;
    private Calendar calendar;

    private EditText edit_offer_name,edit_offer_start_date,edit_offer_end_date;

    private String flag;
    private ProductComboOffer productComboOffer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combo_product_offers);
        initFooterAction(this);
        init();
    }

    private void init(){
        flag = getIntent().getStringExtra("flag");
        myProductItems = new ArrayList<>();
        MyProductItem productItem = new MyProductItem();
        productItem.setProdName("");
        productItem.setProdSp(0);
        myProductItems.add(productItem);
        productItem = new MyProductItem();
        productItem.setProdName("");
        productItem.setProdSp(0);
        myProductItems.add(productItem);

        edit_offer_name = findViewById(R.id.edit_offer_name);
        edit_offer_start_date = findViewById(R.id.edit_offer_start_date);
        edit_offer_end_date = findViewById(R.id.edit_offer_end_date);

        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        datePicker1=new DatePickerDialog(ComboProductOfferActivity.this,new DatePickerDialog.OnDateSetListener(){

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
        datePicker1.setMessage("Select Start Date");

        datePicker2=new DatePickerDialog(ComboProductOfferActivity.this,new DatePickerDialog.OnDateSetListener(){

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
        datePicker2.setMessage("Select End Date");

        add_combo = findViewById(R.id.add_combo);
        ((GradientDrawable)add_combo.getBackground()).setColor(colorTheme);
        add_combo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyProductItem productItem = new MyProductItem();
                productItem.setProdName("");
                myProductItems.add(productItem);
                comboOfferAdapter.notifyDataSetChanged();
            }
        });

        findViewById(R.id.relative_footer_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 createComboOffer();
            }
        });

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

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        comboOfferAdapter=new ComboOfferAdapter(this,myProductItems);
        comboOfferAdapter.setMyItemClickListener(this);
        comboOfferAdapter.setMyItemTypeClickListener(this);
        recyclerView.setAdapter(comboOfferAdapter);

        if (flag != null && flag.equals("edit")) {
            productComboOffer = (ProductComboOffer)getIntent().getSerializableExtra("data");
            edit_offer_name.setText(productComboOffer.getOfferName());
            edit_offer_start_date.setText(productComboOffer.getStartDate());
            edit_offer_end_date.setText(productComboOffer.getEndDate());
            myProductItems.clear();
            for(ProductComboDetails details : productComboOffer.getProductComboOfferDetails()){
                productItem = dbHelper.getProductDetails(details.getPcodProdId());
                productItem.setId(details.getId());
                productItem.setPcoId(details.getPcodPcoId());
                productItem.setQty(details.getPcodProdQty());
                productItem.setProdSp(details.getPcodPrice());
                productItem.setStatus("1");
                myProductItems.add(productItem);
            }

            comboOfferAdapter.notifyDataSetChanged();
            TextView textView = findViewById(R.id.text_action);
            textView.setText("Update");
        }

    }

    private void createComboOffer(){
        String offerName = edit_offer_name.getText().toString();
        String startDate = edit_offer_start_date.getText().toString();
        String endDate = edit_offer_end_date.getText().toString();

        if(TextUtils.isEmpty(offerName)){
            DialogAndToast.showDialog("Please enter offer name.",this);
            return;
        }

        if(TextUtils.isEmpty(startDate)){
            DialogAndToast.showDialog("Please enter offer start date.",this);
            return;
        }

        if(TextUtils.isEmpty(endDate)){
            DialogAndToast.showDialog("Please enter offer end date.",this);
            return;
        }

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject productObject = null;
        try {
            jsonObject.put("offerName",offerName);
            jsonObject.put("status","1");
            jsonObject.put("startDate",startDate);
            jsonObject.put("endDate",endDate);
            for(MyProductItem item : myProductItems){
                productObject = new JSONObject();
                if(flag != null && flag.equals("edit")){
                    productObject.put("id",item.getId());
                    productObject.put("pcodPcoId",item.getPcoId());
                }
                productObject.put("pcodProdId",item.getProdId());
                productObject.put("pcodProdQty",item.getQty());
                productObject.put("pcodPrice",item.getProdSp());
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
        Log.i(TAG,"params "+jsonObject.toString());
        String url ="";
        String api = "";
        if(flag != null && flag.equals("edit")){
           // params.put("id", ""+productDiscountOffer.getId());
            url = getResources().getString(R.string.url) + Constants.UPDATE_COMBO_PRODUCT_OFFER;
            api = "updateComboOffer";

        }else{
            url = getResources().getString(R.string.url) + Constants.CREATE_COMBO_PRODUCT_OFFER;
            api = "comboOffer";
        }
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,jsonObject,api);
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {
        showProgress(false);
        try {
            Log.d("response", response.toString());
            if(apiName.equals("comboOffer")){
                if(response.getString("status").equals("true")||response.getString("status").equals(true)){
                    showMyDialog("Offer created successfully.");

                }else {
                    DialogAndToast.showToast(response.getString("message"),ComboProductOfferActivity.this);
                }
            }else if(apiName.equals("updateComboOffer")){
                if(response.getString("status").equals("true")||response.getString("status").equals(true)){
                    showMyDialog("Offer updated successfully.");
                }else {
                    DialogAndToast.showToast(response.getString("message"),ComboProductOfferActivity.this);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            DialogAndToast.showToast(getResources().getString(R.string.json_parser_error)+e.toString(),ComboProductOfferActivity.this);
        }
    }

    @Override
    public void onItemClicked(int prodId) {
        if(isProductSearch){
            isProductSearch = false;
            MyProductItem item = dbHelper.getProductDetails(prodId);
            MyProductItem myItem = myProductItems.get(position);
            myItem.setProdId(item.getProdId());
            myItem.setProdName(item.getProdName());
            myItem.setProdMrp(item.getProdMrp());
            myItem.setProdSp(item.getProdSp());
            comboOfferAdapter.notifyItemChanged(position);
        }else{
            isProductSearch = true;
            this.position = prodId;
            BottomSearchFragment bottomSearchFragment = new BottomSearchFragment();
            bottomSearchFragment.setCallingActivityName("productList");
            Bundle bundle = new Bundle();
            bundle.putString("flag","searchCartProduct");
            bottomSearchFragment.setArguments(bundle);
            bottomSearchFragment.setMyItemClickListener(this);
            bottomSearchFragment.show(getSupportFragmentManager(), "Search Product Bottom Sheet");

        }

    }

    public void onDialogPositiveClicked(){
        Intent intent = new Intent(ComboProductOfferActivity.this,MyOffersActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onItemClicked(int position, int type) {
        this.position = position;
        Intent intent = new Intent(this, ScannarActivity.class);
        intent.putExtra("flag","offers");
        intent.putExtra("type","offers");
        // startActivity(intent);
        startActivityForResult(intent,2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "Result received");
        if (data != null) {
            if (requestCode == 2) {
                MyProductItem item = dbHelper.getProductDetails(data.getIntExtra("prod_id", 0));
                MyProductItem myItem = myProductItems.get(position);
                myItem.setProdId(item.getProdId());
                myItem.setProdName(item.getProdName());
                myItem.setProdMrp(item.getProdMrp());
                myItem.setProdSp(item.getProdSp());
                comboOfferAdapter.notifyItemChanged(position);
            }
        }
    }
}
