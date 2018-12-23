package cn.vgbhfive.actuatordemo.endpoint;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义Endpoint
 * @Endpoint 是构建rest的唯一路径
 * 不同的请求方式，调用时缺少必要的参数，或者使用无法转化的类型，都会报出404
 * @ReadOperation = GET 状态200 异常404
 * @WriteOperation = POST 状态200 异常204
 * @DeleteOperation = DELETE 状态200 异常204
 *
 * 最后还需要将Endpoint声明为一个Bean -->config/MyEndpointConfiguration
 *
 * @time: 2018/12/23
 * @author: Vgbh
 */
@Endpoint(id = "vgbh")
public class MyEndpoint {

    @ReadOperation
    public Map<String, String> hello() {
        Map<String, String> result = new HashMap<>();
        result.put("name", "lyl");
        result.put("age", "100");
        result.put("email", "vgbhfive@foxmail.com");

        return result;
    }

}
