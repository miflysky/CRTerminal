package com.tieto.crterminal;

public class SocketMessage {  
	
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
	
	
	//socket id, to who, = 0 stand for broadcast msg
    public int to;
    
    //socket id, from who
    public int from;
    
    //what is the event
    public int event;
    
    //message content
    public String msg; 
    
    //message receive/send time
    public String time;
    
    //public SocketThread thread;//socketThread下面有介绍  
}  



