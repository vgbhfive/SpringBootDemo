package com.vgbh.store;

/**
 * @time：2018-04-09
 * @author: Vgbh
 * <p>
 * 成功返回的状态码
 */

public enum PutStatus {

    PUT("success", 1);

    private String desc;
    private int status;

    PutStatus() {
    }

    PutStatus(String desc, int status) {
        this.desc = desc;
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
