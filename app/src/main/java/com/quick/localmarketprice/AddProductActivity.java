package com.quick.localmarketprice;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AddProductActivity extends AppCompatActivity {

    private EditText etName, etPrice, etImage;
    private EditText etM1, etM2, etM3, etM4, etM5;
    private EditText etP1, etP2, etP3, etP4, etP5;
    private EditText etL1, etL2, etL3, etL4, etL5;
    private Button btnSave;
    private ProgressBar progressBar;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        // Basic Info
        etName = findViewById(R.id.etProductName);
        etPrice = findViewById(R.id.etProductPrice);
        etImage = findViewById(R.id.etProductImage);

        // Market Names
        etM1 = findViewById(R.id.etM1);
        etM2 = findViewById(R.id.etM2);
        etM3 = findViewById(R.id.etM3);
        etM4 = findViewById(R.id.etM4);
        etM5 = findViewById(R.id.etM5);

        // Market Prices
        etP1 = findViewById(R.id.etP1);
        etP2 = findViewById(R.id.etP2);
        etP3 = findViewById(R.id.etP3);
        etP4 = findViewById(R.id.etP4);
        etP5 = findViewById(R.id.etP5);

        // Market Links
        etL1 = findViewById(R.id.etL1);
        etL2 = findViewById(R.id.etL2);
        etL3 = findViewById(R.id.etL3);
        etL4 = findViewById(R.id.etL4);
        etL5 = findViewById(R.id.etL5);

        btnSave = findViewById(R.id.btnSave);
        progressBar = findViewById(R.id.addProgressBar);

        databaseReference = FirebaseDatabase.getInstance().getReference("Products");

        btnSave.setOnClickListener(v -> saveProduct());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Add Product");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void saveProduct() {
        String name = etName.getText().toString().trim();
        String price = etPrice.getText().toString().trim();
        String image = etImage.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(price) || TextUtils.isEmpty(image)) {
            Toast.makeText(this, "Please fill Name, Price and Image URL", Toast.LENGTH_SHORT).show();
            return;
        }

        List<MarketModel> markets = new ArrayList<>();
        addMarketToList(markets, etM1, etP1, etL1);
        addMarketToList(markets, etM2, etP2, etL2);
        addMarketToList(markets, etM3, etP3, etL3);
        addMarketToList(markets, etM4, etP4, etL4);
        addMarketToList(markets, etM5, etP5, etL5);

        progressBar.setVisibility(View.VISIBLE);
        btnSave.setEnabled(false);

        String id = databaseReference.push().getKey();
        
        ItemModel product = new ItemModel(id, name, price, image, markets);

        if (id != null) {
            databaseReference.child(id).setValue(product).addOnCompleteListener(task -> {
                progressBar.setVisibility(View.GONE);
                btnSave.setEnabled(true);
                if (task.isSuccessful()) {
                    Toast.makeText(AddProductActivity.this, "Product added successfully", Toast.LENGTH_SHORT).show();
                    finish(); 
                } else {
                    String error = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                    Toast.makeText(AddProductActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void addMarketToList(List<MarketModel> list, EditText etM, EditText etP, EditText etL) {
        String mName = etM.getText().toString().trim();
        String mPrice = etP.getText().toString().trim();
        String mLink = etL.getText().toString().trim();

        if (!TextUtils.isEmpty(mName)) {
            list.add(new MarketModel(mName, mPrice, mLink));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }
}
