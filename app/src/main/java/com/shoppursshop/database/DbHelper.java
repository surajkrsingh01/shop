package com.shoppursshop.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.shoppursshop.activities.CategoryListActivity;
import com.shoppursshop.models.CatListItem;
import com.shoppursshop.models.Category;
import com.shoppursshop.models.MyProduct;
import com.shoppursshop.models.MyProductItem;
import com.shoppursshop.models.MySimpleItem;
import com.shoppursshop.models.SpinnerItem;
import com.shoppursshop.models.SubCategory;
import com.shoppursshop.utilities.Constants;

import java.util.ArrayList;

/**
 * Created by Shweta on 6/9/2016.
 */
public class DbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = Constants.APP_NAME+".db";
    public static final String CAT_TABLE = "CATEGORY";
    public static final String SUB_CAT_TABLE = "SUB_CATEGORY";
    public static final String PRODUCT_TABLE = "PRODUCT_TABLE";
    public static final String ID = "id";
    public static final String CAT_ID = "cat_id";
    public static final String NAME = "name";
    public static final String IMAGE = "image";
    public static final String PROD_SUB_CAT_ID = "PROD_SUB_CAT_ID";
    public static final String PROD_NAME = "PROD_NAME";
    public static final String PROD_BARCODE = "PROD_BARCODE";
    public static final String PROD_DESC = "PROD_DESC";
    public static final String PROD_MRP = "PROD_MRP";
    public static final String PROD_SP = "PROD_SP";
    public static final String PROD_REORDER_LEVEL = "PROD_REORDER_LEVEL";
    public static final String PROD_QOH = "PROD_QOH";
    public static final String PROD_HSN_CODE = "PROD_HSN_CODE";
    public static final String PROD_CGST = "PROD_CGST";
    public static final String PROD_IGST = "PROD_IGST";
    public static final String PROD_SGST = "PROD_SGST";
    public static final String PROD_WARRANTY = "PROD_WARRANTY";
    public static final String PROD_MFG_DATE = "PROD_MFG_DATE";
    public static final String PROD_EXPIRY_DATE = "PROD_EXPIRY_DATE";
    public static final String PROD_MFG_BY = "PROD_MFG_BY";
    public static final String PROD_IMAGE_1 = "PROD_IMAGE_1";
    public static final String PROD_IMAGE_2 = "PROD_IMAGE_2";
    public static final String PROD_IMAGE_3 = "PROD_IMAGE_3";
    public static final String CREATED_BY = "CREATED_BY";
    public static final String UPDATED_BY = "UPDATED_BY";
    public static final String UPDATED_AT = "updatedAt";
    public static final String CREATED_AT = "createdAt";
    private Context context;

    public static final String CREATE_CAT_TABLE = "create table "+CAT_TABLE +
            "("+ID+" TEXT NOT NULL, " +
            " "+NAME+" TEXT NOT NULL, " +
            " "+IMAGE+" TEXT, " +
            " "+CREATED_AT+" TEXT, " +
            " "+UPDATED_AT+" TEXT)";

    public static final String CREATE_SUB_CAT_TABLE = "create table "+SUB_CAT_TABLE +
            "("+ID+" TEXT NOT NULL, " +
            " "+CAT_ID+" TEXT NOT NULL, " +
            " "+NAME+" TEXT NOT NULL, " +
            " "+IMAGE+" TEXT, " +
            " "+CREATED_AT+" TEXT, " +
            " "+UPDATED_AT+" TEXT)";

    public static final String CREATE_PRODUCT_TABLE = "create table "+PRODUCT_TABLE +
            "("+ID+" TEXT NOT NULL, " +
            " "+PROD_SUB_CAT_ID+" TEXT NOT NULL, " +
            " "+PROD_NAME+" TEXT NOT NULL, " +
            " "+PROD_BARCODE+" TEXT, " +
            " "+PROD_DESC+" TEXT, " +
            " "+PROD_MRP+" TEXT, " +
            " "+PROD_SP+" TEXT, " +
            " "+PROD_REORDER_LEVEL+" TEXT, " +
            " "+PROD_QOH+" TEXT, " +
            " "+PROD_HSN_CODE+" TEXT, " +
            " "+PROD_CGST+" TEXT, " +
            " "+PROD_IGST+" TEXT, " +
            " "+PROD_SGST+" TEXT, " +
            " "+PROD_WARRANTY+" TEXT, " +
            " "+PROD_MFG_DATE+" TEXT, " +
            " "+PROD_EXPIRY_DATE+" TEXT, " +
            " "+PROD_MFG_BY+" TEXT, " +
            " "+PROD_IMAGE_1+" TEXT, " +
            " "+PROD_IMAGE_2+" TEXT, " +
            " "+PROD_IMAGE_3+" TEXT, " +
            " "+CREATED_BY+" TEXT, " +
            " "+UPDATED_BY+" TEXT, " +
            " "+CREATED_AT+" TEXT, " +
            " "+UPDATED_AT+" TEXT)";

    public DbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 6);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CAT_TABLE);
        db.execSQL(CREATE_SUB_CAT_TABLE);
        db.execSQL(CREATE_PRODUCT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       // db.execSQL("DROP TABLE IF EXISTS "+CAT_TABLE);
       // db.execSQL("DROP TABLE IF EXISTS "+SUB_CAT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+PRODUCT_TABLE);
        db.execSQL(CREATE_PRODUCT_TABLE);
       // onCreate(db);
    }

    public boolean addCategory(MySimpleItem item, String createdAt, String updatedAt){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, item.getId());
        contentValues.put(NAME, item.getName());
        contentValues.put(IMAGE, item.getImage());
        contentValues.put(CREATED_AT, createdAt);
        contentValues.put(UPDATED_AT, updatedAt);
        db.insert(CAT_TABLE, null, contentValues);
        Log.i("DbHelper","Row is added");
        return true;
    }

    public boolean addSubCategory(MySimpleItem item, String catId,String createdAt, String updatedAt){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, item.getId());
        contentValues.put(CAT_ID, catId);
        contentValues.put(NAME, item.getName());
        contentValues.put(IMAGE, item.getImage());
        contentValues.put(CREATED_AT, createdAt);
        contentValues.put(UPDATED_AT, updatedAt);
        db.insert(SUB_CAT_TABLE, null, contentValues);
        Log.i("DbHelper","Sub Cat Row is added "+item.getName());
        return true;
    }

    public boolean addProduct(MyProductItem item, String createdAt, String updatedAt){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, item.getProdId());
        contentValues.put(PROD_SUB_CAT_ID, item.getProdCatId());
        contentValues.put(PROD_BARCODE, item.getProdBarCode());
        contentValues.put(PROD_NAME, item.getProdName());
        contentValues.put(PROD_DESC, item.getProdDesc());
        contentValues.put(PROD_MRP, item.getProdMrp());
        contentValues.put(PROD_SP, item.getProdSp());
        contentValues.put(PROD_REORDER_LEVEL, item.getProdReorderLevel());
        contentValues.put(PROD_QOH, item.getProdQoh());
        contentValues.put(PROD_HSN_CODE, item.getProdHsnCode());
        contentValues.put(PROD_CGST, item.getProdCgst());
        contentValues.put(PROD_IGST, item.getProdIgst());
        contentValues.put(PROD_SGST, item.getProdSgst());
        contentValues.put(PROD_WARRANTY, item.getProdWarranty());
        contentValues.put(PROD_MFG_DATE, item.getProdMfgDate());
        contentValues.put(PROD_EXPIRY_DATE, item.getProdExpiryDate());
        contentValues.put(PROD_MFG_BY, item.getProdMfgBy());
        contentValues.put(PROD_IMAGE_1, item.getProdImage1());
        contentValues.put(PROD_IMAGE_2, item.getProdImage2());
        contentValues.put(PROD_IMAGE_3, item.getProdImage3());
        contentValues.put(CREATED_BY, item.getCreatedBy());
        contentValues.put(UPDATED_BY, item.getUpdatedBy());
        contentValues.put(CREATED_AT, createdAt);
        contentValues.put(UPDATED_AT, updatedAt);
        db.insert(PRODUCT_TABLE, null, contentValues);
        Log.i("DbHelper","Sub Cat Row is added "+item.getProdName());
        return true;
    }

    public String getCategoryName(String catId){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select "+NAME+" from "+CAT_TABLE+" where "+ID+" =?";
        Cursor res =  db.rawQuery(query, new String[]{catId});
        String name = "";
        if(res.moveToFirst()){
            name = res.getString(res.getColumnIndex(NAME));
        }
        return name;
    }

    public ArrayList<Object> getCategories(){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select * from "+CAT_TABLE;
        Cursor res =  db.rawQuery(query, null);
        // res.moveToFirst();
        ArrayList<Object> itemList=new ArrayList<>();
        MySimpleItem item = null;
        if(res.moveToFirst()){
            do{
                item=new MySimpleItem();
                item.setId(res.getInt(res.getColumnIndex(ID)));
                item.setName(res.getString(res.getColumnIndex(NAME)));
                item.setImage(res.getString(res.getColumnIndex(IMAGE)));
                itemList.add(item);
            }while (res.moveToNext());
        }


        return itemList;
    }

    public ArrayList<SpinnerItem> getCategoriesAddProduct(){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select * from "+CAT_TABLE;
        Cursor res =  db.rawQuery(query, null);
        // res.moveToFirst();
        ArrayList<SpinnerItem> itemList=new ArrayList<>();
        SpinnerItem item = null;
        if(res.moveToFirst()){
            do{
                item=new SpinnerItem();
                item.setId(res.getString(res.getColumnIndex(ID)));
                item.setName(res.getString(res.getColumnIndex(NAME)));
                itemList.add(item);
            }while (res.moveToNext());
        }


        return itemList;
    }


    public ArrayList<Object> getCategoriesForActivity(int limit,int offset){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select * from "+CAT_TABLE+" LIMIT ? OFFSET ?";
        Cursor res =  db.rawQuery(query, new String[]{String.valueOf(limit),String.valueOf(offset)});
        // res.moveToFirst();
        ArrayList<Object> itemList=new ArrayList<>();
        Category item = null;
        if(res.moveToFirst()){
            do{
                item=new Category();
                item.setId(""+res.getInt(res.getColumnIndex(ID)));
                item.setName(res.getString(res.getColumnIndex(NAME)));
                item.setImage(res.getString(res.getColumnIndex(IMAGE)));
                itemList.add(item);
            }while (res.moveToNext());
        }


        return itemList;
    }

    public ArrayList<Object> getCategoriesForProduct(){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select * from "+CAT_TABLE;
        Cursor res =  db.rawQuery(query, null);
        // res.moveToFirst();
        ArrayList<Object> itemList=new ArrayList<>();
        CatListItem item = null;
        if(res.moveToFirst()){
            do{
                item=new CatListItem();
                item.setId(res.getInt(res.getColumnIndex(ID)));
                item.setTitle(res.getString(res.getColumnIndex(NAME)));
                itemList.add(item);
            }while (res.moveToNext());
        }
        return itemList;
    }

    public ArrayList<Object> getSubCategories(){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select * from "+SUB_CAT_TABLE;
        Cursor res =  db.rawQuery(query, null);
        // res.moveToFirst();
        ArrayList<Object> itemList=new ArrayList<>();
        MySimpleItem item = null;
        if(res.moveToFirst()){
            do{
                item=new MySimpleItem();
                item.setId(res.getInt(res.getColumnIndex(ID)));
                item.setName(res.getString(res.getColumnIndex(NAME)));
                itemList.add(item);
            }while (res.moveToNext());
        }

        return itemList;
    }

    public ArrayList<Object> getCatSubCategories(String caId){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select * from "+SUB_CAT_TABLE+" where "+CAT_ID+" = ?";
        Cursor res =  db.rawQuery(query, new String[]{caId});
        ArrayList<Object> itemList=new ArrayList<>();
        MySimpleItem item = null;
        if(res.moveToFirst()){
            do{
                item=new MySimpleItem();
                item.setId(res.getInt(res.getColumnIndex(ID)));
                item.setName(res.getString(res.getColumnIndex(NAME)));
                itemList.add(item);
            }while (res.moveToNext());
        }

        return itemList;
    }

    public ArrayList<SpinnerItem> getCatSubCategoriesAddProduct(String caId){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select * from "+SUB_CAT_TABLE+" where "+CAT_ID+" = ?";
        Cursor res =  db.rawQuery(query, new String[]{caId});
        ArrayList<SpinnerItem> itemList=new ArrayList<>();
        SpinnerItem item = null;
        if(res.moveToFirst()){
            do{
                item=new SpinnerItem();
                item.setId(res.getString(res.getColumnIndex(ID)));
                item.setName(res.getString(res.getColumnIndex(NAME)));
                itemList.add(item);
            }while (res.moveToNext());
        }

        return itemList;
    }

    public ArrayList<Object> getCatSubCategoriesForActivity(String caId,int limit,int offset){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select * from "+SUB_CAT_TABLE+" where "+CAT_ID+" = ? LIMIT ? OFFSET ?";
        Cursor res =  db.rawQuery(query, new String[]{caId,String.valueOf(limit),String.valueOf(offset)});
        ArrayList<Object> itemList=new ArrayList<>();
        SubCategory item = null;
        if(res.moveToFirst()){
            do{
                item=new SubCategory();
                item.setId(""+res.getInt(res.getColumnIndex(ID)));
                item.setName(res.getString(res.getColumnIndex(NAME)));
                item.setImage(res.getString(res.getColumnIndex(IMAGE)));
                item.setWidth(CategoryListActivity.MAX_WIDTH);
                item.setHeight(CategoryListActivity.MAX_HEIGHT);
                itemList.add(item);
            }while (res.moveToNext());
        }

        return itemList;
    }

    public ArrayList<Object> getProducts(String subCatId,int limit,int offset){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select * from "+PRODUCT_TABLE+" where "+PROD_SUB_CAT_ID+" = ? LIMIT ? OFFSET ?";
        Cursor res =  db.rawQuery(query, new String[]{subCatId,String.valueOf(limit),String.valueOf(offset)});
        ArrayList<Object> itemList=new ArrayList<>();
        MyProductItem productItem = null;
        if(res.moveToFirst()){
            do{
                productItem=new MyProductItem();
                productItem.setProdId(res.getInt(res.getColumnIndex(ID)));
                productItem.setProdCatId(res.getInt(res.getColumnIndex(PROD_SUB_CAT_ID)));
                productItem.setProdName(res.getString(res.getColumnIndex(PROD_NAME)));
                productItem.setProdBarCode(res.getString(res.getColumnIndex(PROD_BARCODE)));
                productItem.setProdDesc(res.getString(res.getColumnIndex(PROD_DESC)));
                productItem.setProdReorderLevel(res.getInt(res.getColumnIndex(PROD_REORDER_LEVEL)));
                productItem.setProdQoh(res.getInt(res.getColumnIndex(PROD_QOH)));
                productItem.setProdHsnCode(res.getString(res.getColumnIndex(PROD_HSN_CODE)));
                productItem.setProdCgst(res.getFloat(res.getColumnIndex(PROD_CGST)));
                productItem.setProdIgst(res.getFloat(res.getColumnIndex(PROD_IGST)));
                productItem.setProdSgst(res.getFloat(res.getColumnIndex(PROD_SGST)));
                productItem.setProdWarranty(res.getFloat(res.getColumnIndex(PROD_WARRANTY)));
                productItem.setProdMfgDate(res.getString(res.getColumnIndex(PROD_MFG_DATE)));
                productItem.setProdExpiryDate(res.getString(res.getColumnIndex(PROD_EXPIRY_DATE)));
                productItem.setProdMfgBy(res.getString(res.getColumnIndex(PROD_MFG_BY)));
                productItem.setProdImage1(res.getString(res.getColumnIndex(PROD_IMAGE_1)));
                productItem.setProdImage2(res.getString(res.getColumnIndex(PROD_IMAGE_2)));
                productItem.setProdImage3(res.getString(res.getColumnIndex(PROD_IMAGE_3)));
                productItem.setProdMrp(res.getFloat(res.getColumnIndex(PROD_MRP)));
                productItem.setProdSp(res.getFloat(res.getColumnIndex(PROD_SP)));
                itemList.add(productItem);
            }while (res.moveToNext());
        }

        return itemList;
    }

    public MyProductItem getProductDetails(int prodId){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select * from "+PRODUCT_TABLE+" where "+ID+" = ?";
        Cursor res =  db.rawQuery(query, new String[]{String.valueOf(prodId)});
        MyProductItem productItem = null;
        if(res.moveToFirst()){
            productItem=new MyProductItem();
            productItem.setProdId(res.getInt(res.getColumnIndex(ID)));
            productItem.setProdCatId(res.getInt(res.getColumnIndex(PROD_SUB_CAT_ID)));
            productItem.setProdName(res.getString(res.getColumnIndex(PROD_NAME)));
            productItem.setProdBarCode(res.getString(res.getColumnIndex(PROD_BARCODE)));
            productItem.setProdDesc(res.getString(res.getColumnIndex(PROD_DESC)));
            productItem.setProdReorderLevel(res.getInt(res.getColumnIndex(PROD_REORDER_LEVEL)));
            productItem.setProdQoh(res.getInt(res.getColumnIndex(PROD_QOH)));
            productItem.setProdHsnCode(res.getString(res.getColumnIndex(PROD_HSN_CODE)));
            productItem.setProdCgst(res.getFloat(res.getColumnIndex(PROD_CGST)));
            productItem.setProdIgst(res.getFloat(res.getColumnIndex(PROD_IGST)));
            productItem.setProdSgst(res.getFloat(res.getColumnIndex(PROD_SGST)));
            productItem.setProdWarranty(res.getFloat(res.getColumnIndex(PROD_WARRANTY)));
            productItem.setProdMfgDate(res.getString(res.getColumnIndex(PROD_MFG_DATE)));
            productItem.setProdExpiryDate(res.getString(res.getColumnIndex(PROD_EXPIRY_DATE)));
            productItem.setProdMfgBy(res.getString(res.getColumnIndex(PROD_MFG_BY)));
            productItem.setProdImage1(res.getString(res.getColumnIndex(PROD_IMAGE_1)));
            productItem.setProdImage2(res.getString(res.getColumnIndex(PROD_IMAGE_2)));
            productItem.setProdImage3(res.getString(res.getColumnIndex(PROD_IMAGE_3)));
            productItem.setProdMrp(res.getFloat(res.getColumnIndex(PROD_MRP)));
            productItem.setProdSp(res.getFloat(res.getColumnIndex(PROD_SP)));
        }

        return productItem;
    }

  /*  public boolean insertBookingData(String level0Category,String level1Category,String level2Category,String receiptNumber,
                                  String date,String comment,String photo,int taxRate,String creditCardFee,double amount,
                                     double bookToAccountDebit,double bookToAccountCredit,String version)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LEVEL_0_CATEGORY, level0Category);
        contentValues.put(LEVEL_1_CATEGORY, level1Category);
        contentValues.put(LEVEL_2_CATEGORY, level2Category);
        contentValues.put(RECEIPT_NUMBER, receiptNumber);
        contentValues.put(DATE, date);
        contentValues.put(COMMENT, comment);
        contentValues.put(PHOTO, photo);
        contentValues.put(TAX_RATE, taxRate);
        contentValues.put(CREDIT_CARD_FEE, creditCardFee);
        contentValues.put(AMOUNT, amount);
        contentValues.put(BOOK_TO_ACCOUNT_DEBIT, bookToAccountDebit);
        contentValues.put(BOOK_TO_ACCOUNT_CREDIT, bookToAccountCredit);
        contentValues.put(VERSION, version);
     //   contentValues.put(AMOUNT_CREDIT, amountCredit);

     //   contentValues.put(AMOUNT_DEBIT, amountDebit);

        db.insert("Booking", null, contentValues);
        return true;
    }

    public ArrayList<Booking> getAllBookings(String month){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select * from Booking where strftime('%m',_date)=?";
        Cursor res =  db.rawQuery(query, new String[]{month});
       // res.moveToFirst();
        ArrayList<Booking> bookingList=new ArrayList<>();
        if(res.moveToFirst()){
            do{
                Booking booking=new Booking(res.getString(res.getColumnIndex(LEVEL_0_CATEGORY)),
                        res.getString(res.getColumnIndex(LEVEL_1_CATEGORY)),res.getString(res.getColumnIndex(LEVEL_2_CATEGORY)),
                        res.getString(res.getColumnIndex(RECEIPT_NUMBER)), res.getString(res.getColumnIndex(DATE)),
                        res.getString(res.getColumnIndex(COMMENT)),res.getString(res.getColumnIndex(PHOTO)),
                        res.getInt(res.getColumnIndex(TAX_RATE)),res.getString(res.getColumnIndex(CREDIT_CARD_FEE)),
                        res.getDouble(res.getColumnIndex(AMOUNT)),res.getInt(res.getColumnIndex(BOOK_TO_ACCOUNT_DEBIT)),
                        res.getInt(res.getColumnIndex(BOOK_TO_ACCOUNT_CREDIT)),res.getString(res.getColumnIndex(VERSION)));
                bookingList.add(booking);
            }while (res.moveToNext());
        }


        return bookingList;
    }

     public boolean updateSessionGuestCheckedOutServerStatus(String id,String eventId,String eventCatId,String status)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CHECKED_OUT_SERVER_SYNC_STATUS,status);
        contentValues.put(UPDATED_AT, Utility.getTimeStamp());
        db.update(SESSION_GUEST_TABLE, contentValues,ID+" =? and "+EVENT_ID+" =? and "+EVENT_CATEGORY_ID+" =?",
                new String[]{id,eventId,eventCatId});
        db.close();
        return true;
    }

    public boolean deleteSessionGuestTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(SESSION_GUEST_TABLE, null, null);
        return true;
    }*/

    public boolean deleteTable(String tableName){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tableName, null, null);
        return true;
    }

    public boolean dropAndCreateBookingTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+CAT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+SUB_CAT_TABLE);
        onCreate(db);
        return true;
    }


}
