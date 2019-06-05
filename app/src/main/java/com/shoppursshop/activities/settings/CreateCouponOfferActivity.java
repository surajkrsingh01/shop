package com.shoppursshop.activities.settings;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.shoppursshop.R;
import com.shoppursshop.activities.NetworkBaseActivity;
import com.shoppursshop.models.Coupon;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.DialogAndToast;
import com.shoppursshop.utilities.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CreateCouponOfferActivity extends NetworkBaseActivity {

    private EditText etOfferName,etPercentage,etMaxDiscount,etStartDate,etEndDate;
    private TextView tv_top_parent,tv_parent;
    private String flag;
    private Coupon coupon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_coupon_offer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initFooterAction(this);
        init();

    }

    private void init() {
        flag = getIntent().getStringExtra("flag");
        tv_top_parent = findViewById(R.id.text_left_label);
        tv_parent = findViewById(R.id.text_right_label);
        etOfferName = findViewById(R.id.edit_offer_name);
        etPercentage = findViewById(R.id.edit_offer_percentage);
        etMaxDiscount = findViewById(R.id.edit_offer_amount);
        etStartDate = findViewById(R.id.edit_offer_start_date);
        etEndDate = findViewById(R.id.edit_offer_end_date);
        RelativeLayout relative_footer_action = findViewById(R.id.relative_footer_action);

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
                etStartDate.setText(Utility.parseDate(cal,"yyyy-MM-dd"));
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
                etEndDate.setText(Utility.parseDate(cal,"yyyy-MM-dd"));
            }
        },year,month,day);
        datePicker2.setCancelable(false);
        datePicker2.setMessage("Select Offer End Date");


        if(flag != null && flag.equals("edit")){
            coupon = (Coupon)getIntent().getSerializableExtra("data");
            if(coupon != null){
                etOfferName.setText(coupon.getName());
                etMaxDiscount.setText(""+(int)coupon.getAmount());
                etPercentage.setText(""+(int)coupon.getPercentage());
                etStartDate.setText(coupon.getStartDate());
                etEndDate.setText(coupon.getEndDate());
            }
            TextView textView = findViewById(R.id.text_action);
            textView.setText("Update");
        }

        etStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker1.show();
            }
        });

        etEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker2.show();
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
                startActivity(new Intent(CreateCouponOfferActivity.this, MyOffersActivity.class));
                finish();
            }
        });
        tv_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateCouponOfferActivity.this, CreateOfferActivity.class));
                finish();
            }
        });

    }

    private void createUpdateOffer(){
        String offer_name = etOfferName.getText().toString();
        String percentage = etPercentage.getText().toString();
        String maxDiscount = etMaxDiscount.getText().toString();
        String offer_start_date = etStartDate.getText().toString();
        String offer_end_date = etEndDate.getText().toString();

        if (TextUtils.isEmpty(offer_name)) {
            DialogAndToast.showDialog("Enter Offer Name", this);
            return;
        }

        if (TextUtils.isEmpty(percentage)) {
            DialogAndToast.showDialog("Enter Discount Percentage", this);
            return;
        }
        if (TextUtils.isEmpty(maxDiscount)) {
            DialogAndToast.showDialog("Enter Maximum Discount", this);
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
        params.put("name", offer_name);
        params.put("percentage", percentage);
        params.put("amount", maxDiscount);
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
            params.put("id", ""+coupon.getId());
            url = getResources().getString(R.string.url) + Constants.UPDATE_COUPON_OFFER;
            api = "updateCouponOffer";

        }else{
            url = getResources().getString(R.string.url) + Constants.CREATE_COUPON_OFFER;
            api = "createCouponOffer";
        }
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST, url, new JSONObject(params), api);

    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {
        showProgress(false);
        try {
            Log.d("response", response.toString());
            if(apiName.equals("createCouponOffer")){
                if(response.getString("status").equals("true")||response.getString("status").equals(true)){
                    showMyDialog("Offer created successfully.");
                }else {
                    DialogAndToast.showToast(response.getString("message"),CreateCouponOfferActivity.this);
                }
            }else if(apiName.equals("updateCouponOffer")){
                if(response.getString("status").equals("true")||response.getString("status").equals(true)){
                    showMyDialog("Offer updated successfully.");
                }else {
                    DialogAndToast.showToast(response.getString("message"),CreateCouponOfferActivity.this);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            DialogAndToast.showToast(getResources().getString(R.string.json_parser_error)+e.toString(),CreateCouponOfferActivity.this);
        }
    }

    public void onDialogPositiveClicked(){
        Intent intent = new Intent(CreateCouponOfferActivity.this,MyOffersActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
