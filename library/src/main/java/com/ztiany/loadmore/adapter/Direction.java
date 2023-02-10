package com.ztiany.loadmore.adapter;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({
        Direction.UP,
        Direction.DOWN
})
@Retention(RetentionPolicy.SOURCE)
public @interface Direction {

    /**
     * scroll upwards
     */
    int UP = 1;

    /**
     * scroll downwards
     */
    int DOWN = 2;

}
