package com.ztiany.loadmore.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

class LoadMoreControllerImpl implements LoadMoreController {

    private View mLoadMoreView;

    private boolean mHasMore = false;
    private boolean mStopAutoLoadWhenFailed = LoadMoreConfig.isStopAutoLoadWhenFailed();

    private OnLoadMoreListener mOnLoadMoreListener;

    private LoadMoreViewFactory mLoadMoreViewFactory = LoadMoreConfig.getLoadMoreViewFactory();

    private final static int STATUS_LOADING = 1;
    private final static int STATUS_FAIL = 2;
    private final static int STATUS_COMPLETE = 3;
    private final static int STATUS_PRE = 4;
    private int mCurrentStatus = STATUS_PRE;

    private int mVisibilityWhenNoMore = View.VISIBLE;

    private long mPreviousTimeCallingLoadMore;
    private long mMixLoadMoreInterval = LoadMoreConfig.getMinLoadMoreInterval();
    private final boolean timeLimited;

    @LoadMode
    private int mLoadMode = LoadMoreConfig.getLoadMode();

    @Direction
    private int mDirection = Direction.UP;

    public LoadMoreControllerImpl(boolean useScrollListener) {
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

            if (mStopAutoLoadWhenFailed && mCurrentStatus == STATUS_FAIL) {
                return;
            }
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
        if (direction != mDirection) {
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
            case STATUS_PRE: {
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

    @Override
    public void stopAutoLoadWhenFailed(boolean stopAutoLoadWhenFailed) {
        mStopAutoLoadWhenFailed = stopAutoLoadWhenFailed;
    }

    @Override
    public void setLoadMoreDirection(@Direction int direction) {
        mDirection = direction;
    }

    @Override
    public void setLoadingTriggerThreshold(int loadingTriggerThreshold) {
        //no op
    }

    private void initLoadMoreView(ViewGroup parent) {
        if (mLoadMoreViewFactory == null) {
            mLoadMoreView = new DefaultLoadMoreView(parent.getContext());
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
                //自动加载更多模式，只有错误才能点击
                if ((mCurrentStatus == STATUS_FAIL)) {
                    callLoadMore();
                }
            }  /*点击加载更多模式，只有错误和准备状态才能点击*/ else if (mLoadMode == LoadMode.CLICK_LOAD) {
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
            if (view instanceof LoadMoreView) {
                ((LoadMoreView) view).onLoading();
            }
        }

        static void callCompleted(View view, boolean hasMore) {
            Log.d("MORE", hasMore + "");
            if (view instanceof LoadMoreView) {
                ((LoadMoreView) view).onCompleted(hasMore);
            }
        }

        static void callShowClickLoad(View view) {
            if (view instanceof LoadMoreView) {
                ((LoadMoreView) view).showClickToLoadMore();
            }
        }

        static void callFail(View view) {
            if (view instanceof LoadMoreView) {
                ((LoadMoreView) view).onFailed();
            }
        }
    }

}
