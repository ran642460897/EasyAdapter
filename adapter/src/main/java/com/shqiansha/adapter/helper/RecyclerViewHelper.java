package com.shqiansha.adapter.helper;

import android.view.View;

import com.shqiansha.adapter.EasyAdapter;
import com.shqiansha.adapter.EasyHolder;
import com.shqiansha.adapter.listener.OnRecyclerViewClickListener;
import com.shqiansha.adapter.listener.OnRecyclerViewRefreshListener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class RecyclerViewHelper {

    private RecyclerView recyclerView;
    private EasyAdapter easyAdapter;
    private OnRecyclerViewRefreshListener refreshListener;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView.LayoutManager layoutManager;

    public RecyclerViewHelper(RecyclerView recyclerView, EasyAdapter easyAdapter, OnRecyclerViewRefreshListener listener) {
        this.recyclerView = recyclerView;
        this.easyAdapter = easyAdapter;
        this.refreshListener = listener;
        this.layoutManager=new LinearLayoutManager(recyclerView.getContext());
    }
    public RecyclerViewHelper bindRefreshLayout(SwipeRefreshLayout refreshLayout){
        this.refreshLayout=refreshLayout;
        return this;
    }
    public  RecyclerViewHelper init(){
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(easyAdapter);

        if(refreshListener==null) return this;

        //设置加载失败点击事件
        easyAdapter.setOnTopReloadListener(new OnRecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                refreshListener.onPullDown();
            }
        });
        easyAdapter.setOnBottomReloadListener(new OnRecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                refreshListener.onLoadMore();
            }
        });

        //设置refreshLayout监听
        if(refreshLayout!=null){
            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    refreshListener.onPullDown();
                }
            });
        }



        //滑动到底部，加载更多判断
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(recyclerView.getAdapter()==null||recyclerView.getLayoutManager()==null||!(recyclerView.getLayoutManager() instanceof LinearLayoutManager)) return;

                LinearLayoutManager manager=(LinearLayoutManager) recyclerView.getLayoutManager();
                EasyAdapter adapter=(EasyAdapter)recyclerView.getAdapter();

                if (adapter.isWaitingForLoading() &&  newState == RecyclerView.SCROLL_STATE_IDLE &&
                        manager.findLastVisibleItemPosition() + 1 == adapter.getItemCount()) {
                    adapter.notifyLoading();
                    refreshListener.onLoadMore();
                }
            }
        });

        return this;
    }
}
