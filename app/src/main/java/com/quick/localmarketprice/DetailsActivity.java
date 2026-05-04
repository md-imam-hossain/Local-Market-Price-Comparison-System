package com.quick.localmarketprice;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    private RecyclerView rvRecommended;
    private ItemAdapter recommendedAdapter;
    private List<ItemModel> recommendedList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ImageView detailImage = findViewById(R.id.detailImage);
        TextView detailName = findViewById(R.id.detailName);
        TextView detailPrice = findViewById(R.id.detailPrice);
        RecyclerView rvMarkets = findViewById(R.id.rvMarkets);
        rvRecommended = findViewById(R.id.rvRecommended);

        ItemModel item = (ItemModel) getIntent().getSerializableExtra("item");

        if (item != null) {
            detailName.setText(item.getName());
            detailPrice.setText(item.getPrice());
            
            // Setup Markets RecyclerView
            rvMarkets.setLayoutManager(new LinearLayoutManager(this));
            MarketAdapter marketAdapter = new MarketAdapter(item.getMarkets(), this);
            rvMarkets.setAdapter(marketAdapter);

            // Setup Recommended RecyclerView
            setupRecommendedRecyclerView();

            // Load Image
            Glide.with(this)
                    .load(item.getImage())
                    .placeholder(R.drawable.ls)
                    .error(R.drawable.lr)
                    .into(detailImage);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Product Details");
        }
    }

    private void setupRecommendedRecyclerView() {
        recommendedList = new ArrayList<>();
        rvRecommended.setLayoutManager(new GridLayoutManager(this, 2));
        recommendedAdapter = new ItemAdapter(recommendedList, this);
        rvRecommended.setAdapter(recommendedAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Products");
        loadRecommendedProducts();
    }

    private void loadRecommendedProducts() {
        databaseReference.limitToFirst(10).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recommendedList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ItemModel product = dataSnapshot.getValue(ItemModel.class);
                    if (product != null) {
                        recommendedList.add(product);
                    }
                }
                // Shuffle to show random recommendations
                Collections.shuffle(recommendedList);
                recommendedAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DetailsActivity.this, "Failed to load recommendations", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }
}
