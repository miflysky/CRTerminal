package com.tieto.crterminal.model;

import com.tieto.crterminal.model.wifi.WifiUtils;

import android.content.Context;
import android.net.wifi.WifiManager;


public class GamePlayerFactory {

	public static GameHostPlayer createGameHost(Context context, String userName) {
		//create Wifi ap 
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiUtils wifiUtils = new WifiUtils(wifiManager);

		//create db 
		
		//create game owner
		return new GameHostPlayer(context,wifiUtils,userName) ;
	}

	public static GameGuestPlayer createGameGuest(Context context, String userName) {
		//create Wifi ap 
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiUtils wifiUtils = new WifiUtils(wifiManager);

		//create db 
		
		//create game owner
		return new GameGuestPlayer(context,wifiUtils,userName) ;
	}

}
