package com.shoppursshop.activities.settings;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.shoppursshop.R;
import com.shoppursshop.activities.NetworkBaseActivity;
import com.shoppursshop.activities.settings.profile.AddressActivity;
import com.shoppursshop.adapters.ShopOfferListAdapter;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.DialogAndToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FreeProductOfferActivity extends NetworkBaseActivity {

    private TextView tv_parent, tv_top_parent;
    private EditText edit_offer_name, edit_buying_product, edit_free_product, edit_buy_qty_product,
                     edit_free_qty_product, edit_offer_start_date, edit_offer_end_date;
    private RelativeLayout relative_footer_action;
    private ImageView iv_buy_camera, iv_free_camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_product_offer);
        initFooterAction(this);
        init();
    }

    private void init(){
        tv_top_parent = findViewById(R.id.text_left_label);
        tv_parent = findViewById(R.id.text_right_label);
        edit_offer_name = findViewById(R.id.edit_offer_name);
        edit_buying_product = findViewById(R.id.edit_buying_product);
        edit_free_product = findViewById(R.id.edit_free_product);
        edit_buy_qty_product = findViewById(R.id.edit_buy_qty_product);
        edit_free_qty_product = findViewById(R.id.edit_free_qty_product);
        edit_offer_start_date = findViewById(R.id.edit_offer_start_date);
        edit_offer_end_date = findViewById(R.id.edit_offer_end_date);
        relative_footer_action = findViewById(R.id.relative_footer_action);
        iv_buy_camera = findViewById(R.id.iv_buy_camera);
        iv_free_camera = findViewById(R.id.iv_free_camera);

        relative_footer_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createOffer();
            }
        });

        tv_top_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FreeProductOfferActivity.this, ShopOfferListAdapter.class));
                finish();
            }
        });
        tv_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FreeProductOfferActivity.this, CreateOfferActivity.class));
                finish();
            }
        });
    }

    private void createOffer() {
        String offer_name = edit_offer_name.getText().toString();
        String buying_product = edit_buying_product.getText().toString();
        String free_product = edit_free_product.getText().toString();
        String buy_qty_product = edit_buy_qty_product.getText().toString();
        String free_qty_product = edit_free_qty_product.getText().toString();
        String offer_start_date = edit_offer_start_date.getText().toString();
        String offer_end_date = edit_offer_end_date.getText().toString();

        if (TextUtils.isEmpty(offer_name)) {
            DialogAndToast.showDialog("Enter Offer Name", this);
            return;
        }
        if (TextUtils.isEmpty(buying_product)) {
            DialogAndToast.showDialog("Enter Buying Product", this);
            return;
        }
        if (TextUtils.isEmpty(free_product)) {
            DialogAndToast.showDialog("Enter Free Product", this);
            return;
        }
        if (TextUtils.isEmpty(buy_qty_product)) {
            DialogAndToast.showDialog("Enter Buy Quantity", this);
            return;
        }
        if (TextUtils.isEmpty(free_qty_product)) {
            DialogAndToast.showDialog("Enter Free Quantity", this);
            return;
        }
        if (TextUtils.isEmpty(offer_start_date)) {
            DialogAndToast.showDialog("Enter Offer Start Date", this);
            return;
        }
        if (TextUtils.isEmpty(offer_end_date)) {
            DialogAndToast.showDialog("Enter Offer End Date", this);
            return;
        }

        /*Map<String, String> params = new HashMap<>();
        params.put("address", address);
        params.put("country", country);
        params.put("state", state);
        params.put("city", city);
        params.put("pinCode", pincode);
        if (shopLatLng == null) {
            params.put("latitude", "0.0");
            params.put("longitude", "0.0");
        } else {
            params.put("latitude", "" + shopLatLng.latitude);
            params.put("longitude", "" + shopLatLng.longitude);
        }

        params.put("id", sharedPreferences.getString(Constants.USER_ID, ""));
        params.put("mobile", sharedPreferences.getString(Constants.MOBILE_NO, ""));
        params.put("dbName", sharedPreferences.getString(Constants.DB_NAME, ""));
        params.put("dbUserName", sharedPreferences.getString(Constants.DB_USER_NAME, ""));
        params.put("dbPassword", sharedPreferences.getString(Constants.DB_PASSWORD, ""));
        JSONArray dataArray = new JSONArray();
        JSONObject dataObject = new JSONObject(params);
        dataArray.put(dataObject);
        String url = getResources().getString(R.string.url) + Constants.UPDATE_ADDRESS;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST, url, new JSONObject(params), "updateAddress");*/
    }
}
