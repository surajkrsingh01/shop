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
    private String catName,subCatName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        subCatName = getIntent().getStringExtra("subCatName");

        itemList = new ArrayList<>();
        HomeListItem myItem = new HomeListItem();
        myItem.setTitle("Products");
      //  myItem.setDesc(subCatName+" Products");
        myItem.setDesc("Store Products");
        myItem.setType(0);
        itemList.add(myItem);

        MyHeader myHeader = new MyHeader();
        myHeader.setTitle(subCatName);
        myHeader.setType(1);
        itemList.add(myHeader);

        MyProduct myProduct = new MyProduct();
        myProduct.setName("Item 1");
        myProduct.setMrp("100");
        myProduct.setSellingPrice("90");
        myProduct.setCode("Code 1");
        myProduct.setDesc("Item description");
        myProduct.setLocalImage(R.drawable.thumb_16);
        myProduct.setSubCatName(subCatName);
        itemList.add(myProduct);

        myProduct = new MyProduct();
        myProduct.setName("Item 2");
        myProduct.setMrp("200");
        myProduct.setSellingPrice("190");
        myProduct.setCode("Code 2");
        myProduct.setDesc("Item description");
        myProduct.setLocalImage(R.drawable.thumb_16);
        myProduct.setSubCatName(subCatName);
        itemList.add(myProduct);

        myProduct = new MyProduct();
        myProduct.setName("Item 3");
        myProduct.setMrp("300");
        myProduct.setSellingPrice("290");
        myProduct.setCode("Code 3");
        myProduct.setDesc("Item description");
        myProduct.setLocalImage(R.drawable.thumb_16);
        myProduct.setSubCatName(subCatName);
        itemList.add(myProduct);

        myProduct = new MyProduct();
        myProduct.setName("Item 4");
        myProduct.setMrp("400");
        myProduct.setSellingPrice("390");
        myProduct.setCode("Code 4");
        myProduct.setDesc("Item description");
        myProduct.setLocalImage(R.drawable.thumb_16);
        myProduct.setSubCatName(subCatName);
        itemList.add(myProduct);

        myProduct = new MyProduct();
        myProduct.setName("Item 5");
        myProduct.setMrp("500");
        myProduct.setSellingPrice("490");
        myProduct.setCode("Code 5");
        myProduct.setDesc("Item description");
        myProduct.setLocalImage(R.drawable.thumb_16);
        myProduct.setSubCatName(subCatName);
        itemList.add(myProduct);

        myProduct = new MyProduct();
        myProduct.setName("Item 6");
        myProduct.setMrp("600");
        myProduct.setSellingPrice("590");
        myProduct.setCode("Code 6");
        myProduct.setDesc("Item description");
        myProduct.setLocalImage(R.drawable.thumb_16);
        myProduct.setSubCatName(subCatName);
        itemList.add(myProduct);

        myProduct = new MyProduct();
        myProduct.setName("Item 7");
        myProduct.setMrp("700");
        myProduct.setSellingPrice("690");
        myProduct.setCode("Code 7");
        myProduct.setDesc("Item description");
        myProduct.setLocalImage(R.drawable.thumb_16);
        myProduct.setSubCatName(subCatName);
        itemList.add(myProduct);

        myProduct = new MyProduct();
        myProduct.setName("Item 8");
        myProduct.setMrp("800");
        myProduct.setSellingPrice("790");
        myProduct.setCode("Code 8");
        myProduct.setDesc("Item description");
        myProduct.setLocalImage(R.drawable.thumb_16);
        myProduct.setSubCatName(subCatName);
        itemList.add(myProduct);

        swipeRefreshLayout=findViewById(R.id.swipe_refresh);
        progressBar=findViewById(R.id.progress_bar);
        textViewError = findViewById(R.id.text_error);
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        // staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
      /*  int resId = R.anim.layout_animation_slide_from_bottom;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        recyclerView.setLayoutAnimation(animation);*/
        myItemAdapter=new ProductAdapter(this,itemList,"productList");
        recyclerView.setAdapter(myItemAdapter);

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
