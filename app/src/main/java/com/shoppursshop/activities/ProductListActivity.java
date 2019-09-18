package com.shoppursshop.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shoppursshop.R;
import com.shoppursshop.adapters.OrderAdapter;
import com.shoppursshop.adapters.ProductAdapter;
import com.shoppursshop.fragments.BottomSearchFragment;
import com.shoppursshop.interfaces.MyImageClickListener;
import com.shoppursshop.models.HomeListItem;
import com.shoppursshop.models.MyHeader;
import com.shoppursshop.models.MyProduct;
import com.shoppursshop.models.MyProductItem;
import com.shoppursshop.utilities.DialogAndToast;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends BaseActivity implements MyImageClickListener {

    private RecyclerView recyclerView;
    private ProductAdapter myItemAdapter;
    private List<Object> itemList,originalList;
    private TextView textViewSubCatName,textViewError;
    private Button btnAddProduct;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private String catName,subCatName,subCatID;

    private BottomSearchFragment bottomSearchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.grey900), PorterDuff.Mode.SRC_ATOP);

        subCatName = getIntent().getStringExtra("subCatName");
        subCatID = getIntent().getStringExtra("subCatID");

        Log.d("subCatId ", subCatID);

        textViewSubCatName = findViewById(R.id.text_sub_cat);
        btnAddProduct = findViewById(R.id.btn_add);
        textViewSubCatName.setText(subCatName);

        itemList = dbHelper.getProducts(subCatID,limit,offset);
        originalList = dbHelper.getProducts(subCatID,limit,offset);
        HomeListItem myItem = new HomeListItem();
        myItem.setTitle("Products");
      //  myItem.setDesc(subCatName+" Products");
        myItem.setDesc("Store Products");
        myItem.setType(0);
       // itemList.add(0,myItem);

        MyHeader myHeader = new MyHeader();
        myHeader.setTitle(subCatName);
        myHeader.setType(1);
      //  itemList.add(1,myHeader);

        swipeRefreshLayout=findViewById(R.id.swipe_refresh);
        progressBar=findViewById(R.id.progress_bar);
        textViewError = findViewById(R.id.text_error);
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        final RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        // staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        myItemAdapter=new ProductAdapter(this,itemList,"productList");
        myItemAdapter.setMyImageClickListener(this);
        myItemAdapter.setFlag(getIntent().getStringExtra("flag"));
        myItemAdapter.setSubCatName(subCatName);
        recyclerView.setAdapter(myItemAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(isScroll){
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    Log.i(TAG,"visible "+visibleItemCount+" total "+totalItemCount);
                    pastVisibleItems = ((LinearLayoutManager)layoutManager).findLastVisibleItemPosition();
                    Log.i(TAG,"total visible "+(visibleItemCount+pastVisibleItems));

                    if (!loading) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            loading = true;
                            offset = limit + offset;
                            List<Object> nextItemList = dbHelper.getProducts(subCatID,limit,offset);
                            for(Object ob : nextItemList){
                                itemList.add(ob);
                                originalList.add(ob);
                            }
                            if(nextItemList.size() < limit){
                                isScroll = false;
                            }
                            if(nextItemList.size() > 0){
                                recyclerView.post(new Runnable() {
                                    public void run() {
                                        myItemAdapter.notifyItemRangeInserted(offset,limit);
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

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductListActivity.this,AddProductActivity.class);
                intent.putExtra("flag","manual");
                intent.putExtra("type",getIntent().getStringExtra("flag"));
                startActivity(intent);
            }
        });

        ImageView imageViewSearch = findViewById(R.id.image_search);
        imageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSearchFragment = new BottomSearchFragment();
                bottomSearchFragment.setCallingActivityName("productList");
                Bundle bundle = new Bundle();
                bundle.putString("flag","searchProduct");
                bottomSearchFragment.setArguments(bundle);
                bottomSearchFragment.show(getSupportFragmentManager(), "Search Product Bottom Sheet");
            }
        });

        initFooter(this,1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i(TAG,"Result received");

        if (requestCode == 2){
            if(data != null){
                itemList.clear();
                originalList.clear();
                offset = 0;
                List<Object> itemTempList = dbHelper.getProducts(subCatID,limit,offset);
                for(Object ob : itemTempList){
                    itemList.add(ob);
                    originalList.add(ob);
                }

                myItemAdapter.notifyDataSetChanged();
            }
        }
    }


    private void filterProduct(String query){

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
        if (id == R.id.action_search) {
            bottomSearchFragment = new BottomSearchFragment();
            bottomSearchFragment.setCallingActivityName("productList");
            Bundle bundle = new Bundle();
            bundle.putString("flag","searchProduct");
            bottomSearchFragment.setArguments(bundle);
            bottomSearchFragment.show(getSupportFragmentManager(), "Search Product Bottom Sheet");
            return true;
        }else if (id == R.id.action_favourite) {
            return true;
        }else if (id == android.R.id.home) {
            super.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onImageClicked(int position, int type, View view) {
        MyProductItem item = (MyProductItem) itemList.get(position);

        showImageDialog(item.getProdImage1(),view);
    }
}
