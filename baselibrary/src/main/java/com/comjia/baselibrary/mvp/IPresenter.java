package com.comjia.baselibrary.mvp;

import android.app.Activity;

/**
 * ================================================
 * 框架要求框架中的每个 Presenter 都需要实现此类, 以满足规范
 *
 * @see BasePresenter
 * @see <a href="https://github.com/JessYanCoding/MVPArms/wiki#2.4.4">Presenter wiki 官方文档</a>
 * ================================================
 */
public interface IPresenter {

    /**
     * 做一些初始化操作
     */
    void onStart();

    /**
     * 在框架中 {@link Activity#onDestroy()} 时会默认调用 {@link IPresenter#onDestroy()}
     */
    void onDestroy();
}
