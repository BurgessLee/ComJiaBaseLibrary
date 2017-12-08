package com.comjia.baselibrary.utils.upgrade;

/**
 * 定义下载状态监听规范
 */
public interface OnDownloadListener {

    /**
     * 开始下载
     */
    void onStart();

    /**
     * 下载进度
     *
     * @param progress 进度值
     */
    void onProgress(int progress);

    /**
     * 下载结束
     */
    void onFinish();
}