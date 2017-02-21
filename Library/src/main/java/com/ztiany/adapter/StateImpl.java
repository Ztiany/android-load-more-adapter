package com.ztiany.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

class StateImpl implements StateManager {

    private static final int STATE_CONTENT = 30001;
    private static final int STATE_LOADING = 30002;
    private static final int STATE_FAIL = 30003;
    private static final int STATE_EMPTY = 30004;

    private int mCurrentState = STATE_CONTENT;

    private final static int VIEW_FAIL = Integer.MAX_VALUE - 3;
    private final static int VIEW_LOADING = Integer.MAX_VALUE - 4;
    private final static int VIEW_EMPTY = Integer.MAX_VALUE - 5;

    private StateViewFactory mStateViewFactory;
    private RecyclerView.Adapter mWrappedAdapter;
    private WrapperAdapter mWrapperAdapter;

    StateImpl(WrapperAdapter wrapperAdapter, RecyclerView.Adapter wrappedAdapter) {
        mWrappedAdapter = wrappedAdapter;
        mWrapperAdapter = wrapperAdapter;
    }


    RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
        RecyclerView.ViewHolder viewHolder = null;
        if (type == VIEW_EMPTY) {
            viewHolder = new RecyclerView.ViewHolder(mStateViewFactory.onCreateEmptyView(viewGroup)) {
            };
        }
        if (type == VIEW_FAIL) {
            viewHolder = new RecyclerView.ViewHolder(mStateViewFactory.onCreateFailView(viewGroup)) {
            };
        }
        if (type == VIEW_LOADING) {
            viewHolder = new RecyclerView.ViewHolder(mStateViewFactory.onCreateLoadingView(viewGroup)) {
            };
        }

        if (viewHolder != null) {
            return viewHolder;
        }

        throw new IllegalStateException("call StateImpl onCreateViewHolder type illegal ,type = " + type);
    }


    boolean needProcess() {
        return mCurrentState != STATE_CONTENT && mStateViewFactory != null;
    }

    int getCount() {
        return 1;
    }

    int getType(@SuppressWarnings("unused") int position) {
        if (mCurrentState == STATE_LOADING) {
            return VIEW_LOADING;
        } else if (mCurrentState == STATE_EMPTY) {
            return VIEW_EMPTY;
        } else if (mCurrentState == STATE_FAIL) {
            return VIEW_FAIL;
        }
        throw new IllegalStateException("STATE_CONTENT call StateImpl get type");
    }

    @Override
    public void content() {
        if (mCurrentState == STATE_CONTENT) {
            return;
        }
        mCurrentState = STATE_CONTENT;
        mWrappedAdapter.notifyDataSetChanged();
    }

    @Override
    public void fail() {
        if (mCurrentState == STATE_FAIL) {
            return;
        }
        mCurrentState = STATE_FAIL;
        mWrappedAdapter.notifyDataSetChanged();
    }

    @Override
    public void empty() {
        if (mCurrentState == STATE_EMPTY) {
            return;
        }
        mCurrentState = STATE_EMPTY;
        mWrappedAdapter.notifyDataSetChanged();
    }

    @Override
    public void loading() {
        if (mCurrentState == STATE_LOADING) {
            return;
        }
        mCurrentState = STATE_LOADING;
        mWrappedAdapter.notifyDataSetChanged();
    }

    @Override
    public void setStateViewFactory(StateViewFactory stateViewFactory) {
        mStateViewFactory = stateViewFactory;
    }

    void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressWarnings("unused") int position) {

        KeepFullSpanUtils.keepFullSpan(
                holder.itemView,
                mWrapperAdapter.mRecyclerView,
                true,
                mWrapperAdapter.mSpanSizeLookup,
                mWrapperAdapter.mItemFullSpanProvider
        );

    }

    boolean isNotContentStateViewType(int itemViewType) {
        return VIEW_EMPTY == itemViewType
                || VIEW_FAIL == itemViewType
                || VIEW_LOADING == itemViewType;
    }

}
