package com.tieto.crterminal.ui;

import com.tieto.crterminal.R;
import com.tieto.crterminal.model.network.CRTClient2;
import com.tieto.crterminal.model.network.CRTServer2;
import com.tieto.crterminal.model.wifi.WifiUtils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;

@SuppressLint("CommitTransaction")
public class BaseGameActivity extends Activity {

	public static final String TAG = "CRTerminal";

	public static final int APSELECTOR = 1;

	public static final String APPREFIX = "CRT_";
	public static final String USERPREFIX = "User_";

	// define the NoneUI related event
	public static final int NONEUIEVENTBASE = 100;
	public static final int GOTAPNAME = NONEUIEVENTBASE + 1;

	// define the UI related event
	public static final int UIEVENTBASE = 200;
	public static final int UIEVENT1 = UIEVENTBASE + 1;

	private WifiManager mWifiManager;
	private WifiUtils mWifiUtils;


	CRTServer2 mCRTServer;
	// CRTConnectionServer crtConnectionServer = null;
	CRTServer2 mCRTServer2 = null;
	CRTClient2 mCRTClient2 = null;

	private static boolean isGameHost;
	
	public PlayerFragment mPlayerFragment;
	public GamePadFragment mGamePadFragment;
	public SearchFragment mSearchFragment;

	private FragmentManager fragmentManager;

	public FragmentTransaction transaction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base_game);

		fragmentManager = getFragmentManager();
		transaction = fragmentManager.beginTransaction();

		mGamePadFragment = new GamePadFragment();
		transaction.replace(R.id.gamepad_fragment, mGamePadFragment);

		mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		setWifiUtils(new WifiUtils(mWifiManager));

		mCRTServer = new CRTServer2();

	}



	public FragmentTransaction getTransaction() {
		return transaction;
	}



	// get the default user name
	public String getUserName() {

		String androidID = android.provider.Settings.System.getString(
				getContentResolver(), "android_id");
		String userName;

		if (androidID.length() > 4) {
			userName = USERPREFIX + androidID.substring(0, 4);
		} else {
			userName = USERPREFIX;
		}

		return userName;
	}


	public WifiUtils getWifiUtils() {
		return mWifiUtils;
	}

	public void setWifiUtils(WifiUtils mWifiUtils) {
		this.mWifiUtils = mWifiUtils;
	}

	public static boolean isGameHost() {
		return isGameHost;
	}

	public static void setGameHost(boolean isGameHost) {
		BaseGameActivity.isGameHost = isGameHost;
	}


}
