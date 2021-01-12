package com.ztiany.loadmore.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

class LoadMoreImpl implements ILoadMore {

    private View mLoadMoreView;
    private boolean mHasMore = false;

    private OnLoadMoreListener mOnLoadMoreListener;

    private LoadMoreViewFactory mLoadMoreViewFactory;

    private final static int STATUS_NONE = 0;
    private final static int STATUS_LOADING = 1;
    private final static int STATUS_FAIL = 2;
    private final static int STATUS_COMPLETE = 3;
    private final static int STATUS_PRE = 5;

    private int mVisibilityWhenNoMore = View.VISIBLE;

    private int mCurrentStatus = STATUS_NONE;

    private long mPreviousTimeCallingLoadMore;

    private long mMixLoadMoreInterval = 0;

    private final boolean timeLimited;

    @LoadMode private int mLoadMode = LoadMode.AUTO_LOAD;

    public LoadMoreImpl(boolean useScrollListener) {
        timeLimited = useScrollListener;
    }

    void tryCallLoadMore(int direction) {
        if (mOnLoadMoreListener == null || !mOnLoadMoreListener.canLoadMore()) {
            return;
        }
        if (mCurrentStatus == STATUS_LOADING) {
            return;
        }
        if (isAutoLoad()) {
            mCurrentStatus = STATUS_PRE;
            if (checkIfNeedCallLoadMoreWhenAutoMode(direction)) {
                callLoadMore();
            }
        } else {
            if (mCurrentStatus == STATUS_FAIL) {
                return;
            }
            if (mCurrentStatus == STATUS_COMPLETE && !mHasMore) {
                return;
            }
            mCurrentStatus = STATUS_PRE;
            LoadMoreViewCaller.callShowClickLoad(mLoadMoreView);
        }
    }

    private boolean checkIfNeedCallLoadMoreWhenAutoMode(int direction) {
        if (direction != Direction.UP) {
            return false;
        }
        if (timeLimited) {
            return System.currentTimeMillis() - mPreviousTimeCallingLoadMore >= mMixLoadMoreInterval;
        } else {
            return true;
        }
    }

    View getLoadMoreView(ViewGroup parent) {
        initLoadMoreView(parent);
        if (mLoadMode == LoadMode.CLICK_LOAD) {
            initClickLoadMoreViewStatus();
        } else {
            initAutoLoadMoreViewStatus();
        }
        return mLoadMoreView;
    }

    private void initAutoLoadMoreViewStatus() {
        switch (mCurrentStatus) {
            case STATUS_PRE:
            case STATUS_NONE:
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

    private void initClickLoadMoreViewStatus() {
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

    @Override
    public void setMinLoadMoreInterval(long mixLoadMoreInterval) {
        mMixLoadMoreInterval = mixLoadMoreInterval;
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
        processAutoHiddenWhenNoMore();
    }

    @Override
    public void loadFail() {
        mCurrentStatus = STATUS_FAIL;
        LoadMoreViewCaller.callFail(mLoadMoreView);
        processAutoHiddenWhenNoMore();
    }

    @Override
    public void loadCompleted(final boolean hasMore) {
        Log.d("MORE", "loadCompleted" + hasMore + "");
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
                //自动加载更多只有错误才能点击
                if ((mCurrentStatus == STATUS_FAIL)) {
                    callLoadMore();
                }
            } /*点击加载更多模式，只有暂停和准备状态才能点击*/ else if (mLoadMode == LoadMode.CLICK_LOAD) {
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
            mPreviousTimeCallingLoadMore = System.currentTimeMillis();
        }
    }

    @Override
    public void setAutoHiddenWhenNoMore(boolean autoHiddenWhenNoMore) {
        mVisibilityWhenNoMore = autoHiddenWhenNoMore ? View.INVISIBLE : View.VISIBLE;
        processAutoHiddenWhenNoMore();
    }

    @Override
    public void setVisibilityWhenNoMore(int visibility) {
        mVisibilityWhenNoMore = visibility;
        processAutoHiddenWhenNoMore();
    }

    private void processAutoHiddenWhenNoMore() {
        if (mLoadMoreView != null) {
            if (mCurrentStatus == STATUS_COMPLETE) {
                mLoadMoreView.setVisibility(mHasMore ? View.VISIBLE : mVisibilityWhenNoMore);
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
            if (view instanceof ILoadMoreView) {
                ((ILoadMoreView) view).onLoading();
            }
        }

        static void callCompleted(View view, boolean hasMore) {
            Log.d("MORE", hasMore + "");
            if (view instanceof ILoadMoreView) {
                ((ILoadMoreView) view).onCompleted(hasMore);
            }
        }

        static void callShowClickLoad(View view) {
            if (view instanceof ILoadMoreView) {
                ((ILoadMoreView) view).onClickLoad();
            }
        }

        static void callFail(View view) {
            if (view instanceof ILoadMoreView) {
                ((ILoadMoreView) view).onFail();
            }
        }
    }

}
