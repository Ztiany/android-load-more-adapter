package com.ztiany.loadmore.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

abstract class OnRecyclerViewScrollBottomListener extends RecyclerView.OnScrollListener {

    private int mLayoutManagerType;

    private static final int LINEAR = 1;
    private static final int GRID = 2;
    private static final int STAGGERED_GRID = 3;

    private AdapterInterface mLastVisibleItemPositionGetter;

    private int mLoadingTriggerThreshold;
    private RecyclerView mRecyclerView;

    /**
     * 最后一个的位置
     */
    private int[] mLastPositions;

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        if (mRecyclerView != recyclerView) {
            mRecyclerView = recyclerView;
        }
        super.onScrolled(recyclerView, dx, dy);

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

        if (mLayoutManagerType == 0) {
            if (layoutManager instanceof GridLayoutManager) {
                mLayoutManagerType = GRID;
            } else if (layoutManager instanceof LinearLayoutManager) {
                mLayoutManagerType = LINEAR;
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                mLayoutManagerType = STAGGERED_GRID;
            }
        }

        /* 最后一个可见的item的位置*/
        int lastVisibleItemPosition;
        switch (mLayoutManagerType) {
            case LINEAR:
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                break;
            case GRID:
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                break;
            case STAGGERED_GRID:
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                if (mLastPositions == null) {
                    mLastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                }
                staggeredGridLayoutManager.findLastVisibleItemPositions(mLastPositions);
                lastVisibleItemPosition = findMax(mLastPositions);
                break;
            default: {
                if (mLastVisibleItemPositionGetter == null) {
                    throw new IllegalStateException("you need provide AdapterInterface when you use custom layoutManager");
                }
                lastVisibleItemPosition = mLastVisibleItemPositionGetter.getLastVisibleItemPosition(mRecyclerView);
                break;
            }
        }

        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        if ((visibleItemCount > 0 && (lastVisibleItemPosition) >= totalItemCount - 1 - mLoadingTriggerThreshold)) {
            onBottom();
        }
    }

    void setLastVisibleItemPositionGetter(AdapterInterface adapterInterface) {
        mLastVisibleItemPositionGetter = adapterInterface;
    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        if (mRecyclerView != recyclerView) {
            mRecyclerView = recyclerView;
        }
        super.onScrollStateChanged(recyclerView, newState);
    }

    public abstract void onBottom();

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    private int getLoadingTriggerThreshold() {
        return mLoadingTriggerThreshold;
    }

    void setLoadingTriggerThreshold(int loadingTriggerThreshold) {
        mLoadingTriggerThreshold = loadingTriggerThreshold;
    }

}