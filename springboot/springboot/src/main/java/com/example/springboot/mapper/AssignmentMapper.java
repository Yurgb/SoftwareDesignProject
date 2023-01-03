package com.example.springboot.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springboot.entity.Assignment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface AssignmentMapper extends BaseMapper<Assignment> {
    @Update("update assignment set grade=#{grade} where id=#{id}")
    void setgrade(Float grade, String id);


    @Select("select grade FROM assignment WHERE projectid=#{id} AND studentid=#{studentID}")
    Double selectgrade(Integer studentID, String id);
}
