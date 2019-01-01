package zz.itcast.cn.loadinghelper;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;

public abstract class HandlerThreadCountDownTimer {

    private final Handler mHandler;

    /**
     * 结束时间
     */
    private final long mMillisInFuture;

    /**
     * 时间间隔
     */
    private final long mCountdownInterval;

    /**
     * 未来真正停止的时间
     */
    private long mStopTimeInFuture;

    private boolean mCancelled = false;


    private static final int MSG = 1;

    public HandlerThreadCountDownTimer(long millisInFuture, long countDownInterval) {
        HandlerThread handlerThread = new HandlerThread(this.getClass().getName());
        mHandler = new Handler(handlerThread.getLooper(), handlerCallback);

        this.mMillisInFuture = millisInFuture;
        this.mCountdownInterval = countDownInterval;
    }

    public synchronized final void cancel() {
        mCancelled = true;
        mHandler.removeMessages(MSG);
    }

    public synchronized final HandlerThreadCountDownTimer start() {
        mCancelled = false;
        if (mMillisInFuture <= 0) {//已经结束
            onFinish();
            return this;
        }

        //加上当前时间，计算未来真正停止的时间
        mStopTimeInFuture = SystemClock.elapsedRealtime() + mMillisInFuture;
        mHandler.sendMessage(mHandler.obtainMessage(MSG));
        return this;
    }

    /**
     * 倒计时结束的回调函数
     */
    abstract void onFinish();

    /**
     * 在子线程中执行
     * @param millisUntilFinished 剩余时间
     */
    abstract void onTick(long millisUntilFinished);

    private Handler.Callback handlerCallback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            synchronized (HandlerThreadCountDownTimer.class) {
                if (mCancelled) {
                    return true;
                }

                //计算剩余时间
                final long millisLeft = mStopTimeInFuture - SystemClock.elapsedRealtime();

                if (millisLeft <= 0) {//已经结束
                    onFinish();
                } else if (millisLeft < mCountdownInterval) {//还差一点
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG), millisLeft);
                } else {
                    long lastTickStart = SystemClock.elapsedRealtime();

                    onTick(millisLeft);

                    long delay = lastTickStart + mCountdownInterval - SystemClock.elapsedRealtime();

                    while (delay < 0) delay += mCountdownInterval;

                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG), delay);
                }
                return true;
            }
        }
    };

}
