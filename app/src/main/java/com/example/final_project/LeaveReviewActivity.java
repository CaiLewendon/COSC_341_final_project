package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

public class LeaveReviewActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private EditText etReviewText;
    private Button btnSubmitReview;
    private String playerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_review);

        ratingBar = findViewById(R.id.ratingBar);
        etReviewText = findViewById(R.id.etReviewText);
        btnSubmitReview = findViewById(R.id.btnSubmitReview);

        // Retrieve the player name passed from the previous activity.
        playerName = getIntent().getStringExtra("playerName");

        btnSubmitReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float rating = ratingBar.getRating();
                String reviewText = etReviewText.getText().toString().trim();

                // Validate that the review text is not empty.
                if (reviewText.isEmpty()) {
                    etReviewText.setError("Please write a review.");
                    return;
                }

                // In a real app, here you would submit the review to your backend or database.
                Toast.makeText(LeaveReviewActivity.this, "Review submitted for " + playerName, Toast.LENGTH_SHORT).show();

                // Optionally, pass the new review back to the PlayerDetailActivity using setResult.
                // For now, simply finish this activity to return.
                finish();
            }
        });
    }
}