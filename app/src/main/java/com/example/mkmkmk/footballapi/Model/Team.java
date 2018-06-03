package com.example.mkmkmk.footballapi.Model;

/**
 * Created by mkmkmk on 03/06/2018.
 */

public class Team {

    private String name;
    private String shotName;
    private String urlImageLogo;
    private String urlPlayers;
    private String urlGamesTeam;

    public Team(String name, String shotName, String urlImageLogo, String urlPlayers, String urlGamesTeam) {
        this.name = name;
        this.shotName = shotName;
        this.urlImageLogo = urlImageLogo;
        this.urlPlayers = urlPlayers;
        this.urlGamesTeam = urlGamesTeam;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShotName() {
        return shotName;
    }

    public void setShotName(String shotName) {
        this.shotName = shotName;
    }

    public String getUrlImageLogo() {
        return urlImageLogo;
    }

    public void setUrlImageLogo(String urlImageLogo) {
        this.urlImageLogo = urlImageLogo;
    }

    public String getUrlPlayers() {
        return urlPlayers;
    }

    public void setUrlPlayers(String urlPlayers) {
        this.urlPlayers = urlPlayers;
    }

    public String getUrlGamesTeam() {
        return urlGamesTeam;
    }

    public void setUrlGamesTeam(String urlGamesTeam) {
        this.urlGamesTeam = urlGamesTeam;
    }
}
