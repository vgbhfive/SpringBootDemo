package com.vgbh.demo.enums;

public enum ResultEnum {

    SUCCESS(200, "成功"),
    ERROR(-1, "未知错误"),
    MINAGE(400, "AGE < 18"),
    MAXAGE(400, "AGE > 18 && AGE < 30")

    ;

    private Integer code ;

    private String msg ;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
