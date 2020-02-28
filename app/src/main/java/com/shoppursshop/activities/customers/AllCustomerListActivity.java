package com.shoppursshop.activities.customers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shoppursshop.R;
import com.shoppursshop.activities.base.BaseActivity;
import com.shoppursshop.activities.khatabook.KhatabookActivity;
import com.shoppursshop.adapters.CustomerAdapter;
import com.shoppursshop.fragments.BottomSearchFragment;
import com.shoppursshop.interfaces.MyImageClickListener;
import com.shoppursshop.interfaces.MyItemTypeClickListener;
import com.shoppursshop.models.MyCustomer;
import com.shoppursshop.utilities.DialogAndToast;
import com.shoppursshop.utilities.Utility;

import java.util.ArrayList;
import java.util.List;

public class AllCustomerListActivity extends BaseActivity implements MyItemTypeClickListener, MyImageClickListener {

    private RecyclerView recyclerView;
    private CustomerAdapter myItemAdapter;
    private List<Object> itemList;
    private TextView textViewError,textViewHeader1,text_sub_header;
    private ImageView imageViewSearch;
    private BottomSearchFragment bottomSearchFragment;
    private int position,type;
    private String mobile,flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_customer_list);

        init();
    }

    private void init(){
        text_sub_header = findViewById(R.id.text_sub_header);
        flag = getIntent().getStringExtra("flag");
        itemList = new ArrayList<>();

        textViewError = findViewById(R.id.text_error);
        imageViewSearch = findViewById(R.id.image_search);
        textViewHeader1 = findViewById(R.id.text_header);
        recyclerView=findViewById(R.id.recycler_view_my_customers);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        myItemAdapter=new CustomerAdapter(this,itemList,"customerList");
        myItemAdapter.setMyItemClickListener(this);
        myItemAdapter.setMyImageClickListener(this);
        recyclerView.setAdapter(myItemAdapter);

        imageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSearchFragment = new BottomSearchFragment();
                bottomSearchFragment.setCallingActivityName("customerList");
                bottomSearchFragment.show(getSupportFragmentManager(), "Search Customer Bottom Sheet");
            }
        });


        initFooter(this,10);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (bottomSearchFragment != null) {
            bottomSearchFragment.dismiss();
        }
        List<Object> itemNewList = null;
        if(flag.equals("openKhata")){
            text_sub_header.setText("Open Khatabook");
            itemNewList = dbHelper.getAllCustomerList(limit,offset);
        }else{
            itemNewList = new ArrayList<>();
        }

        itemList.clear();
        itemList.addAll(itemNewList);

        if(itemList.size() == 0){
            showNoData(true);
        }else{
            showNoData(false);
            myItemAdapter.notifyDataSetChanged();
        }
    }



    @Override
    public void onImageClicked(int position, int type, View view) {
        MyCustomer customer = (MyCustomer)itemList.get(position);
        showImageDialog(customer.getImage(),view);
    }

    @Override
    public void onItemClicked(int position, int type) {
        MyCustomer customer = null;
        this.position = position;
        this.type = type;
        if(type == 3 || type == 4|| type == 6){
            customer = (MyCustomer)itemList.get(position);
            if(type == 3){
                makeCall(customer.getMobile());
            }else if(type == 4){
                messageCustomer(customer.getMobile());
            }else{
                if(customer.getLatitude() == null || customer.getLatitude().equals("0.0")
                        || customer.getLatitude().equals("")|| customer.getLatitude().equals("null")){
                    DialogAndToast.showDialog("Location is not available",this);
                }else{
                    startCustomerAddressActivity(customer);
                }
            }
        }else if(type == 7 || type == 8){
            customer = (MyCustomer)itemList.get(position);
            if(TextUtils.isEmpty(customer.getKhataNo()) || customer.getKhataNo().equals("null")){
                showMyBothDialog("Are you sure want to open khata for this customer?","Cancel","Proceed");
            }else{
                startKhatbook();
            }
        }
    }

    @Override
    public void onDialogPositiveClicked() {
        startKhatbook();
    }

    public void makeCall(String mobile){
        this.mobile = mobile;
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+mobile));
        if(Utility.verifyCallPhonePermissions(this))
            startActivity(callIntent);
    }

    public void messageCustomer(String mobile){
        openWhatsApp(mobile);
    }

    public void openWhatsApp(String mobile){
        try {
            mobile = "91"+mobile;
            String text = "This is a test";// Replace with your message.
            //   String toNumber = "xxxxxxxxxx"; // Replace with mobile phone number without +Sign or leading zeros, but with country code
            //Suppose your country is India and your phone number is “xxxxxxxxxx”, then you need to send “91xxxxxxxxxx”.
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+mobile +"&text="));
            startActivity(intent);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void startKhatbook(){
        MyCustomer customer = (MyCustomer)itemList.get(position);
        Intent intent = new Intent(this, KhatabookActivity.class);
        intent.putExtra("name",customer.getName());
        intent.putExtra("address",customer.getAddress());
        intent.putExtra("mobile",customer.getMobile());
        intent.putExtra("country",customer.getCountry());
        intent.putExtra("stateCity",customer.getState()+", "+customer.getCity());
        intent.putExtra("locality",customer.getLocality());
        intent.putExtra("longitude",customer.getLongitude());
        intent.putExtra("latitude",customer.getLatitude());
        intent.putExtra("customerImage",customer.getImage());
        intent.putExtra("isFav",customer.getIsFav());
        intent.putExtra("custCode",customer.getCode());
        intent.putExtra("custId",customer.getId());
        startActivity(intent);
    }

    public void startCustomerAddressActivity(MyCustomer item){
        Intent intent = new Intent(AllCustomerListActivity.this, CustomerAddressActivity.class);
        intent.putExtra("name",item.getName());
        intent.putExtra("address",item.getAddress());
        intent.putExtra("mobile",item.getMobile());
        intent.putExtra("country",item.getCountry());
        intent.putExtra("stateCity",item.getState()+", "+item.getCity());
        intent.putExtra("locality",item.getLocality());
        intent.putExtra("longitude",item.getLongitude());
        intent.putExtra("latitude",item.getLatitude());
        intent.putExtra("customerImage",item.getImage());
        intent.putExtra("isFav",item.getIsFav());
        intent.putExtra("custCode",item.getCode());
        intent.putExtra("custId",item.getId());
        intent.putExtra("ratings",item.getRatings());
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //mLocationPermissionGranted = false;

        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_CALL_PHONE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makeCall(mobile);
                }
                break;
        }

    }

    private void showNoData(boolean show){
        if(show){
            recyclerView.setVisibility(View.GONE);
            textViewError.setVisibility(View.VISIBLE);
        }else{
            recyclerView.setVisibility(View.VISIBLE);
            textViewError.setVisibility(View.GONE);
        }
    }
}
