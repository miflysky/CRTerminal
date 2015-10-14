package com.tieto.crterminal.model.player;

import java.util.ArrayList;

import android.content.Context;

import com.tieto.crterminal.model.network.CRTClient;
import com.tieto.crterminal.model.network.SocketConnection;
import com.tieto.crterminal.model.wifi.WifiUtils;

public class GameGuestPlayer extends JanKenPonPlayer implements PlayerCallbacks{

	@SuppressWarnings("unused")
	private WifiUtils mWifiUtils;

	private SocketConnection mConnection;

	public GameGuestPlayer(String userName) {
		super(userName);

		mConnection = new CRTClient();

	}

	public ArrayList<String> getGameList() {
		return null;

	}

	@Override
	protected SocketConnection getConnection() {
		return mConnection;
	}

	public void joinGame() {

	}

	public void leaveGame() {

	}

}
