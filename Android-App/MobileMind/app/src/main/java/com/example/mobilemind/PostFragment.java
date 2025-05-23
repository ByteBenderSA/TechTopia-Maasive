package com.example.mobilemind;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PostFragment extends Fragment {

    Button btnPost = findViewById(R.id.btnPost);
    RecyclerView recyclerView;

    EditText heading;
    EditText body;


    OkHttpClient client = new OkHttpClient();
    String postURL = "https://lamp.ms.wits.ac.za/home/s2688828/post.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        heading = findViewById(R.id.editTextHeading);
        body = findViewById(R.id.editTextBody);


        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String head = heading.getText().toString();
                String text = body.getText().toString();

                post(head,text);
            }
        });
    }

    private void post(String heading, String body) {
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("POST_HEAD", heading)
                .add("POST_TEXT", body)

                .build();

        Request request = new Request.Builder()
                .url(postURL)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(posting.this, "Network error: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
                Log.e("NetworkError", "Signup request failed", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseBody = response.body().string().trim();
                Log.d("ServerResponse", "Signup response: '" + responseBody + "'");

                runOnUiThread(() -> {
                    if (responseBody.contains("Post successfully created.")) {
                        Toast.makeText(posting.this, "Signup successful! Please verify your email.", Toast.LENGTH_LONG).show();
                        // Clear form fields after successful signup
                        clearSignupFields();
                    } else {
                        Toast.makeText(posting.this, responseBody, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
    private void clearSignupFields() {
        body.setText("");
        heading.setText("");
    }
}
