package com.ztiany.loadmore.adapter;

public class LoadMoreConfig {

    static LoadMoreViewFactory sLoadMoreViewFactory;
    static long sMixLoadMoreInterval;

    public static void setMixLoadMoreInterval(long mixLoadMoreInterval) {
        sMixLoadMoreInterval = mixLoadMoreInterval;
    }

    public static void setDefaultLoadMoreViewFactory(LoadMoreViewFactory loadMoreViewFactory) {
        sLoadMoreViewFactory = loadMoreViewFactory;
    }

}
