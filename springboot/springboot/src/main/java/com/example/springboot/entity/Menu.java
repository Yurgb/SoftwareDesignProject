package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@TableName(value = "sys_menu")//指定表名
public class Menu {
    private Integer id;
    private String name;
    private String path;
    private String icon;
}