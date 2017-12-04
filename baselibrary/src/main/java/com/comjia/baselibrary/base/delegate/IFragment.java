package com.comjia.baselibrary.base.delegate;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.comjia.baselibrary.base.BaseFragment;
import com.comjia.baselibrary.di.component.AppComponent;
import com.comjia.baselibrary.integration.cache.Cache;
import com.comjia.baselibrary.integration.cache.LruCache;
import com.comjia.baselibrary.integration.store.lifecyclemodel.LifecycleModel;
import com.comjia.baselibrary.integration.store.lifecyclemodel.LifecycleModelProviders;

import org.simple.eventbus.EventBus;

/**
 * ================================================
 * 框架要求框架中的每个 {@link Fragment} 都需要实现此类,以满足规范
 *
 * @see BaseFragment
 * ================================================
 */
public interface IFragment {

    /**
     * 提供在 {@link Fragment} 生命周期内的缓存容器, 可向此 {@link Fragment} 存取一些必要的数据
     * 此缓存容器和 {@link Fragment} 的生命周期绑定, 如果 {@link Fragment} 在屏幕旋转或者配置更改的情况下
     * 重新创建, 那此缓存容器中的数据也会被清空, 如果你想避免此种情况请使用 {@link LifecycleModel}
     *
     * @see LifecycleModelProviders#of(Fragment)
     * @return like {@link LruCache}
     */
    @NonNull
    Cache<String, Object> provideCache();

    /**
     * 提供 AppComponent(提供所有的单例对象)给实现类,进行 Component 依赖
     */
    void setupFragmentComponent(AppComponent appComponent);

    /**
     * 是否使用 {@link EventBus}
     */
    boolean useEventBus();

    /**
     * 初始化 View
     */
    View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    /**
     * 初始化数据
     */
    void initData(Bundle savedInstanceState);

    /**
     * 此方法是让外部调用使fragment做一些操作的, 比如说外部的activity想让fragment对象执行一些方法,
     * 建议在有多个需要让外界调用的方法时, 统一传Message, 通过what字段, 来区分不同的方法, 在setData
     * 方法中就可以switch做不同的操作, 这样就可以用统一的入口方法做不同的事
     * <p>
     * 使用此方法时请注意调用时fragment的生命周期, 如果调用此setData方法时onCreate还没执行
     * setData里却调用了presenter的方法时, 是会报空的, 因为dagger注入是在onCreated方法中执行的, 然后才创建的presenter
     * 如果要做一些初始化操作, 可以不必让外部调setData, 在initData中初始化就可以了
     */
    void setData(Object data);
}
