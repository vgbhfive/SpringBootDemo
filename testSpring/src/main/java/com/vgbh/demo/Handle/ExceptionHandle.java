package com.vgbh.demo.Handle;

import com.vgbh.demo.Util.ResultUtil;
import com.vgbh.demo.domain.Result;
import com.vgbh.demo.exception.UserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionHandle {

    @Autowired
    private static final Logger log = LoggerFactory.getLogger(ExceptionHandle.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result handle (Exception e) {
        if (e instanceof UserException) {
            UserException userException = (UserException) e;
            return ResultUtil.error(userException.getCode(), userException.getMessage());
        } else {
            log.info("{系统异常} e" + e.getMessage());
            return ResultUtil.error(-1, "未知错误！");
        }

    }

}
