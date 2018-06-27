package com.android.hcbd.aws.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 14525 on 2017/6/30.
 */

public class PreHistoryInfo implements Serializable {

    /**
     * axisNum : 6
     * beginAmt : null
     * beginTime : null
     * carNo : 鄂J3V507
     * code : 170621000003
     * createTime : 2017-06-21 09:46:04
     * deviceId : 02004E720007
     * endAmt : null
     * endTime : null
     * id : 19390
     * img : /upload/file/2017/6/21/09_46_04.jpg
     * isOver : null
     * isShow : null
     * lane : 2
     * limitAmt : 46.0
     * minAmt : 0
     * modelContent : ["预检历史","/aws/preCheckDataAction!"]
     * overPercent : 0.00%
     * passTime : 20150314182322
     * queryTime : null
     * queryWeight : null
     * speed : 54.32
     * url : null
     * weight : 38.13
     */

    private String axisNum;
    private Object beginAmt;
    private Object beginTime;
    private String carNo;
    private String code;
    private String createTime;
    private String deviceId;
    private Object endAmt;
    private Object endTime;
    private int id;
    private String img;
    private Object isOver;
    private Object isShow;
    private String lane;
    private double limitAmt;
    private double minAmt;
    private String overPercent;
    private String passTime;
    private Object queryTime;
    private Object queryWeight;
    private double speed;
    private Object url;
    private double weight;
    private List<String> modelContent;

    public String getAxisNum() {
        return axisNum;
    }

    public void setAxisNum(String axisNum) {
        this.axisNum = axisNum;
    }

    public Object getBeginAmt() {
        return beginAmt;
    }

    public void setBeginAmt(Object beginAmt) {
        this.beginAmt = beginAmt;
    }

    public Object getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Object beginTime) {
        this.beginTime = beginTime;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
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

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Object getEndAmt() {
        return endAmt;
    }

    public void setEndAmt(Object endAmt) {
        this.endAmt = endAmt;
    }

    public Object getEndTime() {
        return endTime;
    }

    public void setEndTime(Object endTime) {
        this.endTime = endTime;
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

    public Object getIsOver() {
        return isOver;
    }

    public void setIsOver(Object isOver) {
        this.isOver = isOver;
    }

    public Object getIsShow() {
        return isShow;
    }

    public void setIsShow(Object isShow) {
        this.isShow = isShow;
    }

    public String getLane() {
        return lane;
    }

    public void setLane(String lane) {
        this.lane = lane;
    }

    public double getLimitAmt() {
        return limitAmt;
    }

    public void setLimitAmt(double limitAmt) {
        this.limitAmt = limitAmt;
    }

    public double getMinAmt() {
        return minAmt;
    }

    public void setMinAmt(double minAmt) {
        this.minAmt = minAmt;
    }

    public String getOverPercent() {
        return overPercent;
    }

    public void setOverPercent(String overPercent) {
        this.overPercent = overPercent;
    }

    public String getPassTime() {
        return passTime;
    }

    public void setPassTime(String passTime) {
        this.passTime = passTime;
    }

    public Object getQueryTime() {
        return queryTime;
    }

    public void setQueryTime(Object queryTime) {
        this.queryTime = queryTime;
    }

    public Object getQueryWeight() {
        return queryWeight;
    }

    public void setQueryWeight(Object queryWeight) {
        this.queryWeight = queryWeight;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public Object getUrl() {
        return url;
    }

    public void setUrl(Object url) {
        this.url = url;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public List<String> getModelContent() {
        return modelContent;
    }

    public void setModelContent(List<String> modelContent) {
        this.modelContent = modelContent;
    }
}
