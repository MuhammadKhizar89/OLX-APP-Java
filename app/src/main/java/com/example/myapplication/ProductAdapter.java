package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private DatabaseHelper dbHelper;
    private OnProductClickListener listener;

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    public ProductAdapter(List<Product> productList, DatabaseHelper dbHelper) {
        this.productList = productList;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.tvName.setText(product.getName());
        holder.tvPrice.setText(product.getPrice());
        
        // In a real app, use Glide or Picasso to load image from URL
        // holder.ivProduct.setImageResource(...) 

        if (dbHelper.isFavorite(product.getId())) {
            holder.btnFavorite.setImageResource(android.R.drawable.btn_star_big_on);
        } else {
            holder.btnFavorite.setImageResource(android.R.drawable.btn_star_big_off);
        }

        holder.btnFavorite.setOnClickListener(v -> {
            if (dbHelper.isFavorite(product.getId())) {
                dbHelper.removeFavorite(product.getId());
                holder.btnFavorite.setImageResource(android.R.drawable.btn_star_big_off);
            } else {
                dbHelper.addFavorite(product);
                holder.btnFavorite.setImageResource(android.R.drawable.btn_star_big_on);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProduct;
        TextView tvName, tvPrice;
        ImageButton btnFavorite;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProduct = itemView.findViewById(R.id.ivProduct);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
            btnFavorite = itemView.findViewById(R.id.btnFavorite);
        }
    }
}