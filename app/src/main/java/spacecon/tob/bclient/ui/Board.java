package spacecon.tob.bclient.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.FrameLayout;

import spacecon.tob.bclient.Global;
import spacecon.tob.bclient.R;
import spacecon.tob.bclient.common.Common;
import spacecon.tob.bclient.engine.SSInterface;
import spacecon.tob.bclient.net.IProtocol;
import spacecon.tob.bclient.net.ISysProtocol;

import static spacecon.tob.bclient.engine.SSInterface.SS_WHITE;


/**
 * Created by HongGukSong on 10/19/2017.
 */

public class Board extends View implements Common, ISysProtocol {
    FrameLayout m_frameLayout;
    int m_frame_width = 0, m_frame_height = 0;
    Paint paint = new Paint(); //
    Point cp = new Point(); // center_pos 화점(중심점) 절대위치
    PointF sp = new PointF();
    Bitmap bitmap_bg;
    Bitmap m_stone_white, m_stone_black;
    float a_boardsize_2; //상대자리표
    int cellcount = 19;
    float cellwidth;
    int lastposx=-1,lastposy=-1;

    public byte[][] byArea = null;
    public byte[][] byTerrBoard = null;

    SSInterface ssInterface = null;
    public boolean AnalizeFlag = false;
    public boolean EndFlag = false;

    public void setCellcount(short cellcount_1){
        cellcount = cellcount_1;
    }
    public void setInterface(SSInterface ssInterface_1){
        ssInterface = ssInterface_1;
    }
    public Board(Context context, FrameLayout frameLayout) {
        super(context);
        m_frameLayout = frameLayout;

        init();
    }
    public void SetLastStonePos(int x,int y) {
        lastposx=x;
        lastposy=y;
    }

    private void init() {
        bitmap_bg = BitmapFactory.decodeResource(getResources(), R.drawable.game_background);
        m_stone_black= BitmapFactory.decodeResource(getResources(), R.drawable.black_stone);
        m_stone_white= BitmapFactory.decodeResource(getResources(), R.drawable.white_stone);

        //m_stone_black = Bitmap.createScaledBitmap(m_stone_black, (int)m_radius * 2, (int) m_radius * 2, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        m_frame_width = m_frameLayout.getWidth();
        m_frame_height = m_frameLayout.getHeight();
        cp.set(m_frame_width/2, m_frame_height/2);

        //cp.set(Math.min( m_frame_width, m_frame_height) / 2 , Math.min( m_frame_width, m_frame_height) / 2);
        a_boardsize_2 = Math.min( m_frame_width, m_frame_height) / 2 * 0.96f;
        cellwidth=a_boardsize_2 * 2 / cellcount;

        Global.m_bordersize_2 = a_boardsize_2;
        Global.m_cp = cp;
        Global.m_stone_radius = cellwidth / 2;



        canvas.drawBitmap(bitmap_bg,
                new Rect(0 , 0, bitmap_bg.getWidth(), bitmap_bg.getHeight()),
                new RectF(cp.x - a_boardsize_2, cp.y - a_boardsize_2, cp.x + a_boardsize_2 , cp.y + a_boardsize_2),
                null);
        drawPoint(canvas);

        sp.set(cp.x - a_boardsize_2 + cellwidth / 2, cp.y - a_boardsize_2 + cellwidth / 2);
        for(int i = 0; i < cellcount; i ++) {
            canvas.drawLine(sp.x + i * cellwidth, sp.y,
                    sp.x + i * cellwidth, sp.y + cellwidth * (cellcount - 1) ,paint);
            canvas.drawLine(sp.x , sp.y + i * cellwidth,
                    sp.x + cellwidth * (cellcount-1),sp.y + i * cellwidth,
                    paint);
        }
        drawStone(canvas);
        drawLastPos(canvas);
        if(AnalizeFlag){
            draHome(canvas);
        }
        if(EndFlag){
            drawHouse(canvas);
        }
    }

    private void drawHouse(Canvas canvas) {

        int nColor;
        if(ssInterface == null){
            return;
        }


        for ( int x = 0; x <cellcount; x++ ) {
            for ( int y = 0; y < cellcount; y++ ) {
                if ( byArea[ y ][ x ] == 0 ) {
                    continue;
                }

                if ( byArea[ y ][ x ] == byTerrBoard[ y ][ x ] ) {
                    continue;
                }

                if (byArea[ y ][ x ] == SS_WHITE) {
                    nColor = 0xFFFFFFFF;
                } else {
                    nColor = 0xFF000000;
                }
                Paint paint = new Paint();
                paint.setColor(nColor);
                float dx = sp.x + (x * cellwidth);
                float dy = sp.y + (y * cellwidth);
                RectF rect = new RectF();
                canvas.drawCircle(dx, dy, 10.0f, paint);
                //rect.set(dx - cellwidth / 3, dy - cellwidth / 3, dx + cellwidth / 3, dx + cellwidth / 3);
                //canvas.drawRect(rect, paint);
            }
        }
    }

