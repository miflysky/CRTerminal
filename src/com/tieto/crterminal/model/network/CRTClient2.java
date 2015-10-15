package com.tieto.crterminal.model.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;


public class CRTClient2{
    private final String TAG = "CRTClient2";

    // 信道选择器
    private Selector selector;

    // 与服务器通信的信道
    SocketChannel socketChannel;

    // 要连接的服务器Ip地址
    private String hostIp;

    // 要连接的远程服务器在监听的端口
    private int hostListenningPort;
    
    private CRTClient2Thread clientConnectionThread = null;

    /**
     * 构造函数
     * @param HostIp
     * @param HostListenningPort
     * @throws IOException
     */
    public CRTClient2(String HostIp,int HostListenningPort)throws IOException{
        this.hostIp=HostIp;
        this.hostListenningPort=HostListenningPort;   

        initialize();
    }

    /**
     * 初始化
     * @throws IOException
     */
    private void initialize() throws IOException{
        // 打开监听信道并设置为非阻塞模式
        //InetSocketAddress address = new InetSocketAddress(hostIp, hostListenningPort);
        socketChannel=SocketChannel.open();
        socketChannel.configureBlocking(false);

        // 打开并注册选择器到信道
        selector = Selector.open();
        //socketChannel.register(selector, SelectionKey.OP_READ);

        // 启动读取线程
        clientConnectionThread = new CRTClient2Thread(selector, null, socketChannel, hostIp, hostListenningPort);
    }

    /**
     * 发送字符串到服务器
     * @param message
     * @throws IOException
     */
    public void sendMsg(String message) throws IOException{
        ByteBuffer writeBuffer=ByteBuffer.wrap(message.getBytes("UTF-8"));
        socketChannel.write(writeBuffer);
    }

    public void stopConnection()
    {
        clientConnectionThread.stop = true;
        
        
        try {
            socketChannel.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) throws IOException{
        CRTClient2 client=new CRTClient2("192.168.0.1",1978);

        client.sendMsg("你好!Nio!醉里挑灯看剑,梦回吹角连营");
    }
}
