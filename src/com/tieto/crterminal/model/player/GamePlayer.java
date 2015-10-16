// JanKenPonPlayer.java

package com.tieto.crterminal.model.player;

import java.util.ArrayList;
import java.util.HashMap;

import com.tieto.crterminal.model.network.SocketConnectionBase;

public class GamePlayer {

	public String mName;
	public int mValue;
	public int status = NOT_READY;

	public final static int NOT_READY = 0;
	public final static int READY = 1;
	
	public final static int WIN = 8;
	public final static int LOSE = 9;
	public final static int DRAW = 10;
	
	protected HashMap<String, GamePlayer> playersMap = new HashMap<String, GamePlayer>();
	public ArrayList<GamePlayer> winArrayList = new ArrayList<GamePlayer>();
	public ArrayList<GamePlayer> lostArrayList = new ArrayList<GamePlayer>();

	
	public GamePlayer(String username) {
		mName = username;
	}

	// get socket connection host & guest should implement its own function
	SocketConnectionBase getConnection() {
		return null;
	}

	// host & guest should implement its own function
	void sendJanKenPonValue(int value) {
	}

}
