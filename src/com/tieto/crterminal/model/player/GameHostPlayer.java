package com.tieto.crterminal.model.player;

import java.util.ArrayList;

import android.content.Context;

import com.tieto.crterminal.model.command.JsonCRTCommand;
import com.tieto.crterminal.model.command.JsonCommandBuilder;
import com.tieto.crterminal.model.network.CRTServer;
import com.tieto.crterminal.model.network.SocketConnection;
import com.tieto.crterminal.model.wifi.WifiUtils;



public class GameHostPlayer extends JanKenPonPlayer{

	
	public interface GameHostCallback{
		void onUpdateUser();
		void OnError(String errorMessage);
	}
	
	public GameHostPlayer(String name){
		super(name);
	}
	
	public void HostGamestart(){
		
		
	}
	
	public enum GameStatus {
		NOT_START,
		STARTED,
		PLAYING
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
	
	@Override
	protected SocketConnection getConnection() {
		return (SocketConnection)mSocketServer;
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