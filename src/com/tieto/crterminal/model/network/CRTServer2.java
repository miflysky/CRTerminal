package com.tieto.crterminal.model.network;

import android.util.Log;

public class CRTServer2 implements SocketConnectionServer {
    private final String TAG = "CRTServer2";
	private CRTServer2Thread serverThread = null;
	private final int port = SocketConnectionBase.CONNECTIONPORT;

	private TCPProtocol protocol = null;
	private ServerConnectionCallBack mCallback;
	private static final int BufferSize = 1024;

	public interface ServerConnectionCallBack{

		void onReceiveMessage(String receivedString);
		
	}
	
	public CRTServer2(ServerConnectionCallBack callback) {
		mCallback = callback;
	}

	public void startServer() {
	    Log.d(TAG, "startServer on port:" + port);
		this.protocol = new TCPProtocolImpl(BufferSize, mCallback);
		this.serverThread = new CRTServer2Thread(port, protocol);
		this.serverThread.start();		
	}

	public void stopServer() {
	    Log.d(TAG, "stopServer ");
		serverThread.stop = true;
	}

	@Override
	public void broadcastMessage(String message) {
	    Log.d(TAG, "broadcastMessage: " + message);
	    
		try {
			this.protocol.handleBroadcast(message);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "Exception:" + e);
		}

	}

	@Override
	public void openConnection() {
	    startServer();
	}

	@Override
	public void closeConnection() {
	    stopServer();
	}
}
