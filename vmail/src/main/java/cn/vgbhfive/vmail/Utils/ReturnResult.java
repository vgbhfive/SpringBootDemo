package cn.vgbhfive.vmail.Utils;

/**
 * @time:
 * @author: Vgbh
 */
public class ReturnResult {

    private String rspCode;
    private String rspMsg;

    public ReturnResult() {
        this.rspCode = "200";
        this.rspMsg = "发送成功！";
    }

    public ReturnResult(String rspCode, String rspMsg) {
        this.rspCode = rspCode;
        this.rspMsg = rspMsg;
    }

    public String getRspCode() {
        return rspCode;
    }

    public void setRspCode(String rspCode) {
        this.rspCode = rspCode;
    }

    public String getRspMsg() {
        return rspMsg;
    }

    public void setRspMsg(String rspMsg) {
        this.rspMsg = rspMsg;
    }

    @Override
    public String toString() {
        return "ReturnResult{" +
                "rspCode='" + rspCode + '\'' +
                ", rspMsg='" + rspMsg + '\'' +
                '}';
    }
}
