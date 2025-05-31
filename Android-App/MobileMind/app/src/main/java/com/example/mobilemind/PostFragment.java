package com.example.mobilemind;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PostFragment extends Fragment {

    Button btnPost;
    ImageButton btnCancel;
    EditText heading;
    EditText body;
    EditText tags;

    OkHttpClient client = new OkHttpClient();
    String postURL = "https://lamp.ms.wits.ac.za/home/s2688828/post.php";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        // Initialize views from the inflated layout
        btnPost = view.findViewById(R.id.btnSubmit);
        btnCancel = view.findViewById(R.id.btnCancel);
        heading = view.findViewById(R.id.editTextTitle);
        body = view.findViewById(R.id.editTextBody);
        tags = view.findViewById(R.id.editTextTag);

        // Post button click listener
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String head = heading.getText().toString().trim();
                String bod = body.getText().toString().trim();
                
                // Simple validation
                if (head.isEmpty() || bod.isEmpty()) {
                    Toast.makeText(getContext(), "Please fill in title and content", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                post(head, bod);
            }
        });

        // Cancel button click listener
        btnCancel.setOnClickListener(v -> {
            clearFields();
            // Could navigate back to home if needed
        });

        return view;
    }

    private void post(String heading, String body) {
        // Get student number from SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String studentNumber = sharedPreferences.getString("student_number", "");
        
        if (studentNumber.isEmpty()) {
            Toast.makeText(getContext(), "Error: User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("STUDENT_NUMBER", studentNumber)
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
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), "Network error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
                Log.e("NetworkError", "Post request failed", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseBody = response.body().string().trim();
                Log.d("ServerResponse", "Post response: '" + responseBody + "'");

                requireActivity().runOnUiThread(() -> {
                    if (responseBody.contains("Post successfully created.")) {
                        Toast.makeText(requireContext(), "Post created successfully!", Toast.LENGTH_LONG).show();
                        clearFields();
                    } else {
                        Toast.makeText(requireContext(), responseBody, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void clearFields() {
        body.setText("");
        heading.setText("");
        tags.setText("");
    }
}
