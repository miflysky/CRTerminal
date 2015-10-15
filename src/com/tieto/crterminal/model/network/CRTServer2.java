package com.tieto.crterminal.model.network;

import android.R.bool;
import android.R.string;
import android.os.Handler;
import android.util.Log;

public class CRTServer2 implements SocketConnectionServer {
    private final String TAG = "CRTServer2";
	private CRTServer2Thread serverThread = null;
	private final int port = SocketConnectionBase.CONNECTIONPORT;
	private boolean isServerRunning = false;

	private Handler handler = null;
	private TCPProtocol protocol = null;
	private static final int BufferSize = 1024;
	private static final int TimeOut = 3000;

	public void startServer(Handler handler) {
	    Log.d(TAG, "startServer on port:" + port);
	    
		this.handler = handler;
		this.protocol = new TCPProtocolImpl(BufferSize, handler);
		this.serverThread = new CRTServer2Thread(port, protocol);
		this.isServerRunning = true;
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
			Log.e(TAG, "Exception:" + e.getMessage());
		}

	}

	@Override
	public void openConnection() {
	    startServer(null);
	}

	@Override
	public void closeConnection() {
	    stopServer();
	}
}
