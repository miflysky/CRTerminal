package com.tieto.crterminal.ui;

import com.tieto.crterminal.R;
import com.tieto.crterminal.model.player.GamePlayerGuest;
import com.tieto.crterminal.model.player.GamePlayerHost;
import com.tieto.crterminal.model.player.GamePlayerBase;
import com.tieto.crterminal.model.wifi.WifiUtils;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class GameActivity extends Activity {

	private static final String TAG = "CRTerminal";

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
	private NetworkConnectChangedReceiver mNetworkConnectChangedReceiver;

	private boolean mGuestFirstConnectted;

	// if I'm a host
	private GamePlayerBase mHostPlayer;

	// if I'm a guest
	private GamePlayerBase mGuestPlayer;

	private boolean mIsGameOwner;
	private PlayerFragment mPlayerFragment;
	private GamePadFragment mGamePadFragment;
	private SearchFragment mSearchFragment;

	private String mMyName;

	FragmentManager mFragmentManager;
	FragmentTransaction mTransaction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		// Set the default user name
		TextView nameText = (TextView) findViewById(R.id.player_me);
		mMyName = getUserName();
		nameText.setText(mMyName);

		// get default
		mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		mWifiUtils = new WifiUtils(mWifiManager);

		// create the gamepad fragment, host & guest are the same
		mFragmentManager = getFragmentManager();
		mTransaction = mFragmentManager.beginTransaction();
		mGamePadFragment = new GamePadFragment();
		mTransaction.replace(R.id.gamepad_fragment, mGamePadFragment);

		// Check if I'm a host or guest
		Intent intent = getIntent();
		mIsGameOwner = intent.getBooleanExtra("OWNER", true);

		// do difference things ...
		if (mIsGameOwner) {
			startGameAsHost();
		} else {
			startGameAsGuest();
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (mIsGameOwner) {
			endGameAsHost();
		} else {
			endGameAsGuest();
		}

	}

	private void endGameAsHost() {
		mWifiUtils.disableAP();
	}

	private void endGameAsGuest() {

		mGuestFirstConnectted = false;
		unregisterReceiver(mNetworkConnectChangedReceiver);

		mWifiUtils.disableWifi();

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

	private void startGameAsHost() {

		Log.i(TAG, "start as host");

		// start ap
		boolean success = mWifiUtils.enableAP(APPREFIX + mMyName);
		if (!success) {

			Log.w(TAG, "start ap failed");

			// TODO: need info user?
			return;
		}

		// Create the host player
		mHostPlayer = new GamePlayerHost(mMyName);

		// create the player list
		createPlayerList();

	}

	private void startGameAsGuest() {

		// register a receive to get the connection event
		IntentFilter filter = new IntentFilter();

		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		mNetworkConnectChangedReceiver = new NetworkConnectChangedReceiver();
		registerReceiver(mNetworkConnectChangedReceiver, filter);

		mGuestFirstConnectted = false;

		findGameOwner();
		
		mWifiUtils.startWifiScan();
		
	}

	private void findGameOwner() {
		mSearchFragment = new SearchFragment();
		mTransaction.replace(R.id.main_fragment, mSearchFragment);
		mTransaction.commit();
	}

	private void createPlayerList() {
		mPlayerFragment = new PlayerFragment();
		mTransaction.replace(R.id.main_fragment, mPlayerFragment);
		mTransaction.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public class NetworkConnectChangedReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			if (!mGuestFirstConnectted) {
				String action = intent.getAction();

				if (TextUtils.equals(action,
						ConnectivityManager.CONNECTIVITY_ACTION)) {

					ConnectivityManager connectivityManager = (ConnectivityManager) context
							.getSystemService(Context.CONNECTIVITY_SERVICE);
					NetworkInfo wifiNetworkInfo = connectivityManager
							.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

					if (wifiNetworkInfo.isConnected()) {

						String apaddr = mWifiUtils.getAPAddress();

						Log.i(TAG, apaddr);

						mGuestFirstConnectted = true;

						mGuestPlayer = new GamePlayerGuest(mMyName, apaddr);

					}

				}
			}
			return;
		}

	}

}
