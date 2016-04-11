# WrapperAdapter

用于包装RecyclerView的Adapter，实现以下功能：
- 完全无侵入
- 自动加载更多
- 点击加载更多(可配置)
- 可配置load more View
- 显示内容
- 显示loading视图
- 显示empty视图
- 显示error视图

>还存在一些问题，但是已经到深夜了，睡觉了，有问题请跟我交流-_- !

# 使用方式


## 创建WrapperAdapter与控制器

```java

      WrapperAdapter wrapperAdapter = new WrapperAdapter(mRecyclerAdapter);
      mRecyclerView.setAdapter(wrapperAdapter);
      mLoaderManager = wrapperAdapter.getLoadMoreManager();//获取loadMore控制
      mStateManager = wrapperAdapter.getStateManager();//获取内容控制
```


## 根据需求设置

```java
//提供各种状态布局

mStateManager.setStateViewFactory(new StateViewFactory() {
            @Override
            public View onCreateEmptyView(ViewGroup parent) {
                return LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_empty, parent, false);
            }

            @Override
            public View onCreateLoadingView(ViewGroup parent) {
                return LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_loading, parent, false);

            }

            @Override
            public View onCreateFailView(ViewGroup parent) {
                return LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_error, parent, false);

            }
        });

//设置各种loadMore状态

public interface ILoadMore {

    void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener);



    void loadFail();


    void loadCompleted(boolean hasMore);

    void pause(boolean pause);

    boolean isLoadingMore();


    void setLoadMode(@Mode int loadMore);


    void setLoadMoreViewFactory(LoadMoreViewFactory factory);

}


```



# 效果
![](adapter.gif)
