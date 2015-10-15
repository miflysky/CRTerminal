package com.tieto.crterminal.model.network;

import java.io.IOException;
import java.nio.channels.SelectionKey;

public interface TCPProtocol{
    /**
     * 接收一个SocketChannel的处理
     * @param key
     * @throws IOException
     */
    void handleAccept(SelectionKey key) throws IOException;
    
    /**
     * 从一个SocketChannel读取信息的处理
     * @param key
     * @throws IOException
     */
    void handleRead(SelectionKey key) throws IOException;
    
    /**
     * 向一个SocketChannel写入信息的处理
     * @param key
     * @throws IOException
     */
    void handleWrite(SelectionKey key) throws IOException;
    
    /**
     * 向所有的Client发送广播消息
     * @param message : 要发送的广播内容
     * @throws IOException
     */
    void handleBroadcast(String message)  throws IOException;
  }
