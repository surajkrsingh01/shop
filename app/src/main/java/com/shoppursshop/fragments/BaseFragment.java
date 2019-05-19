package com.shoppursshop.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shoppursshop.R;
import com.shoppursshop.activities.BaseActivity;
import com.shoppursshop.activities.settings.profile.AddressActivity;
import com.shoppursshop.activities.settings.profile.BasicProfileActivity;
import com.shoppursshop.activities.settings.profile.DeliveryActivity;
import com.shoppursshop.database.DbHelper;
import com.shoppursshop.utilities.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment {

    protected  String  TAG = "Base";
    protected ProgressDialog progressDialog;
    protected SharedPreferences sharedPreferences;
    protected SharedPreferences.Editor editor;
    protected DbHelper dbHelper;
    protected boolean isDarkTheme;
    protected int colorTheme;

    public BaseFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences=getActivity().getSharedPreferences(Constants.MYPREFERENCEKEY,getActivity().MODE_PRIVATE);
        editor=sharedPreferences.edit();

        colorTheme = sharedPreferences.getInt(Constants.COLOR_THEME,getActivity().getResources().getColor(R.color.red_500));

        dbHelper=new DbHelper(getActivity());
        progressDialog = new ProgressDialog(getActivity());
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

    public void initFooterAction(final Context context,View view){
        view.findViewById(R.id.relative_footer_action).setBackgroundColor(colorTheme);
        TextView tv = view.findViewById(R.id.text_action);
        if(colorTheme == getResources().getColor(R.color.white)){
            tv.setTextColor(getResources().getColor(R.color.primary_text_color));
        }else{
            tv.setTextColor(getResources().getColor(R.color.white));
        }

        tv.setText("Update Bank Details");
    }

    public void showMyDialog(String msg) {
        //  errorNoInternet.setText("Oops... No internet");
        //  errorNoInternet.setVisibility(View.VISIBLE);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
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
