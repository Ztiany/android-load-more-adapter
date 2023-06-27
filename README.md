# Android LoadMore Adapter 

## What is it?

WrapperAdapter can wrap your original adapter of RecyclerView to implement the load-more feature.

- There are two load-more modes: auto loading and click to load.
- Customizing the load-more view is allowed.
 
## Usage

see the code below:

```java
mLoadMoreAdapter = LoadMoreAdapter.wrap(mAdapter);
mLoadMoreAdapter.setLoadMode(LoadMode.CLICK_LOAD);

mLoadMoreAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {

     @Override
     public boolean canLoadMore() {
         return true;
     }

     @Override
     public void onLoadMore() {
          ...
     }

mRecyclerView.setAdapter(mLoadMoreAdapter);
```

## Installation

```groovy
implementation 'io.github.ztiany:android-loadmore-adapter:1.0.10'
```
