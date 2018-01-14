package com.vgbh.demo.aspect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
@Component
public class HttpAspect {

    @Autowired
    private static final Logger log = LoggerFactory.getLogger(HttpAspect.class);

    @Pointcut("execution(public * com.vgbh.demo.controller.UserController.*(..))")
    public void log () {
    }

    @Before("log()")
    public void doBefore () {
        log.info("before..............");
    }

    @After("log()")
    public void doAfter () {
        log.info("after..............");
    }
}
