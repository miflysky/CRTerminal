package com.tieto.crterminal.controler;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.tieto.crterminal.R;
import com.tieto.crterminal.model.GamePlayerFactory;
import com.tieto.crterminal.model.GameHostPlayer;
import com.tieto.crterminal.model.GamePlayer;
import com.tieto.crterminal.model.JanKenPonValue;
import com.tieto.crterminal.model.network.CRTServer;
import com.tieto.crterminal.model.wifi.WifiUtils;

public class MainActivity extends Activity implements OnClickListener {

	private static final String TAG = "CRTerminal";

	private WifiManager mWifiManager;

	public static final int APSELECTOR = 1;

	public static final String APPREFIX = "CRT_";
	public static final String USERPREFIX = "User_";

	// define the NoneUI related event
	public static final int NONEUIEVENTBASE = 100;
	public static final int GOTAPNAME = NONEUIEVENTBASE + 1;

	// define the UI related event
	public static final int UIEVENTBASE = 200;
	public static final int UIEVENT1 = UIEVENTBASE + 1;

	private EditText mNameText;

	private WifiUtils mWifiUtils;
	private Handler mNoneUIEventHandler;

	private Handler mUIEventHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UIEVENT1:
				String apname = (String) msg.obj;
				mWifiUtils.connectToSSID(apname);

				break;
			}
		}
	};

	private HandlerThread mHandlerThread;

	CRTServer mCRTServer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		initView();

		
		
		mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		mWifiUtils = new WifiUtils(mWifiManager);

		mCRTServer = new CRTServer();

		// To handle None-UI event
		startHandlerThread();

	}

	// create a thread to handle NoneUI event
	private void startHandlerThread() {
		mHandlerThread = new HandlerThread("NoneUI-HanderThread");
		mHandlerThread.start();
		mNoneUIEventHandler = new Handler(mHandlerThread.getLooper()) {

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case GOTAPNAME:
					String apname = (String) msg.obj;
					boolean sucess = mWifiUtils.connectToSSID(apname);
					
					if(sucess){
						//TODO: launch guest activity
					} else{
						Log.w(TAG, "connect to host failed");
					}
					
					
					break;
				}
			}
		};
	}

	public Handler getNoneUIEventHandler() {
		return mNoneUIEventHandler;
	}

	public Handler getUIEventHandler() {
		return mUIEventHandler;
	}

	private void initView() {

		// Set the default user name
		mNameText = (EditText) findViewById(R.id.username);

		String androidID = android.provider.Settings.System.getString(
				getContentResolver(), "android_id");
		String userName;

		if (androidID.length() > 4) {
			userName = USERPREFIX + androidID.substring(0, 4);
		} else {
			userName = USERPREFIX;
		}

		mNameText.setText(userName);

		// set the button click event
		findViewById(R.id.Host_Btn).setOnClickListener((OnClickListener) this);

		findViewById(R.id.Guest_Btn).setOnClickListener((OnClickListener) this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.Host_Btn:
			startAsHost(true);
			break;

		case R.id.Guest_Btn:
			startAsGuest();
			break;

		default:
			break;
		}
	}

	// start AP selector fragment
	public void startAsGuest() {

		Log.i(TAG, "start as guest");
		mWifiUtils.startWifiScan();

		APSelectDialogFragment apSelectDialog = APSelectDialogFragment
				.newInstance(this);
		apSelectDialog.show(getFragmentManager(), "apselect");

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public void startAsHost(boolean enabled) {

		
//		Log.i(TAG, "start as host");
//
//		boolean sucess = mWifiUtils.setApEnabled(true, APPREFIX
//				+ mNameText.getText().toString());
//
//		if (!sucess) {
//
//			Log.w(TAG, "start ap failed");
//
//			// TODO: need info user?
//		}
//
//		if (enabled == true) {
//			mCRTServer.startSocketServer();
//		} else {
//			mCRTServer.stopSocketServer();
//		}
		
		//TODO: launch host activity
		testHostObj();
		
	}

	@SuppressWarnings("unused")
	private void onDestory() {
		// TODO Auto-generated method stub
		mGameHost.unRegisterCallBack(mCallback);
		super.onDestroy();
	}

	private GameHostPlayer mGameHost;
	private void testHostObj() {
		String userName = APPREFIX
				+ mNameText.getText().toString();

		mGameHost = GamePlayerFactory.createGameHost(getApplicationContext(),userName);
		mGameHost.registerCallBack(mCallback);
		mGameHost.startGame();
		mGameHost.stopGame();
		mGameHost.newRound();
		mGameHost.endRound();
		
		GamePlayer player = (GamePlayer) mGameHost;
		player.setFingerValue(JanKenPonValue.Paper);
		player.getPlayers();
	}

	
	
	private static GameHostPlayer.GameHostCallback mCallback = new GameHostPlayer.GameHostCallback() {
		
		@Override
		public void onUpdateUser() {
			// TODO Auto-generated method stub
			
		}
		
		
		@Override
		public void OnError(String errorMessage) {
			// TODO Auto-generated method stub
			
		}
	};
	
}