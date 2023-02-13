package com.ztiany.loadmore.adapter;

import androidx.recyclerview.widget.RecyclerView;

public interface LastVisibleItemPositionFinder {

    int getLastVisibleItemPosition(RecyclerView recyclerView);

}
