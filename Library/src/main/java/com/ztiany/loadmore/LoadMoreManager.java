package com.ztiany.loadmore;

/**
 * author Ztiany                                                                        <br/>
 * email 1169654504@qq.com & ztiany3@gmail.com           <br/>
 * date 2016-04-11 11:30                                                         <br/>
 * description                                                                             <br/>
 * version
 */
public interface LoadMoreManager {

    /**
     * 设置加载更多回调
     *
     * @param onLoadMoreListener 回调
     */
    void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener);


    /**
     * 加载失败
     *
     */
    void loadFail();

    /**
     * 加载完成
     *
     * @param hasMore 是否还可以加载更多
     */
    void loadCompleted(boolean hasMore);


    /**
     * 是否正在加载
     *
     * @return
     */
    boolean isLoadingMore();



    /**
     * 设置自动加载
     *
     * @param loadMore 刷新模式
     */
    void setLoadMode(@LoadMode int loadMore);


    void setLoadMoreViewFactory(LoadMoreViewFactory factory);

}
