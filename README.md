# Android LoadMore Aadapter 

## What is it?

WrapperAdapter can wrap your original adapter of RecyclerView to implement the load-more feature.

- There are two load-more modes: auto loading and click to load.
- Customizing the load-more view is allowed.
 
## Usage

see the code below:

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
implementation 'io.github.ztiany:android-loadmore-adapter:1.0.1'
```
