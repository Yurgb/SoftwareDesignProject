package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@TableName(value = "sys_user")//指定表名
public class User {
    private Integer id;
    private String username;
//    @JsonIgnore//忽略密码，不展示给前端
    private String password;
    private String nickname;
    private String email;
    private String phone;
    private String address;
    private String create_time;
    private String avatar_url;
    private String role;
    private String url;


}
