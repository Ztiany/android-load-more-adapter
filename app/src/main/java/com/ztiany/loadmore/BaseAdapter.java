package com.ztiany.loadmore;

import android.content.Context;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：湛添友
 * 时间：2015-05-11 22:38
 * 描述：RecyclerView 的适配器
 */
public abstract class BaseAdapter<T, VH extends ViewHolder> extends RecyclerView.Adapter<VH> {

    protected List<T> mData;
    protected Context mContext;
    protected LayoutInflater mLayoutInflater;

    public BaseAdapter(Context context, List<T> data) {
        this.mData = data;
        this.mContext = context;
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
     *
     * @param t
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
     *
     * @param data
     */
    @UiThread
    public void addAll(List<T> data) {
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

    public int getDataSize() {
        return mData == null ? 0 : mData.size();
    }


    @Override
    public abstract VH onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public abstract void onBindViewHolder(VH viewHolder, int position);


}

