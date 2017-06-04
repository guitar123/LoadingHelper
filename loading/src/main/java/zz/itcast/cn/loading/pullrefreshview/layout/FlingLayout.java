package zz.itcast.cn.loading.pullrefreshview.layout;

import android.content.Context;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;

import com.nineoldandroids.view.ViewHelper;

import zz.itcast.cn.loading.pullrefreshview.support.impl.Pullable;
import zz.itcast.cn.loading.pullrefreshview.support.utils.CanPullUtil;

public class FlingLayout extends FrameLayout /*implements NestedScrollingChild, NestedScrollingParent */ {


//    private NestedScrollingParentHelper mParentHelper;
//    private NestedScrollingChildHelper mChildHelper;

    public final static int SCROLL_STATE_IDLE = 0;//空闲的状态
    public final static int SCROLL_STATE_TOUCH_SCROLL = 1;//触摸滚动的状态
    public final static int SCROLL_STATE_FLING = 2;//松手后的回弹操作

    /**
     * 当前的状态类型，默认是 SCROLL_STATE_IDLE
     */
    private int stateType = SCROLL_STATE_IDLE;
    private int tempStateType = SCROLL_STATE_IDLE;//临时的状态类型

    protected Pullable pullable;

    protected View mPullView;

    private int mTouchSlop;

    private Scroller mScroller;

    protected float downY, downX;
    /**
     * 是否正在滚动
     */
    private boolean isScrolling = false;

    protected float tepmX;
    protected float tepmY;

    private static final int MAX_DURATION = 400;
    private static final int MIN_DURATION = 200;

    private boolean canPullUp = true;
    private boolean canPullDown = true;

    protected OnScrollListener mOnScrollListener;

    protected int maxDistance = 0;
    protected int version;
    int mPointerId;

    protected int MAXDISTANCE = 0;

    float moveY = 0;

    public View getPullView() {
        return mPullView;
    }

    public FlingLayout(Context context) {
        this(context, null);
    }

    public FlingLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlingLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void init(Context context) {
        version = android.os.Build.VERSION.SDK_INT;

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        mScroller = new Scroller(context, new AccelerateDecelerateInterpolator());
//        mParentHelper = new NestedScrollingParentHelper(this);
//        mChildHelper = new NestedScrollingChildHelper(this);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        MAXDISTANCE = getMeasuredHeight() * 3 / 5;
    }

    @Override
    public void computeScroll() {
        if (!mScroller.isFinished()) {
            if (mScroller.computeScrollOffset()) {
                moveTo(mScroller.getCurrY());
                ViewCompat.postInvalidateOnAnimation(this);
            } else if (stateType == SCROLL_STATE_FLING) {
                setScrollState(SCROLL_STATE_IDLE);
            }
        } else if (stateType == SCROLL_STATE_FLING) {
            setScrollState(SCROLL_STATE_IDLE);
        }
        super.computeScroll();
    }


    private boolean canPullUp() {
        if (mPullView != null) {
            return canPullUp && pullable.isGetBottom();
        }
        return canPullUp;
    }

    private boolean canPullDown() {
        if (mPullView != null) {
            return canPullDown && pullable.isGetTop();
        }
        return canPullDown;
    }

    /**
     * 需要移动到的位置
     *
     * @param y 需要移动到的y坐标：currentMoveY+dataY
     */
    private void moveTo(float y) {
        setMoveY(y);
        setScrollState(tempStateType);
        Log.i("flingLayout", "moveY:" + y);
        boolean intercept = onScroll(y);
        if (mOnScrollListener != null) {
            mOnScrollListener.onScroll(this, y);
        }
        if (!intercept) {//使用动画来完成越界下拉或者上拉的UI
            setViewTranslationY(mPullView, y);
        }
    }

