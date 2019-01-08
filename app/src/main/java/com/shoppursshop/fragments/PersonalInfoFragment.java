package com.shoppursshop.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.shoppursshop.R;
import com.shoppursshop.activities.MainActivity;
import com.shoppursshop.activities.RegisterActivity;
import com.shoppursshop.interfaces.OnFragmentInteraction;
import com.shoppursshop.models.MyUser;
import com.shoppursshop.models.SpinnerItem;
import com.shoppursshop.utilities.ConnectionDetector;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.DialogAndToast;
import com.shoppursshop.utilities.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteraction} interface
 * to handle interaction events.
 * Use the {@link PersonalInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonalInfoFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Spinner spinnerCountry,spinnerState,spinnerCity;
    List<SpinnerItem> stateListObject,cityListObject,countryListObject;
    List<String> stateList,cityList,countryList;
    private ArrayAdapter<String> stateAdapter,cityAdapter,countryAdapter;
    private EditText editFullName,editEmail,editMobile,editPassword,editConfPassword;
    private CheckBox checkBoxTerms;
    private Button btnRegister,btnBack;
    private String fullName,email,mobile,password,confPassword;
    private MyUser myUser;
    private View rootView;

    private OnFragmentInteraction mListener;

    public PersonalInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PersonalInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PersonalInfoFragment newInstance(String param1, String param2) {
        PersonalInfoFragment fragment = new PersonalInfoFragment();
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
        rootView = inflater.inflate(R.layout.fragment_personal_info, container, false);
        init();
        return rootView;
    }

    private void init(){
        editFullName=(EditText)rootView.findViewById(R.id.edit_full_name);
        editEmail=(EditText)rootView.findViewById(R.id.edit_email);
        editMobile=(EditText)rootView.findViewById(R.id.edit_mobile);
        editPassword=(EditText)rootView.findViewById(R.id.edit_password);
        editConfPassword=(EditText)rootView.findViewById(R.id.edit_conf_password);
        checkBoxTerms=(CheckBox)rootView.findViewById(R.id.checkbox_terms_condition);

        spinnerCountry = rootView.findViewById(R.id.spinner_country);
        spinnerState = rootView.findViewById(R.id.spinner_state);
        spinnerCity = rootView.findViewById(R.id.spinner_city);
        countryListObject = new ArrayList<>();
        countryList = new ArrayList<>();
        stateListObject = new ArrayList<>();
        stateList = new ArrayList<>();
        cityListObject = new ArrayList<>();
        cityList = new ArrayList<>();

        countryList.add(0,"Select Country");
        countryList.add("India");
        //stateList.add("New Delhi");
        countryAdapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_dropdown_list_item, countryList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0){
                    return false;
                }else{
                    return true;
                }
            }
            @Override
            public View getView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(getResources().getColor(R.color.grey500));
                }else{
                    tv.setTextColor(getResources().getColor(R.color.primary_text_color));
                }
                return view;
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(getResources().getColor(R.color.grey500));
                }else{
                    tv.setTextColor(getResources().getColor(R.color.primary_text_color));
                }
                tv.setPadding(20,20,20,20);
                return view;
            }
        };

        spinnerCountry.setAdapter(countryAdapter);

        stateList.add(0,"Select State");
        stateList.add("Delhi");
        //stateList.add("New Delhi");
        stateAdapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_dropdown_list_item, stateList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0){
                    return false;
                }else{
                    return true;
                }
            }
            @Override
            public View getView(int position, View convertView,
                                ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(getResources().getColor(R.color.grey500));
                }else{
                    tv.setTextColor(getResources().getColor(R.color.primary_text_color));
                }
                return view;
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(getResources().getColor(R.color.grey400));
                }else{
                    tv.setTextColor(getResources().getColor(R.color.primary_text_color));
                }
                tv.setPadding(20,20,20,20);
                return view;
            }
        };

        spinnerState.setAdapter(stateAdapter);

        cityList.add(0,"Select City");
        cityList.add("Delhi");
        //stateList.add("New Delhi");
        cityAdapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_dropdown_list_item, cityList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0){
                    return false;
                }else{
                    return true;
                }
            }
            @Override
            public View getView(int position, View convertView,
                                ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(getResources().getColor(R.color.grey500));
                }else{
                    tv.setTextColor(getResources().getColor(R.color.primary_text_color));
                }
                return view;
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(getResources().getColor(R.color.grey400));
                }else{
                    tv.setTextColor(getResources().getColor(R.color.primary_text_color));
                }
                tv.setPadding(20,20,20,20);
                return view;
            }
        };

        spinnerCity.setAdapter(cityAdapter);


        btnRegister=(Button)rootView.findViewById(R.id.btn_register);
        btnBack=(Button)rootView.findViewById(R.id.btn_back);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myUser = new MyUser();
                mListener.onFragmentInteraction(myUser,RegisterActivity.PERSONAL);
                // attemptRegister();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }

    private void attemptRegister(){
        fullName=editFullName.getText().toString();
        email=editEmail.getText().toString();
        mobile=editMobile.getText().toString();
        //  location=editLocation.getText().toString();
        // password=editPassword.getText().toString();
        // confPassword=editConfPassword.getText().toString();
        password="Vipin@12345";
        confPassword="Vipin@12345";
        boolean isChecked=checkBoxTerms.isChecked();

        View focus=null;
        boolean cancel=false;

        if(TextUtils.isEmpty(password)){
            focus=editPassword;
            cancel=true;
            editPassword.setError(getResources().getString(R.string.password_required));
        }else if(!password.equals(confPassword)){
            focus=editPassword;
            cancel=true;
            editPassword.setError(getResources().getString(R.string.password_not_match));
        }

        if(TextUtils.isEmpty(mobile)){
            focus=editMobile;
            cancel=true;
            editMobile.setError(getResources().getString(R.string.mobile_required));
        }

        if(TextUtils.isEmpty(email)){
            focus=editEmail;
            cancel=true;
            editEmail.setError(getResources().getString(R.string.email_required));
        }

        if(TextUtils.isEmpty(fullName)){
            focus=editFullName;
            cancel=true;
            editFullName.setError(getResources().getString(R.string.full_name_required));
        }

        if(cancel){
            focus.requestFocus();
            return;
        }else {
            if(isChecked){
                if(ConnectionDetector.isNetworkAvailable(getActivity())) {
                    progressDialog.setMessage(getResources().getString(R.string.creating_account));
                    editor.putString(Constants.FULL_NAME,fullName);
                    editor.putString(Constants.EMAIL,email);
                    editor.putString(Constants.MOBILE_NO,mobile);
                    editor.putBoolean(Constants.IS_LOGGED_IN,true);
                    editor.commit();

                }else {
                    DialogAndToast.showDialog(getResources().getString(R.string.no_internet),getActivity());
                }

            }else {
                DialogAndToast.showDialog(getResources().getString(R.string.accept_terms),getActivity());
            }

        }
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
