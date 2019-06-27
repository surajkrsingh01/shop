package com.shoppursshop.activities.payment.ePay;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.pnsol.epay.enums.CountryEnums;
import com.pnsol.epay.sdk.OrderCreation;
import com.pnsol.epay.utils.ObjectMapperUtil;
import com.pnsol.epay.utils.UtilManager;
import com.pnsol.epay.vo.PaymentResponseVO;
import com.pnsol.exception.ServiceCallException;
import com.shoppursshop.R;
import com.shoppursshop.activities.NetworkBaseActivity;
import com.shoppursshop.activities.TransactionDetailsActivity;
import com.shoppursshop.database.DbHelper;
import com.shoppursshop.models.Barcode;
import com.shoppursshop.models.MyProductItem;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.DialogAndToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;

public class EPayPayswiffActivity extends NetworkBaseActivity {

    public static final int SUCCESS = 1;
    public static final int FAIL = -1;
    public static final int INVALID_INPUT = -2;
    public static final int ERROR_MESSAGE = -3;
    private final long merchantId = 1007874;
    private final String secretKey = "RA136A579CB4MB0475GEA022626FZDAJ";

    private String responseData;
    private boolean approved;
    private JSONObject dataObject;

    private PaymentResponseVO requestResponseVO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_epay_payswiff);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //showProgress(true);
        init();

    }

    private void init(){

        //UtilManager.REDIRECT_REQUEST

// OrderCreation
        OrderCreation orderCreation = new OrderCreation(EPayPayswiffActivity.this, handler, merchantId, secretKey,
                11.00, "PN_1234" + Calendar.getInstance().getTimeInMillis(), "Epay SDK Integration test order", 0);
        // set billing details

        orderCreation.setBillingDetails("Test", "Hyderabad", "Hyderabad", "Telangana", "500008",
                CountryEnums.IND.toString(), "test@co.in", "9000100070");
        // set shipping details
        orderCreation.setShippingDetails(null, "", "Hyderabad", "Telangana", "500008",
                CountryEnums.IND.toString(), "test@co.in", "9000100070");

        // initialization ordercreation
        orderCreation.sendRequest();
    }

    @SuppressLint("HandlerLeak")
    public final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.i(TAG,"Pay Response " + msg.obj.toString());
          //  showProgress(false);
            responseData = (String) msg.obj;
            if (msg.what == SUCCESS) {
                try {
                    requestResponseVO = (PaymentResponseVO) ObjectMapperUtil
                            .convertJSONToObject(responseData.getBytes(), requestResponseVO);
                } catch (ServiceCallException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                saveResponse();

                if(requestResponseVO != null || requestResponseVO.getResponseMessage() == ""){
                    Log.i(TAG,"Response message "+"" + requestResponseVO.getResponseMessage());

                    Log.i(TAG,"Success Response "+"orderRefNo :" + "  " + requestResponseVO.getOrderRefNo() + "\n" + "amount :"
                            + "  " + requestResponseVO.getAmount() + "\n" + "paymentId :" + "  "
                            + requestResponseVO.getPaymentId() + "\n" + "transactionId :" + "  "
                            + requestResponseVO.getTransactionId() + "\n" + "merchantId :" + "  "
                            + requestResponseVO.getMerchantId() + "\n" + "responseMessage :" + "  "
                            + requestResponseVO.getResponseMessage() + "\n" + "transactionType :" + "  "
                            + requestResponseVO.getTransactionType() + "\n" + "TransactionDate :" + "  "
                            + requestResponseVO.getTransactionDate() + "\n" + "payment method :" + "  "
                            + requestResponseVO.getPaymentMethod() + "\n" + "Bank name:" + "  "
                            + requestResponseVO.getPaymentBrand());
                }else{

                }


            }else if (msg.what == FAIL) {
                try {
                    requestResponseVO = (PaymentResponseVO) ObjectMapperUtil
                            .convertJSONToObject(responseData.getBytes(), requestResponseVO);
                } catch (ServiceCallException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                saveResponse();

                if (requestResponseVO.getResponseMessage() != null || requestResponseVO.getResponseMessage() == "") {

                    Log.i(TAG,"Response Message : " + requestResponseVO.getResponseMessage() + "\n"

                            + "Order reference no : "
                            + (requestResponseVO.getOrderRefNo() == null ? "" : requestResponseVO.getOrderRefNo())
                            + "\n" + "Order amount : " + (requestResponseVO.getAmount()) + "\n" + "Response code : "
                            + (requestResponseVO.getResponseCode() == null ? "" : requestResponseVO.getResponseCode()));

                    Log.i(TAG,"Failed Response "+"orderRefNo :" + "  " + requestResponseVO.getOrderRefNo() + "\n" + "amount :"
                            + "  " + requestResponseVO.getAmount() + "\n" + "paymentId :" + "  "
                            + requestResponseVO.getPaymentId() + "\n" + "transactionId :" + "  "
                            + requestResponseVO.getTransactionId() + "\n" + "merchantId :" + "  "
                            + requestResponseVO.getMerchantId() + "\n" + "responseMessage :" + "  "
                            + requestResponseVO.getResponseMessage() + "\n" + "transactionType :" + "  "
                            + requestResponseVO.getTransactionType() + "\n" + "TransactionDate :" + "  "
                            + requestResponseVO.getTransactionDate() + "\n" + "payment method :" + "  "
                            + requestResponseVO.getPaymentMethod() + "\n" + "Bank name:" + "  "
                            + requestResponseVO.getPaymentBrand());
                }
            }else if (msg.what == INVALID_INPUT) {

                Log.i(TAG,"Invalid Response "+msg.obj.toString());
            } else if (msg.what == ERROR_MESSAGE) {

                Log.i(TAG,"Error Response "+msg.obj.toString());
            }
        }
    };


    private void saveResponse(){
        try{
            dataObject = new JSONObject(responseData);
            Log.i(TAG,"Save Response "+dataObject.toString());
            try{
                dataObject.put("orderNumber",getIntent().getStringExtra("orderNumber"));
                if(dataObject.getString("responseMessage").equals("Success")){
                    dataObject.put("payStatus", "Done");
                    approved = true;
                }else{
                    dataObject.put("payStatus", "Failed");
                    approved = false;
                }
                dataObject.put("paymentMode", "Epay");
                dataObject.put("custCode",getIntent().getStringExtra("custCode"));
                //dataObject.put("cardHolderName",dataObject.getString("Card Hodler Name"));
                dataObject.put("userName",sharedPreferences.getString(Constants.FULL_NAME,""));
                dataObject.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
                dataObject.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
                dataObject.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
            }catch (JSONException e){
                e.printStackTrace();
            }
            String url=getResources().getString(R.string.url)+Constants.ADD_TRANS_DATA;
            showProgress(true);
            jsonObjectApiRequest(Request.Method.POST,url,dataObject,"updatePaymentStatus");

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {
        showProgress(false);
        try {
            // JSONObject jsonObject=response.getJSONObject("response");
            Log.d("response", response.toString());
            if (apiName.equals("updatePaymentStatus")) {
                if (response.getBoolean("status")) {
                    Intent intent = new Intent(EPayPayswiffActivity.this, TransactionDetailsActivity.class);
                    intent.putExtra("responseData",dataObject.toString());
                    intent.putExtra("shopArray",getIntent().getStringExtra("shopArray"));
                    startActivity(intent);
                    finish();
                }else{
                    DialogAndToast.showDialog(response.getString("message"),this);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            DialogAndToast.showToast(getResources().getString(R.string.json_parser_error)+e.toString(),EPayPayswiffActivity.this);
        }
    }

}
