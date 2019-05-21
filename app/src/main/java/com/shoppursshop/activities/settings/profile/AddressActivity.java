package com.shoppursshop.activities.settings.profile;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.shoppursshop.R;
import com.shoppursshop.activities.NetworkBaseActivity;
import com.shoppursshop.activities.RegisterActivity;
import com.shoppursshop.activities.settings.ProfileActivity;
import com.shoppursshop.activities.settings.SettingActivity;
import com.shoppursshop.interfaces.OnLocationReceivedListener;
import com.shoppursshop.location.GpsLocationProvider;
import com.shoppursshop.location.NetworkSensor;
import com.shoppursshop.models.SpinnerItem;
import com.shoppursshop.utilities.ConnectionDetector;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.DialogAndToast;
import com.shoppursshop.utilities.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressActivity extends NetworkBaseActivity implements OnMapReadyCallback, OnLocationReceivedListener {

    private final float ZOOM = 15f;

    private GoogleMap mMap;
    private Marker marker;
    private LatLng shopLatLng;

    private EditText editAddress,editPincode;
    private Spinner spinnerCountry, spinnerState, spinnerCity;
    List<SpinnerItem> stateListObject, cityListObject, countryListObject;
    List<String> stateList, cityList, countryList;
    private ArrayAdapter<String> stateAdapter, cityAdapter, countryAdapter;
    private TextView tv_parent, tv_top_parent;

    private Button btnGetLocation;
    private boolean isFirstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        initFooterAction(this);
        setToolbarTheme();
    }

    private void init(){

        double latitude = (double) sharedPreferences.getLong(Constants.LATITUDE,0);
        double longitude = (double) sharedPreferences.getLong(Constants.LONGITUDE,0);

        if(latitude != 0d){
            shopLatLng = new LatLng(latitude,longitude);
        }

        btnGetLocation = findViewById(R.id.btn_get_location);
        //int colorTheme = sharedPreferences.getInt(Constants.COLOR_THEME,0);
        RelativeLayout btnUpdate = findViewById(R.id.relative_footer_action);
        btnUpdate.setBackgroundColor(colorTheme);
        btnGetLocation.setBackgroundColor(colorTheme);
        editAddress = findViewById(R.id.edit_address);
        editPincode = findViewById(R.id.edit_pincode);
        editAddress.setText(sharedPreferences.getString(Constants.ADDRESS,""));
        editPincode.setText(sharedPreferences.getString(Constants.ZIP,""));
        initSpinners();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Utility.checkLocationPermission(AddressActivity.this)){
                    createLocationRequest();
                }
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAddress();
            }
        });

        tv_top_parent = findViewById(R.id.text_left_label);
        tv_parent = findViewById(R.id.text_right_label);
        tv_top_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddressActivity.this, SettingActivity.class));
                finish();
            }
        });
        tv_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddressActivity.this, ProfileActivity.class));
                finish();
            }
        });
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (googleMap == null) {
            DialogAndToast.showToast("Map is not available...",AddressActivity.this);
        }
        // Add a marker in Sydney and move the camera
        // LatLng sydney = new LatLng(-34, 151);
        // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //  mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        //   googleMap.setMyLocationEnabled(true); // false to disable
        mMap.getUiSettings().setZoomControlsEnabled(false); // true to enable
        mMap.setTrafficEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if(shopLatLng != null){
            updateMarker(shopLatLng);
        }else{
            if(Utility.checkLocationPermission(AddressActivity.this)){
                createLocationRequest();
            }
        }
    }

    protected void createLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getLocation(true);
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(AddressActivity.this, 1);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });
    }

    private void getLocation(boolean isPermissionGranted){
        if(isPermissionGranted){
            GpsLocationProvider gpsLocationProvider = new GpsLocationProvider(this,true);
            gpsLocationProvider.setOnLocationReceivedListener(this);
            gpsLocationProvider.buildLocationProviderInstance();
            gpsLocationProvider.startLocationUpdates();
        }else{
            NetworkSensor networkSensor = new NetworkSensor(this);
            networkSensor.setOnLocationReceived(this);
            try {
                if (Utility.checkLocationPermission(this)) {
                    networkSensor.getLocation();
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.i("MainActivity", "Error " + e.getMessage());
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    createLocationRequest();
                } else {

                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case 1:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        getLocation(true);
                    case Activity.RESULT_CANCELED:
                        break;
                }
                break;

        }

    }

    @Override
    public void onLocationReceived(Location location) {
            // Add a marker in Sydney and move the camera
         LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
         shopLatLng = latLng;
         updateMarker(latLng);
    }

    private void updateMarker(LatLng latLng){
        if(marker == null){
            marker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    .title("Current Location"));

            mMap.getUiSettings().setZoomControlsEnabled(false); // true to enable
            mMap.setTrafficEnabled(true);
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        }else{
            marker.setPosition(latLng);
        }

        if(mMap.getCameraPosition().zoom < ZOOM){
            // Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng)      // Sets the center of the map to Mountain View
                    .zoom(ZOOM)                   // Sets the zoom
                    // .bearing(90)                // Sets the orientation of the camera to east
                    // .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }else {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
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

    private void updateAddress(){
        String address = editAddress.getText().toString();
        String pincode = editPincode.getText().toString();
        String country = countryList.get(spinnerCountry.getSelectedItemPosition());
        String state = stateList.get(spinnerState.getSelectedItemPosition());
        String city = cityList.get(spinnerCity.getSelectedItemPosition());

        if (TextUtils.isEmpty(address)) {
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
        if (state.equals("Select City")) {
            DialogAndToast.showDialog("Please select your city", this);
            return;
        }

        Map<String,String> params=new HashMap<>();
        params.put("address",address);
        params.put("country",country);
        params.put("state",state);
        params.put("city",city);
        params.put("pinCode",pincode);
        if(shopLatLng == null){
            params.put("latitude","0.0");
            params.put("longitude","0.0");
        }else{
            params.put("latitude",""+shopLatLng.latitude);
            params.put("longitude",""+shopLatLng.longitude);
        }

        params.put("id",sharedPreferences.getString(Constants.USER_ID,""));
        params.put("mobile",sharedPreferences.getString(Constants.MOBILE_NO,""));
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        JSONArray dataArray = new JSONArray();
        JSONObject dataObject = new JSONObject(params);
        dataArray.put(dataObject);
        String url=getResources().getString(R.string.url)+Constants.UPDATE_ADDRESS;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"updateAddress");
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
                        setStateValue();
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
                        setCityValue();
                    }

                } else {
                    DialogAndToast.showDialog(response.getString("message"), this);
                }
            }else {
                if(response.getBoolean("status")){
                    editor.putString(Constants.ADDRESS,editAddress.getText().toString());
                    editor.putString(Constants.ZIP,editPincode.getText().toString());
                    editor.putString(Constants.COUNTRY,countryList.get(spinnerCountry.getSelectedItemPosition()));
                    editor.putString(Constants.STATE,stateList.get(spinnerState.getSelectedItemPosition()));
                    editor.putString(Constants.CITY,cityList.get(spinnerCity.getSelectedItemPosition()));
                    if(shopLatLng != null){
                        editor.putLong(Constants.LATITUDE,(long) shopLatLng.latitude);
                        editor.putLong(Constants.LONGITUDE,(long)shopLatLng.longitude);
                    }
                    editor.commit();
                    DialogAndToast.showToast(response.getString("message"),this);
                }else{
                    DialogAndToast.showDialog(response.getString("message"),this);
                }
            }
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