    private void drawPoint(Canvas canvas) {
        //canvas.drawCircle(cp.x, cp.y, a_boardsize_2 * 0.015f, paint);
        switch (cellcount){
            case 19:
                calculate(canvas, 6);
                break;
            case 17:
                calculate(canvas, 5);
                break;
            case 15:
                calculate(canvas, 4);
                break;
            case 13:
                calculate(canvas, 3);
                break;
            case 11:
                calculate(canvas, 2);
                break;
            case 9:
                calculate(canvas, 0);
                break;
            default:
                break;
        }
    }

    private void calculate(Canvas canvas, int pointsize) { //간격
        float sx = cp.x - (cellwidth * pointsize);
        float sy = cp.y - (cellwidth * pointsize);
        float d = cellwidth * pointsize;
        for(int i = 0; i < 3; i ++){
            for(int j = 0; j < 3; j ++) {
                canvas.drawCircle(sx + (d * j), sy + (d * i), a_boardsize_2 * 0.015f, paint);
            }
        }
    }

    private void draHome(Canvas canvas) {
        int nColor;
        if(ssInterface == null){
            return;
        }

        for ( int x = 0; x <cellcount; x++ ) {
            for ( int y = 0; y < cellcount; y++ ) {
                if ( byArea[ y ][ x ] == 0 ) {
                    continue;
                }

                if ( byArea[ y ][ x ] == byTerrBoard[ y ][ x ] ) {
                    continue;
                }

                if (byArea[ y ][ x ] == SS_WHITE) {
                    nColor = 0xFFFFFFFF;
                } else {
                    nColor = 0xFF000000;
                }
                Paint paint = new Paint();
                paint.setColor(nColor);
                float dx = sp.x + (x * cellwidth);
                float dy = sp.y + (y * cellwidth);
                RectF rect = new RectF();
                canvas.drawCircle(dx, dy, 10.0f, paint);
                //rect.set(dx - cellwidth / 3, dy - cellwidth / 3, dx + cellwidth / 3, dx + cellwidth / 3);
                //canvas.drawRect(rect, paint);
            }
        }
    }

    private void drawLastPos(Canvas canvas) {

        if(ssInterface == null){
            return;
        }
        int[] pos = new int[4];
        ssInterface.SS_GetLastStonePos(pos, true);

        if(pos[ssInterface.MOVE_X]<0 || pos[ssInterface.MOVE_Y]<0)
            return;
        float dx = sp.x + (pos[ssInterface.MOVE_X] * cellwidth);
        float dy = sp.y + (pos[ssInterface.MOVE_Y] * cellwidth);
        Paint paint_last = new Paint();
        paint_last.setColor(getResources().getColor(R.color.colorLastStone));
        Path path = new Path();
        path.setLastPoint(dx, dy);
        path.lineTo(dx + cellwidth /3, dy + 0);
        path.lineTo(dx + 0, dy + cellwidth / 3);
        path.close();
        canvas.drawPath(path, paint_last);
    }

    private void drawStone(Canvas canvas) {
        int nBoardSize = cellcount;
        if(ssInterface == null){
            return;
        }
        byte[][] boardstate = ssInterface.SS_GetBoard();

        for (int y = 0; y < nBoardSize; y++) {
            for (int x = 0; x < nBoardSize; x++) {
                float dx = sp.x + (x * cellwidth);
                float dy = sp.y + (y * cellwidth);
                float dw = cellwidth * 0.48f ;
                byte val = boardstate[y][x];
                if (val == ssInterface.SS_BLACK) {
                    canvas.drawBitmap(m_stone_black,
                            new Rect(0, 0 , m_stone_black.getWidth(), m_stone_black.getHeight()),
                            new RectF(dx - dw, dy - dw, dx + dw, dy + dw), null);

                }else if(val == SS_WHITE){
                    canvas.drawBitmap(m_stone_white,
                            new Rect(0, 0 , m_stone_white.getWidth(), m_stone_white.getHeight()),
                            new RectF(dx - dw, dy - dw, dx + dw, dy + dw), null);
                }else {
                    continue;
                }

            }
        }
    }
}
