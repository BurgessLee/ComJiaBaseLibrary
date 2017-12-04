package com.comjia.baselibrary.demo.di.module;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.comjia.baselibrary.di.scope.ActivityScope;
import com.comjia.baselibrary.demo.mvp.model.UserModel;
import com.comjia.baselibrary.demo.mvp.model.entity.User;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import com.comjia.baselibrary.demo.mvp.contract.UserContract;
import com.comjia.baselibrary.demo.mvp.ui.adapter.UserAdapter;

/**
 * ================================================
 * 展示 Module 的用法
 *
 * @see <a href="https://github.com/JessYanCoding/MVPArms/wiki#2.4.5">Module wiki 官方文档</a>
 * ================================================
 */
@Module
public class UserModule {

    private UserContract.View view;

    /**
     * 构建 UserModule 时, 将 View 的实现类传进来,这样就可以提供 View 的实现类给 Presenter
     */
    public UserModule(UserContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    UserContract.View provideUserView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    UserContract.Model provideUserModel(UserModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    RxPermissions provideRxPermissions() {
        return new RxPermissions(view.getActivity());
    }

    @ActivityScope
    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new GridLayoutManager(view.getActivity(), 2);
    }

    @ActivityScope
    @Provides
    List<User> provideUserList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    RecyclerView.Adapter provideUserAdapter(List<User> list) {
        return new UserAdapter(list);
    }
}
