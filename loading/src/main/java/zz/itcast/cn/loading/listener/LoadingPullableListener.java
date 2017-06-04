package zz.itcast.cn.loading.listener;

import android.view.View;
import android.view.ViewGroup;

/**
 * 需要下拉刷新或上拉加载数据的Listener
 */
public interface LoadingPullableListener {

    /**
     * 当数据加载完成并成功的时候回调
     */
    void onSuccess();

    /**
     * 当数据加载完成并失败的时候回调，这个状态可以点击
     */
    void onFailue();

    /**
     * 当正在加载数据中的回调
     */
    void onLoadingData();

    /**
     * 加载成功之后，设置是否还有更多数据
     *
     * @param hasMore 是否还有更多的数据
     */
    void setHasMoreData(boolean hasMore);

    /**
     * 创建加载更多的脚布局
     *
     * @return 返回加载更多的脚布局
     */
    View getView(ViewGroup viewGroup);
}
