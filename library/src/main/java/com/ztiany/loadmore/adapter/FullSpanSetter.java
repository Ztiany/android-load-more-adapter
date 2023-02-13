package com.ztiany.loadmore.adapter;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public interface FullSpanSetter {

    /**
     * @param itemView     load more  itemView
     * @param recyclerView RecyclerView
     */
    void setItemFullSpan(View itemView, RecyclerView recyclerView);

}
