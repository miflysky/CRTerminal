package com.tieto.crterminal.model.network;

public interface SocketConnection {

	void sendData(byte[] data);

	void openConnection();
	
	void closeConnection();
}
