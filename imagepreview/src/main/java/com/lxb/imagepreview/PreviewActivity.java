package com.lxb.imagepreview;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.lxb.imagepreview.entity.IThumbViewInfo;
import com.lxb.imagepreview.widget.BezierBannerView;
import com.lxb.imagepreview.widget.DotTextView;
import com.lxb.imagepreview.widget.PhotoViewPager;
import com.lxb.imagepreview.widget.SmoothImageView;

import java.util.ArrayList;

public class PreviewActivity extends AppCompatActivity {
    private static final String TAG = PreviewActivity.class.getName();
    /*** 图片的地址***/
    private ArrayList<IThumbViewInfo> imgUrls;

    /*** 当前图片的位置 ***/
    private int currentIndex;
    /*** 展示图片的viewPager ***/
    private PhotoViewPager viewPager;

    private PhotoPagerAdapter photoPagerAdapter;
    /***指示器类型枚举***/
    private PreviewBuilder.IndicatorType type;
    /***默认显示***/
    private boolean isShow = true;

    private boolean isSingleFling = true;

    private boolean isDrag = true;

    @CallSuper
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview_photo);
        View view = findViewById(R.id.img_preview_fl);
        view.setBackgroundColor(Color.TRANSPARENT);

        initData();
        initView();
    }

    @Override
    protected void onDestroy() {
        ZoomMediaLoader.getInstance().getLoader().clearMemory(this);
        if (viewPager != null) {
            viewPager.setAdapter(null);
            viewPager.clearOnPageChangeListeners();
            viewPager.removeAllViews();
            viewPager = null;
        }
        if (imgUrls != null) {
            imgUrls.clear();
            imgUrls = null;
        }
        super.onDestroy();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        imgUrls = getIntent().getParcelableArrayListExtra("imagePaths");
        currentIndex = getIntent().getIntExtra("position", -1);
        type = (PreviewBuilder.IndicatorType) getIntent().getSerializableExtra("type");
        isShow = getIntent().getBooleanExtra("isShow", true);

        isSingleFling = getIntent().getBooleanExtra("isSingleFling", false);
        isDrag = getIntent().getBooleanExtra("isDrag", false);

        int duration = getIntent().getIntExtra("duration", 300);
        SmoothImageView.setDuration(duration);

        if (imgUrls == null) {
            finish();
        }
    }

    /**
     * 初始化控件
     */
    @SuppressLint("StringFormatMatches")
    private void initView() {
        viewPager = findViewById(R.id.viewPager);
        //viewPager的适配器
        photoPagerAdapter = new PhotoPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(photoPagerAdapter);
        viewPager.setCurrentItem(currentIndex);
        viewPager.setOffscreenPageLimit(3);
        /**
         * 指示器控件*/
        BezierBannerView bezierBannerView = findViewById(R.id.bezierBannerView);
        /** 显示图片数*/
        DotTextView ltAddDot = findViewById(R.id.ltAddDot);
        if (type == PreviewBuilder.IndicatorType.Dot) {
            bezierBannerView.setVisibility(View.VISIBLE);
            bezierBannerView.attachToViewpager(viewPager);
        } else {
            ltAddDot.setVisibility(View.VISIBLE);
            ltAddDot.attachToViewpager(viewPager);
        }
        if (imgUrls.size() == 1) {
            if (!isShow) {
                bezierBannerView.setVisibility(View.GONE);
                ltAddDot.setVisibility(View.GONE);
            }
        }
        viewPager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                viewPager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                PhotoFragment fragment = (PhotoFragment) photoPagerAdapter.getItem(currentIndex);
                fragment.transformIn();
            }
        });
    }

    /**
     * 关闭页面
     */
    public void exit() {
        finish();
        overridePendingTransition(0, 0);
    }

    /***
     * 得到PhotoViewPager
     * @return PhotoViewPager
     * **/
    public PhotoViewPager getViewPager() {
        return viewPager;
    }

    /***
     * 自定义布局内容
     * @return int
     ***/
    public int setContentLayout() {
        return 0;
    }

    /**
     * pager的适配器
     */
    private class PhotoPagerAdapter extends FragmentPagerAdapter {

        PhotoPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PhotoFragment.newInstance(imgUrls.get(position), currentIndex == position, isSingleFling, isDrag);
        }

        @Override
        public int getCount() {
            return imgUrls == null ? 0 : imgUrls.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }
    }
}
