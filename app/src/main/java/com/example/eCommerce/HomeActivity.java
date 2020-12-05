package com.example.eCommerce;


import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class HomeActivity extends AppCompatActivity  {


    BottomNavigationView bottomNavigation;;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null)
        {
            new UtilitiesClass().openFragment(HomeActivity.this,new HomeFragment());
        }
        setContentView(R.layout.activity_home);

        bottomNavigation = findViewById(R.id.bottom_navigation);

        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();

    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            new UtilitiesClass().openFragment(HomeActivity.this,new HomeFragment());
                            return true;
                        case R.id.navigation_add:
                            new UtilitiesClass().openFragment(HomeActivity.this,new AddFragment());
                            return true;
                        case R.id.navigation_favourite:
                            new UtilitiesClass().openFragment(HomeActivity.this,new FavouriteFragment());
                            return true;
                        case R.id.navigation_profile:
                            new UtilitiesClass().openFragment(HomeActivity.this,new UserFragment());
                            return true;
                    }
                    return false;
                }
            };




}