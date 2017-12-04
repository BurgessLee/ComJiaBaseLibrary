package com.comjia.baselibrary.http;

import com.comjia.baselibrary.di.module.GlobalConfigModule;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * ================================================
 * 处理 Http 请求和响应结果的处理类
 * 使用 {@link GlobalConfigModule.Builder#globalHttpHandler(GlobalHttpHandler)} 方法配置
 *
 * @see <a href="https://github.com/JessYanCoding/MVPArms/wiki#3.2">GlobalHttpHandler Wiki 官方文档</a>
 * ================================================
 */
public interface GlobalHttpHandler {

    /**
     * 空实现，没有实现任何具体业务逻辑
     */
    GlobalHttpHandler EMPTY = new GlobalHttpHandler() {
        @Override
        public Response onHttpResultResponse(String httpResult, Interceptor.Chain chain, Response response) {
            // 不管是否处理, 都必须将response返回出去
            return response;
        }

        @Override
        public Request onHttpRequestBefore(Interceptor.Chain chain, Request request) {
            // 不管是否处理, 都必须将request返回出去
            return request;
        }
    };

    Response onHttpResultResponse(String httpResult, Interceptor.Chain chain, Response response);

    Request onHttpRequestBefore(Interceptor.Chain chain, Request request);
}
