package com.tieto.crterminal.model;

import java.util.ArrayList;

import android.content.Context;

import com.tieto.crterminal.model.network.CRTClient;
import com.tieto.crterminal.model.wifi.WifiUtils;

public class GameGuestPlayer extends JanKenPonPlayer{

	@SuppressWarnings("unused")
	private WifiUtils mWifiUtils;
	@SuppressWarnings("unused")
	private CRTClient mSocketClient;

	public GameGuestPlayer(Context context, WifiUtils wifiUtils, String userName) {
		mWifiUtils = wifiUtils;
		mSocketClient = new CRTClient();
		name = userName;
	}

	
	public ArrayList<String> getGameList() {
		return null;
		
	}
	
	public void joingGame() {
		
	}
	
	public void endGame(){
		
	}
	
}
