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

      WrapperAdapter wrapperAdapter = new WrapperAdapter(mRecyclerAdapter);
      mRecyclerView.setAdapter(wrapperAdapter);
      mLoaderManager = wrapperAdapter.getLoadMoreManager();//获取loadMore控制
      mStateManager = wrapperAdapter.getStateManager();//获取内容控制

        public interface StateManager {

            void content();

            void fail();

            void empty();

            void loading();

            void setStateViewFactory(StateViewFactory stateViewFactory);
        }

        public interface ILoadMoreView {

            void onLoading();

            void onFail();

            void onCompleted(boolean hasMore);

            void onClickLoad();
        }


```


# Gradle

        compile 'com.ztiany.android:WrapperAdapter:1.0.2'

# 效果

![](adapter.gif)
