package com.tieto.crterminal.model.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;

import android.util.Log;

public class CRTServer2Thread extends Thread {

	private final String TAG = "CRTServer2";
	// private Handler handler = null;
	private Selector selector = null;
	private ServerSocketChannel listenerChannel = null;

	private TCPProtocol protocol = null;
	// private static final int BufferSize=1024;
	private static final int TimeOut = 3000;

	public boolean stop = false;

	public CRTServer2Thread(int port, TCPProtocol protocol) {
		try {
			selector = Selector.open();

			listenerChannel = ServerSocketChannel.open();
			listenerChannel.socket().bind(new InetSocketAddress(port));
			listenerChannel.configureBlocking(false);
			listenerChannel.register(selector, SelectionKey.OP_ACCEPT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.protocol = protocol;
	}

	public void run() {
		while (!stop) {
			int iselect = 0;

			try {
				iselect = selector.select(TimeOut);
				if (iselect == 0) {
					Log.i(TAG, "iselect==0 wating continue...");
					continue;
				}

				Iterator<SelectionKey> keyIter = selector.selectedKeys()
						.iterator();
				while (keyIter.hasNext()) {
					SelectionKey key = keyIter.next();
					try {
						if (key.isAcceptable()) {
							protocol.handleAccept(key);
						}

						if (key.isReadable()) {
							protocol.handleRead(key);
						}
					} catch (IOException ex) {
						keyIter.remove();
						continue;
					}
					keyIter.remove();

				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}// end while

		// thread will exit here, cleanup
		try {
			listenerChannel.close();
			selector.close();
		} catch (Exception e2) {
			Log.e(TAG, e2.getMessage());
		}

	}
}
