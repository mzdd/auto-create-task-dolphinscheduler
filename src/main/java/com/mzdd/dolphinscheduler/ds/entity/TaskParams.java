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

import com.alibaba.fastjson.JSONArray;
import com.mzdd.dolphinscheduler.RunParamConstant;

import java.io.Serializable;

/**
 * TaskParams
 */
public class TaskParams implements Serializable {

    private static final long serialVersionUID = 1L;

    private JSONArray localParams;
    private JSONArray resourceList;
    private String type;
    private Integer datasource;
    private String sql;
    private String sqlType;
    private JSONArray preStatements;
    private JSONArray postStatements;
    private String segmentSeparator;
    private Integer displayRows;

    public TaskParams(String sql) {
        this.setLocalParams(new JSONArray());
        this.setResourceList(new JSONArray());
        this.setType("MYSQL");
        this.setDatasource(RunParamConstant.DS_SOURCE_ID);
        this.setSql(sql);
        this.setSqlType("1");
        this.setPreStatements(new JSONArray());
        this.setPostStatements(new JSONArray());
        this.setSegmentSeparator(";");
        this.setDisplayRows(10);
    }

    public JSONArray getLocalParams() {
        return localParams;
    }

    public void setLocalParams(JSONArray localParams) {
        this.localParams = localParams;
    }

    public JSONArray getResourceList() {
        return resourceList;
    }

    public void setResourceList(JSONArray resourceList) {
        this.resourceList = resourceList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getDatasource() {
        return datasource;
    }

    public void setDatasource(Integer datasource) {
        this.datasource = datasource;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getSqlType() {
        return sqlType;
    }

    public void setSqlType(String sqlType) {
        this.sqlType = sqlType;
    }

    public JSONArray getPreStatements() {
        return preStatements;
    }

    public void setPreStatements(JSONArray preStatements) {
        this.preStatements = preStatements;
    }

    public JSONArray getPostStatements() {
        return postStatements;
    }

    public void setPostStatements(JSONArray postStatements) {
        this.postStatements = postStatements;
    }

    public String getSegmentSeparator() {
        return segmentSeparator;
    }

    public void setSegmentSeparator(String segmentSeparator) {
        this.segmentSeparator = segmentSeparator;
    }

    public Integer getDisplayRows() {
        return displayRows;
    }

    public void setDisplayRows(Integer displayRows) {
        this.displayRows = displayRows;
    }
}
