package com.ztiany.loadmore;

import android.support.v7.widget.RecyclerView;

/**
 * Author Ztiany                   <br/>
 * Email ztiany3@gmail.com      <br/>
 * Date 2016-05-23-0023 1:44      <br/>
 * Description：
 */
public interface LastVisibleItemPosition {

    int getLastVisibleItemPosition(RecyclerView.LayoutManager layoutManager);
}
