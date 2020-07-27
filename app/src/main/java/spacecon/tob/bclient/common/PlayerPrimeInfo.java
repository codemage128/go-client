package spacecon.tob.bclient.common;

import spacecon.tob.bclient.net.CMessage;

/**
 * Created by HongGukSong on 8/21/2017.
 */

public class PlayerPrimeInfo {
    public int m_nID;
    public String m_usName;
    public short m_wLevel;
    public int 	 m_nMoney;
    public int 	 m_nPoint;
    public int 	 m_nStrike;
    public byte	 m_byRefuseReqGame;
    public int	 m_nWinCount;
    public int	 m_nLoseCount;
    public byte	 m_byState;

    public void loadFrom(CMessage msg) {
        m_usName = msg.readUCString();
        m_byState = msg.readByte();
        m_byRefuseReqGame = msg.readByte();
        m_byRefuseReqGame --;
        m_nMoney = msg.readInt();
        m_wLevel = msg.readShort();
        m_nWinCount = msg.readInt();
        m_nLoseCount = msg.readInt();
        m_nPoint = msg.readInt();
    }
}
