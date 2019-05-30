package com.ztiany.loadmore.adapter;

public interface ILoadMore {

    void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener);

    void loadFail();

    void loadCompleted(boolean hasMore);

    boolean isLoadingMore();

    void setLoadMode(@LoadMode int loadMore);

    void setLoadMoreViewFactory(LoadMoreViewFactory factory);

    void setAutoHiddenWhenNoMore(boolean autoHiddenWhenNoMore);
}
