package com.shoppursshop.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.shoppursshop.R;
import com.shoppursshop.activities.auth.RegisterActivity;
import com.shoppursshop.adapters.SimpleItemAdapter;
import com.shoppursshop.interfaces.MyItemClickListener;
import com.shoppursshop.interfaces.OnFragmentInteraction;
import com.shoppursshop.models.MySimpleItem;
import com.shoppursshop.utilities.ConnectionDetector;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.DialogAndToast;
import com.shoppursshop.utilities.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteraction} interface
 * to handle interaction events.
 * Use the {@link CategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryFragment extends NetworkBaseFragment implements MyItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private SimpleItemAdapter itemAdapter;
    private List<Object> itemList;
    private List<Object> selectedItemList;
    private Button btnBack,btnNext,btnSelectAll;
    private boolean isSelectingAll = true;
    private OnFragmentInteraction mListener;
    private View rootView;

    public CategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryFragment newInstance(String param1, String param2) {
        CategoryFragment fragment = new CategoryFragment();
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
        rootView = inflater.inflate(R.layout.fragment_category, container, false);
        init();
        return rootView;
    }

    private void init(){
        btnBack = rootView.findViewById(R.id.btn_back);
        btnNext = rootView.findViewById(R.id.btn_next);
        btnSelectAll = rootView.findViewById(R.id.btn_select_all);

        itemList = new ArrayList<>();
        selectedItemList = new ArrayList<>();
       /* MySimpleItem item = new MySimpleItem();
        item.setName("Grocery");
        itemList.add(item);
        item = new MySimpleItem();
        item.setName("Stationary");
        itemList.add(item);*/

        recyclerView=rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        // staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        itemAdapter=new SimpleItemAdapter(getActivity(),itemList,"simpleList");
        itemAdapter.setMyItemClickListener(this);
        recyclerView.setAdapter(itemAdapter);

        if(ConnectionDetector.isNetworkAvailable(getActivity())){
            getCategories();
        }


        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                MySimpleItem item1 = null;
                for(Object ob : itemList){
                    item1 = (MySimpleItem) ob;
                    if(item1.isSelected())
                    selectedItemList.add(ob);
                }
                if(selectedItemList.size() == 0){
                    DialogAndToast.showDialog("Please select Category",getActivity());
                    return;
                }

                createCategory();
              //  mListener.onFragmentInteraction(selectedItemList,RegisterActivity.CATEGORY);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        btnSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MySimpleItem item = null;
                for(Object ob : itemList){
                    item = (MySimpleItem) ob;
                    if(isSelectingAll){
                        item.setSelected(true);
                    }else{
                        item.setSelected(false);
                    }

                }

                if(isSelectingAll){
                    btnSelectAll.setText("Unselect All");
                    isSelectingAll = false;
                }else{
                    btnSelectAll.setText("Select All");
                    isSelectingAll = true;
                }

                itemAdapter.notifyDataSetChanged();
            }
        });

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


    private void getCategories(){
        String url=getResources().getString(R.string.url)+Constants.GET_CATEGORY;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(),"categories");
    }

    private void createCategory(){
        JSONArray dataArray = new JSONArray();
        JSONObject dataObject = null;
        MySimpleItem category = null;
        for(Object ob: selectedItemList){
            category = (MySimpleItem)ob;
            dataObject = new JSONObject();
            try {
                dataObject.put("retId",sharedPreferences.getString(Constants.USER_ID,""));
                dataObject.put("retCatId",""+category.getId());
                dataObject.put("catName",category.getName());
                dataObject.put("imageUrl",category.getImage());
                dataObject.put("delStatus","N");
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
        String url=getResources().getString(R.string.url)+Constants.CREATE_CATEGORY;
        showProgress(true);
        jsonArrayV2ApiRequest(Request.Method.POST,url,dataArray,"addCategoryRetailer");
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {

            try {
                if(apiName.equals("categories")){

                    if(response.getBoolean("status")){
                        JSONArray dataArray = response.getJSONArray("result");
                        JSONObject jsonObject =null;
                        MySimpleItem item = null;
                        int len = dataArray.length();
                        for(int i=0; i<len; i++){
                            jsonObject = dataArray.getJSONObject(i);
                            item = new MySimpleItem();
                            item.setId(jsonObject.getInt("catId"));
                            item.setName(jsonObject.getString("catName"));
                            item.setImage(jsonObject.getString("imageUrl"));
                            itemList.add(item);
                        }

                        itemAdapter.notifyDataSetChanged();
                    }else{
                        DialogAndToast.showDialog(response.getString("message"),getActivity());
                    }
                }else if(apiName.equals("addCategoryRetailer")){
                    if(response.getBoolean("status")){
                        MySimpleItem category = null;
                        for(Object ob: selectedItemList){
                            category = (MySimpleItem)ob;
                            if(!dbHelper.isCatExist(""+category.getId()))
                            dbHelper.addCategory(category, Utility.getTimeStamp(),Utility.getTimeStamp());
                        }
                        mListener.onFragmentInteraction(selectedItemList,RegisterActivity.CATEGORY);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
    }

    @Override
    public void onItemClicked(int position) {
        MySimpleItem item = (MySimpleItem) itemList.get(position);
        if(item.isSelected()){
            item.setSelected(false);
        }else{
            item.setSelected(true);
        }

        itemAdapter.notifyItemChanged(position);
    }
}
