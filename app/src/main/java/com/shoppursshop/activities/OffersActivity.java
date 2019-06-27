package com.shoppursshop.activities;

import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shoppursshop.R;
import com.shoppursshop.adapters.MyItemAdapter;
import com.shoppursshop.models.HomeListItem;

import java.util.ArrayList;
import java.util.List;

public class OffersActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private MyItemAdapter myItemAdapter;
    private List<Object> itemList;
    private TextView textViewError;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;

    private float MIN_WIDTH = 200,MIN_HEIGHT = 230,MAX_WIDTH = 200,MAX_HEIGHT = 290;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        itemList = new ArrayList<>();
        HomeListItem myItem = new HomeListItem();
        myItem.setTitle("Products");
        myItem.setDesc("Store Category");
        myItem.setType(0);
        itemList.add(myItem);

        myItem = new HomeListItem();
        myItem.setName("Samsung Stores");
        myItem.setLocalIcon(R.drawable.icon_1);
        myItem.setLocalImage(R.drawable.thumb_1);
        myItem.setType(1);
        itemList.add(myItem);

        myItem = new HomeListItem();
        myItem.setTitle("The Fashion Trends");
        myItem.setDesc("Handmade in Italy, Now in India");
        myItem.setCategory("Fashion");
        myItem.setLocalImage(R.drawable.thumb_2);
        myItem.setType(2);
        itemList.add(myItem);

        myItem = new HomeListItem();
        myItem.setName("Grocery");
        myItem.setTitle("Big Grocery Sale !!");
        myItem.setLocalIcon(R.drawable.icon_1);
        myItem.setLocalImage(R.drawable.thumb_3);
        myItem.setType(1);
        itemList.add(myItem);

        myItem = new HomeListItem();
        myItem.setTitle("15 December - 16 December");
        myItem.setDesc("Festive Sales");
        myItem.setType(0);
        itemList.add(myItem);

        myItem = new HomeListItem();
        myItem.setTitle("Sports Store");
        myItem.setLocalImage(R.drawable.thumb_4);
        myItem.setType(3);
        myItem.setWidth(MIN_WIDTH);
        myItem.setHeight(MIN_HEIGHT);
        itemList.add(myItem);

        myItem = new HomeListItem();
        myItem.setTitle("Books and Toys");
        myItem.setLocalImage(R.drawable.thumb_5);
        myItem.setType(3);
        myItem.setWidth(MAX_WIDTH);
        myItem.setHeight(MAX_HEIGHT);
        itemList.add(myItem);

        myItem = new HomeListItem();
        myItem.setTitle("Top Sunglasses");
        myItem.setLocalImage(R.drawable.thumb_6);
        myItem.setType(3);
        myItem.setWidth(MAX_WIDTH);
        myItem.setHeight(MAX_HEIGHT);
        itemList.add(myItem);

        myItem = new HomeListItem();
        myItem.setTitle("Fashion Makeups");
        myItem.setLocalImage(R.drawable.thumb_7);
        myItem.setType(3);
        myItem.setWidth(MAX_WIDTH);
        myItem.setHeight(MAX_HEIGHT);
        itemList.add(myItem);

        myItem = new HomeListItem();
        myItem.setTitle("MotoBikes");
        myItem.setLocalImage(R.drawable.thumb_8);
        myItem.setType(3);
        myItem.setWidth(MAX_WIDTH);
        myItem.setHeight(MAX_HEIGHT);
        itemList.add(myItem);

        myItem = new HomeListItem();
        myItem.setTitle("Swimwear and Lingerie");
        myItem.setLocalImage(R.drawable.thumb_9);
        myItem.setType(3);
        myItem.setWidth(MIN_WIDTH);
        myItem.setHeight(MIN_HEIGHT);
        itemList.add(myItem);

        myItem = new HomeListItem();
        myItem.setTitle("Big Discount in Titan Watches");
        myItem.setDesc("Titan Stores");
        myItem.setCategory("Watches");
        myItem.setLocalImage(R.drawable.thumb_11);
        myItem.setType(4);
        itemList.add(myItem);

        myItem = new HomeListItem();
        myItem.setTitle("10% Discount on Camera And HandyCams");
        myItem.setDesc("Sony Stores");
        myItem.setCategory("Camera And HandyCams");
        myItem.setLocalImage(R.drawable.thumb_12);
        myItem.setType(4);
        itemList.add(myItem);

        myItem = new HomeListItem();
        myItem.setTitle("Upto 30% discount in furnitures and other products");
        myItem.setDesc("Home Town Stores");
        myItem.setCategory("Home Furnishing");
        myItem.setLocalImage(R.drawable.thumb_13);
        myItem.setType(4);
        itemList.add(myItem);

        swipeRefreshLayout=findViewById(R.id.swipe_refresh);
        progressBar=findViewById(R.id.progress_bar);
        textViewError = findViewById(R.id.text_error);
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        // RecyclerView.LayoutManager layoutManagerHomeMenu=new LinearLayoutManager(this);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
      /*  int resId = R.anim.layout_animation_slide_from_bottom;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        recyclerView.setLayoutAnimation(animation);*/
        myItemAdapter=new MyItemAdapter(this,itemList,"homeList");
        recyclerView.setAdapter(myItemAdapter);

        if(itemList.size() == 0){
            showNoData(true);
        }

        initFooter(this,3);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                getItemList();
            }
        });
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
