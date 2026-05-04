package com.quick.localmarketprice;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<ItemModel> itemList;
    private Context context;
    private String searchQuery = "";

    public ItemAdapter(List<ItemModel> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    public void setSearchQuery(String query) {
        this.searchQuery = query;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemModel item = itemList.get(position);
        String name = item.getName();
        
        if (searchQuery != null && !searchQuery.isEmpty() && name != null) {
            SpannableString spannableString = new SpannableString(name);
            int startPos = name.toLowerCase().indexOf(searchQuery.toLowerCase());
            if (startPos != -1) {
                int endPos = startPos + searchQuery.length();
                spannableString.setSpan(new ForegroundColorSpan(Color.RED), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.nameTextView.setText(spannableString);
            } else {
                holder.nameTextView.setText(name);
            }
        } else {
            holder.nameTextView.setText(name);
        }

        holder.priceTextView.setText(item.getPrice());

        // Use Glide to load image from URL
        Glide.with(context)
                .load(item.getImage())
                .placeholder(R.drawable.ls)
                .error(R.drawable.lr)
                .into(holder.productImageView);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra("item", item);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImageView;
        TextView nameTextView, priceTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.productImage);
            nameTextView = itemView.findViewById(R.id.productName);
            priceTextView = itemView.findViewById(R.id.productPrice);
        }
    }
}
