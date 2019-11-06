package com.shoppursshop.activities;

import android.os.Build;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shoppursshop.R;
import com.shoppursshop.adapters.ProductAdapter;
import com.shoppursshop.models.HomeListItem;
import com.shoppursshop.models.SubCategory;

import java.util.ArrayList;
import java.util.List;

public class SubCatListActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private ProductAdapter myItemAdapter;
    private List<Object> itemList;
    private TextView textViewError;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private String catName,catID;
    private float MIN_WIDTH = 200,MIN_HEIGHT = 230,MAX_WIDTH = 200,MAX_HEIGHT = 290;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_cat_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        catName = getIntent().getStringExtra("catName");
        catID = getIntent().getStringExtra("catID");

        Log.i(TAG,"catName "+catName);
        Log.i(TAG,"catId "+catID);

        itemList = dbHelper.getCatSubCategoriesForActivity(catID,limit,offset);
        if(itemList.size() > 0){
            SubCategory s1 = (SubCategory) itemList.get(0);
            s1.setCatId(catID);
            s1.setWidth(MIN_WIDTH);
            s1.setHeight(MIN_HEIGHT);
            s1 = (SubCategory) itemList.get(itemList.size()-1);
            s1.setWidth(MIN_WIDTH);
            s1.setHeight(MIN_HEIGHT);
            HomeListItem myItem = new HomeListItem();
            myItem.setTitle(catName);
            myItem.setDesc(catName+" Products");
            itemList.add(0,myItem);
        }


       // setItemList();

        swipeRefreshLayout=findViewById(R.id.swipe_refresh);
        progressBar=findViewById(R.id.progress_bar);
        textViewError = findViewById(R.id.text_error);
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
      /*  int resId = R.anim.layout_animation_slide_from_bottom;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        recyclerView.setLayoutAnimation(animation);*/
        myItemAdapter=new ProductAdapter(this,itemList,"subCatList");
        myItemAdapter.setFlag(getIntent().getStringExtra("flag"));
        recyclerView.setAdapter(myItemAdapter);

       // int[] posArray = ((StaggeredGridLayoutManager)staggeredGridLayoutManager).findLastCompletelyVisibleItemPositions(null);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(isScroll){
                    visibleItemCount = staggeredGridLayoutManager.getChildCount();
                    totalItemCount = staggeredGridLayoutManager.getItemCount();
                    Log.i(TAG,"visible "+visibleItemCount+" total "+totalItemCount);
                    int[] firstVisibleItems = null;
                    firstVisibleItems = staggeredGridLayoutManager.findFirstVisibleItemPositions(firstVisibleItems);
                    if(firstVisibleItems != null && firstVisibleItems.length > 0) {
                        pastVisibleItems = firstVisibleItems[0];
                    }
                    if (!loading) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            loading = true;
                            offset = limit + offset;
                            List<Object> nextItemList = dbHelper.getCatSubCategoriesForActivity(catID,limit,offset);
                            for(Object ob : nextItemList){
                                itemList.add(ob);
                            }
                            if(nextItemList.size() < limit){
                                isScroll = false;
                            }
                            if(nextItemList.size() > 0){
                                recyclerView.post(new Runnable() {
                                    public void run() {
                                        myItemAdapter.notifyDataSetChanged();
                                       // myItemAdapter.notifyItemRangeInserted(offset,limit);
                                        loading = false;
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
           // window.setStatusBarColor(getResources().getColor(R.color.blue700));
        }

        initFooter(this,1);
    }

    private void getItemList(){
        swipeRefreshLayout.setRefreshing(false);
    }

    private void setItemList(){
        if(catName.equals("Grocery")){
            SubCategory subCategory = new SubCategory();
            subCategory.setName("Breakfast & Dairy");
            subCategory.setLocalImage(R.drawable.thumb_16);
            subCategory.setWidth(MIN_WIDTH);
            subCategory.setHeight(MIN_HEIGHT);
            itemList.add(subCategory);
            subCategory = new SubCategory();
            subCategory.setName("Masala & Spices");
            subCategory.setLocalImage(R.drawable.thumb_17);
            subCategory.setWidth(MAX_WIDTH);
            subCategory.setHeight(MAX_HEIGHT);
            itemList.add(subCategory);
            subCategory = new SubCategory();
            subCategory.setName("Personal Care");
            subCategory.setLocalImage(R.drawable.thumb_18);
            subCategory.setWidth(MAX_WIDTH);
            subCategory.setHeight(MAX_HEIGHT);
            itemList.add(subCategory);
            subCategory = new SubCategory();
            subCategory.setName("Beverages");
            subCategory.setLocalImage(R.drawable.thumb_19);
            subCategory.setWidth(MIN_WIDTH);
            subCategory.setHeight(MIN_HEIGHT);
            itemList.add(subCategory);
        }else{
            SubCategory subCategory = new SubCategory();
            subCategory.setName("Pen & Pen Sets");
            subCategory.setLocalImage(R.drawable.thumb_20);
            subCategory.setWidth(MIN_WIDTH);
            subCategory.setHeight(MIN_HEIGHT);
            itemList.add(subCategory);
            subCategory = new SubCategory();
            subCategory.setName("Notebooks");
            subCategory.setLocalImage(R.drawable.thumb_21);
            subCategory.setWidth(MAX_WIDTH);
            subCategory.setHeight(MAX_HEIGHT);
            itemList.add(subCategory);
            subCategory = new SubCategory();
            subCategory.setName("Papers");
            subCategory.setLocalImage(R.drawable.thumb_22);
            subCategory.setWidth(MAX_WIDTH);
            subCategory.setHeight(MAX_HEIGHT);
            itemList.add(subCategory);
            subCategory = new SubCategory();
            subCategory.setName("Color & Paints");
            subCategory.setLocalImage(R.drawable.thumb_23);
            subCategory.setWidth(MAX_WIDTH);
            subCategory.setHeight(MAX_HEIGHT);
            itemList.add(subCategory);
            subCategory = new SubCategory();
            subCategory.setName("Desk Organizer");
            subCategory.setLocalImage(R.drawable.thumb_24);
            subCategory.setWidth(MAX_WIDTH);
            subCategory.setHeight(MAX_HEIGHT);
            itemList.add(subCategory);
            subCategory = new SubCategory();
            subCategory.setName("Markers");
            subCategory.setLocalImage(R.drawable.thumb_25);
            subCategory.setWidth(MIN_WIDTH);
            subCategory.setHeight(MIN_HEIGHT);
            itemList.add(subCategory);
        }
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
