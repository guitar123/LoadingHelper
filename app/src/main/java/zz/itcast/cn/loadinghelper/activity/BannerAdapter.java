package zz.itcast.cn.loadinghelper.activity;

import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import zz.itcast.cn.loadinghelper.bean.MultipleDataBean;

public class BannerAdapter extends PagerAdapter {

    private List<MultipleDataBean.BannerBean> mDatas;

    public BannerAdapter(List<MultipleDataBean.BannerBean> bannerBeans) {
        this.mDatas = bannerBeans;
    }

    public void setDatas(List<MultipleDataBean.BannerBean> bannerBeans) {
        this.mDatas = bannerBeans;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        TextView textView = new TextView(container.getContext());
        textView.setGravity(Gravity.CENTER);
        MultipleDataBean.BannerBean bannerBean = mDatas.get(position);
        textView.setText(bannerBean.content + " " + bannerBean.action);
        container.addView(textView, layoutParams);
        return textView;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (object instanceof View) {
            container.removeView((View) object);
        }
    }
}
