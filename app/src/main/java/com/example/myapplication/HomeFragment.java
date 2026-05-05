package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView rvProducts;
    private ProgressBar progressBar;
    private ProductAdapter adapter;
    private List<Product> productList;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        rvProducts = view.findViewById(R.id.rvProducts);
        progressBar = view.findViewById(R.id.progressBar);
        dbHelper = new DatabaseHelper(getContext());
        productList = new ArrayList<>();

        fetchProducts();

        return view;
    }

    private void fetchProducts() {
        progressBar.setVisibility(View.VISIBLE);
        
        // Simulating JSON response
        String jsonResponse = "[" +
                "{\"id\":1, \"name\":\"iPhone 13\", \"price\":\"$700\", \"description\":\"Apple iPhone 13 128GB\", \"image_url\":\"\"}," +
                "{\"id\":2, \"name\":\"Samsung S22\", \"price\":\"$600\", \"description\":\"Samsung Galaxy S22\", \"image_url\":\"\"}," +
                "{\"id\":3, \"name\":\"MacBook Air\", \"price\":\"$999\", \"description\":\"M1 Chip MacBook Air\", \"image_url\":\"\"}," +
                "{\"id\":4, \"name\":\"Sony WH-1000XM4\", \"price\":\"$350\", \"description\":\"Noise Cancelling Headphones\", \"image_url\":\"\"}" +
                "]";

        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                Product product = new Product(
                        obj.getInt("id"),
                        obj.getString("name"),
                        obj.getString("price"),
                        obj.getString("description"),
                        obj.getString("image_url")
                );
                productList.add(product);
            }
            adapter = new ProductAdapter(productList, dbHelper);
            rvProducts.setAdapter(adapter);
        } catch (JSONException e) {
            Toast.makeText(getContext(), "Error parsing JSON", Toast.LENGTH_SHORT).show();
        } finally {
            progressBar.setVisibility(View.GONE);
        }
    }
}