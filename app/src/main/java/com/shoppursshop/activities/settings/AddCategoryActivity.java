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
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.shoppursshop.R;
import com.shoppursshop.activities.NetworkBaseActivity;
import com.shoppursshop.activities.settings.profile.AddressActivity;
import com.shoppursshop.adapters.SimpleItemAdapter;
import com.shoppursshop.interfaces.MyItemClickListener;
import com.shoppursshop.models.MySimpleItem;
import com.shoppursshop.utilities.ConnectionDetector;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.DialogAndToast;
import com.shoppursshop.utilities.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddCategoryActivity extends NetworkBaseActivity implements MyItemClickListener {

    private RecyclerView recyclerView;
    private SimpleItemAdapter itemAdapter;
    private List<Object> itemList;
    private List<Object> selectedItemList;
    private Button btnSelectAll;
    private RelativeLayout relative_footer_action;
    private boolean isSelectingAll = true;

    private TextView textViewNoData,textViewSelectCatLabel, tv_top_parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setToolbarTheme();
        initFooterAction(this);
        init();

    }

    private void init(){
        relative_footer_action = findViewById(R.id.relative_footer_action);
        btnSelectAll = findViewById(R.id.btn_select_all);
        textViewNoData = findViewById(R.id.text_no_data);
        textViewSelectCatLabel = findViewById(R.id.text_select_category_label);

        itemList = new ArrayList<>();
        selectedItemList = new ArrayList<>();
       /* MySimpleItem item = new MySimpleItem();
        item.setName("Grocery");
        itemList.add(item);
        item = new MySimpleItem();
        item.setName("Stationary");
        itemList.add(item);*/

        recyclerView= findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        // staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        itemAdapter=new SimpleItemAdapter(this,itemList,"simpleList");
        itemAdapter.setMyItemClickListener(this);
        recyclerView.setAdapter(itemAdapter);

        if(ConnectionDetector.isNetworkAvailable(this)){
            getCategories();
        }


        relative_footer_action.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                MySimpleItem item1 = null;
                for(Object ob : itemList){
                    item1 = (MySimpleItem) ob;
                    if(item1.isSelected())
                        selectedItemList.add(ob);
                }
                if(selectedItemList.size() == 0){
                    DialogAndToast.showDialog("Please select Category", AddCategoryActivity.this);
                    return;
                }

                createCategory();
                //  mListener.onFragmentInteraction(selectedItemList,RegisterActivity.CATEGORY);
            }
        });

        btnSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MySimpleItem item = null;
                for(Object ob : itemList){
                    item = (MySimpleItem) ob;
                    if(isSelectingAll){
                        item.setSelected(true);
                    }else{
                        item.setSelected(false);
                    }

                }

                if(isSelectingAll){
                    btnSelectAll.setText("Unselect All");
                    isSelectingAll = false;
                }else{
                    btnSelectAll.setText("Select All");
                    isSelectingAll = true;
                }

                itemAdapter.notifyDataSetChanged();
            }
        });

        tv_top_parent = findViewById(R.id.text_left_label);
        tv_top_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddCategoryActivity.this, SettingActivity.class));
                finish();
            }
        });
    }

    private void getCategories(){
        String url=getResources().getString(R.string.url)+ Constants.GET_CATEGORY;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(),"categories");
    }

    private void createCategory(){
        JSONArray dataArray = new JSONArray();
        JSONObject dataObject = null;
        MySimpleItem category = null;
        for(Object ob: selectedItemList){
            category = (MySimpleItem)ob;
            dataObject = new JSONObject();
            try {
                dataObject.put("retId",sharedPreferences.getString(Constants.USER_ID,""));
                dataObject.put("retCatId",""+category.getId());
                dataObject.put("catName",category.getName());
                dataObject.put("imageUrl",category.getImage());
                dataObject.put("delStatus","N");
                dataObject.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
                dataObject.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
                dataObject.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
                dataObject.put("createdBy","Vipin Dhama");
                dataObject.put("updatedBy","Vipin Dhama");
                dataArray.put(dataObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String url=getResources().getString(R.string.url)+Constants.CREATE_CATEGORY;
        showProgress(true);
        jsonArrayV2ApiRequest(Request.Method.POST,url,dataArray,"addCategoryRetailer");
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {
        Log.d(TAG, response.toString());

        try {
            if(apiName.equals("categories")){

                if(response.getBoolean("status")){
                    JSONArray dataArray = response.getJSONArray("result");
                    JSONObject jsonObject =null;
                    MySimpleItem item = null;
                    int len = dataArray.length();
                    for(int i=0; i<len; i++){
                        jsonObject = dataArray.getJSONObject(i);
                        item = new MySimpleItem();
                        item.setId(jsonObject.getInt("catId"));
                        item.setName(jsonObject.getString("catName"));
                        item.setImage(jsonObject.getString("imageUrl"));
                        if(!dbHelper.isCatExist(""+item.getId()))
                            itemList.add(item);
                    }

                    if(itemList.size() > 0){
                        showNoData(false);
                        itemAdapter.notifyDataSetChanged();
                    }else{
                        showNoData(true);
                    }

                }else{
                    DialogAndToast.showDialog(response.getString("message"), AddCategoryActivity.this);
                }
            }else if(apiName.equals("addCategoryRetailer")){
                if(response.getBoolean("status")){
                    MySimpleItem category = null;
                    for(Object ob: selectedItemList){
                        category = (MySimpleItem)ob;
                        Log.d(TAG,""+ dbHelper.isCatExist(""+category.getId()));
                        if(!dbHelper.isCatExist(""+category.getId())) {
                            dbHelper.addCategory(category, Utility.getTimeStamp(), Utility.getTimeStamp());
                            showMyDialog("Category added successfully");
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDialogPositiveClicked(){
        Intent intent = new Intent();
        intent.putExtra("flag","categoryAdded");
        setResult(-1,intent);
        finish();
    }

    @Override
    public void onItemClicked(int position) {
        MySimpleItem item = (MySimpleItem) itemList.get(position);
        if(item.isSelected()){
            item.setSelected(false);
        }else{
            item.setSelected(true);
        }

        itemAdapter.notifyItemChanged(position);
    }

    private void showNoData(boolean show){
        if(show){
            recyclerView.setVisibility(View.GONE);
            textViewNoData.setVisibility(View.VISIBLE);
            textViewSelectCatLabel.setVisibility(View.GONE);
            btnSelectAll.setVisibility(View.GONE);
        }else{
            recyclerView.setVisibility(View.VISIBLE);
            textViewNoData.setVisibility(View.GONE);
            textViewSelectCatLabel.setVisibility(View.GONE);
            btnSelectAll.setVisibility(View.VISIBLE);
        }
    }
}
