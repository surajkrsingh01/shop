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

import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.shoppursshop.R;
import com.shoppursshop.activities.CartActivity;
import com.shoppursshop.activities.CustomerInfoActivity;
import com.shoppursshop.activities.NetworkBaseActivity;
import com.shoppursshop.adapters.ChatUserAdapter;
import com.shoppursshop.adapters.MyUserAdapter;
import com.shoppursshop.fragments.BottomSearchFragment;
import com.shoppursshop.interfaces.MyItemClickListener;
import com.shoppursshop.interfaces.MyListItemClickListener;
import com.shoppursshop.models.ChatUser;
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

public class UserListForChatActivity extends NetworkBaseActivity implements MyItemClickListener, MyListItemClickListener {

    private List<ChatUser> itemList;
    private RecyclerView recyclerView;
    private ChatUserAdapter myItemAdapter;
    private BottomSearchFragment bottomSearchFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list_for_chat);

        init();

    }

    private void init(){
        limit = 50;
        offset = 0;

        itemList = new ArrayList<>();
        if(!sharedPreferences.getString(Constants.SHOP_CODE,"").equals("SHP1")){
            ChatUser item = new ChatUser();
            item.setUserCode("SHP1");
            item.setLastMessage("");
            item.setLastMessageDate("");
            item.setUserMobile("9810162596");
            item.setUserName("Shoppurs Technical Support");
            item.setUserPic("");
            itemList.add(item);

            item = new ChatUser();
            item.setUserCode("SHP1");
            item.setLastMessage("");
            item.setLastMessageDate("");
            item.setUserMobile("9810162596");
            item.setUserName("Shoppurs Sale Support");
            item.setUserPic("");
            itemList.add(item);

            item = new ChatUser();
            item.setUserCode("SHP1");
            item.setLastMessage("");
            item.setLastMessageDate("");
            item.setUserMobile("9810162596");
            item.setUserName("Shoppurs Account Support");
            item.setUserPic("");
            itemList.add(item);
        }
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        final RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        myItemAdapter=new ChatUserAdapter(this,itemList);
        recyclerView.setAdapter(myItemAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isScroll) {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    Log.i(TAG,"visible "+visibleItemCount+" total "+totalItemCount);
                    pastVisibleItems = ((LinearLayoutManager)layoutManager).findLastVisibleItemPosition();
                    Log.i(TAG,"past visible "+(pastVisibleItems));

                    if (!loading) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            loading = true;
                            offset = limit + offset;
                            getChatUser();
                        }
                    }

                }
            }
        });

        ImageView imageViewSearch = findViewById(R.id.image_search);
        imageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSearchFragment = new BottomSearchFragment();
                bottomSearchFragment.setCallingActivityName("customerInfoActivity");
                bottomSearchFragment.setMyListItemClickListener(UserListForChatActivity.this);
                bottomSearchFragment.show(getSupportFragmentManager(), "Search Customer Bottom Sheet");
            }
        });

        if(ConnectionDetector.isNetworkAvailable(this))
        getChatUser();
        else
            DialogAndToast.showDialog(getResources().getString(R.string.no_internet),this);
    }

    private void getChatUser(){
        loading = true;
        Map<String,String> params=new HashMap<>();
        params.put("limit",""+limit);
        params.put("offset",""+offset);
        params.put("mobile",sharedPreferences.getString(Constants.MOBILE_NO,""));
        params.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
        String url=getResources().getString(R.string.url)+Constants.GET_CHAT_USERS;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"getChatUsers");
    }

    @Override
    public void onJsonObjectResponse(JSONObject jsonObject, String apiName) {
        try{
            if(apiName.equals("getChatUsers")){
                if(jsonObject.getBoolean("status")){
                    if(!jsonObject.getString("result").equals("1")){
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject dataObject = null;
                        ChatUser item = null;
                        int len = jsonArray.length();
                        for(int i = 0; i<len; i++){
                            dataObject = jsonArray.getJSONObject(i);
                            item = new ChatUser();
                            item.setUserCode(dataObject.getString("userCode"));
                            item.setLastMessage(dataObject.getString("lastMessage"));
                            item.setLastMessageDate(dataObject.getString("lastMessageDate"));
                            item.setUserMobile(dataObject.getString("userMobile"));
                            item.setUserName(dataObject.getString("userName"));
                            item.setUserPic(dataObject.getString("userPic"));
                            if(item.getUserCode().equals("SHP1")){

                            }else{
                                itemList.add(item);
                            }
                        }

                        //myItemAdapter.notifyDataSetChanged();

                        if(itemList.size() > 3){
                            if(len < limit){
                                isScroll = false;
                            }
                            if(len > 0){
                                if(offset == 0){
                                    myItemAdapter.notifyDataSetChanged();
                                }else{
                                    recyclerView.post(new Runnable() {
                                        public void run() {
                                            myItemAdapter.notifyItemRangeInserted(offset,limit);
                                            loading = false;
                                        }
                                    });
                                    Log.d(TAG, "NEXT ITEMS LOADED");
                                }
                            }else{
                                Log.d(TAG, "NO ITEMS FOUND");
                            }
                            recyclerView.scrollToPosition(itemList.size()-1);
                        }else{

                        }
                    }
                }
            }
        }catch (JSONException error){
            error.printStackTrace();
        }
    }

    @Override
    public void onItemClicked(int position) {

    }

    @Override
    public void onItemClicked(Bundle bundle) {
        bottomSearchFragment.dismiss();
        Intent intent = new Intent(UserListForChatActivity.this, ChatActivity.class);
        intent.putExtra("messageTo",bundle.getString("custCode"));
        intent.putExtra("messageToName",bundle.getString("custName"));
        intent.putExtra("messageToMobile",bundle.getString("custMobile"));
        intent.putExtra("messageToPic",bundle.getString("custImage"));
        startActivity(intent);
    }
}
