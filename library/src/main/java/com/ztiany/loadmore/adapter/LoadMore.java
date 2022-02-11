package com.ztiany.loadmore.adapter;

public interface LoadMore {

    void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener);

    void loadFail();

    void loadCompleted(boolean hasMore);

    boolean isLoadingMore();

    void setLoadMode(@LoadMode int loadMore);

    /**
     * custom load-more item view.
     */
    void setLoadMoreViewFactory(LoadMoreViewFactory factory);

    /**
     * only works when use scrolling to trigger load-more.
     */
    void setMinLoadMoreInterval(long minLoadMoreInterval);

    /**
     * for preview load. only works when use scrolling to trigger load-more.
     */
    void setLoadingTriggerThreshold(int threshold);

    /**
     * @param autoHiddenWhenNoMore ture: set load-more view invisible when no more.
     * @see LoadMore#setVisibilityWhenNoMore(int)
     */
    void setAutoHiddenWhenNoMore(boolean autoHiddenWhenNoMore);

    /**
     * set load-more view's visibility when no more.
     *
     * @param visibility {@link android.view.View#VISIBLE}, {@link android.view.View#INVISIBLE} or {@link android.view.View#GONE}.
     * @see LoadMore#setAutoHiddenWhenNoMore(boolean)
     */
    void setVisibilityWhenNoMore(int visibility);

}
