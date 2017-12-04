package com.comjia.baselibrary.base.delegate;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * ================================================
 * {@link Fragment} 代理类, 用于框架内部在每个 {@link Fragment} 的对应生命周期中插入需要的逻辑
 *
 * @see FragmentDelegateImpl
 * @see <a href="https://github.com/JessYanCoding/MVPArms/wiki#3.13">FragmentDelegate wiki 官方文档</a>
 * ================================================
 */
public interface FragmentDelegate {

    String FRAGMENT_DELEGATE = "fragment_delegate";

    void onAttach(Context context);

    void onCreate(Bundle savedInstanceState);

    void onCreateView(View view, Bundle savedInstanceState);

    void onActivityCreate(Bundle savedInstanceState);

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onSaveInstanceState(Bundle outState);

    void onRestoreInstanceState(Bundle savedInstanceState);

    void onDestroyView();

    void onDestroy();

    void onDetach();

    /**
     * Return true if the fragment is currently added to its activity.
     */
    boolean isAdded();
}
