package com.ztiany.adapter;

import android.view.View;
import android.view.ViewGroup;


public interface StateViewFactory {

    View onCreateEmptyView(ViewGroup parent);

    View onCreateLoadingView(ViewGroup parent);

    View onCreateFailView(ViewGroup parent);

}
