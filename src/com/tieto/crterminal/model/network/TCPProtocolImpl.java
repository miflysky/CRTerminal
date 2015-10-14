package com.tieto.crterminal.model.network;

import java.io.IOException;
import java.nio.channels.SelectionKey;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.sql.Date;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public final class TCPProtocolImpl implements TCPProtocol {
   
    private int bufferSize;
    private Handler handler = null;
   
    public TCPProtocolImpl(int bufferSize,Handler handler){
        this.bufferSize=bufferSize;
        this.handler = handler;
    }

    @Override
    public void handleAccept(SelectionKey key) throws IOException {
        // TODO Auto-generated method stub
        SocketChannel clientChannel=((ServerSocketChannel)key.channel()).accept();
        clientChannel.configureBlocking(false);
        clientChannel.register(key.selector(), SelectionKey.OP_READ,ByteBuffer.allocate(bufferSize));
        
        // 发送问候消息
        ByteBuffer buffer=(ByteBuffer)key.attachment();
        buffer.clear();
        String sendString="你好,客户端. ";
        buffer=ByteBuffer.wrap(sendString.getBytes("UTF-8"));
        clientChannel.write(buffer);
    }

    @Override
    public void handleRead(SelectionKey key) throws IOException {
        // TODO Auto-generated method stub
        // 获得与客户端通信的信道
        SocketChannel clientChannel=(SocketChannel)key.channel();
       
        // 得到并清空缓冲区
        ByteBuffer buffer=(ByteBuffer)key.attachment();
        buffer.clear();
       
        // 读取信息获得读取的字节数
        long bytesRead=clientChannel.read(buffer);
       
        if(bytesRead==-1){
          // 没有读取到内容的情况
          clientChannel.close();
        }
        else{
          // 将缓冲区准备为数据传出状态
          buffer.flip();
         
          // 将字节转化为为UTF-8的字符串  
          String receivedString=Charset.forName("UTF-8").newDecoder().decode(buffer).toString();
         
//          Message msg = handler.obtainMessage(AndroidAsynServer.CLIENT_COME);
//          msg.obj = receivedString;
//          msg.sendToTarget();
         
          // 控制台打印出来
          Log.i("TCPProtocolImpl","接收到来自"+clientChannel.socket().getRemoteSocketAddress()+"的信息:"+receivedString);
         
          // 准备发送的文本
         
          String sendString="你好,客户端. @已经收到你的信息"+receivedString;
          buffer=ByteBuffer.wrap(sendString.getBytes("UTF-8"));
          clientChannel.write(buffer);
         
          // 设置为下一次读取或是写入做准备
          key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        }
    }

    @Override
    public void handleWrite(SelectionKey key) throws IOException {
        String sendString = "Just a Test";
        // TODO Auto-generated method stub
        SocketChannel clientChannel=(SocketChannel)key.channel();
        ByteBuffer buffer=(ByteBuffer)key.attachment();
        buffer=ByteBuffer.wrap(sendString.getBytes("UTF-8"));
        clientChannel.write(buffer);
        // 设置为下一次读取或是写入做准备
        key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
    }

}

