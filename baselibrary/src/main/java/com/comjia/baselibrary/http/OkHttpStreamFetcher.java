package com.comjia.baselibrary.http;

import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.HttpException;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.util.ContentLengthInputStream;
import com.bumptech.glide.util.Synthetic;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Fetches an {@link InputStream} using the okhttp library.
 */
public class OkHttpStreamFetcher implements DataFetcher<InputStream>, Callback {

    private static final String TAG = "OkHttpFetcher";

    private final Call.Factory mClient;
    private final GlideUrl mUrl;
    @Synthetic
    InputStream mStream;
    @Synthetic
    ResponseBody mResponseBody;
    private volatile Call mCall;
    private DataCallback<? super InputStream> mCallback;

    public OkHttpStreamFetcher(Call.Factory client, GlideUrl url) {
        mClient = client;
        mUrl = url;
    }

    @Override
    public void loadData(Priority priority, final DataCallback<? super InputStream> callback) {
        Request.Builder requestBuilder = new Request.Builder().url(mUrl.toStringUrl());
        for (Map.Entry<String, String> headerEntry : mUrl.getHeaders().entrySet()) {
            String key = headerEntry.getKey();
            requestBuilder.addHeader(key, headerEntry.getValue());
        }

        Request request = requestBuilder.build();
        mCallback = callback;

        mCall = mClient.newCall(request);
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            mCall.enqueue(this);
        } else {
            try {
                // Calling execute instead of enqueue is a workaround for #2355, where okhttp throws a
                // ClassCastException on O.
                onResponse(mCall, mCall.execute());
            } catch (IOException e) {
                onFailure(mCall, e);
            } catch (ClassCastException e) {
                // It's not clear that this catch is necessary, the error may only occur even on O if
                // enqueue is used.
                onFailure(mCall, new IOException("Workaround for framework bug on O", e));
            }
        }
    }

    @Override
    public void onFailure(Call call, IOException e) {
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "OkHttp failed to obtain result", e);
        }

        mCallback.onLoadFailed(e);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        mResponseBody = response.body();
        if (response.isSuccessful()) {
            long contentLength = mResponseBody.contentLength();
            mStream = ContentLengthInputStream.obtain(mResponseBody.byteStream(), contentLength);
            mCallback.onDataReady(mStream);
        } else {
            mCallback.onLoadFailed(new HttpException(response.message(), response.code()));
        }
    }

    @Override
    public void cleanup() {
        try {
            if (mStream != null) {
                mStream.close();
            }
        } catch (IOException e) {
            // Ignored
        }

        if (mResponseBody != null) {
            mResponseBody.close();
        }
        mCallback = null;
    }

    @Override
    public void cancel() {
        Call local = mCall;
        if (local != null) {
            local.cancel();
        }
    }

    @NonNull
    @Override
    public Class<InputStream> getDataClass() {
        return InputStream.class;
    }

    @NonNull
    @Override
    public DataSource getDataSource() {
        return DataSource.REMOTE;
    }
}

