package com.ztiany.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.ztiany.loadmore.ILoadMore;
import com.ztiany.state.IState;

import static android.support.v7.widget.RecyclerView.Adapter;
import static android.support.v7.widget.RecyclerView.AdapterDataObserver;
import static android.support.v7.widget.RecyclerView.ViewHolder;

/**
 * @author rockerhieu on 7/6/15.
 */
public class WrapperAdapter extends Adapter {

    private static final String TAG = WrapperAdapter.class.getSimpleName();
    protected static final int LOAD_MORE_FOOTER = Integer.MAX_VALUE;
    private RecyclerView.Adapter mWrappedAdapter;
    private LoadMoreImpl mLoadMore;
    private StateImpl mState;
    private OnLoadMoreScrollListener mScrollListener;
    private boolean mIsStaggeredLayout;
    private boolean mIsLinearLayout;

    public WrapperAdapter(Adapter wrapped) {
        super();
        this.mWrappedAdapter = wrapped;
        setDataObservable();
        mLoadMore = new LoadMoreImpl();
        mState = new StateImpl(mWrappedAdapter);
        mScrollListener = new OnLoadMoreScrollListener() {
            @Override
            public void onBottom(RecyclerView recyclerView) {
                mLoadMore.tryCallLoadMore();
            }
        };
    }


    public ILoadMore getLoadMoreManager() {
        return mLoadMore;
    }

    public IState getStateManager() {
        return mState;
    }


    private void setDataObservable() {

        this.mWrappedAdapter.registerAdapterDataObserver(new AdapterDataObserver() {
            public void onChanged() {
                notifyDataSetChanged();
            }

            public void onItemRangeChanged(int positionStart, int itemCount) {
                notifyItemRangeChanged(positionStart, itemCount);
            }

            public void onItemRangeInserted(int positionStart, int itemCount) {
                notifyItemRangeInserted(positionStart, itemCount);
            }

            public void onItemRangeRemoved(int positionStart, int itemCount) {
                notifyItemRangeRemoved(positionStart, itemCount);
            }

            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                notifyItemMoved(fromPosition, toPosition);
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mState.intercept()) {
            return mState.onCreateViewHolder(parent, viewType);
        }

        if (viewType == LOAD_MORE_FOOTER) {
            return onCreateLoadMoreViewHolder(parent, viewType);
        } else {
            return mWrappedAdapter.onCreateViewHolder(parent, viewType);
        }
    }


    private ViewHolder onCreateLoadMoreViewHolder(ViewGroup parent, int viewType) {
        View loadMoreView = mLoadMore.getLoadMoreView(parent);
        if (mIsStaggeredLayout) {
            LoadMoreViewFuller.setFullSpanForStaggered(loadMoreView, false);
        } else if (mIsLinearLayout) {
            LoadMoreViewFuller.setFullSpanForLinear(loadMoreView, false);
        }
        return new ViewHolder(loadMoreView) {
        };
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mState.intercept()) {
            mState.onBindViewHolder(holder, position);
            return;
        }

        if (getItemViewType(position) == LOAD_MORE_FOOTER) {
            onBindLoadMoreViewHolder();
        } else {
            mWrappedAdapter.onBindViewHolder(holder, position);
        }
    }

    private void onBindLoadMoreViewHolder() {

    }

    @Override
    public int getItemCount() {
        if (mState.intercept()) {
            return mState.getCount();
        }
        return mWrappedAdapter.getItemCount() + 1;
    }


    @Override
    public int getItemViewType(int position) {

        if (mState.intercept()) {
            return mState.getType(position);
        }

        if (isLastPosition(position)) {
            return LOAD_MORE_FOOTER;
        }
        return mWrappedAdapter.getItemViewType(position);
    }

    private boolean isLastPosition(int position) {
        return position == mWrappedAdapter.getItemCount();
    }


    @Override
    public void setHasStableIds(boolean hasStableIds) {
        mWrappedAdapter.setHasStableIds(hasStableIds);
    }

    @Override
    public long getItemId(int position) {

        if (!isLastPosition(position))
            return mWrappedAdapter.getItemId(position);

        return position;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onViewRecycled(ViewHolder holder) {
        if (mState.intercept()) {

            return;
        }
        if (!isLoadMoreViewHolder(holder))
            mWrappedAdapter.onViewRecycled(holder);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean onFailedToRecycleView(ViewHolder holder) {
        if (mState.intercept() || isLoadMoreViewHolder(holder)) {

            return true;
        }
        return mWrappedAdapter.onFailedToRecycleView(holder);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        if (mState.intercept()) {

            return;
        }

        if (!isLoadMoreViewHolder(holder))
            mWrappedAdapter.onViewAttachedToWindow(holder);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        if (mState.intercept()) {

            return;
        }

        if (!isLoadMoreViewHolder(holder))
            mWrappedAdapter.onViewDetachedFromWindow(holder);
    }

    @Override
    public void registerAdapterDataObserver(AdapterDataObserver observer) {
        mWrappedAdapter.registerAdapterDataObserver(observer);
    }

    @Override
    public void unregisterAdapterDataObserver(AdapterDataObserver observer) {
        mWrappedAdapter.unregisterAdapterDataObserver(observer);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        initOnAttachedToRecyclerView(recyclerView);
        mWrappedAdapter.onAttachedToRecyclerView(recyclerView);
    }

    private void initOnAttachedToRecyclerView(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(mScrollListener);
        if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            mIsStaggeredLayout = true;
        } else if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            LoadMoreViewFuller.setFullSpanForGird(getItemCount(), ((GridLayoutManager) recyclerView.getLayoutManager()));
        } else if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            mIsLinearLayout = true;
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        recyclerView.removeOnScrollListener(mScrollListener);
        mWrappedAdapter.onDetachedFromRecyclerView(recyclerView);
    }

    private boolean isLoadMoreViewHolder(ViewHolder viewHolder) {
        return viewHolder.getItemViewType() == LOAD_MORE_FOOTER;
    }


} 