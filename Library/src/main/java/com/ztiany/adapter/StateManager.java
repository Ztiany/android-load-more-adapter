package com.ztiany.adapter;


public interface StateManager {

    void content();

    void fail();

    void empty();

    void loading();

    void setStateViewFactory(StateViewFactory stateViewFactory);
}
