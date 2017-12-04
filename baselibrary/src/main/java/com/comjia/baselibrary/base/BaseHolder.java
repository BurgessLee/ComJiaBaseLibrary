package com.comjia.baselibrary.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.comjia.baselibrary.utils.ThirdViewUtil;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * ================================================
 * 基类 {@link RecyclerView.ViewHolder}
 * ================================================
 */
public abstract class BaseHolder<T> extends RecyclerView.ViewHolder implements View.OnClickListener {

    protected OnViewClickListener mOnViewClickListener = null;

    public BaseHolder(View itemView) {
        super(itemView);

        // 点击事件
        itemView.setOnClickListener(this);
        if (ThirdViewUtil.USE_AUTO_LAYOUT == 1) {
            // 适配
            AutoUtils.autoSize(itemView);
        }

        // 绑定
        ThirdViewUtil.bindTarget(this, itemView);
    }

    /**
     * 设置数据
     */
    public abstract void setData(T data, int position);

    /**
     * 释放资源
     */
    protected void onRelease() {
    }

    @Override
    public void onClick(View view) {
        if (mOnViewClickListener != null) {
            mOnViewClickListener.onViewClick(view, getPosition());
        }
    }

    public void setOnItemClickListener(OnViewClickListener listener) {
        mOnViewClickListener = listener;
    }

    public interface OnViewClickListener {
        void onViewClick(View view, int position);
    }
}
