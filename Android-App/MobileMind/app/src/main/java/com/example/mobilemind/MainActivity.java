package com.example.mobilemind;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilemind.Car;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    Button btnGet;
    RecyclerView recyclerView;
    CarAdapter carAdapter;
    List<Car> carList = new ArrayList<>();

    OkHttpClient client = new OkHttpClient();
    String getUrl = "https://lamp.ms.wits.ac.za/home/s2688828/cars.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGet = findViewById(R.id.btnGet);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        carAdapter = new CarAdapter(carList);
        recyclerView.setAdapter(carAdapter);

        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchCars();
            }
        });
    }

    private void fetchCars() {
        Request request = new Request.Builder().url(getUrl).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseData = response.body().string();
                try {
                    JSONArray jsonArray = new JSONArray(responseData);
                    List<Car> newCars = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        String owner = obj.getString("OWNER");
                        newCars.add(new Car(owner));
                    }

                    runOnUiThread(() -> carAdapter.updateData(newCars));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
