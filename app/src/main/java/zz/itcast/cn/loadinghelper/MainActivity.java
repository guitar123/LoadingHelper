package zz.itcast.cn.loadinghelper;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

import zz.itcast.cn.loading.listener.OnLoadMoreListener;
import zz.itcast.cn.loading.loadinghelper.LoadMoreHelper;
import zz.itcast.cn.loading.pullrefreshview.NormalHeaderView;
import zz.itcast.cn.loading.pullrefreshview.layout.BaseHeaderView;

public class MainActivity extends AppCompatActivity implements OnLoadMoreListener {

    private RecyclerView rv_list;
    private DataManager mDataManager;
    private SimpleAdapter mAdapter;
    private LoadMoreHelper mLoadMoreHelper;
    private NormalHeaderView headView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDataManager = DataManager.getInstance();
        initView();
    }

    private void initView() {
        rv_list = (RecyclerView) findViewById(R.id.rv_list);
        rv_list.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rv_list.setItemAnimator(new DefaultItemAnimator());
        rv_list.setHasFixedSize(true);
        rv_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mAdapter = new SimpleAdapter(this, null);

        mLoadMoreHelper = new LoadMoreHelper(rv_list, mAdapter, this);

        headView = (NormalHeaderView) findViewById(R.id.headView);

        headView.setOnRefreshListener(new BaseHeaderView.OnRefreshListener() {
            @Override
            public void onRefresh(BaseHeaderView baseHeaderView) {
                headView.stopRefresh();
                mAdapter.setDataList(mDataManager.getInitDataList());
                mAdapter.notifyDataSetChanged();
                mLoadMoreHelper.setHasMore(true);
            }
        });
    }

    private void refreshUI(boolean isSuccess) {

        if (isSuccess) {
            Log.d("fhp", "加载完成");
            List<String> moreDataList = mDataManager.getMoreDataList();
            mAdapter.addDataList(moreDataList);
            if (count == 5) {
                mLoadMoreHelper.stopLoadMore(true);
                mLoadMoreHelper.setHasMore(false);
            } else {
                mLoadMoreHelper.stopLoadMore(true);
                mLoadMoreHelper.setHasMore(true);
            }
        } else {
            Log.d("fhp", "加载失败");
            mLoadMoreHelper.stopLoadMore(false);
        }

    }

    int count = 0;

    @Override
    public void onLoadMore() {
        Log.d("fhp", "开始加载更多");
        //加载更多的监听
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(2000);


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        count++;

                        if (count == 2) {
                            refreshUI(false);
                        } else {
                            refreshUI(true);
                        }


                    }
                });
            }
        }).start();
    }
}
