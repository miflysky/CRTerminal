package com.tieto.crterminal.model.wifi;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifiUtils {

	private WifiManager mWifiManager;

	private static final String TAG = "CRTerminal";

	public WifiUtils(WifiManager wifiManager) {
		mWifiManager = wifiManager;
	}

	public void startWifiScan() {

		mWifiManager.setWifiEnabled(true);
		mWifiManager.startScan();
	}

	public boolean setApEnabled(boolean enabled, String ssid) {

		// disable WiFi in any case
		mWifiManager.setWifiEnabled(false);
		if (!enabled) {
			return true;
		}
		try {

			WifiConfiguration apConfig = new WifiConfiguration();

			apConfig.SSID = ssid;

			apConfig.preSharedKey = "12345678";

			Method method = mWifiManager.getClass().getMethod(
					"setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);

			boolean success = (Boolean) method.invoke(mWifiManager, apConfig,
					enabled);

			return success;

		} catch (Exception e) {
			return false;
		}
	}

	public boolean connectToSSID(String ssid) {
		
		boolean success = false;
		WifiConfiguration exsitConf = null;
		
		// Check whether the connection are exsit
		List<WifiConfiguration> existingConfigs = mWifiManager
				.getConfiguredNetworks();
		for (WifiConfiguration existingConfig : existingConfigs) {
			if (existingConfig.SSID.equals("\"" + ssid + "\"")) {
				exsitConf = existingConfig;
				break;
			}
		}

		// if already exist, connect directly
		if (exsitConf != null) {
			success = mWifiManager.enableNetwork(exsitConf.networkId, true);
			return success;
		}

		// ...seems we haven't connect to the ssid before

		// create a WifiConfiguration
		WifiConfiguration config = new WifiConfiguration();
		config.SSID = "\"" + ssid + "\"";
		config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

		//add the new WifiConfiguration
		int netid = mWifiManager.addNetwork(config);
		
		//connect to ssid
		success = mWifiManager.enableNetwork(netid, true);
		
		return success;

	}

	// not use currently
	public ArrayList<String> getConnectionList() {
		WifiInfo wifiinfo = mWifiManager.getConnectionInfo();

		if (wifiinfo != null) {
			Log.e(TAG, wifiinfo.toString());
		}

		ArrayList<String> ipaddr = getConnectedIP();
		for (String ip : ipaddr) {
			Log.i(TAG, ip);
		}

		return ipaddr;

	}

	// not use currently
	// IP address HW type Flags HW address Mask Device
	// 192.168.43.62 0x1 0x2 68:76:4f:74:12:86 * wlan0
	private ArrayList<String> getConnectedIP() {
		ArrayList<String> connectedIP = new ArrayList<String>();
		BufferedReader bufReader;
		try {
			bufReader = new BufferedReader(new FileReader("/proc/net/arp"));
			String line;
			line = bufReader.readLine();

			Log.e(TAG, line);

			while ((line = bufReader.readLine()) != null) {

				String[] splitted = line.split(" +");
				if (splitted != null && splitted.length >= 4) {
					String ip = splitted[0];
					connectedIP.add(ip);
				}
			}
			bufReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

		return connectedIP;
	}

}
