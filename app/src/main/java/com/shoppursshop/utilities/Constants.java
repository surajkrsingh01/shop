package com.shoppursshop.utilities;

/**
 * Created by Shweta on 8/10/2016.
 */
public class Constants {

    public static String APP_NAME="shoppurs_shop";

    public static String MYPREFERENCEKEY="com."+APP_NAME+".MyPrefs";
    public static String USER_ID="userID";
    public static String MERCHANT_ID="merchantId";
    public static String PARTNER_ID="partnerId";
    public static String IMEI_NO="imeiNo";
    public static String USER_TYPE_ID="user_type_id";
    public static String USER_TYPE="user_type";
    public static String EMAIL="email";
    public static String PASSWORD="password";
    public static String PROFILE_PIC="profilePic";
    public static String PROFILE_PIC_LOCAL="profilePicLocal";
    public static String MOBILE_NO="mobileNo";
    public static String SHOP_NAME="shopName";
    public static String SHOP_CODE="shopCode";
    public static String LANGUAGE="language";
    public static String CITY="city";
    public static String STATE="state";
    public static String COUNTRY="country";
    public static String ZIP="zip";
    public static String LATITUDE="latitude";
    public static String LONGITUDE="longitude";
    public static String PHOTO="photo";
    public static String USER_LAT="userLat";
    public static String USER_LONG="userLong";
    public static String PAN_NO="panNo";
    public static String AADHAR_NO="aadharNo";
    public static String GST_NO="gstNo";
    public static String ADDRESS="address";
    public static String IS_DELIVERY_AVAILABLE="isDeliveryAvailable";
    public static String MIN_DELIVERY_AMOUNT="minDeliveryAmount";
    public static String MIN_DELIVERY_DISTANCE="minDeliveryDistance";
    public static String DELIVERY_EST_TIME="deliveryEstTime";
    public static String CHARGE_AFTER_MIN_DISTANCE="chargeAfterMinDistance";
    public static String DB_NAME="dbName";
    public static String DB_USER_NAME="dbUserName";
    public static String DB_PASSWORD="dbPassword";
    public static String DB_VERSION="dbVersion";
   // public static String DOB="dob";
    public static String LOCATION="location";
    public static String FULL_NAME="fullName";
    public static String USERNAME="username";
    public static String ROLE="role";
    public static String GOOGLE_MAP_API_KEY="googleMapApiKey";
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
    public static String ACCOUNT_NO="accountNo";
    public static String BANK_NAME="bankName";
    public static String IFSC_CODE="ifscCode";
    public static String BRANCH_ADRESS="branchAddress";
    public static String CHEQUE_IMAGE="chequeImage";
    public static String CHEQUE_IMAGE_LOCAL="chequeImageLocal";
    public static String KYC_NAME="kycName";
    public static String KYC_DOC_NUMBER="kycDocNumber";
    public static String KYC_DOC_TYPE="kycDocType";
    public static String KYC_DOC_IMAGE="kycDocImage";
    public static String IS_SUB_CAT_ADDED="isSubCatAdded";
    public static String IS_PRODUCT_ADDED="isProductAdded";
    public static String MERCHANT_REF_NO="merchantRefNo";
    public static String SYNCED_DATE="syncedDate";

    public static String DEVICE_SER_NO="deviceSerNum";
    public static String DEVICE_MODEL="deviceModel";

    public static String FCM_TOKEN="fcmToken";
    public static String IS_TOKEN_SAVED="isTokenSaved";
    public static String DEVICE_ID="deviceId";
    public static String NOTIFICATION_COUNTER="notificationCounter";
    public static String INIT_DATA_LOADED="initDataLoaded";

    public static String STORE_OPEN_STATUS="storeOpenStatus";
    public static String STORE_CLOSE_DATE="storeCloseDate";

