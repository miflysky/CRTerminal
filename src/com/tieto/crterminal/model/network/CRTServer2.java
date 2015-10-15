package com.tieto.crterminal.model.network;

import android.R.bool;
import android.os.Handler;

public class CRTServer2 {
    private CRTServer2Thread serverThread = null;
    private final int port = 3333;
    private boolean isServerRunning = false;
    
    private Handler handler = null;
    private TCPProtocol protocol = null;
    private static final int BufferSize=1024;
    private static final int TimeOut=3000;
    
    public void startServer(Handler handler)
    {
        this.handler = handler;
        this.protocol = new TCPProtocolImpl(BufferSize,handler);
        this.serverThread = new CRTServer2Thread(port, protocol);
        this.serverThread.start();
        this.isServerRunning = true;
    }
    
    public void stopServer()
    {
        serverThread.stop = true;
    }
    
    public void broadcastMessage(String message)
    {
        try {
            this.protocol.handleBroadcast(message);
        } catch (Exception e) {
            // TODO: handle exception
        }
        
    }
    
    public void sendData(byte[] data)
    {
        
    }
}
