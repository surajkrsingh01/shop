package com.shoppursshop.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.shoppursshop.R;
import com.shoppursshop.activities.payment.mPos.MPayTransactionDetailsActivity;
import com.shoppursshop.database.DbHelper;
import com.shoppursshop.models.Barcode;
import com.shoppursshop.models.MyProductItem;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.DialogAndToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionDetailsActivity extends NetworkBaseActivity {

    private TextView textViewStatusHeader,tvStatus,tvTransactionId,tvRrn,tvAmount,tvPaymentMethod;
    private ImageView imageStatusSuccess,imageViewStatusFailure;
    private RelativeLayout rlFooter;
    private TextView tvFooter;
    private boolean isDelivered;
    private JSONObject dataObject;

    private String custCode,orderNumber,paymentStatus;
    private JSONArray shopArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();
    }

    private void init(){
        textViewStatusHeader = findViewById(R.id.tv_status_header);
        tvStatus = findViewById(R.id.text_status_message);
        tvPaymentMethod = findViewById(R.id.tv_payment_method);
        tvTransactionId = findViewById(R.id.transaction_id);
        tvRrn = findViewById(R.id.tv_rrn);
        tvAmount = findViewById(R.id.tv_amount);
        imageStatusSuccess = findViewById(R.id.image_status_success);
        imageViewStatusFailure = findViewById(R.id.image_status_failure);
        rlFooter = findViewById(R.id.relative_footer_action);
        tvFooter = findViewById(R.id.text_action);
        initFooterAction(this);

        try {
            dataObject = new JSONObject(getIntent().getStringExtra("responseData"));
            tvTransactionId.setText(dataObject.getString("transactionId"));
            tvRrn.setText(dataObject.getString("orderRefNo"));
            tvPaymentMethod.setText(dataObject.getString("paymentMethod"));
            tvAmount.setText(dataObject.getString("amount"));

            custCode = dataObject.getString("custCode");
            orderNumber = dataObject.getString("orderNumber");

            paymentStatus = dataObject.getString("responseMessage");
            if(paymentStatus.equals("Success")){
                placeOrder();
            }else{
                setStatusLayout(false);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }



        rlFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isDelivered){
                    openInvoiceActivity();
                }else{
                    deliverOrder();
                }
            }
        });
    }

    private void setStatusLayout(boolean status){
        if(status){
            imageStatusSuccess.setVisibility(View.VISIBLE);
            imageViewStatusFailure.setVisibility(View.GONE);
            textViewStatusHeader.setText("Congrats");
            tvStatus.setText("Order has been successfully placed.");
            placeOrder();

        }else{
            imageStatusSuccess.setVisibility(View.GONE);
            imageViewStatusFailure.setVisibility(View.VISIBLE);
            textViewStatusHeader.setText("Sorry");
            tvStatus.setText("There is some problem occurred.");

        }
    }

    private void placeOrder(){
        try {
            shopArray = new JSONArray(getIntent().getStringExtra("shopArray"));
            shopArray.getJSONObject(0).put("orderNumber", dataObject.getString("orderNumber"));
            shopArray.getJSONObject(0).put("transactionId", dataObject.getString("transactionId") );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, shopArray.toString());
        String url=getResources().getString(R.string.url)+ Constants.PLACE_ORDER;
        showProgress(true);
        jsonArrayV2ApiRequest(Request.Method.POST,url, shopArray,"place_order");
    }

    private void deliverOrder(){
        Map<String,String> params=new HashMap<>();
        params.put("orderNumber", orderNumber);
        params.put("custCode",custCode);
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+Constants.ORDER_DELIVERED;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"orderDelivered");
    }

    private void openInvoiceActivity(){
        Intent intent = new Intent(TransactionDetailsActivity.this,InvoiceActivity.class);
        intent.putExtra("orderNumber",getIntent().getStringExtra("orderNumber"));
        startActivity(intent);
        finish();
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {
        showProgress(false);
        try {
            // JSONObject jsonObject=response.getJSONObject("response");
            Log.d("response", response.toString());
            if(apiName.equals("place_order")){
                if(response.getString("status").equals("true")||response.getString("status").equals(true)){
                    setStatusLayout(true);
                    Log.d(TAG, "Order Placed" );
                }else {
                    setStatusLayout(false);
                    DialogAndToast.showToast(response.getString("message"),TransactionDetailsActivity.this);
                }
            }else if (apiName.equals("orderDelivered")) {
                if (response.getBoolean("status")) {
                    tvFooter.setText("View Invoice");
                    isDelivered = true;
                }else{
                    DialogAndToast.showDialog(response.getString("message"),this);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            DialogAndToast.showToast(getResources().getString(R.string.json_parser_error)+e.toString(),TransactionDetailsActivity.this);
        }
    }

}
