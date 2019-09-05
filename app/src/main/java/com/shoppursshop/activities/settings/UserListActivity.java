package com.shoppursshop.activities.settings;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import com.shoppursshop.R;
import com.shoppursshop.activities.NetworkBaseActivity;
import com.shoppursshop.adapters.MyUserAdapter;
import com.shoppursshop.interfaces.MyItemClickListener;
import com.shoppursshop.interfaces.MyItemTypeClickListener;
import com.shoppursshop.models.MyUser;
import com.shoppursshop.utilities.ConnectionDetector;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.DialogAndToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserListActivity extends NetworkBaseActivity implements MyItemTypeClickListener {

    private List<MyUser> itemList;
    private RecyclerView recyclerView;
    private MyUserAdapter itemAdapter;
    private int usersAllowed,id,type,position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();
    }

    private void init(){

        itemList = new ArrayList<>();
        usersAllowed = getIntent().getIntExtra("number",0);
        id = getIntent().getIntExtra("id",0);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        itemAdapter=new MyUserAdapter(this,itemList);
        itemAdapter.setMyItemClickListener(this);
        recyclerView.setAdapter(itemAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemList.size() < usersAllowed){
                    Intent intent = new Intent(UserListActivity.this, AddUserActivity.class);
                    intent.putExtra("id",""+id);
                    startActivityForResult(intent,10);
                }else{
                    DialogAndToast.showDialog("Please buy subscription to add new user",UserListActivity.this);
                }
            }
        });

        if(ConnectionDetector.isNetworkAvailable(this)){
            getUsers();
        }
    }

    private void getUsers(){
        Map<String,String> params=new HashMap<>();
        params.put("id",""+id);
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+Constants.GET_USER;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"getUsers");
    }

    private void changeStatus(String status){
        MyUser myUser = itemList.get(position);
        Map<String,String> params=new HashMap<>();
        params.put("status",""+status);
        params.put("mobile",myUser.getMobile());
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        params.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
        params.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
        String url=getResources().getString(R.string.url)+Constants.CHANGE_USER_STATUS;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"changeUserStatus");
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {

        try {
            if (apiName.equals("getUsers")) {
                if (response.getBoolean("status")) {
                    JSONArray jsonArray = response.getJSONArray("result");
                    JSONObject jsonObject = null;
                    MyUser myUser = null;
                    int len = jsonArray.length();
                    for(int i =0; i< len ; i++){
                        myUser = new MyUser();
                        jsonObject = jsonArray.getJSONObject(i);
                        myUser.setId(jsonObject.getString("id"));
                        myUser.setUsername(jsonObject.getString("username"));
                        myUser.setMobile(jsonObject.getString("mobile"));
                        myUser.setIsActive(jsonObject.getString("isActive"));
                        // myUser.setImeiNo(jsonObject.getString("isActive"));
                      /*  myUser.setDbName(jsonObject.getString("dbName"));
                        myUser.setDbUserName(jsonObject.getString("dbUserName"));
                        myUser.setDbPassword(jsonObject.getString("dbPassword"));*/
                        itemList.add(myUser);
                    }

                    itemAdapter.notifyDataSetChanged();

                }else{
                    DialogAndToast.showDialog(response.getString("message"), this);
                }
            }else if (apiName.equals("changeUserStatus")) {
                if (response.getBoolean("status")) {
                       MyUser myUser = itemList.get(position);
                       if(type == 1){
                           itemList.remove(position);
                           itemAdapter.notifyItemRemoved(position);
                       }else if(type == 2){
                           myUser.setIsActive("2");
                           itemAdapter.notifyItemChanged(position);
                       }else if(type == 3){
                           myUser.setIsActive("1");
                           itemAdapter.notifyItemChanged(position);
                       }
                    DialogAndToast.showDialog(response.getString("message"),this);
                }else{
                    DialogAndToast.showDialog(response.getString("message"),this);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClicked(int position,int type) {
     MyUser myUser = itemList.get(position);

     this.position = position;
     this.type = type;

     if(type == 1){
         //remove
         changeStatus("0");
     }else if(type == 2){
         //disable
         changeStatus("2");
     }else if(type == 3){
         //enable
         changeStatus("1");
     }
    }
}
