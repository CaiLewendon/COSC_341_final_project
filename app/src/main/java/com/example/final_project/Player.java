package com.example.final_project;

public class Player {
    private String name;
    private String reviewSummary;

    public Player(String name, String reviewSummary) {
        this.name = name;
        this.reviewSummary = reviewSummary;
    }

    public String getName() {
        return name;
    }

    public String getReviewSummary() {
        return reviewSummary;
    }
}