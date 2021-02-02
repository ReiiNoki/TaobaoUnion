package com.reiinoki.taobaounion.ui.activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.reiinoki.taobaounion.R;
import com.reiinoki.taobaounion.base.BaseActivity;
import com.reiinoki.taobaounion.base.BaseFragment;
import com.reiinoki.taobaounion.ui.fragment.HomeFragment;
import com.reiinoki.taobaounion.ui.fragment.OnSellFragment;
import com.reiinoki.taobaounion.ui.fragment.SelectedFragment;
import com.reiinoki.taobaounion.ui.fragment.SearchFragment;
import com.reiinoki.taobaounion.utils.LogUtils;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements IMainActivity{

    private static final String TAG = "MainActivity";

    @BindView(R.id.main_navigation_bar)
    public BottomNavigationView mNavigationView;
    private HomeFragment mHomeFragment;
    private OnSellFragment mRedPackFragment;
    private SelectedFragment mSaleFragment;
    private SearchFragment mSearchFragment;
    private FragmentManager mFm;


    @Override
    protected void initEvent() {
        initListener();
    }

    @Override
    protected void initView() {
        initFragment();
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    private void initFragment() {
        mHomeFragment = new HomeFragment();
        mRedPackFragment = new OnSellFragment();
        mSaleFragment = new SelectedFragment();
        mSearchFragment = new SearchFragment();
        mFm = getSupportFragmentManager();
        switchFragment(mHomeFragment);
    }

    private void initListener() {
        mNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d(TAG, "title -> " + item.getTitle());
                switch (item.getItemId()) {
                    case R.id.home:
                        LogUtils.debug(this, "now home");
                        switchFragment(mHomeFragment);
                        break;
                    case R.id.selected:
                        LogUtils.debug(this, "now sale");
                        switchFragment(mSaleFragment);
                        break;
                    case R.id.red_pack:
                        LogUtils.debug(this, "now red pack");
                        switchFragment(mRedPackFragment);
                        break;
                    case R.id.search:
                        LogUtils.debug(this, "now search");
                        switchFragment(mSearchFragment);
                        break;
                }
                return true;
            }
        });
    }

    private BaseFragment lastOneFragment = null;

    private void switchFragment(BaseFragment targetFragment) {

        if(lastOneFragment == targetFragment) {
            return;
        }

        FragmentTransaction transaction = mFm.beginTransaction();
        if (!targetFragment.isAdded()) {
            transaction.add(R.id.main_page_container, targetFragment);
        } else {
            transaction.show(targetFragment);
        }
        if (lastOneFragment != null) {
            transaction.hide(lastOneFragment);
        }
        lastOneFragment = targetFragment;
        transaction.commit();
    }

    @Override
    public void switch2Search() {
        //switch to search fragment icon
        mNavigationView.setSelectedItemId(R.id.search);
    }


//    private void initView() {
//        HomeFragment homeFragment = new HomeFragment();
//        FragmentManager fm = getSupportFragmentManager();
//        FragmentTransaction transaction = fm.beginTransaction();
//        transaction.add(R.id.main_page_container, homeFragment);
//        transaction.commit();
//    }
}
