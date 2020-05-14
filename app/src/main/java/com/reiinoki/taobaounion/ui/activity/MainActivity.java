package com.reiinoki.taobaounion.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.reiinoki.taobaounion.R;
import com.reiinoki.taobaounion.base.BaseFragment;
import com.reiinoki.taobaounion.ui.fragment.HomeFragment;
import com.reiinoki.taobaounion.ui.fragment.RedPackFragment;
import com.reiinoki.taobaounion.ui.fragment.SaleFragment;
import com.reiinoki.taobaounion.ui.fragment.SearchFragment;
import com.reiinoki.taobaounion.utils.LogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @BindView(R.id.main_navigation_bar)
    public BottomNavigationView bottomNavigationView;
    private HomeFragment mHomeFragment;
    private RedPackFragment mRedPackFragment;
    private SaleFragment mSaleFragment;
    private SearchFragment mSearchFragment;
    private FragmentManager mFm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        initView();
        initFragment();
        initListener();
    }


    private void initFragment() {
        mHomeFragment = new HomeFragment();
        mRedPackFragment = new RedPackFragment();
        mSaleFragment = new SaleFragment();
        mSearchFragment = new SearchFragment();
        mFm = getSupportFragmentManager();
        switchFragment(mHomeFragment);
    }

    private void initListener() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d(TAG, "title -> " + item.getTitle());
                switch (item.getItemId()) {
                    case R.id.home:
                        LogUtils.debug(MainActivity.class, "now home");
                        switchFragment(mHomeFragment);
                        break;
                    case R.id.sale:
                        LogUtils.debug(MainActivity.class, "now sale");
                        switchFragment(mSaleFragment);
                        break;
                    case R.id.red_pack:
                        LogUtils.debug(MainActivity.class, "now red pack");
                        switchFragment(mRedPackFragment);
                        break;
                    case R.id.search:
                        LogUtils.debug(MainActivity.class, "now search");
                        switchFragment(mSearchFragment);
                        break;
                }
                return true;
            }
        });
    }

    private void switchFragment(BaseFragment targetFragment) {
        FragmentTransaction transaction = mFm.beginTransaction();
        transaction.replace(R.id.main_page_container, targetFragment);
        transaction.commit();
    }


//    private void initView() {
//        HomeFragment homeFragment = new HomeFragment();
//        FragmentManager fm = getSupportFragmentManager();
//        FragmentTransaction transaction = fm.beginTransaction();
//        transaction.add(R.id.main_page_container, homeFragment);
//        transaction.commit();
//    }
}
