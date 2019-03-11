package com.shoppursshop.utilities;

/**
 * Created by Shweta on 8/10/2016.
 */
public class Constants {

    public static String APP_NAME="shoppurs_shop";

    public static String MYPREFERENCEKEY="com."+APP_NAME+".MyPrefs";
    public static String USER_ID="userID";
    public static String IMEI_NO="imeiNo";
    public static String USER_TYPE_ID="user_type_id";
    public static String USER_TYPE="user_type";
    public static String EMAIL="email";
    public static String PASSWORD="password";
    public static String MOBILE_NO="mobileNo";
    public static String SHOP_NAME="shopName";
    public static String SHOP_CODE="shopCode";
    public static String LANGUAGE="language";
    public static String CITY="city";
    public static String STATE="state";
    public static String COUNTRY="country";
    public static String ZIP="zip";
    public static String PHOTO="photo";
    public static String USER_LAT="userLat";
    public static String USER_LONG="userLong";
    public static String PAN_NO="panNo";
    public static String AADHAR_NO="aadharNo";
    public static String GST_NO="gstNo";
    public static String ADDRESS="address";
    public static String DB_NAME="dbName";
    public static String DB_USER_NAME="dbUserName";
    public static String DB_PASSWORD="dbPassword";
   // public static String DOB="dob";
    public static String LOCATION="location";
    public static String FULL_NAME="fullName";
    public static String USERNAME="username";
    public static String ROLE="role";
    public static String ACTIVATE_KEY="activate_key";
    public static String GUID="guid";
    public static String TOKEN="token";
    public static String CREATED="created";
    public static String MODIFIED="modified";
    public static String FORGOT_PASSWORD_REQUEST_TIME="forgot_password_request_time";
    public static String LAST_NAME="lastName";
    public static String IS_LOGGED_IN="isLoggedIn";
    public static String IS_DATABASE_CREATED="isDatabaseCreated";
    public static String VEHICLE_TYPE="vehicleType";
    public static String COLOR_THEME="colorTheme";
    public static String IS_DARK_THEME="isDarkTheme";
    public static String IS_BANK_DETAIL_ADDED="isBankDetailsAdded";
    public static String IS_CAT_ADDED="isCatAdded";
    public static String IS_SUB_CAT_ADDED="isSubCatAdded";
    public static String IS_PRODUCT_ADDED="isProductAdded";

    public static String FCM_TOKEN="fcmToken";
    public static String IS_TOKEN_SAVED="isTokenSaved";
    public static String DEVICE_ID="deviceId";
    public static String NOTIFICATION_COUNTER="notificationCounter";

    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;
    public static final String PACKAGE_NAME =
            "com."+APP_NAME;
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME +
            ".RESULT_DATA_KEY";
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME +
            ".LOCATION_DATA_EXTRA";
    public static final String STATUS = PACKAGE_NAME +
            ".STATUS";


    //api
    public static String LOGIN="/api/user/loginRetailer";
    public static String SYNC="/api/syncdata";
    public static String MANAGE_REGISTRATION="/api/user/manageRegistration";
    public static String GET_COUNTRIES="/api/countries";
    public static String GET_STATES="/api/states?countryId=";
    public static String GET_CITIES="/api/cities?stateId=";
    public static String UPDATE_BANK_DETAILS="/api/user/updateBankDetails";
    public static String GET_CATEGORY="/api/categories/categories";
    public static String CREATE_CATEGORY="/api/categories/addCategoryRetailer";
    public static String GET_SUB_CATEGORY="/api/subcategories?catIds=";
    public static String CREATE_SUB_CATEGORY="/api/categories/addSubCategoryRetailer";
    public static String GET_PRODUCTS="/api/products/productslist";
    public static String CREATE_PRODUCTS="/api/products/addProduct";
}
