package cn.vgbhfive.swaggerdemo.controller;

import cn.vgbhfive.swaggerdemo.entity.User;
import cn.vgbhfive.swaggerdemo.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @time: 2018/11/26
 * @author: Vgbh
 */
@RestController
@RequestMapping("/users")
@Api(tags = "0.0.1", description = "用户管理", value = "用户管理")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/query", method = RequestMethod.GET)
    @ApiOperation(value = "条件查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名"),
            @ApiImplicitParam(name = "password", value = "密码")
    })
    public User query(@RequestParam("username") String username,
                        @RequestParam("password") String password) {
        logger.info("多个参数调用 @ApiImplicitiParams");
        return new User(1l, "lyl", "password");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "主键查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户编号")
    })
    public User get(@PathVariable long id) {
        logger.info("单个参数用 @ApiImplicitiParam");
        return new User(id, "lyl", "password");
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除用户")
    @ApiImplicitParam(name = "id", value = "用户编号")
    public void delete(@PathVariable long id) {
        logger.info("单个参数用 @ApiImplicitiParam");
    }
}
