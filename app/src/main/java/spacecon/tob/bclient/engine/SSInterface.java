package spacecon.tob.bclient.engine;

import android.os.Message;

import java.util.Random;

import spacecon.tob.bclient.Global;
import spacecon.tob.bclient.net.CMessage;

import static spacecon.tob.bclient.net.IProtocol.M_INTERFACE_DELSTONE;

/**
 * Created by HongGukSong on 9/19/2017.
 */

public class SSInterface {
    public static final byte SS_NONE = 0;
    //    public static final byte SS_EMPTY = 0;
    public static final byte SS_WHITE = 2;
    public static final byte SS_BLACK = 1;

    public static final byte SIZE6 = 6;
    public static final byte SIZE7 = 7;
    public static final byte SIZE8 = 8;
    public static final byte SIZE9 = 9;
    public static final byte SIZE13 = 13;
    public static final byte SIZE19 = 19;

    public static final byte SS_ST_NONE = 0;
    public static final byte SS_ST_REPLAY = 1;
    public static final byte SS_ST_EDIT_TERR = 2;
    public static final byte SS_ST_EDIT = 3;
    public static final byte SS_ST_PLAY = 4;
    public static final byte SS_ST_AUTOPLAY = 40;
    public static final byte SS_ST_NETPLAY = 5;
    public static final byte SS_ST_INTERNETPLAY = 6;
    public static final byte SS_ST_TSUME = -1;

    public static final int SS_MAN = 0;
    public static final int SS_NET = 1;
    public static final int SS_COM = 0x100;

    public static final byte SS_LEVEL_LOW = 0;
    public static final byte SS_LEVEL_STANDARD = 1;
    public static final byte SS_LEVEL_HI = 2;
    public static final byte SS_LEVEL_MOSTHI = 3;

    public static final byte SS_INIT_KIFU = 0;
    public static final byte SS_DEL_KIFU = 1;

    public static final byte SS_ACCEPT = 1;
    public static final byte SS_NOACCEPT = 0;

    public static final int MAX_INDEX = 10000;

    // Engine Macros defined by KWJ
    public static final byte IS_BLACK = JPadukKernel.IS_BLACK;
    public static final byte IS_WHITE = JPadukKernel.IS_WHITE;
    public static final byte IS_INITIAL = JPadukKernel.IS_INITIAL;
    public static final byte IS_GAME = JPadukKernel.IS_GAME;
    public static final byte IS_PASS = JPadukKernel.IS_PASS;
    public static final byte IS_GAMEOVER = JPadukKernel.IS_GAMEOVER;
    public static final byte IS_NOTPLACE = JPadukKernel.IS_NOTPLACE;
    public static final byte IS_COUNTOVER = JPadukKernel.IS_COUNTOVER;
    public static final byte IS_OK = JPadukKernel.IS_OK;
    public static final byte IS_OUTBOARD = JPadukKernel.IS_OUTBOARD;
    public static final byte IS_PRESTONE = JPadukKernel.IS_PRESTONE;
    public static final byte IS_PAEPOS = JPadukKernel.IS_PAEPOS;
    public static final byte IS_DANSU = JPadukKernel.IS_DANSU;
    public static final byte IS_NOSTONE = JPadukKernel.IS_NOSTONE;
    public static final int M_PASS = JPadukKernel.M_PASS;
    public static final int MOVE_LIMIT = 400;

    // Attributes
    private short m_nReplayNum;
    private boolean m_bEdit;
    private int m_nGameState;
    private byte[][] m_byBoard = new byte[19][19]; // BYTE
    private byte[][] m_byIniBoard = new byte[19][19]; // BYTE
    private PlaySetting m_setInfo = new PlaySetting();
    private JPadukKernel m_engine = new JPadukKernel();
    private short[] m_wHistory = new short[MAX_INDEX]; // WORD
    private int m_nHistoryIndex;
    private byte m_byTurn;
    private short m_nKifuCounter;
    private short[] m_nDeadCount = new short[2];
    private byte m_byFirstColor; // BYTE
    // 2007-05-02 : 1
    private short[] m_wPaePos = new short[MOVE_LIMIT];
    private boolean m_bGameEnded = false;

    public SSInterface() {
        super();
        //m_engine = null;
        SS_Init();
    }

    // Operation
    public boolean SS_Init() {
        m_setInfo.nBlackPlayer = SS_MAN;
        m_setInfo.nWhitePlayer = SS_MAN;
        m_setInfo.nBoardSize = SIZE19;
        m_setInfo.nComLevel = SS_LEVEL_STANDARD;
        m_setInfo.nGomi = 0;
        m_setInfo.nHandicap = 0;
        //memset(m_byBoard, 0, sizeof(m_byBoard));
        initBoard(m_byBoard, 0);
        initBoard(m_byIniBoard, 0);
        // memset(m_wHistory, -1, sizeof(m_wHistory));
        initHistory(m_wHistory, -1);
        // memset(m_nDeadCount, 0, sizeof(m_nDeadCount));
        initHistory(m_nDeadCount, 0);
        m_nHistoryIndex = 0;
        m_nKifuCounter = 0;
        m_byTurn = SS_BLACK;
        m_bEdit = false;
        m_nReplayNum = 0;
        m_bGameEnded = false;
        //SS_DeleteEngine();

        return true;
    }

    public static final int MOVE_NUM = 0;
    public static final int MOVE_COLOR = 1;
    public static final int MOVE_X = 2;
    public static final int MOVE_Y = 3;

    public int SS_GetNumGOTO(int nX, int nY, int nCurNum){
        if(!isExistStone(nX, nY))
            return -1;
        int []move = new int[4];
        do{
            SS_GetKifuMove(nCurNum, move);
            nCurNum --;
            if(nCurNum < 0)
                return -1;
        }while(!(move[MOVE_X] == nX && move[MOVE_Y] == nY));
        return move[MOVE_NUM];
    }
    public boolean isExistStone(int nX, int nY){
        byte val = m_byBoard[nY][nX];
        if(val != 0)
            return true;
        return false;
    }
    public void SS_GetKifuMove(int nNum, int[] move) {
//        int nIndex;
        short wPos;
        short[] wDeadPos = new short[361];
        int nColor;
        int nNowNum = nNum;

        int[] nBufArray = new int[2];
        nColor = GetStoneInfo(nNowNum, nBufArray, wDeadPos);
//        nIndex = nBufArray[0];
        wPos = (short) nBufArray[1];

        move[MOVE_COLOR] = nColor;
        move[MOVE_NUM] = nNowNum;
        if (wPos == M_PASS) {
            move[MOVE_X] = -1;
        } else {
            move[MOVE_X] = LOBYTE(wPos);
            move[MOVE_Y] = HIBYTE(wPos);
        }
    }

