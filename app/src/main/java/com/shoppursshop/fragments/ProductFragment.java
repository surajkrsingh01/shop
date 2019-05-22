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

import com.android.volley.Request;
import com.shoppursshop.R;
import com.shoppursshop.activities.MainActivity;
import com.shoppursshop.activities.RegisterActivity;
import com.shoppursshop.adapters.SimpleItemAdapter;
import com.shoppursshop.interfaces.MyLevelItemClickListener;
import com.shoppursshop.interfaces.OnFragmentInteraction;
import com.shoppursshop.models.Barcode;
import com.shoppursshop.models.CatListItem;
import com.shoppursshop.models.MyProductItem;
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
 * {@link ProductFragment} interface
 * to handle interaction events.
 * Use the {@link ProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductFragment extends NetworkBaseFragment implements MyLevelItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<Object> itemCatList,selectedItemList;
    private List<MyProductItem> productList,selectedProductList;
    private View rootView;
    private RecyclerView recyclerView;
    private SimpleItemAdapter itemAdapter;
    private List<Object> itemList;
    private Button btnBack,btnNext,btnSkip;
    private String subCats;
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
        productList = new ArrayList<>();
        selectedProductList = new ArrayList<>();
        selectedItemList = new ArrayList<>();
        int i=0;
        itemCatList = dbHelper.getCategoriesForProduct();
        CatListItem category = null;
        MySimpleItem subCat = null;
        List<Object> subCatList = null;
        for(Object ob: itemCatList){
            category = (CatListItem)ob;
            subCatList = dbHelper.getCatSubCategories(""+category.getId());
            category.setItemList(subCatList);
            for(Object ob1 : subCatList){
                subCat = (MySimpleItem)ob1;
                addSubCategory(subCat.getId(),subCat.getName(),itemList.size());
                if(subCats == null){
                    subCats = ""+subCat.getId();
                }else{
                    subCats = subCats+","+subCat.getId();
                }
            }
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

                CatListItem subCat = null,mySelectedCatItem = null;
                List<Object> selectedList = null;
                MyProductItem prod = null;
                for(Object ob: itemList){
                    subCat = (CatListItem)ob;
                    selectedList = new ArrayList<>();
                    mySelectedCatItem = new CatListItem();
                    mySelectedCatItem.setId(subCat.getId());
                    mySelectedCatItem.setTitle(subCat.getTitle());
                    mySelectedCatItem.setDesc(subCat.getDesc());
                    for(Object ob1 : subCat.getItemList()){
                        prod = (MyProductItem) ob1;
                        if(prod.isSelected())
                            selectedList.add(ob1);
                    }
                    if(selectedList.size() > 0){
                        mySelectedCatItem.setItemList(selectedList);
                        selectedItemList.add(mySelectedCatItem);
                    }
                }

                if(selectedItemList.size() == 0){
                    DialogAndToast.showDialog("Please select Products",getActivity());
                    return;
                }
                createProducts();

              /*  Intent intent = new Intent(getActivity(),MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);*/
               /* if(selectedItemList.size() == 0){
                    DialogAndToast.showDialog("Please select Category",getActivity());
                    return;
                }
                */
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

        if(ConnectionDetector.isNetworkAvailable(getActivity())){
            getProducts();
        }
    }

    private void getProducts(){
        String url=getResources().getString(R.string.url)+Constants.GET_PRODUCTS;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(),"productslist");
    }

    private void createProducts(){
        JSONArray dataArray = new JSONArray();
        JSONObject dataObject = null;
        CatListItem category = null;
        MyProductItem subCat = null;
        for(Object ob: selectedItemList){
            category = (CatListItem)ob;
            for(Object ob1 : category.getItemList()){
                subCat = (MyProductItem) ob1;
                dataObject = new JSONObject();
                try {
                    dataObject.put("retRetailerId",sharedPreferences.getString(Constants.USER_ID,""));
                    dataObject.put("prodCatId",""+category.getId());
                    dataObject.put("prodSubCatId",""+subCat.getProdSubCatId());
                    dataObject.put("prodId",""+subCat.getProdId());
                    dataObject.put("prodReorderLevel",""+subCat.getProdReorderLevel());
                    dataObject.put("prodQoh",""+subCat.getProdQoh());
                    dataObject.put("prodName",""+subCat.getProdName());
                    dataObject.put("prodCode",""+subCat.getProdCode());
                    dataObject.put("prodBarCode",""+subCat.getProdBarCode());
                    dataObject.put("prodDesc",""+subCat.getProdDesc());
                    dataObject.put("prodCgst",""+subCat.getProdCgst());
                    dataObject.put("prodIgst",""+subCat.getProdIgst());
                    dataObject.put("prodSgst",""+subCat.getProdSgst());
                    dataObject.put("prodWarranty",""+subCat.getProdWarranty());
                    dataObject.put("prodMrp",""+subCat.getProdMrp());
                    dataObject.put("prodSp",""+subCat.getProdSp());
                    dataObject.put("prodHsnCode",""+subCat.getProdHsnCode());
                    dataObject.put("prodMfgDate",""+subCat.getProdMfgDate());
                    dataObject.put("prodExpiryDate",""+subCat.getProdExpiryDate());
                    dataObject.put("prodMfgBy",""+subCat.getProdMfgBy());
                    dataObject.put("prodImage1",""+subCat.getProdImage1());
                    dataObject.put("prodImage2",""+subCat.getProdImage2());
                    dataObject.put("prodImage3",""+subCat.getProdImage3());
                    dataObject.put("isBarcodeAvailable",subCat.getIsBarCodeAvailable());
                    dataObject.put("barcodeList",subCat.getBarcodeList());
                    dataObject.put("action","2");
                    dataObject.put("createdBy",""+subCat.getCreatedBy());
                    dataObject.put("updatedBy",""+subCat.getUpdatedBy());
                    dataObject.put("dbName",sharedPreferences.getString(Constants.DB_NAME,""));
                    dataObject.put("dbUserName",sharedPreferences.getString(Constants.DB_USER_NAME,""));
                    dataObject.put("dbPassword",sharedPreferences.getString(Constants.DB_PASSWORD,""));
                    dataArray.put(dataObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
        String url=getResources().getString(R.string.url)+Constants.CREATE_PRODUCTS;
        showProgress(true);
        jsonArrayV2ApiRequest(Request.Method.POST,url,dataArray,"addProduct");
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {

        try {
            if(apiName.equals("productslist")){

                if(response.getBoolean("status")){
                    JSONArray dataArray = response.getJSONArray("result");
                    JSONObject jsonObject =null;
                    int len = dataArray.length();
                    MyProductItem item = null;
                    List<Barcode> barCodeList = null;
                    Barcode barcode = null;
                    JSONArray barArray = null;
                    int barLen = 0;
                    for(int i=0; i<len; i++){
                        jsonObject = dataArray.getJSONObject(i);
                        item = new MyProductItem();
                        item.setProdId(jsonObject.getInt("prodId"));
                        item.setProdCatId(jsonObject.getInt("prodCatId"));
                        item.setProdSubCatId(jsonObject.getInt("prodSubCatId"));
                        item.setProdName(jsonObject.getString("prodName"));
                        item.setProdBarCode(jsonObject.getString("prodBarCode"));
                        item.setProdDesc(jsonObject.getString("prodDesc"));
                        item.setProdReorderLevel(jsonObject.getInt("prodReorderLevel"));
                        item.setProdQoh(jsonObject.getInt("prodQoh"));
                        item.setProdHsnCode(jsonObject.getString("prodHsnCode"));
                        item.setProdCgst(Float.parseFloat(jsonObject.getString("prodCgst")));
                        item.setProdIgst(Float.parseFloat(jsonObject.getString("prodIgst")));
                        item.setProdSgst(Float.parseFloat(jsonObject.getString("prodSgst")));
                        item.setProdWarranty(Float.parseFloat(jsonObject.getString("prodWarranty")));
                        item.setProdMfgDate(jsonObject.getString("prodMfgDate"));
                        item.setProdExpiryDate(jsonObject.getString("prodExpiryDate"));
                        item.setProdMfgBy(jsonObject.getString("prodMfgBy"));
                        item.setProdImage1(jsonObject.getString("prodImage1"));
                        item.setProdImage2(jsonObject.getString("prodImage2"));
                        item.setProdImage3(jsonObject.getString("prodImage3"));
                        item.setIsBarCodeAvailable(jsonObject.getString("isBarcodeAvailable"));
                        item.setProdMrp(Float.parseFloat(jsonObject.getString("prodMrp")));
                        item.setProdSp(Float.parseFloat(jsonObject.getString("prodSp")));
                        item.setCreatedBy(jsonObject.getString("createdBy"));
                        item.setUpdatedBy(jsonObject.getString("updatedBy"));
                        item.setCreatedDate(jsonObject.getString("createdDate"));
                        item.setUpdatedDate(jsonObject.getString("updatedDate"));
                        barArray = jsonObject.getJSONArray("barcodeList");
                        barLen = barArray.length();
                        barCodeList = new ArrayList<>();
                        for(int j = 0; j<barLen; j++){
                            barcode = new Barcode();
                            barcode.setBarcode(barArray.getJSONObject(j).getString("barcode"));
                            barCodeList.add(barcode);
                        }
                        item.setBarcodeList(barCodeList);
                        addProduct(item);
                       // productList.add(item);
                    }

                    itemAdapter.notifyDataSetChanged();
                }else{
                    DialogAndToast.showDialog(response.getString("message"),getActivity());
                }
            }else if(apiName.equals("addProduct")){
                if(response.getBoolean("status")){
                    CatListItem subCat = null;
                    MyProductItem productItem = null;
                    for(Object ob: selectedItemList) {
                        subCat = (CatListItem) ob;
                        for (Object ob1 : subCat.getItemList()) {
                            productItem = (MyProductItem) ob1;
                            dbHelper.addProduct(productItem,Utility.getTimeStamp(),Utility.getTimeStamp());
                        }
                    }

                    Intent intent = new Intent(getActivity(),MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addSubCategory(int subCatId,String title,int position) {
        CatListItem myItem = new CatListItem();
        myItem.setSelectingAll(true);
        myItem.setTitle(title);
        myItem.setType(1);
        myItem.setId(subCatId);
        myItem.setPosition(position);
        List<Object> subCatList = new ArrayList<>();
        /*MySimpleItem mySimpleItem = null;
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
        }*/
        myItem.setItemList(subCatList);
        itemList.add(myItem);
    }

    private void addProduct(MyProductItem item){
        CatListItem subCat = null;
        MySimpleItem mySimpleItem = null;
        for(Object ob : itemList){
            subCat = (CatListItem) ob;
            if(subCat.getId() == item.getProdCatId()){
               /* mySimpleItem = new MySimpleItem();
                mySimpleItem.setId(item.getProdId());
                mySimpleItem.setName(item.getProdName());
                mySimpleItem.setPosition(subCat.getPosition());*/
                subCat.getItemList().add(item);
            }

            itemAdapter.notifyItemChanged(subCat.getPosition());
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

    @Override
    public void onLevelItemClicked(int itemPosition, int level) {
        if(level == -1){
            CatListItem item = (CatListItem) itemList.get(itemPosition);
            List<Object> simpleItemList = item.getItemList();
            MyProductItem item1 = null;
            for(Object ob : simpleItemList){
                item1 = (MyProductItem)ob;
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
