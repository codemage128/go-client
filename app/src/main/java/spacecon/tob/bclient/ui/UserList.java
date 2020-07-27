package spacecon.tob.bclient.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Vector;

import spacecon.tob.bclient.Global;
import spacecon.tob.bclient.R;
import spacecon.tob.bclient.common.Common;
import spacecon.tob.bclient.common.PlayerPrimeInfo;

/**
 * Created by HongGukSong on 11/1/2017.
 */

public class UserList extends BaseAdapter implements Common{

    private Vector<PlayerPrimeInfo> m_playerPrimeInfos;
    private LayoutInflater mInflater;

    public UserList(Context context) {
        // Cache the LayoutInflate to avoid asking for a new one each time.
        mInflater = LayoutInflater.from(context);
        m_playerPrimeInfos = Global.m_gameData.m_vecPlayerList;
    }
    @Override
    public int getCount() {
        return m_playerPrimeInfos.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {

            view = mInflater.inflate(R.layout.user_adapter, null);

            // Creates a ViewHolder and store references to the two children views
            // we want to bind data to.
            holder = new ViewHolder();

            //holder.imageButton = (ImageView)view.findViewById(R.id.imageButton);
            holder.name_text = (TextView)view.findViewById(R.id.uername_textView);
            holder.level_text = (TextView)view.findViewById(R.id.userlevel_textView);
            holder.win_text = (TextView)view.findViewById(R.id.win_textView);
            holder.loss_text = (TextView)view.findViewById(R.id.loss_textView);
            holder.request_text = (TextView)view.findViewById(R.id.request_textView);

            view.setTag(holder);
        } else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            holder = (ViewHolder) view.getTag();
        }

        // Bind the data efficiently with the holder.
        //holder.imageButton.setBackgroundResource(R.drawable.symbol);
        holder.name_text.setText(m_playerPrimeInfos.get(i).m_usName);
            short level = m_playerPrimeInfos.get(i).m_wLevel;
            String string = "";
            if(level < 18){
                level = (short) (level + 18);
                string = "Kyu  ";
            }if(level > 18){
                level = (short) (28 - level);
                string = "Prodan  ";
            }
        holder.level_text.setText(String.valueOf(level) + " -" + string);
        holder.win_text.setText("W-" + String.valueOf(m_playerPrimeInfos.get(i).m_nWinCount));
        holder.loss_text.setText("L-" + String.valueOf(m_playerPrimeInfos.get(i).m_nLoseCount));
            byte request = m_playerPrimeInfos.get(i).m_byRefuseReqGame;
            String string_request = "";
            if(request == 0){
                string_request = "  Enable  ";
            }
            else {
                string_request = "  Disable    ";
            }
        holder.request_text.setText(string_request);

        return view;
    }

    static class ViewHolder {
        ImageView imageButton;
        TextView name_text;
        TextView level_text;
        TextView win_text;
        TextView loss_text;
        TextView request_text;
    }

}
