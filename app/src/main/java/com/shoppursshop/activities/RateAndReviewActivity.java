package com.shoppursshop.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.shoppursshop.R;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.DialogAndToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RateAndReviewActivity extends NetworkBaseActivity {

    private String orderNumber;
    private String custCode;
    private TextView text_action;
    private RelativeLayout relative_footer_action;
    private RatingBar ratingBar;
    private EditText et_review;
    private String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_and_review);
        flag = getIntent().getStringExtra("flag");
        orderNumber = getIntent().getStringExtra("orderNumber");
        custCode = getIntent().getStringExtra("custCode");
        text_action = findViewById(R.id.text_action);
        et_review = findViewById(R.id.et_review);
        ratingBar = findViewById(R.id.ratingBar);
        relative_footer_action = findViewById(R.id.relative_footer_action);
        relative_footer_action.setBackgroundColor(colorTheme);
        text_action.setText("Submit");

        relative_footer_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //float rating = ratingBar.getRating();
                submitRatingReview();
            }
        });

    }

    private void submitRatingReview(){
        float rating = ratingBar.getRating();
        String review = et_review.getText().toString();
        if(TextUtils.isEmpty(review)){
            et_review.setError("Enter Your Review");
            et_review.setFocusable(true);
            return;
        }else {
            Map<String,String> params=new HashMap<>();
            String url=getResources().getString(R.string.url)+Constants.SUBMIT_FEEDBACK;
            params.put("rating", String.valueOf(rating));
            params.put("reviewMessage", review);
            params.put("custCode",custCode);
            params.put("number", orderNumber);
            params.put("dbName", sharedPreferences.getString(Constants.DB_NAME, ""));
            params.put("dbUserName", sharedPreferences.getString(Constants.DB_USER_NAME, ""));
            params.put("dbPassword", sharedPreferences.getString(Constants.DB_PASSWORD, ""));
            Log.d(TAG, params.toString());
            showProgress(true);
            jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"submitRatingReview");
        }
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {
        showProgress(false);
        try {
            Log.d("response", response.toString());
            if (apiName.equals("submitRatingReview")) {
                if (response.getString("status").equals("true") || response.getString("status").equals(true)) {
                    //  JSONObject dataObject = response.getJSONObject("result");
                    showMyDialog(response.getString("message"));
                } else {
                    showMyDialog(response.getString("message"));
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
            DialogAndToast.showToast(getResources().getString(R.string.json_parser_error)+e.toString(),RateAndReviewActivity.this);
        }
    }

    @Override
    public void onDialogPositiveClicked() {
        if(flag.equals("cash")){
            Intent intent = new Intent(RateAndReviewActivity.this,InvoiceActivity.class);
            intent.putExtra("orderNumber",orderNumber);
            startActivity(intent);
        }
        finish();
    }
}
