# WrapperAdapter

用于包装RecyclerView的Adapter，实现以下功能：

- 自动加载更多、点击加载更多
- 可配置 load more View
- 显示内容视图
- 显示loading视图
- 显示empty视图
- 显示error视图


# 使用方式


## 创建WrapperAdapter与控制器

```java

         mWrapperAdapter = WrapperAdapter.wrap(mAdapter, ture);
         mWrapperAdapter.setLoadMode(LoadMode.CLICK_LOAD);
         mRecyclerView.setAdapter(mWrapperAdapter);

```


# Gradle

        compile 'com.ztiany.android:WrapperAdapter:2.0.2'

# 效果

![](adapter.gif)
