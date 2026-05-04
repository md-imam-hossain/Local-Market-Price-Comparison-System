package com.quick.localmarketprice;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private List<ItemModel> itemList;
    private List<ItemModel> filteredList;
    private DrawerLayout drawerLayout;
    private ProgressBar progressBar;
    private View noDataLayout;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // 2. Setup Navigation Drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        
        // Force drawer icon to be white
        toggle.getDrawerArrowDrawable().setColor(Color.WHITE);
        
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // 3. UI Elements
        progressBar = findViewById(R.id.progressBar);
        noDataLayout = findViewById(R.id.noDataLayout);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AddProductActivity.class));
        });

        // 4. Firebase Setup
        itemList = new ArrayList<>();
        filteredList = new ArrayList<>();
        itemAdapter = new ItemAdapter(filteredList, this);
        recyclerView.setAdapter(itemAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Products");
        loadDataFromFirebase();

        // 5. Setup Search Bar
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                itemAdapter.setSearchQuery(newText);
                filter(newText);
                return true;
            }
        });
    }

    private void loadDataFromFirebase() {
        progressBar.setVisibility(View.VISIBLE);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ItemModel item = dataSnapshot.getValue(ItemModel.class);
                    if (item != null) {
                        itemList.add(item);
                    }
                }
                progressBar.setVisibility(View.GONE);
                updateEmptyState();
                filter(""); // Show all data initially
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Failed to load data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filter(String text) {
        if (text == null) text = "";
        filteredList.clear();
        for (ItemModel item : itemList) {
            String name = item.getName();
            if (name != null && name.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        itemAdapter.notifyDataSetChanged();
        updateEmptyState();
    }

    private void updateEmptyState() {
        if (filteredList.isEmpty()) {
            noDataLayout.setVisibility(View.VISIBLE);
        } else {
            noDataLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            Toast.makeText(this, "Home Clicked", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_categories) {
            Toast.makeText(this, "Categories Clicked", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_about_developer) {
            startActivity(new Intent(this, AboutDeveloperActivity.class));
        } else if (id == R.id.nav_settings) {
            Toast.makeText(this, "Settings Clicked", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out this app!");
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, null));
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
