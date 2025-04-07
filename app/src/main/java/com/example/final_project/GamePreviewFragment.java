package com.example.final_project;

import android.graphics.Bitmap;
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

public class GamePreviewFragment extends Fragment {

    private Game previewGame;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_preview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get the game from arguments
        if (getArguments() != null) {
            previewGame = (Game) getArguments().getSerializable("preview_game");
        }

        if (previewGame == null) {
            Toast.makeText(requireContext(), "Error loading preview", Toast.LENGTH_SHORT).show();
            getParentFragmentManager().popBackStack();
            return;
        }

        // Set up views
        TextView titleTextView = view.findViewById(R.id.preview_title);
        ImageView gameImageView = view.findViewById(R.id.preview_image);
        TextView descriptionTextView = view.findViewById(R.id.preview_description);
        Button backButton = view.findViewById(R.id.back_button);

        // Format date and time
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy 'at' h:mm a", Locale.getDefault());
        String formattedDateTime = dateFormat.format(previewGame.getDateTime());

        // Set the title
        titleTextView.setText(previewGame.getSportType() + " Game");

        // Set the description
        String description = previewGame.getHostName() + " is hosting this " +
                previewGame.getSportType() + ", at " +
                previewGame.getLocation() + " on " +
                formattedDateTime;
        descriptionTextView.setText(description);

        // Set the image
        try {
            if (previewGame.getImageUri() != null) {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                        requireActivity().getContentResolver(),
                        previewGame.getImageUri());
                gameImageView.setImageBitmap(bitmap);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Failed to load image", Toast.LENGTH_SHORT).show();
        }

        // Set back button
        backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());
    }
}