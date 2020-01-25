package com.shoppursshop.activities.settings;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.shoppursshop.R;
import com.shoppursshop.activities.base.BaseActivity;
import com.shoppursshop.activities.settings.profile.AddressActivity;
import com.shoppursshop.activities.settings.profile.BankDetailsActivity;
import com.shoppursshop.activities.settings.profile.BasicProfileActivity;
import com.shoppursshop.activities.settings.profile.DeliveryActivity;
import com.shoppursshop.activities.settings.profile.KYCActivity;
import com.shoppursshop.adapters.SettingsAdapter;
import com.shoppursshop.interfaces.MyItemClickListener;
import com.shoppursshop.utilities.Constants;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends BaseActivity implements MyItemClickListener {

    private TextView tvName,tvAddress,tvMobile;
    private ImageView imageQrCode;

    private RecyclerView recyclerView;
    private List<String> itemList;
    private SettingsAdapter itemAdapter;
    private TextView tv_top_parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
        initFooter(this,4);
    }

    private void init(){
        tvName = findViewById(R.id.text_shop_name);
        tvAddress = findViewById(R.id.text_shop_address);
        tvMobile = findViewById(R.id.text_shop_mobile);
        imageQrCode = findViewById(R.id.image_qr_code);

        tvName.setText(sharedPreferences.getString(Constants.SHOP_NAME,""));
        tvAddress.setText(sharedPreferences.getString(Constants.ADDRESS,""));
        tvMobile.setText(sharedPreferences.getString(Constants.MOBILE_NO,""));

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        // requestOptions.override(Utility.dpToPx(150, context), Utility.dpToPx(150, context));
        requestOptions.centerCrop();
        requestOptions.skipMemoryCache(false);

        generateBarcode(sharedPreferences.getString(Constants.MOBILE_NO,""));

        /*Glide.with(this)
                .load(getResources().getString(R.string.base_image_url)+"/shops/"+
                        sharedPreferences.getString(Constants.SHOP_CODE,"")+"/qrcode.PNG")
                .apply(requestOptions)
                .into(imageQrCode);*/

        itemList = new ArrayList<>();
        itemList.add("Store Details");
        itemList.add("Address");
        itemList.add("Delivery");
        itemList.add("Bank Details");
        itemList.add("KYC Details");
        //itemList.add("Invite on Shoppurs");
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        itemAdapter=new SettingsAdapter(this,itemList);
        itemAdapter.setMyItemClickListener(this);
        recyclerView.setAdapter(itemAdapter);

        tv_top_parent = findViewById(R.id.text_right_label);
        tv_top_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, SettingActivity.class));
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            super.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClicked(int position) {
        String name = itemList.get(position);
        if(name.equals("Store Details")){
            Intent intent = new Intent(this, BasicProfileActivity.class);
            startActivity(intent);
        }else if(name.equals("Address")){
            Intent intent = new Intent(this, AddressActivity.class);
            startActivity(intent);
        }else if(name.equals("Delivery")){
            Intent intent = new Intent(this, DeliveryActivity.class);
            startActivity(intent);
        }else if(name.equals("Bank Details")){
            Intent intent = new Intent(this, BankDetailsActivity.class);
            startActivity(intent);
        }else if(name.equals("KYC Details")){
            Intent intent = new Intent(this, KYCActivity.class);
            startActivity(intent);
        }
    }

    private void generateBarcode(String text){
        Log.i(TAG,"Generating qrcode...");
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        // requestOptions.override(Utility.dpToPx(150, context), Utility.dpToPx(150, context));
        requestOptions.centerCrop();
        requestOptions.skipMemoryCache(false);

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,200,100);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap barcodeImage = barcodeEncoder.createBitmap(bitMatrix);
            imageQrCode.setImageBitmap(barcodeImage);
            Glide.with(this)
                    .load(barcodeImage)
                    .apply(requestOptions)
                    .into(imageQrCode);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
