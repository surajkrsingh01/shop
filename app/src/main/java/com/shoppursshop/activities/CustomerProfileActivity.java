package com.shoppursshop.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shoppursshop.R;
import com.shoppursshop.adapters.MonthlyGraphAdapter;
import com.shoppursshop.adapters.MyReviewAdapter;
import com.shoppursshop.adapters.OrderAdapter;
import com.shoppursshop.models.Bar;
import com.shoppursshop.models.MyReview;
import com.shoppursshop.models.OrderItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CustomerProfileActivity extends BaseActivity {

    private RecyclerView recyclerViewMonthlyGraph,recyclerViewOrder;
    private RecyclerView.Adapter monthlyGraphAdapter;
    private OrderAdapter orderAdapter;
    private List<Bar> barList;
    private List<Object> orderItemList;

    private TextView textViewName,textViewAddress,textViewStateCity;
    private ImageView imageView2,imageView3,imageView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();
        initFooter(this,2);
    }

    private void init(){
        Intent intent = getIntent();
        textViewName = findViewById(R.id.text_customer_name);
        textViewAddress = findViewById(R.id.text_address);
        textViewStateCity = findViewById(R.id.text_state_city);

        imageView2 = findViewById(R.id.image_view_2);
        imageView3 = findViewById(R.id.image_view_3);
        imageView4 = findViewById(R.id.image_view_4);

        textViewName.setText(intent.getStringExtra("name"));
        textViewAddress.setText(intent.getStringExtra("address"));
        textViewStateCity.setText(intent.getStringExtra("stateCity"));

        imageView2.setImageResource(intent.getIntExtra("customerImage",0));
        imageView3.setImageResource(intent.getIntExtra("customerImage",0));
        imageView4.setImageResource(intent.getIntExtra("customerImage",0));

        barList = new ArrayList<>();
        recyclerViewMonthlyGraph=(RecyclerView)findViewById(R.id.recycler_view_monthly_graph);
        recyclerViewMonthlyGraph.setItemAnimator(new DefaultItemAnimator());
        recyclerViewMonthlyGraph.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManagerMonthlyGraph=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        recyclerViewMonthlyGraph.setLayoutManager(layoutManagerMonthlyGraph);
        monthlyGraphAdapter=new MonthlyGraphAdapter(this,barList,1);
        ((MonthlyGraphAdapter) monthlyGraphAdapter).setTotalTarget(10000);
        recyclerViewMonthlyGraph.setAdapter(monthlyGraphAdapter);
        setMonthlyBar();

        orderItemList = new ArrayList<>();
        recyclerViewOrder=findViewById(R.id.recycler_view_order);
        recyclerViewOrder.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerViewOrder.setLayoutManager(layoutManager);
        recyclerViewOrder.setItemAnimator(new DefaultItemAnimator());
        orderAdapter=new OrderAdapter(this,orderItemList,"orderList");
        recyclerViewOrder.setAdapter(orderAdapter);
        recyclerViewOrder.setNestedScrollingEnabled(false);
        setPreOrders();

    }

    public void setMonthlyBar(){
        Calendar calendarTemp =Calendar.getInstance();
        barList.clear();
        for(int i=0;i<12;i++){
            if(i > 0)
                calendarTemp.add(Calendar.MONTH, -1);

            int month=calendarTemp.get(Calendar.MONTH);
            int year=calendarTemp.get(Calendar.YEAR);
            String monthName=getMonth(month);
            Bar bar=new Bar();
            bar.setName(monthName);
            bar.setSaleValue(5000);
            bar.setSaleAchievedValue(5000);
            bar.setSaleTargetValue(8000);
            bar.setBudget(10000);
            barList.add(bar);
        }

        monthlyGraphAdapter.notifyDataSetChanged();
    }

    private void setPreOrders(){
        OrderItem orderItem = new OrderItem();
        orderItem.setType(2);
        orderItem.setId("00001123");
        orderItem.setStatus("Delivered");
        orderItem.setDateTime("13:32, Nov, 05, 2018");
        orderItem.setRating(4);
        orderItem.setCustomerName("Sonam Kapoor");
        orderItem.setMobile("9718181697");
        orderItem.setAmount(2000);
        orderItem.setDeliveryType("Cash On Delivery");
        orderItem.setLocalImage(R.drawable.thumb_11);
        orderItemList.add(orderItem);

        orderItem = new OrderItem();
        orderItem.setType(2);
        orderItem.setId("00001124");
        orderItem.setStatus("Delivered");
        orderItem.setDateTime("14:32, Nov, 06, 2018");
        orderItem.setRating(4);
        orderItem.setCustomerName("Katie Perry");
        orderItem.setMobile("9718181698");
        orderItem.setAmount(24000);
        orderItem.setDeliveryType("Pick Up");
        orderItem.setLocalImage(R.drawable.thumb_12);
        orderItemList.add(orderItem);

        orderItem = new OrderItem();
        orderItem.setType(2);
        orderItem.setId("00001125");
        orderItem.setStatus("Cancelled");
        orderItem.setDateTime("13:32, Nov, 07, 2018");
        orderItem.setRating(4);
        orderItem.setCustomerName("Mohit Kumar");
        orderItem.setMobile("9718181699");
        orderItem.setAmount(34000);
        orderItem.setDeliveryType("In Store");
        orderItem.setLocalImage(R.drawable.thumb_13);
        orderItemList.add(orderItem);

        orderItem = new OrderItem();
        orderItem.setType(2);
        orderItem.setId("00001128");
        orderItem.setStatus("Delivered");
        orderItem.setDateTime("13:32, Nov, 08, 2018");
        orderItem.setRating(4);
        orderItem.setCustomerName("Sachin Kumar");
        orderItem.setMobile("9718181610");
        orderItem.setAmount(4000);
        orderItem.setDeliveryType("In Store");
        orderItem.setLocalImage(R.drawable.thumb_11);
        orderItemList.add(orderItem);

        orderItem = new OrderItem();
        orderItem.setType(2);
        orderItem.setId("00001129");
        orderItem.setStatus("Delivered");
        orderItem.setDateTime("13:32, Nov, 09, 2018");
        orderItem.setRating(4);
        orderItem.setCustomerName("Amit Kumar");
        orderItem.setMobile("9718181611");
        orderItem.setAmount(10000);
        orderItem.setDeliveryType("Cash On Delivery");
        orderItem.setLocalImage(R.drawable.thumb_12);
        orderItemList.add(orderItem);

        orderAdapter.notifyDataSetChanged();
    }

    public String getMonth(int position){
        String[] monthInText={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

        return monthInText[position];
    }

}
