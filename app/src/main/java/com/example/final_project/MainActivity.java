package com.example.final_project;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // By default, load the Game Creation fragment first
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, new GameCreationFragment())
                .commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Make sure the Game Creation icon is selected initially
        bottomNavigationView.setSelectedItemId(R.id.nav_creation);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int itemId = item.getItemId();

                if (itemId == R.id.nav_discovery) {
                    selectedFragment = new GameDiscoveryFragment();
                }
//                else if (itemId == R.id.nav_registration) {
//                    selectedFragment = new GameRegistrationFragment();
//                }
                else if (itemId == R.id.nav_creation) {
                    selectedFragment = new GameCreationFragment();
                } else if (itemId == R.id.nav_rating) {
                    selectedFragment = new PlayerRatingFragment();
                } else if (itemId == R.id.nav_venue) {
                    selectedFragment = new VenueBrowseFragment();
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main, selectedFragment)
                            .commit();
                }
                return true;
            }
        });
    }
}