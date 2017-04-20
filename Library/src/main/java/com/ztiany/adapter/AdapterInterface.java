package com.ztiany.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public interface AdapterInterface {

    int getLastVisibleItemPosition(RecyclerView recyclerView);

    /**
     * @param itemView load more  itemView
     * @param rv       RecyclerView
     */
    void setItemFullSpan(View itemView, RecyclerView rv);
}
