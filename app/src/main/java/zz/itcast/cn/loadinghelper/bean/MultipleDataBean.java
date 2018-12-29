package zz.itcast.cn.loadinghelper.bean;

import java.util.List;

public class MultipleDataBean implements NodeInterface<MultipleDataBean> {

    public String title;
    public int id;
    public String action;
    public List<DataBean> lists;
    public int viewType;

    public List<BannerBean> banners;

    public List<IndexBean> indexs;

    private boolean isTitle;

    @Override
    public MultipleDataBean getData() {
        return this;
    }

    @Override
    public int getItemViewType() {
        return viewType;
    }

    @Override
    public void setIsTitle(boolean isTitle) {
            this.isTitle = isTitle;
    }

    @Override
    public boolean isTitle() {
        return isTitle;
    }


    public static class DataBean implements NodeInterface<DataBean> {
        public String title;
        public String content;
        public int id;
        public boolean hasRefresh;
        public int viewType;
        public List<DataBean> originalLists;

        @Override
        public DataBean getData() {
            return this;
        }

        @Override
        public int getItemViewType() {
            return viewType;
        }

        @Override
        public void setIsTitle(boolean isTitle) {

        }

        @Override
        public boolean isTitle() {
            return false;
        }


    }

    public static class BannerBean implements NodeInterface<BannerBean> {
        public int id;
        public String action;
        public String content;

        @Override
        public BannerBean getData() {
            return this;
        }

        @Override
        public int getItemViewType() {
            return 0;
        }

        @Override
        public void setIsTitle(boolean isTitle) {

        }

        @Override
        public boolean isTitle() {
            return false;
        }

    }

    public static class IndexBean implements NodeInterface<IndexBean> {
        public int id;
        public String action;
        public String title;

        @Override
        public IndexBean getData() {
            return this;
        }

        @Override
        public int getItemViewType() {
            return 0;
        }

        @Override
        public void setIsTitle(boolean isTitle) {

        }

        @Override
        public boolean isTitle() {
            return false;
        }


    }
}
