package com.shoppursshop.activities.payment.mPos;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.widget.Toolbar;

import com.pnsol.sdk.auth.AccountValidator;
import com.pnsol.sdk.interfaces.DeviceType;
import com.pnsol.sdk.payment.PaymentInitialization;
import com.shoppursshop.R;
import com.shoppursshop.activities.NetworkBaseActivity;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.DialogAndToast;

import java.util.List;
import static com.pnsol.sdk.interfaces.PaymentTransactionConstants.FAIL;
import static com.pnsol.sdk.interfaces.PaymentTransactionConstants.PAYMENT_TYPE;
import static com.pnsol.sdk.interfaces.PaymentTransactionConstants.SALE;
import static com.pnsol.sdk.interfaces.PaymentTransactionConstants.SOCKET_NOT_CONNECTED;
import static com.pnsol.sdk.interfaces.PaymentTransactionConstants.SUCCESS;

public class MPayActivity extends NetworkBaseActivity {

    //Testing
    //private String PARTNER_API_KEY = "2C869B63DD4E";
    //private String MERCHANT_API_KEY = "2C869B63DD4E";
    private String PARTNER_API_KEY = "763432092B47";
    private String MERCHANT_API_KEY = "763432092B47";

    //Production
  //  private String PARTNER_API_KEY = "E5004EA8E077";
 //   private String MERCHANT_API_KEY = "DE4B7AF220C7";

    private int devicePosition;
    private List<String> list;
    private String ordAmount,deviceType;
    private int deviceCommMode;



    private PaymentInitialization initialization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpay);
        Toolbar toolbar = (Toolbar) (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();

    }

    private void init(){
        String devType = getIntent().getStringExtra("deviceType");
        deviceType = devType;
        Log.i(TAG,"device type "+devType+" "+deviceType);
        ordAmount = getIntent().getStringExtra("totalAmount");
       // ordAmount = "50.00";
        Log.i(TAG,"amount "+ordAmount);
        setPaymentPos();
    }

    private void setPaymentPos(){
        try {
            AccountValidator validator = new AccountValidator(getApplicationContext());
            if (!validator.isAccountActivated()) {
                DialogAndToast.showToast("Validating...", MPayActivity.this);
                validator.accountActivation(handler, MERCHANT_API_KEY, PARTNER_API_KEY);
            }else{
                Log.i(TAG,"Activated");
                //getBlueToothSpinnerList();
                initiateTransaction();
                DialogAndToast.showToast("Activated", MPayActivity.this);
            }
        }catch (RuntimeException e) {
            e.printStackTrace();
            Log.i(TAG,"Exception "+e.getMessage());
        }

    }

    private void initiateTransaction(){
       /* try {
            initialization=new PaymentInitialization(MPayActivity.this);
            initialization.getDeviceeserialNumber(handler,1,deviceCommMode,deviceMACAddress);
        }catch (RuntimeException e){
            e.printStackTrace();
            Log.i(TAG,"Exception "+e.getMessage());
        }*/

        Intent i = new Intent(MPayActivity.this, MPaymentTransactionActivity.class);
        i.putExtra("devicetype", deviceType);
        i.putExtra(PAYMENT_TYPE, SALE);
       // i.putExtra("transactionmode", deviceCommMode);
        i.putExtra("orderNumber",getIntent().getStringExtra("orderNumber"));
        i.putExtra("custCode", getIntent().getStringExtra("custCode"));
        i.putExtra("custId", getIntent().getIntExtra("custId",0));
        i.putExtra("custName", getIntent().getStringExtra("custName"));
        i.putExtra("custMobile", getIntent().getStringExtra("custMobile"));
        i.putExtra("custImage", getIntent().getStringExtra("custImage"));
        i.putExtra("custUserCreateStatus", getIntent().getStringExtra("custUserCreateStatus"));
        i.putExtra("amount", ordAmount);
        i.putExtra("ordCouponId",getIntent().getStringExtra("ordCouponId"));
        i.putExtra("totalTax",getIntent().getFloatExtra("totalTax",0f));
        i.putExtra("deliveryCharges",getIntent().getFloatExtra("deliveryCharges",0f));
        i.putExtra("totDiscount",getIntent().getFloatExtra("totDiscount",0f));
        i.putExtra("referanceno", sharedPreferences.getString(Constants.MERCHANT_REF_NO,"null"));
        startActivity(i);
        MPayActivity.this.finish();
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
           // Log.i(TAG,"Handler "+(String) msg.obj);

            if (msg.what == SUCCESS) {
                Log.i(TAG,"Success");
                DialogAndToast.showToast("Success",MPayActivity.this);
                initiateTransaction();
            }
            if (msg.what == FAIL) {
                Log.i(TAG,"Failed ");
                DialogAndToast.showToast("Failed",MPayActivity.this);
            }

            if(msg.what==SOCKET_NOT_CONNECTED) {
                Log.i(TAG,"Failed ");
                DialogAndToast.showToast("Failed",MPayActivity.this);
            }

        };
    };
}
