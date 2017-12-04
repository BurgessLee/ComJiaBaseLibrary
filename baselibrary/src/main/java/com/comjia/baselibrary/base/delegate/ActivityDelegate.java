package com.comjia.baselibrary.base.delegate;

import android.app.Activity;
import android.os.Bundle;

/**
 * ================================================
 * {@link Activity} 代理类, 用于框架内部在每个 {@link Activity} 的对应生命周期中插入需要的逻辑
 *
 * @see ActivityDelegateImpl
 * @see <a href="https://github.com/JessYanCoding/MVPArms/wiki#3.13">ActivityDelegate wiki 官方文档</a>
 * ================================================
 */
public interface ActivityDelegate {

    String LAYOUT_LINEARLAYOUT = "LinearLayout";
    String LAYOUT_FRAMELAYOUT = "FrameLayout";
    String LAYOUT_RELATIVELAYOUT = "RelativeLayout";
    String ACTIVITY_DELEGATE = "activity_delegate";

    void onCreate(Bundle savedInstanceState);

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onSaveInstanceState(Bundle outState);

    void onRestoreInstanceState(Bundle savedInstanceState);

    void onDestroy();
}
