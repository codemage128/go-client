package spacecon.tob.bclient.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import spacecon.tob.bclient.R;

/**
 * Created by HongGukSong on 6/20/2017.
 */

public class WebSite extends AppCompatActivity{
    String m_site = "http://192.168.1.202/";

    private WebView webView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website);

        webView = (WebView) findViewById(R.id.site);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(m_site);
    }

}
