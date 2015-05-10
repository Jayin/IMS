package com.ims.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.http.conn.util.InetAddressUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * 安卓设备工具类
 * 
 * @author Jayin Ton 需要的权限：<uses-permission
 *         android:name="android.permission.READ_PHONE_STATE"/>
 */
public class AndroidUtils {
	/**
	 * 获得屏幕的高宽(像数)
	 * 
	 * @param context
	 * @return size[0]为宽 size[1]为长
	 */
	public static int[] getScreenSize(Context context) {
		int[] size = new int[2];
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		manager.getDefaultDisplay().getMetrics(dm);
		size[0] = dm.widthPixels;
		size[1] = dm.heightPixels;
		return size;
	}

	/**
	 * 判断用户是否在wifi环境下
	 * 
	 * @param context
	 * @return true if it's in the environment of wifi
	 */
	public static boolean isInWifi(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connManager.getActiveNetworkInfo();
		if (info != null && info.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}

	/**
	 * 检测网络是否已经连接
	 * 
	 * @param context
	 * @return true if the network is connect
	 */
	public static boolean isNetworkConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		return info != null && info.isConnected();

	}

	/**
	 * 获得手机ip地址
	 * 
	 * @return null if faild to get ip
	 */
	public static String getPhoneIP() {
		try {
			String ipv4;
			ArrayList<NetworkInterface> mylist = Collections
					.list(NetworkInterface.getNetworkInterfaces());
			for (NetworkInterface ni : mylist) {
				ArrayList<InetAddress> ialist = Collections.list(ni
						.getInetAddresses());
				for (InetAddress address : ialist) {
					if (!address.isLoopbackAddress()
							&& InetAddressUtils.isIPv4Address(ipv4 = address
									.getHostAddress())) {
						return ipv4;
					}
				}

			}
		} catch (SocketException ex) {
             return null;
		}
		return null;
	}

	/**
	 * 获得本机Mac
	 * 
	 * @param context
	 * @return
	 */
	public static String getMacAddress(Context context) {
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}

}