package com.tieto.crterminal.model.network;

import android.R.bool;
import android.os.Handler;

public class CRTServer2 {
    CRTServer2Thread serverThread = null;
    public final int port = 3333;
    boolean isServerRunning = false;
    
    public void startServer(Handler handler)
    {
        serverThread = new CRTServer2Thread(handler, port);
        serverThread.start();
        isServerRunning = true;
    }
    
    public void stopServer()
    {
        serverThread.stop();
    }
}
