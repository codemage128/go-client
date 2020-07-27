package spacecon.tob.bclient.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.microedition.khronos.opengles.GL;

import spacecon.tob.bclient.*;
import spacecon.tob.bclient.Global;
import spacecon.tob.bclient.common.Common;
import spacecon.tob.bclient.common.GameData;
import spacecon.tob.bclient.common.PlayerPrimeInfo;
import spacecon.tob.bclient.common.RoomData;
import spacecon.tob.bclient.net.CLoginCookie;
import spacecon.tob.bclient.net.CMessage;
import spacecon.tob.bclient.net.CNetManager;
import spacecon.tob.bclient.object.GameKiboRecord;
import spacecon.tob.bclient.ui.start.MainActivity;

import static spacecon.tob.bclient.Global.m_packetHandler;
import static spacecon.tob.bclient.Global.netManagerFS;
import static spacecon.tob.bclient.Global.netManagerSS;

/**
 * Created by HongGukSong on 6/22/2017.
 */
public class MenuActivity extends AppCompatActivity implements Common, Handler.Callback {
    //private String nation_url = "http://192.168.1.181/theme/templates/admin/";
    private String world_url = "http://192.168.1.181/theme/templates/frontend/shop-about.html";
    //private String winner_url = "http://192.168.1.181/theme/templates/frontend/onepage-index.html";


    int m_nRoomDataLineCount = 0; // = Globals.m_gameData.getRoomCount();
    int m_nUserDataLineCount = 0; // = Globals.m_gameData.getPlayerCount();

    int m_nMenuType = MENU_TYPE_NONE;

    private static final int MENU_TYPE_NONE = 0;
    private static final int MENU_TYPE_COMMON = 1;
    private static final int MENU_TYPE_MAIN = 2;
    private static final int MENU_TYPE_CHANNEL = 3;
    private int m_ScreenType;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private WebView world, nation, winner;
    private boolean progressflag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {



        Handler packetHandler = new Handler(this);
        Global.m_packetHandler = packetHandler;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);






        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        world = (WebView) findViewById(R.id.world);
        //nation = (WebView)findViewById(R.id.nation);
        //winner = (WebView)findViewById(R.id.winner);

