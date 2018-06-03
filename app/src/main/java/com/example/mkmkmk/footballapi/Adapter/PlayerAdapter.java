package com.example.mkmkmk.footballapi.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mkmkmk.footballapi.Model.Player;
import com.example.mkmkmk.footballapi.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mkmkmk on 03/06/2018.
 */

public class PlayerAdapter extends BaseAdapter {

    List<Player> playerList = new ArrayList<>();
    Context context;

    public PlayerAdapter(Context context, List<Player> playerList) {
        this.playerList = playerList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return playerList.size();
    }

    @Override
    public Object getItem(int position) {
        return playerList.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        View itemView = view;

        if (view == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inf.inflate(R.layout.player_item, null);
        }

        TextView leagueName = (TextView) itemView.findViewById(R.id.txtPlayer);
        leagueName.setText(playerList.get(position).getPlayerName());

        return itemView;
    }
}
