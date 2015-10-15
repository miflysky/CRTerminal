package com.tieto.crterminal.model.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;


public class CRTClient2 implements SocketConnectionClient {

    private final String TAG = "CRTClient2";

    // �ŵ�ѡ����
    private Selector selector;

    // �������ͨ�ŵ��ŵ�
    SocketChannel socketChannel;

    // Ҫ���ӵķ�����Ip��ַ
    private String hostIp;

    // Ҫ���ӵ�Զ�̷������ڼ���Ķ˿�
    private int hostListenningPort;
    
    private CRTClient2Thread clientConnectionThread = null;

    /**
     * ���캯��
     * @param HostIp
     * @param HostListenningPort
     * @throws IOException
     */
	public CRTClient2(String HostIp) throws IOException {
		this.hostIp = HostIp;
		this.hostListenningPort = SocketConnectionBase.CONNECTIONPORT;

		initialize();
	}

	@Override
	public void sendMsgToServer(String msg) {

	}

	@Override
	public void openConnection() {

	}

	@Override
	public void closeConnection() {

	}
    /**
     * ��ʼ��
     * @throws IOException
     */
    private void initialize() throws IOException{
        // �򿪼����ŵ�������Ϊ������ģʽ
        //InetSocketAddress address = new InetSocketAddress(hostIp, hostListenningPort);
        socketChannel=SocketChannel.open();
        socketChannel.configureBlocking(false);

        // �򿪲�ע��ѡ�������ŵ�
        selector = Selector.open();
        //socketChannel.register(selector, SelectionKey.OP_READ);

<<<<<<< HEAD
        // ������ȡ�߳�
        clientConnectionThread = new CRTClient2Thread(selector, null, socketChannel, hostIp, hostListenningPort);
    }
=======
		// ������ȡ�߳�
		new CRTClient2Thread(selector, null, socketChannel, hostIp,
				hostListenningPort);
	}
>>>>>>> 5030cf180364781691f595681290a267333329ac

    /**
     * �����ַ�������
     * @param message
     * @throws IOException
     */
    public void sendMsg(String message) throws IOException{
        ByteBuffer writeBuffer=ByteBuffer.wrap(message.getBytes("UTF-8"));
        socketChannel.write(writeBuffer);
    }

<<<<<<< HEAD
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
=======
	public static void main(String[] args) throws IOException {
		CRTClient2 client = new CRTClient2("192.168.0.1");
>>>>>>> 5030cf180364781691f595681290a267333329ac

        client.sendMsg("���!Nio!�������ƿ���,�λش�����Ӫ");
    }
}
