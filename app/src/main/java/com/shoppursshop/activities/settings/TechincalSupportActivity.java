package com.shoppursshop.activities.settings;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.shoppursshop.R;
import com.shoppursshop.activities.base.NetworkBaseActivity;
import com.shoppursshop.adapters.TechnicalSupportAdapter;
import com.shoppursshop.interfaces.MyItemClickListener;
import com.shoppursshop.models.CommissionRate;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.DialogAndToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TechincalSupportActivity extends NetworkBaseActivity implements MyItemClickListener {

    private RecyclerView recyclerView;
    private List<CommissionRate> itemList;
    private TechnicalSupportAdapter itemAdapter;
    private int position;
    private TextView tvError;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_techincal_support);
        init();
        initFooter(this,4);
    }

    private void init(){
        tvError = findViewById(R.id.tvError);
        itemList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        itemAdapter=new TechnicalSupportAdapter(this,itemList);
        itemAdapter.setMyItemClickListener(this);
        recyclerView.setAdapter(itemAdapter);

        getCommissionList();
    }

    private void getCommissionList(){
        String url=getResources().getString(R.string.partner_url)+ Constants.GET_COMMISSION_RATE+"?userType=Tech";
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(),"commissionRate");
    }

    private void assignPartner(){
        CommissionRate item = itemList.get(position);
        Map<String,String> params=new HashMap<>();
        params.put("commission",""+item.getCommissionRate());
        params.put("serviceType",item.getServiceName());
        params.put("shopCode",sharedPreferences.getString(Constants.SHOP_CODE,""));
        params.put("shopName",sharedPreferences.getString(Constants.SHOP_NAME,""));
        params.put("shopMobile",sharedPreferences.getString(Constants.MOBILE_NO,""));
        params.put("shopAddress",sharedPreferences.getString(Constants.ADDRESS,""));
        params.put("shopLat",sharedPreferences.getString(Constants.USER_LAT,""));
        params.put("shopLong",sharedPreferences.getString(Constants.USER_LONG,""));
        String url=getResources().getString(R.string.partner_url)+Constants.ASSIGN_TECH_PARTNER;
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"assignTechPartner");
    }

    @Override
    public void onJsonObjectResponse(JSONObject jsonObject, String apiName) {
        if(apiName.equals("commissionRate")){
            try {
                if(jsonObject.getBoolean("status")){
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    int len = jsonArray.length();
                    Gson g = new Gson();
                    CommissionRate item = null;
                    for(int i =0;i<len;i++){
                        item = g.fromJson(jsonArray.getString(i), CommissionRate.class);
                        itemList.add(item);
                    }

                    if(itemList.size() == 0){
                        showError(true,"No data available.");
                    }else{
                        showError(false,null);
                        itemAdapter.notifyDataSetChanged();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if(apiName.equals("assignTechPartner")){
            try {
                if(jsonObject.getBoolean("status")){
                    DialogAndToast.showDialog(jsonObject.getString("message"),this);
                }else{
                    DialogAndToast.showDialog(jsonObject.getString("message"),this);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onItemClicked(int position) {
        this.position = position;
        showMyBothDialog("A technical partner will be assign to you. Do you want to proceed",
                "NO","YES");
    }

    @Override
    public void onDialogPositiveClicked() {
        assignPartner();
    }

    private void showError(boolean isShow,String message){
        if(isShow){
            tvError.setVisibility(View.VISIBLE);
            tvError.setText(message);
        }else{
            tvError.setVisibility(View.GONE);
        }
    }
}
