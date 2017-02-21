package com.ztiany.loadmore;

import android.support.v7.widget.RecyclerView;
import android.view.View;


public interface ItemFullSpanProvider {

    /**
     * @param itemView RecyclerView itemView
     * @param rv RecyclerView
     * @param contentViewOrLoadMoreView true mean ContentView ,false mean LoadMoreView
     */
    void setItemFullSpan(View itemView, RecyclerView rv , boolean contentViewOrLoadMoreView);

}
