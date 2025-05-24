package com.example.mobilemind;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // UI Elements
    private TextView txtFirstName;
    private TextView txtLastName;
    private TextView txtStudentNumber;
    private TextView txtRole;
    private EditText editEmail;
    private EditText editMobile;
    private EditText editDateOfBirth;
    private Button saveButton;
    private TextView profileInitials;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        
        // Initialize views
        initViews(view);
        
        // Load user data
        loadUserData();
        
        // Set up save button
        setupSaveButton();
        
        return view;
    }

    private void initViews(View view) {
        txtFirstName = view.findViewById(R.id.txtFirstName);
        txtLastName = view.findViewById(R.id.etxtLastName);
        txtStudentNumber = view.findViewById(R.id.txtStudentNumber);
        txtRole = view.findViewById(R.id.text_role);
        editEmail = view.findViewById(R.id.edit_email);
        editMobile = view.findViewById(R.id.edit_mobile);
        editDateOfBirth = view.findViewById(R.id.edit_date_of_birth);
        saveButton = view.findViewById(R.id.save_button);
        profileInitials = view.findViewById(R.id.profile_initials);
    }

    private void loadUserData() {
        // Get user data from SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        
        String firstName = sharedPreferences.getString("student_fname", "John");
        String lastName = sharedPreferences.getString("student_lname", "Doe");
        String studentNumber = sharedPreferences.getString("student_number", "1234567");
        String email = sharedPreferences.getString("student_email", "student@wits.ac.za");
        
        // Set the data to views
        txtFirstName.setText(firstName);
        txtLastName.setText(lastName);
        txtStudentNumber.setText(studentNumber);
        txtRole.setText("Student");
        editEmail.setText(email);
        
        // Set profile initials
        String fullName = firstName + " " + lastName;
        String initials = ForumUtils.getUserInitials(fullName);
        profileInitials.setText(initials);
        
        // Set placeholder for optional fields
        editMobile.setText("081 234 5678");
        editDateOfBirth.setText("1999-01-01");
    }

    private void setupSaveButton() {
        saveButton.setOnClickListener(v -> {
            // Simple save functionality
            String email = editEmail.getText().toString().trim();
            String mobile = editMobile.getText().toString().trim();
            
            if (!email.isEmpty()) {
                // Save updated data
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("student_email", email);
                editor.putString("student_mobile", mobile);
                editor.apply();
                
                Toast.makeText(getContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Please fill in required fields", Toast.LENGTH_SHORT).show();
            }
        });
    }
}