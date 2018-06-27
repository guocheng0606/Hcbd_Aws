package com.android.hcbd.aws.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by guocheng on 2017/7/11.
 */

public class LedShowInfo implements Serializable {

    /**
     * beginTime : null
     * code : 2
     * createTime : 2017-07-10T18:11:23
     * endTime : null
     * fullTime : 2017-07-10 18:11:23
     * id : 2586
     * modelContent : ["Led显示","/aws/ledAction!"]
     * name : 大屏
     * names : 2-大屏
     * operNames : null
     * orgCode : 027
     * paramsObj : null
     * remark : 请苏EJ6789进站检测
     * state : 1
     * stateContent : 启用
     */

    private Object beginTime;
    private String code;
    private String createTime;
    private Object endTime;
    private String fullTime;
    private int id;
    private String name;
    private String names;
    private Object operNames;
    private String orgCode;
    private Object paramsObj;
    private String remark;
    private String state;
    private String stateContent;
    private List<String> modelContent;

    public Object getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Object beginTime) {
        this.beginTime = beginTime;
    }

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

    public Object getEndTime() {
        return endTime;
    }

    public void setEndTime(Object endTime) {
        this.endTime = endTime;
    }

    public String getFullTime() {
        return fullTime;
    }

    public void setFullTime(String fullTime) {
        this.fullTime = fullTime;
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

    public Object getOperNames() {
        return operNames;
    }

    public void setOperNames(Object operNames) {
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

    public List<String> getModelContent() {
        return modelContent;
    }

    public void setModelContent(List<String> modelContent) {
        this.modelContent = modelContent;
    }
}
