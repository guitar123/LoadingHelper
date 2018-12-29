package zz.itcast.cn.loadinghelper.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import zz.itcast.cn.loading.recycleutils.WrapperUtils;
import zz.itcast.cn.loadinghelper.R;
import zz.itcast.cn.loadinghelper.bean.MultipleDataBean;
import zz.itcast.cn.loadinghelper.bean.NodeInterface;

public class MultipleRecycleItemAdapter extends RecyclerView.Adapter {

    public static final int VIEW_TYPE_BANNER = 460;
    public static final int VIEW_TYPE_INDEX = 364;
    public static final int VIEW_TYPE_ITEM_V = 514;
    public static final int VIEW_TYPE_ITEM_G_2 = 164;
    public static final int VIEW_TYPE_ITEM_G_4 = 165;
    public static final int VIEW_TYPE_GROUP_TITLE = 563;

    private final LayoutInflater mLayoutInflater;

    private List<NodeInterface> mDatas;

    public MultipleRecycleItemAdapter(Context context, List<NodeInterface> datas) {
        this.mDatas = datas;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setDatas(List<NodeInterface> datas) {
        this.mDatas = datas;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        NodeInterface nodeInterface = mDatas.get(position);
        if (nodeInterface.isTitle()) return VIEW_TYPE_GROUP_TITLE;
        if (nodeInterface.getItemViewType() != 0) {
            return nodeInterface.getItemViewType();
        }
        return super.getItemViewType(position);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_BANNER) {
            return new BannerViewHolder(mLayoutInflater.inflate(R.layout.item_banner, parent, false));
        } else if (viewType == VIEW_TYPE_INDEX) {
            return new IndexViewHolder(mLayoutInflater.inflate(R.layout.item_index, parent, false));
        } else if (viewType == VIEW_TYPE_ITEM_V) {
            return new VerticalViewHolder(mLayoutInflater.inflate(R.layout.item_data_vertical, parent, false));
        } else if (viewType == VIEW_TYPE_ITEM_G_2) {
            return new Grid2ViewHolder(mLayoutInflater.inflate(R.layout.item_data_g_2, parent, false));
        } else if (viewType == VIEW_TYPE_ITEM_G_4) {
            return new Grid4ViewHolder(mLayoutInflater.inflate(R.layout.item_data_g4, parent, false));
        } else if (viewType == VIEW_TYPE_GROUP_TITLE) {
            return new GroupTitleViewHolder(mLayoutInflater.inflate(R.layout.item_title, parent, false));
        }
        return new EmptyViewHolder(new View(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BannerViewHolder) {
            ((BannerViewHolder) holder).bindData((MultipleDataBean) mDatas.get(position));
        } else if (holder instanceof IndexViewHolder) {
            ((IndexViewHolder) holder).bindData((MultipleDataBean) mDatas.get(position));
        } else if (holder instanceof VerticalViewHolder) {
            ((VerticalViewHolder) holder).bindData((MultipleDataBean.DataBean) mDatas.get(position));
        } else if (holder instanceof Grid2ViewHolder) {
            ((Grid2ViewHolder) holder).bindData((MultipleDataBean.DataBean) mDatas.get(position));
        } else if (holder instanceof Grid4ViewHolder) {
            ((Grid4ViewHolder) holder).bindData((MultipleDataBean.DataBean) mDatas.get(position));
        } else if (holder instanceof GroupTitleViewHolder) {
            ((GroupTitleViewHolder) holder).bindData((MultipleDataBean) mDatas.get(position));
        }
        // do nothing
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        //修复GradLayoutManager头布局跨度的问题
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            //如果是GridLayoutManager，需要调整头布局的跨度
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();

            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int itemViewType = getItemViewType(position);
                    NodeInterface nodeInterface = mDatas.get(position);
                    if (nodeInterface.isTitle()) return 4;
                    if (itemViewType == VIEW_TYPE_BANNER || itemViewType == VIEW_TYPE_INDEX || itemViewType == VIEW_TYPE_ITEM_V || itemViewType == VIEW_TYPE_GROUP_TITLE) {
                        return 4;
                    } else if (itemViewType == VIEW_TYPE_ITEM_G_2) {
                        return 2;
                    } else if (itemViewType == VIEW_TYPE_ITEM_G_4) {
                        return 1;
                    }
                    return 1;
                }
            });
            gridLayoutManager.setSpanCount(gridLayoutManager.getSpanCount());
        }
    }

    public class BannerViewHolder extends RecyclerView.ViewHolder {

        public BannerViewHolder(View itemView) {
            super(itemView);
        }

        public void bindData(MultipleDataBean multipleDataBean) {

        }
    }

    public class IndexViewHolder extends RecyclerView.ViewHolder {

        public IndexViewHolder(View itemView) {
            super(itemView);
        }

        public void bindData(MultipleDataBean multipleDataBean) {

        }
    }

    public class GroupTitleViewHolder extends RecyclerView.ViewHolder {

        public GroupTitleViewHolder(View itemView) {
            super(itemView);
        }

        public void bindData(MultipleDataBean multipleDataBean) {

        }
    }

    public class VerticalViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTvRefresh;

        public VerticalViewHolder(View itemView) {
            super(itemView);
            mTvRefresh = (TextView) itemView.findViewById(R.id.tv_refresh);
        }

        public void bindData(final MultipleDataBean.DataBean dataBean) {
            if (dataBean.hasRefresh) {
                mTvRefresh.setVisibility(View.VISIBLE);
            } else {
                mTvRefresh.setVisibility(View.GONE);
            }

            mTvRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = mDatas.indexOf(dataBean.originalLists.get(0));
                    mDatas.removeAll(dataBean.originalLists);
                    notifyDataSetChanged();
                }
            });
        }
    }

    public class Grid2ViewHolder extends RecyclerView.ViewHolder {

        public Grid2ViewHolder(View itemView) {
            super(itemView);
        }

        public void bindData(MultipleDataBean.DataBean dataBean) {

        }
    }

    public class Grid4ViewHolder extends RecyclerView.ViewHolder {

        public Grid4ViewHolder(View itemView) {
            super(itemView);
        }

        public void bindData(MultipleDataBean.DataBean dataBean) {

        }
    }

    public class EmptyViewHolder extends RecyclerView.ViewHolder {

        public EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
