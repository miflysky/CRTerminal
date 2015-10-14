package com.tieto.crterminal.model;

import com.tieto.crterminal.model.network.SocketConnection;

abstract public class GamePlayer extends JanKenPonPlayer{

	public void setFingerValue(JanKenPonValue paper) {
		JsonCRTCommand command = JsonCommandBuilder.buildSetFingerValueCommand(paper);
		getConnection().sendData(command.getByte());
		value = paper;
	}

	
	public void getPlayers() {
		//TODO: get players
	}

	protected SocketConnection getConnection() {
		return null;
	}


	
	
}
