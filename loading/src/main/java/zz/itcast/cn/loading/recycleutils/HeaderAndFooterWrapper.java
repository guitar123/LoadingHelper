package zz.itcast.cn.loading.recycleutils;

import androidx.collection.SparseArrayCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;


/**
 * <p>头布局和脚布局的包装类(如果需要的话)</p>
 * Created by fhp on 16/6/23.
 */
public class HeaderAndFooterWrapper<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    /**
     * 头布局的base tag
     */
    private static final int BASE_ITEM_TYPE_HEADER = 100000;
    /**
     * 脚布局的base tag
     */
    private static final int BASE_ITEM_TYPE_FOOTER = 200000;

    private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();//存放所有的headerview
    private SparseArrayCompat<View> mFootViews = new SparseArrayCompat<>();//存放所有的footview

    private RecyclerView.Adapter mInnerAdapter;//被包装的Adapter

    /**
     * @param adapter 需要包装的adapter
     */
    public HeaderAndFooterWrapper(RecyclerView.Adapter adapter) {
        setWrapAdapter(adapter);
    }

    /**
     * 获取真实(没有包装之前)的条目数目
     */
    private int getRealItemCount() {
        return mInnerAdapter.getItemCount();
    }

    /**
     * 是否是HeaderView
     */
    private boolean isHeaderViewPos(int position) {
        return position < getHeadersCount();
    }

    /**
     * 是否是FooterView
     */
    private boolean isFooterViewPos(int position) {
        return position >= getHeadersCount() + getRealItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderViewPos(position)) {
            // BASE_ITEM_TYPE_HEADER+position
            return mHeaderViews.keyAt(position);
        } else if (isFooterViewPos(position)) {
            //BASE_ITEM_TYPE_FOOTER+position
            return mFootViews.keyAt(position - getHeadersCount() - getRealItemCount());
        }
        return mInnerAdapter.getItemViewType(position - getHeadersCount());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderViews.get(viewType) != null) {
            //是头布局,使用头布局的ViewHolder
            ViewHolder holder = ViewHolder.createViewHolder(parent.getContext(), mHeaderViews.get(viewType));
            return holder;

        } else if (mFootViews.get(viewType) != null) {
            //是脚布局，是脚布局的ViewHolder
            ViewHolder holder = ViewHolder.createViewHolder(parent.getContext(), mFootViews.get(viewType));
            return holder;
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeaderViewPos(position)) {
            //头布局直接返回
            return;
        }
        if (isFooterViewPos(position)) {
            //脚布局直接返回
            return;
        }
        mInnerAdapter.onBindViewHolder(holder, position - getHeadersCount());
    }

    @Override
    public int getItemCount() {
        return getHeadersCount() + getFootersCount() + getRealItemCount();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        //修复GradLayoutManager头布局跨度的问题
        WrapperUtils.onAttachedToRecyclerView(mInnerAdapter, recyclerView, new WrapperUtils.SpanSizeCallback() {
            @Override
            public int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position) {
                int viewType = getItemViewType(position);
                if (mHeaderViews.get(viewType) != null) {
                    //头布局跨度最大
                    return layoutManager.getSpanCount();
                } else if (mFootViews.get(viewType) != null) {
                    //脚布局跨度最大
                    return layoutManager.getSpanCount();
                }
                if (oldLookup != null)
                    return oldLookup.getSpanSize(position);
                return 1;
            }
        });
    }

    //当列表项出现到可视界面的时候调用
    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        mInnerAdapter.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        //调整瀑布流
        if (isHeaderViewPos(position) || isFooterViewPos(position)) {
            WrapperUtils.setFullSpan(holder);
        }
    }

    /**
     * 添加头布局
     */
    public void addHeaderView(View view) {
        mHeaderViews.put(mHeaderViews.size() + BASE_ITEM_TYPE_HEADER, view);
    }

    /**
     * 添加脚布局
     */
    public void addFootView(View view) {
        mFootViews.put(mFootViews.size() + BASE_ITEM_TYPE_FOOTER, view);
    }

    /**
     * 获取头布局的数目
     */
    public int getHeadersCount() {
        return mHeaderViews.size();
    }

    /**
     * 获取脚布局的数目
     */
    public int getFootersCount() {
        return mFootViews.size();
    }

    /**
     * 包装adapter
     *
     * @param adapter
     */
    public void setWrapAdapter(RecyclerView.Adapter adapter) {
        if (adapter == null)
            return;
        if (this.mInnerAdapter != null)
            this.mInnerAdapter.unregisterAdapterDataObserver(observer);
        this.mInnerAdapter = adapter;
        this.mInnerAdapter.registerAdapterDataObserver(observer);
    }


    /**
     * 适配器数据观察
     */
    private RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {

        HeaderAndFooterWrapper headfootAdapter = HeaderAndFooterWrapper.this;

        @Override
        public void onChanged() {
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            headfootAdapter.notifyItemRangeChanged(positionStart + getHeadersCount(), itemCount);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            headfootAdapter.notifyItemRangeInserted(positionStart + getHeadersCount(), itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            headfootAdapter.notifyItemRangeRemoved(positionStart + getHeadersCount(), itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            headfootAdapter.notifyItemMoved(fromPosition + getHeadersCount(), itemCount);
        }
    };
}
