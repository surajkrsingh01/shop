package com.shoppursshop.activities;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.shoppursshop.R;
import com.shoppursshop.adapters.MonthlyGraphAdapter;
import com.shoppursshop.adapters.MyItemAdapter;
import com.shoppursshop.adapters.MyReviewAdapter;
import com.shoppursshop.adapters.SpecificationAdapter;
import com.shoppursshop.fragments.DescBottomFragment;
import com.shoppursshop.fragments.MultipleBarcodeBottomFragment;
import com.shoppursshop.models.Bar;
import com.shoppursshop.models.HomeListItem;
import com.shoppursshop.models.MyCustomer;
import com.shoppursshop.models.MyProductItem;
import com.shoppursshop.models.MyReview;
import com.shoppursshop.models.ProductColor;
import com.shoppursshop.models.ProductSize;
import com.shoppursshop.models.ProductUnit;
import com.shoppursshop.models.SpinnerItem;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDetailActivity extends NetworkBaseActivity {

    private final int UNIT = 1,SIZE = 2,COLOR = 3;

    private RecyclerView recyclerViewMonthlyGraph,recyclerViewReview,recyclerViewOffers;
    private RecyclerView.Adapter monthlyGraphAdapter;
    private MyReviewAdapter myReviewAdapter;
    private List<Bar> barList,productSaleList;
    private List<MyReview> myReviewList;

    private TextView textViewSubCatName,textViewProductName,textViewMrp,textViewDesc,textViewCode,
            textReorderLevel,textViewQoh,textViewNoReviews,text_sale_trend_Label, text_reviews_Label;
    private TextView tvStarRatings,tvNumRatings,tvDiscount,tvMrp,tvReadMore,textTotalSale;
    private RelativeLayout relativeLayoutRating;
    private RelativeLayout relative_reorder;
    private LinearLayout linear_amount_division;
    private ImageView imageView2,imageView3,imageView4;
    private Button btnLoadMoreReviews;
    private MyProductItem myProductItem;
    private View viewAddBarcodeSeparator;

    private CheckBox checkBoxMultipleBarcode;
    private Button buttonAddMultipleBarcode;

    private MultipleBarcodeBottomFragment multipleBarcodeBottomFragment;

    private String flag ;

    private TextView tvUnitSizeColor;
    private RelativeLayout rlProductSpecificationLayout;
    private RecyclerView recyclerView;
    private SpecificationAdapter unitAdapter,sizeAdapter,colorAdapter;
    List<Object> unitList,sizeList,colorList;
    private int specificationType;
    private Spinner spinnerSize;
    List<String> sizeSpinnerList;
    private ArrayAdapter<String> sizeSpinnerAdapter;
    private RelativeLayout relative_size;
    private int limit = 5,offset = 0;



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
        flag = intent.getStringExtra("flag");
        textViewSubCatName = findViewById(R.id.text_sub_cat);
        textViewProductName = findViewById(R.id.text_product_name);
        tvStarRatings = findViewById(R.id.text_start_rating);
        tvNumRatings = findViewById(R.id.text_rating_counter);
        relativeLayoutRating = findViewById(R.id.relative_rating);
        tvMrp = findViewById(R.id.text_off);
        tvDiscount = findViewById(R.id.text_off_percentage);
        textViewMrp = findViewById(R.id.text_mrp);
        textViewDesc = findViewById(R.id.text_desc);
        textViewCode = findViewById(R.id.text_bar_code);
        textReorderLevel = findViewById(R.id.text_reorder_level);
        textViewQoh = findViewById(R.id.text_stock);
        textViewNoReviews = findViewById(R.id.text_no_reviews);
        tvReadMore = findViewById(R.id.text_more);
        textTotalSale = findViewById(R.id.text_total_sale);
        viewAddBarcodeSeparator = findViewById(R.id.view_add_barcode_sparator);
        btnLoadMoreReviews = findViewById(R.id.btn_load_more_review);
        buttonAddMultipleBarcode = findViewById(R.id.btn_add_multiple_barcode);
        checkBoxMultipleBarcode = findViewById(R.id.checkbox_multiple_barcode);
        tvMrp.setPaintFlags(tvMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        relative_reorder = findViewById(R.id.relative_reorder);
        linear_amount_division = findViewById(R.id.linear_amount_division);
        text_sale_trend_Label = findViewById(R.id.text_sale_trend_Label);
        text_reviews_Label = findViewById(R.id.text_reviews_Label);
        relative_size = findViewById(R.id.relative_size);


        imageView2 = findViewById(R.id.image_view_2);
        imageView3 = findViewById(R.id.image_view_3);
        imageView4 = findViewById(R.id.image_view_4);

        textViewSubCatName.setText(intent.getStringExtra("subCatName"));

        if(flag != null){
            if(flag.equals("scan") || flag.equals("search")){
                myProductItem = dbHelper.getProductDetails(intent.getIntExtra("id",0));
                textViewSubCatName.setText(dbHelper.getSubCatName(myProductItem.getProdId()));
            }else if(flag.equals("shoppurs_product")){
                buttonAddMultipleBarcode.setVisibility(View.GONE);
                textReorderLevel.setVisibility(View.GONE);
                textReorderLevel.setVisibility(View.GONE);
                relative_reorder.setVisibility(View.GONE);
                linear_amount_division.setVisibility(View.GONE);
                text_sale_trend_Label.setVisibility(View.GONE);
                text_reviews_Label.setVisibility(View.GONE);
                viewAddBarcodeSeparator.setVisibility(View.GONE);
                //myProductItem = dbHelper.getProductDetails(intent.getIntExtra("id",0));
                myProductItem = (MyProductItem) getIntent().getSerializableExtra("myProduct");
            } else{
                myProductItem = dbHelper.getProductDetails(intent.getIntExtra("id",0));
            }
        }else{
            myProductItem = dbHelper.getProductDetails(intent.getIntExtra("id",0));
        }

        if(myProductItem.getProductUnitList() != null){
            Log.i(TAG,"unit size "+myProductItem.getProductUnitList().size());
        }else{
            Log.i(TAG,"unit size null");
        }



        textViewProductName.setText(myProductItem.getProdName());
        textViewMrp.setText(Utility.numberFormat(myProductItem.getProdSp()));
        tvMrp.setText(Utility.numberFormat(myProductItem.getProdMrp()));
        textViewCode.setText(myProductItem.getProdBarCode());

        if(myProductItem.getProdDesc().length() > 200){
            textViewDesc.setText(myProductItem.getProdDesc().substring(0,200)+"...");
            tvReadMore.setVisibility(View.VISIBLE);
        }else{
            textViewDesc.setText(myProductItem.getProdDesc());
        }

        textReorderLevel.setText(""+myProductItem.getProdReorderLevel());
        textViewQoh.setText(""+myProductItem.getProdQoh());

        float diff = myProductItem.getProdMrp() - myProductItem.getProdSp();
        if(diff > 0f){
            float percentage = diff * 100 /myProductItem.getProdMrp();
            tvDiscount.setText(String.format("%.02f",percentage)+"% off");
        }else{
            tvDiscount.setVisibility(View.GONE);
            tvMrp.setVisibility(View.GONE);
        }

        if(myProductItem.getIsBarCodeAvailable()!=null && myProductItem.getIsBarCodeAvailable().equals("N")){
            buttonAddMultipleBarcode.setVisibility(View.GONE);
        }

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        // requestOptions.override(Utility.dpToPx(150, context), Utility.dpToPx(150, context));
        requestOptions.centerCrop();
        requestOptions.skipMemoryCache(false);

        Glide.with(this)
                .load(myProductItem.getProdImage1())
                .apply(requestOptions)
                .error(R.drawable.ic_photo_black_192dp)
                .into(imageView2);

        Glide.with(this)
                .load(myProductItem.getProdImage2())
                .apply(requestOptions)
                .error(R.drawable.ic_photo_black_192dp)
                .into(imageView3);

        Glide.with(this)
                .load(myProductItem.getProdImage3())
                .apply(requestOptions)
                .error(R.drawable.ic_photo_black_192dp)
                .into(imageView4);

        barList = new ArrayList<>();
        productSaleList = new ArrayList<>();
        initMonthlySaleList();
        recyclerViewMonthlyGraph=(RecyclerView)findViewById(R.id.recycler_view_monthly_graph);
        recyclerViewMonthlyGraph.setItemAnimator(new DefaultItemAnimator());
        recyclerViewMonthlyGraph.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManagerMonthlyGraph=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        recyclerViewMonthlyGraph.setLayoutManager(layoutManagerMonthlyGraph);
        monthlyGraphAdapter=new MonthlyGraphAdapter(this,barList,1);
       ((MonthlyGraphAdapter) monthlyGraphAdapter).setTotalTarget(25000);
        recyclerViewMonthlyGraph.setAdapter(monthlyGraphAdapter);

        myReviewList = new ArrayList<>();
        recyclerViewReview=(RecyclerView)findViewById(R.id.recycler_view_review);
        recyclerViewReview.setItemAnimator(new DefaultItemAnimator());
        recyclerViewReview.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManagerReview=new LinearLayoutManager(this);
        recyclerViewReview.setLayoutManager(layoutManagerReview);
        myReviewAdapter=new MyReviewAdapter(this,myReviewList,"productReview");
        recyclerViewReview.setAdapter(myReviewAdapter);
        recyclerViewReview.setNestedScrollingEnabled(false);

        checkBoxMultipleBarcode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });

        buttonAddMultipleBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                multipleBarcodeBottomFragment = new MultipleBarcodeBottomFragment();
                multipleBarcodeBottomFragment.setProdCode(myProductItem.getProdCode());
                multipleBarcodeBottomFragment.setProdId(myProductItem.getProdId());
                multipleBarcodeBottomFragment.show(getSupportFragmentManager(), "Multiple Barcode Bottom Sheet");
            }
        });

        textViewDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DescBottomFragment descBottomFragment = new DescBottomFragment();
                descBottomFragment.setDesc(myProductItem.getProdDesc());
                descBottomFragment.show(getSupportFragmentManager(), "Description Bottom Sheet");
            }
        });

        tvReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DescBottomFragment descBottomFragment = new DescBottomFragment();
                descBottomFragment.setDesc(myProductItem.getProdDesc());
                descBottomFragment.show(getSupportFragmentManager(), "Description Bottom Sheet");
            }
        });

        if(flag!=null && flag.equals("shoppurs_product")){
            recyclerViewMonthlyGraph.setVisibility(View.GONE);
            // recyclerViewOffers.setVisibility(View.GONE);
            recyclerViewReview.setVisibility(View.GONE);
            buttonAddMultipleBarcode.setVisibility(View.GONE);
        }

        sizeSpinnerList = new ArrayList<>();
        spinnerSize = findViewById(R.id.spinner_size);
        tvUnitSizeColor = findViewById(R.id.tvUnitSizeColor);
        rlProductSpecificationLayout = findViewById(R.id.rl_product_specification_layout);
        specificationType = UNIT;

        sizeSpinnerAdapter = new ArrayAdapter<String>(this, R.layout.simple_dropdown_list_item, sizeSpinnerList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0){
                    return false;
                }else{
                    return true;
                }
            }
            @Override
            public View getView(int position, View convertView,
                                ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(getResources().getColor(R.color.grey500));
                }else{
                    if(isDarkTheme){
                        tv.setTextColor(getResources().getColor(R.color.white));
                    }else{
                        tv.setTextColor(getResources().getColor(R.color.primary_text_color));
                    }
                }
                return view;
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                if(isDarkTheme){
                    view.setBackgroundColor(getResources().getColor(R.color.dark_color));
                }else{
                    view.setBackgroundColor(getResources().getColor(R.color.white));
                }
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(getResources().getColor(R.color.grey500));
                }else{
                    if(isDarkTheme){
                        tv.setTextColor(getResources().getColor(R.color.white));
                    }else{
                        tv.setTextColor(getResources().getColor(R.color.primary_text_color));
                    }
                }
                tv.setPadding(20,20,20,20);
                return view;
            }
        };

        spinnerSize.setAdapter(sizeSpinnerAdapter);

        spinnerSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i > 0){
                    colorList.clear();
                    ProductSize size = (ProductSize)sizeList.get(i);
                    for(ProductColor color : size.getProductColorList()){
                        colorList.add(color);
                    }
                    colorAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        tvUnitSizeColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rlProductSpecificationLayout.setVisibility(View.VISIBLE);
            }
        });

        ImageView ivClear = findViewById(R.id.iv_clear);
        ivClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rlProductSpecificationLayout.setVisibility(View.GONE);
            }
        });

        TextView tvUnit = findViewById(R.id.tvUnit);
        tvUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                specificationType = UNIT;
                relative_size.setVisibility(View.GONE);
                initUnitList();
            }
        });

        TextView tvSize = findViewById(R.id.tvSize);
        tvSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                specificationType = SIZE;
                relative_size.setVisibility(View.VISIBLE);
                initSizeList();
            }
        });

        TextView tvColor = findViewById(R.id.tvColor);
        tvColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                specificationType = COLOR;
                relative_size.setVisibility(View.GONE);
                initColorList();
            }
        });

        initUnitColorSizeList();
        initUnitList();

        setReviews();
        //getOffers();
        getSaleData();

        getRatingsData();

    }



    private void setReviews(){
        Map<String,String> params=new HashMap<>();
        params.put("shopId",sharedPreferences.getString(Constants.USER_ID,""));
        params.put("prodId",""+myProductItem.getProdId());
        params.put("limit",""+limit);
        params.put("offset",""+offset);
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+Constants.REVIEW_LIST;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"reviewList");

    }

    private void getOffers(){

    }

    private void getSaleData(){
        Map<String,String> params=new HashMap<>();
        params.put("id",""+myProductItem.getProdId());
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+Constants.PRODUCT_SALE_DATA;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"productSaleData");
    }

    private void getRatingsData(){
        Map<String,String> params=new HashMap<>();
        params.put("code",""+myProductItem.getProdCode());
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+Constants.PRODUCT_RATINGS_DATA;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"productRatingsData");
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {

        try {
            if (apiName.equals("reviewList")) {
                if (response.getBoolean("status")) {
                    JSONArray dataArray = response.getJSONArray("result");
                    JSONObject jsonObject = null;
                    int len = dataArray.length();
                    MyReview myReview= null;
                    myReviewList.clear();
                    for (int i = 0; i < len; i++) {
                        jsonObject = dataArray.getJSONObject(i);
                        myReview = new MyReview();
                        myReview.setUserName(jsonObject.getString("customerName"));
                        myReview.setDateTime(jsonObject.getString("createdDate"));
                        myReview.setRating(Float.parseFloat(jsonObject.getString("rating")));
                        myReview.setReview(jsonObject.getString("reviewMessage"));
                        myReviewList.add(myReview);
                    }

                    if(len == 5){
                        btnLoadMoreReviews.setVisibility(View.VISIBLE);
                    }else{
                        btnLoadMoreReviews.setVisibility(View.GONE);
                    }

                    if(len == 0){
                        textViewNoReviews.setVisibility(View.VISIBLE);
                        recyclerViewReview.setVisibility(View.GONE);
                    }else{
                        textViewNoReviews.setVisibility(View.GONE);
                        recyclerViewReview.setVisibility(View.VISIBLE);
                    }

                    if(myReviewList.size()==0){
                        recyclerViewReview.setVisibility(View.GONE);
                        textViewNoReviews.setVisibility(View.VISIBLE);
                    }

                    myReviewAdapter.notifyDataSetChanged();
                }
            }else if (apiName.equals("productSaleData")) {
                if (response.getBoolean("status")) {
                    if(response.getString("result") == null || response.getString("result").equals("null")){
                        setMonthlyBar();
                    }else{
                        JSONArray dataArray = response.getJSONArray("result");
                        JSONObject jsonObject = null;
                        int len = dataArray.length();
                        float totalAmount = 0;
                        double maxValue = 0;
                        for (int i = 0; i < len; i++) {
                            jsonObject = dataArray.getJSONObject(i);
                            totalAmount = totalAmount + (float) jsonObject.getDouble("amount");
                            updateMonthlySaleList(Utility.parseMonth(jsonObject.getString("orderDate"),
                                    "yyyy-MM-dd HH:mm:ss"), jsonObject.getInt("amount"));

                            if(maxValue <  jsonObject.getDouble("amount")){
                                maxValue =  jsonObject.getDouble("amount");
                            }
                        }

                        textTotalSale.setText(Utility.numberFormat(totalAmount));
                        if(maxValue == 0f){
                            ((MonthlyGraphAdapter) monthlyGraphAdapter).setTotalTarget(50000);
                        }else{
                            ((MonthlyGraphAdapter) monthlyGraphAdapter).setTotalTarget((float)maxValue);
                        }


                        if(len == 0){
                            setNullMonthlyBar();
                        }else{
                            setMonthlyBar();
                        }
                    }
                }
            }else if (apiName.equals("productRatingsData")) {
                if (response.getBoolean("status")) {
                    if(response.getString("result") == null || response.getString("result").equals("null")){
                        tvStarRatings.setText("0.0");
                        tvNumRatings.setText("0 Ratings");
                    }else{
                        JSONObject jsonObject = response.getJSONObject("result");
                        tvStarRatings.setText(String.format("%.01f",(float)jsonObject.getDouble("ratings")));
                        tvNumRatings.setText(jsonObject.getInt("ratingHits")+" Ratings");
                    }

                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
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
            bar.setSaleValue(getSaleData(monthName));
            Log.i(TAG,"Sale value "+bar.getSaleValue());
            bar.setSaleAchievedValue(5000);
            bar.setSaleTargetValue(8000);
            bar.setBudget(10000);
            bar.setBarColor(getBarColor(month));
            barList.add(bar);
        }

        monthlyGraphAdapter.notifyDataSetChanged();
    }

    public void setNullMonthlyBar(){
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
            bar.setSaleValue(0);
            Log.i(TAG,"Sale value "+bar.getSaleValue());
            bar.setSaleAchievedValue(5000);
            bar.setSaleTargetValue(8000);
            bar.setBudget(10000);
            bar.setBarColor(getBarColor(month));
            barList.add(bar);
        }

        monthlyGraphAdapter.notifyDataSetChanged();
    }

    public int getSaleData(String monthName){
        int value = 0;
        for(Bar bar : productSaleList){
            if(bar.getName().equals(monthName)){
                value = bar.getSaleValue();
                break;
            }
        }

        return value;
    }

    public void initMonthlySaleList(){
        Calendar calendarTemp =Calendar.getInstance();
        productSaleList.clear();
        for(int i=0;i<12;i++){
            if(i > 0)
                calendarTemp.add(Calendar.MONTH, -1);

            int month=calendarTemp.get(Calendar.MONTH);
            int year=calendarTemp.get(Calendar.YEAR);
            String monthName=getMonth(month);
            Bar bar=new Bar();
            bar.setName(monthName);
            bar.setSaleValue(0);
            bar.setBarColor(getBarColor(month));
            productSaleList.add(bar);
        }
    }

    public void updateMonthlySaleList(String monthName,int value){
        for(Bar bar : productSaleList){
            if(bar.getName().equals(monthName)){
                bar.setSaleValue(value);
                //bar.setSaleValue(10000);
                break;
            }
        }
    }

    public String getMonth(int position){
        String[] monthInText={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

        return monthInText[position];
    }

    private int getBarColor(int month){
        int[] barColor={getResources().getColor(R.color.light_blue500),
                getResources().getColor(R.color.yellow500),getResources().getColor(R.color.green500),
                getResources().getColor(R.color.orange500),getResources().getColor(R.color.red_500),
                getResources().getColor(R.color.teal_500),getResources().getColor(R.color.cyan500),
                getResources().getColor(R.color.deep_orange500),getResources().getColor(R.color.blue500),
                getResources().getColor(R.color.purple500),getResources().getColor(R.color.amber500),
                getResources().getColor(R.color.light_green500)};

        return barColor[month];
    }

    private void initUnitColorSizeList(){
        unitList = new ArrayList<>();
        try {
            for(ProductUnit unit : myProductItem.getProductUnitList()){
                unitList.add(unit);
                Log.i(TAG,"unit added "+unit.getUnitName()+" status "+unit.getStatus());
            }
        }catch (NullPointerException npe){
            npe.fillInStackTrace();
        }

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        unitAdapter=new SpecificationAdapter(this,unitList,"unitDetail");
        recyclerView.setAdapter(unitAdapter);
      //  unitAdapter.setMyItemTypeClickListener(this);

        sizeList = new ArrayList<>();

        try {
            for(ProductSize size : myProductItem.getProductSizeList()){
                sizeList.add(size);
            }
        }catch (NullPointerException npe){
            npe.fillInStackTrace();
        }


        sizeAdapter=new SpecificationAdapter(this,sizeList,"sizeDetail");
       // sizeAdapter.setMyItemTypeClickListener(this);

        sizeSpinnerList.clear();
        sizeSpinnerList.add("Select Size");
        ProductSize productSize = null;
        for(Object ob : sizeList){
            productSize = (ProductSize)ob;
            sizeSpinnerList.add(productSize.getSize());
        }

        sizeSpinnerAdapter.notifyDataSetChanged();
        colorList = new ArrayList<>();
        colorAdapter=new SpecificationAdapter(this,colorList,"colorDetail");
        //colorAdapter.setMyItemTypeClickListener(this);

    }
    private void initUnitList(){
        Log.i(TAG,"unit size "+unitList.size());
        recyclerView.setAdapter(unitAdapter);
        //unitAdapter.notifyDataSetChanged();
    }

    private void initSizeList(){
        recyclerView.setAdapter(sizeAdapter);
       // sizeAdapter.notifyDataSetChanged();
    }

    private void initColorList(){
        recyclerView.setAdapter(colorAdapter);
       // colorAdapter.notifyDataSetChanged();
    }

}
