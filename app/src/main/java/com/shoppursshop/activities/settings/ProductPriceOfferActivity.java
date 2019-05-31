package com.shoppursshop.activities.settings;

import android.graphics.drawable.GradientDrawable;
import android.net.Network;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.shoppursshop.R;
import com.shoppursshop.activities.BaseActivity;
import com.shoppursshop.activities.NetworkBaseActivity;
import com.shoppursshop.models.MyProductItem;

import java.util.ArrayList;
import java.util.List;

public class ProductPriceOfferActivity extends NetworkBaseActivity {

    private RecyclerView recyclerView;
    private ProductPriceOfferAdapter priceOfferAdapter;
    private List<MyProductItem> myProductItems;
    private Button add_combo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_price_offer);
        initFooterAction(this);
        init();
    }

    private void init(){
        myProductItems = new ArrayList<>();
        MyProductItem productItem = new MyProductItem();
        productItem.setProdName("");
        myProductItems.add(productItem);

        add_combo = findViewById(R.id.add_combo);
        ((GradientDrawable)add_combo.getBackground()).setColor(colorTheme);
        add_combo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyProductItem productItem = new MyProductItem();
                productItem.setProdName("");
                myProductItems.add(productItem);
                priceOfferAdapter.notifyDataSetChanged();
            }
        });


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        priceOfferAdapter=new ProductPriceOfferAdapter(this,myProductItems);
        recyclerView.setAdapter(priceOfferAdapter);
    }
}
