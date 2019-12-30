package com.shoppursshop.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.shoppursshop.R;
import com.shoppursshop.interfaces.MyItemClickListener;
import com.shoppursshop.interfaces.MyItemTypeClickListener;
import com.shoppursshop.utilities.DialogAndToast;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReferAppDialogFragment extends BottomSheetDialogFragment {
    private static final String ARG_PARAM1 = "param1";
    private RadioGroup rgInvite;
    private RadioButton rbCustomer,rbShop;
    private int colorTheme,radioChecked;
    private View rootView;

    private MyItemTypeClickListener myItemClickListener;

    public void setMyItemClickListener(MyItemTypeClickListener myItemClickListener) {
        this.myItemClickListener = myItemClickListener;
    }



    public ReferAppDialogFragment() {
        // Required empty public constructor
    }

    public static ReferAppDialogFragment newInstance(int colorTheme) {
        ReferAppDialogFragment fragment = new ReferAppDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, colorTheme);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            colorTheme = getArguments().getInt(ARG_PARAM1);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_refer_app_dialog, container, false);
        init();
        return rootView;
    }

    private void init(){
        RelativeLayout relative_footer_action = rootView.findViewById(R.id.relative_footer_submit);
        relative_footer_action.setBackgroundColor(colorTheme);
        rgInvite = rootView.findViewById(R.id.rgInvite);
        ImageView iv_clear = rootView.findViewById(R.id.iv_clear);
        TextView tv = rootView.findViewById(R.id.text_action);
        tv.setText("Submit");
        if(colorTheme == getResources().getColor(R.color.white)){
            tv.setTextColor(getResources().getColor(R.color.primary_text_color));
        }else{
            tv.setTextColor(getResources().getColor(R.color.white));
        }

        iv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReferAppDialogFragment.this.dismiss();
            }
        });

        rgInvite.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioChecked = checkedId;
            }
        });


        relative_footer_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmit();
            }
        });

    }

    private void onSubmit(){
        if(radioChecked ==0){
            DialogAndToast.showDialog("Please select Customer or Shop",getActivity());
            return;
        }
        myItemClickListener.onItemClicked(0,radioChecked);
        ReferAppDialogFragment.this.dismiss();
    }

}
