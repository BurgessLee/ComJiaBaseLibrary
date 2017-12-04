package com.comjia.baselibrary.demo.mvp.ui.adapter;

import android.view.View;

import com.comjia.baselibrary.base.BaseHolder;
import com.comjia.baselibrary.base.DefaultAdapter;
import com.comjia.baselibrary.demo.R;
import com.comjia.baselibrary.demo.mvp.model.entity.User;
import com.comjia.baselibrary.demo.mvp.ui.holder.UserItemHolder;

import java.util.List;

/**
 * ================================================
 * 展示 {@link DefaultAdapter} 的用法
 * ================================================
 */
public class UserAdapter extends DefaultAdapter<User> {

    public UserAdapter(List<User> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<User> getHolder(View v, int viewType) {
        return new UserItemHolder(v);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.recycle_list;
    }
}
