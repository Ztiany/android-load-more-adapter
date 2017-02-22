package com.ztiany.loadmore;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * Author 湛添友      <br/>
 * Email ztiany3@gmail.com      <br/>
 * Date 2015-08-24 23:13      <br/>
 * Description：RecyclerView多布局通用滑动监听器
 */
public abstract class OnRecyclerViewScrollBottomListener extends RecyclerView.OnScrollListener {

    private int mLayoutManagerType;
    private static final int LINEAR = 1;
    private static final int GRID = 2;
    private static final int STAGGERED_GRID = 3;

    private LastVisibleItemPositionProvider mLastVisibleItemPositionGetter;

    private int mLoadingTriggerThreshold;

    private int getLoadingTriggerThreshold() {
        return mLoadingTriggerThreshold;
    }

    public void setLoadingTriggerThreshold(int loadingTriggerThreshold) {
        mLoadingTriggerThreshold = loadingTriggerThreshold;
    }

    /**
     * 最后一个的位置
     */
    private int[] mLastPositions;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
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

        /*
      最后一个可见的item的位置
     */
        int lastVisibleItemPosition;
        switch (mLayoutManagerType) {
            case LINEAR:
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager)
                        .findLastVisibleItemPosition();
                break;
            case GRID:
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager)
                        .findLastVisibleItemPosition();
                break;
            case STAGGERED_GRID:
                StaggeredGridLayoutManager staggeredGridLayoutManager
                        = (StaggeredGridLayoutManager) layoutManager;
                if (mLastPositions == null) {
                    mLastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                }
                staggeredGridLayoutManager.findLastVisibleItemPositions(mLastPositions);
                lastVisibleItemPosition = findMax(mLastPositions);
                break;
            default: {
                if (mLastVisibleItemPositionGetter == null) {
                    throw new IllegalStateException("you should set com.ztiany.loadmore.LastVisibleItemPositionProvider when you use custom layoutManager");
                }
                lastVisibleItemPosition = mLastVisibleItemPositionGetter.getLastVisibleItemPosition(layoutManager);
                break;
            }
        }

        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        if ((visibleItemCount > 0 && (lastVisibleItemPosition) >= totalItemCount - 1 - mLoadingTriggerThreshold)) {
            onBottom();
        }

    }

    public void setLastVisibleItemPositionGetter(LastVisibleItemPositionProvider lastVisibleItemPositionGetter) {
        mLastVisibleItemPositionGetter = lastVisibleItemPositionGetter;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
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

}