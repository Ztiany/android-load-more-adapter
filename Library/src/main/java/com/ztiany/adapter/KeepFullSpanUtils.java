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
class KeepFullSpanUtils {

    static void setFullSpanForStaggered(View loadMoreView, boolean matchParent) {

        ViewGroup.LayoutParams layoutParams = loadMoreView.getLayoutParams();
        if (layoutParams == null || !(layoutParams instanceof StaggeredGridLayoutManager.LayoutParams)) {
            layoutParams = new StaggeredGridLayoutManager.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, matchParent ? ViewGroup.LayoutParams.MATCH_PARENT : ViewGroup.LayoutParams.WRAP_CONTENT);
            ((StaggeredGridLayoutManager.LayoutParams) layoutParams).setFullSpan(true);
            loadMoreView.setLayoutParams(layoutParams);

        } else {
            StaggeredGridLayoutManager.LayoutParams slp = (StaggeredGridLayoutManager.LayoutParams) layoutParams;

            if (matchParent) {
                if (!slp.isFullSpan() || slp.height != ViewGroup.LayoutParams.MATCH_PARENT) {
                    slp.setFullSpan(true);
                    slp.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    loadMoreView.setLayoutParams(layoutParams);
                }
            } else {
                if (!slp.isFullSpan() || slp.height == ViewGroup.LayoutParams.MATCH_PARENT) {
                    slp.setFullSpan(true);
                    slp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    loadMoreView.setLayoutParams(layoutParams);
                }
            }
        }
    }


    static void setFullSpanForGird(final GridLayoutManager gridLayoutManager, final GridLayoutManager.SpanSizeLookup spanSizeLookup) {
        final int spanCount = gridLayoutManager.getSpanCount();
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == gridLayoutManager.getItemCount() - 1) {
                    return spanCount;
                }
                return spanSizeLookup.getSpanSize(position);
            }
        });
    }


    public static void setFullSpanForLinear(View loadMoreView, boolean heightMatchParent) {
        ViewGroup.LayoutParams lp = loadMoreView.getLayoutParams();
        if (lp == null) {
            lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, heightMatchParent ? ViewGroup.LayoutParams.MATCH_PARENT : ViewGroup.LayoutParams.WRAP_CONTENT);
            loadMoreView.setLayoutParams(lp);
        } else {
            if (heightMatchParent) {

                if (lp.height != ViewGroup.LayoutParams.MATCH_PARENT) {
                    lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    loadMoreView.setLayoutParams(lp);
                }

            } else {

                if (lp.height == ViewGroup.LayoutParams.MATCH_PARENT) {
                    lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    loadMoreView.setLayoutParams(lp);
                }

            }
        }
    }
}
