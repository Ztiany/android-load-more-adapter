package com.ztiany.adapter;


interface StateManager {

    void content();

    void fail();

    void empty();

    void loading();

    void setStateViewFactory(StateViewFactory stateViewFactory);
}
