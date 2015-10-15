package com.tieto.crterminal.model.player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.tieto.crterminal.model.command.JsonCRTCommand;
import com.tieto.crterminal.model.command.JsonCommadConstant;
import com.tieto.crterminal.model.command.JsonCommandBuilder;
import com.tieto.crterminal.model.network.CRTServer2;
import com.tieto.crterminal.model.network.CRTServer2.ServerConnectionCallBack;
import com.tieto.crterminal.model.network.SocketConnectionBase;

public class GamePlayerHost extends GamePlayer implements ServerConnectionCallBack{

	private static final String TAG = GamePlayerGuest.class.getSimpleName();
	CRTServer2 mConnection;
	private Handler mHandler;
	private static int mCurrentRound = 0;
	
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
		JsonCRTCommand command = JsonCommandBuilder
				.buildJanKenPonValueCommand(mName,value);
		mConnection.broadcastMessage(command.toString());
	}
	
	public void newRound() {
		//send the new round command
		mCurrentRound ++;
		JsonCRTCommand command = JsonCommandBuilder.buildNewRoundCommand(mCurrentRound);
		mConnection.broadcastMessage(command.toString());
		playersMap.put(mName, this);
	}

	public void endRound(ArrayList<GamePlayer> winers, ArrayList<GamePlayer> loser) {
		JsonCRTCommand command = JsonCommandBuilder.buildEndRoundCommand(mCurrentRound,winers,loser);
		mConnection.broadcastMessage(command.toString());
		playersMap.clear();
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
		case JsonCommadConstant.EVENT_STR_JOIN:
			//send to all join
			JsonCRTCommand playerListCommand = JsonCommandBuilder.buildPlayerListCommandplayersMap(playersMap);
			mConnection.broadcastMessage(playerListCommand.toString());			
			break;			
		case JsonCommadConstant.EVENT_STR_LEAVE:
			mConnection.broadcastMessage(command.getStrData());
			playersMap.remove(command.getValue());
			break;
		case JsonCommadConstant.EVENT_STR_CHOOSE:
			mConnection.broadcastMessage(command.getStrData());
			GamePlayer player1 = playersMap.get(command.getValue());
			player1.status = GamePlayer.READY;
			checkIsAllChiose();
			break;
		default:
			Log.w(TAG, "no handle command: " + event);
			return;
		}

		//post message to UI
		sendMessageToUI(command);
	}
	
	private void checkIsAllChiose() {
		ArrayList<GamePlayer> players = new ArrayList<>();
		Iterator<Entry<String, GamePlayer>> iterator = playersMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String,GamePlayer> entry = iterator.next();
			players.add(entry.getValue());
			if(entry.getValue().status == GamePlayer.NOT_READY){
				//still have some player not chiose
				return;
			}
		}
		//all player is set calculate
		winArrayList.clear();
		lostArrayList.clear();
		JanKenPon.judgeCurrentMatchResult(players, winArrayList, lostArrayList);
		endRound(winArrayList, lostArrayList);
		JsonCRTCommand command = new JsonCRTCommand(JsonCommadConstant.EVENT_INT_ENDROUND);
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
