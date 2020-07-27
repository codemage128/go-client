package spacecon.tob.bclient.ui.start;
/*화면 해상도 1280 * 720 기정으로 ...*/
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import spacecon.tob.bclient.common.GameData;
import spacecon.tob.bclient.net.IProtocol;
import spacecon.tob.bclient.R;
import spacecon.tob.bclient.*;
import spacecon.tob.bclient.net.*;
import spacecon.tob.bclient.ui.MenuActivity;
import spacecon.tob.bclient.ui.WebSite;



public class MainActivity extends Activity implements IProtocol , Handler.Callback , View.OnClickListener{
    public String m_ID="";
    public String m_Password="";

    private  void Initialize(){
        try {
            Handler packetHandler = new Handler(this);
            Global.m_packetHandler = packetHandler;
            Global.Initialize();

            if (!Global.netManagerSS.connect()) {
                new AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setCancelable(true)
                        .setMessage("Connection Failure!")
                        /*.setNegativeButton("", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                closeContextMenu();
                            }
                        })*/
                        .show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_start);
        //setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "홈페지방문을 진행할수 있습니다.", Snackbar.LENGTH_LONG)
                        .setAction("홈페지가기", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                goSite();
                            }
                        }).show();
            }
        });


        Initialize();
        GameData.m_Connect_state = 1; // this is the SSConection

    }

    private void startLogin() {
        CMessage msg = new CMessage(M_CS_AUTH);
        msg.writeUCString(Global.m_gameData.m_userName);
        msg.writeString(Global.m_gameData.m_userPassword);
        Global.netManagerSS.send(msg);
    }

    public void logIn(View view){ /*here type the code*/

        m_ID = ((EditText)( findViewById(R.id.editText_name))).getText().toString();
        m_Password = ((EditText)(findViewById(R.id.editText_password))).getText().toString();

        if(m_ID.equals("")){
            Toast.makeText(this,"Please enter your name. Thank you!", Toast.LENGTH_LONG).show();
            return;
        }
        if(m_Password.equals("")){
            Toast.makeText(this,"Please enter your password. Thank you!", Toast.LENGTH_LONG).show();
            return;
        }
        if(!Global.m_connect){
                new AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setCancelable(true)
                        .setMessage("Connection Failure!")
                        /*.setNegativeButton("", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                closeContextMenu();
                            }
                        })*/
                        .show();
            return;
        }

        Global.m_gameData.m_userName = m_ID;
        Global.m_gameData.m_userPassword = m_Password;
        startLogin();
    }

    private void goSite() {
        Intent intent = new Intent(this, WebSite.class);

        startActivity(intent);

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

    public int onMessage(CMessage msg) {
        switch (msg.getHeader()){
            case M_SC_SHARDS:
                GameData.getInstance().m_dID = msg.readInt();
                Log.d("TTTTTTTT", "TTTTTTTTT" + GameData.getInstance().m_dID);
                goLogin();
                break;
            case M_SC_LOGIN_FAIL:

                Log.d("ogError Message:" , String.valueOf(msg.getHeader()));

                new AlertDialog.Builder(this)
                        .setTitle("Error!")
                        .setCancelable(true)
                        .setMessage("Ready Logined.     Please Retry!")
                        /*.setNegativeButton("", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                closeContextMenu();
                            }
                        })*/
                        .show();
                break;
            case M_SC_US_LIST_RES:
                onUserListRes(msg);
                break;
            default:
                Log.d("MainActiviessage:" , String.valueOf(msg.getHeader()));

        }
        return 0;
    }

    private void onUserListRes(CMessage msg) {

    }

    private void goLogin() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public boolean handleMessage(Message message) {
        onMessage((CMessage) message.obj);
        return true;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()){
            case KeyEvent.KEYCODE_BACK:
                new AlertDialog.Builder(this)
                        .setTitle("Really Exit?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
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
}
