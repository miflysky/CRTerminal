package com.tieto.crterminal.model.player;

import java.io.IOException;
import java.util.ArrayList;


import com.tieto.crterminal.model.command.JsonCRTCommand;
import com.tieto.crterminal.model.command.JsonCommandBuilder;
import com.tieto.crterminal.model.network.CRTClient2;
import com.tieto.crterminal.model.network.SocketConnectionBase;
import com.tieto.crterminal.model.network.SocketConnectionClient;
import com.tieto.crterminal.model.wifi.WifiUtils;

public class GamePlayerGuest extends GamePlayerBase implements PlayerCallbacks {

	@SuppressWarnings("unused")
	private WifiUtils mWifiUtils;

	private SocketConnectionClient mConnection;

	public GamePlayerGuest(String userName, String serverIP) {
		super(userName);

		try {
			mConnection = new CRTClient2(serverIP);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public ArrayList<String> getGameList() {
		return null;

	}

	@Override
	protected SocketConnectionBase getConnection() {
		return mConnection;
	}

	public void joinGame() {
		JsonCRTCommand command = JsonCommandBuilder.buildJoinGameCommand(mName);
		mConnection.sendMsgToServer(command.toString());
	}

	public void leaveGame() {
		JsonCRTCommand command = JsonCommandBuilder
				.buildLeaveGameCommand(mName);
		mConnection.sendMsgToServer(command.toString());
	}

	
	public void sendJanKenPonValue(int value){
		mValue = value;

		JsonCRTCommand command = JsonCommandBuilder
				.buildJanKenPonValueCommand(value);
		mConnection.sendMsgToServer(command.toString());
	}

}
