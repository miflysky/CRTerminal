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
        
        // �����ʺ���Ϣ
        ByteBuffer buffer=(ByteBuffer)key.attachment();
        buffer.clear();
        String sendString="���,�ͻ���. ";
        buffer=ByteBuffer.wrap(sendString.getBytes("UTF-8"));
        clientChannel.write(buffer);
    }

    @Override
    public void handleRead(SelectionKey key) throws IOException {
        // TODO Auto-generated method stub
        // �����ͻ���ͨ�ŵ��ŵ�
        SocketChannel clientChannel=(SocketChannel)key.channel();
       
        // �õ�����ջ�����
        ByteBuffer buffer=(ByteBuffer)key.attachment();
        buffer.clear();
       
        // ��ȡ��Ϣ��ö�ȡ���ֽ���
        long bytesRead=clientChannel.read(buffer);
       
        if(bytesRead==-1){
          // û�ж�ȡ�����ݵ����
          clientChannel.close();
        }
        else{
          // ��������׼��Ϊ���ݴ���״̬
          buffer.flip();
         
          // ���ֽ�ת��ΪΪUTF-8���ַ���  
          String receivedString=Charset.forName("UTF-8").newDecoder().decode(buffer).toString();
         
//          Message msg = handler.obtainMessage(AndroidAsynServer.CLIENT_COME);
//          msg.obj = receivedString;
//          msg.sendToTarget();
         
          // ����̨��ӡ����
          Log.i("TCPProtocolImpl","���յ�����"+clientChannel.socket().getRemoteSocketAddress()+"����Ϣ:"+receivedString);
         
          // ׼�����͵��ı�
         
          String sendString="���,�ͻ���. @�Ѿ��յ������Ϣ"+receivedString;
          buffer=ByteBuffer.wrap(sendString.getBytes("UTF-8"));
          clientChannel.write(buffer);
         
          // ����Ϊ��һ�ζ�ȡ����д����׼��
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
        // ����Ϊ��һ�ζ�ȡ����д����׼��
        key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
    }

}

