package com.shoppursshop.activities.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.shoppursshop.R;
import com.shoppursshop.activities.NetworkBaseActivity;
import com.shoppursshop.adapters.ProductAdapter;
import com.shoppursshop.adapters.SimpleItemAdapter;

import java.util.List;

public class MyProductListActivity extends NetworkBaseActivity {

    private RecyclerView recyclerView;
    private List<Object> itemList;
    private ProductAdapter itemAdapter;
    private int counter;
    private Menu menu;
    private TextView tv_top_parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_product_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setToolbarTheme();

        itemList = dbHelper.getProducts(limit,offset);
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        final RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        // staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        itemAdapter=new ProductAdapter(this,itemList,"productList");
        itemAdapter.setFlag("productList");
        itemAdapter.setSubCatName("");
        recyclerView.setAdapter(itemAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(isScroll){
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    Log.i(TAG,"visible "+visibleItemCount+" total "+totalItemCount);
                    pastVisibleItems = ((LinearLayoutManager)layoutManager).findLastVisibleItemPosition();
                    Log.i(TAG,"total visible "+(visibleItemCount+pastVisibleItems));

                    if (loading) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            loading = false;
                            offset = limit + offset;
                            List<Object> nextItemList = dbHelper.getProducts(limit,offset);
                            for(Object ob : nextItemList){
                                itemList.add(ob);
                               // itemAdapter.add(ob);
                            }
                            if(nextItemList.size() < limit){
                                isScroll = false;
                            }
                            if(nextItemList.size() > 0){
                                recyclerView.post(new Runnable() {
                                    public void run() {
                                        itemAdapter.notifyItemRangeInserted(offset,limit);
                                        loading = true;
                                    }
                                });
                                Log.d(TAG, "NEXT ITEMS LOADED");
                            }else{
                                Log.d(TAG, "NO ITEMS FOUND");
                            }

                        }
                    }
                }
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyProductListActivity.this, SyncProductActivity.class);
                startActivityForResult(intent,2);
            }
        });
        tv_top_parent = findViewById(R.id.text_left_label);
        tv_top_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyProductListActivity.this, SettingActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i(TAG,"Result received");

        if (requestCode == 2){
            if(data != null){
                resetList();
            }
        }
    }

    private void resetList(){
        offset = 0;
        List<Object> itemCatList = dbHelper.getProducts(limit,offset);
        itemList.clear();
        for(Object ob : itemCatList){
            itemList.add(ob);
        }

        itemAdapter.notifyDataSetChanged();
    }

}
