package com.ztiany.loadmore;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ztiany.R;

/**
 * author Ztiany                                                                        <br/>
 * email 1169654504@qq.com & ztiany3@gmail.com           <br/>
 * date 2016-04-11 13:18                                                       <br/>
 * description                                                                             <br/>
 * version
 */
public class LoadMoreView extends FrameLayout implements ILoadMoreView {

    private static final String TAG = LoadMoreView.class.getSimpleName();

    private static final String mNoMoreMsg = "没有更多了";
    private static final String mFailMsg = "加载失败，点击重试";
    private static final String mClickLoadMsg = "点击加载更多";
    private static final String mLoadCompleted = "加载完成";


    private ProgressBar mProgressBar;
    private TextView mMsgTv;

    public LoadMoreView(Context context) {
        super(context);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext())
                .inflate(R.layout.list_footer, this, true);
        mMsgTv = (TextView) findViewById(R.id.item_msg_tv);
        mProgressBar = (ProgressBar) findViewById(R.id.item_pb);
    }


    @Override
    public void onLoading() {
        mProgressBar.setVisibility(VISIBLE);
        mMsgTv.setVisibility(GONE);
    }





    @Override
    public void onFail() {
        mProgressBar.setVisibility(GONE);
        mMsgTv.setVisibility(VISIBLE);
        mMsgTv.setText(getFailMsg());
    }

    @Override
    public void onCompleted(boolean hasMore) {
        if (!hasMore) {
            mProgressBar.setVisibility(GONE);
            mMsgTv.setVisibility(VISIBLE);
            mMsgTv.setText(getNoMoreMsg());
        } else {
            mProgressBar.setVisibility(GONE);
            mMsgTv.setVisibility(VISIBLE);
            mMsgTv.setText(getCompletedMsg());
        }
    }


    @Override
    public void onClickLoad() {
        mProgressBar.setVisibility(GONE);
        mMsgTv.setVisibility(VISIBLE);
        mMsgTv.setText(getClickLoadMsg());
    }

    private String getCompletedMsg() {
        return mLoadCompleted;
    }


    public String getNoMoreMsg() {
        return mNoMoreMsg;
    }



    public String getClickLoadMsg() {
        return mClickLoadMsg;
    }

    public String getFailMsg() {
        return mFailMsg;
    }

}
