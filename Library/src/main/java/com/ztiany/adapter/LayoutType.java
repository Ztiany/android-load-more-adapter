package com.ztiany.adapter;

import android.support.annotation.IntDef;

/**
 * Author Ztiany                   <br/>
 * Email ztiany3@gmail.com      <br/>
 * Date 2016-05-22-0022 20:13      <br/>
 * Description：
 */
@IntDef(value = {
        LayoutType.GRID,
        LayoutType.LINEAR,
        LayoutType.STAGGERED,
        LayoutType.OTHER
})
public @interface LayoutType {
    int LINEAR = 10001;
    int GRID = 10002;
    int STAGGERED = 10003;
    int OTHER = 10004;

}
