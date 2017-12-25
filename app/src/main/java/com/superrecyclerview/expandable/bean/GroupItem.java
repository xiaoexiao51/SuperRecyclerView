package com.superrecyclerview.expandable.bean;

import java.util.List;

/**
 * author：Drawthink
 * describe:
 * date: 2017/5/22
 * T 为group数据对象
 * S 为child数据对象
 */
public class GroupItem<T, S> {

    private T groupData;

    private List<S> childDatas;

    // 是否父节点
    private boolean isParent = true;

    // 是否展开,  默认展开
    private boolean isExpand = true;

    public boolean isParent() {
        return isParent;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void onExpand() {
        isExpand = !isExpand;
    }

    public GroupItem(T groupData, List<S> childDatas, boolean isExpand) {
        this.groupData = groupData;
        this.childDatas = childDatas;
        this.isExpand = isExpand;
    }

    public T getGroupData() {
        return groupData;
    }

    public List<S> getChildDatas() {
        return childDatas;
    }

    public boolean hasChilds() {
        if (getChildDatas() == null || getChildDatas().isEmpty()) {
            return false;
        }
        return true;
    }
}
