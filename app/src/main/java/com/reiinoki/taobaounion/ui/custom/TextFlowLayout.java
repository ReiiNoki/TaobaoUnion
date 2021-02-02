package com.reiinoki.taobaounion.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.reiinoki.taobaounion.R;
import com.reiinoki.taobaounion.ui.fragment.SearchFragment;
import com.reiinoki.taobaounion.utils.LogUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TextFlowLayout extends ViewGroup {

    public static final float DEFAULT_SPACE = 10;
    private float mItemHorizontalSpace = DEFAULT_SPACE;
    private float mItemVerticalSpace = DEFAULT_SPACE;
    private int mSelfWidth;
    private int mItemHeight;
    private OnFlowTextItemClickListener mItemClickListener = null;

    public int getContentSize() {
        return mTextList.size();
    }

    public float getItemHorizontalSpace() {
        return mItemHorizontalSpace;
    }

    public void setItemHorizontalSpace(float itemHorizontalSpace) {
        mItemHorizontalSpace = itemHorizontalSpace;
    }

    public float getItemVerticalSpace() {
        return mItemVerticalSpace;
    }

    public void setItemVerticalSpace(float itemVerticalSpace) {
        mItemVerticalSpace = itemVerticalSpace;
    }

    private List<String> mTextList = new ArrayList<>();

    public TextFlowLayout(Context context) {
        this(context, null);
    }

    public TextFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //get attributes
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.TextFlowLayoutStyle);
        mItemHorizontalSpace = typedArray.getDimension(R.styleable.TextFlowLayoutStyle_horizontalSpace, DEFAULT_SPACE);
        mItemVerticalSpace = typedArray.getDimension(R.styleable.TextFlowLayoutStyle_verticalSpace, DEFAULT_SPACE);
        typedArray.recycle();
        LogUtils.debug(this,"mItemHorizontalSpace == > " + mItemHorizontalSpace);
        LogUtils.debug(this,"mItemVerticalSpace == > " + mItemVerticalSpace);
    }

    public void setTextList(List<String> textList) {
        removeAllViews();
        this.mTextList.clear();
        this.mTextList.addAll(textList);
        Collections.reverse(mTextList);

        for (String text : textList) {
            // equal LayoutInflater.from(getContext()).inflate(R.layout.flow_text_view, this, true); add view automatically
            TextView item = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.flow_text_view, this, false);
            item.setText(text);
            item.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onFlowItemClick(text);
                    }
                }
            });
            addView(item);
        }
    }
    

    //all lines
    private List<List<View>> lines = new ArrayList<>();

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount() == 0) {
            return;
        }
        //each line of history items
        List<View> line = null;
        lines.clear();
        mSelfWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        LogUtils.debug(this,"mSelfWidth == > " + mSelfWidth);
        LogUtils.debug(this,"onMeasure -- > " + getChildCount());
        //measure child
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View itemView = getChildAt(i);
            if (itemView.getVisibility() != VISIBLE) {
                continue;
            }
            measureChild(itemView, widthMeasureSpec, heightMeasureSpec);
            if (line == null) {
                //current line is empty, can add a new line
                line = createNewLine(itemView);
            } else {
                if (canBeAdded(itemView, line)) {
                    line.add(itemView);
                } else {
                    line = createNewLine(itemView);
                }
            }
        }
        mItemHeight = getChildAt(0).getMeasuredHeight();
        int selfHeight = (int) (lines.size() * mItemHeight + mItemVerticalSpace * (lines.size() + 1) + 0.5f);
        //measure itself
        setMeasuredDimension(mSelfWidth, selfHeight);
    }

    private List<View> createNewLine(View itemView) {
        List<View> line = new ArrayList<>();
        line.add(itemView);
        lines.add(line);
        return line;
    }

    /**
     *
     * @param itemView
     * @param line
     */
    private boolean canBeAdded(View itemView, List<View> line) {
        //all the width of views added in the line + the width of view will be added
        int totalWidth = itemView.getMeasuredWidth();
        for (View view : line) {
            //all the width added
            totalWidth += view.getMeasuredWidth();
        }
        totalWidth += mItemHorizontalSpace * (line.size() + 1);
        //if total width was less than the screen, add the view; if not, do not add
        return totalWidth <= mSelfWidth;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        LogUtils.debug(this,"onLayout -- > " + getChildCount());
        int topOffset = (int) mItemHorizontalSpace;
        for (List<View> views : lines) {
            int leftOffset = (int) mItemVerticalSpace;
            for (View view : views) {
                view.layout(leftOffset, topOffset, leftOffset + view.getMeasuredWidth(), topOffset + view.getMeasuredHeight());
                leftOffset += view.getMeasuredWidth() + mItemHorizontalSpace;
            }
            topOffset += mItemHeight + mItemHorizontalSpace;
        }
    }

    public void setOnFlowTextItemClickListener(OnFlowTextItemClickListener onFlowTextItemClickListener) {
        this.mItemClickListener = onFlowTextItemClickListener;
    }


    public interface OnFlowTextItemClickListener {
        void onFlowItemClick(String text);
    }
}
