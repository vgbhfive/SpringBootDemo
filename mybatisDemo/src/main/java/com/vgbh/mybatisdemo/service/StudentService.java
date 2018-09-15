package com.vgbh.mybatisdemo.service;

import com.vgbh.mybatisdemo.entity.Student;

import java.io.Serializable;
import java.util.List;

/**
 * @time:
 * @author: Vgbh
 */
public interface StudentService extends Serializable {

    List<Student> selectAll();

}