    public int SS_GetCurrentNum() {
        return m_nReplayNum;
    }

    public int SS_GetStatus() {
        return m_nGameState;
    }

    public void SS_GetBoard(byte byBoard[][]) { // DBOARD
        // memcpy(byBoard, m_byBoard, sizeof(BOARD));
        copyBoard(m_byBoard, byBoard);
    }

    public byte[][] SS_GetBoard() {
        return m_byBoard;
    }

    public boolean SS_IsRotated() {
        return false;
    }

    public int SS_GetBoardSize() {
        return m_setInfo.nBoardSize;
    }

    public PlaySetting SS_GetPlayInfo() {
        // memcpy(pSetInfo, &m_setInfo, sizeof(PLAY_SETTING));
        return m_setInfo;
    }

    public void SS_SetGameEnd() {
        m_bGameEnded = true;
    }
    // implementation
    public boolean SS_IsGameEnd() {
//        int nIndex;
        short wPos1, wPos2;
        short[] wDeadPos = new short[361];
//        int nColor;
        int nNowNum = m_nKifuCounter;
        if ( m_bGameEnded )
            return true;
        if (m_nKifuCounter < 2) {
            return false;
        }

        int[] nBufArray = new int[2];
        /*nColor = */GetStoneInfo(nNowNum, nBufArray, wDeadPos);
//        nIndex = nBufArray[0];
        wPos1 = (short) nBufArray[1];

        /*nColor = */GetStoneInfo(nNowNum - 1, nBufArray, wDeadPos);
//        nIndex = nBufArray[0];
        wPos2 = (short) nBufArray[1];

        if ((wPos1 == wPos2) && (wPos1 == M_PASS)) {
            return true;
        }
        return false;
    }

    public int SS_GetDeadCount(int nColor) {
        return m_nDeadCount[nColor - 1];
    }

    public void SS_GetTerritory(byte byArea[][]) { // DBOARD
        m_engine.Kernel_Ground((byte) 0, byArea);
    }

    public void SS_HintMove(int[] move) {
        short nCase, nFlg = 2;
        short wRet;
        if (m_setInfo.nWhitePlayer == SS_COM) {
            nCase = 1;
        } else {
            nCase = 2;
        }
        short[] scrArray = new short[1];
        wRet = m_engine.Kernel_ComPlay(nCase, nFlg, scrArray);
        if (wRet == M_PASS) {
            move[MOVE_X] = -1;
        } else {
            move[MOVE_X] = LOBYTE(wRet) - 1;
            move[MOVE_Y] = HIBYTE(wRet) - 1;
        }
        move[MOVE_COLOR] = m_byTurn;
        move[MOVE_NUM] = m_nKifuCounter + 1;
    }

    public void SS_FindMove(int[] move) {
        short nCase, nFlg = 1;
        short wRet;
        if (m_setInfo.nWhitePlayer == SS_COM) {
            nCase = 1;
        } else {
            nCase = 2;
        }
        short[] scrArray = new short[1];
        wRet = m_engine.Kernel_ComPlay(nCase, nFlg, scrArray);
//        nScore = scrArray[0];
        if (wRet == M_PASS) {
            move[MOVE_X] = -1;
        } else {
            move[MOVE_X] = LOBYTE(wRet) - 1;
            move[MOVE_Y] = HIBYTE(wRet) - 1;
        }
        move[MOVE_COLOR] = m_byTurn;
        move[MOVE_NUM] = m_nKifuCounter + 1;

        // todo : back to original code.
//        int x;
//        int y;
//        int nColor = this.SS_GetTurn();
//        int nret;
//        int nCounter = 0;
//        while (nCounter < 10 ) {
//            x = rand(m_setInfo.nBoardSize);
//            y = rand(m_setInfo.nBoardSize);
//            nret = TestStone_kwj(x, y, nColor);
//            if ((nret == IS_OK) || (nret == IS_DANSU)) {
//                move.x = x;
//                move.y = y;
//                break;
//            }
//            nCounter++;
//        }
//        if( nCounter >= 10 ) {
//            move.x = -1;
//            move.y = 0;
//        }
//
//        move.nStoneColor = nColor;
//        move.nStoneNum = m_nKifuCounter + 1;
//        move.nTime = 0;
    }

    private Random m_random;
    private boolean m_bRanded = false;
    public int rand(int nNum) {
        if (false == m_bRanded) {
            m_random = new Random();
            m_random.setSeed(System.currentTimeMillis());
            // m_random.setSeed(6554);
            m_bRanded = true;
        }
        return Math.abs(m_random.nextInt() % nNum);
    }

    public void SS_NextTurn() {
        m_byTurn = (byte) (3 - m_byTurn);
    }

    public int SS_GetTurn() {
        return m_byTurn;
    }

    public void SS_SetStatus(int nState) {
        m_nGameState = nState;
    }

    public int SS_GetKifuSize() {
        return m_nKifuCounter;
    }

    public void SS_SetTurn(int nTurn) {
        if ((nTurn != SS_BLACK) && (nTurn != SS_WHITE)) {
            nTurn = SS_NONE;
        }
        m_byTurn = (byte) nTurn;
        if (m_nKifuCounter == 0) {
            m_byFirstColor = m_byTurn;
        }
    }

    public int SS_GetCurrentPlayer() {
        int nCurrentPlayer;
        if (m_byTurn == SS_WHITE) {
            nCurrentPlayer = m_setInfo.nWhitePlayer;
        } else {
            nCurrentPlayer = m_setInfo.nBlackPlayer;
        }

        return nCurrentPlayer;
    }

    public int SS_Replay_Add(int nAddNumber) {
        return ReplayNum(nAddNumber + m_nReplayNum);
    }

