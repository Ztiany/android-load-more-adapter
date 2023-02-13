package com.ztiany.loadmore.adapter;

public class LoadMoreConfig {

    private static LoadMoreViewFactory sLoadMoreViewFactory;
    private static long sMinLoadMoreInterval = 1000;
    private static boolean sStopAutoLoadWhenFailed = false;

    public static void setMinLoadMoreInterval(long minLoadMoreInterval) {
        sMinLoadMoreInterval = minLoadMoreInterval;
    }

    public static void setDefaultLoadMoreViewFactory(LoadMoreViewFactory loadMoreViewFactory) {
        sLoadMoreViewFactory = loadMoreViewFactory;
    }

    public static void setStopAutoLoadWhenFailed(boolean stopAutoLoadWhenFailed) {
        sStopAutoLoadWhenFailed = stopAutoLoadWhenFailed;
    }

    public static LoadMoreViewFactory getLoadMoreViewFactory() {
        return sLoadMoreViewFactory;
    }

    public static long getMinLoadMoreInterval() {
        return sMinLoadMoreInterval;
    }

    public static boolean isStopAutoLoadWhenFailed() {
        return sStopAutoLoadWhenFailed;
    }

}
