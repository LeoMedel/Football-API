package com.example.mkmkmk.footballapi.Model;

/**
 * Created by mkmkmk on 02/06/2018.
 */

public class TeamPosition {

    private String teamName;
    private int position;
    private int numberGames;
    private int points;
    private int goals;
    private int gamesWin;
    private int gamesDraws;
    private int gamesLosses;

    public TeamPosition(String teamName, int position, int numberGames, int points, int goals, int gamesWin, int gamesDraws, int gamesLosses) {
        this.teamName = teamName;
        this.position = position;
        this.numberGames = numberGames;
        this.points = points;
        this.goals = goals;
        this.gamesWin = gamesWin;
        this.gamesDraws = gamesDraws;
        this.gamesLosses = gamesLosses;
    }


    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getNumberGames() {
        return numberGames;
    }

    public void setNumberGames(int numberGames) {
        this.numberGames = numberGames;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getGoals() {
        return goals;
    }

    public void setGoals(int goals) {
        this.goals = goals;
    }

    public int getGamesWin() {
        return gamesWin;
    }

    public void setGamesWin(int gamesWin) {
        this.gamesWin = gamesWin;
    }

    public int getGamesDraws() {
        return gamesDraws;
    }

    public void setGamesDraws(int gamesDraws) {
        this.gamesDraws = gamesDraws;
    }

    public int getGamesLosses() {
        return gamesLosses;
    }

    public void setGamesLosses(int gamesLosses) {
        this.gamesLosses = gamesLosses;
    }
}
