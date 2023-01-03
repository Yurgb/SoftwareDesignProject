package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@TableName(value = "assignment")//指定表名
public class Assignment {
    private Integer id;
    private String projectid;
    private Integer studentid;
    private Float grade;
    private String file;
    private Date time;
}
