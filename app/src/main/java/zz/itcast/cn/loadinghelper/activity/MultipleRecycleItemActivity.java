package zz.itcast.cn.loadinghelper.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

import zz.itcast.cn.loadinghelper.DataManager;
import zz.itcast.cn.loadinghelper.R;
import zz.itcast.cn.loadinghelper.adapter.MultipleRecycleItemAdapter;
import zz.itcast.cn.loadinghelper.bean.MultipleDataBean;
import zz.itcast.cn.loadinghelper.bean.NodeInterface;

public class MultipleRecycleItemActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private GridLayoutManager mGridLayoutManager;
    private MultipleRecycleItemAdapter mAdapter;
    private ProgressDialog progressDialog;
    private List<NodeInterface> mDatas = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycleview);

        List<MultipleDataBean> datas = DataManager.getInstance().getMultipleDatas();
        initView();
        initListener();
        formattingData(datas);


        mAdapter.setDatas(mDatas);
    }

    private void initListener() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int firstVisiablePosition = mGridLayoutManager.findFirstCompletelyVisibleItemPosition();
                    int lastVisiablePosition = mGridLayoutManager.findLastCompletelyVisibleItemPosition();
                    Log.i("fhp", "firstVisiablePosition = " + firstVisiablePosition + " lastVisiablePosition = " + lastVisiablePosition);

                    if (!mRecyclerView.canScrollVertically(1)) {
                        loadMore();
                    }
                }
            }
        });
    }

    private void showLoading() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
        }
        if (progressDialog.isShowing()) return;
        progressDialog.setMessage("loading ... ");
        progressDialog.show();
    }

    private void hideLoading() {
        if (progressDialog == null) return;
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void loadMore() {

        showLoading();
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                List<MultipleDataBean> datas = DataManager.getInstance().loadMore();
                formattingData(datas);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.setDatas(mDatas);
                        hideLoading();
                    }
                });
            }
        }).start();
    }


    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycleView);
        mGridLayoutManager = new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new MultipleRecycleItemAdapter(this, null);
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * 格式化数据
     */
    private void formattingData(List<MultipleDataBean> lists) {

        List<NodeInterface> datas = new ArrayList<>();
        for (int i = 0; i < lists.size(); i++) {

            MultipleDataBean multipleDataBean = lists.get(i);

            if (multipleDataBean.viewType != MultipleRecycleItemAdapter.VIEW_TYPE_BANNER &&
                    multipleDataBean.viewType != MultipleRecycleItemAdapter.VIEW_TYPE_INDEX) {
                //创建并添加 父节点 作为title
                multipleDataBean.setIsParentNode(true);

                datas.add(multipleDataBean);

                for (int j = 0; j < multipleDataBean.lists.size(); j++) {
                    MultipleDataBean.DataBean dataBean = multipleDataBean.lists.get(j);
                    dataBean.viewType = multipleDataBean.viewType;


                    //记录他的 父节点位置
                    dataBean.parentNode = multipleDataBean;

                    datas.add(dataBean);
                }


                if (multipleDataBean.viewType == MultipleRecycleItemAdapter.VIEW_TYPE_ITEM_V) {
                    multipleDataBean.lists.get(multipleDataBean.lists.size() - 1).hasRefresh = true;
                }
            } else {// banner 和 index 的位置的数据直接使用
                datas.add(multipleDataBean);
            }
        }

        mDatas.addAll(datas);
    }


}
