package com.ztiany.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.ztiany.loadmore.ILoadMoreView;
import com.ztiany.loadmore.LoadMode;
import com.ztiany.loadmore.LoadMoreManager;
import com.ztiany.loadmore.LoadMoreView;
import com.ztiany.loadmore.LoadMoreViewFactory;
import com.ztiany.loadmore.OnLoadMoreListener;

/**
 * author Ztiany                                                                        <br/>
 * email 1169654504@qq.com & ztiany3@gmail.com           <br/>
 * date 2016-04-11 12:44                                                         <br/>
 * description                                                                             <br/>
 * version
 */
class LoadMoreImpl implements LoadMoreManager {

    private View mLoadMoreView;
    private boolean mHasMore = true;//是否还有更多
    private OnLoadMoreListener mOnLoadMoreListener;//加载更多监听
    private LoadMoreViewFactory mLoadMoreViewFactory;
    private WrapperAdapter mWrapperAdapter;
    LoadMoreImpl(WrapperAdapter wrapperAdapter) {
        mWrapperAdapter = wrapperAdapter;
    }

    private final static int STATUS_NONE = 0;
    private final static int STATUS_LOADING = 1;
    private final static int STATUS_FAIL = 2;
    private final static int STATUS_COMPLETE = 3;
    private final static int STATUS_PRE = 5;//如果是点击加载，需要一个准备状态

    private int mCurrentStatus = STATUS_NONE;


    private
    @LoadMode
    int mLoadMode = LoadMode.AUTO_LOAD;//是否自动加载更多


    void tryCallLoadMore() {

        if (mOnLoadMoreListener == null || !mOnLoadMoreListener.canLoadMore()) {
            return;
        }
        if (mCurrentStatus == STATUS_LOADING) {
            return;
        }

        if (isAutoLoad()) {

            mCurrentStatus = STATUS_PRE;
            callLoadMore();

        } else {
            if (mCurrentStatus != STATUS_FAIL) {
                mCurrentStatus = STATUS_PRE;
                LoadMoreViewCaller.callShowClickLoad(mLoadMoreView);
            }
        }
    }

    View getLoadMoreView(ViewGroup parent) {
        if (mLoadMoreViewFactory == null) {
            createDefaultLoadMoreView(parent);
        } else {
            mLoadMoreView = mLoadMoreViewFactory.onCreateLoadMoreView(parent);
            if (mLoadMoreView == null) {
                throw new NullPointerException("LoadMoreViewFactory :" + mLoadMoreViewFactory + " call onCreateLoadMoreView return null");
            }
        }
        mLoadMoreView.setOnClickListener(new ClickListener());

        if (mLoadMode == LoadMode.CLICK_LOAD) {

            switch (mCurrentStatus) {
                case STATUS_PRE:
                case STATUS_NONE: {
                    LoadMoreViewCaller.callShowClickLoad(mLoadMoreView);
                    break;
                }
                case STATUS_LOADING:{
                    LoadMoreViewCaller.callLoading(mLoadMoreView);
                    break;
                }
                case STATUS_FAIL: {
                    LoadMoreViewCaller.callFail(mLoadMoreView);
                    break;
                }
                case STATUS_COMPLETE: {
                    LoadMoreViewCaller.callCompleted(mLoadMoreView, mHasMore);
                    break;
                }
            }
        } else {

            switch (mCurrentStatus) {
                case STATUS_PRE:
                case STATUS_NONE: {
                    LoadMoreViewCaller.callLoading(mLoadMoreView);
                    break;
                }
                case STATUS_LOADING: {
                    LoadMoreViewCaller.callLoading(mLoadMoreView);
                    break;
                }
                case STATUS_FAIL: {
                    LoadMoreViewCaller.callFail(mLoadMoreView);
                    break;
                }
                case STATUS_COMPLETE: {
                    LoadMoreViewCaller.callCompleted(mLoadMoreView, mHasMore);
                    break;
                }

            }
        }
        return mLoadMoreView;
    }

    private void createDefaultLoadMoreView(ViewGroup parent) {
        mLoadMoreView = new LoadMoreView(parent.getContext());
    }


    @Override
    public void loadFail() {
        mCurrentStatus = STATUS_FAIL;
        LoadMoreViewCaller.callFail(mLoadMoreView);

    }


    @Override
    public void loadCompleted(final boolean hasMore) {
        mHasMore = hasMore;
        mCurrentStatus = STATUS_COMPLETE;
        LoadMoreViewCaller.callCompleted(mLoadMoreView, mHasMore);
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

    @Override
    public void setEnableLoadMore(boolean enableLoadMore) {
        mWrapperAdapter.enableLoadMore(enableLoadMore);
    }


    private boolean isAutoLoad() {
        return mLoadMode == LoadMode.AUTO_LOAD;
    }


    private class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            if (mLoadMode == LoadMode.AUTO_LOAD) {
                if ((mCurrentStatus == STATUS_FAIL)) {//自动加载更多只有错误才能点击
                    callLoadMore();
                }
            } else if (mLoadMode == LoadMode.CLICK_LOAD) {//点击加载更多只有暂停和准备状态才能点击和
                if (mCurrentStatus == STATUS_PRE || mCurrentStatus == STATUS_FAIL) {
                    callLoadMore();
                }
            }
        }
    }


    @Override
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener;
    }


    private void callLoadMore() {
        if (mCurrentStatus != STATUS_LOADING && mOnLoadMoreListener != null && mHasMore) {

            LoadMoreViewCaller.callLoading(mLoadMoreView);
            mCurrentStatus = STATUS_LOADING;
            mOnLoadMoreListener.onLoadMore();
        }
    }


    /**
     * Method Caller
     */
    private static class LoadMoreViewCaller {


        static void callLoading(View view) {
            if (view != null && view instanceof ILoadMoreView) {
                ((ILoadMoreView) view).onLoading();
            }
        }

        static void callCompleted(View view, boolean hasMore) {
            if (view != null && view instanceof ILoadMoreView) {
                ((ILoadMoreView) view).onCompleted(hasMore);
            }
        }

        static void callShowClickLoad(View view) {
            if (view != null && view instanceof ILoadMoreView) {
                ((ILoadMoreView) view).onClickLoad();
            }
        }


        static void callFail(View view) {
            if (view != null && view instanceof ILoadMoreView) {
                ((ILoadMoreView) view).onFail();
            }
        }
    }
}
