package com.shoppursshop.activities.payment.ccavenue.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.shoppursshop.R;
import com.shoppursshop.activities.BaseActivity;
import com.shoppursshop.activities.NetworkBaseActivity;
import com.shoppursshop.activities.payment.ccavenue.utility.AvenuesParams;
import com.shoppursshop.activities.payment.ccavenue.utility.Constants;
import com.shoppursshop.activities.payment.ccavenue.utility.LoadingDialog;
import com.shoppursshop.activities.payment.ccavenue.utility.RSAUtility;
import com.shoppursshop.activities.payment.ccavenue.utility.ServiceUtility;
import com.shoppursshop.utilities.DialogAndToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class CCAvenueWebViewActivity extends NetworkBaseActivity {

    public static final String TAG = "CCAvenueWeb";
   // public static final String ACCESS_CODE = "4YRUXLSRO20O8NIH";
    public String orderId = "0";
   /* Test
    public static final String ACCESS_CODE = "AVYS02GA48AW12SYWA";
    public static final String MERCHANT_ID = "191918";
    public static final String WORKING_KEY = "42A76E35C3C23567D3E1E3284C1A12AF";*/

   /**/
    public static final String ACCESS_CODE = "AVQH72EH28AD03HQDA";
    public static final String MERCHANT_ID = "143051";
    public static final String WORKING_KEY = "534D4882D2F074761151788FCE5EE352";

    public static final String RSA_KEY_URL = "https://secure.ccavenue.com/transaction/jsp/GetRSA.jsp";
    public String REDIRECT_URL = "";
    public String CANCEL_URL = "";

    Intent mainIntent;
    String encVal;
    String vResponse;
    private String flag;
    private String name,address,mobileNo,email;

    private WebView webview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ccavenue_web_view);

        webview = (WebView) findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);

        name= sharedPreferences.getString(com.shoppursshop.utilities.Constants.FULL_NAME,"");
        address= sharedPreferences.getString(com.shoppursshop.utilities.Constants.ADDRESS,"");
        email= sharedPreferences.getString(com.shoppursshop.utilities.Constants.EMAIL,"");
        mobileNo= sharedPreferences.getString(com.shoppursshop.utilities.Constants.MOBILE_NO,"");

        if(name.equals("null") || name.equals("Not Available")){
            name = "";
        }

        if(address.equals("null") || address.equals("Not Available")){
              address = "";
        }

        if(mobileNo.equals("null") || mobileNo.equals("Not Available")){
            mobileNo = "";
        }

        if(email.equals("null") || email.equals("Not Available")){
           email = "";
        }

        mainIntent = getIntent();
        flag = mainIntent.getStringExtra("flag");

        REDIRECT_URL = getResources().getString(R.string.url)+"/paymentResponseHandler";
        CANCEL_URL = getResources().getString(R.string.url)+"/paymentResponseHandler";

        Integer randomNum = ServiceUtility.randInt(0, 9999999);
       // orderId = mainIntent.getStringExtra(AvenuesParams.ORDER_ID);
        orderId = randomNum.toString();
        Log.i(TAG,"orderID "+orderId);
        get_RSA();


        Log.i(TAG,"amount "+mainIntent.getStringExtra(AvenuesParams.AMOUNT));

      //  get_RSA_key(ACCESS_CODE,orderId);

    }

    private class RenderView extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
           // LoadingDialog.showLoadingDialog(CCAvenueWebViewActivity.this, "Loading...");

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            if (!ServiceUtility.chkNull(vResponse).equals("")
                    && ServiceUtility.chkNull(vResponse).toString().indexOf("ERROR") == -1) {
                StringBuffer vEncVal = new StringBuffer("");
                vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.AMOUNT, mainIntent.getStringExtra(AvenuesParams.AMOUNT)));
                vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.CURRENCY, mainIntent.getStringExtra(AvenuesParams.CURRENCY)));
                /*if(flag.equals("wallet") || flag.equals("credit") || flag.equals("service")){
                    vEncVal.append(ServiceUtility.addToPostParams("merchant_param3",sharedPreferences.getString(com.gsttm.utilities.Constants.USER_ID,"")));
                }*/
                encVal = RSAUtility.encrypt(vEncVal.substring(0, vEncVal.length() - 1), vResponse);  //encrypt amount and currency
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            //LoadingDialog.cancelLoading();
            showProgress(false);

            @SuppressWarnings("unused")
            class MyJavaScriptInterface {
                @JavascriptInterface
                public void processHTML(final String html) {
                    // process the html source code to get final status of transaction
                   // Log.i(TAG,"response "+html);
                    Intent intent = new Intent();
                    try {
                        String response = html.substring(html.indexOf("{"),(html.lastIndexOf("}")+1));
                        Log.i(TAG,"response "+response);
                        JSONObject dataObject = new JSONObject(response);
                        intent.putExtra("transStatus", dataObject.getString("error"));
                        intent.putExtra("message", dataObject.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        intent.putExtra("transStatus", "true");
                    }catch (Exception e){
                        e.printStackTrace();
                        intent.putExtra("transStatus", "true");
                    }

                    setResult(-1,intent);
                    finish();
                }

                @JavascriptInterface
                public void showToast(final String webMessage){
                    final String msgeToast = webMessage;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CCAvenueWebViewActivity.this, webMessage, Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            }
            webview.getSettings().setJavaScriptEnabled(true);
            webview.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
           // webview.addJavascriptInterface(new MyJavaScriptInterface(), "AndroidFunction");
            webview.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(webview, url);
                    LoadingDialog.cancelLoading();
                    Log.i(TAG,"redirecting "+url);
                    if (url.contains("paymentResponseHandler")) {
                        Log.i(TAG,"Loading javascript");
                        webview.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                    }else if(url.contains("processNbkReq")){
                       // webview.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                    }
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    LoadingDialog.showLoadingDialog(CCAvenueWebViewActivity.this, "Loading...");
                }
            });


            try {
                String postData = "";
                postData = AvenuesParams.ACCESS_CODE + "=" + URLEncoder.encode(ACCESS_CODE, "UTF-8") + "&" + AvenuesParams.MERCHANT_ID + "=" + URLEncoder.encode(MERCHANT_ID, "UTF-8") + "&" + AvenuesParams.ORDER_ID + "=" + URLEncoder.encode(orderId, "UTF-8")
                        + "&" + AvenuesParams.REDIRECT_URL + "=" + URLEncoder.encode(REDIRECT_URL, "UTF-8")
                        + "&" + AvenuesParams.CANCEL_URL + "=" + URLEncoder.encode(CANCEL_URL, "UTF-8")
                        + "&" + AvenuesParams.ENC_VAL + "=" + URLEncoder.encode(encVal, "UTF-8")
                        + "&" + "billing_name" + "=" + URLEncoder.encode(name, "UTF-8")
                        + "&" + "billing_address" + "=" + URLEncoder.encode(address, "UTF-8")
                        // + "&" + "billing_zip" + "=" + URLEncoder.encode("Vipin Dhama", "UTF-8")
                        + "&" + "billing_tel" + "=" + URLEncoder.encode(mobileNo, "UTF-8")
                        + "&" + "billing_email" + "=" + URLEncoder.encode(email, "UTF-8")
                        + "&" + "delivery_name" + "=" + URLEncoder.encode(name, "UTF-8")
                        + "&" + "delivery_address" + "=" + URLEncoder.encode(address, "UTF-8")
                        // + "&" + "billing_zip" + "=" + URLEncoder.encode("Vipin Dhama", "UTF-8")
                        + "&" + "delivery_tel" + "=" + URLEncoder.encode(mobileNo, "UTF-8")
                        + "&" + "delivery_email" + "=" + URLEncoder.encode(email, "UTF-8")
                        + "&" + "merchant_param3" + "=" + URLEncoder.encode(sharedPreferences.getString(com.shoppursshop.utilities.Constants.USER_ID,""), "UTF-8");
                Log.i(TAG,"params "+postData);
                webview.postUrl(Constants.TRANS_URL, postData.getBytes());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
    }

    public void get_RSA() {

        Log.i(TAG,"Getting RSA...");
        String url=getResources().getString(R.string.url)+"/api/getRSAKey?orderid="+orderId;
        progressDialog.setMessage("Loading...");
        showProgress(true);
        // Log.i(TAG,"params "+params.toString());
        stringApiRequest(Request.Method.POST,url,"getRSAKey");

    }

    public void get_RSA_key(final String ac, final String od) {
        LoadingDialog.showLoadingDialog(CCAvenueWebViewActivity.this, "Loading...");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RSA_KEY_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(WebViewActivity.this,response,Toast.LENGTH_LONG).show();
                        LoadingDialog.cancelLoading();

                        if (response != null && !response.equals("")) {
                            vResponse = response;     ///save retrived rsa key
                            Log.i(TAG,"key "+vResponse);
                            if (vResponse.contains("!ERROR!")) {
                                show_alert(vResponse);
                            } else {
                                new RenderView().execute();   // Calling async task to get display content
                            }


                        }
                        else
                        {
                            show_alert("No response");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        LoadingDialog.cancelLoading();
                        //Toast.makeText(WebViewActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(AvenuesParams.ACCESS_CODE, ac);
                params.put(AvenuesParams.ORDER_ID, od);
                return params;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void show_alert(String msg) {
        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(
                CCAvenueWebViewActivity.this).create();

        alertDialog.setTitle("Error!!!");
        if (msg.contains("\n"))
            msg = msg.replaceAll("\\\n", "");

        alertDialog.setMessage(msg);



        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });


        alertDialog.show();
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {
        try {
             if(apiName.equals("getRsa")){
                if(!response.getBoolean("error")){
                    JSONObject jsonObject = response.getJSONObject("result");

                }else{
                    DialogAndToast.showDialog(response.getString("message"),this);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStringResponse(String response, String apiName) {
        if (response != null && !response.equals("")) {
            vResponse = response;
            ///save retrived rsa key


            if (vResponse.contains("!ERROR!")) {
                DialogAndToast.showDialog(vResponse,CCAvenueWebViewActivity.this);
            } else {
                showProgress(true);
                new RenderView().execute();

            }
        }
    }

}