    /**
     * 设置滚动的状态
     */
    private void setScrollState(int stateType) {
        if (this.stateType != stateType) {
            this.stateType = stateType;
            this.tempStateType = stateType;
            Log.i("flingLayout", "onScrollChange:" + stateType);
            onScrollChange(stateType);
            if (mOnScrollListener != null) {
                mOnScrollListener.onScrollChange(this, stateType);
            }
        }
    }

    private void moveBy(float dy) {
        moveTo(getMoveY() + dy);
    }

    /**
     * 设置动画平移
     */
    protected static void setViewTranslationY(View view, float value) {
        if (view == null) {
            return;
        }
        ViewHelper.setTranslationY(view, value);
    }

    private void setMoveY(float moveY) {
        this.moveY = moveY;
    }

    /**
     * 获取移动到的y坐标
     */
    public float getMoveY() {
        return moveY;
    }

    public int startMoveBy(float startY, float dy) {
        setScrollState(SCROLL_STATE_FLING);
        int duration = (int) Math.abs(dy);
        int time = Math.min(MAX_DURATION, duration);
        time = Math.max(MIN_DURATION, time);
        mScroller.startScroll(0, (int) startY, 0, (int) dy, time);
        invalidate();
        return time;
    }

    public int startMoveTo(float startY, float endY) {
        return startMoveBy(startY, endY - startY);
    }


    private void startFling() {
        Log.i("flingLayout", " ---> startFling()");
        float nowY = getMoveY();
        if (nowY != 0) {//正在拖动
            if (!onStartFling(nowY)) {
                startMoveTo(nowY, 0);
            }
        } else {
            setScrollState(SCROLL_STATE_IDLE);
        }
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (mPullView == null && (pullable = CanPullUtil.getPullAble(child)) != null) {
            mPullView = child;
        }
        super.addView(child, index, params);
    }

    public void setMaxDistance(int maxDistance) {
        this.maxDistance = maxDistance;
    }


    public void setCanPullDown(boolean canPullDown) {
        this.canPullDown = canPullDown;
        if (!canPullDown && getMoveY() > 0) {
            moveTo(0);
        }
    }

    public void setCanPullUp(boolean canPullUp) {
        this.canPullUp = canPullUp;
        if (!canPullUp && getMoveY() < 0) {
            moveTo(0);
        }
    }

