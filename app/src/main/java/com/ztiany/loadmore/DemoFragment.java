package com.ztiany.loadmore;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ztiany.loadmore.adapter.LoadMode;
import com.ztiany.loadmore.adapter.OnLoadMoreListener;
import com.ztiany.loadmore.adapter.LoadMoreAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


public class DemoFragment extends BaseLayoutFragment {

    private BaseAdapter<String, ViewHolder<String>> mRecyclerAdapter;
    private boolean mHasMore = true;
    private boolean mAddNewHasMore = true;
    private boolean mIsFail;
    private int count = 20;

    private List<String> mData;
    protected LoadMoreAdapter mLoadMoreAdapter;
    protected RecyclerView mRecyclerView;
    protected SwipeRefreshLayout mRefreshLayout;

    private static final String LAYOUT_TYPE = "layout_type";
    private static final String CLICK_LOAD_MORE = "isClickLoadMore";
    private static final String AUTO_HIDDEN_MORE = "auto_hidden_more";

    public static DemoFragment newInstance(int layoutType, boolean isClickLoadMore, int visibilityWhenNoMore) {
        Bundle args = new Bundle();
        args.putInt(LAYOUT_TYPE, layoutType);
        args.putBoolean(CLICK_LOAD_MORE, isClickLoadMore);
        args.putInt(AUTO_HIDDEN_MORE, visibilityWhenNoMore);
        DemoFragment fragment = new DemoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView.LayoutManager createLayoutManager() {
        Bundle arguments = getArguments();
        assert arguments != null;
        int type = getArguments().getInt(LAYOUT_TYPE);
        if (type == 1) {
            return new LinearLayoutManager(getContext());
        } else if (type == 2) {
            return new GridLayoutManager(getContext(), 3);
        } else {
            return new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        }
    }

    public void onButtonClick(View v) {
        PopupMenu pop = new PopupMenu(requireContext(), v);
        Menu menu = pop.getMenu();
        menu.add(Menu.NONE, 1, 0, "next time fail");
        menu.add(Menu.NONE, 2, 1, "next time no more");
        menu.add(Menu.NONE, 3, 2, "next time add new no more");
        menu.add(Menu.NONE, 4, 3, "next time normal");
        menu.add(Menu.NONE, 5, 4, "stop auto load when failed");
        menu.add(Menu.NONE, 6, 5, "enable auto load when failed");
        pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                switch (itemId) {
                    case 1: {
                        mIsFail = true;
                        break;
                    }
                    case 2: {
                        mIsFail = false;
                        mHasMore = false;
                        break;
                    }
                    case 3: {
                        mIsFail = false;
                        mHasMore = true;
                        mAddNewHasMore = false;
                        break;
                    }
                    case 4: {
                        mIsFail = false;
                        mHasMore = true;
                        mAddNewHasMore = true;
                        mLoadMoreAdapter.loadCompleted(true);
                        break;
                    }
                    case 5: {
                        mLoadMoreAdapter.stopAutoLoadWhenFailed(true);
                    }
                    case 6: {
                        mLoadMoreAdapter.stopAutoLoadWhenFailed(true);
                    }
                }
                return true;
            }
        });
        pop.setGravity(Gravity.CENTER);
        pop.show();
    }

    @Override
    protected int provideLayoutRes() {
        return R.layout.fragment_recycler_load;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.frag_show_option).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClick(v);
            }
        });
        mRecyclerView = view.findViewById(R.id.fragment_recycler_rv);
        mRefreshLayout = view.findViewById(R.id.fragment_recycler_ptr);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mData = new ArrayList<>();
        initAdapter();

        mRecyclerView.setLayoutManager(createLayoutManager());
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(
                    @NonNull Rect outRect, @NonNull
                    View view,
                    @NonNull RecyclerView parent,
                    @NonNull RecyclerView.State state
            ) {
                outRect.bottom = DensityUtils.dip2px(getContext(), 10);
                super.getItemOffsets(outRect, view, parent, state);
            }
        });

        mLoadMoreAdapter = LoadMoreAdapter.wrap(mRecyclerAdapter, true);
        mRecyclerView.setAdapter(mLoadMoreAdapter);

        Bundle arguments = getArguments();
        assert arguments != null;

        if (arguments.getBoolean(CLICK_LOAD_MORE)) {
            mLoadMoreAdapter.setLoadMode(LoadMode.CLICK_LOAD);
        }

        mLoadMoreAdapter.setVisibilityWhenNoMore(getArguments().getInt(AUTO_HIDDEN_MORE, View.VISIBLE));

        setOnLoadMoreListener();

        mRefreshLayout.setOnRefreshListener(() -> {
            if (mLoadMoreAdapter.isLoadingMore()) {
                mRefreshLayout.setRefreshing(false);
                return;
            }

            mRefreshLayout.postDelayed(() -> {
                mData.clear();
                for (int i = 0; i < 20; i++) {
                    mData.add("我是Item " + i);
                }
                count = 20;
                mRecyclerAdapter.notifyDataSetChanged();
                mRefreshLayout.setRefreshing(false);
                mLoadMoreAdapter.loadCompleted(mHasMore);
                Toast.makeText(getContext(), "刷新完毕", Toast.LENGTH_SHORT).show();
            }, 1500);
        });
    }

    private void setOnLoadMoreListener() {
        mLoadMoreAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public boolean canLoadMore() {
                return true;
            }

            @Override
            public void onLoadMore() {

                mRefreshLayout.postDelayed(() -> {
                    if (mIsFail) {
                        mLoadMoreAdapter.loadFail();
                        return;
                    }

                    if (!mHasMore) {
                        mLoadMoreAdapter.loadCompleted(false);
                        return;
                    }

                    List<String> newData = new ArrayList<>();
                    for (int i = 0; i < 20; i++) {
                        newData.add("新来的Item" + count++);
                    }
                    mRecyclerAdapter.addAll(newData);
                    mLoadMoreAdapter.loadCompleted(mAddNewHasMore);
                }, 100);

            }
        });
    }

    private void initAdapter() {
        mRecyclerAdapter = new BaseAdapter<String, ViewHolder<String>>(getContext(), mData) {
            @NonNull
            @Override
            public ViewHolder<String> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View inflate = mLayoutInflater.inflate(R.layout.item, parent, false);
                return new ViewHolder<String>(inflate) {
                    private TextView mTextView;

                    @Override
                    protected void findViews() {
                        mTextView = findView(R.id.item_tv);
                    }

                    @Override
                    public void bindData(final String data) {
                        mTextView.setOnClickListener(v -> Toast.makeText(getContext(), data, Toast.LENGTH_SHORT).show());
                        onBindData(mTextView, data);
                    }
                };
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder<String> viewHolder, final int position) {
                viewHolder.bindData(mData.get(position));
            }
        };
    }

    protected void onBindData(TextView textView, String data) {
        textView.setText(data);
    }

}