package com.news.guagua.utils.constant;

public class RefreshStatus {
    public final static int NORMAL=-1;

    /**
     * 完成刷新
     */
    public final static int STOP_REFRESH = 0;
    /**
     * 完成加载更多
     */
    public final static int STOP_LOADMORE = 1;
    /**
     * 完成刷新和加载更多
     */
    public final static int STOP_REFRESH_AND_LOADMORE = 2;
    /**
     * 完成加载并标记没有更多数据
     */
    public final static int STOP_LOADMORE_WithNoMoreData = 3;
    public final static int REFRESH_ING=4;


    /**
     * 下拉刷新
     */
    public final static int REFRESH = 100;


    /**
     * 下拉刷新
     */
    public final static int LOAD_MORE = 99;

}