    public int SS_Replay(int nNumber) {
        return ReplayNum(nNumber);
    }

    public void SS_InitMoveKifu(int nInitMode) {
        int nIndex;
//        short wPos;
        short[] wDeadPos = new short[361];
//        int nColor;
        int nNowNum = m_nReplayNum;

        if ((nInitMode == SS_INIT_KIFU) || (m_nReplayNum == 0)) {
            // memset(m_wHistory, 0xFF, sizeof(m_wHistory));
            initHistory(m_wHistory, -1);
            m_nHistoryIndex = 0;
            m_nKifuCounter = 0;
            m_nReplayNum = 0;
            m_nDeadCount[0] = m_nDeadCount[1] = 0;
        } else if (nInitMode == SS_DEL_KIFU) {
            int[] nBufArray = new int[2];
            /*nColor = */GetStoneInfo(nNowNum, nBufArray, wDeadPos);
            nIndex = nBufArray[0];
//            wPos = (short) nBufArray[1];
            m_nKifuCounter = (short) nNowNum;
            m_nReplayNum = m_nKifuCounter;
            m_nHistoryIndex = nIndex + wDeadPos[0] + 1;
            for (int i = m_nHistoryIndex; i < MAX_INDEX; i++) {
                m_wHistory[i] = -1;
            }
        }
    }

    private void UpdateBoard() {
        m_engine.Kernel_GetBoard(m_byBoard);
    }

    private int ReplayNum(int nReplayNum) {
        int x, y, i;
//        int nIndex;

        if (nReplayNum < 0) {
            nReplayNum = 0;
        }
        if (nReplayNum > m_nKifuCounter) {
            nReplayNum = m_nKifuCounter;
        }
        if (nReplayNum == m_nReplayNum) {
            return m_nReplayNum;
        }

        if (nReplayNum < m_nReplayNum) {
            while (nReplayNum != m_nReplayNum) {
                short wPos;
                short[] wDeadPos = new short[361];
                int[] nBufArray = new int[2];
                int nColor = GetStoneInfo(m_nReplayNum, nBufArray, wDeadPos);
//                nIndex = nBufArray[0];
                wPos = (short) nBufArray[1];
                if ((nColor != SS_BLACK) && (nColor != SS_WHITE)) {
                    return m_nReplayNum;
                }
                if (wPos != M_PASS) {
                    x = LOBYTE(wPos);
                    y = HIBYTE(wPos);
                    short wMove = MAKEWORD(x + 1, y + 1);
                    m_engine.Kernel_DelStone(wMove);
                    for (i = 0; i < wDeadPos[0]; i++) {
                        if (wDeadPos[i + 1] == -1) {
                            break;
                        }
                        x = LOBYTE(wDeadPos[i + 1]);
                        y = HIBYTE(wDeadPos[i + 1]);
                        wMove = MAKEWORD(x + 1, y + 1);
                        m_engine.Kernel_RecoveryStone(wMove,
                                (short) (3 - nColor));
                    }
                    if (m_nReplayNum > 2) {
                        m_engine.Kernel_SetPaePos(m_wPaePos[m_nReplayNum - 2]); // 2007-05-02 :1
                    } else {
                        m_engine.Kernel_SetPaePos((short) 0);
                    }
                } else {
                    m_engine.Kernel_SetPaePos((short) 0); // 2007-05-02 :1
                    m_engine.Kernel_DelStone(wPos);
                }
                m_nDeadCount[nColor - 1] -= wDeadPos[0];
                m_nReplayNum--;
                m_byTurn = (byte) nColor;
            }
        } else {
            while (nReplayNum != m_nReplayNum) {
                short wPos;
                short[] wDeadPos = new short[361];
                int nColor;

                m_nReplayNum++;
                int[] nBufArray = new int[2];
                nColor = GetStoneInfo(m_nReplayNum, nBufArray, wDeadPos);
//                nIndex = nBufArray[0];
                wPos = (short) nBufArray[1];
                if ((nColor != SS_BLACK) && (nColor != SS_WHITE)) {
                    return m_nReplayNum - 1;
                }
                if (wPos != M_PASS) {
                    x = LOBYTE(wPos);
                    y = HIBYTE(wPos);
                    wPos = MAKEWORD(x + 1, y + 1);
                    m_engine.Kernel_PlaceStone(wPos, (short) nColor);
                    m_nDeadCount[nColor - 1] += wDeadPos[0];
                } else {
                    m_engine.Kernel_PlaceStone(wPos, (short) nColor);
                }
                m_byTurn = (byte) (3 - nColor);
            }
        }
        UpdateBoard();
        return m_nReplayNum;
    }


