package zz.itcast.cn.loading.pullrefreshview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import zz.itcast.cn.loading.R;
import zz.itcast.cn.loading.pullrefreshview.layout.BaseHeaderView;
import zz.itcast.cn.loading.pullrefreshview.support.type.LayoutType;


/**
 * author: ${user}
 * Created by Mr.fan on 2017/5/26.
 * descrition: You share rose get fun. *_* *_*
 */

public class NormalHeaderView extends BaseHeaderView {

    private TextView mTv_refresh_state;
    private ImageView mIv_arrow;
    private ProgressBar mProgress;

    public NormalHeaderView(Context context) {
        this(context, null);
    }

    public NormalHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NormalHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.layout_normal_header_view, this, true);

        mTv_refresh_state = (TextView) findViewById(R.id.tv_refresh_state);
        mIv_arrow = (ImageView) findViewById(R.id.iv_arrow);
        mProgress = (ProgressBar) findViewById(R.id.progress);
    }


    @Override
    public float getSpanHeight() {
        return getMeasuredHeight();
    }

    @Override
    protected void onStateChange(int state) {
        Log.d("NormalHead", "state:" + state);
        if (mTv_refresh_state == null || mIv_arrow == null || mProgress == null) {
            Log.d("NormalHead", "mTv_refresh_state=" + mTv_refresh_state + " mIv_arrow=" + mIv_arrow + " mProgress=" + mProgress);
            return;
        }
        initVisible();
        switch (state) {
            case NONE:
                break;
            case PULLING://下拉刷新
                mTv_refresh_state.setText("下拉刷新");
                AnimUtil.startRotation(mIv_arrow,-180f,300,0);
                break;
            case LOOSENT_O_REFRESH://松开刷新
                mTv_refresh_state.setText("松手刷新");
                AnimUtil.startRotation(mIv_arrow,180f,300,0);
                break;
            case REFRESHING://正在刷新
                mTv_refresh_state.setText("正在刷新");
                mIv_arrow.setVisibility(View.INVISIBLE);
                mProgress.setVisibility(View.VISIBLE);
                break;
            case REFRESH_CLONE://刷新完成
                mTv_refresh_state.setText("刷新完成");
                mIv_arrow.setVisibility(View.INVISIBLE);
                mProgress.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private void initVisible() {
        mTv_refresh_state.setVisibility(View.VISIBLE);
        mProgress.setVisibility(View.INVISIBLE);
        mIv_arrow.setVisibility(View.VISIBLE);
    }

    @Override
    public int getLayoutType() {
        return LayoutType.LAYOUT_NORMAL;
    }
}
