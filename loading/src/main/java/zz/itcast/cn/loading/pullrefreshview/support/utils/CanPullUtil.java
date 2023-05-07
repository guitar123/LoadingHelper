package zz.itcast.cn.loading.pullrefreshview.support.utils;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.ScrollView;

import zz.itcast.cn.loading.pullrefreshview.support.impl.Pullable;


/**
 * Created by ybao on 16/3/7.
 */
public class CanPullUtil {

    /**
     * 获取PullAble的实现类，以此来判断是否已经拖动到顶部或者底部
     *
     * @param view
     * @return
     */
    public static Pullable getPullAble(View view) {
        if (view == null) {
            return null;
        }
        if (view instanceof Pullable) {
            return (Pullable) view;
        } else if (view instanceof AbsListView) {
            return new AbsListViewCanPull((AbsListView) view);
        } else if (view instanceof ScrollView || view instanceof NestedScrollView) {
            return new ScrollViewCanPull((ViewGroup) view);
        } else if (view instanceof WebView) {
            return new WebViewCanPull((WebView) view);
        } else if (view instanceof RecyclerView) {
            return new RecyclerViewCanPull((RecyclerView) view);
        }
        return null;
    }

    private static class AbsListViewCanPull implements Pullable {
        public AbsListViewCanPull(AbsListView absListView) {
            this.absListView = absListView;
        }

        AbsListView absListView;

        @Override
        public boolean isGetTop() {
            if (absListView.getCount() == 0) {
                //没有数据的情况
                return true;
            } else if (absListView.getFirstVisiblePosition() == 0 &&
                    absListView.getChildAt(0).getTop() >= absListView.getPaddingTop()) {
                //有数据，第一个条目完全可见的情况
                return true;
            }
            return false;
        }

        @Override
        public boolean isGetBottom() {
            int firstVisiblePosition = absListView.getFirstVisiblePosition();
            int lastVisiblePosition = absListView.getLastVisiblePosition();
            int count = absListView.getCount();
            if (count == 0) {//没有数据的情况
                return true;
            } else if (lastVisiblePosition == (count - 1)) {
                View view = absListView.getChildAt(lastVisiblePosition - firstVisiblePosition);
                if (view != null && view.getBottom() <= absListView.getMeasuredHeight() - absListView.getPaddingBottom()) {
                    //最后一个数据条目完全可见的情况
                    return true;
                }

            }
            return false;
        }
    }

    private static class ScrollViewCanPull implements Pullable {
        public ScrollViewCanPull(ViewGroup scrollView) {
            this.scrollView = scrollView;
        }

        ViewGroup scrollView;

        @Override
        public boolean isGetTop() {
            if (scrollView.getScrollY() <= 0)
                return true;
            else
                return false;
        }

        @Override
        public boolean isGetBottom() {
            if (scrollView.getChildCount() == 0) {//没有子view
                return true;
            }
            if (scrollView.getScrollY() >= (scrollView.getChildAt(0).getHeight() - scrollView.getMeasuredHeight()))
                return true;
            else
                return false;
        }
    }


    private static class RecyclerViewCanPull implements Pullable {
        public RecyclerViewCanPull(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
        }

        RecyclerView recyclerView;
        LinearLayoutManager layoutManager;

        GridLayoutManager mGridLayoutManager;

        private void initLayoutManager() {
            //判断layoutManager
            if (layoutManager == null) {
                RecyclerView.LayoutManager layout = recyclerView.getLayoutManager();
                if (layout != null && layout instanceof LinearLayoutManager) {
                    layoutManager = (LinearLayoutManager) layout;
                }
            }
            //判断layoutManager
            if (mGridLayoutManager == null) {
                RecyclerView.LayoutManager layout = recyclerView.getLayoutManager();
                if (layout != null && layout instanceof GridLayoutManager) {
                    mGridLayoutManager = (GridLayoutManager) layout;
                }
            }
        }

        @Override
        public boolean isGetTop() {
            initLayoutManager();
            if (layoutManager != null) {
                if (layoutManager.getItemCount() == 0) {//没有数据的情况
                    return true;
                } else if (layoutManager.findFirstVisibleItemPosition() == 0 &&
                        recyclerView.getChildAt(0).getTop() >= recyclerView.getPaddingTop()) {

                    return true;
                }
            }

            if (mGridLayoutManager != null) {
                if (mGridLayoutManager.getItemCount() == 0) {//没有数据的情况
                    return true;
                } else if (mGridLayoutManager.findFirstCompletelyVisibleItemPosition() == 0 &&
                        recyclerView.getChildAt(0).getTop() >= recyclerView.getPaddingTop()) {
                    //第一个条目完全显示的时候
                    return true;
                }
            }

            return false;
        }


        @Override
        public boolean isGetBottom() {
            initLayoutManager();
            if (layoutManager != null) {
                int count = layoutManager.getItemCount();
                if (count == 0) {
                    return true;
                } else if (layoutManager.findLastCompletelyVisibleItemPosition() == count - 1) {
                    return true;
                }
            }

            if (mGridLayoutManager != null) {
                int count = mGridLayoutManager.getItemCount();
                if (count == 0) {
                    return true;
                } else if (mGridLayoutManager.findLastCompletelyVisibleItemPosition() == count - 1) {
                    return true;
                }
            }

            return false;
        }
    }

    private static class WebViewCanPull implements Pullable {
        public WebViewCanPull(WebView webView) {
            this.webView = webView;
        }

        WebView webView;

        @Override
        public boolean isGetBottom() {
            if (webView.getScrollY() >= webView.getContentHeight() * webView.getScale() - webView.getMeasuredHeight())
                return true;
            else
                return false;
        }

        @Override
        public boolean isGetTop() {
            if (webView.getScrollY() <= 0)
                return true;
            else
                return false;
        }
    }

}
