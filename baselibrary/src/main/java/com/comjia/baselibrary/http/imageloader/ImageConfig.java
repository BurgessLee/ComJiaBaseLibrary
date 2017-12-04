package com.comjia.baselibrary.http.imageloader;

import android.widget.ImageView;

/**
 * ================================================
 * 这里是图片加载配置信息的基类, 定义一些所有图片加载框架都可以用的通用参数
 * 每个 {@link BaseImageLoaderStrategy} 应该对应一个 {@link ImageConfig} 实现类
 * ================================================
 */
public class ImageConfig {

    protected String mUrl;
    protected ImageView mImageView;
    /**
     * 占位符
     */
    protected int mPlaceholder;
    /**
     * 错误占位符
     */
    protected int mErrorPic;

    public String getUrl() {
        return mUrl;
    }

    public ImageView getImageView() {
        return mImageView;
    }

    public int getPlaceholder() {
        return mPlaceholder;
    }

    public int getErrorPic() {
        return mErrorPic;
    }
}
