package spacecon.tob.bclient.net;

import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.*;

import spacecon.tob.bclient.Global;

/**
 * Created by HongGukSong on 7/24/2017.
 */
public class CNetManager implements IProtocol {

    int m_Connect_state;


    private String m_IpAdress;
    private int m_port;
    private Socket clientSocket;
    private readSocketThread readThread;
    //private writeSocketThread writeThread;

    DataInputStream m_in;
    DataOutputStream m_out;
    int m_nPacketNo;



    public CNetManager(String IPAdress, int Port) {
        this.m_IpAdress = IPAdress;
        this.m_port = Port;
    }

    public boolean connect() throws InterruptedException, IOException {

        CThread cThread = new CThread();
        Thread thread = new Thread(cThread);
        thread.start();
        thread.join(5000);
        clientSocket = null;
        if (cThread.connect_ok == true) {
            Log.d("Connection OK!", "192.168.1.202:600000");
            clientSocket=cThread.socket;
            m_in = new DataInputStream(clientSocket.getInputStream());
            m_out = new DataOutputStream(clientSocket.getOutputStream());
            Global.m_connect = cThread.connect_ok;

            m_nPacketNo = 0;
            readThread = new readSocketThread();
            readThread.m_readstream=m_in;
            readThread.start();

        } else {
            //connection error part
            Global.m_connect=false;
            Log.d("Connection Error!", "192.168.1.202:600000");
        }

        return cThread.connect_ok;
    }

    public boolean send(CMessage msg) {
        Log.d("Send         ", Integer.toHexString(msg.getHeader()));
        if (!Global.m_connect)
            return false;
        if (m_out == null)
            return false;
        byte[] btArray = msg.toBytes();
        if (btArray == null)
            return false;

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(bout);

        int nSize = btArray.length + 4;
        byte[] buf = null;
        try {
            dout.writeInt(nSize);
            dout.writeInt(getRevertEndian(m_nPacketNo));
            dout.write(btArray);
            buf = bout.toByteArray();
            dout.close();
        } catch (Exception e) {

        }

        if (buf == null)
            return false;
        try {
             m_out.write(buf);
            m_out.flush();
            //m_out.close();
            m_nPacketNo++;
            return true;
        } catch (IOException e) {

        }
        return false;

    }

    int getRevertEndian(int nSrc) {
        int nDst = (nSrc >> 24) & 0xFF;
        nDst |= (nSrc >> 8) & 0x0FF00;
        nDst |= (nSrc << 8) & 0x0FF0000;
        nDst |= (nSrc & 0xFF) << 24;
        return nDst;
    }

    class CThread implements Runnable {
        public boolean connect_ok = false; // connect default;
        public Socket socket = null;

        @Override
        public void run() {
            try {
                socket = new Socket(m_IpAdress, m_port);
                socket.setReceiveBufferSize(4096);
                socket.setSendBufferSize(4096);
                connect_ok = true; //connect true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class readSocketThread extends Thread{
        public DataInputStream m_readstream = null;
        @Override
        public void run() {
            byte[] byRecvData;
                while (true) {
                    try {
                        if (m_readstream.available() <= 0) {
                            Thread.sleep(50);
                            continue;
                        }

                        int nSize = m_readstream.readInt();
                        if (nSize == 0) {
                            Thread.sleep(50);
                            continue;
                        }

                        int nRecvLen = 0;
                        int nDataSize = nSize - 4;
                        int nnSerial = getRevertEndian(m_in.readInt()); //
                        byRecvData = new byte[nDataSize];
                        while (nRecvLen < nDataSize) {
                            nRecvLen += m_readstream.read(byRecvData, nRecvLen, nDataSize - nRecvLen);
                        }
                        CMessage msg = new CMessage(byRecvData);

                        if (Global.m_packetHandler == null) {
                            //message dropping. packet lost
                        } else {
                            Message os_message = new Message();
                            os_message.obj = msg;
                            Global.m_packetHandler.sendMessage(os_message);
                        }
                        //m_PacketManager.addPacket(msg);

                    } catch (Exception e) {
                        Global.m_connect = false;
                    }
                }
            }
        }
}

