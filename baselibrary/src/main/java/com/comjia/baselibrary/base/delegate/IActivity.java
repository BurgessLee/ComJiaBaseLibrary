package com.comjia.baselibrary.base.delegate;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.comjia.baselibrary.base.BaseActivity;
import com.comjia.baselibrary.base.BaseFragment;
import com.comjia.baselibrary.di.component.AppComponent;
import com.comjia.baselibrary.integration.cache.Cache;
import com.comjia.baselibrary.integration.cache.LruCache;
import com.comjia.baselibrary.integration.store.lifecyclemodel.LifecycleModel;
import com.comjia.baselibrary.integration.store.lifecyclemodel.LifecycleModelProviders;

import org.simple.eventbus.EventBus;

/**
 * ================================================
 * 框架要求框架中的每个 {@link Activity} 都需要实现此类, 以满足规范
 *
 * @see BaseActivity
 * ================================================
 */
public interface IActivity {

    /**
     * 提供在 {@link Activity} 生命周期内的缓存容器, 可向此 {@link Activity} 存取一些必要的数据
     * 此缓存容器和 {@link Activity} 的生命周期绑定, 如果 {@link Activity} 在屏幕旋转或者配置更改的情况下
     * 重新创建, 那此缓存容器中的数据也会被清空, 如果你想避免此种情况请使用 {@link LifecycleModel}
     *
     * @see LifecycleModelProviders#of(FragmentActivity)
     * @return like {@link LruCache}
     */
    @NonNull
    Cache<String, Object> provideCache();

    /**
     * 提供 AppComponent(提供所有的单例对象)给实现类, 进行 Component 依赖
     */
    void setupActivityComponent(AppComponent appComponent);

    /**
     * 是否使用 {@link EventBus}
     */
    boolean useEventBus();

    /**
     * 初始化View, 如果initView返回0, 框架则不会调用{@link Activity#setContentView(int)}
     */
    int initView(Bundle savedInstanceState);

    /**
     * 初始化数据
     */
    void initData(Bundle savedInstanceState);

    /**
     * 这个Activity是否会使用Fragment, 框架会根据这个属性判断是否注册{@link FragmentManager.FragmentLifecycleCallbacks}
     * 如果返回false, 那意味着这个Activity不需要绑定Fragment, 那你再在这个Activity中绑定继承于 {@link BaseFragment} 的Fragment将不起任何作用
     */
    boolean useFragment();
}
