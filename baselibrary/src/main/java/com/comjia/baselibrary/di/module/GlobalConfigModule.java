package com.comjia.baselibrary.di.module;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.comjia.baselibrary.http.BaseUrl;
import com.comjia.baselibrary.http.GlobalHttpHandler;
import com.comjia.baselibrary.http.RequestInterceptor;
import com.comjia.baselibrary.http.imageloader.BaseImageLoaderStrategy;
import com.comjia.baselibrary.http.imageloader.glide.GlideImageLoaderStrategy;
import com.comjia.baselibrary.integration.cache.Cache;
import com.comjia.baselibrary.integration.cache.CacheType;
import com.comjia.baselibrary.integration.cache.LruCache;
import com.comjia.baselibrary.utils.DataHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.jessyan.rxerrorhandler.handler.listener.ResponseErrorListener;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;

/**
 * ================================================
 * 框架独创的建造者模式 {@link Module}, 可向框架中注入外部配置的自定义参数
 *
 * @see <a href="https://github.com/JessYanCoding/MVPArms/wiki#3.1">GlobalConfigModule Wiki 官方文档</a>
 * ================================================
 */
@Module
public class GlobalConfigModule {

    private HttpUrl mApiUrl;
    private BaseUrl mBaseUrl;
    private BaseImageLoaderStrategy mLoaderStrategy;
    private GlobalHttpHandler mHandler;
    private List<Interceptor> mInterceptors;
    private ResponseErrorListener mErrorListener;
    private File mCacheFile;
    private ClientModule.RetrofitConfiguration mRetrofitConfiguration;
    private ClientModule.OkhttpConfiguration mOkhttpConfiguration;
    private ClientModule.RxCacheConfiguration mRxCacheConfiguration;
    private AppModule.GsonConfiguration mGsonConfiguration;
    private RequestInterceptor.Level mPrintHttpLogLevel;
    private Cache.Factory mCacheFactory;

