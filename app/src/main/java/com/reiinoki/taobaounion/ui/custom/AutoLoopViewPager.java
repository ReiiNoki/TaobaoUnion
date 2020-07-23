package com.reiinoki.taobaounion.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.reiinoki.taobaounion.R;

/**
 *
 *
 */

public class AutoLoopViewPager extends ViewPager {

    public static final long DEFAULT_DURATION = 3000;

    private long mDuration = DEFAULT_DURATION;

    public AutoLoopViewPager(@NonNull Context context) {
        this(context, null);
    }

    public AutoLoopViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.AutoLoopStyle);
        mDuration = t.getInteger(R.styleable.AutoLoopStyle_duration, (int) DEFAULT_DURATION);
        t.recycle();
    }

    private boolean isLoop = false;

    /**
     * set duration of the loop
     * @param duration ms
     */
    public void setDuration(long duration) {
        this.mDuration = duration;
    }

    public void startLoop() {
        isLoop = true;
        //get current position
        post(mTask);
    }

    private Runnable mTask = new Runnable() {
        @Override
        public void run() {
            int currentItem = getCurrentItem();
            currentItem++;
            setCurrentItem(currentItem);
            if (isLoop) {
                postDelayed(this,1000);
            }
        }
    };

    public void stopLoop() {
        isLoop = false;
    }
}
