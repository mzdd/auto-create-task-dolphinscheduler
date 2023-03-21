/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mzdd.dolphinscheduler.ds.entity;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONArray;

import java.io.Serializable;
import java.util.*;

/**
 * task definition
 */
public class TaskDefinition implements Serializable {

    private static final Long serialVersionUID = 1L;

    /**
     * id
     */
    private Integer id;

    /**
     * code
     */
    private Long code;

    /**
     * name
     */
    private String name;

    /**
     * version
     */
    private Integer version;

    /**
     * description
     */
    private String description;

    /**
     * project code
     */
    private Long projectCode;

    /**
     * task user id
     */
    private Integer userId;

    /**
     * task type
     */
    private String taskType;

    /**
     * user defined parameters
     */
    private TaskParams taskParams;

    /**
     * user defined parameter list
     */
    private List<Property> taskParamList;

    /**
     * user define parameter map
     */
    private Map<String, String> taskParamMap;

    /**
     * task is valid: yes/no
     */
    private String flag;

    /**
     * task priority
     */
    private String taskPriority;

    /**
     * user name
     */
    private String userName;

    /**
     * project name
     */
    private String projectName;

    /**
     * worker group
     */
    private String workerGroup;

    /**
     * environment code
     */
    private Long environmentCode;

    /**
     * fail retry times
     */
    private Integer failRetryTimes;

    /**
     * fail retry interval
     */
    private Integer failRetryInterval;

    /**
     * timeout flag
     */
    private String timeoutFlag;

    /**
     * timeout notify strategy
     */
    private String timeoutNotifyStrategy;

    /**
     * task warning time out. unit: minute
     */
    private Integer timeout;

    /**
     * delay execution time.
     */
    private Integer delayTime;

    /**
     * resource ids
     */
    private String resourceIds;

    /**
     * create time
     */
    private Date createTime;

    /**
     * update time
     */
    private Date updateTime;

    /**
     * modify user name
     */
    private String modifyBy;

    /**
     * task group id
     */
    private Integer taskGroupId;
    /**
     * task group id
     */
    private Integer taskGroupPriority;

    public TaskDefinition() {
    }

    public TaskDefinition(Long code, Integer version) {
        this.code = code;
        this.version = version;
    }

    public Integer getTaskGroupId() {
        return taskGroupId;
    }

