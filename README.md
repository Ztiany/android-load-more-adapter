# LoadMoreAdapter [![](https://jitpack.io/v/Ztiany/WrapperAdapter.svg)](https://jitpack.io/#Ztiany/WrapperAdapter)

用于包装 RecyclerView.Adapter 的 Adapter，实现加载更多功能：

- 自动加载更多、点击加载更多
- 可配置 load more View
- 兼容自定义的 LayoutManager
 
## Usage

创建 WrapperAdapter 与控制器：

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
          ...
     }
     
mRecyclerView.setAdapter(mWrapperAdapter);
```

## Gradle

```
maven { url 'https://jitpack.io' }

implementation 'com.github.Ztiany:WrapperAdapter:3.0.7'
```

androidx

```
maven { url 'https://jitpack.io' }

implementation 'com.github.Ztiany:WrapperAdapter:4.1.2'
```
