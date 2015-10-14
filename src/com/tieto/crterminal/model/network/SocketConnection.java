package com.tieto.crterminal.model.network;

public interface SocketConnection {

	public void sendData(byte[] data);

	public void openConnection();
	
	public void closeConnection();
}
