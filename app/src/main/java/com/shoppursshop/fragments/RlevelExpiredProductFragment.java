package com.shoppursshop.fragments;


import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.shoppursshop.R;
import com.shoppursshop.activities.product.RlevelAndExpiredProductActivity;
import com.shoppursshop.utilities.DialogAndToast;
import com.shoppursshop.utilities.Utility;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RlevelExpiredProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RlevelExpiredProductFragment extends BottomSheetDialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private DatePickerDialog datePicker1,datePicker2;
    private Calendar calendar,startCal;
    private EditText et_start_date,et_end_date;
    private RadioGroup rg_type;
    private RadioButton rbReorderLevel,rbExpiredProducts;
    private LinearLayout ll_date_layout;
    private int colorTheme,radioChecked;
    private View rootView;


    public RlevelExpiredProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RlevelExpiredProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RlevelExpiredProductFragment newInstance(String param1, String param2,int colorTheme) {
        RlevelExpiredProductFragment fragment = new RlevelExpiredProductFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putInt(ARG_PARAM3, colorTheme);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            colorTheme = getArguments().getInt(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_rlevel_expired_product, container, false);
        init();
        return rootView;
    }

    private void init(){
        rootView.findViewById(R.id.relative_footer_submit).setBackgroundColor(colorTheme);
        ll_date_layout = rootView.findViewById(R.id.ll_date_layout);
        et_start_date = rootView.findViewById(R.id.et_start_date);
        et_end_date = rootView.findViewById(R.id.et_end_date);
        rg_type = rootView.findViewById(R.id.rg_type);
        ImageView iv_clear = rootView.findViewById(R.id.iv_clear);
        TextView tv = rootView.findViewById(R.id.text_action);
        tv.setText("Submit");
        if(colorTheme == getResources().getColor(R.color.white)){
            tv.setTextColor(getResources().getColor(R.color.primary_text_color));
        }else{
            tv.setTextColor(getResources().getColor(R.color.white));
        }

        calendar = Calendar.getInstance();
        startCal = Calendar.getInstance();
        et_start_date.setText(Utility.parseDate(calendar,"yyyy-MM-dd"));
        calendar.add(Calendar.DATE,7);
        et_end_date.setText(Utility.parseDate(calendar,"yyyy-MM-dd"));

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        datePicker1=new DatePickerDialog(getActivity(),new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker datePicker, int yr, int mon, int dy) {
                startCal = Calendar.getInstance();
                startCal.set(Calendar.YEAR,yr);
                startCal.set(Calendar.MONTH,mon);
                startCal.set(Calendar.DATE,dy);
                et_start_date.setText(Utility.parseDate(startCal,"yyyy-MM-dd"));
            }
        },year,month,day);
        datePicker1.setCancelable(false);
        datePicker1.setMessage("Select Start Date");

        datePicker2=new DatePickerDialog(getActivity(),new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker datePicker, int yr, int mon, int dy) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR,yr);
                cal.set(Calendar.MONTH,mon);
                cal.set(Calendar.DATE,dy);
                if(startCal == null){
                    DialogAndToast.showDialog("Please select start date.",getActivity());
                }else if(startCal.getTimeInMillis() > cal.getTimeInMillis()){
                    DialogAndToast.showDialog("Please select end date greater than start date.",getActivity());
                }else{
                    et_end_date.setText(Utility.parseDate(cal,"yyyy-MM-dd"));
                }
            }
        },year,month,day);
        datePicker2.setCancelable(false);
        datePicker2.setMessage("Select End Date");

        et_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker1.show();
            }
        });

        et_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker2.show();
            }
        });

        rg_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
               radioChecked = checkedId;
               if(checkedId == R.id.rb_expired_products){
                   ll_date_layout.setVisibility(View.VISIBLE);
               }else{
                   ll_date_layout.setVisibility(View.GONE);
               }
            }
        });

        iv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RlevelExpiredProductFragment.this.dismiss();
            }
        });

        RelativeLayout relative_footer_action = rootView.findViewById(R.id.relative_footer_submit);
        relative_footer_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmit();
            }
        });
    }

    private void onSubmit(){

       String startDate = et_start_date.getText().toString();
       String endDate = et_end_date.getText().toString();

       if(radioChecked == R.id.rb_expired_products){
           if(TextUtils.isEmpty(startDate)){
               DialogAndToast.showDialog("Please select start date",getActivity());
               return;
           }

           if(TextUtils.isEmpty(endDate)){
               DialogAndToast.showDialog("Please select end date",getActivity());
               return;
           }
       }



        if(radioChecked ==0){
            DialogAndToast.showDialog("Please select reorder level or expired products",getActivity());
            return;
        }

        ((RlevelAndExpiredProductActivity)getActivity()).onSubmitCalled(startDate,endDate,radioChecked);
        RlevelExpiredProductFragment.this.dismiss();

    }

}
