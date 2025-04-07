package com.example.final_project;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class GameDiscoveryFragment extends Fragment {

    private LinearLayout gamesContainer;
    private TextView emptyStateTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_discovery, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gamesContainer = view.findViewById(R.id.games_container);
        emptyStateTextView = view.findViewById(R.id.empty_state_text);
        FloatingActionButton fab = view.findViewById(R.id.fab_add_game);

        fab.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main, new GameCreationFragment())
                    .commit();

            try {
                BottomNavigationView bottomNavigationView =
                        ((MainActivity) getActivity()).findViewById(R.id.bottom_navigation);
                bottomNavigationView.setSelectedItemId(R.id.nav_creation);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        loadGames();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadGames();
    }

    private void loadGames() {
        gamesContainer.removeAllViews();

        List<Game> games = GameStore.getInstance().getGames();

        if (games.isEmpty()) {
            emptyStateTextView.setVisibility(View.VISIBLE);
        } else {
            emptyStateTextView.setVisibility(View.GONE);

            for (Game game : games) {
                View cardView = LayoutInflater.from(getContext())
                        .inflate(R.layout.item_game_card, gamesContainer, false);

                ImageView gameImage = cardView.findViewById(R.id.game_image);
                TextView gameTitle = cardView.findViewById(R.id.game_title);
                TextView gameHost = cardView.findViewById(R.id.game_host);
                TextView gameLocation = cardView.findViewById(R.id.game_location);
                TextView gameTime = cardView.findViewById(R.id.game_time);

                // Check if game has resource image or URI
                if (game.hasImageResource()) {
                    // Set image from resource ID
                    gameImage.setImageResource(game.getImageResourceId());
                } else if (game.getImageUri() != null) {
                    // Handle URI image if any old games still use it
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                                getActivity().getContentResolver(),
                                game.getImageUri()
                        );
                        gameImage.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                gameTitle.setText(game.getSportType());
                gameHost.setText("Host: " + game.getHostName());
                gameLocation.setText("Location: " + game.getLocation());
                gameTime.setText("Date & Time: " +
                        new SimpleDateFormat("MMM dd, yyyy - hh:mm a", Locale.getDefault())
                                .format(game.getDateTime())
                );

                gamesContainer.addView(cardView);
            }
        }
    }
}