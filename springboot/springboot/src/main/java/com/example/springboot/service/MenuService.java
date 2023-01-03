package com.example.springboot.service;


import com.example.springboot.entity.roleMenu;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.entity.Menu;
import com.example.springboot.mapper.MenuMapper;
import com.example.springboot.mapper.RoleMenuMapper;
import com.example.springboot.mapper.UserMapper;

@Service
public class MenuService extends ServiceImpl<MenuMapper, Menu>{

    private MenuMapper menuMapper;
    private RoleMenuMapper roleMenuMapper;
    private UserMapper userMapper;

    public void setrole(Integer id, int[] roleList) {
        id=userMapper.get_roleid(id);
        roleMenuMapper.deleteById(id);

        for (Integer menuId:roleList)
        {
           roleMenu roles=new roleMenu();
           roles.setRole_id(id);
           roles.setMenu_id(menuId);
           roleMenuMapper.insert(roles);
        }
    }
}
