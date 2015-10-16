package com.tieto.crterminal.model.network;

import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;


public class CRTClient2 implements SocketConnectionClient {

    private final String TAG = "CRTClient2";
    private Selector selector;    
    SocketChannel socketChannel;
    private String hostIp;
    private int hostListenningPort;
    
    private CRTClient2Thread clientConnectionThread = null;
	private ClientConnectionCallback mCallback;

    public interface ClientConnectionCallback{

		void onReceiveMessage(String receivedString);

		void onConnect();
    	
    }
    /**
     * @param HostIp
     * @param HostListenningPort
     * @throws IOException
     */
	public CRTClient2(String HostIp, ClientConnectionCallback callback ) throws IOException {
		this.hostIp = HostIp;
		this.hostListenningPort = SocketConnectionBase.CONNECTIONPORT;
		mCallback = callback;
	}
	
	
    /**
     * @throws IOException
     */
    private void initialize() throws IOException{
        socketChannel=SocketChannel.open();
        socketChannel.configureBlocking(false);        
        selector = Selector.open();
        clientConnectionThread = new CRTClient2Thread(selector, mCallback, socketChannel, hostIp, hostListenningPort);
   	}


    /**
     * @param message
     * @throws IOException
     */
    public void sendMsg(String message) throws IOException{   
    	final String sendMessage = message;
//    	ByteBuffer writeBuffer=ByteBuffer.wrap(sendMessage.getBytes("UTF-8"));
//		socketChannel.write(writeBuffer);
        new Thread(){
        	public void run() {
        		        
                try {
                	ByteBuffer writeBuffer=ByteBuffer.wrap(sendMessage.getBytes("UTF-8"));
					socketChannel.write(writeBuffer);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
        }.start();
    }


    private void stopConnection()
    {    
        clientConnectionThread.stop = true; 
        try {
            socketChannel.close();
            selector.close();
        } catch (IOException e) {
            Log.e(TAG, "Exception:" + e.getMessage());
        }         
    }



    @Override
    public void openConnection() {
        try {
            initialize();
        } catch (Exception e) {
            Log.e(TAG, "Exception:" + e.getMessage());
        }        
    }


    @Override
    public void closeConnection() {
        stopConnection();        
    }


    @Override
    public void sendMsgToServer(String msg) {         
        try {
            if (msg != null && msg.length() > 0)
            {
                sendMsg(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "sendMsgToServer exception: " + e.toString());
        }        
    }     
    
}
