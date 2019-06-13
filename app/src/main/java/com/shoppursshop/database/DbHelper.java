package com.shoppursshop.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.shoppursshop.activities.CategoryListActivity;
import com.shoppursshop.models.Barcode;
import com.shoppursshop.models.CatListItem;
import com.shoppursshop.models.Category;
import com.shoppursshop.models.MyCustomer;
import com.shoppursshop.models.MyProduct;
import com.shoppursshop.models.MyProductItem;
import com.shoppursshop.models.MySimpleItem;
import com.shoppursshop.models.ProductComboDetails;
import com.shoppursshop.models.ProductComboOffer;
import com.shoppursshop.models.ProductDiscountOffer;
import com.shoppursshop.models.SpinnerItem;
import com.shoppursshop.models.SubCategory;
import com.shoppursshop.utilities.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shweta on 6/9/2016.
 */
public class DbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = Constants.APP_NAME+".db";
    public static final String CUSTOMER_INFO_TABLE = "CUSTOMER_INFO";
    public static final String CAT_TABLE = "CATEGORY";
    public static final String SUB_CAT_TABLE = "SUB_CATEGORY";
    public static final String PRODUCT_TABLE = "PRODUCT_TABLE";
    public static final String PRODUCT_BARCODE_TABLE = "PRODUCT_BARCODE_TABLE";
    public static final String CART_TABLE = "CART_TABLE";
    public static final String PROD_FREE_OFFER_TABLE = "PROD_FREE_OFFER_TABLE";
    public static final String PROD_PRICE_TABLE = "PROD_PRICE_TABLE";
    public static final String PROD_PRICE_DETAIL_TABLE = "PROD_PRICE_DETAIL_TABLE";
    public static final String PROD_COMBO_TABLE = "PROD_COMBO_TABLE";
    public static final String PROD_COMBO_DETAIL_TABLE = "PROD_COMBO_DETAIL_TABLE";
    public static final String ID = "id";
    public static final String CAT_ID = "cat_id";
    public static final String NAME = "name";
    public static final String IMAGE = "image";
    public static final String PROD_SUB_CAT_ID = "PROD_SUB_CAT_ID";
    public static final String PROD_CAT_ID = "PROD_CAT_ID";
    public static final String PROD_NAME = "PROD_NAME";
    public static final String PROD_CODE = "PROD_CODE";
    public static final String PROD_BARCODE = "PROD_BARCODE";
    public static final String IS_BARCODE_AVAILABLE = "IS_BARCODE_AVAILABLE";
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
    public static final String TOTAL_AMOUNT = "totalAmount";
    public static final String TOTAL_MRP_AMOUNT = "totalMRPAmount";
    public static final String TOTAL_QTY = "totalQty";
    public static final String SIZE = "size";
    public static final String COLOR = "color";
    public static final String CREATED_BY = "CREATED_BY";
    public static final String UPDATED_BY = "UPDATED_BY";
    public static final String UPDATED_AT = "updatedAt";
    public static final String CREATED_AT = "createdAt";
    public static final String COUPON_ID = "couponId";
    public static final String COUPON_NAME = "couponName";
    public static final String COUPON_PER = "couponPer";
    public static final String COUPON_MAX_AMOUNT = "couponMaxAmount";
    public static final String DELIVERY_ADDRESS = "deliveryAddress";
    public static final String DELIVERY_PIN = "deliveryPin";
    public static final String DELIVERY_STATE = "deliveryState";
    public static final String DELIVERY_CITY = "deliveryCity";
    public static final String DELIVERY_COUNTRY = "deliveryCountry";
    public static final String DELIVERY_CHARGES= "deliveryCharges";
    public static final String LATITUDE= "latitude";
    public static final String LONGITUDE= "longitude";

    public static final String PROD_ID= "prodId";
    public static final String PROD_BUY_ID= "prodBuyId";
    public static final String PROD_FREE_ID= "prodFreeId";
    public static final String PROD_BUY_QTY= "prodBuyQty";
    public static final String PROD_FREE_QTY= "prodFreeQty";
    public static final String OFFER_ID= "offerId";
    public static final String START_DATE= "startDate";
    public static final String END_DATE= "endDate";
    public static final String STATUS= "status";

    public static final String CODE= "code";
    public static final String MOBILE_NO= "mobileNo";
    public static final String EMAIL= "email";
    public static final String ADDRESS= "address";
    public static final String COUNTRY= "country";
    public static final String STATE= "state";
    public static final String CITY= "city";
    public static final String PHOTO= "photo";
    public static final String IS_FAV= "isFav";
    public static final String RATINGS= "ratings";
    public static final String USER_CREATE_STATUS= "userCreateStatus";


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
            " "+PROD_CAT_ID+" TEXT NOT NULL, " +
            " "+PROD_NAME+" TEXT NOT NULL, " +
            " "+PROD_CODE+" TEXT, " +
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
            " "+IS_BARCODE_AVAILABLE+" TEXT, " +
            " "+CREATED_BY+" TEXT, " +
            " "+UPDATED_BY+" TEXT, " +
            " "+CREATED_AT+" TEXT, " +
            " "+UPDATED_AT+" TEXT)";

    public static final String CREATE_PRODUCT_BARCODE_TABLE = "create table "+PRODUCT_BARCODE_TABLE +
            "("+ID+" TEXT NOT NULL, " +
            " "+PROD_BARCODE+" TEXT)";

    public static final String CREATE_CART_TABLE = "create table "+CART_TABLE +
            "("+ID+" TEXT NOT NULL, " +
            " "+COUPON_ID+" TEXT, " +
            " "+COUPON_NAME+" TEXT, " +
            " "+COUPON_PER+" TEXT, " +
            " "+COUPON_MAX_AMOUNT+" TEXT , " +
            " "+DELIVERY_ADDRESS+" TEXT , " +
            " "+DELIVERY_PIN+" TEXT , " +
            " "+DELIVERY_COUNTRY+" TEXT , " +
            " "+DELIVERY_STATE+" TEXT , " +
            " "+DELIVERY_CITY+" TEXT, " +
            " "+DELIVERY_CHARGES+" TEXT, " +
            " "+LONGITUDE+" TEXT, " +
            " "+LATITUDE+" TEXT , " +
            " "+PROD_SUB_CAT_ID+" TEXT NOT NULL, " +
            " "+PROD_CAT_ID+" TEXT NOT NULL, " +
            " "+PROD_NAME+" TEXT NOT NULL, " +
            " "+PROD_CODE+" TEXT, " +
            " "+PROD_BARCODE+" TEXT, " +
            " "+PROD_DESC+" TEXT, " +
            " "+PROD_MRP+" TEXT, " +
            " "+PROD_SP+" TEXT, " +
            " "+PROD_HSN_CODE+" TEXT, " +
            " "+PROD_CGST+" TEXT, " +
            " "+PROD_IGST+" TEXT, " +
            " "+PROD_SGST+" TEXT, " +
            " "+TOTAL_QTY+" TEXT, " +
            " "+TOTAL_AMOUNT+" TEXT, " +
            " "+COLOR+" TEXT, " +
            " "+SIZE+" TEXT, " +
            " "+IS_BARCODE_AVAILABLE+" TEXT, " +
            " "+PROD_IMAGE_1+" TEXT, " +
            " "+PROD_IMAGE_2+" TEXT, " +
            " "+PROD_IMAGE_3+" TEXT)";

    public static final String CREATE_PROD_FREE_OFFER_TABLE = "create table "+PROD_FREE_OFFER_TABLE +
            "("+ID+" TEXT NOT NULL, " +
            " "+NAME+" TEXT NOT NULL, " +
            " "+PROD_BUY_ID+" TEXT NOT NULL, " +
            " "+PROD_FREE_ID+" TEXT NOT NULL, " +
            " "+PROD_BUY_QTY+" TEXT NOT NULL, " +
            " "+PROD_FREE_QTY+" TEXT NOT NULL, " +
            " "+START_DATE+" TEXT, " +
            " "+END_DATE+" TEXT, " +
            " "+STATUS+" TEXT, " +
            " "+CREATED_BY+" TEXT, " +
            " "+UPDATED_BY+" TEXT, " +
            " "+CREATED_AT+" TEXT, " +
            " "+UPDATED_AT+" TEXT)";

    public static final String CREATE_PROD_PRICE_TABLE = "create table "+PROD_PRICE_TABLE +
            "("+ID+" TEXT NOT NULL, " +
            " "+PROD_ID+" TEXT NOT NULL, " +
            " "+NAME+" TEXT NOT NULL, " +
            " "+START_DATE+" TEXT, " +
            " "+END_DATE+" TEXT, " +
            " "+STATUS+" TEXT, " +
            " "+CREATED_BY+" TEXT, " +
            " "+UPDATED_BY+" TEXT, " +
            " "+CREATED_AT+" TEXT, " +
            " "+UPDATED_AT+" TEXT)";

    public static final String CREATE_PROD_PRICE_DETAIL_TABLE = "create table "+PROD_PRICE_DETAIL_TABLE +
            "("+ID+" TEXT NOT NULL, " +
            " "+OFFER_ID+" TEXT NOT NULL, " +
            " "+TOTAL_QTY+" TEXT NOT NULL, " +
            " "+PROD_SP+" TEXT, " +
            " "+STATUS+" TEXT, " +
            " "+CREATED_BY+" TEXT, " +
            " "+UPDATED_BY+" TEXT, " +
            " "+CREATED_AT+" TEXT, " +
            " "+UPDATED_AT+" TEXT)";

    public static final String CREATE_CUSTOMER_INFO_TABLE = "create table "+CUSTOMER_INFO_TABLE +
            "("+ID+" TEXT NOT NULL, " +
            " "+NAME+" TEXT NOT NULL, " +
            " "+CODE+" TEXT NOT NULL, " +
            " "+MOBILE_NO+" TEXT, " +
            " "+EMAIL+" TEXT, " +
            " "+ADDRESS+" TEXT, " +
            " "+COUNTRY+" TEXT, " +
            " "+STATE+" TEXT, " +
            " "+CITY+" TEXT, " +
            " "+PHOTO+" TEXT, " +
            " "+IS_FAV+" TEXT, " +
            " "+RATINGS+" TEXT, " +
            " "+USER_CREATE_STATUS+" TEXT, " +
            " "+STATUS+" TEXT, " +
            " "+CREATED_BY+" TEXT, " +
            " "+UPDATED_BY+" TEXT, " +
            " "+CREATED_AT+" TEXT, " +
            " "+UPDATED_AT+" TEXT)";

    public DbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 17);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CAT_TABLE);
        db.execSQL(CREATE_SUB_CAT_TABLE);
        db.execSQL(CREATE_PRODUCT_TABLE);
        db.execSQL(CREATE_PRODUCT_BARCODE_TABLE);
        db.execSQL(CREATE_CART_TABLE);
        db.execSQL(CREATE_PROD_FREE_OFFER_TABLE);
        db.execSQL(CREATE_PROD_PRICE_TABLE);
        db.execSQL(CREATE_PROD_PRICE_DETAIL_TABLE);
        db.execSQL(CREATE_CUSTOMER_INFO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+PROD_FREE_OFFER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+PROD_PRICE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+PROD_PRICE_DETAIL_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+CUSTOMER_INFO_TABLE);
        db.execSQL(CREATE_PROD_FREE_OFFER_TABLE);
        db.execSQL(CREATE_PROD_PRICE_TABLE);
        db.execSQL(CREATE_PROD_PRICE_DETAIL_TABLE);
        db.execSQL(CREATE_CUSTOMER_INFO_TABLE);
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
        contentValues.put(PROD_SUB_CAT_ID, item.getProdSubCatId());
        contentValues.put(PROD_CAT_ID, item.getProdCatId());
        contentValues.put(PROD_CODE, item.getProdCode());
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
        contentValues.put(IS_BARCODE_AVAILABLE, item.getIsBarCodeAvailable());
        contentValues.put(CREATED_BY, item.getCreatedBy());
        contentValues.put(UPDATED_BY, item.getUpdatedBy());
        contentValues.put(CREATED_AT, createdAt);
        contentValues.put(UPDATED_AT, updatedAt);
        db.insert(PRODUCT_TABLE, null, contentValues);
        Log.i("DbHelper","Product Row is added "+item.getProdName());
        return true;
    }

    public boolean updateProduct(MyProductItem item, String updatedAt){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //contentValues.put(ID, item.getProdId());
      //  contentValues.put(PROD_SUB_CAT_ID, item.getProdCatId());
        contentValues.put(PROD_CODE, item.getProdCode());
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
        contentValues.put(UPDATED_BY, item.getUpdatedBy());
        contentValues.put(UPDATED_AT, updatedAt);
        db.update(PRODUCT_TABLE,contentValues,ID+" = ?",new String[]{String.valueOf(item.getProdId())});
        Log.i("DbHelper","Product Row is updated "+item.getProdName());
        return true;
    }

    public boolean addProductToCart(MyProductItem item){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, item.getProdId());
        contentValues.put(PROD_SUB_CAT_ID, item.getProdSubCatId());
        contentValues.put(PROD_CAT_ID, item.getProdCatId());
        contentValues.put(PROD_CODE, item.getProdCode());
        contentValues.put(PROD_BARCODE, item.getProdBarCode());
        contentValues.put(PROD_NAME, item.getProdName());
        contentValues.put(PROD_DESC, item.getProdDesc());
        contentValues.put(PROD_MRP, item.getProdMrp());
        contentValues.put(PROD_SP, item.getProdSp());
        contentValues.put(PROD_HSN_CODE, item.getProdHsnCode());
        contentValues.put(PROD_CGST, item.getProdCgst());
        contentValues.put(PROD_IGST, item.getProdIgst());
        contentValues.put(PROD_SGST, item.getProdSgst());
        contentValues.put(PROD_IMAGE_1, item.getProdImage1());
        contentValues.put(PROD_IMAGE_2, item.getProdImage2());
        contentValues.put(PROD_IMAGE_3, item.getProdImage3());
        contentValues.put(IS_BARCODE_AVAILABLE, item.getIsBarCodeAvailable());
        contentValues.put(TOTAL_AMOUNT, item.getTotalAmount());
        contentValues.put(TOTAL_QTY, item.getQty());
        db.insert(CART_TABLE, null, contentValues);
        Log.i("DbHelper","Product added in cart"+item.getProdName());
        return true;
    }

    public boolean addProductBarcode(int id,String barCode){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, id);
        contentValues.put(PROD_BARCODE, barCode);
        db.insert(PRODUCT_BARCODE_TABLE, null, contentValues);
        Log.i("DbHelper","Barcode Row is added "+barCode+" id "+ id);
        return true;
    }


    public boolean addProductFreeOffer(ProductDiscountOffer item, String createdAt, String updatedAt){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, item.getId());
        contentValues.put(NAME, item.getOfferName());
        contentValues.put(PROD_BUY_ID, item.getProdBuyId());
        contentValues.put(PROD_FREE_ID, item.getProdFreeId());
        contentValues.put(PROD_BUY_QTY, item.getProdBuyQty());
        contentValues.put(PROD_FREE_QTY, item.getProdFreeQty());
        contentValues.put(START_DATE, item.getStartDate());
        contentValues.put(END_DATE, item.getEndDate());
        contentValues.put(STATUS, item.getStatus());
        contentValues.put(CREATED_AT, createdAt);
        contentValues.put(UPDATED_AT, updatedAt);
        db.insert(PROD_FREE_OFFER_TABLE, null, contentValues);
        Log.i("DbHelper","Row is added");
        return true;
    }


    public boolean addProductPriceOffer(ProductComboOffer item, String createdAt, String updatedAt){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, item.getId());
        contentValues.put(PROD_ID, item.getProdId());
        contentValues.put(NAME, item.getOfferName());
        contentValues.put(START_DATE, item.getStartDate());
        contentValues.put(END_DATE, item.getEndDate());
        contentValues.put(STATUS, item.getStatus());
        contentValues.put(CREATED_AT, createdAt);
        contentValues.put(UPDATED_AT, updatedAt);
        db.insert(PROD_PRICE_TABLE, null, contentValues);
        Log.i("DbHelper","Row is added");
        return true;
    }

    public boolean addProductPriceDetailOffer(ProductComboDetails item, String createdAt, String updatedAt){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, item.getId());
        contentValues.put(OFFER_ID, item.getPcodPcoId());
        contentValues.put(TOTAL_QTY, item.getPcodProdQty());
        contentValues.put(PROD_SP, item.getPcodPrice());
        contentValues.put(STATUS, item.getStatus());
        contentValues.put(CREATED_AT, createdAt);
        contentValues.put(UPDATED_AT, updatedAt);
        db.insert(PROD_PRICE_DETAIL_TABLE, null, contentValues);
        Log.i("DbHelper","Row is added");
        return true;
    }

    public boolean addCustomerInfo(MyCustomer item, String createdAt, String updatedAt){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, item.getId());
        contentValues.put(CODE, item.getCode());
        contentValues.put(NAME, item.getName());
        contentValues.put(MOBILE_NO, item.getMobile());
        contentValues.put(EMAIL, item.getEmail());
        contentValues.put(ADDRESS, item.getAddress());
        contentValues.put(COUNTRY, item.getCountry());
        contentValues.put(STATE, item.getState());
        contentValues.put(CITY, item.getCity());
        contentValues.put(IS_FAV, item.getIsFav());
        contentValues.put(RATINGS, item.getRatings());
        contentValues.put(USER_CREATE_STATUS, item.getCustUserCreateStatus());
        contentValues.put(STATUS, item.getStatus());
        contentValues.put(CREATED_AT, createdAt);
        contentValues.put(UPDATED_AT, updatedAt);
        db.insert(CUSTOMER_INFO_TABLE, null, contentValues);
        Log.i("DbHelper","Row is added");
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

    public String getCategoryName(int catId){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select "+NAME+" from "+CAT_TABLE+" where "+ID+"  = ?";
        Cursor res =  db.rawQuery(query, new String[]{String.valueOf(catId)});
        String name = "";
        if(res.moveToFirst()){
            name = res.getString(res.getColumnIndex(NAME));
        }
        return name;
    }

    public String getSubCategoryName(int subCatId){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select "+NAME+" from "+SUB_CAT_TABLE+" where "+ID+" = ?";
        Cursor res =  db.rawQuery(query, new String[]{String.valueOf(subCatId)});
        String name = "";
        if(res.moveToFirst()){
            name = res.getString(res.getColumnIndex(NAME));
        }
        return name;
    }

    public boolean isCatExist(String catId){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select count(*) as counter from "+CAT_TABLE+" where "+ID+" =?";
        Cursor res =  db.rawQuery(query, new String[]{catId});
        boolean isExist = false;
        int count = 0;
        if(res.moveToFirst()){
            count = res.getInt(res.getColumnIndex("counter"));
        }

        if(count == 0){
            return false;
        }else{
            return true;
        }
    }

    public boolean isSubCatExist(String catId){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select count(*) as counter from "+SUB_CAT_TABLE+" where "+ID+" =?";
        Cursor res =  db.rawQuery(query, new String[]{catId});
        boolean isExist = false;
        int count = 0;
        if(res.moveToFirst()){
            count = res.getInt(res.getColumnIndex("counter"));
        }

        if(count == 0){
            return false;
        }else{
            return true;
        }
    }

    public boolean isProductExist(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select count(*) as counter from "+PRODUCT_TABLE+" where "+PROD_NAME+" = ?";
        Cursor res =  db.rawQuery(query, new String[]{name});
        boolean isExist = false;
        int count = 0;
        if(res.moveToFirst()){
            count = res.getInt(res.getColumnIndex("counter"));
        }

        Log.i("DbHelper","Count "+count+" name "+name);

        if(count == 0){
            return false;
        }else{
            return true;
        }
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
                productItem.setProdCatId(res.getInt(res.getColumnIndex(PROD_CAT_ID)));
                productItem.setProdSubCatId(res.getInt(res.getColumnIndex(PROD_SUB_CAT_ID)));
                productItem.setProdName(res.getString(res.getColumnIndex(PROD_NAME)));
                productItem.setProdCode(res.getString(res.getColumnIndex(PROD_CODE)));
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
                productItem.setIsBarCodeAvailable(res.getString(res.getColumnIndex(IS_BARCODE_AVAILABLE)));
                productItem.setProdMrp(res.getFloat(res.getColumnIndex(PROD_MRP)));
                productItem.setProdSp(res.getFloat(res.getColumnIndex(PROD_SP)));
                itemList.add(productItem);
            }while (res.moveToNext());
        }

        return itemList;
    }

    public ArrayList<Object> getProducts(int limit,int offset){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select * from "+PRODUCT_TABLE+" LIMIT ? OFFSET ?";
        Cursor res =  db.rawQuery(query, new String[]{String.valueOf(limit),String.valueOf(offset)});
        ArrayList<Object> itemList=new ArrayList<>();
        MyProductItem productItem = null;
        if(res.moveToFirst()){
            do{
                productItem=new MyProductItem();
                productItem.setProdId(res.getInt(res.getColumnIndex(ID)));
                productItem.setProdCatId(res.getInt(res.getColumnIndex(PROD_CAT_ID)));
                productItem.setProdSubCatId(res.getInt(res.getColumnIndex(PROD_SUB_CAT_ID)));
                productItem.setProdName(res.getString(res.getColumnIndex(PROD_NAME)));
                productItem.setProdCode(res.getString(res.getColumnIndex(PROD_CODE)));
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
                productItem.setIsBarCodeAvailable(res.getString(res.getColumnIndex(IS_BARCODE_AVAILABLE)));
                productItem.setProdMrp(res.getFloat(res.getColumnIndex(PROD_MRP)));
                productItem.setProdSp(res.getFloat(res.getColumnIndex(PROD_SP)));
                itemList.add(productItem);
            }while (res.moveToNext());
        }

        return itemList;
    }

    public ArrayList<MyProductItem> searchProducts(String keyword,int limit,int offset){
        SQLiteDatabase db = this.getReadableDatabase();
        keyword = "%"+keyword+"%";
        final String query="select * from "+PRODUCT_TABLE+" where "+PROD_NAME+" like ? or "+PROD_CODE+" like ? LIMIT ? OFFSET ?";
        Cursor res =  db.rawQuery(query, new String[]{keyword,keyword,String.valueOf(limit),String.valueOf(offset)});
        ArrayList<MyProductItem> itemList=new ArrayList<>();
        MyProductItem productItem = null;
        if(res.moveToFirst()){
            do{
                productItem=new MyProductItem();
                productItem.setProdId(res.getInt(res.getColumnIndex(ID)));
                productItem.setProdCatId(res.getInt(res.getColumnIndex(PROD_CAT_ID)));
                productItem.setProdSubCatId(res.getInt(res.getColumnIndex(PROD_SUB_CAT_ID)));
                productItem.setProdName(res.getString(res.getColumnIndex(PROD_NAME)));
                productItem.setProdCode(res.getString(res.getColumnIndex(PROD_CODE)));
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
                productItem.setIsBarCodeAvailable(res.getString(res.getColumnIndex(IS_BARCODE_AVAILABLE)));
                productItem.setProdMrp(res.getFloat(res.getColumnIndex(PROD_MRP)));
                productItem.setProdSp(res.getFloat(res.getColumnIndex(PROD_SP)));
                itemList.add(productItem);
            }while (res.moveToNext());
        }

        return itemList;
    }

    public ArrayList<MyProductItem> searchProductsForCart(String keyword,int limit,int offset){
        SQLiteDatabase db = this.getReadableDatabase();
        Log.i("Dbhelper","Searching products for cart");
        keyword = "%"+keyword+"%";
        final String query="select * from "+PRODUCT_TABLE+" where "+PROD_NAME+" like ? or "+ PROD_CODE+" like ?  AND "
                +ID+" NOT IN(select "+ID+" from "+CART_TABLE+") LIMIT ? OFFSET ?";
        Cursor res =  db.rawQuery(query, new String[]{keyword,keyword,String.valueOf(limit),String.valueOf(offset)});
        ArrayList<MyProductItem> itemList=new ArrayList<>();
        MyProductItem productItem = null;
        if(res.moveToFirst()){
            do{
                productItem=new MyProductItem();
                productItem.setProdId(res.getInt(res.getColumnIndex(ID)));
                productItem.setProdCatId(res.getInt(res.getColumnIndex(PROD_CAT_ID)));
                productItem.setProdSubCatId(res.getInt(res.getColumnIndex(PROD_SUB_CAT_ID)));
                productItem.setProdName(res.getString(res.getColumnIndex(PROD_NAME)));
                productItem.setProdCode(res.getString(res.getColumnIndex(PROD_CODE)));
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
                productItem.setIsBarCodeAvailable(res.getString(res.getColumnIndex(IS_BARCODE_AVAILABLE)));
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
            productItem.setProdCatId(res.getInt(res.getColumnIndex(PROD_CAT_ID)));
            productItem.setProdSubCatId(res.getInt(res.getColumnIndex(PROD_SUB_CAT_ID)));
            productItem.setProdName(res.getString(res.getColumnIndex(PROD_NAME)));
            productItem.setProdCode(res.getString(res.getColumnIndex(PROD_CODE)));
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
            productItem.setIsBarCodeAvailable(res.getString(res.getColumnIndex(IS_BARCODE_AVAILABLE)));
            productItem.setProdMrp(res.getFloat(res.getColumnIndex(PROD_MRP)));
            productItem.setProdSp(res.getFloat(res.getColumnIndex(PROD_SP)));
        }

        return productItem;
    }

    public MyProductItem getProductDetailsByCode(String code){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select * from "+PRODUCT_TABLE+" where "+PROD_CODE+" = ?";
        Cursor res =  db.rawQuery(query, new String[]{code});
        MyProductItem productItem = null;
        if(res.moveToFirst()){
            productItem=new MyProductItem();
            productItem.setProdId(res.getInt(res.getColumnIndex(ID)));
            productItem.setProdCatId(res.getInt(res.getColumnIndex(PROD_CAT_ID)));
            productItem.setProdSubCatId(res.getInt(res.getColumnIndex(PROD_SUB_CAT_ID)));
            productItem.setProdName(res.getString(res.getColumnIndex(PROD_NAME)));
            productItem.setProdCode(res.getString(res.getColumnIndex(PROD_CODE)));
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
            productItem.setIsBarCodeAvailable(res.getString(res.getColumnIndex(IS_BARCODE_AVAILABLE)));
            productItem.setProdMrp(res.getFloat(res.getColumnIndex(PROD_MRP)));
            productItem.setProdSp(res.getFloat(res.getColumnIndex(PROD_SP)));
        }

        return productItem;
    }

    public MyProductItem getProductDetailsByBarCode(String barCode){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select pt.*,pbt."+PROD_BARCODE+" from "+PRODUCT_TABLE+" as pt,"+PRODUCT_BARCODE_TABLE+" as pbt " +
                "where pt."+ID+" = pbt."+ID+" AND pbt."+PROD_BARCODE+" = ?";
        Cursor res =  db.rawQuery(query, new String[]{barCode});
        MyProductItem productItem = null;
        if(res.moveToFirst()){
            productItem=new MyProductItem();
            productItem.setProdId(res.getInt(res.getColumnIndex(ID)));
            productItem.setProdCatId(res.getInt(res.getColumnIndex(PROD_CAT_ID)));
            productItem.setProdSubCatId(res.getInt(res.getColumnIndex(PROD_SUB_CAT_ID)));
            productItem.setProdName(res.getString(res.getColumnIndex(PROD_NAME)));
            productItem.setProdCode(res.getString(res.getColumnIndex(PROD_CODE)));
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
            productItem.setIsBarCodeAvailable(res.getString(res.getColumnIndex(IS_BARCODE_AVAILABLE)));
            productItem.setProdMrp(res.getFloat(res.getColumnIndex(PROD_MRP)));
            productItem.setProdSp(res.getFloat(res.getColumnIndex(PROD_SP)));
        }

        return productItem;
    }

    public ArrayList<MyProductItem> getCartProducts(){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select * from "+CART_TABLE;
        Cursor res =  db.rawQuery(query, null);
        ArrayList<MyProductItem> itemList=new ArrayList<>();
        MyProductItem productItem = null;
        if(res.moveToFirst()){
            do{
                productItem=new MyProductItem();
                productItem.setProdId(res.getInt(res.getColumnIndex(ID)));
                productItem.setProdCatId(res.getInt(res.getColumnIndex(PROD_CAT_ID)));
                productItem.setProdSubCatId(res.getInt(res.getColumnIndex(PROD_SUB_CAT_ID)));
                productItem.setProdName(res.getString(res.getColumnIndex(PROD_NAME)));
                productItem.setProdCode(res.getString(res.getColumnIndex(PROD_CODE)));
                productItem.setProdBarCode(res.getString(res.getColumnIndex(PROD_BARCODE)));
                productItem.setProdDesc(res.getString(res.getColumnIndex(PROD_DESC)));
                productItem.setProdHsnCode(res.getString(res.getColumnIndex(PROD_HSN_CODE)));
                productItem.setProdCgst(res.getFloat(res.getColumnIndex(PROD_CGST)));
                productItem.setProdIgst(res.getFloat(res.getColumnIndex(PROD_IGST)));
                productItem.setProdSgst(res.getFloat(res.getColumnIndex(PROD_SGST)));
                productItem.setProdImage1(res.getString(res.getColumnIndex(PROD_IMAGE_1)));
                productItem.setProdImage2(res.getString(res.getColumnIndex(PROD_IMAGE_2)));
                productItem.setProdImage3(res.getString(res.getColumnIndex(PROD_IMAGE_3)));
                productItem.setIsBarCodeAvailable(res.getString(res.getColumnIndex(IS_BARCODE_AVAILABLE)));
                productItem.setProdMrp(res.getFloat(res.getColumnIndex(PROD_MRP)));
                productItem.setProdSp(res.getFloat(res.getColumnIndex(PROD_SP)));
                productItem.setTotalAmount(res.getFloat(res.getColumnIndex(TOTAL_AMOUNT)));
                productItem.setQty(res.getInt(res.getColumnIndex(TOTAL_QTY)));
                itemList.add(productItem);
            }while (res.moveToNext());
        }

        return itemList;
    }

    public List<MyProductItem> getShoppursProducts(String subCatId){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select * from "+PRODUCT_TABLE+" where "+PROD_SUB_CAT_ID+" IN(?)";
        Cursor res =  db.rawQuery(query, new String[]{subCatId});
        ArrayList<MyProductItem> itemList=new ArrayList<>();
        MyProductItem productItem = null;
        if(res.moveToFirst()){
            do{
                productItem=new MyProductItem();
                productItem.setProdId(res.getInt(res.getColumnIndex(ID)));
                productItem.setProdCatId(res.getInt(res.getColumnIndex(PROD_CAT_ID)));
                productItem.setProdSubCatId(res.getInt(res.getColumnIndex(PROD_SUB_CAT_ID)));
                productItem.setProdName(res.getString(res.getColumnIndex(PROD_NAME)));
                productItem.setProdCode(res.getString(res.getColumnIndex(PROD_CODE)));
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
                productItem.setIsBarCodeAvailable(res.getString(res.getColumnIndex(IS_BARCODE_AVAILABLE)));
                productItem.setProdMrp(res.getFloat(res.getColumnIndex(PROD_MRP)));
                productItem.setProdSp(res.getFloat(res.getColumnIndex(PROD_SP)));
                itemList.add(productItem);
            }while (res.moveToNext());
        }

        return itemList;
    }

    public void setQoh(int id,int qty){
        SQLiteDatabase db = this.getReadableDatabase();
        String query="select "+PROD_QOH+" from "+PRODUCT_TABLE+" where "+ID+" = ?";
        Cursor res =  db.rawQuery(query, new String[]{String.valueOf(id)});
        int prodQty = 0;
        if(res.moveToFirst()){
            prodQty = res.getInt(res.getColumnIndex(PROD_QOH));
        }

        prodQty = prodQty + qty;
       // query="UPDATE "+PRODUCT_TABLE+" SET "+PROD_QOH+" = ? where "+PROD_CODE+" = ?";
        ContentValues contentValues = new ContentValues();
        contentValues.put(PROD_QOH, prodQty);
        db.update(PRODUCT_TABLE,contentValues,ID+" = ?",new String[]{String.valueOf(id)});

    }

    public void removeBarCode(String barcode){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(PRODUCT_BARCODE_TABLE, PROD_BARCODE+" = ?",new String[]{barcode});

    }

    public List<String> getBarCodes(int prodId){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select * from "+PRODUCT_BARCODE_TABLE+" where "+ID+" = ?";
        Cursor res =  db.rawQuery(query, new String[]{String.valueOf(prodId)});
        List<String> itemList = new ArrayList<>();
        if(res.moveToFirst()){
            do{
                itemList.add(res.getString(res.getColumnIndex(PROD_BARCODE)));
            }while (res.moveToNext());
        }

        return itemList;
    }

    public List<Barcode> getBarCodesForCart(int prodId){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select * from "+PRODUCT_BARCODE_TABLE+" where "+ID+" = ?";
        Cursor res =  db.rawQuery(query, new String[]{String.valueOf(prodId)});
        List<Barcode> itemList = new ArrayList<>();
        Barcode barcode = null;
        if(res.moveToFirst()){
            do{
                barcode = new Barcode();
                barcode.setBarcode(res.getString(res.getColumnIndex(PROD_BARCODE)));
                itemList.add(barcode);
            }while (res.moveToNext());
        }

        return itemList;
    }

    public String getSubCatName(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select sct."+NAME+" from "+PRODUCT_TABLE+" as pt,"+SUB_CAT_TABLE+" as sct " +
                "where pt."+ID+" = ? and pt."+PROD_SUB_CAT_ID+" = sct."+ID;
        Cursor res =  db.rawQuery(query, new String[]{String.valueOf(id)});
        String name = "";
        if(res.moveToFirst()){

        }

        return name;
    }

    public int checkBarCodeExist(String barCode){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select * from "+PRODUCT_BARCODE_TABLE+" WHERE "+PROD_BARCODE+" = ?";
        Cursor res =  db.rawQuery(query, new String[]{barCode});
        int id = 0;
        if(res.moveToFirst()){
            id = res.getInt(res.getColumnIndex(ID));

        }
        return id;
    }

    public boolean checkProdExistInCart(String barCode){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select * from "+CART_TABLE+" WHERE "+PROD_BARCODE+" = ?";
        Cursor res =  db.rawQuery(query, new String[]{barCode});
        boolean exist = false;
        if(res.moveToFirst()){
            //prodCode = res.getString(res.getColumnIndex(PROD_BARCODE));
            exist = true;

        }else{
           // prodCode =  "no";
        }
        return exist;
    }

    public boolean checkProdExistInCart(int prodId){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select * from "+CART_TABLE+" WHERE "+ID+" = ?";
        Cursor res =  db.rawQuery(query, new String[]{String.valueOf(prodId)});
        boolean exist = false;
        if(res.moveToFirst()){
            //prodCode = res.getString(res.getColumnIndex(PROD_BARCODE));
            exist = true;

        }else{
            // prodCode =  "no";
        }
        return exist;
    }

    public float getTotalPriceCart(){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select sum("+TOTAL_AMOUNT+") as totalAmount from "+CART_TABLE;
        Cursor res =  db.rawQuery(query, null);
        float amount = 0f;
        if(res.moveToFirst()){
            do{
                amount = amount + res.getFloat(res.getColumnIndex(TOTAL_AMOUNT));
            }while (res.moveToNext());

        }

        return amount;
    }

    public float getTotalMrpPriceCart(){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select sum("+PROD_MRP+" * "+TOTAL_QTY+") as totalAmount from "+CART_TABLE;
        Cursor res =  db.rawQuery(query, null);
        float amount = 0f;
        if(res.moveToFirst()){
            do{
                amount = amount + res.getFloat(res.getColumnIndex(TOTAL_AMOUNT));
            }while (res.moveToNext());

        }

        return amount;
    }

    public float getTotalTaxesart(){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select sum("+PROD_CGST+" * "+PROD_SP+" * "+TOTAL_QTY+"/100) as totalAmount from "+CART_TABLE;
        Cursor res =  db.rawQuery(query, null);
        float amount = 0f;
        if(res.moveToFirst()){
            do{
                amount = amount + res.getFloat(res.getColumnIndex(TOTAL_AMOUNT));
            }while (res.moveToNext());

        }

        return amount;
    }

    public int getTotalQuantityCart(){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select sum("+TOTAL_QTY+") as totalQty from "+CART_TABLE;
        Cursor res =  db.rawQuery(query, null);
        int qty = 0;
        if(res.moveToFirst()){
            do{
                qty = qty + res.getInt(res.getColumnIndex("totalQty"));
            }while (res.moveToNext());

        }

        return qty;
    }

    public float getTotalTaxValue(String columnName){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select sum("+columnName+" * "+PROD_MRP+"/100) as totalTax from "+CART_TABLE;
        Cursor res =  db.rawQuery(query, null);
        float tax = 0f;
        if(res.moveToFirst()){
            do{
                tax = tax + res.getFloat(res.getColumnIndex("totalTax"));
            }while (res.moveToNext());

        }

        return tax;
    }

    public float getTotalDisValue(){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select sum("+PROD_MRP+" - "+PROD_SP+") as totalDis from "+CART_TABLE;
        Cursor res =  db.rawQuery(query, null);
        float dis = 0f;
        if(res.moveToFirst()){
            do{
                dis = dis + res.getFloat(res.getColumnIndex("totalDis"));
            }while (res.moveToNext());

        }

        return dis;
    }



    public List<ProductComboOffer> getProductPriceOffer(String prodId){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select * from "+PROD_PRICE_TABLE+" where "+PROD_ID+" IN(?)";
        final String detailQuery="select * from "+PROD_PRICE_DETAIL_TABLE+" where "+OFFER_ID+" IN(?)";
        Cursor res =  db.rawQuery(query, new String[]{prodId});
        Cursor detailsRes =  null;
        ArrayList<ProductComboOffer> itemList=new ArrayList<>();
        ArrayList<ProductComboDetails> productComboOfferDetails=null;
        ProductComboDetails productComboDetails = null;
        ProductComboOffer productComboOffer = null;
        if(res.moveToFirst()){
            do{
                productComboOffer = new ProductComboOffer();
                productComboOffer.setId(res.getInt(res.getColumnIndex(ID)));
                productComboOffer.setProdId(res.getInt(res.getColumnIndex(PROD_ID)));
                productComboOffer.setOfferName(res.getString(res.getColumnIndex(NAME)));
                productComboOffer.setStatus(res.getString(res.getColumnIndex(STATUS)));
                productComboOffer.setStartDate(res.getString(res.getColumnIndex(START_DATE)));
                productComboOffer.setEndDate(res.getString(res.getColumnIndex(END_DATE)));
                productComboOfferDetails=new ArrayList<>();
                detailsRes =  db.rawQuery(detailQuery, new String[]{String.valueOf(productComboOffer.getId())});
                if(detailsRes.moveToFirst()){
                    do{
                        productComboDetails = new ProductComboDetails();
                        productComboDetails.setId(detailsRes.getInt(detailsRes.getColumnIndex(ID)));
                        productComboDetails.setPcodPcoId(detailsRes.getInt(detailsRes.getColumnIndex(OFFER_ID)));
                        productComboDetails.setPcodProdQty(detailsRes.getInt(detailsRes.getColumnIndex(TOTAL_QTY)));
                        productComboDetails.setPcodPrice(detailsRes.getFloat(detailsRes.getColumnIndex(PROD_SP)));
                        productComboDetails.setStatus(detailsRes.getString(detailsRes.getColumnIndex(STATUS)));
                        productComboOfferDetails.add(productComboDetails);
                    }while (detailsRes.moveToNext());
                }
                productComboOffer.setProductComboOfferDetails(productComboOfferDetails);
                itemList.add(productComboOffer);

            }while (res.moveToNext());
        }

        return itemList;
    }

    public List<Object> getProductFreeOffer(){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select * from "+PROD_FREE_OFFER_TABLE;
        Cursor res =  db.rawQuery(query, null);
        List<Object> productDiscountOfferList = new ArrayList<>();
        ProductDiscountOffer productDiscountOffer = null;
        if(res.moveToFirst()){
            do{
                productDiscountOffer = new ProductDiscountOffer();
                productDiscountOffer.setId(res.getInt(res.getColumnIndex(ID)));
                productDiscountOffer.setOfferName(res.getString(res.getColumnIndex(NAME)));
                productDiscountOffer.setProdBuyId(res.getInt(res.getColumnIndex(PROD_BUY_ID)));
                productDiscountOffer.setProdFreeId(res.getInt(res.getColumnIndex(PROD_FREE_ID)));
                productDiscountOffer.setProdBuyQty(res.getInt(res.getColumnIndex(PROD_BUY_QTY)));
                productDiscountOffer.setProdFreeQty(res.getInt(res.getColumnIndex(PROD_FREE_QTY)));
                productDiscountOffer.setStatus(res.getString(res.getColumnIndex(STATUS)));
                productDiscountOffer.setStartDate(res.getString(res.getColumnIndex(START_DATE)));
                productDiscountOffer.setEndDate(res.getString(res.getColumnIndex(END_DATE)));
                productDiscountOfferList.add(productDiscountOffer);
            }while (res.moveToNext());

        }

        return productDiscountOfferList;
    }


    public List<ProductDiscountOffer> getProductFreeOffer(String prodId){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select * from "+PROD_FREE_OFFER_TABLE +" where "+PROD_BUY_ID+" = ?";
        Cursor res =  db.rawQuery(query, new String[]{prodId});
        List<ProductDiscountOffer> productDiscountOfferList = new ArrayList<>();
        ProductDiscountOffer productDiscountOffer = null;
        if(res.moveToFirst()){
            do{
                productDiscountOffer = new ProductDiscountOffer();
                productDiscountOffer.setId(res.getInt(res.getColumnIndex(ID)));
                productDiscountOffer.setOfferName(res.getString(res.getColumnIndex(NAME)));
                productDiscountOffer.setProdBuyId(res.getInt(res.getColumnIndex(PROD_BUY_ID)));
                productDiscountOffer.setProdFreeId(res.getInt(res.getColumnIndex(PROD_FREE_ID)));
                productDiscountOffer.setProdBuyQty(res.getInt(res.getColumnIndex(PROD_BUY_QTY)));
                productDiscountOffer.setProdFreeQty(res.getInt(res.getColumnIndex(PROD_FREE_QTY)));
                productDiscountOffer.setStatus(res.getString(res.getColumnIndex(STATUS)));
                productDiscountOffer.setStartDate(res.getString(res.getColumnIndex(START_DATE)));
                productDiscountOffer.setEndDate(res.getString(res.getColumnIndex(END_DATE)));
                productDiscountOfferList.add(productDiscountOffer);
            }while (res.moveToNext());

        }

        return productDiscountOfferList;
    }

    public List<Object> getCustomerList(String isFav){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select * from "+CUSTOMER_INFO_TABLE+" where "+IS_FAV+" = ?";
        Cursor res =  db.rawQuery(query, new String[]{isFav});
        List<Object> myCustomerList = new ArrayList<>();
        MyCustomer myCustomer = null;
        if(res.moveToFirst()){
            do{
                myCustomer = new MyCustomer();
                myCustomer.setId(res.getString(res.getColumnIndex(ID)));
                myCustomer.setCode(res.getString(res.getColumnIndex(CODE)));
                myCustomer.setName(res.getString(res.getColumnIndex(NAME)));
                myCustomer.setMobile(res.getString(res.getColumnIndex(MOBILE_NO)));
                myCustomer.setEmail(res.getString(res.getColumnIndex(EMAIL)));
                myCustomer.setAddress(res.getString(res.getColumnIndex(ADDRESS)));
                myCustomer.setCountry(res.getString(res.getColumnIndex(COUNTRY)));
                myCustomer.setState(res.getString(res.getColumnIndex(STATE)));
                myCustomer.setCity(res.getString(res.getColumnIndex(CITY)));
                myCustomer.setImage(res.getString(res.getColumnIndex(PHOTO)));
                myCustomer.setIsFav(res.getString(res.getColumnIndex(IS_FAV)));
                myCustomer.setRatings(res.getFloat(res.getColumnIndex(RATINGS)));
                myCustomer.setStatus(res.getString(res.getColumnIndex(STATUS)));
                myCustomer.setCustUserCreateStatus(res.getString(res.getColumnIndex(USER_CREATE_STATUS)));
                myCustomerList.add(myCustomer);
            }while (res.moveToNext());

        }

        return myCustomerList;
    }


    public void deleteCategory(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CAT_TABLE, ID+" = ?",new String[]{String.valueOf(id)});

    }

    public void deleteSubCategory(int catId){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(SUB_CAT_TABLE, CAT_ID+" = ?",new String[]{String.valueOf(catId)});

    }

    public void deleteSubCategoryById(int subCatId){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(SUB_CAT_TABLE, ID+" = ?",new String[]{String.valueOf(subCatId)});

    }

    public void deleteProductById(int prodId){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(PRODUCT_TABLE, ID+" = ?",new String[]{String.valueOf(prodId)});

    }

    public void deleteProducts(int catId){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(PRODUCT_TABLE, PROD_CAT_ID+" = ?",new String[]{String.valueOf(catId)});

    }

    public void deleteProductsBySubCatId(int subCatId){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(PRODUCT_TABLE, PROD_SUB_CAT_ID+" = ?",new String[]{String.valueOf(subCatId)});

    }

    public void updateCartData(String prodBarCode,int qty,float totalAmount){
        SQLiteDatabase db = this.getReadableDatabase();
        // query="UPDATE "+PRODUCT_TABLE+" SET "+PROD_QOH+" = ? where "+PROD_CODE+" = ?";
        ContentValues contentValues = new ContentValues();
        contentValues.put(TOTAL_QTY, qty);
        contentValues.put(TOTAL_AMOUNT, totalAmount);
        db.update(CART_TABLE,contentValues,PROD_BARCODE+" = ?",new String[]{prodBarCode});

    }

    public void updateCartData(int id,int qty,float totalAmount){
        SQLiteDatabase db = this.getReadableDatabase();
        // query="UPDATE "+PRODUCT_TABLE+" SET "+PROD_QOH+" = ? where "+PROD_CODE+" = ?";
        ContentValues contentValues = new ContentValues();
        contentValues.put(TOTAL_QTY, qty);
        contentValues.put(TOTAL_AMOUNT, totalAmount);
        db.update(CART_TABLE,contentValues,ID+" = ?",new String[]{String.valueOf(id)});

    }

    public boolean removeProductFromCart(String prodBarCode){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CART_TABLE, PROD_BARCODE+" = ?",new String[]{prodBarCode});
        return true;
    }

    public boolean removeProductFromCart(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CART_TABLE, ID+" = ?",new String[]{String.valueOf(id)});
        return true;
    }

    public boolean deleteTable(String tableName){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tableName, null, null);
        return true;
    }

    public boolean dropAndCreateBookingTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+CAT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+SUB_CAT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+PRODUCT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+PRODUCT_BARCODE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+CART_TABLE);
        onCreate(db);
        return true;
    }


}
