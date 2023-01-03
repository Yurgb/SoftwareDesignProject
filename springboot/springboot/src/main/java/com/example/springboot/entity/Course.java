package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@TableName(value = "course")//指定表名
public class Course {
    private String id;
    private String name;
    private Integer score;
    private Boolean state;
    private Integer teacherid;
    private Integer projectper;
    private Integer attendenceper;
}