package com.example.final_project;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class PlayerRatingFragment extends Fragment {

    // UI containers
    private LinearLayout playerListLayout;
    private LinearLayout playerDetailLayout;
    private RecyclerView recyclerViewPlayers;
    private RecyclerView recyclerViewReviews;
    private TextView tvPlayerNameDetail;
    private ImageView ivPlayerAvatarDetail;
    private Button btnAddReview;
    private ImageView btnBackToList;

    // Data lists
    private List<Player> playerList;
    private List<Review> reviewList; // reviews for the selected player

    public PlayerRatingFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the combined layout
        return inflater.inflate(R.layout.fragment_player_rating, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Initialize UI elements from the layout
        playerListLayout = view.findViewById(R.id.playerListLayout);
        playerDetailLayout = view.findViewById(R.id.playerDetailLayout);
        recyclerViewPlayers = view.findViewById(R.id.recyclerViewPlayers);
        recyclerViewReviews = view.findViewById(R.id.recyclerViewReviews);
        tvPlayerNameDetail = view.findViewById(R.id.tvPlayerName);
        ivPlayerAvatarDetail = view.findViewById(R.id.ivPlayerAvatar);
        btnAddReview = view.findViewById(R.id.btnAddReview);
        btnBackToList = view.findViewById(R.id.btnBackToList);

        // Setup back button to return to player list
        btnBackToList.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showPlayerList();
            }
        });

        // Setup players list with dummy data
        playerList = new ArrayList<>();
        playerList.add(new Player("Player One", "4.5 ★ (20 Reviews)"));
        playerList.add(new Player("Player Two", "3.8 ★ (15 Reviews)"));
        playerList.add(new Player("Player Three", "5.0 ★ (30 Reviews)"));

        recyclerViewPlayers.setLayoutManager(new LinearLayoutManager(getContext()));
        PlayerAdapter playerAdapter = new PlayerAdapter(playerList);
        recyclerViewPlayers.setAdapter(playerAdapter);

        // Initially show the player list
        showPlayerList();

        // Set up the Add Review button (you can later open another fragment or dialog for review submission)
        btnAddReview.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Here you might open a review submission dialog or fragment.
                // For demonstration, we'll just add a dummy review and refresh the reviews list.
                if (reviewList != null) {
                    reviewList.add(new Review("New Reviewer", 4, "This is a new review.", "2023-04-01"));
                    recyclerViewReviews.getAdapter().notifyDataSetChanged();
                }
            }
        });
    }

    // Show player list view and hide detail view
    private void showPlayerList() {
        playerListLayout.setVisibility(View.VISIBLE);
        playerDetailLayout.setVisibility(View.GONE);
    }

    // Show player detail view for the selected player and load reviews dynamically
    private void showPlayerDetail(Player player) {
        playerListLayout.setVisibility(View.GONE);
        playerDetailLayout.setVisibility(View.VISIBLE);

        // Set player details in the detail view
        tvPlayerNameDetail.setText(player.getName());
        // For the avatar, you could load an image here (using an image loading library, etc.)

        // Create dummy reviews for demonstration
        reviewList = new ArrayList<>();
        reviewList.add(new Review("Alice", 4, "Great player!", "2023-03-01"));
        reviewList.add(new Review("Bob", 5, "Excellent performance.", "2023-03-05"));
        reviewList.add(new Review("Charlie", 3, "Could improve.", "2023-03-10"));

        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(getContext()));
        ReviewAdapter reviewAdapter = new ReviewAdapter(reviewList);
        recyclerViewReviews.setAdapter(reviewAdapter);
    }

    // Adapter for displaying the list of players
    private class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {
        private List<Player> players;

        public PlayerAdapter(List<Player> players) {
            this.players = players;
        }

        @NonNull
        @Override
        public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_player, parent, false);
            return new PlayerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
            final Player player = players.get(position);
            holder.bind(player);
            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    // When a player is clicked, show the detailed view with reviews
                    showPlayerDetail(player);
                }
            });
        }

        @Override
        public int getItemCount() {
            return players.size();
        }

        class PlayerViewHolder extends RecyclerView.ViewHolder {
            TextView tvPlayerName;
            TextView tvReviewSummary;

            public PlayerViewHolder(@NonNull View itemView) {
                super(itemView);
                tvPlayerName = itemView.findViewById(R.id.tvPlayerName);
                tvReviewSummary = itemView.findViewById(R.id.tvReviewSummary);
            }

            public void bind(Player player) {
                tvPlayerName.setText(player.getName());
                tvReviewSummary.setText(player.getReviewSummary());
            }
        }
    }

    // Adapter for displaying reviews in the detail view
    private class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
        private List<Review> reviews;

        public ReviewAdapter(List<Review> reviews) {
            this.reviews = reviews;
        }

        @NonNull
        @Override
        public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_review, parent, false);
            return new ReviewViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
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

            public ReviewViewHolder(@NonNull View itemView) {
                super(itemView);
                tvReviewerName = itemView.findViewById(R.id.tvReviewerName);
                tvRating = itemView.findViewById(R.id.tvRating);
                tvReviewText = itemView.findViewById(R.id.tvReviewText);
                tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            }

            public void bind(Review review) {
                tvReviewerName.setText(review.getReviewerName());
                tvRating.setText(review.getRating() + " ★");
                tvReviewText.setText(review.getReviewText());
                tvTimestamp.setText(review.getTimestamp());
            }
        }
    }
}