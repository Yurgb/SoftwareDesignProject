package com.example.springboot.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springboot.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface UserMapper extends BaseMapper<User> {

      //    @Select("SELECT * FROM sys_user")
//    List<User> findAll();
//
//    Integer insert(User user);
//
 //    Integer update(User user);
//
//    Integer deleteByID(@Param("id") Integer id);
//    @Select("select * from sys_user where username like #{username} limit #{pageNum}, #{pageSize}")
//    List<User> selectPage(Integer pageNum, Integer pageSize,String username);
//
//    @Select("select count(*) from sys_user where username like #{username} ")
//    int count(String username);
      @Select("select password from sys_user where username like #{username}")
      String selectpassword(String username);

      @Select("select * from sys_user where username like #{username}")
      User selectinfo(String a);

      @Select("select role from sys_user where id=#{id}")
      Integer get_roleid(Integer id);

      @Update("update sys_user set password=#{password} where id=#{id}")
      void update1(Integer id,String password);
}
