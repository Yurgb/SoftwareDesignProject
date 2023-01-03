package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@TableName(value = "takes")//指定表名
public class Takes {
    private Integer studentid;
    private String courseid;
    private Float grade;
    private Integer attendence;
}