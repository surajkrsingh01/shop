package com.shoppursshop.activities.product;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.shoppursshop.R;
import com.shoppursshop.activities.base.NetworkBaseActivity;
import com.shoppursshop.adapters.BrowseImageAdapter;
import com.shoppursshop.interfaces.MyItemClickListener;
import com.shoppursshop.models.MyImage;
import com.shoppursshop.utilities.DialogAndToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BrowseImagesActivity extends NetworkBaseActivity implements MyItemClickListener {

    private List<MyImage> itemList,itemOriginalList;
    private RecyclerView recyclerView;
    private BrowseImageAdapter itemAdapter;
    private String cat,imagePath;
    private ProgressBar progress_bar;
    private TextView text_no_files;
    private MyImage myImage;
    private String flag,query;
    private int start = 1,count=10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_images);

        init();
    }

    private void init(){
        flag = getIntent().getStringExtra("flag");
        cat = getIntent().getStringExtra("cat");
        itemList = new ArrayList<>();
        itemOriginalList = new ArrayList<>();
       // itemList.add("1");
      //  itemList.add("2");
        text_no_files = findViewById(R.id.text_no_files);
        progress_bar = findViewById(R.id.progress_bar);
        recyclerView = findViewById(R.id.recycler_view);
       // recyclerView.setHasFixedSize(true);
        final RecyclerView.LayoutManager layoutManager=new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        itemAdapter=new BrowseImageAdapter(this,itemList);
        itemAdapter.setMyItemClickListener(this);
        recyclerView.setAdapter(itemAdapter);

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

                    if(!loading){
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            loading = true;
                            start = start + count;
                            searchGoogleImages();
                        }
                    }
                }
            }
        });

        EditText editTextSearch = findViewById(R.id.edit_search);
        editTextSearch.addTextChangedListener(new TextWatcher() {
            private Timer timer=new Timer();
            private final long DELAY = 1000; // milliseconds
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                query = s.toString();
                if(flag.equals("google")){
                    timer.cancel();
                    timer = new Timer();
                    timer.schedule(
                            new TimerTask() {
                                @Override
                                public void run() {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progress_bar.setVisibility(View.VISIBLE);
                                            searchGoogleImages();
                                        }
                                    });

                                }
                            },
                            DELAY
                    );

                }else{
                    filterImages(s.toString());
                }

            }
        });

        if(flag.equals("firebase")){
            progress_bar.setVisibility(View.VISIBLE);
            browseCategoryImages();
        }

    }

    protected void browseCategoryImages(){
        listFiles("cat/"+cat,null);
    }

    private void searchGoogleImages(){
        loading = true;
        String url = "https://www.googleapis.com/customsearch/v1?cx=008036340262493879777:bd2bp42gndc" +
                "&q="+query+"&key=AIzaSyB-GKvcnqqzEBxT6OvmVPfNs7FBppblo-s&start="+start+"&searchType=image&imgSize=medium" +
                "&fields=searchInformation,items(title,link)";
        googleApiRequest(Request.Method.GET,url,new JSONObject(),"searchGoogle");
    }

    @Override
    public void onJsonObjectResponse(JSONObject jsonObject, String apiName) {
        try {
                if(apiName.equals("searchGoogle")){
                    progress_bar.setVisibility(View.GONE);
                    loading = false;
                    if(jsonObject.has("searchInformation")){
                            if(jsonObject.getJSONObject("searchInformation").getInt("totalResults") > 0){
                                JSONArray jsonArray = jsonObject.getJSONArray("items");
                                JSONObject dataObject = null;
                                int len = jsonArray.length();
                                for(int i=0; i<len; i++){
                                    dataObject = jsonArray.getJSONObject(i);
                                    myImage = new MyImage();
                                    myImage.setUrl(dataObject.getString("link"));
                                    myImage.setName(dataObject.getString("title"));
                                    itemList.add(myImage);
                                    int itemPosition = itemList.size()-1;
                                    Log.i(TAG,"list size "+itemPosition);
                                    itemAdapter.notifyItemInserted(itemPosition);
                                }

                                if(itemList.size() == 0){
                                    showError();
                                }

                            }else{
                                showError();
                            }
                        }else{
                          showError();
                       }
                  }
                }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onServerErrorResponse(VolleyError error, String apiName) {
        super.onServerErrorResponse(error, apiName);
        progress_bar.setVisibility(View.GONE);
        DialogAndToast.showToast("You are not authorize to perform this action",this);
    }

    private void filterImages(String query){
        itemList.clear();
        for(MyImage myImage : itemOriginalList){
            if(myImage.getName().toLowerCase().contains(query.toLowerCase())){
                itemList.add(myImage);
            }
        }

        if(itemList.size() == 0){
            showError();
        }else{
            text_no_files.setVisibility(View.GONE);
            itemAdapter.notifyDataSetChanged();
        }


    }

    public void listFiles(final String dir,String pageToken){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference listRef = storageRef.child("images/"+dir);
// Fetch the next page of results, using the pageToken if we have one.
        Task<ListResult> listPageTask = pageToken != null
                ? listRef.list(100, pageToken)
                : listRef.list(100);

        listPageTask
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        List<StorageReference> prefixes = listResult.getPrefixes();
                        List<StorageReference> items = listResult.getItems();

                        for(StorageReference storageReference : items){
                            Log.i(TAG,"storage "+storageReference.getBucket());
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Log.i(TAG,"Downloaded "+uri.toString());
                                    myImage = new MyImage();
                                    myImage.setUrl(uri.toString());
                                    myImage.setName(uri.getLastPathSegment().substring(uri.getLastPathSegment().lastIndexOf("/")+1));
                                    itemList.add(myImage);
                                    itemOriginalList.add(myImage);
                                    int itemPosition = itemList.size()-1;
                                    Log.i(TAG,"list size "+itemPosition);
                                    itemAdapter.notifyItemInserted(itemPosition);
                                   // itemAdapter.notifyItemInserted(itemPosition);
                                  //  itemAdapter.notifyDataSetChanged();
                                    if(progress_bar.getVisibility() == View.VISIBLE){
                                        progress_bar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }
                        if(items == null || items.size() == 0){
                            showError();
                        }

                        // Recurse onto next page
                        if (listResult.getPageToken() != null) {
                            Log.i(TAG,"Loading next itmes...");
                            listFiles(dir,listResult.getPageToken());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG,"failed..."+e.getMessage());
                showError();
                // Uh-oh, an error occurred.
            }
        });


    }

    private void showError(){
        text_no_files.setVisibility(View.VISIBLE);
        progress_bar.setVisibility(View.GONE);
    }

    @Override
    public void onItemClicked(int position) {
        MyImage image = itemList.get(position);
        imagePath = image.getUrl();
        Intent intent = new Intent();
        intent.putExtra("imagePath",imagePath);
        setResult(-1,intent);
        finish();
    }
}
