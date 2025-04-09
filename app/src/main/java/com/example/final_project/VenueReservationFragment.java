package com.example.final_project;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import com.google.android.material.button.MaterialButton;

public class VenueReservationFragment extends Fragment {
    private TextView startTimeText;
    private TextView endTimeText;
    private TextView totalCostAmount;
    private CalendarView calendarView;
    private Spinner sportSpinner;
    private Spinner venueSpinner;
    private MaterialButton confirmButton;
    private Calendar selectedDate;
    private Calendar startTime;
    private Calendar endTime;
    private int currentHourlyRate = 300;
    private String selectedSportType = "";
    private String selectedVenueName = "";
    private boolean dateSelected = false;

    // Sports and their venues with prices
    private final Map<String, Map<String, Integer>> venuesMap = new HashMap<>();

    public VenueReservationFragment() {
        // Required empty public constructor
        initializeVenuesData();
    }

    private void initializeVenuesData() {
        // Basketball venues
        Map<String, Integer> basketballVenues = new HashMap<>();
        basketballVenues.put("Downtown Arena - 123 Main St ($300/hr)", 300);
        basketballVenues.put("Community Center - 456 Oak Ave ($200/hr)", 200);
        basketballVenues.put("Sports Complex - 789 Pine Rd ($250/hr)", 250);
        venuesMap.put("Basketball", basketballVenues);

        // Soccer venues
        Map<String, Integer> soccerVenues = new HashMap<>();
        soccerVenues.put("City Stadium - 321 Park Lane ($400/hr)", 400);
        soccerVenues.put("Soccer Center - 654 Green St ($350/hr)", 350);
        soccerVenues.put("Training Ground - 987 Field Ave ($275/hr)", 275);
        venuesMap.put("Soccer", soccerVenues);

        // Hockey venues
        Map<String, Integer> hockeyVenues = new HashMap<>();
        hockeyVenues.put("Ice Palace - 147 Frost Rd ($500/hr)", 500);
        hockeyVenues.put("Winter Arena - 258 Cold St ($450/hr)", 450);
        hockeyVenues.put("Hockey Center - 369 Ice Ave ($425/hr)", 425);
        venuesMap.put("Hockey", hockeyVenues);

        // Baseball venues
        Map<String, Integer> baseballVenues = new HashMap<>();
        baseballVenues.put("Grand Stadium - 741 Diamond Dr ($350/hr)", 350);
        baseballVenues.put("Baseball Park - 852 Base Lane ($300/hr)", 300);
        baseballVenues.put("Training Field - 963 Home St ($250/hr)", 250);
        venuesMap.put("Baseball", baseballVenues);

        // Football venues
        Map<String, Integer> footballVenues = new HashMap<>();
        footballVenues.put("Memorial Stadium - 159 Goal Post Rd ($600/hr)", 600);
        footballVenues.put("Football Complex - 357 Touchdown Ave ($500/hr)", 500);
        footballVenues.put("Practice Field - 486 Yard Line St ($400/hr)", 400);
        venuesMap.put("Football", footballVenues);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                            @Nullable ViewGroup container,
                            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_venue_reservation, container, false);
        
        // Initialize views
        startTimeText = view.findViewById(R.id.startTimeText);
        endTimeText = view.findViewById(R.id.endTimeText);
        totalCostAmount = view.findViewById(R.id.totalCostAmount);
        calendarView = view.findViewById(R.id.calendarView);
        sportSpinner = view.findViewById(R.id.sportSpinner);
        venueSpinner = view.findViewById(R.id.venueSpinner);
        confirmButton = view.findViewById(R.id.confirmButton);

        // Initialize calendars
        selectedDate = Calendar.getInstance();
        startTime = Calendar.getInstance();
        endTime = Calendar.getInstance();

        // Set up spinners
        setupSportSpinner();
        
        // Set up click listeners
        startTimeText.setOnClickListener(v -> showTimePickerDialog(true));
        endTimeText.setOnClickListener(v -> showTimePickerDialog(false));
        confirmButton.setOnClickListener(v -> validateAndShowConfirmation());

