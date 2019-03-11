package com.shoppursshop.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.shoppursshop.R;
import com.shoppursshop.activities.RegisterActivity;
import com.shoppursshop.adapters.SimpleItemAdapter;
import com.shoppursshop.interfaces.MyLevelItemClickListener;
import com.shoppursshop.interfaces.OnFragmentInteraction;
import com.shoppursshop.models.CatListItem;
import com.shoppursshop.models.MySimpleItem;
import com.shoppursshop.models.SubCategory;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.DialogAndToast;
import com.shoppursshop.utilities.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.CheckedOutputStream;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteraction} interface
 * to handle interaction events.
 * Use the {@link SubCatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubCatFragment extends NetworkBaseFragment implements MyLevelItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String catIds;
    private int tempCatId;
    private List<Object> itemCatList,selectedItemList;
    private View rootView;
    private RecyclerView recyclerView;
    private SimpleItemAdapter itemAdapter;
    private List<Object> itemList;
    private Button btnBack,btnNext;
    private OnFragmentInteraction mListener;

    public void setItemCatList(List<Object> itemCatList) {
        this.itemCatList = itemCatList;
    }

    public SubCatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SubCatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SubCatFragment newInstance(String param1, String param2) {
        SubCatFragment fragment = new SubCatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_sub_cat, container, false);
        init();
        return rootView;
    }

    private void init(){
        btnBack = rootView.findViewById(R.id.btn_back);
        btnNext = rootView.findViewById(R.id.btn_next);

        itemList = new ArrayList<>();
        selectedItemList = new ArrayList<>();
        MySimpleItem item = null;
        itemCatList = dbHelper.getCategories();
        for(Object ob : itemCatList){
            item = (MySimpleItem) ob;
            Log.i(TAG,"Cat "+item.getName());
            if(catIds == null){
                catIds = ""+item.getId();
            }else{
                catIds = catIds+","+item.getId();
            }
        }
        getSubCategories(catIds);
        //addGrocery();
       // addStationary();
        recyclerView=rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        // staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        itemAdapter=new SimpleItemAdapter(getActivity(),itemList,"subCatList");
        itemAdapter.setMyLevelItemClickListener(this);
        recyclerView.setAdapter(itemAdapter);

        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                MySimpleItem item1 = null;
                CatListItem catListItem = null,mySelectedCatItem = null;
                List<Object> selectedList = null;
                for(Object ob : itemList){
                    catListItem = (CatListItem) ob;
                    selectedList = new ArrayList<>();
                    mySelectedCatItem = new CatListItem();
                    mySelectedCatItem.setId(catListItem.getId());
                    mySelectedCatItem.setTitle(catListItem.getTitle());
                    mySelectedCatItem.setDesc(catListItem.getDesc());
                    for(Object ob1 : catListItem.getItemList()){
                        item1 = (MySimpleItem) ob1;
                        if(item1.isSelected())
                            selectedList.add(ob1);
                    }
                    if(selectedList.size() > 0){
                        mySelectedCatItem.setItemList(selectedList);
                        selectedItemList.add(mySelectedCatItem);
                    }

                }
                if(selectedItemList.size() == 0){
                    DialogAndToast.showDialog("Please select Category",getActivity());
                    return;
                }
                createSubCategory();
                //mListener.onFragmentInteraction(selectedItemList,RegisterActivity.SUB_CATEGORY);
            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }

    private void getSubCategories(String catIds){
        String url=getResources().getString(R.string.url)+ Constants.GET_SUB_CATEGORY +catIds;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(),"sub_categories");
    }

    private void createSubCategory(){
        JSONArray dataArray = new JSONArray();
        JSONObject dataObject = null;
        CatListItem category = null;
        MySimpleItem subCat = null;
        for(Object ob: selectedItemList){
            category = (CatListItem)ob;
            for(Object ob1 : category.getItemList()){
                subCat = (MySimpleItem) ob1;
                dataObject = new JSONObject();
                try {
                    dataObject.put("retId",sharedPreferences.getString(Constants.USER_ID,""));
                    dataObject.put("retCatId",""+category.getId());
                    dataObject.put("retSubCatId",""+subCat.getId());
                    dataObject.put("catName",subCat.getName());
                    dataObject.put("imageUrl",subCat.getImage());
                    dataObject.put("delStatus","N");
                    dataObject.put("retShopCode",sharedPreferences.getString(Constants.SHOP_CODE,""));
                    dataObject.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
                    dataObject.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
                    dataObject.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
                    dataObject.put("createdBy","Vipin Dhama");
                    dataObject.put("updatedBy","Vipin Dhama");
                    dataArray.put(dataObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
        String url=getResources().getString(R.string.url)+Constants.CREATE_SUB_CATEGORY;
        showProgress(true);
        jsonArrayV2ApiRequest(Request.Method.POST,url,dataArray,"addSubCategoryRetailer");
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {

        try {
            if(apiName.equals("sub_categories")){

                if(response.getBoolean("status")){
                    JSONArray dataArray = response.getJSONArray("result");
                    JSONObject jsonObject =null;
                    CatListItem myItem = null;
                    MySimpleItem mySimpleItem = null;
                    List<Object> subCatList = null;
                    int len = dataArray.length();
                    for(int i=0; i<len; i++){
                        jsonObject = dataArray.getJSONObject(i);
                        if(tempCatId == jsonObject.getInt("catId")){
                            myItem = getItem(jsonObject.getInt("catId"));
                            subCatList = myItem.getItemList();
                        }else{
                            myItem = new CatListItem();
                            myItem.setSelectingAll(true);
                            myItem.setType(1);
                            subCatList = new ArrayList<>();
                            tempCatId = jsonObject.getInt("catId");
                            myItem.setId(jsonObject.getInt("catId"));
                            myItem.setTitle(dbHelper.getCategoryName(""+jsonObject.getInt("catId")));
                            myItem.setItemList(subCatList);
                            itemList.add(myItem);
                        }
                        mySimpleItem = new MySimpleItem();
                        mySimpleItem.setId(jsonObject.getInt("subCatId"));
                        mySimpleItem.setName(jsonObject.getString("subCatName"));
                        mySimpleItem.setImage(jsonObject.getString("imageUrl"));
                        mySimpleItem.setPosition(itemList.size());
                        subCatList.add(mySimpleItem);
                    }

                    itemAdapter.notifyDataSetChanged();
                }else{
                    DialogAndToast.showDialog(response.getString("message"),getActivity());
                }
            }else if(apiName.equals("addSubCategoryRetailer")){
                if(response.getBoolean("status")){
                    CatListItem category = null;
                    MySimpleItem subCat = null;
                    for(Object ob: selectedItemList){
                        category = (CatListItem)ob;
                        for(Object ob1 : category.getItemList()) {
                            subCat = (MySimpleItem) ob1;
                            dbHelper.addSubCategory(subCat,""+category.getId(),Utility.getTimeStamp(),Utility.getTimeStamp());
                        }
                    }
                    mListener.onFragmentInteraction(selectedItemList,RegisterActivity.SUB_CATEGORY);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private CatListItem getItem(int id){
        CatListItem item = null;
        for(Object ob: itemList){
            item = (CatListItem)ob;
            if(item.getId() == id){
                break;
            }
        }

        return item;
    }


    private void addGrocery(){
        CatListItem myItem = new CatListItem();
        myItem.setSelectingAll(true);
        myItem.setTitle("Grocery");
        myItem.setType(1);
        List<Object> subCatList = new ArrayList<>();
        MySimpleItem mySimpleItem = new MySimpleItem();
        mySimpleItem.setName("Breakfast & Dairy");
        mySimpleItem.setPosition(0);
        subCatList.add(mySimpleItem);

        mySimpleItem = new MySimpleItem();
        mySimpleItem.setName("Masala & Spices");
        mySimpleItem.setPosition(0);
        subCatList.add(mySimpleItem);

        mySimpleItem = new MySimpleItem();
        mySimpleItem.setName("Personal Care");
        mySimpleItem.setPosition(0);
        subCatList.add(mySimpleItem);

        mySimpleItem = new MySimpleItem();
        mySimpleItem.setName("Beverages");
        mySimpleItem.setPosition(0);
        subCatList.add(mySimpleItem);
        myItem.setItemList(subCatList);
        itemList.add(myItem);
    }

    private void addStationary(){
        CatListItem myItem = new CatListItem();
        myItem.setSelectingAll(true);
        myItem.setTitle("Stationary");
        myItem.setType(1);
        List<Object> subCatList = new ArrayList<>();
        MySimpleItem mySimpleItem = new MySimpleItem();
        mySimpleItem.setName("Pen & Pen Sets");
        mySimpleItem.setPosition(1);
        subCatList.add(mySimpleItem);

        mySimpleItem = new MySimpleItem();
        mySimpleItem.setName("Notebooks");
        mySimpleItem.setPosition(1);
        subCatList.add(mySimpleItem);

        mySimpleItem = new MySimpleItem();
        mySimpleItem.setName("Papers");
        mySimpleItem.setPosition(1);
        subCatList.add(mySimpleItem);

        mySimpleItem = new MySimpleItem();
        mySimpleItem.setName("Color & Paints");
        mySimpleItem.setPosition(1);
        subCatList.add(mySimpleItem);

        mySimpleItem = new MySimpleItem();
        mySimpleItem.setName("Desk Organizer");
        mySimpleItem.setPosition(1);
        subCatList.add(mySimpleItem);

        mySimpleItem = new MySimpleItem();
        mySimpleItem.setName("Markers");
        mySimpleItem.setPosition(1);
        subCatList.add(mySimpleItem);
        myItem.setItemList(subCatList);
        itemList.add(myItem);
    }

    private void addBoth(){
        addGrocery();
        addStationary();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteraction) {
            mListener = (OnFragmentInteraction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onLevelItemClicked(int itemPosition, int level) {
       if(level == -1){
           CatListItem item = (CatListItem) itemList.get(itemPosition);
           List<Object> simpleItemList = item.getItemList();
           MySimpleItem item1 = null;
           for(Object ob : simpleItemList){
               item1 = (MySimpleItem)ob;
               if(item.isSelectingAll()){
                   item1.setSelected(true);
               }else{
                   item1.setSelected(false);
               }

           }
           if(item.isSelectingAll()){
               item.setSelectingAll(false);
           }else{
               item.setSelectingAll(true);
           }

           itemAdapter.notifyItemChanged(itemPosition);
       }else{
           CatListItem item = (CatListItem) itemList.get(level);
           List<Object> simpleItemList = item.getItemList();
           MySimpleItem item1 = (MySimpleItem) simpleItemList.get(itemPosition);
           if(item1.isSelected()){
               item1.setSelected(false);
           }else{
               item1.setSelected(true);
           }

           itemAdapter.notifyItemChanged(level);
       }
    }
}
