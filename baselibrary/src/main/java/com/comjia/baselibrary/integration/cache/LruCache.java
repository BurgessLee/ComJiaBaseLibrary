package com.comjia.baselibrary.integration.cache;

import android.app.Application;
import android.support.annotation.Nullable;

import com.comjia.baselibrary.di.module.GlobalConfigModule;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * ================================================
 * LRU 即 Least Recently Used, 最近最少使用, 也就是说, 当缓存满了, 会优先淘汰那些最近最不常访问的数据
 * 此种缓存策略为框架默认提供, 可自行实现其他缓存策略, 如磁盘缓存, 为框架或开发者提供缓存的功能
 *
 * @see GlobalConfigModule#provideCacheFactory(Application)
 * @see Cache
 * ================================================
 */
public class LruCache<K, V> implements Cache<K, V> {

    private final LinkedHashMap<K, V> mCache = new LinkedHashMap<>(100, 0.75f, true);
    private final int mInitialMaxSize;
    private int mMaxSize;
    private int mCurrentSize = 0;

    /**
     * Constructor for LruCache.
     *
     * @param size 这个缓存的最大 size,这个 size 所使用的单位必须和 {@link #getItemSize(Object)} 所使用的单位一致.
     */
    public LruCache(int size) {
        mInitialMaxSize = size;
        mMaxSize = size;
    }

    /**
     * 设置一个系数应用于当时构造函数中所传入的 size, 从而得到一个新的 {@link #mMaxSize}
     * 并会立即调用 {@link #evict} 开始清除满足条件的条目
     *
     * @param multiplier 系数
     */
    public synchronized void setSizeMultiplier(float multiplier) {
        if (multiplier < 0) {
            throw new IllegalArgumentException("Multiplier must be >= 0");
        }

        mMaxSize = Math.round(mInitialMaxSize * multiplier);
        evict();
    }

    /**
     * 返回每个 {@code item} 所占用的 size,默认为1,这个 size 的单位必须和构造函数所传入的 size 一致
     * 子类可以重写这个方法以适应不同的单位,比如说 bytes
     *
     * @param item 每个 {@code item} 所占用的 size
     */
    protected int getItemSize(V item) {
        return 1;
    }

    /**
     * 当缓存中有被驱逐的条目时,会回调此方法,默认空实现,子类可以重写这个方法
     *
     * @param key   被驱逐条目的 {@code key}
     * @param value 被驱逐条目的 {@code value}
     */
    protected void onItemEvicted(K key, V value) {
        // optional override
    }

    /**
     * 返回当前缓存所能允许的最大 size
     */
    @Override
    public synchronized int getMaxSize() {
        return mMaxSize;
    }

    /**
     * 返回当前缓存已占用的总 size
     */
    @Override
    public synchronized int size() {
        return mCurrentSize;
    }

    /**
     * 如果这个 {@code key} 在缓存中有对应的 {@code value} 并且不为 {@code null},则返回 true
     *
     * @param key 用来映射的 {@code key}
     */
    @Override
    public synchronized boolean containsKey(K key) {
        return mCache.containsKey(key);
    }

    /**
     * 返回当前缓存中含有的所有 {@code key}
     */
    @Override
    public Set<K> keySet() {
        return mCache.keySet();
    }

    /**
     * 返回这个 {@code key} 在缓存中对应的 {@code value}, 如果返回 {@code null} 说明这个 {@code key} 没有对应的 {@code value}
     *
     * @param key 用来映射的 {@code key}
     */
    @Override
    @Nullable
    public synchronized V get(K key) {
        return mCache.get(key);
    }

    /**
     * 将 {@code key} 和 {@code value} 以条目的形式加入缓存,如果这个 {@code key} 在缓存中已经有对应的 {@code value}
     * 则此 {@code value} 被新的 {@code value} 替换并返回,如果为 {@code null} 说明是一个新条目
     * <p>
     * 如果 {@link #getItemSize} 返回的 size 大于或等于缓存所能允许的最大 size, 则不能向缓存中添加此条目
     * 此时会回调 {@link #onItemEvicted(Object, Object)} 通知此方法当前被驱逐的条目
     *
     * @param key   通过这个 {@code key} 添加条目
     * @param value 需要添加的 {@code value}
     */
    @Override
    @Nullable
    public synchronized V put(K key, V value) {
        final int itemSize = getItemSize(value);
        if (itemSize >= mMaxSize) {
            onItemEvicted(key, value);
            return null;
        }

        final V result = mCache.put(key, value);
        if (value != null) {
            mCurrentSize += getItemSize(value);
        }

        if (result != null) {
            mCurrentSize -= getItemSize(result);
        }

        evict();

        return result;
    }

    /**
     * 移除缓存中这个 {@code key} 所对应的条目,并返回所移除条目的 {@code value}
     * 如果返回为 {@code null} 则有可能时因为这个 {@code key} 对应的 {@code value} 为 {@code null} 或条目不存在
     *
     * @param key 使用这个 {@code key} 移除对应的条目
     */
    @Override
    @Nullable
    public synchronized V remove(K key) {
        final V value = mCache.remove(key);
        if (value != null) {
            mCurrentSize -= getItemSize(value);
        }
        return value;
    }

    /**
     * 清除缓存中所有的内容
     */
    @Override
    public void clear() {
        trimToSize(0);
    }

    /**
     * 当指定的 size 小于当前缓存已占用的总 size 时,会开始清除缓存中最近最少使用的条目
     */
    protected synchronized void trimToSize(int size) {
        Map.Entry<K, V> last;
        while (mCurrentSize > size) {
            last = mCache.entrySet().iterator().next();
            final V toRemove = last.getValue();
            mCurrentSize -= getItemSize(toRemove);
            final K key = last.getKey();
            mCache.remove(key);
            onItemEvicted(key, toRemove);
        }
    }

    /**
     * 当缓存中已占用的总 size 大于所能允许的最大 size ,会使用  {@link #trimToSize(int)} 开始清除满足条件的条目
     */
    private void evict() {
        trimToSize(mMaxSize);
    }
}