    public int SS_PutStone(int[] move) {

        short nBDeadCount = 0, nWDeadCount = 0, nDeadCount;
        short[] wDeadList = new short[360];
        int i, x, y;

        if (m_nGameState == SS_ST_EDIT) {
            return PlaceEditStone(move[MOVE_X], move[MOVE_Y], move[MOVE_COLOR]);
        }
        if ((m_nGameState != SS_ST_PLAY) &&(m_nGameState != SS_ST_EDIT_TERR)) {
            return IS_NOTPLACE;
        }
        if (move[MOVE_COLOR] != m_byTurn) {
            return IS_NOTPLACE;
        }
        int nBoardSize = m_setInfo.nBoardSize;

        if (move[MOVE_X] != -1) {
            if ((move[MOVE_X] < 0) || (move[MOVE_Y] < 0) ||
                    (move[MOVE_X] >= nBoardSize) || (move[MOVE_Y] >= nBoardSize)) {
                return IS_OUTBOARD;
            }
        }

        short wPos;
        if (move[MOVE_X] == -1) {
            wPos = M_PASS;
        } else {
            wPos = MAKEWORD(move[MOVE_X], move[MOVE_Y]);
        }
        int nRet = IS_PASS;
        if (wPos != M_PASS) {
            short wMove = MAKEWORD(move[MOVE_X] + 1, move[MOVE_Y] + 1);
            nRet = m_engine.Kernel_PlaceStone(wMove, (short) move[MOVE_COLOR]);
            if ((nRet != IS_OK) && (nRet != IS_DANSU)) {
                return nRet;
            }
        } else {
            nRet = m_engine.Kernel_PlaceStone(wPos, (short) move[MOVE_COLOR]);
        }
        short[] arrBDeadCount = new short[1];
        short[] arrWDeadCount = new short[1];
        arrBDeadCount[0] = nBDeadCount;
        arrWDeadCount[0] = nWDeadCount;
        m_engine.Kernel_DeadInfo(arrBDeadCount, arrWDeadCount);
        nBDeadCount = arrBDeadCount[0];
        nWDeadCount = arrWDeadCount[0];
        if (m_byTurn == SS_BLACK) {
            nDeadCount = (short) (nWDeadCount - m_nDeadCount[m_byTurn - 1]);
        } else {
            nDeadCount = (short) (nBDeadCount - m_nDeadCount[m_byTurn - 1]);
        }

        m_engine.Kernel_GetDeadStonePos(wDeadList, nDeadCount);


        if (nDeadCount + 1 + 1 + m_nHistoryIndex >= MAX_INDEX) {
            short wMove = MAKEWORD(move[MOVE_X] + 1, move[MOVE_Y] + 1);
            if (move[MOVE_X] == -1) {
                wMove = M_PASS;
            }
            m_engine.Kernel_SetPaePos(m_wPaePos[m_nKifuCounter - 1]); // 2007-05-02 :1
            m_engine.Kernel_DelStone(wMove);
            for (i = 0; i < nDeadCount; i++) {
                m_engine.Kernel_RecoveryStone(wDeadList[i],
                        (short) (3 - m_byTurn));
            }
            return IS_COUNTOVER;
        }
        m_wHistory[m_nHistoryIndex] = -1;
        m_nHistoryIndex++;
        m_wHistory[m_nHistoryIndex] = wPos;
        m_nHistoryIndex++;
        for (i = 0; i < nDeadCount; i++) {
            x = LOBYTE(wDeadList[i]) - 1;
            y = HIBYTE(wDeadList[i]) - 1;
            m_wHistory[m_nHistoryIndex] = MAKEWORD(x, y);
            m_nHistoryIndex++;
        }

        m_wPaePos[m_nKifuCounter] = m_engine.Kernel_GetPaePos(); // 2007-05-02 : 1
        m_nKifuCounter++;
        m_nReplayNum = m_nKifuCounter;
        m_nDeadCount[m_byTurn - 1] += nDeadCount;
        //	m_byTurn = 3 - m_byTurn;
        UpdateBoard();
        return nRet;
    }

    private boolean SetBoard(byte[][] byBoard) { // by KWJ : 2007-05-01 void->boolean
        int i, j;

        for (i = 0; i < m_setInfo.nBoardSize; i++) {
            for (j = 0; j < m_setInfo.nBoardSize; j++) {
                if ((byBoard[j][i] == SS_BLACK) || (byBoard[j][i] == SS_WHITE)) {
                    short wPos = MAKEWORD(i + 1, j + 1);
                    // by KWJ : 2007-05-01 : return false;
                    if (m_engine.Kernel_PlaceStone(wPos, byBoard[j][i]) ==
                            IS_COUNTOVER) {
                        return false;
                    }
                }
            }
        }
        UpdateBoard();

        return true;
    }

    public void SS_DeleteEngine() {
        if (m_engine != null) {
            m_engine = null;
        }
        System.gc(); // added by KWJ 21007-03-02
    }

    public boolean SS_SetPlayInfo(PlaySetting setInfo, boolean bNew) {
        int x, y;
        int nCpuColor = SS_BLACK;
        if (!bNew) {
            if ((setInfo.nHandicap != m_setInfo.nHandicap) ||
                    (setInfo.nBoardSize != m_setInfo.nBoardSize)) {
                // TODO : Assert
            }
        } else {
            PlaySetting tmpSet = new PlaySetting();
            setInfo.copyTo( tmpSet );
            SS_Init();
            tmpSet.copyTo( setInfo );
            /*
                         // memset(m_wHistory, 0xFF, sizeof(m_wHistory));
                         initHistory( m_wHistory, -1 );
                         // memset(m_nDeadCount, 0, sizeof(m_nDeadCount));
                         initHistory( m_nDeadCount, 0 );
                         m_nHistoryIndex = 0;
                         m_nKifuCounter = 0;
                         m_nReplayNum = 0;
                         m_bEdit = false;
             */
        }

//        SS_DeleteEngine();
        if (!SS_CreateEngine()) {
            return false;
        }
        setInfo.copyTo(m_setInfo);

        if (bNew) {
            m_byFirstColor = SS_BLACK;
            if (setInfo.nHandicap > 1) { // Q : setInfo? m_setInfo
                m_byFirstColor = SS_WHITE;
            }
            m_byTurn = m_byFirstColor;
        }

        if (m_setInfo.nWhitePlayer == SS_COM) {
            nCpuColor = 0;
        } else {
            nCpuColor = 1;
        }
        m_engine.Kernel_Init((short) m_setInfo.nBoardSize, (short) nCpuColor);
        int nBufLevel;
        switch (m_setInfo.nComLevel) {
            case 0:
                nBufLevel = JPadukKernel.LEVEL0;
                break;
            case 1:
                nBufLevel = JPadukKernel.LEVEL1;
                break;
            case 2:
                nBufLevel = JPadukKernel.LEVEL2;
                break;
            case 3:
                nBufLevel = JPadukKernel.LEVEL3;
                break;
            case 4:
            default:
                nBufLevel = JPadukKernel.LEVEL4;
                break;
        }
        m_engine.Kernel_SetLevel(nBufLevel);

        if (!m_bEdit) {
            InitBoard(m_byIniBoard, (byte) setInfo.nHandicap,
                    (byte) setInfo.nBoardSize); // Q : setInfo, m_setInfo
        }
        // by KWJ : 2007-05-01 : return false;
        if (SetBoard(m_byIniBoard) == false) {
            return false;
        }

        if (m_nKifuCounter > 0) {
//            int nIndex;
            short wPos;
            short[] wDeadList = new short[361];
            int nColor = m_byFirstColor;
            if (!((m_nKifuCounter == 0) ||
                    (nColor == SS_BLACK) || (nColor == SS_WHITE))) {
                // TODO : assert here
            }

            for (int i = 0; i < m_nKifuCounter; i++) {
                int[] nBufArray = new int[2];
                GetStoneInfo(i, nBufArray, wDeadList);
//                nIndex = nBufArray[0];
                wPos = (short) nBufArray[1];

                if (wPos != M_PASS) {
                    x = LOBYTE(wPos);
                    y = HIBYTE(wPos);
                    wPos = MAKEWORD(x + 1, y + 1);
                    m_engine.Kernel_PlaceStone(wPos, (short) nColor);
                } else {
                    m_engine.Kernel_PlaceStone(wPos, (short) nColor);
                }
                nColor = 3 - nColor;
            }
            m_byTurn = (byte) nColor;
            m_nReplayNum = m_nKifuCounter;
        }

        UpdateBoard();
        return true;
    }

