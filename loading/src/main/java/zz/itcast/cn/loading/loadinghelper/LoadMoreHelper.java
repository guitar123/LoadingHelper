package zz.itcast.cn.loading.loadinghelper;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import zz.itcast.cn.loading.listener.LoadingPullableListener;
import zz.itcast.cn.loading.listener.OnLoadMoreListener;
import zz.itcast.cn.loading.recycleutils.HeaderAndFooterWrapper;


/**
 * author: 樊浩鹏
 * Created by fhp on 2017/2/10 17:18.
 * description: You share rose get fun. *_* *_*
 * RecyclerView加载更多的辅助类
 */
public class LoadMoreHelper {

    private boolean isHasMore = false;

    private LoadingPullableListener footerAdapter;

    private boolean isLoading;//是否正在加载

    private OnLoadMoreListener listener;

    //头布局和脚布局的包装类
    private HeaderAndFooterWrapper mWrapper;

    /**
     * @param recyclerView RecycleView
     * @param adapter      原始数据的适配器
     * @param listener     加载更多的监听
     */
    public LoadMoreHelper(RecyclerView recyclerView, final RecyclerView.Adapter adapter, final OnLoadMoreListener listener) {
        //设置脚布局点击事件，点击后加载更多
        this.footerAdapter = new NormalLoadMoreImpl(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击之后继续加载更多数据
                startLoadMore();
            }
        });

        this.listener = listener;

        //获取布局管理器
        final LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        //对adapter进行包装
        mWrapper = new HeaderAndFooterWrapper(adapter);
        mWrapper.addFootView(footerAdapter.getView(recyclerView));
        recyclerView.setAdapter(mWrapper);//设置适配器

        //添加滚动的监听
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isHasMore && adapter.getItemCount() > 0
                        && layoutManager.findLastVisibleItemPosition() == mWrapper.getItemCount()-1
                        && !isLoading) {

                    startLoadMore();
                }
            }
        });
    }


    public HeaderAndFooterWrapper getAdapter() {
        return mWrapper;
    }

    /**
     * 设置是否还有更多
     */
    public void setHasMore(boolean isHasMore) {
        this.isHasMore = isHasMore;
        footerAdapter.setHasMoreData(isHasMore);
    }

    /**
     * 停止加载更多
     *
     * @param isSuccess 是否已经加载成功
     */
    public void stopLoadMore(boolean isSuccess) {
        if (isSuccess)
            footerAdapter.onSuccess();
        else
            footerAdapter.onFailue();
        isLoading = false;
    }

    /**
     * 开始加载更多
     */
    public void startLoadMore() {
        if (listener != null)
            listener.onLoadMore();
        footerAdapter.onLoadingData();
        isLoading = true;
    }
}
