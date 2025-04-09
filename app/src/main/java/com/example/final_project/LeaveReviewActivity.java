package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;

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

        // getthe player name passed from the previous activity/fragment
        playerName = getIntent().getStringExtra("playerName");

        btnSubmitReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float rating = ratingBar.getRating();
                String reviewText = etReviewText.getText().toString().trim();

                // validate that the review text is not empty
                if (reviewText.isEmpty()) {
                    etReviewText.setError("Please write a review.");
                    return;
                }

                //create an intent to hold the new review info
                Intent resultIntent = new Intent();
                // for testing the reviewer name is hardcoded
                resultIntent.putExtra("reviewerName", "Custom Reviewer");
                // convert to an int if your Review class expects an int rating
                resultIntent.putExtra("rating", (int) rating);
                resultIntent.putExtra("reviewText", reviewText);
                //  use a dynamic timestamp
                resultIntent.putExtra("timestamp", "2023-04-01");

                // Set the result so the calling fragment can retrieve the new review
                setResult(RESULT_OK, resultIntent);
                Toast.makeText(LeaveReviewActivity.this, "Review submitted for " + playerName, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}