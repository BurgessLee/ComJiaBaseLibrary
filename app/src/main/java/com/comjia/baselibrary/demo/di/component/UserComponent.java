package com.comjia.baselibrary.demo.di.component;

import com.comjia.baselibrary.di.component.AppComponent;
import com.comjia.baselibrary.di.scope.ActivityScope;
import com.comjia.baselibrary.demo.mvp.ui.activity.UserActivity;

import dagger.Component;
import com.comjia.baselibrary.demo.di.module.UserModule;

/**
 * ================================================
 * 展示 Component 的用法
 *
 * @see <a href="https://github.com/JessYanCoding/MVPArms/wiki#2.4.6">Component wiki 官方文档</a>
 * ================================================
 */
@ActivityScope
@Component(modules = UserModule.class, dependencies = AppComponent.class)
public interface UserComponent {

    void inject(UserActivity activity);
}
