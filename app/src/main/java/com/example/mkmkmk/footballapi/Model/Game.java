package com.example.mkmkmk.footballapi.Model;

/**
 * Created by mkmkmk on 03/06/2018.
 */

public class Game {

    private String status;
    private String matchDay;
    private String homeTeam;
    private String awayTeam;

    private String goalsHome;
    private String goalsAway;


    public Game(String status, String matchDay, String homeTeam, String awayTeam, String goalsHome, String goalsAway) {
        this.status = status;
        this.matchDay = matchDay;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.goalsHome = goalsHome;
        this.goalsAway = goalsAway;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMatchDay() {
        return matchDay;
    }

    public void setMatchDay(String matchDay) {
        this.matchDay = matchDay;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public String getGoalsHome() {
        return goalsHome;
    }

    public void setGoalsHome(String goalsHome) {
        this.goalsHome = goalsHome;
    }

    public String getGoalsAway() {
        return goalsAway;
    }

    public void setGoalsAway(String goalsAway) {
        this.goalsAway = goalsAway;
    }
}
