package com.shoppursshop.activities.customers;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.shoppursshop.R;
import com.shoppursshop.activities.base.NetworkBaseActivity;
import com.shoppursshop.activities.ScannarActivity;
import com.shoppursshop.activities.order.CartActivity;
import com.shoppursshop.fragments.BottomSearchFragment;
import com.shoppursshop.interfaces.MyListItemClickListener;
import com.shoppursshop.utilities.ConnectionDetector;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.DialogAndToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CustomerInfoActivity extends NetworkBaseActivity implements MyListItemClickListener {

    private EditText editTextMobile,editTextName;
    private RelativeLayout btnContinue;
    private boolean isCustomerRegistered,infoChecked;

    private ImageView imageViewScan,imageViewSearch;

    private String mobile,name,custCode,custImage,custUserCreateStatus,khataNo;
    private int custId;
    private  BottomSearchFragment bottomSearchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_info);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
        initFooterAction(this);
       // initFooter(this,0);

    }

    private void init(){
        imageViewScan = findViewById(R.id.image_scan);
        imageViewSearch = findViewById(R.id.image_search);
        editTextMobile = findViewById(R.id.edit_mobile);
        editTextName = findViewById(R.id.edit_full_name);
        btnContinue = findViewById(R.id.relative_footer_action);

        editTextMobile.setText("9958736910");

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(infoChecked){
                    if(isCustomerRegistered){
                        onDialogPositiveClicked();
                    }else{
                        registerCustomer();
                    }
                }else{
                    checkCustomerInfo();
                }
            }
        });

        /*btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoChecked = false;
                isCustomerRegistered = false;
                editTextMobile.setText("");
                editTextName.setText("");
                editTextName.setVisibility(View.GONE);
                btnBack.setVisibility(View.GONE);
                btnContinue.setText("Continue");
            }
        });*/

        imageViewScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openScannar();
            }
        });

        imageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSearchFragment = new BottomSearchFragment();
                bottomSearchFragment.setCallingActivityName("customerInfoActivity");
                bottomSearchFragment.setMyListItemClickListener(CustomerInfoActivity.this);
                bottomSearchFragment.show(getSupportFragmentManager(), "Search Customer Bottom Sheet");
            }
        });

        if (ConnectionDetector.isNetworkAvailable(this)){
           // getItemList();
        }

    }

    @Override
    public void onResume(){
        super.onResume();

        infoChecked = false;
        isCustomerRegistered = false;
        editTextMobile.setText("");
        editTextName.setText("");
        editTextName.setVisibility(View.GONE);
      //  btnBack.setVisibility(View.GONE);
     //   btnContinue.setText("Continue");

    }

    private void checkCustomerInfo(){
        mobile = editTextMobile.getText().toString();

        if(TextUtils.isEmpty(mobile)){
            DialogAndToast.showDialog("Please enter mobile number.",this);
            return;
        }else if(mobile.length() != 10){
            DialogAndToast.showDialog("Please enter valid mobile number.",this);
            return;
        }

        Map<String,String> params=new HashMap<>();
        params.put("mobile",mobile);
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+Constants.IS_CUSTOMER_REGISTERED;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"checkCustomerRegistered");


    }

    private void registerCustomer(){
        mobile = editTextMobile.getText().toString();
        name = editTextName.getText().toString();

        if(TextUtils.isEmpty(mobile)){
            DialogAndToast.showDialog("Please enter mobile number.",this);
            return;
        }else if(mobile.length() != 10){
            DialogAndToast.showDialog("Please enter valid mobile number.",this);
            return;
        }

        if(TextUtils.isEmpty(name)){
            DialogAndToast.showDialog("Please enter customer name.",this);
            return;
        }

        Map<String,String> params=new HashMap<>();
        params.put("mobileNo",mobile);
        params.put("name",name);
        params.put("address","");
        params.put("email","");
        params.put("pin","");
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+Constants.REGISTER_CUSTOMER;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"registerShopCustomer");
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {

        try {
            if (apiName.equals("checkCustomerRegistered")) {
                if (response.getBoolean("status")) {
                    JSONObject dataObject = response.getJSONObject("result");
                    custCode = dataObject.getString("code");
                    custImage = dataObject.getString("photo");
                    name = dataObject.getString("name");
                    custId = dataObject.getInt("id");
                    khataNo = dataObject.getString("kbNo");
                    custUserCreateStatus = dataObject.getString("userCreateStatus");
                     isCustomerRegistered = true;
                     infoChecked = true;
                     editTextName.setVisibility(View.VISIBLE);
                     editTextName.setText(name);
                    // btnBack.setVisibility(View.VISIBLE);
                }else{
                    if(response.getInt("result") == 1){
                        isCustomerRegistered = false;
                        infoChecked = true;
                       // btnContinue.setText("Register");
                        editTextName.setVisibility(View.VISIBLE);
                       // btnBack.setVisibility(View.VISIBLE);
                    }
                    DialogAndToast.showDialog(response.getString("message"),this);
                }
            }else if (apiName.equals("registerShopCustomer")) {
                if (response.getBoolean("status")) {
                   onDialogPositiveClicked();
                }else{
                    if(response.getInt("result") == 1){
                      showMyDialog(response.getString("message"));
                    }else {
                        DialogAndToast.showDialog(response.getString("message"), this);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void openScannar(){
        Intent intent = new Intent(this, ScannarActivity.class);
        intent.putExtra("flag","scan");
        intent.putExtra("type","customerInfo");
        startActivityForResult(intent,2);
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
            bottomSearchFragment.setCallingActivityName("customerInfoActivity");
            bottomSearchFragment.setMyListItemClickListener(this);
            bottomSearchFragment.show(getSupportFragmentManager(), "Search Customer Bottom Sheet");

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
    public void onDialogPositiveClicked(){
        Intent intent = new Intent(CustomerInfoActivity.this, CartActivity.class);
        intent.putExtra("flag","order");
        intent.putExtra("custMobile",mobile);
        intent.putExtra("custName",name);
        intent.putExtra("custId", custId);
        intent.putExtra("custCode",custCode);
        intent.putExtra("custImage",custImage);
        intent.putExtra("khataNo",khataNo);
        intent.putExtra("custUserCreateStatus","S");
        startActivity(intent);
    }

    @Override
    public void onItemClicked(Bundle bundle) {
        bottomSearchFragment.dismiss();
        Intent intent = new Intent(CustomerInfoActivity.this,CartActivity.class);
        intent.putExtra("flag","order");
        intent.putExtra("custMobile",bundle.getString("custMobile"));
        intent.putExtra("custName",bundle.getString("custName"));
        intent.putExtra("custId", bundle.getInt("custId"));
        intent.putExtra("custCode",bundle.getString("custCode"));
        intent.putExtra("custImage",bundle.getString("custImage"));
        intent.putExtra("khataNo",bundle.getString("khataNo"));
        intent.putExtra("custUserCreateStatus",bundle.getString("custUserCreateStatus"));
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i(TAG,"Result received");

        if (requestCode == 2){
            if(data != null){
                Intent intent = new Intent(CustomerInfoActivity.this,CartActivity.class);
                intent.putExtra("flag","order");
                intent.putExtra("custMobile",data.getStringExtra("custMobile"));
                intent.putExtra("custName",data.getStringExtra("custName"));
                intent.putExtra("custId", data.getIntExtra("custId",0));
                intent.putExtra("custCode",data.getStringExtra("custCode"));
                intent.putExtra("custImage",data.getStringExtra("custImage"));
                intent.putExtra("khataNo",data.getStringExtra("khataNo"));
                intent.putExtra("custUserCreateStatus",data.getStringExtra("custUserCreateStatus"));
                startActivity(intent);
            }
        }
    }

}
