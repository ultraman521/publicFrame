package com.news.guagua.net.download;

import java.io.File;

public interface DownLoadListener {
    void onFinish(File file);
    void onProgress(int progress);
    void onFailed(String errMsg);
}
