package com.tieto.crterminal.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.tieto.crterminal.R;
import com.tieto.crterminal.model.command.JsonCommadConstant;
import com.tieto.crterminal.model.player.GamePlayerHost;

public class HostGameActivity extends BaseGameActivity {

	
	public PlayerFragment mPlayerFragment;
	private GamePlayerHost mHostPlayer;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		startGameAsHost();

	}

	
	public void startGameAsHost() {

		Log.i(TAG, "start as host");

		setGameHost(true);
		// start ap
		boolean success = getWifiUtils().enableAP(APPREFIX + mMyName);
		if (!success) {

			Log.w(TAG, "start ap failed");

			// TODO: need info user?
			return;
		}
		createGame();

	}
	private void createGame() {
		
		mHostPlayer = new GamePlayerHost("", mGamePlayerHandler);
		mPlayerFragment = new PlayerFragment(mHostPlayer);
		getTransaction().replace(R.id.main_fragment, mPlayerFragment);
		getTransaction().commit();
	}
	
	public static Handler mGamePlayerHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case JsonCommadConstant.FROM_CLIENT_EVENT_INT_CHOICE:

				break;

			case JsonCommadConstant.FROM_CLIENT_EVENT_STR_JOIN:

				break;

			case JsonCommadConstant.FROM_CLIENT_EVENT_STR_LEAVE:

				break;
							
			}
		}

	};
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		getWifiUtils().disableAP();
	}
	
	

}
