package com.tieto.crterminal.ui;

import com.tieto.crterminal.R;
import com.tieto.crterminal.model.network.CRTServer;
import com.tieto.crterminal.model.wifi.WifiUtils;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
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

	CRTServer mCRTServer;

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

		Intent intent = getIntent();

		mIsGameOwner = intent.getBooleanExtra("OWNER", true);
		mFragmentManager = getFragmentManager();
		mTransaction = mFragmentManager.beginTransaction();

		mGamePadFragment = new GamePadFragment();
		mTransaction.replace(R.id.gamepad_fragment, mGamePadFragment);
		// mTransaction.commit();

		mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		mWifiUtils = new WifiUtils(mWifiManager);

		mCRTServer = new CRTServer();
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

		mWifiUtils.disableWifi();

	}

	// get the default user name
	private String getUserName() {

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

		mCRTServer.startSocketServer();

		createGame();

	}

	private void startGameAsGuest() {
		mWifiUtils.startWifiScan();
		findGameOwner();
	}

	private void findGameOwner() {
		mSearchFragment = new SearchFragment();
		mTransaction.replace(R.id.main_fragment, mSearchFragment);
		mTransaction.commit();
	}

	private void createGame() {
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

	private void startAsHost(boolean enabled) {

	}

}