    /******************************************************************/

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        Log.i("flingLayout", " ---> dispatchTouchEvent()");
        //TODO
//        if (mPullView != null && !ViewCompat.isNestedScrollingEnabled(mPullView)) {
        if (mPullView != null) {
            float moveY = getMoveY();
            int pointerCount = ev.getPointerCount();
            int pointerIndex = ev.getActionIndex();
            if (!mScroller.isFinished()) {
//                Log.i("flingLayout", " ---> mScroller.abortAnimation()");
                mScroller.abortAnimation();
            }

            switch (ev.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    mPointerId = ev.getPointerId(pointerIndex);
                    float x = ev.getX(pointerIndex);
                    float y = ev.getY(pointerIndex);
                    //记录按下时候的xy值
                    tepmY = downY = y;
                    tepmX = downX = x;
                    tempStateType = SCROLL_STATE_TOUCH_SCROLL;

                    if (moveY != 0) {//将事件让自己消费掉
                        return true;
                    }

                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    mPointerId = ev.getPointerId(pointerIndex);
                    tepmX = ev.getX(pointerIndex);
                    tepmY = ev.getY(pointerIndex);
                    break;
                case MotionEvent.ACTION_MOVE:
                    pointerIndex = ev.findPointerIndex(mPointerId);
                    float mx;
                    float my;
                    if (pointerCount > pointerIndex && pointerIndex >= 0) {
                        mx = ev.getX(pointerIndex);
                        my = ev.getY(pointerIndex);
                    } else {
                        mx = ev.getX();
                        my = ev.getY();
                    }
                    //意图分析，避免误操作
                    //计算过程中的变化量
                    int dataX = (int) (mx - tepmX);
                    int dataY = (int) (my - tepmY);
                    tepmX = mx;
                    tepmY = my;

                    if (isScrolling || (Math.abs(dataY) > Math.abs(dataX))) {//正在垂直滚动
                        isScrolling = true;
                        if (moveY == 0) {
                            //开始时 在0,0处
                            //判断是否可以滑动
                            if ((dataY < 0 && canPullUp()) || (dataY > 0 && canPullDown())) {
                                //在顶部并且可以下啦，在底部并且可以上拉
                                moveBy(dataY);
                                return true;
                            }
                        } else {
                            //当不在0,0处
                            ev.setAction(MotionEvent.ACTION_CANCEL);//屏蔽原事件

                            if ((moveY < 0 && moveY + dataY >= 0) || (moveY > 0 && moveY + dataY <= 0)) {
                                //在0,0附近浮动
                                ev.setAction(MotionEvent.ACTION_DOWN);
                                moveTo(0);
                            } else if ((moveY > 0 && dataY > 0) || (moveY < 0 && dataY < 0)) {
                                //是否超过最大距离
                                if (maxDistance == 0 || Math.abs(moveY) < maxDistance) {
                                    int ps = 0;
                                    int hDataY = dataY / 2;
                                    if (maxDistance == 0) {
                                        ps = (int) (-hDataY * Math.abs(moveY) / (float) MAXDISTANCE) - hDataY;
                                    } else {
                                        ps = (int) (-hDataY * Math.abs(moveY) / (float) maxDistance) - hDataY;
                                    }
                                    moveBy(ps + dataY);
                                } else if (moveY > maxDistance) {
                                    moveTo(maxDistance);
                                } else if (moveY < -maxDistance) {
                                    moveTo(-maxDistance);
                                }
                            } else {
                                moveBy(dataY);
                            }
                        }
                    } else {
                        ev.setLocation(mx, downY);
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    startFling();
                    isScrolling = false;
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    // 获取离开屏幕的手指的索引
                    int pointerIdLeave = ev.getPointerId(pointerIndex);
                    if (mPointerId == pointerIdLeave) {
                        // 离开屏幕的正是目前的有效手指，此处需要重新调整，并且需要重置VelocityTracker
                        int reIndex = pointerIndex == 0 ? 1 : 0;
                        mPointerId = ev.getPointerId(reIndex);
                        // 调整触摸位置，防止出现跳动
                        tepmY = ev.getY(reIndex);
                    }
            }
            //分发事件由子view决定

            return super.dispatchTouchEvent(ev) || isScrolling;
        } else {
            return super.dispatchTouchEvent(ev);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        Log.i("flingLayout", " ---> onTouchEvent()");
        if (mPullView != null && !ViewCompat.isNestedScrollingEnabled(mPullView)) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    return true;
            }
        }
        return super.onTouchEvent(event);
    }
    /******************************************************************/

    /******************************************************************/
