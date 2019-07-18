package com.shoppursshop.activities;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AddressComponent;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.shoppursshop.R;
import com.shoppursshop.activities.settings.SettingActivity;
import com.shoppursshop.activities.settings.profile.AddressActivity;
import com.shoppursshop.activities.settings.profile.DeliveryActivity;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class DeliveryAddressActivity extends NetworkBaseActivity implements OnMapReadyCallback, OnLocationReceivedListener {

    private final float ZOOM = 15f;
    private final int SEARCH_LOCATION = 3;

    private GoogleMap mMap;
    private Marker shopMarker,deliveryMarker;
    private LatLng shopLatLng,deliveryLatLng;

    private EditText editAddress,editCountry,editState,editCity,editPincode;
    private String country,state,city,pin;
    private double distance;
    private TextView tv_parent;
    private ImageView ivSearch;

    private Button btnGetLocation;
    private String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_address);
        initFooterAction(this);
        init();
       // setToolbarTheme();
    }
    private void init(){
        // Initialize Places.
        Places.initialize(getApplicationContext(), sharedPreferences.getString(Constants.GOOGLE_MAP_API_KEY,""));
        double latitude = Double.parseDouble(sharedPreferences.getString(Constants.USER_LAT,"0.0"));
        double longitude = Double.parseDouble(sharedPreferences.getString(Constants.USER_LONG,"0.0"));
        editPincode = findViewById(R.id.edit_pincode);
        ivSearch = findViewById(R.id.image_search);
        editAddress = findViewById(R.id.edit_address);
        editCountry = findViewById(R.id.edit_country);
        editState = findViewById(R.id.edit_state);
        editCity = findViewById(R.id.edit_city);
        editPincode = findViewById(R.id.edit_pincode);
       /* editAddress.setText(sharedPreferences.getString(Constants.ADDRESS,""));
        editCountry.setText(sharedPreferences.getString(Constants.COUNTRY,""));
        editState.setText(sharedPreferences.getString(Constants.STATE,""));
        editCity.setText(sharedPreferences.getString(Constants.CITY,""));
        editPincode.setText(sharedPreferences.getString(Constants.ZIP,""));*/
        if(latitude != 0d){
            shopLatLng = new LatLng(latitude,longitude);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        RelativeLayout btnUpdate = findViewById(R.id.relative_footer_action);
        btnUpdate.setBackgroundColor(colorTheme);
        btnGetLocation = findViewById(R.id.btn_get_location);
        if(colorTheme == getResources().getColor(R.color.white)){
            btnGetLocation.setTextColor(getResources().getColor(R.color.primary_text_color));
        }
        btnGetLocation.setBackgroundColor(colorTheme);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDeliveryAddress();
            }
        });

        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchPlaces();
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (googleMap == null) {
            DialogAndToast.showToast("Map is not available...",DeliveryAddressActivity.this);
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
            updateShopMarker(shopLatLng);
        }else{
            if(Utility.checkLocationPermission(DeliveryAddressActivity.this)){
                createLocationRequest();
            }
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                // openDialog();
            }
        });
    }

    private void updateDeliveryAddress(){
        String address = editAddress.getText().toString();
        pin = editPincode.getText().toString();
        country = editCountry.getText().toString();
        state = editState.getText().toString();
        city = editCity.getText().toString();

        if (TextUtils.isEmpty(address)) {
            DialogAndToast.showDialog(getResources().getString(R.string.address_required),this);
            return;
        }

        if (TextUtils.isEmpty(pin)) {
            DialogAndToast.showDialog(getResources().getString(R.string.pincode_required),this);
            return;
        }

        if (TextUtils.isEmpty(state)) {
            DialogAndToast.showDialog("Please enter your state", this);
            return;
        }
        if (TextUtils.isEmpty(city)) {
            DialogAndToast.showDialog("Please enter your city", this);
            return;
        }

        Intent intent = new Intent();
        intent.putExtra("address",address);
        intent.putExtra("country",country);
        intent.putExtra("state",state);
        intent.putExtra("city",city);
        intent.putExtra("zip",pin);
        intent.putExtra("latitude",deliveryLatLng.latitude);
        intent.putExtra("longitude",deliveryLatLng.longitude);
        intent.putExtra("distance", distance);
        setResult(-1,intent);
        finish();
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
                        resolvable.startResolutionForResult(DeliveryAddressActivity.this, 1);
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

        if (requestCode == 1) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    getLocation(true);
                case Activity.RESULT_CANCELED:
                    break;
            }
        }else if (requestCode == SEARCH_LOCATION) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Place: " + place.getName()
                        + ", " + place.getId()+ ", " + place.getAddress());
                List<AddressComponent> componentList = place.getAddressComponents().asList();
                for(AddressComponent component : componentList){
                    Log.i(TAG,"component "+component.getName());
                    List<String> typeList = component.getTypes();
                    for(String type : typeList){
                        Log.i(TAG,"type "+type);
                        if(type.equals("country")){
                            country = component.getName();
                            editCountry.setText(country);
                        }else if(type.equals("administrative_area_level_1")){
                            state = component.getName();
                            editState.setText(state);
                        }else if(type.equals("locality")|| type.equals("neighborhood")){
                            city = component.getName();
                            editCity.setText(city);
                        }else if(type.equals("postal_code")){
                            pin = component.getName();
                            editPincode.setText(pin);
                        }
                    }
                }
                editAddress.setText(place.getAddress());
                deliveryLatLng = place.getLatLng();
                getDistance();
                updateDeliveryMarker(deliveryLatLng);
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }

    }

    @Override
    public void onLocationReceived(Location location) {
        Log.i(TAG,"Location is received.");
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        shopLatLng = latLng;
       // setLocationComponents();
        updateShopMarker(latLng);
    }

    private void setLocationComponents(){
        Log.i(TAG,"Getting address components...");
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(shopLatLng.latitude, shopLatLng.longitude, 1);
        } catch (IOException ioException) {
            Log.e(TAG, ioException.getMessage());
        } catch (IllegalArgumentException illegalArgumentException) {
            Log.e(TAG, illegalArgumentException.getMessage());
        }

        if(addresses.size() > 0){
            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<String>();
            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }
            editAddress.setText(address.getAddressLine(0));
            if(address.getCountryName() != null)
                editCountry.setText(address.getCountryName());
            if(address.getAdminArea() != null)
                editState.setText(address.getAdminArea());
            if(address.getLocality() != null)
                editCity.setText(address.getLocality());
            if(address.getPostalCode() != null)
                editPincode.setText(address.getPostalCode());
        }else{
            Log.e(TAG, "There is some problem in fetching address.");
        }
    }

    private void updateShopMarker(LatLng latLng){
        if(shopMarker == null){
            shopMarker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    .title("Current Location"));

            mMap.getUiSettings().setZoomControlsEnabled(false); // true to enable
            mMap.setTrafficEnabled(true);
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        }else{
            shopMarker.setPosition(latLng);
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

    private void updateDeliveryMarker(LatLng latLng){

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        if(deliveryMarker == null){
            deliveryMarker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    .title("Delivery Location"));

            mMap.getUiSettings().setZoomControlsEnabled(false); // true to enable
            mMap.setTrafficEnabled(true);
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        }else{
            deliveryMarker.setPosition(latLng);
        }

        builder.include(shopMarker.getPosition());
        builder.include(deliveryMarker.getPosition());
        LatLngBounds bounds = builder.build();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = findViewById(R.id.map).getHeight();
        int padding = (int) (height * 0.18); // offset from edges of the map 10% of screen
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height,padding);
        mMap.moveCamera(cu);
      /*  if(mMap.getCameraPosition().zoom < ZOOM){
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
        }*/
    }

    public void searchPlaces(){

        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);
