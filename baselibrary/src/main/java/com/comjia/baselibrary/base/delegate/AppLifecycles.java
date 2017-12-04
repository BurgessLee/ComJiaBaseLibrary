package com.comjia.baselibrary.base.delegate;

import android.app.Application;
import android.content.Context;

/**
 * ================================================
 * 用于代理 {@link Application} 的生命周期
 *
 * @see AppDelegate
 * ================================================
 */
public interface AppLifecycles {

    void attachBaseContext(Context base);

    void onCreate(Application application);

    void onTerminate(Application application);
}
