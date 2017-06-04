package zz.itcast.cn.loading.pullrefreshview.support.impl;


import zz.itcast.cn.loading.pullrefreshview.layout.PullRefreshLayout;

/**
 * 可刷新的接口回调
 */
public interface Refreshable {

    void setPullRefreshLayout(PullRefreshLayout refreshLayout);

    /**
     * 在顶部下拉刷新时候y坐标变化的回调方法
     *
     *
     * @param y 移动到y坐标值
     * @return false FlingLayout会越界运动，true FlingLayout会固定到顶部或者底部
     */
    boolean onScroll(float y);

    /**
     * 状态改变
     *
     * @param state SCROLL_STATE_IDLE,SCROLL_STATE_TOUCH_SCROLL,SCROLL_STATE_FLING
     */
    void onScrollChange(int state);

    boolean onStartFling(float offsetTop);

    void startRefresh();

    void stopRefresh();
}
