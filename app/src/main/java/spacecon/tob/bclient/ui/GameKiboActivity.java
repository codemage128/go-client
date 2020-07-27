package spacecon.tob.bclient.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import javax.microedition.khronos.opengles.GL;

import spacecon.tob.bclient.Global;
import spacecon.tob.bclient.R;
import spacecon.tob.bclient.common.Common;
import spacecon.tob.bclient.engine.PlaySetting;
import spacecon.tob.bclient.engine.SSInterface;
import spacecon.tob.bclient.net.CMessage;
import spacecon.tob.bclient.object.GameKiboRecord;

/**
 * Created by HongGukSong on 10/26/2017.
 */

public class GameKiboActivity extends AppCompatActivity implements Handler.Callback, View.OnTouchListener, Common{

    public TextView kibo_text;
    protected FrameLayout frameLayout_board;
    protected Board board;
    protected GameKiboRecord m_KiboData;
    private byte m_nBoardSize = Global.m_nBoardsize_kibo;
    private SSInterface m_interface = new SSInterface();
    private int m_nCurNum;

    private int m_nLastMove[] = { 0, SS_NONE, 0, 0 };
    private byte m_byReviewing = REVIEWING_NONE;

    private static final byte REVIEWING_NONE = 0;
    private static final byte REVIEWING_KIFU = 1;
    private static final byte REVIEWING_PUTSTONE = 2;
    private PlaySetting m_setInfo;
    private int m_count = 0;
    private int m_tmp = m_count;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        Handler packetHandler = new Handler(this);
        Global.m_packetHandler = packetHandler;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kibo);
        kibo_text = (TextView)findViewById(R.id.textView_kiboroomid) ;
        frameLayout_board = (FrameLayout) findViewById(R.id.frame_board_1);

        Init();
        draw();
    }

    private void draw() {

        frameLayout_board.addView(board);
        frameLayout_board.setOnTouchListener(this);
        startKibo();
    }



    private void Init() {

        m_KiboData = Global.m_kiboRecord;
        //kibo_text.setText(Global.g_KiboNumber);
        board = new Board(this, frameLayout_board);

    }
    public void backward_kibo(View view){

        m_tmp ++;
        if(m_tmp >= m_count) {
            m_tmp = m_count;
        }
        m_interface.SS_Replay(m_count -  m_tmp);
        drawstone();
    }
    public void forward_kibo(View view){
        m_tmp --;
        if (m_tmp < 0){
            m_tmp = 0;
        }
        m_interface.SS_Replay(m_count - m_tmp);
        drawstone();
    }
    public void auto_kibo(View view){

    }

    @Override
    public boolean handleMessage(Message message) {
        onMessage((CMessage) message.obj);
        return false;
    }

    private void onMessage(CMessage message) {
        int nHeader = message.getHeader();
        Log.d("      ==Receive==     ", Integer.toHexString(message.getHeader()));

    }


    private void drawstone() {
        //, 과 -가 없으면 실패하는 코드삽입해야함.
        String[] string = Global.m_KiboData.split(",");
        int count = string.length;
        m_count = count ;
        for (int i = 0; i < m_count - m_tmp; i ++){
            String[] string_temp = string[i].split("-");
            byte stone_color = Byte.parseByte(string_temp[0]);
            int stone_pos = Integer.parseInt(string_temp[1]);

            int x, y;
            if(stone_pos == M_PASS){
                Log.d("THSDFSDF", "ENDEND");
            }
            x = stone_pos & 0xFF;
            y = (stone_pos >> 8) & 0xFF;

            PlaceNetStones(x, y, stone_color);

        }
    }
    private void PlaceNetStones(int nX, int nY, byte nColor){

        int nStatus = m_interface.SS_GetStatus();
        if ((nStatus != SS_ST_PLAY) && (nStatus != SS_ST_EDIT)) {
            return;
        }
        //int n_Color = m_interface.SS_GetTurn();

        int[] move = new int[4];
        move[MOVE_X] = nX;
        move[MOVE_Y] = nY;
        move[MOVE_COLOR] = nColor;
        move[MOVE_NUM] = m_nCurNum + 1;
        if (move[MOVE_NUM] >= 400) {
            return;
        }

        m_interface.SS_PutStone(move);
        /*if (false == checkPutStoneResult(m_interface.SS_PutStone(move), true)) {
            return;
        }*/

        //m_nCurNum++;
        m_interface.SS_NextTurn();
        //RefreshLastStone();

        //board.SetLastStonePos(nX, nY);
        board.invalidate();
    }

    private void RefreshLastStone() {
        m_nLastMove[MOVE_COLOR] = SS_NONE;

        if( m_byReviewing == REVIEWING_NONE ) {
            m_interface.SS_GetLastStonePos( m_nLastMove, false );

        } else {
            m_interface.SS_GetLastStonePos( m_nLastMove, true );

        }
    }

    private boolean checkPutStoneResult(int nResult, boolean bMan) {
        if (nResult != IS_OK && nResult != IS_DANSU && nResult != IS_PASS) {
            switch (nResult) {
                case IS_PRESTONE:

                    // MessageBox("すでに石があります。");
                    break;
                case IS_PAEPOS:

                    //if( true == bMan )
                    //        MessageBox("连环劫，需要缓手。");
                    break;
                case IS_COUNTOVER:
                    // MessageBox("これ以上は打ってません。");
                    break;

                default:

                    //if( false == bMan )
                    //  MessageBox(NOTICE_INVALID_PLAY_BOARD, MB_OK, 0, IDD_INVALID_PLAY);
                    break;
            }
            return false;
        }

        return true;
    }

    private void startKibo() {
        m_nCurNum = 0;
        m_setInfo = new PlaySetting();

        m_setInfo.nBlackPlayer = SSInterface.SS_MAN;
        m_setInfo.nWhitePlayer = SSInterface.SS_MAN;
        m_setInfo.nBoardSize = Global.m_nBoardsize_kibo;
        m_setInfo = m_interface.SS_GetPlayInfo();

        if (m_interface.SS_SetPlayInfo(m_setInfo, true) == true) {
            //saveSetInfo();
            //saveOptions();
            //clearKeyQueue();
            //setState( STATE_GAME, GAME_MODE_NEW );
        } else {

        }
        m_interface.SS_Init();

        m_interface.SS_SetStatus(SSInterface.SS_ST_PLAY);



        board.setCellcount(m_nBoardSize);
        board.setInterface(m_interface);
        drawstone();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent){
        switch (keyEvent.getKeyCode()){
            case KeyEvent.KEYCODE_BACK:
                new AlertDialog.Builder(this)
                        .setCancelable(true)
                        .setTitle("정말 기보감상을 끝내겠습니까?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                send_quitKiboRoom();
                            }
                        })
                        .show();

                break;
            default:
                break;
        }
        return false;
    }

    private void send_quitKiboRoom() {
        Intent intent = new Intent(this, KiboListActivity.class);
        startActivity(intent);
        finish();
        //CMessage message = new CMessage(M_CS_RM_QUIT_ROOM);
        //message.writeInt(Global.g_KiboNumber);
        //Global.netManagerFS.send(message);
    }



    // GAME Engine relation constants
    private static final byte SS_ST_NONE = SSInterface.SS_ST_NONE;
    private static final byte SS_ST_REPLAY = SSInterface.SS_ST_REPLAY;
    private static final byte SS_ST_EDIT_TERR = SSInterface.SS_ST_EDIT_TERR;
    private static final byte SS_ST_EDIT = SSInterface.SS_ST_EDIT;
    private static final byte SS_ST_PLAY = SSInterface.SS_ST_PLAY;
    private static final byte SS_ST_AUTOPLAY = SSInterface.SS_ST_AUTOPLAY;
    private static final byte SS_ST_NETPLAY = SSInterface.SS_ST_NETPLAY;
    private static final byte SS_ST_INTERNETPLAY = SSInterface.SS_ST_INTERNETPLAY;
    private static final byte SS_ST_TSUME = SSInterface.SS_ST_TSUME;

    private static final byte SS_ST_WAIT = 0;

    private static final int SS_MAN = SSInterface.SS_MAN;
    private static final int SS_NET = SSInterface.SS_NET;
    private static final int SS_COM = SSInterface.SS_COM;

    private static final int SS_INIT_KIFU = SSInterface.SS_INIT_KIFU;
    private static final int SS_DEL_KIFU = SSInterface.SS_DEL_KIFU;

    private static final byte SS_NONE = SSInterface.SS_NONE;
    private static final byte SS_BLACK = SSInterface.SS_BLACK;
    private static final byte SS_WHITE = SSInterface.SS_WHITE;

    private static final byte IS_PRESTONE = SSInterface.IS_PRESTONE;
    private static final byte IS_OK = SSInterface.IS_OK;
    private static final byte IS_DANSU = SSInterface.IS_DANSU;
    private static final byte IS_PASS = SSInterface.IS_PASS;
    private static final byte IS_PAEPOS = SSInterface.IS_PAEPOS;
    private static final byte IS_COUNTOVER = SSInterface.IS_COUNTOVER;

    private static final int MOVE_NUM = SSInterface.MOVE_NUM;
    private static final int MOVE_COLOR = SSInterface.MOVE_COLOR;
    private static final int MOVE_X = SSInterface.MOVE_X;
    private static final int MOVE_Y = SSInterface.MOVE_Y;

    private static final int SCORE_BLACK = 0;
    private static final int SCORE_WHITE = 1;
    private static final int SCORE_RESULT = 3;
}
