package com.tieto.crterminal.model.network;

import java.io.IOException;
import java.nio.channels.SelectionKey;

public interface TCPProtocol{
    /**
     * ����һ��SocketChannel�Ĵ���
     * @param key
     * @throws IOException
     */
    void handleAccept(SelectionKey key) throws IOException;
    
    /**
     * ��һ��SocketChannel��ȡ��Ϣ�Ĵ���
     * @param key
     * @throws IOException
     */
    void handleRead(SelectionKey key) throws IOException;
    
    /**
     * ��һ��SocketChannelд����Ϣ�Ĵ���
     * @param key
     * @throws IOException
     */
    void handleWrite(SelectionKey key) throws IOException;
    
    /**
     * �����е�Client���͹㲥��Ϣ
     * @param message : Ҫ���͵Ĺ㲥����
     * @throws IOException
     */
    void handleBroadcast(String message)  throws IOException;
  }