    //    private int GetStoneInfo(int nNum, int *pnIndex, WORD *pwPos, WORD *pwDeadPos);
    private int GetStoneInfo(int nNum, int[] rtValues, short[] wDeadPos) {
        int nStoneCount = m_nKifuCounter;
        int nMataCount = nStoneCount - nNum;
        int nIndex = m_nHistoryIndex - 1;
        int nColor = SS_NONE;
        int nDeadCount = 0;
        final int pnIndex = 0;
        final int pwPos = 1;
        rtValues[pwPos] = -1;
        if (wDeadPos == null) {
            return -1;
        }
        if (nMataCount < 0) {
            return -1;
        } while (nMataCount >= 0) {
            nIndex--;
            if (nIndex == -1) {
                return -1;
            }
            if (m_wHistory[nIndex] == -1) {
                nMataCount--;
                if (nMataCount < 0) {
                    rtValues[pnIndex] = nIndex + 1;
                    break;
                }
            }
        }
        rtValues[pwPos] = m_wHistory[rtValues[pnIndex]];
        nIndex = rtValues[pnIndex];
        while (true) {
            nIndex++;
            if (m_wHistory[nIndex] == -1) {
                break;
            }
            nDeadCount++;
            wDeadPos[nDeadCount] = m_wHistory[nIndex];
        }
        wDeadPos[0] = (short) nDeadCount;

        if (m_byFirstColor == SS_NONE) {
            // TODO : assert
        }

        if ((nNum % 2) != 0) {
            nColor = m_byFirstColor;
        } else {
            nColor = 3 - m_byFirstColor;
        }
        return nColor;
    }

    private int PlaceEditStone(int x, int y, int nColor) {
        if (nColor == SS_NONE) {
            m_byBoard[y][x] = 0;
            return IS_OK;
        }
        int nret = TestStone(x, y, nColor);
        if ((nret == IS_OK) || (nret == IS_DANSU)) {
            FindDeadStones(x, y, nColor);
            short wPos = MAKEWORD(x + 1, y + 1);
            nret = m_engine.Kernel_PlaceStone(wPos, (short) nColor);
            return nret;
        }
        return nret;
    }

    private void FindDeadStones(int x, int y, int nColor) {
        nColor = 3 - nColor;

        if ((y > 0) && (m_byBoard[y - 1][x] == nColor)) {
            TestStoneB(x, y - 1);
        }
        if ((x > 0) && (m_byBoard[y][x - 1] == nColor)) {
            TestStoneB(x - 1, y);
        }
        if ((y < m_setInfo.nBoardSize - 1) && (m_byBoard[y + 1][x] == nColor)) {
            TestStoneB(x, y + 1);
        }
        if ((x < m_setInfo.nBoardSize - 1) && (m_byBoard[y][x + 1] == nColor)) {
            TestStoneB(x + 1, y);
        }
    }

    public int TestStone_kwj(int x, int y, int nCurStone) {
        boolean bAtari = false;
        boolean bRet;
        int nTest;

        // todo : back to original code.
        if (m_byBoard[y][x] != 0) {
            return IS_NOTPLACE;
        }
//            m_byBoard[y][x] = 0;

        m_byBoard[y][x] = (byte) nCurStone;

        bRet = TestStoneA(x, y);
        {
            int nColor = 3 - nCurStone;
            int n = 0;
//            int x1, y1;

            if ((y > 0) && (m_byBoard[y - 1][x] == nColor)) {
                nTest = TestStoneC(x, y - 1);
                if (nTest == 1) {
                    n += 1;
//                    x1 = x;
//                    y1 = y - 1;
                } else if (nTest == 2) {
                    bAtari = true;
                }
            }

            if ((x > 0) && (m_byBoard[y][x - 1] == nColor)) {
                nTest = TestStoneC(x - 1, y);
                if (nTest == 1) {
                    n += 1;
//                    x1 = x - 1;
//                    y1 = y;
                } else if (nTest == 2) {
                    bAtari = true;
                }
            }

            if ((y < m_setInfo.nBoardSize - 1) &&
                    (m_byBoard[y + 1][x] == nColor)) {
                nTest = TestStoneC(x, y + 1);
                if (nTest == 1) {
                    n += 1;
//                    x1 = x;
//                    y1 = y + 1;
                } else if (nTest == 2) {
                    bAtari = true;
                }
            }

            if ((x < m_setInfo.nBoardSize - 1) &&
                    (m_byBoard[y][x + 1] == nColor)) {
                nTest = TestStoneC(x + 1, y);
                if (nTest == 1) {
                    n += 1;
//                    x1 = x + 1;
//                    y1 = y;
                } else if (nTest == 2) {
                    bAtari = true;
                }
            }

            if (bRet) {
                if (n == 0) {
                    m_byBoard[y][x] = 0;
                    return IS_NOTPLACE;
                } else if (n == 1) {
                    if (TestStonePae(x, y)) {
                        m_byBoard[y][x] = 0;
                        return IS_PAEPOS;
                    }
                }
            }
            if (bAtari) {
                return IS_DANSU;
            }
        }
        return IS_OK;
    }