    private GlobalConfigModule(Builder builder) {
        mApiUrl = builder.apiUrl;
        mBaseUrl = builder.baseUrl;
        mLoaderStrategy = builder.loaderStrategy;
        mHandler = builder.handler;
        mInterceptors = builder.interceptors;
        mErrorListener = builder.responseErrorListener;
        mCacheFile = builder.cacheFile;
        mRetrofitConfiguration = builder.retrofitConfiguration;
        mOkhttpConfiguration = builder.okhttpConfiguration;
        mRxCacheConfiguration = builder.rxCacheConfiguration;
        mGsonConfiguration = builder.gsonConfiguration;
        mPrintHttpLogLevel = builder.printHttpLogLevel;
        mCacheFactory = builder.cacheFactory;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Singleton
    @Provides
    @Nullable
    List<Interceptor> provideInterceptors() {
        return mInterceptors;
    }

    /**
     * 提供 BaseUrl, 默认使用 <"https://api.github.com/">
     */
    @Singleton
    @Provides
    HttpUrl provideBaseUrl() {
        if (mBaseUrl != null) {
            HttpUrl httpUrl = mBaseUrl.url();
            if (httpUrl != null) {
                return httpUrl;
            }
        }
        return mApiUrl == null ? HttpUrl.parse("https://api.github.com/") : mApiUrl;
    }

    /**
     * 提供图片加载框架, 默认使用 {@link Glide}
     */
    @Singleton
    @Provides
    BaseImageLoaderStrategy provideImageLoaderStrategy() {
        return mLoaderStrategy == null ? new GlideImageLoaderStrategy() : mLoaderStrategy;
    }

    /**
     * 提供处理 Http 请求和响应结果的处理类
     */
    @Singleton
    @Provides
    @Nullable
    GlobalHttpHandler provideGlobalHttpHandler() {
        return mHandler;
    }

    /**
     * 提供缓存文件
     */
    @Singleton
    @Provides
    File provideCacheFile(Application application) {
        return mCacheFile == null ? DataHelper.getCacheFile(application) : mCacheFile;
    }

    /**
     * 提供处理 RxJava 错误的管理器的回调
     */
    @Singleton
    @Provides
    ResponseErrorListener provideResponseErrorListener() {
        return mErrorListener == null ? ResponseErrorListener.EMPTY : mErrorListener;
    }

    @Singleton
    @Provides
    @Nullable
    ClientModule.RetrofitConfiguration provideRetrofitConfiguration() {
        return mRetrofitConfiguration;
    }

    @Singleton
    @Provides
    @Nullable
    ClientModule.OkhttpConfiguration provideOkhttpConfiguration() {
        return mOkhttpConfiguration;
    }

    @Singleton
    @Provides
    @Nullable
    ClientModule.RxCacheConfiguration provideRxCacheConfiguration() {
        return mRxCacheConfiguration;
    }

    @Singleton
    @Provides
    @Nullable
    AppModule.GsonConfiguration provideGsonConfiguration() {
        return mGsonConfiguration;
    }

    @Singleton
    @Provides
    @Nullable
    RequestInterceptor.Level providePrintHttpLogLevel() {
        return mPrintHttpLogLevel;
    }

    @Singleton
    @Provides
    Cache.Factory provideCacheFactory(Application application) {
        return mCacheFactory == null ? new Cache.Factory() {
            @NonNull
            @Override
            public Cache build(CacheType type) {
                // 若想自定义 LruCache 的 size, 或者不想使用 LruCache , 想使用自己自定义的策略
                // 并使用 GlobalConfigModule.Builder#cacheFactory() 扩展
                return new LruCache(type.calculateCacheSize(application));
            }
        } : mCacheFactory;
    }

    public static final class Builder {
        private HttpUrl apiUrl;
        private BaseUrl baseUrl;
        private BaseImageLoaderStrategy loaderStrategy;
        private GlobalHttpHandler handler;
        private List<Interceptor> interceptors;
        private ResponseErrorListener responseErrorListener;
        private File cacheFile;
        private ClientModule.RetrofitConfiguration retrofitConfiguration;
        private ClientModule.OkhttpConfiguration okhttpConfiguration;
        private ClientModule.RxCacheConfiguration rxCacheConfiguration;
        private AppModule.GsonConfiguration gsonConfiguration;
        private RequestInterceptor.Level printHttpLogLevel;
        private Cache.Factory cacheFactory;

        private Builder() {
        }

        /**
         * 设置基础url
         */
        public Builder baseUrl(String baseUrl) {
            if (TextUtils.isEmpty(baseUrl)) {
                throw new NullPointerException("BaseUrl can not be empty");
            }
            this.apiUrl = HttpUrl.parse(baseUrl);
            return this;
        }

        /**
         * 设置基础url
         */
        public Builder baseUrl(BaseUrl baseUrl) {
            if (baseUrl == null) {
                throw new NullPointerException("BaseUrl can not be null");
            }
            this.baseUrl = baseUrl;
            return this;
        }

        /**
         * 用来请求网络图片
         */
        public Builder imageLoaderStrategy(BaseImageLoaderStrategy loaderStrategy) {
            this.loaderStrategy = loaderStrategy;
            return this;
        }

        /**
         * 用来处理http响应结果
         */
        public Builder globalHttpHandler(GlobalHttpHandler handler) {
            this.handler = handler;
            return this;
        }

        /**
         * 动态添加任意个interceptor
         */
        public Builder addInterceptor(Interceptor interceptor) {
            if (interceptors == null) {
                interceptors = new ArrayList<>();
            }
            this.interceptors.add(interceptor);
            return this;
        }

        /**
         * 处理所有RxJava的onError逻辑
         */
        public Builder responseErrorListener(ResponseErrorListener listener) {
            this.responseErrorListener = listener;
            return this;
        }

        public Builder cacheFile(File cacheFile) {
            this.cacheFile = cacheFile;
            return this;
        }

        public Builder retrofitConfiguration(ClientModule.RetrofitConfiguration retrofitConfiguration) {
            this.retrofitConfiguration = retrofitConfiguration;
            return this;
        }

        public Builder okhttpConfiguration(ClientModule.OkhttpConfiguration okhttpConfiguration) {
            this.okhttpConfiguration = okhttpConfiguration;
            return this;
        }

        public Builder rxCacheConfiguration(ClientModule.RxCacheConfiguration rxCacheConfiguration) {
            this.rxCacheConfiguration = rxCacheConfiguration;
            return this;
        }

        public Builder gsonConfiguration(AppModule.GsonConfiguration gsonConfiguration) {
            this.gsonConfiguration = gsonConfiguration;
            return this;
        }

        /**
         * 是否让框架打印 Http 的请求和响应信息
         */
        public Builder printHttpLogLevel(RequestInterceptor.Level printHttpLogLevel) {
            if (printHttpLogLevel == null) {
                throw new NullPointerException("printHttpLogLevel == null. Use RequestInterceptor.Level.NONE instead.");
            }
            this.printHttpLogLevel = printHttpLogLevel;
            return this;
        }

        public Builder cacheFactory(Cache.Factory cacheFactory) {
            this.cacheFactory = cacheFactory;
            return this;
        }

        public GlobalConfigModule build() {
            return new GlobalConfigModule(this);
        }
    }
}
