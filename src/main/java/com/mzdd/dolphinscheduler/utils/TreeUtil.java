package com.mzdd.dolphinscheduler.utils;

import cn.hutool.core.collection.IterUtil;
import com.mzdd.dolphinscheduler.node.MyTreeNode;

import java.util.*;

/**
 * @author mzdd
 * @create 2023-03-06 18:47
 */
public class TreeUtil {

    /**
     * @param sourceList
     * @return
     */
    public static List<MyTreeNode> getTree(List<MyTreeNode> sourceList) {
        Map<String, Set<MyTreeNode>> sourceMap = new HashMap<>();
        Map<String, Set<MyTreeNode>> targetMap = new HashMap<>();
        for (MyTreeNode node : sourceList) {
            //源表Map
            List<String> subSourceTableList = node.getSourceTableName();
            if (IterUtil.isNotEmpty(subSourceTableList)) {
                for (String subSourceTable : subSourceTableList) {
                    if (sourceMap.containsKey(subSourceTable)) {
                        sourceMap.get(subSourceTable).add(node);
                    } else {
                        Set<MyTreeNode> nodeSet = new HashSet<>();
                        nodeSet.add(node);
                        sourceMap.put(subSourceTable, nodeSet);
                    }
                }
            }

            //目标表Map
            String targetTableName = node.getTargetTableName();
            if (targetMap.containsKey(targetTableName)) {
                targetMap.get(targetTableName).add(node);
            } else {
                Set<MyTreeNode> nodeSet = new HashSet<>();
                nodeSet.add(node);
                targetMap.put(targetTableName, nodeSet);
            }
        }
        //创建一个列表，用于存储根节点
        List<MyTreeNode> rootList = new ArrayList<>();
        for (MyTreeNode node : sourceList) {
            // 将子节点处理好
            String targetTableName = node.getTargetTableName();
            if (sourceMap.containsKey(targetTableName)) {
                List<MyTreeNode> childrenList = node.getChildren();
                if (IterUtil.isEmpty(childrenList)) {
                    childrenList = new ArrayList<>();
                    node.setChildren(childrenList);
                }
                childrenList.addAll(sourceMap.get(targetTableName));
            }

            List<String> subSourceTableList = node.getSourceTableName();
            boolean isRoot = true;
            for (String subSourceTable : subSourceTableList) {
                if (targetMap.containsKey(subSourceTable)) {
                    isRoot = false;
                    break;
                }
            }
            if (isRoot) {
                rootList.add(node);
            }
        }
        return rootList;
    }

    public static void main(String[] args) {
        //创建一个源数据列表
        List<MyTreeNode> sourceList = new ArrayList<>();
        //创建第一个节点，设置其源表名、目标表名和sql语句，并添加到源数据列表中
        MyTreeNode node1 = new MyTreeNode();
        node1.setSourceTableName(Arrays.asList("table1", "table2"));
        node1.setTargetTableName("table3");
        node1.setSql("select * from table1 join table2 on table1.id = table2.id");
        node1.setName("table1+table2=>table3");
        sourceList.add(node1);
        //创建第二个节点，设置其源表名、目标表名和sql语句，并添加到源数据列表中
        MyTreeNode node2 = new MyTreeNode();
        node2.setSourceTableName(Arrays.asList("table3", "table4"));
        node2.setTargetTableName("table7");
        node2.setSql("select * from table3 join table4 on table3.id = table4.id");
        node2.setName("table3+table4=>table7");
        sourceList.add(node2);
        //创建第三个节点，设置其源表名、目标表名和sql语句，并添加到源数据列表中
        MyTreeNode node3 = new MyTreeNode();
        node3.setSourceTableName(Arrays.asList("table5", "table6"));
        node3.setTargetTableName("table4");
        node3.setSql("select * from table5 join table6 on table5.id = table6.id");
        node3.setName("table5+table6=>table4");
        sourceList.add(node3);

        //调用TreeUtil的getTree方法，将源数据转换为树形结构数据
        List<MyTreeNode> treeList = TreeUtil.getTree(sourceList);

        //打印结果
        for (MyTreeNode root : treeList) {
            //打印根节点信息
            System.out.println(root.toString());
            //递归打印子节点信息
            printChildren(root);
        }
    }

    /**
     * 定义一个辅助方法，用于递归打印子节点信息
     */
    public static void printChildren(MyTreeNode parent) {
        if (parent.getChildren() != null && !parent.getChildren().isEmpty()) {
            for (MyTreeNode child : parent.getChildren()) {
                //打印子节点信息
                System.out.println(child.toString());
                //递归打印孙子节点信息
                printChildren(child);
            }
        }
    }

    public static Set<MyTreeNode> getAllNodes(List<MyTreeNode> treeNodes) {
        Set<MyTreeNode> all = new HashSet<>();
        for (MyTreeNode treeNode : treeNodes) {
            getNodes(treeNode, all);
        }
        return all;
    }

    public static void getNodes(MyTreeNode node, Set<MyTreeNode> all) {
        //将当前节点加入
        all.add(node);
        //判断下有没有子节点
        List<MyTreeNode> children = node.getChildren();
        if (IterUtil.isNotEmpty(children)) {
            for (MyTreeNode child : children) {
                //递归打印孙子节点信息
                getNodes(child, all);
            }
        }
    }

}
