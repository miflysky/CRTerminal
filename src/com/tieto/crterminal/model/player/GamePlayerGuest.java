package com.tieto.crterminal.model.player;

import java.io.IOException;
import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.tieto.crterminal.model.command.JsonCRTCommand;
import com.tieto.crterminal.model.command.JsonCommadConstant;
import com.tieto.crterminal.model.command.JsonCommandBuilder;
import com.tieto.crterminal.model.network.CRTClient2;
import com.tieto.crterminal.model.network.CRTClient2.ClientConnectionCallback;
import com.tieto.crterminal.model.network.SocketConnectionBase;
import com.tieto.crterminal.model.network.SocketConnectionClient;
import com.tieto.crterminal.model.wifi.WifiUtils;

public class GamePlayerGuest extends GamePlayerBase implements PlayerCallbacks , ClientConnectionCallback{

	private static final String TAG = GamePlayerGuest.class.getSimpleName();


	private static final int MSG_PLAYER_CONNECTED = 1000;


	@SuppressWarnings("unused")
	private WifiUtils mWifiUtils;

	private SocketConnectionClient mConnection;

	private Handler mHandler;

	public GamePlayerGuest(String userName, Handler handler) {
		super(userName);

		mHandler = handler;
		


	}
	
	public void ConnectToHost(String serverIP){
		try {
			mConnection = new CRTClient2(serverIP,this);
			mConnection.openConnection();
			
		} catch (IOException e) {
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
		mConnection.closeConnection();
	}

	
	public void sendJanKenPonValue(int value){
		mValue = value;

		JsonCRTCommand command = JsonCommandBuilder
				.buildJanKenPonValueCommand(value);
		mConnection.sendMsgToServer(command.toString());
	}

	@Override
	public void onReceiveMessage(String receivedString) {
		//get data from network
		JsonCRTCommand command = new JsonCRTCommand(receivedString);
		hanldeCommand(command);		
	}

	private void hanldeCommand(JsonCRTCommand command) {
		int event = command.getEvent();
		switch (event) {
		case JsonCommadConstant.EVENT_STR_JOIN:
			if(command.getValue().endsWith(mName))
				return;
		case JsonCommadConstant.EVENT_STR_LEAVE:
			if(command.getValue().endsWith(mName))
				return;
		case JsonCommadConstant.EVENT_STR_CHOOSE:
			if(command.getValue().endsWith(mName))
				return;
		case JsonCommadConstant.EVENT_INT_NEWROUND:
			//TODO: set status 
			break;
		case JsonCommadConstant.EVENT_INT_ENDROUND:
			//TODO: set status 
			break;
		default:
			Log.w(TAG, "no handle command: " + event);
			return;
		}

		//post message to UI
		sendMessageToUI(command);
	}

	private void sendMessageToUI(JsonCRTCommand command) {
		Message message = mHandler.obtainMessage();
		message.what = command.getEvent();
		Bundle bundle = message.getData();
		bundle.putString(JsonCommadConstant.KEY_COMMAND_VALUE,command.getValue());
		mHandler.sendMessage(message);
	}

	@Override
	public void onConnect() {
		//TODO: status on connect
		
		//send UI on connect
		mHandler.sendEmptyMessage(MSG_PLAYER_CONNECTED);
		
		//send to server name
		JsonCRTCommand command = JsonCommandBuilder.buildJoinGameCommand(mName);
		mConnection.sendMsgToServer(command.toString());
	}
}
