package spacecon.tob.bclient.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.Image;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import spacecon.tob.bclient.Global;
import spacecon.tob.bclient.R;
import spacecon.tob.bclient.common.Common;

/**
 * Created by HongGukSong on 8/14/2017.
 */
public class Stone extends View implements Common{
    public int m_stone_color = 0;
    public int m_recent_color;
    protected Context m_context;
    protected FrameLayout m_framelayout;
    protected Bitmap m_stone_image;
    private Canvas m_canvas;
    private float m_radius;

    private float m_realX = 0.0f;
    private float m_realY = 0.0f;

    public Stone(Context context, FrameLayout framelayout) {

        super(context);

        m_context = context;
        m_framelayout = framelayout;
        m_radius = Global.m_stone_radius;
        init();

    }

    private void init() {
        if(m_stone_color == BLACK) { //black
            m_stone_image = BitmapFactory.decodeResource(getResources(), R.drawable.black_stone);
        }else if (m_stone_color == WHITE) {
            m_stone_image = BitmapFactory.decodeResource(getResources(), R.drawable.white_stone);
        }else return;

        m_stone_image = Bitmap.createScaledBitmap(m_stone_image, (int)m_radius * 2, (int) m_radius * 2, true);
        m_framelayout.addView(this);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvas.drawBitmap(m_stone_image, m_realX, m_realY, null);

    }
    public void setpos(float x, float y, int m_stoneColor){
        m_stone_color = m_stoneColor;
        m_realX = x - m_radius;
        m_realY = y - m_radius;
        init();
    }


}
