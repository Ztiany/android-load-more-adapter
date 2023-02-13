package com.ztiany.loadmore.adapter;


public interface LoadMoreView {

    void onLoading();

    void onFailed();

    void onCompleted(boolean hasMore);

    void showClickToLoadMore();

}
