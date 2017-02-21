package com.ztiany.loadmore;

import android.support.v7.widget.RecyclerView;


public interface LastVisibleItemPositionProvider {

    int getLastVisibleItemPosition(RecyclerView.LayoutManager layoutManager);

}
