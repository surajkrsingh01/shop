package com.shoppursshop.utilities;

import com.newland.me.ConnUtils;
import com.newland.me.DeviceManager;
import com.newland.mtype.ConnectionCloseEvent;
import com.newland.mtype.Device;
import com.newland.mtype.ModuleType;
import com.newland.mtype.event.DeviceEventListener;
import com.newland.mtype.module.common.cardreader.CardReader;
import com.newland.mtype.module.common.cardreader.K21CardReader;
import com.newland.mtype.module.common.emv.EmvModule;
import com.newland.mtype.module.common.iccard.ICCardModule;
import com.newland.mtype.module.common.light.IndicatorLight;
import com.newland.mtype.module.common.pin.K21Pininput;
import com.newland.mtype.module.common.printer.Printer;
import com.newland.mtype.module.common.rfcard.RFCardModule;
import com.newland.mtype.module.common.scanner.BarcodeScanner;
import com.newland.mtype.module.common.scanner.BarcodeScannerManager;
import com.newland.mtype.module.common.security.SecurityModule;
import com.newland.mtype.module.common.storage.Storage;
import com.newland.mtype.module.common.swiper.K21Swiper;
import com.newland.mtypex.nseries.NSConnV100ConnParams;
import com.newland.mtypex.nseries3.NS3ConnParams;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class N910Util {
	private static Context context;

	private static N910Util n910Util = null;
	private static final String K21_DRIVER_NAME = "com.newland.me.K21Driver";

	public N910Util(Context context) {
		N910Util.context = context;

	}

	public static N910Util getInstance(Context context) {
		if (n910Util == null) {
			synchronized (N910Util.class) {
				if (n910Util == null) {
					n910Util = new N910Util(context);
				}
			}
		}
		N910Util.context = context;
		return n910Util;
	}

	private static DeviceManager deviceManager = ConnUtils.getDeviceManager();

	public boolean isDeviceAlive() {
		boolean ifConnected = (deviceManager == null || deviceManager.getDevice() == null ? false
				: deviceManager.getDevice().isAlive());
		return ifConnected;
	}

	

	public Printer getPrinter() {
		DeviceManager deviceManager = ConnUtils.getDeviceManager();
		Device deviceManager1 = ConnUtils.getDeviceManager().getDevice();
		Printer printer = (Printer) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_PRINTER);
		printer.init();
		return printer;
	}

	
	public Device getDevice() {
		return deviceManager.getDevice();
	}

	public void connectDevice() {
		deviceManager = ConnUtils.getDeviceManager();
		deviceManager.init(context, K21_DRIVER_NAME, new NS3ConnParams(),
				new DeviceEventListener<ConnectionCloseEvent>() {

					public void onEvent(ConnectionCloseEvent event, Handler handler) {
						if (event.isSuccess()) {
						}
						if (event.isFailed()) {
						}
					}
					public Handler getUIHandler() {
						return null;
					}
				});
		try {
			deviceManager.connect();
			deviceManager.getDevice().setBundle(new NS3ConnParams());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void disconnect() {
		new Thread(new Runnable() {
			public void run() {
				try {
					if (deviceManager != null) {
						deviceManager.disconnect();
						deviceManager = null;
					}
				} catch (Exception e) {
					//Toast.makeText(context, "disconnect Exception fail", Toast.LENGTH_LONG).show();
				}
			}
		}).start();
	}

}
