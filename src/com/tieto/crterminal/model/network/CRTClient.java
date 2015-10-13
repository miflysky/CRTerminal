package com.tieto.crterminal.model.network;

public class CRTClient {

	public CRTClient() {
		// TODO: initialize...
	}

	public void startSocketClient() {
		// TODO: create thread
	}

	public void stopSocketClient() {
		// TODO: stop thread
	}

	public boolean ClientConnect() {

		return true;
	}

	public boolean ClientDisconnect() {

		return true;
	}

	public boolean ClientLogin() {

		return true;
	}

	public boolean ClientLogout() {

		return true;
	}

	public class ClientThread extends Thread {

		public ClientThread(String threadName) {
			super(threadName);
		}

	}

}
