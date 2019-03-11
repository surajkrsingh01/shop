package com.shoppursshop.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shoppursshop.R;
import com.shoppursshop.adapters.OrderAdapter;
import com.shoppursshop.adapters.ProductAdapter;
import com.shoppursshop.models.HomeListItem;
import com.shoppursshop.models.MyHeader;
import com.shoppursshop.models.MyProduct;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private ProductAdapter myItemAdapter;
    private List<Object> itemList;
    private TextView textViewError;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private String catName,subCatName,subCatID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        subCatName = getIntent().getStringExtra("subCatName");
        subCatID = getIntent().getStringExtra("subCatID");

        itemList = dbHelper.getProducts(subCatID,limit,offset);
        HomeListItem myItem = new HomeListItem();
        myItem.setTitle("Products");
      //  myItem.setDesc(subCatName+" Products");
        myItem.setDesc("Store Products");
        myItem.setType(0);
        itemList.add(0,myItem);

        MyHeader myHeader = new MyHeader();
        myHeader.setTitle(subCatName);
        myHeader.setType(1);
        itemList.add(1,myHeader);

        swipeRefreshLayout=findViewById(R.id.swipe_refresh);
        progressBar=findViewById(R.id.progress_bar);
        textViewError = findViewById(R.id.text_error);
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        final RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        // staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
      /*  int resId = R.anim.layout_animation_slide_from_bottom;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        recyclerView.setLayoutAnimation(animation);*/
        myItemAdapter=new ProductAdapter(this,itemList,"productList");
        myItemAdapter.setSubCatName(subCatName);
        recyclerView.setAdapter(myItemAdapter);

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
                            List<Object> nextItemList = dbHelper.getProducts(subCatID,limit,offset);
                            for(Object ob : nextItemList){
                                itemList.add(ob);
                            }
                            if(nextItemList.size() < limit){
                                isScroll = false;
                            }
                            if(nextItemList.size() > 0){
                                recyclerView.post(new Runnable() {
                                    public void run() {
                                        myItemAdapter.notifyItemRangeInserted(offset,limit);
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

        if(itemList.size() == 0){
            showNoData(true);
        }


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                getItemList();
            }
        });

        initFooter(this,1);
    }

    private void getItemList(){
        swipeRefreshLayout.setRefreshing(false);
    }

    private void showNoData(boolean show){
        if(show){
            recyclerView.setVisibility(View.GONE);
            textViewError.setVisibility(View.VISIBLE);
        }else{
            recyclerView.setVisibility(View.VISIBLE);
            textViewError.setVisibility(View.GONE);
        }
    }

    private void showProgressBar(boolean show){
        if(show){
            recyclerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            textViewError.setVisibility(View.GONE);
        }else{
            recyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

}
