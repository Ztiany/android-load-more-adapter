package com.ztiany.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

class LoadMoreImpl implements ILoadMore {

    private View mLoadMoreView;
    private boolean mHasMore = true;
    private OnLoadMoreListener mOnLoadMoreListener;
    private LoadMoreViewFactory mLoadMoreViewFactory;

    private final static int STATUS_NONE = 0;
    private final static int STATUS_LOADING = 1;
    private final static int STATUS_FAIL = 2;
    private final static int STATUS_COMPLETE = 3;
    private final static int STATUS_PRE = 5;

    private boolean mAutoHiddenWhenNoMore;

    private int mCurrentStatus = STATUS_NONE;

    @LoadMode
    private int mLoadMode = LoadMode.AUTO_LOAD;//是否自动加载更多

    void tryCallLoadMore() {
        if (mOnLoadMoreListener == null || !mOnLoadMoreListener.canLoadMore()) {
            return;
        }
        if (mCurrentStatus == STATUS_LOADING) {
            return;
        }
        if (isAutoLoad()) {
            mCurrentStatus = STATUS_PRE;
            callLoadMore();
        } else {
            if (mCurrentStatus != STATUS_FAIL) {
                mCurrentStatus = STATUS_PRE;
                LoadMoreViewCaller.callShowClickLoad(mLoadMoreView);
            }
        }
    }

    View getLoadMoreView(ViewGroup parent) {
        initLoadMoreView(parent);
        if (mLoadMode == LoadMode.CLICK_LOAD) {
            processClickLoadMore();
        } else {
            processAutoLoadMore();
        }
        return mLoadMoreView;
    }

    private void processAutoLoadMore() {
        switch (mCurrentStatus) {
            case STATUS_PRE:
            case STATUS_NONE: {
                LoadMoreViewCaller.callLoading(mLoadMoreView);
                break;
            }
            case STATUS_LOADING: {
                LoadMoreViewCaller.callLoading(mLoadMoreView);
                break;
            }
            case STATUS_FAIL: {
                LoadMoreViewCaller.callFail(mLoadMoreView);
                break;
            }
            case STATUS_COMPLETE: {
                LoadMoreViewCaller.callCompleted(mLoadMoreView, mHasMore);
                break;
            }
        }
    }

    private void processClickLoadMore() {
        switch (mCurrentStatus) {
            case STATUS_PRE:
            case STATUS_NONE: {
                LoadMoreViewCaller.callShowClickLoad(mLoadMoreView);
                break;
            }
            case STATUS_LOADING: {
                LoadMoreViewCaller.callLoading(mLoadMoreView);
                break;
            }
            case STATUS_FAIL: {
                LoadMoreViewCaller.callFail(mLoadMoreView);
                break;
            }
            case STATUS_COMPLETE: {
                LoadMoreViewCaller.callCompleted(mLoadMoreView, mHasMore);
                break;
            }
        }
    }

    private void initLoadMoreView(ViewGroup parent) {
        if (mLoadMoreViewFactory == null) {
            mLoadMoreView = new LoadMoreView(parent.getContext());
            mLoadMoreView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        } else {
            mLoadMoreView = mLoadMoreViewFactory.onCreateLoadMoreView(parent);
            if (mLoadMoreView == null) {
                throw new NullPointerException("LoadMoreViewFactory :" + mLoadMoreViewFactory + " call onCreateLoadMoreView return null");
            }
        }
        mLoadMoreView.setOnClickListener(new ClickListener());
    }

    @Override
    public void loadFail() {
        mCurrentStatus = STATUS_FAIL;
        LoadMoreViewCaller.callFail(mLoadMoreView);
        processAutoHiddenWhenNoMore();
    }

    @Override
    public void loadCompleted(final boolean hasMore) {
        mHasMore = hasMore;
        mCurrentStatus = STATUS_COMPLETE;
        LoadMoreViewCaller.callCompleted(mLoadMoreView, mHasMore);
        processAutoHiddenWhenNoMore();
    }

    @Override
    public boolean isLoadingMore() {
        return mCurrentStatus == STATUS_LOADING;
    }

    @Override
    public void setLoadMode(@LoadMode int loadMode) {
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

            if (mLoadMode == LoadMode.AUTO_LOAD) {
                if ((mCurrentStatus == STATUS_FAIL)) {//自动加载更多只有错误才能点击
                    callLoadMore();
                }
            } else if (mLoadMode == LoadMode.CLICK_LOAD) {//点击加载更多只有暂停和准备状态才能点击和
                if (mCurrentStatus == STATUS_PRE || mCurrentStatus == STATUS_FAIL) {
                    callLoadMore();
                }
            }
        }
    }

    @Override
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener;
    }

    private void callLoadMore() {
        if (mCurrentStatus != STATUS_LOADING && mOnLoadMoreListener != null && mHasMore) {
            LoadMoreViewCaller.callLoading(mLoadMoreView);
            mCurrentStatus = STATUS_LOADING;
            mOnLoadMoreListener.onLoadMore();
        }
    }

    @Override
    public void setAutoHiddenWhenNoMore(boolean autoHiddenWhenNoMore) {
        mAutoHiddenWhenNoMore = autoHiddenWhenNoMore;
        processAutoHiddenWhenNoMore();
    }

    private void processAutoHiddenWhenNoMore() {
        if (!mAutoHiddenWhenNoMore) {
            return;
        }
        if (mLoadMoreView != null) {
            if (mCurrentStatus == STATUS_COMPLETE) {
                mLoadMoreView.setVisibility(mHasMore ? View.VISIBLE : View.INVISIBLE);
            } else {
                mLoadMoreView.setVisibility(View.VISIBLE);
            }
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
    }
}