    public void setTaskGroupId(Integer taskGroupId) {
        this.taskGroupId = taskGroupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public TaskParams getTaskParams() {
        return taskParams;
    }

    public void setTaskParams(TaskParams taskParams) {
        this.taskParams = taskParams;
    }

    public List<Property> getTaskParamList() {
        JSONArray localParams = taskParams.getLocalParams();
        if (localParams != null) {
            taskParamList = JSONArray.parseArray(localParams.toJSONString(), Property.class);
        }
        return taskParamList;
    }

    public void setTaskParamList(List<Property> taskParamList) {
        this.taskParamList = taskParamList;
    }

    public void setTaskParamMap(Map<String, String> taskParamMap) {
        this.taskParamMap = taskParamMap;
    }

    public Map<String, String> getTaskParamMap() {
        if (taskParamMap == null && !ObjectUtil.isNull(taskParams)) {
            JSONArray localParams = taskParams.getLocalParams();
            //If a jsonNode is null, not only use !=null, but also it should use the isNull method to be estimated.
            if (localParams != null) {
                List<Property> propList = JSONArray.parseArray(localParams.toJSONString(), Property.class);
                if (CollectionUtil.isNotEmpty(propList)) {
                    taskParamMap = new HashMap<>();
                    for (Property property : propList) {
                        taskParamMap.put(property.getProp(), property.getValue());
                    }
                }
            }
        }
        return taskParamMap;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Long getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(Long projectCode) {
        this.projectCode = projectCode;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getTaskPriority() {
        return taskPriority;
    }

    public void setTaskPriority(String taskPriority) {
        this.taskPriority = taskPriority;
    }

    public String getWorkerGroup() {
        return workerGroup;
    }

    public void setWorkerGroup(String workerGroup) {
        this.workerGroup = workerGroup;
    }

    public Integer getFailRetryTimes() {
        return failRetryTimes;
    }

    public void setFailRetryTimes(Integer failRetryTimes) {
        this.failRetryTimes = failRetryTimes;
    }

    public Integer getFailRetryInterval() {
        return failRetryInterval;
    }

    public void setFailRetryInterval(Integer failRetryInterval) {
        this.failRetryInterval = failRetryInterval;
    }

    public String getTimeoutNotifyStrategy() {
        return timeoutNotifyStrategy;
    }

    public void setTimeoutNotifyStrategy(String timeoutNotifyStrategy) {
        this.timeoutNotifyStrategy = timeoutNotifyStrategy;
    }

    public String getTimeoutFlag() {
        return timeoutFlag;
    }

    public void setTimeoutFlag(String timeoutFlag) {
        this.timeoutFlag = timeoutFlag;
    }

    public String getResourceIds() {
        return resourceIds;
    }

    public void setResourceIds(String resourceIds) {
        this.resourceIds = resourceIds;
    }

    public Integer getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(Integer delayTime) {
        this.delayTime = delayTime;
    }

//    public String getDependence() {
//        return JSONUtils.getNodeString(JSONUtils.toJsonString(taskParams), Constants.DEPENDENCE);
//    }

    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }

    public Long getEnvironmentCode() {
        return this.environmentCode;
    }

    public void setEnvironmentCode(Long environmentCode) {
        this.environmentCode = environmentCode;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        TaskDefinition that = (TaskDefinition) o;
        return failRetryTimes == that.failRetryTimes
            && failRetryInterval == that.failRetryInterval
            && timeout == that.timeout
            && delayTime == that.delayTime
            && Objects.equals(name, that.name)
            && Objects.equals(description, that.description)
            && Objects.equals(taskType, that.taskType)
            && Objects.equals(taskParams, that.taskParams)
            && flag == that.flag
            && taskPriority == that.taskPriority
            && Objects.equals(workerGroup, that.workerGroup)
            && timeoutFlag == that.timeoutFlag
            && timeoutNotifyStrategy == that.timeoutNotifyStrategy
            && (Objects.equals(resourceIds, that.resourceIds)
            || ("".equals(resourceIds) && that.resourceIds == null)
            || ("".equals(that.resourceIds) && resourceIds == null))
            && environmentCode == that.environmentCode
            && taskGroupId == that.taskGroupId
            && taskGroupPriority == that.taskGroupPriority;
    }

    @Override
    public String toString() {
        return "TaskDefinition{"
                + "id=" + id
                + ", code=" + code
                + ", name='" + name + '\''
                + ", version=" + version
                + ", description='" + description + '\''
                + ", projectCode=" + projectCode
                + ", userId=" + userId
                + ", taskType=" + taskType
                + ", taskParams='" + taskParams + '\''
                + ", taskParamList=" + taskParamList
                + ", taskParamMap=" + taskParamMap
                + ", flag=" + flag
                + ", taskPriority=" + taskPriority
                + ", userName='" + userName + '\''
                + ", projectName='" + projectName + '\''
                + ", workerGroup='" + workerGroup + '\''
                + ", failRetryTimes=" + failRetryTimes
                + ", environmentCode='" + environmentCode + '\''
                + ", taskGroupId='" + taskGroupId + '\''
                + ", taskGroupPriority='" + taskGroupPriority + '\''
                + ", failRetryInterval=" + failRetryInterval
                + ", timeoutFlag=" + timeoutFlag
                + ", timeoutNotifyStrategy=" + timeoutNotifyStrategy
                + ", timeout=" + timeout
                + ", delayTime=" + delayTime
                + ", resourceIds='" + resourceIds + '\''
                + ", createTime=" + createTime
                + ", updateTime=" + updateTime
                + '}';
    }

    public Integer getTaskGroupPriority() {
        return taskGroupPriority;
    }

    public void setTaskGroupPriority(Integer taskGroupPriority) {
        this.taskGroupPriority = taskGroupPriority;
    }
}
