package com.shoppursshop.activities;

import android.os.Build;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shoppursshop.R;
import com.shoppursshop.adapters.CustomerAdapter;
import com.shoppursshop.adapters.OrderAdapter;
import com.shoppursshop.models.HomeListItem;
import com.shoppursshop.models.MyCustomer;
import com.shoppursshop.models.MyHeader;

import java.util.ArrayList;
import java.util.List;

public class CustomerListActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private CustomerAdapter myItemAdapter;
    private List<Object> itemList;
    private TextView textViewError;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        itemList = new ArrayList<>();
        MyHeader myItem = new MyHeader();
        myItem.setTitle("Customers");
        myItem.setDesc("Stores Customers");
        myItem.setType(0);
        itemList.add(myItem);

        myItem = new MyHeader();
        myItem.setTitle("My Favourite Customers");
        myItem.setType(1);
        itemList.add(myItem);

        MyCustomer myCustomer = new MyCustomer();
        myCustomer.setName("Sonam Kapoor");
        myCustomer.setMobile("9718181697");
        myCustomer.setAddress("M-180, Kendriya Vihar, Sector-56");
        myCustomer.setState("Haryana");
        myCustomer.setCity("Gurugram");
        myCustomer.setLocalImage(R.drawable.author_1);
        itemList.add(myCustomer);

        myCustomer = new MyCustomer();
        myCustomer.setName("Katie Perry");
        myCustomer.setMobile("9718181698");
        myCustomer.setAddress("M-181, Kendriya Vihar, Sector-56");
        myCustomer.setState("Haryana");
        myCustomer.setCity("Gurugram");
        myCustomer.setLocalImage(R.drawable.author_2);
        itemList.add(myCustomer);

        myCustomer = new MyCustomer();
        myCustomer.setName("Himani Saraswat");
        myCustomer.setMobile("9718181699");
        myCustomer.setAddress("M-182, Kendriya Vihar, Sector-56");
        myCustomer.setState("Haryana");
        myCustomer.setCity("Gurugram");
        myCustomer.setLocalImage(R.drawable.author_3);
        itemList.add(myCustomer);

        myCustomer = new MyCustomer();
        myCustomer.setName("Miley Cirus");
        myCustomer.setMobile("9718181610");
        myCustomer.setAddress("M-183, Kendriya Vihar, Sector-56");
        myCustomer.setState("Haryana");
        myCustomer.setCity("Gurugram");
        myCustomer.setLocalImage(R.drawable.author_4);
        itemList.add(myCustomer);

        myCustomer = new MyCustomer();
        myCustomer.setName("Sachin Kumar");
        myCustomer.setMobile("9718181611");
        myCustomer.setAddress("M-184, Kendriya Vihar, Sector-56");
        myCustomer.setState("Haryana");
        myCustomer.setCity("Gurugram");
        myCustomer.setLocalImage(R.drawable.author_5);
        itemList.add(myCustomer);

        myCustomer = new MyCustomer();
        myCustomer.setName("Sumit Kumar");
        myCustomer.setMobile("9718181612");
        myCustomer.setAddress("M-185, Kendriya Vihar, Sector-56");
        myCustomer.setState("Haryana");
        myCustomer.setCity("Gurugram");
        myCustomer.setLocalImage(R.drawable.author_6);
        itemList.add(myCustomer);

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
        myItemAdapter=new CustomerAdapter(this,itemList,"customerList");
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
            window.setStatusBarColor(getResources().getColor(R.color.yellow500));
        }

        initFooter(this,2);
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
