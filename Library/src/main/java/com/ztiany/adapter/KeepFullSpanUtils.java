package com.ztiany.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.GridLayoutManager.SpanSizeLookup;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.ztiany.loadmore.ItemFullSpanProvider;


final class KeepFullSpanUtils {


    static void keepFullSpan(View itemView, RecyclerView recyclerView, boolean fullHeight, SpanSizeLookup spanSizeLookup, ItemFullSpanProvider itemFullSpanProvider) {

        if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {

            KeepFullSpanUtils.setFullSpanForStaggered(itemView, fullHeight);

        } else if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {

            KeepFullSpanUtils.setFullSpanForGird((GridLayoutManager) recyclerView.getLayoutManager(), spanSizeLookup);

        } else if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            KeepFullSpanUtils.setFullSpanForLinear(itemView, fullHeight);

        } else {

            if (itemFullSpanProvider != null) {
                itemFullSpanProvider.setItemFullSpan(itemView, recyclerView, fullHeight);
            } else {
                throw new NullPointerException("you need set com.ztiany.loadmore.ItemFullSpanProvider when you use custom layoutManager");
            }

        }
    }


    private static void setFullSpanForStaggered(View loadMoreView, boolean matchParent) {

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


    private static void setFullSpanForGird(GridLayoutManager gridLayoutManager, final SpanSizeLookup originSizeLookup) {
        SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();
        if (!(originSizeLookup instanceof InnerSpanSizeLookup)) {
            gridLayoutManager.setSpanSizeLookup(new InnerSpanSizeLookup(spanSizeLookup, gridLayoutManager));
        }
    }


    private static void setFullSpanForLinear(View loadMoreView, boolean heightMatchParent) {
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

    private static class InnerSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

        private SpanSizeLookup mSpanSizeLookup;
        private GridLayoutManager mGridLayoutManager;

        InnerSpanSizeLookup(SpanSizeLookup spanSizeLookup, GridLayoutManager gridLayoutManager) {
            mSpanSizeLookup = spanSizeLookup;
            mGridLayoutManager = gridLayoutManager;
        }

        @Override
        public int getSpanSize(int position) {
            if (position == mGridLayoutManager.getItemCount() - 1) {
                return mGridLayoutManager.getSpanCount();
            }
            return mSpanSizeLookup.getSpanSize(position);
        }
    }
}
