package com.shoppursshop.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.shoppursshop.R;
import com.shoppursshop.activities.MainActivity;
import com.shoppursshop.activities.RegisterActivity;
import com.shoppursshop.adapters.SimpleItemAdapter;
import com.shoppursshop.interfaces.MyLevelItemClickListener;
import com.shoppursshop.interfaces.OnFragmentInteraction;
import com.shoppursshop.models.CatListItem;
import com.shoppursshop.models.MySimpleItem;
import com.shoppursshop.utilities.DialogAndToast;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProductFragment} interface
 * to handle interaction events.
 * Use the {@link ProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductFragment extends Fragment implements MyLevelItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<Object> itemCatList,selectedItemList;
    private View rootView;
    private RecyclerView recyclerView;
    private SimpleItemAdapter itemAdapter;
    private List<Object> itemList;
    private Button btnBack,btnNext,btnSkip;
    private OnFragmentInteraction mListener;

    public void setItemCatList(List<Object> itemCatList) {
        this.itemCatList = itemCatList;
    }

    public ProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductFragment newInstance(String param1, String param2) {
        ProductFragment fragment = new ProductFragment();
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
        rootView =  inflater.inflate(R.layout.fragment_product, container, false);
        init();
        return rootView;
    }

    private void init(){
        btnBack = rootView.findViewById(R.id.btn_back);
        btnNext = rootView.findViewById(R.id.btn_next);
        btnSkip = rootView.findViewById(R.id.btn_skip);

        itemList = new ArrayList<>();
        selectedItemList = new ArrayList<>();
        CatListItem item = null;
        MySimpleItem mySimpleItem = null;
        int i=0;
        for(Object ob : itemCatList){
            item = (CatListItem) ob;
            for(Object ob1 : item.getItemList()){
                mySimpleItem = (MySimpleItem)ob1;
                addProduct(mySimpleItem.getName(),i);

            }
            i++;
        }
        recyclerView=rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        // staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        itemAdapter=new SimpleItemAdapter(getActivity(),itemList,"productList");
        itemAdapter.setMyLevelItemClickListener(this);
        recyclerView.setAdapter(itemAdapter);

        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
               /* if(selectedItemList.size() == 0){
                    DialogAndToast.showDialog("Please select Category",getActivity());
                    return;
                }
                mListener.onFragmentInteraction(selectedItemList,RegisterActivity.PRODUCT);*/
            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private void addProduct(String title,int position) {
        CatListItem myItem = new CatListItem();
        myItem.setSelectingAll(true);
        myItem.setTitle(title);
        myItem.setType(1);
        List<Object> subCatList = new ArrayList<>();
        MySimpleItem mySimpleItem = null;
        int limit = 0;
        if(position == 0){
            limit = 5;
        }else{
            limit = (position * 5) + 5;
        }
        for(int i =(position * 5); i<limit; i++){
            mySimpleItem = new MySimpleItem();
            mySimpleItem.setName("Item"+(i+1));
            mySimpleItem.setPosition(position);
            subCatList.add(mySimpleItem);
        }

        myItem.setItemList(subCatList);
        itemList.add(myItem);
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
        }
    }
}
