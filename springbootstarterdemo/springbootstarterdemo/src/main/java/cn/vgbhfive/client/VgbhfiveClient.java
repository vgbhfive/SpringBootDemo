package cn.vgbhfive.client;

import cn.vgbhfive.properties.DemoProperties;

/**
 * 服务类
 *
 * @time: 2019/08/13
 * @author: Vgbh
 */
public class VgbhfiveClient {

    private DemoProperties demoProperties;

    public VgbhfiveClient () {
    }

    public VgbhfiveClient(DemoProperties p) {
        this.demoProperties = p;
    }

    public String getName() {
        return demoProperties.getName();
    }

}
