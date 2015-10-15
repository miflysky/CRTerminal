package com.tieto.crterminal.model.player;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.tieto.crterminal.model.command.JsonCRTCommand;
import com.tieto.crterminal.model.command.JsonCommadConstant;
import com.tieto.crterminal.model.network.CRTServer2;
import com.tieto.crterminal.model.network.CRTServer2.ServerConnectionCallBack;
import com.tieto.crterminal.model.network.SocketConnectionBase;
import com.tieto.crterminal.model.network.SocketConnectionServer;

public class GamePlayerHost extends GamePlayerBase implements ServerConnectionCallBack{

	private static final String TAG = GamePlayerGuest.class.getSimpleName();
	SocketConnectionServer mConnection;
	private Handler mHandler;
	
	public interface GameHostCallback{
		void onUpdateUser();
		void OnError(String errorMessage);
	}
	
	public enum GameStatus {
		NOT_START,
		STARTED,
		PLAYING
	}
	
	public GamePlayerHost(String name,Handler handler){
		super(name);
		mHandler = handler;
		mConnection = new CRTServer2(this);
		mConnection.openConnection();
	}
	
	public void HostGamestart(){
		
		
	}
	
	@Override
	public void sendJanKenPonValue(int value){
		//TODO: add to local list
	}
	
	@Override
	protected SocketConnectionBase getConnection() {
		return mConnection;
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
		case JsonCommadConstant.FROM_CLIENT_EVENT_STR_JOIN:
			//TODO: set status 
			break;
		case JsonCommadConstant.FROM_CLIENT_EVENT_INT_CHOICE:
			//TODO: set status
			break;			
		case JsonCommadConstant.FROM_CLIENT_EVENT_STR_LEAVE:
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
	
	/*
    private GameStatus mGameStatus = GameStatus.NOT_START;
	private WifiUtils mWifiUtils;
	private CRTServer mSocketServer;
	private ArrayList<GameHostCallback> mCallbacks = new ArrayList<GameHostCallback>();
	private int mCurrentRound;
	
	public GameHostPlayer(Context context, WifiUtils wifiUtils, String userName) {
		mWifiUtils = wifiUtils;
		name = userName;
		mSocketServer = new CRTServer();
	}
	
	public GameStatus getStatus() {
		return mGameStatus;
	}
	
	//start the game,  when start game other unit can join
	public void startGame() {

		mSocketServer.startSocketServer();
		mGameStatus = GameStatus.STARTED;
	}
	
	//stop the game
	public void stopGame() {
		mSocketServer.stopSocketServer();

	}

	public void newRound() {
		//send the new round command
		mCurrentRound++;
		JsonCRTCommand command = JsonCommandBuilder.buildNewRoundCommand(mCurrentRound);
		mSocketServer.sendDataToAll(command.getByte());
		
		//update the status
		mGameStatus = GameStatus.PLAYING;
	}

	public void endRound() {
		JsonCRTCommand command = JsonCommandBuilder.buildEndRoundCommand(mCurrentRound);
		mSocketServer.sendDataToAll(command.getByte());
	}

	public void registerCallBack(GameHostCallback callback) {
		mCallbacks.add(callback);
	}

	public void unRegisterCallBack(GameHostCallback callback) {
		mCallbacks.remove(callback);
	}

	private void notifyCallbackError(String string) {
		for(GameHostCallback callback : mCallbacks){
			callback.OnError(string);
		}
	}
	
	*/

}
