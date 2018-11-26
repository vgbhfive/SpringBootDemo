package cn.vgbhfive.swaggerdemo.service;

import cn.vgbhfive.swaggerdemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @time: 2018/11/26
 * @author: Vgbh
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

}
