package cn.vgbhfive.bookdemos.ch2.el;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @time:
 * @author: Vgbh
 */
@Service
public class DemoService {

    @Value("其他的属性")
    private String another;

    public String getAnother() {
        return another;
    }

    public void setAnother(String another) {
        this.another = another;
    }
}
