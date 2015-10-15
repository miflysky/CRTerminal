package com.tieto.crterminal.model.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;


import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class CRTClient2Thread implements Runnable {

    private final String TAG = "CRTClient2Thread";
    private Selector selector;
   
    private Handler handler;
    
    private SocketChannel socketChannel;
    
    private String hostIpAddress = "";
    private int hostPort = 0;
     
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
            Log.i(TAG, ""+connected);  
            
            while (!socketChannel.finishConnect()){
                // wait for connected;
            }
            
            socketChannel.register(selector, SelectionKey.OP_READ);
            
            while (selector.select() > 0) {
                // 遍历每个有可用IO操作Channel对应的SelectionKey
                for (SelectionKey sk : selector.selectedKeys()) {
                    // 如果该SelectionKey对应的Channel中有可读的数据
                    if (sk.isValid() && sk.isReadable()) {
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
                        String receivedString=Charset.forName("UTF-8").newDecoder().decode(buffer).toString();
                        //Message msg = handler.obtainMessage(AndroidAsynClient.RECV_DATA);
                        //msg.obj = "接收到来自服务器"+sc.socket().getRemoteSocketAddress()+"的信息:"+receivedString;
                        //msg.sendToTarget();
                        // 控制台打印出来
                        Log.i(TAG, "接收到来自服务器"+sc.socket().getRemoteSocketAddress()+"的信息:"+receivedString);
                   
                        // 为下一次读取作准备
                        sk.interestOps(SelectionKey.OP_READ);
                    }
                  // 删除正在处理的SelectionKey
                  selector.selectedKeys().remove(sk);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }  
    }
}
