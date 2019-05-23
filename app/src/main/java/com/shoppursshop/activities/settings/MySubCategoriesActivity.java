package com.shoppursshop.activities.settings;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.shoppursshop.R;
import com.shoppursshop.activities.NetworkBaseActivity;
import com.shoppursshop.adapters.SimpleItemAdapter;
import com.shoppursshop.interfaces.MyItemClickListener;
import com.shoppursshop.interfaces.MyLevelItemClickListener;
import com.shoppursshop.models.CatListItem;
import com.shoppursshop.models.MySimpleItem;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.DialogAndToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MySubCategoriesActivity extends NetworkBaseActivity implements MyItemClickListener, MyLevelItemClickListener {

    private RecyclerView recyclerView;
    private List<Object> itemList;
    private List<Object> itemCatList,selectedItemList;
    private SimpleItemAdapter itemAdapter;
    private int counter;
    private Menu menu;
    private ImageView ivDelete;

    private String catIds;
    private TextView tv_top_parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sub_categories);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setToolbarTheme();

      /*  itemCatList = dbHelper.getCategories();
        MySimpleItem item = null;
        CatListItem myItem = null;
        for(Object ob : itemCatList){
            item = (MySimpleItem) ob;
            Log.i(TAG,"Cat "+item.getName());
            if(catIds == null){
                catIds = ""+item.getId();
            }else{
                catIds = catIds+","+item.getId();
            }

            myItem = new CatListItem();
            myItem.setSelectingAll(true);
            myItem.setType(1);
            myItem.setId(item.getId());
            myItem.setTitle(item.getName());
            myItem.setItemList(dbHelper.getCatSubCategories(""+item.getId()));
            itemList.add(myItem);
        }*/

        ivDelete= findViewById(R.id.action_delete);
        itemList = dbHelper.getSubCategories();
        recyclerView= findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        // staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        itemAdapter=new SimpleItemAdapter(this,itemList,"simpleList");
        itemAdapter.setMyItemClickListener(this);
        itemAdapter.setMyLevelItemClickListener(this);
        recyclerView.setAdapter(itemAdapter);

        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMyBothDialog("Your action will delete the sub categories and related products and affect your stock control. Do you wish to continue?","Cancel","Ok");
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MySubCategoriesActivity.this, AddSubCatActivity.class);
                startActivityForResult(intent,2);
            }
        });
        tv_top_parent = findViewById(R.id.text_left_label);
        tv_top_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MySubCategoriesActivity.this, SettingActivity.class));
                finish();
            }
        });
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
            jsonObject.put("subCatIds",catIds);
            jsonObject.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
            jsonObject.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
            jsonObject.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        }catch (JSONException e){
            e.printStackTrace();
        }

        String url=getResources().getString(R.string.url)+Constants.DELETE_SUB_CATEGORY;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,jsonObject,"deleteSubCategory");
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {
        Log.d(TAG, response.toString());

        try {
            if(apiName.equals("deleteSubCategory")){

                if(response.getBoolean("status")){
                    MySimpleItem item = null;
                    for(Object ob : itemList){
                        item = (MySimpleItem)ob;
                        if(item.isSelected()){
                            dbHelper.deleteSubCategoryById(item.getId());
                            dbHelper.deleteProductsBySubCatId(item.getId());
                        }
                    }

                    resetList();

                }else{
                    DialogAndToast.showDialog(response.getString("message"), MySubCategoriesActivity.this);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDialogNegativeClicked(){

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
            showMyBothDialog("Are you sure want to delete selected sub categories","Cancel","Ok");
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
    public void onLevelItemClicked(int itemPosition, int level) {

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
        List<Object> itemCatList = dbHelper.getSubCategories();
        itemList.clear();
        for(Object ob : itemCatList){
            itemList.add(ob);
        }

        itemAdapter.notifyDataSetChanged();
    }
}
