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
            
            // 打开监听信道
            listenerChannel=ServerSocketChannel.open();
            
            // 与本地端口绑定
            listenerChannel.socket().bind(new InetSocketAddress(port));
            
            // 设置为非阻塞模式
            listenerChannel.configureBlocking(false);
            
            // 将选择器绑定到监听信道,只有非阻塞信道才可以注册选择器.并在注册过程中指出该信道可以进行Accept操作
            listenerChannel.register(selector, SelectionKey.OP_ACCEPT);
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        // 创建一个处理协议的实现类,由它来具体操作
        protocol=new TCPProtocolImpl(BufferSize,handler);
    }
   
    public void run()
    {
        while(true){
            // 等待某信道就绪(或超时)
            int iselect = 0;
            try {
                iselect = selector.select(TimeOut);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if(iselect==0){
                System.out.print("独自等待.");
                Log.i("CRTServer2Thread", "继续等待...");
                continue;
            }
            
            // 取得迭代器.selectedKeys()中包含了每个准备好某一I/O操作的信道的SelectionKey
            Iterator<SelectionKey> keyIter=selector.selectedKeys().iterator();
            while(keyIter.hasNext()){
                SelectionKey key=keyIter.next();
                try{
                    if(key.isAcceptable()){
                        // 有客户端连接请求时
                        protocol.handleAccept(key);
//                        Message msg = handler.obtainMessage(AndroidAsynServer.CLIENT_COME);
//                        msg.obj = "Accept";
//                        msg.sendToTarget();
                    }
             
                    if(key.isReadable()){
                        // 从客户端读取数据
                        protocol.handleRead(key);
//                        Message msg = handler.obtainMessage(AndroidAsynServer.RECV_DATA);
//                        msg.obj = "RECV Data";
//                        msg.sendToTarget();
                    }
             
                    if(key.isValid() && key.isWritable()){
                        // 客户端可写时
                        protocol.handleWrite(key);
//                        Message msg = handler.obtainMessage(AndroidAsynServer.SEND_DATA);
//                        msg.obj = "Send Data";
//                        msg.sendToTarget();
                    }
                }
                catch(IOException ex){
                    // 出现IO异常（如客户端断开连接）时移除处理过的键
                    keyIter.remove();
                    continue;
                }
                // 移除处理过的键
                keyIter.remove();

            }
        }
    }
}
