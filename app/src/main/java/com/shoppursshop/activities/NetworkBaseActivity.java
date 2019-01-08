package com.shoppursshop.activities;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.shoppursshop.utilities.AppController;
import com.shoppursshop.utilities.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NetworkBaseActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void jsonArrayApiRequest(int method, String url, JSONObject jsonObject, final String apiName){
        Log.i(TAG,url);
        Log.i(TAG,jsonObject.toString());
        JsonArrayRequest jsonObjectRequest=new JsonArrayRequest(method,url,jsonObject,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                AppController.getInstance().getRequestQueue().getCache().clear();
                Log.i(TAG,response.toString());
                showProgress(false);
                onJsonArrayResponse(response,apiName);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                AppController.getInstance().getRequestQueue().getCache().clear();
                Log.i(TAG,"Json Error "+error.toString());
                onServerErrorResponse(error,apiName);
                //  DialogAndToast.showDialog(getResources().getString(R.string.connection_error),BaseActivity.this);
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    protected void jsonObjectApiRequest(int method,String url, JSONObject jsonObject, final String apiName){
        Log.i(TAG,url);
        Log.i(TAG,jsonObject.toString());
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(method,url,jsonObject,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                AppController.getInstance().getRequestQueue().getCache().clear();
                Log.i(TAG,response.toString());
                showProgress(false);
                onJsonObjectResponse(response,apiName);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                AppController.getInstance().getRequestQueue().getCache().clear();
                Log.i(TAG,"Json Error "+error.toString());
                showProgress(false);
                onServerErrorResponse(error,apiName);
                // DialogAndToast.showDialog(getResources().getString(R.string.connection_error),BaseActivity.this);
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    protected void stringApiRequest(int method,String url, final String apiName){
        Log.i(TAG,url);
        StringRequest jsonObjectRequest=new StringRequest(method,url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppController.getInstance().getRequestQueue().getCache().clear();
                Log.i(TAG,response.toString());
                showProgress(false);
                onStringResponse(response,apiName);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                AppController.getInstance().getRequestQueue().getCache().clear();
                Log.i(TAG,"Json Error "+error.toString());
                showProgress(false);
                onServerErrorResponse(error,apiName);
                // DialogAndToast.showDialog(getResources().getString(R.string.connection_error),BaseActivity.this);
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    public void onJsonArrayResponse(JSONArray jsonArray, String apiName) {

    }


    public void onJsonObjectResponse(JSONObject jsonObject, String apiName) {
    }

    public void onStringResponse(String response, String apiName) {

    }

    public void onJsonParserResponse(JSONException error, String apiName) {

    }

    public void onServerErrorResponse(VolleyError error, String apiName) {

    }

    public void onStatusNotOk(String message, String apiName) {

    }
}
