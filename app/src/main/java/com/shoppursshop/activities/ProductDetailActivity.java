package com.shoppursshop.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shoppursshop.R;
import com.shoppursshop.adapters.MonthlyGraphAdapter;
import com.shoppursshop.adapters.MyItemAdapter;
import com.shoppursshop.adapters.MyReviewAdapter;
import com.shoppursshop.models.Bar;
import com.shoppursshop.models.HomeListItem;
import com.shoppursshop.models.MyReview;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ProductDetailActivity extends BaseActivity {

    private RecyclerView recyclerViewMonthlyGraph,recyclerViewReview,recyclerViewOffers;
    private RecyclerView.Adapter monthlyGraphAdapter;
    private MyReviewAdapter myReviewAdapter;
    private List<Bar> barList;
    private List<MyReview> myReviewList;

    private MyItemAdapter myItemAdapter;
    private List<Object> itemList;

    private TextView textViewSubCatName,textViewProductName,textViewDesc,textViewCode;
    private ImageView imageView2,imageView3,imageView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();
        initFooter(this,1);

    }

    private void init(){
        Intent intent = getIntent();
        textViewSubCatName = findViewById(R.id.text_sub_cat);
        textViewProductName = findViewById(R.id.text_product_name);
        textViewDesc = findViewById(R.id.text_desc);
        textViewCode = findViewById(R.id.text_bar_code);

        imageView2 = findViewById(R.id.image_view_2);
        imageView3 = findViewById(R.id.image_view_3);
        imageView4 = findViewById(R.id.image_view_4);

        textViewSubCatName.setText(intent.getStringExtra("subCatName"));
        textViewProductName.setText(intent.getStringExtra("productName"));
        textViewCode.setText(intent.getStringExtra("productCode"));
        textViewDesc.setText(intent.getStringExtra("productDesc"));

        imageView2.setImageResource(intent.getIntExtra("productLocalImage",0));
        imageView3.setImageResource(intent.getIntExtra("productLocalImage",0));
        imageView4.setImageResource(intent.getIntExtra("productLocalImage",0));

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

        myReviewList = new ArrayList<>();
        recyclerViewReview=(RecyclerView)findViewById(R.id.recycler_view_review);
        recyclerViewReview.setItemAnimator(new DefaultItemAnimator());
        recyclerViewReview.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManagerReview=new LinearLayoutManager(this);
        recyclerViewReview.setLayoutManager(layoutManagerReview);
        myReviewAdapter=new MyReviewAdapter(this,myReviewList,"productReview");
        recyclerViewReview.setAdapter(myReviewAdapter);
        recyclerViewReview.setNestedScrollingEnabled(false);
        setReviews();

        itemList = new ArrayList<>();
        HomeListItem myItem = new HomeListItem();
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

        recyclerViewOffers=findViewById(R.id.recycler_view_offer);
        recyclerViewOffers.setHasFixedSize(true);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        recyclerViewOffers.setLayoutManager(staggeredGridLayoutManager);
        recyclerViewOffers.setItemAnimator(new DefaultItemAnimator());
        myItemAdapter=new MyItemAdapter(this,itemList,"homeList");
        recyclerViewOffers.setAdapter(myItemAdapter);
        recyclerViewOffers.setNestedScrollingEnabled(false);

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

    private void setReviews(){
        MyReview myReview = new MyReview();
        myReview.setUserName("Vipin Dhama");
        myReview.setDateTime("4 hours ago");
        myReview.setRating(4);
        myReview.setReview("Review about product.");

        myReviewList.add(myReview);
        myReview = new MyReview();
        myReview.setUserName("Amit kumar");
        myReview.setDateTime("8 hours ago");
        myReview.setRating(4);
        myReview.setReview("Review about product.");
        myReviewList.add(myReview);

        myReview = new MyReview();
        myReview.setUserName("Sunil Kumar");
        myReview.setDateTime("6 days ago");
        myReview.setRating(4);
        myReview.setReview("Review about product.");
        myReviewList.add(myReview);

        myReviewAdapter.notifyDataSetChanged();

    }

    public String getMonth(int position){
        String[] monthInText={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

        return monthInText[position];
    }

}
