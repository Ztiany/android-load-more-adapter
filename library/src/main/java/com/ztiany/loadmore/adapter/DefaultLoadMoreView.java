package com.ztiany.loadmore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


/**
 * @author Ztiany
 */
public class DefaultLoadMoreView extends FrameLayout implements LoadMoreView {

    private String mNoMoreMsg = "";
    private String mFailMsg = "";
    private String mClickLoadMsg = "";
    private String mLoadCompleted = "";

    private View mProgressBar;
    private TextView mMsgTv;


    public DefaultLoadMoreView(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (LoadMoreConfig.isHasMaterialLib()) {
            LayoutInflater.from(getContext()).inflate(R.layout.wrapper_adapter_list_material_footer, this, true);
        } else {
            LayoutInflater.from(getContext()).inflate(R.layout.wrapper_adapter_list_footer, this, true);
        }
        mMsgTv = findViewById(R.id.wrapper_adapter_id_item_msg_tv);
        mProgressBar = findViewById(R.id.wrapper_adapter_id_item_pb);

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
    public void onFailed() {
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
    public void showClickToLoadMore() {
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
