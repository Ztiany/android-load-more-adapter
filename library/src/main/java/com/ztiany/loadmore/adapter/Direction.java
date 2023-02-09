package com.ztiany.loadmore.adapter;

import androidx.annotation.IntDef;

@IntDef({
        Direction.UP,
        Direction.DOWN
})
@interface Direction {

    /**
     * scroll upwards
     */
    int UP = 1;

    /**
     * scroll downwards
     */
    int DOWN = 2;

}
