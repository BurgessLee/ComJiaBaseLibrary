package com.comjia.baselibrary.integration.store.lifecyclemodel;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * 空实现，没有添加任何逻辑处理
 */
class EmptyActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
    
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }
}
