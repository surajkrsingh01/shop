package com.shoppursshop.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.google.android.material.textfield.TextInputLayout;
import com.shoppursshop.R;
import com.shoppursshop.adapters.SpecificationAdapter;
import com.shoppursshop.fragments.NetworkBaseFragment;
import com.shoppursshop.interfaces.MyItemTypeClickListener;
import com.shoppursshop.models.Barcode;
import com.shoppursshop.models.MyColor;
import com.shoppursshop.models.MyProductItem;
import com.shoppursshop.models.MyUnitMeasure;
import com.shoppursshop.models.ProductColor;
import com.shoppursshop.models.ProductSize;
import com.shoppursshop.models.ProductUnit;
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

public class AddProductActivity extends BaseImageActivity implements View.OnClickListener, MyItemTypeClickListener {

    private final int UNIT = 1,SIZE = 2,COLOR = 3;

    private EditText editTextName,editTextCode,editTextBarCode,editTextHSN,editTextDesc,editTextMRP,editTextSP,
            editTextRL,editTextQOH,editTextMfgBy,editTextWarranty,editTextCGST,editTextIGST,editTextSGST,editTextMfgDate,editTextExpiryDate;
    private AppCompatCheckBox checkBoxIsBarAvaialble;
    private TextInputLayout tipBarcode;
    private TextView tvHeaderLabel;
    private RelativeLayout btnPhoto1,btnPhoto2,btnPhoto3,relativeLayoutAction;
    private TextView tvAction;
    private ImageView imageView1,imageView2,imageView3;
    private Spinner spinnerCategory,spinnerSubCategory,spinnerSize;
    List<SpinnerItem> categoryListObject,subCatListObject;
    List<String> catList,subCatList,sizeSpinnerList;
    private ArrayAdapter<String> catAdapter,subCatAdapter,sizeSpinnerAdapter;
    private List<String> imageList;

    private DatePickerDialog datePicker1,datePicker2;
    private Calendar calendar;

    private String flag;
    private JSONObject dataObject;
    private boolean scanSelection;
    private int imagePosition;

    private TextView tvUnitSizeColor;
    private RelativeLayout rlProductSpecificationLayout;
    private LinearLayout llUnit,llSize,llColor;
    private EditText etUnitName,etUnitValue,etSize,etColorName;
    private ImageView ivChooseColor;
    private Button btnSave;
    private RecyclerView recyclerView;
    private SpecificationAdapter unitAdapter,sizeAdapter,colorAdapter;
    List<Object> unitList,sizeList,colorList;
    private int specificationType;
    private ImageView imageViewRed,imageViewGreen,imageViewBlue,imageViewPink,imageViewYellow,imageViewAmber,
            imageViewTeal,imageViewPurple,imageViewIndigo,imageViewTemp;
    private RelativeLayout relativeLayoutRed,relativeLayoutGreen,relativeLayoutBlue,relativeLayoutPink,
            relativeLayoutYellow,relativeLayoutAmber,
            relativeLayoutTeal,relativeLayoutIndigo,relativeLayoutPurple;
    private int colorValue;
    private String colorName;
    private boolean colorSelected,viewsDisabled;


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
        tvUnitSizeColor = findViewById(R.id.tvUnitSizeColor);
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

        editTextCGST.setText("0");
        editTextSGST.setText("0");
        editTextIGST.setText("0");

