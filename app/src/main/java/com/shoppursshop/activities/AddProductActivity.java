package com.shoppursshop.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.shoppursshop.R;
import com.shoppursshop.fragments.NetworkBaseFragment;
import com.shoppursshop.models.SpinnerItem;
import com.shoppursshop.utilities.ConnectionDetector;
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

public class AddProductActivity extends NetworkBaseActivity {

    private EditText editTextName,editTextBarCode,editTextHSN,editTextDesc,editTextMRP,editTextSP,
            editTextRL,editTextQOH,editTextMfgBy,editTextWarranty,editTextCGST,editTextIGST,editTextSGST,editTextMfgDate,editTextExpiryDate;
    private Spinner spinnerCategory,spinnerSubCategory;
    List<SpinnerItem> categoryListObject,subCatListObject;
    List<String> catList,subCatList;
    private ArrayAdapter<String> catAdapter,subCatAdapter;

    private DatePickerDialog datePicker1,datePicker2;
    private Calendar calendar;

    private String flag;
    private JSONObject dataObject;
    private boolean scanSelection;
    private Button btnAddProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();
    }

    private void init(){
        flag = getIntent().getStringExtra("flag");
        editTextName = findViewById(R.id.edit_product_name);
        editTextBarCode = findViewById(R.id.edit_product_barcode);
        editTextHSN = findViewById(R.id.edit_product_hsn_code);
        editTextDesc = findViewById(R.id.edit_product_desc);
        editTextMRP = findViewById(R.id.edit_product_mrp);
        editTextSP = findViewById(R.id.edit_product_sp);
        editTextRL = findViewById(R.id.edit_product_reorder_level);
        editTextQOH = findViewById(R.id.edit_product_qoh);
        editTextMfgBy = findViewById(R.id.edit_product_manufacture_by);
        editTextWarranty = findViewById(R.id.edit_product_warranty);
        editTextCGST = findViewById(R.id.edit_product_cgst);
        editTextIGST = findViewById(R.id.edit_product_igst);
        editTextSGST = findViewById(R.id.edit_product_sgst);
        editTextMfgDate = findViewById(R.id.edit_product_manufacture_date);
        editTextExpiryDate = findViewById(R.id.edit_product_expiry_date);
        btnAddProduct = findViewById(R.id.btn_add);
        spinnerCategory = findViewById(R.id.spinner_category);
        spinnerSubCategory = findViewById(R.id.spinner_sub_category);
        categoryListObject = dbHelper.getCategoriesAddProduct();
        catList = new ArrayList<>();
        subCatListObject = new ArrayList<>();
        subCatList = new ArrayList<>();

        for(SpinnerItem item : categoryListObject){
            catList.add(item.getName());
        }
        catList.add(0,"Select Category");
       // categoryListObject.add(new SpinnerItem());

        //stateList.add("New Delhi");
        catAdapter = new ArrayAdapter<String>(this, R.layout.simple_dropdown_list_item, catList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0){
                    return false;
                }else{
                    return true;
                }
            }
            @Override
            public View getView(int position, View convertView,
                                ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(getResources().getColor(R.color.grey500));
                }else{
                    tv.setTextColor(getResources().getColor(R.color.primary_text_color));
                }
                return view;
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(getResources().getColor(R.color.grey500));
                }else{
                    tv.setTextColor(getResources().getColor(R.color.primary_text_color));
                }
                tv.setPadding(20,20,20,20);
                return view;
            }
        };

        spinnerCategory.setAdapter(catAdapter);


        subCatList.add(0,"Select Sub Category");
        //stateList.add("New Delhi");
        subCatAdapter = new ArrayAdapter<String>(this, R.layout.simple_dropdown_list_item, subCatList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0){
                    return false;
                }else{
                    return true;
                }
            }
            @Override
            public View getView(int position, View convertView,
                                ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(getResources().getColor(R.color.grey500));
                }else{
                    tv.setTextColor(getResources().getColor(R.color.primary_text_color));
                }
                return view;
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(getResources().getColor(R.color.grey500));
                }else{
                    tv.setTextColor(getResources().getColor(R.color.primary_text_color));
                }
                tv.setPadding(20,20,20,20);
                return view;
            }
        };

        spinnerSubCategory.setAdapter(subCatAdapter);

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i > 0){
                    subCatList.clear();
                    subCatList.add("Select Sub Category");
                    setProductItems(categoryListObject.get(i-1).getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        datePicker1=new DatePickerDialog(AddProductActivity.this,new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker datePicker, int yr, int mon, int dy) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR,yr);
                cal.set(Calendar.MONTH,mon);
                cal.set(Calendar.DATE,dy);
                editTextMfgDate.setText(Utility.parseDate(cal,"yyyy-MM-dd"));
            }
        },year,month,day);
        datePicker1.setCancelable(false);
        datePicker1.setMessage("Select Manufacture Date");

        datePicker2=new DatePickerDialog(AddProductActivity.this,new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker datePicker, int yr, int mon, int dy) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR,yr);
                cal.set(Calendar.MONTH,mon);
                cal.set(Calendar.DATE,dy);
                editTextExpiryDate.setText(Utility.parseDate(cal,"yyyy-MM-dd"));
            }
        },year,month,day);
        datePicker2.setCancelable(false);
        datePicker2.setMessage("Select Expiry Date");

        editTextMfgDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker1.show();
            }
        });

        editTextExpiryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker2.show();
            }
        });

        if(flag.equals("scan")){
            try {
                dataObject = new JSONObject(getIntent().getStringExtra("data"));
                setCatSelection(dataObject.getString("cat"));
                editTextMfgDate.setText(dataObject.getString("mfg"));
                editTextExpiryDate.setText(dataObject.getString("expiryDate"));
                editTextName.setText(dataObject.getString("productName"));
                editTextBarCode.setText(dataObject.getString("barCode"));
                editTextHSN.setText(dataObject.getString("hsnCode"));
                editTextDesc.setText(dataObject.getString("desc"));
                editTextMRP.setText(dataObject.getString("mrp"));
                editTextSP.setText(dataObject.getString("sp"));
                editTextRL.setText(dataObject.getString("reorderLevel"));
                editTextQOH.setText(dataObject.getString("quantity"));
                editTextMfgBy.setText(dataObject.getString("manufactureBy"));
                editTextWarranty.setText(dataObject.getString("warranty"));
                editTextCGST.setText(dataObject.getString("cgst")+"%");
                editTextIGST.setText(dataObject.getString("igst")+"%");
                editTextSGST.setText(dataObject.getString("sgst")+"%");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ConnectionDetector.isNetworkAvailable(AddProductActivity.this)){
                    attemptAddProduct();
                }
            }
        });

        initFooter(this,1);

    }

    private void attemptAddProduct(){
       String subCatId = null;
       String prodName = editTextName.getText().toString();
       String barCode = editTextBarCode.getText().toString();
       String hsnCode = editTextHSN.getText().toString();
       String desc = editTextDesc.getText().toString();
       String mrp = editTextMRP.getText().toString();
       String sp = editTextSP.getText().toString();
       String reorderLevel = editTextRL.getText().toString();
       String qoh = editTextQOH.getText().toString();
       String mfgDate = editTextMfgDate.getText().toString();
       String expiryDate  = editTextExpiryDate.getText().toString();
       String mfgBy = editTextMfgBy.getText().toString();
       String warranty = editTextWarranty.getText().toString();
       String igst = editTextIGST.getText().toString();
       String sgst = editTextSGST.getText().toString();
       String cgst = editTextCGST.getText().toString();
       boolean cancel = false;
       View focus = null;

       if(spinnerSubCategory.getSelectedItemPosition() == 0){
           DialogAndToast.showDialog("Please select sub category",this);
           return;
       }else{
           subCatId = subCatListObject.get(spinnerSubCategory.getSelectedItemPosition()-1).getId();
       }

       if(TextUtils.isEmpty(prodName)){
           DialogAndToast.showDialog("Please enter product name",this);
           return;
       }

        if(TextUtils.isEmpty(barCode)){
            DialogAndToast.showDialog("Please enter barcode",this);
            return;
        }

        if(TextUtils.isEmpty(hsnCode)){
            DialogAndToast.showDialog("Please enter hsn code",this);
            return;
        }

        if(TextUtils.isEmpty(desc)){
            DialogAndToast.showDialog("Please enter description",this);
            return;
        }

        if(TextUtils.isEmpty(mrp)){
            DialogAndToast.showDialog("Please enter product mrp",this);
            return;
        }

        if(TextUtils.isEmpty(sp)){
            DialogAndToast.showDialog("Please enter product selling price",this);
            return;
        }

        if(TextUtils.isEmpty(reorderLevel)){
            DialogAndToast.showDialog("Please enter product reorder level",this);
            return;
        }

        if(TextUtils.isEmpty(qoh)){
            DialogAndToast.showDialog("Please enter product quantity",this);
            return;
        }

        if(TextUtils.isEmpty(mfgDate)){
            DialogAndToast.showDialog("Please enter product mfg date",this);
            return;
        }

        if(TextUtils.isEmpty(expiryDate)){
            DialogAndToast.showDialog("Please enter product expiry date",this);
            return;
        }

        if(TextUtils.isEmpty(mfgBy)){
            DialogAndToast.showDialog("Please enter product mfg company",this);
            return;
        }

        if(TextUtils.isEmpty(warranty)){
            DialogAndToast.showDialog("Please enter product warranty",this);
            return;
        }

        if(TextUtils.isEmpty(cgst)){
            DialogAndToast.showDialog("Please enter product cgst",this);
            return;
        }

        if(TextUtils.isEmpty(igst)){
            DialogAndToast.showDialog("Please enter product igst",this);
            return;
        }

        if(TextUtils.isEmpty(sgst)){
            DialogAndToast.showDialog("Please enter product sgst",this);
            return;
        }

       if(cancel){
           focus.requestFocus();
           return;
       }else{
           Map<String,String> params=new HashMap<>();
           params.put("prodCatId",subCatId);
           params.put("prodReorderLevel",reorderLevel);
           params.put("prodQoh",qoh);
           params.put("prodName",prodName);
           params.put("prodBarCode",barCode);
           params.put("prodDesc",desc);
           params.put("prodCgst",cgst);
           params.put("prodIgst",igst);
           params.put("prodSgst",sgst);
           params.put("prodWarranty",warranty);
           params.put("prodMrp",mrp);
           params.put("prodSp",sp);
           params.put("prodHsnCode",hsnCode);
           params.put("prodMfgDate",mfgDate);
           params.put("prodExpiryDate",expiryDate);
           params.put("prodMfgBy",mfgBy);
           params.put("action","3");
           params.put("prodImage1","");
           params.put("prodImage2","");
           params.put("prodImage3","");
           params.put("createdBy",sharedPreferences.getString(Constants.FULL_NAME,""));
           params.put("updatedBy",sharedPreferences.getString(Constants.FULL_NAME,""));
           params.put("retRetailerId",sharedPreferences.getString(Constants.USER_ID,""));
           params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
           params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
           params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
           JSONArray dataArray = new JSONArray();
           JSONObject dataObject = new JSONObject(params);
           dataArray.put(dataObject);
           String url=getResources().getString(R.string.url)+"/api/addProduct";
           showProgress(true);
           jsonArrayV2ApiRequest(Request.Method.POST,url,dataArray,"addProduct");
       }
    }



    private void setCatSelection(String catName){
        int i = 0;
       for(String name : catList){
           if(name.equals(catName)){
               scanSelection = true;
               spinnerCategory.setSelection(i);
               break;
           }
           i++;
       }
    }


    private void setProductItems(String catId){
        subCatListObject = dbHelper.getCatSubCategoriesAddProduct(catId);
        for(SpinnerItem item : subCatListObject){
            subCatList.add(item.getName());
        }

        subCatAdapter.notifyDataSetChanged();

        if(scanSelection){
            int i = 0;
            for(String name : subCatList){
                try {
                    if(name.equals(dataObject.getString("productItem"))){
                        scanSelection = false;
                        spinnerSubCategory.setSelection(i);
                        break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                i++;
            }
            scanSelection = false;
        }else{
            spinnerSubCategory.setSelection(0);
        }

        subCatAdapter.notifyDataSetChanged();
    }

}
