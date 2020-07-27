package spacecon.tob.bclient;

import android.graphics.Point;
import android.os.Handler;

import java.util.Vector;

import spacecon.tob.bclient.common.GameData;
import spacecon.tob.bclient.engine.SSInterface;
import spacecon.tob.bclient.net.CNetManager;
import spacecon.tob.bclient.object.GameKiboRecord;

/**
 * Created by HongGukSong on 6/20/2017.
 */

public class Global {



    public static Handler m_packetHandler = null;
    public static Handler m_selfHandler = null;

    public static CNetManager netManagerSS;
    public static CNetManager netManagerFS;

    public static Vector<String> m_PlayerList;
    public static Vector<String> m_RoomList;

    //Global Gamedata
    public static GameData          m_gameData;
    public static boolean 			m_bLogined;


    public static String m_address = "192.168.1.202";
    public static int m_port = 60000;
    public static boolean m_connect = false;
    public static boolean m_logined = false;


    //room data
    public static byte 				m_bType = -1;
    public static int				m_shrRoomNum = -1;
    public static int               g_KiboNumber = -1;
    public static Vector<String> 	m_GamerList;


    public static byte 				m_nRole = -1;
    public static boolean 			m_bInterrupt = false;
    public static int 				m_bCreateRoom = 0;
    //stone imformation
    public static float m_bordersize_2;
    public static Point m_cp = new Point();
    public static float m_stone_radius;
    public static int m_recent_color = 1; //0 black 1 white

    public static Vector<GameKiboRecord> m_kibolist;
    public static GameKiboRecord m_kiboRecord;
    public static Vector<String> m_kibo;
    public static String m_KiboData;
    public static byte m_nBoardsize_kibo;
    public static boolean m_reEnter = false;


    private SSInterface m_interface = new SSInterface();

    public static void Initialize() {
        m_kibolist = new Vector<GameKiboRecord>();
        m_kiboRecord = new GameKiboRecord();
        netManagerSS = new CNetManager(m_address, m_port);
        m_gameData = GameData.getInstance();
        m_PlayerList = new Vector<String>();
        m_GamerList = new Vector<String>();
        m_RoomList = new Vector<String>();
        m_kibo = new Vector<String>();

    }
}
