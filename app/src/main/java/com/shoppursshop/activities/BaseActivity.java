package com.shoppursshop.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shoppursshop.R;
import com.shoppursshop.database.DbHelper;
import com.shoppursshop.utilities.Constants;

import java.util.List;

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
    protected int limit = 30,offset = 0;
    protected int visibleItemCount,pastVisibleItems,totalItemCount;
    protected boolean loading=true,isScroll = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        sharedPreferences=getSharedPreferences(Constants.MYPREFERENCEKEY,MODE_PRIVATE);
        editor=sharedPreferences.edit();
        isDarkTheme = sharedPreferences.getBoolean(Constants.IS_DARK_THEME,false);
        colorTheme = sharedPreferences.getInt(Constants.COLOR_THEME,getResources().getColor(R.color.red_500));

        if(isDarkTheme){
            setTheme(R.style.Dark);
        }else{
            setTheme(R.style.Light);
        }

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

        boolean isDarkTheme = sharedPreferences.getBoolean(Constants.IS_DARK_THEME,false);
        if(this.isDarkTheme != isDarkTheme)
            recreate();
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


    public void showMyDialog(String msg) {
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
                        onDialogPositiveClicked();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void onDialogPositiveClicked(){

    }

    void showProgress(boolean show,String message){
        if(show){
            progressDialog.setMessage(message);
            progressDialog.show();
        }else{
            progressDialog.dismiss();
        }
    }

    void showProgress(boolean show){
        if(show){
            progressDialog.show();
        }else{
            progressDialog.dismiss();
        }
    }

}
