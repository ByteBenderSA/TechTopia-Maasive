package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Car;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    Button btnPost;

    EditText head;
    EditText text;
    RecyclerView recyclerView;
    CarAdapter carAdapter;
    List<Car> carList = new ArrayList<>();

    OkHttpClient client = new OkHttpClient();
    String postUrl = "https://lamp.ms.wits.ac.za/home/s2688828/post.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        String student_no = prefs.getString("STUDENT_NUMBER","");
        String firstName = prefs.getString("STUDENT_FNAME", "");
        String lastName = prefs.getString("STUDENT_LNAME", "");

        // Display welcome message
        TextView welcomeText = findViewById(R.id.welcomeText);
        head = findViewById(R.id.postHead);
        text = findViewById(R.id.postText);
        welcomeText.setText(String.format("Welcome, %s %s!", firstName, lastName));

        btnPost = findViewById(R.id.btnPost);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        carAdapter = new CarAdapter(carList);
        recyclerView.setAdapter(carAdapter);

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String body = text.getText().toString();
                String title = head.getText().toString();
                fetchPost(student_no, title, body);
                head.setText("");
                text.setText("");

            }
        });
    }


    private void fetchPost(String student_no, String postHead, String postText) {
        RequestBody formBody = new FormBody.Builder()
                .add("POST_HEAD", postHead)
                .add("POST_TEXT", postText)
                .add("STUDENT_NUMBER", student_no)
                .build();

        Request request = new Request.Builder()
                .url(postUrl)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(MainActivity.this, "Network error: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
                Log.e("SignupError", "Network failure", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseBody = response.body().string().trim();

                runOnUiThread(() -> {
                    if (responseBody.contains("Post successfully created.")) {
                        Toast.makeText(MainActivity.this, "Post created", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(MainActivity.this, responseBody, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

}
