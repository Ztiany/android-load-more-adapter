package com.ztiany.loadmore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @author Ztiany
 *         email 1169654504@qq.com & ztiany3@gmail.com
 *         date 2015-12-16 12:50
 *         description
 *         vsersion
 */
public abstract class BaseLayoutFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layoutId = provideLayoutRes();
        if (layoutId > 0) {
            return inflater.inflate(provideLayoutRes(), container, false);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @LayoutRes
    protected abstract int provideLayoutRes();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
