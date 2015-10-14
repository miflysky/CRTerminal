package com.tieto.crterminal.model;

import com.tieto.crterminal.model.network.SocketConnection;

abstract public class GamePlayer extends JanKenPonPlayer{

	public void setFingerValue(int paper) {
		JsonCRTCommand command = JsonCommandBuilder.buildJanKenPonValueCommand(paper);
		getConnection().sendData(command.getByte());

	}

	
	public void getPlayers() {
		//TODO: get players
	}

	protected SocketConnection getConnection() {
		return null;
	}


	
	
}
