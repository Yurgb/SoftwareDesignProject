package com.example.springboot.entity;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

@Data
@ToString
@TableName(value = "reset")//指定表名
public class Reset {

    private String email;
    private DateTime time;
    private String code;
}
