package com.tieto.crterminal.model.network;


public interface SocketConnectionClient extends SocketConnectionBase{

	void sendMsgToServer(String msg);

}
