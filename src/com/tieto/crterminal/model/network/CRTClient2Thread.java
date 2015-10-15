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
            // �ȴ�ĳ�ŵ�����(��ʱ)
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

            // ȡ�õ�����.selectedKeys()�а�����ÿ��׼����ĳһI/O�������ŵ���SelectionKey
            Iterator<SelectionKey> keyIter=selector.selectedKeys().iterator();
            while(keyIter.hasNext()){
                SelectionKey sk=keyIter.next();

                try {
                    // �����SelectionKey��Ӧ��Channel���пɶ�������
                    if (sk.isValid() && sk.isReadable()) {
                        handleRead(sk);
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }

                // �Ƴ�������ļ�
                keyIter.remove();
            }// end while(keyIter.hasNext())  
        } // end while(!stop)
    }// end run

    private void handleRead(SelectionKey sk)
    {
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
        String receivedString = "";
        try {
            receivedString = Charset.forName("UTF-8").newDecoder().decode(buffer).toString();
        } catch (CharacterCodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //Message msg = handler.obtainMessage(AndroidAsynClient.RECV_DATA);
        //msg.obj = "���յ����Է�����"+sc.socket().getRemoteSocketAddress()+"����Ϣ:"+receivedString;
        //msg.sendToTarget();
        // ����̨��ӡ����
        Log.i(TAG, "Client receive message from: "+sc.socket().getRemoteSocketAddress()+" msg:"+receivedString);

        // Ϊ��һ�ζ�ȡ��׼��
        sk.interestOps(SelectionKey.OP_READ);
    }

} // end class
