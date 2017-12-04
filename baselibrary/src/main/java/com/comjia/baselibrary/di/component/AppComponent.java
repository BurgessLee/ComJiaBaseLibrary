package com.comjia.baselibrary.di.component;

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.comjia.baselibrary.base.delegate.AppDelegate;
import com.comjia.baselibrary.di.module.AppModule;
import com.comjia.baselibrary.di.module.ClientModule;
import com.comjia.baselibrary.di.module.GlobalConfigModule;
import com.comjia.baselibrary.http.imageloader.ImageLoader;
import com.comjia.baselibrary.integration.AppManager;
import com.comjia.baselibrary.integration.IRepositoryManager;
import com.comjia.baselibrary.integration.cache.Cache;
import com.comjia.baselibrary.utils.ArmsUtils;

import java.io.File;

import javax.inject.Singleton;

import dagger.Component;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import okhttp3.OkHttpClient;

/**
 * ================================================
 * 可通过 {@link ArmsUtils#obtainAppComponentFromContext(Context)} 拿到此接口的实现类
 * 拥有此接口的实现类即可调用对应的方法拿到 Dagger 提供的对应实例
 *
 * @see <a href="https://github.com/JessYanCoding/MVPArms/wiki#2.2">AppComponent wiki 官方文档</a>
 * ================================================
 */
@Singleton
@Component(modules = {AppModule.class, ClientModule.class, GlobalConfigModule.class})
public interface AppComponent {

    Application application();

    /**
     * 用于管理所有Activity
     */
    AppManager appManager();

    /**
     * 用于管理网络请求层, 以及数据缓存层
     */
    IRepositoryManager repositoryManager();

    /**
     * RxJava错误处理管理类
     */
    RxErrorHandler rxErrorHandler();

    /**
     * 图片管理器, 用于加载图片的管理类, 默认使用Glide, 使用策略模式, 可在运行时替换框架
     */
    ImageLoader imageLoader();

    OkHttpClient okHttpClient();

    /**
     * Gson
     */
    Gson gson();

    /**
     * 缓存文件根目录 (RxCache和Glide的缓存都已经作为子文件夹放在这个根目录下), 应该将所有缓存都放到这个根目录下,
     * 便于管理和清理, 可在GlobalConfigModule里配置
     */
    File cacheFile();

    /**
     * 用来存取一些整个App公用的数据, 切勿大量存放大容量数据
     */
    Cache<String, Object> extras();

    /**
     * 用于创建框架所需缓存对象的工厂
     */
    Cache.Factory cacheFactory();

    void inject(AppDelegate delegate);
}
