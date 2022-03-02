package com.example.commutual;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;

                switch (item.getItemId()) {
                    case R.id.home_page:
                        fragment = new HomeFragment();
                        break;
                    case R.id.explore_page:
                        fragment = new ExploreFragment();
                        break;
                    case R.id.chat_page:
                        fragment = new ChatFragment();
                        break;
                    case R.id.profile_page:
                        fragment = new ProfileFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                return true;
            }
        });

        // as soon as the application opens the first
        // fragment should be shown to the user
        // in this case it is algorithm fragment
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
//
//        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.explore_page:
//                        Toast.makeText(MainActivity.this, "Recents", Toast.LENGTH_SHORT).show();
//                        break;
//                    case R.id.chat_page:
//                        Toast.makeText(MainActivity.this, "Favorites", Toast.LENGTH_SHORT).show();
//                        break;
//                    case R.id.profile_page:
//                        Toast.makeText(MainActivity.this, "Nearby", Toast.LENGTH_SHORT).show();
//                        break;
//
//                }
//                return true;
//            }
//        });
//    }
    }
}