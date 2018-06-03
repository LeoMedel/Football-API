package com.example.mkmkmk.footballapi.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mkmkmk.footballapi.Model.ChampionsTable;
import com.example.mkmkmk.footballapi.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by mkmkmk on 02/06/2018.
 */

public class ChampionsTableAdapter extends BaseAdapter {

    List<ChampionsTable> championsTableList;
    Context context;

    public ChampionsTableAdapter(List<ChampionsTable> championsTableList, Context context) {
        this.championsTableList = championsTableList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return championsTableList.size();
    }

    @Override
    public Object getItem(int position) {
        return championsTableList.get(position);
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
            itemView = inf.inflate(R.layout.group_champions_item, null);
        }

        String[] teamsArray = new String[4];
        String[] urlsIMG = new String[4];

        teamsArray = championsTableList.get(position).getTeams();
        urlsIMG = championsTableList.get(position).getUrlsImg();


        TextView group = (TextView) itemView.findViewById(R.id.txtGroup);
        group.setText("Groupe "+championsTableList.get(position).getGroup());

        //ImageView img1 = (ImageView) itemView.findViewById(R.id.image1);
        //Picasso.with(context).load(urlsIMG[0]).error(R.drawable.teamsimg).into(img1);
        TextView team1 = (TextView) itemView.findViewById(R.id.team1);
        team1.setText(teamsArray[0]);

        //ImageView img2 = (ImageView) itemView.findViewById(R.id.image2);
        //Picasso.with(context).load(urlsIMG[1]).error(R.drawable.teamsimg).into(img2);
        TextView team2 = (TextView) itemView.findViewById(R.id.team2);
        team2.setText(teamsArray[1]);

        //ImageView img3 = (ImageView) itemView.findViewById(R.id.image3);
        //Picasso.with(context).load(urlsIMG[2]).error(R.drawable.teamsimg).into(img3);
        TextView team3 = (TextView) itemView.findViewById(R.id.team3);
        team3.setText(teamsArray[2]);

        if (championsTableList.get(position).getGroup().equals("E"))
        {
            TextView team4 = (TextView) itemView.findViewById(R.id.team4);
            team4.setText("Liverpool");
        }
        else if(championsTableList.get(position).getGroup().equals("H"))
        {
            //ImageView img4 = (ImageView) itemView.findViewById(R.id.image41);
            //Picasso.with(context).load(urlsIMG[3]).error(R.drawable.teamsimg).into(img4);
            TextView team4 = (TextView) itemView.findViewById(R.id.team4);
            team4.setText("Real Madrid");
        }
        else
        {
            //ImageView img4 = (ImageView) itemView.findViewById(R.id.image41);
            //Picasso.with(context).load(urlsIMG[3]).error(R.drawable.teamsimg).into(img4);
            TextView team4 = (TextView) itemView.findViewById(R.id.team4);
            team4.setText(teamsArray[3]);
        }

        return itemView;
    }
}
