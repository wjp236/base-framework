package com.platform.base.framework.trunk.model.po;

import com.platform.base.framework.trunk.model.utils.BaseModel;

import java.util.Date;

public abstract class BasePo extends BaseModel {

    private static final long serialVersionUID = 6484851633809295190L;

    private String versionNumber;
    private Date createTime;
    private Date updateTime;
    private String standbyRemark;
    private String delFlag;

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getStandbyRemark() {
        return standbyRemark;
    }

    public void setStandbyRemark(String standbyRemark) {
        this.standbyRemark = standbyRemark;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

}
