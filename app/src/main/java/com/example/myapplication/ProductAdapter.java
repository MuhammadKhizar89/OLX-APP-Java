package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private DatabaseHelper dbHelper;
    private boolean isFavoriteList;

    public ProductAdapter(List<Product> productList, DatabaseHelper dbHelper, boolean isFavoriteList) {
        this.productList = productList;
        this.dbHelper = dbHelper;
        this.isFavoriteList = isFavoriteList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = isFavoriteList ? R.layout.item_favorite : R.layout.item_product;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.tvName.setText(product.getTitle());
        holder.tvPrice.setText("Rs " + String.format("%,d", product.getPrice()));
        
        if (isFavoriteList) {
            holder.btnDelete.setOnClickListener(v -> {
                dbHelper.removeFavorite(product.getId());
                productList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, productList.size());
            });
        } else {
            boolean isFav = dbHelper.isFavorite(product.getId());

            if (isFav) {
                holder.btnFavorite.setImageResource(android.R.drawable.btn_star_big_on);
                holder.btnFavorite.setColorFilter(
                        ContextCompat.getColor(holder.itemView.getContext(), R.color.primary_blue)
                );
            } else {
                holder.btnFavorite.setImageResource(android.R.drawable.btn_star_big_off);
                holder.btnFavorite.setColorFilter(
                        ContextCompat.getColor(holder.itemView.getContext(), R.color.gray)
                );
            }
            if (dbHelper.isFavorite(product.getId())) {
                holder.btnFavorite.setImageResource(android.R.drawable.btn_star_big_on);
            } else {
                holder.btnFavorite.setImageResource(android.R.drawable.btn_star_big_off);
            }

            holder.btnFavorite.setOnClickListener(v -> {
                if (dbHelper.isFavorite(product.getId())) {
                    dbHelper.removeFavorite(product.getId());
                    holder.btnFavorite.setColorFilter(
                            ContextCompat.getColor(holder.itemView.getContext(), R.color.gray)
                    );
                } else {
                    dbHelper.addFavorite(product);
                    holder.btnFavorite.setColorFilter(
                            ContextCompat.getColor(holder.itemView.getContext(), R.color.primary_blue)
                    );
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProduct;
        TextView tvName, tvPrice;
        ImageButton btnFavorite, btnDelete;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProduct = itemView.findViewById(R.id.ivProduct);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
            btnFavorite = itemView.findViewById(R.id.btnFavorite);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}