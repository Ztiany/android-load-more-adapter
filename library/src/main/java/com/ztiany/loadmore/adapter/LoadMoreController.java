package com.ztiany.loadmore.adapter;

public interface LoadMoreController {

    void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener);

    void loadFail();

    void loadCompleted(boolean hasMore);

    boolean isLoadingMore();

    void setLoadMode(@LoadMode int loadMode);

    /**
     * custom load-more item view.
     */
    void setLoadMoreViewFactory(LoadMoreViewFactory factory);

    /**
     * only works when use scrolling to trigger load-more.
     */
    void setMinLoadMoreInterval(long minLoadMoreInterval);

    /**
     * only works when use auto load-more.
     */
    void stopAutoLoadWhenFailed(boolean stopAutoLoadWhenFailed);

    /**
     * only works when use auto load-more. the default direction is {@link Direction#UP}.
     */
    void setLoadMoreDirection(@Direction int direction);

    /**
     * for preview load. only works when use scrolling to trigger load-more.
     */
    void setLoadingTriggerThreshold(int threshold);

    /**
     * @param autoHiddenWhenNoMore ture: set load-more view invisible when no more.
     */

    void setAutoHiddenWhenNoMore(boolean autoHiddenWhenNoMore);

    /**
     * set the state to loading.
     */
    void setLoadingMore();

}
