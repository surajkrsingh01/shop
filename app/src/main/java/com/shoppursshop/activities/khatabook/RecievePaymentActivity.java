package com.shoppursshop.activities.khatabook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.shoppursshop.R;
import com.shoppursshop.activities.base.NetworkBaseActivity;
import com.shoppursshop.activities.payment.mPos.MPayActivity;
import com.shoppursshop.activities.payment.mPos.MPayTransactionDetailsActivity;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.DialogAndToast;
import com.shoppursshop.utilities.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RecievePaymentActivity extends NetworkBaseActivity {

    private TextView tvPayCash,tvMpos;
    private EditText etAmount;
    private String khataNo;
    private int totalDueAmount;
    private int amount;
    private RelativeLayout rlFooter;
    private LinearLayout linearPayLayout;
    private String paymentMethod;
    private JSONObject dataObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recieve_payment);

        init();
        initFooterAction(this);
    }

    private void init(){
        etAmount = findViewById(R.id.edit_amount);
        tvPayCash = findViewById(R.id.tvPayCash);
        tvMpos = findViewById(R.id.tvMpos);
        linearPayLayout = findViewById(R.id.linearPayLayout);
        rlFooter = findViewById(R.id.relative_footer_action);

        khataNo = getIntent().getStringExtra("khataNo");
        totalDueAmount = getIntent().getIntExtra("totalDue",0);

        rlFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearPayLayout.setVisibility(View.VISIBLE);
                if(TextUtils.isEmpty(sharedPreferences.getString(Constants.MERCHANT_ID,""))){
                    tvMpos.setVisibility(View.GONE);
                }else{
                    tvMpos.setVisibility(View.VISIBLE);
                }
            }
        });

        tvPayCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMethod = "Cash";
                receivePayment();
            }
        });

        tvMpos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMethod = "ME3POS";
                receivePayment();
            }
        });
        String merchantRefNo = sharedPreferences.getString(Constants.MERCHANT_REF_NO,"");
        if (merchantRefNo == null || merchantRefNo.equalsIgnoreCase("null") ||
                merchantRefNo.equalsIgnoreCase(""))
        {
            merchantRefNo = String.valueOf(System.currentTimeMillis());
            editor.putString(Constants.MERCHANT_REF_NO,merchantRefNo);
            editor.commit();
        }
    }

    private void receivePayment(){
        try{
            amount = Integer.parseInt(etAmount.getText().toString());
        }catch (NumberFormatException nfe){
            nfe.printStackTrace();
        }

        if(amount == 0){
            DialogAndToast.showDialog("Please enter amount",this);
            return;
        }else if(amount > totalDueAmount){
            DialogAndToast.showDialog("Please enter valid amount. Entered amount is greater than total due amount - Rs "+totalDueAmount,this);
            return;
        }

        if(paymentMethod.equals("Cash")){
            addTransactions();
        }else{
            Intent intent = new Intent(RecievePaymentActivity.this, MPayActivity.class);
            intent.putExtra("totalAmount",String.format("%.02f",amount));
            intent.putExtra("orderNumber",Utility.getTimeStamp());
            intent.putExtra("flag","KhataTransaction");
            intent.putExtra("custCode", getIntent().getStringExtra("custCode"));
            intent.putExtra("custId", getIntent().getIntExtra("custId",0));
            intent.putExtra("custName", getIntent().getStringExtra("custName"));
            intent.putExtra("custMobile", getIntent().getStringExtra("custMobile"));
            intent.putExtra("custImage", getIntent().getStringExtra("custImage"));
            intent.putExtra("custUserCreateStatus", getIntent().getStringExtra("custUserCreateStatus"));
            intent.putExtra("deviceType", "ME3POS");
            startActivity(intent);
            finish();
        }

    }

    private void addTransactions(){
        dataObject = new JSONObject();
        try{
            dataObject.put("responseCode","00");
            dataObject.put("responseMessage","Approved");
            dataObject.put("dateTime", Utility.getTimeStamp());
           // dataObject.put("referenceNumber",mCardSaleResponseData.getRRNO());
            dataObject.put("amount",String.valueOf(amount));
          //  dataObject.put("rrn",mCardSaleResponseData.getRRNO());
            dataObject.put("transactionStatus",true);
            dataObject.put("merchantId",sharedPreferences.getString(Constants.SHOP_CODE,""));
            dataObject.put("merchantRefNo",sharedPreferences.getString(Constants.MERCHANT_REF_NO,""));
            dataObject.put("merchantRefInvoiceNo",sharedPreferences.getString(Constants.MERCHANT_REF_NO,""));
            dataObject.put("cardType","Cash");
            dataObject.put("cardBrand","Cash");
            dataObject.put("paymentMethod","Cash");
            // String tranStatus = dataObject.getString("transactionStatus");
            boolean approved = dataObject.getBoolean("transactionStatus");;
            dataObject.put("approved",approved);
            dataObject.put("transactionId","0");
            dataObject.put("transactionType","Khata/"+khataNo+"/Debit");
            dataObject.put("date",Utility.getTimeStamp());
            dataObject.put("orderNumber","Khata Debit");
            dataObject.put("payStatus", "Done");
            dataObject.put("status_message", "SUCCESS");
            dataObject.put("paymentMode", "Cash");
            dataObject.put("merchantName",sharedPreferences.getString(Constants.SHOP_NAME,""));
            dataObject.put("merchantAddress", sharedPreferences.getString(Constants.ADDRESS,""));
            dataObject.put("custCode",getIntent().getStringExtra("custCode"));
            dataObject.put("custUserCreateStatus",getIntent().getStringExtra("custUserCreateStatus"));
            dataObject.put("paymentMethod","Cash");
            dataObject.put("paymentBrand","Cash");
            dataObject.put("userName",sharedPreferences.getString(Constants.FULL_NAME,""));
            dataObject.put("shopCode",sharedPreferences.getString(Constants.SHOP_CODE,""));
            dataObject.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
            dataObject.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
            dataObject.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        }catch (JSONException e){
            e.printStackTrace();
        }
        String url=getResources().getString(R.string.url)+Constants.ADD_TRANS_DATA;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,dataObject,"updatePaymentStatus");
    }

    @Override
    public void onJsonObjectResponse(JSONObject jsonObject, String apiName) {
        try {
            if (apiName.equals("updatePaymentStatus")) {
                if (jsonObject.getBoolean("status")) {
                    dataObject.put("transactionId",jsonObject.getJSONObject("result").getString("transactionId"));
                    dataObject.put("rrn",dataObject.getString("transactionId"));
                    Intent i = new Intent(RecievePaymentActivity.this, MPayTransactionDetailsActivity.class);
                    i.putExtra("paymentResponseObject", dataObject.toString());
                    i.putExtra("totalAmount", String.valueOf(amount)+".00");
                    i.putExtra("flag", "KhataTransaction");
                    i.putExtra("custCode", getIntent().getStringExtra("custCode"));
                    i.putExtra("custId", getIntent().getIntExtra("custId", 0));
                    i.putExtra("custName", getIntent().getStringExtra("custName"));
                    i.putExtra("custMobile", getIntent().getStringExtra("custMobile"));
                    i.putExtra("custImage", getIntent().getStringExtra("custImage"));
                    i.putExtra("custUserCreateStatus", getIntent().getStringExtra("custUserCreateStatus"));
                    startActivity(i);
                    finish();
                }else{
                    DialogAndToast.showDialog(jsonObject.getString("message"),this);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            DialogAndToast.showToast(getResources().getString(R.string.json_parser_error)+e.toString(),
                    RecievePaymentActivity.this);
        }
    }
}
