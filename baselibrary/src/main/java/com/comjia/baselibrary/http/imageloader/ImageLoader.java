package com.comjia.baselibrary.http.imageloader;

import android.content.Context;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * ================================================
 * {@link ImageLoader} 使用策略模式和建造者模式, 可以动态切换图片请求框架(比如说切换成 Picasso )
 * 当需要切换图片请求框架或图片请求框架升级后变更了 Api 时
 * 这里可以将影响范围降到最低, 所以封装 {@link ImageLoader} 是为了屏蔽这个风险
 *
 * @see <a href="https://github.com/JessYanCoding/MVPArms/wiki#3.4">ImageLoader wiki 文档</a>
 * ================================================
 */
@Singleton
public final class ImageLoader {

    private BaseImageLoaderStrategy mStrategy;

    @Inject
    public ImageLoader(BaseImageLoaderStrategy strategy) {
        setLoadImgStrategy(strategy);
    }

    /**
     * 加载图片
     */
    public <T extends ImageConfig> void loadImage(Context context, T config) {
        mStrategy.loadImage(context, config);
    }

    /**
     * 停止加载或清理缓存
     */
    public <T extends ImageConfig> void clear(Context context, T config) {
        mStrategy.clear(context, config);
    }

    public BaseImageLoaderStrategy getLoadImgStrategy() {
        return mStrategy;
    }

    /**
     * 可在运行时随意切换 {@link BaseImageLoaderStrategy}
     */
    public void setLoadImgStrategy(BaseImageLoaderStrategy strategy) {
        mStrategy = strategy;
    }
}
