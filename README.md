# WrapperAdapter

用于包装 RecyclerView.Adapter 的 Adapter，实现加载更多功能：

- 自动加载更多、点击加载更多
- 可配置 load more View
- 兼容自定义的 LayoutManager

# 使用方式


## 创建WrapperAdapter与控制器

```java
              mWrapperAdapter = WrapperAdapter.wrap(mAdapter);
              mWrapperAdapter.setLoadMode(LoadMode.CLICK_LOAD);
              mRecyclerView.setAdapter(mWrapperAdapter);
              mWrapperAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                     @Override
                     public boolean canLoadMore() {
                         return true;
                     }
         
                     @Override
                     public void onLoadMore() {
                          ...
                     }
                     
              mRecyclerView.setAdapter(mWrapperAdapter);
```


# Gradle

```
        compile 'com.ztiany.android:WrapperAdapter:3.0.1'
```
