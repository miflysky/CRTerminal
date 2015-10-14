// JanKenPonPlayer.java

package com.tieto.crterminal.model.player;

import com.tieto.crterminal.model.command.JsonCRTCommand;
import com.tieto.crterminal.model.command.JsonCommandBuilder;
import com.tieto.crterminal.model.network.SocketConnection;

/*
 * 参与比赛者的信息，主要是用户名和其所出的值
 */

abstract public class JanKenPonPlayer {

	public String mName;
	public int mValue;

	public JanKenPonPlayer(String username) {
		mName = username;
	}

	public void sendJanKenPonValue(int value) {

		mValue = value;

		JsonCRTCommand command = JsonCommandBuilder
				.buildJanKenPonValueCommand(value);
		getConnection().sendData(command.getByte());

	}

	protected SocketConnection getConnection() {
		return null;
	}

}
