package zz.itcast.cn.loading.loadinghelper;

import android.view.View;
import android.view.ViewGroup;


import zz.itcast.cn.loading.listener.LoadingPullableListener;
import zz.itcast.cn.loading.widget.LoadingFooter;

/**
 * author: 樊浩鹏
 * Created by fhp on 2017/2/10 17:18.
 * description: You share rose get fun. *_* *_*
 * 通用的加载更多的脚布局实现类
 */
public class NormalLoadMoreImpl implements LoadingPullableListener {

    private LoadingFooter loadingFooter;//加载更多的布局

    private View.OnClickListener onClickListener;

    public NormalLoadMoreImpl(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public View getView(ViewGroup parent) {
        if (loadingFooter == null) {
            loadingFooter = new LoadingFooter(parent.getContext());
        }
        loadingFooter.setEnabled(false);//初次加载不显示布局
        loadingFooter.setVisibility(View.INVISIBLE);
        return loadingFooter;
    }

    @Override
    public void setHasMoreData(boolean hasMore) {
        //首先可见
        if (loadingFooter.getVisibility() != View.VISIBLE) {//显示加载更多的布局
            loadingFooter.setVisibility(View.VISIBLE);
        }

        if (!hasMore) {//没有更多了
            loadingFooter.setEnabled(true);
            loadingFooter.setState(LoadingFooter.State.TheEnd);
        } else { //有更多
            loadingFooter.setEnabled(false);
            loadingFooter.setState(LoadingFooter.State.Normal);
        }
    }

    @Override
    public void onSuccess() {
        if (loadingFooter.getVisibility() != View.VISIBLE) {//显示布局
            loadingFooter.setVisibility(View.VISIBLE);
        }

        loadingFooter.setEnabled(false);
        loadingFooter.setState(LoadingFooter.State.Normal);
    }

    @Override
    public void onFailue() {
        if (loadingFooter.getVisibility() != View.VISIBLE) {//显示布局
            loadingFooter.setVisibility(View.VISIBLE);
        }
        loadingFooter.setEnabled(true);
        loadingFooter.setState(LoadingFooter.State.NetWorkError);
        loadingFooter.setOnClickListener(onClickListener);
    }

    @Override
    public void onLoadingData() {
        if (loadingFooter.getVisibility() != View.VISIBLE) {//显示布局
            loadingFooter.setVisibility(View.VISIBLE);
        }
        loadingFooter.setEnabled(false);
        //设置状态为正在加载
        loadingFooter.setState(LoadingFooter.State.Loading);
    }
}
