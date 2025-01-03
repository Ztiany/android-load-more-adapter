package com.ztiany.loadmore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * @author Ztiany
 */
public class DefaultLoadMoreView extends FrameLayout implements LoadMoreView {

    private final View mProgressBar;

    private final TextView mMessageTv;

    public DefaultLoadMoreView(Context context) {
        super(context);
        LayoutInflater.from(getContext()).inflate(R.layout.wrapper_adapter_list_material_footer, this, true);
        mMessageTv = findViewById(R.id.wrapper_adapter_id_item_msg_tv);
        mProgressBar = findViewById(R.id.wrapper_adapter_id_item_pb);
    }

    @Override
    public void onLoading() {
        mProgressBar.setVisibility(VISIBLE);
        mMessageTv.setVisibility(GONE);
    }

    @Override
    public void onFailed() {
        mProgressBar.setVisibility(GONE);
        mMessageTv.setVisibility(VISIBLE);
        mMessageTv.setText(R.string.adapter_load_more_fail);
    }

    @Override
    public void onCompleted(boolean hasMore) {
        if (!hasMore) {
            mProgressBar.setVisibility(GONE);
            mMessageTv.setVisibility(VISIBLE);
            mMessageTv.setText(R.string.adapter_no_more_message);
        } else {
            mProgressBar.setVisibility(GONE);
            mMessageTv.setVisibility(VISIBLE);
            mMessageTv.setText(R.string.adapter_load_completed);
        }
    }

    @Override
    public void showClickToLoadMore() {
        mProgressBar.setVisibility(GONE);
        mMessageTv.setVisibility(VISIBLE);
        mMessageTv.setText(R.string.adapter_click_load_more);
    }

}