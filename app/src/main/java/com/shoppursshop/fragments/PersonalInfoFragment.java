package com.shoppursshop.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.shoppursshop.R;
import com.shoppursshop.activities.MainActivity;
import com.shoppursshop.activities.RegisterActivity;
import com.shoppursshop.activities.settings.profile.AddressActivity;
import com.shoppursshop.interfaces.OnFragmentInteraction;
import com.shoppursshop.models.MySimpleItem;
import com.shoppursshop.models.MyUser;
import com.shoppursshop.models.SpinnerItem;
import com.shoppursshop.utilities.ConnectionDetector;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.DialogAndToast;
import com.shoppursshop.utilities.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteraction} interface
 * to handle interaction events.
 * Use the {@link PersonalInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonalInfoFragment extends NetworkBaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String language, mParam1;
    private String mParam2;
    private EditText editFullName, editAddress, editEmail, editMobile, editPassword,
            editConfPassword, editPanCard, editAadharCard, editGstNo;
    private CheckBox checkBoxTerms;
    private Button btnRegister, btnBack;
    private String fullName, address,country,state,city, pincode, email, mobile,
            password, confPassword, panNo, aadharNo, gstNo, idProof, IMEI;
    private double latitude,longitude;
    private MyUser myUser;
    private View rootView;

    private OnFragmentInteraction mListener;

    public void setLanguage(String language) {
        this.language = language;
    }

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
        IMEI = sharedPreferences.getString(Constants.IMEI_NO,"");
        editor.putString(Constants.GOOGLE_MAP_API_KEY,"AIzaSyB-GKvcnqqzEBxT6OvmVPfNs7FBppblo-s");
        editor.commit();
        init();
        return rootView;
    }

    private void init() {
        editFullName = (EditText) rootView.findViewById(R.id.edit_full_name);
        editAddress = rootView.findViewById(R.id.edit_address);
        //editPincode = rootView.findViewById(R.id.edit_pincode);
        editEmail = (EditText) rootView.findViewById(R.id.edit_email);
        editMobile = (EditText) rootView.findViewById(R.id.edit_mobile);
        editPassword = (EditText) rootView.findViewById(R.id.edit_password);
        editConfPassword = (EditText) rootView.findViewById(R.id.edit_conf_password);
        editPanCard = rootView.findViewById(R.id.edit_pan_card);
        editAadharCard = rootView.findViewById(R.id.edit_aadhar_card);
        editGstNo = rootView.findViewById(R.id.edit_gst_no);
        checkBoxTerms = (CheckBox) rootView.findViewById(R.id.checkbox_terms_condition);

        editAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddressActivity.class);
                intent.putExtra("flag","addressForRegister");
                startActivityForResult(intent,10);
            }
        });

        btnRegister = (Button) rootView.findViewById(R.id.btn_register);
        btnBack = (Button) rootView.findViewById(R.id.btn_back);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myUser = new MyUser();
                //mListener.onFragmentInteraction(myUser,RegisterActivity.PERSONAL);
                attemptRegister();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }

    private void attemptRegister() {
        fullName = editFullName.getText().toString();
        address = editAddress.getText().toString();
        email = editEmail.getText().toString();
        mobile = editMobile.getText().toString();
        password = editPassword.getText().toString();
        confPassword = editConfPassword.getText().toString();
        panNo = editPanCard.getText().toString();
        aadharNo = editAadharCard.getText().toString();
        // confPassword="Vipin@12345";
        boolean isChecked = checkBoxTerms.isChecked();

        if (!TextUtils.isEmpty(aadharNo)) {
            idProof = "Aadhar Card";
        } else if (!TextUtils.isEmpty(panNo)) {
            idProof = "Pan Card";
        }


        View focus = null;
        boolean cancel = false;

        if (TextUtils.isEmpty(password)) {
            focus = editPassword;
            cancel = true;
            editPassword.setError(getResources().getString(R.string.password_required));
        } else if (!password.equals(confPassword)) {
            focus = editPassword;
            cancel = true;
            editPassword.setError(getResources().getString(R.string.password_not_match));
        }

        if (TextUtils.isEmpty(mobile)) {
            focus = editMobile;
            cancel = true;
            editMobile.setError(getResources().getString(R.string.mobile_required));
        }else if (mobile.length() != 10) {
            focus = editMobile;
            cancel = true;
            editMobile.setError(getResources().getString(R.string.mobile_valid));
        }

        if (TextUtils.isEmpty(email)) {
            focus = editEmail;
            cancel = true;
            editEmail.setError(getResources().getString(R.string.email_required));
        }

        if (TextUtils.isEmpty(address)) {
            focus = editAddress;
            cancel = true;
            editAddress.setError(getResources().getString(R.string.address_required));
        }

        if (TextUtils.isEmpty(fullName)) {
            focus = editFullName;
            cancel = true;
            editFullName.setError(getResources().getString(R.string.full_name_required));
        }

        if (cancel) {
            focus.requestFocus();
            return;
        } else {
            if (isChecked) {
                if (ConnectionDetector.isNetworkAvailable(getActivity())) {
                    progressDialog.setMessage(getResources().getString(R.string.creating_account));
                    Map<String, String> params = new HashMap<>();

                    params.put("shopCode", "shop_" + mobile);
                    params.put("username", fullName);
                    params.put("shopName", "");
                    params.put("userEmail", email);
                    params.put("mobile", mobile);
                    params.put("mpassword", password);
                    params.put("language", language);
                    params.put("city", city);
                    params.put("province", state);
                    params.put("country", country);
                    params.put("zip", pincode);
                    params.put("address", address);
                    params.put("imeiNo", IMEI);
                    params.put("photo", "");
                    params.put("idProof", idProof);
                    params.put("panNo", panNo);
                    params.put("aadharNo", aadharNo);
                    params.put("gstNo", "");
                    params.put("userLat", ""+latitude);
                    params.put("userLong", ""+longitude);
                    params.put("createdBy", fullName);
                    params.put("updatedBy", fullName);
                    params.put("userType", "Seller");
                    params.put("isActive", "1");

                    String url = getResources().getString(R.string.url) + Constants.MANAGE_REGISTRATION;
                    showProgress(true);
                    jsonObjectApiRequest(Request.Method.POST, url, new JSONObject(params), "manageRegistration");
                   /* editor.putString(Constants.FULL_NAME,fullName);
                    editor.putString(Constants.EMAIL,email);
                    editor.putString(Constants.MOBILE_NO,mobile);
                    editor.putBoolean(Constants.IS_LOGGED_IN,true);
                    editor.commit();*/

                } else {
                    DialogAndToast.showDialog(getResources().getString(R.string.no_internet), getActivity());
                }

            } else {
                DialogAndToast.showDialog(getResources().getString(R.string.accept_terms), getActivity());
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

    private void getCountries() {
        String url = getResources().getString(R.string.url) + Constants.GET_COUNTRIES;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST, url, new JSONObject(), "countries");
    }

    private void getStates(String countryId) {
        String url = getResources().getString(R.string.url) + Constants.GET_STATES + countryId;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST, url, new JSONObject(), "states");
    }

    private void getCities(String stateId) {
        String url = getResources().getString(R.string.url) + Constants.GET_CITIES + stateId;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST, url, new JSONObject(), "cities");
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {

        try {
            if (apiName.equals("manageRegistration")) {
                if (response.getBoolean("status")) {
                    JSONObject dataObject = response.getJSONObject("result");
                    editor.putString(Constants.USER_ID, dataObject.getString("id"));
                    editor.putString(Constants.FULL_NAME, dataObject.getString("username"));
                    editor.putString(Constants.EMAIL, dataObject.getString("userEmail"));
                    editor.putString(Constants.MOBILE_NO, dataObject.getString("mobile"));
                    editor.putString(Constants.SHOP_NAME, dataObject.getString("shopName"));
                    editor.putString(Constants.SHOP_CODE, dataObject.getString("shopCode"));
                    editor.putString(Constants.LANGUAGE, dataObject.getString("language"));
                    //myUser.setMpassword(dataObject.getString("RET_CODE"));
                    editor.putString(Constants.CITY, dataObject.getString("city"));
                    editor.putString(Constants.STATE, dataObject.getString("province"));
                    editor.putString(Constants.COUNTRY, dataObject.getString("country"));
                    editor.putString(Constants.ZIP, dataObject.getString("zip"));
                    editor.putString(Constants.ADDRESS, dataObject.getString("address"));
                    editor.putString(Constants.PHOTO, dataObject.getString("photo"));
                    //myUser.setIdProof(dataObject.getString("RET_CODE"));
                    editor.putString(Constants.PAN_NO, dataObject.getString("panNo"));
                    editor.putString(Constants.AADHAR_NO, dataObject.getString("aadharNo"));
                    editor.putString(Constants.GST_NO, dataObject.getString("gstNo"));
                    editor.putString(Constants.USER_LAT, dataObject.getString("userLat"));
                    editor.putString(Constants.USER_LONG, dataObject.getString("userLong"));
                    editor.putString(Constants.DB_NAME, dataObject.getString("dbName"));
                    editor.putString(Constants.DB_USER_NAME, dataObject.getString("dbUserName"));
                    editor.putString(Constants.DB_PASSWORD, dataObject.getString("dbPassword"));
                    editor.putString(Constants.USER_TYPE, "Seller");
                    editor.putString(Constants.TOKEN,dataObject.getString("token"));
                    token = dataObject.getString("token");
                    editor.putString(Constants.GOOGLE_MAP_API_KEY,dataObject.getString("googleMapApiKey"));
                    editor.putBoolean(Constants.IS_LOGGED_IN, true);
                    editor.commit();
                    mListener.onFragmentInteraction(myUser, RegisterActivity.PERSONAL);
                } else {
                    DialogAndToast.showDialog(response.getString("message"), getActivity());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( requestCode == 10 ) {
           if(data != null){
               address = data.getStringExtra("address");
               country = data.getStringExtra("country");
               state = data.getStringExtra("state");
               city = data.getStringExtra("city");
               pincode = data.getStringExtra("pin");
               latitude = data.getDoubleExtra("latitude",0d);
               longitude = data.getDoubleExtra("longitude",0d);
               editAddress.setText(address);
               Log.i(TAG,"address "+address);
           }
        }
    }
}
