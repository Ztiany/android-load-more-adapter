package com.ztiany.loadmore.adapter;


public interface ILoadMoreView {

    void onLoading();

    void onFail();

    void onCompleted(boolean hasMore);

    void onClickLoad();

}
