package com.shoppursshop.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.shoppursshop.R;
import com.shoppursshop.activities.settings.SettingActivity;
import com.shoppursshop.activities.settings.profile.DeliveryActivity;
import com.shoppursshop.models.SpinnerItem;
import com.shoppursshop.utilities.ConnectionDetector;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.DialogAndToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DeliveryAddressActivity extends NetworkBaseActivity {

    private LinearLayout linear_billing_address;
    private EditText edit_address;

    private EditText editAddress,editPincode;
    private Spinner spinnerCountry, spinnerState, spinnerCity;
    List<SpinnerItem> stateListObject, cityListObject, countryListObject;
    List<String> stateList, cityList, countryList;
    private ArrayAdapter<String> stateAdapter, cityAdapter, countryAdapter;
    private boolean isFirstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_address);
        initFooterAction(this);
        init();
    }
    private void init(){
        linear_billing_address = findViewById(R.id.linear_billing_address);
        edit_address = findViewById(R.id.edit_address);
        editPincode = findViewById(R.id.edit_pincode);
        RelativeLayout btnUpdate = findViewById(R.id.relative_footer_action);
        initSpinners();
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDeliveryAddress();
            }
        });
        TextView tv_top_parent = findViewById(R.id.text_left_label);
        tv_top_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void updateDeliveryAddress(){
        String addr = edit_address.getText().toString();
        String country = countryList.get(spinnerCountry.getSelectedItemPosition());
        String state  = stateList.get(spinnerState.getSelectedItemPosition());
        String city = cityList.get(spinnerCity.getSelectedItemPosition());
        String pincode = editPincode.getText().toString();

        if (TextUtils.isEmpty(addr)) {
            DialogAndToast.showDialog(getResources().getString(R.string.address_required),this);
            return;
        }

        if (TextUtils.isEmpty(pincode)) {
            DialogAndToast.showDialog(getResources().getString(R.string.pincode_required),this);
            return;
        }

        if (state.equals("Select State")) {
            DialogAndToast.showDialog("Please select your state", this);
            return;
        }
        if (city.equals("Select City")) {
            DialogAndToast.showDialog("Please select your city", this);
            return;
        }

        Intent intent = new Intent();
        intent.putExtra("address", addr);
        intent.putExtra("country", country);
        intent.putExtra("state", state);
        intent.putExtra("city", city);
        intent.putExtra("zip", pincode);
        intent.putExtra("latitude", sharedPreferences.getString(Constants.USER_LAT,"0.0"));
        intent.putExtra("longitude", sharedPreferences.getString(Constants.USER_LONG,"0.0"));
        intent.putExtra("distance", 20.3f);
        setResult(101, intent);
        finish();
    }

    private void initSpinners(){
        spinnerCountry = findViewById(R.id.spinner_country);
        spinnerState = findViewById(R.id.spinner_state);
        spinnerCity = findViewById(R.id.spinner_city);
        countryListObject = new ArrayList<>();
        countryList = new ArrayList<>();
        stateListObject = new ArrayList<>();
        stateList = new ArrayList<>();
        cityListObject = new ArrayList<>();
        cityList = new ArrayList<>();

        countryAdapter = new ArrayAdapter<String>(this, R.layout.simple_dropdown_list_item, countryList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getView(int position, View convertView,
                                ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(getResources().getColor(R.color.grey500));
                } else {
                    if(isDarkTheme){
                        tv.setTextColor(getResources().getColor(R.color.white));
                    }else{
                        tv.setTextColor(getResources().getColor(R.color.primary_text_color));
                    }

                }
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(isDarkTheme){
                    view.setBackgroundColor(getResources().getColor(R.color.dark_color));
                }else{
                    view.setBackgroundColor(getResources().getColor(R.color.white));
                }
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(getResources().getColor(R.color.grey500));
                } else {
                    if(isDarkTheme){
                        tv.setTextColor(getResources().getColor(R.color.white));
                    }else{
                        tv.setTextColor(getResources().getColor(R.color.primary_text_color));
                    }
                }
                tv.setPadding(20, 20, 20, 20);
                return view;
            }
        };

        spinnerCountry.setAdapter(countryAdapter);

        // stateList.add(0,"Select State");
        //  stateList.add("Delhi");
        //stateList.add("New Delhi");
        stateAdapter = new ArrayAdapter<String>(this, R.layout.simple_dropdown_list_item, stateList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getView(int position, View convertView,
                                ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(getResources().getColor(R.color.grey500));
                } else {
                    if(isDarkTheme){
                        tv.setTextColor(getResources().getColor(R.color.white));
                    }else{
                        tv.setTextColor(getResources().getColor(R.color.primary_text_color));
                    }
                }
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                if(isDarkTheme){
                    view.setBackgroundColor(getResources().getColor(R.color.dark_color));
                }else{
                    view.setBackgroundColor(getResources().getColor(R.color.white));
                }
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(getResources().getColor(R.color.grey400));
                } else {
                    if(isDarkTheme){
                        tv.setTextColor(getResources().getColor(R.color.white));
                    }else{
                        tv.setTextColor(getResources().getColor(R.color.primary_text_color));
                    }
                }
                tv.setPadding(20, 20, 20, 20);
                return view;
            }
        };

        spinnerState.setAdapter(stateAdapter);

        cityList.add(0, "Select City");
        // cityList.add("Delhi");
        //stateList.add("New Delhi");
        cityAdapter = new ArrayAdapter<String>(this, R.layout.simple_dropdown_list_item, cityList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getView(int position, View convertView,
                                ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(getResources().getColor(R.color.grey500));
                } else {
                    if(isDarkTheme){
                        tv.setTextColor(getResources().getColor(R.color.white));
                    }else{
                        tv.setTextColor(getResources().getColor(R.color.primary_text_color));
                    }
                }
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                if(isDarkTheme){
                    view.setBackgroundColor(getResources().getColor(R.color.dark_color));
                }else{
                    view.setBackgroundColor(getResources().getColor(R.color.white));
                }
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(getResources().getColor(R.color.grey400));
                } else {
                    if(isDarkTheme){
                        tv.setTextColor(getResources().getColor(R.color.white));
                    }else{
                        tv.setTextColor(getResources().getColor(R.color.primary_text_color));
                    }
                }
                tv.setPadding(20, 20, 20, 20);
                return view;
            }
        };

        spinnerCity.setAdapter(cityAdapter);

        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    getStates(countryListObject.get(i).getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    getCities(stateListObject.get(i).getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (ConnectionDetector.isNetworkAvailable(this)) {
            getCountries();
        }
    }

    private void getCountries() {
        String url = getResources().getString(R.string.url) + Constants.GET_COUNTRIES;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST, url, new JSONObject(), "countries");
    }

    private void getStates(String countryId) {
        String url = getResources().getString(R.string.url) + Constants.GET_STATES + countryId;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST, url, new JSONObject(), "states");
    }

    private void getCities(String stateId) {
        String url = getResources().getString(R.string.url) + Constants.GET_CITIES + stateId;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST, url, new JSONObject(), "cities");
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {

        try {
            if (apiName.equals("countries")) {

                if (response.getBoolean("status")) {
                    JSONArray dataArray = response.getJSONArray("result");
                    JSONObject jsonObject = null;
                    SpinnerItem item = null;
                    int len = dataArray.length();
                    countryListObject.clear();
                    countryList.clear();

                    for (int i = 0; i < len; i++) {
                        jsonObject = dataArray.getJSONObject(i);
                        item = new SpinnerItem();
                        item.setId("" + jsonObject.getInt("id"));
                        item.setName(jsonObject.getString("name"));
                        countryListObject.add(item);
                        countryList.add(item.getName());
                    }
                    countryList.add(0, "Select Country");
                    countryListObject.add(0, new SpinnerItem());
                    countryAdapter.notifyDataSetChanged();

                    if (countryList.size() == 2) {
                        spinnerCountry.setSelection(1);
                    }

                } else {
                    DialogAndToast.showDialog(response.getString("message"), this);
                }
            } else if (apiName.equals("states")) {

                if (response.getBoolean("status")) {
                    JSONArray dataArray = response.getJSONArray("result");
                    JSONObject jsonObject = null;
                    SpinnerItem item = null;
                    int len = dataArray.length();
                    stateListObject.clear();
                    stateList.clear();

                    for (int i = 0; i < len; i++) {
                        jsonObject = dataArray.getJSONObject(i);
                        item = new SpinnerItem();
                        item.setId("" + jsonObject.getInt("id"));
                        item.setName(jsonObject.getString("name"));
                        stateListObject.add(item);
                        stateList.add(item.getName());
                    }
                    stateList.add(0, "Select State");
                    stateListObject.add(0, new SpinnerItem());
                    stateAdapter.notifyDataSetChanged();

                    if(isFirstTime){
                       // setStateValue();
                    }

                } else {
                    DialogAndToast.showDialog(response.getString("message"), this);
                }
            }else if (apiName.equals("cities")) {

                if (response.getBoolean("status")) {
                    JSONArray dataArray = response.getJSONArray("result");
                    JSONObject jsonObject = null;
                    SpinnerItem item = null;
                    int len = dataArray.length();
                    cityListObject.clear();
                    cityList.clear();

                    for (int i = 0; i < len; i++) {
                        jsonObject = dataArray.getJSONObject(i);
                        item = new SpinnerItem();
                        item.setId("" + jsonObject.getInt("id"));
                        item.setName(jsonObject.getString("name"));
                        cityListObject.add(item);
                        cityList.add(item.getName());
                    }
                    cityList.add(0, "Select City");
                    cityListObject.add(0, new SpinnerItem());
                    cityAdapter.notifyDataSetChanged();

                    if(isFirstTime){
                     //   setCityValue();
                    }

                } else {
                    DialogAndToast.showDialog(response.getString("message"), this);
                }
            }/*else {
                if(response.getBoolean("status")){
                    editor.putString(Constants.ADDRESS,editAddress.getText().toString());
                    editor.putString(Constants.ZIP,editPincode.getText().toString());
                    editor.putString(Constants.COUNTRY,countryList.get(spinnerCountry.getSelectedItemPosition()));
                    editor.putString(Constants.STATE,stateList.get(spinnerState.getSelectedItemPosition()));
                    editor.putString(Constants.CITY,cityList.get(spinnerCity.getSelectedItemPosition()));
                    if(shopLatLng != null){
                        editor.putLong(Constants.USER_LAT,(long) shopLatLng.latitude);
                        editor.putLong(Constants.USER_LONG,(long)shopLatLng.longitude);
                    }
                    editor.commit();
                    DialogAndToast.showToast(response.getString("message"),this);
                }else{
                    DialogAndToast.showDialog(response.getString("message"),this);
                }
            }*/
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setStateValue(){
        int i = 0;
        for(String state: stateList){
            if(state.equals(sharedPreferences.getString(Constants.STATE,""))){
                spinnerState.setSelection(i);
                break;
            }
            i++;
        }
    }

    private void setCityValue(){
        int i = 0;
        for(String state: cityList){
            if(state.equals(sharedPreferences.getString(Constants.CITY,""))){
                spinnerCity.setSelection(i);
                break;
            }
            i++;
        }
        isFirstTime = false;
    }

}
