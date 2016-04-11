package com.ztiany.loadmore;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * @author Ztiany
 *         email 1169654504@qq.com & ztiany3@gmail.com
 *         date 2015-12-16 12:50
 *         description
 *         vsersion
 */
public abstract class BaseLayoutFragment extends Fragment {

    private static final String TAG = BaseLayoutFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layoutId = provideLayoutRes();
        if (layoutId > 0) {
            View view = inflater.inflate(provideLayoutRes(), container, false);

            ButterKnife.bind(this, view);
                Log.d(TAG, getClass().getSimpleName() + "-->onCreateView:"+view);
            return view;
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }



    @LayoutRes
    protected abstract int provideLayoutRes();


    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }
}
