package com.example.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springboot.entity.Takes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface TakesMapper extends BaseMapper<Takes> {
    @Select("select courseid from takes where studentid=#{studentID}")
    List<Integer> selectCourseID(String studentID);

    @Select("select attendence from takes where studentid=#{studentID} AND courseid=#{courseID}")
    Integer getatten(Integer studentID, String courseID);
    @Update("UPDATE takes SET attendence=attendence+1 WHERE studentid=#{studentID} AND courseid=#{courseID}")
    void SetAttendence(Integer studentID, String courseID);
}
