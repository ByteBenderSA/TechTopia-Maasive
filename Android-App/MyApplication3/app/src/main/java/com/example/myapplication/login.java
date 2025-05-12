package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class login extends AppCompatActivity {

    // Login UI
    EditText studentNumber, password;
    Button loginButton;

    // Signup UI
    EditText signupStudentNumber, firstName, lastName, phoneNumber, passwordFirst, passwordVerify;
    Button signUpButton;

    // URLs
    final String urlLog = "https://lamp.ms.wits.ac.za/home/s2688828/login.php";
    final String urlSign = "https://lamp.ms.wits.ac.za/home/s2688828/signUp.php";

    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.welcome_page);

        // Login views
        studentNumber = findViewById(R.id.studentNumber);
        password = findViewById(R.id.txtPassword);
        loginButton = findViewById(R.id.btnLogin);

        // Signup views
        signupStudentNumber = findViewById(R.id.etSignupStudentNumber);
        firstName = findViewById(R.id.etFirstName);
        lastName = findViewById(R.id.etLastName);
        phoneNumber = findViewById(R.id.etPhoneNumber);
        passwordFirst = findViewById(R.id.etPasswordFirst);
        passwordVerify = findViewById(R.id.etPasswordVerify);
        signUpButton = findViewById(R.id.btnSignUp);



        loginButton.setOnClickListener(v -> {
            String username = studentNumber.getText().toString().trim();
            String pwd = password.getText().toString().trim();

            if (username.isEmpty() || pwd.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            loginUser(username, pwd);
        });

        signUpButton.setOnClickListener(v -> {
            String studentNo = signupStudentNumber.getText().toString().trim();
            String fname = firstName.getText().toString().trim();
            String lname = lastName.getText().toString().trim();
            String phone = phoneNumber.getText().toString().trim();
            String pwd1 = passwordFirst.getText().toString();
            String pwd2 = passwordVerify.getText().toString();

            if (studentNo.isEmpty() || fname.isEmpty() || lname.isEmpty() ||
                    phone.isEmpty() || pwd1.isEmpty() || pwd2.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!pwd1.equals(pwd2)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            signUser(studentNo, pwd1, fname, lname, phone);
        });
    }

    private void loginUser(String username, String password) {
        runOnUiThread(() ->
                Toast.makeText(this, "Logging in...", Toast.LENGTH_SHORT).show()
        );

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
                Log.e("LoginError", "Network failure", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseBody = response.body().string().trim();

                runOnUiThread(() -> {
                    try {
                        if (responseBody.startsWith("<") || responseBody.contains("Warning:")) {
                            Toast.makeText(login.this, "Server error. Contact admin.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        JSONObject json = new JSONObject(responseBody);
                        boolean success = json.getBoolean("success");

                        if (success) {
                            JSONObject user = json.getJSONObject("user");

                            // Save user data in SharedPreferences
                            SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("STUDENT_NUMBER", user.getString("STUDENT_NUMBER"));
                            editor.putString("STUDENT_FNAME", user.getString("STUDENT_FNAME"));
                            editor.putString("STUDENT_LNAME", user.getString("STUDENT_LNAME"));
                            editor.putString("STUDENT_EMAIL", user.getString("STUDENT_EMAIL"));
                            editor.apply();

                            Toast.makeText(login.this, "Login successful!", Toast.LENGTH_SHORT).show();


                            startActivity(new Intent(login.this, MainActivity.class));
                            finish();
                        } else {
                            String errorMsg = json.optString("error", "Login failed");
                            Toast.makeText(login.this, errorMsg, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        String errorMsg = "Invalid response from server";
                        if (responseBody.isEmpty()) errorMsg = "Empty response from server";
                        else if (responseBody.length() > 100) errorMsg = "Unexpected server response";

                        Toast.makeText(login.this, errorMsg, Toast.LENGTH_LONG).show();
                        Log.e("LoginError", "Parsing error", e);
                    }
                });
            }
        });
    }

    private void signUser(String studentNo, String password, String fname, String lname, String phone) {
        RequestBody formBody = new FormBody.Builder()
                .add("STUDENT_NUMBER", studentNo)
                .add("PASSWORD_HASH", password)
                .add("STUDENT_FNAME", fname)
                .add("STUDENT_LNAME", lname)
                .add("STUDENT_CONTACT_NO", phone)
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
                Log.e("SignupError", "Network failure", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseBody = response.body().string().trim();

                runOnUiThread(() -> {
                    if (responseBody.contains("successfully created")) {
                        Toast.makeText(login.this, "Signup successful!", Toast.LENGTH_LONG).show();
                        clearSignupFields();
                    } else {
                        Toast.makeText(login.this, responseBody, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void clearSignupFields() {
        signupStudentNumber.setText("");
        firstName.setText("");
        lastName.setText("");
        phoneNumber.setText("");
        passwordFirst.setText("");
        passwordVerify.setText("");
    }
}
