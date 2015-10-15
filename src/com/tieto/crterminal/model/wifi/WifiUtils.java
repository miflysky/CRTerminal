package com.tieto.crterminal.model.wifi;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.net.DhcpInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifiUtils {

	private WifiManager mWifiManager;

	private static final String TAG = "CRTerminal";

	private String mEnableAPname;

	public WifiUtils(WifiManager wifiManager) {
		mWifiManager = wifiManager;
	}

	public void startWifiScan() {

		// first we should disconnect current wifi
		int wifistate = mWifiManager.getWifiState();
		if (wifistate == WifiManager.WIFI_STATE_ENABLING
				|| wifistate == WifiManager.WIFI_STATE_ENABLED) {

			mWifiManager.setWifiEnabled(false);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		//mWifiManager.setWifiEnabled(true);
		mWifiManager.startScan();
	}

	public boolean enableAP(String ssid) {

		mEnableAPname = ssid;

		// disable WiFi in any case
		mWifiManager.setWifiEnabled(false);

		try {

			WifiConfiguration apConfig = createWifiHotConfig(ssid, "");

			Method method = mWifiManager.getClass().getMethod(
					"setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);

			boolean success = (Boolean) method.invoke(mWifiManager, apConfig,
					true);

			return success;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean disableAP() {

		// disable WiFi in any case
		mWifiManager.setWifiEnabled(false);

		try {

			WifiConfiguration apConfig = createWifiHotConfig(mEnableAPname, "");

			Method method = mWifiManager.getClass().getMethod(
					"setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);

			boolean success = (Boolean) method.invoke(mWifiManager, apConfig,
					false);

			return success;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void disableWifi() {

		// disable WiFi
		mWifiManager.setWifiEnabled(false);

	}

	private WifiConfiguration createWifiHotConfig(String ssid, String passwd) {

		WifiConfiguration config = new WifiConfiguration();
		config.allowedAuthAlgorithms.clear();
		config.allowedGroupCiphers.clear();
		config.allowedKeyManagement.clear();
		config.allowedPairwiseCiphers.clear();
		config.allowedProtocols.clear();

		config.SSID = ssid;
		config.wepKeys[0] = passwd;
		config.allowedAuthAlgorithms
				.set(WifiConfiguration.AuthAlgorithm.SHARED);
		config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
		config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
		config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
		config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
		config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
		config.wepTxKeyIndex = 0;
		config.priority = 0;

		return config;
	}

	public String getAPAddress() {

		String gateway;

		DhcpInfo di = mWifiManager.getDhcpInfo();
		long getewayIpL = di.gateway;
		gateway = long2ip(getewayIpL);

		return gateway;
	}

	private String long2ip(long ip) {
		StringBuffer sb = new StringBuffer();
		sb.append(String.valueOf((int) (ip & 0xff)));
		sb.append('.');
		sb.append(String.valueOf((int) ((ip >> 8) & 0xff)));
		sb.append('.');
		sb.append(String.valueOf((int) ((ip >> 16) & 0xff)));
		sb.append('.');
		sb.append(String.valueOf((int) ((ip >> 24) & 0xff)));
		return sb.toString();
	}

	public boolean connectToSSID(String ssid) {

		if (!mWifiManager.isWifiEnabled()) {
			mWifiManager.setWifiEnabled(true);
		}

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

		// add the new WifiConfiguration
		int netid = mWifiManager.addNetwork(config);

		// connect to ssid
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
