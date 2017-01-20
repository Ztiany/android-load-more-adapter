package com.ztiany.adapter;

import android.support.annotation.IntDef;


@IntDef(value = {
        LayoutType.GRID,
        LayoutType.LINEAR,
        LayoutType.STAGGERED,
        LayoutType.OTHER
})
@interface LayoutType {
    int LINEAR = 10001;
    int GRID = 10002;
    int STAGGERED = 10003;
    int OTHER = 10004;

}
