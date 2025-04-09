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
        // Dummy JSON data containing 20 players with reviews, this is just the initial values, the data will persist with any changes made too it, this is just so the fields are populated
        // !!!I REPEAT, THIS IS JUST INIT VALUES, ALL DATA IS PERSISTENT AND CHANGEABLE!!!
        String dummyJson = "[" +
                "{\"name\":\"Michael Johnson\",\"reviews\":[{\"reviewerName\":\"Alice\",\"rating\":5,\"reviewText\":\"Amazing performance!\",\"timestamp\":\"2023-04-01\"},{\"reviewerName\":\"Bob\",\"rating\":4,\"reviewText\":\"Really impressive.\",\"timestamp\":\"2023-04-01\"}]}," +
                "{\"name\":\"Christopher Smith\",\"reviews\":[{\"reviewerName\":\"Charlie\",\"rating\":3,\"reviewText\":\"Not the best, but okay.\",\"timestamp\":\"2023-04-01\"}]}," +
                "{\"name\":\"James Williams\",\"reviews\":[{\"reviewerName\":\"Diana\",\"rating\":2,\"reviewText\":\"Could use improvement.\",\"timestamp\":\"2023-04-01\"},{\"reviewerName\":\"Edward\",\"rating\":3,\"reviewText\":\"Average performance.\",\"timestamp\":\"2023-04-01\"}]}," +
                "{\"name\":\"Patricia Brown\",\"reviews\":[{\"reviewerName\":\"Fiona\",\"rating\":5,\"reviewText\":\"Outstanding skills!\",\"timestamp\":\"2023-04-01\"}]}," +
                "{\"name\":\"Linda Jones\",\"reviews\":[{\"reviewerName\":\"George\",\"rating\":4,\"reviewText\":\"Solid performance.\",\"timestamp\":\"2023-04-01\"},{\"reviewerName\":\"Helen\",\"rating\":4,\"reviewText\":\"Very consistent.\",\"timestamp\":\"2023-04-01\"}]}," +
                "{\"name\":\"Barbara Garcia\",\"reviews\":[{\"reviewerName\":\"Ian\",\"rating\":3,\"reviewText\":\"Average.\",\"timestamp\":\"2023-04-01\"}]}," +
                "{\"name\":\"Elizabeth Martinez\",\"reviews\":[{\"reviewerName\":\"Jack\",\"rating\":4,\"reviewText\":\"Good player.\",\"timestamp\":\"2023-04-01\"},{\"reviewerName\":\"Karen\",\"rating\":5,\"reviewText\":\"Excellent!\",\"timestamp\":\"2023-04-01\"}]}," +
                "{\"name\":\"Jennifer Rodriguez\",\"reviews\":[{\"reviewerName\":\"Leo\",\"rating\":2,\"reviewText\":\"Not too impressive.\",\"timestamp\":\"2023-04-01\"}]}," +
                "{\"name\":\"Maria Hernandez\",\"reviews\":[{\"reviewerName\":\"Megan\",\"rating\":4,\"reviewText\":\"Really good.\",\"timestamp\":\"2023-04-01\"}]}," +
                "{\"name\":\"Susan Lopez\",\"reviews\":[{\"reviewerName\":\"Nathan\",\"rating\":3,\"reviewText\":\"Could be better.\",\"timestamp\":\"2023-04-01\"},{\"reviewerName\":\"Olivia\",\"rating\":3,\"reviewText\":\"Mediocre.\",\"timestamp\":\"2023-04-01\"}]}," +
                "{\"name\":\"Margaret Gonzalez\",\"reviews\":[{\"reviewerName\":\"Paul\",\"rating\":5,\"reviewText\":\"Superb!\",\"timestamp\":\"2023-04-01\"}]}," +
                "{\"name\":\"Dorothy Wilson\",\"reviews\":[{\"reviewerName\":\"Quinn\",\"rating\":2,\"reviewText\":\"Needs improvement.\",\"timestamp\":\"2023-04-01\"}]}," +
                "{\"name\":\"Lisa Anderson\",\"reviews\":[{\"reviewerName\":\"Rachel\",\"rating\":4,\"reviewText\":\"Very talented.\",\"timestamp\":\"2023-04-01\"},{\"reviewerName\":\"Steve\",\"rating\":4,\"reviewText\":\"Consistent.\",\"timestamp\":\"2023-04-01\"}]}," +
                "{\"name\":\"Nancy Thomas\",\"reviews\":[{\"reviewerName\":\"Tracy\",\"rating\":3,\"reviewText\":\"Okay player.\",\"timestamp\":\"2023-04-01\"}]}," +
                "{\"name\":\"Karen Taylor\",\"reviews\":[{\"reviewerName\":\"Uma\",\"rating\":5,\"reviewText\":\"Fantastic!\",\"timestamp\":\"2023-04-01\"}]}," +
                "{\"name\":\"Betty Moore\",\"reviews\":[{\"reviewerName\":\"Victor\",\"rating\":4,\"reviewText\":\"Great skills!\",\"timestamp\":\"2023-04-01\"},{\"reviewerName\":\"Wendy\",\"rating\":5,\"reviewText\":\"Amazing performance!\",\"timestamp\":\"2023-04-01\"}]}," +
                "{\"name\":\"Helen Jackson\",\"reviews\":[{\"reviewerName\":\"Xavier\",\"rating\":3,\"reviewText\":\"Not my favorite.\",\"timestamp\":\"2023-04-01\"}]}," +
                "{\"name\":\"Sandra Martin\",\"reviews\":[{\"reviewerName\":\"Yvonne\",\"rating\":5,\"reviewText\":\"Excellent work!\",\"timestamp\":\"2023-04-01\"}]}," +
                "{\"name\":\"Donna Lee\",\"reviews\":[{\"reviewerName\":\"Zach\",\"rating\":2,\"reviewText\":\"Could be improved.\",\"timestamp\":\"2023-04-01\"}]}," +
                "{\"name\":\"Carol Perez\",\"reviews\":[{\"reviewerName\":\"Alfred\",\"rating\":4,\"reviewText\":\"Solid performance overall.\",\"timestamp\":\"2023-04-01\"},{\"reviewerName\":\"Bianca\",\"rating\":4,\"reviewText\":\"Good effort.\",\"timestamp\":\"2023-04-01\"}]}" +
                "]";

        List<Player> players = new ArrayList<>();
        try {
            JSONArray playersArray = new JSONArray(dummyJson);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return players;
    }
}