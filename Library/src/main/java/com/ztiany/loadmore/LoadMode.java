package com.ztiany.loadmore;

import android.support.annotation.IntDef;

/**
 * author Ztiany                                                                        <br/>
 * email 1169654504@qq.com & ztiany3@gmail.com           <br/>
 * date 2016-04-11 11:37                                                       <br/>
 * description                                                                             <br/>
 * version
 */
@IntDef(value = {LoadMode.CLICK_LOAD, LoadMode.AUTO_LOAD})
public @interface LoadMode {

    int CLICK_LOAD = 19901005;
    int AUTO_LOAD = 19910707;

}
