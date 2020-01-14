package com.shoppursshop.activities.customers;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import androidx.appcompat.widget.Toolbar;

import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shoppursshop.R;
import com.shoppursshop.activities.base.NetworkBaseActivity;
import com.shoppursshop.utilities.DialogAndToast;

public class CustomerAddressActivity extends NetworkBaseActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private final float ZOOM = 15f;
    private TextView tv_parent, tv_top_parent;
    private LatLng shopLatLng;
    private String address,mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_address);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
    }

    private void init(){

        tv_top_parent = findViewById(R.id.text_left_label);
        tv_parent = findViewById(R.id.text_right_label);

        Intent intent = getIntent();
        address = getIntent().getStringExtra("address");
        mobile = getIntent().getStringExtra("mobile");
        double latitude = Double.parseDouble(intent.getStringExtra("latitude"));
        double longitude = Double.parseDouble(intent.getStringExtra("longitude"));
        if(latitude != 0d){
            shopLatLng = new LatLng(latitude,longitude);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
            DialogAndToast.showToast("Map is not available...",CustomerAddressActivity.this);
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
        }
    }

    private void updateMarker(LatLng latLng){

        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(getIntent().getStringExtra("name"))
                .snippet(address+"\n"+mobile));

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                LinearLayout info = new LinearLayout(CustomerAddressActivity.this);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(CustomerAddressActivity.this);
                title.setTextColor(getResources().getColor(R.color.primary_text_color));
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(CustomerAddressActivity.this);
                snippet.setTextColor(getResources().getColor(R.color.secondary_text_color));
                snippet.setGravity(Gravity.CENTER);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });

        mMap.getUiSettings().setZoomControlsEnabled(false); // true to enable
        mMap.setTrafficEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

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
}
