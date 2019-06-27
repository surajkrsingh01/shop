package com.shoppursshop.fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.shoppursshop.R;
import com.shoppursshop.activities.RegisterActivity;
import com.shoppursshop.adapters.SimpleItemAdapter;
import com.shoppursshop.interfaces.MyItemClickListener;
import com.shoppursshop.interfaces.OnFragmentInteraction;
import com.shoppursshop.models.MySimpleItem;
import com.shoppursshop.utilities.DialogAndToast;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteraction} interface
 * to handle interaction events.
 * Use the {@link LanguageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LanguageFragment extends Fragment implements MyItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private SimpleItemAdapter languageAdapter;
    private List<Object> languageList;
    private Button btnBack,btnNext;
    private int selectedPosition = -1,preSelectedPosition=-1;

    private OnFragmentInteraction mListener;

    public LanguageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LanguageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LanguageFragment newInstance(String param1, String param2) {
        LanguageFragment fragment = new LanguageFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_language, container, false);
        init(rootView);
        return rootView;
    }

    private void init(View rootView){
        btnBack = rootView.findViewById(R.id.btn_back);
        btnNext = rootView.findViewById(R.id.btn_next);

        languageList = new ArrayList<>();
        MySimpleItem item = new MySimpleItem();
        item.setName("Hindi");
        languageList.add(item);
        item = new MySimpleItem();
        item.setName("English");
        languageList.add(item);

        recyclerView=rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        // staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        languageAdapter=new SimpleItemAdapter(getActivity(),languageList,"simpleList");
        languageAdapter.setMyItemClickListener(this);
        recyclerView.setAdapter(languageAdapter);


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedPosition == -1){
                    DialogAndToast.showDialog("Please select Language",getActivity());
                    return;
                }
                MySimpleItem item = (MySimpleItem) languageList.get(selectedPosition);
                mListener.onFragmentInteraction(item.getName(),RegisterActivity.LANGUAGE);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
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

    @Override
    public void onItemClicked(int position) {
        if(selectedPosition != position){
            selectedPosition = position;
            MySimpleItem item = (MySimpleItem) languageList.get(selectedPosition);
            item.setSelected(true);
            languageAdapter.notifyItemChanged(selectedPosition);
            if(preSelectedPosition != -1){
                MySimpleItem preItem = (MySimpleItem) languageList.get(preSelectedPosition);
                preItem.setSelected(false);
                languageAdapter.notifyItemChanged(preSelectedPosition);
            }
            preSelectedPosition = position;
        }

    }

    @Override
    public void onResume(){
        super.onResume();
        if(selectedPosition != -1){
            MySimpleItem item = (MySimpleItem) languageList.get(selectedPosition);
            item.setSelected(true);
            languageAdapter.notifyItemChanged(selectedPosition);
        }

    }
}