        specificationType = UNIT;
        etUnitName = findViewById(R.id.edit_unit_name);
        etUnitValue = findViewById(R.id.edit_unit_value);
        etSize = findViewById(R.id.edit_size);
        btnSave = findViewById(R.id.btn_save);
        relativeLayoutRed = findViewById(R.id.relative_red);
        relativeLayoutBlue= findViewById(R.id.relative_blue);
        relativeLayoutGreen = findViewById(R.id.relative_green);
        relativeLayoutPink = findViewById(R.id.relative_pink);
        relativeLayoutYellow = findViewById(R.id.relative_yellow);
        relativeLayoutAmber = findViewById(R.id.relative_amber);
        relativeLayoutTeal = findViewById(R.id.relative_teal);
        relativeLayoutIndigo = findViewById(R.id.relative_indigo);
        relativeLayoutPurple = findViewById(R.id.relative_purple);
        imageViewRed = findViewById(R.id.image_color_red);
        imageViewBlue= findViewById(R.id.image_color_blue);
        imageViewGreen = findViewById(R.id.image_color_green);
        imageViewPink = findViewById(R.id.image_color_pink);
        imageViewYellow = findViewById(R.id.image_color_yellow);
        imageViewAmber = findViewById(R.id.image_color_amber);
        imageViewTeal = findViewById(R.id.image_color_teal);
        imageViewIndigo = findViewById(R.id.image_color_indigo);
        imageViewPurple = findViewById(R.id.image_color_purple);
        changeColor(imageViewRed.getBackground(),getResources().getColor(R.color.red_500));
        changeColor(imageViewBlue.getBackground(),getResources().getColor(R.color.blue500));
        changeColor(imageViewGreen.getBackground(),getResources().getColor(R.color.green500));
        changeColor(imageViewPink.getBackground(),getResources().getColor(R.color.pink500));
        changeColor(imageViewYellow.getBackground(),getResources().getColor(R.color.yellow500));
        //  changeColor(imageViewGrey.getBackground(),getResources().getColor(R.color.grey600));
        changeColor(imageViewAmber.getBackground(),getResources().getColor(R.color.amber600));
        changeColor(imageViewIndigo.getBackground(),getResources().getColor(R.color.indigo_500));
        relativeLayoutRed.setOnClickListener(this);
        relativeLayoutBlue.setOnClickListener(this);
        relativeLayoutGreen.setOnClickListener(this);
        relativeLayoutPink.setOnClickListener(this);
        relativeLayoutYellow.setOnClickListener(this);
        relativeLayoutAmber.setOnClickListener(this);
        relativeLayoutPurple.setOnClickListener(this);
        relativeLayoutIndigo.setOnClickListener(this);
        relativeLayoutTeal.setOnClickListener(this);
        recyclerView = findViewById(R.id.recycler_view);

        tipBarcode = findViewById(R.id.til_barcode);
        checkBoxIsBarAvaialble = findViewById(R.id.checkbox_is_barcode_available);
        imageView1 = findViewById(R.id.image_view_1);
        imageView2 = findViewById(R.id.image_view_2);
        imageView3 = findViewById(R.id.image_view_3);
        relativeLayoutAction = findViewById(R.id.relative_footer_action);
        rlProductSpecificationLayout = findViewById(R.id.rl_product_specification_layout);
        tvAction = findViewById(R.id.text_action);
        btnPhoto1 = findViewById(R.id.relative_image_1);
        btnPhoto2 = findViewById(R.id.relative_image_2);
        btnPhoto3 = findViewById(R.id.relative_image_3);
        llUnit = findViewById(R.id.ll_unit_layout);
        llSize = findViewById(R.id.ll_size_layout);
        llColor = findViewById(R.id.ll_color_layout);
        spinnerCategory = findViewById(R.id.spinner_category);
        spinnerSubCategory = findViewById(R.id.spinner_sub_category);
        spinnerSize = findViewById(R.id.spinner_size);
        categoryListObject = dbHelper.getCategoriesAddProduct();
        catList = new ArrayList<>();
        subCatListObject = new ArrayList<>();
        subCatList = new ArrayList<>();
        sizeSpinnerList = new ArrayList<>();

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

