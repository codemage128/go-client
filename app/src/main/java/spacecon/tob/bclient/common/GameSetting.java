package spacecon.tob.bclient.common;

import spacecon.tob.bclient.net.CMessage;

/**
 * Created by HongGukSong on 8/21/2017.
 */

public class GameSetting implements Common {
    public byte m_byType;
    public String m_usWhitePlayerName = new String("");
    //public short m_usWhitePlayerLevel = 0;
    public String m_usBlackPlayerName = new String("");
    //public short m_usBlackPlayerLevel = 0;
    public byte m_byBoardSize;
    public byte m_byHandicap;
    public byte m_byDomColor;
    public byte m_byDom;

    public short m_wHaveTime;
    public byte m_byCountDownTime;
    public byte m_byCountDownNum;

    public byte m_byEnableEstimate;
    public byte m_byEnableRetract;

    public byte m_bySurvivalType;

    public int m_nAreanaMoney;

    //bordertype                //by hgs 2017.9.11
    static byte BOARD_6 = 0;
    static byte BOARD_13 = 1;
    static byte BOARD_19 = 19;
    //add by hgs
    static byte BOARD_17 = 3;
    static byte BOARD_11 = 4;
    static byte BOARD_15 = 5;
    static byte BOARD_9 = 6;
    static byte BOARD_SunJang = 7;


    //hgs  2017.9.11
    public static final byte GAME_TYPE_GOMI = 0;   //호선
    public static final byte GAME_TYPE_NOGOMI = 1; //정선
    public static final byte GAME_TYPE_2STONE = 2; //2알접바둑
    public static final byte GAME_TYPE_3STONE = 3; //3알접바둑
    public static final byte GAME_TYPE_4STONE = 4; //...
    public static final byte GAME_TYPE_5STONE = 5;
    public static final byte GAME_TYPE_6STONE = 6;
    public static final byte GAME_TYPE_7STONE = 7;
    public static final byte GAME_TYPE_8STONE = 8;
    public static final byte GAME_TYPE_9STONE = 9; // 9알

    //	uint32 nBetting;

    public GameSetting() {
        m_byType = RT_LEVELINGGAME;
        m_usBlackPlayerName = "";
        //m_usBlackPlayerLevel = 0;
        m_usWhitePlayerName = "";
        //m_usWhitePlayerLevel = 0;

        m_byDomColor = WHITE;
        m_byHandicap = GAME_TYPE_GOMI;
        setDom();
        m_wHaveTime = 1800;
        m_byCountDownTime = 30;
        m_byCountDownNum = 3;
        m_byEnableRetract = 1;
        m_byEnableEstimate = 1;

        m_bySurvivalType = 0;
        m_byBoardSize = BOARD_19;
    }

    private void setDom() {
        if(m_byHandicap == 0){
            switch (m_byBoardSize){
                case 19:
                    m_byDom = 13;
                    break;
                case 17:
                    m_byDom = 11;
                    break;
                //add dom
                default:
                    m_byDom = 0;
                    break;
            }
        }else {
            m_byDom = 0;
        }
    }

    public boolean IsEqual(GameSetting tempSetting) {
        if (m_byType != tempSetting.m_byType)
            return false;
        if (m_usWhitePlayerName.equals(tempSetting.m_usWhitePlayerName) != true)
            return false;
        if (m_usBlackPlayerName.equals(tempSetting.m_usBlackPlayerName) != true)
            return false;
        if (m_byBoardSize != tempSetting.m_byBoardSize)
            return false;
        if (m_byHandicap != tempSetting.m_byHandicap)
            return false;
        if (m_byDomColor != tempSetting.m_byDomColor)
            return false;
        if (m_byDom != tempSetting.m_byDom)
            return false;
        if (m_wHaveTime != tempSetting.m_wHaveTime)
            return false;
        if (m_byCountDownTime != tempSetting.m_byCountDownTime)
            return false;
        if (m_byCountDownNum != tempSetting.m_byCountDownNum)
            return false;
        return true;
    }

    public void readFrom(CMessage msg)
    {
        m_byType = msg.readByte();
        m_usWhitePlayerName = msg.readUCString();
        m_usBlackPlayerName = msg.readUCString();
        m_wHaveTime = 	msg.readShort();
        m_byCountDownTime = msg.readByte();
        m_byCountDownNum = msg.readByte();
        m_byEnableRetract = msg.readByte();
        m_byDom = msg.readByte();
        m_byHandicap = msg.readByte();
        m_byDomColor = msg.readByte();
        m_byEnableEstimate = msg.readByte();


        if (m_byType == 0) {//RT_SURVIVALGAME
            m_bySurvivalType = msg.readByte();
        }
        //m_byDom = msg.readByte();
        m_byBoardSize = msg.readByte();

        //m_nAreanaMoney = msg.readInt();
    }

    public void writeTo(CMessage msg)
    {

        msg.writeByte(m_byType);
        msg.writeUCString(m_usWhitePlayerName);
        msg.writeUCString(m_usBlackPlayerName);

        msg.writeShort(m_wHaveTime);
        msg.writeByte(m_byCountDownTime);
        msg.writeByte(m_byCountDownNum);
        msg.writeByte(m_byEnableRetract);
        msg.writeByte(m_byDom);
        msg.writeByte(m_byHandicap);
        msg.writeByte(m_byDomColor);
        msg.writeByte(m_byEnableEstimate);

        if (m_byType == RT_SURVIVALGAME){//RT_SURVIVALGAME
            msg.writeByte(m_bySurvivalType);
        }
        msg.writeByte(m_byBoardSize); // by hgs
    }
}
