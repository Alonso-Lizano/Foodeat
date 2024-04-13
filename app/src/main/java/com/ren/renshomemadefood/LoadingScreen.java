package com.ren.renshomemadefood;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ren.renshomemadefood.content.screens.MainScreen;

public class LoadingScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_screen);

        Animation belowAnimation = AnimationUtils.loadAnimation(this, R.anim.below);

        TextView tv = findViewById(R.id.tv_name);

        tv.setAnimation(belowAnimation);

        new Handler().postDelayed(() -> {
            if (isConnected()) {
                startActivity(new Intent(LoadingScreen.this, MainScreen.class));
            } else {
                startActivity(new Intent(LoadingScreen.this, NoInternetScreen.class));
            }
            finish();
        }, 3000);
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkCapabilities capabilities = connectivityManager
                    .getNetworkCapabilities(connectivityManager.getActiveNetwork());
            return capabilities != null && (capabilities
                    .hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
        }
        return false;
    }
}