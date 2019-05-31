package com.shoppursshop.fragments;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.shoppursshop.R;
import com.shoppursshop.activities.ScannarActivity;
import com.shoppursshop.adapters.BarCodeAdapter;
import com.shoppursshop.database.DbHelper;
import com.shoppursshop.utilities.AppController;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.DialogAndToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class MultipleBarcodeBottomFragment extends BottomSheetDialogFragment {

    private String TAG = "BottomSearchFragment";
    private ImageView iv_clear;
    private RecyclerView recyclerView;
    private List<String> barCodeList;
    private BarCodeAdapter itemAdapter;
    private RelativeLayout relativeBarCode;
    private String prodCode,barCode;
    private int prodId;

    protected ProgressDialog progressDialog;
    protected DbHelper dbHelper;

    private SharedPreferences sharedPreferences;

    public void setBarCodeList(List<String> barCodeList) {
        this.barCodeList = barCodeList;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public void setProdId(int prodId) {
        this.prodId = prodId;
    }

    public MultipleBarcodeBottomFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences(Constants.MYPREFERENCEKEY,getActivity().MODE_PRIVATE);
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_multiple_barcode_bottom, container, false);
        iv_clear = rootView.findViewById(R.id.iv_clear);

        barCodeList = dbHelper.getBarCodes(prodId);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        itemAdapter=new BarCodeAdapter(getContext(),barCodeList);
        recyclerView.setAdapter(itemAdapter);

        iv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultipleBarcodeBottomFragment.this.dismiss();
            }
        });

        relativeBarCode = rootView.findViewById(R.id.relative_barcode);

        relativeBarCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),ScannarActivity.class);
                intent.putExtra("flag","scan");
                intent.putExtra("type","addProductBarcode");
                //startActivity(intent);
                startActivityForResult(intent,2);
            }
        });

        return rootView;
    }

    private void getBarCodes(){
        Map<String,String> params=new HashMap<>();
        params.put("prodId",""+prodId);
        params.put("code",prodCode);
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+Constants.GET_BAR_CODE;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"barCodes");
    }

    private void addBarCode(){
        Map<String,String> params=new HashMap<>();
        params.put("prodId",""+prodId);
        params.put("prodCode",prodCode);
        params.put("prodBarCode",barCode);
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+Constants.ADD_BAR_CODE;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"addBarCode");
    }

    protected void jsonObjectApiRequest(int method,String url, JSONObject jsonObject, final String apiName){
        try {
            jsonObject.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
            jsonObject.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
            jsonObject.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    public void onJsonObjectResponse(JSONObject response, String apiName) {
        try {
            if(apiName.equals("barCodes")){
                if(response.getBoolean("status")){
                    JSONArray dataArray = response.getJSONArray("result");
                    JSONObject dataObject = null;
                    int len = dataArray.length();
                    for(int i = 0; i< len; i++){
                        dataObject = dataArray.getJSONObject(i);
                        barCodeList.add(dataObject.getString("prodBarCode"));
                    }

                    itemAdapter.notifyDataSetChanged();
                }
            }else{
                if(response.getBoolean("status")){
                    barCodeList.add(barCode);
                    dbHelper.addProductBarcode(prodId,barCode);
                    itemAdapter.notifyDataSetChanged();
                    DialogAndToast.showToast(response.getString("message"),getActivity());
                }else{
                    DialogAndToast.showDialog(response.getString("message"),getActivity());
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onServerErrorResponse(VolleyError error, String apiName) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i(TAG,"Result received");

        if (requestCode == 2){
            if(data != null){
                barCode = data.getStringExtra("barCode");
                addBarCode();
            }
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
