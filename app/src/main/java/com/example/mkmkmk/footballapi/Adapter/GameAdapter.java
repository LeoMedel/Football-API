package com.example.mkmkmk.footballapi.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mkmkmk.footballapi.Model.Game;
import com.example.mkmkmk.footballapi.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * Created by mkmkmk on 03/06/2018.
 */

public class GameAdapter extends BaseAdapter {

    List<Game> gameList = new ArrayList<>();
    Context context;

    public GameAdapter(List<Game> gameList, Context context) {
        this.gameList = gameList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return gameList.size();
    }

    @Override
    public Object getItem(int position) {
        return gameList.get(position);
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
            itemView = inf.inflate(R.layout.game_item, null);
        }

        TextView matchDay = (TextView) itemView.findViewById(R.id.txtMatchday);
        matchDay.setText("Matchday  "+gameList.get(position).getMatchDay());

        TextView teamHome = (TextView) itemView.findViewById(R.id.txtTeamHome);
        teamHome.setText(gameList.get(position).getHomeTeam());

        TextView teamAway = (TextView) itemView.findViewById(R.id.txtTeamAway);
        teamAway.setText(gameList.get(position).getAwayTeam());

        TextView goalsHome = (TextView) itemView.findViewById(R.id.txtGoalsHome);
        goalsHome.setText(gameList.get(position).getGoalsHome());

        TextView goalsAway = (TextView) itemView.findViewById(R.id.txtGoalsAway);
        goalsAway.setText(gameList.get(position).getGoalsAway());

        TextView status = (TextView) itemView.findViewById(R.id.txtStatus);
        status.setText(gameList.get(position).getStatus());

        return itemView;

    }
}
