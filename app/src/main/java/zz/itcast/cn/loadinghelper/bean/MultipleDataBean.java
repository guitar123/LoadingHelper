package zz.itcast.cn.loadinghelper.bean;

import java.util.List;

public class MultipleDataBean implements NodeInterface<MultipleDataBean> {

    public String title;
    public int id;
    public String action;
    public List<DataBean> lists;
    public int viewType;
    //活动结束时间
    public long activityEndTimeMillisecond = -1L;
    //当前时间
    public long currentTimeMillisencond = -1L;

    private long getDiffTimeMillWithCurrent() {
        return activityEndTimeMillisecond - currentTimeMillisencond;
    }

    public String getDiffTimeStringWithCurrent() {
        if (activityEndTimeMillisecond <= 0) return null;

        if (getDiffTimeMillWithCurrent() <= 0) return "0天0时0分0秒";

        //计算天
        long days = getDiffTimeMillWithCurrent() / (1000 * 60 * 60 * 24);
        //计算小时
        long hours = (getDiffTimeMillWithCurrent() - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        //计算分钟
        long minutes = (getDiffTimeMillWithCurrent() - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
        //计算秒
        long second = (getDiffTimeMillWithCurrent() - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60) - minutes * (1000 * 60)) / 1000;

        return days + "天" + hours + "时" + minutes + "分" + second + "秒";
    }

    public List<BannerBean> banners;

    public List<IndexBean> indexs;

    private boolean isParentNode;

    @Override
    public int getItemViewType() {
        return viewType;
    }

    @Override
    public void setIsParentNode(boolean isParentNode) {
        this.isParentNode = isParentNode;
    }

    @Override
    public boolean isParentNode() {
        return isParentNode;
    }


    public static class DataBean implements NodeInterface<DataBean> {
        public String title;
        public String content;
        public int id;
        public boolean hasRefresh;
        public int viewType;

        public MultipleDataBean parentNode;

        @Override
        public int getItemViewType() {
            return viewType;
        }

        @Override
        public void setIsParentNode(boolean isParentNode) {

        }

        @Override
        public boolean isParentNode() {
            return false;
        }


    }

    public static class BannerBean implements NodeInterface<BannerBean> {
        public int id;
        public String action;
        public String content;

        @Override
        public int getItemViewType() {
            return 0;
        }

        @Override
        public void setIsParentNode(boolean isParentNode) {

        }

        @Override
        public boolean isParentNode() {
            return false;
        }

    }

    public static class IndexBean implements NodeInterface<IndexBean> {
        public int id;
        public String action;
        public String title;

        @Override
        public int getItemViewType() {
            return 0;
        }

        @Override
        public void setIsParentNode(boolean isParentNode) {

        }

        @Override
        public boolean isParentNode() {
            return false;
        }


    }
}
