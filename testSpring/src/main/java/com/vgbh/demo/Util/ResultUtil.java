package com.vgbh.demo.Util;

import com.vgbh.demo.domain.Result;
import org.aspectj.apache.bcel.classfile.Code;

public class ResultUtil {

    public static Result success(Object object) {
        Result result = new Result();
        result.setCode(200);
        result.setMsg("成功");
        result.setData(object);

        return result;
    }

    public static Result success () {
        Result result = new Result();
        result.setCode(200);
        result.setMsg("成功");
        result.setData(null);

        return result;
    }

    public static Result error (Integer code, String msg) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(null);

        return result;
    }
}
