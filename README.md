# WrapperAdapter

用于包装RecyclerView的Adapter，实现LoadMore功能：

- 自动加载更多、点击加载更多
- 可配置 load more View
- 兼容自定义的LayoutManager

# 使用方式


## 创建WrapperAdapter与控制器

```java
              mWrapperAdapter = WrapperAdapter.wrap(mAdapter);
              mWrapperAdapter.setLoadMode(LoadMode.CLICK_LOAD);
         
              mWrapperAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                     @Override
                     public boolean canLoadMore() {
                         return true;
                     }
         
                     @Override
                     public void onLoadMore() {
                                doLoadMore();
                     }
                     
               mRecyclerView.setAdapter(mWrapperAdapter);
```


# Gradle

        compile 'com.ztiany.android:WrapperAdapter:3.0.1'
 
