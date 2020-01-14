package com.shoppursshop.activities.settings;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;

import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.shoppursshop.R;
import com.shoppursshop.activities.base.NetworkBaseActivity;
import com.shoppursshop.models.MyUser;
import com.shoppursshop.services.NotificationService;
import com.shoppursshop.utilities.ConnectionDetector;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.DialogAndToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddUserActivity extends NetworkBaseActivity {

    private EditText editTextName,editTextMobile;
    private MyUser myUser;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();
        initFooterAction(this);
    }

    private void init(){
        editTextName = findViewById(R.id.edit_user_name);
        editTextMobile = findViewById(R.id.edit_mobile);

        id = getIntent().getIntExtra("id",0);

        findViewById(R.id.relative_footer_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ConnectionDetector.isNetworkAvailable(AddUserActivity.this)){
                    attemptAddUser();
                }else{
                    DialogAndToast.showDialog(getResources().getString(R.string.no_internet),AddUserActivity.this);
                }
            }
        });

        TextView tv_top_parent = findViewById(R.id.text_left_label);
        TextView tv_parent = findViewById(R.id.text_right_label);

        tv_top_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(new Intent(AddUserActivity.this, SettingActivity.class));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        tv_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void attemptAddUser(){
        String name = editTextName.getText().toString();
        String mobile = editTextMobile.getText().toString();

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
        params.put("ulId",""+id);
        params.put("mobile",mobile);
        params.put("name",name);
        params.put("shopCode",sharedPreferences.getString(Constants.SHOP_CODE,""));
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+Constants.REGISTER_USER;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"registerShopUser");

    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {

        try {
            if (apiName.equals("registerShopUser")) {
                if (response.getBoolean("status")) {
                    JSONObject jsonObject = response.getJSONObject("result");
                    myUser = new MyUser();
                    myUser.setId(jsonObject.getString("id"));
                    myUser.setUsername(jsonObject.getString("username"));
                    myUser.setMobile(jsonObject.getString("mobile"));
                    myUser.setIsActive(jsonObject.getString("isActive"));
                   // myUser.setImeiNo(jsonObject.getString("isActive"));
                    myUser.setDbName(jsonObject.getString("dbName"));
                    myUser.setDbUserName(jsonObject.getString("dbUserName"));
                    myUser.setDbPassword(jsonObject.getString("dbPassword"));
                    NotificationService.displayNotification(this,"Password for "+myUser.getUsername()+
                            " is "+jsonObject.getString("mpassword"));
                    showMyDialog(response.getString("message"));
                }else{
                    DialogAndToast.showDialog(response.getString("message"), this);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDialogPositiveClicked(){
        Intent intent = new Intent();
        intent.putExtra("myUser",myUser);
        setResult(-1,intent);
        finish();
    }

    @Override
    protected void onFooterActionClicked() {
        super.onFooterActionClicked();

        if(ConnectionDetector.isNetworkAvailable(AddUserActivity.this)){
            attemptAddUser();
        }else{
            DialogAndToast.showDialog(getResources().getString(R.string.no_internet),AddUserActivity.this);
        }
    }
}
