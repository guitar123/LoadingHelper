package zz.itcast.cn.loading.recycleutils;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.ViewGroup;

/**
 * Created by fhp on 16/6/28.
 */
public class WrapperUtils {

    /**
     * 获取设置跨度大小的回调，用来修复GridLayoutManager头布局或者脚布局跨度大小的问题
     */
    public interface SpanSizeCallback {
        /**
         * 设置获取跨度大小，用来修复GridLayoutManager头布局或者脚布局跨度大小的问题
         *
         * @param layoutManager
         * @param oldLookup
         * @param position
         * @return
         */
        int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position);
    }

    /**
     * 调整GridLayoutManager头布局跨度的问题
     *
     * @param innerAdapter
     * @param recyclerView
     * @param callback
     */
    public static void onAttachedToRecyclerView(RecyclerView.Adapter innerAdapter, RecyclerView recyclerView, final SpanSizeCallback callback) {

        innerAdapter.onAttachedToRecyclerView(recyclerView);

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            //如果是GridLayoutManager，需要调整头布局的跨度
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();

            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return callback.getSpanSize(gridLayoutManager, spanSizeLookup, position);
                }
            });
            gridLayoutManager.setSpanCount(gridLayoutManager.getSpanCount());
        }
    }

    /**
     * 调整瀑布流StaggeredGridLayoutManager
     *
     * @param holder
     */
    public static void setFullSpan(RecyclerView.ViewHolder holder) {

        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();

        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {

            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;

            p.setFullSpan(true);
        }
    }
}
