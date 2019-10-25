package com.shoppursshop.activities.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.volley.Request;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shoppursshop.R;
import com.shoppursshop.activities.NetworkBaseActivity;
import com.shoppursshop.activities.ScannarActivity;
import com.shoppursshop.adapters.ReturnProductListAdapter;
import com.shoppursshop.interfaces.MyImageClickListener;
import com.shoppursshop.interfaces.MyItemTypeClickListener;
import com.shoppursshop.models.MyProductItem;
import com.shoppursshop.models.SaleReturn;
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

public class ReturnProductActivity extends NetworkBaseActivity implements MyImageClickListener, MyItemTypeClickListener {

    private LinearLayout linearLayoutScanCenter;
    private RecyclerView recyclerView;
    private ReturnProductListAdapter myItemAdapter;
    private List<SaleReturn> itemList;

    private EditText edit_search_invoice;
    private TextView tvInvoiceNo,tvDate,tvCustomerName;
    private String invoiceNo;
    private RelativeLayout rl_invoice_details;
    private FloatingActionButton fab;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_product);
        initFooterAction(this);
        init();

    }

    private void init(){

        edit_search_invoice = findViewById(R.id.edit_search_invoice);
        tvInvoiceNo = findViewById(R.id.text_invoice_no);
        tvDate = findViewById(R.id.text_date);
        tvCustomerName = findViewById(R.id.text_customer_name);
        rl_invoice_details = findViewById(R.id.rl_invoice_details);

        itemList = new ArrayList<>();
        linearLayoutScanCenter = findViewById(R.id.linear_action);
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        final RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        myItemAdapter=new ReturnProductListAdapter(this,itemList);
        myItemAdapter.setMyImageClickListener(this);
        myItemAdapter.setDarkTheme(isDarkTheme);
        myItemAdapter.setColoTheme(colorTheme);
        myItemAdapter.setMyItemTypeClickListener(this);
        recyclerView.setAdapter(myItemAdapter);

        linearLayoutScanCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openScannar();
            }
        });

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openScannar();
            }
        });
    }

    private void openScannar(){
        Intent intent = new Intent(this, ScannarActivity.class);
        intent.putExtra("flag","scan");
        intent.putExtra("type","searchInvoice");
        startActivityForResult(intent,10);
    }

    private void getInvoice(String invNo){
        Map<String,String> params=new HashMap<>();
        params.put("number",invNo);
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+Constants.GET_INVOICE_BY_INV_NO;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"searchInvoice");
    }

    private void returnProduct(){
        SaleReturn saleReturn = itemList.get(position);

        Map<String,String> params=new HashMap<>();
        params.put("invNo",invoiceNo);
        params.put("custCode",saleReturn.getCustCode());
        params.put("prodCode",saleReturn.getProdCode());
        params.put("prodId",""+saleReturn.getProdId());
        params.put("qty",""+saleReturn.getQty());
        params.put("prodName",saleReturn.getProdName());
        params.put("prodBarCode",saleReturn.getProdBarCode());
        params.put("prodDesc",saleReturn.getProdDesc());
        params.put("prodMrp",""+saleReturn.getProdMrp());
        params.put("prodSp",""+saleReturn.getProdSp());
        params.put("prodCgst",""+saleReturn.getProdCgst());
        params.put("prodSgst",""+saleReturn.getProdSgst());
        params.put("prodIgst",""+saleReturn.getProdIgst());
        params.put("prodImage1",saleReturn.getProdImage1());
        params.put("prodImage2",saleReturn.getProdImage2());
        params.put("prodImage3",saleReturn.getProdImage3());
        params.put("status",saleReturn.getStatus());
        params.put("unit",saleReturn.getUnit());
        params.put("size",saleReturn.getSize());
        params.put("color",saleReturn.getColor());
        params.put("userName",sharedPreferences.getString(Constants.SHOP_NAME,""));
        params.put("shopCode",sharedPreferences.getString(Constants.SHOP_CODE,""));
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+Constants.RETURN_PRODUCT;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"returnProduct");
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {

        try {
            if (apiName.equals("searchInvoice")) {
                if (response.getBoolean("status")) {
                    // JSONArray dataArray = response.getJSONArray("result");
                    JSONObject jsonObject = response.getJSONObject("result");
                    JSONArray invoiceDetailsArray = jsonObject.getJSONArray("invoiceDetailList");
                    tvDate.setText(jsonObject.getString("invDate"));
                    tvCustomerName.setText(jsonObject.getString("invCustName"));
                    tvInvoiceNo.setText("Invoice No: "+jsonObject.getString("invNo"));
                    invoiceNo = jsonObject.getString("invNo");
                    String custCode = jsonObject.getString("custCode");
                    int len = invoiceDetailsArray.length();
                    for (int i = 0; i < len; i++) {
                        jsonObject = invoiceDetailsArray.getJSONObject(i);
                        //invoiceDetail = new InvoiceDetail();

                        SaleReturn item = new SaleReturn();
                        item.setStatus(jsonObject.getString("invDProdStatus"));
                        item.setProdName(jsonObject.getString("invDProdName"));
                        item.setProdCode(jsonObject.getString("invDProdCode"));
                        item.setProdImage1(jsonObject.getString("invDProdImage1"));
                        item.setProdImage2(jsonObject.getString("invDProdImage2"));
                        item.setProdImage3(jsonObject.getString("invDProdImage3"));
                        item.setCustCode(custCode);
                        item.setQty(jsonObject.getInt("invDQty"));
                        item.setProdCgst(Float.parseFloat(jsonObject.getString("invDCGST")));
                        item.setProdSgst(Float.parseFloat(jsonObject.getString("invDSGST")));
                        item.setProdIgst(Float.parseFloat(jsonObject.getString("invDIGST")));
                        item.setProdMrp((float) jsonObject.getDouble("invDMrp"));
                        item.setUnit(jsonObject.getString("invdProdUnit"));
                        item.setColor(jsonObject.getString("invdProdColor"));
                        item.setSize(jsonObject.getString("invdProdSize"));
                        itemList.add(item);
                    }

                    if(itemList.size() > 0){
                        myItemAdapter.notifyDataSetChanged();
                        fab.setVisibility(View.VISIBLE);
                        rl_invoice_details.setVisibility(View.VISIBLE);
                        linearLayoutScanCenter.setVisibility(View.GONE);
                        findViewById(R.id.relative_footer_action).setVisibility(View.GONE);
                    }
                }else{
                    DialogAndToast.showDialog(response.getString("message"),this);
                }
            }else if(apiName.equals("returnProduct")){
                if (response.getBoolean("status")) {
                    showMyDialog(response.getString("message"));
                }else{
                    DialogAndToast.showDialog(response.getString("message"),this);
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 10){
            if(data != null){
               getInvoice(data.getStringExtra("barCode"));
            }
        }
    }

    @Override
    public void onImageClicked(int position, int type, View view) {

    }

    @Override
    public void onItemClicked(int position, int type) {
        this.position = position;
        if(type == 2){
          returnProduct();
        }
    }

    @Override
    protected void onFooterActionClicked(){
       invoiceNo = edit_search_invoice.getText().toString();

       if(TextUtils.isEmpty(invoiceNo)){
           DialogAndToast.showDialog("Please enter Invoice number.",this);
           return;
       }

       getInvoice(invoiceNo);
    }
}
