package com.ztiany.loadmore;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * Author Ztiany      <br/>
 * Email ztiany3@gmail.com      <br/>
 * Date 2016-03-19 20:02      <br/>
 * Description：
 */
public abstract class ViewHolder<T> extends RecyclerView.ViewHolder {

    public ViewHolder(View itemView) {
        super(itemView);
        findViews();
    }

    protected abstract void findViews();

    public abstract void bindData(T data);

    protected Context getContext() {
        return itemView.getContext();
    }

    public <T extends View> T findView(@IdRes int viewId) {
        @SuppressWarnings("unchecked")//需要什么类型，就返回什么类型
                T view = (T) itemView.findViewById(viewId);
        return view;
    }
}
