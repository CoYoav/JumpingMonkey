package com.example.jumpingmonkey.util;

public class StateManager {
    private String playerName = "";
    private static StateManager instance;
    private StateManager(String playerName){
    this.playerName = playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public static StateManager getInstance() {
        if(instance == null) instance = new StateManager("");
        return instance;
    }
}
