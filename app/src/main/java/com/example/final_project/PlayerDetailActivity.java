package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class PlayerDetailActivity extends AppCompatActivity {

    private TextView tvPlayerName;
    private ImageView ivPlayerAvatar;
    private RecyclerView recyclerViewReviews;
    private FloatingActionButton fabAddReview;
    private ReviewAdapter reviewAdapter;
    private List<Review> reviewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_detail);

        tvPlayerName = findViewById(R.id.tvPlayerName);
        ivPlayerAvatar = findViewById(R.id.ivPlayerAvatar);
        //recyclerViewReviews = findViewById(R.id.recyclerViewReviews);
        fabAddReview = findViewById(R.id.fabAddReview);

        String playerName = getIntent().getStringExtra("playerName");
        tvPlayerName.setText(playerName);
        // TODO: Load the player's avatar and other details as needed.

        //set up the RecyclerView for reviews
        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(this));

        // these were test reviews, no longer used
        reviewList = new ArrayList<>();
        reviewList.add(new Review("Alice", 4, "Great player, really agile!", "2023-03-01"));
        reviewList.add(new Review("Bob", 5, "Excellent teamwork and skills.", "2023-03-05"));
        reviewList.add(new Review("Charlie", 3, "Average performance overall.", "2023-03-10"));

        reviewAdapter = new ReviewAdapter(reviewList);
        recyclerViewReviews.setAdapter(reviewAdapter);

        // Handle FAB click to add a new review, this is now out of date, no longer used was for initial testing
        fabAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the LeaveReviewActivity and pass the player name.
                Intent intent = new Intent(PlayerDetailActivity.this, LeaveReviewActivity.class);
                intent.putExtra("playerName", playerName);
                startActivity(intent);
            }
        });
    }

    // Adapter for the review RecyclerView. this here is still needed for functionality in listing players
    public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

        private List<Review> reviews;

        public ReviewAdapter(List<Review> reviews) {
            this.reviews = reviews;
        }

        @Override
        public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(PlayerDetailActivity.this)
                    .inflate(R.layout.item_review, parent, false);
            return new ReviewViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ReviewViewHolder holder, int position) {
            Review review = reviews.get(position);
            holder.bind(review);
        }

        @Override
        public int getItemCount() {
            return reviews.size();
        }

        class ReviewViewHolder extends RecyclerView.ViewHolder {
            TextView tvReviewerName;
            TextView tvRating;
            TextView tvReviewText;
            TextView tvTimestamp;

            public ReviewViewHolder(View itemView) {
                super(itemView);
                tvReviewerName = itemView.findViewById(R.id.tvReviewerName);
                tvRating = itemView.findViewById(R.id.tvRating);
                tvReviewText = itemView.findViewById(R.id.tvReviewText);
                tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            }

            public void bind(Review review) {
                tvReviewerName.setText(review.getReviewerName());
                    // q    For simplicity, display the numeric rating with a star symbol
                tvRating.setText(review.getRating() + " ★");
                tvReviewText.setText(review.getReviewText());
                tvTimestamp.setText(review.getTimestamp());
            }
        }
    }
}