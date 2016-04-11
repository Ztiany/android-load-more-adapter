package com.ztiany.state;

import com.ztiany.loadmore.StateViewFactory;

/**
 * author Ztiany                                                                        <br/>
 * email 1169654504@qq.com & ztiany3@gmail.com           <br/>
 * date 2016-04-11 11:59                                                       <br/>
 * description                                                                             <br/>
 * version
 */
public interface IState {

    void content();

    void fail();

    void empty();

    void loading();


    void setStateViewFactory(StateViewFactory stateViewFactory);
}
