package spacecon.tob.bclient.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Vector;

import spacecon.tob.bclient.Global;
import spacecon.tob.bclient.R;
import spacecon.tob.bclient.common.Common;
import spacecon.tob.bclient.net.CMessage;
import spacecon.tob.bclient.object.GameKiboRecord;


/**
 * Created by HongGukSong on 10/26/2017.
 */
public class KiboListActivity extends AppCompatActivity implements Handler.Callback, AdapterView.OnItemClickListener, Common{
    private ListView kibolist;
    Vector<String> gameKiboRecordVector;
    private byte m_nBoardSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Handler packetHandler = new Handler(this);
        Global.m_packetHandler = packetHandler;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kibolist);

        init();

        kibolist = (ListView)findViewById(R.id.kibolist);
        kibolist.setAdapter(new KiboList(KiboListActivity.this));
        //kibolist.setTextFilterEnabled(true);
        kibolist.setOnItemClickListener(this);
    }

    private void init() {
        gameKiboRecordVector = Global.m_kibo;
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
            case M_SC_KB_KIBO_RES:
                on_SC_KB_KIBO_RES(message);
                break;
            default:
                break;
        }
    }

    private void on_SC_KB_KIBO_RES(CMessage message) {

        byte result = message.readByte();
        byte nRoomtype = message.readByte();
        int nRoomNum = message.readInt();
        byte nKiboKind = message.readByte();
        Global.m_KiboData = message.readUCString();
        Global.m_nBoardsize_kibo = message.readByte();
        StartKibo();
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.kibolist:
                final int sel_kiboid = Integer.parseInt(gameKiboRecordVector.get(i));
                new AlertDialog.Builder(this)
                        .setCancelable(true)
                        .setTitle("您 。。。房 间 去 吧？")
                        .setPositiveButton("。。好啊！", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int j) {
                                EnterKiboScreen(Global.m_gameData.m_userName, sel_kiboid);

                            }
                        }).show();
                break;
            default:
                break;
        }
    }

    private void EnterKiboScreen(String m_userName, int sel_kiboid) {
        CMessage message = new CMessage(M_CS_KB_KIBO_REQ);
        int kiboid = sel_kiboid;
        Global.g_KiboNumber = sel_kiboid;
        message.writeByte(RT_SHOWKIBO);
        message.writeInt(0);
        message.writeByte((byte) 0);
        message.writeInt(kiboid);
        Global.netManagerFS.send(message);
    }

    private void StartKibo() {

        Intent intent =  new Intent(this, GameKiboActivity.class);
        startActivity(intent);

    }
}
