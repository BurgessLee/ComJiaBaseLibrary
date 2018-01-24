package com.comjia.baselibrary.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.comjia.baselibrary.base.delegate.IFragment;
import com.comjia.baselibrary.integration.cache.Cache;
import com.comjia.baselibrary.integration.cache.CacheType;
import com.comjia.baselibrary.integration.lifecycle.FragmentLifecycleable;
import com.comjia.baselibrary.mvp.IPresenter;
import com.comjia.baselibrary.utils.BaseAppUtils;
import com.sensorsdata.analytics.android.sdk.SensorsDataTrackFragmentAppViewScreen;
import com.trello.rxlifecycle2.android.FragmentEvent;

import javax.inject.Inject;

import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * ================================================
 * 因为 Java 只能单继承, 所以如果要用到需要继承特定 @{@link Fragment} 的三方库, 那你就需要自己自定义 @{@link Fragment}
 * 继承于这个特定的 @{@link Fragment}, 然后再按照 {@link BaseFragment} 的格式, 将代码复制过去, 记住一定要实现{@link IFragment}
 * ================================================
 */
@SensorsDataTrackFragmentAppViewScreen
public abstract class BaseFragment<P extends IPresenter> extends Fragment implements IFragment, FragmentLifecycleable {

    protected final String TAG = getClass().getSimpleName();

    private final BehaviorSubject<FragmentEvent> mLifecycleSubject = BehaviorSubject.create();
    @Inject
    protected P mPresenter;
    private Cache<String, Object> mCache;

    @NonNull
    @Override
    public synchronized Cache<String, Object> provideCache() {
        if (mCache == null) {
            mCache = BaseAppUtils.obtainAppComponentFromContext(getActivity()).cacheFactory().build(CacheType.FRAGMENT_CACHE);
        }
        return mCache;
    }

    @NonNull
    @Override
    public final Subject<FragmentEvent> provideLifecycleSubject() {
        return mLifecycleSubject;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // 释放资源
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
        mPresenter = null;
    }

    /**
     * 是否使用eventBus, 默认为使用(true)
     */
    @Override
    public boolean useEventBus() {
        return true;
    }

    /**
     * 显示软键盘
     */
    public void showSoftInput(View target) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInputFromInputMethod(target.getWindowToken(), 0);
        }
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftInput(View target) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(target.getWindowToken(), 0);
        }
    }

    /**
     * 切换软键盘的显示与隐藏
     */
    public void switchSoftInput(View target) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInputFromWindow(target.getWindowToken(), 0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
