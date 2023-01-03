package com.example.springboot.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springboot.entity.roleMenu;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface RoleMenuMapper extends BaseMapper<roleMenu> {

    @Delete("delete  from sys_role_menu where role_id=#{id}")
    Integer deleteByID1(@Param("id") Integer id);

    @Select("select menu_id from sys_role_menu where role_id=#{id}")
    List<Integer> selectMenus( @Param("id")Integer id);
}
