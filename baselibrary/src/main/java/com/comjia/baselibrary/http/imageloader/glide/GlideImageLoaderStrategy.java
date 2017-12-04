package com.comjia.baselibrary.http.imageloader.glide;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.comjia.baselibrary.di.module.GlobalConfigModule;
import com.comjia.baselibrary.http.imageloader.BaseImageLoaderStrategy;
import com.comjia.baselibrary.http.imageloader.ImageConfig;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * ================================================
 * 此类只是简单的实现了 Glide 加载的策略, 方便快速使用, 但大部分情况会需要应对复杂的场景
 * 这时可自行实现 {@link BaseImageLoaderStrategy} 和 {@link ImageConfig} 替换现有策略
 *
 * @see GlobalConfigModule.Builder#imageLoaderStrategy(BaseImageLoaderStrategy)
 * ================================================
 */
public class GlideImageLoaderStrategy implements BaseImageLoaderStrategy<ImageConfigImpl>, GlideAppliesOptions {

    @Override
    public void loadImage(Context context, ImageConfigImpl config) {
        if (context == null) {
            throw new NullPointerException("Context is required");
        }

        if (config == null) {
            throw new NullPointerException("ImageConfigImpl is required");
        }

        if (TextUtils.isEmpty(config.getUrl())) {
            throw new NullPointerException("Url is required");
        }

        if (config.getImageView() == null) {
            throw new NullPointerException("ImageView is required");
        }

        // 如果context是Activity, 则自动使用Activity的生命周期
        GlideRequests requests = GlideArms.with(context);
        GlideRequest<Drawable> glideRequest = requests.load(config.getUrl())
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop();

        // 缓存策略
        switch (config.getCacheStrategy()) {
            case 0:
                glideRequest.diskCacheStrategy(DiskCacheStrategy.ALL);
                break;
            case 1:
                glideRequest.diskCacheStrategy(DiskCacheStrategy.NONE);
                break;
            case 2:
                glideRequest.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                break;
            case 3:
                glideRequest.diskCacheStrategy(DiskCacheStrategy.DATA);
                break;
            case 4:
                glideRequest.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
                break;
        }

        // glide用它来改变图形的形状
        if (config.getTransformation() != null) {
            glideRequest.transform(config.getTransformation());
        }

        // 设置占位符
        if (config.getPlaceholder() != 0) {
            glideRequest.placeholder(config.getPlaceholder());
        }

        // 设置错误的图片
        if (config.getErrorPic() != 0) {
            glideRequest.error(config.getErrorPic());
        }

        // 设置请求 url 为空图片
        if (config.getFallback() != 0) {
            glideRequest.fallback(config.getFallback());
        }

        glideRequest.into(config.getImageView());
    }

    @Override
    public void clear(final Context context, ImageConfigImpl config) {
        if (context == null) {
            throw new NullPointerException("Context is required");
        }

        if (config == null) {
            throw new NullPointerException("ImageConfigImpl is required");
        }

        // 取消正在执行的任务并且释放资源
        if (config.getImageViews() != null && config.getImageViews().length > 0) {
            for (ImageView imageView : config.getImageViews()) {
                GlideArms.get(context).getRequestManagerRetriever().get(context).clear(imageView);
            }
        }

        // 清除本地缓存
        if (config.isClearDiskCache()) {
            Observable.just(0)
                    .observeOn(Schedulers.io())
                    .subscribe(integer -> Glide.get(context).clearDiskCache());
        }

        // 清除内存缓存
        if (config.isClearMemory()) {
            Observable.just(0)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(integer -> Glide.get(context).clearMemory());
        }
    }
    
    @Override
    public void applyGlideOptions(Context context, GlideBuilder builder) {
        Timber.w("applyGlideOptions");
    }
}
