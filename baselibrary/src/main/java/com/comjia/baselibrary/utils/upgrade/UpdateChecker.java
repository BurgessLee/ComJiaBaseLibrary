package com.comjia.baselibrary.utils.upgrade;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateChecker implements IUpdateChecker {

    final byte[] mPostData;

    public UpdateChecker() {
        mPostData = null;
    }

    public UpdateChecker(byte[] data) {
        mPostData = data;
    }

    @Override
    public void check(ICheckAgent agent, String url) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestProperty("Accept", "application/json");

            if (mPostData == null) {
                connection.setRequestMethod("GET");
                connection.connect();
            } else {
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setInstanceFollowRedirects(false);
                connection.setUseCaches(false);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("Content-Length", Integer.toString(mPostData.length));
                connection.getOutputStream().write(mPostData);
            }

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                agent.setInfo(UpdateUtil.readString(connection.getInputStream()));
            } else {
                agent.setError(new UpdateError(UpdateError.CHECK_HTTP_STATUS, "" + connection.getResponseCode()));
            }
        } catch (IOException e) {
            e.printStackTrace();
            agent.setError(new UpdateError(UpdateError.CHECK_NETWORK_IO));
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}