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

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * process task relation
 */
public class ProcessTaskRelation implements Serializable {

    private static final Long serialVersionUID = 1L;

    /**
     * id
     */
    private Integer id;

    /**
     * name
     */
    private String name;

    /**
     * process version
     */
    private Integer processDefinitionVersion;

    /**
     * project code
     */
    private Long projectCode;

    /**
     * process code
     */
    private Long processDefinitionCode;

    /**
     * pre task code
     */
    private Long preTaskCode;

    /**
     * pre node version
     */
    private Integer preTaskVersion;

    /**
     * post task code
     */
    private Long postTaskCode;

    /**
     * post node version
     */
    private Integer postTaskVersion;

    /**
     * condition type
     */
    private String conditionType;

    /**
     * condition parameters
     */
    private String conditionParams;

    /**
     * create time
     */
    private Date createTime;

    /**
     * update time
     */
    private Date updateTime;

    public ProcessTaskRelation() {
    }

    public ProcessTaskRelation(String name,
                               Integer processDefinitionVersion,
                               Long projectCode,
                               Long processDefinitionCode,
                               Long preTaskCode,
                               Integer preTaskVersion,
                               Long postTaskCode,
                               Integer postTaskVersion,
                               String conditionType,
                               String conditionParams,
                               Date createTime,
                               Date updateTime) {
        this.name = name;
        this.processDefinitionVersion = processDefinitionVersion;
        this.projectCode = projectCode;
        this.processDefinitionCode = processDefinitionCode;
        this.preTaskCode = preTaskCode;
        this.preTaskVersion = preTaskVersion;
        this.postTaskCode = postTaskCode;
        this.postTaskVersion = postTaskVersion;
        this.conditionType = conditionType;
        this.conditionParams = conditionParams;
        this.createTime = createTime;
        this.updateTime = updateTime;
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

    public String getConditionParams() {
        return conditionParams;
    }

    public void setConditionParams(String conditionParams) {
        this.conditionParams = conditionParams;
    }

    public Integer getProcessDefinitionVersion() {
        return processDefinitionVersion;
    }

    public void setProcessDefinitionVersion(Integer processDefinitionVersion) {
        this.processDefinitionVersion = processDefinitionVersion;
    }

    public Long getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(Long projectCode) {
        this.projectCode = projectCode;
    }

    public Long getProcessDefinitionCode() {
        return processDefinitionCode;
    }

    public void setProcessDefinitionCode(Long processDefinitionCode) {
        this.processDefinitionCode = processDefinitionCode;
    }

    public Long getPreTaskCode() {
        return preTaskCode;
    }

    public void setPreTaskCode(Long preTaskCode) {
        this.preTaskCode = preTaskCode;
    }

    public Long getPostTaskCode() {
        return postTaskCode;
    }

    public void setPostTaskCode(Long postTaskCode) {
        this.postTaskCode = postTaskCode;
    }

    public String getConditionType() {
        return conditionType;
    }

    public void setConditionType(String conditionType) {
        this.conditionType = conditionType;
    }

    public Integer getPreTaskVersion() {
        return preTaskVersion;
    }

    public void setPreTaskVersion(Integer preTaskVersion) {
        this.preTaskVersion = preTaskVersion;
    }

    public Integer getPostTaskVersion() {
        return postTaskVersion;
    }

    public void setPostTaskVersion(Integer postTaskVersion) {
        this.postTaskVersion = postTaskVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProcessTaskRelation that = (ProcessTaskRelation) o;
        return processDefinitionVersion == that.processDefinitionVersion
            && projectCode == that.projectCode
            && processDefinitionCode == that.processDefinitionCode
            && preTaskCode == that.preTaskCode
            && preTaskVersion == that.preTaskVersion
            && postTaskCode == that.postTaskCode
            && postTaskVersion == that.postTaskVersion
            && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, processDefinitionVersion, projectCode, processDefinitionCode, preTaskCode, preTaskVersion, postTaskCode, postTaskVersion);
    }

    @Override
    public String toString() {
        return "ProcessTaskRelation{"
            + "id=" + id
            + ", name='" + name + '\''
            + ", processDefinitionVersion=" + processDefinitionVersion
            + ", projectCode=" + projectCode
            + ", processDefinitionCode=" + processDefinitionCode
            + ", preTaskCode=" + preTaskCode
            + ", preTaskVersion=" + preTaskVersion
            + ", postTaskCode=" + postTaskCode
            + ", postTaskVersion=" + postTaskVersion
            + ", conditionType=" + conditionType
            + ", conditionParams='" + conditionParams + '\''
            + ", createTime=" + createTime
            + ", updateTime=" + updateTime
            + '}';
    }
}