// Set the fields to specify which types of place data to return.
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.ADDRESS,
                Place.Field.LAT_LNG,Place.Field.ADDRESS_COMPONENTS);


        Intent intent =
                new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN,fields)
                        .build(this);
        startActivityForResult(intent, SEARCH_LOCATION);

    }

    public void getDistance(){
        distance = 0;
        progressDialog.setMessage("Loading...");
        showProgress(true);
        String url="https://maps.googleapis.com/maps/api/distancematrix/json?" +
                "units=imperial&origins="+ shopLatLng.latitude + "," + shopLatLng.longitude+
                "&destinations="+ deliveryLatLng.latitude + "," + deliveryLatLng.longitude+"&key="+sharedPreferences.getString(Constants.GOOGLE_MAP_API_KEY,"");

        jsonObjectApiRequest(Request.Method.GET,url,new JSONObject(),"distance");
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {
        try {
            if(response.getString("status").equals("OK")){
                JSONArray jsonArray = response.getJSONArray("rows");
                if(jsonArray.length() > 0){
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    JSONArray jsonArray1 = jsonObject.getJSONArray("elements");
                    if(jsonArray1.length() > 0){
                        JSONObject jsonObject1 = jsonArray1.getJSONObject(0);
                        if(jsonObject1.getString("status").equals("OK")){
                            distance = jsonObject1.getJSONObject("distance").getDouble("value")/1000;
                            Log.i(TAG,"Distance "+distance+" Kms");
                        }
                    }else {
                        distance = -1;
                        errorInCalculatingDistance();
                    }

                }else {
                    distance = -1;
                    errorInCalculatingDistance();
                }

            }else {
                distance = -1;
                errorInCalculatingDistance();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void errorInCalculatingDistance(){
        distance = 0;
        Log.i("Customer feed: ", "Error in calculating distance.");
    }
}
