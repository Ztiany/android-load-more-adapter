package com.ztiany.loadmore.adapter;

import androidx.recyclerview.widget.RecyclerView;

public interface LastVisibleItemPositionFinder {

    /**
     * You should return the last visible item position in the RecyclerView.
     *
     * @return the last visible item position.
     */
    int find(RecyclerView recyclerView);

}
