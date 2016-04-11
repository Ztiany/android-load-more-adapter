package com.ztiany.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

/**
 * Author Ztiany                   <br/>
 * Email ztiany3@gmail.com      <br/>
 * Date 2016-04-11 21:08      <br/>
 * Description：
 */
class LoadMoreViewFuller {

    static void setFullSpanForStaggered(View loadMoreView , boolean matchParent) {
        StaggeredGridLayoutManager.LayoutParams layoutParams = new StaggeredGridLayoutManager.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, matchParent?ViewGroup.LayoutParams.MATCH_PARENT:ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setFullSpan(true);
        loadMoreView.setLayoutParams(layoutParams);
    }


    static void setFullSpanForGird(final int itemCount, GridLayoutManager gridLayoutManager) {
        final int spanCount = gridLayoutManager.getSpanCount();
        final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == itemCount - 1) {
                    return spanCount;
                }
                return spanSizeLookup.getSpanSize(position);
            }
        });
    }


    public static void setFullSpanForLinear(View loadMoreView,boolean matchParent) {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, matchParent?ViewGroup.LayoutParams.MATCH_PARENT:ViewGroup.LayoutParams.WRAP_CONTENT);
        loadMoreView.setLayoutParams(layoutParams);
    }
}
