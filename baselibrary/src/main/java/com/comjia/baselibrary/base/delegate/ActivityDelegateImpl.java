package com.comjia.baselibrary.base.delegate;

import android.app.Activity;
import android.os.Bundle;

import com.comjia.baselibrary.utils.BaseAppUtils;

import org.simple.eventbus.EventBus;

/**
 * ================================================
 * {@link ActivityDelegate} 默认实现类
 * ================================================
 */
public class ActivityDelegateImpl implements ActivityDelegate {

    private Activity mActivity;
    private IActivity iActivity;

    public ActivityDelegateImpl(Activity activity) {
        mActivity = activity;
        iActivity = (IActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // 使用eventbus请确保useEventBus()方法返回true
        if (iActivity.useEventBus()) {
            // 注册到事件主线
            EventBus.getDefault().register(mActivity);
        }
        // 依赖注入
        iActivity.setupActivityComponent(BaseAppUtils.obtainAppComponentFromContext(mActivity));
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onStop() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
    }

    @Override
    public void onDestroy() {
        // 使用eventbus请确保useEventBus()方法返回true
        if (iActivity != null && iActivity.useEventBus()) {
            EventBus.getDefault().unregister(mActivity);
        }

        iActivity = null;
        mActivity = null;
    }
}
