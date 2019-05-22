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
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.shoppursshop.R;
import com.shoppursshop.activities.MainActivity;
import com.shoppursshop.activities.NetworkBaseActivity;
import com.shoppursshop.activities.settings.profile.DeliveryActivity;
import com.shoppursshop.adapters.SimpleItemAdapter;
import com.shoppursshop.interfaces.MyLevelItemClickListener;
import com.shoppursshop.models.Barcode;
import com.shoppursshop.models.CatListItem;
import com.shoppursshop.models.MyProductItem;
import com.shoppursshop.models.MySimpleItem;
import com.shoppursshop.utilities.ConnectionDetector;
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

public class SyncProductActivity extends NetworkBaseActivity implements MyLevelItemClickListener {

    private List<Object> itemCatList,selectedItemList;
    private List<MyProductItem> productList,selectedProductList;
    private RecyclerView recyclerView;
    private SimpleItemAdapter itemAdapter;
    private List<Object> itemList,itemRemoveObject;
    private RelativeLayout rlFooter;
    private String subCats;

    private boolean isAddingProduct;

    private TextView textViewNoData;
    private LinearLayout linearLayoutFooter;
    private TextView tv_top_parent, tv_parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_product);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initFooterAction(this);
        setToolbarTheme();
        init();
    }

    private void init(){
        rlFooter = findViewById(R.id.relative_footer_action);
        linearLayoutFooter = findViewById(R.id.linear_footer);
        textViewNoData = findViewById(R.id.text_no_data);

        itemList = new ArrayList<>();
        productList = new ArrayList<>();
        selectedProductList = new ArrayList<>();
        selectedItemList = new ArrayList<>();
        int i=0;
        itemCatList = dbHelper.getCategoriesForProduct();
        CatListItem category = null;
        MySimpleItem subCat = null;
        List<Object> subCatList = null;

        for(Object ob: itemCatList){
            category = (CatListItem)ob;
            subCatList = dbHelper.getCatSubCategories(""+category.getId());
            category.setItemList(subCatList);
            for(Object ob1 : subCatList){
                subCat = (MySimpleItem)ob1;
                addSubCategory(subCat.getId(),subCat.getName(),itemList.size());
                if(subCats == null){
                    subCats = ""+subCat.getId();
                }else{
                    subCats = subCats+","+subCat.getId();
                }
            }
        }

        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        // staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        itemAdapter=new SimpleItemAdapter(this,itemList,"productList");
        itemAdapter.setMyLevelItemClickListener(this);
        recyclerView.setAdapter(itemAdapter);

        rlFooter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                isAddingProduct = true;
                showMyBothDialog("Are you sure to want add selected products?","Cancel","Yes");

            }
        });


        if(ConnectionDetector.isNetworkAvailable(this)){
            getProducts();
        }

        tv_top_parent = findViewById(R.id.text_left_label);
        tv_parent = findViewById(R.id.text_right_label);
        tv_top_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SyncProductActivity.this, SettingActivity.class));
                finish();
            }
        });
        tv_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SyncProductActivity.this, MyProductListActivity.class));
                finish();
            }
        });
    }

    private void getProducts(){
        Map<String,String> params = new HashMap<>();
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+ Constants.GET_PRODUCTS;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"productslist");
    }

    private void createProducts(){
        JSONArray dataArray = new JSONArray();
        JSONObject dataObject = null;
        CatListItem category = null;
        MyProductItem subCat = null;
        for(Object ob: selectedItemList){
            category = (CatListItem)ob;
            for(Object ob1 : category.getItemList()){
                subCat = (MyProductItem) ob1;
                dataObject = new JSONObject();
                try {
                    dataObject.put("retRetailerId",sharedPreferences.getString(Constants.USER_ID,""));
                    dataObject.put("prodCatId",""+category.getId());
                    dataObject.put("prodSubCatId",""+subCat.getProdSubCatId());
                    dataObject.put("prodId",""+subCat.getProdId());
                    dataObject.put("prodReorderLevel",""+subCat.getProdReorderLevel());
                    dataObject.put("prodQoh",""+subCat.getProdQoh());
                    dataObject.put("prodName",""+subCat.getProdName());
                    dataObject.put("prodBarCode",""+subCat.getProdBarCode());
                    dataObject.put("prodCode",""+subCat.getProdCode());
                    dataObject.put("prodDesc",""+subCat.getProdDesc());
                    dataObject.put("prodCgst",""+subCat.getProdCgst());
                    dataObject.put("prodIgst",""+subCat.getProdIgst());
                    dataObject.put("prodSgst",""+subCat.getProdSgst());
                    dataObject.put("prodWarranty",""+subCat.getProdWarranty());
                    dataObject.put("prodMrp",""+subCat.getProdMrp());
                    dataObject.put("prodSp",""+subCat.getProdSp());
                    dataObject.put("prodHsnCode",subCat.getProdHsnCode());
                    dataObject.put("prodMfgDate",subCat.getProdMfgDate());
                    dataObject.put("prodExpiryDate",subCat.getProdExpiryDate());
                    dataObject.put("prodMfgBy",""+subCat.getProdMfgBy());
                    dataObject.put("prodImage1",subCat.getProdImage1());
                    dataObject.put("prodImage2",subCat.getProdImage2());
                    dataObject.put("prodImage3",subCat.getProdImage3());
                    dataObject.put("isBarcodeAvailable",subCat.getIsBarCodeAvailable());
                    dataObject.put("barcodeList",subCat.getBarcodeList());
                    dataObject.put("action","2");
                    dataObject.put("createdBy",subCat.getCreatedBy());
                    dataObject.put("updatedBy",subCat.getUpdatedBy());
                    dataObject.put("shopCode",sharedPreferences.getString(Constants.SHOP_CODE,""));
                    dataObject.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
                    dataObject.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
                    dataObject.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
                    dataArray.put(dataObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
        String url=getResources().getString(R.string.url)+Constants.CREATE_PRODUCTS;
        showProgress(true);
        jsonArrayV2ApiRequest(Request.Method.POST,url,dataArray,"addProduct");
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {

        try {
            if(apiName.equals("productslist")){

                if(response.getBoolean("status")){
                    JSONArray dataArray = response.getJSONArray("result");
                    JSONObject jsonObject =null;
                    int len = dataArray.length();
                    MyProductItem item = null;
                    List<Barcode> barCodeList = null;
                    Barcode barcode = null;
                    JSONArray barArray = null;
                    int barLen = 0;
                    for(int i=0; i<len; i++){
                        jsonObject = dataArray.getJSONObject(i);
                        if(!dbHelper.isProductExist(jsonObject.getString("prodName"))){
                            item = new MyProductItem();
                            item.setProdId(jsonObject.getInt("prodId"));
                            item.setProdCatId(jsonObject.getInt("prodCatId"));
                            item.setProdSubCatId(jsonObject.getInt("prodSubCatId"));
                            item.setProdName(jsonObject.getString("prodName"));
                            item.setProdCode(jsonObject.getString("prodCode"));
                            item.setProdBarCode(jsonObject.getString("prodBarCode"));
                            item.setProdDesc(jsonObject.getString("prodDesc"));
                            item.setProdReorderLevel(jsonObject.getInt("prodReorderLevel"));
                            item.setProdQoh(jsonObject.getInt("prodQoh"));
                            item.setProdHsnCode(jsonObject.getString("prodHsnCode"));
                            item.setProdCgst(Float.parseFloat(jsonObject.getString("prodCgst")));
                            item.setProdIgst(Float.parseFloat(jsonObject.getString("prodIgst")));
                            item.setProdSgst(Float.parseFloat(jsonObject.getString("prodSgst")));
                            item.setProdWarranty(Float.parseFloat(jsonObject.getString("prodWarranty")));
                            item.setProdMfgDate(jsonObject.getString("prodMfgDate"));
                            item.setProdExpiryDate(jsonObject.getString("prodExpiryDate"));
                            item.setProdMfgBy(jsonObject.getString("prodMfgBy"));
                            item.setProdImage1(jsonObject.getString("prodImage1"));
                            item.setProdImage2(jsonObject.getString("prodImage2"));
                            item.setProdImage3(jsonObject.getString("prodImage3"));
                            item.setIsBarCodeAvailable(jsonObject.getString("isBarcodeAvailable"));
                            item.setProdMrp(Float.parseFloat(jsonObject.getString("prodMrp")));
                            item.setProdSp(Float.parseFloat(jsonObject.getString("prodSp")));
                            item.setCreatedBy(jsonObject.getString("createdBy"));
                            item.setUpdatedBy(jsonObject.getString("updatedBy"));
                            item.setCreatedDate(jsonObject.getString("createdDate"));
                            item.setUpdatedDate(jsonObject.getString("updatedDate"));
                            barArray = jsonObject.getJSONArray("barcodeList");
                            barLen = barArray.length();
                            barCodeList = new ArrayList<>();
                            for(int j = 0; j<barLen; j++){
                                barcode = new Barcode();
                                barcode.setBarcode(barArray.getJSONObject(j).getString("barcode"));
                                barCodeList.add(barcode);
                            }
                            item.setBarcodeList(barCodeList);
                            addProduct(item);
                        }
                        // productList.add(item);
                    }
                    removeCategories();
                    if(itemList.size() == 0){
                        showNoData(true);
                    }else{
                        showNoData(false);
                        itemAdapter.notifyDataSetChanged();
                    }
                }else{
                    DialogAndToast.showDialog(response.getString("message"),this);
                }
            }else if(apiName.equals("addProduct")){
                if(response.getBoolean("status")){
                    CatListItem subCat = null;
                    MyProductItem productItem = null;
                    for(Object ob: selectedItemList) {
                        subCat = (CatListItem) ob;
                        for (Object ob1 : subCat.getItemList()) {
                            productItem = (MyProductItem) ob1;
                            dbHelper.addProduct(productItem, Utility.getTimeStamp(),Utility.getTimeStamp());
                        }
                    }

                    showMyDialog("Product added successfully");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addSubCategory(int subCatId,String title,int position) {
        CatListItem myItem = new CatListItem();
        myItem.setSelectingAll(true);
        myItem.setTitle(title);
        myItem.setType(1);
        myItem.setId(subCatId);
        myItem.setPosition(position);
        List<Object> subCatList = new ArrayList<>();
        myItem.setItemList(subCatList);
        itemList.add(myItem);
    }

    private void addProduct(MyProductItem item){
        CatListItem subCat = null;
        MySimpleItem mySimpleItem = null;
        for(Object ob : itemList){
            subCat = (CatListItem) ob;
            if(subCat.getId() == item.getProdCatId()){

                subCat.getItemList().add(item);
            }

            itemAdapter.notifyItemChanged(subCat.getPosition());
        }


    }

    private void removeCategories(){
        CatListItem subCat = null;
        MySimpleItem mySimpleItem = null;
        itemRemoveObject = new ArrayList<>();
        for(Object ob : itemList){
            subCat = (CatListItem) ob;
            if(subCat.getItemList().size() == 0){
                itemRemoveObject.add(ob);
            }

        }

        for(Object ob : itemRemoveObject){
            itemList.remove(ob);
        }

        itemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDialogPositiveClicked(){
        if(isAddingProduct){
            CatListItem subCat = null,mySelectedCatItem = null;
            List<Object> selectedList = null;
            MyProductItem prod = null;
            for(Object ob: itemList){
                subCat = (CatListItem)ob;
                selectedList = new ArrayList<>();
                mySelectedCatItem = new CatListItem();
                mySelectedCatItem.setId(subCat.getId());
                mySelectedCatItem.setTitle(subCat.getTitle());
                mySelectedCatItem.setDesc(subCat.getDesc());
                for(Object ob1 : subCat.getItemList()){
                    prod = (MyProductItem) ob1;
                    if(prod.isSelected())
                        selectedList.add(ob1);
                }
                if(selectedList.size() > 0){
                    mySelectedCatItem.setItemList(selectedList);
                    selectedItemList.add(mySelectedCatItem);
                }
            }

            if(selectedItemList.size() == 0){
                DialogAndToast.showDialog("Please select Products",SyncProductActivity.this);
                return;
            }

            createProducts();
        }else{
            Intent intent = new Intent();
            intent.putExtra("flag","productAdded");
            setResult(-1,intent);
            finish();
        }

    }

    @Override
    public void onLevelItemClicked(int itemPosition, int level) {
        if(level == -1){
            CatListItem item = (CatListItem) itemList.get(itemPosition);
            List<Object> simpleItemList = item.getItemList();
            MyProductItem item1 = null;
            for(Object ob : simpleItemList){
                item1 = (MyProductItem)ob;
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
        }
    }

    private void showNoData(boolean show){
        if(show){
            recyclerView.setVisibility(View.GONE);
            rlFooter.setVisibility(View.GONE);
            textViewNoData.setVisibility(View.VISIBLE);
        }else{
            recyclerView.setVisibility(View.VISIBLE);
            rlFooter.setVisibility(View.VISIBLE);
            textViewNoData.setVisibility(View.GONE);
        }
    }
}
