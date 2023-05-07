package zz.itcast.cn.loadinghelper.adapter;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import zz.itcast.cn.loadinghelper.DataManager;
import zz.itcast.cn.loadinghelper.R;
import zz.itcast.cn.loadinghelper.activity.BannerAdapter;
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

        if (nodeInterface.isParentNode()) return VIEW_TYPE_GROUP_TITLE;

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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payloads) {
        if (payloads != null && !payloads.isEmpty() && payloads.get(0) instanceof MultipleDataBean) {
            MultipleDataBean multipleDataBean = (MultipleDataBean) payloads.get(0);
            if (mDatas.get(position) instanceof MultipleDataBean && holder instanceof GroupTitleViewHolder && multipleDataBean.isParentNode()) {
                ((GroupTitleViewHolder) holder).bindCountDownTimer(multipleDataBean);
            }
        } else {
            onBindViewHolder(holder, position);
        }
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
                    if (itemViewType == VIEW_TYPE_ITEM_G_2) {
                        return 2;
                    } else if (itemViewType == VIEW_TYPE_ITEM_G_4) {
                        return 1;
                    } else {
                        return 4;
                    }
                }
            });
            gridLayoutManager.setSpanCount(gridLayoutManager.getSpanCount());
        }
    }

    public class BannerViewHolder extends RecyclerView.ViewHolder {

        private final ViewPager viewPager;

        public BannerViewHolder(View itemView) {
            super(itemView);
            viewPager = (ViewPager) itemView.findViewById(R.id.viewPage);
        }

        public void bindData(MultipleDataBean multipleDataBean) {
            BannerAdapter adapter = new BannerAdapter(multipleDataBean.banners);
            viewPager.setAdapter(adapter);
        }
    }

    public class IndexViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvIndex1;
        private final TextView tvIndex2;
        private final TextView tvIndex3;
        private final TextView tvIndex4;

        public IndexViewHolder(View itemView) {
            super(itemView);
            tvIndex1 = (TextView) itemView.findViewById(R.id.tv_index1);
            tvIndex2 = (TextView) itemView.findViewById(R.id.tv_index2);
            tvIndex3 = (TextView) itemView.findViewById(R.id.tv_index3);
            tvIndex4 = (TextView) itemView.findViewById(R.id.tv_index4);
        }

        public void bindData(MultipleDataBean multipleDataBean) {
            tvIndex1.setText(multipleDataBean.indexs.get(0).title);
            tvIndex2.setText(multipleDataBean.indexs.get(1).title);
            tvIndex3.setText(multipleDataBean.indexs.get(2).title);
            tvIndex4.setText(multipleDataBean.indexs.get(3).title);
        }
    }

    public class GroupTitleViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTimer;

        public GroupTitleViewHolder(View itemView) {
            super(itemView);
            tvTimer = (TextView) itemView.findViewById(R.id.tv_timer);
        }

        public void bindData(MultipleDataBean multipleDataBean) {
            if (multipleDataBean.activityEndTimeMillisecond <= 0L) {
                tvTimer.setVisibility(View.GONE);
            } else {
                tvTimer.setVisibility(View.VISIBLE);
                tvTimer.setText(multipleDataBean.getDiffTimeStringWithCurrent());
            }
        }

        public void bindCountDownTimer(MultipleDataBean multipleDataBean) {
            if (multipleDataBean.activityEndTimeMillisecond <= 0L) {
                tvTimer.setVisibility(View.GONE);
            } else {
                tvTimer.setVisibility(View.VISIBLE);
                tvTimer.setText(multipleDataBean.getDiffTimeStringWithCurrent());
            }
        }
    }

    public class VerticalViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTvRefresh;
        private final TextView tvTitle;
        private final TextView tvContent;

        public VerticalViewHolder(View itemView) {
            super(itemView);
            mTvRefresh = (TextView) itemView.findViewById(R.id.tv_refresh);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
        }

        public void bindData(final MultipleDataBean.DataBean dataBean) {
            if (dataBean.hasRefresh) {
                mTvRefresh.setVisibility(View.VISIBLE);
            } else {
                mTvRefresh.setVisibility(View.GONE);
            }


            tvTitle.setText(dataBean.title);
            tvContent.setText(dataBean.content);

            mTvRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击刷新

                    MultipleDataBean parentNode = dataBean.parentNode;
                    List<MultipleDataBean.DataBean> childs = parentNode.lists;
                    int parentPosition = mDatas.indexOf(parentNode);
                    mDatas.removeAll(childs);//移除老数据

                    List<MultipleDataBean.DataBean> newDatas = DataManager.getInstance().refreshChildDatas();
                    parentNode.lists = newDatas;//记录新数据

                    for (int i = 0; i < newDatas.size(); i++) {
                        MultipleDataBean.DataBean data = newDatas.get(i);
                        data.parentNode = parentNode;
                        data.viewType = parentNode.viewType;
                        if (i == newDatas.size() - 1) {
                            data.hasRefresh = true;
                        }
                    }
                    //添加新数据
                    mDatas.addAll(parentPosition + 1, newDatas);

                    if (newDatas.size() == childs.size()) {
                        notifyItemRangeChanged(parentPosition + 1, newDatas.size());
                    } else {
                        notifyDataSetChanged();
                    }
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
