package com.shoppursshop.activities.settings;

import android.app.DatePickerDialog;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.shoppursshop.R;
import com.shoppursshop.activities.AddProductActivity;
import com.shoppursshop.activities.NetworkBaseActivity;
import com.shoppursshop.adapters.PaymentSchemeAdapter;
import com.shoppursshop.fragments.BottomSearchFragment;
import com.shoppursshop.interfaces.MyItemClickListener;
import com.shoppursshop.models.MyProductItem;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.DialogAndToast;
import com.shoppursshop.utilities.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComboProductOfferActivity extends NetworkBaseActivity implements MyItemClickListener {

    private RecyclerView recyclerView;
    private ComboOfferAdapter comboOfferAdapter;
    private List<MyProductItem> myProductItems;
    private Button add_combo;
    private boolean isProductSearch;
    private int position;

    private DatePickerDialog datePicker1,datePicker2;
    private Calendar calendar;

    private EditText edit_offer_name,edit_offer_start_date,edit_offer_end_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combo_product_offers);
        initFooterAction(this);
        init();
    }

    private void init(){
        myProductItems = new ArrayList<>();
        MyProductItem productItem = new MyProductItem();
        productItem.setProdName("");
        myProductItems.add(productItem);
        productItem = new MyProductItem();
        productItem.setProdName("");
        myProductItems.add(productItem);

        edit_offer_name = findViewById(R.id.edit_offer_name);
        edit_offer_start_date = findViewById(R.id.edit_offer_start_date);
        edit_offer_end_date = findViewById(R.id.edit_offer_end_date);

        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        datePicker1=new DatePickerDialog(ComboProductOfferActivity.this,new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker datePicker, int yr, int mon, int dy) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR,yr);
                cal.set(Calendar.MONTH,mon);
                cal.set(Calendar.DATE,dy);
                edit_offer_start_date.setText(Utility.parseDate(cal,"yyyy-MM-dd"));
            }
        },year,month,day);
        datePicker1.setCancelable(false);
        datePicker1.setMessage("Select Start Date");

        datePicker2=new DatePickerDialog(ComboProductOfferActivity.this,new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker datePicker, int yr, int mon, int dy) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR,yr);
                cal.set(Calendar.MONTH,mon);
                cal.set(Calendar.DATE,dy);
                edit_offer_end_date.setText(Utility.parseDate(cal,"yyyy-MM-dd"));
            }
        },year,month,day);
        datePicker2.setCancelable(false);
        datePicker2.setMessage("Select End Date");

        add_combo = findViewById(R.id.add_combo);
        ((GradientDrawable)add_combo.getBackground()).setColor(colorTheme);
        add_combo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyProductItem productItem = new MyProductItem();
                productItem.setProdName("");
                myProductItems.add(productItem);
                comboOfferAdapter.notifyDataSetChanged();
            }
        });

        findViewById(R.id.relative_footer_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        edit_offer_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker1.show();
            }
        });

        edit_offer_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker2.show();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        comboOfferAdapter=new ComboOfferAdapter(this,myProductItems);
        comboOfferAdapter.setMyItemClickListener(this);
        recyclerView.setAdapter(comboOfferAdapter);
    }

    private void createComboOffer(){
        String offerName = edit_offer_name.getText().toString();
        String startDate = edit_offer_start_date.getText().toString();
        String endDate = edit_offer_end_date.getText().toString();

        if(TextUtils.isEmpty(offerName)){
            DialogAndToast.showDialog("Please enter offer name.",this);
            return;
        }

        if(TextUtils.isEmpty(startDate)){
            DialogAndToast.showDialog("Please enter offer start date.",this);
            return;
        }

        if(TextUtils.isEmpty(endDate)){
            DialogAndToast.showDialog("Please enter offer end date.",this);
            return;
        }

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject productObject = null;
        try {
            jsonObject.put("offerName",offerName);
            jsonObject.put("status","1");
            jsonObject.put("startDate",startDate);
            jsonObject.put("endDate",endDate);
            jsonObject.put("productComboOfferDetails","");
            jsonObject.put("userName",sharedPreferences.getString(Constants.FULL_NAME,""));
            jsonObject.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
            jsonObject.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
            jsonObject.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url=getResources().getString(R.string.url)+ Constants.CREATE_COMBO_PRODUCT_OFFER;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,jsonObject,"comboOffer");
    }

    @Override
    public void onItemClicked(int prodId) {
        if(isProductSearch){
            isProductSearch = false;
            MyProductItem item = dbHelper.getProductDetails(prodId);
            MyProductItem myItem = myProductItems.get(position);
            myItem.setProdId(item.getProdId());
            myItem.setProdName(item.getProdName());
            //myItem.setProdMrp(item.getProdMrp());
            //myItem.setProdSp(item.getProdSp());
            comboOfferAdapter.notifyItemChanged(position);
        }else{
            isProductSearch = true;
            this.position = prodId;
            BottomSearchFragment bottomSearchFragment = new BottomSearchFragment();
            bottomSearchFragment.setCallingActivityName("productList");
            Bundle bundle = new Bundle();
            bundle.putString("flag","searchCartProduct");
            bottomSearchFragment.setArguments(bundle);
            bottomSearchFragment.setMyItemClickListener(this);
            bottomSearchFragment.show(getSupportFragmentManager(), "Search Product Bottom Sheet");

        }

    }
}
