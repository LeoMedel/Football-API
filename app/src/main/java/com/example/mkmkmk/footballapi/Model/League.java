package com.example.mkmkmk.footballapi.Model;

/**
 * Created by mkmkmk on 02/06/2018.
 */

public class League {

    private String league;
    private String year;
    private int numberGames;
    private int numberTeams;

    private String urlAllTeams;
    private String urlAllGames;
    private String urlTable;


    public League(String urlTable, String urlAllGames, String urlAllTeams, String league, String year, int numberGames, int numberTeams )
    {
        this.urlTable = urlTable;
        this.urlAllGames = urlAllGames;
        this.urlAllTeams = urlAllTeams;
        this.league = league;
        this.year = year;
        this.numberGames = numberGames;
        this.numberTeams = numberTeams;
    }


    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getNumberGames() {
        return numberGames;
    }

    public void setNumberGames(int numberGames) {
        this.numberGames = numberGames;
    }

    public int getNumberTeams() {
        return numberTeams;
    }

    public void setNumberTeams(int numberTeams) {
        this.numberTeams = numberTeams;
    }

    public String getUrlAllTeams() {
        return urlAllTeams;
    }

    public void setUrlAllTeams(String urlAllTeams) {
        this.urlAllTeams = urlAllTeams;
    }

    public String getUrlAllGames() {
        return urlAllGames;
    }

    public void setUrlAllGames(String urlAllGames) {
        this.urlAllGames = urlAllGames;
    }

    public String getUrlTable() {
        return urlTable;
    }

    public void setUrlTable(String urlTable) {
        this.urlTable = urlTable;
    }
}
