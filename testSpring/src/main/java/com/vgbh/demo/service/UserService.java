package com.vgbh.demo.service;

import com.vgbh.demo.domain.User;
import com.vgbh.demo.enums.ResultEnum;
import com.vgbh.demo.exception.UserException;
import com.vgbh.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional//事务
    public void insertTwo () {

    }

    public void getAge (Integer id) throws Exception{
        User user = userRepository.getOne(id);
        Integer age = user.getAge();
        if (age < 18) {
            throw new UserException(ResultEnum.MINAGE);
        } else if (age > 18 && age < 30) {
            throw new UserException(ResultEnum.MAXAGE);
        }

    }

    /**
     * 通过id查看用户的信息
     * @param id
     * @return
     */
    public User findOne (Integer id) {
        return userRepository.findOne(id);
    }

}
