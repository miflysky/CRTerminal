package com.tieto.crterminal.model;

public class JsonCommandBuilder {
	
	
	private static final String CMDTYPE = "CMDEVENT";
	
	private static final String DATATYPE = "CMDDATA";
	
	
	private static final int EVENT_FROM_CLIENT_BASE = 1;
	
	private static final int EVENT_FROM_SERVER_BASE = 100;
	
	//client inform server I'm login
	public static final int EVENT_FROM_CLIENT_LOGIN = EVENT_FROM_CLIENT_BASE + 1;
	//client inform server I'm login out
	public static final int EVENT_FROM_CLIENT_LOGOUT = EVENT_FROM_CLIENT_BASE + 2;
	//client inform server what is his choice
	public static final int EVENT_FROM_CLIENT_CHOICE = EVENT_FROM_CLIENT_BASE + 3;
	
	
	//server inform client game started
	public static final int EVENT_FROM_SERVER_STARTGAME = EVENT_FROM_SERVER_BASE + 1;
	//server inform client game result
	public static final int EVENT_FROM_SERVER_GAMERESULT = EVENT_FROM_SERVER_BASE + 2;
	//server inform client new round start
	public static final int EVENT_FROM_SERVER_NEWROUND = EVENT_FROM_SERVER_BASE + 3;
	//server inform client game round end
	public static final int EVENT_FROM_SERVER_ENDROUND = EVENT_FROM_SERVER_BASE + 4;
	
	
	
	
	public static JsonCRTCommand buildNewRoundCommand(int roundNumber) {
		JsonCRTCommand command = new JsonCRTCommand(EVENT_FROM_SERVER_NEWROUND);
		return command;
	}

	public static JsonCRTCommand buildEndRoundCommand(int roundNumber) {
		JsonCRTCommand command = new JsonCRTCommand();
		return command;
	}

	public static JsonCRTCommand buildSetFingerValueCommand(JanKenPonValue paper) {
		// TODO Auto-generated method stub
		return null;
	}

}
