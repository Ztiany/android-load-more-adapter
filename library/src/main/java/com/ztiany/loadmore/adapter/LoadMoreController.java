package com.ztiany.loadmore.adapter;

public interface LoadMoreController {

    /**
     * set the listener to be called when load more is required.
     */
    void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener);

    /**
     * custom load-more item view.
     */
    void setLoadMoreViewFactory(LoadMoreViewFactory factory);

    /**
     * notify the controller that the load more operation has failed.
     */
    void loadFailed();

    /**
     * notify the controller that the load more operation has completed.
     *
     * @param hasMore true if there may be more items, false otherwise.
     */
    void loadCompleted(boolean hasMore);

    /**
     * notify the controller that the load more operation has completed.
     *
     * @param hasMore  true if there may be more items, false otherwise.
     * @param appended true if there are new items appended, false otherwise.
     */
    void loadCompleted(boolean hasMore, boolean appended);

    /**
     * @return true if a load more operation is in progress, false otherwise.
     */
    boolean isLoadingMore();

    /**
     * set the load mode. the default is {@link LoadMode#AUTO_LOAD}.
     */
    void setLoadMode(@LoadMode int loadMode);

    /**
     * only works when use scrolling to trigger load-more. the default is 1000ms.
     */
    void setMinLoadMoreInterval(long minLoadMoreInterval);

    /**
     * only works when use auto load-more. the default is false.
     */
    void stopAutoLoadWhenFailed(boolean stopAutoLoadWhenFailed);

    /**
     * only works when use auto load-more. the default direction is {@link Direction#UP}.
     */
    void setLoadMoreDirection(@Direction int direction);

    /**
     * for preview load. only works when use scrolling to trigger load-more. the default is 0.
     */
    void setLoadingTriggerThreshold(int threshold);

    /**
     * @param hideWhenNoMore ture: set load-more view invisible when no more. the default is false.
     */

    void setAutoHideWhenNoMore(boolean hideWhenNoMore);

    /**
     * modify the load-more view's state to loading.
     */
    void setLoadingMore();

}