package com.tieto.crterminal.model.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.util.Iterator;

import android.util.Log;

import com.tieto.crterminal.model.network.CRTClient2.ClientConnectionCallback;

public class CRTClient2Thread implements Runnable {

	private final String TAG = "CRTClient2";
	private final String TAG2 = "CRTClient2Thread";
	private Selector selector;

	private ClientConnectionCallback mCallback;

	private SocketChannel socketChannel;

	private String hostIpAddress = "";
	private int hostPort = 0;
	private static final int TimeOut = 3000;

	public boolean stop = false;

	public CRTClient2Thread(Selector selector,
			ClientConnectionCallback mCallback, SocketChannel socketChannel,
			String hostIp, int hostListenningPort) {
		this.selector = selector;
		this.mCallback = mCallback;
		this.socketChannel = socketChannel;
		this.hostIpAddress = hostIp;
		this.hostPort = hostListenningPort;
		new Thread(this).start();
	}

	@Override
	public void run() {
		try {
			boolean connected = socketChannel.connect(new InetSocketAddress(
					hostIpAddress, hostPort));
			Log.i(TAG, TAG2 + ", IP:" + hostIpAddress + ", Port:" + hostPort
					+ " connected:" + connected);

			while (!socketChannel.finishConnect()) {
				// wait for connected;
			}

			mCallback.onConnect();
			
			Log.i(TAG, TAG2 + ", IP:" + hostIpAddress + ", Port:" + hostPort
					+ " connected finished");

			socketChannel.register(selector, SelectionKey.OP_READ);
		} catch (IOException ex) {
			Log.i(TAG, TAG2 + " connected exception");
			Log.i(TAG, TAG2 + " " + ex.getMessage());
			;
		}

		while (!stop) {
			int iselect = 0;

			try {
				iselect = selector.select(TimeOut);
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (iselect == 0) {
				Log.i("CRTServer2Thread", "iselect==0 waiting continue...");
				continue;
			}

			Iterator<SelectionKey> keyIter = selector.selectedKeys().iterator();
			while (keyIter.hasNext()) {
				SelectionKey sk = keyIter.next();

				try {
					if (sk.isValid() && sk.isReadable()) {
						handleRead(sk);
					}
				} catch (Exception e) {
					Log.e(TAG, e.getMessage());
				}

				keyIter.remove();
			}// end while(keyIter.hasNext())
		} // end while(!stop)
	}// end run

	private void handleRead(SelectionKey sk) {
		SocketChannel sc = (SocketChannel) sk.channel();
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		try {
			sc.read(buffer);
		} catch (Exception e) {
			Log.i(TAG, e.getMessage());
		}

		buffer.flip();

		String receivedString = "";
		try {
			receivedString = Charset.forName("UTF-8").newDecoder()
					.decode(buffer).toString();
			Log.i(TAG, "Client receive message from: "
					+ sc.socket().getRemoteSocketAddress() + " msg:"
					+ receivedString);
			if(receivedString.length() != 0 && mCallback != null){
				mCallback.onReceiveMessage(receivedString);
			}
		} catch (CharacterCodingException e) {
			e.printStackTrace();
		}

		sk.interestOps(SelectionKey.OP_READ);
	}
}
