package com.shoppursshop.activities;

import android.graphics.Paint;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.os.Environment;
import android.view.View;
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
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.shoppursshop.R;
import com.shoppursshop.adapters.InvoiceItemAdapter;
import com.shoppursshop.models.Coupon;
import com.shoppursshop.models.Invoice;
import com.shoppursshop.models.InvoiceDetail;
import com.shoppursshop.models.InvoiceItem;
import com.shoppursshop.utilities.ConnectionDetector;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.DialogAndToast;
import com.shoppursshop.utilities.EnglishNumberToWords;
import com.shoppursshop.utilities.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvoiceActivity extends NetworkBaseActivity {

    private RecyclerView recyclerView;
    private List<InvoiceItem> itemList;
    private InvoiceItemAdapter itemAdapter;

    private TextView tvShopName,tvShopAddress,tvShopEmail,tvShopPhone,tvShopGSTIN,tvInvoiceNo,tvDate,tvCustomerName,
                      tvSubTotAmt,tvGrossTotAmt,tvTotSgst,tvTotCgst,tvTotIgst,tvShortExcess,tvNetPayableAmt,tvNetPayableWords,
                      tvPaidAmt,tvBalAmt,tvTotQty,tvDiscount,tvPaymentMethod,tvPaymentBrand,tvTransId,tvPaymentAmount,tvTotSavings;

    private RelativeLayout rlCouponLayout;
    private TextView tvCouponOfferName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();

        itemList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        itemAdapter=new InvoiceItemAdapter(this,itemList);
        recyclerView.setAdapter(itemAdapter);
        recyclerView.setNestedScrollingEnabled(false);

      //  getItemList();

        if(ConnectionDetector.isNetworkAvailable(this))
        getInvoice();

    }

    private void init(){
        tvShopName = findViewById(R.id.text_shop_name);
        tvShopAddress = findViewById(R.id.text_shop_address);
        tvShopEmail = findViewById(R.id.text_shop_email);
        tvShopPhone = findViewById(R.id.text_shop_phone);
        tvShopGSTIN = findViewById(R.id.text_shop_gst);
        tvInvoiceNo = findViewById(R.id.text_invoice_no);
        tvDate = findViewById(R.id.text_date);
        tvCustomerName = findViewById(R.id.text_customer_name);
        tvSubTotAmt = findViewById(R.id.text_sub_total_amount);
        tvGrossTotAmt = findViewById(R.id.text_gross_total_amount);
        tvTotSgst = findViewById(R.id.text_sgst);
        tvTotCgst = findViewById(R.id.text_cgst);
        tvTotIgst = findViewById(R.id.text_igst);
        tvShortExcess = findViewById(R.id.text_short_excess);
        tvNetPayableAmt = findViewById(R.id.text_net_payable_amount);
        tvNetPayableWords = findViewById(R.id.text_net_payable_amount_words);
        tvPaidAmt = findViewById(R.id.text_paid_amount);
        tvBalAmt = findViewById(R.id.text_balance);
        tvDiscount = findViewById(R.id.text_discount);
        tvPaymentMethod = findViewById(R.id.text_payment_method);
        tvPaymentBrand = findViewById(R.id.text_payment_brand);
        tvTransId = findViewById(R.id.text_payment_transaction_id);
        tvPaymentAmount = findViewById(R.id.text_paid_amount);
        tvTotSavings = findViewById(R.id.text_total_savings);
        tvTotQty = findViewById(R.id.text_total_qty);

        rlCouponLayout = findViewById(R.id.rl_coupon_layout);
        tvCouponOfferName = findViewById(R.id.tv_offer_name);

        ImageView ivClose = findViewById(R.id.image_close);
        ImageView ivShare = findViewById(R.id.image_share);
        ImageView ivPrint = findViewById(R.id.image_print);
        ImageView ivDownload = findViewById(R.id.image_download);

        ivPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPdf();
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void getInvoice(){
        Map<String,String> params=new HashMap<>();
        params.put("number",getIntent().getStringExtra("orderNumber"));
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+Constants.GET_INVOICE;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"orders");
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {

        try {
            if (apiName.equals("orders")) {
                if (response.getBoolean("status")) {
                   // JSONArray dataArray = response.getJSONArray("result");
                    JSONObject jsonObject = response.getJSONObject("result");
                    JSONArray invoiceDetailsArray = jsonObject.getJSONArray("invoiceDetailList");
                    tvShopName.setText(jsonObject.getString("invShopName"));
                    tvShopAddress.setText(jsonObject.getString("invShopAddress"));
                    tvShopPhone.setText("Ph: "+jsonObject.getString("invShopMobile"));
                    tvShopEmail.setText("Email: "+jsonObject.getString("invShopEmail"));
                    tvShopGSTIN.setText("GSTIN: "+jsonObject.getString("invShopGSTIn"));
                    tvDate.setText(jsonObject.getString("invDate"));
                    tvCustomerName.setText(jsonObject.getString("invCustName"));
                    tvInvoiceNo.setText("Invoice No: "+jsonObject.getString("invNo"));
                    float subTotal = Float.parseFloat(""+(jsonObject.getDouble("invTotAmount") - jsonObject.getDouble("invTotTaxAmount")));
                    tvSubTotAmt.setText(Utility.numberFormat(subTotal));
                    tvGrossTotAmt.setText(Utility.numberFormat(subTotal));
                    tvTotIgst.setText(Utility.numberFormat(jsonObject.getDouble("invTotIGST")));
                    tvTotSgst.setText(Utility.numberFormat(jsonObject.getDouble("invTotSGST")));
                    tvTotCgst.setText(Utility.numberFormat(jsonObject.getDouble("invTotCGST")));
                   // tvShortExcess.setText(Utility.numberFormat(jsonObject.getDouble("invTotDisAmount")));
                    float netPayable = (float) Math.round(jsonObject.getDouble("invTotNetPayable"));
                    tvNetPayableAmt.setText(Utility.numberFormat(netPayable));
                    tvShortExcess.setText(Utility.numberFormat(netPayable - (float)jsonObject.getDouble("invTotNetPayable")));
                    tvPaidAmt.setText(Utility.numberFormat(netPayable));
                    tvNetPayableWords.setText(EnglishNumberToWords.convert((int)netPayable)+" rupees");
                    tvPaymentMethod.setText(jsonObject.getString("paymentMethod"));
                    tvPaymentBrand.setText(jsonObject.getString("paymentBrand"));
                    tvTransId.setText(jsonObject.getString("invTransId"));
                    tvPaymentAmount.setText(Utility.numberFormat(netPayable));
                    tvTotSavings.setText("Total Savings(Rupees) "+Utility.numberFormat(jsonObject.getDouble("invTotDisAmount")));
                    tvDiscount.setText(Utility.numberFormat(jsonObject.getDouble("invTotDisAmount")));

                    int couponId = jsonObject.getInt("invCoupenId");
                    if(couponId == 0){
                        rlCouponLayout.setVisibility(View.GONE);
                    }else{
                        rlCouponLayout.setVisibility(View.VISIBLE);
                        Coupon coupon = dbHelper.getCouponOffer(String.valueOf(couponId));
                        tvCouponOfferName.setText(coupon.getName());
                    }

                    int len = invoiceDetailsArray.length();
                  //  InvoiceDetail invoiceDetail= null;
                    int totQty = 0;
                    for (int i = 0; i < len; i++) {
                        jsonObject = invoiceDetailsArray.getJSONObject(i);
                        //invoiceDetail = new InvoiceDetail();

                        InvoiceItem item = new InvoiceItem();
                        item.setItemName(jsonObject.getString("invDProdName"));
                        item.setHsn(jsonObject.getString("invDHsnCode"));
                        item.setQty(jsonObject.getInt("invDQty"));
                        item.setGst(jsonObject.getInt("invDIGST"));
                        item.setMrp((float) jsonObject.getDouble("invDMrp"));
                        item.setRate(Float.parseFloat(jsonObject.getString("invDSp")));
                        item.setFreeItems(jsonObject.getInt("invDFreeItems"));
                        item.setOfferId(jsonObject.getString("invdOfferId"));
                        item.setOfferType(jsonObject.getString("invdOfferType"));
                        item.setUnit(jsonObject.getString("invdProdUnit"));
                        item.setColor(jsonObject.getString("invdProdColor"));
                        item.setSize(jsonObject.getString("invdProdSize"));
                        itemList.add(item);
                        totQty = totQty + item.getQty();
                    }

                    tvTotQty.setText(""+totQty);
                    itemAdapter.notifyDataSetChanged();

                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getItemList(){
        InvoiceItem item = new InvoiceItem();
        item.setItemName("Ghee Pongal");
        item.setHsn("9963");
        item.setQty(1);
        item.setGst(5);
        item.setRate(50);
        itemList.add(item);

        item = new InvoiceItem();
        item.setItemName("Coffee");
        item.setHsn("9963");
        item.setQty(1);
        item.setGst(5);
        item.setRate(25);
        itemList.add(item);

        itemAdapter.notifyDataSetChanged();
    }

    public String getFile() {
        String date=new SimpleDateFormat("yyyy_MM_dd").format(new Date());
        String time=new SimpleDateFormat("HH:mm:ss").format(new Date());

        String root="";
        if(BaseImageActivity.isExternalStorageAvailable()){
            root = Environment.getExternalStoragePublicDirectory("").toString();
        }
        File myDir = new File(root+"/Shoppurs/Shoppurs documents");
        myDir.mkdirs();
        String fname="";
        fname = date+time+".pdf";

        String imagePath=root+"/Shoppurs/Shoppurs documents/"+fname;
       // File file = new File (myDir, fname);
        return imagePath;

    }


    private void createPdf(){

        String fileName = getFile();

        /***
         * Variables for further use....
         */
        BaseColor mColorAccent = new BaseColor(0, 153, 204, 255);
        float mHeadingFontSize = 20.0f;
        float mValueFontSize = 26.0f;

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
            document.addCreator(sharedPreferences.getString(Constants.FULL_NAME,""));


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

            Chunk taxInvoiceChunk = new Chunk("Tax Invoice", descFont);
            Paragraph taxInvoiceParagraph = new Paragraph(taxInvoiceChunk);
            shopMobileParagraph.setSpacingBefore(10);
            shopMobileParagraph.setSpacingAfter(10);
            taxInvoiceParagraph.setAlignment(Element.ALIGN_CENTER);
            document.add(taxInvoiceParagraph);

            // Fields of Order Details...
// Adding Chunks for Title and value
          /*  Font mOrderIdFont = new Font(baseFont, mHeadingFontSize, Font.NORMAL, mColorAccent);
            Chunk mOrderIdChunk = new Chunk("Order No:", mOrderIdFont);
            Paragraph mOrderIdParagraph = new Paragraph(mOrderIdChunk);
            document.add(mOrderIdParagraph);*/

            document.add(new Paragraph(""));
            document.add(new Chunk(lineSeparator));
            document.add(new Paragraph(""));

            document.close();

            DialogAndToast.showToast("Pdf created successfully.",this);

        } catch (DocumentException e) {
            e.printStackTrace();
            DialogAndToast.showToast("Error in creating pdf.",this);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            DialogAndToast.showToast("Error in creating pdf.",this);
        }

    }

}
