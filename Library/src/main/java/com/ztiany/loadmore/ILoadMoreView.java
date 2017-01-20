package com.ztiany.loadmore;


public interface ILoadMoreView {

    void onLoading();

    void onFail();

    void onCompleted(boolean hasMore);

    void onClickLoad();
}
