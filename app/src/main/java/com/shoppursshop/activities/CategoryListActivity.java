package com.shoppursshop.activities;

import android.os.Build;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.appcompat.widget.Toolbar;
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

    public static float MIN_WIDTH = 200,MIN_HEIGHT = 230,MAX_WIDTH = 200,MAX_HEIGHT = 290;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        itemList = new ArrayList<>();
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
        myItemAdapter.setColorTheme(colorTheme);
        myItemAdapter.setFlag(getIntent().getStringExtra("flag"));
        recyclerView.setAdapter(myItemAdapter);

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

    @Override
    public void onResume(){
        super.onResume();

        getItemList();
    }


    private void getItemList(){
        swipeRefreshLayout.setRefreshing(false);

        itemList.clear();
        CatListItem myItem = new CatListItem();
        myItem.setTitle("Products");
        myItem.setDesc("Store Category");
        myItem.setType(0);
        List<Object> catList = dbHelper.getCategoriesForActivity(limit,offset);

        Category category = new Category();
        category.setName("Scan");
        catList.add(0,category);

        myItem.setItemList(catList);
        itemList.add(myItem);

        for(Object ob : catList){
            Category cat = (Category)ob;
            if(!cat.getName().equals("Scan")){
                myItem = new CatListItem();
                myItem.setTitle(cat.getName());
                myItem.setId(Integer.parseInt(cat.getId()));
                myItem.setType(1);
                List<Object> subCatList = dbHelper.getCatSubCategoriesForActivity(cat.getId(),smallLimit,smallOffset);
                if(subCatList.size() > 0){
                    SubCategory s1 = (SubCategory) subCatList.get(0);
                    s1.setWidth(MIN_WIDTH);
                    s1.setHeight(MIN_HEIGHT);
                    s1 = (SubCategory) subCatList.get(subCatList.size()-1);
                    s1.setWidth(MIN_WIDTH);
                    s1.setHeight(MIN_HEIGHT);
                    myItem.setItemList(subCatList);
                    itemList.add(myItem);
                }

            }

        }
        if(itemList.size() == 0){
            showNoData(true);
        }
        myItemAdapter.notifyDataSetChanged();
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
