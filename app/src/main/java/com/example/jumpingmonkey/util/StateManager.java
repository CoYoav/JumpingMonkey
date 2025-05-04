package com.example.jumpingmonkey.util;

public class StateManager {

    private static StateManager instance;

    private String playerName;
    private int lastScore;

    // Private constructor for singleton pattern
    private StateManager(String playerName) {
        this.playerName = playerName;
        this.lastScore = 0;
    }

    // Singleton access
    public static synchronized StateManager getInstance() {
        if (instance == null) {
            instance = new StateManager("");
        }
        return instance;
    }

    // Getters and setters
    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getLastScore() {
        return lastScore;
    }

    public void setLastScore(int lastScore) {
        this.lastScore = lastScore;
    }
}
