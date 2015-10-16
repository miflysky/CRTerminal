package com.tieto.crterminal.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;

import com.tieto.crterminal.R;
import com.tieto.crterminal.model.command.JsonCommadConstant;
import com.tieto.crterminal.model.player.GamePlayer;
import com.tieto.crterminal.model.player.GamePlayerHost;

public class HostGameActivity extends BaseGameActivity {

	
	public PlayerFragment mPlayerFragment;
	public GamePlayerHost mHostPlayer;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		startGame();
	}

	
	public void startGame() {

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
		
		mHostPlayer = new GamePlayerHost(mMyName, mGamePlayerHandler);
		mPlayerFragment = new PlayerFragment(mHostPlayer);
		getTransaction().replace(R.id.main_fragment, mPlayerFragment);
		getTransaction().commit();
	}
	
	public  Handler mGamePlayerHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case JsonCommadConstant.EVENT_STR_CHOOSE:
				String choose = msg.getData().getString(JsonCommadConstant.KEY_COMMAND_VALUE);
				int value = Integer.decode(choose).intValue();
				String userName = msg.getData().getString(JsonCommadConstant.KEY_USER_NAME);
				mPlayerFragment.playMakeChoice(userName, value);
				break;
			case JsonCommadConstant.EVENT_STR_JOIN:
				String nameAdd = msg.getData().getString(JsonCommadConstant.KEY_COMMAND_VALUE);
				mPlayerFragment.playerAdd(nameAdd);
				break;

			case JsonCommadConstant.EVENT_STR_LEAVE:
				String nameLeave = msg.getData().getString(JsonCommadConstant.KEY_COMMAND_VALUE);
				mPlayerFragment.playerLeave(nameLeave);
				break;
			case JsonCommadConstant.EVENT_INT_NEWROUND:
				Log.i(TAG, "Game new round.");
				mGamePadFragment.showGamepad();
				break;
			
			case JsonCommadConstant.EVENT_INT_ENDROUND:
				Log.i(TAG, "Game end round.");
				
				ArrayList<GamePlayer> winList = mHostPlayer.winArrayList;
				ArrayList<GamePlayer> lostList = mHostPlayer.lostArrayList;
				mPlayerFragment.notifyResult(winList, lostList);
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
