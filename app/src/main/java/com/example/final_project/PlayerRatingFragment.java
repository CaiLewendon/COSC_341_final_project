package com.example.final_project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PlayerRatingFragment extends Fragment {

    // UI stufff
    private LinearLayout playerListLayout;
    private LinearLayout playerDetailLayout;
    private RecyclerView recyclerViewPlayers;
    private RecyclerView recyclerViewReviews;
    private TextView tvPlayerNameDetail;
    private ImageView ivPlayerAvatarDetail;
    private Button btnAddReview;
    private ImageView btnBackToList;
    private SearchView searchViewPlayers;

    // Data and Adapter
    private List<Player> playerList; //pull player list from file
    private PlayerAdapter playerAdapter; // adapter for players
    private List<Review> reviewList; // reviews for the selected player
    private Player currentPlayer;   //      Currently selected player
    private PlayerDataStorage storage;  //file storage helper

    // a Request code for launching the review activity
    private static final int REQUEST_CODE_LEAVE_REVIEW = 1;

    public PlayerRatingFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //inflate the combined layout
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
        searchViewPlayers = view.findViewById(R.id.searchViewPlayers);

        //inhit storage helper and load players from the local file
        storage = new PlayerDataStorage(getContext());
        playerList = storage.readPlayers();

        // sort players by average rating highest to lowest
        Collections.sort(playerList, new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                return Double.compare(p2.getAverageRating(), p1.getAverageRating());
            }
        });

        // players list using a RecyclerView
        playerAdapter = new PlayerAdapter(playerList);
        recyclerViewPlayers.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewPlayers.setAdapter(playerAdapter);

        // Setup search view to filter players by name
        searchViewPlayers.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterPlayers(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterPlayers(newText);
                return false;
            }
        });

        //Initially show the player list view
        showPlayerList();

        //a back button to return to player list view
        btnBackToList.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showPlayerList();
            }
        });

        // the Add Review button to open the review submission stuff
        btnAddReview.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (currentPlayer != null) {
                    // Launch LeaveReviewActivity passing the player's name.
                    Intent intent = new Intent(getContext(), LeaveReviewActivity.class);
                    intent.putExtra("playerName", currentPlayer.getName());
                    startActivityForResult(intent, REQUEST_CODE_LEAVE_REVIEW);
                }
            }
        });
    }

    // filter for players by name using the search
    private void filterPlayers(String query) {
        List<Player> filteredList = new ArrayList<>();
        for (Player player : playerList) {
            if (player.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(player);
            }
        }
        // Update adapter with the filtered list
        playerAdapter.updatePlayers(filteredList);
    }

    // Handle the result from LeaveReviewActivity
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_LEAVE_REVIEW && resultCode == Activity.RESULT_OK) {
            // get review details from the returned intent
            String reviewerName = data.getStringExtra("reviewerName");
            int rating = data.getIntExtra("rating", 0);
            String reviewText = data.getStringExtra("reviewText");
            String timestamp = data.getStringExtra("timestamp");

            // Create  new review and add it to the current player's reviews list
            Review newReview = new Review(reviewerName, rating, reviewText, timestamp);
            currentPlayer.getReviews().add(newReview);

            // Update the stored players list
            storage.writePlayers(playerList);

            // refresh the review adapter.
            recyclerViewReviews.getAdapter().notifyDataSetChanged();
        }
    }

    //the player list view and hides the detail view
    private void showPlayerList() {
        // read the players from storage for most up to date info
        playerList = storage.readPlayers();
        Collections.sort(playerList, new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                return Double.compare(p2.getAverageRating(), p1.getAverageRating());
            }
        });
        playerAdapter.updatePlayers(playerList);

        // Switch to the list view
        playerListLayout.setVisibility(View.VISIBLE);
        playerDetailLayout.setVisibility(View.GONE);
    }

    // detail view for the selected player and loads reviews
    private void showPlayerDetail(Player player) {
        currentPlayer = player;
        playerListLayout.setVisibility(View.GONE);
        playerDetailLayout.setVisibility(View.VISIBLE);

        // Set the player details in the detail view
        tvPlayerNameDetail.setText(player.getName());

        // getreviews from the player object
        List<Review> reviews = player.getReviews();
        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewReviews.setAdapter(new ReviewAdapter(reviews));
    }

    // Adapter for displaying the list of players
    private class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {
        private List<Player> players;

        public PlayerAdapter(List<Player> players) {
            this.players = players;
        }

        // change the list with new data for search filtering
        public void updatePlayers(List<Player> newPlayers) {
            this.players = newPlayers;
            notifyDataSetChanged();
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
                    // When  player clicked, show their detail view and reviews
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

    //adapter for displaying reviews in the detail view
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