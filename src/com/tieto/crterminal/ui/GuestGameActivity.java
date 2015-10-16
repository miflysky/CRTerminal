package com.tieto.crterminal.ui;

import java.lang.ref.WeakReference;

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

import com.tieto.crterminal.R;
import com.tieto.crterminal.model.command.JsonCommadConstant;
import com.tieto.crterminal.model.player.GamePlayerGuest;

public class GuestGameActivity extends BaseGameActivity {

	private boolean mGuestFirstConnectted;
	private NetworkConnectChangedReceiver mNetworkConnectChangedReceiver;
	public GamePlayerGuest mGuestPlayer;
	private PlayerFragment mPlayerFragment;
	private SearchFragment mSearchFragment;
	public Handler mGamePlayerHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		startGame();
		mGamePlayerHandler = new GamePlayerHandler(this);
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

		//getWifiUtils().disableWifi();
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

	
	
	public static class  GamePlayerHandler extends Handler {
		
		private WeakReference<GuestGameActivity> weakActivity;

		public GamePlayerHandler(GuestGameActivity guestGameActivity) {
 			 weakActivity = new WeakReference<GuestGameActivity>(guestGameActivity);
		}

		@Override
		public void handleMessage(Message msg) {

			GuestGameActivity guestGameActivity = weakActivity.get();
			if(guestGameActivity == null){
				return;
			}
			
			guestGameActivity.mPlayerFragment.notifyUIChange();
			switch (msg.what) {
			case JsonCommadConstant.EVENT_NULL_STARTGAME:
				Log.i(TAG, "Game start.");

				break;

			case JsonCommadConstant.EVENT_NULL_ENDGAME:
				Log.i(TAG, "Game end.");
				break;

			case JsonCommadConstant.EVENT_INT_NEWROUND:
				Log.i(TAG, "Game new round.");
				guestGameActivity.mGamePadFragment.showGamepad();
				break;

			case JsonCommadConstant.EVENT_INT_ENDROUND:
				Log.i(TAG, "Game end round.");
				String result = msg.getData().getString(
						JsonCommadConstant.KEY_COMMAND_VALUE);
				guestGameActivity.mPlayerFragment.notifyResult(result);
				break;

			case JsonCommadConstant.EVENT_STR_JOIN:
				Log.i(TAG, "Join game success.");
				String nameAdd = msg.getData().getString(
						JsonCommadConstant.KEY_COMMAND_VALUE);
				guestGameActivity.mPlayerFragment.playerAdd(nameAdd);

				break;
			case JsonCommadConstant.EVENT_STR_CHOOSE:
				String choose = msg.getData().getString(
						JsonCommadConstant.KEY_COMMAND_VALUE);
				int value = Integer.decode(choose).intValue();
				String userName = msg.getData().getString(
						JsonCommadConstant.KEY_USER_NAME);
				guestGameActivity.mPlayerFragment.playMakeChoice(userName, value);
				break;
			}
		}
	};

	public class NetworkConnectChangedReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			String action = intent.getAction();

			if (TextUtils.equals(action,
					ConnectivityManager.CONNECTIVITY_ACTION)) {

				ConnectivityManager connectivityManager = (ConnectivityManager) context
						.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo wifiNetworkInfo = connectivityManager
						.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

				if (wifiNetworkInfo.isConnected()) {

					if (!mGuestFirstConnectted) {

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
					}
				} else {
					mGuestFirstConnectted = false;
				}

			}
			return;
		}

	}

}
