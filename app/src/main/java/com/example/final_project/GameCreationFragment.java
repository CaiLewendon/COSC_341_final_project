package com.example.final_project;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class GameCreationFragment extends Fragment {

    private EditText hostNameEditText;
    private Spinner sportTypeSpinner;
    private EditText addressEditText;
    private TextView dateTextView;
    private TextView timeTextView;
    private ImageView gameImageView;
    private Button uploadImageButton;
    private Button createGameButton;
    private Button previewButton;

    private Calendar selectedDateTime = Calendar.getInstance();
    private int selectedImageResource = -1; // Using -1 to indicate no image is selected
    private boolean imageSelected = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_creation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize UI components
        hostNameEditText = view.findViewById(R.id.host_name_edit_text);
        sportTypeSpinner = view.findViewById(R.id.sport_type_spinner);
        addressEditText = view.findViewById(R.id.address_edit_text);
        dateTextView = view.findViewById(R.id.date_text_view);
        timeTextView = view.findViewById(R.id.time_text_view);
        gameImageView = view.findViewById(R.id.game_image_view);
        uploadImageButton = view.findViewById(R.id.upload_image_button);
        createGameButton = view.findViewById(R.id.create_game_button);
        previewButton = view.findViewById(R.id.preview_button);

        // Change the text on the upload button to better reflect its new function
        uploadImageButton.setText("Select Image");

        // Setup sport type spinner
        setupSportTypeSpinner();

        // Setup date and time pickers
        setupDateAndTimePickers();

        // Setup image selection
        setupImageSelection();

        // Setup create button
        createGameButton.setOnClickListener(v -> createGame());

        // Setup preview button
        previewButton.setOnClickListener(v -> previewGame());
    }

    private void setupSportTypeSpinner() {
        // List of common sports
        String[] sports = {"Basketball", "Soccer", "Football", "Baseball", "Tennis",
                "Volleyball", "Hockey", "Ultimate Frisbee", "Badminton", "Other"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                sports);

        sportTypeSpinner.setAdapter(adapter);
    }

    private void setupDateAndTimePickers() {
        // Initialize with current date and time
        updateDateTextView();
        updateTimeTextView();

        // Date picker dialog
        dateTextView.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    requireContext(),
                    (view, year, month, dayOfMonth) -> {
                        selectedDateTime.set(Calendar.YEAR, year);
                        selectedDateTime.set(Calendar.MONTH, month);
                        selectedDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateDateTextView();
                    },
                    selectedDateTime.get(Calendar.YEAR),
                    selectedDateTime.get(Calendar.MONTH),
                    selectedDateTime.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();
        });

        // Time picker dialog
        timeTextView.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    requireContext(),
                    (view, hourOfDay, minute) -> {
                        selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        selectedDateTime.set(Calendar.MINUTE, minute);
                        updateTimeTextView();
                    },
                    selectedDateTime.get(Calendar.HOUR_OF_DAY),
                    selectedDateTime.get(Calendar.MINUTE),
                    false
            );
            timePickerDialog.show();
        });
    }

    private void updateDateTextView() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault());
        dateTextView.setText(dateFormat.format(selectedDateTime.getTime()));
    }

    private void updateTimeTextView() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
        timeTextView.setText(timeFormat.format(selectedDateTime.getTime()));
    }

    private void setupImageSelection() {
        uploadImageButton.setOnClickListener(v -> {
            // Create dialog to choose between two images
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Select Game Location");

            // Define the options
            final CharSequence[] options = {"Park", "Court"};

            builder.setItems(options, (dialog, which) -> {
                // Handle the option selected
                if (which == 0) {
                    // Park image selected
                    selectedImageResource = R.drawable.park;
                    gameImageView.setImageResource(R.drawable.park);
                } else {
                    // Court image selected
                    selectedImageResource = R.drawable.court;
                    gameImageView.setImageResource(R.drawable.court);
                }

                imageSelected = true;
                gameImageView.setVisibility(View.VISIBLE);

                // Show success feedback
                Snackbar.make(requireView(), "Image selected successfully", Snackbar.LENGTH_SHORT).show();
            });

            builder.show();
        });
    }

    private void createGame() {
        // Validate input fields
        if (!validateInputs()) {
            return;
        }

        // Store game information to be passed to the discovery fragment
        // Using the image resource ID instead of URI
        Game game = new Game(
                hostNameEditText.getText().toString(),
                sportTypeSpinner.getSelectedItem().toString(),
                addressEditText.getText().toString(),
                selectedDateTime.getTime(),
                selectedImageResource
        );

        // Add the game to the singleton store
        GameStore.getInstance().addGame(game);

        // Show success message
        Snackbar.make(requireView(), "Game created successfully!", Snackbar.LENGTH_LONG).show();

        // Navigate back to discovery fragment
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, new GameDiscoveryFragment())
                .commit();
    }

    private void previewGame() {
        // Validate input fields
        if (!validateInputs()) {
            return;
        }

        // Create a temporary game for preview
        Game previewGame = new Game(
                hostNameEditText.getText().toString(),
                sportTypeSpinner.getSelectedItem().toString(),
                addressEditText.getText().toString(),
                selectedDateTime.getTime(),
                selectedImageResource
        );

        // Navigate to preview fragment
        GamePreviewFragment previewFragment = new GamePreviewFragment();
        Bundle args = new Bundle();
        args.putSerializable("preview_game", previewGame);
        previewFragment.setArguments(args);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, previewFragment)
                .addToBackStack(null)
                .commit();
    }

    private boolean validateInputs() {
        boolean isValid = true;

        // Validate host name
        if (hostNameEditText.getText().toString().trim().isEmpty()) {
            hostNameEditText.setError("Host name is required");
            isValid = false;
        }

        // Validate address
        if (addressEditText.getText().toString().trim().isEmpty()) {
            addressEditText.setError("Address is required");
            isValid = false;
        }

        // Check if image is selected
        if (!imageSelected) {
            Toast.makeText(requireContext(), "Please select an image", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        // Check if date/time is in the future
        Calendar now = Calendar.getInstance();
        if (selectedDateTime.before(now)) {
            Toast.makeText(requireContext(), "Please select a future date and time", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        return isValid;
    }
}