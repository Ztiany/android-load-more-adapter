package com.ztiany.loadmore.adapter;

public class LoadMoreConfig {

    private static LoadMoreViewFactory sLoadMoreViewFactory;

    private static long sMinLoadMoreInterval = 1000;

    private static boolean sStopAutoLoadWhenFailed = false;

    private static LastVisibleItemPositionFinder sLastVisibleItemPositionFinder = null;

    private static FullSpanSetter sFullSpanSetter = null;

    private static int sLoadMode = LoadMode.AUTO_LOAD;

    private static Boolean sHasMaterialLib = null;

    public static void setMinLoadMoreInterval(long minLoadMoreInterval) {
        sMinLoadMoreInterval = minLoadMoreInterval;
    }

    public static void setDefaultLoadMoreViewFactory(LoadMoreViewFactory loadMoreViewFactory) {
        sLoadMoreViewFactory = loadMoreViewFactory;
    }

    public static void setStopAutoLoadWhenFailed(boolean stopAutoLoadWhenFailed) {
        sStopAutoLoadWhenFailed = stopAutoLoadWhenFailed;
    }

    public static LoadMoreViewFactory getLoadMoreViewFactory() {
        return sLoadMoreViewFactory;
    }

    public static long getMinLoadMoreInterval() {
        return sMinLoadMoreInterval;
    }

    public static boolean isStopAutoLoadWhenFailed() {
        return sStopAutoLoadWhenFailed;
    }


    public static int getLoadMode() {
        return sLoadMode;
    }

    public static void setLoadMode(int loadMode) {
        sLoadMode = loadMode;
    }

    public static LastVisibleItemPositionFinder getLastVisibleItemPositionFinder() {
        return sLastVisibleItemPositionFinder;
    }

    public static void setLastVisibleItemPositionFinder(LastVisibleItemPositionFinder lastVisibleItemPositionFinder) {
        sLastVisibleItemPositionFinder = lastVisibleItemPositionFinder;
    }

    public static FullSpanSetter getFullSpanSetter() {
        return sFullSpanSetter;
    }

    public static void setFullSpanSetter(FullSpanSetter fullSpanSetter) {
        sFullSpanSetter = fullSpanSetter;
    }

    public static boolean isHasMaterialLib() {
        if (sHasMaterialLib != null) {
            return sHasMaterialLib;
        }

        try {
            Class.forName("com.google.android.material.progressindicator.CircularProgressIndicator");
            sHasMaterialLib = true;
        } catch (ClassNotFoundException e) {
            sHasMaterialLib = false;
        }

        return sHasMaterialLib;
    }

}