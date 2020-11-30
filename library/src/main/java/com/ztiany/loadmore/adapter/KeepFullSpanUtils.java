package com.ztiany.loadmore.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

final class KeepFullSpanUtils {

    private InnerSpanSizeLookup mInnerSpanSizeLookup;

    GridLayoutManager.SpanSizeLookup mOriginSpanSizeLookup;

    KeepFullSpanUtils() {
    }

    void cleanFullSpanIfNeed(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager.SpanSizeLookup spanSizeLookup = ((GridLayoutManager) layoutManager).getSpanSizeLookup();
            if (spanSizeLookup == mInnerSpanSizeLookup) {
                ((GridLayoutManager) layoutManager).setSpanSizeLookup(mOriginSpanSizeLookup);
            }
        }
    }

    void setFullSpanForStaggered(View loadMoreView) {
        ViewGroup.LayoutParams layoutParams = loadMoreView.getLayoutParams();
        if (!(layoutParams instanceof StaggeredGridLayoutManager.LayoutParams)) {
            layoutParams = new StaggeredGridLayoutManager.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            ((StaggeredGridLayoutManager.LayoutParams) layoutParams).setFullSpan(true);
            loadMoreView.setLayoutParams(layoutParams);

        } else {
            StaggeredGridLayoutManager.LayoutParams slp = (StaggeredGridLayoutManager.LayoutParams) layoutParams;
            if (!slp.isFullSpan() || slp.height == ViewGroup.LayoutParams.MATCH_PARENT) {
                slp.setFullSpan(true);
                slp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                loadMoreView.setLayoutParams(layoutParams);
            }
        }
    }

    void setFullSpanForGird(GridLayoutManager gridLayoutManager) {
        GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();
        if (mOriginSpanSizeLookup == spanSizeLookup) {//first in
            if (mInnerSpanSizeLookup == null) {
                mInnerSpanSizeLookup = new InnerSpanSizeLookup(mOriginSpanSizeLookup, gridLayoutManager);
            }
            gridLayoutManager.setSpanSizeLookup(mInnerSpanSizeLookup);
            return;
        }
        if (mInnerSpanSizeLookup != spanSizeLookup) {//spanSizeLookup is new
            mOriginSpanSizeLookup = spanSizeLookup;
            mInnerSpanSizeLookup = new InnerSpanSizeLookup(mOriginSpanSizeLookup, gridLayoutManager);
            gridLayoutManager.setSpanSizeLookup(mInnerSpanSizeLookup);
        }
    }

    private static class InnerSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

        private GridLayoutManager.SpanSizeLookup mOriginSpanSizeLookup;
        private GridLayoutManager mGridLayoutManager;
        private final int mSpanCount;

        InnerSpanSizeLookup(GridLayoutManager.SpanSizeLookup originSpanSizeLookup, GridLayoutManager gridLayoutManager) {
            mOriginSpanSizeLookup = originSpanSizeLookup;
            mGridLayoutManager = gridLayoutManager;
            mSpanCount = mGridLayoutManager.getSpanCount();
        }

        @Override
        public int getSpanSize(int position) {
            if (position == mGridLayoutManager.getItemCount() - 1) {
                return mSpanCount;
            }
            return mOriginSpanSizeLookup.getSpanSize(position);
        }
    }

}