    public static String ANDROID_DEVICE_TYPE="androidDeviceType";

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
    public static String GET_INIT_DATA="/useradmin/init_data";
    public static String LOGIN="/useradmin/loginRetailer";
    public static String AUTHENTICATE="/api/user/authenticate_user";
    public static String GET_PASSWORD="/useradmin/getPassword";
    public static String VALIDATE_OTP="/useradmin/validate_otp";
    public static String SAVE_OTP="/useradmin/save_otp";
    public static String RESET_PASSWORD="/useradmin/shop/reset_password";
    public static String SYNC="/api/syncdata";
    public static String SYNC_SHOP_DATA="/api/sync_shop_data";
    public static String MANAGE_REGISTRATION="/useradmin/manageRegistration";
    public static String GET_COUNTRIES="/api/countries";
    public static String GET_STATES="/api/states?countryId=";
    public static String GET_CITIES="/api/cities?stateId=";
    public static String UPDATE_BANK_DETAILS="/api/user/updateBankDetails";
    public static String GET_CATEGORY="/api/categories/categories";
    public static String CREATE_CATEGORY="/api/categories/addCategoryRetailer";
    public static String DELETE_CATEGORY="/api/categories/delete";
    public static String GET_SUB_CATEGORY="/api/categories/subcategories";
    public static String DELETE_SUB_CATEGORY="/api/categories/sub_categories/delete";
    public static String CREATE_SUB_CATEGORY="/api/categories/addSubCategoryRetailer";
    public static String GET_SYNCED_PRODUCTS="/api/products/syncproductslist";
    public static String GET_PRODUCTS="/api/products/productslist";
    public static String GET_PRODUCTS_WITH_BAR="/api/products/productslistbar";
    public static String CREATE_PRODUCTS="/api/products/addProduct";
    public static String DELETE_PRODUCTS="/api/products/delete";
    public static String MANAGE_ORDERS="/api/order/manageShoppursOrder";
    public static String CUSTOMER_LIST="/api/shop_customer/customers";
    public static String REFRESH_CUSTOMER_LIST="/api/shop_customer/refresh_customers";
    public static String REVIEW_LIST="/api/product_review/getReview";
    public static String PRODUCT_SALE_DATA="/api/products/product_sale_data";
    public static String PRODUCT_RATINGS_DATA="/api/products/product_ratings";
    public static String PRODUCT_REVIEWS_DATA="/api/products/getReview";
    public static String SHOP_SALE_DATE="/api/user/shop_sale_data";
    public static String IS_CUSTOMER_REGISTERED="/api/shop_customer/is_registered";
    public static String REGISTER_CUSTOMER="/api/shop_customer/register_shop_customer";
    public static String REGISTER_USER="/api/user/add_user";
    public static String GET_USER="/api/user/get_users";
    public static String CHANGE_USER_STATUS="/api/user/update_user_status";
    public static String ORDER_PRODUCTS="/api/shop/order/productslist";
    public static String GET_SHOP_ORDERS="/api/shop/order/get_shop_order";
    public static String GET_CUSTOMER_ORDERS="/api/shop/order/get_shop_cust_order";
    public static String GET_PENDING_ORDERS="/api/shop/order/get_pending_order";
    public static String ACCEPT_ORDER="/api/shop/order/accept_order";
    public static String CANCEL_ORDER="/api/shop/order/cancel_order";
    public static String ORDER_DELIVERED="/api/shop/order/order_delivered";
    public static String CUST_SET_FAV_STATUS="/api/shop_customer/favourite";
    public static String CUST_SET_RATINGS="/api/shop_customer/ratings";
    public static String CUST_GET_RATINGS="/api/shop_customer/get_ratings";
    public static String CUST_SALE_DATA="/api/shop_customer/customer_sale_data";
    public static String CUST_PRE_ORDER="/api/shop_customer/customer_pre_orders";
    public static String ADD_BAR_CODE="/api/products/add_product_barcode";
    public static String GET_BAR_CODE="/api/products/products_barcode_list";
    public static String SEARCH_CUSTOMER="/api/search/shop_customers";
    public static String UPDATE_BASIC_PROFILE="/api/shop/profile/update_basic_details";
    public static String UPDATE_PROFILE_IMAGE="/api/shop/profile/update_profile_image";
    public static String UPDATE_ADDRESS="/api/shop/profile/update_address";
    public static String UPDATE_DELIVERY_STAUS="/api/shop/profile/update_delivery_status";
    public static String GET_INVOICE="/api/trans/get_invoice";
    public static String GET_INVOICE_BY_INV_NO="/api/trans/get_invoice_inv";

