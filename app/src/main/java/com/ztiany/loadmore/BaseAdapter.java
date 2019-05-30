package com.ztiany.loadmore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 作者：湛添友
 * 时间：2015-05-11 22:38
 * 描述：RecyclerView 的适配器
 */
public abstract class BaseAdapter<T, VH extends ViewHolder> extends RecyclerView.Adapter<VH> {

    List<T> mData;
    LayoutInflater mLayoutInflater;

    BaseAdapter(Context context, List<T> data) {
        this.mData = data;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public BaseAdapter(Context context) {
        this(context, null);
    }

    public List<T> getData() {
        return mData;
    }

    /**
     * 添加一条数据，这里需要注意的是，如果使用type来实现多个条目。在notify的时候，可能需要写此方法
     */
    @UiThread
    public void addItem(T t) {
        if (t == null) {
            return;
        }
        if (mData != null) {
            int lastIndex = mData.size();
            mData.add(t);
            notifyItemInserted(lastIndex);
        } else {
            mData = new ArrayList<>();
            mData.add(t);
            notifyDataSetChanged();
        }
    }

    /**
     * 添加数据集合，这里需要注意的是，如果使用type来实现多个条目。在notify的时候，可能需要写此方法
     */
    @UiThread
    void addAll(List<T> data) {
        if (data == null || data.isEmpty()) {
            return;
        }
        if (this.mData != null) {
            int lastIndex = getDataSize();
            this.mData.addAll(data);
            notifyItemRangeInserted(lastIndex, data.size());
        } else {
            this.mData = data;
            notifyDataSetChanged();
        }
    }


    @UiThread
    public void setData(List<T> data) {
        this.mData = data;
        notifyDataSetChanged();
    }


    @UiThread
    public boolean removeItem(int position) {
        if (isEmpty()) {
            return false;
        }
        if (position < getDataSize()) {
            mData.remove(position);
            notifyItemRemoved(position);
            return true;
        }
        return false;
    }


    @UiThread
    public boolean clear() {
        if (mData != null) {
            mData.clear();
            mData = null;
            notifyDataSetChanged();
            return true;
        }
        return false;
    }

    public T getItem(int position) {
        if (position >= 0 && position < getDataSize()) {
            return mData.get(position);
        }
        return null;
    }


    @Override
    public int getItemCount() {
        return getDataSize();
    }

    public boolean isEmpty() {
        return getDataSize() == 0;
    }

    private int getDataSize() {
        return mData == null ? 0 : mData.size();
    }


    @NonNull
    @Override
    public abstract VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType);

    @Override
    public abstract void onBindViewHolder(@NonNull VH viewHolder, int position);

}