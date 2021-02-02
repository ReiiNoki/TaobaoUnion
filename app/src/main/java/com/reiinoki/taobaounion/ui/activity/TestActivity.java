package com.reiinoki.taobaounion.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.reiinoki.taobaounion.R;
import com.reiinoki.taobaounion.ui.custom.TextFlowLayout;
import com.reiinoki.taobaounion.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestActivity extends Activity {

    @BindView(R.id.test_navigation_bar)
    public RadioGroup navigationBar;

    @BindView(R.id.test_flow_layout)
    public TextFlowLayout mTextFlowLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        ButterKnife.bind(this);
        initListener();

        List<String> testList = new ArrayList<>();

        testList.add("电脑");
        testList.add("电脑显示器");
        testList.add("Nuxt.js");
        testList.add("Vue.js课程");
        testList.add("机械键盘");
        testList.add("滑板鞋");
        testList.add("运动鞋");
        testList.add("肥宅快乐水");
        testList.add("阳光沙滩");
        testList.add("android编程");
        testList.add("机械键盘");
        testList.add("滑板鞋");
        testList.add("运动鞋");
        testList.add("肥宅快乐水");
        testList.add("阳光沙滩");
        testList.add("android编程");
        testList.add("机械键盘");
        testList.add("滑板鞋");
        testList.add("肥宅快乐水");
        testList.add("阳光沙滩");
        testList.add("android编程");
        testList.add("机械键盘");
        testList.add("滑板鞋");
        testList.add("运动鞋");
        testList.add("肥宅快乐水");
        testList.add("阳光沙滩");
        testList.add("android编程");
        testList.add("JavaWeb后台");

        mTextFlowLayout.setTextList(testList);
        mTextFlowLayout.setOnFlowTextItemClickListener(new TextFlowLayout.OnFlowTextItemClickListener() {
            @Override
            public void onFlowItemClick(String text) {
                LogUtils.debug(TestActivity.this, "click text: " + text);
            }
        });
    }

    public void showToast(View view) {
        Toast.makeText(this, "test", Toast.LENGTH_SHORT).show();
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
