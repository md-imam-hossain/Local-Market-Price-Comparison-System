package com.quick.localmarketprice;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MarketAdapter extends RecyclerView.Adapter<MarketAdapter.ViewHolder> {

    private List<MarketModel> marketList;
    private Context context;

    public MarketAdapter(List<MarketModel> marketList, Context context) {
        this.marketList = marketList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_market_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MarketModel market = marketList.get(position);
        holder.tvName.setText(market.getName());
        holder.tvPrice.setText(market.getPrice());

        if (market.getLink() != null && !market.getLink().isEmpty()) {
            holder.btnCheckOut.setVisibility(View.VISIBLE);
            holder.btnCheckOut.setOnClickListener(v -> {
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("url", market.getLink());
                context.startActivity(intent);
            });
        } else {
            holder.btnCheckOut.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return marketList != null ? marketList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice;
        Button btnCheckOut;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvMarketName);
            tvPrice = itemView.findViewById(R.id.tvMarketPrice);
            btnCheckOut = itemView.findViewById(R.id.btnMarketCheckOut);
        }
    }
}
