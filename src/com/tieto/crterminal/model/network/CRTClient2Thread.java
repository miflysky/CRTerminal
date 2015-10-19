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
			// TODO: do re-connect
		}

		while (!stop) {
			int iselect = 0;

			try {
				iselect = selector.select(TimeOut);
			} catch (IOException e) {
				e.printStackTrace();
				// TODO: do what?
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
					e.printStackTrace();
					// TODO: do what?
				}

				keyIter.remove();
			}// end while(keyIter.hasNext())
		} // end while(!stop)
	}// end run

	private void handleRead(SelectionKey sk) {
		SocketChannel sc = (SocketChannel) sk.channel();
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		try {
			int size = sc.read(buffer);
			if( size < 0){
				sc.close();
				sk.cancel();
			}
		} catch (Exception e) {
			Log.i(TAG, e.getMessage());
			// TODO: do what
		}

		buffer.flip();

		String receivedString = "";
		try {
			receivedString = Charset.forName("UTF-8").newDecoder()
					.decode(buffer).toString();
			Log.i(TAG, "====> Client receive message from: "
					+ sc.socket().getRemoteSocketAddress() + " ,msg:"
					+ receivedString);
			
			if(receivedString.length() != 0 && mCallback != null){
				handleReceivedMessageFromServer(receivedString);				
			}	
			
		} catch (CharacterCodingException e) {
			e.printStackTrace();
		}
		
	}
	
	private void handleReceivedMessageFromServer(String receivedMessage)
	{		
		String tocken = "}{";
		String subMessage = "";
		int pos = receivedMessage.indexOf(tocken);
		int start = 0;
		while( pos != -1)
		{
			// there are multiple messages in the received content			
			subMessage = receivedMessage.substring(start, pos +  1);				
			mCallback.onReceiveMessage(subMessage);
			
			start = pos + 1;
			pos = receivedMessage.indexOf(tocken, start);
			
			Log.i(TAG, "====>====> handling message: " + subMessage);
		}
		
		// the last message (or the first message if there is only one)
		subMessage = receivedMessage.substring(start);
		mCallback.onReceiveMessage(subMessage);
		Log.i(TAG, "====>====> handling message: " + subMessage);
	}
}
