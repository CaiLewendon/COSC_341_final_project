package com.example.final_project;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class PlayerDataStorage {
    private Context context;
    private static final String FILE_NAME = "players.json";

    public PlayerDataStorage(Context context) {
        this.context = context;
    }

    // reads the list of players from internal storage
    public List<Player> readPlayers() {
        List<Player> players = new ArrayList<>();
        try {
            FileInputStream fis = context.openFileInput(FILE_NAME);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
            fis.close();
            String jsonStr = baos.toString();
            JSONArray playersArray = new JSONArray(jsonStr);
            for (int i = 0; i < playersArray.length(); i++) {
                JSONObject pObj = playersArray.getJSONObject(i);
                String name = pObj.getString("name");
                List<Review> reviews = new ArrayList<>();
                JSONArray reviewsArray = pObj.optJSONArray("reviews");
                if (reviewsArray != null) {
                    for (int j = 0; j < reviewsArray.length(); j++) {
                        JSONObject rObj = reviewsArray.getJSONObject(j);
                        String reviewerName = rObj.getString("reviewerName");
                        int rating = rObj.getInt("rating");
                        String reviewText = rObj.getString("reviewText");
                        String timestamp = rObj.getString("timestamp");
                        reviews.add(new Review(reviewerName, rating, reviewText, timestamp));
                    }
                }
                players.add(new Player(name, reviews));
            }
        } catch (FileNotFoundException e) {
            // File not found, create dummy players
            players = getDummyPlayers();
            writePlayers(players);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return players;
    }

    // saves the provided list of players back to the file
    public void writePlayers(List<Player> players) {
        try {
            JSONArray playersArray = new JSONArray();
            for (Player player : players) {
                JSONObject pObj = new JSONObject();
                pObj.put("name", player.getName());
                JSONArray reviewsArray = new JSONArray();
                for (Review review : player.getReviews()) {
                    JSONObject rObj = new JSONObject();
                    rObj.put("reviewerName", review.getReviewerName());
                    rObj.put("rating", review.getRating());
                    rObj.put("reviewText", review.getReviewText());
                    rObj.put("timestamp", review.getTimestamp());
                    reviewsArray.put(rObj);
                }
                pObj.put("reviews", reviewsArray);
                playersArray.put(pObj);
            }
            String jsonString = playersArray.toString();
            FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            fos.write(jsonString.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Returns dummy data if no file exists yet, ie everything is emptoy
    private List<Player> getDummyPlayers() {
        List<Player> players = new ArrayList<>();
        players.add(new Player("Player One", new ArrayList<Review>()));
        players.add(new Player("Player Two", new ArrayList<Review>()));
        players.add(new Player("Player Three", new ArrayList<Review>()));
        return players;
    }
}