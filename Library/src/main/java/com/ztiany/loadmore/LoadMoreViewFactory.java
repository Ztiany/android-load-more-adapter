package com.ztiany.loadmore;

import android.view.View;
import android.view.ViewGroup;

/**
 * Author Ztiany                   <br/>
 * Email ztiany3@gmail.com      <br/>
 * Date 2016-04-11 21:58      <br/>
 * Description：
 */
public interface LoadMoreViewFactory {
    View onCreateLoadMoreView(ViewGroup parent);
}
