package spacecon.tob.bclient.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Vector;

import spacecon.tob.bclient.Global;
import spacecon.tob.bclient.R;
import spacecon.tob.bclient.common.Common;
import spacecon.tob.bclient.common.PlayerPrimeInfo;
import spacecon.tob.bclient.object.GameKiboRecord;

/**
 * Created by HongGukSong on 11/1/2017.
 */



public class KiboList extends BaseAdapter implements Common {

    private Vector<GameKiboRecord> m_kibolist;
    private LayoutInflater mInflater;

    public KiboList(Context context) {
        // Cache the LayoutInflate to avoid asking for a new one each time.
        mInflater = LayoutInflater.from(context);
        m_kibolist = Global.m_kibolist;
    }

    @Override
    public int getCount() {
        return m_kibolist.size();
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

            view = mInflater.inflate(R.layout.kibo_adapter, null);

            // Creates a ViewHolder and store references to the two children views
            // we want to bind data to.
            holder = new ViewHolder();
            holder.akibo_num = (TextView)view.findViewById(R.id.akibo_num);
            holder.akibo_date = (TextView)view.findViewById(R.id.akibo_date);
            holder.akibo_boardtype = (TextView)view.findViewById(R.id.akibo_boardtype);
            holder.akibo_wplayer = (TextView)view.findViewById(R.id.akibo_wplayer);
            holder.akibo_bplayer = (TextView)view.findViewById(R.id.akibo_bplayer);
            holder.akibo_type = (TextView)view.findViewById(R.id.akibo_type);

            view.setTag(holder);
        } else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            holder = (ViewHolder) view.getTag();
        }

        // Bind the data efficiently with the holder.
        holder.akibo_num.setText(String.valueOf(m_kibolist.get(i).m_nKiboID));
        holder.akibo_date.setText(m_kibolist.get(i).m_usKiboTime);
        holder.akibo_boardtype.setText(String.valueOf(m_kibolist.get(i).m_byBoardersize));
        holder.akibo_wplayer.setText(m_kibolist.get(i).m_usWhitePlayerName);
        holder.akibo_bplayer.setText(m_kibolist.get(i).m_usBlackPlayerName);
        int type = m_kibolist.get(i).m_byGameType;
        String string = "";
        switch (type){
            case RT_LEVELINGGAME:
                string = "Leveling Game";
                break;
            default:
                string = "Unknown";
                break;
        }
        holder.akibo_type.setText(string);


        return view;
    }

    static class ViewHolder {
        TextView akibo_num;
        TextView akibo_date;
        TextView akibo_boardtype;
        TextView akibo_wplayer;
        TextView akibo_bplayer;
        TextView akibo_type;
    }

}
