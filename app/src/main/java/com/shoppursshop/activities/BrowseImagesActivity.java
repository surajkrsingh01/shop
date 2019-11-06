package com.shoppursshop.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.shoppursshop.adapters.BrowseImageAdapter;
import com.shoppursshop.interfaces.MyItemClickListener;
import com.shoppursshop.models.MyImage;

import java.util.ArrayList;
import java.util.List;

public class BrowseImagesActivity extends NetworkBaseActivity implements MyItemClickListener {

    private List<MyImage> itemList,itemOriginalList;
    private RecyclerView recyclerView;
    private BrowseImageAdapter itemAdapter;
    private String cat,imagePath;
    private ProgressBar progress_bar;
    private TextView text_no_files;
    private MyImage myImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_images);

        init();
    }

    private void init(){
        cat = getIntent().getStringExtra("cat");
        itemList = new ArrayList<>();
        itemOriginalList = new ArrayList<>();
       // itemList.add("1");
      //  itemList.add("2");
        text_no_files = findViewById(R.id.text_no_files);
        progress_bar = findViewById(R.id.progress_bar);
        recyclerView = findViewById(R.id.recycler_view);
       // recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        itemAdapter=new BrowseImageAdapter(this,itemList);
        itemAdapter.setMyItemClickListener(this);
        recyclerView.setAdapter(itemAdapter);

        EditText editTextSearch = findViewById(R.id.edit_search);
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
               filterImages(s.toString());
            }
        });

        progress_bar.setVisibility(View.VISIBLE);
        browseCategoryImages();
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

    protected void browseCategoryImages(){
        listFiles("cat/"+cat,null);
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
