package com.ztiany.loadmore.adapter;


import android.os.Handler;
import android.os.Looper;

final class Executor {

    private final static Handler HANDLER = new Handler(Looper.getMainLooper());

    static void runOnUIThread(Runnable runnable) {
        if(Looper.myLooper() == Looper.getMainLooper()){
            runnable.run();
            return;
        }
        HANDLER.post(runnable);
    }

    static void runOnUIThreadDelayed(Runnable runnable, long delayMillis) {
        HANDLER.postDelayed(runnable, delayMillis);
    }

}