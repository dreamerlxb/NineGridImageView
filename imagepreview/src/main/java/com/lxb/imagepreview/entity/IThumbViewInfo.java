package com.lxb.imagepreview.entity;

import android.graphics.Rect;
import android.os.Parcelable;

/**
 * 作者：tiger on 18/5/22 14:29
 */
public interface IThumbViewInfo extends Parcelable {
    /****
     * 图片地址
     * @return String
     * ****/
    String getUrl();

    /**
     * 记录坐标
     *
     * @return Rect
     ***/
    Rect getBounds();
}
