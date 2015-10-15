package com.tieto.crterminal.ui;

import com.tieto.crterminal.R;
import com.tieto.crterminal.model.command.JsonCommadConstant;
import com.tieto.crterminal.model.network.CRTClient2;
import com.tieto.crterminal.model.player.GamePlayerGuest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

public class GuestGameActivity extends BaseGameActivity {

	private boolean mGuestFirstConnectted;
	private NetworkConnectChangedReceiver mNetworkConnectChangedReceiver;
	public GamePlayerGuest mGuestPlayer;

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
		mGuestPlayer.leaveGame();

	}
	
	public void startGameAsGuest() {

		setGameHost(false);
		// register a receive to get the connection event
		IntentFilter filter = new IntentFilter();

		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		mNetworkConnectChangedReceiver = new NetworkConnectChangedReceiver();
		registerReceiver(mNetworkConnectChangedReceiver, filter);

		mGuestFirstConnectted = false;

		mGuestPlayer = new GamePlayerGuest(mMyName, mGamePlayerHandler);
				
		findGameOwner();
		
		
	}

	private void findGameOwner() {
		mSearchFragment = new SearchFragment(mGuestPlayer);
		getTransaction().replace(R.id.main_fragment, mSearchFragment);
		getTransaction().commit();
	}
	

	
	
	public Handler mGamePlayerHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case JsonCommadConstant.EVENT_NULL_STARTGAME:
				sendBroadcast(new Intent(GamePadFragment.GAME_READY_ACTION));
				break;

			case JsonCommadConstant.EVENT_NULL_ENDGAME:

				break;

			case JsonCommadConstant.EVENT_INT_NEWROUND:

				break;
			
			case JsonCommadConstant.EVENT_INT_ENDROUND:
				
				break;
				
			case JsonCommadConstant.EVENT_STR_JOIN:
				Log.i(TAG, "Join game success.");
				
				
				break;

			}
		}
	};
	
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
						
						try {
							mGuestPlayer.ConnectToHost(apaddr);
							mGuestPlayer.joinGame();
						} catch (Exception e) {
							// TODO: handle exception
							Log.e("GameActivity", e.getMessage());
						}						

					}

				}
			}
			return;
		}

	}
	
	
	
}
