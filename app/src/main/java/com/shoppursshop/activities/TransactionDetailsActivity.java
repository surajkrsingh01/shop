package com.shoppursshop.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shoppursshop.R;

import org.json.JSONException;
import org.json.JSONObject;

public class TransactionDetailsActivity extends NetworkBaseActivity {

    private TextView textViewStatusHeader,tvStatus,tvTransactionId,tvRrn,tvAmount,tvPaymentMethod;
    private ImageView imageStatusSuccess,imageViewStatusFailure;
    private RelativeLayout rlFooter;
    private TextView tvFooter;
    private boolean isDelivered;
    private JSONObject dataObject;

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

            String status = dataObject.getString("responseMessage");
            if(status.equals("Success")){
                imageStatusSuccess.setVisibility(View.VISIBLE);
                imageViewStatusFailure.setVisibility(View.GONE);
                textViewStatusHeader.setText("Congrats");
                tvStatus.setText("Order has been successfully placed.");

            }else{
                imageStatusSuccess.setVisibility(View.GONE);
                imageViewStatusFailure.setVisibility(View.VISIBLE);
                textViewStatusHeader.setText("Sorry");
                tvStatus.setText("There is some problem occurred.");

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

    private void deliverOrder(){

    }

    private void openInvoiceActivity(){

    }

}
