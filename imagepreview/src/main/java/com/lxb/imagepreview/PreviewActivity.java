package com.lxb.imagepreview;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.lxb.imagepreview.entity.IThumbViewInfo;
import com.lxb.imagepreview.widget.BezierBannerView;
import com.lxb.imagepreview.widget.PhotoViewPager;
import com.lxb.imagepreview.widget.SmoothImageView;

import java.util.ArrayList;
import java.util.List;

public class PreviewActivity extends AppCompatActivity {
    private static final String TAG = PreviewActivity.class.getName();
//    private boolean isTransformOut = false;
    /*** 图片的地址***/
    private ArrayList<IThumbViewInfo> imgUrls;
    /*** 当前图片的位置 ***/
    private int currentIndex;
    /*** 图片的展示的Fragment***/
    private List<BasePhotoFragment> fragments = new ArrayList<>();
    /*** 展示图片的viewPager ***/
    private PhotoViewPager viewPager;
    /*** 显示图片数**/
    private TextView ltAddDot;
    /***指示器控件**/
    private BezierBannerView bezierBannerView;
    /***指示器类型枚举***/
    private PreviewBuilder.IndicatorType type;
    /***默认显示***/
    private boolean isShow = true;

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
        if (fragments != null) {
            fragments.clear();
            fragments = null;
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
        int duration = getIntent().getIntExtra("duration", 300);
        try {
            SmoothImageView.setDuration(duration);
            Class<? extends BasePhotoFragment> sss;
            sss = (Class<? extends BasePhotoFragment>) getIntent().getSerializableExtra("className");
            initFragment(imgUrls, currentIndex, sss);
        } catch (Exception e) {
            initFragment(imgUrls, currentIndex, BasePhotoFragment.class);
        }
    }

    /**
     * 初始化
     *
     * @param imgUrls      集合
     * @param currentIndex 选中索引
     * @param className    显示Fragment
     **/
    protected void initFragment(List<IThumbViewInfo> imgUrls, int currentIndex, Class<? extends BasePhotoFragment> className) {
        if (imgUrls != null) {
            int size = imgUrls.size();
            boolean s = getIntent().getBooleanExtra("isSingleFling", false);
            boolean isDrag = getIntent().getBooleanExtra("isDrag", false);
            for (int i = 0; i < size; i++) {
                fragments.add(BasePhotoFragment.getInstance(className, imgUrls.get(i), currentIndex == i, s, isDrag));
            }
        } else {
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
        PhotoPagerAdapter adapter = new PhotoPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(currentIndex);
        viewPager.setOffscreenPageLimit(3);
        bezierBannerView = findViewById(R.id.bezierBannerView);
        ltAddDot = findViewById(R.id.ltAddDot);
        if (type == PreviewBuilder.IndicatorType.Dot) {
            bezierBannerView.setVisibility(View.VISIBLE);
            bezierBannerView.attachToViewpager(viewPager);
        } else {
            ltAddDot.setVisibility(View.VISIBLE);
            ltAddDot.setText(getString(R.string.string_count, (currentIndex + 1), imgUrls.size()));
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    //当被选中的时候设置小圆点和当前位置
                    if (ltAddDot != null) {
                        ltAddDot.setText(getString(R.string.string_count, (position + 1), imgUrls.size()));
                    }
                    currentIndex = position;
                    viewPager.setCurrentItem(currentIndex, true);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
        if (imgUrls.size() == 1) {
            if (!isShow) {
                bezierBannerView.setVisibility(View.GONE);
                ltAddDot.setVisibility(View.GONE);
            }
        }
        viewPager.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
    }

    private ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onGlobalLayout() {
            viewPager.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
            BasePhotoFragment fragment = fragments.get(currentIndex);
            fragment.transformIn();
        }
    };

    /***
     * 得到PhotoFragment集合
     * @return List
     * **/
    public List<BasePhotoFragment> getFragments() {
        return fragments;
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
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments == null ? 0 : fragments.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }
    }
}
