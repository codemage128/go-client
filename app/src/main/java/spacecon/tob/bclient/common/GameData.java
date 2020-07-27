package spacecon.tob.bclient.common;

import java.util.Vector;

import spacecon.tob.bclient.net.CLoginCookie;

/**
 * Created by HongGukSong on 8/21/2017.
 */

public class GameData {

    public Vector<PlayerPrimeInfo>      m_vecPlayerList;
    public Vector<RoomData> 			m_vecGameList;
    public Vector<String> 				m_vecFriendList;
    public Vector<String> 				m_vecBadList;


    public static String        m_userName = ""; // user name
    public static String        m_userPassword = ""; // user password
    public static int           m_shardId;  // this is the shardID
    public static int           m_dID;
    public static int           m_Connect_state;

    public String						m_strFSAddress;
    public int							m_nFSPort;

    public CLoginCookie         m_Cookie;

    private static GameData 	_instance;

    public static PlayerPrimeInfo 	m_OwnInfo = null;


    public String						m_usOppName;

    public GameData() {
        m_OwnInfo = new PlayerPrimeInfo();
        m_vecFriendList = new Vector<String>();
        m_vecPlayerList = new Vector<PlayerPrimeInfo>();
        m_vecGameList = new Vector<RoomData>();
    }

    public static GameData getInstance() {
        if (_instance == null)
            _instance = new GameData();
        return _instance;
    }
    public static void setOwnInfo( PlayerPrimeInfo pInfo) {
        if( m_OwnInfo != null)
            m_OwnInfo = null;

        m_OwnInfo = new PlayerPrimeInfo();
        m_OwnInfo.m_nID = pInfo.m_nID;
        m_OwnInfo.m_byRefuseReqGame = pInfo.m_byRefuseReqGame;
        m_OwnInfo.m_byState = pInfo.m_byState;
        m_OwnInfo.m_nLoseCount = pInfo.m_nLoseCount;
        m_OwnInfo.m_nMoney = pInfo.m_nMoney;
        m_OwnInfo.m_nPoint = pInfo.m_nPoint;
        m_OwnInfo.m_nStrike = pInfo.m_nStrike;
        m_OwnInfo.m_nWinCount = pInfo.m_nWinCount;
        m_OwnInfo.m_usName = pInfo.m_usName;
        m_OwnInfo.m_wLevel = pInfo.m_wLevel;            //by hgs
    }

    public void addPlayer(PlayerPrimeInfo player) {
        m_vecPlayerList.addElement(player);
    }

    public void createPlayerList() {
        m_vecPlayerList.removeAllElements();
    }

    public void createRoomList() {
        m_vecGameList.removeAllElements();
    }

    public void addRoom(RoomData room) {
        m_vecGameList.addElement(room);
    }

    public int getRoomCount() {
        if (m_vecGameList == null) return -1;
        return m_vecGameList.size();
    }

    public void updateRoom(RoomData room) {
        int index = getRoomIndex(room.m_wRoomNo);
        if (index == -1) return;
        m_vecGameList.setElementAt(room, index);
    }

    public int getRoomIndex(short wRoomID) {
        int nCount = getRoomCount();
        for (int i = 0; i < nCount; i++) {
            RoomData room = getRoom(i);
            if (wRoomID == room.m_wRoomNo)
                return i;
        }
        return -1;
    }
    public RoomData getRoom(int nIndex) {
        return (RoomData) m_vecGameList.elementAt(nIndex);
    }

    public void removeRoom(short wRoomID) {
        int index = getRoomIndex(wRoomID);
        if (index != -1)
            m_vecGameList.removeElementAt(index);
    }

    public int getPlayerCount() {
        if (m_vecPlayerList == null) return -1;
        return m_vecPlayerList.size();
    }

    public void updatePlayer(PlayerPrimeInfo player) {
        int index = getPlayerIndex(player.m_usName);
        if (index == -1) return;
        m_vecPlayerList.setElementAt(player, index);
    }
    public int getPlayerIndex(String usName) {
        int nCount = getPlayerCount();
        for (int i = 0; i < nCount; i++) {
            PlayerPrimeInfo player = getPlayer(i);
            if (usName.equals(player.m_usName))
                return i;
        }
        return -1;
    }
    public PlayerPrimeInfo getPlayer(int nIndex) {
        return (PlayerPrimeInfo) m_vecPlayerList.elementAt(nIndex);
    }
    public void removePlayer(String usName) {
        int index = getPlayerIndex(usName);
        if (index == -1) return;
        m_vecPlayerList.removeElementAt(index);
    }
}
