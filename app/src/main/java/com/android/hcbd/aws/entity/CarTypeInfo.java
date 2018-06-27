package com.android.hcbd.aws.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by guocheng on 2017/7/25.
 */

public class CarTypeInfo implements Serializable{

    /**
     * axisNum : 6
     * checkLimit : 47.0
     * code : 001
     * createTime : 2017-07-19T09:01:58
     * height : 4.0
     * id : 1
     * img :
     * isDef : 1
     * isDefName : 是
     * isShow : 0
     * isShowName : 否
     * length : 0.0
     * limit : 47.0
     * modelContent : ["车型信息","/aws/carTypeAction!"]
     * name : 6轴49吨车型
     * names : 001-6轴49吨车型
     * operNames : S0000-Eingabe
     * orgCode : 027
     * paramsObj : null
     * remark : null
     * state : 1
     * stateContent : 启用
     * upload : null
     * uploadContentType : null
     * uploadFileName : null
     * width : 2.55
     */

    private String axisNum;
    private double checkLimit;
    private String code;
    private String createTime;
    private double height;
    private int id;
    private String img;
    private String isDef;
    private String isDefName;
    private String isShow;
    private String isShowName;
    private double length;
    private double limit;
    private String name;
    private String names;
    private String operNames;
    private String orgCode;
    private Object paramsObj;
    private Object remark;
    private String state;
    private String stateContent;
    private Object upload;
    private Object uploadContentType;
    private Object uploadFileName;
    private double width;
    private List<String> modelContent;

    public String getAxisNum() {
        return axisNum;
    }

    public void setAxisNum(String axisNum) {
        this.axisNum = axisNum;
    }

    public double getCheckLimit() {
        return checkLimit;
    }

    public void setCheckLimit(double checkLimit) {
        this.checkLimit = checkLimit;
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

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getIsDef() {
        return isDef;
    }

    public void setIsDef(String isDef) {
        this.isDef = isDef;
    }

    public String getIsDefName() {
        return isDefName;
    }

    public void setIsDefName(String isDefName) {
        this.isDefName = isDefName;
    }

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }

    public String getIsShowName() {
        return isShowName;
    }

    public void setIsShowName(String isShowName) {
        this.isShowName = isShowName;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getLimit() {
        return limit;
    }

    public void setLimit(double limit) {
        this.limit = limit;
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

    public Object getRemark() {
        return remark;
    }

    public void setRemark(Object remark) {
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

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public List<String> getModelContent() {
        return modelContent;
    }

    public void setModelContent(List<String> modelContent) {
        this.modelContent = modelContent;
    }
}
