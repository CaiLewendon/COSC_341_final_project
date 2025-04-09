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
import java.util.List;

public class PlayerListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PlayerAdapter adapter;
    private List<Player> playerList;
    private PlayerDataStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_list);

        recyclerView = findViewById(R.id.recyclerViewPlayers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // read players from local storage
        storage = new PlayerDataStorage(this);
        playerList = storage.readPlayers();

        adapter = new PlayerAdapter(playerList);
        recyclerView.setAdapter(adapter);
    }

    private class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {

        private List<Player> players;

        public PlayerAdapter(List<Player> players) {
            this.players = players;
        }

        @Override
        public PlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(PlayerListActivity.this)
                    .inflate(R.layout.item_player, parent, false);
            return new PlayerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(PlayerViewHolder holder, int position) {
            Player player = players.get(position);
            holder.bind(player);
        }

        @Override
        public int getItemCount() {
            return players.size();
        }

        class PlayerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView tvPlayerName;
            TextView tvReviewSummary;
            ImageView ivPlayerAvatar;

            public PlayerViewHolder(View itemView) {
                super(itemView);
                tvPlayerName = itemView.findViewById(R.id.tvPlayerName);
                tvReviewSummary = itemView.findViewById(R.id.tvReviewSummary);
                ivPlayerAvatar = itemView.findViewById(R.id.ivPlayerAvatar);
                itemView.setOnClickListener(this);
            }

            public void bind(Player player) {
                tvPlayerName.setText(player.getName());
                tvReviewSummary.setText(player.getReviewSummary());
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Player clickedPlayer = players.get(position);
                    // start PlayerDetailActivity  and pass player details as needed
                    Intent intent = new Intent(PlayerListActivity.this, PlayerDetailActivity.class);
                    intent.putExtra("playerName", clickedPlayer.getName());
                    //  can pass additional data (e.g. reviews) or reload the storage in the detail activity.
                    startActivity(intent);
                }
            }
        }
    }
}