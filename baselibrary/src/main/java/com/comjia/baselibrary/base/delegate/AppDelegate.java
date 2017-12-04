package com.comjia.baselibrary.base.delegate;

import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;

import com.comjia.baselibrary.base.App;
import com.comjia.baselibrary.base.BaseApplication;
import com.comjia.baselibrary.di.component.AppComponent;
import com.comjia.baselibrary.di.component.DaggerAppComponent;
import com.comjia.baselibrary.di.module.AppModule;
import com.comjia.baselibrary.di.module.ClientModule;
import com.comjia.baselibrary.di.module.GlobalConfigModule;
import com.comjia.baselibrary.http.imageloader.glide.ImageConfigImpl;
import com.comjia.baselibrary.integration.ActivityLifecycle;
import com.comjia.baselibrary.integration.ConfigModule;
import com.comjia.baselibrary.integration.ManifestParser;
import com.comjia.baselibrary.integration.lifecycle.ActivityLifecycleForRxLifecycle;
import com.comjia.baselibrary.utils.Preconditions;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * ================================================
 * AppDelegate 可以代理 Application 的生命周期, 在对应的生命周期执行对应的逻辑, 因为 Java 只能单继承
 * 所以当遇到某些三方库需要继承于它的 Application 的时候, 就只有自定义 Application 并继承于三方库的 Application
 * 这时就不用再继承 BaseApplication, 只用在自定义Application中对应的生命周期调用AppDelegate对应的方法
 * (Application一定要实现APP接口), 框架就能照常运行
 *
 * @see BaseApplication
 * @see <a href="https://github.com/JessYanCoding/MVPArms/wiki#3.12">AppDelegate wiki 官方文档</a>
 * ================================================
 */
public class AppDelegate implements App, AppLifecycles {

    @Inject
    protected ActivityLifecycle mActivityLifecycle;
    @Inject
    protected ActivityLifecycleForRxLifecycle mActivityLifecycleForRxLifecycle;
    private Application mApplication;
    private AppComponent mAppComponent;
    private List<ConfigModule> mModules;
    private List<AppLifecycles> mAppLifecycles = new ArrayList<>();
    private List<Application.ActivityLifecycleCallbacks> mActivityLifecycles = new ArrayList<>();
    private ComponentCallbacks2 mComponentCallback;

    public AppDelegate(Context context) {
        mModules = new ManifestParser(context).parse();
        for (ConfigModule module : mModules) {
            module.injectAppLifecycle(context, mAppLifecycles);
            module.injectActivityLifecycle(context, mActivityLifecycles);
        }
    }

    @Override
    public void attachBaseContext(Context base) {
        for (AppLifecycles lifecycle : mAppLifecycles) {
            lifecycle.attachBaseContext(base);
        }
    }

    @Override
    public void onCreate(Application application) {
        mApplication = application;
        mAppComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(mApplication)) // 提供application
                .clientModule(new ClientModule())       // 用于提供okhttp和retrofit的单例
                .globalConfigModule(getGlobalConfigModule(mApplication, mModules))  // 全局配置
                .build();
        mAppComponent.inject(this);

        mAppComponent.extras().put(ConfigModule.class.getName(), mModules);

        mModules = null;

        mApplication.registerActivityLifecycleCallbacks(mActivityLifecycle);
        mApplication.registerActivityLifecycleCallbacks(mActivityLifecycleForRxLifecycle);

        for (Application.ActivityLifecycleCallbacks lifecycle : mActivityLifecycles) {
            mApplication.registerActivityLifecycleCallbacks(lifecycle);
        }

        mComponentCallback = new AppComponentCallbacks(mApplication, mAppComponent);

        mApplication.registerComponentCallbacks(mComponentCallback);

        for (AppLifecycles lifecycle : mAppLifecycles) {
            lifecycle.onCreate(mApplication);
        }
    }

    @Override
    public void onTerminate(Application application) {
        if (mActivityLifecycle != null) {
            mApplication.unregisterActivityLifecycleCallbacks(mActivityLifecycle);
        }

        if (mActivityLifecycleForRxLifecycle != null) {
            mApplication.unregisterActivityLifecycleCallbacks(mActivityLifecycleForRxLifecycle);
        }

        if (mComponentCallback != null) {
            mApplication.unregisterComponentCallbacks(mComponentCallback);
        }

        if (mActivityLifecycles != null && mActivityLifecycles.size() > 0) {
            for (Application.ActivityLifecycleCallbacks lifecycle : mActivityLifecycles) {
                mApplication.unregisterActivityLifecycleCallbacks(lifecycle);
            }
        }

        if (mAppLifecycles != null && mAppLifecycles.size() > 0) {
            for (AppLifecycles lifecycle : mAppLifecycles) {
                lifecycle.onTerminate(mApplication);
            }
        }

        mAppComponent = null;
        mActivityLifecycle = null;
        mActivityLifecycleForRxLifecycle = null;
        mActivityLifecycles = null;
        mComponentCallback = null;
        mAppLifecycles = null;
        mApplication = null;
    }


    /**
     * 将app的全局配置信息封装进module(使用Dagger注入到需要配置信息的地方)
     * 需要在AndroidManifest中声明{@link ConfigModule}的实现类, 和Glide的配置方式相似
     */
    private GlobalConfigModule getGlobalConfigModule(Context context, List<ConfigModule> modules) {

        GlobalConfigModule.Builder builder = GlobalConfigModule.builder();

        for (ConfigModule module : modules) {
            module.applyOptions(context, builder);
        }

        return builder.build();
    }


    /**
     * 将AppComponent返回出去, 供其它地方使用, AppComponent接口中声明的方法返回的实例, 在getAppComponent()拿到对象后都可以直接使用
     */
    @NonNull
    @Override
    public AppComponent getAppComponent() {
        Preconditions.checkNotNull(
                mAppComponent,
                "%s cannot be null, first call %s#onCreate(Application) in %s#onCreate()",
                AppComponent.class.getName(),
                getClass().getName(),
                Application.class.getName()
        );
        return mAppComponent;
    }


    private static class AppComponentCallbacks implements ComponentCallbacks2 {

        private Application mApplication;
        private AppComponent mAppComponent;

        public AppComponentCallbacks(Application application, AppComponent appComponent) {
            mApplication = application;
            mAppComponent = appComponent;
        }

        @Override
        public void onTrimMemory(int level) {
        }

        @Override
        public void onConfigurationChanged(Configuration newConfig) {
        }

        @Override
        public void onLowMemory() {
            // 内存不足时清理图片请求框架的内存缓存
            mAppComponent.imageLoader().clear(
                    mApplication,
                    ImageConfigImpl.builder().isClearMemory(true).build()
            );
        }
    }
}

