package com.shoppursshop.activities.khatabook;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.shoppursshop.R;
import com.shoppursshop.activities.InvoiceActivity;
import com.shoppursshop.activities.base.NetworkBaseActivity;
import com.shoppursshop.adapters.KhataTransactionAdapter;
import com.shoppursshop.interfaces.MyItemClickListener;
import com.shoppursshop.models.KhataTransaction;
import com.shoppursshop.utilities.ConnectionDetector;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.DialogAndToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KhatabookActivity extends NetworkBaseActivity implements MyItemClickListener {

    private TextView textKhataNo,textTotalDueAmount,tvKhataNo,textKhataOpenDate,textCustomerName,textCustMobile;
    private String khataNo;
    private RecyclerView recyclerView;
    private List<KhataTransaction> itemList;
    private KhataTransactionAdapter itemAdapter;
    private float totalDueAmount;
    private RelativeLayout rlFooter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khatabook);

        init();
        initFooterAction(this);
        if(ConnectionDetector.isNetworkAvailable(this)){
            checkKhata();
        }else{
            DialogAndToast.showDialog(getResources().getString(R.string.no_internet),this);
        }
    }

    private void init(){
        tvKhataNo = findViewById(R.id.text_sub_header);
        textKhataNo = findViewById(R.id.textKhataNo);
        textTotalDueAmount = findViewById(R.id.textTotalDueAmount);
        textKhataOpenDate = findViewById(R.id.textKhataOpenDate);
        textCustomerName = findViewById(R.id.textCustomerName);
        textCustMobile = findViewById(R.id.textCustMobile);
        rlFooter = findViewById(R.id.relative_footer_action);
        textCustomerName.setText(getIntent().getStringExtra("name"));
        textCustMobile.setText(getIntent().getStringExtra("mobile"));

        itemList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);
       // recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        itemAdapter=new KhataTransactionAdapter(this,itemList);
        itemAdapter.setMyItemClickListener(this);
        recyclerView.setAdapter(itemAdapter);

        rlFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receivePayment();
            }
        });

    }

    private void checkKhata(){
        Map<String,String> params=new HashMap<>();
        params.put("kbCustId",getIntent().getStringExtra("custId"));
        params.put("custCode",getIntent().getStringExtra("custCode"));
        params.put("custDbName",getIntent().getStringExtra("custCode"));
        params.put("shopCode",sharedPreferences.getString(Constants.SHOP_CODE,""));
        params.put("kbShopId",sharedPreferences.getString(Constants.USER_ID,""));
        params.put("userName",sharedPreferences.getString(Constants.SHOP_NAME,""));
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+Constants.OPEN_KHATA;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"openKhata");
    }

    private void getTransactions(){
        Map<String,String> params=new HashMap<>();
        params.put("kbNo",khataNo);
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+Constants.KHATA_TRANSACTIONS;
        //showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"khataTransaction");
    }

    private void receivePayment(){
        Intent intent = new Intent(KhatabookActivity.this, RecievePaymentActivity.class);
        intent.putExtra("khataNo",khataNo);
        intent.putExtra("totalDue",(int)totalDueAmount);
        intent.putExtra("custCode", getIntent().getStringExtra("custCode"));
        intent.putExtra("custId", getIntent().getIntExtra("custId",0));
        intent.putExtra("custName", getIntent().getStringExtra("custName"));
        intent.putExtra("custMobile", getIntent().getStringExtra("custMobile"));
        intent.putExtra("custImage", getIntent().getStringExtra("custImage"));
        intent.putExtra("custUserCreateStatus", getIntent().getStringExtra("custUserCreateStatus"));
        startActivityForResult(intent,123);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(itemList.size() > 0){
            totalDueAmount = 0f;
            itemList.clear();
            itemAdapter.notifyDataSetChanged();
            getTransactions();
        }
    }

    @Override
    public void onJsonObjectResponse(JSONObject jsonObject, String apiName) {
        try {
              if(apiName.equals("openKhata")){
                if(jsonObject.getBoolean("status")){
                    JSONObject dataObject = jsonObject.getJSONObject("result");
                    khataNo = dataObject.getString("kbNo");
                    textKhataNo.setText("Khata No - "+khataNo);
                    textKhataOpenDate.setText("Open Date - "+dataObject.getString("createdDate"));
                    getTransactions();
                }
            }if(apiName.equals("khataTransaction")) {
                if (jsonObject.getBoolean("status")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    int len = jsonArray.length();
                    KhataTransaction item = null;
                    Gson gson = new Gson();
                    for(int i=0;i<len;i++){
                        item = gson.fromJson(jsonArray.getJSONObject(i).toString(),KhataTransaction.class);
                        if(item.getPaymentTransactionType().contains("Credit")){
                            totalDueAmount = totalDueAmount + item.getPaymentAmount();
                        }else{
                            totalDueAmount = totalDueAmount - item.getPaymentAmount();
                        }
                        itemList.add(item);
                    }

                    if(totalDueAmount > 0){
                        rlFooter.setVisibility(View.VISIBLE);
                        textTotalDueAmount.setText(String.format("%.02f",totalDueAmount));
                    }else{
                        rlFooter.setVisibility(View.GONE);
                        textTotalDueAmount.setText("0.00");
                    }

                    itemAdapter.notifyDataSetChanged();
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onItemClicked(int position) {
       KhataTransaction item = itemList.get(position);
        Intent intent = new Intent(KhatabookActivity.this, InvoiceActivity.class);
        intent.putExtra("orderNumber",item.getCoNo());
        startActivity(intent);
    }
}