    private int TestStone(int x, int y, int nCurStone) {
        boolean bAtari = false;
        boolean bRet;
        int nTest;

        if (m_byBoard[y][x] != 0) {
            m_byBoard[y][x] = 0;
        }

        m_byBoard[y][x] = (byte) nCurStone;

        bRet = TestStoneA(x, y);
        {
            int nColor = 3 - nCurStone;
            int n = 0;
//            int x1, y1;

            if ((y > 0) && (m_byBoard[y - 1][x] == nColor)) {
                nTest = TestStoneC(x, y - 1);
                if (nTest == 1) {
                    n += 1;
//                    x1 = x;
//                    y1 = y - 1;
                } else if (nTest == 2) {
                    bAtari = true;
                }
            }

            if ((x > 0) && (m_byBoard[y][x - 1] == nColor)) {
                nTest = TestStoneC(x - 1, y);
                if (nTest == 1) {
                    n += 1;
//                    x1 = x - 1;
//                    y1 = y;
                } else if (nTest == 2) {
                    bAtari = true;
                }
            }

            if ((y < m_setInfo.nBoardSize - 1) &&
                    (m_byBoard[y + 1][x] == nColor)) {
                nTest = TestStoneC(x, y + 1);
                if (nTest == 1) {
                    n += 1;
//                    x1 = x;
//                    y1 = y + 1;
                } else if (nTest == 2) {
                    bAtari = true;
                }
            }

            if ((x < m_setInfo.nBoardSize - 1) &&
                    (m_byBoard[y][x + 1] == nColor)) {
                nTest = TestStoneC(x + 1, y);
                if (nTest == 1) {
                    n += 1;
//                    x1 = x + 1;
//                    y1 = y;
                } else if (nTest == 2) {
                    bAtari = true;
                }
            }

            if (bRet) {
                if (n == 0) {
                    m_byBoard[y][x] = 0;
                    return IS_NOTPLACE;
                } else if (n == 1) {
                    if (TestStonePae(x, y)) {
                        m_byBoard[y][x] = 0;
                        return IS_PAEPOS;
                    }
                }
            }
            if (bAtari) {
                return IS_DANSU;
            }
        }
        return IS_OK;
    }

    private boolean TestStonePae(int x, int y) {
        return false;
    }

    private void TestStoneB(int x, int y) {
        byte[][] byTemp = new byte[19][19];

        int nTemp, x1, y1;
        int nColor = m_byBoard[y][x];
        initBoard(byTemp, 0);
        byTemp[y][x] = 1;
        do {
            nTemp = 0;
            for (y1 = 0; y1 < m_setInfo.nBoardSize; y1++) {
                for (x1 = 0; x1 < m_setInfo.nBoardSize; x1++) {
                    if ((byTemp[y1][x1] != 0) || (m_byBoard[y1][x1] != nColor)) {
                        continue;
                    }
                    if (((x1 > 0) && (byTemp[y1][x1 - 1] != 0)) ||
                            ((y1 > 0) && (byTemp[y1 - 1][x1] != 0)) ||
                            ((x1 < m_setInfo.nBoardSize - 1) &&
                                    (byTemp[y1][x1 + 1] != 0)) ||
                            ((y1 < m_setInfo.nBoardSize - 1) &&
                                    (byTemp[y1 + 1][x1] != 0))) {
                        byTemp[y1][x1] = 1;
                        nTemp++;
                    }
                }
            }
        } while (nTemp != 0);

        nTemp = 0;
        for (y1 = 0; y1 < m_setInfo.nBoardSize; y1++) {
            for (x1 = 0; x1 < m_setInfo.nBoardSize; x1++) {
                if (m_byBoard[y1][x1] != 0) {
                    continue;
                }
                if (((x1 > 0) && (byTemp[y1][x1 - 1] != 0)) ||
                        ((y1 > 0) && (byTemp[y1 - 1][x1] != 0)) ||
                        ((x1 < m_setInfo.nBoardSize - 1) &&
                                (byTemp[y1][x1 + 1] != 0)) ||
                        ((y1 < m_setInfo.nBoardSize - 1) &&
                                (byTemp[y1 + 1][x1] != 0))) {
                    break;
                }
            }
            if (x1 < m_setInfo.nBoardSize) {
                break;
            }
        }
        if (y1 < m_setInfo.nBoardSize) {
            return;
        }

        for (y1 = 0; y1 < m_setInfo.nBoardSize; y1++) {
            for (x1 = 0; x1 < m_setInfo.nBoardSize; x1++) {
                if (byTemp[y1][x1] == 0) {
                    continue;
                }
//		m_wHistory[m_nHistoryIndex ++] = (y1 << 8) | x1;
                m_byBoard[y1][x1] = 0;
            }
        }
    }

    private int TestStoneC(int x, int y) {
        byte[][] byTemp = new byte[19][19];

        int nTemp, x1, y1;
        int nColor = m_byBoard[y][x];
        int nLiveNum = 0;

        initBoard(byTemp, 0);
        byTemp[y][x] = 1;
        do {
            nTemp = 0;
            for (y1 = 0; y1 < m_setInfo.nBoardSize; y1++) {
                for (x1 = 0; x1 < m_setInfo.nBoardSize; x1++) {
                    if ((byTemp[y1][x1] != 0) || (m_byBoard[y1][x1] != nColor)) {
                        continue;
                    }
                    if (((x1 > 0) && (byTemp[y1][x1 - 1] != 0)) ||
                            ((y1 > 0) && (byTemp[y1 - 1][x1] != 0)) ||
                            ((x1 < m_setInfo.nBoardSize - 1) &&
                                    (byTemp[y1][x1 + 1] != 0)) ||
                            ((y1 < m_setInfo.nBoardSize - 1) &&
                                    (byTemp[y1 + 1][x1] != 0))) {
                        byTemp[y1][x1] = 1;
                        nTemp++;
                    }
                }
            }
        } while (nTemp != 0);

        nTemp = 0;
        for (y1 = 0; y1 < m_setInfo.nBoardSize; y1++) {
            for (x1 = 0; x1 < m_setInfo.nBoardSize; x1++) {
                if (m_byBoard[y1][x1] != 0) {
                    continue;
                }
                if (((x1 > 0) && (byTemp[y1][x1 - 1] != 0)) ||
                        ((y1 > 0) && (byTemp[y1 - 1][x1] != 0)) ||
                        ((x1 < m_setInfo.nBoardSize - 1) &&
                                (byTemp[y1][x1 + 1] != 0)) ||
                        ((y1 < m_setInfo.nBoardSize - 1) &&
                                (byTemp[y1 + 1][x1] != 0))) {
                    nLiveNum++;
                    if (nLiveNum >= 2) {
                        break;
                    }
                    continue;
                }
            }
            if (x1 < m_setInfo.nBoardSize) {
                break;
            }
        }
        if (nLiveNum == 1) {
            return 2;
        }
        if ((y1 < m_setInfo.nBoardSize) || (nLiveNum > 1)) {
            return 0;
        }

        return 1;
    }

