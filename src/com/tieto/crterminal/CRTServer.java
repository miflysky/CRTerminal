package com.tieto.crterminal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import org.json.JSONObject;

import android.util.Log;

import com.tieto.crterminal.SocketMessage;

public class CRTServer {

	private static final String TAG = "CRTerminal";

	private static final int SOCKET_PORT = 2000;

	private ServerSocket mServerSocket;

	private boolean mIsStartServer;

	private ArrayList<SocketMessage> mBroadcastMsgList = new ArrayList<SocketMessage>();
	private ArrayList<SocketThread> mThreadList = new ArrayList<SocketThread>();

	// private ArrayList<SocketThread> mThreadList = new
	// ArrayList<SocketThread>();

	CRTServer() {
		// TODO: initialize...
	}

	public void startSocketServer() {
		ServerThread thread = new ServerThread("ServerThread");
		mIsStartServer = true;
		thread.start();
	}

	public void stopSocketServer() {
		mIsStartServer = false;
	}

	public class ServerThread extends Thread {

		public ServerThread(String threadName) {

			super(threadName);
		}

		@Override
		public void run() {
			super.run();

			Log.i(TAG, "ServerThread start");

			mIsStartServer = true;

			try {

				// create a server socket
				mServerSocket = new ServerSocket(SOCKET_PORT);

				Socket socket = null;

				// represent a client, = 0 is for broadcast
				int socketID = 1;

				// start the broadcast thread
				startBroadcastThread();

				// server socket main loop
				while (mIsStartServer) {
					// waiting for client connect
					socket = mServerSocket.accept();

					// client connected ...

					SocketThread thread = new SocketThread(socket, socketID++);
					thread.mIsSocketThreadStart = true;
					thread.start();
					mThreadList.add(thread);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			Log.i(TAG, "ServerThread stoped");
		}

		// for broadcast msg to all client
		private void startBroadcastThread() {
			new Thread("BroadcastThread") {
				@Override
				public void run() {
					super.run();
					try {
						while (mIsStartServer) {

							if (mBroadcastMsgList.size() > 0) {

								SocketMessage msg = mBroadcastMsgList.get(0);
								for (SocketThread to : mThreadList) {
									if ((to.mSocketID == 0)
											|| (to.mSocketID == msg.to)) {
										BufferedWriter writer = to.mWriter;
										JSONObject json = new JSONObject();

										json.put("to", msg.to);
										json.put("from", msg.from);

										json.put("event", msg.event);
										json.put("msg", msg.msg);

										json.put("time", msg.time);

										writer.write(json.toString() + "\n");

										writer.flush();
										break;
									}

								}

								mBroadcastMsgList.remove(0);
							}
							Thread.sleep(200);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}.start();
		}

	}

	// each for one client
	private class SocketThread extends Thread {

		public int mSocketID;
		public Socket mSocket;// Socket鐢ㄤ簬鑾峰彇杈撳叆娴併�杈撳嚭娴� public BufferedWriter
								// mWriter;// BufferedWriter 鐢ㄤ簬鎺ㄩ�娑堟伅
		public BufferedReader mReader;// BufferedReader 鐢ㄤ簬鎺ユ敹娑堟伅
		public BufferedWriter mWriter;
		public boolean mIsSocketThreadStart;

		public SocketThread(Socket socket, int count) {
			mSocketID = count;
			mSocket = socket;
			Log.i(TAG, "New client connected" + mSocketID);
		}

		@Override
		public void run() {
			super.run();

			try {
				// initialize BufferedReader
				mReader = new BufferedReader(new InputStreamReader(
						mSocket.getInputStream(), "utf-8"));

				// initialize BufferedWriter
				mWriter = new BufferedWriter(new OutputStreamWriter(
						mSocket.getOutputStream(), "utf-8"));

				while (mIsStartServer) {
					// check if reader is ready
					if (mReader.ready()) {
						// read
						String data = mReader.readLine();
						// 璁瞕ata浣滀负json瀵硅薄鐨勫唴瀹癸紝鍒涘缓涓�釜json瀵硅薄
						JSONObject json = new JSONObject(data);
						// 鍒涘缓涓�釜SocketMessage瀵硅薄锛岀敤浜庢帴鏀秊son涓殑鏁版嵁
						SocketMessage msg = new SocketMessage();
						msg.to = json.getInt("to");
						msg.msg = json.getString("msg");
						msg.from = mSocketID;

						// 鎺ユ敹鍒颁竴鏉℃秷鎭悗锛屽皢璇ユ秷鎭坊鍔犲埌娑堟伅闃熷垪mMsgList
						// mMsgList.add(msg);
						Log.i(TAG, "msg content: " + msg.msg);

					}
					// 鐫＄湢100ms锛屾瘡100ms妫�祴涓�鏄惁鏈夋帴鏀跺埌娑堟伅
					Thread.sleep(100);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
}
