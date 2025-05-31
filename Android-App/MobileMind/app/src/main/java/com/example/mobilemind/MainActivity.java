package com.example.mobilemind;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize sample user data for testing
        initializeSampleUserData();

        // Initialize sample data if needed
        DataSeeder dataSeeder = new DataSeeder(this);
        if (!dataSeeder.isDataSeeded()) {
            dataSeeder.seedAllData();
        }

        // Initialize bottom navigation
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Load HomeFragment by default
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }

        // Set up bottom navigation listener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int itemId = item.getItemId();
            if (itemId == R.id.btnHome) {
                selectedFragment = new HomeFragment();
            } else if (itemId == R.id.btnPost) {
                selectedFragment = new PostFragment();
            } else if (itemId == R.id.btnActivity) {
                selectedFragment = new ActivityFragment();
            } else if (itemId == R.id.btnProfile) {
                selectedFragment = new ProfileFragment();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
                return true;
            }
            return false;
        });
    }

    /**
     * Initialize sample user data for testing the voting system
     */
    private void initializeSampleUserData() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        
        // Check if user data is already initialized
        String existingUserId = sharedPreferences.getString("student_number", "");
        if (existingUserId.isEmpty()) {
            // Set default test user data
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("student_number", "2688828"); // Default to John Doe for testing
            editor.putString("user_name", "John Doe");
            editor.putString("user_email", "john.doe@students.wits.ac.za");
            editor.apply();
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
} 