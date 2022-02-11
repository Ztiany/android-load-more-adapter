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


public class WrapperAdapter extends RecyclerViewAdapterWrapper implements ILoadMore {

    private static final String TAG = "WrapperAdapter";

    private static final int LOAD_MORE_FOOTER = Integer.MAX_VALUE;

    private final LoadMoreImpl mLoadMoreManager;

    private OnRecyclerViewScrollBottomListener mScrollListener;

    private AdapterInterface mAdapterInterface;

    private RecyclerView mRecyclerView;

    private final KeepFullSpanUtils mKeepFullSpanUtils;

    public static WrapperAdapter wrap(RecyclerView.Adapter adapter) {
        return new WrapperAdapter(adapter, false);
    }

    public static WrapperAdapter wrap(RecyclerView.Adapter adapter, boolean useScrollListener) {
        return new WrapperAdapter(adapter, useScrollListener);
    }

    private WrapperAdapter(RecyclerView.Adapter wrapped, boolean useScrollListener) {
        super(wrapped);
        mLoadMoreManager = new LoadMoreImpl(useScrollListener);
        mKeepFullSpanUtils = new KeepFullSpanUtils();

        if (useScrollListener) {
            mScrollListener = new OnRecyclerViewScrollBottomListener() {
                @Override
                public void onBottom(@Direction int direction) {
                    Log.d(TAG, "onBottom call LoadMore.");
                    mLoadMoreManager.tryCallLoadMore(direction);
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

    public void setAdapterInterface(AdapterInterface lastVisibleItemPosition) {
        mAdapterInterface = lastVisibleItemPosition;
        if (mScrollListener != null) {
            mScrollListener.setLastVisibleItemPositionGetter(lastVisibleItemPosition);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == LOAD_MORE_FOOTER) {
            View loadMoreView = mLoadMoreManager.getLoadMoreView(parent);
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
            if (mAdapterInterface != null) {
                mAdapterInterface.setItemFullSpan(itemView, mRecyclerView);
            } else {
                throw new NullPointerException("you need provide  ItemFullSpanProvider when you use custom layoutManager");
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (getItemViewType(position) != LOAD_MORE_FOOTER) {
            super.onBindViewHolder(holder, position);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List payloads) {
        if (getItemViewType(position) != LOAD_MORE_FOOTER) {
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
            return LOAD_MORE_FOOTER;
        } else {
            return super.getItemViewType(position);
        }
    }

    private boolean isWrapperType(int position) {
        return getItemViewType(position) == LOAD_MORE_FOOTER;
    }

    @Override
    public long getItemId(int position) {
        if (!isWrapperType(position)) {
            return super.getItemId(position);
        }
        return position;
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
        return !(isLoadMoreOrStateViewHolder(holder)) && super.onFailedToRecycleView(holder);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        if (isLoadMoreOrStateViewHolder(holder)) {
            if (mScrollListener == null) {
                Log.d(TAG, "onLoadMoreViewAttachedToWindow call LoadMore.");
                mLoadMoreManager.tryCallLoadMore(Direction.UP);
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
        return itemViewType == LOAD_MORE_FOOTER;
    }

    ///////////////////////////////////////////////////////////////////////////
    // ILoadMore
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mLoadMoreManager.setOnLoadMoreListener(onLoadMoreListener);
    }

    @Override
    public void loadFail() {
        mLoadMoreManager.loadFail();
    }

    @Override
    public void loadCompleted(boolean hasMore) {
        mLoadMoreManager.loadCompleted(hasMore);
    }

    @Override
    public boolean isLoadingMore() {
        return mLoadMoreManager.isLoadingMore();
    }

    @Override
    public void setLoadMode(@LoadMode int loadMore) {
        mLoadMoreManager.setLoadMode(loadMore);
    }

    @Override
    public void setLoadMoreViewFactory(LoadMoreViewFactory factory) {
        mLoadMoreManager.setLoadMoreViewFactory(factory);
    }

    @Override
    public void setAutoHiddenWhenNoMore(boolean autoHiddenWhenNoMore) {
        mLoadMoreManager.setAutoHiddenWhenNoMore(autoHiddenWhenNoMore);
    }

    @Override
    public void setVisibilityWhenNoMore(int visibility) {
        mLoadMoreManager.setVisibilityWhenNoMore(visibility);
    }

    @Override
    public void setMinLoadMoreInterval(long minLoadMoreInterval) {
        mLoadMoreManager.setMinLoadMoreInterval(minLoadMoreInterval);
    }

}