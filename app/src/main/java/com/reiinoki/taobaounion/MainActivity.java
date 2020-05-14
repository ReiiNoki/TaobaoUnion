package com.reiinoki.taobaounion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.reiinoki.taobaounion.ui.fragment.HomeFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @BindView(R.id.main_navigation_bar)
    public BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initListener();
    }

    private void initListener() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d(TAG, "title -> " + item.getTitle());
                switch (item.getItemId()) {
                    case R.id.home:
                        Log.d(TAG, "onNavigationItemSelected: home");
                        break;
                    case R.id.sale:
                        Log.d(TAG, "onNavigationItemSelected: sale");
                        break;
                    case R.id.red_pack:
                        Log.d(TAG, "onNavigationItemSelected: red pack");
                        break;
                    case R.id.search:
                        Log.d(TAG, "onNavigationItemSelected: search");
                        break;
                }
                return true;
            }
        });
    }


    private void initView() {
        HomeFragment homeFragment = new HomeFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.main_page_container, homeFragment);
        transaction.commit();
    }
}
