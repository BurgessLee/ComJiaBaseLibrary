package com.comjia.baselibrary.base.delegate;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.comjia.baselibrary.utils.AppUtils;

import org.simple.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

/**
 * ================================================
 * {@link FragmentDelegate} 默认实现类
 * ================================================
 */
public class FragmentDelegateImpl implements FragmentDelegate {

    private FragmentManager mFragmentManager;
    private Fragment mFragment;
    private IFragment iFragment;
    private Unbinder mUnbinder;


    public FragmentDelegateImpl(FragmentManager fragmentManager, Fragment fragment) {
        mFragmentManager = fragmentManager;
        mFragment = fragment;
        iFragment = (IFragment) fragment;
    }

    @Override
    public void onAttach(Context context) {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // 使用eventbus请确保useEventBus()方法返回true
        if (iFragment.useEventBus()) {
            // 注册到事件主线
            EventBus.getDefault().register(mFragment);
        }
        // 依赖注入
        iFragment.setupFragmentComponent(AppUtils.obtainAppComponentFromContext(mFragment.getActivity()));
    }

    @Override
    public void onCreateView(View view, Bundle savedInstanceState) {
        // 绑定到butterknife
        if (view != null) {
            mUnbinder = ButterKnife.bind(mFragment, view);
        }
    }

    @Override
    public void onActivityCreate(Bundle savedInstanceState) {
        iFragment.initData(savedInstanceState);
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
    public void onDestroyView() {
        if (mUnbinder != null && mUnbinder != mUnbinder.EMPTY) {
            try {
                mUnbinder.unbind();
            } catch (IllegalStateException e) {
                e.printStackTrace();
                // fix Bindings already cleared
                Timber.w("onDestroyView: " + e.getMessage());
            }
        }
    }

    @Override
    public void onDestroy() {
        // 使用eventbus请确保useEventBus()方法返回true
        if (iFragment != null && iFragment.useEventBus()) {
            EventBus.getDefault().unregister(mFragment);
        }

        mUnbinder = null;
        mFragmentManager = null;
        mFragment = null;
        iFragment = null;
    }

    @Override
    public void onDetach() {
    }

    /**
     * Return true if the fragment is currently added to its activity.
     */
    @Override
    public boolean isAdded() {
        return mFragment != null && mFragment.isAdded();
    }
}
