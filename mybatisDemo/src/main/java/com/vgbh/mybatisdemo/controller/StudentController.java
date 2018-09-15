package com.vgbh.mybatisdemo.controller;

import com.vgbh.mybatisdemo.entity.Student;
import com.vgbh.mybatisdemo.service.StudentService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @time:
 * @author: Vgbh
 */
@RestController
public class StudentController {

    @Resource
    private StudentService studentService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<Student> selectAll() {
        return studentService.selectAll();
    }


}
