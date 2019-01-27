package cn.vgbhfive.errordemo.exception;

/**
 * 自定义异常
 *
 * @time: 2019/1/27
 * @author: Vgbh
 */
public class CustomException extends RuntimeException{

    private static final long serialVersionUID = 4563456682233577977L;

    private Integer code;

    public CustomException() {
    }

    public CustomException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
