package zz.itcast.cn.loadinghelper.bean;

public interface NodeInterface<T> {

    int getItemViewType();

    void setIsParentNode(boolean isParentNode);

    boolean isParentNode();
}
