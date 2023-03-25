## 一、主要背景

在基于doris建立数仓时，使用dolphinscheduler进行调度（配置参数一致，基本属于一套模板、手动创建任务量大且易出错，需要精准控制任务依赖关系最大化利用性能）前提下，实现自动识别分析各个sql脚本的血缘关系，调用dolphinscheduler的接口自动创建工作流和任务定义。



## 二、主要流程：

1. 从指定的git仓库拉取文件并过滤出所有的sql文件
2. 使用阿里的druid中的SQLParser相关实现对sql进行血缘解析
3. 根据解析的血缘关系将所有的sql以文件为单位建立节点，并组装成树型结构
4. 将树型结构转换成dolphinscheduler的创建工作流的对象并调用接口进行创建



## 三、关键解析

### 1.血缘解析

```java
/**
     * sql解析
     *
     * @param sqlStr
     * @return
     */
    public static Map<String, Set<String>> sqlParser(String sqlStr) {
        List<String> sqlList = StrUtil.split(sqlStr, ";");

        Map<String, Set<String>> map = new HashMap<>();
        for (String sql : sqlList) {
            if (StrUtil.isBlank(sql)) {
                continue;
            }
            // 这里使用的时 mysql 解析
            MySqlStatementParser parser = new MySqlStatementParser(sql);
            SQLStatement sqlStatement = parser.parseStatement();
            MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
            sqlStatement.accept(visitor);
            Map<TableStat.Name, TableStat> tableStatMap = visitor.getTables();
            for (Map.Entry<TableStat.Name, TableStat> tableStatEntry : tableStatMap.entrySet()) {
                String name = tableStatEntry.getKey().getName();
                // 这里的 value 有两种 Select（父级）、Insert（子级）
                String value = tableStatEntry.getValue().toString();
                if (map.containsKey(value)) {
                    map.get(value).add(name);
                } else {
                    Set<String> list = new HashSet<>();
                    list.add(name);
                    map.put(value, list);
                }
            }
        }
        return map;
    }
```

### 2.树型节点组装

#### 2.1 节点对象定义

```java
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * @author mzdd
 * @create 2023-03-06 18:31
 */
public class DsTaskNode implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 源表
     */
    private List<String> sourceTableName = new ArrayList<>();

    /**
     * 目标表
     */
    private String targetTableName;

    /**
     * 源sql
     */
    private String sql;

    /**
     * 用sql做一个MD5签名
     */
    private String md5;

    /**
     * 用sql名称
     */
    private String name;

    /**
     * 任务code
     */
    private Long taskCode;

    public List<String> getSourceTableName() {
        return sourceTableName;
    }

    public void setSourceTableName(List<String> sourceTableName) {
        this.sourceTableName = sourceTableName;
    }

    public String getTargetTableName() {
        return targetTableName;
    }

    public void setTargetTableName(String targetTableName) {
        this.targetTableName = targetTableName;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getMd5() {
        if (StrUtil.isBlank(this.md5)) {
            this.md5 = DigestUtil.md5Hex(getSql());
        }
        return md5;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(Long taskCode) {
        this.taskCode = taskCode;
    }
}
```

#### 2.2 组装实现

```java
/**
     * 解析sql,并组装node
     *
     * @param files
     * @return
     */
private static List<MyTreeNode> treeNodeProcess(List<File> files) {
    List<MyTreeNode> sourceList = new ArrayList<>();
    for (File sqlFile : files) {
        // 1 取出里面的 sql 脚本内容
        String sql = FileUtil.readUtf8String(sqlFile);
        // 2 解析里面的脚本内容
        Map<String, Set<String>> map = null;
        try {
            // 血缘解析
            map = AutoCreateTask.sqlParser(sql);
        } catch (Exception e) {
            log.error(" table-parser error: {}", sqlFile.getPath());
        }
        // 3 将每一个sql的 source , target 解析出来
        if (ObjectUtil.isNotNull(map)) {
            MyTreeNode treeNode = new MyTreeNode();
            treeNode.setName(sqlFile.getName());
            if (map.containsKey("Select")) {
                Set<String> select = map.get("Select");
                treeNode.setSourceTableName(new ArrayList<>(select));
            }
            treeNode.setSql(sql);
            if (map.containsKey("Insert")) {
                Set<String> insert = map.get("Insert");
                treeNode.setTargetTableName(new ArrayList<>(insert).get(0));
            }
            sourceList.add(treeNode);
        }
    }
    // 将sql按照 source , target 组成 树状结构
    return TreeUtil.getTree(sourceList);
}

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
```

## 四、注意点

- sql脚本内容编写规范，注释“--”后面一定要接一个空格=》“-- xxx”
- 在RunParamConstant中修改启动常量（dolphinscheduler）
- 需要先从dolphinscheduler平台手动创建一个模板工作流和任务，以获取固定的参数，并修改到对应的类中（TaskDefinitionLog、TaskParams、ProcessTaskRelationLog）有参构造方法中

![1679747681720](.\assets\1679747681720.png)

![1679747723571](.\assets\1679747723571.png)

![1679747759752](.\assets\1679747759752.png)

- 启动入口AutoCreateTask.mian 

![1679747813612](.\assets\1679747813612.png)



