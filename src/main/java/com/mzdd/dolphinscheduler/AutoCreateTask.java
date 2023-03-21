package com.mzdd.dolphinscheduler;

import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import com.mzdd.dolphinscheduler.git.GitUtil;
import com.mzdd.dolphinscheduler.ds.entity.ProcessTaskRelationLog;
import com.mzdd.dolphinscheduler.ds.entity.TaskCodeLocation;
import com.mzdd.dolphinscheduler.ds.entity.TaskDefinitionLog;
import com.mzdd.dolphinscheduler.ds.entity.TaskParams;
import com.mzdd.dolphinscheduler.node.MyTreeNode;
import com.mzdd.dolphinscheduler.utils.DsApiUtil;
import com.mzdd.dolphinscheduler.utils.TreeUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileFilter;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

/**
 * 自动创建处理
 *
 * @author mzdd
 * @create 2023-03-03 10:59
 */
@Slf4j
public class AutoCreateTask {

    public static void main(String[] args) {
        // 1. 拉取代码并存入文件
        File file = gitProcess(RunParamConstant.LOCAL_PATCH, RunParamConstant.GIT_URL, RunParamConstant.GIT_BRANCH, RunParamConstant.GIT_USER_NAME, RunParamConstant.GIT_PASSWORD);
        // 2. 过滤出其中sql文件
        List<File> files = sqlFileProcess(file);
        // 3. 解析sql脚本,并组装node节点信息
        List<MyTreeNode> treeList = treeNodeProcess(files);
        // 4. 调用ds的接口创建 工作流，创建任务
        dsApiProcess(treeList);
    }

    /**
     * 调用ds的接口创建 工作流，创建任务
     *
     * @param treeNodes 树形节点信息
     */
    public static void dsApiProcess(List<MyTreeNode> treeNodes) {
        // 采用时间戳做名称后缀
        long now = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        //1. 拆分所有的节点信息
        Set<MyTreeNode> allNodes = TreeUtil.getAllNodes(treeNodes);
        if (IterUtil.isEmpty(allNodes)) {
            log.info("allNodes isEmpty ");
            return;
        }
        //2. 根据所有的节点信息，批量获取 任务taskcode
        List<Long> taskCodeList = DsApiUtil.genTaskCodes(allNodes.size());
        if (IterUtil.isEmpty(taskCodeList) || taskCodeList.size() != allNodes.size()) {
            log.info("taskCodeList isEmpty or  taskCodeList.size {} != allNodes.size{} ", taskCodeList.size(), allNodes.size());
            return;
        }
        //3. node节循环：组装任务task、组装任务节点的位置信息
        List<TaskDefinitionLog> taskDefinitionLogs = new ArrayList<>();
        List<TaskCodeLocation> locations = new ArrayList<>();
        int i = 0;
        for (MyTreeNode node : allNodes) {
            // taskCode赋值
            Long taskCode = taskCodeList.get(i);
            node.setTaskCode(taskCode);
            String sql = node.getSql();
            String name = RunParamConstant.NAME_PREFIX + node.getName() + "_" + now;

            // 组装任务task
            TaskParams taskParams = new TaskParams(sql);
            TaskDefinitionLog taskDefinitionLog = new TaskDefinitionLog(taskCode, "", name, taskParams);
            taskDefinitionLogs.add(taskDefinitionLog);

            // 组装任务节点的位置信息
            TaskCodeLocation location = new TaskCodeLocation();
            location.setTaskCode(taskCode);
            location.setX(0);
            location.setY(0);
            locations.add(location);
            i++;
        }

        //4. 组装所有的node连线关系
        List<ProcessTaskRelationLog> taskRelationList = new ArrayList<>();
        for (MyTreeNode treeNode : treeNodes) {
            //将父节点加入
            ProcessTaskRelationLog relation = new ProcessTaskRelationLog(treeNode.getTaskCode());
            taskRelationList.add(relation);
            getTaskRelation(treeNode, taskRelationList);
        }

        String processDefinitionName = RunParamConstant.NAME_PREFIX + now;
        DsApiUtil.createProcessDefinition(processDefinitionName, taskDefinitionLogs, taskRelationList, locations);
    }

    /**
     * 递归组装这个，关联关系
     *
     * @param node
     * @param taskRelationList
     */
    public static void getTaskRelation(MyTreeNode node, List<ProcessTaskRelationLog> taskRelationList) {
        //判断下有没有子节点
        List<MyTreeNode> children = node.getChildren();
        if (IterUtil.isEmpty(children)) {
            return;
        }
        for (MyTreeNode child : children) {
            //将关联关系记录
            ProcessTaskRelationLog relation = new ProcessTaskRelationLog(node.getTaskCode(), child.getTaskCode());
            taskRelationList.add(relation);
            //递归打印孙子节点信息
            getTaskRelation(child, taskRelationList);
        }
    }


    /**
     * 拉取代码并存入文件
     *
     * @return
     */
    private static File gitProcess(String localPatch, String url, String branch, String userName, String password) {
        File file = FileUtil.file(localPatch);
        GitUtil.clone(file, url, branch, userName, password);
        return file;
    }

    /**
     * sql文件过滤
     *
     * @param file 文件夹
     * @return
     */
    private static List<File> sqlFileProcess(File file) {
        FileFilter filter = new FileFilter() {
            /**
             * 过滤规则：
             *         accept方法中，判断file对象是否是以.sql结尾，
             *         如果是，返回true，否则返回false
             * @param pathname file传递过来的路径名称
             * @return true or false
             */
            @Override
            public boolean accept(File pathname) {
                if (pathname.isDirectory()) {
                    return true;
                }
                return pathname.getName().endsWith(".sql");
            }
        };
        return FileUtil.loopFiles(file, filter);
    }

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
            MySqlStatementParser parser = new MySqlStatementParser(sql);
            SQLStatement sqlStatement = parser.parseStatement();
            MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
            sqlStatement.accept(visitor);
            Map<TableStat.Name, TableStat> tableStatMap = visitor.getTables();
            for (Map.Entry<TableStat.Name, TableStat> tableStatEntry : tableStatMap.entrySet()) {
                String name = tableStatEntry.getKey().getName();
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

}
