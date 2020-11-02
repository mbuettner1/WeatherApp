package com.example.fullweatherapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {    @Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
    bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_weather:
                    Toast.makeText(MainActivity.this, "Recents", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.action_map:
                    Toast.makeText(MainActivity.this, "Favorites", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.action_history:
                    Toast.makeText(MainActivity.this, "Nearby", Toast.LENGTH_SHORT).show();
                    break;                }
            return true;
        }
    });
}
}