        // Set up calendar listener
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            selectedDate.set(year, month, dayOfMonth);
            dateSelected = true;
            updateTotalCost();
        });

        return view;
    }

    private void validateAndShowConfirmation() {
        if (!dateSelected) {
            showError("Please select a date");
            return;
        }

        if (selectedSportType.isEmpty()) {
            showError("Please select a sport");
            return;
        }

        if (selectedVenueName.isEmpty()) {
            showError("Please select a venue");
            return;
        }

        if (startTimeText.getText().toString().equals("4:05 PM")) {
            showError("Please select a start time");
            return;
        }

        if (endTimeText.getText().toString().equals("11:05 PM")) {
            showError("Please select an end time");
            return;
        }

        // Calculate duration and total cost
        long diffMillis = endTime.getTimeInMillis() - startTime.getTimeInMillis();
        float hours = diffMillis / (1000f * 60f * 60f);
        if (hours <= 0) {
            showError("End time must be after start time");
            return;
        }

        // Show confirmation dialog
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(selectedDate.getTime());
        
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirm Reservation")
               .setMessage(String.format(
                   "Please confirm your reservation:\n\n" +
                   "Sport: %s\n" +
                   "Venue: %s\n" +
                   "Date: %s\n" +
                   "Time: %s - %s\n" +
                   "Duration: %.1f hours\n" +
                   "Rate: $%d/hour\n" +
                   "Total Cost: $%d",
                   selectedSportType,
                   selectedVenueName.substring(0, selectedVenueName.lastIndexOf("($")),
                   formattedDate,
                   startTimeText.getText(),
                   endTimeText.getText(),
                   hours,
                   currentHourlyRate,
                   (int)(hours * currentHourlyRate)
               ))
               .setPositiveButton("Confirm", (dialog, which) -> {
                   // Handle confirmation
                   Toast.makeText(requireContext(), "Reservation confirmed!", Toast.LENGTH_LONG).show();
               })
               .setNegativeButton("Cancel", null)
               .show();
    }

    private void showError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void setupSportSpinner() {
        ArrayAdapter<String> sportAdapter = new ArrayAdapter<>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            new String[]{"Basketball", "Soccer", "Hockey", "Baseball", "Football"}
        );
        sportAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sportSpinner.setAdapter(sportAdapter);

        sportSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSportType = parent.getItemAtPosition(position).toString();
                updateVenueSpinner(selectedSportType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void updateVenueSpinner(String sport) {
        Map<String, Integer> venues = venuesMap.get(sport);
        if (venues != null) {
            ArrayAdapter<String> venueAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                venues.keySet().toArray(new String[0])
            );
            venueAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            venueSpinner.setAdapter(venueAdapter);

            venueSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedVenueName = parent.getItemAtPosition(position).toString();
                    currentHourlyRate = venues.get(selectedVenueName);
                    updateTotalCost();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
        }
    }

    private void showTimePickerDialog(boolean isStartTime) {
        Calendar calendar = isStartTime ? startTime : endTime;
        TimePickerDialog timePickerDialog = new TimePickerDialog(
            requireContext(),
            (view, hourOfDay, minute) -> {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                
                SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
                String formattedTime = timeFormat.format(calendar.getTime());
                
                if (isStartTime) {
                    startTimeText.setText(formattedTime);
                } else {
                    endTimeText.setText(formattedTime);
                }
                
                updateTotalCost();
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false
        );
        timePickerDialog.show();
    }

    private void updateTotalCost() {
        if (startTime != null && endTime != null) {
            long diffMillis = endTime.getTimeInMillis() - startTime.getTimeInMillis();
            float hours = diffMillis / (1000f * 60f * 60f);
            if (hours > 0) {
                int totalCost = (int) (hours * currentHourlyRate);
                totalCostAmount.setText(String.format("$ %d", totalCost));
            }
        }
    }
}