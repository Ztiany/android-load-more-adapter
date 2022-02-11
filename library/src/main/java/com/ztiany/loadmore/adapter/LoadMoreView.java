package com.ztiany.loadmore.adapter;


public interface LoadMoreView {

    void onLoading();

    void onFail();

    void onCompleted(boolean hasMore);

    void onClickLoad();

}
