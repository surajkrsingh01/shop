package com.shoppursshop.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.shoppursshop.activities.CategoryListActivity;
import com.shoppursshop.models.Barcode;
import com.shoppursshop.models.CatListItem;
import com.shoppursshop.models.Category;
import com.shoppursshop.models.Coupon;
import com.shoppursshop.models.MyCustomer;
import com.shoppursshop.models.MyProduct;
import com.shoppursshop.models.MyProductItem;
import com.shoppursshop.models.MySimpleItem;
import com.shoppursshop.models.ProductColor;
import com.shoppursshop.models.ProductComboDetails;
import com.shoppursshop.models.ProductComboOffer;
import com.shoppursshop.models.ProductDiscountOffer;
import com.shoppursshop.models.ProductSize;
import com.shoppursshop.models.ProductUnit;
import com.shoppursshop.models.SpinnerItem;
import com.shoppursshop.models.SubCategory;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.Utility;

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
    public static final String PRODUCT_UNIT_TABLE = "PRODUCT_UNIT_TABLE";
    public static final String PRODUCT_SIZE_TABLE = "PRODUCT_SIZE_TABLE";
    public static final String PRODUCT_COLOR_TABLE = "PRODUCT_COLOR_TABLE";
    public static final String CART_TABLE = "CART_TABLE";
    public static final String SHOP_CART_TABLE = "SHOP_CART_TABLE";
    public static final String PROD_FREE_OFFER_TABLE = "PROD_FREE_OFFER_TABLE";
    public static final String PROD_PRICE_TABLE = "PROD_PRICE_TABLE";
    public static final String PROD_PRICE_DETAIL_TABLE = "PROD_PRICE_DETAIL_TABLE";
    public static final String PROD_COMBO_TABLE = "PROD_COMBO_TABLE";
    public static final String PROD_COMBO_DETAIL_TABLE = "PROD_COMBO_DETAIL_TABLE";
    public static final String COUPON_TABLE = "COUPON_TABLE";
    public static final String CART_PRODUCT_UNIT_TABLE = "CART_PRODUCT_UNIT_TABLE";
    public static final String CART_PRODUCT_SIZE_TABLE = "CART_PRODUCT_SIZE_TABLE";
    public static final String CART_PRODUCT_COLOR_TABLE = "CART_PRODUCT_COLOR_TABLE";
    public static final String CART_PROD_FREE_OFFER_TABLE = "CART_PROD_FREE_OFFER_TABLE";
    public static final String CART_PROD_PRICE_TABLE = "CART_PROD_PRICE_TABLE";
    public static final String CART_PROD_PRICE_DETAIL_TABLE = "CART_PROD_PRICE_DETAIL_TABLE";
    public static final String CART_PROD_COMBO_TABLE = "CART_PROD_COMBO_TABLE";
    public static final String CART_PROD_COMBO_DETAIL_TABLE = "CART_PROD_COMBO_DETAIL_TABLE";
    public static final String CART_COUPON_TABLE = "CART_COUPON_TABLE";
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
    public static final String PROD_CGST_PER = "PROD_CGST_PER";
    public static final String PROD_IGST_PER = "PROD_IGST_PER";
    public static final String PROD_SGST_PER = "PROD_SGST_PER";
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
    public static final String LOCALITY= "locality";
    public static final String STATE= "state";
    public static final String CITY= "city";
    public static final String PHOTO= "photo";
    public static final String IS_FAV= "isFav";
    public static final String RATINGS= "ratings";
    public static final String USER_CREATE_STATUS= "userCreateStatus";

    public static final String OFFER_TYPE= "offerType";
    public static final String OFFER_ITEM_COUNTER= "offerItemCounter";
    public static final String FREE_PRODUCT_POSITION= "freeProductPosition";
    public static final String COMBO_PRODUCT_IDS= "comboProductIds";
    public static final String VALUE= "value";
    public static final String SIZE_ID= "sizeId";
    public static final String COLOR_NAME= "colorName";
    public static final String COLOR_VALUE= "colorValue";
    public static final String UNIT= "unit";
    private Context context;

    public static final String CREATE_CAT_TABLE = "create table "+CAT_TABLE +
            "("+ID+" TEXT NOT NULL, " +
            " "+NAME+" TEXT NOT NULL, " +
            " "+IMAGE+" TEXT, " +
            " "+CREATED_AT+" TEXT, " +
            " "+UPDATED_AT+" TEXT)";

    public static final String CREATE_SUB_CAT_TABLE = "create table "+SUB_CAT_TABLE +
            "("+ID+" TEXT  NOT NULL, " +
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

    public static final String CREATE_PRODUCT_UNIT_TABLE = "create table "+PRODUCT_UNIT_TABLE +
            "("+ID+" TEXT NOT NULL, " +
            " "+PROD_ID+" TEXT, " +
            " "+NAME+" TEXT, " +
            " "+VALUE+" TEXT, " +
            " "+STATUS+" TEXT)";

    public static final String CREATE_PRODUCT_SIZE_TABLE = "create table "+PRODUCT_SIZE_TABLE +
            "("+ID+" TEXT NOT NULL, " +
            " "+PROD_ID+" TEXT, " +
            " "+SIZE+" TEXT, " +
            " "+STATUS+" TEXT)";

    public static final String CREATE_PRODUCT_COLOR_TABLE = "create table "+PRODUCT_COLOR_TABLE +
            "("+ID+" TEXT NOT NULL, " +
            " "+PROD_ID+" TEXT, " +
            " "+SIZE_ID+" TEXT, " +
            " "+COLOR_NAME+" TEXT, " +
            " "+COLOR_VALUE+" TEXT, " +
            " "+STATUS+" TEXT)";

    public static final String CREATE_CART_TABLE = "create table "+CART_TABLE +
            "("+ID+" TEXT, " +
            " "+COUPON_ID+" TEXT, " +
            " "+COUPON_NAME+" TEXT, " +
            " "+COUPON_PER+" TEXT, " +
            " "+COUPON_MAX_AMOUNT+" TEXT , " +
            " "+OFFER_ID+" TEXT , " +
            " "+OFFER_TYPE+" TEXT , " +
            " "+DELIVERY_ADDRESS+" TEXT , " +
            " "+DELIVERY_PIN+" TEXT , " +
            " "+DELIVERY_COUNTRY+" TEXT , " +
            " "+DELIVERY_STATE+" TEXT , " +
            " "+DELIVERY_CITY+" TEXT, " +
            " "+DELIVERY_CHARGES+" TEXT, " +
            " "+LONGITUDE+" TEXT, " +
            " "+LATITUDE+" TEXT , " +
            " "+OFFER_ITEM_COUNTER+" TEXT, " +
            " "+FREE_PRODUCT_POSITION+" INTEGER, " +
            " "+COMBO_PRODUCT_IDS+" TEXT, " +
            " "+PROD_SUB_CAT_ID+" TEXT, " +
            " "+PROD_CAT_ID+" TEXT, " +
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
            " "+PROD_CGST_PER+" TEXT, " +
            " "+PROD_IGST_PER+" TEXT, " +
            " "+PROD_SGST_PER+" TEXT, " +
            " "+TOTAL_QTY+" TEXT, " +
            " "+TOTAL_AMOUNT+" TEXT, " +
            " "+UNIT+" TEXT, " +
            " "+COLOR+" TEXT, " +
            " "+SIZE+" TEXT, " +
            " "+IS_BARCODE_AVAILABLE+" TEXT, " +
            " "+PROD_IMAGE_1+" TEXT, " +
            " "+PROD_IMAGE_2+" TEXT, " +
            " "+PROD_IMAGE_3+" TEXT)";

    public static final String CREATE_SHOP_CART_TABLE = "create table "+SHOP_CART_TABLE +
            "("+ID+" TEXT, " +
            " "+COUPON_ID+" TEXT, " +
            " "+COUPON_NAME+" TEXT, " +
            " "+COUPON_PER+" TEXT, " +
            " "+COUPON_MAX_AMOUNT+" TEXT , " +
            " "+OFFER_ID+" TEXT , " +
            " "+OFFER_TYPE+" TEXT , " +
            " "+DELIVERY_ADDRESS+" TEXT , " +
            " "+DELIVERY_PIN+" TEXT , " +
            " "+DELIVERY_COUNTRY+" TEXT , " +
            " "+DELIVERY_STATE+" TEXT , " +
            " "+DELIVERY_CITY+" TEXT, " +
            " "+DELIVERY_CHARGES+" TEXT, " +
            " "+LONGITUDE+" TEXT, " +
            " "+LATITUDE+" TEXT , " +
            " "+OFFER_ITEM_COUNTER+" TEXT, " +
            " "+FREE_PRODUCT_POSITION+" INTEGER, " +
            " "+COMBO_PRODUCT_IDS+" TEXT, " +
            " "+PROD_SUB_CAT_ID+" TEXT, " +
            " "+PROD_CAT_ID+" TEXT, " +
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
            " "+PROD_CGST_PER+" TEXT, " +
            " "+PROD_IGST_PER+" TEXT, " +
            " "+PROD_SGST_PER+" TEXT, " +
            " "+TOTAL_QTY+" TEXT, " +
            " "+TOTAL_AMOUNT+" TEXT, " +
            " "+UNIT+" TEXT, " +
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

    public static final String CREATE_PROD_COMBO_TABLE = "create table "+PROD_COMBO_TABLE +
            "("+ID+" TEXT NOT NULL, " +
            " "+NAME+" TEXT NOT NULL, " +
            " "+START_DATE+" TEXT, " +
            " "+END_DATE+" TEXT, " +
            " "+STATUS+" TEXT, " +
            " "+CREATED_BY+" TEXT, " +
            " "+UPDATED_BY+" TEXT, " +
            " "+CREATED_AT+" TEXT, " +
            " "+UPDATED_AT+" TEXT)";

    public static final String CREATE_PROD_COMBO_DETAIL_TABLE = "create table "+PROD_COMBO_DETAIL_TABLE +
            "("+ID+" TEXT NOT NULL, " +
            " "+OFFER_ID+" TEXT NOT NULL, " +
            " "+PROD_ID+" TEXT NOT NULL, " +
            " "+TOTAL_QTY+" TEXT NOT NULL, " +
            " "+PROD_SP+" TEXT, " +
            " "+STATUS+" TEXT, " +
            " "+CREATED_BY+" TEXT, " +
            " "+UPDATED_BY+" TEXT, " +
            " "+CREATED_AT+" TEXT, " +
            " "+UPDATED_AT+" TEXT)";

    public static final String CREATE_COUPON_TABLE = "create table "+COUPON_TABLE +
            "("+ID+" TEXT NOT NULL, " +
            " "+COUPON_PER+" TEXT NOT NULL, " +
            " "+COUPON_MAX_AMOUNT+" TEXT NOT NULL, " +
            " "+NAME+" TEXT NOT NULL, " +
            " "+STATUS+" TEXT, " +
            " "+START_DATE+" TEXT, " +
            " "+END_DATE+" TEXT, " +
            " "+CREATED_BY+" TEXT, " +
            " "+UPDATED_BY+" TEXT, " +
            " "+CREATED_AT+" TEXT, " +
            " "+UPDATED_AT+" TEXT)";

    public static final String CREATE_CART_PRODUCT_UNIT_TABLE = "create table "+CART_PRODUCT_UNIT_TABLE +
            "("+ID+" TEXT NOT NULL, " +
            " "+PROD_ID+" TEXT, " +
            " "+NAME+" TEXT, " +
            " "+VALUE+" TEXT, " +
            " "+STATUS+" TEXT)";

    public static final String CREATE_CART_PRODUCT_SIZE_TABLE = "create table "+CART_PRODUCT_SIZE_TABLE +
            "("+ID+" TEXT NOT NULL, " +
            " "+PROD_ID+" TEXT, " +
            " "+SIZE+" TEXT, " +
            " "+STATUS+" TEXT)";

    public static final String CREATE_CART_PRODUCT_COLOR_TABLE = "create table "+CART_PRODUCT_COLOR_TABLE +
            "("+ID+" TEXT NOT NULL, " +
            " "+PROD_ID+" TEXT, " +
            " "+SIZE_ID+" TEXT, " +
            " "+COLOR_NAME+" TEXT, " +
            " "+COLOR_VALUE+" TEXT, " +
            " "+STATUS+" TEXT)";

    public static final String CREATE_CART_PROD_FREE_OFFER_TABLE = "create table "+CART_PROD_FREE_OFFER_TABLE +
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

    public static final String CREATE_CART_PROD_PRICE_TABLE = "create table "+CART_PROD_PRICE_TABLE +
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

    public static final String CREATE_CART_PROD_PRICE_DETAIL_TABLE = "create table "+CART_PROD_PRICE_DETAIL_TABLE +
            "("+ID+" TEXT NOT NULL, " +
            " "+OFFER_ID+" TEXT NOT NULL, " +
            " "+TOTAL_QTY+" TEXT NOT NULL, " +
            " "+PROD_SP+" TEXT, " +
            " "+STATUS+" TEXT, " +
            " "+CREATED_BY+" TEXT, " +
            " "+UPDATED_BY+" TEXT, " +
            " "+CREATED_AT+" TEXT, " +
            " "+UPDATED_AT+" TEXT)";

    public static final String CREATE_CART_PROD_COMBO_TABLE = "create table "+CART_PROD_COMBO_TABLE +
            "("+ID+" TEXT NOT NULL, " +
            " "+NAME+" TEXT NOT NULL, " +
            " "+START_DATE+" TEXT, " +
            " "+END_DATE+" TEXT, " +
            " "+STATUS+" TEXT, " +
            " "+CREATED_BY+" TEXT, " +
            " "+UPDATED_BY+" TEXT, " +
            " "+CREATED_AT+" TEXT, " +
            " "+UPDATED_AT+" TEXT)";

    public static final String CREATE_CART_PROD_COMBO_DETAIL_TABLE = "create table "+CART_PROD_COMBO_DETAIL_TABLE +
            "("+ID+" TEXT NOT NULL, " +
            " "+OFFER_ID+" TEXT NOT NULL, " +
            " "+PROD_ID+" TEXT NOT NULL, " +
            " "+TOTAL_QTY+" TEXT NOT NULL, " +
            " "+PROD_SP+" TEXT, " +
            " "+STATUS+" TEXT, " +
            " "+CREATED_BY+" TEXT, " +
            " "+UPDATED_BY+" TEXT, " +
            " "+CREATED_AT+" TEXT, " +
            " "+UPDATED_AT+" TEXT)";

    public static final String CREATE_CART_COUPON_TABLE = "create table "+CART_COUPON_TABLE +
            "("+ID+" TEXT NOT NULL, " +
            " "+COUPON_PER+" TEXT NOT NULL, " +
            " "+COUPON_MAX_AMOUNT+" TEXT NOT NULL, " +
            " "+NAME+" TEXT NOT NULL, " +
            " "+STATUS+" TEXT, " +
            " "+START_DATE+" TEXT, " +
            " "+END_DATE+" TEXT, " +
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
            " "+LOCALITY+" TEXT, " +
            " "+STATE+" TEXT, " +
            " "+CITY+" TEXT, " +
            " "+LATITUDE+" TEXT, " +
            " "+LONGITUDE+" TEXT, " +
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
        super(context, DATABASE_NAME, null, 30);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CAT_TABLE);
        db.execSQL(CREATE_SUB_CAT_TABLE);
        db.execSQL(CREATE_PRODUCT_TABLE);
        db.execSQL(CREATE_PRODUCT_BARCODE_TABLE);
        db.execSQL(CREATE_PRODUCT_UNIT_TABLE);
        db.execSQL(CREATE_PRODUCT_SIZE_TABLE);
        db.execSQL(CREATE_PRODUCT_COLOR_TABLE);
        db.execSQL(CREATE_CART_TABLE);
        db.execSQL(CREATE_SHOP_CART_TABLE);
        db.execSQL(CREATE_PROD_FREE_OFFER_TABLE);
        db.execSQL(CREATE_PROD_PRICE_TABLE);
        db.execSQL(CREATE_PROD_PRICE_DETAIL_TABLE);
        db.execSQL(CREATE_PROD_COMBO_TABLE);
        db.execSQL(CREATE_PROD_COMBO_DETAIL_TABLE);
        db.execSQL(CREATE_COUPON_TABLE);
        db.execSQL(CREATE_CART_PRODUCT_UNIT_TABLE);
        db.execSQL(CREATE_CART_PRODUCT_SIZE_TABLE);
        db.execSQL(CREATE_CART_PRODUCT_COLOR_TABLE);
        db.execSQL(CREATE_CART_PROD_FREE_OFFER_TABLE);
        db.execSQL(CREATE_CART_PROD_PRICE_TABLE);
        db.execSQL(CREATE_CART_PROD_PRICE_DETAIL_TABLE);
        db.execSQL(CREATE_CART_PROD_COMBO_TABLE);
        db.execSQL(CREATE_CART_PROD_COMBO_DETAIL_TABLE);
        db.execSQL(CREATE_CART_COUPON_TABLE);
        db.execSQL(CREATE_CUSTOMER_INFO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+CART_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+SHOP_CART_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+CART_PRODUCT_SIZE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+CART_PRODUCT_UNIT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+CART_PRODUCT_COLOR_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+CART_PROD_FREE_OFFER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+CART_PROD_PRICE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+CART_PROD_PRICE_DETAIL_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+CART_PROD_COMBO_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+CART_PROD_COMBO_DETAIL_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+CART_COUPON_TABLE);
        db.execSQL(CREATE_CART_TABLE);
        db.execSQL(CREATE_SHOP_CART_TABLE);
        db.execSQL(CREATE_CART_PRODUCT_SIZE_TABLE);
        db.execSQL(CREATE_CART_PRODUCT_UNIT_TABLE);
        db.execSQL(CREATE_CART_PRODUCT_COLOR_TABLE);
        db.execSQL(CREATE_CART_PROD_FREE_OFFER_TABLE);
        db.execSQL(CREATE_CART_PROD_PRICE_TABLE);
        db.execSQL(CREATE_CART_PROD_PRICE_DETAIL_TABLE);
        db.execSQL(CREATE_CART_PROD_COMBO_TABLE);
        db.execSQL(CREATE_CART_PROD_COMBO_DETAIL_TABLE);
        db.execSQL(CREATE_CART_COUPON_TABLE);

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
        contentValues.put(OFFER_ID, item.getOfferId());
        contentValues.put(OFFER_TYPE, item.getOfferType());
        contentValues.put(PROD_SUB_CAT_ID, item.getProdSubCatId());
        contentValues.put(PROD_CAT_ID, item.getProdCatId());
        contentValues.put(PROD_CODE, item.getProdCode());
        contentValues.put(PROD_BARCODE, item.getProdBarCode());
        contentValues.put(PROD_NAME, item.getProdName());
        contentValues.put(PROD_DESC, item.getProdDesc());
        contentValues.put(PROD_MRP, item.getProdMrp());
        contentValues.put(PROD_SP, item.getProdSp());
        contentValues.put(PROD_HSN_CODE, item.getProdHsnCode());
        float rate = ((item.getProdSp() * (item.getProdCgst()+item.getProdSgst()))/(100 +
                (item.getProdCgst()+item.getProdSgst())));
        contentValues.put(PROD_CGST, rate/2);
        contentValues.put(PROD_IGST, rate);
        contentValues.put(PROD_SGST, rate/2);
        contentValues.put(PROD_CGST_PER, item.getProdCgst());
        contentValues.put(PROD_IGST_PER, item.getProdIgst());
        contentValues.put(PROD_SGST_PER, item.getProdSgst());
        contentValues.put(PROD_IMAGE_1, item.getProdImage1());
        contentValues.put(PROD_IMAGE_2, item.getProdImage2());
        contentValues.put(PROD_IMAGE_3, item.getProdImage3());
        contentValues.put(IS_BARCODE_AVAILABLE, item.getIsBarCodeAvailable());
        contentValues.put(TOTAL_AMOUNT, item.getTotalAmount());
        contentValues.put(TOTAL_QTY, item.getQty());
        contentValues.put(OFFER_ITEM_COUNTER, item.getOfferCounter());
        contentValues.put(FREE_PRODUCT_POSITION, item.getFreeProductPosition());
        contentValues.put(COMBO_PRODUCT_IDS, item.getComboProductIds());

        if(TextUtils.isEmpty(item.getUnit())){
            if(item.getProductUnitList()!=null && item.getProductUnitList().size() > 0){
                ProductUnit unit = item.getProductUnitList().get(0);
                contentValues.put(UNIT, unit.getUnitValue()+" "+unit.getUnitName());
            }else{
                contentValues.put(UNIT, item.getUnit());
            }

        }else{
            contentValues.put(UNIT, item.getUnit());
        }

        if(TextUtils.isEmpty(item.getSize())){
            if(item.getProductSizeList()!=null && item.getProductSizeList().size() > 0){
                ProductSize size = item.getProductSizeList().get(0);
                contentValues.put(SIZE, size.getSize());
                if(TextUtils.isEmpty(item.getColor())){
                    if(size.getProductColorList()!=null && size.getProductColorList().size() > 0){
                        ProductColor color = size.getProductColorList().get(0);
                        contentValues.put(COLOR, color.getColorName());
                    }else{
                        contentValues.put(COLOR, item.getColor());
                    }
                }else{
                    contentValues.put(COLOR, item.getColor());
                }
            }else{
                contentValues.put(SIZE, item.getSize());
                contentValues.put(COLOR, item.getColor());
            }

        }else{
            contentValues.put(SIZE, item.getSize());
            contentValues.put(COLOR, item.getColor());
        }
       // contentValues.put(UNIT, item.getUnit());
        db.insert(CART_TABLE, null, contentValues);
        Log.i("DbHelper","Product added in cart"+item.getProdName());
        return true;
    }

    public boolean addProductToShopCart(MyProductItem item){
        Object ob = item.getProductOffer();
        if(ob instanceof ProductComboOffer) {
            ProductComboOffer productPriceOffer = (ProductComboOffer)ob;
            addCartProductPriceOffer(productPriceOffer, Utility.getTimeStamp(),Utility.getTimeStamp());
            for(ProductComboDetails productPriceDetails : productPriceOffer.getProductComboOfferDetails()){
                addCartProductPriceDetailOffer(productPriceDetails,  Utility.getTimeStamp(),Utility.getTimeStamp());
            }
        }else if(ob instanceof ProductDiscountOffer){
            ProductDiscountOffer productDiscountOffer = (ProductDiscountOffer)ob;
            addCartProductFreeOffer(productDiscountOffer,  Utility.getTimeStamp(),Utility.getTimeStamp());
        }

        if(item.getProductUnitList()!=null) {
            for (int i = 0; i < item.getProductUnitList().size(); i++) {
                addCartProductUnit(item.getProductUnitList().get(i), item.getId());
            }
        }
        if(item.getProductSizeList()!=null) {
            for (int i = 0; i < item.getProductSizeList().size(); i++) {
                addCartProductSize(item.getProductSizeList().get(i), item.getId());
                for (int j = 0; j < item.getProductSizeList().get(i).getProductColorList().size(); j++) {
                    addCartProductColor(item.getProductSizeList().get(i).getProductColorList().get(j), item.getProductSizeList().get(i).getId());
                }
            }
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, item.getProdId());
        contentValues.put(OFFER_ID, item.getOfferId());
        contentValues.put(OFFER_TYPE, item.getOfferType());
        contentValues.put(PROD_SUB_CAT_ID, item.getProdSubCatId());
        contentValues.put(PROD_CAT_ID, item.getProdCatId());
        contentValues.put(PROD_CODE, item.getProdCode());
        contentValues.put(PROD_BARCODE, item.getProdBarCode());
        contentValues.put(PROD_NAME, item.getProdName());
        contentValues.put(PROD_DESC, item.getProdDesc());
        contentValues.put(PROD_MRP, item.getProdMrp());
        contentValues.put(PROD_SP, item.getProdSp());
        contentValues.put(PROD_HSN_CODE, item.getProdHsnCode());
        float rate = ((item.getProdSp() * (item.getProdCgst()+item.getProdSgst()))/(100 +
                (item.getProdCgst()+item.getProdSgst())));
        contentValues.put(PROD_CGST, rate/2);
        contentValues.put(PROD_IGST, rate);
        contentValues.put(PROD_SGST, rate/2);
        contentValues.put(PROD_CGST_PER, item.getProdCgst());
        contentValues.put(PROD_IGST_PER, item.getProdIgst());
        contentValues.put(PROD_SGST_PER, item.getProdSgst());
        Log.i("dbhelper","cgst "+rate/2);
        contentValues.put(PROD_IMAGE_1, item.getProdImage1());
        contentValues.put(PROD_IMAGE_2, item.getProdImage2());
        contentValues.put(PROD_IMAGE_3, item.getProdImage3());
        contentValues.put(IS_BARCODE_AVAILABLE, item.getIsBarCodeAvailable());
        contentValues.put(TOTAL_AMOUNT, item.getTotalAmount());
        contentValues.put(TOTAL_QTY, item.getQty());
        contentValues.put(OFFER_ITEM_COUNTER, item.getOfferCounter());
        contentValues.put(FREE_PRODUCT_POSITION, item.getFreeProductPosition());
        contentValues.put(COMBO_PRODUCT_IDS, item.getComboProductIds());

        if(TextUtils.isEmpty(item.getUnit())){
            if(item.getProductUnitList()!=null && item.getProductUnitList().size() > 0){
                ProductUnit unit = item.getProductUnitList().get(0);
                contentValues.put(UNIT, unit.getUnitValue()+" "+unit.getUnitName());
            }else{
                contentValues.put(UNIT, item.getUnit());
            }

        }else{
            contentValues.put(UNIT, item.getUnit());
        }

        if(TextUtils.isEmpty(item.getSize())){
            if(item.getProductSizeList()!=null && item.getProductSizeList().size() > 0){
                ProductSize size = item.getProductSizeList().get(0);
                contentValues.put(SIZE, size.getSize());
                if(TextUtils.isEmpty(item.getColor())){
                    if(size.getProductColorList()!=null && size.getProductColorList().size() > 0){
                        ProductColor color = size.getProductColorList().get(0);
                        contentValues.put(COLOR, color.getColorName());
                    }else{
                        contentValues.put(COLOR, item.getColor());
                    }
                }else{
                    contentValues.put(COLOR, item.getColor());
                }
            }else{
                contentValues.put(SIZE, item.getSize());
                contentValues.put(COLOR, item.getColor());
            }

        }else{
            contentValues.put(SIZE, item.getSize());
            contentValues.put(COLOR, item.getColor());
        }
        // contentValues.put(UNIT, item.getUnit());
        db.insert(SHOP_CART_TABLE, null, contentValues);
        Log.i("DbHelper","Product added in shop cart"+item.getProdName());
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

    public boolean addProductUnit(ProductUnit item,int prodId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, item.getId());
        contentValues.put(PROD_ID, prodId);
        contentValues.put(NAME, item.getUnitName());
        contentValues.put(VALUE, item.getUnitValue());
        contentValues.put(STATUS, item.getStatus());
        db.insert(PRODUCT_UNIT_TABLE, null, contentValues);
        Log.i("DbHelper","Unit Row is added");
        return true;
    }

    public boolean addProductSize(ProductSize item, int prodId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, item.getId());
        contentValues.put(PROD_ID, prodId);
        contentValues.put(SIZE, item.getSize());
        contentValues.put(STATUS, item.getStatus());
        db.insert(PRODUCT_SIZE_TABLE, null, contentValues);
        Log.i("DbHelper","Size Row is added");
        return true;
    }

    public boolean addProductColor(ProductColor item, int prodId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, item.getId());
        contentValues.put(PROD_ID, prodId);
        contentValues.put(SIZE_ID, item.getSizeId());
        contentValues.put(COLOR_NAME, item.getColorName());
        contentValues.put(COLOR_VALUE, item.getColorValue());
        contentValues.put(STATUS, item.getStatus());
        db.insert(PRODUCT_COLOR_TABLE, null, contentValues);
        Log.i("DbHelper","Color Row is added");
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

    public boolean addProductComboOffer(ProductComboOffer item, String createdAt, String updatedAt){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, item.getId());
        contentValues.put(NAME, item.getOfferName());
        contentValues.put(START_DATE, item.getStartDate());
        contentValues.put(END_DATE, item.getEndDate());
        contentValues.put(STATUS, item.getStatus());
        contentValues.put(CREATED_AT, createdAt);
        contentValues.put(UPDATED_AT, updatedAt);
        db.insert(PROD_COMBO_TABLE, null, contentValues);
        Log.i("DbHelper","Row is added");
        return true;
    }

    public boolean addProductComboDetailOffer(ProductComboDetails item, String createdAt, String updatedAt){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, item.getId());
        contentValues.put(OFFER_ID, item.getPcodPcoId());
        contentValues.put(PROD_ID, item.getPcodProdId());
        contentValues.put(TOTAL_QTY, item.getPcodProdQty());
        contentValues.put(PROD_SP, item.getPcodPrice());
        contentValues.put(STATUS, item.getStatus());
        contentValues.put(CREATED_AT, createdAt);
        contentValues.put(UPDATED_AT, updatedAt);
        db.insert(PROD_COMBO_DETAIL_TABLE, null, contentValues);
        Log.i("DbHelper","Row is added");
        return true;
    }

    public boolean addCouponOffer(Coupon item, String createdAt, String updatedAt){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, item.getId());
        contentValues.put(COUPON_PER, item.getPercentage());
        contentValues.put(NAME, item.getName());
        contentValues.put(COUPON_MAX_AMOUNT, item.getAmount());
        contentValues.put(STATUS, item.getStatus());
        contentValues.put(START_DATE, item.getStartDate());
        contentValues.put(END_DATE, item.getEndDate());
        contentValues.put(CREATED_AT, createdAt);
        contentValues.put(UPDATED_AT, updatedAt);
        db.insert(COUPON_TABLE, null, contentValues);
        Log.i("DbHelper","Row is added");
        return true;
    }

    public boolean addCartProductUnit(ProductUnit item,int prodId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, item.getId());
        contentValues.put(PROD_ID, prodId);
        contentValues.put(NAME, item.getUnitName());
        contentValues.put(VALUE, item.getUnitValue());
        contentValues.put(STATUS, item.getStatus());
        db.insert(CART_PRODUCT_UNIT_TABLE, null, contentValues);
        Log.i("DbHelper","Unit Row is added");
        return true;
    }

    public boolean addCartProductSize(ProductSize item, int prodId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, item.getId());
        contentValues.put(PROD_ID, prodId);
        contentValues.put(SIZE, item.getSize());
        contentValues.put(STATUS, item.getStatus());
        db.insert(CART_PRODUCT_SIZE_TABLE, null, contentValues);
        Log.i("DbHelper","Size Row is added");
        return true;
    }

    public boolean addCartProductColor(ProductColor item, int prodId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, item.getId());
        contentValues.put(PROD_ID, prodId);
        contentValues.put(SIZE_ID, item.getSizeId());
        contentValues.put(COLOR_NAME, item.getColorName());
        contentValues.put(COLOR_VALUE, item.getColorValue());
        contentValues.put(STATUS, item.getStatus());
        db.insert(CART_PRODUCT_COLOR_TABLE, null, contentValues);
        Log.i("DbHelper","Color Row is added");
        return true;
    }

    public boolean addCartProductFreeOffer(ProductDiscountOffer item, String createdAt, String updatedAt){
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
        db.insert(CART_PROD_FREE_OFFER_TABLE, null, contentValues);
        Log.i("DbHelper","Row is added");
        return true;
    }


    public boolean addCartProductPriceOffer(ProductComboOffer item, String createdAt, String updatedAt){
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
        db.insert(CART_PROD_PRICE_TABLE, null, contentValues);
        Log.i("DbHelper","Cart price offer Row is added");
        return true;
    }

    public boolean addCartProductPriceDetailOffer(ProductComboDetails item, String createdAt, String updatedAt){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, item.getId());
        contentValues.put(OFFER_ID, item.getPcodPcoId());
        contentValues.put(TOTAL_QTY, item.getPcodProdQty());
        contentValues.put(PROD_SP, item.getPcodPrice());
        contentValues.put(STATUS, item.getStatus());
        contentValues.put(CREATED_AT, createdAt);
        contentValues.put(UPDATED_AT, updatedAt);
        db.insert(CART_PROD_PRICE_DETAIL_TABLE, null, contentValues);
        Log.i("DbHelper","Row is added");
        return true;
    }

    public boolean addCartProductComboOffer(ProductComboOffer item, String createdAt, String updatedAt){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, item.getId());
        contentValues.put(NAME, item.getOfferName());
        contentValues.put(START_DATE, item.getStartDate());
        contentValues.put(END_DATE, item.getEndDate());
        contentValues.put(STATUS, item.getStatus());
        contentValues.put(CREATED_AT, createdAt);
        contentValues.put(UPDATED_AT, updatedAt);
        db.insert(CART_PROD_COMBO_TABLE, null, contentValues);
        Log.i("DbHelper","Row is added");
        return true;
    }

    public boolean addCartProductComboDetailOffer(ProductComboDetails item, String createdAt, String updatedAt){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, item.getId());
        contentValues.put(OFFER_ID, item.getPcodPcoId());
        contentValues.put(PROD_ID, item.getPcodProdId());
        contentValues.put(TOTAL_QTY, item.getPcodProdQty());
        contentValues.put(PROD_SP, item.getPcodPrice());
        contentValues.put(STATUS, item.getStatus());
        contentValues.put(CREATED_AT, createdAt);
        contentValues.put(UPDATED_AT, updatedAt);
        db.insert(CART_PROD_COMBO_DETAIL_TABLE, null, contentValues);
        Log.i("DbHelper","Row is added");
        return true;
    }

    public boolean addCartCouponOffer(Coupon item, String createdAt, String updatedAt){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, item.getId());
        contentValues.put(COUPON_PER, item.getPercentage());
        contentValues.put(NAME, item.getName());
        contentValues.put(COUPON_MAX_AMOUNT, item.getAmount());
        contentValues.put(STATUS, item.getStatus());
        contentValues.put(START_DATE, item.getStartDate());
        contentValues.put(END_DATE, item.getEndDate());
        contentValues.put(CREATED_AT, createdAt);
        contentValues.put(UPDATED_AT, updatedAt);
        db.insert(CART_COUPON_TABLE, null, contentValues);
        Log.i("DbHelper","Row is added");
        return true;
    }

    public boolean addCustomerInfo(MyCustomer item, String createdAt, String updatedAt){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, item.getId());
        contentValues.put(CODE, item.getCode());
        contentValues.put(NAME, item.getName());
        contentValues.put(PHOTO, item.getImage());
        contentValues.put(MOBILE_NO, item.getMobile());
        contentValues.put(EMAIL, item.getEmail());
        contentValues.put(ADDRESS, item.getAddress());
        contentValues.put(COUNTRY, item.getCountry());
        contentValues.put(STATE, item.getState());
        contentValues.put(CITY, item.getCity());
        contentValues.put(LOCALITY, item.getLocality());
        contentValues.put(LATITUDE, item.getLatitude());
        contentValues.put(LONGITUDE, item.getLongitude());
        contentValues.put(IS_FAV, item.getIsFav());
        contentValues.put(RATINGS, item.getRatings());
        contentValues.put(USER_CREATE_STATUS, item.getCustUserCreateStatus());
        contentValues.put(STATUS, item.getStatus());
        contentValues.put(CREATED_AT, createdAt);
        contentValues.put(UPDATED_AT, updatedAt);
        db.insert(CUSTOMER_INFO_TABLE, null, contentValues);
        Log.i("DbHelper","Customer Row is added");
        return true;
    }

    public boolean updatedCustomerInfo(MyCustomer item,String updatedAt){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, item.getName());
        contentValues.put(PHOTO, item.getImage());
        contentValues.put(MOBILE_NO, item.getMobile());
        contentValues.put(EMAIL, item.getEmail());
        contentValues.put(ADDRESS, item.getAddress());
        contentValues.put(COUNTRY, item.getCountry());
        contentValues.put(STATE, item.getState());
        contentValues.put(CITY, item.getCity());
        contentValues.put(LOCALITY, item.getLocality());
        contentValues.put(LATITUDE, item.getLatitude());
        contentValues.put(LONGITUDE, item.getLongitude());
        contentValues.put(IS_FAV, item.getIsFav());
        contentValues.put(RATINGS, item.getRatings());
        contentValues.put(USER_CREATE_STATUS, item.getCustUserCreateStatus());
        contentValues.put(STATUS, item.getStatus());
        contentValues.put(UPDATED_AT, updatedAt);
        db.update(CUSTOMER_INFO_TABLE, contentValues,CODE + " = ?",new String[]{item.getCode()});
        Log.i("DbHelper","Customer Row is updated");
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
        final String query="select * from "+CAT_TABLE;
        //Cursor res =  db.rawQuery(query, new String[]{String.valueOf(limit),String.valueOf(offset)});
        Cursor res =  db.rawQuery(query, null);
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
        String sizeSql="select * from "+PRODUCT_SIZE_TABLE+" WHERE "+PROD_ID+" = ? AND "+STATUS+" = 'N'";
        String colorSql="select * from "+PRODUCT_COLOR_TABLE+" WHERE "+SIZE_ID+" = ? AND "+STATUS+" = 'N'";
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
                productItem.setProductUnitList(getProductUnitList(db,productItem.getProdId()));
                productItem.setProductSizeList(getProductSizeList(db,productItem.getProdId()));

                itemList.add(productItem);
            }while (res.moveToNext());
        }

        return itemList;
    }

    public List<ProductUnit> getProductUnitList(SQLiteDatabase db,int prodId){
        String unitSql="select * from "+PRODUCT_UNIT_TABLE+" WHERE "+PROD_ID+" = ? AND "+STATUS+" = 'N'";
        Cursor res =  db.rawQuery(unitSql, new String[]{String.valueOf(prodId)});
        ArrayList<ProductUnit> itemList=new ArrayList<>();
        ProductUnit item = null;
        if(res.moveToFirst()){
            do{
                item=new ProductUnit();
                item.setId(res.getInt(res.getColumnIndex(ID)));
                item.setUnitName(res.getString(res.getColumnIndex(NAME)));
                item.setUnitValue(res.getString(res.getColumnIndex(VALUE)));
                item.setStatus(res.getString(res.getColumnIndex(STATUS)));
                itemList.add(item);
            }while (res.moveToNext());
        }

        return itemList;
    }

    public List<ProductSize> getProductSizeList(SQLiteDatabase db,int prodId){
        String unitSql="select * from "+PRODUCT_SIZE_TABLE+" WHERE "+PROD_ID+" = ? AND "+STATUS+" = 'N'";
        Cursor res =  db.rawQuery(unitSql, new String[]{String.valueOf(prodId)});
        ArrayList<ProductSize> itemList=new ArrayList<>();
        ProductSize item = null;
        if(res.moveToFirst()){
            do{
                item=new ProductSize();
                item.setId(res.getInt(res.getColumnIndex(ID)));
                item.setSize(res.getString(res.getColumnIndex(SIZE)));
                item.setStatus(res.getString(res.getColumnIndex(STATUS)));
                item.setProductColorList(getProductColorList(db,item.getId()));
                itemList.add(item);
            }while (res.moveToNext());
        }
        return itemList;
    }

    public List<ProductColor> getProductColorList(SQLiteDatabase db,int sizeId){
        String unitSql="select * from "+PRODUCT_COLOR_TABLE+" WHERE "+SIZE_ID+" = ? AND "+STATUS+" = 'N'";
        Cursor res =  db.rawQuery(unitSql, new String[]{String.valueOf(sizeId)});
        ArrayList<ProductColor> itemList=new ArrayList<>();
        ProductColor item = null;
        if(res.moveToFirst()){
            do{
                item=new ProductColor();
                item.setId(res.getInt(res.getColumnIndex(ID)));
                item.setSizeId(res.getInt(res.getColumnIndex(SIZE_ID)));
                item.setColorName(res.getString(res.getColumnIndex(COLOR_NAME)));
                item.setColorValue(res.getString(res.getColumnIndex(COLOR_VALUE)));
                item.setStatus(res.getString(res.getColumnIndex(STATUS)));
                itemList.add(item);
            }while (res.moveToNext());
        }
        return itemList;
    }

    public List<ProductUnit> getCartProductUnitList(SQLiteDatabase db,int prodId){
        String unitSql="select * from "+CART_PRODUCT_UNIT_TABLE+" WHERE "+PROD_ID+" = ? AND "+STATUS+" = 'N'";
        Cursor res =  db.rawQuery(unitSql, new String[]{String.valueOf(prodId)});
        ArrayList<ProductUnit> itemList=new ArrayList<>();
        ProductUnit item = null;
        if(res.moveToFirst()){
            do{
                item=new ProductUnit();
                item.setId(res.getInt(res.getColumnIndex(ID)));
                item.setUnitName(res.getString(res.getColumnIndex(NAME)));
                item.setUnitValue(res.getString(res.getColumnIndex(VALUE)));
                item.setStatus(res.getString(res.getColumnIndex(STATUS)));
                itemList.add(item);
            }while (res.moveToNext());
        }

        return itemList;
    }

    public List<ProductSize> getCartProductSizeList(SQLiteDatabase db,int prodId){
        String unitSql="select * from "+CART_PRODUCT_SIZE_TABLE+" WHERE "+PROD_ID+" = ? AND "+STATUS+" = 'N'";
        Cursor res =  db.rawQuery(unitSql, new String[]{String.valueOf(prodId)});
        ArrayList<ProductSize> itemList=new ArrayList<>();
        ProductSize item = null;
        if(res.moveToFirst()){
            do{
                item=new ProductSize();
                item.setId(res.getInt(res.getColumnIndex(ID)));
                item.setSize(res.getString(res.getColumnIndex(SIZE)));
                item.setStatus(res.getString(res.getColumnIndex(STATUS)));
                item.setProductColorList(getProductColorList(db,item.getId()));
                itemList.add(item);
            }while (res.moveToNext());
        }
        return itemList;
    }

    public List<ProductColor> getCartProductColorList(SQLiteDatabase db,int sizeId){
        String unitSql="select * from "+CART_PRODUCT_COLOR_TABLE+" WHERE "+SIZE_ID+" = ? AND "+STATUS+" = 'N'";
        Cursor res =  db.rawQuery(unitSql, new String[]{String.valueOf(sizeId)});
        ArrayList<ProductColor> itemList=new ArrayList<>();
        ProductColor item = null;
        if(res.moveToFirst()){
            do{
                item=new ProductColor();
                item.setId(res.getInt(res.getColumnIndex(ID)));
                item.setSizeId(res.getInt(res.getColumnIndex(SIZE_ID)));
                item.setColorName(res.getString(res.getColumnIndex(COLOR_NAME)));
                item.setColorValue(res.getString(res.getColumnIndex(COLOR_VALUE)));
                item.setStatus(res.getString(res.getColumnIndex(STATUS)));
                itemList.add(item);
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
                productItem.setProductUnitList(getProductUnitList(db,productItem.getProdId()));
                productItem.setProductSizeList(getProductSizeList(db,productItem.getProdId()));
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
                productItem.setProductUnitList(getProductUnitList(db,productItem.getProdId()));
                productItem.setProductSizeList(getProductSizeList(db,productItem.getProdId()));
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
                productItem.setProductUnitList(getProductUnitList(db,productItem.getProdId()));
                productItem.setProductSizeList(getProductSizeList(db,productItem.getProdId()));
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
            productItem.setProductUnitList(getProductUnitList(db,productItem.getProdId()));
            productItem.setProductSizeList(getProductSizeList(db,productItem.getProdId()));
        }

        return productItem;
    }

    public MyProductItem getShopCartProductDetails(int prodId){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select * from "+SHOP_CART_TABLE+" where "+ID+" = ?";
        Cursor res =  db.rawQuery(query, new String[]{String.valueOf(prodId)});
        MyProductItem productItem = null;
        if(res.moveToFirst()){
            productItem=new MyProductItem();
            productItem.setProdId(res.getInt(res.getColumnIndex(ID)));
            productItem.setId(res.getInt(res.getColumnIndex(ID)));
            productItem.setOfferId(res.getString(res.getColumnIndex(OFFER_ID)));
            productItem.setOfferType(res.getString(res.getColumnIndex(OFFER_TYPE)));
            productItem.setProdCatId(res.getInt(res.getColumnIndex(PROD_CAT_ID)));
            productItem.setProdSubCatId(res.getInt(res.getColumnIndex(PROD_SUB_CAT_ID)));
            productItem.setProdName(res.getString(res.getColumnIndex(PROD_NAME)));
            productItem.setProdCode(res.getString(res.getColumnIndex(PROD_CODE)));
            productItem.setProdBarCode(res.getString(res.getColumnIndex(PROD_BARCODE)));
            productItem.setProdDesc(res.getString(res.getColumnIndex(PROD_DESC)));
            productItem.setProdHsnCode(res.getString(res.getColumnIndex(PROD_HSN_CODE)));
            productItem.setProdCgst(res.getFloat(res.getColumnIndex(PROD_CGST_PER)));
            productItem.setProdIgst(res.getFloat(res.getColumnIndex(PROD_IGST_PER)));
            productItem.setProdSgst(res.getFloat(res.getColumnIndex(PROD_SGST_PER)));
            productItem.setProdImage1(res.getString(res.getColumnIndex(PROD_IMAGE_1)));
            productItem.setProdImage2(res.getString(res.getColumnIndex(PROD_IMAGE_2)));
            productItem.setProdImage3(res.getString(res.getColumnIndex(PROD_IMAGE_3)));
            productItem.setIsBarCodeAvailable(res.getString(res.getColumnIndex(IS_BARCODE_AVAILABLE)));
            productItem.setProdMrp(res.getFloat(res.getColumnIndex(PROD_MRP)));
            productItem.setProdSp(res.getFloat(res.getColumnIndex(PROD_SP)));
            productItem.setTotalAmount(res.getFloat(res.getColumnIndex(TOTAL_AMOUNT)));
            productItem.setQty(res.getInt(res.getColumnIndex(TOTAL_QTY)));
            productItem.setOfferCounter(res.getInt(res.getColumnIndex(OFFER_ITEM_COUNTER)));
            productItem.setFreeProductPosition(res.getInt(res.getColumnIndex(FREE_PRODUCT_POSITION)));
            productItem.setComboProductIds(res.getString(res.getColumnIndex(COMBO_PRODUCT_IDS)));
            productItem.setUnit(res.getString(res.getColumnIndex(UNIT)));
            productItem.setColor(res.getString(res.getColumnIndex(COLOR)));
            productItem.setSize(res.getString(res.getColumnIndex(SIZE)));

            productItem.setProductUnitList(getCartProductUnitList(db,productItem.getProdId()));
            productItem.setProductSizeList(getCartProductSizeList(db,productItem.getProdId()));

            List<ProductComboOffer> productPriceOfferList = getCartProductPriceOffer(""+productItem.getId());
            List<ProductDiscountOffer> productDiscountOfferList = getCartProductFreeOffer(""+productItem.getId());
            if(productPriceOfferList.size() > 0){
                productItem.setProductOffer(productPriceOfferList.get(0));
                productItem.setOfferId(""+productPriceOfferList.get(0).getId());
                productItem.setOfferType("price");
            }else if(productDiscountOfferList.size() > 0){
                productItem.setProductOffer(productDiscountOfferList.get(0));
                productItem.setOfferId(""+productDiscountOfferList.get(0).getId());
                productItem.setOfferType("free");
            }

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
            productItem.setProductUnitList(getProductUnitList(db,productItem.getProdId()));
            productItem.setProductSizeList(getProductSizeList(db,productItem.getProdId()));
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
            productItem.setProductUnitList(getProductUnitList(db,productItem.getProdId()));
            productItem.setProductSizeList(getProductSizeList(db,productItem.getProdId()));
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
                productItem.setOfferId(res.getString(res.getColumnIndex(OFFER_ID)));
                productItem.setOfferType(res.getString(res.getColumnIndex(OFFER_TYPE)));
                productItem.setProdCatId(res.getInt(res.getColumnIndex(PROD_CAT_ID)));
                productItem.setProdSubCatId(res.getInt(res.getColumnIndex(PROD_SUB_CAT_ID)));
                productItem.setProdName(res.getString(res.getColumnIndex(PROD_NAME)));
                productItem.setProdCode(res.getString(res.getColumnIndex(PROD_CODE)));
                productItem.setProdBarCode(res.getString(res.getColumnIndex(PROD_BARCODE)));
                productItem.setProdDesc(res.getString(res.getColumnIndex(PROD_DESC)));
                productItem.setProdHsnCode(res.getString(res.getColumnIndex(PROD_HSN_CODE)));
                productItem.setProdCgst(res.getFloat(res.getColumnIndex(PROD_CGST_PER)));
                productItem.setProdIgst(res.getFloat(res.getColumnIndex(PROD_IGST_PER)));
                productItem.setProdSgst(res.getFloat(res.getColumnIndex(PROD_SGST_PER)));
                productItem.setProdImage1(res.getString(res.getColumnIndex(PROD_IMAGE_1)));
                productItem.setProdImage2(res.getString(res.getColumnIndex(PROD_IMAGE_2)));
                productItem.setProdImage3(res.getString(res.getColumnIndex(PROD_IMAGE_3)));
                productItem.setIsBarCodeAvailable(res.getString(res.getColumnIndex(IS_BARCODE_AVAILABLE)));
                productItem.setProdMrp(res.getFloat(res.getColumnIndex(PROD_MRP)));
                productItem.setProdSp(res.getFloat(res.getColumnIndex(PROD_SP)));
                productItem.setTotalAmount(res.getFloat(res.getColumnIndex(TOTAL_AMOUNT)));
                productItem.setQty(res.getInt(res.getColumnIndex(TOTAL_QTY)));
                productItem.setOfferCounter(res.getInt(res.getColumnIndex(OFFER_ITEM_COUNTER)));
                productItem.setFreeProductPosition(res.getInt(res.getColumnIndex(FREE_PRODUCT_POSITION)));
                productItem.setComboProductIds(res.getString(res.getColumnIndex(COMBO_PRODUCT_IDS)));
                productItem.setUnit(res.getString(res.getColumnIndex(UNIT)));
                productItem.setColor(res.getString(res.getColumnIndex(COLOR)));
                productItem.setSize(res.getString(res.getColumnIndex(SIZE)));
                productItem.setProductUnitList(getProductUnitList(db,productItem.getProdId()));
                productItem.setProductSizeList(getProductSizeList(db,productItem.getProdId()));
                if(productItem.getProdSp() == 0f){
                    itemList.add(productItem.getFreeProductPosition(),productItem);
                }else{
                    itemList.add(productItem);
                }


            }while (res.moveToNext());
        }

        return itemList;
    }

    public ArrayList<MyProductItem> getShopCartProducts(){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select * from "+SHOP_CART_TABLE;
        Cursor res =  db.rawQuery(query, null);
        ArrayList<MyProductItem> itemList=new ArrayList<>();
        MyProductItem productItem = null;
        if(res.moveToFirst()){
            do{
                productItem=new MyProductItem();
                productItem.setProdId(res.getInt(res.getColumnIndex(ID)));
                productItem.setId(res.getInt(res.getColumnIndex(ID)));
                productItem.setOfferId(res.getString(res.getColumnIndex(OFFER_ID)));
                productItem.setOfferType(res.getString(res.getColumnIndex(OFFER_TYPE)));
                productItem.setProdCatId(res.getInt(res.getColumnIndex(PROD_CAT_ID)));
                productItem.setProdSubCatId(res.getInt(res.getColumnIndex(PROD_SUB_CAT_ID)));
                productItem.setProdName(res.getString(res.getColumnIndex(PROD_NAME)));
                productItem.setProdCode(res.getString(res.getColumnIndex(PROD_CODE)));
                productItem.setProdBarCode(res.getString(res.getColumnIndex(PROD_BARCODE)));
                productItem.setProdDesc(res.getString(res.getColumnIndex(PROD_DESC)));
                productItem.setProdHsnCode(res.getString(res.getColumnIndex(PROD_HSN_CODE)));
                productItem.setProdCgst(res.getFloat(res.getColumnIndex(PROD_CGST_PER)));
                productItem.setProdIgst(res.getFloat(res.getColumnIndex(PROD_IGST_PER)));
                productItem.setProdSgst(res.getFloat(res.getColumnIndex(PROD_SGST_PER)));
                productItem.setProdImage1(res.getString(res.getColumnIndex(PROD_IMAGE_1)));
                productItem.setProdImage2(res.getString(res.getColumnIndex(PROD_IMAGE_2)));
                productItem.setProdImage3(res.getString(res.getColumnIndex(PROD_IMAGE_3)));
                productItem.setIsBarCodeAvailable(res.getString(res.getColumnIndex(IS_BARCODE_AVAILABLE)));
                productItem.setProdMrp(res.getFloat(res.getColumnIndex(PROD_MRP)));
                productItem.setProdSp(res.getFloat(res.getColumnIndex(PROD_SP)));
                productItem.setTotalAmount(res.getFloat(res.getColumnIndex(TOTAL_AMOUNT)));
                productItem.setQty(res.getInt(res.getColumnIndex(TOTAL_QTY)));
                productItem.setOfferCounter(res.getInt(res.getColumnIndex(OFFER_ITEM_COUNTER)));
                productItem.setFreeProductPosition(res.getInt(res.getColumnIndex(FREE_PRODUCT_POSITION)));
                productItem.setComboProductIds(res.getString(res.getColumnIndex(COMBO_PRODUCT_IDS)));
                productItem.setUnit(res.getString(res.getColumnIndex(UNIT)));
                productItem.setColor(res.getString(res.getColumnIndex(COLOR)));
                productItem.setSize(res.getString(res.getColumnIndex(SIZE)));
                productItem.setProductUnitList(getCartProductUnitList(db,productItem.getProdId()));
                productItem.setProductSizeList(getCartProductSizeList(db,productItem.getProdId()));

                if(productItem.getProdSp() == 0f){
                    if(itemList.size()>productItem.getFreeProductPosition())
                        itemList.add(productItem.getFreeProductPosition(),productItem);
                }else{
                    List<ProductComboOffer> productPriceOfferList = getCartProductPriceOffer(""+productItem.getId());
                    List<ProductDiscountOffer> productDiscountOfferList = getCartProductFreeOffer(""+productItem.getId());
                    if(productPriceOfferList.size() > 0){
                        productItem.setProductOffer(productPriceOfferList.get(0));
                        productItem.setOfferId(""+productPriceOfferList.get(0).getId());
                        productItem.setOfferType("price");
                    }else if(productDiscountOfferList.size() > 0){
                        productItem.setProductOffer(productDiscountOfferList.get(0));
                        productItem.setOfferId(""+productDiscountOfferList.get(0).getId());
                        productItem.setOfferType("free");
                    }
                    itemList.add(productItem);
                }


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

    public void updateQoh(int id,int qty){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PROD_QOH, qty);
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

    public boolean checkProdExistInShopCart(String prodId){

        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select * from "+SHOP_CART_TABLE+" WHERE "+ID+" = ?" + "  AND " + PROD_SP + "!=? ";
        Cursor res =  db.rawQuery(query, new String[]{ prodId, String.valueOf(0f)});
        boolean exist = false;
        if(res.moveToFirst()){
            //prodCode = res.getString(res.getColumnIndex(PROD_BARCODE));
            exist = true;

        }else{
            // prodCode =  "no";
        }
        return exist;
    }

    public boolean checkProdExistInShopCart(int prodId){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select * from "+SHOP_CART_TABLE+" WHERE "+ID+" = ?";
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

    public float getTotalPriceShopCart(){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select sum("+TOTAL_AMOUNT+") as totalAmount from "+SHOP_CART_TABLE;
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

    public float getTotalMrpShopPriceCart(){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select sum("+PROD_MRP+" * "+TOTAL_QTY+") as totalAmount from "+SHOP_CART_TABLE;
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
        final String query="select sum(("+PROD_CGST+" + "+PROD_SGST+")*"+TOTAL_QTY+") as totalAmount from "+CART_TABLE;
        Cursor res =  db.rawQuery(query, null);
        float amount = 0f;
        if(res.moveToFirst()){
            do{
                amount = amount + res.getFloat(res.getColumnIndex(TOTAL_AMOUNT));
            }while (res.moveToNext());

        }

        return amount;
    }

    public float getTotalTaxesShopCart(){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select sum(("+PROD_CGST+" + "+PROD_SGST+")*"+TOTAL_QTY+") as totalAmount from "+SHOP_CART_TABLE;
        Cursor res =  db.rawQuery(query, null);
        float amount = 0f;
        if(res.moveToFirst()){
            do{
                amount = amount + res.getFloat(res.getColumnIndex(TOTAL_AMOUNT));
            }while (res.moveToNext());

        }

        return amount;
    }

    public float getTaxesCart(String taxType){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "";
        if(taxType.equals("cgst")){
            query="select sum("+PROD_CGST+"*"+TOTAL_QTY+") as totalAmount from "+CART_TABLE;
        }else if(taxType.equals("sgst")){
            query="select sum("+PROD_SGST+"*"+TOTAL_QTY+") as totalAmount from "+CART_TABLE;
        }else{
            query="select sum("+PROD_IGST+"*"+TOTAL_QTY+") as totalAmount from "+CART_TABLE;
        }
        Cursor res =  db.rawQuery(query, null);
        float amount = 0f;
        if(res.moveToFirst()){
            do{
                amount = amount + res.getFloat(res.getColumnIndex(TOTAL_AMOUNT));
            }while (res.moveToNext());

        }

        return amount;
    }

    public float getTaxesShopCart(String taxType){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "";
        if(taxType.equals("cgst")){
            query="select sum("+PROD_CGST+"*"+TOTAL_QTY+") as totalAmount from "+SHOP_CART_TABLE;
        }else if(taxType.equals("sgst")){
            query="select sum("+PROD_SGST+"*"+TOTAL_QTY+") as totalAmount from "+SHOP_CART_TABLE;
        }else{
            query="select sum("+PROD_IGST+"*"+TOTAL_QTY+") as totalAmount from "+SHOP_CART_TABLE;
        }
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

    public int getTotalQuantityCart(int prodId){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select "+TOTAL_QTY+" as totalQty from "+CART_TABLE+" where "+ID+" = ? and "+PROD_SP+" != ?";
        Cursor res =  db.rawQuery(query, new String[]{String.valueOf(prodId),String.valueOf(0)});
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


    public List<ProductComboOffer> getProductPriceOffer(){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select * from "+PROD_PRICE_TABLE;
        final String detailQuery="select * from "+PROD_PRICE_DETAIL_TABLE+" where "+OFFER_ID+" IN(?)";
        Cursor res =  db.rawQuery(query, null);
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

    public List<ProductComboOffer> getProductComboOffer(){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select * from "+PROD_COMBO_TABLE;
        final String detailQuery="select * from "+PROD_COMBO_DETAIL_TABLE+" where "+OFFER_ID+" IN(?)";
        Cursor res =  db.rawQuery(query, null);
        Cursor detailsRes =  null;
        ArrayList<ProductComboOffer> itemList=new ArrayList<>();
        ArrayList<ProductComboDetails> productComboOfferDetails=null;
        ProductComboDetails productComboDetails = null;
        ProductComboOffer productComboOffer = null;
        if(res.moveToFirst()){
            do{
                productComboOffer = new ProductComboOffer();
                productComboOffer.setId(res.getInt(res.getColumnIndex(ID)));
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
                        productComboDetails.setPcodProdId(detailsRes.getInt(detailsRes.getColumnIndex(PROD_ID)));
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

    public Coupon getCouponOffer(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select * from "+COUPON_TABLE +" where "+ID+" = ?";
        Cursor res =  db.rawQuery(query, new String[]{id});
        Coupon item = null;
        if(res.moveToFirst()){
            item = new Coupon();
            item.setId(res.getInt(res.getColumnIndex(ID)));
            item.setName(res.getString(res.getColumnIndex(NAME)));
            item.setAmount(res.getFloat(res.getColumnIndex(COUPON_MAX_AMOUNT)));
            item.setPercentage(res.getFloat(res.getColumnIndex(COUPON_PER)));
            item.setStatus(res.getString(res.getColumnIndex(STATUS)));
            item.setStartDate(res.getString(res.getColumnIndex(START_DATE)));
            item.setEndDate(res.getString(res.getColumnIndex(END_DATE)));
        }

        return item;
    }

    public List<Coupon> getCouponOffer(){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select * from "+COUPON_TABLE;
        Cursor res =  db.rawQuery(query,null);
        List<Coupon> itemList = new ArrayList<>();
        Coupon item = null;
        if(res.moveToFirst()){
            do{
                item = new Coupon();
                item.setId(res.getInt(res.getColumnIndex(ID)));
                item.setName(res.getString(res.getColumnIndex(NAME)));
                item.setAmount(res.getFloat(res.getColumnIndex(COUPON_MAX_AMOUNT)));
                item.setPercentage(res.getFloat(res.getColumnIndex(COUPON_PER)));
                item.setStatus(res.getString(res.getColumnIndex(STATUS)));
                item.setStartDate(res.getString(res.getColumnIndex(START_DATE)));
                item.setEndDate(res.getString(res.getColumnIndex(END_DATE)));
                itemList.add(item);
            }while (res.moveToNext());

        }

        return itemList;
    }


    public List<ProductComboOffer> getCartProductPriceOffer(){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select * from "+CART_PROD_PRICE_TABLE;
        final String detailQuery="select * from "+CART_PROD_PRICE_DETAIL_TABLE+" where "+OFFER_ID+" IN(?)";
        Cursor res =  db.rawQuery(query, null);
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

    public List<ProductComboOffer> getCartProductPriceOffer(String prodId){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select * from "+CART_PROD_PRICE_TABLE+" where "+PROD_ID+" IN(?)";
        final String detailQuery="select * from "+CART_PROD_PRICE_DETAIL_TABLE+" where "+OFFER_ID+" IN(?)";
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

    public List<ProductComboOffer> getCartProductComboOffer(){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select * from "+CART_PROD_COMBO_TABLE;
        final String detailQuery="select * from "+CART_PROD_COMBO_DETAIL_TABLE+" where "+OFFER_ID+" IN(?)";
        Cursor res =  db.rawQuery(query, null);
        Cursor detailsRes =  null;
        ArrayList<ProductComboOffer> itemList=new ArrayList<>();
        ArrayList<ProductComboDetails> productComboOfferDetails=null;
        ProductComboDetails productComboDetails = null;
        ProductComboOffer productComboOffer = null;
        if(res.moveToFirst()){
            do{
                productComboOffer = new ProductComboOffer();
                productComboOffer.setId(res.getInt(res.getColumnIndex(ID)));
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
                        productComboDetails.setPcodProdId(detailsRes.getInt(detailsRes.getColumnIndex(PROD_ID)));
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

    public List<Object> getCartProductFreeOffer(){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select * from "+CART_PROD_FREE_OFFER_TABLE;
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


    public List<ProductDiscountOffer> getCartProductFreeOffer(String prodId){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select * from "+CART_PROD_FREE_OFFER_TABLE +" where "+PROD_BUY_ID+" = ?";
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

    public Coupon getCartCouponOffer(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select * from "+CART_COUPON_TABLE +" where "+ID+" = ?";
        Cursor res =  db.rawQuery(query, new String[]{id});
        Coupon item = null;
        if(res.moveToFirst()){
            item = new Coupon();
            item.setId(res.getInt(res.getColumnIndex(ID)));
            item.setName(res.getString(res.getColumnIndex(NAME)));
            item.setAmount(res.getFloat(res.getColumnIndex(COUPON_MAX_AMOUNT)));
            item.setPercentage(res.getFloat(res.getColumnIndex(COUPON_PER)));
            item.setStatus(res.getString(res.getColumnIndex(STATUS)));
            item.setStartDate(res.getString(res.getColumnIndex(START_DATE)));
            item.setEndDate(res.getString(res.getColumnIndex(END_DATE)));
        }

        return item;
    }

    public List<Coupon> getCartCouponOffer(){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select * from "+CART_COUPON_TABLE;
        Cursor res =  db.rawQuery(query,null);
        List<Coupon> itemList = new ArrayList<>();
        Coupon item = null;
        if(res.moveToFirst()){
            do{
                item = new Coupon();
                item.setId(res.getInt(res.getColumnIndex(ID)));
                item.setName(res.getString(res.getColumnIndex(NAME)));
                item.setAmount(res.getFloat(res.getColumnIndex(COUPON_MAX_AMOUNT)));
                item.setPercentage(res.getFloat(res.getColumnIndex(COUPON_PER)));
                item.setStatus(res.getString(res.getColumnIndex(STATUS)));
                item.setStartDate(res.getString(res.getColumnIndex(START_DATE)));
                item.setEndDate(res.getString(res.getColumnIndex(END_DATE)));
                itemList.add(item);
            }while (res.moveToNext());

        }

        return itemList;
    }

    public List<Object> getCustomerList(String isFav,int limit,int offset){
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
                myCustomer.setLocality(res.getString(res.getColumnIndex(LOCALITY)));
                myCustomer.setLatitude(res.getString(res.getColumnIndex(LATITUDE)));
                myCustomer.setLongitude(res.getString(res.getColumnIndex(LONGITUDE)));
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

    public List<Object> getFavCustomerList(String isFav,int limit,int offset){
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
                myCustomer.setLocality(res.getString(res.getColumnIndex(LOCALITY)));
                myCustomer.setLatitude(res.getString(res.getColumnIndex(LATITUDE)));
                myCustomer.setLongitude(res.getString(res.getColumnIndex(LONGITUDE)));
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

    // Getting contacts Count
    public int getCartCount() {
        String countQuery = "SELECT  * FROM " + SHOP_CART_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        // return count
        return count;
    }

    public boolean checkCustomerExistInCart(String code){
        SQLiteDatabase db = this.getReadableDatabase();
        final String query="select * from "+CUSTOMER_INFO_TABLE+" WHERE "+CODE+" = ?";
        Cursor res =  db.rawQuery(query, new String[]{code});
        if(res.getCount() > 0){
            return true;
        }else{
            return false;
        }
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

    public boolean updateProductFreeOffer(ProductDiscountOffer item, String updatedAt){
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
        contentValues.put(UPDATED_AT, updatedAt);
        db.update(PROD_FREE_OFFER_TABLE,contentValues,ID+" = ?",
                new String[]{String.valueOf(item.getId())});
        Log.i("DbHelper","Row is updated");
        return true;
    }


    public boolean updateProductPriceOffer(ProductComboOffer item, String updatedAt){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //contentValues.put(ID, item.getId());
        contentValues.put(PROD_ID, item.getProdId());
        contentValues.put(NAME, item.getOfferName());
        contentValues.put(START_DATE, item.getStartDate());
        contentValues.put(END_DATE, item.getEndDate());
        contentValues.put(STATUS, item.getStatus());
        contentValues.put(UPDATED_AT, updatedAt);
        db.update(PROD_PRICE_TABLE,contentValues,ID+" = ?",
                new String[]{String.valueOf(item.getId())});
        Log.i("DbHelper","Row is updated");
        return true;
    }

    public boolean updateProductPriceDetailOffer(ProductComboDetails item, String updatedAt){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(OFFER_ID, item.getPcodPcoId());
        contentValues.put(TOTAL_QTY, item.getPcodProdQty());
        contentValues.put(PROD_SP, item.getPcodPrice());
        contentValues.put(STATUS, item.getStatus());
        contentValues.put(UPDATED_AT, updatedAt);
        db.update(PROD_PRICE_DETAIL_TABLE,contentValues,ID+" = ?",
                new String[]{String.valueOf(item.getId())});
        Log.i("DbHelper","Row is updated");
        return true;
    }

    public boolean updateProductComboOffer(ProductComboOffer item, String updatedAt){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, item.getOfferName());
        contentValues.put(START_DATE, item.getStartDate());
        contentValues.put(END_DATE, item.getEndDate());
        contentValues.put(STATUS, item.getStatus());
        contentValues.put(UPDATED_AT, updatedAt);
        db.update(PROD_COMBO_TABLE,contentValues,ID+" = ?",
                new String[]{String.valueOf(item.getId())});
        Log.i("DbHelper","Row is updated");
        return true;
    }

    public boolean updateProductComboDetailOffer(ProductComboDetails item, String updatedAt){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PROD_ID, item.getPcodProdId());
        contentValues.put(TOTAL_QTY, item.getPcodProdQty());
        contentValues.put(PROD_SP, item.getPcodPrice());
        contentValues.put(STATUS, item.getStatus());
        contentValues.put(UPDATED_AT, updatedAt);
        db.update(PROD_COMBO_DETAIL_TABLE,contentValues,ID+" = ?",
                new String[]{String.valueOf(item.getId())});
        Log.i("DbHelper","Row is updated");
        return true;
    }

    public void updateFreeCartData(int id,int qty,float totalAmount){
        SQLiteDatabase db = this.getReadableDatabase();
        // query="UPDATE "+PRODUCT_TABLE+" SET "+PROD_QOH+" = ? where "+PROD_CODE+" = ?";
        ContentValues contentValues = new ContentValues();
        contentValues.put(PROD_CGST, 0);
        contentValues.put(PROD_IGST, 0);
        contentValues.put(PROD_SGST, 0);
        contentValues.put(TOTAL_QTY, qty);
        contentValues.put(TOTAL_AMOUNT, totalAmount);
        db.update(CART_TABLE,contentValues,ID+" = ? AND "+PROD_SP+" == ?",
                new String[]{String.valueOf(id),String.valueOf(0f)});
      //  db.update(CART_TABLE,contentValues,PROD_BARCODE+" = ? AND "+PROD_SP+" == ?",
      //          new String[]{String.valueOf(id),String.valueOf(0f)});

    }

    public void updateFreeShopCartData(int id,int qty,float totalAmount){
        SQLiteDatabase db = this.getReadableDatabase();
        // query="UPDATE "+PRODUCT_TABLE+" SET "+PROD_QOH+" = ? where "+PROD_CODE+" = ?";
        ContentValues contentValues = new ContentValues();
        contentValues.put(PROD_CGST, 0);
        contentValues.put(PROD_IGST, 0);
        contentValues.put(PROD_SGST, 0);
        contentValues.put(TOTAL_QTY, qty);
        contentValues.put(TOTAL_AMOUNT, totalAmount);
        db.update(SHOP_CART_TABLE,contentValues,ID+" = ? AND "+PROD_SP+" == ?",
                new String[]{String.valueOf(id),String.valueOf(0f)});
        //  db.update(CART_TABLE,contentValues,PROD_BARCODE+" = ? AND "+PROD_SP+" == ?",
        //          new String[]{String.valueOf(id),String.valueOf(0f)});

    }

    public void updateCartData(MyProductItem item,int qty,float totalAmount){
        SQLiteDatabase db = this.getReadableDatabase();
        // query="UPDATE "+PRODUCT_TABLE+" SET "+PROD_QOH+" = ? where "+PROD_CODE+" = ?";
        ContentValues contentValues = new ContentValues();
       /* if(item.getComboProductIds() != null){
            contentValues.put(PROD_CGST, item.getProdCgst());
            contentValues.put(PROD_IGST, (item.getProdIgst() * totalAmount) /100);
            contentValues.put(PROD_SGST, (item.getProdSgst() * totalAmount) /100);
        }else{
            contentValues.put(PROD_CGST, (item.getProdCgst() * totalAmount) /100);
            contentValues.put(PROD_IGST, (item.getProdIgst() * totalAmount) /100);
            contentValues.put(PROD_SGST, (item.getProdSgst() * totalAmount) /100);
        }*/

        Log.i("dbhelper","cgst "+item.getProdCgst());
        Log.i("dbhelper","sgst "+item.getProdSgst());
        contentValues.put(TOTAL_QTY, qty);
        contentValues.put(TOTAL_AMOUNT, totalAmount);
        contentValues.put(PROD_SP, item.getProdSp());

        float rate = ((item.getProdSp() * (item.getProdCgst()+item.getProdSgst()))/(100 +
                (item.getProdCgst()+item.getProdSgst())));
        Log.d("Rate ", ""+rate);
        contentValues.put(PROD_CGST, rate/2);
        contentValues.put(PROD_IGST, rate);
        contentValues.put(PROD_SGST, rate/2);

        db.update(CART_TABLE,contentValues,ID+" = ? AND "+PROD_SP+" != ?",
                new String[]{String.valueOf(item.getProdId()),String.valueOf(0f)});
        db.update(CART_TABLE,contentValues,PROD_BARCODE+" = ? AND "+PROD_SP+" != ?",
                new String[]{item.getProdBarCode(),String.valueOf(0f)});

    }

    public void updateCartData(MyProductItem item,float totalAmount){
        SQLiteDatabase db = this.getReadableDatabase();
        // query="UPDATE "+PRODUCT_TABLE+" SET "+PROD_QOH+" = ? where "+PROD_CODE+" = ?";
        ContentValues contentValues = new ContentValues();
        Log.i("dbhelper","cgst "+item.getProdCgst());
        Log.i("dbhelper","sgst "+item.getProdSgst());
        Log.i("dbhelper","sp "+item.getProdSp());
        contentValues.put(PROD_SP, item.getProdSp());
        contentValues.put(TOTAL_AMOUNT, totalAmount);

        float rate = ((item.getProdSp() * (item.getProdCgst()+item.getProdSgst()))/(100 +
                (item.getProdCgst()+item.getProdSgst())));
        Log.d("Rate ", ""+rate);
        contentValues.put(PROD_CGST, rate/2);
        contentValues.put(PROD_IGST, rate);
        contentValues.put(PROD_SGST, rate/2);

        db.update(CART_TABLE,contentValues,ID+" = ? AND "+PROD_SP+" != ?",
                new String[]{String.valueOf(item.getProdId()),String.valueOf(0f)});

    }

    public void updateShopCartData(MyProductItem item){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TOTAL_QTY, item.getQty());
        contentValues.put(TOTAL_AMOUNT, item.getTotalAmount());
        contentValues.put(PROD_SP, item.getProdSp());

        float rate = ((item.getProdSp() * (item.getProdCgst()+item.getProdSgst()))/(100 +
                (item.getProdCgst()+item.getProdSgst())));
        Log.d("Rate ", ""+rate);
        contentValues.put(PROD_CGST, rate/2);
        contentValues.put(PROD_IGST, rate);
        contentValues.put(PROD_SGST, rate/2);

        db.update(SHOP_CART_TABLE, contentValues, ID + " = ?" + " AND " + PROD_SP+" != ?",
                new String[] { String.valueOf(item.getProdId()), String.valueOf(0)});
    }

    public void updateOfferCounterCartData(int offerCounter,int prodId){
        SQLiteDatabase db = this.getReadableDatabase();
        // query="UPDATE "+PRODUCT_TABLE+" SET "+PROD_QOH+" = ? where "+PROD_CODE+" = ?";
        ContentValues contentValues = new ContentValues();
        contentValues.put(OFFER_ITEM_COUNTER, offerCounter);
        db.update(CART_TABLE,contentValues,ID+" = ? AND "+PROD_SP+" != ?",
                new String[]{String.valueOf(prodId),String.valueOf(0f)});

    }

    public void updateOfferCounterShopCartData(int offerCounter,int prodId){
        SQLiteDatabase db = this.getReadableDatabase();
        // query="UPDATE "+PRODUCT_TABLE+" SET "+PROD_QOH+" = ? where "+PROD_CODE+" = ?";
        ContentValues contentValues = new ContentValues();
        contentValues.put(OFFER_ITEM_COUNTER, offerCounter);
        db.update(SHOP_CART_TABLE,contentValues,ID+" = ? AND "+PROD_SP+" != ?",
                new String[]{String.valueOf(prodId),String.valueOf(0f)});

    }

    public void updateFreePositionCartData(int position,int prodId){
        SQLiteDatabase db = this.getReadableDatabase();
        // query="UPDATE "+PRODUCT_TABLE+" SET "+PROD_QOH+" = ? where "+PROD_CODE+" = ?";
        ContentValues contentValues = new ContentValues();
        contentValues.put(FREE_PRODUCT_POSITION, position);
        db.update(CART_TABLE,contentValues,ID+" = ? AND "+PROD_SP+" != ?",
                new String[]{String.valueOf(prodId),String.valueOf(0f)});

    }

    public void updateFreePositionShopCartData(int position,int prodId){
        SQLiteDatabase db = this.getReadableDatabase();
        // query="UPDATE "+PRODUCT_TABLE+" SET "+PROD_QOH+" = ? where "+PROD_CODE+" = ?";
        ContentValues contentValues = new ContentValues();
        contentValues.put(FREE_PRODUCT_POSITION, position);
        db.update(SHOP_CART_TABLE,contentValues,ID+" = ? AND "+PROD_SP+" != ?",
                new String[]{String.valueOf(prodId),String.valueOf(0f)});

    }

    public void updateFavStatus(String code,String status){
        SQLiteDatabase db = this.getReadableDatabase();
        // query="UPDATE "+PRODUCT_TABLE+" SET "+PROD_QOH+" = ? where "+PROD_CODE+" = ?";
        ContentValues contentValues = new ContentValues();
        contentValues.put(IS_FAV, status);
        db.update(CUSTOMER_INFO_TABLE,contentValues,CODE+" = ?",
                new String[]{code});

        Log.i("dbhelper","status updated "+status+" code "+code);

    }

    public boolean removeProductFromCart(String prodBarCode){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CART_TABLE, PROD_BARCODE+" = ? AND "+PROD_SP+" != ?",new String[]{prodBarCode,String.valueOf(0f)});
        return true;
    }

    public boolean removeProductFromCart(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CART_TABLE, ID+" = ? AND "+PROD_SP+" != ?",new String[]{String.valueOf(id),String.valueOf(0f)});
        return true;
    }

    public boolean removeProductFromShopCart(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(SHOP_CART_TABLE, ID+" = ? AND "+PROD_SP+" != ?",new String[]{String.valueOf(id),String.valueOf(0f)});
        return true;
    }

    public boolean removePriceProductFromCart(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        long val = db.delete(CART_PROD_PRICE_TABLE, PROD_ID+" = ?" ,new String[]{String.valueOf(id)});
        return true;
    }

    public boolean removePriceProductDetailsFromCart(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        long val = db.delete(CART_PROD_PRICE_DETAIL_TABLE, ID+" = ?",new String[]{String.valueOf(id)});
        return true;
    }

    public boolean removeFreeProductFromCart(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CART_TABLE, ID+" = ? AND "+PROD_SP+" = ?",new String[]{String.valueOf(id),String.valueOf(0f)});
        return true;
    }

    public boolean removeFreeProductFromShopCart(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(SHOP_CART_TABLE, ID+" = ? AND "+PROD_SP+" = ?",new String[]{String.valueOf(id),String.valueOf(0f)});
        return true;
    }

    public boolean removeUnit(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(PRODUCT_UNIT_TABLE, ID+" = ?",new String[]{String.valueOf(id)});
        return true;
    }

    public boolean removeSize(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(PRODUCT_SIZE_TABLE, ID+" = ?",new String[]{String.valueOf(id)});
        db.delete(PRODUCT_COLOR_TABLE, SIZE_ID+" = ?",new String[]{String.valueOf(id)});
        return true;
    }

    public boolean removeColor(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(PRODUCT_COLOR_TABLE, ID+" = ?",new String[]{String.valueOf(id)});
        return true;
    }

    public boolean removeCartUnit(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CART_PRODUCT_UNIT_TABLE, ID+" = ?",new String[]{String.valueOf(id)});
        return true;
    }

    public boolean removeCartSize(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CART_PRODUCT_SIZE_TABLE, ID+" = ?",new String[]{String.valueOf(id)});
        db.delete(CART_PRODUCT_COLOR_TABLE, SIZE_ID+" = ?",new String[]{String.valueOf(id)});
        return true;
    }

    public boolean removeCartColor(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CART_PRODUCT_COLOR_TABLE, ID+" = ?",new String[]{String.valueOf(id)});
        return true;
    }

    public boolean deleteTable(String tableName){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tableName, null, null);
        return true;
    }

    public void deleteAllTable(){
        deleteTable(DbHelper.CAT_TABLE);
        deleteTable(DbHelper.SUB_CAT_TABLE);
        deleteTable(DbHelper.PRODUCT_TABLE);
        deleteTable(DbHelper.PRODUCT_BARCODE_TABLE);
        deleteTable(DbHelper.PRODUCT_UNIT_TABLE);
        deleteTable(DbHelper.PRODUCT_SIZE_TABLE);
        deleteTable(DbHelper.PRODUCT_COLOR_TABLE);
        deleteTable(DbHelper.CART_TABLE);
        deleteTable(DbHelper.PROD_COMBO_TABLE);
        deleteTable(DbHelper.PROD_COMBO_DETAIL_TABLE);
        deleteTable(DbHelper.PROD_PRICE_TABLE);
        deleteTable(DbHelper.PROD_PRICE_DETAIL_TABLE);
        deleteTable(DbHelper.PROD_FREE_OFFER_TABLE);
        deleteTable(DbHelper.COUPON_TABLE);
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
