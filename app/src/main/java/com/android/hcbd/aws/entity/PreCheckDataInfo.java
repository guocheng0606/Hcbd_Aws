package com.android.hcbd.aws.entity;

import java.io.Serializable;

/**
 * Created by guocheng on 2017/6/21.
 */

public class PreCheckDataInfo implements Serializable {


    /**
     * axisNum : 6
     * beginAmt : null
     * beginTime : 2017-07-11 00:00
     * carNo : 鄂FL6924
     * code : 170711001254
     * createTime : 2017-07-11 08:42:56
     * deviceId : A0F6FD4FD52A
     * endAmt : null
     * endTime : 2017-07-11 23:59
     * id : 135890
     * img : /upload/file/2017/7/11/08_42_56.jpg
     * isOver : null
     * isShow : 0
     * lane : 3
     * limitAmt : 49.0
     * minAmt : 0.67
     * overPercent : 1.35%
     * passTime : 20170711084246
     * queryTime : null
     * queryWeight : null
     * speed : 84.67
     * url : null
     * weight : 49.67
     */

    private String axisNum;
    private Object beginAmt;
    private String beginTime;
    private String carNo;
    private String code;
    private String createTime;
    private String deviceId;
    private Object endAmt;
    private String endTime;
    private int id;
    private String img;
    private Object isOver;
    private String isShow;
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

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
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

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
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

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
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
}
