package com.shoppursshop.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.shoppursshop.R;
import com.shoppursshop.models.SpinnerItem;
import com.shoppursshop.utilities.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddProductActivity extends BaseActivity {

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
        spinnerCategory = findViewById(R.id.spinner_category);
        spinnerSubCategory = findViewById(R.id.spinner_sub_category);
        categoryListObject = new ArrayList<>();
        catList = new ArrayList<>();
        subCatListObject = new ArrayList<>();
        subCatList = new ArrayList<>();

        catList.add(0,"Select Category");
        catList.add("Grocery");
        catList.add("Stationary");
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


        subCatList.add(0,"Select Product");
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
                    subCatList.add("Select Product");
                    setProductItems(catList.get(i));
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
                editTextMfgDate.setText(Utility.parseDate(cal,"dd/MM/yyyy"));
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
                editTextExpiryDate.setText(Utility.parseDate(cal,"dd/MM/yyyy"));
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

        initFooter(this,1);

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


    private void setProductItems(String productName){
        if(productName.equals("Grocery")){
            subCatList.add("Breakfast & Dairy");
            subCatList.add("Masala & Spices");
            subCatList.add("Personal Care");
            subCatList.add("Beverages");
        }else{
            subCatList.add("Pen & Pen Sets");
            subCatList.add("Notebooks");
            subCatList.add("Papers");
            subCatList.add("Color & Paints");
            subCatList.add("Desk Organizer");
            subCatList.add("Markers");
        }

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
