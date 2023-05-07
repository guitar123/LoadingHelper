package zz.itcast.cn.loading.pullrefreshview.extras;

import android.content.Context;
import android.util.AttributeSet;

import zz.itcast.cn.loading.pullrefreshview.support.impl.Pullable;


public class PullableImageView extends androidx.appcompat.widget.AppCompatImageView implements Pullable {

    public PullableImageView(Context context) {
        super(context);
    }

    public PullableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullableImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    @Override
    public boolean isGetTop() {
        return true;
    }

    @Override
    public boolean isGetBottom() {
        return true;
    }

}
