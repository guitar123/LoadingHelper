package zz.itcast.cn.loadinghelper;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * author: ${user}
 * Created by Mr.fan on 2017/6/4.
 * descrition: You share rose get fun. *_* *_*
 */

public class SimpleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final Context mContext;
    private List<String> mList;
    private final LayoutInflater mInflate;

    public SimpleAdapter(Context context, List<String> list) {
        this.mContext = context;
        this.mList = list;
        this.mInflate = LayoutInflater.from(mContext);
    }

    public void setDataList(List<String> dataList) {
       this.mList = dataList;
        notifyDataSetChanged();
    }

    public void addDataList(List<String> dataList) {
        if (mList != null && dataList != null && dataList.size() > 0) {
            int start = mList.size();
            mList.addAll(dataList);
            notifyItemRangeInserted(start, dataList.size());
            Log.d("fhp", "notifyItemRangeInserted  start=" + start + " size=" + dataList.size());
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(mInflate.inflate(R.layout.item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindHolder((ItemViewHolder) holder, position);
    }

    private void bindHolder(ItemViewHolder holder, int position) {
        holder.tv_item.setText(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_item;

        public ItemViewHolder(View itemView) {
            super(itemView);
            this.tv_item = (TextView) itemView.findViewById(R.id.tv_item);
        }
    }

}
