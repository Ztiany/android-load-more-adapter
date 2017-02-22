package com.ztiany.adapter;

import com.ztiany.loadmore.LoadMode;
import com.ztiany.loadmore.LoadMoreViewFactory;
import com.ztiany.loadmore.OnLoadMoreListener;

/**
 * author Ztiany                                                                        <br/>
 * email 1169654504@qq.com & ztiany3@gmail.com           <br/>
 * date 2016-04-11 11:30                                                         <br/>
 * description                                                                             <br/>
 * version
 */
interface LoadMoreManager {

    void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener);

    void loadFail();

    void loadCompleted(boolean hasMore);

    boolean isLoadingMore();

    void setLoadMode(@LoadMode int loadMore);

    void setLoadMoreViewFactory(LoadMoreViewFactory factory);

}
