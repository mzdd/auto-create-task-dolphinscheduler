package com.mzdd.dolphinscheduler.node;

import java.util.List;

/**
 * @author mzdd
 * @create 2023-03-07 11:02
 */
public class MyTreeNode extends DsTaskNode {

    /**
     * 添加一个子节点列表属性
     */
    private List<MyTreeNode> children;

    public List<MyTreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<MyTreeNode> children) {
        this.children = children;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
