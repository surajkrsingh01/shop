package com.shoppursshop.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.shoppursshop.R;
import com.shoppursshop.fragments.NetworkBaseFragment;
import com.shoppursshop.models.Barcode;
import com.shoppursshop.models.MyProductItem;
import com.shoppursshop.models.SpinnerItem;
import com.shoppursshop.utilities.ConnectionDetector;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.DialogAndToast;
import com.shoppursshop.utilities.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddProductActivity extends BaseImageActivity {

    private EditText editTextName,editTextCode,editTextBarCode,editTextHSN,editTextDesc,editTextMRP,editTextSP,
            editTextRL,editTextQOH,editTextMfgBy,editTextWarranty,editTextCGST,editTextIGST,editTextSGST,editTextMfgDate,editTextExpiryDate;
    private AppCompatCheckBox checkBoxIsBarAvaialble;
    private TextInputLayout tipBarcode;
    private TextView tvHeaderLabel;
    private RelativeLayout btnPhoto1,btnPhoto2,btnPhoto3,relativeLayoutAction;
    private TextView tvAction;
    private ImageView imageView1,imageView2,imageView3;
    private Spinner spinnerCategory,spinnerSubCategory;
    List<SpinnerItem> categoryListObject,subCatListObject;
    List<String> catList,subCatList;
    private ArrayAdapter<String> catAdapter,subCatAdapter;
    private List<String> imageList;

    private DatePickerDialog datePicker1,datePicker2;
    private Calendar calendar;

    private String flag;
    private JSONObject dataObject;
    private boolean scanSelection;
    private int imagePosition;

    private MyProductItem myProductItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initFooterAction(this);
        init();
    }

    private void init(){
        imageList = new ArrayList<>();
        imageList.add("no");
        imageList.add("no");
        imageList.add("no");
        flag = getIntent().getStringExtra("flag");
        tvHeaderLabel = findViewById(R.id.text_sub_header);
        editTextName = findViewById(R.id.edit_product_name);
        editTextCode = findViewById(R.id.edit_product_code);
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
        tipBarcode = findViewById(R.id.til_barcode);
        checkBoxIsBarAvaialble = findViewById(R.id.checkbox_is_barcode_available);
        imageView1 = findViewById(R.id.image_view_1);
        imageView2 = findViewById(R.id.image_view_2);
        imageView3 = findViewById(R.id.image_view_3);
        relativeLayoutAction = findViewById(R.id.relative_footer_action);
        tvAction = findViewById(R.id.text_action);
        btnPhoto1 = findViewById(R.id.relative_image_1);
        btnPhoto2 = findViewById(R.id.relative_image_2);
        btnPhoto3 = findViewById(R.id.relative_image_3);
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
                    if(isDarkTheme){
                        tv.setTextColor(getResources().getColor(R.color.white));
                    }else{
                        tv.setTextColor(getResources().getColor(R.color.primary_text_color));
                    }
                }
                return view;
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                if(isDarkTheme){
                    view.setBackgroundColor(getResources().getColor(R.color.dark_color));
                }else{
                    view.setBackgroundColor(getResources().getColor(R.color.white));
                }
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(getResources().getColor(R.color.grey500));
                }else{
                    if(isDarkTheme){
                        tv.setTextColor(getResources().getColor(R.color.white));
                    }else{
                        tv.setTextColor(getResources().getColor(R.color.primary_text_color));
                    }
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
                    if(isDarkTheme){
                        tv.setTextColor(getResources().getColor(R.color.white));
                    }else{
                        tv.setTextColor(getResources().getColor(R.color.primary_text_color));
                    }
                }
                return view;
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                if(isDarkTheme){
                    view.setBackgroundColor(getResources().getColor(R.color.dark_color));
                }else{
                    view.setBackgroundColor(getResources().getColor(R.color.white));
                }
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(getResources().getColor(R.color.grey500));
                }else{
                    if(isDarkTheme){
                        tv.setTextColor(getResources().getColor(R.color.white));
                    }else{
                        tv.setTextColor(getResources().getColor(R.color.primary_text_color));
                    }
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
                    setSubCatItems(categoryListObject.get(i-1).getId());
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
        }else if(flag.equals("editProduct")){
            myProductItem = (MyProductItem)getIntent().getSerializableExtra("product");
            editTextMfgDate.setText(myProductItem.getProdMfgDate());
            editTextExpiryDate.setText(myProductItem.getProdExpiryDate());
            editTextName.setText(myProductItem.getProdName());
            editTextCode.setText(myProductItem.getProdCode());
            editTextBarCode.setVisibility(View.GONE);
            checkBoxIsBarAvaialble.setVisibility(View.GONE);
            editTextHSN.setText(myProductItem.getProdHsnCode());
            editTextDesc.setText(myProductItem.getProdDesc());
            editTextMRP.setText(""+myProductItem.getProdMrp());
            editTextSP.setText(""+myProductItem.getProdSp());
            editTextRL.setText(""+myProductItem.getProdReorderLevel());
            editTextQOH.setText(""+myProductItem.getProdQoh());
            editTextMfgBy.setText(myProductItem.getProdMfgBy());
            editTextWarranty.setText(""+myProductItem.getProdWarranty());
            editTextCGST.setText(""+myProductItem.getProdCgst());
            editTextIGST.setText(""+myProductItem.getProdIgst());
            editTextSGST.setText(""+myProductItem.getProdSgst());
            tvHeaderLabel.setText("Update");
            setCatSelection(dbHelper.getCategoryName(myProductItem.getProdCatId()));


            if(myProductItem.getProdImage1().contains("http")){
                imageView1.setVisibility(View.VISIBLE);
                Glide.with(this)
                        .load(myProductItem.getProdImage1())
                        .apply(requestOptions)
                        .error(R.drawable.ic_photo_black_192dp)
                        .into(imageView1);
            }
            if(myProductItem.getProdImage2().contains("http")){
                imageView2.setVisibility(View.VISIBLE);
                Glide.with(this)
                        .load(myProductItem.getProdImage2())
                        .apply(requestOptions)
                        .error(R.drawable.ic_photo_black_192dp)
                        .into(imageView2);
            }

            if(myProductItem.getProdImage3().contains("http")){
                imageView3.setVisibility(View.VISIBLE);
                Glide.with(this)
                        .load(myProductItem.getProdImage3())
                        .apply(requestOptions)
                        .error(R.drawable.ic_photo_black_192dp)
                        .into(imageView3);
            }
        }

        checkBoxIsBarAvaialble.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    tipBarcode.setVisibility(View.VISIBLE);
                }else{
                    tipBarcode.setVisibility(View.GONE);
                }
            }
        });

        editTextBarCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    Intent intent = new Intent(AddProductActivity.this,ScannarActivity.class);
                    intent.putExtra("flag","scan");
                    intent.putExtra("type","addProduct");
                   // startActivity(intent);
                    startActivityForResult(intent,2);
                }
            }
        });

        editTextBarCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddProductActivity.this,ScannarActivity.class);
                intent.putExtra("flag","scan");
                intent.putExtra("type","addProduct");
                //startActivity(intent);
                startActivityForResult(intent,2);
            }
        });

        btnPhoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePosition = 1;
                selectImage();
            }
        });

        btnPhoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePosition = 2;
                selectImage();
            }
        });

        btnPhoto3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePosition = 3;
                selectImage();
            }
        });

        relativeLayoutAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ConnectionDetector.isNetworkAvailable(AddProductActivity.this)){
                    if(flag.equals("editProduct")){
                        updateProduct();
                    }else{
                        attemptAddProduct();
                    }

                }
            }
        });

        if(flag.equals("editProduct")){
            tvAction.setText("Update Product");
        }else{
            tvAction.setText("Add Product");
        }


        //initFooter(this,1);

    }

    private void attemptAddProduct(){
       String subCatId = null;
       String prodName = editTextName.getText().toString();
        String prodCode = editTextCode.getText().toString();
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

        if(TextUtils.isEmpty(prodCode)){
            DialogAndToast.showDialog("Please enter product code",this);
            return;
        }

        if(TextUtils.isEmpty(barCode)){
          //  DialogAndToast.showDialog("Please enter barcode",this);
         //   return;
            barCode = "";
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
           params.put("prodCode",prodCode);
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
           params.put("prodImage1",imageList.get(0));
           params.put("prodImage2",imageList.get(1));
           params.put("prodImage3",imageList.get(2));
           params.put("shopCode",sharedPreferences.getString(Constants.SHOP_CODE,""));
           params.put("createdBy",sharedPreferences.getString(Constants.FULL_NAME,""));
           params.put("updatedBy",sharedPreferences.getString(Constants.FULL_NAME,""));
           params.put("retRetailerId",sharedPreferences.getString(Constants.USER_ID,""));
           params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
           params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
           params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
           JSONArray dataArray = new JSONArray();
           JSONObject dataObject = new JSONObject(params);
           if(!TextUtils.isEmpty(barCode)){
               List<Barcode> barcodeList = new ArrayList<>();
               Barcode barcode = new Barcode();
               barcode.setBarcode(barCode);
               barcodeList.add(barcode);
               try {
                   dataObject.put("barcodeList",barcodeList);
               } catch (JSONException e) {
                   e.printStackTrace();
               }
           }
           dataArray.put(dataObject);
           String url=getResources().getString(R.string.url)+"/api/products/addProduct";
           showProgress(true);
           jsonArrayV2ApiRequest(Request.Method.POST,url,dataArray,"addProduct");
       }
    }

    private void updateProduct(){
        String subCatId = null;
        String prodName = editTextName.getText().toString();
        String prodCode = editTextCode.getText().toString();
        //String barCode = editTextBarCode.getText().toString();
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

        if(TextUtils.isEmpty(prodCode)){
            DialogAndToast.showDialog("Please enter product code",this);
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
        }else {
            Map<String, String> params = new HashMap<>();
            params.put("prodId", ""+myProductItem.getProdId());
            params.put("prodCatId", subCatId);
            params.put("prodReorderLevel", reorderLevel);
            params.put("prodQoh", qoh);
            params.put("prodName", prodName);
            params.put("prodCode", prodCode);
            params.put("prodDesc", desc);
            params.put("prodCgst", cgst);
            params.put("prodIgst", igst);
            params.put("prodSgst", sgst);
            params.put("prodWarranty", warranty);
            params.put("prodMrp", mrp);
            params.put("prodSp", sp);
            params.put("prodHsnCode", hsnCode);
            params.put("prodMfgDate", mfgDate);
            params.put("prodExpiryDate", expiryDate);
            params.put("prodMfgBy", mfgBy);
            params.put("action", "3");
            params.put("prodImage1", imageList.get(0));
            params.put("prodImage2", imageList.get(1));
            params.put("prodImage3", imageList.get(2));
            params.put("shopCode", sharedPreferences.getString(Constants.SHOP_CODE, ""));
            params.put("createdBy", sharedPreferences.getString(Constants.FULL_NAME, ""));
            params.put("updatedBy", sharedPreferences.getString(Constants.FULL_NAME, ""));
            params.put("retRetailerId", sharedPreferences.getString(Constants.USER_ID, ""));
            params.put("dbName", sharedPreferences.getString(Constants.DB_NAME, ""));
            params.put("dbUserName", sharedPreferences.getString(Constants.DB_USER_NAME, ""));
            params.put("dbPassword", sharedPreferences.getString(Constants.DB_PASSWORD, ""));
            String url = getResources().getString(R.string.url) + "/api/products/updateProduct";
            showProgress(true);
            jsonObjectApiRequest(Request.Method.POST, url, new JSONObject(params), "updateProduct");
        }
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {
        if(apiName.equals("addProduct")){
            try {
                  if(response.getBoolean("status")){
                      JSONObject jsonObject = response.getJSONObject("result");
                      JSONArray barArray = null;
                      int barLen = 0;
                      MyProductItem productItem = new MyProductItem();
                      productItem.setProdId(jsonObject.getInt("prodId"));
                      productItem.setProdCatId(jsonObject.getInt("prodCatId"));
                      productItem.setProdName(jsonObject.getString("prodName"));
                      productItem.setProdCode(jsonObject.getString("prodCode"));
                      productItem.setProdDesc(jsonObject.getString("prodDesc"));
                      productItem.setProdReorderLevel(jsonObject.getInt("prodReorderLevel"));
                      productItem.setProdQoh(jsonObject.getInt("prodQoh"));
                      productItem.setProdHsnCode(jsonObject.getString("prodHsnCode"));
                      productItem.setProdCgst(Float.parseFloat(jsonObject.getString("prodCgst")));
                      productItem.setProdIgst(Float.parseFloat(jsonObject.getString("prodIgst")));
                      productItem.setProdSgst(Float.parseFloat(jsonObject.getString("prodSgst")));
                      productItem.setProdWarranty(Float.parseFloat(jsonObject.getString("prodWarranty")));
                      productItem.setProdMfgDate(jsonObject.getString("prodMfgDate"));
                      productItem.setProdExpiryDate(jsonObject.getString("prodExpiryDate"));
                      productItem.setProdMfgBy(jsonObject.getString("prodMfgBy"));
                      productItem.setProdImage1(jsonObject.getString("prodImage1"));
                      productItem.setProdImage2(jsonObject.getString("prodImage2"));
                      productItem.setProdImage3(jsonObject.getString("prodImage3"));
                      productItem.setProdMrp(Float.parseFloat(jsonObject.getString("prodMrp")));
                      productItem.setProdSp(Float.parseFloat(jsonObject.getString("prodSp")));
                      productItem.setCreatedBy(jsonObject.getString("createdBy"));
                      productItem.setUpdatedBy(jsonObject.getString("updatedBy"));
                      productItem.setCreatedDate(jsonObject.getString("createdDate"));
                      productItem.setUpdatedDate(jsonObject.getString("updatedDate"));
                      productItem.setIsBarCodeAvailable(jsonObject.getString("isBarcodeAvailable"));
                      dbHelper.addProduct(productItem,Utility.getTimeStamp(),Utility.getTimeStamp());
                      if(!jsonObject.getString("barcodeList").equals("null")){
                          barArray = jsonObject.getJSONArray("barcodeList");
                          barLen = barArray.length();
                          for(int j = 0; j<barLen; j++){
                              dbHelper.addProductBarcode(productItem.getProdId(),barArray.getJSONObject(j).getString("barcode"));
                          }
                      }
                      clearData();
                      DialogAndToast.showToast(response.getString("message"),AddProductActivity.this);
                  }else{
                      DialogAndToast.showDialog(response.getString("message"),AddProductActivity.this);
                  }
            }catch (JSONException e){

            }
        }else if(apiName.equals("updateProduct")){
            try {
                if(response.getBoolean("status")){
                    JSONObject jsonObject = response.getJSONObject("result");
                    JSONArray barArray = null;
                    int barLen = 0;
                    MyProductItem productItem = new MyProductItem();
                    productItem.setProdId(jsonObject.getInt("prodId"));
                    productItem.setProdCatId(jsonObject.getInt("prodCatId"));
                    productItem.setProdName(jsonObject.getString("prodName"));
                    productItem.setProdCode(jsonObject.getString("prodCode"));
                    productItem.setProdDesc(jsonObject.getString("prodDesc"));
                    productItem.setProdReorderLevel(jsonObject.getInt("prodReorderLevel"));
                    productItem.setProdQoh(jsonObject.getInt("prodQoh"));
                    productItem.setProdHsnCode(jsonObject.getString("prodHsnCode"));
                    productItem.setProdCgst(Float.parseFloat(jsonObject.getString("prodCgst")));
                    productItem.setProdIgst(Float.parseFloat(jsonObject.getString("prodIgst")));
                    productItem.setProdSgst(Float.parseFloat(jsonObject.getString("prodSgst")));
                    productItem.setProdWarranty(Float.parseFloat(jsonObject.getString("prodWarranty")));
                    productItem.setProdMfgDate(jsonObject.getString("prodMfgDate"));
                    productItem.setProdExpiryDate(jsonObject.getString("prodExpiryDate"));
                    productItem.setProdMfgBy(jsonObject.getString("prodMfgBy"));
                    productItem.setProdImage1(jsonObject.getString("prodImage1"));
                    productItem.setProdImage2(jsonObject.getString("prodImage2"));
                    productItem.setProdImage3(jsonObject.getString("prodImage3"));
                    productItem.setProdMrp(Float.parseFloat(jsonObject.getString("prodMrp")));
                    productItem.setProdSp(Float.parseFloat(jsonObject.getString("prodSp")));
                    productItem.setUpdatedBy(jsonObject.getString("updatedBy"));
                    productItem.setUpdatedDate(jsonObject.getString("updatedDate"));
                    dbHelper.updateProduct(productItem,Utility.getTimeStamp());
                   // clearData();
                    showMyDialog(response.getString("message"));
                    finish();
                }else{
                    DialogAndToast.showDialog(response.getString("message"),AddProductActivity.this);
                }
            }catch (JSONException e){

            }
        }
    }

    @Override
    public void onDialogPositiveClicked(){
        Intent intent = new Intent();
        intent.putExtra("status","updated");
        setResult(-1,intent);
        finish();
    }

    private void clearData(){
        editTextName.setText("");
        editTextCode.setText("");
        editTextBarCode.setText("");
        editTextHSN.setText("");
        editTextDesc.setText("");
        editTextMRP.setText("");
        editTextSP.setText("");
        editTextRL.setText("");
        editTextQOH.setText("");
        editTextMfgDate.setText("");
        editTextExpiryDate.setText("");
        editTextMfgBy.setText("");
        editTextWarranty.setText("");
        editTextIGST.setText("");
        editTextSGST.setText("");
        editTextCGST.setText("");
        imageList.set(0,"no");
        imageList.set(1,"no");
        imageList.set(2,"no");
        spinnerCategory.setSelection(0);
        spinnerSubCategory.setSelection(0);
        Glide.with(this).clear(imageView1);
        Glide.with(this).clear(imageView2);
        Glide.with(this).clear(imageView3);
        imageView1.setVisibility(View.GONE);
        imageView2.setVisibility(View.GONE);
        imageView3.setVisibility(View.GONE);
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



    private void setSubCatItems(String catId){
        subCatListObject = dbHelper.getCatSubCategoriesAddProduct(catId);
        for(SpinnerItem item : subCatListObject){
            subCatList.add(item.getName());
        }

        //subCatAdapter.notifyDataSetChanged();

        if(scanSelection){
            int i = 0;
            for(String name : subCatList){
                if(name.equals(dbHelper.getSubCategoryName(myProductItem.getProdCatId()))){
                    scanSelection = false;
                    spinnerSubCategory.setSelection(i);
                    break;
                }
                i++;
            }
            scanSelection = false;
        }else{
            spinnerSubCategory.setSelection(0);
        }

        subCatAdapter.notifyDataSetChanged();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i(TAG,"Result received");

        if (requestCode == 2){
            if(data != null){
                editTextBarCode.setText(data.getStringExtra("barCode"));
            }
        }
    }

    @Override
    protected void imageAdded(){

        if(imagePosition == 1){
            imageView1.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(imagePath)
                    .apply(requestOptions)
                    .into(imageView1);
        }else if(imagePosition == 2){
            imageView2.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(imagePath)
                    .apply(requestOptions)
                    .into(imageView2);
        }else{
            imageView3.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(imagePath)
                    .apply(requestOptions)
                    .into(imageView3);
        }

         imageList.set(imagePosition-1,convertToBase64(new File(imagePath)));
         Log.i(TAG,"added image is "+imageList.get(imagePosition-1));
    }

}
