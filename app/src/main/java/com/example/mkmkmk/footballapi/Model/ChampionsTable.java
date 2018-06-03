package com.example.mkmkmk.footballapi.Model;

/**
 * Created by mkmkmk on 02/06/2018.
 */

public class ChampionsTable {

    private String group;

    private String[] teams = new String[4];
    private String[] urlsImg = new String[4];

    public ChampionsTable(String group, String[] teams, String[] urlsImg) {
        this.group = group;
        this.teams = teams;
        this.urlsImg = urlsImg;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String[] getTeams() {
        return teams;
    }

    public void setTeams(String[] teams) {
        this.teams = teams;
    }

    public String[] getUrlsImg() {
        return urlsImg;
    }

    public void setUrlsImg(String[] urlsImg) {
        this.urlsImg = urlsImg;
    }
}