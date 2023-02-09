package com.ztiany.loadmore.adapter;

public class LoadMoreConfig {

    static LoadMoreViewFactory sLoadMoreViewFactory;
    static long sMinLoadMoreInterval = 1000;

    public static void setMinLoadMoreInterval(long minLoadMoreInterval) {
        sMinLoadMoreInterval = minLoadMoreInterval;
    }

    public static void setDefaultLoadMoreViewFactory(LoadMoreViewFactory loadMoreViewFactory) {
        sLoadMoreViewFactory = loadMoreViewFactory;
    }

}
