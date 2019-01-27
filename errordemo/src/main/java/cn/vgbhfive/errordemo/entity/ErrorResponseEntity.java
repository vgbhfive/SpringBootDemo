package cn.vgbhfive.errordemo.entity;

/**
 * @time: 2019/1/27
 * @author: Vgbh
 */
public class ErrorResponseEntity {

    private Integer code;

    private String message;

    public ErrorResponseEntity() {
    }

    public ErrorResponseEntity(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
