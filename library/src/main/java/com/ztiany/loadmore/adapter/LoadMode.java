package com.ztiany.loadmore.adapter;

import android.support.annotation.IntDef;


@IntDef(value = {LoadMode.CLICK_LOAD, LoadMode.AUTO_LOAD})
public @interface LoadMode {

    int CLICK_LOAD = 19901005;
    int AUTO_LOAD = 19910707;

}
