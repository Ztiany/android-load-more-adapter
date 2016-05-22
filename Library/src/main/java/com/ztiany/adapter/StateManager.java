package com.ztiany.adapter;

/**
 * author Ztiany                                                                        <br/>
 * email 1169654504@qq.com & ztiany3@gmail.com           <br/>
 * date 2016-04-11 11:59                                                       <br/>
 * description                                                                             <br/>
 * version
 */
public interface StateManager {

    void content();

    void fail();

    void empty();

    void loading();


    void setStateViewFactory(StateViewFactory stateViewFactory);
}
