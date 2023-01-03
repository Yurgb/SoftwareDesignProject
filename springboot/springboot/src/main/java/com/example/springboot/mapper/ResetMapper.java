package com.example.springboot.mapper;
import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springboot.entity.Reset;
import org.apache.ibatis.annotations.*;

import java.sql.Time;
import java.util.Date;

@Mapper
public interface ResetMapper extends BaseMapper<Reset>{
    @Select("select code,email from reset where email=#{email}")
    String cha(String email);

    @Insert("INSERT INTO reset(email,code,time) VALUES (#{email},#{code},#{offsetMinute})")
    void save1(String email, String code, DateTime offsetMinute);

    @Update("update reset set code=#{code},time=#{offsetMinute} where email=#{email}}")
    void update1(String email,String code, Date offsetMinute);

    @Delete("DELETE FROM `reset` WHERE email=#{email}")
    void shan(String email);
}
