package com.example.courseforum;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    EditText emailInput;
    EditText passwordInput;
    Button loginButton;

    Button signButton;

    TextView textView;

    String urlLog = "https://lamp.ms.wits.ac.za/home/s2688828/login.php";
    String urlSign = "https://lamp.ms.wits.ac.za/home/s2688828/signUp.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        emailInput = findViewById(R.id.emailText);
        passwordInput = findViewById(R.id.txtPassword);
        loginButton = findViewById(R.id.btnLogin);
        signButton =  findViewById(R.id.btnSignUp);
        textView = findViewById(R.id.textView);

        loginButton.setOnClickListener(v -> {
            String username = emailInput.getText().toString();
            String password = passwordInput.getText().toString();
            loginUser(username, password);
        });

        signButton.setOnClickListener(v -> {
            String username = emailInput.getText().toString();
            String password = passwordInput.getText().toString();
            signUser(username, password);
        });


    }

    private void loginUser(String username, String password) {
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();

        Request request = new Request.Builder()
                .url(urlLog)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(MainActivity.this, "Network error", Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string().trim();
                runOnUiThread(() -> {
                    if (responseBody.equals("success")) {
                        Toast.makeText(MainActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                        textView.setText("You are logged in");
                        // Intent to next activity
                    } else {
                        Toast.makeText(MainActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }



    private void signUser(String username, String password) {
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();

        Request request = new Request.Builder()
                .url(urlSign)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(MainActivity.this, "Network error", Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string().trim();
                runOnUiThread(() -> {
                    if (responseBody.equals("User successfully created. Please check your email to verify your account.")) {
                        Toast.makeText(MainActivity.this, "SignUp successful!", Toast.LENGTH_SHORT).show();
                        textView.setText("You are signed up");
                        // Intent to next activity
                    } else {
                        Log.d("ServerResponse", "Response: '" + responseBody + "'");
                        Toast.makeText(MainActivity.this, responseBody, Toast.LENGTH_LONG).show();
                        textView.setText(responseBody);

                    }
                });
            }
        });
    }

}