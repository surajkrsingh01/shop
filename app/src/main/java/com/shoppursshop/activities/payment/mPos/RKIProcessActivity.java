package com.shoppursshop.activities.payment.mPos;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pnsol.sdk.interfaces.PaymentTransactionConstants;
import com.pnsol.sdk.payment.PaymentInitialization;
import com.shoppursshop.R;


/**
 * @Author : pradeep.arige
 * 
 * @Version : V-1.0.15
 * @Date : Sep 19, 2014
 * @Copyright : (C) Paynear Solutions Pvt. Ltd.
 */
public class RKIProcessActivity extends AppCompatActivity implements
		PaymentTransactionConstants,  OnClickListener {

	public static final String PN_LOG = RKIProcessActivity.class.getName();
	private static final int REQUEST_ENABLE_BT = 1;
	private BluetoothAdapter mBtAdapter;
	private Button regd;
	private static String MAC_ADDRESS = "macAddress";
	private String deviceMACAddress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(PN_LOG, "onCreate()");
		setContentView(R.layout.regd);
		regd = (Button) findViewById(R.id.regd);
		regd.setOnClickListener(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.i(PN_LOG, "onStart()");
		// Bluetooth is enabled or not if not then asks for enable
		if (mBtAdapter != null) {
			mBtAdapter.cancelDiscovery();
			if (!mBtAdapter.isEnabled()) {
				Log.i(PN_LOG, "Bluetooth Enable request");
				Intent enableIntent = new Intent(
						BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
			}
		}
	}

	@Override
	public void onBackPressed() {
		 super.onBackPressed();
	}
	

	public void deviceConnection(String address) {
		Log.i(PN_LOG, "deviceConnection()");
		try {
			PaymentInitialization initialization = new PaymentInitialization(
					getApplicationContext());
			initialization.cardReaderConfiguration(handler, address);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	@SuppressLint("HandlerLeak")
	private final Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			Log.i(PN_LOG, "Response "+(String) msg.obj);

			if (msg.what == SOCKET_NOT_CONNECTED) {
				Log.i(PN_LOG, "Socket not connected");
				Toast.makeText(RKIProcessActivity.this,
						(String) msg.obj, Toast.LENGTH_SHORT).show();
			}
			if (msg.what == P2PE_STATUS_INITIALISED) {
				Log.i(PN_LOG, "P2PE status initialised:" + (String) msg.obj);
				Toast.makeText(RKIProcessActivity.this,
						(String) msg.obj, Toast.LENGTH_SHORT).show();
			}
			if (msg.what == NETWORK_ERROR) {
				Log.i(PN_LOG, "Network Error:" + (String) msg.obj);
				Toast.makeText(RKIProcessActivity.this,
						(String) msg.obj, Toast.LENGTH_SHORT).show();
			}
			if (msg.what == PED_STATUS) {
				Log.i(PN_LOG, "PED Status:" + (String) msg.obj);
				Toast.makeText(RKIProcessActivity.this,
						(String) msg.obj, Toast.LENGTH_SHORT).show();
			}
			/*if (msg.what == FILE_STATUS) {
				Toast.makeText(RKIProcessActivity.this,
						msg.arg1, 1000).show();
				
			}*/
		};
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i(PN_LOG, "onDestroy()");
	}

	@Override
	public void onClick(View v) {
		deviceMACAddress = getIntent().getStringExtra(MAC_ADDRESS);
		deviceConnection(deviceMACAddress);
	}
}
