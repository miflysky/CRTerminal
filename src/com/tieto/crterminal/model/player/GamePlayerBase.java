// JanKenPonPlayer.java

package com.tieto.crterminal.model.player;

import com.tieto.crterminal.model.network.SocketConnectionBase;

abstract public class GamePlayerBase {

	public String mName;
	public int mValue;
	public int status = NOT_READY;

	public final static int NOT_READY = 0;
	public final static int READY = 1;

	public GamePlayerBase(String username) {
		mName = username;
	}

	// get socket connection host & guest should implement its own function
	abstract SocketConnectionBase getConnection();

	// host & guest should implement its own function
	abstract void sendJanKenPonValue(int value);

}
