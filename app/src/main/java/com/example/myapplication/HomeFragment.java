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
import androidx.recyclerview.widget.GridLayoutManager;
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

        rvProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));

        fetchProducts();

        return view;
    }

    private void fetchProducts() {
        progressBar.setVisibility(View.VISIBLE);
        
        // JSON format from requirement
        String jsonResponse = "{" +
                "\"products\": [" +
                "{\"id\":1, \"title\":\"iPhone 11\", \"price\":85000, \"image_url\":\"\"}," +
                "{\"id\":2, \"title\":\"Honda Bike\", \"price\":120000, \"image_url\":\"\"}," +
                "{\"id\":3, \"title\":\"MacBook Pro\", \"price\":185000, \"image_url\":\"\"}," +
                "{\"id\":4, \"title\":\"Sony Headphones\", \"price\":35000, \"image_url\":\"\"}" +
                "]" +
                "}";

        try {
            JSONObject root = new JSONObject(jsonResponse);
            JSONArray jsonArray = root.getJSONArray("products");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                Product product = new Product(
                        obj.getInt("id"),
                        obj.getString("title"),
                        obj.getInt("price"),
                        obj.optString("image_url", "")
                );
                productList.add(product);
            }
            adapter = new ProductAdapter(productList, dbHelper, false);
            rvProducts.setAdapter(adapter);
        } catch (JSONException e) {
            Toast.makeText(getContext(), "Error parsing JSON", Toast.LENGTH_SHORT).show();
        } finally {
            progressBar.setVisibility(View.GONE);
        }
    }
}
