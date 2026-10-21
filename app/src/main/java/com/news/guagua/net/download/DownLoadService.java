package com.news.guagua.net.download;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface DownLoadService {
    /**
     * 文件下载
     * @param url
     * @return
     */
    @Streaming
    @GET
    Call<ResponseBody> download(@Url String url);
}
