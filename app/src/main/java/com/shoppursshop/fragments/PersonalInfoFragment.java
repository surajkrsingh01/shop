package com.shoppursshop.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
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

    private Spinner spinnerCountry, spinnerState, spinnerCity;
    List<SpinnerItem> stateListObject, cityListObject, countryListObject;
    List<String> stateList, cityList, countryList;
    private ArrayAdapter<String> stateAdapter, cityAdapter, countryAdapter;
    private EditText editFullName, editAddress, editPincode, editEmail, editMobile, editPassword,
            editConfPassword, editPanCard, editAadharCard, editGstNo;
    private CheckBox checkBoxTerms;
    private Button btnRegister, btnBack;
    private String fullName, address, pincode, email, mobile, password, confPassword, panNo, aadharNo, gstNo, idProof, IMEI;
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
        init();
        IMEI = sharedPreferences.getString(Constants.IMEI_NO,"");
        return rootView;
    }

    private void init() {
        editFullName = (EditText) rootView.findViewById(R.id.edit_full_name);
        editAddress = rootView.findViewById(R.id.edit_address);
        editPincode = rootView.findViewById(R.id.edit_pincode);
        editEmail = (EditText) rootView.findViewById(R.id.edit_email);
        editMobile = (EditText) rootView.findViewById(R.id.edit_mobile);
        editPassword = (EditText) rootView.findViewById(R.id.edit_password);
        editConfPassword = (EditText) rootView.findViewById(R.id.edit_conf_password);
        editPanCard = rootView.findViewById(R.id.edit_pan_card);
        editAadharCard = rootView.findViewById(R.id.edit_aadhar_card);
        editGstNo = rootView.findViewById(R.id.edit_gst_no);
        checkBoxTerms = (CheckBox) rootView.findViewById(R.id.checkbox_terms_condition);

        spinnerCountry = rootView.findViewById(R.id.spinner_country);
        spinnerState = rootView.findViewById(R.id.spinner_state);
        spinnerCity = rootView.findViewById(R.id.spinner_city);
        countryListObject = new ArrayList<>();
        countryList = new ArrayList<>();
        stateListObject = new ArrayList<>();
        stateList = new ArrayList<>();
        cityListObject = new ArrayList<>();
        cityList = new ArrayList<>();


        //countryList.add(0,"Select Country");
        //countryList.add("India");
        //stateList.add("New Delhi");
        countryAdapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_dropdown_list_item, countryList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getView(int position, View convertView,
                                ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(getResources().getColor(R.color.grey500));
                } else {
                    tv.setTextColor(getResources().getColor(R.color.primary_text_color));
                }
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(getResources().getColor(R.color.grey500));
                } else {
                    tv.setTextColor(getResources().getColor(R.color.primary_text_color));
                }
                tv.setPadding(20, 20, 20, 20);
                return view;
            }
        };

        spinnerCountry.setAdapter(countryAdapter);

        // stateList.add(0,"Select State");
        //  stateList.add("Delhi");
        //stateList.add("New Delhi");
        stateAdapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_dropdown_list_item, stateList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getView(int position, View convertView,
                                ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(getResources().getColor(R.color.grey500));
                } else {
                    tv.setTextColor(getResources().getColor(R.color.primary_text_color));
                }
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(getResources().getColor(R.color.grey400));
                } else {
                    tv.setTextColor(getResources().getColor(R.color.primary_text_color));
                }
                tv.setPadding(20, 20, 20, 20);
                return view;
            }
        };

        spinnerState.setAdapter(stateAdapter);

        cityList.add(0, "Select City");
       // cityList.add("Delhi");
        //stateList.add("New Delhi");
        cityAdapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_dropdown_list_item, cityList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getView(int position, View convertView,
                                ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(getResources().getColor(R.color.grey500));
                } else {
                    tv.setTextColor(getResources().getColor(R.color.primary_text_color));
                }
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(getResources().getColor(R.color.grey400));
                } else {
                    tv.setTextColor(getResources().getColor(R.color.primary_text_color));
                }
                tv.setPadding(20, 20, 20, 20);
                return view;
            }
        };

        spinnerCity.setAdapter(cityAdapter);


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

        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    getStates(countryListObject.get(i).getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    getCities(stateListObject.get(i).getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (ConnectionDetector.isNetworkAvailable(getActivity())) {
            getCountries();
        }
    }

    private void attemptRegister() {
        fullName = editFullName.getText().toString();
        address = editAddress.getText().toString();
        pincode = editPincode.getText().toString();
        email = editEmail.getText().toString();
        mobile = editMobile.getText().toString();
        password = editPassword.getText().toString();
        confPassword = editConfPassword.getText().toString();
        panNo = editPanCard.getText().toString();
        aadharNo = editAadharCard.getText().toString();
        gstNo = editGstNo.getText().toString();
        String country = countryList.get(spinnerCountry.getSelectedItemPosition());
        String state = stateList.get(spinnerState.getSelectedItemPosition());
        String city = cityList.get(spinnerCity.getSelectedItemPosition());
        //password="Vipin@12345";
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
        }

        if (TextUtils.isEmpty(email)) {
            focus = editEmail;
            cancel = true;
            editEmail.setError(getResources().getString(R.string.email_required));
        }

        if (TextUtils.isEmpty(pincode)) {
            focus = editPincode;
            cancel = true;
            editPincode.setError(getResources().getString(R.string.pincode_required));
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
            if (state.equals("Select State")) {
                DialogAndToast.showDialog("Please select your state", getActivity());
                return;
            }
            if (state.equals("Select City")) {
                DialogAndToast.showDialog("Please select your city", getActivity());
                return;
            }

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
                    params.put("gstNo", gstNo);
                    params.put("userLat", "0.0");
                    params.put("userLong", "0.0");
                    params.put("createdBy", "Vipin Dhama");
                    params.put("updatedBy", "Vipin Dhama");
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
            if (apiName.equals("countries")) {

                if (response.getBoolean("status")) {
                    JSONArray dataArray = response.getJSONArray("result");
                    JSONObject jsonObject = null;
                    SpinnerItem item = null;
                    int len = dataArray.length();
                    countryListObject.clear();
                    countryList.clear();

                    for (int i = 0; i < len; i++) {
                        jsonObject = dataArray.getJSONObject(i);
                        item = new SpinnerItem();
                        item.setId("" + jsonObject.getInt("id"));
                        item.setName(jsonObject.getString("name"));
                        countryListObject.add(item);
                        countryList.add(item.getName());
                    }
                    countryList.add(0, "Select Country");
                    countryListObject.add(0, new SpinnerItem());
                    countryAdapter.notifyDataSetChanged();

                    if (countryList.size() == 2) {
                        spinnerCountry.setSelection(1);
                    }

                } else {
                    DialogAndToast.showDialog(response.getString("message"), getActivity());
                }
            } else if (apiName.equals("states")) {

                if (response.getBoolean("status")) {
                    JSONArray dataArray = response.getJSONArray("result");
                    JSONObject jsonObject = null;
                    SpinnerItem item = null;
                    int len = dataArray.length();
                    stateListObject.clear();
                    stateList.clear();

                    for (int i = 0; i < len; i++) {
                        jsonObject = dataArray.getJSONObject(i);
                        item = new SpinnerItem();
                        item.setId("" + jsonObject.getInt("id"));
                        item.setName(jsonObject.getString("name"));
                        stateListObject.add(item);
                        stateList.add(item.getName());
                    }
                    stateList.add(0, "Select State");
                    stateListObject.add(0, new SpinnerItem());
                    stateAdapter.notifyDataSetChanged();

                } else {
                    DialogAndToast.showDialog(response.getString("message"), getActivity());
                }
            }else if (apiName.equals("cities")) {

                if (response.getBoolean("status")) {
                    JSONArray dataArray = response.getJSONArray("result");
                    JSONObject jsonObject = null;
                    SpinnerItem item = null;
                    int len = dataArray.length();
                    cityListObject.clear();
                    cityList.clear();

                    for (int i = 0; i < len; i++) {
                        jsonObject = dataArray.getJSONObject(i);
                        item = new SpinnerItem();
                        item.setId("" + jsonObject.getInt("id"));
                        item.setName(jsonObject.getString("name"));
                        cityListObject.add(item);
                        cityList.add(item.getName());
                    }
                    cityList.add(0, "Select City");
                    cityListObject.add(0, new SpinnerItem());
                    cityAdapter.notifyDataSetChanged();

                } else {
                    DialogAndToast.showDialog(response.getString("message"), getActivity());
                }
            } else if (apiName.equals("manageRegistration")) {
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
                    editor.putBoolean(Constants.IS_LOGGED_IN, true);
                    editor.commit();
                    mListener.onFragmentInteraction(myUser, RegisterActivity.PERSONAL);
                } else {

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
}
