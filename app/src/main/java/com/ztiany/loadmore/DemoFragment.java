package com.ztiany.loadmore;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ztiany.adapter.LoadMode;
import com.ztiany.adapter.OnLoadMoreListener;
import com.ztiany.adapter.WrapperAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class DemoFragment extends BaseLayoutFragment {

    private BaseAdapter<String, ViewHolder<String>> mRecyclerAdapter;
    private boolean mHasMore = true;
    private boolean mIsFail;
    private int count = 20;

    private List<String> mData;
    protected WrapperAdapter mWrapperAdapter;
    protected RecyclerView mRecyclerView;
    protected PtrClassicFrameLayout mPtrClassicFrameLayout;

    private static final String LAYOUT_TYPE = "layout_type";
    private static final String CLICK_LOAD_MORE = "isClickLoadMore";
    private static final String AUTO_HIDDEN_MORE = "auto_hidden_more";

    public static DemoFragment newInstance(int layoutType, boolean isClickLoadMore, boolean autoHidden) {
        Bundle args = new Bundle();
        args.putInt(LAYOUT_TYPE, layoutType);
        args.putBoolean(CLICK_LOAD_MORE, isClickLoadMore);
        args.putBoolean(AUTO_HIDDEN_MORE, autoHidden);
        DemoFragment fragment = new DemoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView.LayoutManager createLayoutManager() {
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
        PopupMenu pop = new PopupMenu(getContext(), v);
        Menu menu = pop.getMenu();
        menu.add(Menu.NONE, 1, 0, "next time fail");
        menu.add(Menu.NONE, 2, 1, "next time no more");
        menu.add(Menu.NONE, 4, 3, "next time normal");
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
                    case 4: {
                        mIsFail = false;
                        mHasMore = true;
                        mWrapperAdapter.loadCompleted(true);
                        break;
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.frag_show_option).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClick(v);
            }
        });
        mRecyclerView = view.findViewById(R.id.fragment_recycler_rv);
        mPtrClassicFrameLayout = view.findViewById(R.id.fragment_recycler_ptr);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mData = new ArrayList<>();
        initAdapter();

        mRecyclerView.setLayoutManager(createLayoutManager());
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = DensityUtils.dip2px(getContext(), 10);
                super.getItemOffsets(outRect, view, parent, state);
            }
        });

        mWrapperAdapter = WrapperAdapter.wrap(mRecyclerAdapter);
        mRecyclerView.setAdapter(mWrapperAdapter);

        if (getArguments().getBoolean(CLICK_LOAD_MORE)) {
            mWrapperAdapter.setLoadMode(LoadMode.CLICK_LOAD);
        }
        if (getArguments().getBoolean(AUTO_HIDDEN_MORE)) {
            mWrapperAdapter.setAutoHiddenWhenNoMore(true);
        }

        setOnLoadMoreListener();

        mPtrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                if (mWrapperAdapter.isLoadingMore()) {
                    frame.refreshComplete();
                    return;
                }

                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mData.clear();
                        for (int i = 0; i < 20; i++) {
                            mData.add("我是Item " + i);
                        }
                        mRecyclerAdapter.notifyDataSetChanged();
                        frame.refreshComplete();
                        mWrapperAdapter.loadCompleted(true);
                        Toast.makeText(getContext(), "刷新完毕", Toast.LENGTH_SHORT).show();
                    }
                }, 1000);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return super.checkCanDoRefresh(frame, mRecyclerView, header);
            }
        });
    }

    private void setOnLoadMoreListener() {

        mWrapperAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public boolean canLoadMore() {
                return true;
            }

            @Override
            public void onLoadMore() {

                mPtrClassicFrameLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (mIsFail) {
                            mWrapperAdapter.loadFail();
                            return;
                        }
                        if (!mHasMore) {
                            mWrapperAdapter.loadCompleted(false);
                            return;
                        }

                        mRecyclerAdapter.addAll(Arrays.asList(
                                "新来的Item" + count++,
                                "新来的Item" + count++,
                                "新来的Item" + count++,
                                "新来的Item" + count++,
                                "新来的Item" + count++,
                                "新来的Item" + count++,
                                "新来的Item" + count++,
                                "新来的Item" + count++,
                                "新来的Item" + count++,
                                "新来的Item" + count++,
                                "新来的Item" + count++,
                                "新来的Item" + count++,
                                "新来的Item" + count++,
                                "新来的Item" + count++,
                                "新来的Item" + count++,
                                "新来的Item" + count++,
                                "新来的Item" + count++,
                                "新来的Item" + count++,
                                "新来的Item" + count++,
                                "新来的Item" + count++,
                                "新来的Item" + count++));
                        mWrapperAdapter.loadCompleted(true);
                    }
                }, 1000);
            }
        });

    }

    private void initAdapter() {
        mRecyclerAdapter = new BaseAdapter<String, ViewHolder<String>>(getContext(), mData) {
            @Override
            public ViewHolder<String> onCreateViewHolder(ViewGroup parent, int viewType) {
                View inflate = mLayoutInflater.inflate(R.layout.item, parent, false);
                return new ViewHolder<String>(inflate) {
                    private TextView mTextView;

                    @Override
                    protected void findViews() {
                        mTextView = findView(R.id.item_tv);
                    }

                    @Override
                    public void bindData(final String data) {
                        mTextView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getContext(), data, Toast.LENGTH_SHORT).show();
                            }
                        });
                        onBindData(mTextView, data);
                    }
                };
            }

            @Override
            public void onBindViewHolder(ViewHolder<String> viewHolder, final int position) {
                viewHolder.bindData(mData.get(position));
            }
        };
    }

    protected void onBindData(TextView textView, String data) {
        textView.setText(data);
    }

}
