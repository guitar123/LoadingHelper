package zz.itcast.cn.loading.widget.progressindicator.indicator;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

/**
 * 小球旋转淡入淡出动画
 */
public class BallSpinFadeLoaderIndicator extends BaseIndicatorController {

    public static final float SCALE = 1.0f;

    public static final int ALPHA = 255;

    float[] scaleFloats = new float[]{SCALE,
            SCALE,
            SCALE,
            SCALE,
            SCALE,
            SCALE,
            SCALE,
            SCALE};

    int[] alphas = new int[]{ALPHA,
            ALPHA,
            ALPHA,
            ALPHA,
            ALPHA,
            ALPHA,
            ALPHA,
            ALPHA};


    @Override
    public void draw(Canvas canvas, Paint paint) {
        //小球半径是宽度的1/10
        float radius = getWidth() / 10;
        //
        for (int i = 0; i < 8; i++) {
            canvas.save();
            //计算圆的位置(一共8个小圆，每45度一个)
            Point point = circleAt(getWidth(), getHeight(), getWidth() / 2 - radius, i * (Math.PI / 4));
            //移动画板到每一个小圆的圆心位置进行绘制
            canvas.translate(point.x, point.y);
            //设置画板缩放的值
            canvas.scale(scaleFloats[i], scaleFloats[i]);
            //设置画笔的透明度
            paint.setAlpha(alphas[i]);
            //绘制圆
            canvas.drawCircle(0, 0, radius, paint);

            canvas.restore();
        }
    }

    /**
     * 圆O的圆心为(a,b),半径为R,点A与到X轴的为角α.
     * 则点A的坐标为(a+R*cosα,b+R*sinα)
     *
     * @param width  targetView控件的宽度
     * @param height targetView控件的高度
     * @param radius 小圆的分布半径
     * @param angle  小圆所处的位置角度
     */
    Point circleAt(int width, int height, float radius, double angle) {
        float x = (float) (width / 2 + radius * (Math.cos(angle)));
        float y = (float) (height / 2 + radius * (Math.sin(angle)));
        return new Point(x, y);
    }

    @Override
    public List<Animator> createAnimation() {
        List<Animator> animators = new ArrayList<>();
        int[] delays = {0, 120, 240, 360, 480, 600, 720, 780, 840};
        for (int i = 0; i < 8; i++) {
            final int index = i;
            //创建值缩放动画
            ValueAnimator scaleAnim = ValueAnimator.ofFloat(1, 0.4f, 1);
            scaleAnim.setDuration(1000);
            scaleAnim.setRepeatCount(-1);
            //设置每一个小圆的开始延时时间
            scaleAnim.setStartDelay(delays[i]);
            scaleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    //计算动画数据
                    scaleFloats[index] = (float) animation.getAnimatedValue();
                    //重新绘制
                    postInvalidate();
                }
            });
            scaleAnim.start();

            //创建值透明度动画
            ValueAnimator alphaAnim = ValueAnimator.ofInt(255, 77, 255);
            alphaAnim.setDuration(1000);
            alphaAnim.setRepeatCount(-1);
            alphaAnim.setStartDelay(delays[i]);
            alphaAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    alphas[index] = (int) animation.getAnimatedValue();
                    //重新绘制
                    postInvalidate();
                }
            });
            alphaAnim.start();
            //添加动画到集合
            animators.add(scaleAnim);
            animators.add(alphaAnim);
        }
        return animators;
    }

    final class Point {
        public float x;
        public float y;

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }


}