    private boolean TestStoneA(int x, int y) {
        byte[][] byTemp = new byte[19][19];

        int nTemp, x1, y1;
        int nColor = m_byBoard[y][x];

        initBoard(byTemp, 0);
        byTemp[y][x] = 1;
        do {
            nTemp = 0;
            for (y1 = 0; y1 < m_setInfo.nBoardSize; y1++) {
                for (x1 = 0; x1 < m_setInfo.nBoardSize; x1++) {
                    if ((byTemp[y1][x1] != 0) || (m_byBoard[y1][x1] != nColor)) {
                        continue;
                    }
                    if (((x1 > 0) && (byTemp[y1][x1 - 1] != 0)) ||
                            ((y1 > 0) && (byTemp[y1 - 1][x1] != 0)) ||
                            ((x1 < m_setInfo.nBoardSize - 1) &&
                                    (byTemp[y1][x1 + 1] != 0)) ||
                            ((y1 < m_setInfo.nBoardSize - 1) &&
                                    (byTemp[y1 + 1][x1] != 0))) {
                        byTemp[y1][x1] = 1;
                        nTemp++;
                    }
                }
            }
        } while (nTemp != 0);

        nTemp = 0;
        for (y1 = 0; y1 < m_setInfo.nBoardSize; y1++) {
            for (x1 = 0; x1 < m_setInfo.nBoardSize; x1++) {
                if (m_byBoard[y1][x1] != 0) {
                    continue;
                }
                if (((x1 > 0) && (byTemp[y1][x1 - 1] != 0)) ||
                        ((y1 > 0) && (byTemp[y1 - 1][x1] != 0)) ||
                        ((x1 < m_setInfo.nBoardSize - 1) &&
                                (byTemp[y1][x1 + 1] != 0)) ||
                        ((y1 < m_setInfo.nBoardSize - 1) &&
                                (byTemp[y1 + 1][x1] != 0))) {
                    break;
                }
            }
            if (x1 < m_setInfo.nBoardSize) {
                break;
            }
        }
        if (y1 < m_setInfo.nBoardSize) {
            return false;
        }

        return true;
    }

    private final void InitBoard(byte[][] pByBoard, byte byHandiCap,
                                 byte byBoardSize) { // BYTE
        byte[][][] byGIniStonePos = new byte[][][] { {
                { 3, 15}, {15, 3}, {3, 3}, {15, 15}, {3, 9}, {15, 9}, {9, 3}, {9, 15}, {9, 9}, {16, 2}
        }, { {3, 9}, {9, 3}, {3, 3}, {9, 9}, {3, 6}, {9, 6}, {6, 3}, {6, 9}, {6, 6}, {10, 2}
        }, { {2, 6}, {6, 2}, {2, 2}, {6, 6}, {2, 4}, {6, 4}, {4, 2}, {4, 6}, {4, 4}, {7, 1}
        }
        }; // BYTE

        byte[][] byIniBoard = new byte[19][19];
        // memset(byIniBoard, 0, sizeof(BOARD));
        initBoard(byIniBoard, 0);

        if (byHandiCap < 2) {
            return;
        }

        if ((byHandiCap > 9) && (byBoardSize != 19)) {
            byHandiCap = 9;
        }

        if ((byHandiCap >= 2) && (byHandiCap <= 9)) {
            int nSize, i, x, y;

            if (byBoardSize == 19) {
                nSize = 0;
            } else if (byBoardSize == 13) {
                nSize = 1;
            } else {
                nSize = 2;
            }

            if ((byHandiCap == 5) || (byHandiCap == 7)) {
                for (i = 0; i < byHandiCap - 1; i++) {
                    x = byGIniStonePos[nSize][i][0];
                    y = byGIniStonePos[nSize][i][1];
                    byIniBoard[y][x] = SS_BLACK;
                }

                x = byGIniStonePos[nSize][8][0];
                y = byGIniStonePos[nSize][8][1];
                byIniBoard[y][x] = SS_BLACK;
            } else {
                for (i = 0; i < byHandiCap; i++) {
                    x = byGIniStonePos[nSize][i][0];
                    y = byGIniStonePos[nSize][i][1];
                    byIniBoard[y][x] = SS_BLACK;
                }
            }
        }

        if ((byHandiCap > 9) && (byBoardSize == 19)) {
            int[][][] nGIniStoneOverPos = new int[][][] { { {6, 6}, {12, 12}
            }, { {6, 6}, {12, 12}, {9, 9} //10
            }, { {6, 6}, {12, 12}, {6, 12}, {12, 6} //11
            }, { {6, 6}, {12, 12}, {6, 12}, {12, 6}, {9, 9} //12
            }, { {6, 6}, {12, 12}, {6, 12}, {12, 6}, {2, 6}, {16, 12} //13
            }, { {6, 6}, {12, 12}, {6, 12}, {12, 6}, {2, 6}, {16, 12}, {9, 9} //14
            }, { {6, 6}, {12, 12}, {6, 12}, {12, 6}, {2, 6}, {16, 12}, {6, 16},  //15
                    {12, 2}
            }, { {6, 6}, {12, 12}, {6, 12}, {12, 6}, {2, 6}, {16, 12}, {6, 16},  //16
                    {12, 2}, {9, 9}
            }, { {6, 6}, {12, 12}, {6, 12}, {12, 6}, {2, 6}, {16, 12}, {6, 16},  //17
                    {12, 2}, {2, 12}, {16, 6}
            }, { {6, 6}, {12, 12}, {6, 12}, {12, 6}, {2, 6}, {16, 12}, {6, 16},  //18
                    {12, 2}, {2, 12}, {16, 6}, {9, 9}
            }, { {6, 6}, {12, 12}, {6, 12}, {12, 6}, {2, 6}, {16, 12}, {6, 16},  //19
                    {12, 2}, {2, 12}, {16, 6}, {6, 2},
                    {12, 16}
            }, { {6, 6}, {12, 12}, {6, 12}, {12, 6}, {2, 6}, {16, 12}, {6, 16},  //20
                    {12, 2}, {2, 12}, {16, 6}, {6, 2},
                    {12, 16}, {9, 9}
            }, { {6, 6}, {12, 12}, {6, 12}, {12, 6}, {2, 6}, {16, 12}, {6, 16},  //21
                    {12, 2}, {2, 12}, {16, 6}, {6, 2},
                    {12, 16}, {1, 9}, {17, 9}
            }, { {6, 6}, {12, 12}, {6, 12}, {12, 6}, {2, 6}, {16, 12}, {6, 16},  //22
                    {12, 2}, {2, 12}, {16, 6}, {6, 2},
                    {12, 16}, {1, 9}, {17, 9}, {9, 9}
            }, { {6, 6}, {12, 12}, {6, 12}, {12, 6}, {2, 6}, {16, 12}, {6, 16},  //23
                    {12, 2}, {2, 12}, {16, 6}, {6, 2},
                    {12, 16}, {1, 9}, {17, 9}, {9, 1}, {9,
                    17}
            }, { {6, 6}, {12, 12}, {6, 12}, {12, 6}, {2, 6}, {16, 12}, {6, 16},  //24
                    {12, 2}, {2, 12}, {16, 6}, {6, 2},
                    {12, 16}, {1, 9}, {17, 9}, {9, 1}, {9,
                    17}, {9, 9}
            } //25
            };
            int i, x, y;
            for (i = 0; i < 8; i++) {
                x = byGIniStonePos[0][i][0];
                y = byGIniStonePos[0][i][1];
                byIniBoard[y][x] = SS_BLACK;
            }
            for (i = 0; i < byHandiCap - 8; i++) {
                x = nGIniStoneOverPos[byHandiCap - 10][i][0];
                y = nGIniStoneOverPos[byHandiCap - 10][i][1];
                byIniBoard[y][x] = SS_BLACK;
            }
        }

        copyBoard(byIniBoard, pByBoard);
    }

