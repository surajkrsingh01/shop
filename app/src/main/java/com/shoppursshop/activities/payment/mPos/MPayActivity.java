package com.shoppursshop.activities.payment.mPos;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pnsol.sdk.auth.AccountValidator;
import com.pnsol.sdk.payment.PaymentInitialization;
import com.shoppursshop.R;
import com.shoppursshop.activities.base.NetworkBaseActivity;
import com.shoppursshop.adapters.BlueToothAdapter;
import com.shoppursshop.interfaces.MyItemClickListener;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.DialogAndToast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.pnsol.sdk.interfaces.DeviceCommunicationMode.BLUETOOTHCOMMUNICATION;
import static com.pnsol.sdk.interfaces.PaymentTransactionConstants.FAIL;
import static com.pnsol.sdk.interfaces.PaymentTransactionConstants.PAYMENT_TYPE;
import static com.pnsol.sdk.interfaces.PaymentTransactionConstants.REQUEST_ENABLE_BT;
import static com.pnsol.sdk.interfaces.PaymentTransactionConstants.SALE;
import static com.pnsol.sdk.interfaces.PaymentTransactionConstants.SOCKET_NOT_CONNECTED;
import static com.pnsol.sdk.interfaces.PaymentTransactionConstants.SUCCESS;

public class MPayActivity extends NetworkBaseActivity implements MyItemClickListener {

    //Testing
   // private String PARTNER_API_KEY = "763432092B47";
   // private String MERCHANT_API_KEY = "763432092B47";

    //Production
    private String PARTNER_API_KEY = "E5004EA8E077";
    private String MERCHANT_API_KEY = "DE4B7AF220C7";

    private int devicePosition;
    private List<String> list;
    private String ordAmount,deviceType,deviceName,device_MAC_Add,deviceMACAddress;
    private int deviceCommMode;
    private RecyclerView recyclerView;
    private BlueToothAdapter itemAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private Set<BluetoothDevice> pairedDevices;
    private PaymentInitialization initialization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpay);
        Toolbar toolbar = (Toolbar) (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();

    }

    private void init(){

       // PARTNER_API_KEY = sharedPreferences.getString(Constants.PARTNER_ID,"");
       // MERCHANT_API_KEY = sharedPreferences.getString(Constants.MERCHANT_ID,"");

        String devType = getIntent().getStringExtra("deviceType");
        deviceType = devType;
        Log.i(TAG,"device type "+devType+" "+deviceType);
        ordAmount = getIntent().getStringExtra("totalAmount");
       // ordAmount = "50.00";
        Log.i(TAG,"amount "+ordAmount);

        list = new ArrayList<String>();
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        final RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        itemAdapter=new BlueToothAdapter(this,list);
        itemAdapter.setMyItemClickListener(this);
        recyclerView.setAdapter(itemAdapter);

        setPaymentPos();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // take an instance of BluetoothAdapter`
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {

            Toast.makeText(this, "Bluetooth is not supported on this device", Toast.LENGTH_LONG).show();
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent discoveryIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(discoveryIntent, REQUEST_ENABLE_BT);
            }else{
                getBlueToothSpinnerList();
            }
        }

    }

    private void getBlueToothSpinnerList() {

        pairedDevices = mBluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : pairedDevices) {
            deviceName = device.getName();
            if (deviceName.startsWith("Paynear") ||
                    deviceName.startsWith("MPOS") ||
                    deviceName.startsWith("C-ME")) {
                list.add(deviceName);
            }

        }

        itemAdapter.notifyDataSetChanged();

    }

    private void setPaymentPos(){
        try {
            AccountValidator validator = new AccountValidator(getApplicationContext());
            if (!validator.isAccountActivated()) {
                DialogAndToast.showToast("Validating...", MPayActivity.this);
                validator.accountActivation(handler, MERCHANT_API_KEY, PARTNER_API_KEY);
            }else{
                Log.i(TAG,"Activated");
               // getBlueToothSpinnerList();
                //initiateTransaction();
                DialogAndToast.showToast("Activated", MPayActivity.this);
            }
        }catch (RuntimeException e) {
            e.printStackTrace();
            Log.i(TAG,"Exception "+e.getMessage());
        }

    }

    private void initiateTransaction(){
       /* try {
            initialization=new PaymentInitialization(MPayActivity.this);
            initialization.getDeviceeserialNumber(handler,1,deviceCommMode,deviceMACAddress);
        }catch (RuntimeException e){
            e.printStackTrace();
            Log.i(TAG,"Exception "+e.getMessage());
        }*/

        Intent i = new Intent(MPayActivity.this, MPaymentTransactionActivity.class);
        i.putExtra("devicetype", deviceType);
        i.putExtra(PAYMENT_TYPE, SALE);
        i.putExtra("deviceName", deviceName);
        i.putExtra("deviceCommMode", deviceCommMode);
        i.putExtra("deviceMACAddress", deviceMACAddress);
        i.putExtra("orderNumber",getIntent().getStringExtra("orderNumber"));
        i.putExtra("custCode", getIntent().getStringExtra("custCode"));
        i.putExtra("custId", getIntent().getIntExtra("custId",0));
        i.putExtra("custName", getIntent().getStringExtra("custName"));
        i.putExtra("custMobile", getIntent().getStringExtra("custMobile"));
        i.putExtra("custImage", getIntent().getStringExtra("custImage"));
        i.putExtra("custUserCreateStatus", getIntent().getStringExtra("custUserCreateStatus"));
        i.putExtra("amount", ordAmount);
        i.putExtra("ordCouponId",getIntent().getStringExtra("ordCouponId"));
        i.putExtra("totalTax",getIntent().getFloatExtra("totalTax",0f));
        i.putExtra("deliveryCharges",getIntent().getFloatExtra("deliveryCharges",0f));
        i.putExtra("totDiscount",getIntent().getFloatExtra("totDiscount",0f));
        i.putExtra("referanceno", sharedPreferences.getString(Constants.MERCHANT_REF_NO,"null"));
        startActivity(i);
        MPayActivity.this.finish();
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
           // Log.i(TAG,"Handler "+(String) msg.obj);

            if (msg.what == SUCCESS) {
                Log.i(TAG,"Success");
                DialogAndToast.showToast("Success",MPayActivity.this);
               // getBlueToothSpinnerList();
               // initiateTransaction();
            }
            if (msg.what == FAIL) {
                Log.i(TAG,"Failed ");
                DialogAndToast.showToast("Failed",MPayActivity.this);
            }

            if(msg.what==SOCKET_NOT_CONNECTED) {
                Log.i(TAG,"Failed ");
                DialogAndToast.showToast("Failed",MPayActivity.this);
            }

        };
    };

    @Override
    public void onItemClicked(int position) {
        device_MAC_Add = list.get(position);
        Iterator<BluetoothDevice> i = pairedDevices.iterator();
        while (i.hasNext()) {
            BluetoothDevice device = i.next();
            if (device.getName().equalsIgnoreCase(device_MAC_Add)) {
                deviceMACAddress = device.getAddress();
                deviceName = device.getName();
            }
            deviceCommMode = BLUETOOTHCOMMUNICATION;
        }

        initiateTransaction();
    }
}
