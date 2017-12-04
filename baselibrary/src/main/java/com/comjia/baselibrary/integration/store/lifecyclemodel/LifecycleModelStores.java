package com.comjia.baselibrary.integration.store.lifecyclemodel;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

/**
 * Factory methods for {@link LifecycleModelStore} class.
 */
@SuppressWarnings("WeakerAccess")
public class LifecycleModelStores {

    private LifecycleModelStores() {
    }

    /**
     * Returns the {@link LifecycleModelStore} of the given activity.
     *
     * @param activity an activity whose {@code LifecycleModelStore} is requested
     * @return a {@code LifecycleModelStore}
     */
    @MainThread
    public static LifecycleModelStore of(@NonNull FragmentActivity activity) {
        return HolderFragment.holderFragmentFor(activity).getLifecycleModelStore();
    }

    /**
     * Returns the {@link LifecycleModelStore} of the given fragment.
     *
     * @param fragment a fragment whose {@code LifecycleModelStore} is requested
     * @return a {@code LifecycleModelStore}
     */
    @MainThread
    public static LifecycleModelStore of(@NonNull Fragment fragment) {
        return HolderFragment.holderFragmentFor(fragment).getLifecycleModelStore();
    }
}
