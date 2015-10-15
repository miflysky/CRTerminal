package com.tieto.crterminal.model.player;

import java.util.ArrayList;

import android.content.Context;

import com.tieto.crterminal.model.command.JsonCRTCommand;
import com.tieto.crterminal.model.command.JsonCommandBuilder;
import com.tieto.crterminal.model.network.CRTServer2;
import com.tieto.crterminal.model.network.SocketConnectionBase;
import com.tieto.crterminal.model.network.SocketConnectionServer;




public class GamePlayerHost extends GamePlayerBase{

	
	
	SocketConnectionServer mConnection;
	
	
	
	
	public interface GameHostCallback{
		void onUpdateUser();
		void OnError(String errorMessage);
	}
	
	public GamePlayerHost(String name){
		super(name);
		
		// added by lujun - begin
		mConnection = new CRTServer2();
		mConnection.openConnection();
		// added by lujun - end	
	}
	
	public void HostGamestart(){
		
		
	}
	
	public enum GameStatus {
		NOT_START,
		STARTED,
		PLAYING
	}
	
	
	@Override
	public void sendJanKenPonValue(int value){
		//TODO: add to local list
	}
	
	@Override
	protected SocketConnectionBase getConnection() {
		return mConnection;
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
