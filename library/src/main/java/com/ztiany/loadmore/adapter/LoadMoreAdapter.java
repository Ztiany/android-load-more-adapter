package com.ztiany.loadmore.adapter;

import androidx.recyclerview.widget.RecyclerView;

public class LoadMoreAdapter extends WrapperAdapter {

    LoadMoreAdapter(RecyclerView.Adapter wrapped, boolean useScrollListener) {
        super(wrapped, useScrollListener);
    }

    public static LoadMoreAdapter wrap(RecyclerView.Adapter adapter) {
        return new LoadMoreAdapter(adapter, false);
    }

    public static LoadMoreAdapter wrap(RecyclerView.Adapter adapter, boolean useScrollListener) {
        return new LoadMoreAdapter(adapter, useScrollListener);
    }

}
