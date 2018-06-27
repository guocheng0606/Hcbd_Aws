package com.android.hcbd.aws.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 14525 on 2017/6/21.
 */

public class CheckDataInfo implements Serializable {


    /**
     * amt : 43.05
     * beginAmt : null
     * beginTime : 2017-07-11 00:00
     * carNo : 鄂DB5988
     * carType : {"axisNum":"6","code":"001","createTime":"2017-07-10T18:11:24","height":4,"id":1,"img":"","isDef":"1","isDefName":"是","isShow":"0","isShowName":"否","length":0,"limit":49,"modelContent":["车型信息","/aws/carTypeAction!"],"name":"6轴49吨车型","names":"001-6轴49吨车型","operNames":"S0002-admin","orgCode":"027","paramsObj":null,"remark":null,"state":"1","stateContent":"启用","upload":null,"uploadContentType":null,"uploadFileName":null,"width":2.55}
     * checkAmt : null
     * checkOper : null
     * checkTime : null
     * checkUnLoad : null
     * code : 170711000002
     * createTime : 2017-07-11 02:50:17
     * driver : 1
     * endAmt : null
     * endTime : 2017-07-11 23:59
     * goods : 百货
     * id : 488
     * img : null
     * isCheck : null
     * lane : 1
     * modelContent : ["精检历史","/aws/checkDataAction!"]
     * operNames : S0012-1号道
     * orgCode : 027
     * overAmt : 0.0
     * preCheckCode : null
     * preCheckData : null
     */

    private double amt;
    private Object beginAmt;
    private String beginTime;
    private String carNo;
    private CarTypeBean carType;
    private Object checkAmt;
    private Object checkOper;
    private Object checkTime;
    private Object checkUnLoad;
    private String code;
    private String createTime;
    private String driver;
    private Object endAmt;
    private String endTime;
    private String goods;
    private int id;
    private Object img;
    private Object isCheck;
    private String lane;
    private String operNames;
    private String orgCode;
    private double overAmt;
    private Object preCheckCode;
    private Object preCheckData;
    private List<String> modelContent;

    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public double getAmt() {
        return amt;
    }

    public void setAmt(double amt) {
        this.amt = amt;
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

    public CarTypeBean getCarType() {
        return carType;
    }

    public void setCarType(CarTypeBean carType) {
        this.carType = carType;
    }

    public Object getCheckAmt() {
        return checkAmt;
    }

    public void setCheckAmt(Object checkAmt) {
        this.checkAmt = checkAmt;
    }

    public Object getCheckOper() {
        return checkOper;
    }

    public void setCheckOper(Object checkOper) {
        this.checkOper = checkOper;
    }

    public Object getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Object checkTime) {
        this.checkTime = checkTime;
    }

    public Object getCheckUnLoad() {
        return checkUnLoad;
    }

    public void setCheckUnLoad(Object checkUnLoad) {
        this.checkUnLoad = checkUnLoad;
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

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
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

    public String getGoods() {
        return goods;
    }

    public void setGoods(String goods) {
        this.goods = goods;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Object getImg() {
        return img;
    }

    public void setImg(Object img) {
        this.img = img;
    }

    public Object getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(Object isCheck) {
        this.isCheck = isCheck;
    }

    public String getLane() {
        return lane;
    }

    public void setLane(String lane) {
        this.lane = lane;
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

    public double getOverAmt() {
        return overAmt;
    }

    public void setOverAmt(double overAmt) {
        this.overAmt = overAmt;
    }

    public Object getPreCheckCode() {
        return preCheckCode;
    }

    public void setPreCheckCode(Object preCheckCode) {
        this.preCheckCode = preCheckCode;
    }

    public Object getPreCheckData() {
        return preCheckData;
    }

    public void setPreCheckData(Object preCheckData) {
        this.preCheckData = preCheckData;
    }

    public List<String> getModelContent() {
        return modelContent;
    }

    public void setModelContent(List<String> modelContent) {
        this.modelContent = modelContent;
    }

    public static class CarTypeBean implements Serializable {
        /**
         * axisNum : 6
         * code : 001
         * createTime : 2017-07-10T18:11:24
         * height : 4.0
         * id : 1
         * img :
         * isDef : 1
         * isDefName : 是
         * isShow : 0
         * isShowName : 否
         * length : 0.0
         * limit : 49.0
         * modelContent : ["车型信息","/aws/carTypeAction!"]
         * name : 6轴49吨车型
         * names : 001-6轴49吨车型
         * operNames : S0002-admin
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
        //private double limit;
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

        /*public double getLimit() {
            return limit;
        }

        public void setLimit(double limit) {
            this.limit = limit;
        }*/

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
}
