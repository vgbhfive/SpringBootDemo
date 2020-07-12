package cn.vgbhfive.mpdemo.test;

import cn.vgbhfive.mpdemo.entity.User;
import cn.vgbhfive.mpdemo.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author Vgbh
 * @date 2020/7/12 15:24
 */
@SpringBootTest
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.between("age", 20, 25);
        userQueryWrapper.orderByDesc("id");
        List<User> userList = userMapper.selectList(userQueryWrapper);
        userList.forEach(System.out::println);

        System.out.println(("----- delete ------"));
        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.eq("id", 5);
        userMapper.delete(userUpdateWrapper);

        System.out.println(("----- after delete ------"));
        List<User> userList1 = userMapper.selectList(userQueryWrapper);
        userList1.forEach(System.out::println);
    }

}
