package com.example.mkmkmk.footballapi.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mkmkmk.footballapi.Model.Team;
import com.example.mkmkmk.footballapi.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mkmkmk on 03/06/2018.
 */

public class TeamAdapter extends BaseAdapter {

    List<Team> teamList = new ArrayList<>();
    Context context;

    public TeamAdapter(Context context, List<Team> teamList) {
        this.teamList = teamList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return teamList.size();
    }

    @Override
    public Object getItem(int position) {
        return teamList.get(position);
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
            itemView = inf.inflate(R.layout.team_item, null);
        }

        TextView teamName = (TextView) itemView.findViewById(R.id.teamItem);
        teamName.setText(teamList.get(position).getName()+" ("+teamList.get(position).getShotName()+")");

        return itemView;
    }
}
