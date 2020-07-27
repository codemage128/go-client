package spacecon.tob.bclient.common;

import spacecon.tob.bclient.net.IProtocol;

/**
 * Created by HongGukSong on 8/4/2017.
 */

public interface Common extends IProtocol {

    public int FIRSTSHARDID = 100;
    public int SECONDSHARDID = 110;// 现在  THIS IS THE OUR SERVER SHARDID. // 国松
    public int THIRDSHARDID = 120;
    public int FORTHSHARDID = 130;

    public byte LS_INSERT = 0;
    public byte LS_UPDATE = 1;
    public byte LS_DELETE = 2;

    public static final byte MSG_UNKNOWN = 0;
    public static final byte MSG_NEWS = 1;
    public static final byte MSG_ALL = 1;
    // result
    public static final byte RES_FAIL = 0;
    public static final byte RES_SUCCESS = 1;
    public byte RES_ACCEPT = 2;
    public byte RES_REJECT = 3;
    //room type
    public byte RT_NONE = 0;
    public byte RT_NORMGAME = 1;
    public byte RT_LEVELINGGAME = 2;
    public byte RT_FRIENDSHIPGAME = 3;
    public byte RT_AIGAME = 4;
    public byte RT_SCENARIO = 5;
    public byte RT_SURVIVALGAME = 6;
    public byte RT_EXPLAIN = 7;
    public byte RT_TEACHGAME = 8;
    public byte RT_SHOWKIBO = 9;
    public byte RT_TNHISTORY = 10;
    public byte RT_ETC = 11;

    // enum ROOM_STATE
    public byte RS_NONE = 0;
    public byte RS_BEFORESTART = 1;
    public byte RS_STARTNOW = 2;
    public byte RS_PREPARE = 3;
    public byte RS_INPROGRESS = 4;
    public byte GS_GAME_INIT = 5;
    public byte GS_GAME_MIDDLE = 6;
    public byte GS_GAME_FINAL = 7;
    public byte GS_GAME_DEADSTONE = 8;
    public byte GS_GAME_END = 9;
    public byte GS_GAME_REPLAY = 10;

    public byte GSET_NONE = 0;
    public byte GSET_OK = 1;
    public byte GSET_CANCEL = 2;

    public short M_PASS = 0x2020;
    //
    public byte GR_OBSERVER = 0;
    public byte GR_MASTER = 1;
    public byte GR_GAMER = 2;

    //
    public byte NONE = 0;
    public byte BLACK = 1;
    public byte WHITE = 2;

    //
    public byte COL_NONE = 0;
    public byte COL_BLACK = 1;
    public byte COL_WHITE = 2;
    public byte COL_BOTH = 3;

    //Dialog type
    public int GAME_SETTING_DIALOG = 1;

    public static final byte GMEND_INVALID = 0;
    public static final byte GMEND_DRAW = 1;
    public static final byte GMEND_CALC = 2;
    public static final byte GMEND_THROW = 3;
    public static final byte GMEND_TIMEOVER = 4;
    public static final byte GMEND_INTERRUPT = 5;

    //kifu type
    public static final byte KB_NONE = 0;
    public static final byte KB_PLAYER = 1;
    public static final byte KB_FAMOUSE = 2;
    public static final byte KB_PRO = 3;
    public static final byte KB_TEACH = 4;
    public static final byte KB_ALL = 5;
    public static final byte KB_EXPLAIN = 6;
    public static final byte KB_AI = 7;


}
