package com.shoppursshop.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.shoppursshop.R;
import com.shoppursshop.fragments.BankFragment;
import com.shoppursshop.fragments.CategoryFragment;
import com.shoppursshop.fragments.LanguageFragment;
import com.shoppursshop.fragments.PersonalInfoFragment;
import com.shoppursshop.fragments.ProductFragment;
import com.shoppursshop.fragments.SubCatFragment;
import com.shoppursshop.interfaces.OnFragmentInteraction;

import java.util.List;

public class RegisterActivity extends NetworkBaseActivity implements OnFragmentInteraction {

    public static final int LANGUAGE = 1,PERSONAL=2,BANK =3,CATEGORY =4,SUB_CATEGORY =5,PRODUCT =6;

    private String fragmentTag;
    private LanguageFragment languageFragment;
    private PersonalInfoFragment personalInfoFragment;
    private BankFragment bankFragment;
    private CategoryFragment categoryFragment;
    private SubCatFragment subCatFragment;
    private ProductFragment productFragment;
    private String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        languageFragment = LanguageFragment.newInstance("","");
        personalInfoFragment = PersonalInfoFragment.newInstance("","");
        bankFragment = BankFragment.newInstance("","");
        categoryFragment = CategoryFragment.newInstance("","");
        subCatFragment = SubCatFragment.newInstance("","");
        productFragment = ProductFragment.newInstance("","");

        FragmentTransaction trans = getSupportFragmentManager()
                .beginTransaction();
        /*
         * IMPORTANT: We use the "root frame" defined in
         * "root_fragment.xml" as the reference to replace fragment
         */
        trans.replace(R.id.container, languageFragment,"languageFragment");
        fragmentTag = "languageFragment";
        /*
         * IMPORTANT: The following lines allow us to add the fragment
         * to the stack and return to it later, by pressing back
         */
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        //  trans.addToBackStack(null);
        trans.commit();

    }

    @Override
    public void onFragmentInteraction(Object item, int type) {
        if(type == LANGUAGE){
            language = (String)item;
            FragmentTransaction trans = getSupportFragmentManager()
                    .beginTransaction();
            trans.replace(R.id.container, personalInfoFragment,"personalInfoFragment");
            fragmentTag = "personalInfoFragment";
            trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            trans.addToBackStack(null);
            trans.commit();
        }else if(type == PERSONAL){
            FragmentTransaction trans = getSupportFragmentManager()
                    .beginTransaction();
            trans.replace(R.id.container, bankFragment,"bankFragment");
            fragmentTag = "bankFragment";
            trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            trans.addToBackStack(null);
            trans.commit();
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
            List<Object> selectedItemList = (List<Object>) item;
            productFragment.setItemCatList(selectedItemList);
            FragmentTransaction trans = getSupportFragmentManager()
                    .beginTransaction();
            trans.replace(R.id.container, productFragment,"productFragment");
            fragmentTag = "productFragment";
            trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            trans.addToBackStack(null);
            trans.commit();
        }
    }
}
