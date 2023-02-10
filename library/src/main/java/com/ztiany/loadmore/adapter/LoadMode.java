package com.ztiany.loadmore.adapter;


import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef(value = {
        LoadMode.CLICK_LOAD,
        LoadMode.AUTO_LOAD
})
@Retention(RetentionPolicy.SOURCE)
public @interface LoadMode {

    int CLICK_LOAD = 19901005;
    int AUTO_LOAD = 19910707;

}
