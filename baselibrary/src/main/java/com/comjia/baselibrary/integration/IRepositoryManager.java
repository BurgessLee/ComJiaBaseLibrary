package com.comjia.baselibrary.integration;

import android.content.Context;

import com.comjia.baselibrary.mvp.IModel;

/**
 * ================================================
 * 用来管理网络请求层,以及数据缓存层,以后可能添加数据库请求层
 * 提供给 {@link IModel} 必要的 Api 做数据处理
 *
 * @see <a href="https://github.com/JessYanCoding/MVPArms/wiki#2.3">RepositoryManager wiki 官方文档</a>
 * ================================================
 */
public interface IRepositoryManager {

    Context getContext();

    /**
     * 根据传入的 Class 获取对应的 Retrofit service
     */
    <T> T obtainRetrofitService(Class<T> service);

    /**
     * 根据传入的 Class 获取对应的 RxCache service
     */
    <T> T obtainCacheService(Class<T> cache);

    /**
     * 清理所有缓存
     */
    void clearAllCache();
}
