package spacecon.tob.bclient.object;

import spacecon.tob.bclient.net.CMessage;

/**
 * Created by HongGukSong on 10/26/2017.
 */
public class GameKiboRecord
{
    public GameKiboRecord(){
    }
    //    public GameKiboRecord(uint32 nKiboID, ucstring usKiboTime, ucstring usWhitePlayerName,
//                uint16 wWhitePlayerLevel, ucstring usBlackPlayerName,
//                uint16 wBlackPlayerLevel, uint8 byGameType,
//                ucstring usHandicap, ucstring usDom, ucstring usResult, uint16 wKiboCount,
//                ucstring usDescription):m_nKiboID(nKiboID), m_usKiboTime(usKiboTime), m_usWhitePlayerName(usWhitePlayerName), m_wWhitePlayerLevel(wWhitePlayerLevel), m_usBlackPlayerName(usBlackPlayerName), m_wBlackPlayerLevel(wBlackPlayerLevel), m_byGameType(byGameType), m_usHandicap(usHandicap), m_usDom(usDom), m_usResult(usResult), m_wKiboCount(wKiboCount), m_usDescription(usDescription) { };
    public int m_nKiboID;
    public String m_usKiboTime;
    public String m_usWhitePlayerName;
    public short m_wWhitePlayerLevel;
    public String m_usBlackPlayerName;
    public short m_wBlackPlayerLevel;
    public byte m_byGameType;
    //public byte m_byHandicap;
    //public byte m_byDom;
    public String m_usHeader;
    //public short m_wKiboCount;
    //public String m_usDescription;
    public byte m_byBoardersize;

    public void read(CMessage msg)
    {
        // verify that we initialized the cookie before writing it
        m_nKiboID = msg.readInt();
        m_usKiboTime = msg.readUCString();
        m_byGameType = msg.readByte();
        m_usWhitePlayerName = msg.readUCString();
        m_wWhitePlayerLevel = msg.readShort();
        m_usBlackPlayerName = msg.readUCString();
        m_wBlackPlayerLevel = msg.readShort();
        m_usHeader = msg.readUCString();
        m_byBoardersize = msg.readByte();

        /*m_byHandicap = msg.readByte();
        m_byDom = msg.readByte();
        m_usResult = msg.readString();
        m_wKiboCount = msg.readShort();
        m_usDescription = msg.readUCString();
        */
    }
    public void write(CMessage msg)
    {
        // verify that we initialized the cookie before writing it
        msg.writeInt(m_nKiboID);
        msg.writeUCString(m_usKiboTime);
        msg.writeByte(m_byGameType);
        msg.writeUCString(m_usWhitePlayerName);
        msg.writeShort(m_wWhitePlayerLevel);
        msg.writeUCString(m_usBlackPlayerName);
        msg.writeShort(m_wBlackPlayerLevel);
        msg.writeUCString(m_usHeader);
        msg.writeByte(m_byBoardersize);

        /*
        msg.writeByte(m_byHandicap);
        msg.writeByte(m_byDom);
        msg.writeUCString(m_usResult);
        msg.writeShort(m_wKiboCount);
        msg.writeUCString(m_usDescription);
        */
    }

};
