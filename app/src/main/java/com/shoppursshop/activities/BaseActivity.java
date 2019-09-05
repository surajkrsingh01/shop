package com.shoppursshop.activities;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.appbar.AppBarLayout;
import com.shoppursshop.R;
import com.shoppursshop.activities.payment.mPos.MPayTransactionDetailsActivity;
import com.shoppursshop.activities.settings.AddCategoryActivity;
import com.shoppursshop.activities.settings.AddSubCatActivity;
import com.shoppursshop.activities.settings.AddUserActivity;
import com.shoppursshop.activities.settings.ComboProductOfferActivity;
import com.shoppursshop.activities.settings.CreateCouponOfferActivity;
import com.shoppursshop.activities.settings.FreeProductOfferActivity;
import com.shoppursshop.activities.settings.ProductPriceOfferActivity;
import com.shoppursshop.activities.settings.SettingActivity;
import com.shoppursshop.activities.settings.SyncDataActivity;
import com.shoppursshop.activities.settings.SyncProductActivity;
import com.shoppursshop.activities.settings.profile.AddressActivity;
import com.shoppursshop.activities.settings.profile.BasicProfileActivity;
import com.shoppursshop.activities.settings.profile.DeliveryActivity;
import com.shoppursshop.database.DbHelper;
import com.shoppursshop.morphdialog.DialogActivity;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.Utility;

public class BaseActivity extends AppCompatActivity {

    protected  String  TAG = "Base";
    protected ProgressDialog progressDialog;
    protected SharedPreferences sharedPreferences;
    protected SharedPreferences.Editor editor;
    protected DbHelper dbHelper;
    protected boolean isDarkTheme;
    protected int colorTheme;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    protected int limit = 20,offset = 0;
    protected int smallLimit = 4,smallOffset = 0;
    protected int visibleItemCount,pastVisibleItems,totalItemCount;
    protected boolean loading=true,isScroll = true;

