package com.tieto.crterminal.model.network;

public interface ServerSocketConnection extends SocketConnection{

	void broadcastData(byte[] data);

}
