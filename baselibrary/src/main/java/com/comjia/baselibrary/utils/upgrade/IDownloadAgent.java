package com.comjia.baselibrary.utils.upgrade;

public interface IDownloadAgent extends OnDownloadListener {

    UpdateInfo getInfo();

    void setError(UpdateError error);
}