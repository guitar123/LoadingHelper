package zz.itcast.cn.loadinghelper.bean;

public interface NodeInterface<T> {

    T getData();

    int getItemViewType();

    void setIsTitle(boolean isTitle);

    boolean isTitle();
}
