package com.example.final_project;

import java.util.List;

public class Player {
    private String name;
    private List<Review> reviews;

    public Player(String name, List<Review> reviews) {
        this.name = name;
        this.reviews = reviews;
    }

    public String getName() {
        return name;
    }

    public List<Review> getReviews() {
        return reviews;
    }


    public String getReviewSummary() {
        if (reviews == null || reviews.isEmpty()) {
            return "No Reviews";
        }
        double total = 0;
        for (Review review : reviews) {
            total += review.getRating();
        }
        double avg = total / reviews.size();
        return String.format("%.1f ★ (%d Reviews)", avg, reviews.size());
    }

    public double getAverageRating() {
        if (reviews == null || reviews.size() == 0) return 0;
        double total = 0;
        for (Review review : reviews) {
            total += review.getRating();
        }
        return total / reviews.size();
    }

}