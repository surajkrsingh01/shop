package com.shoppursshop.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shoppursshop.R;
import com.shoppursshop.adapters.ProductAdapter;
import com.shoppursshop.models.Category;
import com.shoppursshop.models.SubCategory;
import com.shoppursshop.models.CatListItem;

import java.util.ArrayList;
import java.util.List;

public class CategoryListActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private ProductAdapter myItemAdapter;
    private List<Object> itemList;
    private TextView textViewError;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;

    private float MIN_WIDTH = 200,MIN_HEIGHT = 230,MAX_WIDTH = 200,MAX_HEIGHT = 290;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        itemList = new ArrayList<>();
        CatListItem myItem = new CatListItem();
        myItem.setTitle("Products");
        myItem.setDesc("Store Category");
        myItem.setType(0);
        List<Object> catList = new ArrayList<>();

        Category category = new Category();
        category.setName("Scan");
        catList.add(category);

        category = new Category();
        category.setName("Grocery");
        category.setLocalImage(R.drawable.thumb_14);
        catList.add(category);

        category = new Category();
        category.setName("Stationary");
        category.setLocalImage(R.drawable.thumb_15);
        catList.add(category);
        myItem.setItemList(catList);
        itemList.add(myItem);

        myItem = new CatListItem();
        myItem.setTitle("Grocery");
        myItem.setType(1);
        List<Object> prodList = new ArrayList<>();
        SubCategory subCategory = new SubCategory();
        subCategory.setName("Breakfast & Dairy");
        subCategory.setLocalImage(R.drawable.thumb_16);
        subCategory.setWidth(MIN_WIDTH);
        subCategory.setHeight(MIN_HEIGHT);
        prodList.add(subCategory);
        subCategory = new SubCategory();
        subCategory.setName("Masala & Spices");
        subCategory.setLocalImage(R.drawable.thumb_17);
        subCategory.setWidth(MAX_WIDTH);
        subCategory.setHeight(MAX_HEIGHT);
        prodList.add(subCategory);
        subCategory = new SubCategory();
        subCategory.setName("Personal Care");
        subCategory.setLocalImage(R.drawable.thumb_18);
        subCategory.setWidth(MAX_WIDTH);
        subCategory.setHeight(MAX_HEIGHT);
        prodList.add(subCategory);
        subCategory = new SubCategory();
        subCategory.setName("Beverages");
        subCategory.setLocalImage(R.drawable.thumb_19);
        subCategory.setWidth(MIN_WIDTH);
        subCategory.setHeight(MIN_HEIGHT);
        prodList.add(subCategory);
        myItem.setItemList(prodList);
        itemList.add(myItem);

        myItem = new CatListItem();
        myItem.setTitle("Stationary");
        myItem.setType(1);
        prodList = new ArrayList<>();
        subCategory = new SubCategory();
        subCategory.setName("Pen & Pen Sets");
        subCategory.setLocalImage(R.drawable.thumb_20);
        subCategory.setWidth(MIN_WIDTH);
        subCategory.setHeight(MIN_HEIGHT);
        prodList.add(subCategory);
        subCategory = new SubCategory();
        subCategory.setName("Notebooks");
        subCategory.setLocalImage(R.drawable.thumb_21);
        subCategory.setWidth(MAX_WIDTH);
        subCategory.setHeight(MAX_HEIGHT);
        prodList.add(subCategory);
        subCategory = new SubCategory();
        subCategory.setName("Papers");
        subCategory.setLocalImage(R.drawable.thumb_22);
        subCategory.setWidth(MAX_WIDTH);
        subCategory.setHeight(MAX_HEIGHT);
        prodList.add(subCategory);
        subCategory = new SubCategory();
        subCategory.setName("Color & Paints");
        subCategory.setLocalImage(R.drawable.thumb_23);
        subCategory.setWidth(MAX_WIDTH);
        subCategory.setHeight(MAX_HEIGHT);
        prodList.add(subCategory);
        subCategory = new SubCategory();
        subCategory.setName("Desk Organizer");
        subCategory.setLocalImage(R.drawable.thumb_24);
        subCategory.setWidth(MAX_WIDTH);
        subCategory.setHeight(MAX_HEIGHT);
        prodList.add(subCategory);
        subCategory = new SubCategory();
        subCategory.setName("Markers");
        subCategory.setLocalImage(R.drawable.thumb_25);
        subCategory.setWidth(MIN_WIDTH);
        subCategory.setHeight(MIN_HEIGHT);
        prodList.add(subCategory);
        myItem.setItemList(prodList);
        itemList.add(myItem);

        swipeRefreshLayout=findViewById(R.id.swipe_refresh);
        progressBar=findViewById(R.id.progress_bar);
        textViewError = findViewById(R.id.text_error);
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
      /*  int resId = R.anim.layout_animation_slide_from_bottom;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        recyclerView.setLayoutAnimation(animation);*/
        myItemAdapter=new ProductAdapter(this,itemList,"catList");
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.blue700));
        }

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
