package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@TableName(value = "project")//指定表名
public class Project {
    private String id;
    private Integer score;
    private String content;
    private String name;
    private String courseid;
    private Date time;
}