    public boolean SS_CreateEngine() {
        m_engine = null;
        System.gc();
        m_engine = new JPadukKernel();
        if (m_engine == null) {
            return false;
        }
        // TODO : init with 0 m_engine
        // memset(m_engine, 0, sizeof(CPadukEngine));
        return true;
    }

    public boolean SS_ExitEditBoard(int nExitMode) { // by KWJ : void -> boolean
        PlaySetting setInfo = new PlaySetting();
        if (nExitMode == SS_NOACCEPT) {
            UpdateBoard();
            return true; // by KWJ : 2007-05-01 return-> return true
        }
        SS_InitMoveKifu(SS_INIT_KIFU);
        m_bEdit = true;
        copyBoard(m_byBoard, m_byIniBoard);
        m_setInfo.copyTo(setInfo);
        // by KWJ : 2007-05-01 : return false
        if (SS_SetPlayInfo(setInfo, false) == false) {
            return false;
        }
        m_byTurn = SS_NONE;
        m_nGameState = SS_ST_REPLAY;
        m_byFirstColor = SS_NONE;

        return true;
    }

    public byte[][] SS_GetIniBoard() {
        return m_byIniBoard;
//            memcpy(byIniBoard, m_byIniBoard, sizeof(BOARD));
    }

    public void SS_SetIniBoard(byte[][] byBoard) {
        copyBoard(byBoard, m_byIniBoard);
//            memcpy(m_byIniBoard, byBoard, sizeof(BOARD));
    }

    public void SS_SetBoard(byte[][] byBoard) {
        SetBoard(byBoard);
    }

    public boolean SS_IsEditFlag() {
        return m_bEdit;
    }

    public void SS_SetEditFlag(boolean bEdit) {
        m_bEdit = bEdit;
    }

    public byte SS_GetFirstTurn() {
        return m_byFirstColor;
    }

    private byte m_byElegalGame = 0;
    public byte SS_GetElegalGame() {
        return m_byElegalGame;
    }

    public void SS_SetElegalGame(byte byElegal) {
        m_byElegalGame = byElegal;
    }


    // Private functions defined by KWJ
    public static final short MAKEWORD(int lowByte, int highByte) {
        lowByte = lowByte & 0xFF;
        highByte = highByte & 0xFF;
        short sResult = (short) ((highByte << 8) | lowByte);

        return sResult;
    }

    public static final byte HIBYTE(int nValue) {
        return (byte) ((nValue >> 8) & 0xFF);
    }

    public static final byte LOBYTE(int nValue) {
        return (byte) (nValue & 0xFF);
    }

    public static final void copyBoard(byte[][] srcBoard, byte[][] destBoard) {
        int i, j;
        for (i = 0; i < destBoard.length; i++) {
            for (j = 0; j < destBoard[i].length; j++) {
                destBoard[i][j] = srcBoard[i][j];
            }
        }
    }

    public static final void initBoard(byte[][] board, int nInitValue) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = (byte) nInitValue;
            }
        }
    }

    public static final void initHistory(short[] wHistory, int nInitValue) {
        for (int i = 0; i < wHistory.length; i++) {
            wHistory[i] = (short) nInitValue;
        }
    }

    public static final void initArray(int[] nArray, int nInitValue) {
        for (int i = 0; i < nArray.length; i++) {
            nArray[i] = nInitValue;
        }
    }

    public void SS_GetLastStonePos( int nPos[], boolean bReplay ) {
        short wPos;
        short[] wDeadPos = new short[361];
        int[] nBufArray = new int[2];
        int nColor;
        int nNum = bReplay ? m_nReplayNum : m_nKifuCounter;
        while (nNum > 0) {
            nColor = GetStoneInfo(nNum, nBufArray, wDeadPos);
            wPos = (short) nBufArray[1];
            if ((nColor != SS_BLACK) && (nColor != SS_WHITE)) {
                break;
            }
            if (wPos != M_PASS) {
                nPos[MOVE_NUM] = nNum;
                nPos[MOVE_COLOR] = nColor;
                nPos[MOVE_X] = LOBYTE(wPos);
                nPos[MOVE_Y] = HIBYTE(wPos);
                break;
            }
            nNum --;
        }
    }
}
