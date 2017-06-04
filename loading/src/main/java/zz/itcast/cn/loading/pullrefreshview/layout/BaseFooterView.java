package zz.itcast.cn.loading.pullrefreshview.layout;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

import zz.itcast.cn.loading.pullrefreshview.support.impl.Loadable;
import zz.itcast.cn.loading.pullrefreshview.support.type.LayoutType;


public abstract class BaseFooterView extends RelativeLayout implements Loadable {

    public final static int NONE = 0;//空闲
    public final static int PULLING = 1;//正在拖动
    public final static int LOOSENT_O_LOAD = 2;
    public final static int LOADING = 3;//正在加载的状态
    public final static int LOAD_CLONE = 4;//加载完成，布局不再显示脚布局
    private int stateType = NONE;//当前状态

    private PullRefreshLayout pullRefreshLayout;

    private boolean isLockState = false;//状态是否被锁定,(是否正在加载)

    private OnLoadListener onLoadListener;

    private int scrollState = FlingLayout.SCROLL_STATE_IDLE;

    public BaseFooterView(Context context) {
        this(context, null);
    }

    public BaseFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //设置不可触摸
        setFocusable(false);
        setFocusableInTouchMode(false);
    }

    /**
     * 状态是否被锁定
     *
     * @return
     */
    protected boolean isLockState() {
        return isLockState;
    }

    /**
     * 获取布局类型
     *
     * @return
     */
    public int getLayoutType() {
        return LayoutType.LAYOUT_NORMAL;
    }

    private void setState(int state) {
        if (isLockState || stateType == state) {
            return;
        }
        Log.i("BaseFooterView", "" + state);
        this.stateType = state;
        if (state == LOADING) {
            //如果是正在加载就锁定状态
            isLockState = true;
            if (onLoadListener != null) {
                onLoadListener.onLoad(this);
            }
        }
        onStateChange(state);
    }


    public int getType() {
        return stateType;
    }

    /**
     * 停止刷新后让布局重新隐藏到脚布局不显示的位置
     */
    private void close() {
        if (this.pullRefreshLayout != null) {
            float moveY = pullRefreshLayout.getMoveY();
            if (moveY < 0) {
                pullRefreshLayout.startMoveTo(moveY, 0);
                setState(NONE);
            }
        }
    }

    @Override
    public void setPullRefreshLayout(PullRefreshLayout pullRefreshLayout) {
        this.pullRefreshLayout = pullRefreshLayout;
    }

    @Override
    public void startLoad() {
        //开始加载更多
        if (this.pullRefreshLayout != null) {
            float moveY = pullRefreshLayout.getMoveY();
            if (moveY == 0) {
                float footerSpanHeight = getSpanHeight();
                //让布局移动到脚布局刚好显示的位置，正在加载的状态也将被锁定
                pullRefreshLayout.startMoveTo(0, -footerSpanHeight);
                setState(LOADING);
            }
        }
    }

    @Override
    public void stopLoad() {
        //停止加载，接触被锁定的状态
        isLockState = false;
        //设置状态
        setState(LOAD_CLONE);
        //延时关闭，（让布局不再显示脚布局）
        postDelayed(new Runnable() {
            @Override
            public void run() {
                close();
            }
        }, 400);
    }


    @Override
    public boolean onScroll(float y) {
        boolean intercept = false;
        int footerLayoutType = getLayoutType();
        if (footerLayoutType == LayoutType.LAYOUT_SCROLLER) {
            ViewCompat.setTranslationY(this, -getMeasuredHeight());
        } else if (footerLayoutType == LayoutType.LAYOUT_DRAWER) {
            ViewCompat.setTranslationY(this, y);
            ViewCompat.setTranslationY(pullRefreshLayout.getPullView(), 0);
            intercept = true;
        } else {
            ViewCompat.setTranslationY(this, y);
        }
        float footerSpanHeight = getSpanHeight();
        if (scrollState == FlingLayout.SCROLL_STATE_TOUCH_SCROLL) {
            if (y <= -footerSpanHeight) {
                setState(LOOSENT_O_LOAD);
            } else {
                setState(PULLING);
            }
        }
        return intercept;
    }

    @Override
    public void onScrollChange(int state) {
        scrollState = state;
    }

    @Override
    public boolean onStartFling(float nowY) {
        float footerSpanHeight = getSpanHeight();
        if (nowY <= -footerSpanHeight) {
            pullRefreshLayout.startMoveTo(nowY, -footerSpanHeight);
            setState(LOADING);
            return true;
        }
        pullRefreshLayout.startMoveTo(nowY, 0);
        setState(NONE);
        return false;
    }

    public abstract float getSpanHeight();

    protected abstract void onStateChange(int state);


    public interface OnLoadListener {
        void onLoad(BaseFooterView baseFooterView);
    }

    public void setOnLoadListener(OnLoadListener onRefreshListener) {
        this.onLoadListener = onRefreshListener;
    }

}

