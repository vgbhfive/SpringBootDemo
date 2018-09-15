package com.vgbh.mybatisdemo.dao;

import com.vgbh.mybatisdemo.entity.Student;

import java.io.Serializable;
import java.util.List;

/**
 * @time:
 * @author: Vgbh
 */
public interface StudentDao extends Serializable {

    /**
     * Select All
     * @return
     */
    List<Student> selectAll();

}
