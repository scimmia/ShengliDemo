package com.jiayusoft.shengli.bingan.user;

/**
 * Created by Administrator on 14-12-26.
 */
public class UpdateInfo {
    int versionCode;
    String versionName;
    String softName;
    String softUrl;
    String updateLog;

    public UpdateInfo() {
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getSoftName() {
        return softName;
    }

    public void setSoftName(String softName) {
        this.softName = softName;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getSoftUrl() {
        return softUrl;
    }

    public void setSoftUrl(String softUrl) {
        this.softUrl = softUrl;
    }

    public String getUpdateLog() {
        return updateLog;
    }

    public void setUpdateLog(String updateLog) {
        this.updateLog = updateLog;
    }
}
