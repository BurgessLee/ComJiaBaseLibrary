package com.comjia.baselibrary.utils.upgrade;

import java.io.File;

public interface IUpdateDownloader {

    void download(IDownloadAgent agent, String url, File temp);
}