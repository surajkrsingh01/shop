package com.shoppursshop.activities.payment.mPos;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class Utils {
	
	
	protected static String getAmountFormat(String amount) {
		// TODO Auto-generated method stub
		StringBuilder strBind = new StringBuilder(amount);
		strBind.append(".00");
		return strBind.toString();
	}

	static boolean checkAmount(String amount) {
		try {
			
			if (convertStringToDouble(amount) >= 11)
				return true;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static double convertStringToDouble(String data) {
		try {
			NumberFormat nf = NumberFormat.getInstance(Locale.getDefault());
			double amountcheck = nf.parse(data).doubleValue();
			return amountcheck;
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0.0;
	}
	public static String getRupeeAmount(double value) {
		String rupeeAmount = "0.0";
		NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale(
				"en", "in"));
		rupeeAmount = formatter.format(value);
		return rupeeAmount;

	}

	public static String chckStringNull(String value) {
		String value1 = "";
		try {
			if( value != null &&(!value.equalsIgnoreCase(null)
					&& !value.equalsIgnoreCase("null")
					&& !value.equalsIgnoreCase(""))){
				value1 = value.trim();
			}else{
				value1 = "";
			}
		} catch (Exception e) {
			value1 = "";
		}
		return value1;
	}
}
