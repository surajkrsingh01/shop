package com.shoppursshop.activities.settings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.shoppursshop.R;
import com.shoppursshop.activities.NetworkBaseActivity;
import com.shoppursshop.adapters.ShopOfferListAdapter;
import com.shoppursshop.adapters.ShopOfferTypeAdapter;

import java.util.ArrayList;
import java.util.List;

public class CreateOfferActivity extends NetworkBaseActivity {

    private RecyclerView recycler_offer_type;
    private ShopOfferTypeAdapter myItemAdapter;
    private List<String> itemList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_offer);
        getOfferTypes();
        init();
    }

    private void getOfferTypes(){
        itemList = new ArrayList<>();
        itemList.add("Free Product Offer");
        itemList.add("Combo Product Offer");
        itemList.add("Product Price Offer");
    }

    private void init(){
        recycler_offer_type = findViewById(R.id.recycler_offer_type);
        recycler_offer_type.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recycler_offer_type.setLayoutManager(layoutManager);
        recycler_offer_type.setItemAnimator(new DefaultItemAnimator());
        myItemAdapter=new ShopOfferTypeAdapter(this,itemList);
        recycler_offer_type.setAdapter(myItemAdapter);
    }
}
