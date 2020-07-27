package spacecon.tob.bclient.ui.start;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import spacecon.tob.bclient.Global;
import spacecon.tob.bclient.R;

/**
 * Created by HongGukSong on 10/9/2017.
 */

public class LogActivity extends Activity implements View.OnClickListener{
    Timer mUpdateTimer;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout_log);
        relativeLayout.setOnClickListener(this);
        boolean bAutoNext = true;
        if(bAutoNext){
            mUpdateTimer = new Timer("LogScreen");

            TimerTask task = new TimerTask(){

                @Override
                public void run() {

                    onNext();

                }
            };
            mUpdateTimer.schedule(task, 3000);
        }
    }

    public void onNext(){
        if(mUpdateTimer != null) {
            mUpdateTimer.cancel();
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onClick(View view) {

        onNext();
    }
}
