package com.example.mkmkmk.footballapi.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mkmkmk.footballapi.Model.TeamPosition;
import com.example.mkmkmk.footballapi.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mkmkmk on 02/06/2018.
 */

public class TableAdapter extends BaseAdapter {

    List<TeamPosition> positionList = new ArrayList<>();
    Context context;

    public TableAdapter(List<TeamPosition> positionList, Context context) {
        this.positionList = positionList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return positionList.size();
    }

    @Override
    public Object getItem(int position) {
        return positionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int positionIndex, View view, ViewGroup viewGroup) {

        View itemView = view;

        if (view == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inf.inflate(R.layout.table_item, null);
        }

        TextView position = (TextView) itemView.findViewById(R.id.position);
        position.setText(""+positionList.get(positionIndex).getPosition());

        TextView team = (TextView) itemView.findViewById(R.id.team);
        team.setText(positionList.get(positionIndex).getTeamName());

        TextView games = (TextView) itemView.findViewById(R.id.nGames);
        games.setText(""+ positionList.get(positionIndex).getNumberGames());

        TextView goals = (TextView) itemView.findViewById(R.id.goals);
        goals.setText(""+ positionList.get(positionIndex).getGoals());

        TextView gamesWins = (TextView) itemView.findViewById(R.id.winner);
        gamesWins.setText(""+ positionList.get(positionIndex).getGamesWin());

        TextView gamesLoser = (TextView) itemView.findViewById(R.id.loser);
        gamesLoser.setText(""+ positionList.get(positionIndex).getGamesLosses());

        TextView gamesDraws = (TextView) itemView.findViewById(R.id.draws);
        gamesDraws.setText(""+ positionList.get(positionIndex).getGamesDraws());

        TextView points = (TextView) itemView.findViewById(R.id.points);
        points.setText(""+ positionList.get(positionIndex).getPoints());

        return itemView;
    }
}
