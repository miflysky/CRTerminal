package com.tieto.crterminal.model.network;

public interface SocketConnectionServer extends SocketConnectionBase{

	void broadcastMessage(String msg);

}