        sizeSpinnerAdapter = new ArrayAdapter<String>(this, R.layout.simple_dropdown_list_item, sizeSpinnerList){
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

        spinnerSize.setAdapter(sizeSpinnerAdapter);

        spinnerSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i > 0){
                    colorList.clear();
                    ProductSize size = (ProductSize)sizeList.get(i-1);
                    try {
                        for(ProductColor color : size.getProductColorList()){
                            colorList.add(color);
                        }
                    }catch (NullPointerException npe){
                       npe.printStackTrace();
                    }

                    colorAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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
           // editTextBarCode.setVisibility(View.GONE);
           // checkBoxIsBarAvaialble.setVisibility(View.GONE);
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
            spinnerCategory.setEnabled(false);
            spinnerSubCategory.setEnabled(false);

            if(myProductItem.getProdImage1().contains("http")){
                imageView1.setVisibility(View.VISIBLE);
                requestOptions.signature(new ObjectKey(sharedPreferences.getString("product_signature"+myProductItem.getProdId()+"_1","")));
                Glide.with(this)
                        .load(myProductItem.getProdImage1())
                        .apply(requestOptions)
                        .error(R.drawable.ic_photo_black_192dp)
                        .into(imageView1);
            }
            if(myProductItem.getProdImage2().contains("http")){
                imageView2.setVisibility(View.VISIBLE);
                requestOptions.signature(new ObjectKey(sharedPreferences.getString("product_signature"+myProductItem.getProdId()+"_2","")));
                Glide.with(this)
                        .load(myProductItem.getProdImage2())
                        .apply(requestOptions)
                        .error(R.drawable.ic_photo_black_192dp)
                        .into(imageView2);
            }

            if(myProductItem.getProdImage3().contains("http")){
                imageView3.setVisibility(View.VISIBLE);
                requestOptions.signature(new ObjectKey(sharedPreferences.getString("product_signature"+myProductItem.getProdId()+"_3","")));
                Glide.with(this)
                        .load(myProductItem.getProdImage3())
                        .apply(requestOptions)
                        .error(R.drawable.ic_photo_black_192dp)
                        .into(imageView3);
            }
        }else{
            String timestamp = Utility.getTimeStamp();
            timestamp = timestamp.replaceAll("-","").replaceAll(" ","").replaceAll(":","");
            //timestamp = timestamp.replaceAll(":","");
            editTextCode.setText(sharedPreferences.getString(Constants.SHOP_CODE,"")+"/prd/"+timestamp);
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
                if(!viewsDisabled)
                selectImage();
            }
        });

        btnPhoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePosition = 2;
                if(!viewsDisabled)
                selectImage();
            }
        });

        btnPhoto3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePosition = 3;
                if(!viewsDisabled)
                selectImage();
            }
        });

        relativeLayoutAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ConnectionDetector.isNetworkAvailable(AddProductActivity.this)){
                    if(!viewsDisabled){
                        if(flag.equals("editProduct")){
                            updateProduct();
                        }else{
                            attemptAddProduct();
                        }
                    }
                }
            }
        });

        if(flag.equals("editProduct")){
            tvAction.setText("Update Product");
        }else{
            tvAction.setText("Add Product");
        }

        tvUnitSizeColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!viewsDisabled)
                rlProductSpecificationLayout.setVisibility(View.VISIBLE);
            }
        });

        ImageView ivClear = findViewById(R.id.iv_clear);
        ivClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rlProductSpecificationLayout.setVisibility(View.GONE);
            }
        });



        LinearLayout llContainer = findViewById(R.id.container);
        llContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //rlProductSpecificationLayout.setVisibility(View.GONE);
            }
        });

        TextView tvUnit = findViewById(R.id.tvUnit);
        tvUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                specificationType = UNIT;
                llUnit.setVisibility(View.VISIBLE);
                llSize.setVisibility(View.GONE);
                llColor.setVisibility(View.GONE);
                initUnitList();
            }
        });

        TextView tvSize = findViewById(R.id.tvSize);
        tvSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                specificationType = SIZE;
                llUnit.setVisibility(View.GONE);
                llSize.setVisibility(View.VISIBLE);
                llColor.setVisibility(View.GONE);
                initSizeList();
            }
        });

        TextView tvColor = findViewById(R.id.tvColor);
        tvColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                specificationType = COLOR;
                llUnit.setVisibility(View.GONE);
                llSize.setVisibility(View.GONE);
                llColor.setVisibility(View.VISIBLE);
                initColorList();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(specificationType == UNIT){
                    saveUnit();
                }else if(specificationType == SIZE){
                    saveSize();
                }if(specificationType == COLOR){
                    saveColor();
                }
            }
        });

        initUnitColorSizeList();
        initUnitList();
        //initFooter(this,1);

    }

    private void attemptAddProduct(){
       String subCatId = null;
        String catId = null;
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
           catId = categoryListObject.get(spinnerCategory.getSelectedItemPosition()-1).getId();
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

       /*  if(TextUtils.isEmpty(expiryDate)){
            DialogAndToast.showDialog("Please enter product expiry date",this);
            return;
        }*/

        if(TextUtils.isEmpty(mfgBy)){
            DialogAndToast.showDialog("Please enter product mfg company",this);
            return;
        }

       /*   if(TextUtils.isEmpty(warranty)){
            DialogAndToast.showDialog("Please enter product warranty",this);
            return;
        }*/

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

        if(Float.parseFloat(cgst) != Float.parseFloat(sgst)){
            DialogAndToast.showDialog("CGST and SGST must be same.",this);
            return;
        }

        if(Float.parseFloat(mrp) < Float.parseFloat(sp)){
            DialogAndToast.showDialog("Product enter Selling price less than MRP.",this);
            return;
        }

       if(cancel){
           focus.requestFocus();
           return;
       }else{
           Map<String,String> params=new HashMap<>();
           params.put("prodCatId",catId);
           params.put("prodSubCatId",subCatId);
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

           try{

               if(unitList.size() > 0){
                   JSONArray unitArray = new JSONArray();
                   JSONObject unitObject = null;
                   ProductUnit productUnit = null;
                   for(Object ob : unitList){
                       unitObject = new JSONObject();
                       productUnit = (ProductUnit)ob;
                       unitObject.put("id",productUnit.getId());
                       unitObject.put("unitName",productUnit.getUnitName());
                       unitObject.put("unitValue",productUnit.getUnitValue());
                       unitObject.put("status",productUnit.getStatus());
                       unitArray.put(unitObject);
                   }
                   dataObject.put("productUnitList", unitArray);
               }

               if(sizeList != null && sizeList.size() > 0){
                   JSONArray sizeArray = new JSONArray();
                   JSONObject sizeObject = null;
                   ProductSize productSize = null;
                   for(Object ob : sizeList){
                       sizeObject = new JSONObject();
                       productSize = (ProductSize)ob;
                       sizeObject.put("id",productSize.getId());
                       sizeObject.put("size",productSize.getSize());
                       sizeObject.put("status",productSize.getStatus());

                       JSONArray colorArray = new JSONArray();
                       JSONObject colorObject = null;
                       //ProductColor productColor = null;
                       for(ProductColor productColor : productSize.getProductColorList()){
                           colorObject = new JSONObject();
                           colorObject.put("id",productColor.getId());
                           colorObject.put("sizeId",productColor.getSizeId());
                           colorObject.put("colorName",productColor.getColorName());
                           colorObject.put("colorValue",productColor.getColorValue());
                           colorObject.put("status",productColor.getStatus());
                           colorArray.put(colorObject);
                       }

                       if(colorArray.length() > 0)
                           sizeObject.put("productColorList", colorArray);

                       sizeArray.put(sizeObject);
                   }
                   dataObject.put("productSizeList", sizeArray);
               }

           }catch (JSONException e){
               e.fillInStackTrace();
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

       /* if(TextUtils.isEmpty(expiryDate)){
            DialogAndToast.showDialog("Please enter product expiry date",this);
            return;
        }*/

        if(TextUtils.isEmpty(mfgBy)){
            DialogAndToast.showDialog("Please enter product mfg company",this);
            return;
        }

       /* if(TextUtils.isEmpty(warranty)){
            DialogAndToast.showDialog("Please enter product warranty",this);
            return;
        }*/

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
            JSONObject dataObject = new JSONObject();
            try {
                dataObject.put("prodId", ""+myProductItem.getProdId());
                dataObject.put("prodCatId", subCatId);
                dataObject.put("prodReorderLevel", reorderLevel);
                dataObject.put("prodQoh", qoh);
                dataObject.put("prodName", prodName);
                dataObject.put("prodCode", prodCode);
                dataObject.put("prodDesc", desc);
                dataObject.put("prodCgst", cgst);
                dataObject.put("prodIgst", igst);
                dataObject.put("prodSgst", sgst);
                dataObject.put("prodWarranty", warranty);
                dataObject.put("prodMrp", mrp);
                dataObject.put("prodSp", sp);
                dataObject.put("prodHsnCode", hsnCode);
                dataObject.put("prodMfgDate", mfgDate);
                dataObject.put("prodExpiryDate", expiryDate);
                dataObject.put("prodMfgBy", mfgBy);
                dataObject.put("action", "3");
                dataObject.put("prodImage1", imageList.get(0));
                dataObject.put("prodImage2", imageList.get(1));
                dataObject.put("prodImage3", imageList.get(2));
                dataObject.put("shopCode", sharedPreferences.getString(Constants.SHOP_CODE, ""));
                dataObject.put("createdBy", sharedPreferences.getString(Constants.FULL_NAME, ""));
                dataObject.put("updatedBy", sharedPreferences.getString(Constants.FULL_NAME, ""));
                dataObject.put("retRetailerId", sharedPreferences.getString(Constants.USER_ID, ""));
                dataObject.put("dbName", sharedPreferences.getString(Constants.DB_NAME, ""));
                dataObject.put("dbUserName", sharedPreferences.getString(Constants.DB_USER_NAME, ""));
                dataObject.put("dbPassword", sharedPreferences.getString(Constants.DB_PASSWORD, ""));


                if(unitList.size() > 0){
                    JSONArray unitArray = new JSONArray();
                    JSONObject unitObject = null;
                    ProductUnit productUnit = null;
                    for(Object ob : unitList){
                        unitObject = new JSONObject();
                        productUnit = (ProductUnit)ob;
                        unitObject.put("id",productUnit.getId());
                        unitObject.put("unitName",productUnit.getUnitName());
                        unitObject.put("unitValue",productUnit.getUnitValue());
                        unitObject.put("status",productUnit.getStatus());
                        unitArray.put(unitObject);
                    }
                    dataObject.put("productUnitList", unitArray);
                }

                if(sizeList != null && sizeList.size() > 0){
                    JSONArray sizeArray = new JSONArray();
                    JSONObject sizeObject = null;
                    ProductSize productSize = null;
                    for(Object ob : sizeList){
                        sizeObject = new JSONObject();
                        productSize = (ProductSize)ob;
                        sizeObject.put("id",productSize.getId());
                        sizeObject.put("size",productSize.getSize());
                        sizeObject.put("status",productSize.getStatus());

                        JSONArray colorArray = new JSONArray();
                        JSONObject colorObject = null;
                        //ProductColor productColor = null;
                        for(ProductColor productColor : productSize.getProductColorList()){
                            colorObject = new JSONObject();
                            colorObject.put("id",productColor.getId());
                            colorObject.put("sizeId",productColor.getSizeId());
                            colorObject.put("colorName",productColor.getColorName());
                            colorObject.put("colorValue",productColor.getColorValue());
                            colorObject.put("status",productColor.getStatus());
                            colorArray.put(colorObject);
                        }

                        if(colorArray.length() > 0)
                        sizeObject.put("productColorList", colorArray);

                        sizeArray.put(sizeObject);
                    }
                    dataObject.put("productSizeList", sizeArray);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            String url = getResources().getString(R.string.url) + "/api/products/updateProduct";
            showProgress(true);
            jsonObjectApiRequest(Request.Method.POST, url, dataObject, "updateProduct");
        }
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {
        if(apiName.equals("addProduct")){
            try {
                  if(response.getBoolean("status")){
                      JSONObject jsonObject = response.getJSONObject("result");
                      JSONArray barArray = null;
                      JSONArray tempArray = null;
                      JSONObject tempObject = null;
                      ProductUnit productUnit = null;
                      ProductSize productSize = null;
                      ProductColor productColor = null;
                      int barLen = 0;
                      MyProductItem productItem = new MyProductItem();
                      productItem.setProdId(jsonObject.getInt("prodId"));
                      productItem.setProdCatId(jsonObject.getInt("prodCatId"));
                      productItem.setProdSubCatId(jsonObject.getInt("prodSubCatId"));
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

                      if(!jsonObject.getString("productUnitList").equals("null")){
                          tempArray = jsonObject.getJSONArray("productUnitList");
                          int tempLen = tempArray.length();

                          for(int unitCounter = 0; unitCounter < tempLen ; unitCounter++){
                              tempObject = tempArray.getJSONObject(unitCounter);
                              productUnit = new ProductUnit();
                              productUnit.setId(tempObject.getInt("id"));
                              productUnit.setUnitName(tempObject.getString("unitName"));
                              productUnit.setUnitValue(tempObject.getString("unitValue"));
                              productUnit.setStatus(tempObject.getString("status"));
                              dbHelper.addProductUnit(productUnit,productItem.getProdId());
                          }
                      }

                      if(!jsonObject.getString("productSizeList").equals("null")){
                          tempArray = jsonObject.getJSONArray("productSizeList");
                          int tempLen = tempArray.length();

                          for(int unitCounter = 0; unitCounter < tempLen ; unitCounter++){
                              tempObject = tempArray.getJSONObject(unitCounter);
                              productSize = new ProductSize();
                              productSize.setId(tempObject.getInt("id"));
                              productSize.setSize(tempObject.getString("size"));
                              productSize.setStatus(tempObject.getString("status"));
                              dbHelper.addProductSize(productSize,productItem.getProdId());
                              if(!tempObject.getString("productSizeList").equals("null")){
                                  JSONArray colorArray = tempObject.getJSONArray("productColorList");
                                  for(int colorCounter = 0; colorCounter < colorArray.length() ; colorCounter++){
                                      tempObject = colorArray.getJSONObject(colorCounter);
                                      productColor = new ProductColor();
                                      productColor.setId(tempObject.getInt("id"));
                                      productColor.setSizeId(tempObject.getInt("sizeId"));
                                      productColor.setColorName(tempObject.getString("colorName"));
                                      productColor.setColorValue(tempObject.getString("colorValue"));
                                      productColor.setStatus(tempObject.getString("status"));
                                      dbHelper.addProductColor(productColor,productItem.getId());
                                  }
                              }

                          }
                      }


                      clearData();
                      DialogAndToast.showToast(response.getString("message"),AddProductActivity.this);
                  }else{
                      DialogAndToast.showDialog(response.getString("message"),AddProductActivity.this);
                  }
            }catch (JSONException e){
                e.printStackTrace();
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
                   // "product_signature"+myProductItem.getProdId()+"_3"
                    String timestamp = Utility.getTimeStamp();
                    if(!imageList.get(0).equals("no")){
                        timestamp = timestamp + "_1";
                        editor.putString("product_signature"+productItem.getProdId()+"_1",timestamp);
                    }

                    if(!imageList.get(1).equals("no")){
                        timestamp = Utility.getTimeStamp();
                        timestamp = timestamp + "_2";
                        editor.putString("product_signature"+productItem.getProdId()+"_2",timestamp);
                    }

                    if(!imageList.get(2).equals("no")){
                        timestamp = Utility.getTimeStamp();
                        timestamp = timestamp + "_3";
                        editor.putString("product_signature"+productItem.getProdId()+"_3",timestamp);
                    }

                    ProductUnit unit = null;
                    for(Object ob : unitList){
                        unit = (ProductUnit)ob;
                        if(unit.getStatus().equals("A")){
                            unit.setStatus("N");
                            Log.i(TAG,"Adding unit..."+unit.getUnitValue());
                            dbHelper.addProductUnit(unit,productItem.getProdId());
                        }else if(unit.getStatus().equals("D")){
                            Log.i(TAG,"Removing unit..."+unit.getUnitValue());
                            dbHelper.removeUnit(unit.getId());
                        }
                    }

                    ProductSize size = null;
                    for(Object ob : sizeList){
                        size = (ProductSize)ob;
                        if(size.getStatus().equals("A")){
                            dbHelper.addProductSize(size,productItem.getProdId());
                        }else if(size.getStatus().equals("D")){
                            dbHelper.removeSize(unit.getId());
                        }
                    }

                    ProductColor color = null;
                    for(Object ob : colorList){
                        color = (ProductColor)ob;
                        if(color.getStatus().equals("A")){
                            dbHelper.addProductColor(color,productItem.getProdId());
                        }else if(color.getStatus().equals("D")){
                            dbHelper.removeColor(unit.getId());
                        }
                    }

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
        editTextCGST.setText("0");
        editTextSGST.setText("0");
        editTextIGST.setText("0");
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

        if(subCatListObject.size() == 0){
            DialogAndToast.showDialog("Sync Sub category for the selected Category. Contact Shoppurs Support from Profile section.",this);
            disableViews(true);
        }else{
            disableViews(false);
            for(SpinnerItem item : subCatListObject){
                subCatList.add(item.getName());
            }

            //subCatAdapter.notifyDataSetChanged();

            if(scanSelection){
                int i = 0;
                for(String name : subCatList){
                    if(name.equals(dbHelper.getSubCategoryName(myProductItem.getProdSubCatId()))){
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

    private void initUnitColorSizeList(){
        unitList = new ArrayList<>();
        if(flag.equals("editProduct")){
            for(ProductUnit unit : myProductItem.getProductUnitList()){
                unitList.add(unit);
            }
        }
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        unitAdapter=new SpecificationAdapter(this,unitList,"unit");
        unitAdapter.setMyItemTypeClickListener(this);

        sizeList = new ArrayList<>();
        if(flag.equals("editProduct")){
            for(ProductSize size : myProductItem.getProductSizeList()){
                sizeList.add(size);
            }
        }
        sizeAdapter=new SpecificationAdapter(this,sizeList,"size");
        sizeAdapter.setMyItemTypeClickListener(this);

        sizeSpinnerList.clear();
        sizeSpinnerList.add("Select Size");
        ProductSize productSize = null;
        for(Object ob : sizeList){
            productSize = (ProductSize)ob;
            sizeSpinnerList.add(productSize.getSize());
        }

        sizeSpinnerAdapter.notifyDataSetChanged();
        colorList = new ArrayList<>();
        colorAdapter=new SpecificationAdapter(this,colorList,"color");
        colorAdapter.setMyItemTypeClickListener(this);

    }
    private void initUnitList(){
        Log.i(TAG,"unit size "+unitList.size());
        recyclerView.setAdapter(unitAdapter);
    }

    private void initSizeList(){
        recyclerView.setAdapter(sizeAdapter);
    }

    private void initColorList(){
        recyclerView.setAdapter(colorAdapter);
    }

    private void saveUnit(){
        String name = etUnitName.getText().toString();
        String value = etUnitValue.getText().toString();

        if(TextUtils.isEmpty(name)){
           DialogAndToast.showDialog("Please enter unit name",this);
           return;
        }

        if(TextUtils.isEmpty(value)){
            DialogAndToast.showDialog("Please enter unit value",this);
            return;
        }

        ProductUnit myUnitMeasure = new ProductUnit();
        myUnitMeasure.setUnitName(name);
        myUnitMeasure.setUnitValue(value);
        myUnitMeasure.setStatus("A");
        unitList.add(myUnitMeasure);
        unitAdapter.notifyDataSetChanged();

        etUnitName.setText("");
        etUnitValue.setText("");
    }

    private void saveSize(){
        String name = etSize.getText().toString();

        if(TextUtils.isEmpty(name)){
            DialogAndToast.showDialog("Please enter size",this);
            return;
        }

        ProductSize productSize = new ProductSize();
        productSize.setSize(name);
        productSize.setStatus("A");
        sizeList.add(productSize);
        sizeAdapter.notifyDataSetChanged();
        sizeSpinnerList.add(productSize.getSize());
        sizeSpinnerAdapter.notifyDataSetChanged();
        etSize.setText("");
    }

    private void saveColor(){

        int selectedPosition = spinnerSize.getSelectedItemPosition();

        if(selectedPosition == 0){
            DialogAndToast.showDialog("Please select size",this);
            return;
        }

        selectedPosition = selectedPosition - 1;

        ProductSize productSize = (ProductSize)sizeList.get(selectedPosition);


        if(!colorSelected){
            DialogAndToast.showDialog("Please select color",this);
            return;
        }

        ProductColor item = new ProductColor();
        item.setColorName(colorName);
        item.setColorValue(""+colorValue);
        item.setSize(productSize.getSize());
        item.setSizeId(productSize.getId());
        item.setStatus("A");
        colorList.add(item);

        List<ProductColor> productColorList = productSize.getProductColorList();
        if(productColorList == null){
            productColorList = new ArrayList<>();
        }
        productColorList.add(item);

        colorAdapter.notifyDataSetChanged();
        colorSelected = false;
        if(imageViewTemp != null){
            imageViewTemp.setImageResource(0);
        }
    }

    private void changeColor(Drawable drawable, int color){
        drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
    }

    @Override
    public void onClick(View view) {
        if(view == relativeLayoutRed){
            imageViewRed.setImageResource(R.drawable.ic_check_black_24dp);
            if(imageViewTemp != null){
                imageViewTemp.setImageResource(0);
            }
            imageViewTemp  = imageViewRed;
            colorName = "Red";
            colorValue = getResources().getColor(R.color.red_500);
        }else if(view == relativeLayoutBlue){
            colorName = "Blue";
            colorValue = getResources().getColor(R.color.blue500);
            imageViewBlue.setImageResource(R.drawable.ic_check_black_24dp);
            if(imageViewTemp != null){
                imageViewTemp.setImageResource(0);
            }
            imageViewTemp  = imageViewBlue;
        }else if(view == relativeLayoutGreen){
            imageViewGreen.setImageResource(R.drawable.ic_check_black_24dp);
            if(imageViewTemp != null){
                imageViewTemp.setImageResource(0);
            }
            imageViewTemp  = imageViewGreen;
            colorName = "Green";
            colorValue = getResources().getColor(R.color.green500);
        }else if(view == relativeLayoutPink){
            imageViewPink.setImageResource(R.drawable.ic_check_black_24dp);
            if(imageViewTemp != null){
                imageViewTemp.setImageResource(0);
            }
            imageViewTemp  = imageViewPink;
            colorName = "Pink";
            colorValue = getResources().getColor(R.color.pink500);
        }else if(view == relativeLayoutYellow){
            imageViewYellow.setImageResource(R.drawable.ic_check_black_24dp);
            if(imageViewTemp != null){
                imageViewTemp.setImageResource(0);
            }
            imageViewTemp  = imageViewYellow;
            colorName = "Yellow";
            colorValue = getResources().getColor(R.color.yellow500);
        }else if(view == relativeLayoutAmber){
            imageViewAmber.setImageResource(R.drawable.ic_check_black_24dp);
            if(imageViewTemp != null){
                imageViewTemp.setImageResource(0);
            }
            imageViewTemp  = imageViewAmber;
            colorName = "Amber";
            colorValue = getResources().getColor(R.color.amber600);
        }else if(view == relativeLayoutTeal){
            editor.putInt(Constants.COLOR_THEME,getResources().getColor(R.color.teal_500));
            imageViewTeal.setImageResource(R.drawable.ic_check_black_24dp);
            if(imageViewTemp != null){
                imageViewTemp.setImageResource(0);
            }
            imageViewTemp  = imageViewTeal;
        }else if(view == relativeLayoutIndigo){
            imageViewIndigo.setImageResource(R.drawable.ic_check_black_24dp);
            if(imageViewTemp != null){
                imageViewTemp.setImageResource(0);
            }
            imageViewTemp  = imageViewIndigo;
            colorName = "Indigo";
            colorValue = getResources().getColor(R.color.indigo_500);
        }else if(view == relativeLayoutPurple){
            imageViewPurple.setImageResource(R.drawable.ic_check_black_24dp);
            if(imageViewTemp != null){
                imageViewTemp.setImageResource(0);
            }
            imageViewTemp  = imageViewPurple;
            colorName = "Purple";
            colorValue = getResources().getColor(R.color.purple500);
        }
        colorSelected = true;
    }

    @Override
    public void onItemClicked(int position, int type) {
       if(type == 1){
           ProductUnit unit = (ProductUnit)unitList.get(position);
           if(unit.getStatus().equals("A")){
               unitList.remove(position);
               unitAdapter.notifyDataSetChanged();
           }else{
               unit.setStatus("D");
               unitAdapter.notifyDataSetChanged();
           }
       }else if(type == 2){
           ProductSize size = (ProductSize)sizeList.get(position);
           if(size.getStatus().equals("A")){
               sizeList.remove(position);
               sizeAdapter.notifyDataSetChanged();
           }else{
               size.setStatus("D");
               for(ProductColor color : size.getProductColorList()){
                   color.setStatus("D");
               }
               sizeAdapter.notifyDataSetChanged();
           }
       }else if(type == 3){
           ProductColor color = (ProductColor) colorList.get(position);
           if(color.getStatus().equals("A")){
               colorList.remove(position);
               colorAdapter.notifyDataSetChanged();
           }else{
               color.setStatus("D");
               colorAdapter.notifyDataSetChanged();
           }
           String selectedSize = (String) spinnerSize.getSelectedItem();
           ProductSize size = null;
           for(Object ob : sizeList){
               size = (ProductSize)ob;
               if(size.getSize().equals(selectedSize)){
                   break;
               }
           }

           for(ProductColor color1 : size.getProductColorList()){
              if(color.getId() == color1.getId()){
                  if(color.getStatus().equals("A")){
                      size.getProductColorList().remove(color1);
                  }else{
                      color1.setStatus("D");
                  }
                  break;
              }
           }
       }
    }

    private void disableViews(boolean isDisable){
        if(isDisable){
            editTextMfgDate.setEnabled(false);
            editTextExpiryDate.setEnabled(false);
            editTextName.setEnabled(false);
            editTextBarCode.setEnabled(false);
            editTextHSN.setEnabled(false);
            editTextDesc.setEnabled(false);
            editTextMRP.setEnabled(false);
            editTextSP.setEnabled(false);
            editTextRL.setEnabled(false);
            editTextQOH.setEnabled(false);
            editTextMfgBy.setEnabled(false);
            editTextWarranty.setEnabled(false);
            editTextCGST.setEnabled(false);
            editTextIGST.setEnabled(false);
            editTextSGST.setEnabled(false);
            checkBoxIsBarAvaialble.setEnabled(false);
            editTextCode.setEnabled(false);
            viewsDisabled = true;
        }else{
            if(viewsDisabled){
                editTextMfgDate.setEnabled(true);
                editTextExpiryDate.setEnabled(true);
                editTextName.setEnabled(true);
                editTextBarCode.setEnabled(true);
                editTextHSN.setEnabled(true);
                editTextDesc.setEnabled(true);
                editTextMRP.setEnabled(true);
                editTextSP.setEnabled(true);
                editTextRL.setEnabled(true);
                editTextQOH.setEnabled(true);
                editTextMfgBy.setEnabled(true);
                editTextWarranty.setEnabled(true);
                editTextCGST.setEnabled(true);
                editTextIGST.setEnabled(true);
                editTextSGST.setEnabled(true);
                checkBoxIsBarAvaialble.setEnabled(true);
                editTextCode.setEnabled(true);
                viewsDisabled = false;
            }
        }
    }
}
