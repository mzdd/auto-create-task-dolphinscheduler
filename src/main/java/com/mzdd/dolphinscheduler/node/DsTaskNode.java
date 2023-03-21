package com.mzdd.dolphinscheduler.node;

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

