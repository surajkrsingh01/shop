package com.shoppursshop.utilities;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

/**
 * Created by Shweta on 8/22/2016.
 */
public class DialogAndToast {

    public static void showDialog(String msg,Context context) {
        //  errorNoInternet.setText("Oops... No internet");
        //  errorNoInternet.setVisibility(View.VISIBLE);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        // set title
        // alertDialogBuilder.setTitle("Oops...No internet");
        // set dialog message
        alertDialogBuilder
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public static void showToast(String msg,Context context){
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }
}