//
//    @Override
//    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
//        if (!isNestedScrollingEnabled()) {
//            setNestedScrollingEnabled(true);
//        }
//        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
//    }
//
//    @Override
//    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
//        mParentHelper.onNestedScrollAccepted(child, target, nestedScrollAxes);
//        startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
//    }
//
//    @Override
//    public void onStopNestedScroll(View target) {
//        startFling();
//        stopNestedScroll();
//    }
//
//    @Override
//    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
//        int[] offsetInWindow = new int[2];
//        dispatchNestedScroll(0, dyConsumed, 0, dyUnconsumed, offsetInWindow);
//        moveBy(-dyUnconsumed - offsetInWindow[1]);
//    }
//
//    @Override
//    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
//        this.tempStateType = SCROLL_STATE_TOUCH_SCROLL;
//        float moveY = getMoveY();
//        if (mPullView == null || moveY == 0) {
//            dispatchNestedPreScroll(0, dy, consumed, null);
//        } else {
//            consumed[0] = 0;
//            stopNestedScroll();
//            int dataY = -dy;
//            if ((moveY < 0 && moveY + dataY >= 0) || (moveY > 0 && moveY + dataY <= 0)) {
//                moveTo(0);
//                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
//                consumed[1] = (int) (moveY - 0);
//                int[] pconsumed = new int[2];
//                dispatchNestedPreScroll(0, dy - consumed[1], pconsumed, null);
//                consumed[1] += pconsumed[1];
//            } else if ((moveY > 0 && dataY > 0) || (moveY < 0 && dataY < 0)) {
//                //是否超过最大距离
//                if (maxDistance == 0 || Math.abs(moveY) < maxDistance) {
//                    int ps = 0;
//                    int hDataY = dataY / 2;
//                    if (maxDistance == 0) {
//                        ps = (int) (-hDataY * Math.abs(moveY) / (float) MAXDISTANCE) - hDataY;
//                    } else {
//                        ps = (int) (-hDataY * Math.abs(moveY) / (float) maxDistance) - hDataY;
//                    }
//                    moveBy(ps + dataY);
//                } else if (moveY > maxDistance) {
//                    moveTo(maxDistance);
//                } else if (moveY < -maxDistance) {
//                    moveTo(-maxDistance);
//                }
//                consumed[1] = dy;
//            } else {
//                moveBy(dataY);
//                consumed[1] = dy;
//            }
//        }
//    }
//
//    @Override
//    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
//        return dispatchNestedFling(velocityX, velocityY, consumed);
//    }
//
//    @Override
//    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
//        boolean consumed = dispatchNestedPreFling(velocityX, velocityY);
//        if (consumed) {
//            return true;
//        }
//        Pullable pullable = CanPullUtil.getPullAble(target);
//        if (pullable != null) {
//            if (pullable.isGetBottom() && velocityY < 0) {
//                return true;
//            } else if (pullable.isGetTop() && velocityY > 0) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    @Override
//    public int getNestedScrollAxes() {
//        return mParentHelper.getNestedScrollAxes();
//    }
//
//
//    @Override
//    public void setNestedScrollingEnabled(boolean enabled) {
//        mChildHelper.setNestedScrollingEnabled(enabled);
//    }
//
//    @Override
//    public boolean isNestedScrollingEnabled() {
//        return mChildHelper.isNestedScrollingEnabled();
//    }
//
//    @Override
//    public boolean startNestedScroll(int axes) {
//        return mChildHelper.startNestedScroll(axes);
//    }
//
//    @Override
//    public void stopNestedScroll() {
//        mChildHelper.stopNestedScroll();
//    }
//
//    @Override
//    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
//        return mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
//    }
//
//    @Override
//    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
//        return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
//    }
//
//    @Override
//    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
//        return mChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
//    }
//
//    @Override
//    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
//        return mChildHelper.dispatchNestedPreFling(velocityX, velocityY);
//    }
//
//    @Override
//    public void onDetachedFromWindow() {
//        super.onDetachedFromWindow();
//        mChildHelper.onDetachedFromWindow();
//    }

    /******************************************************************/

    /**
     * 正在滚动
     *
     * @param y 移动到的y坐标
     * @return true会拦截动画，false不拦截动画
     */
    protected boolean onScroll(float y) {
        return false;
    }

    protected boolean onStartFling(float nowY) {
        return false;
    }

    /**
     * 滚动状态改变
     */
    protected void onScrollChange(int stateType) {
    }

    /**
     * 滚动的监听
     */
    public interface OnScrollListener {

        /**
         * 正在滚动
         *
         * @param y 当前移动到的y坐标
         */
        void onScroll(FlingLayout flingLayout, float y);

        /**
         * 滚动状态改变
         */
        void onScrollChange(FlingLayout flingLayout, int state);

    }

    public void setOnScrollListener(OnScrollListener mOnScrollListener) {
        this.mOnScrollListener = mOnScrollListener;
    }
}