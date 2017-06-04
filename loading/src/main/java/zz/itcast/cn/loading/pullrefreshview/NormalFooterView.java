package zz.itcast.cn.loading.pullrefreshview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import zz.itcast.cn.loading.R;
import zz.itcast.cn.loading.pullrefreshview.layout.BaseFooterView;
import zz.itcast.cn.loading.pullrefreshview.support.type.LayoutType;
import zz.itcast.cn.loading.widget.progressindicator.AVLoadingIndicatorView;


/**
 * Created by 樊浩鹏 on 2016/11/4.
 */
public class NormalFooterView extends BaseFooterView {

    private TextView textView;
    private AVLoadingIndicatorView pb;


    public NormalFooterView(Context context) {
        this(context, null);
    }

    public NormalFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NormalFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_footer_normal, this, true);
        textView = (TextView) findViewById(R.id.text);
        pb = (AVLoadingIndicatorView) findViewById(R.id.pb);
    }


    @Override
    protected void onStateChange(int state) {
        textView.setVisibility(View.VISIBLE);
        switch (state) {
            case NONE:
                break;
            case PULLING:
                textView.setText("上拉加载更多");
                break;
            case LOOSENT_O_LOAD:
                textView.setText("松开加载");
                break;
            case LOADING:
                textView.setText("正在加载....");
                pb.setVisibility(View.VISIBLE);
                break;
            case LOAD_CLONE:
                textView.setText("加载完成");
                pb.setVisibility(View.INVISIBLE);
                break;

        }

    }

    @Override
    public float getSpanHeight() {
        return getHeight();
    }

    @Override
    public int getLayoutType() {
        return LayoutType.LAYOUT_NORMAL;
    }
}
