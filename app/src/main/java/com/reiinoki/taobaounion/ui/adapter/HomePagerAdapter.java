package com.reiinoki.taobaounion.ui.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.reiinoki.taobaounion.model.domain.Categories;
import com.reiinoki.taobaounion.ui.fragment.HomePagerFragment;
import com.reiinoki.taobaounion.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class HomePagerAdapter extends FragmentPagerAdapter {

    private List<Categories.DataBean> categoryList = new ArrayList<>();

    public HomePagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return categoryList.get(position).getTitle();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        LogUtils.debug(this, "getItem -> " + position);
        Categories.DataBean dataBean = categoryList.get(position);
        HomePagerFragment homePagerFragment = HomePagerFragment.newInstance(dataBean);
        return homePagerFragment;
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    public void setCategories(Categories categories) {
        categoryList.clear();
        List<Categories.DataBean> data = categories.getData();
        this.categoryList.addAll(data);
        LogUtils.debug(this, "size -> " + this.categoryList.size());
        notifyDataSetChanged();
    }
}
