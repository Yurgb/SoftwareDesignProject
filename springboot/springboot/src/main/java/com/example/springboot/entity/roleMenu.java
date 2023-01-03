package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "sys_role_menu")//指定表名
public class roleMenu {
    private Integer role_id;
    private Integer menu_id;
}