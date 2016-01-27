package com.jiayusoft.shengli.bingan.selfupload;

import java.util.LinkedList;

/**
 * Created by Administrator on 15-3-17.
 */
public class SelfUpload {
    String serialNum;
    String orgCode;
    String idCard;
    String describe;
    String uploadTime;
    int uploadType; // 1病情自述，2健康查体
    LinkedList<String> fileNames;

    public SelfUpload() {
        fileNames = new LinkedList<String>();
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public int getUploadType() {
        return uploadType;
    }

    public void setUploadType(int uploadType) {
        this.uploadType = uploadType;
    }

    public LinkedList<String> getFileNames() {
        return fileNames;
    }

    public void setFileNames(LinkedList<String> fileNames) {
        this.fileNames = fileNames;
    }
}
