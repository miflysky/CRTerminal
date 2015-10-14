package com.tieto.crterminal.model;

public class JsonCommadConstant {

	
	/*****************************The data type********************************************/
	public static final String EVENTSTRING = "event";
	public static final String INTDATA = "intdata";
	public static final String STRDATA = "strdata";
	
	
	/****************************** The event from server *****************************/
	private static final int FROM_SERVER_EVENT_BASE = 1;
	
	//server inform client game started
	public static final int FROM_SERVER_EVENT_NULL_STARTGAME = FROM_SERVER_EVENT_BASE + 1;
	//server inform client game result
	public static final int FROM_SERVER_EVENT_NULL_GAMERESULT = FROM_SERVER_EVENT_BASE + 2;
	
	
	public static final int FROM_SERVER_EVENT_NULL_END = FROM_SERVER_EVENT_BASE + 9;
	
	
	//server inform client new round start
	public static final int FROM_SERVER_EVENT_INT_NEWROUND = FROM_SERVER_EVENT_BASE + 10;
	//server inform client game round end
	public static final int FROM_SERVER_EVENT_INT_ENDROUND = FROM_SERVER_EVENT_BASE + 11;
	
	
	
	
	//server inform client user list
	public static final int FROM_SERVER_EVENT_STR_USERLIST = FROM_SERVER_EVENT_BASE + 20;	
	
	/****************************** The event from client *****************************/
	private static final int FROM_CLIENT_EVENT_BASE = 100;
	
	//client inform server I'm login
	public static final int FROM_CLIENT_EVENT_NULL_LOGIN = FROM_CLIENT_EVENT_BASE + 1;
	//client inform server I'm login out
	public static final int FROM_CLIENT_EVENT_NULL_LOGOUT = FROM_CLIENT_EVENT_BASE + 2;

	//client inform server what is his choice
	public static final int FROM_CLIENT_EVENT_CHOICE = FROM_CLIENT_EVENT_BASE + 10;
	
}
