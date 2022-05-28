# LoadMoreAdapter 

## What is it?

Wrap the adapter of RecyclerView and implement the load-more feature.

- two load-more modes: auto loading and click to load.
- customizing the loading more view is allowed.
 
## Usage

see the code below.

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

## Installation

```groovy
implementation 'io.github.ztiany:loadmore-adapter:4.5.0'
```
