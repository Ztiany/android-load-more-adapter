package com.ztiany.loadmore.adapter;

import androidx.recyclerview.widget.RecyclerView;

/**
 * You must take the opportunities(one of them) this interface provides to keep the full span of the load more view holder.
 */
public interface FullSpanKeeper {

    /**
     * Called when the load more view holder created.
     *
     * @param viewHolder   load more  itemView
     * @param recyclerView RecyclerView
     */
    void onViewHolderCreated(RecyclerView.ViewHolder viewHolder, RecyclerView recyclerView);

    /**
     * Called when the adapter attached to the RecyclerView.
     *
     * @param recyclerView RecyclerView
     */
    void onAttachedToRecyclerView(RecyclerView recyclerView);

}
