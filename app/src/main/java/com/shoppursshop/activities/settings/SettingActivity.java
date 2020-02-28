package com.shoppursshop.activities.settings;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;

import com.android.volley.Request;
import com.shoppursshop.R;
import com.shoppursshop.activities.auth.LoginActivity;
import com.shoppursshop.activities.base.NetworkBaseActivity;
import com.shoppursshop.activities.categories.MyCategoriesActivity;
import com.shoppursshop.activities.categories.MySubCategoriesActivity;
import com.shoppursshop.activities.khatabook.CustomerListWithKhataActivity;
import com.shoppursshop.activities.offers.MyOffersActivity;
import com.shoppursshop.activities.order.MyOrdersActivity;
import com.shoppursshop.activities.product.MyProductListActivity;
import com.shoppursshop.activities.product.ReturnProductActivity;
import com.shoppursshop.activities.product.RlevelAndExpiredProductActivity;
import com.shoppursshop.activities.product.ShoppursProductListActivity;
import com.shoppursshop.adapters.SettingsAdapter;
import com.shoppursshop.database.DbHelper;
import com.shoppursshop.fragments.ReferAppDialogFragment;
import com.shoppursshop.interfaces.MyItemClickListener;
import com.shoppursshop.interfaces.MyItemTypeClickListener;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.DialogAndToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingActivity extends NetworkBaseActivity implements MyItemClickListener, MyItemTypeClickListener {

    private RecyclerView recyclerView;
    private List<String> itemList;
    private SettingsAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
        //initFooter(this,4);
    }

    private void init(){
        itemList = new ArrayList<>();
        itemList.add("Store Profile");
        itemList.add("Khatabook");
        itemList.add("Store Categories");
        itemList.add("Store Sub Category");
        itemList.add("Add Products to Store");
        itemList.add("Shoppurs Products");
        itemList.add("Return Products");
        itemList.add("Check Reorder Level/Expired Products");
        itemList.add("Orders to Shoppurs");
        itemList.add("Customer Orders");
        itemList.add("Frequency Customer Orders");
        itemList.add("Store Sales");
        itemList.add("Store Offers");
        itemList.add("Payment Device");
        itemList.add("User License");
        itemList.add("Sync Data");
        itemList.add("Chat");
        itemList.add("Technical Support");
        itemList.add("Invite to Shoppurs");
        itemList.add("Display");
        itemList.add("Logout");
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        itemAdapter=new SettingsAdapter(this,itemList);
        itemAdapter.setMyItemClickListener(this);
        recyclerView.setAdapter(itemAdapter);
    }

    @Override
    public void onResume(){
        super.onResume();

        initFooter(this,4);
    }

    public void changeStoreStatus(){
        Map<String,String> params=new HashMap<>();
        params.put("status","0");
        params.put("mobile",sharedPreferences.getString(Constants.MOBILE_NO,""));
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+"/api/user/update_store_open_status";
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"changeOpenStatus");
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {
        try {
            if (apiName.equals("changeOpenStatus")) {
                if (response.getBoolean("status")) {
                    String IMEI_NO = sharedPreferences.getString(Constants.IMEI_NO,"");
                    String fcmToken = sharedPreferences.getString(Constants.FCM_TOKEN,"");
                    editor.clear();
                    editor.putString(Constants.IMEI_NO,IMEI_NO);
                    editor.putString(Constants.FCM_TOKEN,fcmToken);
                    editor.commit();
                    dbHelper.deleteTable(DbHelper.CAT_TABLE);
                    dbHelper.deleteTable(DbHelper.SUB_CAT_TABLE);
                    dbHelper.deleteTable(DbHelper.PRODUCT_TABLE);
                    dbHelper.deleteTable(DbHelper.PRODUCT_BARCODE_TABLE);
                    dbHelper.deleteTable(DbHelper.PRODUCT_UNIT_TABLE);
                    dbHelper.deleteTable(DbHelper.PRODUCT_SIZE_TABLE);
                    dbHelper.deleteTable(DbHelper.PRODUCT_COLOR_TABLE);
                    dbHelper.deleteTable(DbHelper.CART_TABLE);
                    dbHelper.deleteTable(DbHelper.PROD_COMBO_TABLE);
                    dbHelper.deleteTable(DbHelper.PROD_COMBO_DETAIL_TABLE);
                    dbHelper.deleteTable(DbHelper.PROD_PRICE_TABLE);
                    dbHelper.deleteTable(DbHelper.PROD_PRICE_DETAIL_TABLE);
                    dbHelper.deleteTable(DbHelper.PROD_FREE_OFFER_TABLE);
                    dbHelper.deleteTable(DbHelper.COUPON_TABLE);
                    dbHelper.deleteTable(DbHelper.SHOP_CART_TABLE);
                    dbHelper.deleteTable(DbHelper.CART_PROD_PRICE_TABLE);
                    dbHelper.deleteTable(DbHelper.CART_PROD_PRICE_DETAIL_TABLE);
                    dbHelper.deleteTable(DbHelper.CART_PROD_COMBO_TABLE);
                    dbHelper.deleteTable(DbHelper.CART_PROD_COMBO_DETAIL_TABLE);
                    dbHelper.deleteTable(DbHelper.CART_PROD_FREE_OFFER_TABLE);
                    dbHelper.deleteTable(DbHelper.CART_COUPON_TABLE);
                    dbHelper.deleteTable(DbHelper.CART_PRODUCT_UNIT_TABLE);
                    dbHelper.deleteTable(DbHelper.CART_PRODUCT_SIZE_TABLE);
                    dbHelper.deleteTable(DbHelper.CART_PRODUCT_COLOR_TABLE);
                    dbHelper.deleteTable(DbHelper.CUSTOMER_INFO_TABLE);

                    Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else{
                    DialogAndToast.showDialog(response.getString("message"),this);
                }
            }
        }catch (JSONException error){
            error.printStackTrace();
        }
    }


    @Override
    public void onItemClicked(int position) {
        String name = itemList.get(position);
        if(name.equals("Store Profile")){
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        }else if(name.equals("Khatabook")){
            Intent intent = new Intent(this, CustomerListWithKhataActivity.class);
            startActivity(intent);
        }else if(name.equals("Invite to Shoppurs")){
            openReferDialog();
        } else if(name.equals("Store Categories")){
            Intent intent = new Intent(this, MyCategoriesActivity.class);
            startActivity(intent);
        }else if(name.equals("Store Sub Category")){
            Intent intent = new Intent(this, MySubCategoriesActivity.class);
            startActivity(intent);
        }else if(name.equals("Add Products to Store")){
            Intent intent = new Intent(this, MyProductListActivity.class);
            startActivity(intent);
        }else if(name.equals("Shoppurs Products")){
            Intent intent = new Intent(this, ShoppursProductListActivity.class);
            intent.putExtra("flag","shoppursProducts");
            startActivity(intent);
        }else if(name.equals("Orders to Shoppurs")){
            Intent intent = new Intent(this, MyOrdersActivity.class);
            intent.putExtra("flag","storeOrders");
            startActivity(intent);
        }else if(name.equals("Customer Orders")){
            Intent intent = new Intent(this, MyOrdersActivity.class);
            intent.putExtra("flag","customerOrders");
            startActivity(intent);
        }else if(name.equals("Frequency Customer Orders")){
            Intent intent = new Intent(this, FrequencyOrderCustomerListActivity.class);
            intent.putExtra("flag","frequencyCustomerOrders");
            startActivity(intent);
        }else if(name.equals("My Orders")){
            Intent intent = new Intent(this, MyOrdersActivity.class);
            startActivity(intent);
        }else if(name.equals("Store Offers")){
            Intent intent = new Intent(this, MyOffersActivity.class);
            startActivity(intent);
            finish();
        }
        else if(name.equals("Store Sales")){
            Intent intent = new Intent(this, MySalesActivity.class);
            startActivity(intent);
        }else if(name.equals("Payment Device")){
            Intent intent = new Intent(this, AddPaymentDevice.class);
            startActivity(intent);
        }else if(name.equals("User License")){
            Intent intent = new Intent(this, BuyUserLicenceActivity.class);
            startActivity(intent);
        }else if(name.equals("Sync Data")){
            Intent intent = new Intent(this, SyncDataActivity.class);
            startActivity(intent);
        }else if(name.equals("Chat")){
            Intent intent = new Intent(this, UserListForChatActivity.class);
            startActivity(intent);
        }else if(name.equals("Technical Support")){
            Intent intent = new Intent(this, TechincalSupportActivity.class);
            startActivity(intent);
        }else if(name.equals("Display")){
            Intent intent = new Intent(this, DisplaySettingsActivity.class);
            startActivity(intent);
        }else if(name.equals("Return Products")){
            Intent intent = new Intent(this, ReturnProductActivity.class);
            startActivity(intent);
        }else if(name.equals("Check Reorder Level/Expired Products")){
            Intent intent = new Intent(this, RlevelAndExpiredProductActivity.class);
            intent.putExtra("flag","settings");
            startActivity(intent);
        }else if(name.equals("Logout")){
            changeStoreStatus();
        }
    }

    private void openReferDialog(){
        ReferAppDialogFragment bottomSearchFragment = ReferAppDialogFragment.newInstance(colorTheme);
        bottomSearchFragment.setMyItemClickListener(this);
        bottomSearchFragment.show(getSupportFragmentManager(), "Refer App dialog fragment");
    }

    private void referToShop(){
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
            String sAux = "\n Download "+getResources().getString(R.string.app_name)+" app from below link \n\n";
            sAux = sAux + "https://play.google.com/store/apps/details?id=com.shoppursshop \n\n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "Choose one"));
        } catch(Exception e) {
            //e.toString();
        }
    }

    private void referToCustomer(){
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "Shoppurs Customer");
            String sAux = "\n Dear Valued Customer \n\n";
            sAux = sAux + "\n Greetings";
            sAux = sAux + "\n Please download the Shoppurs App from the link below and avail the spectacular offers, discounts etc  \n\n";
            sAux = sAux + "https://play.google.com/store/apps/details?id=com.shoppurs \n\n";
            sAux = sAux + "\n Have a great Shopping experience \n\n";
            sAux = sAux + "\n "+sharedPreferences.getString(Constants.SHOP_NAME,"");
            sAux = sAux + "\n "+sharedPreferences.getString(Constants.ADDRESS,"")+" \n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "Choose one"));
        } catch(Exception e) {
            //e.toString();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
         if (id == android.R.id.home) {
            super.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClicked(int position, int type) {
        if(type == R.id.rbCustomer){
            referToCustomer();
        }else{
            referToShop();
        }
    }
}
