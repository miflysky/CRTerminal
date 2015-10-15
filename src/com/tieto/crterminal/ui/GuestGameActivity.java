package com.tieto.crterminal.ui;

import com.tieto.crterminal.R;
import com.tieto.crterminal.model.network.CRTClient2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

public class GuestGameActivity extends BaseGameActivity {

	private boolean mGuestFirstConnectted;
	private NetworkConnectChangedReceiver mNetworkConnectChangedReceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		startGameAsGuest();
	}

	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		endGameAsGuest();
	}
	
	private void endGameAsGuest() {

		mGuestFirstConnectted = false;
		unregisterReceiver(mNetworkConnectChangedReceiver);

		getWifiUtils().disableWifi();

	}
	
	public void startGameAsGuest() {

		setGameHost(false);
		// register a receive to get the connection event
		IntentFilter filter = new IntentFilter();

		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		mNetworkConnectChangedReceiver = new NetworkConnectChangedReceiver();
		registerReceiver(mNetworkConnectChangedReceiver, filter);

		mGuestFirstConnectted = false;

		getWifiUtils().startWifiScan();
		findGameOwner();
	}

	private void findGameOwner() {
		mSearchFragment = new SearchFragment();
		getTransaction().replace(R.id.main_fragment, mSearchFragment);
		getTransaction().commit();
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

						String apaddr = getWifiUtils().getAPAddress();

						Log.i(TAG, apaddr);

						mGuestFirstConnectted = true;
						// TODO: set status connected

						// added by lujun - begin
						try {
							mCRTClient2 = new CRTClient2(apaddr, 3333);
							// mCRTClient2.sendMsg("how are you, server!");
						} catch (Exception e) {
							// TODO: handle exception
							Log.e("GameActivity", e.getMessage());
						}

						// added by lujun - end

					}

				}
			}
			return;
		}

	}
}
