package cn.vgbhfive.bookdemos.ch2.scope;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * @time:
 * @author: Vgbh
 */
@Service
@Scope("prototype")
public class DemoPrototypeService {

}
