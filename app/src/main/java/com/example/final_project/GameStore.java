package com.example.final_project;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Singleton class to store games in memory (no database required)
 */
public class GameStore {
    private static GameStore instance;
    private List<Game> games;

    private GameStore() {
        games = new ArrayList<>();
    }

    public static synchronized GameStore getInstance() {
        if (instance == null) {
            instance = new GameStore();
        }
        return instance;
    }

    public void addGame(Game game) {
        games.add(game);
    }

    public List<Game> getGames() {
        return games;
    }

    public void clearGames() {
        games.clear();
    }
}

/**
 * Game model class
 */
class Game implements Serializable {
    private String hostName;
    private String sportType;
    private String location;
    private Date dateTime;
    private Uri imageUri;
    private int imageResourceId; // New field for drawable resource ID

    // Original constructor with Uri
    public Game(String hostName, String sportType, String location, Date dateTime, Uri imageUri) {
        this.hostName = hostName;
        this.sportType = sportType;
        this.location = location;
        this.dateTime = dateTime;
        this.imageUri = imageUri;
        this.imageResourceId = -1; // Default value
    }

    // New constructor with resource ID
    public Game(String hostName, String sportType, String location, Date dateTime, int imageResourceId) {
        this.hostName = hostName;
        this.sportType = sportType;
        this.location = location;
        this.dateTime = dateTime;
        this.imageUri = null; // No URI when using resource
        this.imageResourceId = imageResourceId;
    }

    public String getHostName() {
        return hostName;
    }

    public String getSportType() {
        return sportType;
    }

    public String getLocation() {
        return location;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public boolean hasImageResource() {
        return imageResourceId != -1;
    }
}