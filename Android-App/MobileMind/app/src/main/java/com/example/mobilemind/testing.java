package com.example.mobilemind;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class testing extends AppCompatActivity {

    // Signup fields
    TextView username;
    TextView stu_num;
    TextView contact_num;
    TextView email;
    TextView user_role;


    // URL


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.testing); // Make sure this is the correct layout name


        // Get views
        username = findViewById(R.id.username);
        stu_num = findViewById(R.id.stu_num);
        contact_num = findViewById(R.id.contact_num);
        email = findViewById(R.id.email);
        user_role = findViewById(R.id.user_role);

        // Get stored data
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String fullName = sharedPreferences.getString("student_fname", "") + " " + sharedPreferences.getString("student_lname", "");
        String studentNumber = sharedPreferences.getString("student_number", "");
        String contactNumber = sharedPreferences.getString("student_contact_no", "");
        String emailAddress = sharedPreferences.getString("student_email", "");
        String role = sharedPreferences.getString("user_role", "");

        // Set text
        username.setText(fullName);
        stu_num.setText(studentNumber);
        contact_num.setText(contactNumber);
        email.setText(emailAddress);
        user_role.setText(role);

    }



    // Helper method to clear signup form fields

}