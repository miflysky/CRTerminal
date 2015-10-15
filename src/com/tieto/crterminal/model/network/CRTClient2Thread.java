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
                // ����ÿ���п���IO����Channel��Ӧ��SelectionKey
                for (SelectionKey sk : selector.selectedKeys()) {
                    // �����SelectionKey��Ӧ��Channel���пɶ�������
                    if (sk.isValid() && sk.isReadable()) {
                        // ʹ��NIO��ȡChannel�е�����
                        SocketChannel sc = (SocketChannel) sk.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        try {
                            sc.read(buffer);
                        } catch (Exception e) {
                            Log.i(TAG, e.getMessage());
                        }
                        
                        buffer.flip();
                   
                        // ���ֽ�ת��ΪΪUTF-16���ַ���  
                        String receivedString=Charset.forName("UTF-8").newDecoder().decode(buffer).toString();
                        //Message msg = handler.obtainMessage(AndroidAsynClient.RECV_DATA);
                        //msg.obj = "���յ����Է�����"+sc.socket().getRemoteSocketAddress()+"����Ϣ:"+receivedString;
                        //msg.sendToTarget();
                        // ����̨��ӡ����
                        Log.i(TAG, "���յ����Է�����"+sc.socket().getRemoteSocketAddress()+"����Ϣ:"+receivedString);
                   
                        // Ϊ��һ�ζ�ȡ��׼��
                        sk.interestOps(SelectionKey.OP_READ);
                    }
                  // ɾ�����ڴ����SelectionKey
                  selector.selectedKeys().remove(sk);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }  
    }
}
