package com.shoppursshop.activities.settings;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shoppursshop.R;
import com.shoppursshop.activities.BaseActivity;
import com.shoppursshop.adapters.ExpiredProductAdapter;
import com.shoppursshop.adapters.ProductAdapter;
import com.shoppursshop.adapters.ReorderLevelAdapter;
import com.shoppursshop.fragments.RlevelExpiredProductFragment;
import com.shoppursshop.interfaces.MyImageClickListener;
import com.shoppursshop.models.MyProduct;
import com.shoppursshop.models.MyProductItem;

import java.util.ArrayList;
import java.util.List;

public class RlevelAndExpiredProductActivity extends BaseActivity implements MyImageClickListener {

    private TextView text_sub_header;
    private RecyclerView recyclerView;
    private ExpiredProductAdapter expiredProductAdapter;
    private ReorderLevelAdapter reorderLevelAdapter;
    private List<MyProductItem> itemList;
    private RlevelExpiredProductFragment bottomSearchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rlevel_and_expired_product);
        init();
    }

    private void init(){
        itemList = new ArrayList<>();
        text_sub_header = findViewById(R.id.text_sub_header);
        bottomSearchFragment = RlevelExpiredProductFragment.newInstance("","",colorTheme);
        bottomSearchFragment.show(getSupportFragmentManager(), "Reorder Level / Expired Products Bottom Sheet");

        ImageView imageViewSearch = findViewById(R.id.image_search);

        imageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSearchFragment = RlevelExpiredProductFragment.newInstance("","",colorTheme);
                bottomSearchFragment.show(getSupportFragmentManager(), "Reorder Level / Expired Products Bottom Sheet");
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        expiredProductAdapter=new ExpiredProductAdapter(this,itemList);
        reorderLevelAdapter=new ReorderLevelAdapter(this,itemList);
        expiredProductAdapter.setMyImageClickListener(this);
        reorderLevelAdapter.setMyImageClickListener(this);

    }

    public void onSubmitCalled(String startDate,String endDate,int type){
        Log.i(TAG,"start date "+startDate+" end date "+endDate+" type "+type);
        offset = 0;
        if(text_sub_header.getVisibility() == View.GONE){
            text_sub_header.setVisibility(View.VISIBLE);

        }
        if(type == R.id.rb_expired_products){
            text_sub_header.setText("Expired Products");
            startDate = startDate +" 00:00:00";
            endDate = endDate +" 00:00:00";
            List<MyProductItem> itemExpiredList = dbHelper.getExpiredProducts(startDate,endDate,limit,offset);
            itemList.clear();
            itemList.addAll(itemExpiredList);
            recyclerView.setAdapter(expiredProductAdapter);
            expiredProductAdapter.notifyDataSetChanged();
            Log.i(TAG,"Expired size "+itemList.size());
        }else {
            text_sub_header.setText("Reorder Level");
            List<MyProductItem> itemRLList = dbHelper.getReorderLevelProducts(limit,offset);
            itemList.clear();
            itemList.addAll(itemRLList);
            recyclerView.setAdapter(reorderLevelAdapter);
            reorderLevelAdapter.notifyDataSetChanged();
            Log.i(TAG,"Reorder size "+itemList.size());
        }

    }

    @Override
    public void onImageClicked(int position, int type, View view) {

    }
}
