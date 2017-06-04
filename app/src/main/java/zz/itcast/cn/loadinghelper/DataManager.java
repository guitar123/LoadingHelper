package zz.itcast.cn.loadinghelper;

import java.util.ArrayList;
import java.util.List;

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
        for (int i = 0; i < 5; i++) {
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
}
