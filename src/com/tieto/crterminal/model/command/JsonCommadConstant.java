package com.tieto.crterminal.model.command;

public class JsonCommadConstant {

	
	/*****************************The data type********************************************/
	public static final String EVENTSTRING = "event";
	public static final String INTDATA = "intdata";
	public static final String STRDATA = "strdata";
	public static final String KEY_COMMAND_VALUE = "commandValue";
	public static final String KEY_USER_NAME = "userName";

	
	/****************************** The event from server *****************************/
	private static final int FROM_SERVER_EVENT_BASE = 1;
	
	
	//server inform client game started
	public static final int EVENT_NULL_STARTGAME = FROM_SERVER_EVENT_BASE + 2;
	public static final int EVENT_NULL_ENDGAME = FROM_SERVER_EVENT_BASE + 4;

	
	//server inform client new round start
	public static final int EVENT_INT_NEWROUND = FROM_SERVER_EVENT_BASE + 10;
	//server inform client game round end
	public static final int EVENT_INT_ENDROUND = FROM_SERVER_EVENT_BASE + 11;
	
	public static final int EVENT_STR_JOIN = FROM_SERVER_EVENT_BASE + 12;
	public static final int EVENT_STR_LEAVE = FROM_SERVER_EVENT_BASE + 13;
	public static final int EVENT_STR_CHOOSE = FROM_SERVER_EVENT_BASE + 14;
	public static final int EVENT_STR_PLAYER_LIST = FROM_SERVER_EVENT_BASE + 15;

	
}
