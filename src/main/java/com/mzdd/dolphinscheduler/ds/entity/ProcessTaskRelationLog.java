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

import java.util.Date;

/**
 * process task relation log
 */
public class ProcessTaskRelationLog extends ProcessTaskRelation {

    /**
     * operator user id
     */
    private Integer operator;

    /**
     * operate time
     */
    private Date operateTime;

    public ProcessTaskRelationLog() {
        super();
    }

    public ProcessTaskRelationLog(long postTaskCode) {
        this.setName("");
        this.setPreTaskCode(0L);
        this.setPreTaskVersion(0);
        this.setPostTaskCode(postTaskCode);
        this.setPostTaskVersion(0);
        this.setConditionType("NONE");
        this.setConditionParams(null);
    }

    public ProcessTaskRelationLog(long preTaskCode, long postTaskCode) {
        this.setName("");
        this.setPreTaskCode(preTaskCode);
        this.setPreTaskVersion(0);
        this.setPostTaskCode(postTaskCode);
        this.setPostTaskVersion(0);
        this.setConditionType("NONE");
        this.setConditionParams(null);
    }


    public ProcessTaskRelationLog(ProcessTaskRelation processTaskRelation) {
        super();
        this.setId(processTaskRelation.getId());
        this.setName(processTaskRelation.getName());
        this.setProcessDefinitionCode(processTaskRelation.getProcessDefinitionCode());
        this.setProcessDefinitionVersion(processTaskRelation.getProcessDefinitionVersion());
        this.setProjectCode(processTaskRelation.getProjectCode());
        this.setPreTaskCode(processTaskRelation.getPreTaskCode());
        this.setPreTaskVersion(processTaskRelation.getPreTaskVersion());
        this.setPostTaskCode(processTaskRelation.getPostTaskCode());
        this.setPostTaskVersion(processTaskRelation.getPostTaskVersion());
        this.setConditionType(processTaskRelation.getConditionType());
        this.setConditionParams(processTaskRelation.getConditionParams());
        this.setCreateTime(processTaskRelation.getCreateTime());
        this.setUpdateTime(processTaskRelation.getUpdateTime());
    }

    public Integer getOperator() {
        return operator;
    }

    public void setOperator(Integer operator) {
        this.operator = operator;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
