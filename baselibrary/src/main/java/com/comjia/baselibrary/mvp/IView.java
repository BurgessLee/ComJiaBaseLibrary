package com.comjia.baselibrary.mvp;

import android.app.Activity;
import android.content.Intent;

/**
 * ================================================
 * 框架要求框架中的每个 View 都需要实现此类, 以满足规范
 *
 * @see <a href="https://github.com/JessYanCoding/MVPArms/wiki#2.4.2">View wiki 官方文档</a>
 * ================================================
 */
public interface IView {

    /**
     * 显示加载
     */
    void showLoading();

    /**
     * 隐藏加载
     */
    void hideLoading();

    /**
     * 显示信息
     */
    void showMessage(String message);

    /**
     * 跳转 {@link Activity}
     */
    void launchActivity(Intent intent);

    /**
     * 杀死自己
     */
    void killMyself();
}
