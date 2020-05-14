package com.reiinoki.taobaounion.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;

import com.reiinoki.taobaounion.R;
import com.reiinoki.taobaounion.utils.LogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestActivity extends Activity {

    @BindView(R.id.test_navigation_bar)
    public RadioGroup navigationBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        ButterKnife.bind(this);
        initListener();
    }

    private void initListener() {
        navigationBar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                LogUtils.debug(TestActivity.class, "checkID -> " + checkedId);
                switch (checkedId) {
                    case R.id.test_home:
                        LogUtils.debug(TestActivity.class, "now home");
                        break;
                    case R.id.test_sale:
                        LogUtils.debug(TestActivity.class, "now sale");
                        break;
                    case R.id.test_red_pack:
                        LogUtils.debug(TestActivity.class, "now red pack");
                        break;
                    case R.id.test_search:
                        LogUtils.debug(TestActivity.class, "now search");
                        break;

                }
            }
        });
    }
}
