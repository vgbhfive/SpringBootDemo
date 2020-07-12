package cn.vgbhfive.mpdemo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

/**
 * @author Vgbh
 * @date 2020/7/12 15:23
 */
public class User {

    @TableId
    private Long id;

    private String name;

    private Integer age;

    private String email;

    @TableField(value = "user_pass")
    private String userPass;

    public User() {
    }

    public User(Long id, String name, Integer age, String email, String userPass) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.email = email;
        this.userPass = userPass;
    }

    public Long getId() {
        return id;
    }

    public User setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getAge() {
        return age;
    }

    public User setAge(Integer age) {
        this.age = age;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getUserPass() {
        return userPass;
    }

    public User setUserPass(String userPass) {
        this.userPass = userPass;
        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", userPass='" + userPass + '\'' +
                '}';
    }
}
