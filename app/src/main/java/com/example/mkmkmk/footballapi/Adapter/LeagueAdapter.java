package com.example.mkmkmk.footballapi.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mkmkmk.footballapi.Model.League;
import com.example.mkmkmk.footballapi.R;

import java.util.List;

/**
 * Created by mkmkmk on 02/06/2018.
 */

public class LeagueAdapter extends BaseAdapter{

    List<League> leaguesList;
    Context context;

    public LeagueAdapter(List<League> leaguesList, Context context) {
        this.leaguesList = leaguesList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return leaguesList.size();
    }

    @Override
    public Object getItem(int position) {
        return leaguesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View itemView = view;

        if (view == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inf.inflate(R.layout.league_item, null);
        }

        TextView leagueName = (TextView) itemView.findViewById(R.id.txtLeague);
        leagueName.setText(leaguesList.get(position).getLeague());

        return itemView;
    }
}
