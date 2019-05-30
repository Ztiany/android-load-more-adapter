package com.ztiany.loadmore.adapter;

import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class RecyclerViewAdapterWrapper extends RecyclerView.Adapter {

    private final RecyclerView.Adapter wrapped;

    RecyclerViewAdapterWrapper(RecyclerView.Adapter wrapped) {
        super();
        this.wrapped = wrapped;
        this.wrapped.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            public void onChanged() {
                notifyDataSetChanged();
            }

            public void onItemRangeChanged(int positionStart, int itemCount) {
                notifyItemRangeChanged(positionStart, itemCount);
            }

            public void onItemRangeInserted(int positionStart, int itemCount) {
                notifyItemRangeInserted(positionStart, itemCount);
            }

            public void onItemRangeRemoved(int positionStart, int itemCount) {
                notifyItemRangeRemoved(positionStart, itemCount);
            }

            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                notifyItemMoved(fromPosition, toPosition);
            }
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return wrapped.onCreateViewHolder(parent, viewType);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        wrapped.onBindViewHolder(holder, position);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List payloads) {
        wrapped.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public int getItemCount() {
        return wrapped.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        return wrapped.getItemViewType(position);
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        wrapped.setHasStableIds(hasStableIds);
    }

    @Override
    public long getItemId(int position) {
        return wrapped.getItemId(position);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        wrapped.onViewRecycled(holder);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean onFailedToRecycleView(@NonNull RecyclerView.ViewHolder holder) {
        return wrapped.onFailedToRecycleView(holder);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        wrapped.onViewAttachedToWindow(holder);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        wrapped.onViewDetachedFromWindow(holder);
    }

    @Override
    public void registerAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
        wrapped.registerAdapterDataObserver(observer);
    }

    @Override
    public void unregisterAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
        wrapped.unregisterAdapterDataObserver(observer);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        wrapped.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        wrapped.onDetachedFromRecyclerView(recyclerView);
    }

    public RecyclerView.Adapter getWrappedAdapter() {
        return wrapped;
    }

} 