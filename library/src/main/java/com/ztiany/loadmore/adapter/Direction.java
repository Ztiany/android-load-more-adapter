package com.ztiany.loadmore.adapter;

import androidx.annotation.IntDef;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-11-30 15:04
 */
@IntDef({
        Direction.UP,
        Direction.DOWN
})
@interface Direction {
    int UP = 1;
    int DOWN = 2;
}
