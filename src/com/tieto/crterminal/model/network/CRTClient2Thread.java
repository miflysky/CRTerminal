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


import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class CRTClient2Thread implements Runnable {

    private final String TAG = "CRTClient2";
    private final String TAG2 = "CRTClient2Thread";
    private Selector selector;

    private Handler handler;

    private SocketChannel socketChannel;

    private String hostIpAddress = "";
    private int hostPort = 0;
    private static final int TimeOut=3000;

    public boolean stop = false;

    //    public CRTClient2Thread(Selector selector, SocketChannel socketChannel, String hostIp, int hostListenningPort){
    //        this.selector=selector;
    //        this.handler = null;
    //        new Thread(this).start();
    //      }

    public CRTClient2Thread(Selector selector,Handler handler,  
            SocketChannel socketChannel, String hostIp, int hostListenningPort){
        this.selector=selector;
        this.handler = handler;
        this.socketChannel =socketChannel;
        this.hostIpAddress = hostIp;
        this.hostPort = hostListenningPort;
        new Thread(this).start();
    }
    @Override
    public void run() {
        // TODO Auto-generated method stub
        try {                   
            boolean connected = socketChannel.connect(new InetSocketAddress(hostIpAddress, hostPort));            
            Log.i(TAG, TAG2 + "IP:" + hostIpAddress + ", Port:" + hostPort + "connected:" + connected);  

            while (!socketChannel.finishConnect()){
                // wait for connected;
            }
            
            Log.i(TAG, TAG2 + "IP:" + hostIpAddress + ", Port:" + hostPort + "connected finished");  

            socketChannel.register(selector, SelectionKey.OP_READ);
        } catch (IOException ex) {
            Log.i(TAG, TAG2 + " connected exception");  
            Log.i(TAG, TAG2 + ex.getMessage());;  
        }// end try-catch

        while(!stop)
        {
            // 等待某信道就绪(或超时)
            int iselect = 0;

            try {
                iselect = selector.select(TimeOut);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if(iselect==0){                
                Log.i("CRTServer2Thread", "iselect==0 waiting continue...");
                continue;
            }

            // 取得迭代器.selectedKeys()中包含了每个准备好某一I/O操作的信道的SelectionKey
            Iterator<SelectionKey> keyIter=selector.selectedKeys().iterator();
            while(keyIter.hasNext()){
                SelectionKey sk=keyIter.next();

                try {
                    // 如果该SelectionKey对应的Channel中有可读的数据
                    if (sk.isValid() && sk.isReadable()) {
                        handleRead(sk);
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }

                // 移除处理过的键
                keyIter.remove();
            }// end while(keyIter.hasNext())  
        } // end while(!stop)
    }// end run

    private void handleRead(SelectionKey sk)
    {
        // 使用NIO读取Channel中的数据
        SocketChannel sc = (SocketChannel) sk.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        try {
            sc.read(buffer);
        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
        }

        buffer.flip();

        // 将字节转化为为UTF-16的字符串  
        String receivedString = "";
        try {
            receivedString = Charset.forName("UTF-8").newDecoder().decode(buffer).toString();
        } catch (CharacterCodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //Message msg = handler.obtainMessage(AndroidAsynClient.RECV_DATA);
        //msg.obj = "接收到来自服务器"+sc.socket().getRemoteSocketAddress()+"的信息:"+receivedString;
        //msg.sendToTarget();
        // 控制台打印出来
        Log.i(TAG, "Client receive message from: "+sc.socket().getRemoteSocketAddress()+" msg:"+receivedString);

        // 为下一次读取作准备
        sk.interestOps(SelectionKey.OP_READ);
    }

} // end class
