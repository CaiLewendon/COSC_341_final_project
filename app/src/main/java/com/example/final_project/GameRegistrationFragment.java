package com.example.final_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class GameRegistrationFragment extends Fragment {
    private boolean hasRegistered = false;
    private Game selectedGame;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_game_registration, container, false);

        if (getArguments() != null) {
            selectedGame = (Game) getArguments().getSerializable("selected_game");

            ImageView imageView = view.findViewById(R.id.imageView);
            imageView.setImageResource(selectedGame.getImageResourceId());

            TextView gameTitle = view.findViewById(R.id.gameTitleText);
            gameTitle.setText(selectedGame.getLocation());

            TextView distanceText = view.findViewById(R.id.distanceTextView);
            distanceText.setText("~1.2 km away");

            TextView tvRegistration = view.findViewById(R.id.tvRegistration);

            long hoursUntilGame = getHoursUntil(selectedGame.getDateTime());

            tvRegistration.setText(
                    "Sport: " + selectedGame.getSportType() + "\n" +
                            "Starts in: " + hoursUntilGame + " hrs\n" +
                            "Players Needed: " + selectedGame.getPlayersNeeded()
            );

            TextView secondHalfText = view.findViewById(R.id.secondHalfTextView);
            secondHalfText.setText(
                    "Game Host: " + selectedGame.getHostName() + "\n\n" +
                            "Description:\n" + selectedGame.getDescription()
            );
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button returnButton = view.findViewById(R.id.returnButton);
        Button mapViewButton = view.findViewById(R.id.mapViewButton);
        Button registerButton = view.findViewById(R.id.btn_register);

        TextView tvRegistration = view.findViewById(R.id.tvRegistration);

        returnButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main, new GameDiscoveryFragment())
                    .commit();
        });

        mapViewButton.setOnClickListener(v -> {
            if (selectedGame != null && selectedGame.getLocation() != null) {
                Intent intent = new Intent(requireContext(), MapsActivity.class);
                intent.putExtra("location", selectedGame.getLocation());
                intent.putExtra("markerTitle", "Players Needed: " + selectedGame.getPlayersNeeded());
                startActivity(intent);
            }
        });

        registerButton.setOnClickListener(v -> {
            if (!hasRegistered) {
                hasRegistered = true;

                int playersLeft = selectedGame.getPlayersNeeded();
                if (playersLeft > 0) {
                    selectedGame.setPlayersNeeded(playersLeft - 1);
                }

                long hoursUntilGame = getHoursUntil(selectedGame.getDateTime());

                // Update top TextView
                tvRegistration.setText(
                        "Sport: " + selectedGame.getSportType() + "\n" +
                                "Starts in: " + hoursUntilGame + " hrs (" +
                                new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(selectedGame.getDateTime()) + ")\n" +
                                "Players Needed: " + selectedGame.getPlayersNeeded()
                );

                registerButton.setEnabled(false);
                Toast.makeText(getContext(), "You registered for the game!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private long getHoursUntil(Date gameDateTime) {
        long diffMillis = gameDateTime.getTime() - System.currentTimeMillis();
        return TimeUnit.MILLISECONDS.toHours(diffMillis);
    }
}