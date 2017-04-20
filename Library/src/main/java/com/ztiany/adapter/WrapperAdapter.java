package com.ztiany.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import static android.support.v7.widget.RecyclerView.Adapter;
import static android.support.v7.widget.RecyclerView.ViewHolder;

public class WrapperAdapter extends RecyclerViewAdapterWrapper implements LoadMoreManager {

    private static final int LOAD_MORE_FOOTER = Integer.MAX_VALUE;//loading Footer

    private LoadMoreImpl mLoadMoreManager;
    private OnRecyclerViewScrollBottomListener mScrollListener;

    private AdapterInterface mAdapterInterface;

    private RecyclerView mRecyclerView;

    private KeepFullSpanHelper mKeepFullSpanHelper;

    public static WrapperAdapter wrap(RecyclerView.Adapter adapter) {
        return new WrapperAdapter(adapter);
    }

    private WrapperAdapter(Adapter wrapped) {
        super(wrapped);

        mLoadMoreManager = new LoadMoreImpl();
        mScrollListener = new OnRecyclerViewScrollBottomListener() {
            @Override
            public void onBottom() {
                mLoadMoreManager.tryCallLoadMore();
            }
        };
        mKeepFullSpanHelper = new KeepFullSpanHelper();
    }

    private void initOnAttachedToRecyclerView() {

        mRecyclerView.removeOnScrollListener(mScrollListener);
        mRecyclerView.addOnScrollListener(mScrollListener);

        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();

        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            mKeepFullSpanHelper.mOriginSpanSizeLookup = gridLayoutManager.getSpanSizeLookup();
            mKeepFullSpanHelper.setFullSpanForGird(gridLayoutManager);
        }
    }

    @SuppressWarnings("unused")
    public void setLoadingTriggerThreshold(int loadingTriggerThreshold) {
        mScrollListener.setLoadingTriggerThreshold(loadingTriggerThreshold);
    }

    @SuppressWarnings("unused")
    public void setAdapterInterface(AdapterInterface lastVisibleItemPosition) {
        mAdapterInterface = lastVisibleItemPosition;
        mScrollListener.setLastVisibleItemPositionGetter(lastVisibleItemPosition);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == LOAD_MORE_FOOTER) {
            View loadMoreView = mLoadMoreManager.getLoadMoreView(parent);
            keepFullSpan(loadMoreView);
            return new ViewHolder(loadMoreView) {
            };
        } else {
            return super.onCreateViewHolder(parent, viewType);
        }
    }

    @SuppressWarnings("all")
    private void keepFullSpan(View itemView) {

        if (mRecyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {

            mKeepFullSpanHelper.setFullSpanForStaggered(itemView);

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

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (getItemViewType(position) != LOAD_MORE_FOOTER) {
            super.onBindViewHolder(holder, position);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payloads) {
        if (getItemViewType(position) != LOAD_MORE_FOOTER) {
            super.onBindViewHolder(holder, position, payloads);
        }
    }

    @Override
    public int getItemCount() {
        int count = super.getItemCount();
        return count == 0 ? count : count + 1;
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

    @SuppressWarnings("unchecked")
    @Override
    public void onViewRecycled(ViewHolder holder) {
        if (isLoadMoreOrStateViewHolder(holder)) {
            return;
        }
        super.onViewRecycled(holder);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean onFailedToRecycleView(ViewHolder holder) {
        return !(isLoadMoreOrStateViewHolder(holder)) && super.onFailedToRecycleView(holder);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        if (isLoadMoreOrStateViewHolder(holder)) {
            return;
        }
        super.onViewAttachedToWindow(holder);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        if (isLoadMoreOrStateViewHolder(holder)) {
            return;
        }
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
        initOnAttachedToRecyclerView();
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        recyclerView.removeOnScrollListener(mScrollListener);
        mKeepFullSpanHelper.cleanFullSpanIfNeed(recyclerView);
    }

    private boolean isLoadMoreOrStateViewHolder(ViewHolder viewHolder) {
        int itemViewType = viewHolder.getItemViewType();
        return itemViewType == LOAD_MORE_FOOTER;
    }

    ///////////////////////////////////////////////////////////////////////////
    // LoadMoreManager
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


}