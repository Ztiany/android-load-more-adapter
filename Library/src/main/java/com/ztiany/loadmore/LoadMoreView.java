package com.ztiany.loadmore;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ztiany.wrapperadapter.R;


/**
 * author Ztiany                                                                        <br/>
 * email 1169654504@qq.com & ztiany3@gmail.com           <br/>
 * date 2016-04-11 13:18                                                       <br/>
 * description                                                                             <br/>
 * version
 */
public class LoadMoreView extends FrameLayout implements ILoadMoreView {

    private String mNoMoreMsg = "";
    private String mFailMsg = "";
    private String mClickLoadMsg = "";
    private String mLoadCompleted = "";


    private ProgressBar mProgressBar;
    private TextView mMsgTv;

    public LoadMoreView(Context context) {
        super(context);
        init();
    }

    private void init() {

        LayoutInflater.from(getContext())
                .inflate(R.layout.wrapper_adapter_list_footer, this, true);
        mMsgTv = (TextView) findViewById(R.id.wrapper_adapter_id_item_msg_tv);
        mProgressBar = (ProgressBar) findViewById(R.id.wrapper_adapter_id_item_pb);

        mNoMoreMsg = getContext().getString(R.string.adapter_no_more_message);
        mFailMsg = getContext().getString(R.string.adapter_load_more_fail);
        mClickLoadMsg = getContext().getString(R.string.adapter_click_load_more);
        mLoadCompleted = getContext().getString(R.string.adapter_load_completed);
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
