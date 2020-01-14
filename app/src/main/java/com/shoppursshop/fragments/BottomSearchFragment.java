package com.shoppursshop.fragments;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.shoppursshop.R;
import com.shoppursshop.activities.product.ProductDetailActivity;
import com.shoppursshop.adapters.SearchProductAdapter;
import com.shoppursshop.adapters.SearchCustomerAdapter;
import com.shoppursshop.database.DbHelper;
import com.shoppursshop.interfaces.MyImageClickListener;
import com.shoppursshop.interfaces.MyItemClickListener;
import com.shoppursshop.interfaces.MyItemTypeClickListener;
import com.shoppursshop.interfaces.MyListItemClickListener;
import com.shoppursshop.models.MyCustomer;
import com.shoppursshop.models.MyProductItem;
import com.shoppursshop.morphdialog.DialogActivity;
import com.shoppursshop.utilities.AppController;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.DialogAndToast;
import com.shoppursshop.utilities.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by suraj kumar singh on 18-04-2019.
 */

public class BottomSearchFragment extends BottomSheetDialogFragment implements MyItemClickListener,
        MyItemTypeClickListener, MyImageClickListener {
    private String TAG = "BottomSearchFragment";
    protected ProgressDialog progressDialog;
    protected SharedPreferences sharedPreferences;
    protected SharedPreferences.Editor editor;
    protected DbHelper dbHelper;
    private ImageView iv_clear;
    private RecyclerView recyclerView_Search;
    private EditText etSearch;
    private SearchCustomerAdapter customerAdapter;
    private SearchProductAdapter productAdapter;
    private List<MyProductItem> myProductList;
    private List<MyCustomer> myCustomerList;
    private String callingActivityName, flag,mobile,token;
    private int colorTheme;
    private boolean isDarkTheme;


    private int limit = 20,offset = 0;

    private MyItemClickListener myItemClickListener;
    private MyListItemClickListener myListItemClickListener;

    public void setMyItemClickListener(MyItemClickListener myItemClickListener) {
        this.myItemClickListener = myItemClickListener;
    }

    public void setMyListItemClickListener(MyListItemClickListener myListItemClickListener) {
        this.myListItemClickListener = myListItemClickListener;
    }

    public BottomSearchFragment() {

    }

    public void setCallingActivityName(String name){
     this.callingActivityName = name;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences=getActivity().getSharedPreferences(Constants.MYPREFERENCEKEY,getActivity().MODE_PRIVATE);
        editor=sharedPreferences.edit();
        colorTheme = sharedPreferences.getInt(Constants.COLOR_THEME,getResources().getColor(R.color.red_500));
        isDarkTheme = sharedPreferences.getBoolean(Constants.IS_DARK_THEME,false);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         View view = inflater.inflate(R.layout.fragment_bottom_sheet_dialog, container, false);
         RelativeLayout relative_search = view.findViewById(R.id.relative_search);
        ((GradientDrawable)relative_search.getBackground()).setColor(colorTheme);
         iv_clear = view.findViewById(R.id.iv_clear);
         etSearch = view.findViewById(R.id.et_search);
         recyclerView_Search = view.findViewById(R.id.recyclerView_Search);
      //   Log.d(TAG, callingActivityName);

         iv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSearchFragment.this.dismiss();
            }
         });

        if(callingActivityName.equals("customerList") || callingActivityName.equals("customerInfoActivity"))
            initCustomer();
        else if(callingActivityName.equals("productList")) {
            Bundle bundle = getArguments();
            flag = bundle.getString("flag");
            intiProducts();
        }

        etSearch.addTextChangedListener(new TextWatcher() {
            private Timer timer=new Timer();
            private final long DELAY = 1000; // milliseconds
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(final Editable editable) {
                timer.cancel();
                timer = new Timer();
                timer.schedule(
                        new TimerTask() {
                            @Override
                            public void run() {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(callingActivityName.equals("customerList") ||
                                                callingActivityName.equals("customerInfoActivity")){
                                            filterCustomerSearch(editable.toString());
                                        }else{
                                            filterProductSearch(editable.toString());
                                        }

                                    }
                                });

                            }
                        },
                        DELAY
                );

            }
        });

        return view;
    }

    private void filterCustomerSearch(String query){
       myCustomerList.clear();
        Map<String,String> params=new HashMap<>();
        params.put("query",query);
        params.put("limit",""+limit);
        params.put("offset",""+offset);
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.cust_url)+Constants.SEARCH_CUSTOMER;
       // showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"searchCustomer");
    }

    private void filterProductSearch(String query){
        myProductList.clear();
        List<MyProductItem> itemList = null;
        Log.i(TAG,"flag "+flag);
        if(flag.equals("searchCartProduct")){
            itemList = dbHelper.searchProductsForCart(query,limit,offset);
        }else{
            itemList = dbHelper.searchProducts(query,limit,offset);
        }

        for(MyProductItem ob : itemList){
            if(flag.equals("searchCartProduct")){
                if(!dbHelper.checkProdExistInCart(ob.getProdId())){
                    myProductList.add(ob);
                }
            }else{
                myProductList.add(ob);
            }
        }

        productAdapter.notifyDataSetChanged();
    }

    private void initCustomer(){
        myCustomerList = new ArrayList<>();
        MyCustomer myCustomer = new MyCustomer();
        myCustomer.setId("3");
        myCustomer.setCode("c3");
        myCustomer.setName("Vipin Dhama");
        myCustomer.setMobile("9718181697");
        myCustomer.setEmail("vipinsuper19@gmail.com");
        myCustomer.setImage("");
        myCustomer.setIsFav("Y");
        myCustomer.setRatings(4.5f);
        //myCustomerList.add(myCustomer);
        recyclerView_Search.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView_Search.setLayoutManager(layoutManager);
        recyclerView_Search.setItemAnimator(new DefaultItemAnimator());
        customerAdapter=new SearchCustomerAdapter(getContext(),myCustomerList);
        customerAdapter.setMyImageClickListener(this);
        customerAdapter.setType(callingActivityName);
        customerAdapter.setMyItemTypeClickListener(this);
        recyclerView_Search.setAdapter(customerAdapter);
    }

    private void intiProducts(){
        myProductList = new ArrayList<>();
        recyclerView_Search.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView_Search.setLayoutManager(layoutManager);
        recyclerView_Search.setItemAnimator(new DefaultItemAnimator());
        productAdapter = new SearchProductAdapter(getContext(),myProductList);
        productAdapter.setMyItemClickListener(this);
        productAdapter.setMyImageClickListener(this);
        productAdapter.setFlag(flag);
        recyclerView_Search.setAdapter(productAdapter);

    }

    protected void jsonObjectApiRequest(int method, String url, JSONObject jsonObject, final String apiName){
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
                DialogAndToast.showDialog(getResources().getString(R.string.connection_error),getActivity());
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer "+token);
                //params.put("VndUserDetail", appVersion+"#"+deviceName+"#"+osVersionName);
                return params;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    public void onJsonObjectResponse(JSONObject response, String apiName) {
         try {
              if(apiName.equals("searchCustomer")){
                  if(response.getBoolean("status")){
                      JSONArray dataArray = response.getJSONArray("result");
                      JSONObject jsonObject = null;
                      int len = dataArray.length();
                      MyCustomer myCustomer= null;
                      for (int i = 0; i < len; i++) {
                          jsonObject = dataArray.getJSONObject(i);
                          myCustomer = new MyCustomer();
                          myCustomer.setId(jsonObject.getString("id"));
                          myCustomer.setCode(jsonObject.getString("code"));
                          myCustomer.setName(jsonObject.getString("name"));
                          myCustomer.setMobile(jsonObject.getString("mobileNo"));
                          myCustomer.setEmail(jsonObject.getString("email"));
                          myCustomer.setImage(jsonObject.getString("photo"));
                          myCustomer.setIsFav(jsonObject.getString("isFav"));
                          myCustomer.setCustUserCreateStatus(jsonObject.getString("userCreateStatus"));
                          myCustomer.setRatings((float)jsonObject.getDouble("ratings"));
                          myCustomerList.add(myCustomer);
                      }
                      customerAdapter.notifyDataSetChanged();
                  }else{
                      DialogAndToast.showDialog(response.getString("message"),getActivity());
                  }
              }
         }catch (JSONException e){
             e.printStackTrace();
         }
    }


    void showProgress(boolean show){
        if(show){
            progressDialog.show();
        }else{
            progressDialog.dismiss();
        }
    }

    @Override
    public void onItemClicked(int position) {
        if(myItemClickListener != null){
            myItemClickListener.onItemClicked(myProductList.get(position).getProdId());
        }else{
            MyProductItem item = (MyProductItem) myProductList.get(position);
            Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
            intent.putExtra("id",item.getProdId());
            intent.putExtra("subCatName","");
            intent.putExtra("flag","search");
            startActivity(intent);
        }
        this.dismiss();
    }

    @Override
    public void onItemClicked(int position, int type) {
        MyCustomer customer = myCustomerList.get(position);
        if(type == 2){
            Bundle bundle = new Bundle();
            bundle.putString("custMobile",customer.getMobile());
            bundle.putString("custName",customer.getName());
            bundle.putInt("custId", Integer.parseInt(customer.getId()));
            bundle.putString("custCode",customer.getCode());
            bundle.putString("custImage",customer.getImage());
            bundle.putString("custUserCreateStatus",customer.getCustUserCreateStatus());
            myListItemClickListener.onItemClicked(bundle);
            this.dismiss();
        }else if(type == 3){
           makeCall(customer.getMobile());
        }else if(type == 4){
            openWhatsApp(customer.getMobile());
        }

    }

    public void makeCall(String mobile){
        this.mobile = mobile;
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+mobile));
        if(Utility.verifyCallPhonePermissions(getActivity()))
            startActivity(callIntent);
    }

    public void openWhatsApp(String mobile){
        try {
            mobile = "91"+mobile;
            String text = "This is a test";// Replace with your message.
            //   String toNumber = "xxxxxxxxxx"; // Replace with mobile phone number without +Sign or leading zeros, but with country code
            //Suppose your country is India and your phone number is “xxxxxxxxxx”, then you need to send “91xxxxxxxxxx”.
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+mobile +"&text="));
            startActivity(intent);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //mLocationPermissionGranted = false;

        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_CALL_PHONE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makeCall(mobile);

                }
                break;
        }

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

    @Override
    public void onImageClicked(int position, int type, View view) {
        if(callingActivityName.equals("customerList") || callingActivityName.equals("customerInfoActivity")) {
            MyCustomer customer = myCustomerList.get(position);
            showImageDialog(customer.getImage(),view);
        }
        else if(callingActivityName.equals("productList")) {
          MyProductItem myProductItem = myProductList.get(position);
          showImageDialog(myProductItem.getProdImage1(),view);
        }
    }

}

