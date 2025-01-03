package com.ztiany.loadmore.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

final class LoadMoreControllerImpl implements LoadMoreController {

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
    private final boolean mTimeLimited;

    @LoadMode
    private int mLoadMode = LoadMoreConfig.getLoadMode();

    @Direction
    private int mDirection = Direction.UP;

    public LoadMoreControllerImpl(boolean useScrollListener, RecyclerView.Adapter adapter) {
        mTimeLimited = useScrollListener;
    }

    void tryCallLoadMore(int direction) {
        if (mOnLoadMoreListener == null || !mOnLoadMoreListener.canLoadMore()) {
            return;
        }

        if (mCurrentStatus == STATUS_LOADING) {
            changeAppearanceByStatus();
            return;
        }

        if (isAutoLoad()) {
            if ((mStopAutoLoadWhenFailed && mCurrentStatus == STATUS_FAIL) ||
                    (mCurrentStatus == STATUS_COMPLETE && !mHasMore)) {
                changeAppearanceByStatus();
                return;
            }
            mCurrentStatus = STATUS_PRE;
            if (checkIfNeedCallLoadMoreWhenAutoMode(direction)) {
                callLoadMore();
            }
            return;
        }

        // Click load more mode
        if (mCurrentStatus == STATUS_FAIL) {
            changeAppearanceByStatus();
            return;
        }
        if (mCurrentStatus == STATUS_COMPLETE && !mHasMore) {
            changeAppearanceByStatus();
            return;
        }
        mCurrentStatus = STATUS_PRE;
        LoadMoreViewCaller.callShowClickLoad(mLoadMoreView);
    }

    private boolean checkIfNeedCallLoadMoreWhenAutoMode(int direction) {
        if (direction != 0 && direction != mDirection) {
            return false;
        }
        if (mTimeLimited) {
            return System.currentTimeMillis() - mPreviousTimeCallingLoadMore >= mMixLoadMoreInterval;
        } else {
            return true;
        }
    }

    View getLoadMoreView(ViewGroup parent) {
        createLoadMoreView(parent);
        changeAppearanceByStatus();
        return mLoadMoreView;
    }

    private void changeAppearanceByStatus() {
        autoHideWhenNoMore();

        if (mLoadMode == LoadMode.CLICK_LOAD) {
            switch (mCurrentStatus) {
                case STATUS_PRE: {
                    LoadMoreViewCaller.callShowClickLoad(mLoadMoreView);
                    break;
                }
                case STATUS_LOADING: {
                    LoadMoreViewCaller.callWhenLoading(mLoadMoreView);
                    break;
                }
                case STATUS_FAIL: {
                    LoadMoreViewCaller.callWhenFailed(mLoadMoreView);
                    break;
                }
                case STATUS_COMPLETE: {
                    LoadMoreViewCaller.callWhenCompleted(mLoadMoreView, mHasMore);
                    break;
                }
            }
            return;
        }

        // Auto load more mode
        switch (mCurrentStatus) {
            case STATUS_PRE:
            case STATUS_LOADING: {
                LoadMoreViewCaller.callWhenLoading(mLoadMoreView);
                break;
            }
            case STATUS_FAIL: {
                LoadMoreViewCaller.callWhenFailed(mLoadMoreView);
                break;
            }
            case STATUS_COMPLETE: {
                LoadMoreViewCaller.callWhenCompleted(mLoadMoreView, mHasMore);
                break;
            }
        }
    }

    private void createLoadMoreView(ViewGroup parent) {
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
        autoHideWhenNoMore();
    }

    private void callLoadMore() {
        if (mCurrentStatus != STATUS_LOADING && mHasMore) {
            LoadMoreViewCaller.callWhenLoading(mLoadMoreView);
            mCurrentStatus = STATUS_LOADING;
            if (mOnLoadMoreListener != null) {
                mOnLoadMoreListener.onLoadMore();
            }
            mPreviousTimeCallingLoadMore = System.currentTimeMillis();
        } else {
            changeAppearanceByStatus();
        }
    }

    private void autoHideWhenNoMore() {
        if (mLoadMoreView != null) {
            if (mCurrentStatus == STATUS_COMPLETE) {
                mLoadMoreView.setVisibility(mHasMore ? View.VISIBLE : mVisibilityWhenNoMore);
            } else {
                mLoadMoreView.setVisibility(View.VISIBLE);
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // public api
    ///////////////////////////////////////////////////////////////////////////

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

    @Override
    public void setAutoHideWhenNoMore(boolean hideWhenNoMore) {
        mVisibilityWhenNoMore = hideWhenNoMore ? View.INVISIBLE : View.VISIBLE;
        autoHideWhenNoMore();
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

    @Override
    public void setLoadingMore() {
        mCurrentStatus = STATUS_LOADING;
        changeAppearanceByStatus();
    }

    @Override
    public void loadFailed() {
        mCurrentStatus = STATUS_FAIL;
        changeAppearanceByStatus();
    }

    @Override
    public void loadCompleted(final boolean hasMore) {
        this.loadCompleted(hasMore, false);
    }

    @Override
    public void loadCompleted(boolean hasMore, boolean appended) {
        mHasMore = hasMore;
        mCurrentStatus = STATUS_COMPLETE;
        // If there are new items appended, we don't need to change the appearance.
        // Because the new items will be shown in the list.
        if (!appended) {
            changeAppearanceByStatus();
        }
    }

    @Override
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener;
    }

    void onBindViewHolder(RecyclerView.ViewHolder holder) {
        changeAppearanceByStatus();
    }

    ///////////////////////////////////////////////////////////////////////////
    // inner class
    ///////////////////////////////////////////////////////////////////////////

    private final class ClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (mLoadMode == LoadMode.AUTO_LOAD) {
                if ((mCurrentStatus == STATUS_FAIL)) {
                    callLoadMore();
                }
            } else if (mLoadMode == LoadMode.CLICK_LOAD) {
                if (mCurrentStatus == STATUS_PRE || mCurrentStatus == STATUS_FAIL) {
                    callLoadMore();
                }
            }
        }
    }

    /**
     * Method Caller
     */
    private static class LoadMoreViewCaller {

        static void callWhenLoading(View view) {
            if (view instanceof LoadMoreView) {
                ((LoadMoreView) view).onLoading();
            }
        }

        static void callWhenCompleted(View view, boolean hasMore) {
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

        static void callWhenFailed(View view) {
            if (view instanceof LoadMoreView) {
                ((LoadMoreView) view).onFailed();
            }
        }
    }

}
