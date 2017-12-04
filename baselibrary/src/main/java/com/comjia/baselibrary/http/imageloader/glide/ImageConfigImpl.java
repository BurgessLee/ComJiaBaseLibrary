package com.comjia.baselibrary.http.imageloader.glide;

import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.comjia.baselibrary.http.imageloader.ImageConfig;

/**
 * ================================================
 * 这里存放图片请求的配置信息, 可以一直扩展字段, 如果外部调用时想让图片加载框架
 * 做一些操作, 比如清除缓存或者切换缓存策略, 则可以定义一个 int 类型的变量, 内部根据 switch(int) 做不同的操作
 * 其他操作同理
 * ================================================
 */
public class ImageConfigImpl extends ImageConfig {

    /**
     * 0 - DiskCacheStrategy.ALL,
     * 1 - DiskCacheStrategy.NONE
     * 2 - DiskCacheStrategy.SOURCE
     * 3 - DiskCacheStrategy.RESULT
     */
    private int mCacheStrategy;
    /**
     * 请求 url 为空, 则使用此图片作为占位符
     */
    private int mFallback;
    /**
     * glide用它来改变图形的形状
     */
    private BitmapTransformation mTransformation;
    private ImageView[] mImageViews;
    /**
     * 清理内存缓存
     */
    private boolean mIsClearMemory;
    /**
     * 清理本地缓存
     */
    private boolean mIsClearDiskCache;

    private ImageConfigImpl(Builder builder) {
        mUrl = builder.url;
        mImageView = builder.imageView;
        mPlaceholder = builder.placeholder;
        mErrorPic = builder.errorPic;
        mFallback = builder.fallback;
        mCacheStrategy = builder.cacheStrategy;
        mTransformation = builder.transformation;
        mImageViews = builder.imageViews;
        mIsClearMemory = builder.isClearMemory;
        mIsClearDiskCache = builder.isClearDiskCache;
    }

    public static Builder builder() {
        return new Builder();
    }

    public int getCacheStrategy() {
        return mCacheStrategy;
    }

    public BitmapTransformation getTransformation() {
        return mTransformation;
    }

    public ImageView[] getImageViews() {
        return mImageViews;
    }

    public boolean isClearMemory() {
        return mIsClearMemory;
    }

    public boolean isClearDiskCache() {
        return mIsClearDiskCache;
    }

    public int getFallback() {
        return mFallback;
    }

    public static final class Builder {

        private String url;
        private ImageView imageView;
        private int placeholder;
        private int errorPic;
        /**
         * 请求 url 为空,则使用此图片作为占位符
         */
        private int fallback;
        /**
         * 0 - DiskCacheStrategy.ALL,
         * 1 - DiskCacheStrategy.NONE
         * 2 - DiskCacheStrategy.SOURCE
         * 3 - DiskCacheStrategy.RESULT
         */
        private int cacheStrategy;
        /**
         * glide用它来改变图形的形状
         */
        private BitmapTransformation transformation;
        private ImageView[] imageViews;
        /**
         * 清理内存缓存
         */
        private boolean isClearMemory;
        /**
         * 清理本地缓存
         */
        private boolean isClearDiskCache;

        private Builder() {
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder placeholder(int placeholder) {
            this.placeholder = placeholder;
            return this;
        }

        public Builder errorPic(int errorPic) {
            this.errorPic = errorPic;
            return this;
        }

        public Builder fallback(int fallback) {
            this.fallback = fallback;
            return this;
        }

        public Builder imageView(ImageView imageView) {
            this.imageView = imageView;
            return this;
        }

        public Builder cacheStrategy(int cacheStrategy) {
            this.cacheStrategy = cacheStrategy;
            return this;
        }

        public Builder transformation(BitmapTransformation transformation) {
            this.transformation = transformation;
            return this;
        }

        public Builder imageViews(ImageView... imageViews) {
            this.imageViews = imageViews;
            return this;
        }

        public Builder isClearMemory(boolean isClearMemory) {
            this.isClearMemory = isClearMemory;
            return this;
        }

        public Builder isClearDiskCache(boolean isClearDiskCache) {
            this.isClearDiskCache = isClearDiskCache;
            return this;
        }

        public ImageConfigImpl build() {
            return new ImageConfigImpl(this);
        }
    }
}
