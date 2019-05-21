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
import com.shoppursshop.activities.RegisterActivity;
import com.shoppursshop.adapters.SimpleItemAdapter;
import com.shoppursshop.interfaces.MyLevelItemClickListener;
import com.shoppursshop.models.CatListItem;
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

public class AddSubCatActivity extends NetworkBaseActivity implements MyLevelItemClickListener {

    private List<Object> itemCatList,selectedItemList;
    private RecyclerView recyclerView;
    private SimpleItemAdapter itemAdapter;
    private List<Object> itemList;
    private TextView textViewNoData;
    private RelativeLayout relative_footer_action;
    private String catIds;
    private TextView tv_top_parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sub_cat);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initFooterAction(this);
        init();
        setToolbarTheme();
    }

    private void init(){
        relative_footer_action = findViewById(R.id.relative_footer_action);
        textViewNoData = findViewById(R.id.text_no_data);

        itemList = new ArrayList<>();
        selectedItemList = new ArrayList<>();
        MySimpleItem item = null;
        itemCatList = dbHelper.getCategories();

        for(Object ob : itemCatList){
            item = (MySimpleItem) ob;
            Log.i(TAG,"Cat "+item.getName());
            if(catIds == null){
                catIds = ""+item.getId();
            }else{
                catIds = catIds+","+item.getId();
            }
        }
        getSubCategories(catIds);

        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        // staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        itemAdapter=new SimpleItemAdapter(this,itemList,"subCatList");
        itemAdapter.setMyLevelItemClickListener(this);
        recyclerView.setAdapter(itemAdapter);

        relative_footer_action.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                MySimpleItem item1 = null;
                CatListItem catListItem = null,mySelectedCatItem = null;
                List<Object> selectedList = null;
                for(Object ob : itemList){
                    catListItem = (CatListItem) ob;
                    selectedList = new ArrayList<>();
                    mySelectedCatItem = new CatListItem();
                    mySelectedCatItem.setId(catListItem.getId());
                    mySelectedCatItem.setTitle(catListItem.getTitle());
                    mySelectedCatItem.setDesc(catListItem.getDesc());
                    for(Object ob1 : catListItem.getItemList()){
                        item1 = (MySimpleItem) ob1;
                        if(item1.isSelected())
                            selectedList.add(ob1);
                    }
                    if(selectedList.size() > 0){
                        mySelectedCatItem.setItemList(selectedList);
                        selectedItemList.add(mySelectedCatItem);
                    }

                }
                if(selectedItemList.size() == 0){
                    DialogAndToast.showDialog("Please select Category",AddSubCatActivity.this);
                    return;
                }
                createSubCategory();
                //mListener.onFragmentInteraction(selectedItemList,RegisterActivity.SUB_CATEGORY);
            }
        });

        tv_top_parent = findViewById(R.id.text_left_label);
        tv_top_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddSubCatActivity.this, SettingActivity.class));
                finish();
            }
        });
    }

    private void getSubCategories(String catIds){
        Map<String,String> params = new HashMap<>();
        params.put("ids",catIds);
        String url=getResources().getString(R.string.url)+ Constants.GET_SUB_CATEGORY;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"sub_categories");
    }

    private void createSubCategory(){
        JSONArray dataArray = new JSONArray();
        JSONObject dataObject = null;
        CatListItem category = null;
        MySimpleItem subCat = null;
        for(Object ob: selectedItemList){
            category = (CatListItem)ob;
            for(Object ob1 : category.getItemList()){
                subCat = (MySimpleItem) ob1;
                dataObject = new JSONObject();
                try {
                    dataObject.put("retId",sharedPreferences.getString(Constants.USER_ID,""));
                    dataObject.put("retCatId",""+category.getId());
                    dataObject.put("retSubCatId",""+subCat.getId());
                    dataObject.put("catName",category.getTitle());
                    dataObject.put("subCatName",subCat.getName());
                    dataObject.put("imageUrl",subCat.getImage());
                    dataObject.put("delStatus","N");
                    dataObject.put("retShopCode",sharedPreferences.getString(Constants.SHOP_CODE,""));
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

        }
        String url=getResources().getString(R.string.url)+Constants.CREATE_SUB_CATEGORY;
        showProgress(true);
        jsonArrayV2ApiRequest(Request.Method.POST,url,dataArray,"addSubCategoryRetailer");
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {

        try {
            if(apiName.equals("sub_categories")){

                if(response.getBoolean("status")){
                    JSONArray dataArray = response.getJSONArray("result");
                    JSONObject jsonObject =null;
                    CatListItem myItem = null;
                    MySimpleItem mySimpleItem = null;
                    List<Object> subCatList = null;
                    int len = dataArray.length();
                    int tempCatId = 0;
                    for(int i=0; i<len; i++){
                        jsonObject = dataArray.getJSONObject(i);
                        if(!dbHelper.isSubCatExist(jsonObject.getString("subCatId")) && dbHelper.isCatExist(jsonObject.getString("catId"))){
                            if(tempCatId == jsonObject.getInt("catId")){
                                myItem = getItem(jsonObject.getInt("catId"));
                                subCatList = myItem.getItemList();
                            }else{
                                myItem = new CatListItem();
                                myItem.setSelectingAll(true);
                                myItem.setType(1);
                                subCatList = new ArrayList<>();
                                tempCatId = jsonObject.getInt("catId");
                                myItem.setId(jsonObject.getInt("catId"));
                                myItem.setTitle(dbHelper.getCategoryName(""+jsonObject.getInt("catId")));
                                myItem.setItemList(subCatList);
                                itemList.add(myItem);
                            }
                            mySimpleItem = new MySimpleItem();
                            mySimpleItem.setId(jsonObject.getInt("subCatId"));
                            mySimpleItem.setName(jsonObject.getString("subCatName"));
                            mySimpleItem.setImage(jsonObject.getString("imageUrl"));
                            mySimpleItem.setPosition(itemList.size());
                            subCatList.add(mySimpleItem);
                        }
                    }
                    if(itemList.size() > 0){
                        showNoData(false);
                        itemAdapter.notifyDataSetChanged();
                    }else{
                        showNoData(true);
                    }
                }else{
                    DialogAndToast.showDialog(response.getString("message"),this);
                }
            }else if(apiName.equals("addSubCategoryRetailer")){
                if(response.getBoolean("status")){
                    CatListItem category = null;
                    MySimpleItem subCat = null;
                    for(Object ob: selectedItemList){
                        category = (CatListItem)ob;
                        for(Object ob1 : category.getItemList()) {
                            subCat = (MySimpleItem) ob1;
                            dbHelper.addSubCategory(subCat,""+category.getId(), Utility.getTimeStamp(),Utility.getTimeStamp());
                        }
                    }

                    showMyDialog("Product added successfully");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

  /*  private boolean isSubCatExist(int id){
        boolean isExist = false;
        MySimpleItem item = null;
        for(Object ob : existSubCatList){
            item = (MySimpleItem)ob;
            if(item.getId() == id){
                isExist = true;
                break;
            }
        }

        return isExist;
    }

    private boolean isCatExist(int id){
        boolean isExist = false;
        MySimpleItem item = null;
        for(Object ob : itemCatList){
            item = (MySimpleItem)ob;
            if(item.getId() == id){
                isExist = true;
                break;
            }
        }

        return isExist;
    }*/

    private CatListItem getItem(int id){
        CatListItem item = null;
        for(Object ob: itemList){
            item = (CatListItem)ob;
            if(item.getId() == id){
                break;
            }
        }

        return item;
    }

    @Override
    public void onDialogPositiveClicked(){
       finish();
    }

    @Override
    public void onLevelItemClicked(int itemPosition, int level) {
        if(level == -1){
            CatListItem item = (CatListItem) itemList.get(itemPosition);
            List<Object> simpleItemList = item.getItemList();
            MySimpleItem item1 = null;
            for(Object ob : simpleItemList){
                item1 = (MySimpleItem)ob;
                if(item.isSelectingAll()){
                    item1.setSelected(true);
                }else{
                    item1.setSelected(false);
                }

            }
            if(item.isSelectingAll()){
                item.setSelectingAll(false);
            }else{
                item.setSelectingAll(true);
            }

            itemAdapter.notifyItemChanged(itemPosition);
        }else{
            CatListItem item = (CatListItem) itemList.get(level);
            List<Object> simpleItemList = item.getItemList();
            MySimpleItem item1 = (MySimpleItem) simpleItemList.get(itemPosition);
            if(item1.isSelected()){
                item1.setSelected(false);
            }else{
                item1.setSelected(true);
            }

            itemAdapter.notifyItemChanged(level);
        }
    }

    private void showNoData(boolean show){
        if(show){
            recyclerView.setVisibility(View.GONE);
            textViewNoData.setVisibility(View.VISIBLE);
        }else{
            recyclerView.setVisibility(View.VISIBLE);
            textViewNoData.setVisibility(View.GONE);
        }
    }
}
