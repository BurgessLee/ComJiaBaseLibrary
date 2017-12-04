package com.comjia.baselibrary.integration.store.lifecyclemodel;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.comjia.baselibrary.integration.cache.Cache;
import com.comjia.baselibrary.integration.cache.LruCache;

/**
 * 这个类的作用为存储 {@link LifecycleModel}
 * <p>
 * 此类的实例可以在配置更改或者屏幕旋转导致的 {@link Activity} 重建的情况下被完好无损的保留下来, 所以重建后的新 {@link Activity}
 * 持有的实例和重建前持有实例为同一对象
 * <p>
 * 如果当 {@link Activity} 被真正的 {@link Activity#finish()} 不再重建, {@link #clear()} 会被调用
 * 并且之前存储的所有 {@link LifecycleModel} 的  {@link LifecycleModel#onCleared()} 方法也会被调用
 * <p>
 * 通过 {@link LifecycleModelStores} 可向 {@link Activity} 和 {@link Fragment} 提供 {@code LifecycleModelStore}
 */
public class LifecycleModelStore {

    private final Cache<String, LifecycleModel> mCache = new LruCache<>(80);

    /**
     * 将 {@code lifecycleModel} 以 {@code key} 作为键进行存储
     *
     * @param key
     * @param lifecycleModel 如果这个 {code key} 之前还存储有其他 {@link LifecycleModel} 对象则返回, 否则返回 {@code null}
     */
    public final void put(String key, LifecycleModel lifecycleModel) {
        LifecycleModel oldLifecycleModel = mCache.get(key);
        if (oldLifecycleModel != null) {
            oldLifecycleModel.onCleared();
        }
        mCache.put(key, lifecycleModel);
    }

    /**
     * 根据给定的 {@code key} 获取存储的 {@link LifecycleModel} 实现类
     */
    public final <T extends LifecycleModel> T get(String key) {
        return (T) mCache.get(key);
    }

    /**
     * 根据给定的 {@code key} 移除存储的 {@link LifecycleModel} 实现类
     */
    public final <T extends LifecycleModel> T remove(String key) {
        return (T) mCache.remove(key);
    }

    /**
     * 清理存储的 {@link LifecycleModel}, 并且通知 {@link LifecycleModel#onCleared()}
     */
    public final void clear() {
        for (String key : mCache.keySet()) {
            mCache.get(key).onCleared();
        }
        mCache.clear();
    }
}
