package com.tieto.crterminal.model.network;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;


import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class CRTServer2Thread extends Thread {
   
   
    private Handler handler = null;
    private int port = 0;
    private Selector selector = null;
    private ServerSocketChannel listenerChannel = null;
   
    private TCPProtocol protocol = null;
    private static final int BufferSize=1024;
    private static final int TimeOut=3000;
   
    public CRTServer2Thread(Handler handler,int port)
    {
        this.handler = handler;
        this.port = port;
        try {
            selector = Selector.open();
            
            // �򿪼����ŵ�
            listenerChannel=ServerSocketChannel.open();
            
            // �뱾�ض˿ڰ�
            listenerChannel.socket().bind(new InetSocketAddress(port));
            
            // ����Ϊ������ģʽ
            listenerChannel.configureBlocking(false);
            
            // ��ѡ�����󶨵������ŵ�,ֻ�з������ŵ��ſ���ע��ѡ����.����ע�������ָ�����ŵ����Խ���Accept����
            listenerChannel.register(selector, SelectionKey.OP_ACCEPT);
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        // ����һ������Э���ʵ����,�������������
        protocol=new TCPProtocolImpl(BufferSize,handler);
    }
   
    public void run()
    {
        while(true){
            // �ȴ�ĳ�ŵ�����(��ʱ)
            int iselect = 0;
            try {
                iselect = selector.select(TimeOut);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if(iselect==0){
                System.out.print("���Եȴ�.");
                Log.i("CRTServer2Thread", "�����ȴ�...");
                continue;
            }
            
            // ȡ�õ�����.selectedKeys()�а�����ÿ��׼����ĳһI/O�������ŵ���SelectionKey
            Iterator<SelectionKey> keyIter=selector.selectedKeys().iterator();
            while(keyIter.hasNext()){
                SelectionKey key=keyIter.next();
                try{
                    if(key.isAcceptable()){
                        // �пͻ�����������ʱ
                        protocol.handleAccept(key);
//                        Message msg = handler.obtainMessage(AndroidAsynServer.CLIENT_COME);
//                        msg.obj = "Accept";
//                        msg.sendToTarget();
                    }
             
                    if(key.isReadable()){
                        // �ӿͻ��˶�ȡ����
                        protocol.handleRead(key);
//                        Message msg = handler.obtainMessage(AndroidAsynServer.RECV_DATA);
//                        msg.obj = "RECV Data";
//                        msg.sendToTarget();
                    }
             
                    if(key.isValid() && key.isWritable()){
                        // �ͻ��˿�дʱ
                        protocol.handleWrite(key);
//                        Message msg = handler.obtainMessage(AndroidAsynServer.SEND_DATA);
//                        msg.obj = "Send Data";
//                        msg.sendToTarget();
                    }
                }
                catch(IOException ex){
                    // ����IO�쳣����ͻ��˶Ͽ����ӣ�ʱ�Ƴ�������ļ�
                    keyIter.remove();
                    continue;
                }
                // �Ƴ�������ļ�
                keyIter.remove();

            }
        }
    }
}
