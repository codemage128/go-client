package spacecon.tob.bclient.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import java.util.Vector;

import spacecon.tob.bclient.Global;
import spacecon.tob.bclient.R;
import spacecon.tob.bclient.common.Common;
import spacecon.tob.bclient.common.GameData;
import spacecon.tob.bclient.common.PlayerPrimeInfo;
import spacecon.tob.bclient.common.RoomData;
import spacecon.tob.bclient.net.CMessage;

public class RoomActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        AdapterView.OnItemClickListener, Common, Handler.Callback {

    Vector<String> items , roomlistitems;
    ListView uerlistview, roomlistview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Handler packetHandler = new Handler(this);
        Global.m_packetHandler = packetHandler;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        Init();
        draw();

    }


    private void Init() {
        roomlistitems = Global.m_RoomList;
        items = Global.m_PlayerList;

        OnReq_CS_US_EXISTPREVGAME_REQ();
    }


    private void OnReq_CS_US_EXISTPREVGAME_REQ() {

        CMessage msgout = new CMessage(M_CS_US_EXISTPREVGAME_REQ);
        //RomNumIn, responsive_result add msgout
        Global.netManagerFS.send(msgout);
    }
    private void draw() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*자동대국신청부분*/
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /**this is the tab part**/

        TabHost tabs = (TabHost)findViewById(R.id.tabhost);
        tabs.setup();

        String string = "선수목록";


        TabHost.TabSpec spec = tabs.newTabSpec("TAB");
        spec.setContent(R.id.user_tab);
        spec.setIndicator("선수목록");
        tabs.addTab(spec);
        tabs.setCurrentTab(1);

        spec = tabs.newTabSpec("TAB2");
        spec.setContent(R.id.user_tab1);
        spec.setIndicator("방목록");
        tabs.addTab(spec);


        /*this is the list part*/

        uerlistview = (ListView)findViewById(R.id.user_list);
        roomlistview = (ListView)findViewById(R.id.roomlist);



        uerlistview.setAdapter(new UserList(RoomActivity.this));
        //uerlistview.setTextFilterEnabled(true);
        uerlistview.setOnItemClickListener(this);

        roomlistview.setAdapter(new RoomList(RoomActivity.this));
        //roomlistview.setTextFilterEnabled(true);
        roomlistview.setOnItemClickListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.user_list:
                if (items.get(i) != Global.m_gameData.m_userName) {
                    Global.m_gameData.m_usOppName = items.get(i);
                    new AlertDialog.Builder(this)
                            .setCancelable(true)
                            .setTitle(Global.m_gameData.m_usOppName + "에게 경기신청하겠습니까?")
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    RequestGame(Global.m_gameData.m_usOppName);
                                    Log.d("xxx 에게 경기신청", "?");
                                }
                            }).show();
                } else {
                    new AlertDialog.Builder(this)
                            .setTitle(items.get(i) + "의 정보")
                            .setCancelable(true)
                            .show();
                }
                break;
            case R.id.roomlist:
                final int sel_roomid = Integer.parseInt(roomlistitems.get(i));
                Toast.makeText(this, "room" + sel_roomid , Toast.LENGTH_SHORT).show();
                new AlertDialog.Builder(this)
                        .setCancelable(true)
                        .setTitle("room Enter")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int j) {
                                EnterRoom(Global.m_gameData.m_userName, sel_roomid);
                                Log.d("xxx Enter", "?");
                            }
                        }).show();
                break;
            default:
                break;
        }
    }

    private void EnterRoom(String m_userName, int sel_roomNo) {
        int room_index = Global.m_gameData.getRoomIndex((short) sel_roomNo);
        RoomData sel_roomInfo = Global.m_gameData.getRoom(room_index);

        if (Global.m_gameData.getRoomCount() > 0) {
            if (m_userName.equals(sel_roomInfo.m_usBlackPlayerName) ||
                    m_userName.equals(sel_roomInfo.m_usWhitePlayerName)) {
                    reqReenterGame();
            }
            else{
                Global.m_nRole = GR_OBSERVER;
                enterRoom(sel_roomInfo.m_wRoomNo);
            }
        }
    }
    private void reqReenterGame() {
        CMessage msg = new CMessage(M_CS_US_EXISTPREVGAME_REQ);
        Global.netManagerFS.send(msg);
    }
    private void enterRoom(int nRoomID) {
        CMessage msg = new CMessage(M_CS_RM_ENTERROOM);
        msg.writeInt(nRoomID);
        Global.netManagerFS.send(msg);
    }

    private void RequestGame(String oppName) {
        CMessage msgout = new CMessage(M_CS_US_SELOPP);
        msgout.writeUCString(oppName);
        Global.netManagerFS.send(msgout);
    }

    @Override
    public boolean handleMessage(Message message) {
        onMessage((CMessage) message.obj);
        return false;
    }
    public void onMessage(CMessage msg){
        int nHeader = msg.getHeader();
        Log.d("      ==Receive==     ", Integer.toHexString(msg.getHeader()));
        switch (nHeader){
            case M_SC_RM_OBSERVERCHANGE:
                break;
            case M_SC_US_SELBYANOTHER:
                on_SC_US_SELBYANOTHER(msg);
                break;
            case M_SC_US_SELOPP_RES:
                on_SC_US_SELOPP_RES(msg);
                break;
            case M_SC_RM_ENTERROOM_RES:
                on_SC_RM_ENTERROOM_RES(msg);
                break;
            case M_SC_RM_CREATEROOM_RES:
                on_SC_RM_CREATEROOM_RES(msg);
                //creat room.  you are room manager
                break;
            case M_SC_RM_ROOMINFO_RES:
                on_SC_RM_ROOMINFO_RES(msg);
                break;
            case M_SC_RM_CHANGELIST:
                on_SC_RM_CHANGELIST(msg);
                break;
            case M_SC_US_CHANGELIST:
                on_SC_US_CHANGELIST(msg);
                break;
            case M_SC_US_PUBLIC_NOTICE:
                on_us_publick_notice(msg);
                break;
            case M_SC_US_EXISTPREVGAME:
                on_SC_US_EXISTPREVGAME(msg);
                break;
            default:
        }
    }




    private void on_SC_US_EXISTPREVGAME(CMessage msg) {
        final short roomNo = msg.readShort();
        new AlertDialog.Builder(this)
                .setNegativeButton("ACCEPT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("Tag:", "this is the accept");
                        CMessage msgOut = new CMessage(M_CS_US_EXISTGAME_RES);
                        msgOut.writeShort(roomNo);
                        msgOut.writeByte(RES_SUCCESS);
                        Global.netManagerFS.send(msgOut);
                        enterRoom(roomNo);
                    }
                })
                .setPositiveButton("REJECT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("Tag:", "this is the reject");
                        CMessage msgOut = new CMessage(M_CS_US_EXISTGAME_RES);
                        msgOut.writeShort(roomNo);
                        msgOut.writeByte(RES_FAIL);
                        Global.netManagerFS.send(msgOut);
                    }
                })
                .setCancelable(false)
                .setTitle("ReEnter Room?")
                .show();
    }

    private void on_us_publick_notice(CMessage msg) {
        byte count = msg.readByte();
        String str = msg.readUCString();
        Log.d("String:" , str);
    }

    private void OnRes_SC_US_EXISTPREVGAME(CMessage message) {
        short roomNo = message.readShort();
        CMessage msgOut = new CMessage(M_CS_US_EXISTGAME_RES);
        msgOut.writeShort(roomNo);
        msgOut.writeByte(RES_FAIL);
        Global.netManagerFS.send(msgOut);
    }
    private void on_SC_US_CHANGELIST(CMessage msg) {
        if(Global.m_gameData.getPlayerCount() < 0){
            return;
        }
        int nCount = msg.readInt();
        for(int i = 0; i < nCount; i ++){
            byte byState;
            byState = msg.readByte();

            PlayerPrimeInfo player = new PlayerPrimeInfo();
            if (byState != LS_DELETE) {
                player.loadFrom(msg);
                if (player.m_usName != null) {
                    if (Global.m_gameData.m_userName.equals(player.m_usName))
                        Global.m_gameData.setOwnInfo(player);
                    if (byState == LS_UPDATE) {
                        Global.m_gameData.updatePlayer(player);
                    } else if (byState == LS_INSERT) {
                        Global.m_gameData.addPlayer(player);
                        addPlayer(player.m_usName);

                    }
                }
            }
            else {
                String usDelUserName = msg.readUCString();
                Global.m_gameData.removePlayer(usDelUserName);
                removePlayer(usDelUserName);
            }
            uerlistview.setAdapter(new UserList(this));
        }

    }
    private void removePlayer(String usName) {
        Global.m_PlayerList.removeElement(usName);
    }


    private void addPlayer(String usName) {
        Global.m_PlayerList.addElement(usName);
    }

    private void on_SC_RM_CHANGELIST(CMessage msg) {
        if (Global.m_gameData.getRoomCount() < 0)
            return;

        short nCount = msg.readShort();
        int nServerTime = msg.readInt();

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
            }
            else {
                short wRoomNo = msg.readShort();
                Global.m_gameData.removeRoom(wRoomNo);
            }
            roomlistview.setAdapter(new RoomList(this));
        }
    }

    private void on_SC_RM_ROOMINFO_RES(CMessage msg) {

    }

    private void on_SC_RM_CREATEROOM_RES(CMessage msg) {
        byte byRes = msg.readByte();
        byte byType = msg.readByte();
        short wRoomNum = msg.readShort();
        short nObserverCount = msg.readShort();
        switch (byRes){
            case RES_SUCCESS:
                //방에 들어가겠다.
                StartGame();
                break;
            case RES_FAIL:
                //방에 안들어 가겠다.
                break;
            default:
                break;
        }
        for (int i = 0; i < nObserverCount; i++) {
            String usName = msg.readUCString();
            Global.m_GamerList.addElement(usName);
        }

        Global.m_bType = byType;
        Global.m_shrRoomNum = wRoomNum;
        Global.m_bCreateRoom = 0;
        Global.m_nRole = GR_MASTER;
        StartGame();
    }

    private void on_SC_RM_ENTERROOM_RES(CMessage msg) {
        byte byRes = msg.readByte();
        byte byType = msg.readByte();
        int wRoomNum = msg.readInt();
        Global.m_bType = byType;
        Global.m_shrRoomNum = wRoomNum;
        Global.m_bCreateRoom = 1;
        Log.d("this is the room enter", "see carefully" + Global.m_shrRoomNum + "room type" + Global.m_bType);
        switch (byRes){
            case RES_SUCCESS:
                //방에 들어가겠다.
                Global.m_reEnter = true;
                StartGame();
                break;
            case RES_FAIL:
                //방에 안들어 가겠다.
                break;
            default:
                break;
        }
        StartGame();
    }

    private void StartGame() {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    private void on_SC_US_SELOPP_RES(CMessage msgin) {
        String usOppName = msgin.readUCString();
        Global.m_gameData.m_usOppName = usOppName;
    }

    private void on_SC_US_SELBYANOTHER(CMessage msgin) {
        String oppName = msgin.readUCString();
        Global.m_gameData.m_usOppName = oppName;
        Global.m_nRole = GR_GAMER;

        new AlertDialog.Builder(this)
                .setNegativeButton("ACCEPT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("Tag:", "this is the accept");
                        on_CS_US_SELOPP_RES(RES_ACCEPT);
                    }
                })
                .setPositiveButton("REJECT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("Tag:", "this is the reject");
                        on_CS_US_SELOPP_RES(RES_REJECT);
                    }
                })
                .setCancelable(false)
                .setTitle(Global.m_gameData.m_usOppName + "에게서 신청이 들어왔습니다.")
                .show();

    }

    private void on_CS_US_SELOPP_RES(byte resAccept) {

        String oppName = Global.m_gameData.m_usOppName;
        CMessage msgout = new CMessage(M_CS_US_SELOPP_RES);
        msgout.writeUCString(oppName);
        msgout.writeByte(resAccept);
        Global.netManagerFS.send(msgout);
    }

    @Override
    public boolean onKeyDown(int i, KeyEvent keyEvent) {

        switch (keyEvent.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:

                break;
            default:
                break;
        }
        return false;
    }

    public void OK(View view){
        new AlertDialog.Builder(this)
                .setTitle("asdfasdf")
                .show();
    }
}
