package com.shoppursshop.activities.settings;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import com.shoppursshop.R;
import com.shoppursshop.activities.base.BaseImageActivity;
import com.shoppursshop.activities.InvoiceActivity;
import com.shoppursshop.activities.base.NetworkBaseActivity;
import com.shoppursshop.adapters.CustomerSaleReportAdapter;
import com.shoppursshop.adapters.MonthlyGraphAdapter;
import com.shoppursshop.interfaces.MyItemClickListener;
import com.shoppursshop.models.Bar;
import com.shoppursshop.models.CustomerSaleReport;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.DialogAndToast;
import com.shoppursshop.utilities.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MySalesActivity extends NetworkBaseActivity implements MyItemClickListener {

    private List<Bar> barList;
    private List<Bar> productSaleList;
    private List<CustomerSaleReport> customerSaleList;
    private RecyclerView recyclerViewMonthlyGraph,recyclerViewCustomerSale;
    private MonthlyGraphAdapter monthlyGraphAdapter;
    private CustomerSaleReportAdapter customerSaleReportAdapter;
    private Toolbar toolbar;
    private TextView tv_top_parent;

    private RelativeLayout relativeLayoutFromDate, relativeLayoutToDate;
    private TextView textViewFromDate, textViewToDate;
    private DatePickerDialog frpmDatePicker, toDatePicker;
    private String fromDate, toDate;
    private String monthTwoDigits = "";
    private String dayTwoDigits = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sales);
        init();
        setToolbarTheme();
        getSales();
    }

    private void init(){
        ImageView image_download = findViewById(R.id.image_download);
        textViewFromDate = findViewById(R.id.text_from_date);
        textViewToDate = findViewById(R.id.text_to_date);
        relativeLayoutFromDate = findViewById(R.id.relative_from_date);
        relativeLayoutToDate = findViewById(R.id.relative_to_date);
        recyclerViewMonthlyGraph= findViewById(R.id.recyclerView);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.grey900), PorterDuff.Mode.SRC_ATOP);

        Utility.setColorFilter(relativeLayoutFromDate.getBackground(),colorTheme);
        Utility.setColorFilter(relativeLayoutToDate.getBackground(),colorTheme);
        Utility.setColorFilter(image_download.getDrawable(),colorTheme);

        tv_top_parent = findViewById(R.id.text_left_label);
        tv_top_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MySalesActivity.this, SettingActivity.class));
                finish();
            }
        });

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        Calendar calendarPre = Calendar.getInstance();
       // calendarPre.add(Calendar.DAY_OF_MONTH, -(day - 1));
        int yearPre = calendarPre.get(Calendar.YEAR);
        int monthPre = calendarPre.get(Calendar.MONTH);
        int dayPre = calendarPre.get(Calendar.DAY_OF_MONTH);

        if (month < 10) {
            monthTwoDigits = "0" + (month + 1);
        } else {
            monthTwoDigits = "" + (month + 1);
        }

        if (day < 10) {
            dayTwoDigits = "0" + day;
        } else {
            dayTwoDigits = "" + day;
        }
        toDate = year + "-" + monthTwoDigits + "-" + dayTwoDigits;

        if (monthPre < 10) {
            monthTwoDigits = "0" + (monthPre + 1);
        } else {
            monthTwoDigits = "" + (monthPre + 1);
        }

        if (dayPre < 10) {
            dayTwoDigits = "0" + dayPre;
        } else {
            dayTwoDigits = "" + dayPre;
        }
        fromDate = yearPre + "-" + monthTwoDigits + "-" + dayTwoDigits;

        textViewFromDate.setText(dayPre + " " + Utility.getMonthName(monthPre) + " " + yearPre);
        textViewToDate.setText(day + " " + Utility.getMonthName(month) + " " + year);

        frpmDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int yr, int mon, int dy) {
                textViewFromDate.setText(dy + " " + Utility.getMonthName(mon) + " " + yr);
                if (mon < 10) {
                    monthTwoDigits = "0" + (mon + 1);
                } else {
                    monthTwoDigits = "" + (mon + 1);
                }

                if (dy < 10) {
                    dayTwoDigits = "0" + dy;
                } else {
                    dayTwoDigits = "" + dy;
                }

                fromDate = year + "-" + monthTwoDigits + "-" + dayTwoDigits;

                getSaleData();
            }
        }, yearPre, monthPre, dayPre);
        frpmDatePicker.setMessage("Select Start Date");


        toDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int yr, int mon, int dy) {
                textViewToDate.setText(dy + " " + Utility.getMonthName(mon) + " " + yr);
                if (mon < 10) {
                    monthTwoDigits = "0" + (mon + 1);
                } else {
                    monthTwoDigits = "" + (mon + 1);
                }

                if (dy < 10) {
                    dayTwoDigits = "0" + dy;
                } else {
                    dayTwoDigits = "" + dy;
                }

                toDate = year + "-" + monthTwoDigits + "-" + dayTwoDigits;

                getSaleData();
            }
        }, year, month, day);
        toDatePicker.setMessage("Select End Date");

        relativeLayoutFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frpmDatePicker.show();
            }
        });

        relativeLayoutToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDatePicker.show();
            }
        });

        customerSaleList = new ArrayList<>();
        recyclerViewCustomerSale= findViewById(R.id.recyclerViewCustomerSaleReport);
        recyclerViewCustomerSale.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCustomerSale.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManagerMonthlyGraph=new LinearLayoutManager(this);
        recyclerViewCustomerSale.setLayoutManager(layoutManagerMonthlyGraph);
        customerSaleReportAdapter=new CustomerSaleReportAdapter(this,customerSaleList);
        customerSaleReportAdapter.setMyItemClickListener(this);
        customerSaleReportAdapter.setColoTheme(colorTheme);
        recyclerViewCustomerSale.setAdapter(customerSaleReportAdapter);
        recyclerViewCustomerSale.setNestedScrollingEnabled(false);

        image_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(customerSaleList.size() == 0){
                   DialogAndToast.showDialog("There is no report to download",MySalesActivity.this);
                }else{
                    createPdf();
                }

            }
        });
    }

    private void getSales(){
        barList = new ArrayList<>();
        productSaleList = new ArrayList<>();
        initMonthlySaleList();
        recyclerViewMonthlyGraph.setItemAnimator(new DefaultItemAnimator());
        recyclerViewMonthlyGraph.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManagerMonthlyGraph=new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewMonthlyGraph.setLayoutManager(layoutManagerMonthlyGraph);
        monthlyGraphAdapter=new MonthlyGraphAdapter(this,barList,1);
        ((MonthlyGraphAdapter) monthlyGraphAdapter).setTotalTarget(25000);
        recyclerViewMonthlyGraph.setAdapter(monthlyGraphAdapter);
        getSaleData();
    }

    private void getSaleData(){
        Map<String,String> params=new HashMap<>();
        params.put("fromDate",fromDate);
        params.put("toDate",toDate);
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+Constants.SHOP_SALE_DATE;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"shopSaleData");
    }

    public int getSaleData(String monthName){
        int value = 0;
        for(Bar bar : productSaleList){
            if(bar.getName().equals(monthName)){
                value = bar.getSaleValue();
                break;
            }
        }

        return value;
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {

        try {
            if (apiName.equals("shopSaleData")) {
                if (response.getBoolean("status")) {
                    if(response.getString("result") == null || response.getString("result").equals("null")){
                        setMonthlyBar();
                    }else{
                        JSONObject dataObject = response.getJSONObject("result");
                        JSONArray dataArray = dataObject.getJSONArray("monthlyGraphData");
                        JSONArray custSaleReportArray = dataObject.getJSONArray("customerSaleData");
                        JSONObject jsonObject = null;
                        int len = dataArray.length();
                        double maxValue = 0;
                        for (int i = 0; i < len; i++) {
                            jsonObject = dataArray.getJSONObject(i);
                            if(maxValue <  jsonObject.getDouble("amount")){
                                maxValue =  jsonObject.getDouble("amount");
                            }
                            updateMonthlySaleList(Utility.parseMonth(jsonObject.getString("orderDate"),
                                    "yyyy-MM-dd HH:mm:ss"), jsonObject.getInt("amount"));
                        }

                        if(maxValue == 0f){
                            ((MonthlyGraphAdapter) monthlyGraphAdapter).setTotalTarget(50000);
                        }else{
                            ((MonthlyGraphAdapter) monthlyGraphAdapter).setTotalTarget((float)maxValue);
                        }

                        len = custSaleReportArray.length();
                        CustomerSaleReport item = null;
                        for (int i = 0; i < len; i++) {
                            jsonObject = custSaleReportArray.getJSONObject(i);
                            item = new CustomerSaleReport();
                            item.setName(jsonObject.getString("custName"));
                            item.setInvNum(jsonObject.getString("invNum"));
                            item.setOrderNum(jsonObject.getString("orderNumber"));
                            item.setMobile(jsonObject.getString("custMobile"));
                            item.setDate(jsonObject.getString("invDate"));
                            item.setAmount((float) jsonObject.getDouble("netPayable"));
                            item.setInvTotTax((float) jsonObject.getDouble("invTotTax"));
                            item.setInvId(jsonObject.getInt("invId"));
                            customerSaleList.add(item);
                        }

                        customerSaleReportAdapter.notifyDataSetChanged();

                        if(len == 0){
                            setNullMonthlyBar();
                        }else{
                            setMonthlyBar();
                        }
                    }
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void initMonthlySaleList(){
        Calendar calendarTemp =Calendar.getInstance();
        productSaleList.clear();
        for(int i=0;i<12;i++){
            if(i > 0)
                calendarTemp.add(Calendar.MONTH, -1);

            int month=calendarTemp.get(Calendar.MONTH);
            int year=calendarTemp.get(Calendar.YEAR);
            String monthName=getMonth(month);
            Bar bar=new Bar();
            bar.setName(monthName);
            bar.setSaleValue(0);
            bar.setBarColor(getBarColor(month));
            productSaleList.add(bar);
        }
    }

    public String getMonth(int position){
        String[] monthInText={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

        return monthInText[position];
    }

    public void setMonthlyBar(){
        Calendar calendarTemp =Calendar.getInstance();
        barList.clear();
        for(int i=0;i<12;i++){
            if(i > 0)
                calendarTemp.add(Calendar.MONTH, -1);

            int month=calendarTemp.get(Calendar.MONTH);
            int year=calendarTemp.get(Calendar.YEAR);
            String monthName=getMonth(month);
            Bar bar=new Bar();
            bar.setName(monthName);
            bar.setSaleValue(getSaleData(monthName));
            Log.i(TAG,"Sale value "+bar.getSaleValue());
            bar.setSaleAchievedValue(5000);
            bar.setSaleTargetValue(8000);
            bar.setBudget(10000);
            bar.setBarColor(getBarColor(month));
            barList.add(bar);
        }

        monthlyGraphAdapter.notifyDataSetChanged();
    }

    public void setNullMonthlyBar(){
        Calendar calendarTemp =Calendar.getInstance();
        barList.clear();
        for(int i=0;i<12;i++){
            if(i > 0)
                calendarTemp.add(Calendar.MONTH, -1);

            int month=calendarTemp.get(Calendar.MONTH);
            int year=calendarTemp.get(Calendar.YEAR);
            String monthName=getMonth(month);
            Bar bar=new Bar();
            bar.setName(monthName);
            bar.setSaleValue(0);
            Log.i(TAG,"Sale value "+bar.getSaleValue());
            bar.setSaleAchievedValue(5000);
            bar.setSaleTargetValue(8000);
            bar.setBudget(10000);
            bar.setBarColor(getBarColor(month));
            barList.add(bar);
        }

        monthlyGraphAdapter.notifyDataSetChanged();
    }

    public void updateMonthlySaleList(String monthName,int value){
        for(Bar bar : productSaleList){
            if(bar.getName().equals(monthName)){
                bar.setSaleValue(value);
                break;
            }
        }
    }

    private int getBarColor(int month){
        int[] barColor={getResources().getColor(R.color.light_blue500),
                getResources().getColor(R.color.yellow500),getResources().getColor(R.color.green500),
                getResources().getColor(R.color.orange500),getResources().getColor(R.color.red_500),
                getResources().getColor(R.color.teal_500),getResources().getColor(R.color.cyan500),
                getResources().getColor(R.color.deep_orange500),getResources().getColor(R.color.blue500),
                getResources().getColor(R.color.purple500),getResources().getColor(R.color.amber500),
                getResources().getColor(R.color.light_green500)};

        return barColor[month];
    }

    @Override
    public void onItemClicked(int position) {
        CustomerSaleReport item = customerSaleList.get(position);
        Intent intent = new Intent(MySalesActivity.this, InvoiceActivity.class);
        intent.putExtra("orderNumber",""+item.getOrderNum());
        startActivity(intent);
    }

    private void createPdf() {

        if (Utility.verifyStorageOnlyPermissions(this)) {
            String fileName = getFile();
            File file = new File(fileName);
            if(file.exists()) {
                Log.i(TAG, "pdf file is already exist.");
                return;
            }

            BaseFont baseFont = null;

            /**
             * How to USE FONT....
             */
            try {
                baseFont = BaseFont.createFont("assets/fonts/brandon_medium.otf", "UTF-8", BaseFont.EMBEDDED);
            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // LINE SEPARATOR
            LineSeparator lineSeparator = new LineSeparator();
            lineSeparator.setLineColor(new BaseColor(0, 0, 0, 68));

            // create a new document
            //PdfDocument document = new PdfDocument();
            Document document = new Document();
            // Location to save
            try {
                PdfWriter.getInstance(document, new FileOutputStream(fileName));
                // Open to write
                document.open();
                // Document Settings
                document.setPageSize(PageSize.A4);
                document.addCreationDate();
                document.addAuthor("Shoppurs");
                document.addCreator(sharedPreferences.getString(Constants.FULL_NAME, ""));
                // Title Order Details...
// Adding Title....
                Font headerFont = new Font(baseFont, 20.0f, Font.NORMAL, BaseColor.BLACK);
                Font descFont = new Font(baseFont, 15.0f, Font.NORMAL, BaseColor.BLACK);

                String shopName = sharedPreferences.getString(Constants.SHOP_NAME,"");
                shopName = shopName.toUpperCase();

// Creating Chunk
                Chunk shopNameChunk = new Chunk(shopName, headerFont);
// Creating Paragraph to add...
                Paragraph shopNameParagraph = new Paragraph(shopNameChunk);
// Setting Alignment for Heading
                shopNameParagraph.setAlignment(Element.ALIGN_CENTER);
// Finally Adding that Chunk
                document.add(shopNameParagraph);

                document.add(new Paragraph(""));
                document.add(new Paragraph(""));

                String shopAddress = sharedPreferences.getString(Constants.ADDRESS,"");
                Chunk shopAddressChunk = new Chunk(shopAddress, descFont);
                Paragraph shopAddressParagraph = new Paragraph(shopAddressChunk);
                shopAddressParagraph.setAlignment(Element.ALIGN_CENTER);
                document.add(shopAddressParagraph);

                String shopPhone = "Ph: "+sharedPreferences.getString(Constants.MOBILE_NO,"");
                Chunk shopPhoneChunk = new Chunk(shopPhone, descFont);
                Paragraph shopMobileParagraph = new Paragraph(shopPhoneChunk);
                shopMobileParagraph.setSpacingBefore(20);
                shopMobileParagraph.setAlignment(Element.ALIGN_CENTER);
                document.add(shopMobileParagraph);

                String shopEmail = "Email: "+sharedPreferences.getString(Constants.EMAIL,"");
                Chunk shopEmailChunk = new Chunk(shopEmail, descFont);
                Paragraph shopEmailParagraph = new Paragraph(shopEmailChunk);
                shopEmailParagraph.setAlignment(Element.ALIGN_CENTER);
                document.add(shopEmailParagraph);

                String shopGSTIN = "GSTIN: "+sharedPreferences.getString(Constants.GST_NO,"");
                Chunk shopGSTINChunk = new Chunk(shopGSTIN, descFont);
                Paragraph shopGSTINParagraph = new Paragraph(shopGSTINChunk);
                shopGSTINParagraph.setAlignment(Element.ALIGN_CENTER);
                document.add(shopGSTINParagraph);

                document.add(new Chunk(lineSeparator));

                double amt = 0f;
                for(CustomerSaleReport item : customerSaleList){
                    amt = amt + item.getAmount();
                }

                Chunk glue = new Chunk(new VerticalPositionMark());
                Chunk dateChunk = new Chunk(fromDate+" to "+toDate, descFont);
                Chunk nameChunk = new Chunk("Total Amount "+Utility.numberFormat(amt), descFont);
                Paragraph dateParagraph = new Paragraph(dateChunk);
                dateParagraph.add(new Chunk(glue));
                dateParagraph.add(nameChunk);
                document.add(dateParagraph);

                document.add(new Chunk(lineSeparator));

                PdfPTable pdfPTable = new PdfPTable(4);
                pdfPTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
                pdfPTable.setWidthPercentage(100);

                Chunk itemNameLabelChunk = new Chunk("Invoice No", descFont);
                PdfPCell itemNameLabelCell = new PdfPCell();
                itemNameLabelCell.setUseAscender(true);
                itemNameLabelCell.setVerticalAlignment(Element.ALIGN_TOP);
                itemNameLabelCell.setBorder(Rectangle.NO_BORDER);
                itemNameLabelCell.addElement(itemNameLabelChunk);
                // itemNameCell.setColspan(2);
                pdfPTable.addCell(itemNameLabelCell);

                Chunk qtyCodeLabelChunk = new Chunk("Date", descFont);
                PdfPCell qtyLabelCell = new PdfPCell();
                qtyLabelCell.setUseAscender(true);
                qtyLabelCell.setVerticalAlignment(Element.ALIGN_TOP);
                Paragraph qtyLabelParagraph = new Paragraph(qtyCodeLabelChunk);
                qtyLabelParagraph.setAlignment(Element.ALIGN_RIGHT);
                qtyLabelCell.setBorder(Rectangle.NO_BORDER);
                qtyLabelCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                qtyLabelCell.addElement(qtyLabelParagraph);
                pdfPTable.addCell(qtyLabelCell);

                // Paragraph itemMenuParagraph = new Paragraph(itemNameChunk);
                Chunk hsnGstLabelChunk = new Chunk("GST(INR)", descFont);
                PdfPCell hsnGstLabelCell = new PdfPCell();
                hsnGstLabelCell.setUseAscender(true);
                hsnGstLabelCell.setVerticalAlignment(Element.ALIGN_TOP);
                Paragraph gstParagraphLabel = new Paragraph(hsnGstLabelChunk);
                gstParagraphLabel.setAlignment(Element.ALIGN_RIGHT);
                hsnGstLabelCell.setBorder(Rectangle.NO_BORDER);
                hsnGstLabelCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                hsnGstLabelCell.addElement(gstParagraphLabel);
                pdfPTable.addCell(hsnGstLabelCell);

                // Paragraph itemMenuParagraph = new Paragraph(itemNameChunk);
                Chunk hsnCodeLabelChunk = new Chunk("Amount(INR)", descFont);
                PdfPCell hsnCodeLabelCell = new PdfPCell();
                hsnCodeLabelCell.setUseAscender(true);
                hsnCodeLabelCell.setVerticalAlignment(Element.ALIGN_TOP);
                Paragraph hsnParagraphLabel = new Paragraph(hsnCodeLabelChunk);
                hsnParagraphLabel.setAlignment(Element.ALIGN_RIGHT);
                hsnCodeLabelCell.setBorder(Rectangle.NO_BORDER);
                hsnCodeLabelCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                hsnCodeLabelCell.addElement(hsnParagraphLabel);
                pdfPTable.addCell(hsnCodeLabelCell);


                document.add(pdfPTable);

                document.add(new Chunk(lineSeparator));

                PdfPTable itemPdfPTable = new PdfPTable(4);
                itemPdfPTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
                itemPdfPTable.setWidthPercentage(100);

                for(CustomerSaleReport item : customerSaleList){

                    Chunk itemNameChunk = new Chunk(item.getInvNum(), descFont);
                    PdfPCell itemNameCell = new PdfPCell();
                    itemNameCell.setUseAscender(true);
                    itemNameCell.setVerticalAlignment(Element.ALIGN_TOP);
                    itemNameCell.setBorder(Rectangle.NO_BORDER);
                    itemNameCell.addElement(itemNameChunk);
                    itemPdfPTable.addCell(itemNameCell);

                    Log.i(TAG,"date "+item.getDate().split(" ")[0]);
                    Chunk qtyCodeChunk = new Chunk(Utility.parseDate(item.getDate().split(" ")[0],
                            "yyyy-MM-dd","dd MMM yyyy"), descFont);
                    PdfPCell qtyCell = new PdfPCell();
                    qtyCell.setUseAscender(true);
                    qtyCell.setVerticalAlignment(Element.ALIGN_TOP);
                    Paragraph qtyParagraph = new Paragraph(qtyCodeChunk);
                    qtyParagraph.setAlignment(Element.ALIGN_RIGHT);
                    qtyCell.setBorder(Rectangle.NO_BORDER);
                    qtyCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    qtyCell.addElement(qtyParagraph);
                    itemPdfPTable.addCell(qtyCell);

                    // Paragraph itemMenuParagraph = new Paragraph(itemNameChunk);
                    Chunk hsnGstChunk = new Chunk(String.format("%.02f",item.getInvTotTax()), descFont);
                    PdfPCell hsnGstCell = new PdfPCell();
                    hsnGstCell.setUseAscender(true);
                    hsnGstCell.setVerticalAlignment(Element.ALIGN_TOP);
                    Paragraph gstParagraph = new Paragraph(hsnGstChunk);
                    gstParagraph.setAlignment(Element.ALIGN_RIGHT);
                    hsnGstCell.setBorder(Rectangle.NO_BORDER);
                    hsnGstCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    hsnGstCell.addElement(gstParagraph);
                    itemPdfPTable.addCell(hsnGstCell);

                    Chunk hsnCodeChunk = new Chunk(Utility.numberFormat(item.getAmount()), descFont);
                    PdfPCell hsnCodeCell = new PdfPCell();
                    hsnCodeCell.setUseAscender(true);
                    hsnCodeCell.setVerticalAlignment(Element.ALIGN_TOP);
                    Paragraph hsnParagraph = new Paragraph(hsnCodeChunk);
                    hsnParagraph.setAlignment(Element.ALIGN_RIGHT);
                    hsnCodeCell.setBorder(Rectangle.NO_BORDER);
                    hsnCodeCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    hsnCodeCell.addElement(hsnParagraph);
                    itemPdfPTable.addCell(hsnCodeCell);
                }

                document.add(itemPdfPTable);
                document.add(new Chunk(lineSeparator));
                document.close();

                DialogAndToast.showToast("Pdf has been created successfully.The location is "+fileName,this);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                DialogAndToast.showToast("Error in creating pdf.",this);
            } catch (DocumentException e) {
                e.printStackTrace();
                DialogAndToast.showToast("Error in creating pdf.",this);
            }
        }
    }

    public String getFile() {
       //  String date=new SimpleDateFormat("yyyy_MM_dd").format(new Date());
       //  String time=new SimpleDateFormat("HH:mm:ss").format(new Date());

        String root="";
        if(BaseImageActivity.isExternalStorageAvailable()){
            root = Environment.getExternalStoragePublicDirectory("").toString();
        }
        File myDir = new File(root+"/Shoppurs/Shoppurs documents");
        myDir.mkdirs();
        String fname="SaleReport"+fromDate+toDate+".pdf";
        // fname = date+time+".pdf";

        String imagePath=root+"/Shoppurs/Shoppurs documents/"+fname;
        // File file = new File (myDir, fname);
        return imagePath;

    }
}
