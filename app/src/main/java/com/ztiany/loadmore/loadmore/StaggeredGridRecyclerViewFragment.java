package com.ztiany.loadmore.loadmore;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.TextView;

import com.ztiany.loadmore.LoadMoreManager;
import com.ztiany.loadmore.LoadMode;

import java.util.Random;

/**
 * @author Ztiany
 *         email 1169654504@qq.com & ztiany3@gmail.com
 *         date 2015-12-18 11:18
 *         description
 *         vsersion
 */
public class StaggeredGridRecyclerViewFragment extends BaseDemoFragment {
    private Random mRandom = new Random();


    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    }


    @Override
    protected void onBindData(TextView textView, String data) {
        super.onBindData(textView, data + mRandom.nextInt()+mRandom.nextDouble());
    }


    @Override
    protected void onCreateLoaderManager(LoadMoreManager loaderManager) {
        loaderManager.setLoadMode(LoadMode.CLICK_LOAD);

    }
}
