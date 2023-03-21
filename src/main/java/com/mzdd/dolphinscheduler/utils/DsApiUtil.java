package com.mzdd.dolphinscheduler.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mzdd.dolphinscheduler.RunParamConstant;
import com.mzdd.dolphinscheduler.ds.entity.ProcessTaskRelationLog;
import com.mzdd.dolphinscheduler.ds.entity.TaskCodeLocation;
import com.mzdd.dolphinscheduler.ds.entity.TaskDefinitionLog;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * dolphinscheduler接口的访问工具类
 *
 * @author mzdd
 * @create 2023-03-07 14:31
 */
@Slf4j
public class DsApiUtil {

    /**
     * 创建工作流
     *
     * @param name               工作流名称
     * @param taskDefinitionLogs 任务定义信息
     * @param taskRelationList   任务关系信息
     * @param locations          任务视图位置信息
     */
    public static void createProcessDefinition(String name, List<TaskDefinitionLog> taskDefinitionLogs, List<ProcessTaskRelationLog> taskRelationList, List<TaskCodeLocation> locations) {
        String url = RunParamConstant.DS_DOMAIN +
                "/dolphinscheduler/projects/" +
                RunParamConstant.DS_PROJECT_ID +
                "/process-definition";
        Map<String, Object> paramMap = new HashMap<>(13);

        String taskDefinitionLogsStr = JSONUtils.toJsonString(taskDefinitionLogs);
        // 替换 换行符
        taskDefinitionLogsStr = StrUtil.replace(taskDefinitionLogsStr, "\\r\\n", "\\n");
        log.debug("taskDefinitionLogsStr : {}", taskDefinitionLogsStr);
        String taskRelationListStr = JSONUtils.toJsonString(taskRelationList);
        log.debug("taskRelationListStr : {}", taskRelationListStr);
        String locationsStr = JSONUtils.toJsonString(locations);
        log.debug("locationsStr : {}", locationsStr);

        paramMap.put("taskDefinitionJson", taskDefinitionLogsStr);
        paramMap.put("taskRelationJson", taskRelationListStr);
        paramMap.put("locations", locationsStr);
        paramMap.put("name", name);
        paramMap.put("tenantCode", "default");
        paramMap.put("executionType", "PARALLEL");
        paramMap.put("description", name);
        paramMap.put("globalParams", RunParamConstant.DS_GLOBAL_PARAMS);
        paramMap.put("timeout", "0");
        String body = HttpRequest.post(url)
                //头信息，多个头信息多次调用此方法即可
                .header("token", RunParamConstant.DS_TOKEN)
                //表单内容
                .form(paramMap)
                //超时，毫秒
                .timeout(20000)
                .execute().body();
        log.debug("createProcessDefinition body {}", body);
        JSONObject js = processBody(body);
        log.debug("createProcessDefinition result {}", js);
    }

    /**
     * 预创建任务taskCode的
     * ds size 在 0 ~ 100
     *
     * @param genNum code数量
     */
    public static List<Long> genTaskCodes(int genNum) {
        List<Long> all = new ArrayList<>();
        int k = genNum / 100;
        int y = genNum % 100;
        for (int i = 0; i < k; i++) {
            genTaskCodes(100, all);
        }
        if (y > 0) {
            genTaskCodes(y, all);
        }
        return all;
    }

    /**
     * 预创建任务taskCode
     *
     * @param genNum code数量
     */
    public static void genTaskCodes(int genNum, List<Long> list) {
        String url = RunParamConstant.DS_DOMAIN +
                "/dolphinscheduler/projects/" +
                RunParamConstant.DS_PROJECT_ID +
                "/task-definition/gen-task-codes?genNum=" +
                genNum;
        String body = HttpRequest.get(url)
                //头信息，多个头信息多次调用此方法即可
                .header("token", RunParamConstant.DS_TOKEN)
                //超时，毫秒
                .timeout(20000)
                .execute().body();
        log.debug("genTaskCodes body {}", body);
        JSONObject js = processBody(body);
        JSONArray data = js.getJSONArray("data");
        int size = data.size();
        for (int i = 0; i < size; i++) {
            list.add((Long) data.get(i));
        }
        return;
    }

    public static JSONObject processBody(String body) {
        try {
            JSONObject js = JSONObject.parseObject(body);
            Boolean success = js.getBoolean("success");
            if (success) {
                return js;
            }
        } catch (Exception e) {
            log.error("processBody error", e);
        }
        return null;
    }

    public static void main(String[] args) {
        List<Long> list = DsApiUtil.genTaskCodes(1);
        System.out.println(list);
    }

}
