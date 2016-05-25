package com.ztiany.loadmore;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Author Ztiany                   <br/>
 * Email ztiany3@gmail.com      <br/>
 * Date 2016-05-23-0023 1:44      <br/>
 * Description：
 */
public interface ItemFullSpanProvider {


    /**
     *
     * @param itemView RecyclerView itemView
     * @param rv RecyclerView
     * @param contentViewOrLoadMoreView true mean ContentView ,false mean LoadMoreView
     */
    void setItemFullSpan(View itemView, RecyclerView rv , boolean contentViewOrLoadMoreView);
}