        world.getSettings().setJavaScriptEnabled(true);
        world.loadUrl(world_url);
        //int progress = world.getProgress();
        //Log.d("getProgress~~~~", String.valueOf(progress));
        //nation.getSettings().setJavaScriptEnabled(true);
        //nation.loadUrl(nation_url);
        //winner.getSettings().setJavaScriptEnabled(true);
        //winner.loadUrl(winner_url);
        Button button_classroom = (Button)findViewById(R.id.button_classroom);
        button_classroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(1);
            }
        });
    }



    private void retry_func() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void goRecord(View view) {
        m_ScreenType = RT_SHOWKIBO;
        if (Global.m_connect) {
            showDialog(1);
            EnterToShard(SECONDSHARDID);
            //EnterRecord();

        }
    }

    private void EnterRecord() {
        CMessage msg = new CMessage(M_CS_KB_KIBOLIST_REQ);
        byte nRoomType = RT_SHOWKIBO;
        byte nKibotype = KB_ALL;
        int nRoomNum = 0;
        int nLastKiboNum = 0;
        String usName = Global.m_gameData.m_userName;


        msg.writeByte(nRoomType);
        msg.writeInt(nRoomNum);
        msg.writeByte(nKibotype);
        msg.writeInt(nLastKiboNum);
        msg.writeUCString(usName);

        Global.netManagerFS.send(msg);
    }
    protected Dialog onCreateDialog(int id){
        switch (id){
            case 1:
                ProgressDialog dialog = new ProgressDialog(MenuActivity.this);
                dialog.setMessage("Please wait while loading...");
                dialog.setIndeterminate(true);
                dialog.setCancelable(false);
                return dialog;
            case 2:
                break;
        }
        return null;
    }

    public void goRoom(View view) {

        if (Global.m_connect) {
            m_ScreenType = RT_NONE;
            showDialog(1);
            EnterToShard(SECONDSHARDID);
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("LogIn Error;")
                    .setCancelable(false)
                    .setNegativeButton("Retry:", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            retry_func();
                        }
                    })
                    .show();

        }

    }


    private void EnterToShard(int shardid) {

        Global.m_gameData.m_shardId = shardid;
        CMessage msgout = new CMessage(M_CS_CS);
        msgout.writeUCString(Global.m_gameData.m_userName);
        msgout.writeString(Global.m_gameData.m_userPassword);
        msgout.writeInt(Global.m_gameData.m_dID);
        msgout.writeInt(Global.m_gameData.m_shardId);
        Global.netManagerSS.send(msgout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onLoginServerSCS(CMessage msg) {
        short wErrorNo = msg.readShort();
        if (wErrorNo != ERR_NOERR) {
            return;
        }
        GameData.getInstance().m_Cookie = new CLoginCookie();
        GameData.getInstance().m_Cookie.readFrom(msg);
        String fsAddr = msg.readString();
        int commaPos = fsAddr.indexOf(':');

        String fsAddress = fsAddr.substring(0, commaPos);
        ;
        GameData.getInstance().m_strFSAddress = fsAddress;
        String port = fsAddr.substring(commaPos + 1);

        int nPort = Integer.parseInt(port);
        GameData.getInstance().m_nFSPort = nPort;

        netManagerFS = new CNetManager(fsAddress, nPort);
        try {
            if (!netManagerFS.connect()) {
                return;
            }
            GameData.m_Connect_state = 2; // this is the Fs Connection
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void onShardEntered(CMessage msg) {
        CMessage msgout = new CMessage(M_CS_FES_CONNECTED);
        netManagerFS.send(msgout);
    }

    private void OnUserListRes(CMessage msg) {
        //먼저 자기를 등록한다. //by hgs 2017.8.30
        int nCount = 0;
        nCount = msg.readInt();
        Log.d("PerSonNumber", String.valueOf(nCount));

        for (int i = 0; i < nCount; i++) {

            PlayerPrimeInfo pInfo = new PlayerPrimeInfo();
            pInfo.loadFrom(msg);

            if (pInfo.m_usName != null) {
                if (Global.m_gameData.m_userName.equals(pInfo.m_usName)) {//재등록하는 과정이라고 생각하자.//by hgs
                    Global.m_gameData.setOwnInfo(pInfo);
                }
                else {
                    Global.m_gameData.addPlayer(pInfo);
                    addPlayer(pInfo.m_usName);
                }

            }

        }
        Log.d("PerSonNumber", String.valueOf(Global.m_PlayerList.size()));
        CMessage msgout = new CMessage(M_CS_RM_LIST_REQ);
        Global.netManagerFS.send(msgout);
    }

    private void OnRoomListRes(CMessage msg) {
        short nCount = msg.readShort();

        Global.m_gameData.createRoomList();
        for (int i = 0; i < nCount; i++) {
            RoomData RoomInfo = new RoomData();
            RoomInfo.readFrom(msg);
            Global.m_gameData.addRoom(RoomInfo);
            addRoom(RoomInfo.m_wRoomNo);
        }
        // reqReenterGame();
        changedGameList();
        Log.d("Here", "Here");
        OnReq_CS_US_FRIENDLIST_REQ();
    }

    private void addRoom(int m_roomid) {
        Global.m_RoomList.addElement(String.valueOf(m_roomid));
    }

    private void OnRes_SC_US_FRIENDLIST_RES(CMessage msgin) {
        int nCount = msgin.readInt();
        String sFriendName;

        for (int n = 0; n < nCount; n++) {
            sFriendName = msgin.readUCString();
            Global.m_gameData.m_vecFriendList.addElement(sFriendName);
        }
        OnReq_CS_US_BADLIST_REQ();
    }

    private void OnRes_SC_US_BADLIST_RES(CMessage msgin) {

        OnReq_CS_US_MEMOLIST_REQ();
        //OnReq_CS_US_EXISTPREVGAME_REQ();
    }

    private void OnRes_SC_US_MEMOLIST_RES(CMessage msg) {
        OnReq_CS_SH_LIST_REQ();
    }

    private void OnRes_SC_SH_LIST_RES(CMessage msg) {
        //here display the data
        OnReq_CS_US_EXISTPREVGAME_REQ();
    }


    private void OnReq_CS_US_EXISTPREVGAME_REQ() {

        CMessage msgout = new CMessage(M_CS_US_EXISTPREVGAME_REQ);
        //RomNumIn, responsive_result add msgout
        Global.netManagerFS.send(msgout);
    }

    private void OnRes_SC_US_EXISTPREVGAME(CMessage message) {
        short roomNo = message.readShort();
        CMessage msgOut = new CMessage(M_CS_US_EXISTGAME_RES);
        msgOut.writeShort(roomNo);
        msgOut.writeByte(RES_FAIL);
        Global.netManagerFS.send(msgOut);
    }

    private void OnReq_CS_SH_LIST_REQ() {
        CMessage msgout = new CMessage(M_CS_SH_LIST_REQ);
        Global.netManagerFS.send(msgout);
    }

    private void OnReq_CS_US_MEMOLIST_REQ() {
        Log.d("MEMOLIST SEND:", "HERE STOP");
        CMessage msgout = new CMessage(M_CS_US_MEMOLIST_REQ);
        msgout.writeByte(MSG_NEWS);
        Global.netManagerFS.send(msgout);

    }

    private void OnReq_CS_US_FRIENDLIST_REQ() {
        CMessage msgout = new CMessage(M_CS_US_FRIENDLIST_REQ);
        Global.netManagerFS.send(msgout);
    }

    private void OnReq_CS_US_BADLIST_REQ() {
        CMessage msgout = new CMessage(M_CS_US_BADLIST_REQ);
        Global.netManagerFS.send(msgout);
    }

    private void changedGameList() {

    }

    private void changePlayerList() {
    }

    private void addPlayer(String usName) {
        Global.m_PlayerList.addElement(usName);
    }

    @Override
    public boolean handleMessage(Message message) {

        onMessage((CMessage) message.obj);
        return false;
    }

    private void onMessage(CMessage msg) {


        int nHeader = msg.getHeader();
        Log.d("      ==Receive==     ", Integer.toHexString(msg.getHeader()));
        switch (nHeader) {
            case M_HANDSHKOK:
                if (GameData.m_Connect_state == 1) {
                } else if (GameData.m_Connect_state == 2) {
                    fsAuth();
                } else {
                    Log.d("ServerToClient!", "No Packet");
                }
                break;
            case M_LOGIN_SERVER_SCS:
                Log.d("ServerToClient", "지역써버성공");
                //Log.d("ServerToClient!", String.valueOf(msg.getHeader()));
                onLoginServerSCS(msg);
                break;
            case M_SC_SV:
                onShardEntered(msg);
                break;
            case M_SC_FES_CONNECTED:
                onFESConnected(msg);
                break;
            case M_SC_US_LIST_RES:
                OnUserListRes(msg);
                break;
            case M_SC_RM_LIST_RES:
                OnRoomListRes(msg);
                break;
            case M_SC_US_FRIENDLIST_RES:
                OnRes_SC_US_FRIENDLIST_RES(msg);
                break;
            case M_SC_US_BADLIST_RES:
                OnRes_SC_US_BADLIST_RES(msg);
                break;
            case M_SC_US_MEMOLIST:
                OnRes_SC_US_MEMOLIST_RES(msg);
                break;
            case M_SC_SH_LIST:
                OnRes_SC_SH_LIST_RES(msg);
                break;
            case M_SC_US_EXISTPREVGAME:
                //OnRes_SC_US_EXISTPREVGAME(msg);
                Log.d("대국도중에차단된경우아님", "No Packet");
                break;
            case M_SC_US_PUBLIC_NOTICE:
                on_us_publick_notice(msg);
                progressflag = false;
                Log.d("대국도중에차단된경우아님", "No Packet");
                EnterRoom();
                break;
            case M_SC_KB_KIBOLIST:
                on_SC_KB_KIBOLIST(msg);
                break;
            case M_SC_RM_CHANGELIST:
                //on_SC_RM_CHANGELIST(msg);
                break;
            case M_SC_US_CHANGELIST:
                break;
            default:
        }

    }

    private void on_us_publick_notice(CMessage msg) {
        String str = msg.readUCString();
    }


    private void EnterRoom() {
        if (m_ScreenType == RT_SHOWKIBO) {
            EnterRecord();

        } else {
            roomenter();
        }

    }
    private void roomenter(){
        Intent intent = new Intent(this, RoomActivity.class);
        startActivity(intent);
    }

    private void fsAuth() {
        CMessage msgout = new CMessage(M_LOGIN_SERVER_SV);
        GameData.getInstance().m_Cookie.writeTo(msgout);
        Global.netManagerFS.send(msgout);
    }

    private void on_SC_KB_KIBOLIST(CMessage msg) {
        byte nRoomType = msg.readByte();
        if (nRoomType == RT_SHOWKIBO) {
            int nRoomNum = msg.readInt();
            byte nKbKind = msg.readByte();
            byte nKiboCount = msg.readByte();
            msg.readInt();
            Global.m_kibolist.removeAllElements();

            for (int nKiboIndex = 0; nKiboIndex < nKiboCount; nKiboIndex++) {
                GameKiboRecord kiboRecord = new GameKiboRecord();
                kiboRecord.read(msg);
                Global.m_kibolist.addElement(kiboRecord);
                addKibo(kiboRecord.m_nKiboID);
            }

            Intent intent = new Intent(this, KiboListActivity.class);
            startActivity(intent);
        }
    }

    private void addKibo(int m_nKiboID) {
        Global.m_kibo.addElement(String.valueOf(m_nKiboID));
    }

    private void on_SC_RM_CHANGELIST(CMessage msg) {
        if (Global.m_gameData.getRoomCount() < 0)
            return;

        short nCount = msg.readShort();
        int nServerTime = msg.readInt();

        if (nCount == 0)
            return;
        for (int n = 0; n < nCount; n++) {
            byte byState;
            byState = msg.readByte();

            RoomData roomData = new RoomData();
            if (byState != LS_DELETE) {
                roomData.readFrom(msg);
                if (byState == LS_UPDATE) {
                    Global.m_gameData.updateRoom(roomData);
                } else if (byState == LS_INSERT) {
                    Global.m_gameData.addRoom(roomData);
                }
            } else {
                short wRoomNo = msg.readShort();
                Global.m_gameData.removeRoom(wRoomNo);

                // room table update
                /*m_nRoomDataLineCount = Global.m_gameData.getRoomCount();
                if (m_nMenuType == MENU_TYPE_NONE) {
                    int nIndex = m_nRoomSelectedLine + m_nFirstOfRoomPage;

                    if (m_nRoomDataLineCount < nIndex) {
                        if (m_nFirstOfRoomPage > 1) {
                            m_nFirstOfRoomPage = m_nRoomDataLineCount - m_nRoomSelectedLine;
                            if (m_nFirstOfRoomPage < 1)
                                m_nFirstOfRoomPage = 1;
                        } else {
                            m_nFirstOfRoomPage = 1;
                            m_nRoomSelectedLine = m_nRoomDataLineCount - m_nFirstOfRoomPage;
                            // by RZS
                            if (m_nRoomSelectedLine < 0)
                                m_nRoomSelectedLine = 0;

                        }
                    }
                }*/
            }
        }

        changedGameList();
    }

    private void onFESConnected(CMessage msg) {
        CMessage message = new CMessage(M_CS_US_LIST_REQ);
        Global.netManagerFS.send(message);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Menu Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent){
        switch (keyEvent.getKeyCode()){
            case KeyEvent.KEYCODE_BACK:
                new AlertDialog.Builder(this)
                        .setTitle("Really go out?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                logout();
                            }
                        })
                        .setCancelable(true)
                        .show();
                break;
            default:
                break;
        }
        return false;
    }

    private void logout() {
        //Global.netManagerFS.stop();
    }
}
