package com.ztiany.loadmore;

import android.view.View;
import android.view.ViewGroup;

/**
 * Author Ztiany                   <br/>
 * Email ztiany3@gmail.com      <br/>
 * Date 2016-04-11 22:15      <br/>
 * Description：
 */
public interface StateViewFactory {

    View onCreateEmptyView(ViewGroup parent);

    View onCreateLoadingView(ViewGroup parent);

    View onCreateFailView(ViewGroup parent);

}
