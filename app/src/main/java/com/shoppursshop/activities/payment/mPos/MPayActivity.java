package com.shoppursshop.activities.payment.mPos;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Spinner;
import android.widget.Toast;

import com.pnsol.sdk.auth.AccountValidator;
import com.pnsol.sdk.interfaces.DeviceType;
import com.pnsol.sdk.payment.PaymentInitialization;
import com.shoppursshop.R;
import com.shoppursshop.activities.NetworkBaseActivity;
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
import static com.pnsol.sdk.interfaces.PaymentTransactionConstants.QPOS_ID;
import static com.pnsol.sdk.interfaces.PaymentTransactionConstants.SALE;
import static com.pnsol.sdk.interfaces.PaymentTransactionConstants.SOCKET_NOT_CONNECTED;
import static com.pnsol.sdk.interfaces.PaymentTransactionConstants.SUCCESS;

public class MPayActivity extends NetworkBaseActivity implements MyItemClickListener {

   // private String PARTNER_API_KEY = "2C869B63DD4E";
   // private String MERCHANT_API_KEY = "2C869B63DD4E";

    private String PARTNER_API_KEY = "E5004EA8E077";
    private String MERCHANT_API_KEY = "DE4B7AF220C7";

    private Spinner pairedSpinnerSelectProvider;
    private static final int REQUEST_ENABLE_BT = 1;
    private int devicePosition;
    private List<String> list;
    private Set<BluetoothDevice> pairedDevices;
    private String deviceMACAddress, deviceName, device_MAC_Add,ordAmount;
    private int deviceCommMode,deviceType;

    private RecyclerView recyclerView;
    private BlueToothAdapter blueToothAdapter;

    private BluetoothAdapter mBluetoothAdapter;


