package com.tieto.crterminal.ui;

import com.tieto.crterminal.R;
import com.tieto.crterminal.model.command.JsonCommadConstant;
import com.tieto.crterminal.model.player.GamePlayerGuest;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
	private PlayerFragment mPlayerFragment;
	private SearchFragment mSearchFragment;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		startGame();
	}


	
	@Override
	protected void onDestroy() {
	    endGameAsGuest();
	    super.onDestroy();		
	}
	
	private void endGameAsGuest() {
	    mGuestPlayer.leaveGame();
		mGuestFirstConnectted = false;
		unregisterReceiver(mNetworkConnectChangedReceiver);

		getWifiUtils().disableWifi();	
	}
	
	public void startGame() {

		setGameHost(false);
		// register a receive to get the connection event
		IntentFilter filter = new IntentFilter();

		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		mNetworkConnectChangedReceiver = new NetworkConnectChangedReceiver();
		registerReceiver(mNetworkConnectChangedReceiver, filter);

		mGuestFirstConnectted = false;

		mGuestPlayer = new GamePlayerGuest(mMyName, mGamePlayerHandler);
				
		startSearchFragment();
		
		
	}

	private void startSearchFragment() {
		mSearchFragment = new SearchFragment();
		getTransaction().replace(R.id.main_fragment, mSearchFragment);
		getTransaction().commit();
	}
	
	public void startPlayerFragment() {
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		mPlayerFragment = new PlayerFragment(mGuestPlayer);
		ft.replace(R.id.main_fragment, mPlayerFragment);
		ft.commit();
	}

	
	
	public Handler mGamePlayerHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			mPlayerFragment.notifyUIChange();
			switch (msg.what) {
			case JsonCommadConstant.EVENT_NULL_STARTGAME:
				Log.i(TAG, "Game start.");
				GamePadFragment gamePad = new GamePadFragment();
				gamePad.showGamepad();
				break;

			case JsonCommadConstant.EVENT_NULL_ENDGAME:
				Log.i(TAG, "Game end.");
				break;

			case JsonCommadConstant.EVENT_INT_NEWROUND:
				Log.i(TAG, "Game new round.");
				break;
			
			case JsonCommadConstant.EVENT_INT_ENDROUND:
				Log.i(TAG, "Game end round.");
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
							
						} catch (Exception e) {
							// TODO: handle exception
							Log.e("GameActivity", e.getMessage());
						}						
						mGuestPlayer.joinGame();
					}

				}
			}
			return;
		}

	}
	
	
	
}
