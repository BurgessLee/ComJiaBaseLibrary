package com.comjia.baselibrary.mvp;

/**
 * ================================================
 * 框架要求框架中的每个 Model 都需要实现此类, 以满足规范
 *
 * @see BaseModel
 * @see <a href="https://github.com/JessYanCoding/MVPArms/wiki#2.4.3">Model wiki 官方文档</a>
 * ================================================
 */
public interface IModel {
    /**
     * 在框架中 {@link BasePresenter#onDestroy()} 时会默认调用 {@link IModel#onDestroy()}
     */
    void onDestroy();
}
