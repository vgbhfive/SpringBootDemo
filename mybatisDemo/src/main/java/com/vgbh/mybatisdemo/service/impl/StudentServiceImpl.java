package com.vgbh.mybatisdemo.service.impl;

import com.vgbh.mybatisdemo.dao.StudentDao;
import com.vgbh.mybatisdemo.entity.Student;
import com.vgbh.mybatisdemo.service.StudentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @time:
 * @author: Vgbh
 */
@Service("studentService")
public class StudentServiceImpl implements StudentService {

    @Resource
    private StudentDao studentDao;

    @Override
    public List<Student> selectAll() {
        return studentDao.selectAll();
    }
}
