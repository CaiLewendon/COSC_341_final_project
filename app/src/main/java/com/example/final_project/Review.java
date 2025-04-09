package com.example.final_project;

public class Review {
    private String reviewerName;
    private int rating;
    private String reviewText;
    private String timestamp;

    public Review(String reviewerName, int rating, String reviewText, String timestamp) {
        this.reviewerName = reviewerName;
        this.rating = rating;
        this.reviewText = reviewText;
        this.timestamp = timestamp;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public int getRating() {
        return rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public String getTimestamp() {
        return timestamp;
    }
}