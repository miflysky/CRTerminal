package com.tieto.crterminal.model.network;

public interface SocketConnectionBase {

	public static final int CONNECTIONPORT = 2000;


	public void openConnection();
	public void closeConnection();
	
}
