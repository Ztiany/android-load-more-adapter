package com.ztiany.loadmore;

import android.support.v7.widget.RecyclerView;
import android.view.View;


public interface ItemFullSpanProvider {

    /**
     * @param itemView state or load more  itemView
     * @param rv RecyclerView
     */
    void setItemFullSpan(View itemView, RecyclerView rv , boolean fullHeight);

}
