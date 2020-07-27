package spacecon.tob.bclient.common;

import spacecon.tob.bclient.net.CMessage;

/**
 * Created by HongGukSong on 8/21/2017.
 */

public class RoomData {
    public short		m_wRoomNo;
    public byte		    m_byRoomType;
    public String		m_usMasterName;
    public short		m_wMasterLevel;
    public short		m_wCountOfObservers;
    public String		m_usWhitePlayerName;
    public short		m_wWhitePlayerLevel;
    public String		m_usBlackPlayerName;
    public short		m_wBlackPlayerLevel;
    public short		m_wLinkRoomID;
    public byte	        m_byState;
    public String		m_usDescription;

    public void readFrom(CMessage msg) {
        m_wRoomNo = (short) msg.readInt();
        m_byRoomType = msg.readByte();
        m_usMasterName = msg.readUCString();
        m_wMasterLevel =  msg.readShort();
        m_wCountOfObservers = (short) msg.readShort();
        m_usWhitePlayerName = msg.readUCString();
        m_wWhitePlayerLevel = (short) msg.readShort();
        m_usBlackPlayerName = msg.readUCString();
        m_wBlackPlayerLevel = (short) msg.readShort();
        m_wLinkRoomID = (short) msg.readInt();
        m_byState = msg.readByte();
        m_usDescription = msg.readUCString();
    }

    public void writeTo(CMessage msg) {
        msg.writeShort(m_wRoomNo);
        msg.writeByte(m_byRoomType);
        msg.writeUCString(m_usMasterName);
        msg.writeShort(m_wMasterLevel);
        msg.writeShort(m_wCountOfObservers);
        msg.writeUCString(m_usWhitePlayerName);
        msg.writeShort(m_wWhitePlayerLevel);
        msg.writeUCString(m_usBlackPlayerName);
        msg.writeShort(m_wBlackPlayerLevel);
        msg.writeShort(m_wLinkRoomID);
        msg.writeByte(m_byState);
        msg.writeUCString(m_usDescription);
    }

}
