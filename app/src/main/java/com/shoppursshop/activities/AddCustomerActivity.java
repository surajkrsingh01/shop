package com.shoppursshop.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.shoppursshop.R;
import com.shoppursshop.utilities.ConnectionDetector;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.DialogAndToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddCustomerActivity extends NetworkBaseActivity {

    private EditText etName,etMobile,etEmail,etAddress,etPinCode;

    private RelativeLayout rlFooter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_customer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initFooterAction(this);

        init();

    }

    private void init(){
        etName = findViewById(R.id.edit_customer_name);
        etMobile = findViewById(R.id.edit_customer_name);
        etEmail = findViewById(R.id.edit_customer_name);
        etAddress = findViewById(R.id.edit_customer_name);
        etPinCode = findViewById(R.id.edit_customer_name);
        rlFooter = findViewById(R.id.relative_footer_action);

        rlFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ConnectionDetector.isNetworkAvailable(AddCustomerActivity.this)){
                    registerCustomer();
                }
            }
        });
    }

    private void registerCustomer(){
        String mobile = etMobile.getText().toString();
        String name =   etName.getText().toString();
        String email =   etEmail.getText().toString();
        String address =   etAddress.getText().toString();
        String pin =   etPinCode.getText().toString();

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
        params.put("address",address);
        params.put("email",email);
        params.put("pin",pin);
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
            if (apiName.equals("registerShopCustomer")) {
                if (response.getBoolean("status")) {
                    showMyDialog(response.getString("message"));
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

    @Override
    public void onDialogPositiveClicked(){
        etName.setText("");
        etMobile.setText("");
        etName.setError(null);
        etMobile.setError(null);
        etEmail.setText("");
        etAddress.setText("");
        etPinCode.setText("");
    }

}
