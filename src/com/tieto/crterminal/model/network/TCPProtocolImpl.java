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
import java.util.ArrayList;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public final class TCPProtocolImpl implements TCPProtocol {
   
    private final String TAG = "CRTServer2";
    private final String TAG2 = "TCPProtocolImpl";
    private int bufferSize;
    private Handler handler = null;
    private ArrayList<SocketChannel> clientChannels = null;
   
    public TCPProtocolImpl(int bufferSize,Handler handler){
        this.bufferSize=bufferSize;
        this.handler = handler;
        this.clientChannels = new ArrayList<SocketChannel>();
    }
    
    protected void finalize()
    {
        for (int i = 0; i < clientChannels.size(); ++ i)
        {
            SocketChannel clientChannel = (SocketChannel) clientChannels.get(i);
            
            try {
                clientChannel.close();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
            
        }
    }

    @Override
    public void handleAccept(SelectionKey key) throws IOException {
        Log.i(TAG, TAG2 + " , a new client connected...");
        // TODO Auto-generated method stub
        SocketChannel clientChannel=((ServerSocketChannel)key.channel()).accept();
        clientChannel.configureBlocking(false);
        clientChannel.register(key.selector(), SelectionKey.OP_READ,ByteBuffer.allocate(bufferSize));
        
        // �����ʺ���Ϣ        
        String message="Hello, Client";
        ByteBuffer writeBuffer=ByteBuffer.wrap(message.getBytes("UTF-8"));
        clientChannel.write(writeBuffer);
        
        // �����б�
        this.clientChannels.add(clientChannel);
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
          Log.i(TAG, TAG2 + " , receive message from" + clientChannel.socket().getRemoteSocketAddress()+", msg:"+receivedString);
                   
          // ׼�����͵��ı�
         
          String sendString="hello client, your msg received: "+receivedString;
          buffer=ByteBuffer.wrap(sendString.getBytes("UTF-8"));
          clientChannel.write(buffer);
         
          // ����Ϊ��һ�ζ�ȡ����д����׼��
          key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        }
    }

    @Override
    public void handleWrite(SelectionKey key) throws IOException 
    {
        String sendString = "Just a Test";
        
        SocketChannel clientChannel=(SocketChannel)key.channel();
        ByteBuffer buffer=(ByteBuffer)key.attachment();
        buffer=ByteBuffer.wrap(sendString.getBytes("UTF-8"));
        clientChannel.write(buffer);
        
        // ����Ϊ��һ�ζ�ȡ����д����׼��
        key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        
        Log.i(TAG, TAG2 + " , sendmessage:" + sendString);
    }
    
    @Override
    public void handleBroadcast(String message)  throws IOException
    {
        Log.i(TAG, TAG2 + " , broadcast message:" + message);
        
        for (int i = 0; i < clientChannels.size(); ++ i)
        {
            SocketChannel clientChannel = (SocketChannel) clientChannels.get(i);
            ByteBuffer writeBuffer=ByteBuffer.wrap(message.getBytes("UTF-8"));
            clientChannel.write(writeBuffer);
        }
    }

}

