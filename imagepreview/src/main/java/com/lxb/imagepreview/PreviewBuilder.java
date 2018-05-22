package com.lxb.imagepreview;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.lxb.imagepreview.entity.IThumbViewInfo;

import java.util.ArrayList;
import java.util.List;

public final class PreviewBuilder {
    private Activity mContext;
    private Intent intent;
    private Class className;

    private PreviewBuilder(@NonNull Activity activity) {
        mContext = activity;
        intent = new Intent();
    }

    /***
     * 设置开始启动预览
     * @param activity  启动
     *@return PreviewBuilder
     * **/
    public static PreviewBuilder from(@NonNull Activity activity) {
        return new PreviewBuilder(activity);
    }

    /***
     * 设置开始启动预览
     * @param fragment  启动
     *@return PreviewBuilder
     * **/
    public static PreviewBuilder from(@NonNull Fragment fragment) {
        return new PreviewBuilder(fragment.requireActivity());
    }

    /****
     *自定义预览activity 类名
     * @param className   继承GPreviewActivity
     *@return PreviewBuilder
     * **/
    public PreviewBuilder to(@NonNull Class className) {
        this.className = className;
        intent.setClass(mContext, className);
        return this;
    }

    /***
     * 设置数据源
     * @param imgUrls 数据
     *@param   <T>    你的实体类类型
     * @return PreviewBuilder
     * **/
    public <T extends IThumbViewInfo> PreviewBuilder setData(@NonNull List<T> imgUrls) {
        intent.putParcelableArrayListExtra("imagePaths", new ArrayList<T>(imgUrls));
        return this;
    }

    /***
     * 设置单个数据源
     * @param imgUrl 数据
     *@param   <E>    你的实体类类型
     * @return PreviewBuilder
     * **/
    public <E extends IThumbViewInfo> PreviewBuilder setSingleData(@NonNull E imgUrl) {
        ArrayList<E> arrayList = new ArrayList<>();
        arrayList.add(imgUrl);
        intent.putParcelableArrayListExtra("imagePaths", arrayList);
        return this;
    }

    /***
     * 设置默认索引
     * @param currentIndex 数据
     * @return PreviewBuilder
     * **/
    public PreviewBuilder setCurrentIndex(int currentIndex) {
        intent.putExtra("position", currentIndex);
        return this;
    }

    /***
     * 设置指示器类型
     * @param indicatorType 枚举
     * @return PreviewBuilder
     * **/
    public PreviewBuilder setType(@NonNull IndicatorType indicatorType) {
        intent.putExtra("type", indicatorType);
        return this;
    }

    /***
     * 设置图片禁用拖拽返回
     * @param isDrag  true  可以 false 默认 true
     * @return PreviewBuilder
     * **/
    public PreviewBuilder setDrag(boolean isDrag) {
        intent.putExtra("isDrag", isDrag);
        return this;
    }

    /***
     * 是否设置为一张图片时 显示指示器  默认显示
     * @param isShow   true  显示 false 不显示
     * @return PreviewBuilder
     * **/
    public PreviewBuilder setSingleShowType(boolean isShow) {
        intent.putExtra("isShow", isShow);
        return this;
    }

    /***
     * 设置超出内容点击退出（黑色区域）
     * @param isSingleFling  true  可以 false
     * @return PreviewBuilder
     * **/
    public PreviewBuilder setSingleFling(boolean isSingleFling) {
        intent.putExtra("isSingleFling", isSingleFling);
        return this;
    }

    /***
     *  设置动画的时长
     * @param setDuration  单位毫秒
     * @return PreviewBuilder
     * **/
    public PreviewBuilder setDuration(int setDuration) {
        intent.putExtra("duration", setDuration);
        return this;
    }

    /***
     * 启动
     * **/
    public void start() {
        if (className == null) {
            intent.setClass(mContext, PreviewActivity.class);
        } else {
            intent.setClass(mContext, className);
        }
        mContext.startActivity(intent);
        mContext.overridePendingTransition(0, 0);
        intent = null;
        mContext = null;
    }

    public enum IndicatorType {
        Dot, Number
    }
}
