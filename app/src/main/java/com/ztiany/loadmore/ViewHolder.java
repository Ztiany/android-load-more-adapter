package com.ztiany.loadmore;

import android.content.Context;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Author Ztiany      <br/>
 * Email ztiany3@gmail.com      <br/>
 * Date 2016-03-19 20:02      <br/>
 * Description：
 */
public abstract class ViewHolder<T> extends RecyclerView.ViewHolder {

    ViewHolder(View itemView) {
        super(itemView);
        findViews();
    }

    protected abstract void findViews();

    public abstract void bindData(T data);

    protected Context getContext() {
        return itemView.getContext();
    }

    <V extends View> V findView(@IdRes int viewId) {
        @SuppressWarnings("unchecked")//需要什么类型，就返回什么类型
                V view = itemView.findViewById(viewId);
        return view;
    }

}