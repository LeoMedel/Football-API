package com.example.mkmkmk.footballapi.Model;

/**
 * Created by mkmkmk on 03/06/2018.
 */

public class Player {

    private String playerName;
    private String positionPlayer;
    private String jerseyNumber;
    private String birthday;
    private String nationality;
    private String contract;

    public Player(String playerName, String positionPlayer, String jerseyNumber, String birthday, String nationality, String contract) {
        this.playerName = playerName;
        this.positionPlayer = positionPlayer;
        this.jerseyNumber = jerseyNumber;
        this.birthday = birthday;
        this.nationality = nationality;
        this.contract = contract;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPositionPlayer() {
        return positionPlayer;
    }

    public void setPositionPlayer(String positionPlayer) {
        this.positionPlayer = positionPlayer;
    }

    public String getJerseyNumber() {
        return jerseyNumber;
    }

    public void setJerseyNumber(String jerseyNumber) {
        this.jerseyNumber = jerseyNumber;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }
}

