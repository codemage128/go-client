package spacecon.tob.bclient.net;

/**
 * Created by HongGukSong on 7/21/2017.
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.*;

public class CMessage {
    boolean isReading;
    byte _Type = 0x11;
    byte[] buf;
    int	m_nHeader;

    DataInputStream m_in;
    ByteArrayOutputStream m_binout;
    DataOutputStream m_out;

    int m_nLength;

    public CMessage() {
        isReading = true;
    }

    public CMessage(int nMsg) {
        isReading = false;

        m_nHeader = nMsg;
        m_binout = new ByteArrayOutputStream();
        m_out = new DataOutputStream(m_binout);
        writeByte(_Type);
        writeInt(m_nHeader);
    }

    public CMessage(byte[] buf) {
        isReading = true;
        try {
            m_in = new DataInputStream(new ByteArrayInputStream(buf));
            _Type = readByte();
            m_nHeader = readInt();
            m_nLength = buf.length;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getHeader() {
        return m_nHeader;
    }

    public boolean writeByte(byte b) {
        if (isReading) return false;
        try {
            m_out.writeByte(b);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    public boolean writeShort(short sh) {
        boolean ret = false;
        byte bh = (byte) ((sh >> 8) & 0xFF);
        byte bl = (byte) (sh & 0xFF);
        ret = writeByte(bl);
        ret = writeByte(bh) && ret;
        return ret;
    }
    public boolean writeInt(int n) {
        if (isReading) return false;
        boolean ret = false;
        short bh = (short) ((n >> 16) & 0xFFFF);
        short bl = (short) (n & 0xFFFF);
        ret = writeShort(bl);
        ret = writeShort(bh) && ret;
        return ret;
    }



    public boolean writeUCString(String str) {
        if (isReading) return false;
        boolean bSuccess;
        char[] chArr = str.toCharArray();
        int len = chArr.length;
        bSuccess = writeInt(len);
        for (int i = 0; i < len; i++) {
            bSuccess = writeShort((short)chArr[i]) && bSuccess;
        }
        return true;
    }

    public boolean writeString(String str) {
        if (isReading) return false;
        boolean bSuccess;
        char[] chArr = str.toCharArray();
        int len = chArr.length;
        bSuccess = writeInt(len);
        for (int i = 0; i < len; i++) {
            bSuccess = writeByte((byte)chArr[i]) && bSuccess;
        }
        return true;
    }

    public byte readByte() {
        if (!isReading) return -1;
        try {
            return m_in.readByte();
        } catch (IOException e) {
            return -1;
        }
    }

    public short readShort() {
        if (!isReading) return -1;
        short sh = (short)(readByte() & 0xFF);
        sh |= (readByte() << 8);
        return sh;
    }

    public int readInt() {
        if (!isReading) return -1;
        int n = (readShort() & 0xFFFF);
        n |= (readShort() << 16);
        return n;
    }



//	public int readUInt64() {  //만들어야 함
//		if (!isReading) return -1;
//		int n = (readShort() & 0xFFFF);
//		n |= (readShort() << 16);
//		return n;
//	}

    public String readString() {
        if (!isReading) return null;

        int len = readInt();
        byte[] btArr = new byte[len];
        for (int i = 0; i < len; i++) {
            btArr[i] = readByte();
        }

        String str = new String(btArr);
        return str;
    }

    public String readUCString() {
        if (!isReading) return null;
        int len = readInt();
        if (len < 0 || m_nLength < len ) {
            return "";
        }
        char[] chArr = new char[len];
        for (int i = 0; i < len; i++) {
            chArr[i] = (char)readShort();
        }

        String str = new String(chArr);
        return str;
    }

    public byte[] toBytes() {
        if (isReading) return null;
        return m_binout.toByteArray();
    }

    public int length() {
        if (isReading)
            return m_nLength;
        else
            return m_binout.size();

    }
}


