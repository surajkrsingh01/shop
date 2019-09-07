package com.shoppursshop.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.shoppursshop.R;
import com.shoppursshop.adapters.SimpleItemAdapter;
import com.shoppursshop.interfaces.MyItemClickListener;
import com.shoppursshop.models.MySimpleItem;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.DialogAndToast;
import com.shoppursshop.utilities.Utility;

import java.util.ArrayList;
import java.util.List;

public class ChooseDeviceActivity extends NetworkBaseActivity implements MyItemClickListener {

    private RecyclerView recyclerView;
    private SimpleItemAdapter itemAdapter;
    private List<Object> itemList;
    private int selectedPosition = -1,preSelectedPosition=-1;
    private String flag;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_device);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initFooterAction(this);
        init();
    }

    private void init(){
        flag = getIntent().getStringExtra("flag");

        itemList = new ArrayList<>();
        MySimpleItem item = new MySimpleItem();
        item.setName("Android");
        itemList.add(item);
        item = new MySimpleItem();
        item.setName("Android POS(N910)");
        itemList.add(item);

        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        // staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        itemAdapter=new SimpleItemAdapter(this,itemList,"simpleList");
        itemAdapter.setMyItemClickListener(this);
        recyclerView.setAdapter(itemAdapter);

        RelativeLayout relativeLayoutContinue = findViewById(R.id.relative_footer_action);
        relativeLayoutContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedPosition == -1){
                    DialogAndToast.showDialog("Please choose device",ChooseDeviceActivity.this);
                    return;
                }
                MySimpleItem item = (MySimpleItem) itemList.get(selectedPosition);
                if(item.getName().equals("Android")){
                    editor.putString(Constants.ANDROID_DEVICE_TYPE,"Android");
                }else{
                    editor.putString(Constants.ANDROID_DEVICE_TYPE,"N910");
                }
                editor.commit();

                if(flag.equals("register")){
                    Intent intent = new Intent(ChooseDeviceActivity.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else{
                     ChooseDeviceActivity.this.finish();
                }

            }
        });

    }

    @Override
    public void onResume(){
        super.onResume();
        if(selectedPosition != -1){
            MySimpleItem item = (MySimpleItem) itemList.get(selectedPosition);
            item.setSelected(true);
            itemAdapter.notifyItemChanged(selectedPosition);
        }

    }

    @Override
    public void onItemClicked(int position) {
        if(selectedPosition != position){
            selectedPosition = position;
            MySimpleItem item = (MySimpleItem) itemList.get(selectedPosition);
            item.setSelected(true);
            itemAdapter.notifyItemChanged(selectedPosition);
            if(preSelectedPosition != -1){
                MySimpleItem preItem = (MySimpleItem) itemList.get(preSelectedPosition);
                preItem.setSelected(false);
                itemAdapter.notifyItemChanged(preSelectedPosition);
            }
            preSelectedPosition = position;
        }
    }
}
