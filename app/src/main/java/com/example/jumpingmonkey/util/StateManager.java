package com.example.jumpingmonkey.util;

public class StateManager {
    private String playerName = "";
    private int lastScore = 0;
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
    public void setLastScore(int lastScore){
        this.lastScore = lastScore;
    }
    public int getLastScore(){
        return this.lastScore;
    }
    public static StateManager getInstance() {
        if(instance == null) instance = new StateManager("");
        return instance;
    }
}
