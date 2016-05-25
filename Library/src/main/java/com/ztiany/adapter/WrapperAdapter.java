package com.ztiany.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.ztiany.loadmore.ItemFullSpanProvider;
import com.ztiany.loadmore.LoadMoreManager;
import com.ztiany.loadmore.OnRecyclerViewScrollBottomListener;

import static android.support.v7.widget.RecyclerView.Adapter;
import static android.support.v7.widget.RecyclerView.ViewHolder;

/**
 * @author rockerhieu on 7/6/15.
 */
public class WrapperAdapter extends RecyclerViewAdapterWrapper {

    private static final String TAG = WrapperAdapter.class.getSimpleName();

    protected static final int LOAD_MORE_FOOTER = Integer.MAX_VALUE;//loading Footer
    private LoadMoreImpl mLoadMore;
    private StateImpl mState;

    private ItemFullSpanProvider itemFullSpanProvider;

    private OnRecyclerViewScrollBottomListener mScrollListener;


    @LayoutType
    private int mLayoutType;


    public WrapperAdapter(Adapter wrapped) {
        super(wrapped);
        mLoadMore = new LoadMoreImpl();
        mState = new StateImpl(getWrappedAdapter());
        mScrollListener = new OnRecyclerViewScrollBottomListener() {
            @Override
            public void onBottom() {
                mLoadMore.tryCallLoadMore();
            }
        };
    }


    public void setLoadingTriggerThreshold(int loadingTriggerThreshold) {
        mScrollListener.setLoadingTriggerThreshold(loadingTriggerThreshold);
    }

    public void setAdapterInterface(AdapterInterface lastVisibleItemPosition) {
        itemFullSpanProvider = lastVisibleItemPosition;
        mScrollListener.setLastVisibleItemPositionGetter(lastVisibleItemPosition);

        mState.setItemFullSpanProvider(lastVisibleItemPosition);

    }

    public LoadMoreManager getLoadMoreManager() {
        return mLoadMore;
    }

    public StateManager getStateManager() {
        return mState;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //需要显示一场布局
        if (mState.needProcess()) {
            return mState.onCreateViewHolder(parent, viewType);
        }

        if (viewType == LOAD_MORE_FOOTER) {//是自己的loadingView，交给自己处理
            return onCreateLoadMoreViewHolder(parent, viewType);
        } else {
            return super.onCreateViewHolder(parent, viewType);
        }
    }


    private ViewHolder onCreateLoadMoreViewHolder(ViewGroup parent, int viewType) {
        View loadMoreView = mLoadMore.getLoadMoreView(parent);

        if (mLayoutType == LayoutType.STAGGERED) {

            KeepFullSpanUtils.setFullSpanForStaggered(loadMoreView, false);

        } else if (mLayoutType == LayoutType.LINEAR) {

            KeepFullSpanUtils.setFullSpanForLinear(loadMoreView, false);

        } else if (mLayoutType == LayoutType.OTHER) {
            if (itemFullSpanProvider != null) {
                itemFullSpanProvider.setItemFullSpan(loadMoreView, (RecyclerView) parent, false);
            } else {
                throw new NullPointerException("you need set com.ztiany.loadmore.ItemFullSpanProvider when you use custom layoutManager");
            }
        }
        return new ViewHolder(loadMoreView) {
        };
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mState.needProcess()) {
            mState.onBindViewHolder(holder, position);
            return;
        }

        if (getItemViewType(position) == LOAD_MORE_FOOTER) {
            onBindLoadMoreViewHolder(holder, position);
        } else {
            super.onBindViewHolder(holder, position);
        }
    }

    protected void onBindLoadMoreViewHolder(ViewHolder holder, int position) {
        //Do Nothing
    }


    @Override
    public int getItemCount() {
        if (mState.needProcess()) {
            return mState.getCount();
        }
        int count = super.getItemCount();
        return count == 0 ? count : count + 1;
    }


    @Override
    public int getItemViewType(int position) {

        if (mState.needProcess()) {
            return mState.getType(position);
        }

        if (isLastPosition(position)) {
            return LOAD_MORE_FOOTER;
        }

        return super.getItemViewType(position);
    }

    private boolean isLastPosition(int position) {
        return position == super.getItemCount();
    }


    @Override
    public long getItemId(int position) {
        if (!isLastPosition(position)) {
            return super.getItemId(position);
        }
        return position;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onViewRecycled(ViewHolder holder) {
        if (mState.needProcess() || isLoadMoreOrStateViewHolder(holder)) {

            return;
        }
        super.onViewRecycled(holder);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean onFailedToRecycleView(ViewHolder holder) {
        if (mState.needProcess() || isLoadMoreOrStateViewHolder(holder)) {

            return true;
        }
        return super.onFailedToRecycleView(holder);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        if (mState.needProcess() || isLoadMoreOrStateViewHolder(holder)) {

            return;
        }
        super.onViewAttachedToWindow(holder);

    }

    @SuppressWarnings("unchecked")
    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        if (mState.needProcess() || isLoadMoreOrStateViewHolder(holder)) {

            return;
        }
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        initOnAttachedToRecyclerView(recyclerView);
        super.onAttachedToRecyclerView(recyclerView);
    }

    private void initOnAttachedToRecyclerView(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(mScrollListener);
        if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            mLayoutType = LayoutType.STAGGERED;
        } else if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {

            mLayoutType = LayoutType.GRID;
            GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
            GridLayoutManager.SpanSizeLookup spanSizeLookup = layoutManager.getSpanSizeLookup();
            mState.mSpanSizeLookup = spanSizeLookup;
            mState.mGridLayoutManager = layoutManager;
            KeepFullSpanUtils.setFullSpanForGird(layoutManager, spanSizeLookup);

        } else if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            mLayoutType = LayoutType.LINEAR;
        } else {
            mLayoutType = LayoutType.OTHER;
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        recyclerView.removeOnScrollListener(mScrollListener);
        super.onDetachedFromRecyclerView(recyclerView);
    }

    private boolean isLoadMoreOrStateViewHolder(ViewHolder viewHolder) {
        int itemViewType = viewHolder.getItemViewType();
        return itemViewType == LOAD_MORE_FOOTER
                || mState.isStateViewType(itemViewType);
    }


}