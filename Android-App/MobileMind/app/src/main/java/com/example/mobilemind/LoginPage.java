package com.example.mobilemind;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
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

public class LoginPage extends AppCompatActivity {

    // Login fields
    EditText studentNumber;
    EditText password;
    Button loginButton;
    Button createAccount;

    // URLs
    final String urlLog = "https://lamp.ms.wits.ac.za/home/s2688828/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.login_page);

        // Initialize login UI elements
        studentNumber = findViewById(R.id.login_username);
        password = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.sign_in_button);
        createAccount = findViewById(R.id.create_account);

        // Login button click listener
        loginButton.setOnClickListener(v -> {
            String username = studentNumber.getText().toString().trim();
            String passwordLogin = password.getText().toString().trim();

            // Validate inputs
            if (username.isEmpty() || passwordLogin.isEmpty()) {
                Toast.makeText(LoginPage.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            loginUser(username, passwordLogin);
        });

        createAccount.setOnClickListener(v -> {
            startActivity(new Intent(LoginPage.this, RegisterPage.class));
            finish();
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
                runOnUiThread(() -> {
                    Toast.makeText(LoginPage.this, "Network error!", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();

                runOnUiThread(() -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(responseBody);

                        if (jsonResponse.getBoolean("success")) {
                            Toast.makeText(LoginPage.this, "Login successful!", Toast.LENGTH_SHORT).show();

                            // Get user data
                            JSONObject userData = jsonResponse.getJSONObject("user");

                            // Store basic user info
                            SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            
                            editor.putString("student_number", userData.getString("STUDENT_NUMBER"));
                            editor.putString("student_fname", userData.getString("STUDENT_FNAME"));
                            editor.putString("student_lname", userData.getString("STUDENT_LNAME"));
                            editor.putString("student_email", userData.getString("STUDENT_EMAIL"));

                            // Store posts and comments if they exist
                            if (jsonResponse.has("posts")) {
                                editor.putString("posts_data", jsonResponse.getJSONArray("posts").toString());
                            }
                            if (jsonResponse.has("comments")) {
                                editor.putString("comments_data", jsonResponse.getJSONArray("comments").toString());
                            }

                            editor.apply();

                            // Go to main activity
                            startActivity(new Intent(LoginPage.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(LoginPage.this, "Login failed!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(LoginPage.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}