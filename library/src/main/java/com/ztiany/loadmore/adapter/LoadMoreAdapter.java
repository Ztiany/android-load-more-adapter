package com.ztiany.loadmore.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.List;


public class LoadMoreAdapter extends RecyclerViewAdapterWrapper implements LoadMoreController {

    private static final String TAG = "LoadMoreAdapter";

    private static final int LOAD_MORE_TYPE = Integer.MAX_VALUE;
    private static final int LOAD_MORE_ID = Integer.MAX_VALUE - 999;

    private final LoadMoreControllerImpl mLoadMoreImpl;

    private OnRecyclerViewScrollBottomListener mScrollListener;

    private RecyclerView mRecyclerView;

    private final KeepFullSpanUtils mKeepFullSpanUtils;

    public static LoadMoreAdapter wrap(RecyclerView.Adapter adapter) {
        return new LoadMoreAdapter(adapter, false);
    }

    public static LoadMoreAdapter wrap(RecyclerView.Adapter adapter, boolean useScrollListener) {
        return new LoadMoreAdapter(adapter, useScrollListener);
    }

    LoadMoreAdapter(RecyclerView.Adapter wrapped, boolean useScrollListener) {
        super(wrapped);
        mLoadMoreImpl = new LoadMoreControllerImpl(useScrollListener);
        mKeepFullSpanUtils = new KeepFullSpanUtils();

        if (useScrollListener) {
            mScrollListener = new OnRecyclerViewScrollBottomListener() {
                @Override
                public void onBottom(@Direction int direction) {
                    Log.d(TAG, "onBottom call LoadMore.");
                    mLoadMoreImpl.tryCallLoadMore(direction);
                }
            };
        }
    }

    private void initOnAttachedToRecyclerView() {
        if (mScrollListener != null) {
            mRecyclerView.addOnScrollListener(mScrollListener);
        }

        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();

        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            mKeepFullSpanUtils.mOriginSpanSizeLookup = gridLayoutManager.getSpanSizeLookup();
            mKeepFullSpanUtils.setFullSpanForGird(gridLayoutManager);
        }
    }

    @Override
    public void setLoadingTriggerThreshold(int loadingTriggerThreshold) {
        if (mScrollListener != null) {
            mScrollListener.setLoadingTriggerThreshold(loadingTriggerThreshold);
        } else {
            Log.d(TAG, "you are not using ScrollListener, this call has no effect.");
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == LOAD_MORE_TYPE) {
            View loadMoreView = mLoadMoreImpl.getLoadMoreView(parent);
            keepFullSpan(loadMoreView);
            return new ViewHolder(loadMoreView) {
            };
        } else {
            return super.onCreateViewHolder(parent, viewType);
        }
    }

    private void keepFullSpan(View itemView) {
        if (mRecyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            mKeepFullSpanUtils.setFullSpanForStaggered(itemView);
        } else if (mRecyclerView.getLayoutManager() instanceof GridLayoutManager) {
            // no op
        } else if (mRecyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            // no op
        } else {
            AdapterInterface adapterInterface = LoadMoreConfig.getAdapterInterface();
            if (adapterInterface != null) {
                adapterInterface.setItemFullSpan(itemView, mRecyclerView);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (getItemViewType(position) != LOAD_MORE_TYPE) {
            super.onBindViewHolder(holder, position);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List payloads) {
        if (getItemViewType(position) != LOAD_MORE_TYPE) {
            super.onBindViewHolder(holder, position, payloads);
        }
    }

    @Override
    public int getItemCount() {
        int count = super.getItemCount();
        return count == 0 ? 0 : count + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == super.getItemCount()) {
            return LOAD_MORE_TYPE;
        } else {
            return super.getItemViewType(position);
        }
    }

    private boolean isWrapperType(int position) {
        return getItemViewType(position) == LOAD_MORE_TYPE;
    }

    @Override
    public long getItemId(int position) {
        if (!isWrapperType(position)) {
            return super.getItemId(position);
        }
        return LOAD_MORE_ID;
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        if (isLoadMoreOrStateViewHolder(holder)) {
            return;
        }
        super.onViewRecycled(holder);
    }

    @Override
    public boolean onFailedToRecycleView(@NonNull ViewHolder holder) {
        if (isLoadMoreOrStateViewHolder(holder)) {
            return false;
        }
        return super.onFailedToRecycleView(holder);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        if (isLoadMoreOrStateViewHolder(holder)) {
            if (mScrollListener == null) {
                Log.d(TAG, "onLoadMoreViewAttachedToWindow call LoadMore.");
                mLoadMoreImpl.tryCallLoadMore(Direction.UP);
            }
            return;
        }
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        if (isLoadMoreOrStateViewHolder(holder)) {
            return;
        }
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
        initOnAttachedToRecyclerView();
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        recyclerView.removeOnScrollListener(mScrollListener);
        mKeepFullSpanUtils.cleanFullSpanIfNeed(recyclerView);
    }

    private boolean isLoadMoreOrStateViewHolder(ViewHolder viewHolder) {
        int itemViewType = viewHolder.getItemViewType();
        return itemViewType == LOAD_MORE_TYPE;
    }

    ///////////////////////////////////////////////////////////////////////////
    // LoadMoreController
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mLoadMoreImpl.setOnLoadMoreListener(onLoadMoreListener);
    }

    @Override
    public void loadFail() {
        mLoadMoreImpl.loadFail();
    }

    @Override
    public void loadCompleted(boolean hasMore) {
        mLoadMoreImpl.loadCompleted(hasMore);
    }

    @Override
    public boolean isLoadingMore() {
        return mLoadMoreImpl.isLoadingMore();
    }

    @Override
    public void setLoadMode(@LoadMode int loadMode) {
        mLoadMoreImpl.setLoadMode(loadMode);
    }

    @Override
    public void setLoadMoreViewFactory(LoadMoreViewFactory factory) {
        mLoadMoreImpl.setLoadMoreViewFactory(factory);
    }

    @Override
    public void setAutoHiddenWhenNoMore(boolean autoHiddenWhenNoMore) {
        mLoadMoreImpl.setAutoHiddenWhenNoMore(autoHiddenWhenNoMore);
    }

    @Override
    public void setMinLoadMoreInterval(long minLoadMoreInterval) {
        mLoadMoreImpl.setMinLoadMoreInterval(minLoadMoreInterval);
    }

    @Override
    public void stopAutoLoadWhenFailed(boolean stopAutoLoadWhenFailed) {
        mLoadMoreImpl.stopAutoLoadWhenFailed(stopAutoLoadWhenFailed);
    }

    @Override
    public void setLoadMoreDirection(@Direction int direction) {
        mLoadMoreImpl.setLoadMoreDirection(direction);
    }

}