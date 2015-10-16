package com.tieto.crterminal.model.network;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;

import android.util.Log;

import com.tieto.crterminal.model.network.CRTServer2.ServerConnectionCallBack;

public final class TCPProtocolImpl implements TCPProtocol {

	private final String TAG = "CRTServer2";
	private final String TAG2 = "TCPProtocolImpl";
	private int mBufferSize;
	private ArrayList<SocketChannel> mClientChannels = null;
	private ServerConnectionCallBack mCallback;

	public TCPProtocolImpl(int bufferSize, ServerConnectionCallBack callback) {
		mBufferSize = bufferSize;
		mCallback = callback;
		mClientChannels = new ArrayList<SocketChannel>();
	}

	protected void finalize() {
		for (int i = 0; i < mClientChannels.size(); ++i) {
			SocketChannel clientChannel = (SocketChannel) mClientChannels
					.get(i);

			try {
				clientChannel.close();
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
			}

		}
	}

	@Override
	public void handleAccept(SelectionKey key) throws IOException {
		Log.i(TAG, TAG2 + " , a new client connected...");
		SocketChannel clientChannel = ((ServerSocketChannel) key.channel())
				.accept();
		clientChannel.configureBlocking(false);
		clientChannel.register(key.selector(), SelectionKey.OP_READ,
				ByteBuffer.allocate(mBufferSize));
		mClientChannels.add(clientChannel);
	}

	@Override
	public void handleRead(SelectionKey key) throws IOException {
		SocketChannel clientChannel = (SocketChannel) key.channel();

		ByteBuffer buffer = (ByteBuffer) key.attachment();
		buffer.clear();

		long bytesRead = clientChannel.read(buffer);

		if (bytesRead == -1) {
			clientChannel.close();
		} else {
			buffer.flip();
			String receivedString = Charset.forName("UTF-8").newDecoder()
					.decode(buffer).toString();
			Log.i(TAG, TAG2 + " , receive message from"
					+ clientChannel.socket().getRemoteSocketAddress()
					+ ", msg:" + receivedString);
			mCallback.onReceiveMessage(receivedString);
		}
	}

	@Override
	public void handleWrite(SelectionKey key) throws IOException {
		String sendString = "Just a Test";

		SocketChannel clientChannel = (SocketChannel) key.channel();
		ByteBuffer buffer = (ByteBuffer) key.attachment();
		buffer = ByteBuffer.wrap(sendString.getBytes("UTF-8"));
		clientChannel.write(buffer);

		key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
		Log.i(TAG, TAG2 + " , sendmessage:" + sendString);
	}

	@Override
	public void handleBroadcast(String originalMessage) throws IOException {
		final String message = originalMessage;
		new Thread(){
			public void run()
			{
				Log.i(TAG, TAG2 + " , broadcast message:" + message);

				for (int i = 0; i < mClientChannels.size(); ++i) {
					SocketChannel clientChannel = (SocketChannel) mClientChannels
							.get(i);
					ByteBuffer writeBuffer;
					try {
						writeBuffer = ByteBuffer.wrap(message.getBytes("UTF-8"));
						clientChannel.write(writeBuffer);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		}.start();
	}

}
