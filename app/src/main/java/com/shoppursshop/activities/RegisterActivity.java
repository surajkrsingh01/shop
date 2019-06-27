package com.shoppursshop.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.shoppursshop.R;
import com.shoppursshop.fragments.BankFragment;
import com.shoppursshop.fragments.CategoryFragment;
import com.shoppursshop.fragments.LanguageFragment;
import com.shoppursshop.fragments.PersonalInfoFragment;
import com.shoppursshop.fragments.ProductFragment;
import com.shoppursshop.fragments.SubCatFragment;
import com.shoppursshop.interfaces.OnFragmentInteraction;
import com.shoppursshop.models.CatListItem;
import com.shoppursshop.models.Category;
import com.shoppursshop.models.MySimpleItem;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.Utility;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends BaseImageActivity implements OnFragmentInteraction {

    public static final int LANGUAGE = 1,PERSONAL=2,BANK =3,CATEGORY =4,SUB_CATEGORY =5,PRODUCT =6;

    private String fragmentTag;
    private LanguageFragment languageFragment;
    private PersonalInfoFragment personalInfoFragment;
    private BankFragment bankFragment;
    private CategoryFragment categoryFragment;
    private SubCatFragment subCatFragment;
    private ProductFragment productFragment;
    private String language;
    private int initType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        languageFragment = LanguageFragment.newInstance("","");
        personalInfoFragment = PersonalInfoFragment.newInstance("","");
        bankFragment = BankFragment.newInstance("register","");
        categoryFragment = CategoryFragment.newInstance("","");
        subCatFragment = SubCatFragment.newInstance("","");
        productFragment = ProductFragment.newInstance("","");

        initType = getIntent().getIntExtra("type",0);

        FragmentTransaction trans = getSupportFragmentManager()
                .beginTransaction();
        /*
         * IMPORTANT: We use the "root frame" defined in
         * "root_fragment.xml" as the reference to replace fragment
         */
        if(initType == BANK){
            trans.replace(R.id.container, bankFragment,"bankFragment");
            fragmentTag = "bankFragment";
        }else if(initType == CATEGORY){
            trans.replace(R.id.container, categoryFragment,"bankFragment");
            fragmentTag = "categoryFragment";
        }else if(initType == SUB_CATEGORY){
            List<Object> selectedItemList = dbHelper.getCategories();
            Log.i(TAG,"Size "+selectedItemList.size());
            subCatFragment.setItemCatList(selectedItemList);
            trans.replace(R.id.container, subCatFragment,"subCatFragment");
            fragmentTag = "subCatFragment";
        }else if(initType == PRODUCT){
            /*List<Object> catList = dbHelper.getCategoriesForProduct();
            CatListItem category = null;
            List<Object> subCatList = null;
            for(Object ob: catList){
                category = (CatListItem)ob;
                subCatList = dbHelper.getCatSubCategories(category.getId());
                category.setItemList(subCatList);
            }
            Log.i(TAG,"Size "+catList.size());
            productFragment.setItemCatList(catList);*/
            trans.replace(R.id.container, productFragment,"productFragment");
            fragmentTag = "productFragment";
        }else{
            trans.replace(R.id.container, languageFragment,"languageFragment");
            fragmentTag = "languageFragment";
        }

        /*
         * IMPORTANT: The following lines allow us to add the fragment
         * to the stack and return to it later, by pressing back
         */
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        //  trans.addToBackStack(null);
        trans.commit();

        //testApi();

    }

    @Override
    public void onFragmentInteraction(Object item, int type) {
        if(type == LANGUAGE){
            language = (String)item;
            FragmentTransaction trans = getSupportFragmentManager()
                    .beginTransaction();
            personalInfoFragment.setLanguage(language);
            trans.replace(R.id.container, personalInfoFragment,"personalInfoFragment");
            fragmentTag = "personalInfoFragment";
            trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            trans.addToBackStack(null);
            trans.commit();
        }else if(type == PERSONAL){
            FragmentTransaction trans = getSupportFragmentManager()
                    .beginTransaction();
            trans.replace(R.id.container, categoryFragment,"categoryFragment");
            fragmentTag = "categoryFragment";
            trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            trans.addToBackStack(null);
            trans.commit();
            /*FragmentTransaction trans = getSupportFragmentManager()
                    .beginTransaction();
            trans.replace(R.id.container, bankFragment,"bankFragment");
            fragmentTag = "bankFragment";
            trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            trans.addToBackStack(null);
            trans.commit();*/
        }else if(type == BANK){
            FragmentTransaction trans = getSupportFragmentManager()
                    .beginTransaction();
            trans.replace(R.id.container, categoryFragment,"categoryFragment");
            fragmentTag = "categoryFragment";
            trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            trans.addToBackStack(null);
            trans.commit();
        }else if(type == CATEGORY){
            List<Object> selectedItemList = (List<Object>) item;
            subCatFragment.setItemCatList(selectedItemList);
            FragmentTransaction trans = getSupportFragmentManager()
                    .beginTransaction();
            trans.replace(R.id.container, subCatFragment,"subCatFragment");
            fragmentTag = "subCatFragment";
            trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            trans.addToBackStack(null);
            trans.commit();
        }else if(type == SUB_CATEGORY){
            /*List<Object> selectedItemList = (List<Object>) item;
            productFragment.setItemCatList(selectedItemList);
            FragmentTransaction trans = getSupportFragmentManager()
                    .beginTransaction();
            trans.replace(R.id.container, productFragment,"productFragment");
            fragmentTag = "productFragment";
            trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            trans.addToBackStack(null);
            trans.commit();*/

            Intent intent = new Intent(this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);


        }else if(type == 0){
            selectImage();
        }
    }

    @Override
    protected void imageAdded(){
        bankFragment.setImageBase64(convertToBase64(new File(imagePath)),imagePath);
    }

    private void testApi(){
        String url = getResources().getString(R.string.url)+"/greeting";
        Log.i(TAG, Utility.getTimeStamp("yyyy-MM-dd HH:mm:ss"));
        jsonObjectApiRequest(Request.Method.GET,url,new JSONObject(),"greeting");
    }

    @Override
    public void onJsonObjectResponse(JSONObject jsonObject, String apiName) {
        if(apiName.equals("greeting")){
            Log.i(TAG, Utility.getTimeStamp("yyyy-MM-dd HH:mm:ss"));
        }
    }

}