    protected String token,appName,appVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        sharedPreferences=getSharedPreferences(Constants.MYPREFERENCEKEY,MODE_PRIVATE);
        editor=sharedPreferences.edit();
        isDarkTheme = sharedPreferences.getBoolean(Constants.IS_DARK_THEME,false);
        colorTheme = sharedPreferences.getInt(Constants.COLOR_THEME,getResources().getColor(R.color.red_500));
        token = sharedPreferences.getString(Constants.TOKEN,"");
        appName = "Shoppurs";
        try {
            appVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            Log.i("About","Version "+appVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if(isDarkTheme){
            setTheme(R.style.Dark);
        }else{
            setTheme(R.style.Light);
        }

        setColorTheme();

        dbHelper=new DbHelper(this);
        progressDialog = new ProgressDialog(BaseActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        // Disable the back button
        DialogInterface.OnKeyListener keyListener = new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        };
        progressDialog.setOnKeyListener(keyListener);
    }

    @Override
    public void onResume(){
        super.onResume();
        colorTheme = sharedPreferences.getInt(Constants.COLOR_THEME,getResources().getColor(R.color.red_500));
        boolean isDarkTheme = sharedPreferences.getBoolean(Constants.IS_DARK_THEME,false);
        if(this.isDarkTheme != isDarkTheme)
            recreate();
    }

    public void setColorTheme(){
        if(colorTheme == getResources().getColor(R.color.red_500)){
            getTheme().applyStyle(R.style.MyRedViewTheme, true);
        }else if(colorTheme == getResources().getColor(R.color.blue500)){
            getTheme().applyStyle(R.style.MyBlueViewTheme, true);
        }else if(colorTheme == getResources().getColor(R.color.green500)){
            getTheme().applyStyle(R.style.MyGreenViewTheme, true);
        }else if(colorTheme == getResources().getColor(R.color.pink500)){
            getTheme().applyStyle(R.style.MyPinkViewTheme, true);
        }else if(colorTheme == getResources().getColor(R.color.yellow500)){
            getTheme().applyStyle(R.style.MyYellowViewTheme, true);
        }else if(colorTheme == getResources().getColor(R.color.amber600)){
            getTheme().applyStyle(R.style.MyAmberViewTheme, true);
        }else if(colorTheme == getResources().getColor(R.color.grey600)){
            getTheme().applyStyle(R.style.MyGreyViewTheme, true);
        }else if(colorTheme == getResources().getColor(R.color.white)){
            getTheme().applyStyle(R.style.MyWhiteViewTheme, true);
        }else if(colorTheme == getResources().getColor(R.color.black)){
            getTheme().applyStyle(R.style.MyBlackViewTheme, true);
        }else if(colorTheme == getResources().getColor(R.color.teal_500)){
            getTheme().applyStyle(R.style.MyTealViewTheme, true);
        }else if(colorTheme == getResources().getColor(R.color.purple500)){
            getTheme().applyStyle(R.style.MyPurpleViewTheme, true);
        }else if(colorTheme == getResources().getColor(R.color.indigo_500)){
            getTheme().applyStyle(R.style.MyIndigoViewTheme, true);
        }
    }

    public void setToolbarTheme(){
        int backColor=0,textColor = 0;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        appBarLayout = findViewById(R.id.app_bar);
        if(isDarkTheme){
            backColor = getResources().getColor(R.color.dark_color);
            textColor = getResources().getColor(R.color.white);
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }else{
            backColor = getResources().getColor(R.color.white);
            textColor = getResources().getColor(R.color.primary_text_color);
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        }

        appBarLayout.setBackgroundColor(backColor);
        toolbar.setBackgroundColor(backColor);
        toolbar.setTitleTextColor(textColor);
    }

    public void initFooterAction(final  Context context){
        findViewById(R.id.relative_footer_action).setBackgroundColor(colorTheme);
        TextView tv = findViewById(R.id.text_action);
        if(colorTheme == getResources().getColor(R.color.white)){
            tv.setTextColor(getResources().getColor(R.color.primary_text_color));
        }else{
            tv.setTextColor(getResources().getColor(R.color.white));
        }

        if (context instanceof DeliveryActivity) {
            tv.setText("Update Delivery Details");
        }else if (context instanceof BasicProfileActivity) {
            tv.setText("Update Store Details");
        }else if (context instanceof AddressActivity) {
            tv.setText("Update Store Address");
        }else if (context instanceof CustomerInfoActivity) {
            tv.setText("Continue");
        }else if(context instanceof AddCategoryActivity){
            tv.setText("Add Categories");
        }else if(context instanceof AddSubCatActivity){
            tv.setText("Add Sub Categories");
        }else if(context instanceof SyncProductActivity){
            tv.setText("Add Products");
        }else if(context instanceof TransactionDetailsActivity){
            tv.setText("Deliver Order");
        }else if(context instanceof MPayTransactionDetailsActivity){
            tv.setText("Deliver Order");
        }else if(context instanceof DeliveryAddressActivity){
            tv.setText("Update Delivery Address");
        }else if(context instanceof FreeProductOfferActivity){
            tv.setText("Save");
        }else if(context instanceof ComboProductOfferActivity){
            tv.setText("Save");
        }else if(context instanceof ProductPriceOfferActivity){
            tv.setText("Save");
        }else if(context instanceof CreateCouponOfferActivity){
            tv.setText("Save");
        }else if(context instanceof ChooseDeviceActivity){
            tv.setText("Continue");
        }else if(context instanceof AddUserActivity){
            tv.setText("Add User");
        }else if(context instanceof SyncDataActivity){
            tv.setText("Sync Data");
        }
    }

    public void changeViewBackgroundColor(View view) {
        view.setBackgroundColor(colorTheme);
    }


    public void initFooter(final Context context, int type) {
        int backColor=0,textColor = 0;
        if(isDarkTheme){
            backColor = getResources().getColor(R.color.dark_color);
            textColor = getResources().getColor(R.color.white);
        }else{
            backColor = getResources().getColor(R.color.white);
            textColor = getResources().getColor(R.color.primary_text_color);
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        appBarLayout = findViewById(R.id.app_bar);
        appBarLayout.setBackgroundColor(backColor);
        toolbar.setBackgroundColor(backColor);
        toolbar.setTitleTextColor(textColor);

        findViewById(R.id.linear_footer).setBackgroundColor(backColor);
        findViewById(R.id.separator_footer_1).setBackgroundColor(backColor);
        findViewById(R.id.separator_footer_2).setBackgroundColor(backColor);
        findViewById(R.id.separator_footer_3).setBackgroundColor(backColor);
        findViewById(R.id.separator_footer_4).setBackgroundColor(backColor);
        findViewById(R.id.separator_footer_5).setBackgroundColor(backColor);

        RelativeLayout relativeLayoutFooter1 = findViewById(R.id.relative_footer_1);
        RelativeLayout relativeLayoutFooter2 = findViewById(R.id.relative_footer_2);
        RelativeLayout relativeLayoutFooter3 = findViewById(R.id.relative_footer_3);
        RelativeLayout relativeLayoutFooter4 = findViewById(R.id.relative_footer_4);
        RelativeLayout relativeLayoutFooter5 = findViewById(R.id.relative_footer_5);

        ImageView imageViewFooter1 = findViewById(R.id.image_footer_1);
        ImageView imageViewFooter2 = findViewById(R.id.image_footer_2);
        ImageView imageViewFooter3 = findViewById(R.id.image_footer_3);
        ImageView imageViewFooter4 = findViewById(R.id.image_footer_4);
        ImageView imageViewFooter5 = findViewById(R.id.image_footer_5);

        TextView textViewFooter1 = findViewById(R.id.text_footer_1);
        TextView textViewFooter2 = findViewById(R.id.text_footer_2);
        TextView textViewFooter3 = findViewById(R.id.text_footer_3);
        TextView textViewFooter4 = findViewById(R.id.text_footer_4);
        TextView textViewFooter5 = findViewById(R.id.text_footer_5);

        View view1 = findViewById(R.id.separator_footer_1);
        View view2 = findViewById(R.id.separator_footer_2);
        View view3 = findViewById(R.id.separator_footer_3);
        View view4 = findViewById(R.id.separator_footer_4);
        View view5 = findViewById(R.id.separator_footer_5);

        switch (type) {
            case 0:
                imageViewFooter1.setColorFilter(colorTheme);
                textViewFooter1.setTextColor(colorTheme);
                view1.setBackgroundColor(colorTheme);
                break;
            case 1:
                imageViewFooter2.setColorFilter(colorTheme);
                textViewFooter2.setTextColor(colorTheme);
                view2.setBackgroundColor(colorTheme);
                break;
            case 2:
                imageViewFooter3.setColorFilter(colorTheme);
                textViewFooter3.setTextColor(colorTheme);
                view3.setBackgroundColor(colorTheme);
                break;
            case 3:
                imageViewFooter4.setColorFilter(colorTheme);
                textViewFooter4.setTextColor(colorTheme);
                view4.setBackgroundColor(colorTheme);
                break;
            case 4:
                imageViewFooter5.setColorFilter(colorTheme);
                textViewFooter5.setTextColor(colorTheme);
                view5.setBackgroundColor(colorTheme);
                break;
        }

        relativeLayoutFooter1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof MainActivity) {
                    //DialogAndToast.showToast("Profile clicked in profile",BaseActivity.this);
                } else {
                    Intent intent = new Intent(BaseActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });
        relativeLayoutFooter2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof CategoryListActivity) {
                    //DialogAndToast.showToast("Profile clicked in profile",BaseActivity.this);
                } else {
                    Intent intent = new Intent(BaseActivity.this, CategoryListActivity.class);
                    intent.putExtra("flag","shop");
                    startActivity(intent);
                }
            }
        });
        relativeLayoutFooter3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof CustomerListActivity) {
                    //DialogAndToast.showToast("Profile clicked in profile",BaseActivity.this);
                } else {
                    Intent intent = new Intent(BaseActivity.this, CustomerListActivity.class);
                    startActivity(intent);
                }
            }
        });

        relativeLayoutFooter4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof OffersActivity) {
                    //DialogAndToast.showToast("Profile clicked in profile",BaseActivity.this);
                } else {
                    Intent intent = new Intent(BaseActivity.this, OffersActivity.class);
                    startActivity(intent);
                }
            }
        });

        relativeLayoutFooter5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof SettingActivity) {
                    //DialogAndToast.showToast("Profile clicked in profile",BaseActivity.this);
                } else {
                    Intent intent = new Intent(BaseActivity.this, SettingActivity.class);
                    startActivity(intent);
                }
            }
        });

    }


    public void showMyDialog(final String msg) {
        //  errorNoInternet.setText("Oops... No internet");
        //  errorNoInternet.setVisibility(View.VISIBLE);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set title
        // alertDialogBuilder.setTitle("Oops...No internet");
        // set dialog message
        alertDialogBuilder
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(msg.equals("You are not authorized to perform this action.")){
                          logout();
                        }else{
                            onDialogPositiveClicked();
                        }

                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void showMyBothDialog(String msg,String negative,String positive) {
        //  errorNoInternet.setText("Oops... No internet");
        //  errorNoInternet.setVisibility(View.VISIBLE);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set title
        // alertDialogBuilder.setTitle("Oops...No internet");
        // set dialog message
        alertDialogBuilder
                .setMessage(msg)
                .setCancelable(false)
                .setNegativeButton(negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onDialogNegativeClicked();
                    }
                })
                .setPositiveButton(positive, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        onDialogPositiveClicked();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void showImageDialog(String url,View v){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Intent intent = new Intent(this, DialogActivity.class);
            intent.putExtra("image",url);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, v, getString(R.string.transition_dialog));
            startActivityForResult(intent, 100, options.toBundle());
        }else {
            int view = R.layout.activity_dialog;
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder
                    .setView(view)
                    .setCancelable(true);

            // create alert dialog
            final AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();

            final ImageView imageView = (ImageView) alertDialog.findViewById(R.id.image);

            Glide.with(getApplicationContext())
                    .load(url)
                    .centerCrop()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .override(Utility.dpToPx(300,this),Utility.dpToPx(300,this))
                    .into(imageView);
        }
    }

    public void onDialogPositiveClicked(){

    }

    public void onDialogNegativeClicked(){

    }

    public void logout(){
        String IMEI_NO = sharedPreferences.getString(Constants.IMEI_NO,"");
        editor.clear();
        editor.putString(Constants.IMEI_NO,IMEI_NO);
        editor.commit();
        dbHelper.deleteTable(DbHelper.CAT_TABLE);
        dbHelper.deleteTable(DbHelper.SUB_CAT_TABLE);
        dbHelper.deleteTable(DbHelper.PRODUCT_TABLE);
        dbHelper.deleteTable(DbHelper.PRODUCT_BARCODE_TABLE);
        dbHelper.deleteTable(DbHelper.PRODUCT_UNIT_TABLE);
        dbHelper.deleteTable(DbHelper.PRODUCT_SIZE_TABLE);
        dbHelper.deleteTable(DbHelper.PRODUCT_COLOR_TABLE);
        dbHelper.deleteTable(DbHelper.CART_TABLE);
        dbHelper.deleteTable(DbHelper.PROD_COMBO_TABLE);
        dbHelper.deleteTable(DbHelper.PROD_COMBO_DETAIL_TABLE);
        dbHelper.deleteTable(DbHelper.PROD_PRICE_TABLE);
        dbHelper.deleteTable(DbHelper.PROD_PRICE_DETAIL_TABLE);
        dbHelper.deleteTable(DbHelper.PROD_FREE_OFFER_TABLE);
        dbHelper.deleteTable(DbHelper.COUPON_TABLE);
        dbHelper.deleteTable(DbHelper.SHOP_CART_TABLE);
        dbHelper.deleteTable(DbHelper.CART_PROD_PRICE_TABLE);
        dbHelper.deleteTable(DbHelper.CART_PROD_PRICE_DETAIL_TABLE);
        dbHelper.deleteTable(DbHelper.CART_PROD_COMBO_TABLE);
        dbHelper.deleteTable(DbHelper.CART_PROD_COMBO_DETAIL_TABLE);
        dbHelper.deleteTable(DbHelper.CART_PROD_FREE_OFFER_TABLE);
        dbHelper.deleteTable(DbHelper.CART_COUPON_TABLE);
        dbHelper.deleteTable(DbHelper.CART_PRODUCT_UNIT_TABLE);
        dbHelper.deleteTable(DbHelper.CART_PRODUCT_SIZE_TABLE);
        dbHelper.deleteTable(DbHelper.CART_PRODUCT_COLOR_TABLE);

        Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    void showProgress(boolean show,String message){
        if(show){
            progressDialog.setMessage(message);
            progressDialog.show();
        }else{
            progressDialog.dismiss();
        }
    }

    public void showProgress(boolean show){
        if(show){
            progressDialog.show();
        }else{
            progressDialog.dismiss();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            super.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
