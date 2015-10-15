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

	private static final String KEY_COMMAND_VALUE = "commandValue";

	@SuppressWarnings("unused")
	private WifiUtils mWifiUtils;

	private SocketConnectionClient mConnection;

	private Handler mHandler;

	public GamePlayerGuest(String userName, String serverIP, Handler handler) {
		super(userName);

		try {
			mConnection = new CRTClient2(serverIP,this);
			mConnection.openConnection();
			mHandler = handler;
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
	public void getStringByNetwork(String receivedString) {
		//get data from network
		JsonCRTCommand command = new JsonCRTCommand(receivedString);
		hanldeCommand(command);		
	}

	private void hanldeCommand(JsonCRTCommand command) {
		int event = command.getEvent();
		switch (event) {
		case JsonCommadConstant.FROM_SERVER_EVENT_NULL_STARTGAME:
			//TODO: set status 
			break;
		case JsonCommadConstant.FROM_SERVER_EVENT_NULL_GAMERESULT:
			//TODO: set status
			break;			
		case JsonCommadConstant.FROM_SERVER_EVENT_INT_NEWROUND:
			//TODO: set status 
			break;
		case JsonCommadConstant.FROM_SERVER_EVENT_INT_ENDROUND:
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
		bundle.putString(KEY_COMMAND_VALUE,command.getValue());
		mHandler.sendMessage(message);
	}

}
