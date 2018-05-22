package com.lxb.imagepreview.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.lxb.imagepreview.R;

/**
 * 作者：tiger on 18/5/22 17:16
 */
public class DotTextView extends AppCompatTextView implements ViewPager.OnPageChangeListener {
    private int currentIndex;
    private int allCount;

    public DotTextView(Context context) {
        super(context);
    }

    public DotTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DotTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 绑定viewpager
     *
     * @param viewPager viewPager
     */
    public void attachToViewpager(ViewPager viewPager) {
        viewPager.removeOnPageChangeListener(this);
        viewPager.addOnPageChangeListener(this);
        allCount = viewPager.getAdapter().getCount();
        currentIndex = viewPager.getCurrentItem();

        @SuppressLint("StringFormatMatches")
        String str = getContext().getString(R.string.string_count, (currentIndex + 1), allCount);
        setText(str);

        invalidate();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //当被选中的时候设置小圆点和当前位置
        @SuppressLint("StringFormatMatches")
        String str = getContext().getString(R.string.string_count, (position + 1), allCount);
        setText(str);

        currentIndex = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
