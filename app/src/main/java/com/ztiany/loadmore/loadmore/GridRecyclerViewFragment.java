package com.ztiany.loadmore.loadmore;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * @author Ztiany
 *         email 1169654504@qq.com & ztiany3@gmail.com
 *         date 2015-12-18 11:18
 *         description
 *         vsersion
 */
public class GridRecyclerViewFragment extends BaseDemoFragment {


    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        GridLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), 2);

        return linearLayoutManager;
    }
}