    private PaymentInitialization initialization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpay);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();

    }

    private void init(){
        String devType = getIntent().getStringExtra("deviceType");
        if(devType.equals("ME30S")){
            deviceType = DeviceType.ME30S;
        }else{
            deviceType = DeviceType.QPOS;
        }
        Log.i(TAG,"device type "+devType+" "+deviceType);
        ordAmount = getIntent().getStringExtra("totalAmount");
       // ordAmount = "50.00";
        Log.i(TAG,"amount "+ordAmount);
        list = new ArrayList<String>();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        blueToothAdapter=new BlueToothAdapter(this,list);
        blueToothAdapter.setMyItemClickListener(this);
        recyclerView.setAdapter(blueToothAdapter);
       // setPaymentPos();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // take an instance of BluetoothAdapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not supported in this device", Toast.LENGTH_LONG).show();
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent discoveryIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(discoveryIntent, REQUEST_ENABLE_BT);
            }else{
                setPaymentPos();
            }
        }

    }

    private void getBlueToothSpinnerList() {
        list.clear();
        pairedDevices = mBluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : pairedDevices) {
            deviceName = device.getName();
            Log.i(TAG,"device "+deviceName);
            if (deviceName.startsWith("Paynear") || deviceName.startsWith("MPOS") || deviceName.startsWith("C-ME30S")) {
                list.add(deviceName);
            }else{
                //list.add(deviceName);
            }

        }

        blueToothAdapter.notifyDataSetChanged();

    }

    private void setPaymentPos(){
        try {
            AccountValidator validator = new AccountValidator(getApplicationContext());
            if (!validator.isAccountActivated()) {
                validator.accountActivation(handler, MERCHANT_API_KEY, PARTNER_API_KEY);
            }else{
                Log.i(TAG,"Activated");
                getBlueToothSpinnerList();
                DialogAndToast.showToast("Activated", MPayActivity.this);
            }
        }catch (RuntimeException e) {
            e.printStackTrace();
            Log.i(TAG,"Exception "+e.getMessage());
        }

    }

    private void getSerialNumber(){
        Intent i = new Intent(MPayActivity.this, MPaymentTransactionActivity.class);
        if (deviceCommMode == BLUETOOTHCOMMUNICATION)
            i.putExtra("macAddress", deviceMACAddress);
        else
            i.putExtra("macAddress", "");
        i.putExtra("deviceType", deviceType);
        i.putExtra(PAYMENT_TYPE, "serialNumber");
        i.putExtra("transactionmode", deviceCommMode);
        // i.putExtra("serialNumber", "serialNumber");

        startActivity(i);
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
        if (deviceCommMode == BLUETOOTHCOMMUNICATION) {
            i.putExtra("macAddress", deviceMACAddress);
        }
        else {
            // i.putExtra("macAddress", selectedUSBDevice);
        }
        i.putExtra("devicetype", deviceType);
        i.putExtra(PAYMENT_TYPE, SALE);
        i.putExtra("transactionmode", deviceCommMode);
        i.putExtra("ordId",getIntent().getStringExtra("ordId"));
        i.putExtra("custCode", getIntent().getStringExtra("custCode"));
        i.putExtra("custId", getIntent().getIntExtra("custId",0));
        i.putExtra("custName", getIntent().getStringExtra("custName"));
        i.putExtra("custMobile", getIntent().getStringExtra("custMobile"));
        i.putExtra("custImage", getIntent().getStringExtra("custImage"));
        i.putExtra("custUserCreateStatus", getIntent().getStringExtra("custUserCreateStatus"));
        i.putExtra("amount", ordAmount);
        i.putExtra("referanceno", sharedPreferences.getString(Constants.MERCHANT_REF_NO,"null"));
        startActivity(i);

        MPayActivity.this.finish();
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            Log.i(TAG,"Handler "+(String) msg.obj);

            if (msg.what == SUCCESS) {
                Log.i(TAG,"Success");
                DialogAndToast.showToast("Success",MPayActivity.this);
            }
            if (msg.what == FAIL) {
                Log.i(TAG,"Failed "+(String) msg.obj);
                DialogAndToast.showToast((String) msg.obj,MPayActivity.this);
            }

            if(msg.what==SOCKET_NOT_CONNECTED) {
                Log.i(TAG,"Failed "+(String) msg.obj);
            }

            if(msg.what==QPOS_ID){
                Log.i(TAG,"Success "+(String) msg.obj);
                Intent i = new Intent(MPayActivity.this, MPayTransactionDetailsActivity.class);
                if (deviceCommMode == BLUETOOTHCOMMUNICATION) {
                    i.putExtra("macAddress", deviceMACAddress);
                }
                else {
                   // i.putExtra("macAddress", selectedUSBDevice);
                }
                i.putExtra("devicetype", deviceType);
                i.putExtra(PAYMENT_TYPE, SALE);
                i.putExtra("transactionmode", deviceCommMode);
                i.putExtra("amount", "11");
                i.putExtra("referanceno", sharedPreferences.getString(Constants.MERCHANT_REF_NO,""));
                startActivity(i);
            }

        };
    };

    @Override
    public void onItemClicked(int position) {
        int type = 0;
        devicePosition = position;
        device_MAC_Add = (String) list.get(devicePosition);
        Iterator<BluetoothDevice> i = pairedDevices.iterator();
        while (i.hasNext()) {
            BluetoothDevice device = i.next();
            if (device.getName().equalsIgnoreCase(device_MAC_Add)) {
                deviceMACAddress = device.getAddress();
                deviceName = device.getName();
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    type = device.getType();
                }*/
            }
            deviceCommMode = BLUETOOTHCOMMUNICATION;
        }

        Log.i(TAG,"device_MAC_Add "+device_MAC_Add+" deviceMACAddress "
                +deviceMACAddress+" deviceName "+deviceName+" deviceCommMode "+deviceCommMode+" type "+type);

        //initiateDeviceRegisterProcess();
        initiateTransaction();
       // getSerialNumber();
    }

    private void initiateDeviceRegisterProcess(){
        Intent intent = new Intent(MPayActivity.this, RKIProcessActivity.class);
        intent.putExtra("macAddress",deviceMACAddress);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_ENABLE_BT){
            setPaymentPos();
        }
    }
}
