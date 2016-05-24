package com.ztiany.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.ztiany.loadmore.ItemFullSpanProvider;

/**
 * Author Ztiany                   <br/>
 * Email ztiany3@gmail.com      <br/>
 * Date 2016-04-11 21:06      <br/>
 * Description：
 */
class StateImpl implements StateManager {

    private static final int STATE_CONTENT = 30001;
    private static final int STATE_LOADING = 30002;
    private static final int STATE_FAIL = 30003;
    private static final int STATE_EMPTY = 30004;

    private ItemFullSpanProvider mItemFullSpanProvider;

    private int mCurrentState = STATE_CONTENT;

    private final static int VIEW_FAIL = Integer.MAX_VALUE - 3;
    private final static int VIEW_LOADING = Integer.MAX_VALUE - 4;
    private final static int VIEW_EMPTY = Integer.MAX_VALUE - 5;

    private RecyclerView.Adapter mWrapperAdapter;


    private StateViewFactory mStateViewFactory;

    GridLayoutManager mGridLayoutManager;
    GridLayoutManager.SpanSizeLookup mSpanSizeLookup;

    StateImpl(RecyclerView.Adapter wrapperAdapter) {
        mWrapperAdapter = wrapperAdapter;
    }


    RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {

        if (type == VIEW_EMPTY) {
            View view = mStateViewFactory.onCreateEmptyView(viewGroup);
            keepFullSpan(view, (RecyclerView) viewGroup);
            return new RecyclerView.ViewHolder(view) {
            };
        }

        if (type == VIEW_FAIL) {
            View view = mStateViewFactory.onCreateFailView(viewGroup);
            keepFullSpan(view, (RecyclerView) viewGroup);

            return new RecyclerView.ViewHolder(view) {
            };
        }

        if (type == VIEW_LOADING) {
            View view = mStateViewFactory.onCreateLoadingView(viewGroup);
            keepFullSpan(view, (RecyclerView) viewGroup);

            return new RecyclerView.ViewHolder(view) {
            };
        }
        throw new IllegalStateException("call StateImpl onCreateViewHolder type illegal ,type = " + type);
    }

    boolean needProcess() {
        return mCurrentState != STATE_CONTENT && mStateViewFactory != null;
    }

    int getCount() {
        return 1;
    }

    int getType(int position) {
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
        mWrapperAdapter.notifyDataSetChanged();
    }


    public void setItemFullSpanProvider(ItemFullSpanProvider itemFullSpanProvider) {
        mItemFullSpanProvider = itemFullSpanProvider;
    }

    @Override
    public void fail() {
        if (mCurrentState == STATE_FAIL) {
            return;
        }
        mCurrentState = STATE_FAIL;
        mWrapperAdapter.notifyDataSetChanged();
    }

    @Override
    public void empty() {
        if (mCurrentState == STATE_EMPTY) {
            return;
        }
        mCurrentState = STATE_EMPTY;
        mWrapperAdapter.notifyDataSetChanged();

    }

    @Override
    public void loading() {
        if (mCurrentState == STATE_LOADING) {
            return;
        }
        mCurrentState = STATE_LOADING;
        mWrapperAdapter.notifyDataSetChanged();
    }

    @Override
    public void setStateViewFactory(StateViewFactory stateViewFactory) {
        mStateViewFactory = stateViewFactory;
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }


    void keepFullSpan(View view, RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            KeepFullSpanUtils.setFullSpanForGird((GridLayoutManager) layoutManager, mSpanSizeLookup);
        } else if (layoutManager instanceof LinearLayoutManager) {
            KeepFullSpanUtils.setFullSpanForLinear(view, true);
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            KeepFullSpanUtils.setFullSpanForStaggered(view, true);
        }else {
            if (mItemFullSpanProvider != null) {
                mItemFullSpanProvider.setItemFullSpan(view, recyclerView);
            }else {
                throw new NullPointerException("you need set com.ztiany.loadmore.ItemFullSpanProvider when you use custom layoutManager");
            }
        }
    }

    public boolean isStateViewType(int itemViewType) {
        return VIEW_EMPTY == itemViewType
                || VIEW_FAIL == itemViewType
                || VIEW_LOADING == itemViewType;
    }

}
