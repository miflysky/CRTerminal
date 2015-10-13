package com.tieto.crterminal.model;

import com.tieto.crterminal.model.network.SocketConnectiong;

abstract public class GamePlayer extends JanKenPonPlayer{

	public void setFingerValue(JanKenPonValue paper) {
		JsonCRTCommand command = JsonCommandBuilder.buildSetFingerValueCommand(paper);
		getConnecting().sendData(command.getByte());
		value = paper;
	}

	
	public void getPlayers() {
		//TODO: get players
	}

	protected SocketConnectiong getConnecting() {
		return null;
	}


	
	
}
