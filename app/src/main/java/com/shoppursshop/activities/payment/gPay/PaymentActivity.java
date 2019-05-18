package com.shoppursshop.activities.payment.gPay;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.shoppursshop.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PaymentActivity extends AppCompatActivity {

    private PaymentsClient mPaymentsClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPaymentsClient =
                Wallet.getPaymentsClient(
                        this,
                        new Wallet.WalletOptions.Builder()
                                .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
                                .build());


        IsReadyToPayRequest request =
                IsReadyToPayRequest.fromJson(getIsReadyToPayRequest().toString());
        Task<Boolean> task = mPaymentsClient.isReadyToPay(request);
        task.addOnCompleteListener(
                new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        try {
                            boolean result = task.getResult(ApiException.class);
                            if (result) {
                                // show Google Pay as a payment option
                            }
                        } catch (ApiException e) {
                        }
                    }
                });


    }

    public static JSONObject getPaymentDataRequest() {
        JSONObject paymentDataRequest = getBaseRequest();
        try {
            paymentDataRequest.put(
                    "allowedPaymentMethods",
                    new JSONArray()
                            .put(getCardPaymentMethod()));
            paymentDataRequest.put("transactionInfo", getTransactionInfo());
            paymentDataRequest.put("merchantInfo", getMerchantInfo());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return paymentDataRequest;
    }

    private static JSONObject getMerchantInfo() {
        try {
            return new JSONObject()
                    .put("merchantName", "Example Merchant");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static JSONObject getTransactionInfo() {
        JSONObject transactionInfo = new JSONObject();
        try {
            transactionInfo.put("totalPrice", "12.34");
            transactionInfo.put("totalPriceStatus", "FINAL");
            transactionInfo.put("currencyCode", "USD");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return transactionInfo;
    }

    public static JSONObject getIsReadyToPayRequest() {
        JSONObject isReadyToPayRequest = getBaseRequest();
        try {
            isReadyToPayRequest.put(
                    "allowedPaymentMethods",
                    new JSONArray()
                            .put(getBaseCardPaymentMethod()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return isReadyToPayRequest;
    }

    private static JSONObject getBaseRequest() {
        JSONObject baseObject = null;
        try {
            baseObject = new JSONObject();
            baseObject.put("apiVersion",2);
            baseObject.put("apiVersionMinor",0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return baseObject;
    }

    private static JSONObject getTokenizationSpecification() {
        JSONObject tokenizationSpecification = new JSONObject();
        try {
            tokenizationSpecification.put("type", "PAYMENT_GATEWAY");
            tokenizationSpecification.put(
                    "parameters",
                    new JSONObject()
                            .put("gateway", "example")
                            .put("gatewayMerchantId", "exampleMerchantId"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return tokenizationSpecification;
    }

    private static JSONArray getAllowedCardNetworks() {
        return new JSONArray()
                .put("AMEX")
                .put("DISCOVER")
                .put("JCB")
                .put("MASTERCARD")
                .put("VISA");
    }

    private static JSONArray getAllowedCardAuthMethods() {
        return new JSONArray()
                .put("PAN_ONLY")
                .put("CRYPTOGRAM_3DS");
    }

    private static JSONObject getBaseCardPaymentMethod() {
        JSONObject cardPaymentMethod = new JSONObject();
        try {
            cardPaymentMethod.put("type", "CARD");
            cardPaymentMethod.put(
                    "parameters",
                    new JSONObject()
                            .put("allowedAuthMethods", getAllowedCardAuthMethods())
                            .put("allowedCardNetworks", getAllowedCardNetworks()));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return cardPaymentMethod;
    }

    private static JSONObject getCardPaymentMethod() {
        JSONObject cardPaymentMethod = getBaseCardPaymentMethod();
        try {
            cardPaymentMethod.put("tokenizationSpecification", getTokenizationSpecification());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cardPaymentMethod;
    }

}
