package com.android.hcbd.aws.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 14525 on 2017/6/26.
 */

public class PrePreferencesInfo implements Serializable {

    /**
     * code : 002
     * createTime : 2017-06-06T10:22:43
     * id : 2
     * modelContent : ["分析代码数据","/nsp/typeAction!"]
     * name : 最小报警重量(吨)
     * names : 002-最小报警重量(吨)
     * operNames : S0000-Eingabe
     * orgCode : 027
     * paramsObj : null
     * remark : 75
     * state : 1
     * stateContent : 启用
     * type : A001
     * upload : null
     * uploadContentType : null
     * uploadFileName : null
     */

    private String code;
    private String createTime;
    private int id;
    private String name;
    private String names;
    private String operNames;
    private String orgCode;
    private Object paramsObj;
    private String remark;
    private String state;
    private String stateContent;
    private String type;
    private Object upload;
    private Object uploadContentType;
    private Object uploadFileName;
    private List<String> modelContent;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getOperNames() {
        return operNames;
    }

    public void setOperNames(String operNames) {
        this.operNames = operNames;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public Object getParamsObj() {
        return paramsObj;
    }

    public void setParamsObj(Object paramsObj) {
        this.paramsObj = paramsObj;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateContent() {
        return stateContent;
    }

    public void setStateContent(String stateContent) {
        this.stateContent = stateContent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getUpload() {
        return upload;
    }

    public void setUpload(Object upload) {
        this.upload = upload;
    }

    public Object getUploadContentType() {
        return uploadContentType;
    }

    public void setUploadContentType(Object uploadContentType) {
        this.uploadContentType = uploadContentType;
    }

    public Object getUploadFileName() {
        return uploadFileName;
    }

    public void setUploadFileName(Object uploadFileName) {
        this.uploadFileName = uploadFileName;
    }

    public List<String> getModelContent() {
        return modelContent;
    }

    public void setModelContent(List<String> modelContent) {
        this.modelContent = modelContent;
    }
}
