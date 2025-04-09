package com.example.final_project;
// The folloiwng code was created with the assistance of Artificial Intelligence/LLMs
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import android.widget.CalendarView;
import android.widget.ImageButton;

public class VenueReservationFragment extends Fragment {
    private TextView titleText;
    private TextView startTimeText;
    private TextView endTimeText;
    private TextView totalCostAmount;
    private Calendar selectedDate;
    private Calendar startTime;
    private Calendar endTime;
    private int hourlyRate;
    private boolean dateSelected = false;
    private String venueName;
    private boolean startTimeSelected = false;
    private boolean endTimeSelected = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                            @Nullable ViewGroup container,
                            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_venue_reservation, container, false);
        
        // Get venue information from arguments
        if (getArguments() != null) {
            venueName = getArguments().getString("venueName", "");
            hourlyRate = getArguments().getInt("venuePrice", 300);
        }

        // Initialize views
        titleText = view.findViewById(R.id.titleText);
        startTimeText = view.findViewById(R.id.startTimeText);
        endTimeText = view.findViewById(R.id.endTimeText);
        totalCostAmount = view.findViewById(R.id.totalCostAmount);
        TextView hourlyRateAmount = view.findViewById(R.id.hourlyRateAmount);
        MaterialButton confirmButton = view.findViewById(R.id.confirmButton);
        ImageButton backButton = view.findViewById(R.id.backButton);

        // Set venue name as title and hourly rate
        titleText.setText(venueName);
        hourlyRateAmount.setText(String.format("$ %d", hourlyRate));

        // Initialize calendars and set current date
        selectedDate = Calendar.getInstance();
        startTime = Calendar.getInstance();
        endTime = Calendar.getInstance();
        dateSelected = true; // Since we're using current date by default

        // Set default text for time selections
        startTimeText.setText("Select Time");
        endTimeText.setText("Select Time");
        totalCostAmount.setText("$ 0");

        // Set up click listeners
        startTimeText.setOnClickListener(v -> showTimePickerDialog(true));
        endTimeText.setOnClickListener(v -> showTimePickerDialog(false));
        confirmButton.setOnClickListener(v -> validateAndShowConfirmation());
        backButton.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        // Set up calendar with current date
        CalendarView calendarView = view.findViewById(R.id.calendarView);
        calendarView.setDate(selectedDate.getTimeInMillis());
        
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            Calendar currentDate = Calendar.getInstance();
            Calendar selectedCal = Calendar.getInstance();
            selectedCal.set(year, month, dayOfMonth);
            
            // Reset to midnight for date comparison
            currentDate.set(Calendar.HOUR_OF_DAY, 0);
            currentDate.set(Calendar.MINUTE, 0);
            currentDate.set(Calendar.SECOND, 0);
            currentDate.set(Calendar.MILLISECOND, 0);
            selectedCal.set(Calendar.HOUR_OF_DAY, 0);
            selectedCal.set(Calendar.MINUTE, 0);
            selectedCal.set(Calendar.SECOND, 0);
            selectedCal.set(Calendar.MILLISECOND, 0);

            if (selectedCal.before(currentDate)) {
                showError("Please select today or a future date");
                return;
            }

            selectedDate.set(year, month, dayOfMonth);
            dateSelected = true;
            updateTotalCost();
        });

        return view;
    }

    private void validateAndShowConfirmation() {
        if (!startTimeSelected) {
            showError("Please select a start time");
            return;
        }

        if (!endTimeSelected) {
            showError("Please select an end time");
            return;
        }

        // Check if selected date is today and start time is in the past
        Calendar currentTime = Calendar.getInstance();
        Calendar selectedStartTime = (Calendar) startTime.clone();
        selectedStartTime.set(Calendar.YEAR, selectedDate.get(Calendar.YEAR));
        selectedStartTime.set(Calendar.MONTH, selectedDate.get(Calendar.MONTH));
        selectedStartTime.set(Calendar.DAY_OF_MONTH, selectedDate.get(Calendar.DAY_OF_MONTH));

        if (isSameDay(selectedDate, currentTime) && selectedStartTime.before(currentTime)) {
            showError("Start time must be after current time for today's reservations");
            return;
        }

        // Validate end time is after start time
        Calendar selectedEndTime = (Calendar) endTime.clone();
        selectedEndTime.set(Calendar.YEAR, selectedDate.get(Calendar.YEAR));
        selectedEndTime.set(Calendar.MONTH, selectedDate.get(Calendar.MONTH));
        selectedEndTime.set(Calendar.DAY_OF_MONTH, selectedDate.get(Calendar.DAY_OF_MONTH));

        if (!selectedEndTime.after(selectedStartTime)) {
            showError("End time must be after start time");
            return;
        }

        // Calculate duration and total cost
        long diffMillis = selectedEndTime.getTimeInMillis() - selectedStartTime.getTimeInMillis();
        float hours = diffMillis / (1000f * 60f * 60f);

        // Show confirmation dialog
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(selectedDate.getTime());
        
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirm Reservation")
               .setMessage(String.format(
                   "Please confirm your reservation:\n\n" +
                   "Venue: %s\n" +
                   "Date: %s\n" +
                   "Time: %s - %s\n" +
                   "Duration: %.1f hours\n" +
                   "Rate: $%d/hour\n" +
                   "Total Cost: $%d",
                   venueName,
                   formattedDate,
                   startTimeText.getText(),
                   endTimeText.getText(),
                   hours,
                   hourlyRate,
                   (int)(hours * hourlyRate)
               ))
               .setPositiveButton("Confirm", (dialog, which) -> {
                   // Handle confirmation
                   Toast.makeText(requireContext(), "Reservation confirmed!", Toast.LENGTH_LONG).show();
                   // Return to browse page
                   requireActivity().getSupportFragmentManager().popBackStack();
               })
               .setNegativeButton("Cancel", null)
               .show();
    }

    private boolean isSameDay(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
               cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
               cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }

    private void showTimePickerDialog(boolean isStartTime) {
        Calendar calendar = isStartTime ? startTime : endTime;
        TimePickerDialog timePickerDialog = new TimePickerDialog(
            requireContext(),
            (view, hourOfDay, minute) -> {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                // Validate end time is after start time when setting end time
                if (!isStartTime && startTimeSelected) {
                    Calendar selectedStartTime = (Calendar) startTime.clone();
                    Calendar selectedEndTime = (Calendar) endTime.clone();
                    
                    if (!selectedEndTime.after(selectedStartTime)) {
                        showError("End time must be after start time");
                        return;
                    }
                }
                
                SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
                String formattedTime = timeFormat.format(calendar.getTime());
                
                if (isStartTime) {
                    startTimeText.setText(formattedTime);
                    startTimeSelected = true;
                } else {
                    endTimeText.setText(formattedTime);
                    endTimeSelected = true;
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
        if (startTimeSelected && endTimeSelected) {
            long diffMillis = endTime.getTimeInMillis() - startTime.getTimeInMillis();
            float hours = diffMillis / (1000f * 60f * 60f);
            if (hours > 0) {
                int totalCost = (int) (hours * hourlyRate);
                totalCostAmount.setText(String.format("$ %d", totalCost));
            }
        }
    }

    private void showError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}