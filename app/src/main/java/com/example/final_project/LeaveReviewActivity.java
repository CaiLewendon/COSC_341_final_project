package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;
import android.content.SharedPreferences;

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

        // get player name passed from the previous activity/fragment
        playerName = getIntent().getStringExtra("playerName");

        btnSubmitReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float rating = ratingBar.getRating();
                String reviewText = etReviewText.getText().toString().trim();

                //validate that the review text is not empty
                if (reviewText.isEmpty()) {
                    etReviewText.setError("Please write a review.");
                    return;
                }

                // Read the username from SharedPreferences
                SharedPreferences prefs = getSharedPreferences(LoginFragment.PREFS_NAME, MODE_PRIVATE);
                String username = prefs.getString(LoginFragment.KEY_USERNAME, null);

                if (username == null) {
                    // Force login if no username was stored
                    Intent loginIntent = new Intent(LeaveReviewActivity.this, LoginFragment.class);
                    startActivity(loginIntent);
                    finish();
                    return;
                }

                //   Create an intent to hold the new review info
                Intent resultIntent = new Intent();
                resultIntent.putExtra("reviewerName", username);
                resultIntent.putExtra("rating", (int) rating);
                resultIntent.putExtra("reviewText", reviewText);
                resultIntent.putExtra("timestamp", "2023-04-01");

                //Set the result so the calling fragment can retrieve the new review
                setResult(RESULT_OK, resultIntent);
                Toast.makeText(LeaveReviewActivity.this, "Review submitted for " + playerName, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}