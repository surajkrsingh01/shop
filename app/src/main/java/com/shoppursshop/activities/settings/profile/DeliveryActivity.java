package com.shoppursshop.activities.settings.profile;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.android.volley.Request;
import com.shoppursshop.R;
import com.shoppursshop.activities.NetworkBaseActivity;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.DialogAndToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DeliveryActivity extends NetworkBaseActivity {

    private Switch switchDelivery;
    private EditText editTextMinAmount;
    private Button btnUpdate;
    private int amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        setToolbarTheme();
    }

    private void init(){
        switchDelivery = findViewById(R.id.switch_delivery);
        editTextMinAmount = findViewById(R.id.edit_min_delivery_amount);
        btnUpdate = findViewById(R.id.btn_update);

        boolean delivery = sharedPreferences.getBoolean(Constants.IS_DELIVERY_AVAILABLE,false);
        if(delivery){
            switchDelivery.setChecked(true);
        }

        editTextMinAmount.setText(""+sharedPreferences.getInt(Constants.MIN_DELIVERY_AMOUNT,0));

        /*switchDelivery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });*/


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDeliveryDetails();
            }
        });

    }

    private void updateDeliveryDetails(){
        String amountTemp = editTextMinAmount.getText().toString();
        try {
            amount = Integer.parseInt(amountTemp);
        }catch (NumberFormatException nfe){
            nfe.printStackTrace();
        }


        Map<String,String> params=new HashMap<>();
        if(switchDelivery.isChecked()){
            params.put("deliveryStatus","Y");
        }else{
            params.put("deliveryStatus","N");
        }
        params.put("amount",""+amount);
        params.put("id",sharedPreferences.getString(Constants.USER_ID,""));
        params.put("mobile",sharedPreferences.getString(Constants.MOBILE_NO,""));
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        JSONArray dataArray = new JSONArray();
        JSONObject dataObject = new JSONObject(params);
        dataArray.put(dataObject);
        String url=getResources().getString(R.string.url)+Constants.UPDATE_DELIVERY_STAUS;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"updateDeliveryDetails");
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {
        if (apiName.equals("updateDeliveryDetails")) {
            try{
                if(response.getBoolean("status")){
                    if(switchDelivery.isChecked()){
                        editor.putBoolean(Constants.IS_DELIVERY_AVAILABLE,true);
                    }else{
                        editor.putBoolean(Constants.IS_DELIVERY_AVAILABLE,false);
                    }

                    editor.putInt(Constants.MIN_DELIVERY_AMOUNT,amount);
                    editor.commit();
                    DialogAndToast.showToast(response.getString("message"),this);
                }else{
                    DialogAndToast.showDialog(response.getString("message"),this);
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

}
