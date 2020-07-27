package spacecon.tob.bclient.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Vector;

import spacecon.tob.bclient.Global;
import spacecon.tob.bclient.R;
import spacecon.tob.bclient.common.GameEndInfo;
import spacecon.tob.bclient.common.PlayerPrimeInfo;
import spacecon.tob.bclient.engine.*;
import spacecon.tob.bclient.common.BettingInfo;
import spacecon.tob.bclient.common.Common;
import spacecon.tob.bclient.common.GameSetting;
import spacecon.tob.bclient.common.RoomData;
import spacecon.tob.bclient.net.CMessage;
import spacecon.tob.bclient.net.IProtocol;


/**
 * Created by HongGukSong on 8/8/2017.
 */

public class GameActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener, View.OnTouchListener ,
        Handler.Callback, IProtocol, Common, RadioGroup.OnCheckedChangeListener, AdapterView.OnItemSelectedListener{

    private int[] spinner_boardsize = {19,17,15,13,11,9,18};
    private int[] spinner_handicap = {0,1,2,3,4,5,6,7,8,9};
    private double[] spinner_dom = {
            0, 0.5,
            1, 1.5,
            2, 2.5,
            3, 3.5,
            4, 4.5,
            5, 5.5,
            6, 6.5,
            7, 7.5,
            8, 8.5,
            9, 9.5,
            10, 10.5};
    private int[] time = {
            0, 1, 5, 10, 15, 20, 30, 40, 50, 60, 90, 120, 180, 300, 480, 0xffff};
    private int[] countingtime = {
            20, 30, 40, 60};
    private int[] counting_num = {
            1, 3, 5
    };

    protected Board board;
    protected Stone stone;
    private boolean m_startflg = false; // game starting
    public float posx, posy;
    public int m_send_posx, m_send_posy;

    //setting data
    private int m_turnColor;



    protected FrameLayout frameLayout_board;
    protected ImageView img_board;
    float m_x = 0.0f, m_y = 0.0f;
    private byte m_byRole;

    private int m_createflg = -1;

    public int RoomNumber;
    public int RoomType;

    public RoomData m_roomData;
    String m_strMyName;
    String m_strOppName;

    Dialog settingDialog;

    private RadioGroup    mRadioGroup_time;
    private RadioGroup    mRadioGroup_first;
    private RadioGroup    mRadioGroup_gametype;

    private Spinner mSpinner_bordertype;
    private Spinner mSpinner_handicap;
    private Spinner mSpinner_compensation;
    private Spinner mSpinner_time;
    private Spinner mSpinner_count;
    private Spinner mSpinner_overtime;


    private GameSetting m_playSetting, tmp_setting;
    private byte myRole;
    private Vector<String> m_rmPlayerList;
    private boolean m_isSettingfirst = false;
    private Vector<BettingInfo> m_vecBettingInfo = null;
    private Canvas m_canvas;
    private PlaySetting m_setInfo;
    private SSInterface m_interface = new SSInterface();
    private int m_nCurNum;
    private boolean m_passflag = false;
    private int my_stone_id = 0x12345678;//                         305419896
    private int opp_stone_id = 0x12345808;//    305419896  + 400 =  305420296

    private int m_nLastMove[] = { 0, SS_NONE, 0, 0 };
    private byte m_byReviewing = REVIEWING_NONE;

    private static final byte REVIEWING_NONE = 0;
    private static final byte REVIEWING_KIFU = 1;
    private static final byte REVIEWING_PUTSTONE = 2;
    private byte m_nRoomstate;


    private short m_nWRemainHaveTime;
    private byte m_nWRemainCountDownNum;
    private short m_nBRemainHaveTime;
    private byte m_nBRemainCountDownNum;
    private byte m_nBRemainCountDownTime;
    private byte m_nWRemainCountDownTime;
    private byte m_nDCColor;
    private short m_nRemainDCTime;
    private int passcount = 0;
    private byte[][] m_byArea = null;
    private byte[][] m_byTerrBoard = null;


    private TextView wplayer, bplayer, game_state, m_time_dlg ;
    private boolean m_changeflag = false;
    private int analizecnt = 0;
    private TextView m_total_time, m_total_time_r,
            m_count_time, m_count_time_r;
    private ImageView live_l1, live_l2, live_l3, live_l4, live_l5,
            live_1, live_2, live_3,  live_4, live_5;
    private AlertDialog alertDialog;
    private CountDownTimer currentCDT = null;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState){
        Handler packetHandler = new Handler(this);
        Global.m_packetHandler = packetHandler;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        frameLayout_board = (FrameLayout) findViewById(R.id.frame_board);
        wplayer = (TextView) findViewById(R.id.textview_wplayer);
        bplayer = (TextView)findViewById(R.id.textview_bplayer);
        game_state = (TextView)findViewById(R.id.game_state);
        m_total_time = (TextView)findViewById(R.id.textView_total_time);
        m_total_time_r = (TextView)findViewById(R.id.textView_total_time_r);
        m_count_time = (TextView)findViewById(R.id.textView_count_time);
        m_count_time_r =  (TextView)findViewById(R.id.textView_count_time_r);

        live_l1 = (ImageView)findViewById(R.id.imageView_l1);
        live_l2 = (ImageView)findViewById(R.id.imageView_l2);
        live_l3 = (ImageView)findViewById(R.id.imageView_l3);
        live_l4 = (ImageView)findViewById(R.id.imageView_l4);
        live_l5 = (ImageView)findViewById(R.id.imageView_l5);

        live_1 = (ImageView)findViewById(R.id.imageView_1);
        live_2 = (ImageView)findViewById(R.id.imageView_2);
        live_3 = (ImageView)findViewById(R.id.imageView_3);
        live_4 = (ImageView)findViewById(R.id.imageView_4);
        live_5 = (ImageView)findViewById(R.id.imageView_5);

        Init();

        darw();
        startwork();
    }

    protected Dialog onCreateDialog(int dialogType) {
        switch (dialogType) {
            case GAME_SETTING_DIALOG:
                show_settingDialog();
                break;
            default:
        }
        return null;
    }

    private void show_settingDialog() {

        settingDialog.setContentView(R.layout.activity_setting);


            mRadioGroup_first = (RadioGroup) settingDialog.findViewById(R.id.choose_first);
            if(m_playSetting.m_usWhitePlayerName == Global.m_gameData.m_userName){
                mRadioGroup_first.check(R.id.white);
            }
            mRadioGroup_gametype = (RadioGroup)settingDialog.findViewById(R.id.game_type);
            mRadioGroup_time = (RadioGroup)settingDialog.findViewById(R.id.time_type);
            mRadioGroup_first.setOnCheckedChangeListener(this);
            mRadioGroup_gametype.setOnCheckedChangeListener(this);
            mRadioGroup_time.setOnCheckedChangeListener(this);

            ArrayAdapter<CharSequence> adapter;
            /* bordertype*/
            mSpinner_bordertype = (Spinner) settingDialog.findViewById(R.id.spinner_bordertype);
            adapter = ArrayAdapter.createFromResource(this, R.array.Bordertype,
                    android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpinner_bordertype.setAdapter(adapter);
            for(int i1 = 0; i1 < spinner_boardsize.length; i1 ++){
                if(spinner_boardsize[i1] == m_playSetting.m_byBoardSize)
                    mSpinner_bordertype.setSelection(i1);
            }

            mSpinner_bordertype.setOnItemSelectedListener(this);
            /*handicap*/
            mSpinner_handicap = (Spinner)settingDialog.findViewById(R.id.spinner_handicap);
            adapter = ArrayAdapter.createFromResource(this, R.array.Handcip,
                    android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpinner_handicap.setAdapter(adapter);
            for(int i = 0; i < spinner_handicap.length; i ++){
                if(spinner_handicap[i] == m_playSetting.m_byHandicap)
                    mSpinner_handicap.setSelection(i);
            }

            mSpinner_handicap.setOnItemSelectedListener(this);
            /*compensation*/
            mSpinner_compensation = (Spinner) settingDialog.findViewById(R.id.spinner_compensation);
            adapter = ArrayAdapter.createFromResource(this, R.array.Compensation,
                    android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpinner_compensation.setAdapter(adapter);
            for(int i2 = 0; i2 < spinner_dom.length; i2 ++){
                if(spinner_dom[i2] == m_playSetting.m_byDom)
                    mSpinner_compensation.setSelection(i2);
            }
            mSpinner_compensation.setOnItemSelectedListener(this);
         /*time*/
        mSpinner_time= (Spinner) settingDialog.findViewById(R.id.spinner_time);
        adapter = ArrayAdapter.createFromResource(this, R.array.Main_time,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner_time.setAdapter(adapter);
        for(int i3 = 0; i3 < time.length; i3 ++) {
            if (time[i3] == m_playSetting.m_wHaveTime / 60) {
                mSpinner_time.setSelection(i3);
            }
        }
        mSpinner_time.setOnItemSelectedListener(this);
            /*overtime*/
            mSpinner_overtime = (Spinner) settingDialog.findViewById(R.id.spinner_overtime);
            adapter = ArrayAdapter.createFromResource(this, R.array.Counting_time,
                    android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpinner_overtime.setAdapter(adapter);
            for(int i4 = 0; i4 < countingtime.length; i4 ++) {
                if (countingtime[i4] == m_playSetting.m_byCountDownTime) {
                    mSpinner_overtime.setSelection(i4);
                }
            }
            mSpinner_overtime.setOnItemSelectedListener(this);
            /*count*/
            mSpinner_count = (Spinner) settingDialog.findViewById(R.id.spinner_count);
            adapter = ArrayAdapter.createFromResource(this, R.array.Counting_count,
                    android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpinner_count.setAdapter(adapter);
            for(int i5 = 0; i5 < counting_num.length; i5 ++) {
                if (counting_num[i5] == m_playSetting.m_byCountDownNum) {
                    mSpinner_count.setSelection(i5);
                }
            }

            mSpinner_count.setOnItemSelectedListener(this);


        if(m_playSetting.m_usBlackPlayerName.equals(Global.m_gameData.m_userName)){
            m_turnColor = BLACK;
            m_startflg = true;
            mRadioGroup_first.check(R.id.black);
        }else if(m_playSetting.m_usWhitePlayerName.equals(Global.m_gameData.m_userName)){
            m_turnColor = WHITE;
            mRadioGroup_first.check(R.id.white);
        }else{
            m_turnColor = NONE;
        }
        if(m_playSetting.m_byType == RT_FRIENDSHIPGAME){
            mRadioGroup_gametype.check(R.id.friendship_game);
        }else if(m_playSetting.m_byType == RT_LEVELINGGAME){
            mRadioGroup_gametype.check(R.id.leveling_game);
        }

        settingDialog.setTitle(Global.m_gameData.m_userName + "  " +  "VS" +  "  " + Global.m_gameData.m_usOppName);
        settingDialog.setCancelable(false);
        settingDialog.show();
    }
    public void Game_Pass(View view){
        if(m_startflg){
            m_passflag = true;
            sendPutStone(-1, 0);
            passcount ++;
            if(passcount == 2){
                SEND_ESTIMATE(RES_ACCEPT);
            }
        }

    }
    public void Game_Analize(View view){
        analizecnt ++;
        if(analizecnt % 2 == 0) {
            m_interface.SS_GetTerritory(m_byArea);
            m_interface.SS_SetStatus(SS_ST_EDIT_TERR);
            m_interface.SS_GetBoard(m_byTerrBoard);
            board.byArea = m_byArea;
            board.byTerrBoard = m_byTerrBoard;
            board.AnalizeFlag = true;
        }
        else {
            m_interface.SS_SetStatus(SS_ST_PLAY);
            board.EndFlag = false;
        }
        board.invalidate();
    }

    public void Game_Retract(View view){

        CMessage msgout = new CMessage(M_CS_GM_RETRACT_REQ);
        msgout.writeInt(RoomNumber);
        Global.netManagerFS.send(msgout);
    }
    public void Game_Counting_Home(View view){
       m_startflg = false;
        m_interface.SS_GetTerritory(m_byArea);

        CMessage msgout = new CMessage(M_CS_GM_ESTIMATE_REQ);
        msgout.writeInt(RoomNumber);
        Global.netManagerFS.send(msgout);
    }
    public void Resign(View view){
        m_startflg = false;
        CMessage msgout = new CMessage(M_CS_GM_THROW);

        msgout.writeInt(RoomNumber);

        Global.netManagerFS.send(msgout);
    }
    public void Accept(View view){

        if(Global.m_bCreateRoom == 0)//
        {
            m_startflg = true;
            sendAcceptSetting(GSET_OK);

        }else if(Global.m_bCreateRoom == 1) {
            sendAcceptSetting(GSET_CANCEL);
        }
        //sendAcceptSetting(GSET_NONE);
        settingDialog.hide();
        startGame();


        wplayer.setText(m_playSetting.m_usWhitePlayerName);
        bplayer.setText(m_playSetting.m_usBlackPlayerName);


    }

    private void startGame() {
        m_nCurNum = 0;
        m_setInfo = new PlaySetting();

        m_setInfo.nBlackPlayer = SSInterface.SS_MAN;
        m_setInfo.nWhitePlayer = SSInterface.SS_MAN;
        m_setInfo.nBoardSize = (byte)m_playSetting.m_byBoardSize;//(byte) nBoardType;
        /*board redraw*/
        board.setCellcount(m_setInfo.nBoardSize);
        board.invalidate();
        m_setInfo.nComLevel = 0;

        m_setInfo.nHandicap = (byte)m_playSetting.m_byHandicap;


        if (m_interface.SS_SetPlayInfo(m_setInfo, true) == true) {
            //saveSetInfo();
            //saveOptions();
            //clearKeyQueue();
            //setState( STATE_GAME, GAME_MODE_NEW );
        } else {

        }

        m_setInfo = null;
        m_setInfo = m_interface.SS_GetPlayInfo();
        m_interface.SS_SetStatus( SS_ST_PLAY );

        m_byArea = new byte[m_setInfo.nBoardSize][m_setInfo.nBoardSize];
        m_byTerrBoard = new byte[m_setInfo.nBoardSize][m_setInfo.nBoardSize];
        board.setCellcount(m_setInfo.nBoardSize);
        board.setInterface(m_interface);
        m_nBRemainCountDownNum = m_playSetting.m_byCountDownNum;
        m_nWRemainCountDownNum = m_playSetting.m_byCountDownNum;
        display_star();
        //
    }

    private void sendAcceptSetting(byte res) {
        copy_setting();
        switch (res){
            case GSET_OK:
                Send_GM_SETTING_REQ();
                if(m_playSetting.m_usBlackPlayerName.equals(Global.m_gameData.m_userName)){
                    m_turnColor = BLACK;
                    m_startflg = true;
                }else if(m_playSetting.m_usWhitePlayerName.equals(Global.m_gameData.m_userName)){
                    m_turnColor = WHITE;
                }else{
                    m_turnColor = NONE;
                }
                break;
            case GSET_NONE:
            case GSET_CANCEL:
                changeValue();
                if(m_changeflag == true) {
                    CMessage msgout = new CMessage(M_CS_GM_SETRES);
                    msgout.writeInt(RoomNumber);
                    msgout.writeUCString(Global.m_gameData.m_usOppName);
                    msgout.writeByte(RES_ACCEPT);
                    Global.netManagerFS.send(msgout);
                } else{
                   Send_GM_SETTING_REQ();
                }
                break;
            default:
        }

    }

    private void changeValue() {
        if(tmp_setting.m_usBlackPlayerName == m_playSetting.m_usBlackPlayerName){
            if(tmp_setting.m_usWhitePlayerName == m_playSetting.m_usWhitePlayerName){
                if(tmp_setting.m_byBoardSize == m_playSetting.m_byBoardSize){
                    if(tmp_setting.m_byHandicap == m_playSetting.m_byHandicap){
                        if(tmp_setting.m_byDom == m_playSetting.m_byDom){
                            if(tmp_setting.m_wHaveTime == m_playSetting.m_wHaveTime){
                                if(tmp_setting.m_byCountDownTime == m_playSetting.m_byCountDownTime){
                                    if(tmp_setting.m_byCountDownNum == m_playSetting.m_byCountDownNum){
                                        m_changeflag = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void Init() {
        board = new Board(this, frameLayout_board);
        board.SetLastStonePos(-1, -1);
        settingDialog = new Dialog(this);
        m_roomData = new RoomData();
        m_playSetting = new GameSetting();
        tmp_setting = new GameSetting();
        m_rmPlayerList = new Vector<String>();

        //init variable
        m_nRoomstate = RS_PREPARE;
        m_strMyName = Global.m_gameData.m_userName;
        m_strOppName = Global.m_gameData.m_usOppName;
        RoomNumber = Global.m_shrRoomNum;
        RoomType = Global.m_bType;
        if(!Global.m_reEnter)
            setMyRole(Global.m_nRole);

        if(Global.m_bCreateRoom == 0){
            //your create  -- master
            m_createflg = 0;
            m_rmPlayerList = Global.m_GamerList;
            Global.m_GamerList.removeAllElements();
            setPlayers(Global.m_gameData.m_userName, Global.m_gameData.m_usOppName);
        }

    }

    private void setPlayers(String m_userName, String m_usOppName) {
        m_roomData.m_usMasterName = m_userName;
        //m_playSetting.m_usWhitePlayerName = m_userName;
        //m_playSetting.m_usBlackPlayerName = m_usOppName;
        m_roomData.m_usBlackPlayerName = m_userName;
        m_roomData.m_usWhitePlayerName = m_usOppName;
    }

    private void startwork() {

        switch (getMyRole()) {//방장인가 아닌가 검사
            case GR_GAMER:
            case GR_OBSERVER:
                m_isSettingfirst = false;
                requestRoomInfo();
                Toast.makeText(this,"동지는 Gamer , OBSER",Toast.LENGTH_LONG).show();
                break;
            case GR_MASTER:
                m_isSettingfirst = true;
                if(m_nRoomstate == RS_PREPARE){
                    m_playSetting.m_usWhitePlayerName = m_roomData.m_usWhitePlayerName;
                    m_playSetting.m_usBlackPlayerName = m_roomData.m_usBlackPlayerName;
                }
                Toast.makeText(this,"동지는 Master",Toast.LENGTH_LONG).show();
                requestRoomInfo();
                showdialog_setting();

                break;
            default:
        }

    }

    private void showdialog_setting() {
        showDialog(GAME_SETTING_DIALOG);
    }

    private void requestRoomInfo() {
        CMessage msg = new CMessage(M_CS_RM_ROOMINFO_REQ);
        Log.d("     ==RoomNum==     ", String.valueOf(RoomNumber));
        msg.writeInt(RoomNumber);
        Global.netManagerFS.send(msg);
    }

    private void darw() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //img_board = (ImageView)findViewById(R.id.img_board);
        frameLayout_board.addView(board);
        frameLayout_board.setOnTouchListener(this);



        startTime();
        //Button button_pass = (Button)findViewById(R.id.button_pass);
        //button_pass.setText("통 과 ");
    }
    private void display_star(){

        live_1.setVisibility(View.VISIBLE);
        live_2.setVisibility(View.VISIBLE);
        live_3.setVisibility(View.VISIBLE);
        live_4.setVisibility(View.VISIBLE);
        live_5.setVisibility(View.VISIBLE);

        live_l1.setVisibility(View.VISIBLE);
        live_l2.setVisibility(View.VISIBLE);
        live_l3.setVisibility(View.VISIBLE);
        live_l4.setVisibility(View.VISIBLE);
        live_l5.setVisibility(View.VISIBLE);

        switch (m_nBRemainCountDownNum){
            case 0:
                live_1.setVisibility(View.INVISIBLE);
            case 1:
                live_2.setVisibility(View.INVISIBLE);
            case 2:
                live_3.setVisibility(View.INVISIBLE);
            case 3:
                live_4.setVisibility(View.INVISIBLE);
            case 4:
                live_5.setVisibility(View.INVISIBLE);
        }
        switch (m_nWRemainCountDownNum){
            case 0:
                live_l1.setVisibility(View.INVISIBLE);
            case 1:
                live_l2.setVisibility(View.INVISIBLE);
            case 2:
                live_l3.setVisibility(View.INVISIBLE);
            case 3:
                live_l4.setVisibility(View.INVISIBLE);
            case 4:
                live_l5.setVisibility(View.INVISIBLE);
        }
    }

    private void startTime() {
        new CountDownTimer(Long.MAX_VALUE, 1000) {

            public void onTick(long millisUntilFinished) {
                if(!m_interface.SS_IsGameEnd())
                    updatime();
            }

            @Override
            public void onFinish() {

            }

        }.start();
    }

    private void updatime() {

        if (m_interface.SS_GetTurn() == WHITE) {
            if(m_nWRemainHaveTime > 1) {
                m_nWRemainHaveTime--;
            }else
            {
                if (m_nWRemainCountDownTime > 1)
                {
                    m_nWRemainCountDownTime--;
                }
            }

        } else if (m_interface.SS_GetTurn() == BLACK) {
            if(m_nBRemainHaveTime > 1) {
                m_nBRemainHaveTime--;
            }
            else {
                if (m_nBRemainCountDownTime > 1)
                {
                    m_nBRemainCountDownTime--;
                }
            }
        }

        //draw update Time message
        int sec = (int) m_nWRemainHaveTime;
        int hour = (int) sec / 3600;
        int min = (sec % 3600) / 60;
        sec = sec % 60;
        m_total_time.setText("" + (hour > 0 ? hour + ":" : "") + min + ":" + sec);

        int sec_r = (int) m_nBRemainHaveTime;
        int hour_r = (int) sec_r / 3600;
        int min_r = (sec_r % 3600) / 60;
        sec_r = sec_r % 60;
        m_total_time_r.setText("" + (hour_r > 0 ? hour_r + ":" : "") + min_r + ":" + sec_r);

        int sec1 = (int) m_nBRemainCountDownTime;
        int hour1 = (int) sec1 / 3600;
        int min1 = (sec1% 3600) / 60;
        sec1 = sec1 % 60;
        m_count_time_r.setText("" + (hour1 > 0 ? hour1 + ":" : "") + min1 + ":" + sec1);


        int sec12 = (int) m_nWRemainCountDownTime;
        int hour12 = (int) sec12 / 3600;
        int min12 = (sec12% 3600) / 60;
        sec12 = sec12 % 60;
        m_count_time.setText("" + (hour12 > 0 ? hour12 + ":" : "") + min12 + ":" + sec12);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Log.d("heyheyheyhey:", String.valueOf(view.getWidth()));
        Log.d("heyheyheyhey:", String.valueOf(view.getHeight()));
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    float touchx, touchy;
                    touchx = motionEvent.getX();
                    touchy = motionEvent.getY();
                    Log.d("touchX:", String.valueOf(touchx));
                    Log.d("touchY:", String.valueOf(touchy));
                    if(m_startflg){

                        if(calculation(touchx, touchy) == true) {
                            //stone = new Stone(this, frameLayout_board);
                            //my_stone_id = my_stone_id + 1;
                            //stone.setId(my_stone_id);
                            //Log.d("-----Stone_id----:", String.valueOf(my_stone_id - 305419896));
                            sendPutStone(m_send_posx, m_send_posy);

                        }

                    }
                    break;
                default:
            }
        return false;
    }
    private boolean calculation(float touchx, float touchy) {
        if (touchx > (Global.m_cp.x - Global.m_bordersize_2) && touchx < (Global.m_cp.x + Global.m_bordersize_2)){
            if (touchy > (Global.m_cp.y - Global.m_bordersize_2) && touchy < (Global.m_cp.y + Global.m_bordersize_2)) {
                float realx = touchx - (Global.m_cp.x - Global.m_bordersize_2 - Global.m_stone_radius);
                float realy = touchy - (Global.m_cp.y - Global.m_bordersize_2 - Global.m_stone_radius);
                m_send_posx = Math.round(realx / (Global.m_stone_radius * 2)) - 1  ;

                m_send_posy = Math.round(realy / (Global.m_stone_radius * 2)) - 1 ;
                posxy(m_send_posx, m_send_posy);
                return true;
            }
        }
        return false;
    }

    private void sendPutStone(int nX, int nY) {

        int nStatus = m_interface.SS_GetStatus();
        if ((nStatus != SS_ST_PLAY) && (nStatus != SS_ST_EDIT)) {
            return;
        }
        int nColor = m_interface.SS_GetTurn();

        int[] move = new int[4];
        move[MOVE_X] = nX;
        move[MOVE_Y] = nY;
        move[MOVE_COLOR] = nColor;
        move[MOVE_NUM] = m_nCurNum + 1;

        if (move[MOVE_NUM] >= 400) {
            return;
        }

        if (false == checkPutStoneResult(m_interface.SS_PutStone(move), true)) {
            return;
        }

        //RefreshLastStone();

        m_nCurNum++;
        m_interface.SS_NextTurn();
        if (m_interface.SS_IsGameEnd()) {
            ProcGameOver();
            return;
        }

        if(m_startflg) {

            short wPos = 0;
            if(m_passflag == true){
                wPos = M_PASS;
                board.invalidate();
                m_passflag = false;
            }else {
                passcount = 0;
                wPos = (short) (m_send_posy << 8 | m_send_posx);
                board.SetLastStonePos(m_send_posx, m_send_posy);
                board.invalidate();
                //drawstone(m_turnColor);
            }
            SEND_PUT_STONE(wPos, (byte) m_turnColor);
            m_startflg = false;
        }
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

    private void drawstone(int stoneColor) {
        stone.setpos(posx, posy , stoneColor);
    }

    private void SEND_PUT_STONE(short wPos,  byte stone_color) {
        if(getMyRole() == GR_OBSERVER){
            return;
        }
        CMessage msg = new CMessage(M_CS_GM_PUTSTONE);
        Log.d("HGSHGSHGSHGSHGSHGS", String.valueOf(wPos));
        msg.writeInt(RoomNumber);//by hgs 2017.9.18
        msg.writeByte(stone_color);
        msg.writeShort(wPos);
        Global.netManagerFS.send(msg);
    }

    private void posxy(int x, int y){
        float realx = Global.m_cp.x - Global.m_bordersize_2 - Global.m_stone_radius;
        float realy = Global.m_cp.y - Global.m_bordersize_2 - Global.m_stone_radius;
        posx = realx + Global.m_stone_radius * 2 * (x + 1);
        posy = realy + Global.m_stone_radius * 2 * (y + 1);
    }


    @Override
    public boolean handleMessage(Message message) {
        onMessage((CMessage) message.obj);
            return false;
    }

    private void onMessage(CMessage message) {
        int nHeader = message.getHeader();
        Log.d("      ==Receive==     ", Integer.toHexString(message.getHeader()));
        switch (nHeader){
            case M_INTERFACE_DELSTONE:
                on_IN_DEL_STONE(message);
                break;
            case M_SC_RM_ROOMINFO_RES:
                on_SC_RM_ROOMINFO_RES(message);
                break;
            case M_SC_RM_BETLIST_RES:
                On_SC_RM_BETLIST_RES(message);
                break;
            case M_SC_GM_GAMEINFO_RES:
                on_SC_GM_GAMEINFO_RES(message);
                break;
            case M_SC_GM_SETTING:
                on_SC_GM_SETTING(message);
                break;
            case M_SC_GM_SETRES:
                on_SC_GM_SETRES(message);
                break;
            case M_SC_GM_PUTSTONE:
                on_SC_GM_PUTSONE(message);
                break;
            case M_SC_GM_RETRACT_REQ: //물리기 신청
                on_SC_GM_RETRACT_REQ(message);
                break;
            case M_SC_GM_RETRACT_RES:
                on_SC_GM_RETRACT_RES(message);
                break;
            case M_SC_GM_ESTIMATE_RES:
                on_SC_GM_ESTIMATE_RES(message);
                break;
            case M_SC_RM_QUIT_ROOM:
                on_SC_RM_QUIT_ROOM(message);
                break;
            case M_SC_GM_ESTIMATE_REQ:
                on_SC_GM_ESTIMATE_REQ(message);
                break;
            case M_SC_GM_END:
                on_SC_GM_END(message);
                break;
            case M_SC_GM_COUNTDOWN:
                on_SC_GM_COUNTDOWN(message);
                break;
            case M_SC_GM_HAVETIMEOVER:
                on_SC_GM_HAVETIMEOVER(message);
                break;
            case M_SC_GM_GAMER_DISCONNECTED:
                Log.d("asdfasdf", " asdf");
                on_SC_GM_GAMER_DISCONNECTED(message);
                break;
            case M_SC_GM_GAMER_REENTER:
                on_SC_GM_GAMER_REENTER(message);
                break;
            default:
        }
    }

    private void on_SC_GM_GAMER_REENTER(CMessage message) {


        short roomNo = message.readShort();
        m_nRemainDCTime = 0;
        m_nDCColor = SS_NONE;
        alertDialog.hide();
    }

    private void on_SC_GM_COUNTDOWN(CMessage message) {
        int nRoomNum = message.readInt();
        Byte ncolor = message.readByte();
        Byte nCount = message.readByte();
        if (ncolor == COL_BLACK)
        {
            m_nBRemainCountDownTime = m_playSetting.m_byCountDownTime;
            m_nBRemainCountDownNum = nCount;

        }
        else
        {
            m_nWRemainCountDownTime = m_playSetting.m_byCountDownTime;
            m_nWRemainCountDownNum = nCount;
        }
        display_star();
    }

    private void on_SC_GM_HAVETIMEOVER(CMessage message) {
        int nRoomNum = message.readInt();
        Byte ncolor = message.readByte();
        if (ncolor == COL_BLACK)
        {
            m_nBRemainHaveTime = 0;
            m_nBRemainCountDownTime = m_playSetting.m_byCountDownTime;
        }
        else
        {
            m_nWRemainHaveTime = 0;
            m_nWRemainCountDownTime = m_playSetting.m_byCountDownTime;
        }
    }

    public void on_SC_GM_GAMER_DISCONNECTED(CMessage message){

        short roomNo = message.readShort();
        byte byDisconPlayColor = message.readByte();
        m_nRemainDCTime = message.readShort();
        m_nDCColor = byDisconPlayColor;
        //////////////////////////////////////////////////////////////////////////////////////////////
        //m_time_dlg = (TextView) findViewById(R.id.textView_time);

        alertDialog = new AlertDialog.Builder(this)
                .setView(R.layout.time_dlg)
                 .setCancelable(false)
                .show();
        m_time_dlg = (TextView)alertDialog.findViewById(R.id.textView_time);
        if(currentCDT != null){
            currentCDT.cancel();
        }
        currentCDT = new CountDownTimer(300000, 1000) { // 5min

            public void onTick(long millisUntilFinished) {
                int sec = (int) millisUntilFinished / 1000;
                int hour = (int)sec / 3600;
                int min = (sec % 3600) / 60;
                sec = sec % 60;
                m_time_dlg.setText("" + (hour>0?hour + ":":"") + min + ":" + sec );
            }

            public void onFinish() {
                m_time_dlg.setText("done!");
                alertDialog.hide();
                Toast.makeText(GameActivity.this, "Game Ended", Toast.LENGTH_SHORT).show();
            }
        }.start();

        //////////////////////////////////////////////////////////////////////////////////////////////
    }

    private void on_SC_GM_END(CMessage message) {
        m_startflg = false;
        int wRoomNo = message.readInt();
        byte res = message.readByte();
        byte byWinColor = 0;
        GameEndInfo endinfo = new GameEndInfo();

        if (res == GMEND_CALC) {
            endinfo.readFrom(message);
            m_interface.SS_GetTerritory(m_byArea);
            //String s = getScoreMsg();
            m_interface.SS_SetStatus(SS_ST_EDIT_TERR);
            m_interface.SS_SetGameEnd();
            board.byArea = m_byArea;
            board.byTerrBoard = m_byTerrBoard;
            board.EndFlag = true;
            board.invalidate();
        }
    }

    private void on_SC_GM_ESTIMATE_REQ(CMessage message) {
        int nRoomNum = message.readInt();
        String reqScore = message.readUCString();
        //
    }

    private void on_SC_RM_QUIT_ROOM(CMessage message) {
        Intent intent = new Intent(this, RoomActivity.class);
        startActivity(intent);
        finish();
    }

    private void on_SC_GM_ESTIMATE_RES(CMessage msg) {
        int nRoomNum;
        byte nRes;
        nRoomNum = msg.readInt();
        nRes = msg.readByte();

        if (nRes == RES_ACCEPT) {
            Toast.makeText(this, "GAMEENDEEEEEENNNNNDDD", Toast.LENGTH_SHORT).show();
            ProcGameOver();
            board.EndFlag = true;
            board.invalidate();
        } else if (nRes == RES_REJECT) {

        }
    }

    private void on_IN_DEL_STONE(CMessage message) {
        short deadone = message.readShort();
        int x, y;
        x = m_interface.LOBYTE(deadone) - 1;
        y = m_interface.HIBYTE(deadone) - 1;

    }

    private void on_SC_GM_RETRACT_RES(CMessage message) {

        int nRoomNum;
        byte nRes;
        nRoomNum = message.readInt();
        nRes = message.readByte();

        if (nRes == RES_ACCEPT) {
                OnRetract();
                Toast.makeText(this, "성공", Toast.LENGTH_LONG).show();

        } else if (nRes == RES_REJECT) {
            Toast.makeText(this, "실패", Toast.LENGTH_LONG).show();
        }

    }

    private boolean OnRetract() {
            // todo : test tool
        if (m_interface.SS_GetKifuSize() < 2) {
            //MessageBox(CONFIRM_MATA2);
            return false;
        }
        int nMataCount = 2;
        // todo : test tool
//        if( m_nMode == GAME_MODE_AUTO_KIFU )
//            nMataCount = 1;
        if ((m_setInfo.nBlackPlayer == SS_MAN) &&
                (m_setInfo.nWhitePlayer == SS_MAN)) {
            nMataCount = 2;
        }
        m_interface.SS_Replay_Add( -nMataCount);
        m_interface.SS_InitMoveKifu(SS_DEL_KIFU);
        m_nCurNum -= nMataCount;
        //ChangeCursor();
        board.invalidate();

        RefreshLastStone();

        return true;
    }

    private void on_SC_GM_RETRACT_REQ(CMessage message) {
        new AlertDialog.Builder(this)
                .setNegativeButton("ACCEPT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("Tag:", "this is the accept");
                        SendResRetract(RES_ACCEPT);
                    }
                })
                .setPositiveButton("REJECT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("Tag:", "this is the reject");
                        SendResRetract(RES_REJECT);
                    }
                })
                .setCancelable(false)
                .setTitle(Global.m_gameData.m_usOppName + "에게서 물리기신청이 들어왔습니다.")
                .show();
    }

    public void SendResRetract(byte nRes) {
        CMessage msgin = new CMessage(M_CS_GM_RETRACT_RES);
        msgin.writeInt(RoomNumber);
        msgin.writeByte(nRes);
        Global.netManagerFS.send(msgin);
    }
    private void on_SC_GM_PUTSONE(CMessage message) {

        int wRoomNo = message.readInt();
        byte byColor = message.readByte();
        short wPos = message.readShort();
        int n = message.readInt();
        Short havetime = message.readShort();

        if(byColor == BLACK){
            m_nBRemainHaveTime = havetime;
            if(havetime == 0){
                m_nBRemainCountDownTime = m_playSetting.m_byCountDownTime;
            }
        }else if(byColor == WHITE){
            m_nWRemainHaveTime = havetime;
            if(havetime == 0){
                m_nWRemainCountDownTime = m_playSetting.m_byCountDownTime;
            }
        }
        int x, y;
        if(wPos == M_PASS){
            Log.d("THSDFSDF", "ENDEND");
        }
        x = wPos & 0xFF;
        y = (wPos >> 8) & 0xFF;
        //posxy(x, y);

        if(byColor != m_turnColor) {
            //stone = new Stone(this, frameLayout_board);
            //opp_stone_id = opp_stone_id + 1;
            //stone.setId(opp_stone_id);
            //Log.d("-----opp_Stone_id----:", String.valueOf(opp_stone_id - 305419896));
            //drawstone(byColor);
            //board.SetLastStonePos(x, y);
            //board.invalidate();
            if(m_nRoomstate == RS_PREPARE){
                m_startflg = true;
            }

            PlaceNetStones(wPos);
        }
    }

    private void PlaceNetStones(short wPos) {

        int nColor = m_interface.SS_GetTurn();
        int nStatus = m_interface.SS_GetStatus();
        if ((nStatus != SS_ST_PLAY) && (nStatus != SS_ST_EDIT) && (nStatus != SS_ST_EDIT_TERR)) {
            return;
        }

        int[] move = new int[4];
        int nX = 0, nY = 0;
        if (wPos == M_PASS) {
            passcount ++;
            if(passcount == 2){
                SEND_ESTIMATE(RES_ACCEPT);
                return;
            }
            nX = -1;
            nY = -1;
        } else {
            passcount = 0;
            nX = wPos & 0xFF;
            nY = (wPos >> 8) & 0xFF;
        }
        move[MOVE_X] = nX;
        move[MOVE_Y] = nY;
        move[MOVE_COLOR] = nColor;
        move[MOVE_NUM] = m_nCurNum + 1;

        //     if (move[MOVE_COLOR] != nColor || move[MOVE_NUM] != m_nCurNum + 1) {
        // ASSERT
        //   }

        int nRet = m_interface.SS_PutStone(move);
        if (false == checkPutStoneResult(nRet, false)) {
            return;
        }
        m_nCurNum++;
        m_interface.SS_NextTurn();
        RefreshLastStone();

        if (m_interface.SS_IsGameEnd()) {
            ProcGameOver();
            return;
        }
        //board.SetLastStonePos(nX, nY);
        board.invalidate();
    }

    private void SEND_ESTIMATE(byte resAccept) {
        CMessage msgout = new CMessage(M_CS_GM_ESTIMATE_RES);
        msgout.writeInt(RoomNumber);
        msgout.writeByte(resAccept);
        Global.netManagerFS.send(msgout);
    }


    private void on_SC_GM_SETRES(CMessage message) {
         int roomNum = message.readInt();
        String oppName = message.readUCString();
        byte byRes = message.readByte();
        changeValue();
        if(m_changeflag == false) {
            Send_GM_SETTING_REQ();
        } else{
            startGame();
        }
    }

    private void on_SC_GM_GAMEINFO_RES(CMessage message) {

        int m_wRoomNo = (short)message.readInt();
        m_playSetting.readFrom(message);
        copy_setting();
        m_setInfo = new PlaySetting();
        m_setInfo.nBlackPlayer = SSInterface.SS_MAN;
        m_setInfo.nWhitePlayer = SSInterface.SS_MAN;
        m_setInfo.nBoardSize = (byte)m_playSetting.m_byBoardSize;//(byte) nBoardType;
        board.setCellcount(m_playSetting.m_byBoardSize);
        m_setInfo.nComLevel = 0;
        m_setInfo.nHandicap = (byte)m_playSetting.m_byHandicap;
        m_interface.SS_SetStatus(SS_ST_PLAY);
        board.setInterface(m_interface);
        if (m_interface.SS_SetPlayInfo(m_setInfo, true) == true) {
            //saveSetInfo();
            //saveOptions();
            //clearKeyQueue();
            //setState( STATE_GAME, GAME_MODE_NEW );
        } else {

        }


        /*this is the level display*/
        InitTime();
        short seconds;
        m_nWRemainHaveTime = message.readShort();
        m_nWRemainCountDownNum = message.readByte();
        m_nBRemainHaveTime = message.readShort();
        m_nBRemainCountDownNum = message.readByte();
        seconds = message.readShort();

        m_nDCColor = message.readByte();

        if (m_nDCColor != COL_NONE) {
            m_nRemainDCTime = message.readShort();
        }
        short nKiboCount;

        nKiboCount = message.readShort();

        for(int i = 0; i < nKiboCount; i ++){
            byte ncolor = message.readByte();
            short wPos = message.readShort();
            String desc = message.readUCString();
            int x =-1, y = -1;

            if(wPos == M_PASS){
                Log.d("THSDFSDF", "ENDEND");
            }
            x = wPos & 0xFF;
            y = (wPos >> 8) & 0xFF;

            PlaceNetStones(x, y, ncolor);
            //posxy(x, y);
            //stone = new Stone(this, frameLayout_board);
            //opp_stone_id = opp_stone_id + 1;
            //stone.setId(opp_stone_id);
            //Log.d("-----opp_Stone_id----:", String.valueOf(opp_stone_id - 305419896));
            //drawstone(ncolor);
            //m_startflg = true;
            board.SetLastStonePos(x, y);
            board.invalidate();
        }


        /*if(m_playSetting.m_usBlackPlayerName.equals(Global.m_gameData.m_userName)){
            m_turnColor = BLACK;
            //m_startflg = true;
        }else if(m_playSetting.m_usWhitePlayerName.equals(Global.m_gameData.m_userName)){
            m_turnColor = WHITE;
        }else{
            m_turnColor = NONE;
        }*/

        /*short seconds;
        m_nWRemainHaveTime = message.readShort();
        m_nWRemainCountDownNum = message.readByte();
        m_nBRemainHaveTime = message.readShort();
        m_nBRemainCountDownNum = message.readByte();*/

    }

    private void copy_setting() {
        tmp_setting.m_byBoardSize = m_playSetting.m_byBoardSize;
        tmp_setting.m_wHaveTime = m_playSetting.m_wHaveTime;
        tmp_setting.m_byCountDownNum = m_playSetting.m_byCountDownNum;
        tmp_setting.m_byCountDownTime = m_playSetting.m_byCountDownTime;
        tmp_setting.m_usBlackPlayerName = m_playSetting.m_usBlackPlayerName;
        tmp_setting.m_usWhitePlayerName = m_playSetting.m_usWhitePlayerName;
        tmp_setting.m_byHandicap = m_playSetting.m_byHandicap;
        tmp_setting.m_byDom = m_playSetting.m_byDom;

    }

    private void PlaceNetStones(int nX, int nY, byte nColor){

        int nStatus = m_interface.SS_GetStatus();
        if ((nStatus != SS_ST_PLAY) && (nStatus != SS_ST_EDIT)) {
            return;
        }
        int n_Color = m_interface.SS_GetTurn();

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
        RefreshLastStone();
        m_nCurNum++;
        m_interface.SS_NextTurn();


    }
    private void InitTime() {
        m_nBRemainHaveTime = m_playSetting.m_wHaveTime;
        m_nWRemainHaveTime = m_playSetting.m_wHaveTime;
        m_nBRemainCountDownTime = m_playSetting.m_byCountDownTime;
        m_nWRemainCountDownTime = m_playSetting.m_byCountDownTime;
        m_nBRemainCountDownNum = m_playSetting.m_byCountDownNum;
        m_nWRemainCountDownNum = m_playSetting.m_byCountDownNum;
    }

    private void On_SC_RM_BETLIST_RES(CMessage msgin) {
        //short nRoomNum = (short) msgin.readInt();
        //m_vecBettingInfo.clear();
        //AddBettingList( msg );
        if (m_nRoomstate != RS_PREPARE) {
            Log.d("heyheyheyhey:","djdjdjd");
            Send_GM_GAMEINFO_REQ();
            //Send_GM_SETTING_REQ();
        }

    }

    private void on_SC_GM_SETTING(CMessage msgin) {
        int nRoomNum =  msgin.readInt();
        Log.d("Tag", "" + RoomNumber);
        copy_setting();
        m_playSetting.readFrom(msgin);
        receiveGameSetting();
    }

    private void on_SC_RM_ROOMINFO_RES(CMessage msgin) {

        int wRoomid = msgin.readInt();
        m_roomData.readFrom(msgin);
        updateRoomState();
        for (int i = 0; i < m_roomData.m_wCountOfObservers; i++) {
            String usName = msgin.readUCString();
            m_rmPlayerList.addElement(usName);
        }
        if(m_roomData.m_byState != RS_PREPARE){
            Send_GM_GAMEINFO_REQ();
        }

        /*
        short wRoomId = msgin.readShort();
        //Log.d("     ++++RoomdId ++++    ", String.valueOf(wRoomId));
        m_roomData.readFrom(msgin);

        //short wCount = msgin.readShort();
        //Log.d("Tag", "" + wCount);
        m_roomData.m_wCountOfObservers = msgin.readShort();

        Log.d("Tag", "ofobservers" + m_roomData.m_wCountOfObservers);
        for (int i = 0; i < m_roomData.m_wCountOfObservers; i++) {
            String usName = msgin.readUCString();
            m_rmPlayerList.addElement(usName);
        }


        if(m_roomData.m_byState != RS_PREPARE){
            Send_GM_GAMEINFO_REQ();
        }*/
        CMessage msgout = new CMessage(M_CS_RM_BETLIST_REQ);
        msgout.writeInt(RoomNumber);
        Global.netManagerFS.send(msgout);
    }

    private void updateRoomState() {
        m_nRoomstate = m_roomData.m_byState;
    }

    private void Send_GM_GAMEINFO_REQ() {
        CMessage msgout = new CMessage(M_CS_GM_GAMEINFO_REQ);
        msgout.writeInt(RoomNumber);
        Global.netManagerFS.send(msgout);

    }

    private void Send_GM_SETTING_REQ() {
        CMessage msgout = new CMessage(M_CS_GM_SETTING);
        msgout.writeInt(RoomNumber);
        m_playSetting.writeTo(msgout);
        Global.netManagerFS.send(msgout);

    }

    private void receiveGameSetting() {
        //you are gamer
        if(m_playSetting.m_usBlackPlayerName.equals(Global.m_gameData.m_userName)){
            m_turnColor = BLACK;
            m_startflg = true;
            //mRadioGroup_first.check(R.id.black);
        }else if(m_playSetting.m_usWhitePlayerName.equals(Global.m_gameData.m_userName)){
            m_turnColor = WHITE;
            //mRadioGroup_first.check(R.id.white);
        }else{
            m_turnColor = NONE;
        }
        /*if(m_playSetting.m_byType == RT_FRIENDSHIPGAME){
            mRadioGroup_gametype.check(R.id.friendship_game);
        }else if(m_playSetting.m_byType == RT_LEVELINGGAME){
            mRadioGroup_gametype.check(R.id.leveling_game);
        }*/
        showdialog_setting();

    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (radioGroup.getId()){
            case R.id.choose_first:

                if(i == R.id.black){
                    m_playSetting.m_usBlackPlayerName = m_strMyName;
                    m_playSetting.m_usWhitePlayerName = m_strOppName;
                }else if(i == R.id.white){
                    m_playSetting.m_usWhitePlayerName = m_strMyName;
                    m_playSetting.m_usBlackPlayerName = m_strOppName;

                    //Toast.makeText(this, "White" + "===" + i, Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.game_type:
                if(i == R.id.leveling_game){
                    m_playSetting.m_byType = RT_LEVELINGGAME;
                }
                else if(i == R.id.friendship_game){
                    m_playSetting.m_byType = RT_FRIENDSHIPGAME;
                }
                //Toast.makeText(this, "친선대국인가 아니면 승강급대국인가?" + "===" + i, Toast.LENGTH_LONG).show();
                break;
            case R.id.time_type:
                switch (i){
                    case R.id.short_time:
                        break;
                    case R.id.long_time:
                        break;
                    case R.id.normal_time:
                        break;
                    default:
                        break;
                }
                //Toast.makeText(this, "short, Medium, long?" + "===" + i, Toast.LENGTH_LONG).show();
                break;
            default:
        }
    }

    private void setMyRole(byte role){
        myRole = role;
    }
    private byte getMyRole() {
        return myRole;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()){
            case R.id.spinner_bordertype:
                m_playSetting.m_byBoardSize = (byte) spinner_boardsize[i];
                //Toast.makeText(this, "판크기?" + "===" + i, Toast.LENGTH_LONG).show();
                break;
            case R.id.spinner_handicap:
                m_playSetting.m_byHandicap = (byte) spinner_handicap[i];
                //Toast.makeText(this, "돌 얼마나 놓자?" + "===" + i, Toast.LENGTH_LONG).show();
                break;
            case R.id.spinner_compensation:
                m_playSetting.m_byDom = (byte) spinner_dom[i];
                //Toast.makeText(this, "점수얼마나?" + "===" + i, Toast.LENGTH_LONG).show();
                break;
            case R.id.spinner_time:
                m_playSetting.m_wHaveTime = (short) (time[i] * 60);
                //Toast.makeText(this, "경기시간?" + "===" + i, Toast.LENGTH_LONG).show();
                break;
            case R.id.spinner_overtime:
                m_playSetting.m_byCountDownTime = (byte) countingtime[i];
                //Toast.makeText(this, "초읽기 회수?" + "===" + i, Toast.LENGTH_LONG).show();
                break;
            case R.id.spinner_count:
                m_playSetting.m_byCountDownNum = (byte) counting_num[i];
                //Toast.makeText(this, "시간세는 회수?" + "===" + i, Toast.LENGTH_LONG).show();
                break;
            default:
                break;

        }

    }
    private void ProcGameOver() {
        m_interface.SS_GetTerritory(m_byArea);
        //DecideResult(true);
        //by RZS --------------
        GameEndInfo pGameInfo = new GameEndInfo();
        pGameInfo.m_wBlackCrosses = (short)getCrosses( SCORE_BLACK ) ;
        pGameInfo.m_wTakenWhiteStones = (short)m_interface.SS_GetDeadCount(SS_BLACK);
        pGameInfo.m_wWhiteCrosses = (short)getCrosses( SCORE_WHITE ) ;
        pGameInfo.m_wTakenBlackStones = (short)m_interface.SS_GetDeadCount(SS_WHITE);
        pGameInfo.m_byDom = m_setInfo.nGomi;
        pGameInfo.m_byEndRes = GMEND_CALC;
        pGameInfo.m_byWinnerCol = (byte)getCrosses( SCORE_RESULT );

        CMessage msgOut = new CMessage( M_CS_GM_END );
        msgOut.writeInt( RoomNumber );
        msgOut.writeByte( GMEND_CALC );
        pGameInfo.writeTo( msgOut );
        Global.netManagerFS.send( msgOut );
        //--------------

        /*
                if( m_bMustScored ) {
                m_nCalcedPoints = calcPoint();
                m_nScore[SCORE_TOTAL][SCORE_TOTAL_POINTS] += m_nCalcedPoints;
                m_nScore[SCORE_TOTAL][SCORE_TOTAL_ENDGAMES]++;
                int nScore = getScore();
            if( m_interface.SS_GetPlayInfo().nWhitePlayer != SS_MAN )
                nScore *= -1;
            if( nScore > 0 ) {
                m_nScore[m_setInfo.nComLevel+1][SCORE_WINS]++;
                m_nScore[m_setInfo.nComLevel+1][SCORE_CONT_WINS]++;
                if( m_nScore[m_setInfo.nComLevel+1][SCORE_CONT_WINS] >
                m_nScore[m_setInfo.nComLevel+1][SCORE_MAX_CONT_WINS] )
                        m_nScore[m_setInfo.nComLevel+1][SCORE_MAX_CONT_WINS]=
         m_nScore[m_setInfo.nComLevel+1][SCORE_CONT_WINS];
            } else if( nScore < 0 ) {
                m_nScore[m_setInfo.nComLevel+1][SCORE_LOSES]++;
                m_nScore[m_setInfo.nComLevel+1][SCORE_CONT_WINS] = 0;
            } else {
                m_nScore[m_setInfo.nComLevel+1][SCORE_DRAWS]++;
                m_nScore[m_setInfo.nComLevel+1][SCORE_CONT_WINS] = 0;
            }

            saveScore();
                }

                showCharAnim((byte) 4);
                // DecideResult(true);
                invalidate();
         */
    }
    private int getCrosses(int nScoreType) {
        int wCrosses = 0;
        int bCrosses = 0;
        byte[][] byBoard = new byte[19][19];
        m_interface.SS_GetBoard(byBoard);

        for (int y = 0; y < m_setInfo.nBoardSize; y++) {
            for (int x = 0; x < m_setInfo.nBoardSize; x++) {
                if (m_byArea[y][x] == SS_WHITE) {
                    if (byBoard[y][x] == 0) {
                        wCrosses += 1;
                    }
                    if (byBoard[y][x] == SS_BLACK) {
                        wCrosses += 2;
                    }
                }
                if (m_byArea[y][x] == SS_BLACK) {
                    if (byBoard[y][x] == 0) {
                        bCrosses += 1;
                    }
                    if (byBoard[y][x] == SS_WHITE) {
                        bCrosses += 2;
                    }
                }
            }
        }

        if (nScoreType == SCORE_BLACK) {
            return bCrosses;
        } else if (nScoreType == SCORE_WHITE) {
            return wCrosses;
        } else {
            wCrosses += m_interface.SS_GetDeadCount(SS_WHITE);
            bCrosses += m_interface.SS_GetDeadCount(SS_BLACK);
            int score = (wCrosses - bCrosses) * 10;
            if (m_setInfo.nHandicap == 0) {
                score += m_setInfo.nGomi;
            }
            if (m_nCurNum >= 400) {
                score = 0;
            }
            return score>0 ? SS_WHITE : SS_BLACK;
        }
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent){
        switch (keyEvent.getKeyCode()){
            case KeyEvent.KEYCODE_BACK:
                new AlertDialog.Builder(this)
                        .setCancelable(true)
                        .setTitle("Really Quit?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                send_quitRoom();
                            }
                        })
                        .show();

                break;
            default:
                break;
        }
        return false;
    }

    private void send_quitRoom() {
        CMessage message = new CMessage(M_CS_RM_QUIT_ROOM);
        message.writeInt(RoomNumber);
        Global.netManagerFS.send(message);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    // GAME Engine relation constants
    private static final byte SS_ST_NONE = SSInterface.SS_ST_NONE;
    private static final byte SS_ST_REPLAY = SSInterface.SS_ST_REPLAY;
    private static final byte SS_ST_EDIT_TERR = SSInterface.SS_ST_EDIT_TERR;
    private static final byte SS_ST_EDIT = SSInterface.SS_ST_EDIT;
    private static final byte SS_ST_PLAY = SSInterface.SS_ST_PLAY;
    private static final byte SS_ST_AUTOPLAY = SSInterface.SS_ST_AUTOPLAY;
    private static final byte SS_ST_NETPLAY = SSInterface.SS_ST_NETPLAY;
    private static final byte SS_ST_INTERNETPLAY = SSInterface.
            SS_ST_INTERNETPLAY;
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
