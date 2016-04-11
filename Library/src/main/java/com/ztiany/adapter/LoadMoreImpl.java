package com.ztiany.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.ztiany.loadmore.ILoadMore;
import com.ztiany.loadmore.ILoadMoreView;
import com.ztiany.loadmore.LoadMode;
import com.ztiany.loadmore.LoadMoreView;
import com.ztiany.loadmore.LoadMoreViewFactory;
import com.ztiany.loadmore.Mode;
import com.ztiany.loadmore.OnLoadMoreListener;

/**
 * author Ztiany                                                                        <br/>
 * email 1169654504@qq.com & ztiany3@gmail.com           <br/>
 * date 2016-04-11 12:44                                                         <br/>
 * description                                                                             <br/>
 * version
 */
class LoadMoreImpl implements ILoadMore {

    private View mLoadMoreView;
    private boolean mIsPause;//是否暂停loadMore
    private boolean mHasMore = true;//是否还有更多
    private boolean mIsLoadingMore;//是否正在加载更多
    private OnLoadMoreListener mOnLoadMoreListener;//加载更多监听
    private LoadMoreViewFactory mLoadMoreViewFactory;

    private
    @Mode
    int mLoadMode = LoadMode.AUTO_LOAD;//是否自动加载更多


    void tryCallLoadMore() {
        if (isAutoLoad()) {
            if (!mIsPause) {
                callLoadMore();
            }
        } else {
            if (!mIsPause) {
                LoadMoreViewCaller.callShowClickLoad(mLoadMoreView);
            }
        }
    }

    View getLoadMoreView(ViewGroup parent) {
        if (mLoadMoreViewFactory == null) {
            createDefaultLoadMoreView(parent);
        } else {
            mLoadMoreView = mLoadMoreViewFactory.onCreateLoadMoreView(parent);
            if (mLoadMoreView == null) {
                throw new NullPointerException("LoadMoreViewFactory :" + mLoadMoreViewFactory + " call onCreateLoadMoreView return null");
            }
        }
        mLoadMoreView.setOnClickListener(new ClickListener());
        if (mLoadMode == LoadMode.CLICK_LOAD) {
            LoadMoreViewCaller.callShowClickLoad(mLoadMoreView);
        } else {
            LoadMoreViewCaller.callLoading(mLoadMoreView);
        }
        return mLoadMoreView;
    }

    private void createDefaultLoadMoreView(ViewGroup parent) {
        mLoadMoreView = new LoadMoreView(parent.getContext());
    }


    @Override
    public void loadFail() {
        loadEnd();
        LoadMoreViewCaller.callFail(mLoadMoreView);

    }

    @Override
    public void loadCompleted(final boolean hasMore) {
        loadEnd();
        mHasMore = hasMore;
        LoadMoreViewCaller.callCompleted(mLoadMoreView, mHasMore);
    }

    @Override
    public void pause(boolean pause) {
        mIsPause = pause;
        LoadMoreViewCaller.callPause(mLoadMoreView);
    }

    @Override
    public boolean isLoadingMore() {
        return mIsLoadingMore;
    }

    @Override
    public void setLoadMode(@Mode int loadMode) {
        mLoadMode = loadMode;
    }

    @Override
    public void setLoadMoreViewFactory(LoadMoreViewFactory factory) {
        mLoadMoreViewFactory = factory;
    }


    private boolean isAutoLoad() {
        return mLoadMode == LoadMode.AUTO_LOAD;
    }


    private class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            /**
             * 点击条目，可能需要执行其他的逻辑，比如，没有网络的情况下，打开网络
             */
            if (mIsPause) {
                if (mOnLoadMoreListener != null) {
                    mOnLoadMoreListener.onPauseClick();
                }
                return;
            }
            callLoadMore();
        }
    }


    @Override
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener;
    }

    /**
     * 加载结束
     */
    private void loadEnd() {
        mIsLoadingMore = false;
    }


    private void callLoadMore() {
        if (!mIsLoadingMore && mOnLoadMoreListener != null && mHasMore) {
            LoadMoreViewCaller.callLoading(mLoadMoreView);
            mOnLoadMoreListener.onLoadMore();
            mIsLoadingMore = true;
        }
    }

    /**
     * Method Caller
     */
    private static class LoadMoreViewCaller {


        static void callLoading(View view) {
            if (view != null && view instanceof ILoadMoreView) {
                ((ILoadMoreView) view).onLoading();
            }
        }

        static void callCompleted(View view, boolean hasMore) {
            if (view != null && view instanceof ILoadMoreView) {
                ((ILoadMoreView) view).onCompleted(hasMore);
            }
        }

        static void callShowClickLoad(View view) {
            if (view != null && view instanceof ILoadMoreView) {
                ((ILoadMoreView) view).onClickLoad();
            }
        }


        static void callFail(View view) {
            if (view != null && view instanceof ILoadMoreView) {
                ((ILoadMoreView) view).onFail();
            }
        }

        static void callPause(View view) {
            if (view != null && view instanceof ILoadMoreView) {
                ((ILoadMoreView) view).onPause();
            }
        }


    }


}
