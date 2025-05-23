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
                    Log.e("NetworkError", "Login request failed", e);
                    Toast.makeText(LoginPage.this, "Network error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseBody = response.body().string().trim();
                Log.d("ServerResponse", "Login response: '" + responseBody + "'");

                runOnUiThread(() -> {
                    try {
                        // Parse the JSON response
                        JSONObject jsonResponse = new JSONObject(responseBody);

                        // Check for success or error in the response
                        if (jsonResponse.has("success")) {
                            boolean isSuccess = jsonResponse.getBoolean("success");

                            if (isSuccess) {
                                Toast.makeText(LoginPage.this, "Login successful!", Toast.LENGTH_SHORT).show();

                                // Extract user data if available
                                if (jsonResponse.has("user")) {
                                    JSONObject userData = jsonResponse.getJSONObject("user");

                                    // Extract individual user data fields
                                    String student_number = userData.getString("STUDENT_NUMBER");
                                    String student_fname = userData.getString("STUDENT_FNAME");
                                    String student_lname = userData.getString("STUDENT_LNAME");
                                    String student_contact_no = userData.getString("STUDENT_CONTACT_NO");
                                    String student_email = userData.getString("STUDENT_EMAIL");
                                    String user_role = userData.getString("USER_ROLE");

                                    // Store user data in SharedPreferences
                                    SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("student_number", student_number);
                                    editor.putString("student_fname", student_fname);
                                    editor.putString("student_lname", student_lname);
                                    editor.putString("student_contact_no", student_contact_no);
                                    editor.putString("student_email", student_email);
                                    editor.putString("user_role", user_role);
                                    editor.apply();

                                    // Navigate to the next activity
                                    startActivity(new Intent(LoginPage.this, ForumPostActivity.class));
                                    finish();
                                } else {
                                    // Handle the case where user data is missing
                                    Toast.makeText(LoginPage.this, "Login successful, but no user data received", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Handle authentication failure
                                String errorMessage = jsonResponse.optString("message", "Authentication failed");
                                Toast.makeText(LoginPage.this, errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        } else if (jsonResponse.has("error")) {
                            // Handle old error format
                            String errorMessage = jsonResponse.getString("error");
                            Toast.makeText(LoginPage.this, errorMessage, Toast.LENGTH_SHORT).show();
                        } else {
                            // Unknown response format
                            Toast.makeText(LoginPage.this, "Unrecognized server response", Toast.LENGTH_SHORT).show();
                            Log.e("LoginError", "Unrecognized response format: " + responseBody);
                        }
                    } catch (JSONException e) {
                        Log.e("JSONParsingError", "Error parsing JSON response: " + e.getMessage(), e);
                        Log.e("JSONParsingError", "Response body: " + responseBody);
                        Toast.makeText(LoginPage.this, "Error processing server response", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}