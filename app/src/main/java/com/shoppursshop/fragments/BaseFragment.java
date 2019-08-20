package com.shoppursshop.fragments;


import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shoppursshop.R;
import com.shoppursshop.activities.BaseActivity;
import com.shoppursshop.activities.settings.profile.AddressActivity;
import com.shoppursshop.activities.settings.profile.BasicProfileActivity;
import com.shoppursshop.activities.settings.profile.DeliveryActivity;
import com.shoppursshop.database.DbHelper;
import com.shoppursshop.morphdialog.DialogActivity;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.Utility;

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
    protected String token;

    public BaseFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences=getActivity().getSharedPreferences(Constants.MYPREFERENCEKEY,getActivity().MODE_PRIVATE);
        editor=sharedPreferences.edit();

        colorTheme = sharedPreferences.getInt(Constants.COLOR_THEME,getActivity().getResources().getColor(R.color.red_500));
        token = sharedPreferences.getString(Constants.TOKEN,"");
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

    public void showImageDialog(String url,View v){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Intent intent = new Intent(getActivity(), DialogActivity.class);
            intent.putExtra("image",url);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), v, getString(R.string.transition_dialog));
            startActivityForResult(intent, 100, options.toBundle());
        }else {
            int view = R.layout.activity_dialog;
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder
                    .setView(view)
                    .setCancelable(true);

            // create alert dialog
            final AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();

            final ImageView imageView = (ImageView) alertDialog.findViewById(R.id.image);

            Glide.with(getActivity())
                    .load(url)
                    .centerCrop()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .override(Utility.dpToPx(300,getActivity()),Utility.dpToPx(300,getActivity()))
                    .into(imageView);
        }
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
