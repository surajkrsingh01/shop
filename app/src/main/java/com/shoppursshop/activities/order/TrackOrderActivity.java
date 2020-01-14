package com.shoppursshop.activities.order;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.shoppursshop.R;
import com.shoppursshop.activities.MainActivity;
import com.shoppursshop.activities.base.NetworkBaseActivity;
import com.shoppursshop.activities.customers.CustomerAddressActivity;
import com.shoppursshop.models.ShoppursPartner;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.DialogAndToast;

import java.util.ArrayList;

public class TrackOrderActivity extends NetworkBaseActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private final float ZOOM = 15f;

    private LatLng shopLatLng,custLatLng,orderLatLng;
    private String shopName,shopAddress,shopMobile,custName,
            custAddress,custMobile;

    private TextView tvPartnerName,tvPartnerMobile;
    private ShoppursPartner partner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order);

        init();
    }

    private void init(){
        partner = (ShoppursPartner)getIntent().getSerializableExtra("partner");
        tvPartnerName = findViewById(R.id.tvPartnerName);
        tvPartnerMobile = findViewById(R.id.tvPartnerMobile);

        tvPartnerName.setText(partner.getName());
        tvPartnerMobile.setText(partner.getMobile());

        double latitude = Double.parseDouble(getIntent().getStringExtra("custLat"));
        double longitude = Double.parseDouble(getIntent().getStringExtra("custLong"));
        if(latitude != 0d){
            custLatLng = new LatLng(latitude,longitude);
        }

        latitude = Double.parseDouble(sharedPreferences.getString(Constants.USER_LAT,""));
        longitude = Double.parseDouble(sharedPreferences.getString(Constants.USER_LONG,""));
        if(latitude != 0d){
            shopLatLng = new LatLng(latitude,longitude);
        }

        shopName = sharedPreferences.getString(Constants.FULL_NAME,"");
        shopMobile = sharedPreferences.getString(Constants.MOBILE_NO,"");
        shopAddress = sharedPreferences.getString(Constants.ADDRESS,"");

        custName = getIntent().getStringExtra("custName");
        custMobile = getIntent().getStringExtra("custMobile");
        custAddress = getIntent().getStringExtra("custAddress");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        TextView text_header_1 = findViewById(R.id.text_header_1);
        TextView text_header_1_2 = findViewById(R.id.text_header_1_2);

        text_header_1_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        text_header_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrackOrderActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (googleMap == null) {
            DialogAndToast.showToast("Map is not available...", TrackOrderActivity.this);
        }
        mMap.getUiSettings().setZoomControlsEnabled(false); // true to enable
        mMap.setTrafficEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                LinearLayout info = new LinearLayout(TrackOrderActivity.this);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(TrackOrderActivity.this);
                title.setTextColor(getResources().getColor(R.color.primary_text_color));
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());
                info.addView(title);

                if(!TextUtils.isEmpty(marker.getSnippet())){
                    TextView snippet = new TextView(TrackOrderActivity.this);
                    snippet.setTextColor(getResources().getColor(R.color.secondary_text_color));
                    snippet.setGravity(Gravity.CENTER);
                    snippet.setText(marker.getSnippet());
                    info.addView(snippet);
                }
                return info;
            }
        });

        trackOrder();
    }


    private void trackOrder(){
        MarkerOptions options = new MarkerOptions();
        ArrayList<MarkerOptions> latlngs = new ArrayList<>();

        options.position(shopLatLng);
        options.title(shopName);
        options.snippet(shopAddress+"/n"+shopMobile);
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        mMap.addMarker(options);

        options.position(custLatLng);
        options.title(custName);
        options.snippet(custAddress+"/n"+custMobile);
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mMap.addMarker(options);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(shopLatLng);
        builder.include(custLatLng);

        LatLngBounds bounds = builder.build();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.30); // offset from edges of the map 10% of screen
        //int padding = 40; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height,padding);
        // mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 70));
        mMap.moveCamera(cu);
    }
}
