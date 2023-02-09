package com.ztiany.loadmore.adapter;

public class LoadMoreConfig {

    static LoadMoreViewFactory sLoadMoreViewFactory;
    static long sMinLoadMoreInterval = 1000;
    static boolean sStopAutoLoadWhenFailed = false;

    public static void setMinLoadMoreInterval(long minLoadMoreInterval) {
        sMinLoadMoreInterval = minLoadMoreInterval;
    }

    public static void setDefaultLoadMoreViewFactory(LoadMoreViewFactory loadMoreViewFactory) {
        sLoadMoreViewFactory = loadMoreViewFactory;
    }

    public static void setStopAutoLoadWhenFailed(boolean stopAutoLoadWhenFailed) {
        sStopAutoLoadWhenFailed = stopAutoLoadWhenFailed;
    }

}
