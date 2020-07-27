package spacecon.tob.bclient.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.Vector;

import spacecon.tob.bclient.Global;
import spacecon.tob.bclient.R;
import spacecon.tob.bclient.common.Common;
import spacecon.tob.bclient.common.PlayerPrimeInfo;
import spacecon.tob.bclient.common.RoomData;

/**
 * Created by HongGukSong on 11/1/2017.
 */

public class RoomList extends BaseAdapter implements Common {

    private Vector<RoomData> m_roominfo;
    private LayoutInflater mInflater;

    public RoomList(Context context) {
        // Cache the LayoutInflate to avoid asking for a new one each time.
        mInflater = LayoutInflater.from(context);
        m_roominfo = Global.m_gameData.m_vecGameList;
    }
    @Override
    public int getCount() {
        return m_roominfo.size();
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

            view = mInflater.inflate(R.layout.room_adapter, null);

            // Creates a ViewHolder and store references to the two children views
            // we want to bind data to.
            holder = new ViewHolder();
            holder.room_number = (TextView)view.findViewById(R.id.aroom_number);
            holder.room_type = (TextView)view.findViewById(R.id.aroom_type);
            holder.room_member = (TextView)view.findViewById(R.id.aroom_member);
            holder.room_white = (TextView)view.findViewById(R.id.aroom_white);
            holder.room_black = (TextView)view.findViewById(R.id.aroom_black);
            holder.room_state = (TextView)view.findViewById(R.id.aroom_state);

            view.setTag(holder);
        } else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            holder = (ViewHolder) view.getTag();
        }
        String roomtype = "";
        String roomState = "";

        switch (m_roominfo.get(i).m_byRoomType){
            case RT_NONE:
                roomtype = "Default";
                break;
            case RT_LEVELINGGAME:
                roomtype = "Leveling Game";
                break;
            case RT_SHOWKIBO:
                roomtype = "Show Kibo";
            default:
                roomtype = "UnKnown";
                break;
        }
        switch (m_roominfo.get(i).m_byState){
            case RS_PREPARE:
                roomState = "Ready game";
                break;
            case RS_STARTNOW:
                roomState = "Beginnig Game";
                break;
            default:
                roomState = "UnKown";
                break;
        }





        holder.room_number.setText(String.valueOf(m_roominfo.get(i).m_wRoomNo));
        holder.room_type.setText(roomtype);
        holder.room_member.setText(String.valueOf(m_roominfo.get(i).m_wCountOfObservers));
        holder.room_white.setText(m_roominfo.get(i).m_usWhitePlayerName +
                "(" + String.valueOf(m_roominfo.get(i).m_wWhitePlayerLevel) + ")");
        holder.room_black.setText(m_roominfo.get(i).m_usBlackPlayerName +
                "("+ String.valueOf(m_roominfo.get(i).m_wBlackPlayerLevel) + ")");
        holder.room_state.setText(roomState);
        // Bind the data efficiently with the holder.



        return view;
    }

    static class ViewHolder {
        TextView room_number;
        TextView room_type;
        TextView room_member;
        TextView room_white;
        TextView room_black;
        TextView room_state;

    }

}