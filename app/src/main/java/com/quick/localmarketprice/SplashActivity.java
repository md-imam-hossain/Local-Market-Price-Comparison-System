package com.quick.localmarketprice;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

public class SplashActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView tvProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Force Light Mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Hide ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Allow layout behind system bars (optional safe mode)
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);

        // STATUS BAR SETTINGS (WHITE + DARK ICONS)
        getWindow().setStatusBarColor(getResources().getColor(R.color.white));

        WindowInsetsControllerCompat controller =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());

        if (controller != null) {
            controller.show(WindowInsetsCompat.Type.statusBars());
            controller.setAppearanceLightStatusBars(true); // dark icons
        }

        progressBar = findViewById(R.id.splashProgressBar);
        tvProgress = findViewById(R.id.tvProgress);

        startLoading();
    }

    private void startLoading() {
        ValueAnimator animator = ValueAnimator.ofInt(0, 100);
        animator.setDuration(3000);

        animator.addUpdateListener(animation -> {
            int progress = (int) animation.getAnimatedValue();

            if (progressBar != null) {
                progressBar.setProgress(progress);
            }

            if (tvProgress != null) {
                tvProgress.setText(progress + "%");
            }

            if (progress == 100) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        });

        animator.start();
    }
}