    public static String GENERATE_ORDER="/api/shop/order/generate_order";
    public static String PLACE_ORDER="/api/shop/order/place_order";
    public static String ADD_TRANS_DATA="/api/trans/add_trans_data";
    public static String ADD_INVOICE_DATA="/api/trans/generate_invoice";
    public static String GENERATE_QR_CODE="/api/shop/profile/generate_qrcode";

    public static String GET_USER_LICENSE="/api/user/user_licenses";
    public static String BUY_USER_LICENSE="/api/user/buy_user_license";
    public static String GET_USER_LICENSE_ORDER="/api/shop/order/generate_user_licence_order";

    public static String GET_ALL_PRODUCT_OFFER="/api/offers/get_offer_list";
    public static String GET_ACTIVE_PRODUCT_OFFER="/api/offers/get_active_offer_list";
    public static String CREATE_FREE_PRODUCT_OFFER="/api/offers/create_product_discount_offer";
    public static String UPDATE_FREE_PRODUCT_OFFER="/api/offers/update_product_discount_offer";
    public static String CREATE_COMBO_PRODUCT_OFFER="/api/offers/create_product_combo_offer";
    public static String UPDATE_COMBO_PRODUCT_OFFER="/api/offers/update_product_combo_offer";
    public static String CREATE_PRODUCT_PRICE_OFFER = "/api/offers/create_product_price_offer";
    public static String UPDATE_PRODUCT_PRICE_OFFER = "/api/offers/update_product_price_offer";
    public static String CREATE_COUPON_OFFER="/api/offers/create_coupon_offer";
    public static String UPDATE_COUPON_OFFER="/api/offers/update_coupon_offer";
    public static String GET_COUPON_OFFER="/api/offers/get_coupon_offer";
    public static String VALIDATE_COUPON_OFFER="/api/offers/validate_coupon_offer";

    public static String CHANGE_FREE_PRODUCT_OFFER="/api/offers/status_product_discount_offer";
    public static String CHANGE_COMBO_PRODUCT_OFFER="/api/offers/status_product_combo_offer";
    public static String CHANGE_PRODUCT_PRICE_OFFER="/api/offers/status_product_price_offer";
    public static String CHANGE_COUPON_OFFER="/api/offers/status_coupon_offer";

    public static String GET_BANNER_OFFERS="/api/offers/get_banner_offers";
    public static String GET_CATEGORY_OFFERS="/api/offers/get_active_offer_categories_shop";
    public static String GET_PRODUCT_OFFERS="/api/customers/products/ret_productslist_with_offers";
    public static String GET_PRODUCT_OFFERS_BY_CAT="/api/customers/products/ret_productslist_with_offers_by_cat";

    public static String UPDATE_STOCK="/api/products/update_stock";
    public static String UPDATE_STATUS="/api/products/update_status";

    public static String GET_CHAT_USERS="/api/chat/get_chat_users";
    public static String SEND_MESSAGE="/api/chat/chat_for_support";
    public static String GET_MESSAGE="/api/chat/get_chat_for_support";

    public static String GENERATE_FREQUENCY_ORDER="/api/order/frequency_order";
    public static String FREQUENCY_CUSTOMER_LIST="/api/order/get_frequency_order_customer_list";
    public static String GET_FREQUENCY_PRODUCT_LIST="/api/order/get_frequency_order";

    public static String UPDATE_DB_VERSION="/api/db_version/handle_change_version";
    public static String RETURN_PRODUCT="/api/return/send_return_request";

}
