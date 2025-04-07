package com.example.final_project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Date;

public class GameRegistrationFragment extends Fragment {

    private Game selectedGame;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_game_registration, container, false);

        if (getArguments() != null) {
            selectedGame = (Game) getArguments().getSerializable("selected_game");


            TextView gameTitle = view.findViewById(R.id.gameTitleText);
            gameTitle.setText(selectedGame.getLocation());

            TextView tvRegistration = view.findViewById(R.id.tvRegistration);
            tvRegistration.setText(
                    "Sport: " + selectedGame.getSportType() + "\n" +
                            "Host: " + selectedGame.getHostName() + "\n" +
                            "Date & Time: " + new SimpleDateFormat("MMM dd, yyyy - hh:mm a", Locale.getDefault())
                            .format(selectedGame.getDateTime()) + "\n" +
                            "Description: " + selectedGame.getDescription() + "\n" +
                            "Players Needed: " + selectedGame.getPlayersNeeded()
            );

            ImageView imageView = view.findViewById(R.id.imageView);
            imageView.setImageResource(selectedGame.getImageResourceId());
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button registerButton = view.findViewById(R.id.btn_register);
        registerButton.setOnClickListener(v -> {
            Toast.makeText(getContext(), "You registered for the game!", Toast.LENGTH_SHORT).show();
        });

        Button returnButton = view.findViewById(R.id.returnButton);
        returnButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main, new GameDiscoveryFragment())
                    .commit();
        });

        Button mapViewButton = view.findViewById(R.id.mapViewButton);
        mapViewButton.setOnClickListener(v -> {
            if (selectedGame != null && selectedGame.getLocation() != null) {
                Intent intent = new Intent(requireContext(), MapsActivity.class);
                intent.putExtra("location", selectedGame.getLocation());
                intent.putExtra("markerTitle", "Players Needed: " + selectedGame.getPlayersNeeded());
                startActivity(intent);
            }
        });
    }
}