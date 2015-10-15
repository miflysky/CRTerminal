package com.tieto.crterminal.ui;

import com.tieto.crterminal.R;
import com.tieto.crterminal.model.network.CRTServer2;

import android.os.Bundle;
import android.util.Log;

public class HostGameActivity extends BaseGameActivity {

	
	public PlayerFragment mPlayerFragment;
	private String mMyName;
	
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

		// mCRTServer.startSocketServer();

		// added by lujun - begin
		mCRTServer2 = new CRTServer2();
		mCRTServer2.startServer(null);
		// added by lujun - end

		createGame();

	}
	private void createGame() {
		mPlayerFragment = new PlayerFragment();
		getTransaction().replace(R.id.main_fragment, mPlayerFragment);
		getTransaction().commit();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		getWifiUtils().disableAP();
	}
	
	

}
