package com.example.springboot.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springboot.entity.Menu;
import org.apache.ibatis.annotations.*;


@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    @Select("select id from sys_menu where name=#{menuName}}")
    Integer selectID(@Param("menuName") String menuName);
}
