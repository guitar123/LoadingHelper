package zz.itcast.cn.loadinghelper.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import zz.itcast.cn.loadinghelper.R;
import zz.itcast.cn.loadinghelper.adapter.MultipleRecycleItemAdapter;
import zz.itcast.cn.loadinghelper.bean.MultipleDataBean;
import zz.itcast.cn.loadinghelper.bean.NodeInterface;

public class MultipleRecycleItemActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private GridLayoutManager mGridLayoutManager;
    private List<MultipleDataBean> mDatas;
    private MultipleRecycleItemAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycleview);

        initView();
        initData();
        formattingData();
    }


    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycleView);
        mGridLayoutManager = new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new MultipleRecycleItemAdapter(this, null);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initData() {
        mDatas = new ArrayList<>();
        MultipleDataBean multipleDataBean;
        for (int i = 0; i < 10; i++) {

            multipleDataBean = new MultipleDataBean();
            multipleDataBean.action = "MultipleDataBean action: i = " + i;
            multipleDataBean.title = "MultipleDataBean title: i = " + i;
            multipleDataBean.id = i;

            if (i == 0) {
                multipleDataBean.viewType = MultipleRecycleItemAdapter.VIEW_TYPE_BANNER;
            } else if (i == 1) {
                multipleDataBean.viewType = MultipleRecycleItemAdapter.VIEW_TYPE_INDEX;
            } else if (i == 3) {
                multipleDataBean.viewType = MultipleRecycleItemAdapter.VIEW_TYPE_ITEM_G_2;
            } else if (i == 4) {
                multipleDataBean.viewType = MultipleRecycleItemAdapter.VIEW_TYPE_ITEM_G_4;
            } else if (i % 2 == 0) {
                multipleDataBean.viewType = MultipleRecycleItemAdapter.VIEW_TYPE_ITEM_V;
            } else {
                multipleDataBean.viewType = MultipleRecycleItemAdapter.VIEW_TYPE_ITEM_G_4;
            }

            List<MultipleDataBean.DataBean> lists = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                MultipleDataBean.DataBean dataBean = new MultipleDataBean.DataBean();
                dataBean.title = "data title : i = " + i + " j = " + j;
                dataBean.id = j;
                dataBean.content = "data content: i = " + i + " j = " + j;

                lists.add(dataBean);
            }
            multipleDataBean.lists = lists;

            if (i == 0) {
                List<MultipleDataBean.BannerBean> bannerBeans = new ArrayList<>();
                for (int j = 0; j < 4; j++) {
                    MultipleDataBean.BannerBean bannerBean = new MultipleDataBean.BannerBean();
                    bannerBean.action = "banner action: i = " + i + " j = " + j;
                    bannerBean.content = " banner content: i = " + i + " j = " + j;
                    bannerBean.id = j;
                    bannerBeans.add(bannerBean);
                }
                multipleDataBean.banners = bannerBeans;
            }
            if (i == 1) {
                List<MultipleDataBean.IndexBean> indexBeans = new ArrayList<>();
                for (int j = 0; j < 4; j++) {
                    MultipleDataBean.IndexBean indexBean = new MultipleDataBean.IndexBean();
                    indexBean.id = j;
                    indexBean.action = "index action: i = " + i + " j = " + j;
                    indexBean.title = "index action: i = " + i + " j = " + j;
                    indexBeans.add(indexBean);
                }
                multipleDataBean.indexs = indexBeans;
            }

            mDatas.add(multipleDataBean);
        }
    }

    private void formattingData() {

        List<NodeInterface> datas = new ArrayList<>();
        for (int i = 0; i < mDatas.size(); i++) {

            MultipleDataBean multipleDataBean = mDatas.get(i);

            if (multipleDataBean.viewType != MultipleRecycleItemAdapter.VIEW_TYPE_BANNER &&
                    multipleDataBean.viewType != MultipleRecycleItemAdapter.VIEW_TYPE_INDEX) {
                //创建并添加 title
                multipleDataBean.setIsTitle(true);

                datas.add(multipleDataBean);

                for (int j = 0; j < multipleDataBean.lists.size(); j++) {
                    MultipleDataBean.DataBean dataBean = multipleDataBean.lists.get(j);
                    dataBean.viewType = multipleDataBean.viewType;

                    dataBean.originalLists = multipleDataBean.lists;
                    datas.add(dataBean);
                }
                if (multipleDataBean.viewType == MultipleRecycleItemAdapter.VIEW_TYPE_ITEM_V) {
                    multipleDataBean.lists.get(multipleDataBean.lists.size() - 1).hasRefresh = true;
                }
            } else {
                datas.add(multipleDataBean);
            }
        }

        mAdapter.setDatas(datas);
    }
}
