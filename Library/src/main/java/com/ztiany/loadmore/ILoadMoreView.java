package com.ztiany.loadmore;

/**
 * Author Ztiany      <br/>
 * Email ztiany3@gmail.com      <br/>
 * Date 2015-12-07 21:55      <br/>
 * Description： loadMoreView 视图抽象
 * <p/>
 * 加载中...
 * 点击加载更多 -->(加载完成)
 * 没有更多了 noMore
 * 加载错误  fail
 * 停止加载更多 pause
 */
public interface ILoadMoreView {

    void onLoading();

    void onFail();

    void onCompleted(boolean hasMore);

    void onClickLoad();
}
