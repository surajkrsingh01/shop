package com.shoppursshop.activities.settings;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.shoppursshop.R;
import com.shoppursshop.activities.NetworkBaseActivity;
import com.shoppursshop.activities.settings.profile.DeliveryActivity;
import com.shoppursshop.adapters.SimpleItemAdapter;
import com.shoppursshop.interfaces.MyItemClickListener;
import com.shoppursshop.interfaces.MyListItemClickListener;
import com.shoppursshop.models.MySimpleItem;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.DialogAndToast;
import com.shoppursshop.utilities.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyCategoriesActivity extends NetworkBaseActivity implements MyItemClickListener {

    private RecyclerView recyclerView;
    private List<Object> itemList;
    private SimpleItemAdapter itemAdapter;
    private int counter;
    private Menu menu;
    private TextView tv_top_parent;
    private ImageView ivDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_categories);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setToolbarTheme();

        ivDelete= findViewById(R.id.action_delete);
        itemList = dbHelper.getCategories();
        recyclerView= findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        // staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        itemAdapter=new SimpleItemAdapter(this,itemList,"simpleList");
        itemAdapter.setMyItemClickListener(this);
        recyclerView.setAdapter(itemAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMyBothDialog("Your action will delete the categories,sub categories and related products and affect your stock control. Do you wish to continue?","Cancel","Ok");
            }
        });

        tv_top_parent = findViewById(R.id.text_left_label);
        tv_top_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyCategoriesActivity.this, SettingActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();

    }

    @Override
    public void onDialogPositiveClicked(){

        MySimpleItem item = null;
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        StringBuilder sb = new StringBuilder();
        try {
            for(Object ob : itemList){
                item = (MySimpleItem)ob;

                if(item.isSelected()){
                    sb.append(item.getId());
                    sb.append(",");
                }
                //jsonArray.put(item.getId());
            }
            String catIds = sb.toString();
            catIds = catIds.substring(0,catIds.length()-1);
            jsonObject.put("catIds",catIds);
            jsonObject.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
            jsonObject.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
            jsonObject.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        }catch (JSONException e){
            e.printStackTrace();
        }

        String url=getResources().getString(R.string.url)+Constants.DELETE_CATEGORY;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,jsonObject,"deleteCategory");
    }

    @Override
    public void onDialogNegativeClicked(){

    }


    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {
        Log.d(TAG, response.toString());

        try {
            if(apiName.equals("deleteCategory")){

                if(response.getBoolean("status")){
                    MySimpleItem item = null;
                    for(Object ob : itemList){
                        item = (MySimpleItem)ob;
                        if(item.isSelected()){
                            dbHelper.deleteCategory(item.getId());
                            dbHelper.deleteSubCategory(item.getId());
                            dbHelper.deleteProducts(item.getId());
                        }
                    }

                    resetList();

                }else{
                    DialogAndToast.showDialog(response.getString("message"), MyCategoriesActivity.this);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.delete_menu, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {
            showMyBothDialog("Are you sure want to delete selected categories","Cancel","Ok");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClicked(int position) {
        MySimpleItem item = (MySimpleItem) itemList.get(position);
        if(item.isSelected()){
            counter--;
            item.setSelected(false);
        }else{
            counter++;
            item.setSelected(true);
        }

        if(counter == 0){
            menu.getItem(0).setVisible(false);
            ivDelete.setVisibility(View.GONE);
           // setTitle(getResources().getString(R.string.title_activity_my_categories));
        }else if(counter == 1){
            menu.getItem(0).setVisible(true);
            ivDelete.setVisibility(View.VISIBLE);
           // setTitle(""+counter);
        }else{
          //  setTitle(""+counter);
        }

        itemAdapter.notifyItemChanged(position);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i(TAG,"Result received");

        if (requestCode == 2){
            if(data != null){
                resetList();
            }
        }
    }

    private void resetList(){
        List<Object> itemCatList = dbHelper.getCategories();
        itemList.clear();
        for(Object ob : itemCatList){
            itemList.add(ob);
        }

        itemAdapter.notifyDataSetChanged();
    }
}
