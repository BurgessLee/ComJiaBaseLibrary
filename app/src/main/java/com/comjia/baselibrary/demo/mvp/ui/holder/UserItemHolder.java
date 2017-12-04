package com.comjia.baselibrary.demo.mvp.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.comjia.baselibrary.base.BaseHolder;
import com.comjia.baselibrary.demo.R;
import com.comjia.baselibrary.demo.mvp.model.entity.User;
import com.comjia.baselibrary.di.component.AppComponent;
import com.comjia.baselibrary.http.imageloader.ImageLoader;
import com.comjia.baselibrary.http.imageloader.glide.ImageConfigImpl;
import com.comjia.baselibrary.utils.AppUtils;

import butterknife.BindView;
import io.reactivex.Observable;

/**
 * ================================================
 * 展示 {@link BaseHolder} 的用法
 * ================================================
 */
public class UserItemHolder extends BaseHolder<User> {

    @BindView(R.id.iv_avatar)
    ImageView mAvatar;
    @BindView(R.id.tv_name)
    TextView mName;
    private AppComponent mAppComponent;
    private ImageLoader mImageLoader;//用于加载图片的管理类,默认使用 Glide,使用策略模式,可替换框架

    public UserItemHolder(View itemView) {
        super(itemView);
        // 可以在任何可以拿到 Context 的地方,拿到 AppComponent,从而得到用 Dagger 管理的单例对象
        mAppComponent = AppUtils.obtainAppComponentFromContext(itemView.getContext());
        mImageLoader = mAppComponent.imageLoader();
    }

    @Override
    public void setData(User data, int position) {
        Observable.just(data.getLogin())
                .subscribe(s -> mName.setText(s));

        //itemView 的 Context 就是 Activity, Glide 会自动处理并和该 Activity 的生命周期绑定
        mImageLoader.loadImage(itemView.getContext(),
                ImageConfigImpl
                        .builder()
                        .url(data.getAvatarUrl())
                        .imageView(mAvatar)
                        .build());
    }

    @Override
    protected void onRelease() {
        mImageLoader.clear(mAppComponent.application(), ImageConfigImpl.builder()
                .imageViews(mAvatar)
                .build());
    }
}
