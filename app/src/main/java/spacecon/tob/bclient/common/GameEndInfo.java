package spacecon.tob.bclient.common;

import spacecon.tob.bclient.net.CMessage;

/**
 * Created by HongGukSong on 8/21/2017.
 */

public class GameEndInfo {

    public short m_wBlackCrosses;
    public short m_wTakenWhiteStones;
    public short m_wWhiteCrosses;
    public short m_wTakenBlackStones;
    public byte  m_byDom;
    public byte  m_byEndRes;
    public byte  m_byWinnerCol;

    public GameEndInfo() {
        m_wBlackCrosses = 0;
        m_wTakenWhiteStones = 0;
        m_wWhiteCrosses = 0;
        m_wTakenBlackStones = 0;
        m_byDom = 0;
        m_byEndRes = 0;
        m_byWinnerCol = 0;
    }

    public void readFrom(CMessage msg) {
        m_wBlackCrosses = msg.readShort();
        m_wTakenWhiteStones = msg.readShort();
        m_wWhiteCrosses = msg.readShort();
        m_wTakenBlackStones = msg.readShort();
        m_byDom = msg.readByte();
        m_byEndRes = msg.readByte();
        m_byWinnerCol = msg.readByte();
    }

    public void writeTo(CMessage msg) {
        msg.writeShort( m_wBlackCrosses );
        msg.writeShort( m_wTakenWhiteStones );
        msg.writeShort( m_wWhiteCrosses );
        msg.writeShort( m_wTakenBlackStones );
        msg.writeByte( m_byDom );
        msg.writeByte( m_byEndRes );
        msg.writeByte( m_byWinnerCol );
    }

}
