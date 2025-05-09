package com.example.mobilemind;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
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

public class login extends AppCompatActivity {

    // Login fields
    EditText studentNumber;
    EditText password;
    Button loginButton;

    // Signup fields
    EditText signupStudentNumber;
    EditText firstName;
    EditText lastName;
    EditText phoneNumber;
    EditText passwordFirst;
    EditText passwordVerify;
    Button signUpButton;

    // URLs
    String urlLog = "https://lamp.ms.wits.ac.za/home/s2688828/login.php";
    String urlSign = "https://lamp.ms.wits.ac.za/home/s2688828/signUp.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.welcome_page); // Make sure this is the correct layout name

        // Initialize login UI elements
        studentNumber = findViewById(R.id.login_username); // Using the correct ID from your layout
        password = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.sign_in_button);

        // Initialize signup UI elements
        signupStudentNumber = findViewById(R.id.student_number);
        firstName = findViewById(R.id.etFirstName);
        lastName = findViewById(R.id.etLastName);
        phoneNumber = findViewById(R.id.etPhoneNumber);
        passwordFirst = findViewById(R.id.etPasswordFirst);
        passwordVerify = findViewById(R.id.etPasswordVerify);
        signUpButton = findViewById(R.id.btnSignUp);

        // Login button click listener
        loginButton.setOnClickListener(v -> {
            String username = studentNumber.getText().toString().trim();
            String passwordLogin = password.getText().toString().trim();

            // Validate inputs
            if (username.isEmpty() || passwordLogin.isEmpty()) {
                Toast.makeText(login.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            loginUser(username, passwordLogin);
        });

        // Signup button click listener
        signUpButton.setOnClickListener(v -> {
            // Get and trim all inputs
            String studentNo = signupStudentNumber.getText().toString().trim();
            String firstNameStr = firstName.getText().toString().trim();
            String lastNameStr = lastName.getText().toString().trim();
            String phoneNumberStr = phoneNumber.getText().toString().trim();
            String passwordStr = passwordFirst.getText().toString();
            String verifyStr = passwordVerify.getText().toString();

            // Validate inputs
            if (studentNo.isEmpty() || firstNameStr.isEmpty() || lastNameStr.isEmpty() ||
                    phoneNumberStr.isEmpty() || passwordStr.isEmpty() || verifyStr.isEmpty()) {
                Toast.makeText(login.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Correct string comparison with .equals()
            if (passwordStr.equals(verifyStr)) {
                signUser(studentNo, passwordStr, firstNameStr, lastNameStr, phoneNumberStr);
            } else {
                Toast.makeText(login.this, "Passwords Do Not Match", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loginUser(String username, String password) {
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("STUDENT_NUMBER", username)
                .add("PASSWORD_HASH", password)
                .build();

        Request request = new Request.Builder()
                .url(urlLog)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(login.this, "Network error: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
                Log.e("NetworkError", "Login request failed", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseBody = response.body().string().trim();
                Log.d("ServerResponse", "Login response: '" + responseBody + "'");

                runOnUiThread(() -> {
                    if (responseBody.equals("success")) {
                        Toast.makeText(login.this, "Login successful!", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(login.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(login.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void signUser(String username, String password, String fname, String lname, String phoneNumber) {
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("STUDENT_NUMBER", username)
                .add("PASSWORD_HASH", password)
                .add("STUDENT_FNAME", fname)
                .add("STUDENT_LNAME", lname)
                .add("STUDENT_CONTACT_NO", phoneNumber)
                .build();

        Request request = new Request.Builder()
                .url(urlSign)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(login.this, "Network error: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
                Log.e("NetworkError", "Signup request failed", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseBody = response.body().string().trim();
                Log.d("ServerResponse", "Signup response: '" + responseBody + "'");

                runOnUiThread(() -> {
                    if (responseBody.contains("successfully created")) {
                        Toast.makeText(login.this, "Signup successful! Please verify your email.", Toast.LENGTH_LONG).show();
                        // Clear form fields after successful signup
                        clearSignupFields();
                    } else {
                        Toast.makeText(login.this, responseBody, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    // Helper method to clear signup form fields
    private void clearSignupFields() {
        signupStudentNumber.setText("");
        firstName.setText("");
        lastName.setText("");
        phoneNumber.setText("");
        passwordFirst.setText("");
        passwordVerify.setText("");
    }
}