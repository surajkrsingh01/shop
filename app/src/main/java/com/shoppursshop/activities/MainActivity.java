package com.shoppursshop.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shoppursshop.R;
import com.shoppursshop.adapters.MyItemAdapter;
import com.shoppursshop.adapters.OrderAdapter;
import com.shoppursshop.models.HomeListItem;
import com.shoppursshop.models.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private OrderAdapter myItemAdapter;
    private List<Object> itemList;
    private TextView textViewError;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;

    private float MIN_WIDTH = 200,MIN_HEIGHT = 230,MAX_WIDTH = 200,MAX_HEIGHT = 290;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        itemList = new ArrayList<>();
        HomeListItem myItem = new HomeListItem();
        myItem.setTitle("Sunday 16 December, 2018");
        myItem.setDesc("Today Orders");
        myItem.setType(0);
        itemList.add(myItem);

        OrderItem orderItem = new OrderItem();
        orderItem.setType(1);
        orderItem.setCustomerName("Sonam Kapoor");
        orderItem.setMobile("9718181697");
        orderItem.setAmount(2000);
        orderItem.setDeliveryType("Cash On Delivery");
        orderItem.setLocalImage(R.drawable.thumb_11);
        itemList.add(orderItem);

        orderItem = new OrderItem();
        orderItem.setType(1);
        orderItem.setCustomerName("Katie Perry");
        orderItem.setMobile("9718181698");
        orderItem.setAmount(24000);
        orderItem.setDeliveryType("Pick Up");
        orderItem.setLocalImage(R.drawable.thumb_12);
        itemList.add(orderItem);

        orderItem = new OrderItem();
        orderItem.setType(1);
        orderItem.setCustomerName("Mohit Kumar");
        orderItem.setMobile("9718181699");
        orderItem.setAmount(34000);
        orderItem.setDeliveryType("In Store");
        orderItem.setLocalImage(R.drawable.thumb_13);
        itemList.add(orderItem);

        orderItem = new OrderItem();
        orderItem.setType(1);
        orderItem.setCustomerName("Sachin Kumar");
        orderItem.setMobile("9718181610");
        orderItem.setAmount(4000);
        orderItem.setDeliveryType("In Store");
        orderItem.setLocalImage(R.drawable.thumb_11);
        itemList.add(orderItem);

        orderItem = new OrderItem();
        orderItem.setType(1);
        orderItem.setCustomerName("Amit Kumar");
        orderItem.setMobile("9718181611");
        orderItem.setAmount(10000);
        orderItem.setDeliveryType("Cash On Delivery");
        orderItem.setLocalImage(R.drawable.thumb_12);
        itemList.add(orderItem);

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
        myItemAdapter=new OrderAdapter(this,itemList,"orderList");
        recyclerView.setAdapter(myItemAdapter);

        if(itemList.size() == 0){
            showNoData(true);
        }

        initFooter(this,0);

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

    private void runLayoutAnimation() {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_slide_from_bottom);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
