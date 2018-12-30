package zz.itcast.cn.loadinghelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import zz.itcast.cn.loadinghelper.adapter.MultipleRecycleItemAdapter;
import zz.itcast.cn.loadinghelper.bean.MultipleDataBean;

/**
 * author: ${user}
 * Created by Mr.fan on 2017/6/4.
 * descrition: You share rose get fun. *_* *_*
 */

public class DataManager {
    private static DataManager mInstance = null;

    private DataManager() {
    }

    public static DataManager getInstance() {
        if (mInstance == null) {
            synchronized (DataManager.class) {
                if (mInstance == null) {
                    mInstance = new DataManager();
                }
            }
        }
        return mInstance;
    }

    ArrayList<String> dataList = new ArrayList<>();

    public List<String> getInitDataList() {
        dataList.clear();
        for (int i = 0; i < 10; i++) {
            dataList.add("数据：" + i);
        }
        return dataList;
    }

    public List<String> getMoreDataList() {
        ArrayList<String> moreDataList = new ArrayList<>();
        for (int i = dataList.size(); i < dataList.size() + 20; i++) {
            moreDataList.add("更多数据 ： " + i);
        }
        return moreDataList;
    }


    public List<MultipleDataBean> getMultipleDatas() {
        List<MultipleDataBean> datas = new ArrayList<>();
        MultipleDataBean multipleDataBean;
        for (int i = 0; i < 10; i++) {

            multipleDataBean = new MultipleDataBean();
            multipleDataBean.action = "MultipleDataBean action: i = " + i;
            multipleDataBean.title = "MultipleDataBean title: i = " + i;
            multipleDataBean.id = i;

            if (i == 0) {// banner
                multipleDataBean.viewType = MultipleRecycleItemAdapter.VIEW_TYPE_BANNER;
            } else if (i == 1) {// index
                multipleDataBean.viewType = MultipleRecycleItemAdapter.VIEW_TYPE_INDEX;
            } else if (i == 3) {// 2列表格
                multipleDataBean.viewType = MultipleRecycleItemAdapter.VIEW_TYPE_ITEM_G_2;
            } else if (i == 4) {//4列表格
                multipleDataBean.viewType = MultipleRecycleItemAdapter.VIEW_TYPE_ITEM_G_4;
            } else if (i % 2 == 0) {// 垂直 item
                multipleDataBean.viewType = MultipleRecycleItemAdapter.VIEW_TYPE_ITEM_V;
            } else {// 4列表格
                multipleDataBean.viewType = MultipleRecycleItemAdapter.VIEW_TYPE_ITEM_G_4;
            }

            //创建 5 个item数据
            List<MultipleDataBean.DataBean> lists = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                MultipleDataBean.DataBean dataBean = new MultipleDataBean.DataBean();
                dataBean.title = "data title : i = " + i + " j = " + j;
                dataBean.id = j;
                dataBean.content = "data content: i = " + i + " j = " + j;

                lists.add(dataBean);
            }
            multipleDataBean.lists = lists;

            if (i == 0) {//创建banner数据
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

            if (i == 1) {//创建index数据
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

            datas.add(multipleDataBean);
        }
        return datas;
    }

    public List<MultipleDataBean.DataBean> refreshChildDatas() {
        List<MultipleDataBean.DataBean> datas = new ArrayList<>();

        Random random = new Random();

        int randm = random.nextInt(10);
        if (randm <= 3) randm = 3;

        for (int i = 0; i < randm; i++) {
            MultipleDataBean.DataBean dataBean = new MultipleDataBean.DataBean();
            dataBean.content = "random = " + randm + " index = " + i;
            dataBean.title = "index = " + i;
            datas.add(dataBean);
        }
        return datas;
    }

    public List<MultipleDataBean> loadMore() {
        List<MultipleDataBean> datas = new ArrayList<>();
        MultipleDataBean multipleDataBean;
        for (int i = 0; i < 3; i++) {

            multipleDataBean = new MultipleDataBean();
            multipleDataBean.action = "MultipleDataBean action: i = " + i;
            multipleDataBean.title = "MultipleDataBean title: i = " + i;
            multipleDataBean.id = i;

            if (i % 2 == 0) {// 垂直 item
                multipleDataBean.viewType = MultipleRecycleItemAdapter.VIEW_TYPE_ITEM_V;
            } else {// 4列表格
                multipleDataBean.viewType = MultipleRecycleItemAdapter.VIEW_TYPE_ITEM_G_2;
            }

            //创建 5 个item数据
            List<MultipleDataBean.DataBean> lists = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                MultipleDataBean.DataBean dataBean = new MultipleDataBean.DataBean();
                dataBean.title = "data title : i = " + i + " j = " + j;
                dataBean.id = j;
                dataBean.content = "data content: i = " + i + " j = " + j;

                lists.add(dataBean);
            }
            multipleDataBean.lists = lists;

            datas.add(multipleDataBean);
        }
        return datas;
    }
}
